package es.ucm.pad.teamjvr.mylittlebusiness.Model.DBAdapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import es.ucm.pad.teamjvr.mylittlebusiness.Model.Product;

public class ProductsDBAdapter {
	private class ProductsDBHelper extends SQLiteOpenHelper {
		private static final String SQL_CREATE_TABLE = "CREATE TABLE " + DATABASE_TABLE +
				" ("+KEY_PROD_NAME+" TEXT PRIMARY KEY NOT NULL UNIQUE, "+
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
		public void onUpgrade(SQLiteDatabase _db, int _oldVersion,
				int _newVersion) {
			Log.w("ProductsDBAdapter", "Upgrading from version " + _oldVersion
					+ " to " + _newVersion
					+ ", it will destroy all old data stored.");

			_db.execSQL("DROP TABLE IF EXIST " + DATABASE_TABLE);
			onCreate(_db);
		}
	}
	
	private static final String DATABASE_NAME = "myLittleBusines.db";
	private static final String DATABASE_TABLE = "Products";
	
	private static final int 	DATABASE_VERSION = 1;
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
		return (db.insert(DATABASE_TABLE, null, prod.toContentValues()) >= 0);
	}

	public void close() {
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

		return new Product(cursor);
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
	 * @return a List with all products in the database
	 */
	public List<Product> toList() {
		ArrayList<Product> products = new ArrayList<Product>();

		Cursor cursor = db.query(DATABASE_TABLE, KEYS_PROD, null, null, null,
				null, null);

		if (cursor.moveToFirst())
			do {
				products.add(new Product(cursor));
			} while (cursor.moveToNext());

		return products;
	}

	public boolean updateProduct(Product prod) {
		return db.update(DATABASE_TABLE, prod.toContentValues(), KEY_PROD_NAME
				+ " = '" + prod.getName() + "'", null) > 0;
	}
}