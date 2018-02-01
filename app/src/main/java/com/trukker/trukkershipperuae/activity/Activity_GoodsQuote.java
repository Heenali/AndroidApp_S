package com.trukker.trukkershipperuae.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

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
import java.util.Date;

public class Activity_GoodsQuote extends AppCompatActivity
{
    String android_id;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    TextView  total_value;
    String json_save;
    String final_order="";
    int change_labour,change_handyman;
    TextView basequote_edittext,nolabour_edittext,nolabour_value,unloadingmin_editext, loadingmin_editext,tc_title,nohandyman_edittext,nohandyman_value;
    Button nolabour_decrese,nolabour_increse,submit,nohandyman_decrese,nohandyman_increse;
    ToggleButton tc_condition;
    String static_labourvalue;
    String tc_value="N";
    TextView back;
    TextView applay_btn,discount,total_value_promp;
    EditText promocode_value;
    LinearLayout total_layout,total_layout_promp;
    Button package_information;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Constants.counter=1;
            Intent i = new Intent(Activity_GoodsQuote.this,Activity_MoveMyGoods.class);
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
        setContentView(R.layout.activity_goodsquote);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Constants.panding=0;
        init();

        if(Constants.storemail_send_goods.equalsIgnoreCase("0"))
        {
            if (cd.isConnectingToInternet())
            {
                new GetJson_save_mail().execute();
            }
            else
            {
                UF.msg("Check your internet connection.");
            }

        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.counter=1;
                Intent i = new Intent(Activity_GoodsQuote.this,Activity_MoveMyGoods.class);
                 i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();

            }
        });
        package_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CustomDialog_packageinfo packinfo = new CustomDialog_packageinfo(Activity_GoodsQuote.this);
                packinfo.show();


            }
        });
        static_labourvalue=Constants.store_TotalLabourRate_g;
        String htmlText ="<font color='#DEDEDE'>I accept </font><font color='#70B4FF'> Terms and Conditions</font>";
        tc_title.setText(Html.fromHtml(htmlText));

        basequote_edittext.setText(Constants.store_BaseRate_g+" AED");
        loadingmin_editext.setText(Constants.TimeForLoadingInMinute_g);
        unloadingmin_editext.setText(Constants.TimeForUnloadingInMinute_g);
        nolabour_edittext.setText(Constants.store_NoOfLabour_g);
        nolabour_value.setText("AED "+Constants.store_TotalLabourRate_g);

        total_value.setText("AED "+Constants.totlecost_g);
        nohandyman_edittext.setText(Constants.store_NoOfHandiman_g);
        nohandyman_value.setText("AED "+Constants.store_TotalHandimanRate_g);
        change_labour=Integer.parseInt(Constants.store_NoOfLabour_g);
        change_handyman=Integer.parseInt(Constants.store_NoOfHandiman_g);
        tc_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "sssss", Toast.LENGTH_SHORT).show();
                // Intent i = new Intent(Activity_GetQuote_premium.this,Activity_PDFfileopen.class);
                // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // startActivity(i);
                // finish();
                Log.e("URL OF PPURL",UserFunctions.URL+Constants.ppurl);
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.ppurl));
                startActivity(browserIntent);


            }
        });
        tc_condition.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (tc_condition.isChecked()) {
                    tc_value = "Y";
                } else {
                    tc_value = "N";
                }
            }
        });
        applay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (cd.isConnectingToInternet()) {
                    try {
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(applay_btn.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);

                        new GetJsonpromocode_save().execute();
                    } catch (Exception e) {

                    }

                } else {
                    UF.msg("Check Your Internet Connection.");
                }
            }
        });
        nolabour_decrese.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                total_layout_promp.setVisibility(View.GONE);
                total_layout.setVisibility(View.VISIBLE);
                if (cd.isConnectingToInternet())
                {
                    if (Integer.parseInt(Constants.store_TotalLabourRate_g_incdec) < Integer.parseInt(nolabour_edittext.getText().toString())) {

                        Constants.promocode_discount_value_store="";
                        Constants.promocode_store="";
                        discount.setText("");
                        promocode_value.setText("");
                        change_labour = Integer.parseInt(nolabour_edittext.getText().toString()) - 1;
                        new GetJsonmovemyhome_save().execute();
                    }
                    else
                    {
                        UF.msg("Number Can Not Be Decreased");
                    }


                } else {
                    UF.msg("Check Your Internet Connection.");
                }

            }
        });
        nohandyman_decrese.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                total_layout_promp.setVisibility(View.GONE);
                total_layout.setVisibility(View.VISIBLE);
                if (cd.isConnectingToInternet()) {
                    if (Integer.parseInt(Constants.store_TotalHandimanRate_g_incdec) < Integer.parseInt(nohandyman_edittext.getText().toString())) {

                        Constants.promocode_discount_value_store="";
                        Constants.promocode_store="";
                        discount.setText("");
                        promocode_value.setText("");
                        change_handyman = Integer.parseInt(nohandyman_edittext.getText().toString()) - 1;
                        new GetJsonmovemyhome_save().execute();
                    }
                    else
                    {
                        UF.msg("Number Can Not Be Decreased");
                    }


                } else {
                    UF.msg("Check Your Internet Connection.");
                }

            }
        });
        nolabour_increse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_layout_promp.setVisibility(View.GONE);
                total_layout.setVisibility(View.VISIBLE);
                if (cd.isConnectingToInternet())
                {
                    Constants.promocode_discount_value_store="";
                    Constants.promocode_store="";
                    discount.setText("");
                    promocode_value.setText("");
                    change_labour=Integer.parseInt(nolabour_edittext.getText().toString())+1;
                    // Toast.makeText(getApplicationContext(), nolabour_edittext.getText().toString(), Toast.LENGTH_SHORT).show();

                    new GetJsonmovemyhome_save().execute();
                }
                else
                {
                    UF.msg("Check Your Internet Connection.");
                }

            }
        });
        nohandyman_increse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                total_layout_promp.setVisibility(View.GONE);
                total_layout.setVisibility(View.VISIBLE);
                if (cd.isConnectingToInternet())
                {
                    Constants.promocode_discount_value_store="";
                    Constants.promocode_store="";
                    discount.setText("");
                    promocode_value.setText("");
                    change_handyman=Integer.parseInt(nohandyman_edittext.getText().toString())+1;
                    // Toast.makeText(getApplicationContext(), nolabour_edittext.getText().toString(), Toast.LENGTH_SHORT).show();

                    new GetJsonmovemyhome_save().execute();
                }
                else
                {
                    UF.msg("Check Your Internet Connection.");
                }

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tc_value.equalsIgnoreCase("N"))
                {
                    UF.msg("Please Accept Terms and Conditions");
                }
                else {
                    Constants.counter=2;
                    if (cd.isConnectingToInternet()) {
                        final_order = "Y";
                        new GetJsonmovemyhome_save().execute();
                    } else {
                        UF.msg("Check Your Internet Connection.");
                    }
                }

            }
        });


    }
    public void init()
    {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_GoodsQuote.this);
        UF = new UserFunctions(Activity_GoodsQuote.this);
        cd = new ConnectionDetector(Activity_GoodsQuote.this);
        package_information=(Button)findViewById(R.id.package_information);
        back=(TextView)findViewById(R.id.back);
        discount=(TextView)findViewById(R.id.discount);
        applay_btn=(TextView)findViewById(R.id.applay_btn);
        promocode_value=(EditText)findViewById(R.id.promocode_value);
        total_layout=(LinearLayout)findViewById(R.id.total_layout);
        total_layout_promp=(LinearLayout)findViewById(R.id.total_layout_promp);
        total_value_promp=(TextView)findViewById(R.id.total_value_promp);
        total_layout_promp.setVisibility(View.GONE);
        basequote_edittext=(TextView)findViewById(R.id.basequote_edittext);
        nolabour_edittext=(TextView)findViewById(R.id.nolabour_edittext);
        nolabour_value=(TextView)findViewById(R.id.nolabour_value);
        nolabour_decrese=(Button)findViewById(R.id.nolabour_decrese);
        nolabour_increse=(Button)findViewById(R.id.nolabour_increse);
        unloadingmin_editext=(TextView)findViewById(R.id.unloadingmin_editext);
        loadingmin_editext=(TextView)findViewById(R.id.loadingmin_editext);
        tc_title=(TextView)findViewById(R.id.tc_title);
        tc_condition=(ToggleButton)findViewById(R.id.tc_condition);
        submit=(Button)findViewById(R.id.submit);
        total_value=(TextView)findViewById(R.id.total_value);

        nohandyman_decrese=(Button)findViewById(R.id.nohandyman_decrese);
        nohandyman_increse=(Button)findViewById(R.id.nohandyman_increse);
        nohandyman_edittext=(TextView)findViewById(R.id.nohandyman_edittext);
        nohandyman_value=(TextView)findViewById(R.id.nohandyman_value);
    }
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GoodsQuote.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
        }

        @Override
        protected String doInBackground(Void... params)
        {
            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            try
            {

                String total=total_value.getText().toString().substring(3);
                String total_promo=total_value_promp.getText().toString().substring(3);
                String helpervalue=nolabour_value.getText().toString().substring(3);
                String noofhelper=String.valueOf(change_labour);
                String installer=String.valueOf(change_handyman);
                int requiredproce=0;
try
{
    requiredproce=Integer.parseInt(Constants.promocode_discountstore)+Integer.parseInt(Constants.promocode_discount_value_store);

}
catch (Exception e)
{

}


                Log.e("ddddddd",total);
                Log.e("helpervalue",helpervalue);
                Log.e("noofhelper", noofhelper);
                Log.e("installer", installer);
                Log.e("requiredproce", String.valueOf(requiredproce));


                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("Area", "0");
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");
                prmsLogin.put("NoOfTruck", "");
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfHandiman", installer);
                prmsLogin.put("NoOfLabour", noofhelper);

                prmsLogin.put("shipper_id", Constants.store_shipperid);
                prmsLogin.put("email_id", Constants.store_email);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);

                prmsLogin.put("inquiry_source_addr", Constants.store_movefrom);
                prmsLogin.put("inquiry_source_city", Constants.postload_source_city);
                prmsLogin.put("inquiry_source_lat", Constants.postloadsourceLat);
                prmsLogin.put("inquiry_source_lng",Constants.postloadsourceLong);
                prmsLogin.put("source_pincode", "");
                prmsLogin.put("load_inquiry_shipping_date", Constants.store_date);
                prmsLogin.put("load_inquiry_shipping_time", Constants.store_time);
                prmsLogin.put("inquiry_destination_addr", Constants.store_moveto);
                prmsLogin.put("inquiry_destination_city", Constants.postload_dest_city);
                prmsLogin.put("inquiry_destionation_lat", Constants.postloaddestLat);
                prmsLogin.put("inquiry_destionation_lng", Constants.postloaddestLong);
                prmsLogin.put("destination_pincode", "");
                prmsLogin.put("remarks", "");
                prmsLogin.put("payment_mode", "O");
                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "shipper");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("Isfinalorder", "N");

                prmsLogin.put("rate_type_flag", "B");
                prmsLogin.put("required_price", String.valueOf(requiredproce));
                prmsLogin.put("IncludePackingCharge", "N");
                prmsLogin.put("Isupdate", "Y");

                prmsLogin.put("TruckTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("goods_type_flag", "Y");
                prmsLogin.put("promocode", "");

                prmsLogin.put("goods_details", "");
                prmsLogin.put("order_type_flag", "GL");
                prmsLogin.put("payment_status", "");
                prmsLogin.put("Isupdatebillingadd", "N");
                prmsLogin.put("promocode", Constants.promocode_store);
                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);
                Log.e("prmsLogin", prms + "");
                json_save = UF.RegisterUser("postorder/SaveMovingGoodsDetails", prms);

            } catch (JSONException e)
            {

                e.printStackTrace();
            }


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

                        Log.e("standard get normal", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1"))
                        {

                            applay_btn.setEnabled(true);
                            applay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.promocode_select));
                            applay_btn.setTextColor(Color.parseColor("#3675C5"));

                            JSONArray array=new JSONArray();
                            array=jobj.getJSONArray("message");
                            Log.e("array",array.toString());
                            Log.e("array0",array.getJSONObject(0).getString("Total_cost").toString());

                            String NoOfTruck_s= array.getJSONObject(0).getString("NoOfTruck").toString();
                            String NoOfLabour_s= array.getJSONObject(0).getString("NoOfLabour").toString();
                            String NoOfHandiman_s= array.getJSONObject(0).getString("NoOfHandiman").toString();
                            String BaseRate_s= array.getJSONObject(0).getString("BaseRate").toString();
                            String TotalLabourRate_s= array.getJSONObject(0).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_s= array.getJSONObject(0).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_s= array.getJSONObject(0).getString("TotalPackingRate").toString();
                            String totlecost_stan=array.getJSONObject(0).getString("Total_cost").toString();
                            String billing_name=array.getJSONObject(0).getString("billing_name").toString();
                           String billing_add=array.getJSONObject(0).getString("billing_add").toString();
                           String source_full_add=array.getJSONObject(0).getString("source_full_add").toString();
                            String destination_full_add=array.getJSONObject(0).getString("destination_full_add").toString();

                            nolabour_edittext.setText(NoOfLabour_s);
                            basequote_edittext.setText(BaseRate_s +" AED");
                            nolabour_value.setText("AED "+TotalLabourRate_s);
                            total_value.setText("AED "+totlecost_stan);
                            nohandyman_edittext.setText(NoOfHandiman_s);
                            nohandyman_value.setText("AED "+TotalHandimanRate_s);

                            Log.e("NoOfHandiman_s", NoOfHandiman_s);
                            Log.e("TotalHandimanRate_s",TotalHandimanRate_s);
                            Log.e("NoOfLabour_s",NoOfLabour_s);
                            Log.e("TotalLabourRate_s",TotalLabourRate_s);

                            if(final_order.equalsIgnoreCase("Y"))
                            {


                                Constants.totlecost_g = totlecost_stan;
                                Constants.store_NoOfTruck_g = NoOfTruck_s;
                                Constants.store_NoOfLabour_g = NoOfLabour_s;
                                Constants.store_NoOfHandiman_g = NoOfHandiman_s;
                                Constants.store_BaseRate_g = BaseRate_s;
                                Constants.store_TotalLabourRate_g = TotalLabourRate_s;
                                Constants.store_TotalHandimanRate_g = TotalHandimanRate_s;
                                Constants.store_TotalPackingRate_g = TotalPackingRate_s;
                                Constants.store_billing_name = billing_name;
                                Constants.store_billing_add = billing_add;
                                Constants.store_source_full_add = source_full_add;
                               Constants.store_destination_full_add = destination_full_add;


                                Intent i = new Intent(Activity_GoodsQuote.this,Activity_Goods_detail.class);
                                i.putExtra("page", "standard");
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();

                            }


                        } else {
                            UF.msg(message + "");
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }


        }
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //clear all data
        if(Constants.counter>=2)
        {
            tc_value="Y";
            tc_condition.setChecked(true);
        }
        else
        {
            tc_value="N";
            tc_condition.setChecked(false);
        }
        if(Constants.promocode_store.equalsIgnoreCase(""))
        {
        }
        else
        {

            tc_value="Y";
            tc_condition.setChecked(true);
            Log.e("promocode_store",Constants.promocode_store);
            Log.e("promocode_discountstore",Constants.promocode_discountstore);
            Log.e("discount_value_store", Constants.promocode_discount_value_store);

            promocode_value.setText(Constants.promocode_store);
            total_layout.setVisibility(View.GONE);
            total_layout_promp.setVisibility(View.VISIBLE);
            total_value_promp.setText("AED "+String.valueOf(Constants.promocode_discountstore));
            discount.setText("-" + Constants.promocode_discount_value_store);
            applay_btn.setEnabled(false);
            applay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.promocode_blank));
            applay_btn.setTextColor(Color.parseColor("#ffffff"));

        }




    }
    private class GetJson_save_mail extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GoodsQuote.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
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

                String inputPatternt = "h:mm";
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
                    Log.e("Date",Constants.store_date);
                    Log.e("Time",Constants.store_time);
                    date = inputFormatd.parse(Constants.store_date);
                    date1 = inputFormatt.parse(Constants.store_time);

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
                prmsLogin.put("Sizetypecode",Constants.store_Hometypename);
                prmsLogin.put("Sourceaddress", Constants.store_movefrom);
                prmsLogin.put("Destinationaddress",Constants.store_moveto);
                prmsLogin.put("ShippingDatetime", strd+" , "+strt);
                prmsLogin.put("load_inquiry_no",Constants.store_loadinquiry_no);
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM",Constants.store_unit);


                jsonArray.put(prmsLogin);

                prms.put("Email", jsonArray);

            } catch (JSONException e)
            {

                e.printStackTrace();
            }

            json_save = UF.RegisterUser("mailer/SaveQuotationRequestMail", prms);
            Log.e("emailsend data......>", prms.toString());

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

                            Constants.storemail_send_goods="1";


                        } else {
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
    private class GetJsonpromocode_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GoodsQuote.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);

        }

        @Override
        protected String doInBackground(Void... params)
        {
            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            try
            {
                String stotal="";
                stotal=total_value.getText().toString();
                if(stotal.equalsIgnoreCase(""))
                {

                }
                else
                {
                    stotal=stotal.substring(4);
                }

                prmsLogin.put("coupon_code", promocode_value.getText().toString());
                prmsLogin.put("shipper_id", sm.getUniqueId());
                prmsLogin.put("Total_cost",stotal);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);

                jsonArray.put(prmsLogin);

                prms.put("Promocode", jsonArray);
                Log.e("prmsLogin", prms + "");
                json_save = UF.RegisterUser("Postorder/ValidatePromocode", prms);

            } catch (JSONException e)
            {

                e.printStackTrace();
            }


            return json_save;
        }

        @Override
        protected void onPostExecute(String result)
        {
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

                        Log.e("promocode value display", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1"))
                        {

                            JSONArray array=new JSONArray();
                            array=jobj.getJSONArray("message");
                            Log.e("array",array.toString());


                            String discount_store=array.getJSONObject(0).getString("Discount_value").toString();
                            String Total_cost=array.getJSONObject(0).getString("Total_cost").toString();
                            String promocode=array.getJSONObject(0).getString("coupon_code").toString();

                            Constants.promocode_store=promocode;
                            total_layout_promp.setVisibility(View.VISIBLE);
                            total_layout.setVisibility(View.GONE);
                            discount.setText("-"+discount_store);
                            int cost=Integer.parseInt(Total_cost)-Integer.parseInt(discount_store);
                            Constants.promocode_discountstore=String.valueOf(cost);
                            Constants.promocode_discount_value_store=String.valueOf(discount_store);
                            total_value_promp.setText("AED "+String.valueOf(cost));
                            promocode_value.setText(Constants.promocode_store);

                            applay_btn.setEnabled(false);
                            applay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.promocode_blank));
                            applay_btn.setTextColor(Color.parseColor("#ffffff"));


                        } else {
                            UF.msg(message + "");
                        }
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }
                }
            }


        }
    }
    class CustomDialog_packageinfo extends Dialog
    {

        public Context context;
        public Dialog d;

        private ProgressDialog  loading;
        LinearLayout promocode_layout;
        LinearLayout container;
        public CustomDialog_packageinfo(Context a)
        {
            super(a);
            this.context = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_packageinfo);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            context=this.getContext();
            container = (LinearLayout)findViewById(R.id.container);
            promocode_layout= (LinearLayout)findViewById(R.id.promocode_layout);
            promocode_layout.setVisibility(View.GONE);
            try
            {

              /*  loading = new ProgressDialog(Activity_GoodsQuote.this);
                loading.getWindow().setBackgroundDrawable(new

                        ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setIndeterminate(true);
                loading.setCancelable(false);
                loading.show();
                loading.setContentView(R.layout.my_progress);
                String s="N";*/


               // String url = UserFunctions.URL + "shipper/GetPackingMaterialDetailsBySizeType?sizetype="+Constants.store_Hometypeid+"&ratetypeflag="+s;
               // SyncMethod(url);

                for(int aa=0;aa<2;aa++)
                {

                    LayoutInflater layoutInflater =
                            (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    final View addView = layoutInflater.inflate(R.layout.list_packageinfo1, null);
                    TextView materiale = (TextView)addView.findViewById(R.id.material);
                    TextView  quality= (TextView)addView.findViewById(R.id.size);


                    if(aa==0)
                    {
                        materiale.setText("Time for Loading");
                        quality.setText(Constants.TimeForLoadingInMinute_g);

                    }
                  else if(aa==1)
                    {
                        materiale.setText("Time for UnLoading");
                        quality.setText(Constants.TimeForUnloadingInMinute_g);

                    }


                    container.addView(addView);
                }



            }
            catch (Exception e)
            {

            }



        }


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