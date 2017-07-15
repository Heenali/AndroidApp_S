package com.trukker.trukkershipperuae.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.trukker.trukkershipperuae.R;

/*
 * Demo of creating an application to open any URL inside the application and clicking on any link from that URl
should not open Native browser but  that URL should open in the same screen.

- Load WebView with progress bar
 */
public class Activity_IteamDelivery extends Activity {
    /** Called when the activity is first created. */

    WebView web;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteamdelivery);

        web = (WebView) findViewById(R.id.webView1);
        web.setWebViewClient(new myWebClient());
        web.getSettings().setJavaScriptEnabled(true);
        web.loadUrl("http://test.trukker.ae/trukker_postload_item_delivery.html");
    }

    public class myWebClient extends WebViewClient
    {
        ProgressDialog loading;

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
            loading = new ProgressDialog(Activity_IteamDelivery.this);
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
        public void onPageFinished(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onPageFinished(view, url);

            loading.dismiss();
        }
    }

    // To handle "Back" key press event for WebView to go back to previous screen.
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && web.canGoBack()) {
            web.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}