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
import java.util.HashMap;

import it.polito.easyshopping.app.Product;
import it.polito.easyshopping.app.R;

/**
 * Created by jessica on 08/05/14.
 */
public class ProductsAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private HashMap<String, ArrayList<Product>> map;
    private String section;

    public ProductsAdapter(Context context, HashMap<String, ArrayList<Product>> map, String section) {
        this.mInflater = LayoutInflater.from(context);
        this.map = map;
        this.section = section;
    }

    @Override
    public int getCount() {
        return map.size();
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
        ArrayList<Product> products = map.get(section);
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

        if (position <= products.size()) {
            holder.imageName.setText(products.get(position).getName());
            //holder.littleDescription.setText("Something here...");

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
