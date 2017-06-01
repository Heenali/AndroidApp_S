package com.trukker.trukkershipperuae.Slider;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.support.v13.app.FragmentPagerAdapter;

/**
 * Created by nirmal on 12/08/15.
 */
public class ImageViewPagerAdapter extends FragmentPagerAdapter {
    private Context _context;
    public static int totalPage = 5;

    public ImageViewPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        _context = context;

    }

    @Override
    public Fragment getItem(int position) {
        Fragment f = new Fragment();
        switch (position) {
            case 0:
                f = new Slider_One();
                break;
            case 1:
                f = new Slider_Two();
                break;
            case 2:
                f = new Slider_Three();
                break;
            case 3:
                f = new Slider_Four();
                break;
            case 4:
                f = new Slider_Five();
                break;

        }
        return f;
    }

    @Override
    public int getCount() {
        return totalPage;
    }

}

