package es.ucm.pad.teamjvr.mylittlebusiness;

import android.app.Application;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;

/**
 * Application que guarda un estado común para el resto de Activitys
 *
 */
public class MLBApplication extends Application {
	private Product currentProd = null;

	public Product getCurrentProd() {
		return currentProd;
	}

	public void setCurrentProd(Product currentProd) {
		this.currentProd = currentProd;
	}
}