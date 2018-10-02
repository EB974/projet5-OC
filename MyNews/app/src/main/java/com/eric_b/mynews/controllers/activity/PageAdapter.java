package com.eric_b.mynews.controllers.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;

import com.eric_b.mynews.controllers.fragments.BusinessFragment;
import com.eric_b.mynews.controllers.fragments.MostPopularFragment;
import com.eric_b.mynews.controllers.fragments.TopStoriesFragment;

class PageAdapter extends FragmentPagerAdapter {
    // 2 - Default Constructor
    PageAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return(3); // 3 - Number of page to show
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: //Page number 1
                return new TopStoriesFragment();
            case 1: //Page number 2
                return new MostPopularFragment();
            case 2: //Page number 3
                return new BusinessFragment();
            default:
                return null;
        }
    }


    @Override
    public CharSequence getPageTitle(int position) {
        String[]  namePage = {"Top Stories","Most Popular","Business"};
        return namePage[position];
    }


    public void setItem(int setPosition){
        this.getItem(setPosition);
    }
}