package it.polito.easyshopping.app.customviews;

import android.content.Context;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import it.polito.easyshopping.app.model.Product;

/**
 * Created by jessica on 14/05/14.
 */
public class ProductView extends View {
    private Paint paint;
    private Product product;

    public ProductView(Context context) {
        super(context);
    }

    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}