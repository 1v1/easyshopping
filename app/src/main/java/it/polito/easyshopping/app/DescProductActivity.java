package it.polito.easyshopping.app;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 09/05/14.
 */
public class DescProductActivity extends Activity {
    private ImageView imageView;
    private HashMap<String, ArrayList<Product>> map;
    private JsonUtils utils;
    private String set, productID, imagePath, description, width, depth, price;
    private TextView tv_description, tv_dimensions, tv_price;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_details_product);
        SharedPreferences settings = getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        set = settings.getString("set", null);
        productID = settings.getString("productID", null);
        imageView = (ImageView) findViewById(R.id.iv_product);
        tv_description = (TextView) findViewById(R.id.desc_product);
        tv_dimensions = (TextView) findViewById(R.id.dim_product);
        tv_price = (TextView) findViewById(R.id.price_product);
        utils = new JsonUtils(getApplicationContext());
        map = utils.getMap();
        setAttributesProduct();
        if (imagePath != null && description != null
                && width != null && depth != null) {
            // get input stream
            InputStream inputStream = null;
            try {
                inputStream = getApplicationContext().getAssets().open(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            // load image as Drawable
            Drawable d = Drawable.createFromStream(inputStream, null);
            Drawable resizedDrawable = scaleImage(d, 5);
            imageView.setImageDrawable(resizedDrawable);
            tv_description.setText("Description: " + description);
            tv_dimensions.setText("Dimensions (width x depth): " + width + "cm" + " x " + depth + "cm");
            tv_price.setText("Price: " + price);
        }
    }

    private void setAttributesProduct() {
        ArrayList<Product> products = map.get(set);
        for (Product product : products) {
            if (product.getProductID().equals(productID)) {
                imagePath = product.getImagePath();
                description = product.getDescription();
                width = product.getWidth();
                depth = product.getDepth();
                price = product.getPrice();
            }
        }
    }

    public Drawable scaleImage (Drawable image, float scaleFactor) {
        if ((image == null) || !(image instanceof BitmapDrawable)) {
            return image;
        }

        Bitmap b = ((BitmapDrawable)image).getBitmap();

        int sizeX = Math.round(image.getIntrinsicWidth() * scaleFactor);
        int sizeY = Math.round(image.getIntrinsicHeight() * scaleFactor);
        Bitmap bitmapResized = Bitmap.createScaledBitmap(b, sizeX, sizeY, false);

        image = new BitmapDrawable(getResources(), bitmapResized);

        return image;
    }
}
