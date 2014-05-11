package it.polito.easyshopping.utils;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import it.polito.easyshopping.app.Product;

/**
 * Created by jessica on 08/05/14.
 */
public class JsonUtils {
    private Context context;

    public JsonUtils(Context context) {
        this.context = context;
    }

    public HashMap<String, ArrayList<Product>> getMap() {
        HashMap<String, ArrayList<Product>> map = new HashMap<String, ArrayList<Product>>();
        String json = getStringJson();
        try {
            JSONObject stringJson = new JSONObject(json); // string to jsonObject
            JSONArray products = stringJson.getJSONArray("products"); // array with query results

            for (int i = 0; i < products.length(); i++) {
                JSONObject result = products.getJSONObject(i);
                String set = (String) result.get("set");
                Product product = createProduct(result);

                if (set.equals("chairs")) {
                    check("chairs", product, map);
                } else if (set.equals("tables")) {
                    check("tables", product, map);
                } else if (set.equals("stands")) {
                    check("stands", product, map);
                } else if (set.equals("sofas")) {
                    check("sofas", product, map);
                } else if (set.equals("beds")) {
                    check("beds", product, map);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", "<json> - " + e.toString());
        }
        return map;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> allProducts = new ArrayList<Product>();
        String json = getStringJson();
        try {
            JSONObject stringJson = new JSONObject(json); // string to jsonObject
            JSONArray products = stringJson.getJSONArray("products"); // array with query results
            for (int i = 0; i < products.length(); i++) {
                JSONObject result = products.getJSONObject(i);
                Product product = createProduct(result);
                allProducts.add(product);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", "<json> - " + e.toString());
        }
        return allProducts;
    }

    private Product createProduct(JSONObject result) {
        Product product = new Product();
        try {
            product.setName(result.getString("name"));
            product.setDescription(result.getString("description"));
            product.setPrice(result.getString("price"));
            product.setProductID(result.getString("productID"));
            product.setWidth(result.getString("width"));
            product.setDepth(result.getString("depth"));
            product.setSet(result.getString("set"));
            product.setImagePath(result.getString("imagePath"));
            product.setMapPath(result.getString("mapPath"));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("EXCEPTION", "<json> - " + e.toString());
        }
        return product;
    }

    private void check(String key, Product product, HashMap<String, ArrayList<Product>> map) {
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
            br = new BufferedReader(new InputStreamReader(context.getAssets().
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
