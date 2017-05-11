package com.trukker.trukkershipperuae.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.adapter.TabPagerItem;
import com.trukker.trukkershipperuae.adapter.TruckViewPagerAdapter;
import com.trukker.trukkershipperuae.helper.Constants;

import java.util.ArrayList;
import java.util.List;



/**
 * Created by BOOM on 3/14/2016.
 */
public class Fragment_Myorders extends Fragment {

    private List<TabPagerItem> tabs = new ArrayList<>();

    String drOwnerJson;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Constants.order_movingpage="";

        Constants.paymentcompleted_order="";
        Constants.feedback_flow = "";
        tabsCreate();

    }

    private void tabsCreate()
    {
        tabs.add(new TabPagerItem("HOME", Fragment_Myorder_Home.newFreeTruck("HOME")));
        tabs.add(new TabPagerItem("GOODS", Fragment_Myorder_Goods.newFreeTruck("GOODS")));
        tabs.add(new TabPagerItem("HIRE TRUCK", Fragment_Myorder_HireTruck.newFreeTruck("HIRE TRUCK")));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.myorder_viewpager, container, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPager);

        viewPager.setOffscreenPageLimit(tabs.size());
        viewPager.setAdapter(new TruckViewPagerAdapter(getChildFragmentManager(), tabs));

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabLayout);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            tabLayout.setElevation(15);
        }
        if(Constants.goodscompleted_moving.equalsIgnoreCase(""))
        {

        }
        else  if(Constants.goodscompleted_moving.equalsIgnoreCase("Goods_congratulations"))
        {
            viewPager.setCurrentItem(1);

        }
        else  if(Constants.goodscompleted_moving.equalsIgnoreCase("HIre_congratulations"))
        {
            viewPager.setCurrentItem(2);

        }

        tabLayout.setupWithViewPager(viewPager);




    }


}
