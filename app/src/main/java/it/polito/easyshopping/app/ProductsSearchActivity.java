package it.polito.easyshopping.app;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import it.polito.easyshopping.adapter.ProductsSearchAdapter;
import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 09/05/14.
 */
public class ProductsSearchActivity extends Activity {
    private ListView list;
    private ProductsSearchAdapter adapter;
    private EditText inputSearch;
    private ArrayList<Product> allProducts;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        JsonUtils utils = new JsonUtils(getApplicationContext());
        allProducts = utils.getAllProducts();

        adapter = new ProductsSearchAdapter(ProductsSearchActivity.this, allProducts);
        list = (ListView) findViewById(R.id.list_products);
        inputSearch = (EditText) findViewById(R.id.inputSearch);
        list.setAdapter(adapter);

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
            Intent i = new Intent(ProductsSearchActivity.this, DescProductActivity.class);
            startActivity(i);
            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                ProductsSearchActivity.this.adapter.getFilter().filter(cs);
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.activity_main_actions, menu);

        return super.onCreateOptionsMenu(menu);
    }
}
