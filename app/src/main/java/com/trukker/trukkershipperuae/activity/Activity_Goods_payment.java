package com.trukker.trukkershipperuae.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.gson.Gson;
import com.payfort.fort.android.sdk.base.FortSdk;
import com.payfort.fort.android.sdk.base.callbacks.FortCallBackManager;
import com.payfort.fort.android.sdk.base.callbacks.FortCallback;
import com.payfort.sdk.android.dependancies.base.FortInterfaces;
import com.payfort.sdk.android.dependancies.models.FortRequest;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;


/**
 * Created by admin on 8/31/2016.
 */
public class Activity_Goods_payment extends AppCompatActivity
{


    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

    String one,two="",three="",four="";
    String two_dest="",three_dest="",four_dest="";
    String two_source="",three_source="",four_source="";
    String page;
    String json_save;
    String   android_id;
    String payment_mode="O";
    UserFunctions UF;
    SessionManager sm;
    TextView savedraft_btn;
    String Isfinalorder="N";
    ConnectionDetector cd;
    String  goodswunit_value;
    EditText billingname_edittext,billingadress_edittext,movingfromadd_edittext,movingtoadd_edittext;
    ToggleButton icon_sourceadd,icon_destadd,cashdelivery_sucess,onlinedelivery_sucess;
    //String icon_sourceadd_value="N",icon_destadd_value="N",cashdelivery_sucess_value="N",onlinedelivery_sucess_value="Y";
    TextView continue_btn;
    String bill_add,toadd,fromadd;
    TextView back;
    String amount;
    String Isupdatebillingadd="N";
    String android_id_sdk;
    String json_payfory;
    EditText goodsw_edittext;
    Spinner goodswunit_edittext;

    private FortCallBackManager fortCallback  = null;
    StringBuffer sb = new StringBuffer();
    TextView title_page;

    EditText vilano_edittext,streetadd_edittext,tovilano_edittext,fromvilano_edittext;
    LinearLayout movingfromadd_layout, cashdelievery_layout,movingtoadd_layout,online_layout;
    AutoCompleteTextView fromstreetadd_edittext,tostreetadd_edittext;
    EditText goods_edittext;
    int movingfromadd_layout_value=0,movingtoadd_layout_value=0,online_layout_value=1,cashdelievery_layout_value=0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            Constants.paymentpage_point="Y";
            Constants.page_billingname_edittext=billingname_edittext.getText().toString();
            Constants.page_vilano_edittext=vilano_edittext.getText().toString();

            Constants.page_streetadd_edittext=streetadd_edittext.getText().toString();
            Constants.page_fromvilano_edittext=fromvilano_edittext.getText().toString();

            Constants.page_fromstreetadd_edittext=fromstreetadd_edittext.getText().toString();
            Constants.page_tovilano_edittext=tovilano_edittext.getText().toString();

            Constants.page_tostreetadd_edittext=tostreetadd_edittext.getText().toString();
            Constants.page_goodsdetails_edittext=goods_edittext.getText().toString();
            Constants.page_movingfromadd_layout_value=movingfromadd_layout_value;
            Constants.page_movingtoadd_layout_value=movingtoadd_layout_value;
            Constants.page_cashdelievery_layout_value=cashdelievery_layout_value;
            Constants.page_goodsw=goodsw_edittext.getText().toString();
            Constants.page_goodswunit=goodswunit_value;

            Intent i = new Intent(Activity_Goods_payment.this,Activity_Goods_detail.class);

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
        setContentView(R.layout.activity_payment1);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        if (Constants.HomeDraft_array_value==1)
        {
            try
            {
                Log.e("Heenali Lkahn",Constants.HomeDraft_array_value+"");
               // Constants.HomeDraft_array_value=0;
                Log.e("Heenali Lkahn",Constants.HomeDraft_array.toString());
                JSONObject obj=new JSONObject(Constants.HomeDraft_array.toString());
                String billing_name=obj.getString("billing_name").toString();
                String billing_add=obj.getString("billing_add").toString();
                Log.e("billing_add",billing_name);
                String source_full_add=obj.getString("source_full_add").toString();
                String destination_full_add=obj.getString("destination_full_add").toString();

                Constants.storepayment_goodsw=obj.getString("goods_weight").toString();
                Constants.storepayment_goodswunit=obj.getString("goods_weightUOM").toString();
                Constants.store_billing_name = billing_name;

                Constants.store_billing_add = billing_add;
                Constants.store_source_full_add = source_full_add;
                Constants.store_destination_full_add = destination_full_add;

                String NoOfTruck_s= obj.getString("NoOfTruck").toString();
                String NoOfLabour_s= obj.getString("NoOfLabour").toString();
                String NoOfHandiman_s= obj.getString("NoOfHandiman").toString();
                String BaseRate_s= obj.getString("BaseRate").toString();
                String TotalLabourRate_s= obj.getString("TotalLabourRate").toString();
                String TotalHandimanRate_s= obj.getString("TotalHandimanRate").toString();
                String TotalPackingRate_s= obj.getString("TotalPackingCharge").toString();


                Constants.TimeForLoadingInMinute_g= obj.getString("TimeForloadingInMinute").toString();
                Constants.TimeForUnloadingInMinute_g= obj.getString("TimeForUnloadingInMinute").toString();

                //Constants.TimeForLoadingInMinute_g= "30.00";
               // Constants.TimeForUnloadingInMinute_g= "30.00";
                Constants.store_goodsdetails=obj.getString("goods_details").toString();

                String totlecost_stan=obj.getString("Total_cost").toString();
                payment_mode=obj.getString("payment_mode").toString();
                //    Toast.makeText(getApplicationContext(), payment_mode,Toast.LENGTH_SHORT).show();



                Constants.eta = obj.getString("TimeToTravelInMinute").toString();
                Constants.store_loadinquiry_no=obj.getString("load_inquiry_no");
                Constants.store_date =obj.getString("load_inquiry_shipping_date");
                Constants.store_time =obj.getString("load_inquiry_shipping_time");

                String coupon_code=obj.getString("coupon_code").toString();
                Constants.promocode_store=coupon_code;
                String Discount=obj.getString("Discount").toString();
                Constants.promocode_discount_value_store=Discount;

                Constants.promocode_discountstore=obj.getString("rem_amt_to_receive");
                try
                {
                    String date=Constants.store_date;
                    SimpleDateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
                    SimpleDateFormat DesiredFormat  = new SimpleDateFormat("dd/MM/yyyy");

                    Date date1 = sourceFormat.parse(date);
                    Constants.store_date= DesiredFormat.format(date1);

                }
                catch (Exception e)
                {

                }
                try
                {
                    converttimeformate(Constants.store_time.toString());
                }
                catch (Exception e)
                {

                }

                Constants.store_movefrom=obj.getString("inquiry_source_addr");
                Constants.postload_source_city=obj.getString("inquiry_source_city");
                Constants.postloadsourceLat=Double.parseDouble(obj.getString("inquiry_source_lat"));
                Constants.postloadsourceLong=Double.parseDouble(obj.getString("inquiry_source_lng"));

                Constants.store_moveto=obj.getString("inquiry_destination_addr");
                Constants.postload_dest_city=obj.getString("inquiry_destination_city");
                Constants.postloaddestLat=Double.parseDouble(obj.getString("inquiry_destionation_lat"));
                Constants.postloaddestLong =Double.parseDouble(obj.getString("inquiry_destionation_lng"));

                Constants.store_Hometypeid=obj.getString("SizeTypeCode");
                Constants.store_Hometypename=obj.getString("SizeTypeDesc");
                Constants.store_area=obj.getString("Area");
                Constants.store_placedistace=obj.getString("TotalDistance");
                Constants.store_distance = Constants.store_placedistace;
                Constants.store_unit=obj.getString("TotalDistanceUOM");
                Constants.store_shipperid=obj.getString("shipper_id");
                //Log.e


                    Constants.totlecost_g = totlecost_stan;
                    Constants.store_NoOfTruck_g = NoOfTruck_s;
                    Constants.store_NoOfLabour_g = NoOfLabour_s;
                    Constants.store_NoOfHandiman_g = NoOfHandiman_s;
                    Constants.store_BaseRate_g = BaseRate_s;
                    Constants.store_TotalLabourRate_g = TotalLabourRate_s;
                    Constants.store_TotalHandimanRate_g = TotalHandimanRate_s;
                    Constants.store_TotalPackingRate_g = TotalPackingRate_s;

                        amount= Constants.totlecost_g;



            } catch (JSONException e)
            {
                e.printStackTrace();
            }


        }
        else
        {

            amount= Constants.totlecost_g;
        }
        init();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Constants.paymentpage_point="Y";
                Constants.page_billingname_edittext=billingname_edittext.getText().toString();
                Constants.page_vilano_edittext=vilano_edittext.getText().toString();

