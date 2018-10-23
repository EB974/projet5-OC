package com.eric_b.mynews.controllers.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.eric_b.mynews.controllers.fragments.Help1Fragment;
import com.eric_b.mynews.controllers.fragments.Help2Fragment;

class HelpPageAdapter extends FragmentPagerAdapter {
    static int nbOfFrament = 3;
    HelpPageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return(nbOfFrament);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: //Page number 1
                return new Help1Fragment();
            case 1: //Page number 2
                return new Help2Fragment();
            case 2: //Page number 3
                return new Help1Fragment();
            default:
                return null;
        }
    }

    public static int getNbOfFragment(){
        return nbOfFrament;
    }

}
