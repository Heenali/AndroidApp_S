package com.trukker.trukkershipperuae.activity;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.fragment.Fragment_Dashboard;
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
import java.net.URL;
import java.net.URLConnection;

/*
 * Demo of creating an application to open any URL inside the application and clicking on any link from that URl
should not open Native browser but  that URL should open in the same screen.

- Load WebView with progress bar
 */
public class Activity_IteamDelivery extends Activity {
    /** Called when the activity is first created. */

    WebView web;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    ProgressDialog loading;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_iteamdelivery);
        sm=new SessionManager(Activity_IteamDelivery.this);
        UF=new UserFunctions(Activity_IteamDelivery.this);
        cd= new ConnectionDetector(Activity_IteamDelivery.this);
        web = (WebView) findViewById(R.id.webView1);
        try
        {
            loading = new ProgressDialog(Activity_IteamDelivery.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
                String url = UserFunctions.URL + "postorder/GetParameterDetails?paramvalue=DELLINK";
                SyncMethod(url);

        }
        catch (Exception e)
        {
        }



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
    public void SyncMethod(final String GetUrl)
    {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
            // After call for background.start this run method call
            public void run() {
                try {
                    String url = GetUrl;
                    String SetServerString = "";
                    // document all_stuff = null;

                    SetServerString = fetchResult(url);
                    threadMsg(SetServerString);
                } catch (Throwable t) {
                    Log.e("Animation", "Thread  exception " + t);
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler11.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler11.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler11 = new Handler() {
                public void handleMessage(Message msg) {
                    try {
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam", "screen>>" + aResponse);
                        aResponse = aResponse.trim();
                        aResponse = aResponse.substring(1, aResponse.length() - 1);
                        aResponse = aResponse.replace("\\", "");


                        JSONObject get_res = new JSONObject(aResponse);
                        JSONArray array = new JSONArray();

                        array = get_res.getJSONArray("message");
                        Log.e("mess", "screen>>" + array.toString());

                        Log.e("mess", "screen>>" + array.getJSONObject(0).getString("Value").toString());

                        loading.dismiss();
                        web.setWebViewClient(new myWebClient());
                        web.getSettings().setJavaScriptEnabled(true);
                        web.loadUrl(array.getJSONObject(0).getString("Value").toString());


                    } catch (Exception e) {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }
    public String fetchResult(String url) throws JSONException
    {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith("https"));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(url);
        HttpGet getMethod = new HttpGet(newURI);
        try {
            response = httpClient.execute(getMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }
    public static String convertStreamToString(InputStream is) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

}