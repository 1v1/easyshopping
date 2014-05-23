package it.polito.easyshopping.app.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import it.polito.easyshopping.adapter.CartAdapter;
import it.polito.easyshopping.app.model.Product;
import it.polito.easyshopping.app.R;

/**
 * Created by jessica on 06/05/14.
 */
public class CartFragment extends Fragment {
    private HashMap<String, ArrayList<Product>> map;
    private ListView listView;
    private Product selectedProduct;
    private ArrayList<Product> products;
    private TextView empty, tv_totalPrice;

    //declaring receiver of the event when product is removed
    public class RemoveProductReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context,  Intent intent) {
            updateListView();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        empty = (TextView) rootView.findViewById(R.id.noProducts);
        tv_totalPrice = (TextView) rootView.findViewById(R.id.totalPrice);
        //selectedProduct = new Product();
        products = new ArrayList<Product>();

        listView = (ListView) rootView.findViewById(R.id.list_cart);

        if (products.isEmpty()) {
            empty.setText("Your basket is empty.");
        }

        //registering receiver of the event when product is removed
        getActivity().registerReceiver(new RemoveProductReceiver(), new IntentFilter("arrayUpdate"));

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateListView();
    }

    private void updateListView() {
        if (empty != null) {
            ViewGroup layout = (ViewGroup) empty.getParent();
            layout.removeView(empty);
            empty = null;
        }

        float totalPrice = totalPrice();
        products = Product.getAddedProducts();
        listView.setAdapter(new CartAdapter(getActivity().getApplicationContext(), products));
        tv_totalPrice.setText("Total price: €" + totalPrice);
    }

//    private boolean checkArray(String productID) {
//        for(Product prod : products) {
//            if (prod.getProductID().equals(productID)) {
//                indexProdToRemove = products.indexOf(prod);
//                return true;
//            }
//        }
//        return false;
//    }

    private float totalPrice() {
        float totalPrice = 0;
        for(Product prod : products) {
            totalPrice = totalPrice + Float.parseFloat(prod.getPrice());
        }
        return totalPrice;
    }
}