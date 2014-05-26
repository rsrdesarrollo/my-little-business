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
	private Product currentProd = null;

	public boolean addProduct(Product prod) {
		regenerateAttr();
		
		db.open();
		
		if (db.addProduct(prod)) {
			products.put(prod.getName(), prod);
			return true;
		}
		
		return false;
	}

	public void closeDatabase() {
		db.close();
	}

	public boolean deleteProduct(Product prod) {
		regenerateAttr();
		
		db.open();
		
		if (db.deleteProduct(prod)) {
			products.remove(prod.getName());
			return true;
		}

		return false;
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
		
		db.open();
		
		if (db.updateProduct(prod)) {
			products.put(prod.getName(), prod);
			return true;
		}
		
		return false;
	}

	public Product getCurrentProd() {
		return currentProd;
	}

	public void setCurrentProd(Product currentProd) {
		this.currentProd = currentProd;
	}
}