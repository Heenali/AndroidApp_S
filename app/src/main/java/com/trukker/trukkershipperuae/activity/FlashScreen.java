package com.trukker.trukkershipperuae.activity;

/**
 * Created by admin on 8/29/2016.
 */

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.Constants;
//import io.fabric.sdk.android.Fabric;


public class FlashScreen extends Activity
{
    protected boolean _active = true;
    protected int _splashTime = 5000;
    //SessionManager session;
     String role_id;
    TextView animation_text,animation_text1;
    ImageView splashLogo;
    // UserFunctions UF;
    // DataBaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
       // Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splashscreen);
        animation_text=(TextView)findViewById(R.id.animation_text);
        animation_text1=(TextView)findViewById(R.id.animation_text1);
        splashLogo=(ImageView)findViewById(R.id.splashLogo);
        Animation animFadeIn = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.bouns);

        animation_text.startAnimation(animFadeIn);
        animation_text1.startAnimation(animFadeIn);
      // splashLogo.startAnimation(animFadeIn1);


        ObjectAnimator animation = ObjectAnimator.ofFloat(splashLogo, "rotationY", 0.0f, 360f);
        animation.setDuration(5600);
        animation.setRepeatCount(ObjectAnimator.INFINITE);
        animation.setInterpolator(new AccelerateDecelerateInterpolator());
        animation.start();
        //////////////////////////////////////////////////////
        initValue();

        //////////////////////////////////////////////////////
        Thread splashThread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    while (_active && (waited < _splashTime))
                    {
                        sleep(100);

                        if (_active) {
                            waited += 100;
                        }
                    }

                } catch (InterruptedException e) {

                }
                finally
                {
                        Intent intent = new Intent(FlashScreen.this, MainActivity.class);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up);
                        finish();


                }
            }
        };
        splashThread.start();
    }

    private void initValue() {
        try
        {
            String countryIso="",networkIso="";
            TelephonyManager tm = (TelephonyManager)this.getSystemService(this.TELEPHONY_SERVICE);


           // Toast.makeText(getApplication(),countryCodeValue,Toast.LENGTH_SHORT).show();
            try
            {
                networkIso= tm.getNetworkCountryIso();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

            try {
                countryIso = tm.getSimCountryIso();
            } catch (Exception e) {
                e.printStackTrace();
            }

            Log.e("networkIso", networkIso);//
            Log.e("countryIso", countryIso);//
            if (networkIso.equalsIgnoreCase("") || networkIso.equalsIgnoreCase(null))
            {
                if (countryIso.equalsIgnoreCase("") || countryIso.equalsIgnoreCase(null))
                {
                    Constants.countryISOCod="AE";
                }
                else
                {
                    Constants.countryISOCod=countryIso;
                }
            }
            else
            {
                Constants.countryISOCod=networkIso;
            }


        }
        catch (Exception e)
        {
            Constants.countryISOCod="AE";
        }


    }

}
