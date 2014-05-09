package it.polito.easyshopping.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import it.polito.easyshopping.app.R;

/**
 * Created by jessica on 09/05/14.
 */
public class ProductsSearchAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public ProductsSearchAdapter(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.list_row_products_search, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;
    }
    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_row_products_search, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.tv_product_search);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.iv_product_search);
        txtTitle.setText(web[position]);
        imageView.setImageResource(imageId[position]);
        return rowView;
    }
}
