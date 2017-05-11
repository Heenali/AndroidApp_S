package com.trukker.trukkershipperuae.activity;

/**
 * Created by admin on 1/12/2017.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity_HireQuote extends AppCompatActivity
{

    LinearLayout noofdays_layout;
    TextView title_text_noofdays;
    String static_labourvalue;
    String static_handymanvalue;
    String tc_value="N";
    String android_id;
    String json_save;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    LinearLayout hometype_layout;
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
    LinearLayout total_layout,total_layout_promp;
    Button package_information;
    TextView back;
    ImageView quote_icon;
    TextView noof_packing_title;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Constants.counter = 1;
            Intent i = new Intent(Activity_HireQuote.this,Activity_HireTruck.class);
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
        if(Constants.storemail_send_goods.equalsIgnoreCase("0"))
        {
            if (cd.isConnectingToInternet())
            {
                new GetJson_save_mail().execute();
            } else {
                UF.msg("Check your internet connection.");
            }

        }
        quote_icon.setBackgroundResource(R.drawable.quote_standard);


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Constants.counter = 1;
                Intent i = new Intent(Activity_HireQuote.this,Activity_HireTruck.class);
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
            public void onClick(View v)
            {

                displayPopupWindow(v);

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
                //Toast.makeText(getApplicationContext(), "sssss", Toast.LENGTH_SHORT).show();
                // Intent i = new Intent(Activity_GetQuote_premium.this,Activity_PDFfileopen.class);
                // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                // startActivity(i);
                // finish();
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


            nooftruck_edittext.setText(Constants.store_NoOfTruck_s);
            nolabour_edittext.setText(Constants.store_NoOfLabour_s);
            nohandyman_edittext.setText(Constants.store_NoOfHandiman_s);

            String basequotevalue= Constants.store_BaseRate_s+"<font size=\"8\"> AED"+ "</font>";
            basequote_edittext.setText(Html.fromHtml(basequotevalue));

            nolabour_value.setText("AED "+ Constants.store_TotalLabourRate_s);
            nohandyman_value.setText("AED "+ Constants.store_TotalHandimanRate_s);
            nopacking_value.setText("AED "+ Constants.store_TotalPackingRate_s);
            total_value.setText("AED "+ Constants.store_totalcost_s);

            static_handymanvalue = Constants.static_handymanvalue_s;
            static_labourvalue = Constants.static_labourvalue_s;



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

                            Constants.promocode_store="";
                            Constants.promocode_discount_value_store="";
                            discount.setText("");
                            promocode_value.setText("");


                            total_layout_promp.setVisibility(View.GONE);
                            total_layout.setVisibility(View.VISIBLE);
                            change_labour = Integer.parseInt(nolabour_edittext.getText().toString()) - 1;
                            new GetJsonmovemyhome_save().execute();


                    }
                    else
                    {

                            Constants.promocode_store="";
                            Constants.promocode_discount_value_store="";
                            discount.setText("");
                            promocode_value.setText("");

                            total_layout_promp.setVisibility(View.GONE);
                            total_layout.setVisibility(View.VISIBLE);
                            change_labour = Integer.parseInt(nolabour_edittext.getText().toString()) - 1;
                            new GetJsonmovemyhome_save().execute();

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
                    int value_store=(Integer.parseInt(nolabour_edittext.getText().toString())+Integer.parseInt(nohandyman_edittext.getText().toString()));
                    // Toast.makeText(getApplicationContext(),Constants.store_NoOfLabour_incdec_s,Toast.LENGTH_SHORT).show();
                    if (value_store<3) {
                        Constants.promocode_store = "";
                        Constants.promocode_discount_value_store = "";
                        discount.setText("");
                        promocode_value.setText("");

                        total_layout_promp.setVisibility(View.GONE);
                        total_layout.setVisibility(View.VISIBLE);
                        change_labour = Integer.parseInt(nolabour_edittext.getText().toString()) + 1;
                        // Toast.makeText(getApplicationContext(), nolabour_edittext.getText().toString(), Toast.LENGTH_SHORT).show();
                        new GetJsonmovemyhome_save().execute();
                    }
                    else
                    {
                        UF.msg("Allow Maximum 3 labor per trucks");
                    }


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
                    int value_store=Integer.parseInt(nolabour_edittext.getText().toString())+Integer.parseInt(nohandyman_edittext.getText().toString());
                    // Toast.makeText(getApplicationContext(),Constants.store_NoOfLabour_incdec_s,Toast.LENGTH_SHORT).show();
                    if (value_store<3) {
                        Constants.promocode_store = "";
                        Constants.promocode_discount_value_store = "";
                        total_layout_promp.setVisibility(View.GONE);
                        total_layout.setVisibility(View.VISIBLE);
                        discount.setText("");
                        promocode_value.setText("");

                        change_handyman = Integer.parseInt(nohandyman_edittext.getText().toString()) + 1;
                        new GetJsonmovemyhome_save().execute();
                    }
                    else
                    {
                        UF.msg("Allow Maximum 3 labor per trucks");
                    }
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
                    {
                        Constants.promocode_store="";
                        discount.setText("");
                        promocode_value.setText("");

                        Constants.promocode_discount_value_store="";
                        total_layout_promp.setVisibility(View.GONE);
                        total_layout.setVisibility(View.VISIBLE);
                          change_handyman = Integer.parseInt(nohandyman_edittext.getText().toString()) - 1;
                            new GetJsonmovemyhome_save().execute();

                    }
                    else
                    {
                        Constants.promocode_discount_value_store="";
                        Constants.promocode_store="";
                        discount.setText("");
                        promocode_value.setText("");
                        total_layout_promp.setVisibility(View.GONE);
                        total_layout.setVisibility(View.VISIBLE);
                         change_handyman = Integer.parseInt(nohandyman_edittext.getText().toString()) - 1;
                            new GetJsonmovemyhome_save().execute();

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
                Constants.counter=2;
                if(tc_value.equalsIgnoreCase("N"))
                {
                    UF.msg("Please Accept Terms and Conditions");
                }
                else
                {
                    if (cd.isConnectingToInternet())
                    {

                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(Activity_HireQuote.this);
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
                UF.msg("You can not Change value");

            }
        });
        notruck_increse_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UF.msg("You can not Change value");

            }
        });

    }

    public void init()
    {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm=new SessionManager(Activity_HireQuote.this);
        UF=new UserFunctions(Activity_HireQuote.this);
        cd= new ConnectionDetector(Activity_HireQuote.this);
        noofdays_layout=(LinearLayout)findViewById(R.id.hometype_layout);
        noofdays_layout.setVisibility(View.VISIBLE);
        LinearLayout servicedis_layout=(LinearLayout)findViewById(R.id.servicedis_layout);
        servicedis_layout.setVisibility(View.GONE);

        title_text_noofdays=(TextView)findViewById(R.id.title_text);
        title_text_noofdays.setText("No.of Days "+Constants.store_noofdays);
        hometype_layout=(LinearLayout)findViewById(R.id.hometype_layout);
        hometype_layout.setVisibility(View.GONE);
        noof_packing_title=(TextView)findViewById(R.id.noof_packing_title);
        noof_packing_title.setText("Fuel Included");
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
           // Toast.makeText(getApplicationContext(),Constants.counter+"",Toast.LENGTH_LONG).show();
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
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_HireQuote.this);
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
            try {

                prmsLogin.put("Hiretruck_NoofDay", Constants.store_noofdays);
                prmsLogin.put("Hiretruck_To_datetime", Constants.store_date2);
                prmsLogin.put("Hiretruck_IncludingFuel",packing_chcek);
                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("TotalDistance","0");
                prmsLogin.put("TotalDistanceUOM","KM");
                prmsLogin.put("TimeToTravelInMinute","0");
                prmsLogin.put("NoOfTruck", Constants.store_NoOfTruck_s);
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", String.valueOf(change_labour));
                prmsLogin.put("NoOfHandiman", String.valueOf(change_handyman));
                prmsLogin.put("NoOfHelper", "");
                prmsLogin.put("shipper_id", sm.getUniqueId());
                prmsLogin.put("email_id", sm.getemailid());
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);
                prmsLogin.put("required_price", requiredproce);//
                prmsLogin.put("inquiry_source_addr", Constants.store_movefrom);
                prmsLogin.put("inquiry_source_city", Constants.postload_source_city);
                prmsLogin.put("inquiry_source_lat", Constants.postloadsourceLat);
                prmsLogin.put("inquiry_source_lng", Constants.postloadsourceLong);
                prmsLogin.put("source_pincode","");
                prmsLogin.put("load_inquiry_shipping_date", Constants.store_date);
                prmsLogin.put("load_inquiry_shipping_time", Constants.store_time_display);
                prmsLogin.put("inquiry_destination_addr", "");
                prmsLogin.put("inquiry_destination_city", "");
                prmsLogin.put("inquiry_destionation_lat", "");
                prmsLogin.put("inquiry_destionation_lng","" );
                prmsLogin.put("destination_pincode","" );
                prmsLogin.put("remarks","");
                prmsLogin.put("load_inquiry_truck_type","");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id",android_id.toString());
                prmsLogin.put("device_type","Android");
                prmsLogin.put("Isfinalorder", "N");
                prmsLogin.put("rate_type_flag", "B");
                prmsLogin.put("IncludePackingCharge", "N");
                prmsLogin.put("Isupdate", "Y");
                prmsLogin.put("order_type_flag","HT");
                prmsLogin.put("TruckTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("billing_add", "");
                prmsLogin.put("billing_name", "");
                prmsLogin.put("payment_mode","");
                prmsLogin.put("payment_status", "");
                prmsLogin.put("destination_full_add","");
                prmsLogin.put("source_full_add", "");
                prmsLogin.put("promocode", Constants.promocode_store);
                prmsLogin.put("goods_details", "");
                prmsLogin.put("Isupdatebillingadd", Constants.store_Isupdatebillingadd);

                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("postorder/SaveHireTruckDetails", prms);

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

                            String NoOfTruck_s = array.getJSONObject(0).getString("NoOfTruck").toString();
                            String NoOfLabour_s = array.getJSONObject(0).getString("NoOfLabour").toString();
                            String NoOfHandiman_s = array.getJSONObject(0).getString("NoOfHandiman").toString();
                            String BaseRate_s = array.getJSONObject(0).getString("BaseRate").toString();
                            String TotalLabourRate_s = array.getJSONObject(0).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_s = array.getJSONObject(0).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_s = array.getJSONObject(0).getString("Hiretruck_TotalFuelRate").toString();
                            String totlecost_stan = array.getJSONObject(0).getString("Total_cost").toString();
                            String Hiretruck_MaxKM_s = array.getJSONObject(0).getString("Hiretruck_MaxKM").toString();

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


                            if(final_order.equalsIgnoreCase("Y"))
                            {
                                Constants.store_billing_name = billing_name;
                                Constants.store_billing_add = billing_add;
                                Constants.store_source_full_add = source_full_add;
                                Constants.store_destination_full_add = destination_full_add;



                                    Constants.store_totalcost_s = totlecost_stan;
                                    Constants.store_NoOfTruck_s = NoOfTruck_s;
                                    Constants.store_NoOfLabour_s = NoOfLabour_s;
                                    Constants.store_NoOfHandiman_s = NoOfHandiman_s;
                                    Constants.store_BaseRate_s = BaseRate_s;
                                    Constants.store_TotalLabourRate_s = TotalLabourRate_s;
                                    Constants.store_TotalHandimanRate_s = TotalHandimanRate_s;
                                    Constants.store_TotalPackingRate_s = TotalPackingRate_s;
                                    Constants.store_Hiretruck_MaxKM_s=Hiretruck_MaxKM_s;
                                    Constants.static_labourvalue_s = NoOfLabour_s;
                                    Constants.static_handymanvalue_s = NoOfHandiman_s;
                                    Log.e("NoOfLabour_s", Constants.store_NoOfLabour_s);
                                    Log.e("NoOfHandiman_s", Constants.store_NoOfHandiman_s);
                                    Log.e("store_BaseRate_s", Constants.store_BaseRate_s);
                                    Log.e("store_totalcost_s", Constants.store_totalcost_s);


                               Intent i = new Intent(Activity_HireQuote.this,Activity_HirePayment.class);
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

            loading = new ProgressDialog(Activity_HireQuote.this);
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

        tc_value="N";
        tc_condition.setChecked(false);
        if(Constants.counter>=3)
        {    // Toast.makeText(getApplicationContext(),Constants.counter+"",Toast.LENGTH_LONG).show();
            // Toast.makeText(getApplicationContext(), Constants.counter + "", Toast.LENGTH_SHORT).show();
            defaultvalueset();
        }


    }



    public void  defaultvalueset()
    {
        tc_value="Y";
        tc_condition.setChecked(true);

        Log.e("ssssssssssssssss", Constants.packing_chcek_s);
        Log.e("----------------", Constants.selectedIntent_getquote);
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


            tc_value="Y";
            tc_condition.setChecked(true);
            Log.e("promocode_store",Constants.promocode_store);
            Log.e("promocode_discountstore",Constants.promocode_discountstore);
            Log.e("discount_value_store", Constants.promocode_discount_value_store);


            total_layout.setVisibility(View.GONE);
            total_layout_promp.setVisibility(View.VISIBLE);
            total_value_promp.setText("AED "+String.valueOf(Constants.promocode_discountstore));
            discount.setText("-" + Constants.promocode_discount_value_store);
            applay_btn.setEnabled(false);
            applay_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.promocode_blank));
            applay_btn.setTextColor(Color.parseColor("#ffffff"));

            promocode_value.setText(Constants.promocode_store);

        }

    }

        private void displayPopupWindow(View anchorView) {
            PopupWindow popup = new PopupWindow(Activity_HireQuote.this);
            View layout = getLayoutInflater().inflate(R.layout.dialog_packinfohire, null);
            TextView t1;
            t1=(TextView)layout.findViewById(R.id.tvCaption);
            t1.setText("Fuel includes for up to "+Constants.store_Hiretruck_MaxKM_s+ " kms per day");
            popup.setContentView(layout);
            // Set content width and height
            popup.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);
            popup.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
            // Closes the popup window when touch outside of it - when looses focus
            popup.setOutsideTouchable(true);
            popup.setFocusable(true);
            // Show anchored to button
            popup.setBackgroundDrawable(new BitmapDrawable());
            popup.showAsDropDown(anchorView);
        }

    //changing condition of billing address  when selected payment option
    // in painting page selection ,display default mobileno noof user (when user login)
    //solving crashing issue in Activity_MoveMyHome_Detail
    private class GetJson_save_mail extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_HireQuote.this);
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
                prmsLogin.put("Sizetypecode",Constants.store_Hometypename);
                prmsLogin.put("Sourceaddress", Constants.store_movefrom);
                prmsLogin.put("Destinationaddress","");
                prmsLogin.put("ShippingDatetime", strd+" "+strt);
                prmsLogin.put("load_inquiry_no",Constants.store_loadinquiry_no);
                prmsLogin.put("TotalDistance", "0");
                prmsLogin.put("TotalDistanceUOM","KM");


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
}
/*
round_mainbutton_black    replace     promocode_select    applay_btn.setTextColor(Color.parseColor("#3675C5"));
			      promocode_blank 	    applay_btn.setTextColor(Color.parseColor("#ffffff"));*/