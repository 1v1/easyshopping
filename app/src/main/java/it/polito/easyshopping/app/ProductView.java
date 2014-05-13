package it.polito.easyshopping.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by jessica on 14/05/14.
 */
public class ProductView extends View {
    private Paint paint;

    public ProductView(Context context) {
        super(context);
    }

    public ProductView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProductView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onDraw(Canvas canvas)
    {
        paint = new Paint();
        paint.setColor(Color.MAGENTA);
        canvas.drawRect(30f, 30f, 80f, 80f, paint);
    }
}
