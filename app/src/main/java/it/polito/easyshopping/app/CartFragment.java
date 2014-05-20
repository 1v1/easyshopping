package it.polito.easyshopping.app;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import it.polito.easyshopping.adapter.CartAdapter;
import it.polito.easyshopping.adapter.CatalogueAdapter;
import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 06/05/14.
 */
public class CartFragment extends Fragment {
    private HashMap<String, ArrayList<Product>> map;
    private ListView listView;
    private JsonUtils utils;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private Product selectedProduct;
    private ArrayList<Product> products;
    private TextView empty, tv_totalPrice;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        empty = (TextView) rootView.findViewById(R.id.noProducts);
        tv_totalPrice = (TextView) rootView.findViewById(R.id.totalPrice);
        selectedProduct = new Product();
        products = new ArrayList<Product>();

        listView = (ListView) rootView.findViewById(R.id.list_cart);

        if (products.isEmpty()) {
            empty.setText("Your basket is empty.");
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        settings = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();

        //editor.remove("selectedProduct");
        String selectedProductID = settings.getString("selectedProduct", null);

        if (selectedProductID != null) {
            editor.remove("selectedProduct");
            editor.commit();

            JsonUtils utils = new JsonUtils(getActivity());
            ArrayList<Product> allProducts = utils.getAllProducts();

            for (Product prod : allProducts) {
                if (prod.getProductID().equals(selectedProductID))
                    selectedProduct = prod;
            }

            products.add(selectedProduct);

            float totalPrice = totalPrice();

            if (empty != null) {
                ViewGroup layout = (ViewGroup) empty.getParent();
                layout.removeView(empty);
                empty = null;
            }

            listView.setAdapter(new CartAdapter(getActivity().getApplicationContext(), products));
            tv_totalPrice.setText("Total price: €" + totalPrice);
        }
    }

    private float totalPrice() {
        float totalPrice = 0;
        for(Product prod : products) {
            totalPrice = totalPrice + Float.parseFloat(prod.getPrice());
        }
        return totalPrice;
    }
}
