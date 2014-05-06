package it.polito.easyshopping.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayDeque;
import java.util.ArrayList;

import it.polito.easyshopping.app.Product;
import it.polito.easyshopping.app.R;

/**
 * Created by jessica on 06/05/14.
 */
public class CatalogueAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Product> arrayProducts;

    public CatalogueAdapter(Context context, ArrayList<Product> arrayProducts) {
        this.mInflater = LayoutInflater.from(context);
        this.arrayProducts = arrayProducts;
    }

    @Override
    public int getCount() {
        return arrayProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.imageName = (TextView) convertView.findViewById(R.id.title);
            holder.littleDescription = (TextView) convertView.findViewById(R.id.description);
            holder.image = (ImageView) convertView.findViewById(R.id.list_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Product product = arrayProducts.get(position);
        holder.imageName.setText(product.getName());
        holder.littleDescription.setText("Something here...");
        // get input stream
        InputStream inputStream = null;
        try {
            inputStream = convertView.getContext().getAssets().open(product.getImagePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // load image as Drawable
        Drawable d = Drawable.createFromStream(inputStream, null);
        // set image to ImageView
        holder.image.setImageDrawable(d);

        return convertView;
    }
    static class ViewHolder {
        TextView imageName;
        TextView littleDescription;
        ImageView image;
    }

}
