package it.polito.easyshopping.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;

import it.polito.easyshopping.app.Product;
import it.polito.easyshopping.app.R;
import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 09/05/14.
 */
public class ProductsSearchAdapter extends ArrayAdapter<String> implements Filterable {
    private final Activity context;
    private LayoutInflater mInflater;
    private ArrayList<Product> allProducts;
    private Filter myFilter;

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

        implementsFilter();

        return rowView;
    }

    public void implementsFilter() {
        myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults results = new FilterResults();
                ArrayList<Product> tempList = new ArrayList<Product>();
//                if (allProducts == null) {
//                    allProducts = new ArrayList<Product>(displayedProducts); // saves the original data in mOriginalValues
//                }

                if (charSequence == null || charSequence.length() == 0) {
                    // set the Original result to return
                    results.count = allProducts.size();
                    results.values = allProducts;
                } else {
                    charSequence = charSequence.toString().toLowerCase();
                    for (Product product : allProducts) {
                        String data = product.getName();
                        //if (data.toLowerCase().startsWith(charSequence.toString())) {
                        if (data.toLowerCase().contains(charSequence.toString())) {
                            tempList.add(product);
                        }
                    }

                    // set the Filtered result to return
                    results.count = tempList.size();
                    results.values = tempList;
                }
                return results; //return list of products that are filtered
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                allProducts = (ArrayList<Product>)results.values;
                if (results.count > 0)
                    notifyDataSetChanged();
                else
                    notifyDataSetInvalidated();
            }
        };
    }

    @Override
    public Filter getFilter() {
        return myFilter;
    }
}
