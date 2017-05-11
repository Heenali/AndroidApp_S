package com.trukker.trukkershipperuae.fragment;

/**
 * Created by Ravi on 29/07/15.
 */

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.activity.Activity_GetQuote;
import com.trukker.trukkershipperuae.activity.Activity_GoodsQuote;
import com.trukker.trukkershipperuae.activity.Activity_HireQuote;
import com.trukker.trukkershipperuae.activity.Activity_HireTruck;
import com.trukker.trukkershipperuae.activity.Activity_MoveMyGoods;
import com.trukker.trukkershipperuae.activity.Activity_MoveMyHome;
import com.trukker.trukkershipperuae.activity.Activity_paintingselection;
import com.trukker.trukkershipperuae.activity.MainActivity;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static java.lang.System.exit;

public class Fragment_Dashboard extends Fragment
{ CardView movehome_btn;
    CardView painting,cleaning,other,pest,moving_hireturck;
    String jsonGcm1;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    View rootView;
    CardView moving_goods;
    String imei_num;
    String  gcmRegID="",unregJson,android_id;
    private SharedPreferences prefs;
    String currentVersion;
    ProgressDialog  loading;
    private String prefName = "Notification_regi";
    public Fragment_Dashboard()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        Constants.Draft="N";
        Log.e("********************", "******************************");
        Log.e("********************", "Dashbord page 1 api call(Notification)");
        Log.e("********************", "******************************");

        init();
        loading = new ProgressDialog(getContext().getApplicationContext());
        try
        {
            loading = new ProgressDialog(getContext());
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
            String url = UserFunctions.URL + "postorder/GetParameterDetails?paramvalue=PP";
            SyncMethod_ppurl(url);
        }
        catch (Exception e) {

        }

