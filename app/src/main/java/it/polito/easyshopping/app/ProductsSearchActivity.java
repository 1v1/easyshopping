package it.polito.easyshopping.app;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

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
    private ArrayList<Product> tempList;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
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
        list.setTextFilterEnabled(true);
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                editor = settings.edit();
                String map = settings.getString("mapEditor", null);

                Product selectedProduct;
                tempList = ProductsSearchActivity.this.adapter.getTempList();
                if (tempList != null)
                    selectedProduct = tempList.get(position);
                else
                    selectedProduct = allProducts.get(position);

                if (map.equals("disabled")) { //
                    String productID = selectedProduct.getProductID();
                    String set = selectedProduct.getSet();
                    String imagePath = selectedProduct.getImagePath();
                    editor.putString("productID", productID);
                    editor.putString("set", set);
                    editor.putString("imagePath", imagePath); //VER DEPOIS
                    editor.commit();
                    Intent i = new Intent(ProductsSearchActivity.this, DescProductActivity.class);
                    startActivity(i);
                } else {
                    Product.getAddedProducts().add(selectedProduct);
                    finish();
                }

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                ProductsSearchActivity.this.adapter.getFilter().filter(cs);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        inputSearch.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode == KeyEvent.KEYCODE_DEL){
                    adapter = new ProductsSearchAdapter(ProductsSearchActivity.this, allProducts);
                    list.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        return super.onCreateOptionsMenu(menu);
    }

//    @Override
//    public void onBackPressed() {
//        adapter = new ProductsSearchAdapter(ProductsSearchActivity.this, allProducts);
//        list.setAdapter(adapter);
//        adapter.notifyDataSetChanged();
//    }

}