package it.polito.easyshopping.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

/**
 * Created by jessica on 15/05/14.
 */
public class RoomView extends ViewGroup {
    private int width;
    private float height;
    private Drawable enterShape = getResources().getDrawable(R.drawable.enter_shape);
    private Drawable normalShape = getResources().getDrawable(R.drawable.normal_shape);

    public RoomView(Context context) {
        super(context);
    }

    public RoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RoomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RoomView(Context context, int width, float height) {
        super(context);
        this.width = width;
        this.height = height;
    }

    @Override
    protected void onLayout(boolean b, int i, int i2, int i3, int i4) {

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#F4A460"));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(15);
        canvas.drawRect(0, 0, width, height, paint);
    }
}
