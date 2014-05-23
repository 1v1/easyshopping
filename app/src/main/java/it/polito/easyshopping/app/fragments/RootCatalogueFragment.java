package it.polito.easyshopping.app.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.polito.easyshopping.app.R;

/**
 * Created by jessica on 08/05/14.
 */
public class RootCatalogueFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_root_catalogue, container, false);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.frag_root_catalogue, new CatalogueFragment());
        transaction.commit();
        return view;
    }
}
