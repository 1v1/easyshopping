package it.polito.easyshopping.app;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import it.polito.easyshopping.adapter.SectionAdapter;

/**
 * Created by jessica on 06/05/14.
 */
public class CatalogueFragment extends Fragment {
    private HashMap<String, ArrayList<Product>> map;
    private ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catalogue, container, false);
        getMap();
//        ArrayList<Product> arrayProducts = new ArrayList<Product>();
//        Product product = new Product();
//        product.setName("Chair");
//        product.setImagePath("chair.png");
//        arrayProducts.add(product);
        listView = (ListView) rootView.findViewById(R.id.list);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView.setAdapter(new SectionAdapter(getActivity().getApplicationContext(), map));
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
