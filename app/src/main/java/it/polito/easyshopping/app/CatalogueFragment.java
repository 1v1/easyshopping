package it.polito.easyshopping.app;

import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import it.polito.easyshopping.adapter.CatalogueAdapter;
import it.polito.easyshopping.utils.JsonUtils;

/**
 * Created by jessica on 06/05/14.
 */
public class CatalogueFragment extends Fragment {
    private HashMap<String, ArrayList<Product>> map;
    private ListView listView;
    public static final String PREFS_NAME = "MyPrefsFile";
    private JsonUtils utils;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_catalogue, container, false);
        utils = new JsonUtils(getActivity().getApplicationContext());
        map = utils.getMap();
        listView = (ListView) rootView.findViewById(R.id.list_catalogue);
        listView.setAdapter(new CatalogueAdapter(getActivity().getApplicationContext(), map));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sectionSelected = ((TextView) view.findViewById(R.id.title)).getText().toString();
                SharedPreferences settings = getActivity().getApplicationContext().getSharedPreferences(PREFS_NAME, 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("section", sectionSelected);
                editor.commit();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.frag_root_catalogue, new ProductsFragment());
                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
