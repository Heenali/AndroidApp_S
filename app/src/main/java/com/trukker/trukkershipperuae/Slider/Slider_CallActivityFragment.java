package com.trukker.trukkershipperuae.Slider;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.navigationview_demo.NavigationView_Activity;
import com.navigationview_demo.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class Slider_CallActivityFragment extends Fragment {
    private ViewPager _mViewPager;
    private ImageViewPagerAdapter _adapter;
    private ImageView _btn1, _btn2, _btn3,_btn4,_btn5;
    private Button btnnext,btnskip;
    public Slider_CallActivityFragment() {
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setUpView();
        setTab();
        onCircleButtonClick();

        btnnext.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


            }
        });

        btnskip.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent in = new Intent(getActivity(), NavigationView_Activity.class);
                in.putExtra("value", true);
                startActivity(in);
                getActivity().finish();

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.slider_fragment, container, false);
    }

    private void onCircleButtonClick() {

        _btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn1.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(0);
            }
        });

        _btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn2.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(1);
            }
        });
        _btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn3.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(2);
            }
        });
        _btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _btn4.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(3);
            }
        });
        _btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                _btn5.setImageResource(R.drawable.fill_circle);
                _mViewPager.setCurrentItem(4);
            }
        });
    }

    private void setUpView() {
        _mViewPager = (ViewPager) getView().findViewById(R.id.imageviewPager);
        _adapter = new ImageViewPagerAdapter(getActivity(), getFragmentManager());
        _mViewPager.setAdapter(_adapter);
        _mViewPager.setCurrentItem(0);
        initButton();
    }

    private void setTab() {
        _mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int position) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub
                _btn1.setImageResource(R.drawable.holo_circle);
                _btn2.setImageResource(R.drawable.holo_circle);
                _btn3.setImageResource(R.drawable.holo_circle);
                _btn4.setImageResource(R.drawable.holo_circle);
                _btn5.setImageResource(R.drawable.holo_circle);
                btnAction(position);
            }

        });

    }

    private void btnAction(int action) {
        switch (action) {
            case 0:
                _btn1.setImageResource(R.drawable.fill_circle);
                btnskip.setText("Skip");
                break;
            case 1:
                _btn2.setImageResource(R.drawable.fill_circle);
                btnskip.setText("Skip");
                break;
            case 2:
                btnskip.setText("Skip");
                _btn3.setImageResource(R.drawable.fill_circle);
                break;
            case 3:
                btnskip.setText("Skip");
                _btn4.setImageResource(R.drawable.fill_circle);
                break;
            case 4:
                btnskip.setText("Done");
                _btn5.setImageResource(R.drawable.fill_circle);

                break;
        }
    }

    private void initButton()
    {
        _btn1 = (ImageView) getView().findViewById(R.id.btn1);
        _btn1.setImageResource(R.drawable.fill_circle);
        _btn2 = (ImageView) getView().findViewById(R.id.btn2);

        _btn3 = (ImageView) getView().findViewById(R.id.btn3);

        _btn4 = (ImageView) getView().findViewById(R.id.btn4);

        _btn5 = (ImageView) getView().findViewById(R.id.btn5);
        btnnext = (Button) getView().findViewById(R.id.btnnext);
        btnskip = (Button) getView().findViewById(R.id.btnskip);


    }

    private void setButton(Button btn, String text, int h, int w) {
        btn.setWidth(w);
        btn.setHeight(h);
        btn.setText(text);
    }
}
