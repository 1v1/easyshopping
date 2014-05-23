package it.polito.easyshopping.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import it.polito.easyshopping.app.model.Product;
import it.polito.easyshopping.app.R;

/**
 * Created by jessica on 08/05/14.
 */
public class ProductsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Product> products;
    private String section;


    public ProductsAdapter(Context context, ArrayList<Product> products) {
        this.mInflater = LayoutInflater.from(context);
        this.products = products;
    }

    @Override
    public int getCount() {
        return products.size();
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
    public View getView(int position, View convertView, final ViewGroup viewGroup) {
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

        if (position < products.size()) {
            holder.imageName.setText(products.get(position).getName());

            // get input stream
            InputStream inputStream = null;
            try {
                inputStream = convertView.getContext().getAssets().open(products.get(position).getImagePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(inputStream, null);
            // set image to ImageView
            holder.image.setImageDrawable(d);
        }
        return convertView;
    }

    static class ViewHolder {
        TextView imageName;
        TextView littleDescription;
        ImageView image;
    }
}