                Constants.page_streetadd_edittext=streetadd_edittext.getText().toString();
                Constants.page_fromvilano_edittext=fromvilano_edittext.getText().toString();

                Constants.page_fromstreetadd_edittext=fromstreetadd_edittext.getText().toString();
                Constants.page_tovilano_edittext=tovilano_edittext.getText().toString();

                Constants.page_tostreetadd_edittext=tostreetadd_edittext.getText().toString();
                Constants.page_goodsdetails_edittext=goods_edittext.getText().toString();

                Constants.page_movingfromadd_layout_value=movingfromadd_layout_value;
                Constants.page_movingtoadd_layout_value=movingtoadd_layout_value;
                Constants.page_cashdelievery_layout_value=cashdelievery_layout_value;
                Constants.page_goodsw=goodsw_edittext.getText().toString();
                Constants.page_goodswunit=goodswunit_value;

                ////////////////////////

                Intent i = new Intent(Activity_Goods_payment.this,Activity_Goods_detail.class);

                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();

            }
        });
        movingfromadd_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(movingfromadd_layout.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                if (movingfromadd_layout_value == 0)
                {
                    icon_sourceadd.setBackgroundResource(R.drawable.success);

                    movingfromadd_layout_value = 1;

                    String one = vilano_edittext.getText().toString();

                    String three = streetadd_edittext.getText().toString();

                    fromvilano_edittext.setText(one.toString());

                    fromstreetadd_edittext.setText(three.toString());

                    fromvilano_edittext.setError(null);

                    fromstreetadd_edittext.setError(null);

                    if(movingtoadd_layout_value==1)
                    {
                        icon_destadd.setBackgroundResource(R.drawable.success_blank);
                        movingtoadd_layout_value=0;
                        tovilano_edittext.setText("");

                        tostreetadd_edittext.setText("");
                    }

                    DecimalFormat df = new DecimalFormat("#.#");
                    df.setRoundingMode(RoundingMode.CEILING);

                        Float d = 2.20f;
                        System.out.println(df.format(d));

                }
                else
                {
                    icon_sourceadd.setBackgroundResource(R.drawable.success_blank);
                    movingfromadd_layout_value = 0;

                    fromvilano_edittext.setText("");

                    fromstreetadd_edittext.setText("");

                }

            }
        });
        icon_sourceadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(movingfromadd_layout.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                if (movingfromadd_layout_value == 0)
                {
                    icon_sourceadd.setBackgroundResource(R.drawable.success);

                    movingfromadd_layout_value = 1;

                    String one = vilano_edittext.getText().toString();

                    String three = streetadd_edittext.getText().toString();

                    fromvilano_edittext.setText(one.toString());

                    fromstreetadd_edittext.setText(three.toString());

                    fromvilano_edittext.setError(null);

                    fromstreetadd_edittext.setError(null);

                    if(movingtoadd_layout_value==1)
                    {
                        icon_destadd.setBackgroundResource(R.drawable.success_blank);
                        movingtoadd_layout_value=0;
                        tovilano_edittext.setText("");

                        tostreetadd_edittext.setText("");
                    }


                }
                else
                {
                    icon_sourceadd.setBackgroundResource(R.drawable.success_blank);
                    movingfromadd_layout_value = 0;

                    fromvilano_edittext.setText("");

                    fromstreetadd_edittext.setText("");

                }


            }
        });
        icon_destadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(movingtoadd_layout.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                if(movingtoadd_layout_value==0)
                {
                    icon_destadd.setBackgroundResource(R.drawable.success);
                    movingtoadd_layout_value=1;


                    String one=vilano_edittext.getText().toString();

                    String three=streetadd_edittext.getText().toString();


                    tovilano_edittext.setText(one.toString());

                    tostreetadd_edittext.setText(three.toString());

                    tovilano_edittext.setError(null);

                    tostreetadd_edittext.setError(null);

                    if(movingfromadd_layout_value==1)
                    {
                        icon_sourceadd.setBackgroundResource(R.drawable.success_blank);
                        movingfromadd_layout_value=0;
                        fromvilano_edittext.setText("");

                        fromstreetadd_edittext.setText("");

                    }

                }
                else
                {
                    icon_destadd.setBackgroundResource(R.drawable.success_blank);
                    movingtoadd_layout_value=0;
                    tovilano_edittext.setText("");

                    tostreetadd_edittext.setText("");
                }
            }
        });

        movingtoadd_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(movingtoadd_layout.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                if(movingtoadd_layout_value==0)
                {
                    icon_destadd.setBackgroundResource(R.drawable.success);
                    movingtoadd_layout_value=1;


                    String one=vilano_edittext.getText().toString();

                    String three=streetadd_edittext.getText().toString();


                    tovilano_edittext.setText(one.toString());

                    tostreetadd_edittext.setText(three.toString());

                    tovilano_edittext.setError(null);

                    tostreetadd_edittext.setError(null);

                    if(movingfromadd_layout_value==1)
                    {
                        icon_sourceadd.setBackgroundResource(R.drawable.success_blank);
                        movingfromadd_layout_value=0;
                        fromvilano_edittext.setText("");

                        fromstreetadd_edittext.setText("");

                    }

                }
                else
                {
                    icon_destadd.setBackgroundResource(R.drawable.success_blank);
                    movingtoadd_layout_value=0;
                    tovilano_edittext.setText("");

                    tostreetadd_edittext.setText("");
                }

            }
        });
        cashdelivery_sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cashdelievery_layout_value==0)
                {
                    cashdelivery_sucess.setBackgroundResource(R.drawable.success);
                    cashdelievery_layout_value=1;
                    onlinedelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                    online_layout_value=0;
                    payment_mode="C";
                }
                else
                {
                    cashdelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                    cashdelievery_layout_value=0;
                    payment_mode="O";
                    onlinedelivery_sucess.setBackgroundResource(R.drawable.success);
                    online_layout_value=1;
                }
            }
        });
        onlinedelivery_sucess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(online_layout_value==0)
                {
                    onlinedelivery_sucess.setBackgroundResource(R.drawable.success);
                    online_layout_value=1;
                    cashdelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                    cashdelievery_layout_value=0;
                    payment_mode="O";
                }
                else
                {
                    onlinedelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                    online_layout_value=0;
                    cashdelivery_sucess.setBackgroundResource(R.drawable.success);
                    cashdelievery_layout_value=1;
                    payment_mode="C";
                }
            }
        });
        cashdelievery_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(cashdelievery_layout_value==0)
                {
                    cashdelivery_sucess.setBackgroundResource(R.drawable.success);
                    cashdelievery_layout_value=1;
                    onlinedelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                    online_layout_value=0;
                    payment_mode="C";
                }
                else
                {
                    cashdelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                    cashdelievery_layout_value=0;
                    payment_mode="O";
                    onlinedelivery_sucess.setBackgroundResource(R.drawable.success);
                    online_layout_value=1;
                }

            }
        });
        online_layout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(online_layout_value==0)
                {
                    onlinedelivery_sucess.setBackgroundResource(R.drawable.success);
                    online_layout_value=1;
                    cashdelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                    cashdelievery_layout_value=0;
                    payment_mode="O";
                }
                else
                {
                    onlinedelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                    online_layout_value=0;
                    cashdelivery_sucess.setBackgroundResource(R.drawable.success);
                    cashdelievery_layout_value=1;
                    payment_mode="C";
                }

            }
        });

        continue_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(continue_btn.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

                signup();
            }
        });
        savedraft_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {


                if (!validate())
                {
                    onSignupFailed();
                    return;
                }


                bill_add=streetadd_edittext.getText().toString();
                toadd=tovilano_edittext.getText().toString()+"^"+tostreetadd_edittext.getText().toString();
                fromadd=fromvilano_edittext.getText().toString()+"^"+fromstreetadd_edittext.getText().toString();

                Constants.storepayment_billling_add=bill_add;

                Constants.storepayment_billingname= billingname_edittext.getText().toString();
                Constants.storepayment_movingtoadd=toadd;
                Constants.storepayment_movingfromadd=fromadd;

                Constants.storepayment_paymentmode= payment_mode;
                Constants.store_goodsdetails=goods_edittext.getText().toString();

                Constants.storepayment_goodsw=goodsw_edittext.getText().toString();
                Constants.storepayment_goodswunit=goodswunit_value;
                new GetJson_draft().execute();
            }
        });

    }
    public  void init() {
        Intent i = getIntent();
        page= i.getStringExtra("page");
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_Goods_payment.this);
        UF = new UserFunctions(Activity_Goods_payment.this);
        cd = new ConnectionDetector(Activity_Goods_payment.this);
        back=(TextView)findViewById(R.id.back);
        title_page=(TextView)findViewById(R.id.title);
        goodsw_edittext=(EditText)findViewById(R.id.goodsw_edittext);
        goodswunit_edittext=(Spinner)findViewById(R.id.goodswunit_edittext);

        title_page.setText("Location Details");
        goods_edittext=(EditText)findViewById(R.id.goodsdetails_edittext);
        billingname_edittext=(EditText)findViewById(R.id.billingname_edittext);
        vilano_edittext=(EditText)findViewById(R.id.vilano_edittext);

        streetadd_edittext=(EditText)findViewById(R.id.streetadd_edittext);
        savedraft_btn=(TextView)findViewById(R.id.savedraft_btn);
        movingfromadd_layout=(LinearLayout)findViewById(R.id.movingfromadd_layout);
        movingtoadd_layout=(LinearLayout)findViewById(R.id.movingtoadd_layout);

        cashdelievery_layout=(LinearLayout)findViewById(R.id.cashdelievery_layout);
        online_layout=(LinearLayout)findViewById(R.id.online_layout);

        tovilano_edittext=(EditText)findViewById(R.id.tovilano_edittext);

        tostreetadd_edittext=(AutoCompleteTextView) findViewById(R.id.tostreetadd_edittext);
        fromvilano_edittext=(EditText)findViewById(R.id.fromvilano_edittext);
        fromstreetadd_edittext=(AutoCompleteTextView) findViewById(R.id.fromstreetadd_edittext);

        icon_sourceadd=(ToggleButton)findViewById(R.id.icon_sourceadd);
        icon_destadd=(ToggleButton)findViewById(R.id.icon_destiadd);
        cashdelivery_sucess=(ToggleButton)findViewById(R.id.cashdelivery_sucess);
        onlinedelivery_sucess=(ToggleButton)findViewById(R.id.onlinedelivery_sucess);

        continue_btn=(TextView)findViewById(R.id.continue_btn);

        goodswunit_edittext.setPrompt("SELECT UNIT");
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.spinner_unit, R.layout.list_year);
        goodswunit_edittext.setAdapter(adapter3);
        goodswunit_edittext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                // TODO Auto-generated method stub

                goodswunit_value = goodswunit_edittext.getSelectedItem().toString();


            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        billingname_edittext.setText(Html.fromHtml(sm.getUserName().toString()));
        streetadd_edittext.setText(Html.fromHtml(Constants.store_movefrom));

        if(Constants.paymentpage_point.equalsIgnoreCase("Y"))
        {

            Log.e("enter in step1 ", ".............");
            billingname_edittext.setText(Constants.page_billingname_edittext);
            vilano_edittext.setText(Constants.page_vilano_edittext);

            streetadd_edittext.setText(Constants.page_streetadd_edittext);
            fromvilano_edittext.setText(Constants.page_fromvilano_edittext);

            fromstreetadd_edittext.setText(Constants.page_fromstreetadd_edittext);
            tovilano_edittext.setText(Constants.page_tovilano_edittext);

            tostreetadd_edittext.setText(Constants.page_tostreetadd_edittext);
            goods_edittext.setText(Constants.page_goodsdetails_edittext);
            goodsw_edittext.setText(Constants.page_goodsw);

            goodswunit_edittext.setSelection(((ArrayAdapter<String>)goodswunit_edittext.getAdapter()).getPosition(Constants.page_goodswunit));
           // Toast.makeText(getApplicationContext(),String.valueOf(Constants.page_movingfromadd_layout_value),Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),String.valueOf(Constants.page_movingtoadd_layout_value),Toast.LENGTH_SHORT).show();

            if(Constants.page_movingfromadd_layout_value==1)
            {


                icon_sourceadd.setBackgroundResource(R.drawable.success);
                Constants.page_movingfromadd_layout_value=0;
                movingfromadd_layout_value=1;
                movingtoadd_layout_value=0;

            }
            if(Constants.page_movingtoadd_layout_value==1)
            {
                movingtoadd_layout_value=1;
                movingfromadd_layout_value=0;
                icon_destadd.setBackgroundResource(R.drawable.success);
                Constants.page_movingtoadd_layout_value=0;



            }
            //Toast.makeText(getApplicationContext(),String.valueOf(Constants.page_cashdelievery_layout_value),Toast.LENGTH_SHORT).show();
            if(Constants.page_cashdelievery_layout_value==1)
            {
                onlinedelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                online_layout_value=0;
                cashdelivery_sucess.setBackgroundResource(R.drawable.success);
                cashdelievery_layout_value=1;
                payment_mode="C";
            }
            else
            {
                onlinedelivery_sucess.setBackgroundResource(R.drawable.success);
                online_layout_value=1;
                cashdelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                cashdelievery_layout_value=0;
                payment_mode="O";
            }

        }
        else
        {
            Log.e("enter in step2 ",".............");
            goods_edittext.setText(Constants.store_goodsdetails);
            goodsw_edittext.setText(Constants.storepayment_goodsw);
            goodswunit_edittext.setSelection(((ArrayAdapter<String>)goodswunit_edittext.getAdapter()).getPosition(Constants.storepayment_goodswunit));
            if(payment_mode.equalsIgnoreCase("O"))
            {
                cashdelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                cashdelievery_layout_value=0;
                payment_mode="O";
                onlinedelivery_sucess.setBackgroundResource(R.drawable.success);
                online_layout_value=1;

            }
            else
            {
                onlinedelivery_sucess.setBackgroundResource(R.drawable.success_blank);
                online_layout_value=0;
                cashdelivery_sucess.setBackgroundResource(R.drawable.success);
                cashdelievery_layout_value=1;
                payment_mode="C";
            }
            Log.e("hhhhh",Constants.store_billing_name);
            if(Constants.store_billing_name.equalsIgnoreCase(""))
            {
               // billingname_edittext.setText(sm.getUserName());
                fromstreetadd_edittext.setText(Constants.store_movefrom);
                tostreetadd_edittext.setText(Constants.store_moveto);
                Log.e("enter in step3 ", ".............");
            }
            else
            {

            }
            if(Constants.store_source_full_add.equalsIgnoreCase(""))
            {
                fromvilano_edittext.setText("");
                tovilano_edittext.setText("");
                fromstreetadd_edittext.setText(Constants.store_movefrom);
                tostreetadd_edittext.setText(Constants.store_moveto);
            }
            else
            {
                if(Constants.HomeDraft_array_value==1) {


                    String all = Constants.store_source_full_add;
                    all = all.replace('^', '<');
                    Log.e("ddddddddd", all.toString());
                    ArrayList aList = new ArrayList(Arrays.asList(all.split("<")));
                    String t2 = "";
                    for (int i1 = 0; i1 < aList.size(); i1++) {

                        System.out.println(" -->" + aList.get(i1));
                        if (i1 == 0) {
                            two_source = aList.get(i1).toString();
                            fromvilano_edittext.setText(aList.get(i1).toString());
                        } else if (i1 == 1) {
                            t2 = aList.get(i1).toString();
                            three_source = aList.get(i1).toString();
                            fromstreetadd_edittext.setText(aList.get(i1).toString());

                        } else if (i1 == 2) {
                            four_source = aList.get(i1).toString();
                            fromstreetadd_edittext.setText(t2 + aList.get(i1).toString());

                        }
                    }
                }else
                {

                    fromvilano_edittext.setText("");
                    tovilano_edittext.setText("");
                    fromstreetadd_edittext.setText(Constants.store_movefrom);
                    tostreetadd_edittext.setText(Constants.store_moveto);
                }
            }
            if(Constants.store_destination_full_add.equalsIgnoreCase(""))
            {
                fromvilano_edittext.setText("");
                tovilano_edittext.setText("");
                fromstreetadd_edittext.setText(Constants.store_movefrom);
                tostreetadd_edittext.setText(Constants.store_moveto);
            }
            else
            {
                if(Constants.HomeDraft_array_value==1) {

                    Constants.HomeDraft_array_value=0;
                    String all = Constants.store_destination_full_add;
                    all = all.replace('^', '<');
                    Log.e("ddddddddd", all.toString());
                    ArrayList aList = new ArrayList(Arrays.asList(all.split("<")));
                    String t2 = "";
                    for (int i1 = 0; i1 < aList.size(); i1++) {

                        System.out.println(" -->" + aList.get(i1));
                        if (i1 == 0) {
                            two_dest = aList.get(i1).toString();
                            tovilano_edittext.setText(aList.get(i1).toString());
                        } else if (i1 == 1) {
                            t2 = aList.get(i1).toString();
                            three_dest = aList.get(i1).toString();
                            tostreetadd_edittext.setText(aList.get(i1).toString());

                        } else if (i1 == 2) {
                            four_dest = aList.get(i1).toString();
                            tostreetadd_edittext.setText(t2 + aList.get(i1).toString());

                        }

                    }
                }
                else
                {

                    fromvilano_edittext.setText("");
                    tovilano_edittext.setText("");
                    fromstreetadd_edittext.setText(Constants.store_movefrom);
                    tostreetadd_edittext.setText(Constants.store_moveto);
                }
            }
        }

        try
        {
            fromstreetadd_edittext.setThreshold(1);
            fromstreetadd_edittext.setAdapter(new Activity_Goods_payment.GooglePlacesAutocompleteAdapter_so(Activity_Goods_payment.this, R.layout.list_item));
            tostreetadd_edittext.setThreshold(1);
            tostreetadd_edittext.setAdapter(new Activity_Goods_payment.GooglePlacesAutocompleteAdapter_so(Activity_Goods_payment.this, R.layout.list_item));

        }
        catch (Exception e) {

        }

    }

    public void signup()
    {
        Log.d("mess", "Signup");

        if (!validate())
        {
            onSignupFailed();
            return;
        }

        continue_btn.setEnabled(false);

        onSignupSuccess();

    }
    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();

        continue_btn.setEnabled(true);
    }
    public void onSignupSuccess()
    {
        continue_btn.setEnabled(true);
        setResult(RESULT_OK, null);

        if (cd.isConnectingToInternet())
        {
            Constants.counter=5;

           bill_add=streetadd_edittext.getText().toString();
            toadd=tovilano_edittext.getText().toString()+"^"+tostreetadd_edittext.getText().toString();
            fromadd=fromvilano_edittext.getText().toString()+"^"+fromstreetadd_edittext.getText().toString();

          // bill_add=vilano_edittext.getText().toString()+" "+commname_edittext.getText().toString()+" "+streetadd_edittext.getText().toString();
          //  toadd=tovilano_edittext.getText().toString()+" "+tocommname_edittext.getText().toString()+" "+tostreetadd_edittext.getText().toString();
           // fromadd=fromvilano_edittext.getText().toString()+" "+fromcommname_edittext.getText().toString()+" "+fromstreetadd_edittext.getText().toString();

            Constants.storepayment_goodsw=goodsw_edittext.getText().toString();
            Constants.storepayment_goodswunit=goodswunit_value;
            Constants.storepayment_billling_add=bill_add;
            Constants.storepayment_billling_add=bill_add;
            Constants.storepayment_billingname= billingname_edittext.getText().toString();
            Constants.storepayment_movingtoadd=toadd;
            Constants.storepayment_movingfromadd=fromadd;

            Constants.storepayment_paymentmode= payment_mode;
            Constants.store_goodsdetails=goods_edittext.getText().toString();
            // Constants.storepayment_sourcevalue=icon_sourceadd_value;
            // Constants.storepayment_destvalue=icon_destadd_value;
            if(two.equalsIgnoreCase("")||three.equalsIgnoreCase("")||four.equalsIgnoreCase(""))
            {
                Isupdatebillingadd="Y";
            }
            else
            {
                if(one.equalsIgnoreCase(billingname_edittext.getText().toString()))
                {
                    Isupdatebillingadd="N";
                    if(two.equalsIgnoreCase(vilano_edittext.getText().toString()))
                    {
                        Isupdatebillingadd="N";
                        //if(three.equalsIgnoreCase(commname_edittext.getText().toString()))
                       // {
                           // Isupdatebillingadd="N";
                            if(four.equalsIgnoreCase(streetadd_edittext.getText().toString()))
                            {
                                Isupdatebillingadd="N";

                                if(two_dest.equalsIgnoreCase("")||three_dest.equalsIgnoreCase("")||four_dest.equalsIgnoreCase(""))
                                {
                                    Isupdatebillingadd="Y";
                                }
                                else
                                {

                                    if(two_dest.equalsIgnoreCase(fromvilano_edittext.getText().toString()))
                                    {
                                        Isupdatebillingadd="N";
                                       // if(three_dest.equalsIgnoreCase(fromcommname_edittext.getText().toString()))
                                       // {
                                            Isupdatebillingadd="N";
                                            if(four_dest.equalsIgnoreCase(fromstreetadd_edittext.getText().toString()))
                                            {
                                                Isupdatebillingadd="N";
                                                //source
                                                ////////////////
                                                if(two_source.equalsIgnoreCase("")||three_source.equalsIgnoreCase("")||four_source.equalsIgnoreCase(""))
                                                {
                                                    Isupdatebillingadd="Y";
                                                }
                                                else
                                                {

                                                    if(two_source.equalsIgnoreCase(tovilano_edittext.getText().toString()))
                                                    {
                                                        Isupdatebillingadd="N";
                                                      //  if(three_source.equalsIgnoreCase(tocommname_edittext.getText().toString()))
                                                       // {
                                                            Isupdatebillingadd="N";
                                                            if(four_source.equalsIgnoreCase(tostreetadd_edittext.getText().toString()))
                                                            {
                                                                Isupdatebillingadd="N";
                                                            }
                                                            else
                                                            {
                                                                Isupdatebillingadd="Y";
                                                            }

                                                      //  }
                                                       // else
                                                       // {
                                                       //     Isupdatebillingadd="Y";
                                                       // }
                                                    }
                                                    else
                                                    {
                                                        Isupdatebillingadd="Y";
                                                    }
                                                }
                                                //////////////////

                                            }
                                            else
                                            {
                                                Isupdatebillingadd="Y";
                                            }

                                       // }
                                       // else
                                       // {
                                       //     Isupdatebillingadd="Y";
                                       // }
                                    }
                                    else
                                    {
                                        Isupdatebillingadd="Y";
                                    }
                                }
//---------------------------

                            }
                            else
                            {
                                Isupdatebillingadd="Y";
                            }



                    }
                    else
                    {
                        Isupdatebillingadd="Y";
                    }
                }
                else
                {
                    Isupdatebillingadd="Y";
                }

            }




            ////payment store////
         /*   edit_billingname= billingname_edittext.getText().toString();
            edit_bapt=vilano_edittext.getText().toString();
            edit_bcommunity=commname_edittext.getText().toString();
            edit_badd=streetadd_edittext.getText().toString();

            edit_fapt=fromvilano_edittext.getText().toString();
            edit_fcommunity=fromcommname_edittext.getText().toString();
            edit_fadd=fromstreetadd_edittext.getText().toString();

            edit_tapt=tovilano_edittext.getText();
            edit_tcommunity=tocommname_edittext.getText().toString();
            edit_tadd=tostreetadd_edittext.getText().toString();*/


            new GetJsonmovemyhome_save().execute();

        }
        else
        {
            UF.msg("Check Your Internet Connection.");
        }
    }
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_Goods_payment.this);
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
            int requiredproce=0;
            try
            {
                requiredproce=Integer.parseInt(Constants.promocode_discountstore)+Integer.parseInt(Constants.promocode_discount_value_store);

            }
            catch (Exception e)
            {

            }
            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            Constants.Isupdatebillingadd=Isupdatebillingadd;

            try {

                Log.e("Heenali primium", "");
                prmsLogin.put("NoOfTruck", Constants.store_NoOfTruck_g);
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", Constants.store_NoOfLabour_g);
                prmsLogin.put("NoOfHandiman", Constants.store_NoOfHandiman_g);
                prmsLogin.put("rate_type_flag", "B");


                if(payment_mode.equalsIgnoreCase("O"))
                {
                    prmsLogin.put("Isfinalorder", Isfinalorder);
                    prmsLogin.put("payment_mode",payment_mode);

                }
                else
                {
                    prmsLogin.put("Isfinalorder", "Y");
                    prmsLogin.put("payment_mode",payment_mode);

                }

                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");

                prmsLogin.put("shipper_id", Constants.store_shipperid);
                prmsLogin.put("email_id", Constants.store_email);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);
                //prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);

                Log.e("Constants.totlecost_g", String.valueOf(requiredproce));
                prmsLogin.put("required_price", requiredproce);
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

                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "shipper");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("goods_details",Constants.store_goodsdetails);

                prmsLogin.put("IncludePackingCharge", "N");
                prmsLogin.put("Isupdate", "Y");
                prmsLogin.put("order_type_flag", "GL");
                prmsLogin.put("TruckTypeCode", Constants.store_Hometypeid);


                prmsLogin.put("billing_add",Constants.storepayment_billling_add);
                prmsLogin.put("billing_name",Constants.storepayment_billingname);
                prmsLogin.put("inquiry_source_state", "");
                prmsLogin.put("payment_status", "Y");
                prmsLogin.put("destination_full_add",Constants.storepayment_movingtoadd);
                prmsLogin.put("source_full_add",Constants.storepayment_movingfromadd);
                prmsLogin.put("Isupdatebillingadd",Isupdatebillingadd);

                prmsLogin.put("promocode", Constants.promocode_store);

                prmsLogin.put("goods_weight",Constants.storepayment_goodsw);
                prmsLogin.put("goods_weightUOM",Constants.storepayment_goodswunit);


                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);
                Log.e("prmsLogin", prms + "");
                json_save = UF.RegisterUser("postorder/SaveMovingGoodsDetails", prms);

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

                        Log.e("payment data get ", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1"))
                        {


                            // UF.msg(message + "");
                            if(payment_mode.equalsIgnoreCase("O"))
                            {
                               /* Intent i = new Intent(Activity_payment.this,Activity_payment_online.class);
                                i.putExtra("page", page);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();*/

                                fortCallback = FortCallback.Factory.create();

                                android_id_sdk = FortSdk.getDeviceId (Activity_Goods_payment.this);
                                UF = new UserFunctions(Activity_Goods_payment.this);
                                Log.i("signature", android_id_sdk);

                                //test
                             // String text = "jgjsgcyfy6rfhkfaccess_code=gUErE32CkOPj2QGLqL97device_id="+android_id_sdk+"language=enmerchant_identifier=UIxcLVrJservice_command=SDK_TOKENjgjsgcyfy6rfhkf";

                                //live
                              String text ="reqtrukker2016access_code=ZbzswidlgkuYgQhgDViYdevice_id="+android_id_sdk+"language=enmerchant_identifier=GqJmbjUhservice_command=SDK_TOKENreqtrukker2016";

                                Log.e("hash key.....", text);

                                MessageDigest md = null;
                                try
                                {
                                    md = MessageDigest.getInstance("SHA-256");
                                    md.update(text.getBytes());

                                }
                                catch (NoSuchAlgorithmException e)
                                {
                                    e.printStackTrace();
                                }
                                byte byteData[] = md.digest();
                                for (int i = 0; i < byteData.length; i++)
                                {
                                    sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
                                }

                                System.out.println("Hex format : " + sb.toString());
                                new GetJson().execute();


                            }
                            else
                            {


                                JSONArray array = new JSONArray();
                                array = jobj.getJSONArray("data");

                                Log.e("array", array.toString());
                                Log.e("cbmlink", array.getJSONObject(0).getString("cbmlink").toString());

                                Constants.store_cbmlink=array.getJSONObject(0).getString("cbmlink").toString();


                                Intent i = new Intent(Activity_Goods_payment.this,Activity_Goods_Compelted.class);
                                i.putExtra("page", "");
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                finish();
                                UF.msg(message + "");
                            }



                        } else {
                            UF.msg(message + "");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


        }
    }
    public boolean validate() {
        boolean valid = true;

        String billname_vali=billingname_edittext.getText().toString();
        String vilano_edittext_vali = vilano_edittext.getText().toString();

        String streetadd_edittext_vali = streetadd_edittext.getText().toString();

        String fromvilano_edittext_vali = fromvilano_edittext.getText().toString();

        String fromstreetadd_edittext_vali = fromstreetadd_edittext.getText().toString();

        String tovilano_edittext_vali = tovilano_edittext.getText().toString();

        String tostreetadd_edittext_vali = tostreetadd_edittext.getText().toString();
        String goodsdetail_edittext_vali = goods_edittext.getText().toString();
        String goodsw = goodsw_edittext.getText().toString();

        if (goodsdetail_edittext_vali.isEmpty() )
        {
            goods_edittext.setError("Enter Valid Goods Details");

            valid = false;
        } else {
           goods_edittext.setError(null);
        }
        if (goodsw.isEmpty())
        {
            goodsw_edittext.setError("Enter Valid Goods weight");

            valid = false;
        } else {
            goodsw_edittext.setError(null);
        }

        if (billname_vali.isEmpty() ) {
            billingname_edittext.setError("Enter Valid Billing Name");

            valid = false;
        } else {
            billingname_edittext.setError(null);
        }


        if (tovilano_edittext_vali.isEmpty() ) {
            tovilano_edittext.setError("Enter Valid Premise No");

            valid = false;
        } else {
            tovilano_edittext.setError(null);
        }


        if ( tostreetadd_edittext_vali.isEmpty() ) {
            tostreetadd_edittext.setError("Enter valid street Address");

            valid = false;
        } else {
            tostreetadd_edittext.setError(null);
        }



        if (fromstreetadd_edittext_vali.isEmpty() ) {
            fromstreetadd_edittext.setError("Enter valid street Address");

            valid = false;
        } else {
            fromstreetadd_edittext.setError(null);
        }




        if (fromvilano_edittext_vali.isEmpty() ) {
            fromvilano_edittext.setError("Enter Valid Premise No");

            valid = false;
        } else {
            fromvilano_edittext.setError(null);
        }
////////////////////////////////

      /*  if (vilano_edittext_vali.isEmpty() )
        {
            vilano_edittext.setError("Enter Valid Premise No");

            valid = false;
        }
        else
        {
            vilano_edittext.setError(null);
        }*/



        if (streetadd_edittext_vali.isEmpty() ) {
            streetadd_edittext.setError("Enter Valid Street Address");

            valid = false;
        } else {
            streetadd_edittext.setError(null);
        }



        return  valid;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        //clear all data

       /* billingname_edittext.setText("");
         vilano_edittext.setText("");
        commname_edittext.setText("");
        streetadd_edittext.setText("");
        tovilano_edittext.setText("");
        tocommname_edittext.setText("");
        tostreetadd_edittext.setText("");

        fromcommname_edittext.setText("");
        fromvilano_edittext.setText("");
        fromstreetadd_edittext.setText("");
         movingfromadd_layout_value=0;movingtoadd_layout_value=0;online_layout_value=1;cashdelievery_layout_value=0;
        payment_mode="O";
        onlinedelivery_sucess.setBackgroundResource(R.drawable.success);
        movingfromadd_layout_value = 0;
        movingtoadd_layout_value=0;
        icon_sourceadd.setBackgroundResource(R.drawable.success_blank);
        icon_destadd.setBackgroundResource(R.drawable.success_blank);
        cashdelivery_sucess.setBackgroundResource(R.drawable.success_blank);

        cashdelievery_layout_value=0;
        online_layout_value=1;*/



    }
    private class GetJson extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_Goods_payment.this);
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
            try {

                try {

                    JSONObject prms = new JSONObject();
                    JSONObject prmsLogin = new JSONObject();
                    //live
                   prmsLogin.put("access_code", "ZbzswidlgkuYgQhgDViY");
                    prmsLogin.put("device_id", android_id_sdk);
                    prmsLogin.put("language", "en");
                    prmsLogin.put("merchant_identifier", "GqJmbjUh");
                    prmsLogin.put("service_command", "SDK_TOKEN");
                    prmsLogin.put("signature", sb.toString());

                    //test
                   /* prmsLogin.put("access_code", "gUErE32CkOPj2QGLqL97");
                    prmsLogin.put("device_id", android_id_sdk);
                    prmsLogin.put("language", "en");
                    prmsLogin.put("merchant_identifier", "UIxcLVrJ");
                    prmsLogin.put("service_command", "SDK_TOKEN");
                    prmsLogin.put("signature", sb.toString());*/

                    //test
                 // json_payfory = UF.RegisterUser_payfort("https://sbpaymentservices.payfort.com/FortAPI/paymentApi", prmsLogin);

                    //live
                    json_payfory = UF.RegisterUser_payfort("https://paymentservices.payfort.com/FortAPI/paymentApi", prmsLogin);

                    Log.e("latlngJson.. prms..",  prmsLogin.toString() );



                } catch (JSONException e) {

                }
            } catch (Exception e) {
            }
            return json_payfory;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.e("Result", json_payfory);
            loading.dismiss();
          // json_payfory="{"+json_payfory+"}";
            try
            {
                JSONObject jobj = new JSONObject(json_payfory);
                String apires=jobj.getString("sdk_token");
                Log.e("getquotealldata ", apires);
                Random r = new Random();
                int i1 = r.nextInt(1880 - 10) + 65;
                int aed_amount=Integer.parseInt(amount)*100;
                Map<String, String> map1 = new HashMap<String, String>();
                map1.put("amount", String.valueOf(aed_amount));
                map1.put("command", "PURCHASE");
                map1.put("currency", "AED");
                map1.put("customer_email", sm.getemailid());
                map1.put("installments", "");
                map1.put("language", "en");
                map1.put("merchant_reference", String.valueOf(i1)+"6456456");
                map1.put("sdk_token",apires.toString() );
                map1.put("payment_option", "");
                map1.put("token_name", apires.toString());

                FortRequest g=new FortRequest();
                g.setRequestMap(map1);
                g.setShowResponsePage(true);

                try {
                    FortSdk.getInstance().registerCallback(Activity_Goods_payment.this, g,FortSdk.ENVIRONMENT.PRODUCTION , 5, fortCallback, new FortInterfaces.OnTnxProcessed() {
                        @Override
                        public void onCancel(Map<String, String> requestParamsMap, Map<String, String> responseMap) {
                            Toast.makeText(getApplicationContext(),"Payment Cancelled",Toast.LENGTH_SHORT).show();
                            android_id_sdk="";

                            // finish();

                        }

                        @Override
                        public void onFailure(Map<String, String> requestParamsMap, Map<String,
                                String> fortResponseMap)
                        {
                            Toast.makeText(getApplicationContext(),"Payment failured ",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onSuccess(Map<String, String> requestParamsMap, Map<String, String> fortResponseMap)
                        {


                            String value="";
                            for (Map.Entry entry : fortResponseMap.entrySet())
                            {
                                Log.e("payment......", "......");
                                System.out.println(entry.getKey() + ", " + entry.getValue());
                                value = new Gson().toJson(fortResponseMap);

                                System.out.println(value);


                            }
                            Constants.payment_json=value;
                            Constants.mycheck="O";
                            Intent i = new Intent(Activity_Goods_payment.this,Activity_Goods_Compelted.class);
                            i.putExtra("page", "online");
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();


                        }
                    });
                } catch (Exception e)
                {
                    e.printStackTrace();
                }


            } catch (JSONException e)
            {
                e.printStackTrace();
            }


        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            fortCallback.onActivityResult(requestCode,resultCode,data);
        }
        catch (Exception e)
        {

        }

    }
    private class GetJson_draft extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_Goods_payment.this);
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
            Constants.Isupdatebillingadd=Isupdatebillingadd;
            int requiredproce=0;
            try
            {
                requiredproce=Integer.parseInt(Constants.promocode_discountstore)+Integer.parseInt(Constants.promocode_discount_value_store);

            }
            catch (Exception e)
            {

            }
            try {

                Log.e("Heenali primium", "");
                prmsLogin.put("NoOfTruck", Constants.store_NoOfTruck_g);
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", Constants.store_NoOfLabour_g);
                prmsLogin.put("NoOfHandiman", Constants.store_NoOfHandiman_g);
                prmsLogin.put("rate_type_flag", "B");


                if(payment_mode.equalsIgnoreCase("O"))
                {
                    prmsLogin.put("Isfinalorder", Isfinalorder);
                    prmsLogin.put("payment_mode",payment_mode);

                }
                else
                {
                    prmsLogin.put("Isfinalorder", "Y");
                    prmsLogin.put("payment_mode",payment_mode);

                }


                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");

                prmsLogin.put("shipper_id", Constants.store_shipperid);
                prmsLogin.put("email_id", Constants.store_email);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);
                //prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);
                prmsLogin.put("required_price", String.valueOf(requiredproce));
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

                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "shipper");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("goods_details",Constants.store_goodsdetails);

                prmsLogin.put("IncludePackingCharge", "N");
                prmsLogin.put("Isupdate", "Y");
                prmsLogin.put("order_type_flag", "GL");
                prmsLogin.put("TruckTypeCode", Constants.store_Hometypeid);


                prmsLogin.put("billing_add",Constants.storepayment_billling_add);
                prmsLogin.put("billing_name",Constants.storepayment_billingname);
                prmsLogin.put("inquiry_source_state", "");
                prmsLogin.put("payment_status", "Y");
                prmsLogin.put("destination_full_add",Constants.storepayment_movingtoadd);
                prmsLogin.put("source_full_add",Constants.storepayment_movingfromadd);
                prmsLogin.put("Isupdatebillingadd",Isupdatebillingadd);
                prmsLogin.put("promocode", Constants.promocode_store);

                prmsLogin.put("goods_weight",Constants.storepayment_goodsw);
                prmsLogin.put("goods_weightUOM",Constants.storepayment_goodswunit);


                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);
                Log.e("prmsLogin", prms + "");
                json_save = UF.RegisterUser("postorder/SaveOrderDraftDetails", prms);

            } catch (JSONException e) {

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

                        Log.e("payment data get ", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1"))
                        {


                             UF.msg(message + "");

                        }
                        else
                        {
                            UF.msg(message + "");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }
    }
    void converttimeformate(String timename_store) throws ParseException
    {
        Log.e("time:",timename_store);
        timename_store = timename_store.replaceAll("0", "");
        timename_store = timename_store.replaceAll(":", "");
        Constants.store_time_display=timename_store+" AM";
    }
    class GooglePlacesAutocompleteAdapter_so extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter_so(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {

                        resultList = autocomplete_so(constraint.toString());

                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }
    public static ArrayList<String> autocomplete_so(String input) {
        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);

            sb.append("&input=" + URLEncoder.encode(input, "utf8") );


            URL url = new URL(sb.toString());
            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {

            return resultList;
        } catch (IOException e) {

            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {

        }

        return resultList;
    }

}

