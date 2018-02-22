package com.trukker.trukkershipperuae.Webview;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.activity.Activity_GetQuote;
import com.trukker.trukkershipperuae.activity.Activity_IteamDelivery;
import com.trukker.trukkershipperuae.activity.Activity_MoveMyHome;
import com.trukker.trukkershipperuae.activity.MainActivity;
import com.trukker.trukkershipperuae.fragment.Fragment_Login;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.httpsrequest.HTTPUtils;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;

/**
 * Created by Heenali on 2/20/2018.
 */

public class Hiretruck_Webview extends AppCompatActivity
{

    WebView webView;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    ProgressDialog loading;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            Intent i = new Intent(Hiretruck_Webview.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.right_in, R.anim.left_out);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        sm=new SessionManager(Hiretruck_Webview.this);
        UF=new UserFunctions(Hiretruck_Webview.this);
        cd= new ConnectionDetector(Hiretruck_Webview.this);

        try
        {
            webView = (WebView)findViewById(R.id.webView1);
            webView.setWebViewClient(new WebViewClient());
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(UserFunctions.day_web);

        }
        catch (Exception e)
        {

        }



    }
    public class WebViewClient extends android.webkit.WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);

            loading = new ProgressDialog(Hiretruck_Webview.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            // TODO Auto-generated method stub
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {

            // TODO Auto-generated method stub

            super.onPageFinished(view, url);


            loading.dismiss();

        }

    }

}