        Constants.order_movingpage="";
        Constants.counter=0;
        if(Constants.getquote_moving.equalsIgnoreCase("Login"))
        {
            Constants.Draft="N";
            Constants.getquote_moving="";
            Constants.store_shipperid = sm.getUniqueId();
            Constants.store_email = sm.getemailid();
            Constants.counter=1;
            Intent i = new Intent(getActivity().getApplicationContext(), Activity_GetQuote.class);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        if(Constants.getquote_moving.equalsIgnoreCase("Login_Goods"))
        {
            Constants.Draft="N";
            Constants.getquote_moving="";
            Constants.store_shipperid = sm.getUniqueId();
            Constants.store_email = sm.getemailid();
            Constants.counter=1;
            Intent i = new Intent(getActivity().getApplicationContext(), Activity_GoodsQuote.class);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        if(Constants.getquote_moving.equalsIgnoreCase("Login_hire"))
        {
            Constants.Draft="N";
            Constants.getquote_moving="";
            Constants.store_shipperid = sm.getUniqueId();
            Constants.store_email = sm.getemailid();
            Constants.counter=1;
            Intent i = new Intent(getActivity().getApplicationContext(), Activity_HireQuote.class);
            startActivity(i);
            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
        }
        moving_goods.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Constants.Draft="N";
                Intent i = new Intent(getActivity().getApplicationContext(), Activity_MoveMyGoods.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();

            }
        });
        moving_hireturck.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Constants.Draft="N";
                Intent i = new Intent(getActivity().getApplicationContext(), Activity_HireTruck.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();

            }
        });

        movehome_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Constants.Draft="N";
                Intent i = new Intent(getActivity().getApplicationContext(), Activity_MoveMyHome.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();
            }
        });


        pest.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent i = new Intent(getActivity().getApplicationContext(), Activity_paintingselection.class);
                i.putExtra("pagename","pest control");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();
            }
        });

        other.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent i = new Intent(getActivity().getApplicationContext(), Activity_paintingselection.class);
                i.putExtra("pagename","other");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();
            }
        });

        cleaning.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent i = new Intent(getActivity().getApplicationContext(), Activity_paintingselection.class);
                i.putExtra("pagename","cleaning");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();
            }
        });

        painting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity().getApplicationContext(), Activity_paintingselection.class);
                i.putExtra("pagename", "painting");
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void init()
    {

        sm=new SessionManager(Fragment_Dashboard.this.getActivity());
        UF=new UserFunctions(Fragment_Dashboard.this.getActivity());
        cd= new ConnectionDetector(Fragment_Dashboard.this.getActivity());

        android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        TelephonyManager mngr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        imei_num = mngr.getDeviceId();
        moving_hireturck=(CardView)rootView.findViewById(R.id.hire);
        movehome_btn=(CardView)rootView.findViewById(R.id.homess);
        painting=(CardView)rootView.findViewById(R.id.painting);
        cleaning=(CardView)rootView.findViewById(R.id.cleaning);
        other=(CardView)rootView.findViewById(R.id.other);
        pest=(CardView)rootView.findViewById(R.id.pest);
        moving_goods = (CardView) rootView.findViewById(R.id.loads);


        int notofication_id=0;
        prefs = getActivity().getSharedPreferences(prefName, getActivity().MODE_PRIVATE);
        notofication_id=prefs.getInt("id", notofication_id);
        if(notofication_id==1)
            Log.e("alredy register","Notification api");
        else
            GetGCM();



    }
    private void GetGCM()
    {

        gcmRegID = FirebaseInstanceId.getInstance().getToken();
        new RegGCMService().execute();

    }
    private class RegGCMService extends AsyncTask<Void, Void, String>
    {

        @Override
        protected String doInBackground(Void... params)
        {
            try {

                JSONObject prmsLogin = new JSONObject();
                prmsLogin.put("UniqueId", "0");
                prmsLogin.put("AppName", "TrukkerUAE");
                prmsLogin.put("DeviceId", gcmRegID.toString());
                prmsLogin.put("TokenId", "AIzaSyAM_KdqAGIrG9yyW8WO2HYvyq9ImUqyAjw");
                prmsLogin.put("DeviceInfo", "0");
                prmsLogin.put("OS", "Android");
                prmsLogin.put("IMEINo", imei_num);


                Log.e("--------------------", "----------------------------------");
                Log.e("Notification Get--\n", prmsLogin.toString());
                Log.e("Notification Url--\n", "login/RegisterDevice");
                jsonGcm1 = UF.LoginUser("login/RegisterDevice", prmsLogin);

            } catch (Exception e) {

            }
            return jsonGcm1;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                Log.e("Notification Get--\n", jsonGcm1);
                Log.e("--------------------", "----------------------------------");
                if (jsonGcm1 != null)
                {
                    if (jsonGcm1.equalsIgnoreCase("0"))
                    {

                    }
                    else
                    {
                        JSONObject jobj = new JSONObject(jsonGcm1);
                        String status = jobj.getString("status");
                        if (status.equalsIgnoreCase("1")) {

                            //registation of notification done
                            prefs = getActivity().getSharedPreferences(prefName, getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("id", 1);
                            editor.commit();

                        } else {
                           /* String servermsg = jobj.getString("message");
                            UF.msg(servermsg);*/
                        }
                    }
                } else {
                    // UF.msg(String.valueOf(R.string.internet_slow));
                }
            } catch (Exception e) {
                //UF.msg(String.valueOf(R.string.internet_slow));
            }

        }
    }
    public void SyncMethod_ppurl(final String GetUrl)
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
                        loading.cancel();

                        JSONObject get_res = new JSONObject(aResponse);
                        JSONArray array = new JSONArray();

                        array = get_res.getJSONArray("message");
                        Log.e("mess", "screen>>" + array.toString());

                        Log.e("mess", "screen>>" + array.getJSONObject(0).getString("Value").toString());
                        Constants.ppurl=array.getJSONObject(0).getString("Value").toString();

                        String currentVersion =getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
                        Constants.currentVersion_apk=currentVersion;


                    } catch (Exception e) {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }
    public String fetchResult(String urlString) throws JSONException {
        StringBuilder builder;
        BufferedReader reader;
        URLConnection connection = null;
        URL url = null;
        String line;
        builder = new StringBuilder();
        reader = null;
        try {
            url = new URL(urlString);
            connection = url.openConnection();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            //Log.d("DATA", builder.toString());
        } catch (Exception e) {

        }
        //JSONArray arr=new JSONArray(builder.toString());
        return builder.toString();
    }

}
