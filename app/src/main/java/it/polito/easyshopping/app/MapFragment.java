package it.polito.easyshopping.app;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;

import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 06/05/14.
 */
public class MapFragment extends Fragment {
    private Button button;
    private ProductView selectedView;
    private View rootView;
    private RoomView room;
    private LinearLayout ll;
    private RelativeLayout roomLayout;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private int width, height;
    private float imageWidth, imageHeight, scale, newHeight, eventX, eventY;
    private ArrayList<Product> products;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);
        ll = (LinearLayout) rootView.findViewById(R.id.rect);
        roomLayout = (RelativeLayout) rootView.findViewById(R.id.mapEditor);
        button = (Button) rootView.findViewById(R.id.button_map);
        products = new ArrayList<Product>();
        setHasOptionsMenu(true);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());
                final View promptView = layoutInflater.inflate(R.layout.input_dialog, null);
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(rootView.getContext());

                // set prompts.xml to be the layout file of the alertdialog builder
                alertDialogBuilder.setView(promptView);

                final EditText input_width = (EditText) promptView.findViewById(R.id.userInput_width);
                final EditText input_depth = (EditText) promptView.findViewById(R.id.userInput_depth);

                // setup a dialog window
                alertDialogBuilder
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ViewGroup layout = (ViewGroup) button.getParent();
                                if (layout != null) { // for safety only as you are doing onClick
                                    layout.removeView(button);
                                    button = null;

                                    DisplayMetrics displaymetrics = new DisplayMetrics();
                                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                                    width = displaymetrics.widthPixels; // screen width
                                    height = displaymetrics.heightPixels; // screen height

                                    imageWidth = Float.parseFloat(input_width.getText().toString()); // room width
                                    imageHeight = Float.parseFloat(input_depth.getText().toString()); // room height

                                    scale = parametrizingDimensions(width, height, imageWidth, imageHeight);

                                    newHeight = scale*Float.parseFloat(input_depth.getText().toString());

                                    // creating the available space to draw
                                    //Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                                    Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(),
                                            R.drawable.ic_launcher);


                                    Bitmap bg = Bitmap.createBitmap(roomLayout.getWidth(), roomLayout.getHeight(), Bitmap.Config.ARGB_8888);
                                    roomLayout.setBackgroundDrawable(new BitmapDrawable(bg));

                                    // creating the rectangle
                                    Canvas canvas = new Canvas(bg);
                                    room = new RoomView(getActivity(), width, newHeight);
                                    room.onDraw(canvas);
                                }
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,	int id) {
                                        dialog.cancel();
                                    }
                                });

                // create an alert dialog
                AlertDialog alertD = alertDialogBuilder.create();
                alertD.show();

            }
        });
        return rootView;
    }

    final Handler handler = new Handler();
    Runnable mLongPressed = new Runnable() {
        public void run() {
            Log.i("", "Long press!");
            Dialog dialog = actionsDialog(selectedView);
            dialog.show();
        }
    };

    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {

        public boolean onTouch(View view, MotionEvent motionEvent) {
            if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                handler.postDelayed(mLongPressed, 500);

                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            }  else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                handler.removeCallbacks(mLongPressed);
                return false;
            } else {
                return false;
            }
        }
    }

    public Dialog actionsDialog(final View selectedView) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.choose_action)
                .setItems(R.array.actions_array, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        ProductView productView = (ProductView) selectedView;

                        switch (which) {
                            case 0:
                                // rotate
                                productView.setX(eventX - (productView.getWidth()/2));
                                productView.setY(eventY - (productView.getHeight()/2));

                                ViewGroup owner = (ViewGroup) productView.getParent();
                                owner.removeView(productView);

                                LinearLayout.LayoutParams parms
                                        = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

                                parms.width = productView.getHeight();
                                parms.height = productView.getWidth();
                                productView.setLayoutParams(parms);

                                ll.addView(productView);
                                break;
                            case 1:
                                // delete
                                Product.getAddedProducts().remove(productView.getProduct());
                                ll.removeView(selectedView);

                                //sending message to receiver registered (mapFragment) to notify about the removed product
                                Intent data = new Intent("arrayUpdate");
                                getActivity().sendBroadcast(data);

                                break;
                            case 2:
                                // cancel
                                break;
                        }
                    }
                });
        return builder.create();
    }

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {

            View view = (View) event.getLocalState();

            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.d("DEBUG", "teste");
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.d("DEBUG", "teste");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.d("DEBUG", "teste");
                    break;
                case DragEvent.ACTION_DROP:
                    selectedView = (ProductView) view;

                    eventX = event.getX();
                    eventY = event.getY();

                    if (isViewContains(eventX, eventY)) {
                        view.setX(eventX - (view.getWidth()/2));
                        view.setY(eventY - (view.getHeight()/2));

                        ViewGroup owner = (ViewGroup) view.getParent();
                        owner.removeView(view);

                        //RelativeLayout container = (RelativeLayout) v;
                        ll.addView(view);
                    } else {
                        Log.d("DEBUG", "View is outside of the room");
                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.d("DEBUG", "teste");
                    break;
                default:
                    Log.d("DEBUG", "teste");
                    break;
            }
            view.setVisibility(View.VISIBLE);
            return true;
        }

        private boolean dropEventNotHandled(DragEvent dragEvent) {
            return !dragEvent.getResult();
        }

        private boolean isViewContains(float x, float y) {
            Rect boundary = new Rect(0, 0, width, (int) newHeight);
            return boundary.contains((int) x, (int) y);
        }
    }

    @Override
    public void onResume() {

        ArrayList<Product> productsList = Product.getAddedProducts();

        if (ll.getChildCount() < productsList.size()) {
            Product selectedProduct = productsList.get(productsList.size()-1);
            float productWidth = selectedProduct.getScreenWidth(imageWidth, width);
            float productHeight = selectedProduct.getScreenHight(imageHeight, newHeight);
            setProductParams(Math.round(productWidth), Math.round(productHeight), selectedProduct);
        }
        super.onResume();
    }

    public void setProductParams(int width, int height, Product selectedProduct) {
        ProductView productView = new ProductView(getActivity().getApplicationContext());
        productView.setProduct(selectedProduct);

        LinearLayout.LayoutParams parms
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        parms.leftMargin = 0;
        parms.topMargin = 0;
        parms.width = width;
        parms.height = height;
        productView.setLayoutParams(parms);
        productView.setBackgroundColor(Color.BLUE);

        productView.setOnTouchListener(new MyTouchListener());
        ll.setOnDragListener(new MyDragListener());

        Bitmap bg = Bitmap.createBitmap(ll.getWidth(), ll.getHeight(), Bitmap.Config.ARGB_8888);
        ll.addView(productView);

        ll.setBackgroundDrawable(new BitmapDrawable(bg));
    }

    public float parametrizingDimensions(int screenWidth, int screenHeight, float pictureWidth, float pictureDepth) {
        return Math.min(screenWidth/pictureWidth, screenHeight/pictureDepth);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // if a room is already created
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu, inflater);

    }
    /**
     * On selecting action bar icons
     * */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.addButton:
                if (button == null) {
                    // setting shared preferences to manage the onItemClick in the ProductsSearch
                    settings = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                    editor = settings.edit();
                    editor.putString("mapEditor", "enabled");
                    editor.commit();
                    allProducts();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void allProducts() {
        Intent i = new Intent(getActivity(), ProductsSearchActivity.class);
        getActivity().startActivity(i);
    }

}