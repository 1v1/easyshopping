package it.polito.easyshopping.app;

import android.app.AlertDialog;
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
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
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

import org.json.JSONArray;

import java.util.ArrayList;

import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 06/05/14.
 */
public class MapFragment extends Fragment {
    private Button button;
    private ProductView productView;
    private View rootView;
    private RoomView room;
    private RelativeLayout roomLayout;
    private SharedPreferences settings;
    private SharedPreferences.Editor editor;
    private int width, height;
    private float imageWidth, imageHeight, scale, newHeight;
    private ArrayList<Product> products;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

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
                                    Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                                    Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(),
                                            R.drawable.ic_launcher);
                                    // creating the rectangle
                                    Canvas canvas = new Canvas(bg);
                                    room = new RoomView(getActivity(), width, newHeight);
                                    room.onDraw(canvas);

                                    roomLayout = (RelativeLayout) rootView.findViewById(R.id.mapEditor);
                                    roomLayout.setBackgroundDrawable(new BitmapDrawable(bg));
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

    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                ClipData data = ClipData.newPlainText("", "");
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                view.startDrag(data, shadowBuilder, view, 0);
                view.setVisibility(View.INVISIBLE);
                return true;
            } else {
                return false;
            }
        }
    }

    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    break;
                case DragEvent.ACTION_DROP:
                    View view = (View) event.getLocalState();

                    float left = event.getX();
                    float top = event.getY();

                    view.setX(left - (view.getWidth()/2));
                    view.setY(top - (view.getHeight()/2));

//                    ViewGroup owner = (ViewGroup) view.getParent();
//                    owner.removeView(view);
//
//                    RelativeLayout container = (RelativeLayout) v;
//                    container.addView(view);
                    view.setVisibility(View.VISIBLE);
                    return true;
                case DragEvent.ACTION_DRAG_ENDED:
//                    if (dropEventNotHandled(event)) {
//                        v.setVisibility(View.VISIBLE);
//                    }
                    break;
                default:
                    break;
            }
            return true;
        }

        private boolean dropEventNotHandled(DragEvent dragEvent) {
            return !dragEvent.getResult();
        }
    }

    @Override
    public void onResume() {
        settings = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
        editor = settings.edit();
        String idProduct = settings.getString("productMap", null);
        if (idProduct != null) {
            editor.remove("productMap");
            editor.commit();

            Product selectedProduct = new Product();

            // get the product from json
            JsonUtils utils = new JsonUtils(getActivity());
            ArrayList<Product> allProducts = utils.getAllProducts();

            for (Product prod : allProducts) {
                if (prod.getProductID().equals(idProduct))
                    selectedProduct = prod;
            }

            editor.putString("selectedProduct", selectedProduct.getProductID());
            editor.commit();

            float productWidth = selectedProduct.getScreenWidth(imageWidth, 720);
            float productHeight = selectedProduct.getScreenHight(imageHeight, newHeight);
            setProductParams(Math.round(productWidth), Math.round(productHeight));
        }
        super.onResume();
    }

    public void setProductParams(int width, int height) {
        productView = new ProductView(getActivity().getApplicationContext());

        LinearLayout.LayoutParams parms
                = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);

        parms.leftMargin = Math.round((this.width - imageWidth)/2);
        parms.topMargin = Math.round((newHeight - imageHeight)/2);
        parms.width = width;
        parms.height = height;
        productView.setLayoutParams(parms);
        productView.setBackgroundColor(Color.BLUE);

        productView.setOnTouchListener(new MyTouchListener());
        roomLayout.setOnDragListener(new MyDragListener()); ////
        //room.setOnDragListener(new RoomView(getActivity()));

        Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.rect);
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
