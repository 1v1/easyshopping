package it.polito.easyshopping.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import it.polito.easyshopping.adapter.ProductsSearchAdapter;
import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 11/05/14.
 */
public class ProductsSearch extends Activity {
    // List view
    private ListView list;
    private ArrayList<Product> allProducts;
    private ArrayList<String> products;
    private ArrayAdapter<String> adapter;
    private EditText inputSearch;
    private ImageView imageProduct;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        products = new ArrayList<String>();

        JsonUtils utils = new JsonUtils(getApplicationContext());
        allProducts = utils.getAllProducts();

        for (Product product : allProducts) {
            products.add(product.getName());
        }

        list = (ListView) findViewById(R.id.list_products);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        imageProduct = (ImageView) findViewById(R.id.list_image);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Product selectedProduct = allProducts.get(position);
                String productID = selectedProduct.getProductID();
                String set = selectedProduct.getSet();
                SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("productID", productID);
                editor.putString("set", set);
                editor.commit();
                Intent i = new Intent(ProductsSearch.this, DescProductActivity.class);
                startActivity(i);
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ProductsSearch.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });

        // Adding items to listview
        adapter = new ArrayAdapter<String>(this, R.layout.list_row, R.id.title, products);
        //adapter = new ProductsSearchAdapter(ProductsSearch.this, allProducts);
        list.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        for (String product : products) {
            int position = adapter.getPosition(product);
            // get input stream
            InputStream inputStream = null;
            try {
                inputStream = getApplicationContext().getAssets().open(allProducts.get(position).getImagePath());
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(inputStream, null);
            // set image to ImageView
            imageProduct.setImageDrawable(d);
        }
    }
}
