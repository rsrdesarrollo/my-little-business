package es.ucm.pad.teamjvr.mylittlebusiness.view;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import es.ucm.pad.teamjvr.mylittlebusiness.R;
import es.ucm.pad.teamjvr.mylittlebusiness.model.Product;

/**
 * Clase que implementa un adaptador para poder mostrar los productos en la ListActivity
 * (simplemente una lista de Products)
 *
 */
public class ProductAdapter extends ArrayAdapter<Product> {
	private List<Product> items;
	private LayoutInflater inflater;

	public ProductAdapter(Context context, int resource, List<Product> products) {
		super(context, resource, products);
		this.items = products;
		this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View vi = convertView;
		if (convertView == null)
			vi = inflater.inflate(R.layout.list_item_product, null);

		TextView name = (TextView) vi.findViewById(R.id.nameItemText);
		TextView stock = (TextView) vi.findViewById(R.id.stockItemText);
		TextView benefit = (TextView) vi.findViewById(R.id.benefitItemText);
		TextView price = (TextView) vi.findViewById(R.id.priceItemText);
		ImageView photo = (ImageView) vi.findViewById(R.id.photoItemImage);

		String sCurrency = Currency.getInstance(Locale.getDefault()).getSymbol();
		String sPrice = getContext().getResources().getString(R.string.price_hint);
		String sStock = getContext().getResources().getString(R.string.stock_hint);
		String sBenefit = getContext().getResources().getString(R.string.benefits_hint);			
		
		Product item = items.get(position);
		int cBenefit;
		
		if(item.getBenefits() <= 0)
			cBenefit = getContext().getResources().getColor(R.color.negative_benefits);
		else
			cBenefit = getContext().getResources().getColor(R.color.positive_benefits);

		if (item != null) {
			name.setText(item.getName());
			stock.setText(sStock+" "+item.getStock());
			price.setText(sPrice+" "+item.getPrice()+sCurrency);
			benefit.setText(sBenefit+" "+item.getBenefits()+sCurrency);
			benefit.setTextColor(cBenefit);
			
			if (item.getPhoto() != null)
				photo.setImageBitmap(item.getPhoto());
		}

		return vi;
	}
}