package es.ucm.pad.teamjvr.mylittlebusiness.model.db_adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;
import es.ucm.pad.teamjvr.mylittlebusiness.model.exceptions.ProductAttrException;

public class ProductsDBAdapter {
	private class ProductsDBHelper extends SQLiteOpenHelper {
		private static final String SQL_CREATE_TABLE = "CREATE VIRTUAL TABLE " + DATABASE_TABLE +
				" USING fts3"+
				" ("+KEY_PROD_NAME+" TEXT NOT NULL, "+
					 KEY_PROD_STOCK+ " INTEGER NOT NULL, "+
					 KEY_PROD_COST+ " REAL NOT NULL, "+ 
					 KEY_PROD_PRICE+ " REAL NOT NULL, "+
					 KEY_PROD_BOUGHT+ " INTEGER NOT NULL, "+ 
					 KEY_PROD_PHOTO+ " BLOB);";
		
		public ProductsDBHelper(Context context, String name,
				CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase _db) {
			_db.execSQL(SQL_CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion) {
			Log.w("ProductsDBAdapter", "Upgrading from version " + _oldVersion
					+ " to " + _newVersion
					+ ", it will destroy all old data stored.");

			_db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(_db);
		}
	}
	
	private static final String DATABASE_NAME = "myLittleBusines.db";
	private static final String DATABASE_TABLE = "Products";
	
	private static final int 	DATABASE_VERSION = 4;
	public static final String KEY_PROD_NAME = "prod_name";
	public static final String KEY_PROD_STOCK = "prod_stock";
	public static final String KEY_PROD_COST = "prod_cost";
	public static final String KEY_PROD_PRICE = "prod_price";
	public static final String KEY_PROD_BOUGHT = "prod_bought";
	
	public static final String KEY_PROD_PHOTO = "prod_photo";
	
	private static final String[] KEYS_PROD = { KEY_PROD_NAME, KEY_PROD_STOCK,
			KEY_PROD_COST, KEY_PROD_PRICE, KEY_PROD_BOUGHT, KEY_PROD_PHOTO };
	public static final int PROD_NAME_COL = 0;
	public static final int PROD_STOCK_COL = 1;
	public static final int PROD_COST_COL = 2;
	public static final int PROD_PRICE_COL = 3;
	public static final int PROD_BOUGHT_COL = 4;
	
	public static final int PROD_PHOTO_COL = 5;
	private SQLiteDatabase db;
	
	private ProductsDBHelper dbHelper;

	public ProductsDBAdapter(Context context) {
		this.dbHelper = new ProductsDBHelper(context, DATABASE_NAME, null,
				DATABASE_VERSION);
	}

	public boolean addProduct(Product prod) {
		try{
			this.getProduct(prod.getName());
		}catch (SQLException e){
			return (db.insert(DATABASE_TABLE, null, contentValuesFrom(prod)) >= 0);
		}
		
		return false;
		
	}

	public void close() {
		Log.i(ProductsDBAdapter.class.getName(), "Database is closed");
		db.close();
	}

	public boolean deleteProduct(Product prod) {
		return db.delete(DATABASE_TABLE,
				KEY_PROD_NAME + " = '" + prod.getName() + "'", null) > 0;
	}

	public Product getProduct(String name) throws SQLException {
		Cursor cursor = db.query(DATABASE_TABLE, KEYS_PROD, KEY_PROD_NAME
				+ " = '" + name + "'", null, null, null, null);

		if (cursor.getCount() == 0 || !cursor.moveToFirst())
			throw new SQLException("No Product found for condition: "
					+ KEY_PROD_NAME + " = '" + name + "'");

		return productFrom(cursor);
	}

	public void open() throws SQLException {
		try {
			db = dbHelper.getWritableDatabase();
			Log.i(ProductsDBAdapter.class.getName(),
					"Database is open in rw-mode mode :)");
		} catch (SQLException ex) {
			db = dbHelper.getReadableDatabase();
			Log.i(ProductsDBAdapter.class.getName(),
					"Database is open in read-only mode!!!");
		}
	}

	/**
	 * 
	 * @param filter Filtro de nombre a aplicar en la consulta
	 * @return Lista con todos los productos de la BD que encajan con el filtro
	 */
	public List<Product> getProductsList(String filter) {
		//TODO: Implementar filtro
		ArrayList<Product> products = new ArrayList<Product>();
		Cursor cursor;
		if(filter != null){
			String[] filters = filter.split("\\s+");
			filter = "";
			for (String f : filters) {
				filter += " *"+f+"*";
			}

			Log.i(ProductsDBAdapter.class.getName(), "Filter on query: "+filter);
			filters = new String[] {filter};
			
			cursor = db.query(DATABASE_TABLE, KEYS_PROD, KEY_PROD_NAME+" MATCH ?", filters, null, null, null);
		}else{
			cursor = db.query(DATABASE_TABLE, KEYS_PROD, null, null, null, null, null);
		}

		if (cursor.moveToFirst())
			do {
				products.add(productFrom(cursor));
			} while (cursor.moveToNext());

		return products;
	}

	public boolean updateProduct(Product prod) {
		return db.update(DATABASE_TABLE, contentValuesFrom(prod), KEY_PROD_NAME
				+ " = '" + prod.getName() + "'", null) > 0;
	}
	
	private ContentValues contentValuesFrom(Product p) {
		ContentValues content = new ContentValues();

		content.put(ProductsDBAdapter.KEY_PROD_NAME, p.getName());
		content.put(ProductsDBAdapter.KEY_PROD_STOCK, p.getStock());
		content.put(ProductsDBAdapter.KEY_PROD_COST, p.getCost());
		content.put(ProductsDBAdapter.KEY_PROD_PRICE, p.getPrice());
		content.put(ProductsDBAdapter.KEY_PROD_BOUGHT, p.getBoughtUnits());
		content.put(ProductsDBAdapter.KEY_PROD_PHOTO, p.getPhotoAsByteArray());

		return content;
	}
	
	private Product productFrom(Cursor cursor) {
		String name = cursor.getString(ProductsDBAdapter.PROD_NAME_COL);
		int stock = cursor.getInt(ProductsDBAdapter.PROD_STOCK_COL);
		double cost = cursor.getDouble(ProductsDBAdapter.PROD_COST_COL);
		double price = cursor.getDouble(ProductsDBAdapter.PROD_PRICE_COL);
		int boughtUnits = cursor.getInt(ProductsDBAdapter.PROD_BOUGHT_COL);

		byte[] imageBlob = cursor.getBlob(ProductsDBAdapter.PROD_PHOTO_COL);
		Bitmap photo = BitmapFactory.decodeByteArray(imageBlob, 0,
				imageBlob.length);
		
		Product ret = null;
		try {
			ret = new Product(name, stock, cost, price, photo, boughtUnits);
		} catch (ProductAttrException e) {}
		
		return ret;
	}
}