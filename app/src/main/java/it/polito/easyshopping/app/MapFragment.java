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

/**
 * Created by jessica on 06/05/14.
 */
public class MapFragment extends Fragment {
    private Button button;
    private ProductView productView;
    private View rootView;
    public static final String PREFS_NAME = "MyPrefsFile";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        button = (Button) rootView.findViewById(R.id.button_map);
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
                                    int width = displaymetrics.widthPixels; // screen width
                                    int height = displaymetrics.heightPixels; // screen height

                                    float imageWidth = Float.parseFloat(input_width.getText().toString()); // room width
                                    float imageHeight = Float.parseFloat(input_depth.getText().toString()); // room height

                                    float scale = parametrizingDimensions(width, height, imageWidth, imageHeight);

                                    float newHeight = scale*Float.parseFloat(input_depth.getText().toString());

                                    // creating the available space to draw
                                    Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                                    Bitmap image = BitmapFactory.decodeResource(getActivity().getResources(),
                                            R.drawable.ic_launcher);
                                    // creating the rectangle
                                    Canvas canvas = new Canvas(bg);
                                    RoomView room = new RoomView(getActivity(), width, newHeight);
                                    room.onDraw(canvas);

                                    productView = new ProductView(getActivity().getApplicationContext());

                                    LinearLayout.LayoutParams parms
                                            = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                                    parms.leftMargin = 210;
                                    parms.topMargin = 117;
                                    parms.rightMargin = 210;
                                    parms.bottomMargin = 900;
                                    productView.setLayoutParams(parms);
                                    productView.setBackgroundColor(Color.BLUE);

                                    productView.setOnTouchListener(new MyTouchListener());
                                    room.setOnDragListener(new RoomView(getActivity()));


                                    LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.rect);
                                    ll.addView(productView);
                                    ll.setBackgroundDrawable(new BitmapDrawable(bg));

                                    RelativeLayout mapLayout = (RelativeLayout) rootView.findViewById(R.id.mapEditor);
                                    mapLayout.addView(room);
                                    mapLayout.setBackgroundDrawable(new BitmapDrawable(bg));
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
                    SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                    SharedPreferences.Editor editor = settings.edit();
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
