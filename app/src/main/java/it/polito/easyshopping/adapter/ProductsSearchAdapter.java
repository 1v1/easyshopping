package it.polito.easyshopping.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import it.polito.easyshopping.app.Product;
import it.polito.easyshopping.app.R;
import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 09/05/14.
 */
public class ProductsSearchAdapter extends ArrayAdapter<String> {
    private final Activity context;
    private LayoutInflater mInflater;
    private ArrayList<Product> allProducts;

    public ProductsSearchAdapter(Activity context, ArrayList<Product> allProducts) {
        super(context, R.layout.list_row);
        this.context = context;
        this.allProducts = allProducts;
    }

    @Override
    public int getCount() {
        return allProducts.size();
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        mInflater = context.getLayoutInflater();
        View rowView = mInflater.inflate(R.layout.list_row, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.title);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.list_image);

        if (position < allProducts.size()) {
            txtTitle.setText(allProducts.get(position).getName());

            // get input stream
            InputStream inputStream = null;
            try {
                inputStream = context.getAssets().open(allProducts.get(position).getImagePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(inputStream, null);
            // set image to ImageView
            imageView.setImageDrawable(d);
        }

        return rowView;
    }
}
