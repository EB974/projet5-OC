package com.eric_b.mynews.controllers.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.eric_b.mynews.controllers.fragments.Help1Fragment;
import com.eric_b.mynews.controllers.fragments.Help2Fragment;
import com.eric_b.mynews.controllers.fragments.Help3Fragment;
import com.eric_b.mynews.controllers.fragments.Help4Fragment;
import com.eric_b.mynews.controllers.fragments.Help5Fragment;
import com.eric_b.mynews.controllers.fragments.Help6Fragment;
import com.eric_b.mynews.controllers.fragments.Help7Fragment;

class HelpPageAdapter extends FragmentPagerAdapter {
    private static int nbOfFrament = 7;
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
            case 0:
                return new Help1Fragment();
            case 1:
                return new Help2Fragment();
            case 2:
                return new Help3Fragment();
            case 3:
                return new Help4Fragment();
            case 4:
                return new Help5Fragment();
            case 5:
                return new Help6Fragment();
            case 6:
                return new Help7Fragment();
            default:
                return null;
        }
    }

    static int getNbOfFragment(){
        return nbOfFrament;
    }

}
