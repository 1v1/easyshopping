package it.polito.easyshopping.app;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jessica on 07/05/14.
 */
public class ProductsFragment extends Fragment {
    private HashMap<String, ArrayList<Product>> map;
    private ListView listView;

    public ProductsFragment() {

    }

    public static Fragment newInstance() {
        ProductsFragment mFrgment = new ProductsFragment();
        return mFrgment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        //getMap();
       // listView = (ListView) rootView.findViewById(R.id.list);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //listView.setAdapter(new ProductsAdapter(getActivity().getApplicationContext(), map));
        //listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                startFragment();
//            }
//        });
    }

    public void getMap() {
        map = new HashMap<String, ArrayList<Product>>();
        String json = getStringJson();
        try {
            JSONObject stringJson = new JSONObject(json); // string to jsonObject
            JSONArray products = stringJson.getJSONArray("products"); // array with query results

            for (int i = 0; i < products.length(); i++) {
                JSONObject result = products.getJSONObject(i);
                String set = (String) result.get("set");
                Product product = createProduct(result);

                if (set.equals("chairs")) {
                    check("chairs", product);
                } else if (set.equals("tables")) {
                    check("tables", product);
                } else if (set.equals("stands")) {
                    check("stands", product);
                } else if (set.equals("sofas")) {
                    check("sofas", product);
                } else if (set.equals("beds")) {
                    check("beds", product);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", "<json> - " + e.toString());
        }
    }

    private Product createProduct(JSONObject result) {
        Product product = new Product();
        try {
            product.setName(result.getString("name"));
            product.setDescription(result.getString("description"));
            product.setPrice(result.getString("price"));
            product.setWidth(result.getString("width"));
            product.setDepth(result.getString("depth"));
            product.setImagePath(result.getString("imagePath"));
            product.setMapPath(result.getString("mapPath"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", "<json> - " + e.toString());
        }
        return product;
    }

    private void check(String key, Product product) {
        if(!map.containsKey(key)) {
            ArrayList<Product> array = new ArrayList<Product>();
            array.add(product);
            map.put(key, array);
        } else {
            map.get(key).add(product);
        }
    }

    private String getStringJson() {
        // Reading text file from assets folder
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(getActivity().getApplicationContext().getAssets().
                    open("products.json")));
            String temp;
            while ((temp = br.readLine()) != null)
                sb.append(temp);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                br.close(); // stop reading
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sb.toString();
    }

    public void startFragment() {
        // Create new fragment and transaction
        Fragment newFragment = new ProductsFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.list, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    private static class ProductsAdapter extends BaseAdapter {
        private LayoutInflater mInflater;
        private HashMap<String, ArrayList<Product>> map;

        public ProductsAdapter(Context context, HashMap<String, ArrayList<Product>> map) {
            this.mInflater = LayoutInflater.from(context);
            this.map = map;
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

            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.list_row, null);
                holder = new ViewHolder();
                holder.imageName = (TextView) convertView.findViewById(R.id.title);
                //holder.littleDescription = (TextView) convertView.findViewById(R.id.description);
                holder.image = (ImageView) convertView.findViewById(R.id.list_image);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            ArrayList<String> sections = getSections();
            holder.imageName.setText(sections.get(position));
            //holder.littleDescription.setText("Something here...");

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

}
