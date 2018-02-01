package com.trukker.trukkershipperuae.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.RelativeSizeSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trukker.trukkershipperuae.R;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 8/31/2016.
 */
public class Activity_GetQuote extends AppCompatActivity
{
    TextView supersaving;
    ProgressDialog loading;
    public  LinearLayout container;
    String[] title_s;
    String[] description_s;
    String[] title_p;
    String[] description_p;
    String[] title_saving;
    String[] description_saving;
    String[] type_code;
    TextView next;
    String android_id;
    LinearLayout standard_btn,premium_btn;
    TextView s_cost,p_cost;
    String json_save;
    TextView stander_selected,premium_selected;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    String  selectedIntent="S";
    ImageView triangle_left,triangle_right,triangle_center;
    LinearLayout standardselect_layout,premiumselect_layout;
    TextView back,package_title;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Constants.counter=1;
                Intent i = new Intent(Activity_GetQuote.this,Activity_MoveMyHome.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();

        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getquote);
        Constants.panding=0;
        init();
        //Toast.makeText(getApplicationContext(), Constants.selectedIntent_getquote, Toast.LENGTH_SHORT).show();
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        Constants.counter = 1;
                        Intent i = new Intent(Activity_GetQuote.this, Activity_MoveMyHome.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(i);
                        overridePendingTransition(R.anim.left_in, R.anim.right_out);
                        finish();

                    }
                });


        //send mail single time during postloade

            if (cd.isConnectingToInternet())
            {
                Constants.packing_chcek_s="Y";
                Constants.promocode_store="";
                Constants.counter_addon=0;
                Constants.selected_Home_name.clear();
                Constants.selected_Home_price.clear();
                Constants.IncludeAddonService="N";
                Constants.AddonServices="";
                Constants.Home_store_addons_price .clear();
                Constants.Home_store_addons_view .clear();
                Constants.Home_store_addons_spinner .clear();
                Constants.Home_store_addons.clear();
                new  GetJsonmovemyhome_save().execute();
            } else {
                UF.msg("Check your internet connection.");
            }



        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if (cd.isConnectingToInternet())
                {
                    Constants.packing_chcek_s="Y";
                    Constants.promocode_store="";
                    Constants.counter_addon=0;
                    Constants.selected_Home_name.clear();
                    Constants.selected_Home_price.clear();
                    Constants.IncludeAddonService="N";
                    Constants.AddonServices="";
                    Constants.Home_store_addons_price .clear();
                    Constants.Home_store_addons_view .clear();
                    Constants.Home_store_addons_spinner .clear();
                    Constants.Home_store_addons.clear();
                    new  GetJsonmovemyhome_save_next().execute();
                }
                else
                {
                    UF.msg("Check your internet connection.");
                }

            }
       });

