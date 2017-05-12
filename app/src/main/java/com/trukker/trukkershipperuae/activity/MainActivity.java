package com.trukker.trukkershipperuae.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.badgers.ShortcutBadger;
import com.trukker.trukkershipperuae.fragment.FragmentDrawer;
import com.trukker.trukkershipperuae.fragment.Fragment_Contactus;
import com.trukker.trukkershipperuae.fragment.Fragment_Dashboard;
import com.trukker.trukkershipperuae.fragment.Fragment_Forgotpass_Loginwith;
import com.trukker.trukkershipperuae.fragment.Fragment_Login;
import com.trukker.trukkershipperuae.fragment.Fragment_Myorders;
import com.trukker.trukkershipperuae.fragment.Fragment_Myorders_draft;
import com.trukker.trukkershipperuae.fragment.Fragment_Notification;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private static String TAG = MainActivity.class.getSimpleName();
    LinearLayout mToolbar;
    Toolbar toolbar_main;
    TextView activtiy_title;
    View shade_view;
    UserFunctions UF;
    String gcmRegID, jsonGcm1;
    ConnectionDetector cd;
    private FragmentDrawer drawerFragment;
    SessionManager session;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Are you sure you want close application?");
            builder.setCancelable(false);
            // builder.setTitle("New Order");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {

                    finish();

                }
            });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
            builder.create().show();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        Constants.selected_Home_name.clear();
        Constants.selected_Home_price.clear();


      /*  if (cd.isConnectingToInternet())
        {
            if(Constants.currentVersion_apk.equalsIgnoreCase("0") && Constants.playstoreversion_apk.equalsIgnoreCase("0"))
            {
                versionMatcher();
            }
        }*/
       // checkversion();

      //  if(Constants.versionCode_playstore.equalsIgnoreCase("0"))
        //{

           // version_find();
      //  }

        checkint();
        boolean success = ShortcutBadger.applyCount(getApplicationContext(), 0);
        // display the first navigation drawer view on app launch
        displayView(0);
    }

    private void init()
    {

        session = new SessionManager(MainActivity.this);
        UF = new UserFunctions(MainActivity.this);
        cd = new ConnectionDetector(MainActivity.this);
        //toolbar = findViewById(R.id.toolbar);
        mToolbar = (LinearLayout) findViewById(R.id.toolbar);
        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar_main.setBackgroundResource(R.color.Appcolor);
        activtiy_title = (TextView) findViewById(R.id.activtiy_title);
        activtiy_title.setTextColor(Color.WHITE);
        activtiy_title.setText("TRUKKER");


        drawerFragment = (FragmentDrawer) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), toolbar_main);
        drawerFragment.setDrawerListener(this);

        // display the first navigation drawer view on app launch
        displayView(0);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        //with login menu
        if (session.isLoggedIn())
            switch (position) {
                case 0:
                    // Toast.makeText(getApplicationContext(), Constants.order_movingpage, Toast.LENGTH_SHORT).show();

                    if (Constants.feedback_flow.equalsIgnoreCase("Order"))
                    {
                        // Toast.makeText(getApplicationContext(), "2222", Toast.LENGTH_SHORT).show();
                        Constants.notification_open=0;

                        Constants.feedback_flow = "";
                        fragment = new Fragment_Myorders();
                        title = getString(R.string.title_Orders);
                        activtiy_title.setText("My Orders");

                    }
                    else if (Constants.paymentcompleted_order.equalsIgnoreCase("paymentcompleted_order"))
                    {
                        Constants.notification_open=0;

                        fragment = new Fragment_Myorders();
                        title = getString(R.string.title_Orders);
                        activtiy_title.setText("My Orders");


                    } else if (Constants.notification_open==1)
                    {

                        fragment = new Fragment_Notification();
                        title = getString(R.string.title_Notification);
                        activtiy_title.setText("Notification");

                    }
                    else if (Constants.order_movingpage.equalsIgnoreCase("order"))
                    {
                        Constants.notification_open=0;

                        //Toast.makeText(getApplicationContext(), "11111", Toast.LENGTH_SHORT).show();
                        fragment = new Fragment_Myorders();
                        title = getString(R.string.title_Orders);
                        activtiy_title.setText("My Orders");


                    } else if (Constants.goodscompleted_moving.equalsIgnoreCase("Goods_congratulations") || Constants.goodscompleted_moving.equalsIgnoreCase("Hire_congratulations")) {

                       // Constants.notification_open=0;

                        fragment = new Fragment_Myorders();
                        title = getString(R.string.title_Orders);
                        activtiy_title.setText("My Orders");

                    }
                    else
                    {
                        Constants.notification_open=0;

                        //  Toast.makeText(getApplicationContext(), "elseee", Toast.LENGTH_SHORT).show();
                        fragment = new Fragment_Dashboard();
                        title = getString(R.string.title_Dashboard);
                        activtiy_title.setText("TRUKKER");

                    }

                    break;


                case 1:
                    fragment = new Fragment_Myorders();
                    title = getString(R.string.title_Orders);
                    activtiy_title.setText("My Orders");

                    break;
                case 2:
                    fragment = new Fragment_Myorders_draft();
                    title = getString(R.string.title_Orders);
                    activtiy_title.setText("My Draft Orders");

                    break;
                case 3:

                    fragment = new Fragment_Forgotpass_Loginwith();
                    title = getString(R.string.title_ChangePassword);
                    activtiy_title.setText("Change Password");
                    break;
                case 4:

                    fragment = new Fragment_Notification();
                    title = getString(R.string.title_Notification);
                    activtiy_title.setText("Notification");
                    break;

                case 5:

                    fragment = new Fragment_Contactus();
                    title = getString(R.string.title_ContactUs);
                    activtiy_title.setText("Contact Us");
                    break;
                case 6:
                    logoutdialog();
                    // title = getString(R.string.title_LoginLogout);
                    break;

                default:
                    break;
            }
        else {
            //without login menu
            switch (position) {
                case 0:

                    if (Constants.getquote_moving.equalsIgnoreCase("Login_hire") ||Constants.getquote_moving.equalsIgnoreCase("Login_Goods") || Constants.registration_moving.equalsIgnoreCase("Login") || Constants.getquote_moving.equalsIgnoreCase("Login")) {
                        //Toast.makeText(getApplicationContext(), "one", Toast.LENGTH_SHORT).show();

                        fragment = new Fragment_Login();
                        Constants.order_movingpage = "";

                        title = getString(R.string.title_LoginLogout);
                        activtiy_title.setText("Login");
                    } else {
                        // Toast.makeText(getApplicationContext(),"two",Toast.LENGTH_SHORT).show();
                        fragment = new Fragment_Dashboard();
                        title = getString(R.string.title_Dashboard);
                        activtiy_title.setText("TRUKKER");
                    }


                    break;

                case 1:
                    Constants.order_movingpage = "";
                    Constants.order_movingpage = "order";
                    fragment = new Fragment_Login();
                    activtiy_title.setText("Login");
                    title = getString(R.string.title_LoginLogout);

                    break;
                case 2:

                    Constants.order_movingpage = "";
                    fragment = new Fragment_Notification();
                    title = getString(R.string.title_Notification);
                    activtiy_title.setText("Notification");
                    break;

                case 3:
                    Constants.order_movingpage = "";
                    fragment = new Fragment_Contactus();
                    activtiy_title.setText("Contact Us");
                    title = getString(R.string.title_ContactUs);
                    break;


                case 4:
                    Constants.order_movingpage = "";
                    fragment = new Fragment_Login();
                    title = getString(R.string.title_LoginLogout);
                    Constants.getquote_moving = "";
                    activtiy_title.setText("Login");
                    break;

                default:
                    break;
            }
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }
    }

    protected void logoutdialog() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
        builder.setMessage("Are you sure want to Logout?");
        builder.setTitle("LOGOUT");

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                GetGCM();


            }
        });

        builder.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        builder.create().show();
    }

    private void GetGCM()
    {

        gcmRegID = FirebaseInstanceId.getInstance().getToken();
        new RegGCMService().execute();

    }

    private class RegGCMService extends AsyncTask<Void, Void, String>
    {
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(MainActivity.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                TelephonyManager mngr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String imei_num = mngr.getDeviceId();
                JSONObject prmsLogin = new JSONObject();
                prmsLogin.put("UniqueId", "0");
                prmsLogin.put("AppName", "TrukkerUAE");
                prmsLogin.put("DeviceId", gcmRegID.toString());
                prmsLogin.put("TokenId", "AIzaSyAM_KdqAGIrG9yyW8WO2HYvyq9ImUqyAjw");
                prmsLogin.put("DeviceInfo", "0");
                prmsLogin.put("OS", "Android");
                prmsLogin.put("IMEINo", imei_num);

                Log.e("gcm_reg_json", prmsLogin + "");

                jsonGcm1 = UF.LoginUser("login/RegisterDevice", prmsLogin);
                //  Log.e("gcm_reg_response", jsonGcm + "");
            } catch (Exception e) {
                // e.printStackTrace();
            }
            return jsonGcm1;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                loading.dismiss();
                if (jsonGcm1 != null) {
                    if (jsonGcm1.equalsIgnoreCase("0")) {

                    } else {
                        JSONObject jobj = new JSONObject(jsonGcm1);
                        String status = jobj.getString("status");
                        if (status.equalsIgnoreCase("1")) {

                            //registation of notification done
                            SharedPreferences prefs;
                            String prefName = "Notification_regi";
                            prefs = getSharedPreferences(prefName, MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("id", 1);
                            editor.commit();

                            Constants.moveing_page = "Login";
                            Intent i = new Intent(MainActivity.this, MainActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            finish();

                            session.logoutUser();


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
void  checkint()
    {
        if (cd.isConnectingToInternet())
        {

        }
        else
        {
            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);
            builder.setCancelable(false);
            builder.setMessage("No Internet connection.(You need to be Connected to internet, please turn it on and try again)");
                    // builder.setTitle("New Order");

                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which) {

                            finish();
                            startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);

                        }
                    });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {

                            dialog.cancel();
                            finish();
                        }
                    });
            builder.create().show();

        }


    }
    public void version_find()
    {
        PackageManager manager =getApplicationContext().getPackageManager();
        PackageInfo info = null;
        try
        {
            info = manager.getPackageInfo(getApplicationContext().getPackageName(), 0);
            Constants.versionCode_app=info.versionName;
            //Toast.makeText(getApplicationContext(),"app : "+Constants.versionCode_app,Toast.LENGTH_LONG).show();
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();

        }
        VersionChecker versionChecker = new VersionChecker();
        try
        {
            String latestVersion = versionChecker.execute().get();
            Constants.versionCode_playstore=latestVersion;
           // Toast.makeText(getApplicationContext(),"play store :"+latestVersion,Toast.LENGTH_LONG).show();

            SharedPreferences prefs;
            String prefName = "version";
            prefs = getSharedPreferences(prefName, MODE_PRIVATE);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("version_api",Constants.versionCode_app);
            editor.putString("version_playstore", Constants.versionCode_playstore);
            editor.commit();

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
        catch (ExecutionException e)
        {
            e.printStackTrace();
        }
    }
    public class VersionChecker extends io.fabric.sdk.android.services.concurrency.AsyncTask<String, String, String>
    {
        private ProgressDialog loading;
        String newVersion;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();


            //---saves the values---

            loading = new ProgressDialog(MainActivity.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
        }

        @Override

        protected String doInBackground(String... params)
        {

            try {

                newVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.trukker.trukkershipperuae" + "&hl=en")

                        .timeout(30000)

                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")

                        .referrer("http://www.google.com")

                        .get()

                        .select("div[itemprop=softwareVersion]")

                        .first()

                        .ownText();

            } catch (IOException e) {

                e.printStackTrace();
                loading.dismiss();

            }

            return newVersion;

        }
        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            loading.dismiss();
            Log.e("Version",result);
        }
    }
   /* public void checkversion()
    {
        String api_v="0",api_playstore="0";

        SharedPreferences prefs;
        String prefName = "version";
        prefs = getSharedPreferences(prefName, MODE_PRIVATE);
        api_v= prefs.getString("version_api",api_v );
        api_playstore= prefs.getString("version_playstore", api_playstore);

        if(Float.parseFloat(api_playstore)>Float.parseFloat(api_v))
        {

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

            builder.setMessage("Now TruKKer App Update available in playstore (Are you sure you want to update)");
            builder.setTitle("TruKKer UPDATE ");
            builder.setCancelable(false);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    try
                    {
                        finish();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.trukker.trukkershipperuae")));
                    }
                    catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.trukker.trukkershipperuae")));
                    }

                }
            });

            builder.setNegativeButton("Cancel",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
            builder.create().show();



        }
    }*/
   private void versionMatcher()
   {

       try
       {

           String currentVersion =getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
           VersionChecker versionChecker = new VersionChecker();
           Constants.currentVersion_apk=currentVersion;

           String playstoreversion_apk = versionChecker.execute().get();
           Constants.playstoreversion_apk=playstoreversion_apk;

           checkversion();
       }
       catch (PackageManager.NameNotFoundException e)
       {
           e.printStackTrace();
       } catch (InterruptedException e) {
           e.printStackTrace();
       } catch (ExecutionException e) {
           e.printStackTrace();
       }

   }

    public void checkversion()
    {

        if(Float.parseFloat(Constants.playstoreversion_apk)>Float.parseFloat(Constants.currentVersion_apk))
        {

            android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this);

            builder.setMessage("There is newer version of this application available, Please click OK to upgrade now");
            builder.setTitle("TruKKer UPDATE ");
            builder.setCancelable(false);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    try
                    {
                       finish();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.trukker.trukkershipperuae")));
                    }
                    catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + "com.trukker.trukkershipperuae")));
                    }

                }
            });

            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
            builder.create().show();



        }
    }

}