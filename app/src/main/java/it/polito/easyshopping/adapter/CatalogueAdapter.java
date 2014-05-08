package it.polito.easyshopping.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import it.polito.easyshopping.app.CatalogueFragment;
import it.polito.easyshopping.app.Product;
import it.polito.easyshopping.app.R;

/**
 * Created by jessica on 06/05/14.
 */
public class CatalogueAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private HashMap<String, ArrayList<Product>> map;

    public CatalogueAdapter(Context context, HashMap<String, ArrayList<Product>> map) {
        this.mInflater = LayoutInflater.from(context);
        this.map = map;
    }

    @Override
    public int getCount() {
        return map.size();
    }

    @Override
    public Object getItem(int position) {
        return map.get(position);
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
        ArrayList<String> sections = getSections();
        holder.imageName.setText(sections.get(position));
        holder.littleDescription.setText("Something here...");

        // get input stream
        InputStream inputStream = null;
        try {
            inputStream = convertView.getContext().getAssets().open(map.get(sections.get(position)).get(0).getImagePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        // load image as Drawable
        Drawable d = Drawable.createFromStream(inputStream, null);
        // set image to ImageView
        holder.image.setImageDrawable(d);

        return convertView;
    }

    private ArrayList<String> getSections() {
        ArrayList<String> sections = new ArrayList<String>();
        for(Map.Entry entry : map.entrySet()) {
            sections.add(entry.getKey().toString());
        }
        return sections;
    }

    static class ViewHolder {
        TextView imageName;
        TextView littleDescription;
        ImageView image;
    }
}
