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
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.Arrays;
import java.util.List;

public class Activity_GetQuote_standard extends AppCompatActivity
{

    String addonsvalue_price="";
    String static_labourvalue;
    String static_handymanvalue;
    String tc_value="N";
    String android_id;
    String json_save;
    UserFunctions UF;
    SessionManager sm;
    LinearLayout  noofdays_layout;
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
    ImageView quote_icon;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
{
    if (keyCode == KeyEvent.KEYCODE_BACK)
    {
        Intent i = new Intent(Activity_GetQuote_standard.this,Activity_GetQuote.class);
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
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();

        if(Constants.selectedIntent_getquote.equalsIgnoreCase("SAVINGS"))
        {
           quote_icon.setBackgroundResource(R.drawable.quote_supersaving);
        }
        else
        {
            quote_icon.setBackgroundResource(R.drawable.quote_standard);
        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Activity_GetQuote_standard.this, Activity_GetQuote.class);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();

            }
        });

        if(Constants.packing_chcek_s.equalsIgnoreCase("Y"))
        {
            nopacking_sucess.setChecked(true);
        }
        else
        {
            nopacking_sucess.setChecked(false);
        }
        applay_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {



                    promovalue = promocode_value.getText().toString();
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
        package_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialog_packageinfo packinfo = new CustomDialog_packageinfo(Activity_GetQuote_standard.this);
                packinfo.show();


            }
        });
        nopacking_sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)

            {
                Constants.promocode_store="";
                Constants.promocode_discount_value_store="";
                discount.setText("");
                promocode_value.setText("");
                total_layout_promp.setVisibility(View.GONE);
                total_layout.setVisibility(View.VISIBLE);

                if (nopacking_sucess.isChecked()) {
                    packing_chcek = "Y";
                    Constants.packing_chcek_s="Y";
                    if (cd.isConnectingToInternet())
                    {
                        try {

                            new GetJsonmovemyhome_save().execute();
                        } catch (Exception e) {

                        }

                    } else {
                        UF.msg("Check Your Internet Connection.");
                    }


                } else {
                    packing_chcek = "N";
                    Constants.packing_chcek_s="N";
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

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.ppurl));
                startActivity(browserIntent);

            }
        });
        tc_condition.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if (tc_condition.isChecked())
                {
                    tc_value = "Y";
                } else {
                    tc_value = "N";
                }
            }
        });
     //   Toast.makeText(getApplicationContext(),Constants.selectedIntent_getquote,Toast.LENGTH_SHORT).show();
        if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
        {
            nooftruck_edittext.setText(Constants.store_NoOfTruck_s);
            nolabour_edittext.setText(Constants.store_NoOfLabour_s);
            nohandyman_edittext.setText(Constants.store_NoOfHandiman_s);

            String basequotevalue=Constants.store_BaseRate_s+"<font size=\"8\"> AED"+ "</font>";
            basequote_edittext.setText(Html.fromHtml(basequotevalue));

            nolabour_value.setText("AED "+Constants.store_TotalLabourRate_s);
            nohandyman_value.setText("AED "+Constants.store_TotalHandimanRate_s);
            nopacking_value.setText("AED "+Constants.store_TotalPackingRate_s);
            total_value.setText("AED "+Constants.store_totalcost_s);

            static_handymanvalue = Constants.static_handymanvalue_s;
            static_labourvalue = Constants.static_labourvalue_s;

        }
        else
        {
            nooftruck_edittext.setText(Constants.store_NoOfTruck_saving);
            nolabour_edittext.setText(Constants.store_NoOfLabour_saving);
            nohandyman_edittext.setText(Constants.store_NoOfHandiman_saving);
            basequote_edittext.setText(Constants.store_BaseRate_saving +" AED");

            nolabour_value.setText("AED "+Constants.store_TotalLabourRate_saving);
            nohandyman_value.setText("AED "+Constants.store_TotalHandimanRate_saving);
            nopacking_value.setText("AED "+Constants.store_TotalPackingRate_saving);
            total_value.setText("AED "+Constants.store_totalcost_saving);

            static_handymanvalue = Constants.static_handymanvalue_saving;
            static_labourvalue = Constants.static_labourvalue_saving;
        }


        change_labour=Integer.parseInt(nolabour_edittext.getText().toString());
        change_handyman=Integer.parseInt(nohandyman_edittext.getText().toString());
        nolabour_decrese_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet())
                {
                    if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                    {
                        // Toast.makeText(getApplicationContext(),Constants.store_NoOfLabour_incdec_s,Toast.LENGTH_SHORT).show();
                        if (Integer.parseInt(Constants.store_NoOfLabour_incdec_s) < Integer.parseInt(nolabour_edittext.getText().toString())) {

                            Constants.promocode_store="";
                            Constants.promocode_discount_value_store="";
                            discount.setText("");
                            promocode_value.setText("");


                            total_layout_promp.setVisibility(View.GONE);
                            total_layout.setVisibility(View.VISIBLE);
                            change_labour = Integer.parseInt(nolabour_edittext.getText().toString()) - 1;
                            new GetJsonmovemyhome_save().execute();
                        } else
                        {
                            UF.msg("Number Can Not Be Decreased");
                        }
                    }
                    else
                    {
                        if (Integer.parseInt(Constants.store_NoOfLabour_incdec_saving) < Integer.parseInt(nolabour_edittext.getText().toString())) {

                            Constants.promocode_store="";
                            Constants.promocode_discount_value_store="";
                            discount.setText("");
                            promocode_value.setText("");

                            total_layout_promp.setVisibility(View.GONE);
                            total_layout.setVisibility(View.VISIBLE);
                            change_labour = Integer.parseInt(nolabour_edittext.getText().toString()) - 1;
                            new GetJsonmovemyhome_save().execute();
                        } else {
                            UF.msg("Number Can Not Be Decreased");
                        }
                    }

                } else {
                    UF.msg("Check Your Internet Connection.");
                }

            }
        });
        nolabour_increse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet())
                {
                    Constants.promocode_store="";
                    Constants.promocode_discount_value_store="";
                    discount.setText("");
                    promocode_value.setText("");

                    total_layout_promp.setVisibility(View.GONE);
                    total_layout.setVisibility(View.VISIBLE);
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

        nohandyman_increse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet())
                {
                    Constants.promocode_store="";
                    Constants.promocode_discount_value_store="";
                    total_layout_promp.setVisibility(View.GONE);
                    total_layout.setVisibility(View.VISIBLE);
                    discount.setText("");
                    promocode_value.setText("");

                    change_handyman=Integer.parseInt(nohandyman_edittext.getText().toString())+1;
                    new GetJsonmovemyhome_save().execute();
                }
                else
                {
                    UF.msg("Check Your Internet Connection.");
                }

            }
        });
        nohandyman_decrese_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                if (cd.isConnectingToInternet())
                {

                    if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                    {Constants.promocode_store="";
                        discount.setText("");
                        promocode_value.setText("");

                        Constants.promocode_discount_value_store="";
                        total_layout_promp.setVisibility(View.GONE);
                        total_layout.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(Constants.store_NoOfHandiman_incdec_s) < Integer.parseInt(nohandyman_edittext.getText().toString())) {
                            change_handyman = Integer.parseInt(nohandyman_edittext.getText().toString()) - 1;
                            new GetJsonmovemyhome_save().execute();
                        }
                        else {
                            UF.msg("Number Can Not Be Decreased");
                        }
                    }
                    else
                    {
                        Constants.promocode_discount_value_store="";
                        Constants.promocode_store="";
                        discount.setText("");
                        promocode_value.setText("");
                        total_layout_promp.setVisibility(View.GONE);
                        total_layout.setVisibility(View.VISIBLE);
                        if (Integer.parseInt(Constants.store_NoOfHandiman_incdec_saving) < Integer.parseInt(nohandyman_edittext.getText().toString())) {
                            change_handyman = Integer.parseInt(nohandyman_edittext.getText().toString()) - 1;
                            new GetJsonmovemyhome_save().execute();
                        }
                        else {
                            UF.msg("Number Can Not Be Decreased");
                        }
                    }
                }
                else
                {
                    UF.msg("Check Your Internet Connection.");
                }

            }
        });
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

                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(Activity_GetQuote_standard.this);
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
        notruck_decrese_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UF.msg("Number Can Not Be Changed");

            }
        });
        notruck_increse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UF.msg("Number Can Not Be Changed");

            }
        });

    }

    public void init()
    {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm=new SessionManager(Activity_GetQuote_standard.this);
        UF=new UserFunctions(Activity_GetQuote_standard.this);
        cd= new ConnectionDetector(Activity_GetQuote_standard.this);

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
        basequote_edittext=(TextView)findViewById(R.id.basequote_edittext);
        submit=(Button)findViewById(R.id.submit);
        notruck_increse_btn = (Button)findViewById(R.id.notruck_increse);
        notruck_decrese_btn = (Button)findViewById(R.id.notruck_decrese);
        back=(TextView)findViewById(R.id.back);
        nolabour_decrese_btn = (Button)findViewById(R.id.nolabour_decrese);
        nolabour_increse_btn = (Button)findViewById(R.id.nolabour_increse);
        quote_icon = (ImageView)findViewById(R.id.quote_icon);
        nohandyman_decrese_btn = (Button)findViewById(R.id.nohandyman_decrese);
        nohandyman_increse_btn = (Button)findViewById(R.id.nohandyman_increse);
        nopacking_sucess= (ToggleButton) findViewById(R.id.nopacking_sucess);
        nooftruck_edittext=(TextView)findViewById(R.id.nooftruck_edittext);
        nolabour_edittext=(TextView)findViewById(R.id.nolabour_edittext);
        nohandyman_edittext=(TextView)findViewById(R.id.nohandyman_edittext);
        package_information=(Button)findViewById(R.id.package_information);

        nolabour_value=(TextView)findViewById(R.id.nolabour_value);
        nohandyman_value=(TextView)findViewById(R.id.nohandyman_value);
        nopacking_value=(TextView)findViewById(R.id.nopacking_value);

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

         if(Constants.promocode_store.equalsIgnoreCase(""))
         {

         }
        else
         {
             Log.e("promocode_store",Constants.promocode_store);
             Log.e("promocode_discountstore",Constants.promocode_discountstore);
             Log.e("discount_value_store", Constants.promocode_discount_value_store);


             total_layout.setVisibility(View.GONE);
             total_layout_promp.setVisibility(View.VISIBLE);
             total_value_promp.setText(String.valueOf("AED " + Constants.promocode_discountstore));
             discount.setText("-"+Constants.promocode_discount_value_store);
             promocode_value.setText(Constants.promocode_store);
         }



    }
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog  loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GetQuote_standard.this);
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
            int requiredproce=0;
            try
            {
                requiredproce=Integer.parseInt(Constants.promocode_discountstore)+Integer.parseInt(Constants.promocode_discount_value_store);

            }
            catch (Exception e)
            {

            }

            try
            {
                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("Area", Constants.store_area);
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");
                prmsLogin.put("NoOfTruck", nooftruck_edittext.getText().toString());
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", String.valueOf(change_labour));
                prmsLogin.put("NoOfHandiman", String.valueOf(change_handyman));
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
                prmsLogin.put("payment_mode", final_order);
                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("Isfinalorder", "N");
              if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
              {
                  prmsLogin.put("rate_type_flag", "B");
                  prmsLogin.put("required_price", requiredproce);
              }
               else
              {
                  prmsLogin.put("rate_type_flag", "S");
                  prmsLogin.put("required_price", requiredproce);
              }
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



                                nooftruck_edittext.setText(NoOfTruck_s);
                                nolabour_edittext.setText(NoOfLabour_s);
                                nohandyman_edittext.setText(NoOfHandiman_s);
                                basequote_edittext.setText(BaseRate_s +" AED");


                                nolabour_value.setText("AED "+TotalLabourRate_s);
                                nohandyman_value.setText("AED "+TotalHandimanRate_s);
                                nopacking_value.setText("AED "+TotalPackingRate_s);
                                total_value.setText("AED "+totlecost_stan);

                            List<String> items = Arrays.asList(Constants.store_addonsvalue.split(","));
                            if(final_order.equalsIgnoreCase("Y"))
                            {

                                Constants.store_billing_name = billing_name;
                                Constants.store_billing_add = billing_add;
                                Constants.store_source_full_add = source_full_add;
                                Constants.store_destination_full_add = destination_full_add;

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

                                if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
                                {

                                    Constants.store_totalcost_s = totlecost_stan;
                                    Constants.store_NoOfTruck_s = NoOfTruck_s;
                                    Constants.store_NoOfLabour_s = NoOfLabour_s;
                                    Constants.store_NoOfHandiman_s = NoOfHandiman_s;
                                    Constants.store_BaseRate_s = BaseRate_s;
                                    Constants.store_TotalLabourRate_s = TotalLabourRate_s;
                                    Constants.store_TotalHandimanRate_s = TotalHandimanRate_s;
                                    Constants.store_TotalPackingRate_s = TotalPackingRate_s;

                                    Constants.static_labourvalue_s = NoOfLabour_s;
                                    Constants.static_handymanvalue_s = NoOfHandiman_s;
                                    Log.e("NoOfLabour_s",Constants.store_NoOfLabour_s);
                                    Log.e("NoOfHandiman_s",Constants.store_NoOfHandiman_s);
                                    Log.e("store_BaseRate_s",Constants.store_BaseRate_s);
                                    Log.e("store_totalcost_s",Constants.store_totalcost_s);

                                }
                                else
                                {
                                    Constants.store_totalcost_saving = totlecost_stan;
                                    Constants.store_NoOfTruck_saving = NoOfTruck_s;
                                    Constants.store_NoOfLabour_saving = NoOfLabour_s;
                                    Constants.store_NoOfHandiman_saving = NoOfHandiman_s;
                                    Constants.store_BaseRate_saving = BaseRate_s;
                                    Constants.store_TotalLabourRate_saving = TotalLabourRate_s;
                                    Constants.store_TotalHandimanRate_saving = TotalHandimanRate_s;
                                    Constants.store_TotalPackingRate_saving = TotalPackingRate_s;

                                    Constants.static_labourvalue_saving = NoOfLabour_s;
                                    Constants.static_handymanvalue_saving = NoOfHandiman_s;
                                    Log.e("NoOfLabour_saving",Constants.store_NoOfLabour_saving);
                                    Log.e("NoOfHandiman_saving",Constants.store_NoOfHandiman_saving);
                                    Log.e("store_BaseRate_saving",Constants.store_BaseRate_saving);
                                    Log.e("store_totalcost_saving",Constants.store_totalcost_saving);
                                }

                               /* Intent i = new Intent(Activity_GetQuote_standard.this,Activity_payment.class);
                                i.putExtra("page", "standard");
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();*/


                                Intent i = new Intent(Activity_GetQuote_standard.this,Activity_MoveMyHome_Addons.class);
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
    private class GetJsonpromocode_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_GetQuote_standard.this);
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

                            applay_btn.setEnabled(false);
                            applay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.promocode_blank));
                            applay_btn.setTextColor(Color.parseColor("#ffffff"));

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
            promocode_layout= (LinearLayout)findViewById(R.id.promocode_layout_goods);
            promocode_layout.setVisibility(View.GONE);

            try
            {

                loading = new ProgressDialog(Activity_GetQuote_standard.this);
                loading.getWindow().setBackgroundDrawable(new

                ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setIndeterminate(true);
                loading.setCancelable(false);
                loading.show();
                loading.setContentView(R.layout.my_progress);
                String s="";
if(Constants.selectedIntent_getquote.equalsIgnoreCase("S"))
{
    s="N";

}
else if(Constants.selectedIntent_getquote.equalsIgnoreCase("SAVINGS"))
{
    s="S";
}else if(Constants.selectedIntent_getquote.equalsIgnoreCase("P"))
{
    s="P";
}

                    String url = UserFunctions.URL + "shipper/GetPackingMaterialDetailsBySizeType?sizetype="+Constants.store_Hometypeid+"&ratetypeflag="+s;
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
        tc_value="N";
        packing_chcek="Y";
        tc_condition.setChecked(false);
        promocode_value.setText("");

        if(Constants.counter>=3)
        {
           // Toast.makeText(getApplicationContext(), Constants.counter + "", Toast.LENGTH_SHORT).show();
            defaultvalueset();
        }


    }



    public void  defaultvalueset()
    {
        tc_condition.setChecked(true);
        tc_value="Y";
        Log.e("ssssssssssssssss", Constants.packing_chcek_s);
        Log.e("----------------",Constants.selectedIntent_getquote);
            if(Constants.packing_chcek_s.equalsIgnoreCase("Y"))
            {
                nopacking_sucess.setChecked(true);
            }
            else
            {
                nopacking_sucess.setChecked(false);
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

            applay_btn.setEnabled(false);
            applay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.promocode_blank));
            applay_btn.setTextColor(Color.parseColor("#ffffff"));
            total_layout.setVisibility(View.GONE);
            total_layout_promp.setVisibility(View.VISIBLE);
            total_value_promp.setText("AED " + String.valueOf(Constants.promocode_discountstore));
            discount.setText("-" + Constants.promocode_discount_value_store);
            promocode_value.setText(Constants.promocode_store);


        }


    }
    //changing condition of billing address  when selected payment option
    // in painting page selection ,display default mobileno noof user (when user login)
    //solving crashing issue in Activity_MoveMyHome_Detail
  public String convert_atos(String f)
    {

        f=f.replace("*,", "").replace("*","").replace("[","").replace("]","");
        f=f.trim();
        if (f.endsWith(",")) {
            f = f.substring(0, f.length() - 1);
        }
        return f;
    }


}