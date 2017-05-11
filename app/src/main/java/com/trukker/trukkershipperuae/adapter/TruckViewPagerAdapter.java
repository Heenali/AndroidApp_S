package com.trukker.trukkershipperuae.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by BOOM on 3/14/2016.
 */
public class TruckViewPagerAdapter extends FragmentPagerAdapter {

    List<TabPagerItem> tabs;
    public TruckViewPagerAdapter(FragmentManager childFragmentManager, List<TabPagerItem> tabs) {
        super(childFragmentManager);
        this.tabs=tabs;
    }


    @Override
    public Fragment getItem(int position) {

        return tabs.get(position).getFragment();
    }

    @Override
    public int getCount() {

        return tabs.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabs.get(position).getTitle();
    }
}
