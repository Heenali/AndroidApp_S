package com.trukker.trukkershipperuae.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.List;

public class Activity_GetQuote_premium extends AppCompatActivity
{

    String addonsvalue_price="";
    String static_labourvalue;
    String static_handymanvalue;
    String tc_value="N";
    String android_id;
    String json_save;
    UserFunctions UF;
    LinearLayout  noofdays_layout;
    LinearLayout package_layout;
    TextView  package_information_line;
    SessionManager sm;
    ConnectionDetector cd;
    EditText promocode_value;
    TextView total_value;
    TextView applay_btn;
    String promovalue="";
    TextView basequote_edittext;
    int change_handyman,change_labour;
    TextView nolabour_value,nohandyman_value,nopacking_value;
    TextView nooftruck_edittext,nolabour_edittext,nohandyman_edittext;
    Button submit;
    ToggleButton tc_condition;
    TextView tc_title;
    ToggleButton nopacking_sucess;
    String packing_chcek="Y";
    String final_order="N";
    Button notruck_increse_btn,notruck_decrese_btn,nolabour_decrese_btn,nolabour_increse_btn,nohandyman_increse_btn,nohandyman_decrese_btn;
    TextView discount,total_value_promp;
    LinearLayout  total_layout,total_layout_promp;
    Button package_information;
    TextView back;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent i = new Intent(Activity_GetQuote_premium.this,Activity_GetQuote.class);
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
        setContentView(R.layout.activity_getquote_standard);
        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Activity_GetQuote_premium.this, Activity_GetQuote.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();

            }
        });
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Intent i = getIntent();
        static_handymanvalue = i.getStringExtra("static_handymanvalue");
        static_labourvalue = i.getStringExtra("static_labourvalue");


        package_information.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                CustomDialog_packageinfo packinfo = new CustomDialog_packageinfo(Activity_GetQuote_premium.this);
                packinfo.show();


            }
        });
        applay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                promovalue = promocode_value.getText().toString();
                if (cd.isConnectingToInternet()) {
                    try
                    {
                        new GetJsonpromocode_save().execute();
                        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(applay_btn.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);


                    } catch (Exception e) {

                    }

                } else {
                    UF.msg("Check Your Internet Connection.");
                }
            }
        });
        nopacking_sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (nopacking_sucess.isChecked()) {
                    packing_chcek = "Y";

                    if (cd.isConnectingToInternet()) {
                        try {
                            total_layout_promp.setVisibility(View.GONE);
                            total_layout.setVisibility(View.VISIBLE);
                            discount.setText("");
                            new GetJsonmovemyhome_save().execute();
                        } catch (Exception e) {

                        }

                    } else {
                        UF.msg("Check Your Internet Connection.");
                    }


                } else {
                    packing_chcek = "N";
                    if (cd.isConnectingToInternet()) {
                        new GetJsonmovemyhome_save().execute();
                    } else {
                        UF.msg("Check Your Internet Connection.");
                    }
                }
            }
        });

        tc_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(), "sssss", Toast.LENGTH_SHORT).show();
                // Intent i = new Intent(Activity_GetQuote_premium.this,Activity_PDFfileopen.class);
                // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // startActivity(i);
                // finish();
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.ppurl));
                startActivity(browserIntent);

            }
        });
        tc_condition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tc_condition.isChecked()) {
                    tc_value = "Y";
                } else {
                    tc_value = "N";
                }
            }
        });


            nooftruck_edittext.setText(Constants.store_NoOfTruck_p);
            nolabour_edittext.setText(Constants.store_NoOfLabour_p);
            nohandyman_edittext.setText(Constants.store_NoOfHandiman_p);

            String basequotevalue=Constants.store_totalcost_p+"<font size=\"8\"> AED"+ "</font>";
            basequote_edittext.setText(Html.fromHtml(basequotevalue));

            nolabour_value.setText("AED "+Constants.store_TotalLabourRate_p);
            nohandyman_value.setText("AED "+Constants.store_TotalHandimanRate_p);
            nopacking_value.setText("AED "+Constants.store_TotalPackingRate_p);
            total_value.setText("AED "+Constants.store_totalcost_p);




        change_labour=Integer.parseInt(nolabour_edittext.getText().toString());
        change_handyman=Integer.parseInt(nohandyman_edittext.getText().toString());

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                if(tc_value.equalsIgnoreCase("N"))
                {
                    UF.msg("Please Accept Terms and Conditions");
                }
                else
                {
                    if (cd.isConnectingToInternet())
                    {

                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(Activity_GetQuote_premium.this);
                        builder.setMessage("Are you sure want to generate Order?");
                        builder.setTitle("New Order");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                final_order="Y";
                                try
                                {
                                    Constants.counter=3;
                                    new GetJsonmovemyhome_save().execute();
                                }
                                catch (Exception e)
                                {

                                }


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
                    else
                    {
                        UF.msg("Check Your Internet Connection.");
                    }
                }


            }
        });

    }

    public void init()
    {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm=new SessionManager(Activity_GetQuote_premium.this);
        UF=new UserFunctions(Activity_GetQuote_premium.this);
        cd= new ConnectionDetector(Activity_GetQuote_premium.this);
        basequote_edittext=(TextView)findViewById(R.id.basequote_edittext);
        submit=(Button)findViewById(R.id.submit);


        LinearLayout servicedis_layout=(LinearLayout)findViewById(R.id.servicedis_layout);
        TextView servicedis_text=(TextView) findViewById(R.id.servicedis_textd);
        TextView addons_title=(TextView)findViewById(R.id.nameof_addons);
        TextView addons_price=(TextView)findViewById(R.id.price_addons);
        if(Constants.counter_addon==1)
        {

            if(Constants.store_combodis.equalsIgnoreCase("-AED 0") || Constants.store_combodis.equalsIgnoreCase("-AED 00") ||Constants.IncludeAddonService.equalsIgnoreCase("N"))
            {
                servicedis_layout.setVisibility(View.GONE);
            }
            else
            {
                servicedis_text.setText(Constants.store_combodis);
                servicedis_layout.setVisibility(View.VISIBLE);

                int cou=0;

                for(int i=0;i< Constants.selected_Home_price.size();i++)
                {

                    if(Constants.selected_Home_price.get(i).equalsIgnoreCase("*")&&Constants.selected_Home_name.get(i).equalsIgnoreCase("*"))
                    {

                    }
                    else
                    {
                        String sum= Constants.selected_Home_price.get(i);

                        if(i==0)
                        {

                            cou= cou=Integer.parseInt(sum);
                        }

                        else
                        {
                            cou=cou+(cou=Integer.parseInt(sum));

                        }
                    }



                }
                String name=convert_atos(Constants.selected_Home_name.toString());
                addons_title.setText(name);

                addons_price.setText("AED "+String.valueOf(cou));
            }


        }
        else
        {
            servicedis_layout.setVisibility(View.GONE);
        }



        noofdays_layout=(LinearLayout)findViewById(R.id.noofdays_layout);
        noofdays_layout.setVisibility(View.GONE);
        notruck_increse_btn = (Button)findViewById(R.id.notruck_increse);
        notruck_decrese_btn = (Button)findViewById(R.id.notruck_decrese);

        nolabour_decrese_btn = (Button)findViewById(R.id.nolabour_decrese);
        nolabour_increse_btn = (Button)findViewById(R.id.nolabour_increse);

        back = (TextView)findViewById(R.id.back);
        nohandyman_decrese_btn = (Button)findViewById(R.id.nohandyman_decrese);
        nohandyman_increse_btn = (Button)findViewById(R.id.nohandyman_increse);
        nopacking_sucess= (ToggleButton) findViewById(R.id.nopacking_sucess);
        nooftruck_edittext=(TextView)findViewById(R.id.nooftruck_edittext);
        nolabour_edittext=(TextView)findViewById(R.id.nolabour_edittext);
        nohandyman_edittext=(TextView)findViewById(R.id.nohandyman_edittext);
        package_information=(Button)findViewById(R.id.package_information);
        package_information.setVisibility(View.GONE);
        nolabour_value=(TextView)findViewById(R.id.nolabour_value);
        nolabour_value.setVisibility(View.GONE);

        nohandyman_value=(TextView)findViewById(R.id.nohandyman_value);
        nohandyman_value.setVisibility(View.GONE);

        nopacking_value=(TextView)findViewById(R.id.nopacking_value);
        package_layout=(LinearLayout)findViewById(R.id.package_layout);
        package_information_line=(TextView)findViewById(R.id.package_information_line);
        package_information_line.setVisibility(View.GONE);
        package_layout.setVisibility(View.GONE);
        total_value=(TextView)findViewById(R.id.total_value);
        tc_title=(TextView)findViewById(R.id.tc_title);
        promocode_value=(EditText)findViewById(R.id.promocode_value);
        applay_btn=(TextView)findViewById(R.id.applay_btn);
        tc_condition=(ToggleButton)findViewById(R.id.tc_condition);

        total_layout=(LinearLayout)findViewById(R.id.total_layout);
        total_layout_promp=(LinearLayout)findViewById(R.id.total_layout_promp);
        total_value_promp=(TextView)findViewById(R.id.total_value_promp);
        discount=(TextView)findViewById(R.id.discount);

        total_layout_promp.setVisibility(View.GONE);

        String htmlText ="<font color='#DEDEDE'>I accept </font><font color='#70B4FF'> Terms and Conditions</font>";
        tc_title.setText(Html.fromHtml(htmlText));


    }
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog  loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GetQuote_premium.this);
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
                prmsLogin.put("NoOfTruck", Constants.store_NoOfTruck_p);
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", Constants.store_NoOfLabour_p);
                prmsLogin.put("NoOfHandiman", Constants.store_NoOfHandiman_p);
                prmsLogin.put("shipper_id", Constants.store_shipperid);
                prmsLogin.put("email_id", Constants.store_email);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);
                prmsLogin.put("required_price", requiredproce);
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
                prmsLogin.put("payment_mode", final_order);
                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("Isfinalorder", "N");
                prmsLogin.put("rate_type_flag", "P");
                prmsLogin.put("IncludePackingCharge", packing_chcek);
                prmsLogin.put("Isupdate", "Y");
                prmsLogin.put("order_type_flag", "H");
                prmsLogin.put("TruckTypeCode", "");
                prmsLogin.put("goods_type_flag", "Y");
                prmsLogin.put("promocode", Constants.promocode_store);
                prmsLogin.put("Isupdatebillingadd","N");
                prmsLogin.put("IncludeAddonService", Constants.IncludeAddonService);
                prmsLogin.put("AddonServices", Constants.AddonServices);

                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);
                Log.e("prmsLogin", prms + "");
                json_save = UF.RegisterUser("postorder/SaveMovingHomeDetails", prms);

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

                        Log.e("premium get normal", jobj.toString());
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

                            Constants.store_totalcost_p= totlecost_stan;
                            Constants.store_NoOfTruck_p=NoOfTruck_s;
                            Constants.store_NoOfLabour_p=NoOfLabour_s;
                            Constants.store_NoOfHandiman_p=NoOfHandiman_s;
                            Constants.store_BaseRate_p=BaseRate_s;
                            Constants.store_TotalLabourRate_p=TotalLabourRate_s;
                            Constants.store_TotalHandimanRate_p=TotalHandimanRate_s;
                            Constants.store_TotalPackingRate_p=TotalPackingRate_s;

                            Constants.store_billing_name = billing_name;
                            Constants.store_billing_add = billing_add;
                            Constants.store_source_full_add = source_full_add;
                            Constants.store_destination_full_add = destination_full_add;

                            nooftruck_edittext.setText(Constants.store_NoOfTruck_p);
                            nolabour_edittext.setText(Constants.store_NoOfLabour_p);
                            nohandyman_edittext.setText(Constants.store_NoOfHandiman_p);
                            basequote_edittext.setText(Constants.store_BaseRate_p +" AED");


                            nolabour_value.setText("AED "+Constants.store_TotalLabourRate_p);
                            nohandyman_value.setText("AED "+Constants.store_TotalHandimanRate_p);
                            nopacking_value.setText("AED "+Constants.store_TotalPackingRate_p);
                            total_value.setText("AED "+Constants.store_totalcost_p);

                            List<String> items = Arrays.asList(Constants.store_addonsvalue.split(","));
                            if(final_order.equalsIgnoreCase("Y"))
                            {

                                Log.e("NoOfLabour_s",Constants.store_NoOfLabour_p);
                                Log.e("NoOfHandiman_s",Constants.store_NoOfHandiman_p);
                                Log.e("store_BaseRate_s",Constants.store_BaseRate_p);
                                Log.e("store_totalcost_s",Constants.store_totalcost_p);


                                for(int b=0;b<items.size();b++)
                                {
                                    if(b==0)
                                    {
                                        addonsvalue_price=array.getJSONObject(0).getString(items.get(b).toString()).toString();
                                    }
                                    else
                                    {
                                        addonsvalue_price=addonsvalue_price+","+array.getJSONObject(0).getString(items.get(b).toString()).toString();
                                    }
                                }
                                Log.e("Addonsvalue price",addonsvalue_price);
                                Constants.store_addonsvalue_price=addonsvalue_price;


                                Intent i = new Intent(Activity_GetQuote_premium.this,Activity_MoveMyHome_Addons.class);
                                i.putExtra("page", "premium");
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
    private class GetJsonpromocode_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GetQuote_premium.this);
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

                prmsLogin.put("coupon_code", promocode_value.getText().toString());
                prmsLogin.put("shipper_id", sm.getUniqueId());
                prmsLogin.put("Total_cost", Constants.store_totalcost_p);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);

                jsonArray.put(prmsLogin);

                prms.put("Promocode", jsonArray);
                Log.e("prmsLogin", prms + "");
                json_save = UF.RegisterUser("Postorder/ValidatePromocode", prms);

            }
            catch (JSONException e)
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
                            Constants.promocode_discount_value_store=String.valueOf(discount_store);
                            Constants.promocode_store=promocode;
                            total_layout_promp.setVisibility(View.VISIBLE);
                            total_layout.setVisibility(View.GONE);

                            discount.setText("-" + discount_store);
                            int cost=Integer.parseInt(Total_cost)-Integer.parseInt(discount_store);
                            Constants.promocode_discountstore=String.valueOf(cost);
                            total_value_promp.setText(String.valueOf(cost));
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
            context=this.getContext();
            container = (LinearLayout)findViewById(R.id.container);


            try
            {

                loading = new ProgressDialog(Activity_GetQuote_premium.this);
                loading.getWindow().setBackgroundDrawable(new

                        ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setIndeterminate(true);
                loading.setCancelable(false);
                loading.show();
                loading.setContentView(R.layout.my_progress);
                if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                {
                    Constants.selectedIntent_getquote="N";

                }
                else if(Constants.selectedIntent_getquote.equalsIgnoreCase("SAVINGS"))
                {
                    Constants.selectedIntent_getquote="S";
                }else if(Constants.selectedIntent_getquote.equalsIgnoreCase("P"))
                {
                    Constants.selectedIntent_getquote="P";
                }

                String url = UserFunctions.URL + "shipper/GetPackingMaterialDetailsBySizeType?sizetype="+Constants.store_Hometypeid+"&ratetypeflag="+Constants.selectedIntent_getquote;
                SyncMethod(url);



            }
            catch (Exception e)
            {

            }



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

                            JSONArray array=new JSONArray();

                            array=get_res.getJSONArray("message");
                            Log.e("mess", "screen>>" + array.toString());
                            for(int aa=0;aa<array.length();aa++)
                            {

                                LayoutInflater layoutInflater =
                                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View addView = layoutInflater.inflate(R.layout.list_packageinfo, null);
                                TextView materiale = (TextView)addView.findViewById(R.id.material);
                                TextView  quality= (TextView)addView.findViewById(R.id.quantity);
                                TextView size = (TextView)addView.findViewById(R.id.size);

                                materiale.setText(array.getJSONObject(aa).getString("PackingMaterial_desc").toString());
                                quality.setText(array.getJSONObject(aa).getString("quantity").toString());
                                size.setText(array.getJSONObject(aa).getString("PackingSize").toString());



                                container.addView(addView);
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

    }
    public String fetchResult(String urlString) throws JSONException
    {
        StringBuilder builder;
        BufferedReader reader;
        URLConnection connection=null;
        URL url=null;
        String line;
        builder=new StringBuilder();
        reader=null;
        try {
            url=new URL(urlString);
            connection=url.openConnection();

            reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line=reader.readLine())!=null)
            {
                builder.append(line);
            }
            //Log.d("DATA", builder.toString());
        } catch (Exception e) {

        }
        //JSONArray arr=new JSONArray(builder.toString());
        return builder.toString();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //clear all data
        tc_value="N";
        packing_chcek="Y";
        tc_condition.setChecked(false);
        promocode_value.setText("");

        if(Constants.counter>=3)
        {
            defaultvalueset();
        }
        if(Constants.promocode_store.equalsIgnoreCase(""))
        {
        }
        else
        {
            tc_condition.setChecked(true);
            tc_value="Y";

            Log.e("promocode_store", Constants.promocode_store);
            Log.e("promocode_discountstore",Constants.promocode_discountstore);
            Log.e("discount_value_store", Constants.promocode_discount_value_store);

            total_layout.setVisibility(View.GONE);
            total_layout_promp.setVisibility(View.VISIBLE);
            total_value_promp.setText(String.valueOf(Constants.promocode_discountstore));
            discount.setText("-" + Constants.promocode_discount_value_store);
            promocode_value.setText(Constants.promocode_store);
            applay_btn.setEnabled(false);
            applay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.promocode_blank));
            applay_btn.setTextColor(Color.parseColor("#ffffff"));

        }


    }
    public void  defaultvalueset()
    {

        tc_condition.setChecked(true);
        tc_value="Y";

    }
    public String convert_atos(String f)
    {

        f=f.replace("*,", "").replace("*","").replace("[","").replace("]","");
        f=f.trim();
        if (f.length() > 0)
        {
            String c=f.substring(f.length() - 1);
            if(c.contains(","))
                f=f.substring(0, f.length() - 1);

        }

        f=f.replaceAll("\\s+","");
        return f;
    }

}