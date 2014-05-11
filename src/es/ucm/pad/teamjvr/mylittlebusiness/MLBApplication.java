package es.ucm.pad.teamjvr.mylittlebusiness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import es.ucm.pad.teamjvr.mylittlebusiness.Model.Product;
import es.ucm.pad.teamjvr.mylittlebusiness.Model.DBAdapter.ProductsDBAdapter;

public class MLBApplication extends Application {
	private ProductsDBAdapter db = null;
	private Map<String, Product> products = null;

	public boolean addProduct(Product prod) {
		regenerateAttr();

		products.put(prod.getName(), prod);
		db.open();
		return db.addProduct(prod);
	}

	public void closeDatabase() {
		db.close();
	}

	public boolean deleteProduct(Product prod) {
		regenerateAttr();

		products.remove(prod.getName());
		db.open();
		return db.deleteProduct(prod);
	}

	public Product getProduct(String name) {
		regenerateAttr();

		return products.get(name);
	}

	public List<Product> productList() {
		regenerateAttr();

		return new ArrayList<Product>(products.values());
	}

	private void regenerateAttr() {
		if (db == null)
			db = new ProductsDBAdapter(this);

		if (products == null) {
			products = new HashMap<String, Product>();

			db.open();

			for (Product p : db.toList())
				products.put(p.getName(), p);
		}
	}

	public boolean updateProduct(Product prod) {
		regenerateAttr();

		products.put(prod.getName(), prod);
		db.open();
		return db.updateProduct(prod);
	}
}