package com.trukker.trukkershipperuae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Activity_Goods_detail extends AppCompatActivity
{
    static public ViewPager pager;
    String jsonLogin;
    TextView trucktype_text,movingfrom_text,movingto_text,distance_text,eta_text,date_text,time_text,labour_text,back;
    Button continue_booklater;
    LinearLayout labour_layout;
    TextView intstaller_text;
    TextView nooflable_title,noofinstaller_title;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            Intent i = new Intent(Activity_Goods_detail.this,Activity_GoodsQuote.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodsdetails);

        init();
        trucktype_text.setText(Constants.store_Hometypename.toString());
        movingfrom_text.setText(Constants.store_movefrom);
        movingto_text.setText(Constants.store_moveto);
        intstaller_text.setText(Constants.store_NoOfHandiman_g);
        SimpleDateFormat sourceFormat = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat DesiredFormat  = new SimpleDateFormat("dd MMMM yy");

        String date=Constants.store_date;
        Date date1 = null;
        try
        {
            date1 = sourceFormat.parse(date);
            String formattedDate = DesiredFormat.format(date1);

            date_text.setText(formattedDate);


        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        distance_text.setText(Constants.store_distance+" KM");

        try
        {
            int s=Integer.parseInt(Constants.eta)/60;

            int d=00,d1=00;
            d=Integer.parseInt(Constants.eta)/60;
            d1=Integer.parseInt(Constants.eta)%60;
            eta_text.setText(String.valueOf(d)+" hr "+String.valueOf(d1)+" min");
        }
        catch (Exception e)
        {

        }

        time_text.setText(Constants.store_time);
        labour_text.setText(Constants.store_NoOfLabour_g);
        if(Constants.store_NoOfLabour_g.equalsIgnoreCase("0"))
        {
            labour_text.setVisibility(View.GONE);
            nooflable_title.setVisibility(View.GONE);
        }
        if(Constants.store_NoOfHandiman_g.equalsIgnoreCase("0"))
        {
            intstaller_text.setVisibility(View.GONE);
            noofinstaller_title.setVisibility(View.GONE);
        }

        continue_booklater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_Goods_detail.this,Activity_Goods_payment.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                finish();
            }
        });
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Activity_Goods_detail.this,Activity_GoodsQuote.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);

                finish();
            }
        });
    }


public void init()
{
    back=(TextView)findViewById(R.id.back);
    trucktype_text=(TextView)findViewById(R.id.trucktype_text);
    movingfrom_text=(TextView)findViewById(R.id.movingfrom_text);
    movingto_text=(TextView)findViewById(R.id.movingto_text);
    distance_text=(TextView)findViewById(R.id.distance_text);
    eta_text=(TextView)findViewById(R.id.eta_text);
    date_text=(TextView)findViewById(R.id.date_text);
    time_text=(TextView)findViewById(R.id.time_text);
    labour_text=(TextView)findViewById(R.id.labour_text);
    continue_booklater=(Button)findViewById(R.id.continue_booklater);
    labour_layout=(LinearLayout)findViewById(R.id.labour_layout);
    intstaller_text=(TextView)findViewById(R.id.intstaller_text);
    nooflable_title=(TextView)findViewById(R.id.nooflable_title);
    noofinstaller_title=(TextView)findViewById(R.id.noofinstaller_title);
}

}
