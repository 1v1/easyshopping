package it.polito.easyshopping.app;

import android.content.Context;
import android.content.SharedPreferences;
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

import it.polito.easyshopping.adapter.CatalogueAdapter;
import it.polito.easyshopping.adapter.ProductsAdapter;

/**
 * Created by jessica on 07/05/14.
 */
public class ProductsFragment extends Fragment {
    private HashMap<String, ArrayList<Product>> map;
    private ListView listView;
    public static final String PREFS_NAME = "MyPrefsFile";

    public ProductsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products, container, false);
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        getMap();
        listView = (ListView) rootView.findViewById(R.id.list_products);
        listView.setAdapter(new ProductsAdapter(getActivity().getApplicationContext(), map, settings.getString("section", null)));
        return rootView;
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
}
