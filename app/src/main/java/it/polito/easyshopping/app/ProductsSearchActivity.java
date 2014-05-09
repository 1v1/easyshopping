package it.polito.easyshopping.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import it.polito.easyshopping.adapter.ProductsSearchAdapter;

/**
 * Created by jessica on 09/05/14.
 */
public class ProductsSearchActivity extends Activity {
    ListView list;
    String[] web = {
            "Google Plus",
            "Twitter",
            "Windows",
            "Bing",
            "Itunes",
            "Wordpress",
            "Drupal"
    } ;
    Integer[] imageId = {
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher,
            R.drawable.ic_launcher
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ProductsSearchAdapter adapter = new ProductsSearchAdapter(ProductsSearchActivity.this, web, imageId);
        list = (ListView)findViewById(R.id.lv_products_search);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(ProductsSearchActivity.this, "You Clicked at " + web[+position], Toast.LENGTH_SHORT).show();
            }
        });
    }
}