//////////////////
       try
        {
            String url=UserFunctions.URL+"postOrder/GetQuotSpecificationsDetails";
            SyncMethod(url);
        }
        catch (Exception e)
        {

        }


    }
    public void init()
    {

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm=new SessionManager(Activity_GetQuote.this);
        UF=new UserFunctions(Activity_GetQuote.this);
        cd= new ConnectionDetector(Activity_GetQuote.this);

        container = (LinearLayout)findViewById(R.id.container);
        container.removeAllViews();
        standard_btn = (LinearLayout)findViewById(R.id.stadard_layout);
        premium_btn = (LinearLayout)findViewById(R.id.premium_layout);
        standardselect_layout = (LinearLayout)findViewById(R.id.standardselect_layout);
        premiumselect_layout = (LinearLayout)findViewById(R.id.premiumselect_layout);

        triangle_left= (ImageView)findViewById(R.id.triangle_left);
        triangle_right = (ImageView)findViewById(R.id.triangle_right);
        triangle_center = (ImageView)findViewById(R.id.triangle_center);
        package_title=(TextView)findViewById(R.id.title_pac);
        supersaving=(TextView)findViewById(R.id.supersaving);
        back=(TextView)findViewById(R.id.back);
        s_cost=(TextView)findViewById(R.id.stander_text);
        p_cost=(TextView)findViewById(R.id.premium_text);

        stander_selected=(TextView)findViewById(R.id.stander_selected);
        premium_selected=(TextView)findViewById(R.id.premium_selected);

        //premium_selected.setVisibility(View.GONE);
        triangle_center.setVisibility(View.GONE);
        triangle_right.setVisibility(View.GONE);
        triangle_left.setVisibility(View.VISIBLE);


        premiumselect_layout.setVisibility(View.GONE);
        next=(TextView)findViewById(R.id.next_btn);
        Log.e("Exam", "inside");
        loading = new ProgressDialog(Activity_GetQuote.this);
        loading.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.show();
        loading.setContentView(R.layout.my_progress);
    }
    public void SyncMethod(final String GetUrl)
    {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable()
        {
            // After call for background.start this run method call
            public void run()
            {
                try
                {
                    String url=GetUrl;
                    String SetServerString = "";
                    // document all_stuff = null;

                    SetServerString=fetchResult(url);
                    threadMsg(SetServerString);
                }
                catch (Throwable t)
                {
                    Log.e("Animation", "Thread  exception " + t);
                }
            }
            private void threadMsg(String msg)
            {

                if (!msg.equals(null) && !msg.equals(""))
                {
                    Message msgObj = handler11.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler11.sendMessage(msgObj);
                }
            }
            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler11 = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    try
                    {
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam","screen>>"+aResponse);
                        aResponse = aResponse.trim();
                        aResponse = aResponse.substring(1, aResponse.length() - 1);
                        aResponse = aResponse.replace("\\", "");
                        loading.cancel();

                        JSONObject get_res=new JSONObject(aResponse);

                        //actorsList = new ArrayList<Document_method>();
                        JSONArray array=new JSONArray();

                        array=get_res.getJSONArray("message");
                        Log.e("Heenali Lakhani", "screen>>" + array.toString());
                        title_s= new String[array.length()];
                        description_s = new String[array.length()];
                        title_p= new String[array.length()];
                        description_p = new String[array.length()];
                        title_saving= new String[array.length()];
                        description_saving = new String[array.length()];
                        type_code = new String[array.length()];
                        container.removeAllViews();
                        for(int aa=0;aa<array.length();aa++)
                        {

                            String type_code=array.getJSONObject(aa).getString("type_code").toString();
                            if(type_code.equalsIgnoreCase("B"))
                            {
                                title_s[aa]=array.getJSONObject(aa).getString("type_name").toString();
                                description_s[aa]=array.getJSONObject(aa).getString("description").toString();
                                title_p[aa]="1";
                                description_p[aa]="1";
                                title_saving[aa]="1";
                                description_saving[aa]="1";


                                if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                                {
                                    Constants.selectedIntent_getquote="S";
                                    package_title.setText("standard package details");
                                    LayoutInflater layoutInflater =(LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View addView = layoutInflater.inflate(R.layout.list_ordertype, null);
                                    TextView type_name = (TextView) addView.findViewById(R.id.title);
                                    TextView description = (TextView) addView.findViewById(R.id.title_sub);
                                    type_name.setText(title_s[aa].toString());
                                    description.setText(description_s[aa].toString());

                                    container.addView(addView);
                                }


                            }
                            else if(type_code.equalsIgnoreCase("p"))
                            {
                                title_p[aa]=array.getJSONObject(aa).getString("type_name").toString();
                                description_p[aa]=array.getJSONObject(aa).getString("description").toString();
                                title_s[aa]="1";
                                description_s[aa]="1";
                                title_saving[aa]="1";
                                description_saving[aa]="1";

                                if(Constants.selectedIntent_getquote.equalsIgnoreCase("P"))
                                {
                                    package_title.setText("premuim package details");
                                    selectedIntent = "P";
                                    Constants.selectedIntent_getquote="P";
                                    triangle_right.setVisibility(View.VISIBLE);
                                    triangle_left.setVisibility(View.GONE);
                                    triangle_center.setVisibility(View.GONE);
                                    premiumselect_layout.setVisibility(View.VISIBLE);
                                    standardselect_layout.setVisibility(View.GONE);

                                    LayoutInflater layoutInflater =
                                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View addView = layoutInflater.inflate(R.layout.list_ordertype, null);
                                    TextView type_name = (TextView) addView.findViewById(R.id.title);
                                    TextView description = (TextView) addView.findViewById(R.id.title_sub);
                                    type_name.setText(title_p[aa].toString());
                                    description.setText(description_p[aa].toString());

                                    container.addView(addView);
                                }

                            }
                            else if(type_code.equalsIgnoreCase("S"))
                            {


                                title_saving[aa]=array.getJSONObject(aa).getString("type_name").toString();
                                description_saving[aa]=array.getJSONObject(aa).getString("description").toString();
                                title_s[aa]="1";
                                description_s[aa]="1";
                                title_p[aa]="1";
                                description_p[aa]="1";

                                if(Constants.selectedIntent_getquote.equalsIgnoreCase("SAVINGS"))
                                {
                                    package_title.setText("Super saver package details");
                                    selectedIntent = "SAVINGS";
                                    Constants.selectedIntent_getquote="SAVINGS";
                                    triangle_right.setVisibility(View.GONE);
                                    triangle_left.setVisibility(View.GONE);
                                    triangle_center.setVisibility(View.VISIBLE);

                                    premiumselect_layout.setVisibility(View.GONE);
                                    standardselect_layout.setVisibility(View.GONE);

                                    LayoutInflater layoutInflater =
                                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View addView = layoutInflater.inflate(R.layout.list_ordertype, null);
                                    TextView type_name = (TextView) addView.findViewById(R.id.title);
                                    TextView description = (TextView) addView.findViewById(R.id.title_sub);
                                    type_name.setText(title_saving[aa].toString());
                                    description.setText(description_saving[aa].toString());

                                    container.addView(addView);
                                }

                            }
                            else {


                                /*    package_title.setText("standard package details");
                                    LayoutInflater layoutInflater =
                                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                    final View addView = layoutInflater.inflate(R.layout.list_ordertype, null);
                                    TextView type_name = (TextView) addView.findViewById(R.id.title);
                                    TextView description = (TextView) addView.findViewById(R.id.title_sub);
                                    type_name.setText(title_s[aa].toString());
                                    description.setText(description_s[aa].toString());

                                    container.addView(addView);*/

                            }



                            standard_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                   // premium_selected.setVisibility(View.GONE);
                                   // stander_selected.setVisibility(View.VISIBLE);
                                  standard_btn_click();
                                    triangle_right.setVisibility(View.GONE);
                                    triangle_left.setVisibility(View.VISIBLE);
                                    triangle_center.setVisibility(View.GONE);

                                }
                            });
                            premium_btn.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v)
                                {
                                    //premium_selected.setVisibility(View.VISIBLE);
                                    // stander_selected.setVisibility(View.GONE);
                                 setPremium_btn_selected();
                                    triangle_right.setVisibility(View.VISIBLE);
                                    triangle_left.setVisibility(View.GONE);
                                    triangle_center.setVisibility(View.GONE);

                                }
                            });
                          supersaving.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    package_title.setText("Super saver package details");
                                    //premium_selected.setVisibility(View.VISIBLE);
                                    // stander_selected.setVisibility(View.GONE);
                                    triangle_right.setVisibility(View.GONE);
                                    triangle_left.setVisibility(View.GONE);
                                    triangle_center.setVisibility(View.VISIBLE);

                                    premiumselect_layout.setVisibility(View.GONE);
                                    standardselect_layout.setVisibility(View.GONE);
                                    container.removeAllViews();
                                    selectedIntent = "SAVINGS";
                                    for(int aa=0;aa<title_saving.length;aa++)
                                    {
                                        if(title_saving[aa].toString().equalsIgnoreCase("1"))
                                        {

                                        }
                                        else
                                        {
                                            LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                            final View addView = layoutInflater.inflate(R.layout.list_ordertype, null);
                                            TextView type_name = (TextView) addView.findViewById(R.id.title);
                                            TextView description = (TextView) addView.findViewById(R.id.title_sub);
                                            type_name.setText(title_saving[aa].toString());
                                            description.setText(description_saving[aa].toString());

                                            container.addView(addView);
                                        }

                                    }


                                }
                            });

                        }


                    }
                    catch(Exception e)
                    {

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


    @Override
    public void onResume()
    {
        super.onResume();

        //clear all data
      //  container.removeAllViews();
        ////////////////




    }
    public void standard_btn_click()
    {
        package_title.setText("standard package details");
        selectedIntent="S";
        premiumselect_layout.setVisibility(View.GONE);
        standardselect_layout.setVisibility(View.VISIBLE);
        container.removeAllViews();
        Log.e("length", String.valueOf(title_s.length));
        Constants.selectedIntent_getquote="S";
        for(int aa=0;aa<title_s.length;aa++)
        {


            if(title_s[aa].toString().equalsIgnoreCase("1"))
            {

            }
            else
            {
                LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.list_ordertype, null);
                TextView type_name = (TextView) addView.findViewById(R.id.title);
                TextView description = (TextView) addView.findViewById(R.id.title_sub);
                type_name.setText(title_s[aa].toString());
                description.setText(description_s[aa].toString());

                container.addView(addView);
            }

        }
    }
    public void setPremium_btn_selected()
    {
        selectedIntent = "P";
        package_title.setText("premuim package details");
        premiumselect_layout.setVisibility(View.VISIBLE);
        standardselect_layout.setVisibility(View.GONE);
        container.removeAllViews();

        for (int aa = 0; aa < title_p.length; aa++) {
            if (title_p[aa].toString().equalsIgnoreCase("1")) {

            } else {
                Log.e("length", String.valueOf(title_p.length));
                LayoutInflater layoutInflater =
                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View addView = layoutInflater.inflate(R.layout.list_ordertype, null);
                TextView type_name = (TextView) addView.findViewById(R.id.title);
                TextView description = (TextView) addView.findViewById(R.id.title_sub);
                type_name.setText(title_p[aa].toString());
                description.setText(description_p[aa].toString());

                container.addView(addView);
            }
        }

    }
    private class GetJson_save_mail extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GetQuote.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();



            try {

           ///////////////////////////////////////
                String inputPatternd = "dd/MM/yyyy";
                String outputPatternd = "dd MMM yyyy";

                String inputPatternt = "h a";
                String outputPatternt = "h:mm";

                SimpleDateFormat inputFormatd = new SimpleDateFormat(inputPatternd);
                SimpleDateFormat outputFormatd = new SimpleDateFormat(outputPatternd);

                SimpleDateFormat inputFormatt = new SimpleDateFormat(inputPatternt);
                SimpleDateFormat outputFormatt = new SimpleDateFormat(outputPatternt);

                Date date = null;
                Date date1 = null;
                String strd = null;
                String strt = null;

                try {
                    date = inputFormatd.parse(Constants.store_date);
                    date1 = inputFormatt.parse(Constants.store_time_display);
                    strd = outputFormatd.format(date);
                    strt= outputFormatt.format(date1);

                    Log.e("Date",strd);
                    Log.e("Time",strt);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
///////////////////////////////

//22 aug 2016 like that time 24 hrs
                prmsLogin.put("EmailID", sm.getemailid());
                prmsLogin.put("Message", "");
                prmsLogin.put("Source", "QUOTES");
                prmsLogin.put("mobile_no", sm.getUserId());
                prmsLogin.put("name", sm.getUserName());
                prmsLogin.put("subject", Constants.store_Hometypename);
                prmsLogin.put("Sizetypecode",Constants.store_Hometypename );
                prmsLogin.put("Sourceaddress", Constants.store_movefrom);
                prmsLogin.put("Destinationaddress",Constants.store_moveto);
                prmsLogin.put("ShippingDatetime", strd+" , "+strt);
                prmsLogin.put("load_inquiry_no",Constants.store_loadinquiry_no );
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM",Constants.store_unit);

                jsonArray.put(prmsLogin);

                prms.put("Email", jsonArray);

            } catch (JSONException e)
            {

                e.printStackTrace();
            }

            json_save = UF.RegisterUser("mailer/SaveQuotationRequestMail", prms);
            Log.e("contaus mydata......>", prms.toString());

            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();


            if (json_save.equals("lost")) {
                UF.msg("Connection Problem.");
            } else {
                if (json_save.equalsIgnoreCase("0"))
                {
                    UF.msg("Invalid");
                } else {
                    try {
                        JSONObject jobj = new JSONObject(json_save);
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        Log.e("contaus mail......>",status.toString());
                        Log.e("contaus mail......>",message.toString());
                        if (status.equalsIgnoreCase("1"))
                        {

                            Constants.storemail_send="1";


                        }
                        else
                        {
                           // UF.msg(message + "");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


        }
    }
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GetQuote.this);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);

        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            try
            {
                int requiredproce=0;
                try
                {
                    requiredproce=Integer.parseInt(Constants.promocode_discountstore)+Integer.parseInt(Constants.promocode_discount_value_store);

                }
                catch (Exception e)
                {

                }

                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("Area", Constants.store_area);
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");
                prmsLogin.put("NoOfTruck", "");
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", "");
                prmsLogin.put("NoOfHandiman", "");

                if (sm.isLoggedIn()) {
                    prmsLogin.put("shipper_id", sm.getUniqueId());
                } else {
                    prmsLogin.put("shipper_id", "");
                }
                if (sm.isLoggedIn()) {
                    prmsLogin.put("load_inquiry_no",  Constants.store_loadinquiry_no);
                } else {
                    // prmsLogin.put("load_inquiry_no", posting_shippingid);
                }

                Log.e("heenali Lkahni jay", "dddd");
                prmsLogin.put("email_id", Constants.store_email);

                prmsLogin.put("required_price", "0");
                prmsLogin.put("inquiry_source_addr", Constants.store_movefrom);
                prmsLogin.put("inquiry_source_city", Constants.postload_source_city);
                prmsLogin.put("inquiry_source_lat", Constants.postloadsourceLat);
                prmsLogin.put("inquiry_source_lng", Constants.postloadsourceLong);
                prmsLogin.put("source_pincode", "");
                prmsLogin.put("load_inquiry_shipping_date", Constants.store_date);
                prmsLogin.put("load_inquiry_shipping_time", Constants.store_time);
                prmsLogin.put("inquiry_destination_addr", Constants.store_moveto);
                prmsLogin.put("inquiry_destination_city", Constants.postload_dest_city);
                prmsLogin.put("inquiry_destionation_lat", Constants.postloaddestLat);
                prmsLogin.put("inquiry_destionation_lng", Constants.postloaddestLong);
                prmsLogin.put("destination_pincode", "");
                prmsLogin.put("remarks", "");
                prmsLogin.put("payment_mode", "");
                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("Isfinalorder", "N");

                prmsLogin.put("rate_type_flag", "N");




                prmsLogin.put("IncludePackingCharge", "Y");

                prmsLogin.put("Isupdate", "Y");

                prmsLogin.put("order_type_flag", "H");
                prmsLogin.put("TruckTypeCode", "");
                prmsLogin.put("goods_type_flag", "Y");
                prmsLogin.put("promocode", "");
                prmsLogin.put("Isupdatebillingadd", "N");
                prmsLogin.put("IncludeAddonService", Constants.IncludeAddonService);
                prmsLogin.put("AddonServices", Constants.AddonServices);


                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin heenali", prms + "");
            json_save = UF.RegisterUser("postorder/SaveMovingHomeDetails", prms);

            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();

                if (json_save.equalsIgnoreCase("0")) {
                    UF.msg("Invalidjjj");
                } else {
                    try {
                        JSONObject jobj = new JSONObject(json_save);

                        Log.e("getquotealldata hhhhh ", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray array = new JSONArray();

                            array = jobj.getJSONArray("message");
                            Log.e("array", array.toString());
                            String totlecost_pri = array.getJSONObject(0).getString("Total_cost").toString();
                            String totlecost_stan = array.getJSONObject(1).getString("Total_cost").toString();
                            String totlecost_saving = array.getJSONObject(2).getString("Total_cost").toString();


                            String NoOfTruck_p = array.getJSONObject(0).getString("NoOfTruck").toString();
                            String NoOfLabour_p = array.getJSONObject(0).getString("NoOfLabour").toString();
                            String NoOfHandiman_p = array.getJSONObject(0).getString("NoOfHandiman").toString();
                            String BaseRate_p = array.getJSONObject(0).getString("Total_cost_without_discount").toString();
                            String TotalLabourRate_p = array.getJSONObject(0).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_p = array.getJSONObject(0).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_p = array.getJSONObject(0).getString("TotalPackingRate").toString();


                            String NoOfTruck_s = array.getJSONObject(1).getString("NoOfTruck").toString();
                            String NoOfLabour_s = array.getJSONObject(1).getString("NoOfLabour").toString();
                            String NoOfHandiman_s = array.getJSONObject(1).getString("NoOfHandiman").toString();
                            String BaseRate_s = array.getJSONObject(1).getString("BaseRate").toString();
                            String TotalLabourRate_s = array.getJSONObject(1).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_s = array.getJSONObject(1).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_s = array.getJSONObject(1).getString("TotalPackingRate").toString();


                            String NoOfTruck_saving = array.getJSONObject(2).getString("NoOfTruck").toString();
                            String NoOfLabour_saving = array.getJSONObject(2).getString("NoOfLabour").toString();
                            String NoOfHandiman_saving = array.getJSONObject(2).getString("NoOfHandiman").toString();

                            String BaseRate_saving = array.getJSONObject(2).getString("BaseRate").toString();
                            String TotalLabourRate_saving = array.getJSONObject(2).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_saving = array.getJSONObject(2).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_saving = array.getJSONObject(2).getString("TotalPackingRate").toString();



                            Constants.store_totalcost_p = totlecost_pri;
                            Constants.store_totalcost_s = totlecost_stan;
                            Constants.store_totalcost_p_getquotepage = totlecost_pri;
                            Constants.store_totalcost_s_getquotepage = totlecost_stan;

                            Constants.store_NoOfTruck_s = NoOfTruck_s;
                            Constants.store_NoOfLabour_s = NoOfLabour_s;
                            Constants.store_NoOfHandiman_s = NoOfHandiman_s;
                            Constants.store_BaseRate_s = BaseRate_s;
                            Constants.store_TotalLabourRate_s = TotalLabourRate_s;
                            Constants.store_TotalHandimanRate_s = TotalHandimanRate_s;
                            Constants.store_TotalPackingRate_s = TotalPackingRate_s;
                            Constants.static_labourvalue_s = NoOfLabour_s;
                            Constants.static_handymanvalue_s = NoOfHandiman_s;
                            Constants.store_NoOfLabour_incdec_s = NoOfLabour_s;
                            Constants.store_NoOfHandiman_incdec_s = NoOfHandiman_s;

                            // Toast.makeText(getApplicationContext(),Constants.store_NoOfLabour_incdec_s,Toast.LENGTH_SHORT).show();

                            Constants.store_NoOfTruck_p = NoOfTruck_p;
                            Constants.store_NoOfLabour_p = NoOfLabour_p;
                            Constants.store_NoOfHandiman_p = NoOfHandiman_p;
                            Constants.store_BaseRate_p = BaseRate_p;
                            Constants.store_TotalLabourRate_p = TotalLabourRate_p;
                            Constants.store_TotalHandimanRate_p = TotalHandimanRate_p;
                            Constants.store_TotalPackingRate_p = TotalPackingRate_p;

                            Constants.store_NoOfTruck_saving = NoOfTruck_saving;
                            Constants.store_NoOfLabour_saving = NoOfLabour_saving;
                            Constants.store_NoOfHandiman_saving = NoOfHandiman_saving;
                            Constants.store_BaseRate_saving = BaseRate_saving;
                            Constants.store_TotalLabourRate_saving = TotalLabourRate_saving;
                            Constants.store_TotalHandimanRate_saving = TotalHandimanRate_saving;
                            Constants.store_TotalPackingRate_saving = TotalPackingRate_saving;
                            Constants.static_labourvalue_saving = NoOfLabour_saving;
                            Constants.static_handymanvalue_saving = NoOfHandiman_saving;

                            Constants.store_NoOfLabour_incdec_saving = NoOfLabour_saving;
                            Constants.store_NoOfHandiman_incdec_saving = NoOfHandiman_saving;

                            Constants.store_totalcost_saving = totlecost_saving;
                            Constants.store_totalcost_saving_gequotepage = totlecost_saving;

                            Constants.store_totalcost_p_getquotepage = totlecost_pri;
                            Constants.store_totalcost_s_getquotepage = totlecost_stan;
                            Constants.store_totalcost_saving_gequotepage = totlecost_saving;
                            Constants.store_totalcost_p = totlecost_pri;
                            Constants.store_totalcost_s= totlecost_stan;
                            Constants.store_totalcost_saving = totlecost_saving;

                            String string_s_cost= "AED "+Constants.store_totalcost_s_getquotepage;
                            SpannableString string_s_cost1=  new SpannableString(string_s_cost);
                            string_s_cost1.setSpan(new RelativeSizeSpan(0.7f), 0,3, 0); // set size

                            String string_c_cost= "AED "+Constants.store_totalcost_p_getquotepage;
                            SpannableString string_c_cost1=  new SpannableString(string_c_cost);
                            string_c_cost1.setSpan(new RelativeSizeSpan(0.7f), 0, 3, 0); // set size

                            s_cost.setText(string_s_cost1);
                            p_cost.setText(string_c_cost1);

                            supersaving.setText("Go for Super Saver and move for AED " + Constants.store_totalcost_saving_gequotepage);

                            ObjectAnimator textColorAnim;
                            textColorAnim = ObjectAnimator.ofInt(supersaving, "textColor", Color.RED, Color.rgb(34,139,34));
                            textColorAnim.setDuration(1000);
                            textColorAnim.setEvaluator(new ArgbEvaluator());
                            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
                            textColorAnim.start();

                            //sending mail afer geting all data
                            if(Constants.storemail_send.equalsIgnoreCase("0"))
                            {
                                if (cd.isConnectingToInternet())
                                {
                                    new GetJson_save_mail().execute();
                                } else {
                                    UF.msg("Check your internet connection.");
                                }

                            }


                        }
                        else
                        {
                            UF.msg(message + "");
                        }
                    } catch (JSONException e)
                    {

                        e.printStackTrace();
                    }
                }



        }
    }

    private class GetJsonmovemyhome_save_next extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GetQuote.this);
            loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);

        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            try {
                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("Area", Constants.store_area);
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");
                prmsLogin.put("NoOfTruck", "");
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", "");
                prmsLogin.put("NoOfHandiman", "");

                if (sm.isLoggedIn()) {
                    prmsLogin.put("shipper_id", sm.getUniqueId());
                } else {
                    prmsLogin.put("shipper_id", "");
                }
                if (sm.isLoggedIn()) {
                    prmsLogin.put("load_inquiry_no",  Constants.store_loadinquiry_no);
                } else {
                    // prmsLogin.put("load_inquiry_no", posting_shippingid);
                }

                Log.e("heenali Lkahni jay", "dddd");
                prmsLogin.put("email_id", Constants.store_email);

                prmsLogin.put("required_price", "0");
                prmsLogin.put("inquiry_source_addr", Constants.store_movefrom);
                prmsLogin.put("inquiry_source_city", Constants.postload_source_city);
                prmsLogin.put("inquiry_source_lat", Constants.postloadsourceLat);
                prmsLogin.put("inquiry_source_lng", Constants.postloadsourceLong);
                prmsLogin.put("source_pincode", "");
                prmsLogin.put("load_inquiry_shipping_date", Constants.store_date);
                prmsLogin.put("load_inquiry_shipping_time", Constants.store_time);
                prmsLogin.put("inquiry_destination_addr", Constants.store_moveto);
                prmsLogin.put("inquiry_destination_city", Constants.postload_dest_city);
                prmsLogin.put("inquiry_destionation_lat", Constants.postloaddestLat);
                prmsLogin.put("inquiry_destionation_lng", Constants.postloaddestLong);
                prmsLogin.put("destination_pincode", "");
                prmsLogin.put("remarks", "");
                prmsLogin.put("payment_mode", "");
                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("Isfinalorder", "N");
                if(selectedIntent.equalsIgnoreCase("S"))
                {
                    prmsLogin.put("rate_type_flag", "B");

                }
                else if(selectedIntent.equalsIgnoreCase("P"))
                {
                    prmsLogin.put("rate_type_flag", "P");
                }
                else
                {
                    prmsLogin.put("rate_type_flag", "S");

                }



                prmsLogin.put("IncludePackingCharge", "Y");

                prmsLogin.put("Isupdate", "Y");

                prmsLogin.put("order_type_flag", "H");
                prmsLogin.put("TruckTypeCode", "");
                prmsLogin.put("goods_type_flag", "Y");
                prmsLogin.put("promocode", "");
                prmsLogin.put("Isupdatebillingadd", "N");
                prmsLogin.put("IncludeAddonService", Constants.IncludeAddonService);
                prmsLogin.put("AddonServices", Constants.AddonServices);


                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin lakhani", prms + "");
            json_save = UF.RegisterUser("postorder/SaveMovingHomeDetails", prms);

            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();


            if (json_save.equals("lost")) {
                UF.msg("Connection Problem.");
            } else {
                if (json_save.equalsIgnoreCase("0")) {
                    UF.msg("Invalid");
                } else {
                    try {
                        JSONObject jobj = new JSONObject(json_save);

                        Log.e("getquotealldata ", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1"))
                        {

                            if(selectedIntent.equalsIgnoreCase("S"))
                            {
                                Constants.selectedIntent_getquote=selectedIntent;
                                Intent i = new Intent(Activity_GetQuote.this, Activity_GetQuote_standard.class);

                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();

                            }
                            else if(selectedIntent.equalsIgnoreCase("SAVINGS"))
                            {
                                Constants.selectedIntent_getquote=selectedIntent;
                                Intent i = new Intent(Activity_GetQuote.this, Activity_GetQuote_standard.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                            }
                            else
                            {
                                Constants.selectedIntent_getquote=selectedIntent;
                                Intent i = new Intent(Activity_GetQuote.this,Activity_GetQuote_premium.class);
                                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();


                            }


                        }
                        else
                        {
                            UF.msg(message + "");
                        }
                    } catch (JSONException e)
                    {

                        e.printStackTrace();
                    }
                }
            }


        }
    }
}