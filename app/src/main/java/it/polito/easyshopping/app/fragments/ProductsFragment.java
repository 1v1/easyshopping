package it.polito.easyshopping.app.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

import it.polito.easyshopping.adapter.ProductsAdapter;
import it.polito.easyshopping.app.model.Product;
import it.polito.easyshopping.app.R;
import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 07/05/14.
 */
public class ProductsFragment extends Fragment {
    private HashMap<String, ArrayList<Product>> map;
    private ListView listView;
    private JsonUtils utils;
    private ArrayList<Product> products;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_products, container, false);
        SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        utils = new JsonUtils(getActivity().getApplicationContext());
        map = utils.getMap();
        products = map.get(settings.getString("section", null));
        listView = (ListView) rootView.findViewById(R.id.list_products);
        listView.setAdapter(new ProductsAdapter(getActivity().getApplicationContext(), products));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Product selectedProduct = products.get(position);
                String productID = selectedProduct.getProductID();
                String set = selectedProduct.getSet();
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("productID", productID);
                editor.putString("set", set);
                editor.commit();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_root_catalogue, new DescProductFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return rootView;
    }
}
