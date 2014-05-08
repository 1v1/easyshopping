package it.polito.easyshopping.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import it.polito.easyshopping.app.CartFragment;
import it.polito.easyshopping.app.CatalogueFragment;
import it.polito.easyshopping.app.MapFragment;
import it.polito.easyshopping.app.RootCatalogueFragment;

/**
 * Created by jessica on 06/05/14.
 */
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RootCatalogueFragment();
            case 1:
                return new MapFragment();
            case 2:
                return new CartFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 3; // Number of tabs
    }
}
