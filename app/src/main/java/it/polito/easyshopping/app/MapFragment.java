package it.polito.easyshopping.app;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jessica on 06/05/14.
 */
public class MapFragment extends Fragment {
    private Button button;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_map, container, false);
        button = (Button) rootView.findViewById(R.id.button_map);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get prompts.xml view
                LayoutInflater layoutInflater = LayoutInflater.from(rootView.getContext());

                View promptView = layoutInflater.inflate(R.layout.input_dialog, null);

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
                                    Paint paint = new Paint();
                                    paint.setColor(Color.parseColor("#CD5C5C"));

                                    DisplayMetrics displaymetrics = new DisplayMetrics();
                                    getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                                    int width = displaymetrics.widthPixels; // screen width
                                    int height = displaymetrics.heightPixels; // screen height

                                    float scale = parametrizingDimensions(width, height,
                                            Float.parseFloat(input_width.getText().toString()), // rectangle width
                                            Float.parseFloat(input_depth.getText().toString())); // rectangle depth

                                    // creating the available space to draw
                                    Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                                    // creating the rectangle
                                    Canvas canvas = new Canvas(bg);
                                    canvas.drawRect(0, 0, width,
                                            scale*Float.parseFloat(input_depth.getText().toString()), paint);

                                    LinearLayout ll = (LinearLayout) rootView.findViewById(R.id.rect);
                                    ll.setBackgroundDrawable(new BitmapDrawable(bg));
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

    public float parametrizingDimensions(int screenWidth, int screenHeight, float pictureWidth, float pictureDepth) {
        return Math.min(screenWidth/pictureWidth, screenHeight/pictureDepth);
    }
}
