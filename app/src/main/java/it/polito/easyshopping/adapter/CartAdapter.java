package it.polito.easyshopping.adapter;

import android.content.Context;
import android.content.SharedPreferences;
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
 * Created by jessica on 20/05/14.
 */
public class CartAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private ArrayList<Product> arrayProducts;
    public static final String PREFS_NAME = "MyPrefsFile";
    private SharedPreferences settings;
    private Context context;

    public CartAdapter(Context context, ArrayList<Product> arrayProducts) {
        this.mInflater = LayoutInflater.from(context);
        this.arrayProducts = arrayProducts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return arrayProducts.size();
    }

    @Override
    public Object getItem(int position) {
        return arrayProducts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        settings = settings = context.getSharedPreferences(PREFS_NAME, 0);

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.list_row, null);
            holder = new ViewHolder();
            holder.imageName = (TextView) convertView.findViewById(R.id.title);
            holder.price = (TextView) convertView.findViewById(R.id.description);
            holder.image = (ImageView) convertView.findViewById(R.id.list_image);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.imageName.setText(arrayProducts.get(position).getName());
        // get input stream
        InputStream inputStream = null;
        try {
            inputStream = convertView.getContext().getAssets().open(arrayProducts.get(position).getImagePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // load image as Drawable
        Drawable d = Drawable.createFromStream(inputStream, null);
        // set image to ImageView
        holder.image.setImageDrawable(d);
        holder.price.setText("€" + arrayProducts.get(position).getPrice());

        return convertView;
    }

    static class ViewHolder {
        TextView imageName;
        TextView price;
        ImageView image;
    }
}
