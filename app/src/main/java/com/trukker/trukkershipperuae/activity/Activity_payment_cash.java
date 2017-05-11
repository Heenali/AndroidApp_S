package com.trukker.trukkershipperuae.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
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

import java.util.Iterator;


/**
 * Created by admin on 8/31/2016.
 */
public class Activity_payment_cash extends AppCompatActivity
{
    ToggleButton oldhouse,newhouse,extraservices,pestservices;
    TextView cancle_btn,submit_btn;
    String oldhouse_value="N",newhouse_value="N",extraservices_value="N",pestservices_value="N";
    String json_save;
    String  android_id;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    String json_mess="Mooving Goods";
    String page;
    TextView clicktext,order_id;
    LinearLayout housepaint_layout,houseclean_layout,controlservices_layout,extraservices_layout;
    int housepaint_layout_flag=0,houseclean_layout_flag=0,controlservices_layout_flag=0,extraservices_layout_flag=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

               // Intent i = new Intent(Activity_payment_cash.this,Activity_payment.class);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               // i.putExtra("page",page);
               // startActivity(i);
              //  overridePendingTransition(R.anim.left_in, R.anim.right_out);
               // finish();

            AlertDialog alertDialog = new AlertDialog.Builder(Activity_payment_cash.this,R.style.AppTheme_Dark_Dialog).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("You can not update Order");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();


        }
        return super.onKeyDown(keyCode, event);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_cash);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        Constants.storemail_send="0";

        Intent i = getIntent();
        page= i.getStringExtra("page");

        if(Constants.mycheck.equalsIgnoreCase("O"))
        {
            new GetJsonmovemyhome_save().execute();
        }
        init();
        String htmlText_id ="Order No : " +
                "<font color='#46C37B'>"+
                Constants.store_loadinquiry_no+" </font>";
        order_id.setText(Html.fromHtml(htmlText_id));
        String htmlText ="Make sure to fill in details of your moving contents so everything's perfect on the day of your move.\n" +
                "Click <font color='#A3CFFF'> HERE </font>";
       clicktext.setText(Html.fromHtml(htmlText));

        ObjectAnimator textColorAnim;
        textColorAnim = ObjectAnimator.ofInt(clicktext, "textColor", Color.RED, Color.rgb(34, 139, 34));
        textColorAnim.setDuration(1000);
        textColorAnim.setEvaluator(new ArgbEvaluator());
        textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
        textColorAnim.setRepeatMode(ValueAnimator.REVERSE);
        textColorAnim.start();


        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cd.isConnectingToInternet()) {

                    if (oldhouse_value.equalsIgnoreCase("N") && newhouse_value.equalsIgnoreCase("N") && extraservices_value.equalsIgnoreCase("N") && pestservices_value.equalsIgnoreCase("N")) {
                        Toast.makeText(getApplicationContext(), "Please Select option", Toast.LENGTH_SHORT).show();

                    } else {
                        if (oldhouse_value.equalsIgnoreCase("N")) {
                            json_mess = json_mess;
                        } else {
                            json_mess = json_mess + ",Painting";
                        }
                        if (newhouse_value.equalsIgnoreCase("N")) {
                            json_mess = json_mess;
                        } else {
                            json_mess = json_mess + ",Cleaning";
                        }

                        if (pestservices_value.equalsIgnoreCase("N")) {

                        } else {
                            json_mess = json_mess + ",Pest Control";
                        }

                        if (extraservices_value.equalsIgnoreCase("N")) {

                        } else {
                            json_mess = json_mess + ",Extra Storage";
                        }


                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Activity_payment_cash.this);
                        builder.setMessage("Are you sure want to submit?");
                        // builder.setTitle("New Order");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                new GetJson_save().execute();

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

                } else {
                    UF.msg("Check Your Internet Connection.");
                }

            }
        });
       cancle_btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Constants.paymentcompleted_order = "paymentcompleted_order";
               Intent i = new Intent(Activity_payment_cash.this, MainActivity.class);
               i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
               startActivity(i);
               overridePendingTransition(R.anim.left_in, R.anim.right_out);
               finish();


           }
       });
        housepaint_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (housepaint_layout_flag == 0) {
                    oldhouse_value = "Painting";
                    housepaint_layout_flag = 1;
                    oldhouse.setBackgroundResource(R.drawable.square_checkselect);

                } else {
                    oldhouse_value = "N";
                    oldhouse.setBackgroundResource(R.drawable.square_checkblanck);
                    housepaint_layout_flag = 0;
                }
            }
        });
        houseclean_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (houseclean_layout_flag == 0) {
                    newhouse_value = "Cleaning";

                    houseclean_layout_flag = 1;
                    newhouse.setBackgroundResource(R.drawable.square_checkselect);

                } else {
                    newhouse_value = "N";
                    newhouse.setBackgroundResource(R.drawable.square_checkblanck);
                    houseclean_layout_flag = 0;
                }
            }
        });
        controlservices_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(controlservices_layout_flag==0)
                {
                    pestservices_value = "Pest Control";
                    controlservices_layout_flag=1;
                    pestservices.setBackgroundResource(R.drawable.square_checkselect);

                }else
                {
                    pestservices_value = "N";
                    pestservices.setBackgroundResource(R.drawable.square_checkblanck);
                    controlservices_layout_flag=0;
                }
            }
        });
        extraservices_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(extraservices_layout_flag==0)
                {
                    extraservices_value = "Extra Storage";
                    extraservices_layout_flag=1;
                    extraservices.setBackgroundResource(R.drawable.square_checkselect);

                }else
                {
                    extraservices_value = "N";
                    extraservices.setBackgroundResource(R.drawable.square_checkblanck);
                    extraservices_layout_flag=0;
                }
            }
        });
        oldhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (housepaint_layout_flag == 0) {
                    oldhouse_value = "Painting";
                    housepaint_layout_flag = 1;
                    oldhouse.setBackgroundResource(R.drawable.square_checkselect);

                } else {
                    oldhouse_value = "N";
                    oldhouse.setBackgroundResource(R.drawable.square_checkblanck);
                    housepaint_layout_flag = 0;
                }
            }
        });
        extraservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(extraservices_layout_flag==0)
                {
                    extraservices_value = "Extra Storage";
                    extraservices_layout_flag=1;
                    extraservices.setBackgroundResource(R.drawable.square_checkselect);

                }else
                {
                    extraservices_value = "N";
                    extraservices.setBackgroundResource(R.drawable.square_checkblanck);
                    extraservices_layout_flag=0;
                }
            }
        });
        pestservices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(controlservices_layout_flag==0)
                {
                    pestservices_value = "Pest Control";
                    controlservices_layout_flag=1;
                    pestservices.setBackgroundResource(R.drawable.square_checkselect);

                }else
                {
                    pestservices_value = "N";
                    pestservices.setBackgroundResource(R.drawable.square_checkblanck);
                    controlservices_layout_flag=0;
                }
            }
        });
        newhouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (houseclean_layout_flag == 0) {
                    newhouse_value = "Cleaning";

                    houseclean_layout_flag = 1;
                    newhouse.setBackgroundResource(R.drawable.square_checkselect);

                } else {
                    newhouse_value = "N";
                    newhouse.setBackgroundResource(R.drawable.square_checkblanck);
                    houseclean_layout_flag = 0;
                }
            }
        });


        clicktext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.store_cbmlink));
                startActivity(browserIntent);
            }
        });
    }
    public void init()
    {

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_payment_cash.this);
        UF = new UserFunctions(Activity_payment_cash.this);
        cd = new ConnectionDetector(Activity_payment_cash.this);

        submit_btn=(TextView)findViewById(R.id.submit_btn);
        cancle_btn=(TextView)findViewById(R.id.cancel_btn);
        oldhouse=(ToggleButton)findViewById(R.id.toggle_oldhouse);
        newhouse=(ToggleButton)findViewById(R.id.toggle_newhouse);
        pestservices=(ToggleButton)findViewById(R.id.toggle_pestservices);
        extraservices=(ToggleButton)findViewById(R.id.toggle_extraservices);
        clicktext=(TextView)findViewById(R.id.clicktext);
        order_id=(TextView)findViewById(R.id.order_id);


                housepaint_layout=(LinearLayout)findViewById(R.id.housepaint_layout);
                houseclean_layout=(LinearLayout)findViewById(R.id.houseclean_layout);
                controlservices_layout=(LinearLayout)findViewById(R.id.controlservices_layout);
                extraservices_layout=(LinearLayout)findViewById(R.id.extraservices_layout);

    }
    private class GetJson_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_payment_cash.this);
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

                prmsLogin.put("EmailID",sm.getemailid().toString());
                prmsLogin.put("Message",json_mess.toString());
                prmsLogin.put("Source", "ADDON");
                prmsLogin.put("shipper_idt",sm.getUniqueId().toString());
                prmsLogin.put("name", sm.getUserName().toString());
                prmsLogin.put("subject", json_mess.toString());
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);
                prmsLogin.put("mobile_no", sm.getUserId());


                jsonArray.put(prmsLogin);
                prms.put("Email", jsonArray);


            }
            catch (JSONException e)
            {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("Mailer/SaveAddOnMailLogDetails", prms);

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
                            Constants.paymentcompleted_order="paymentcompleted_order";
                            Intent i = new Intent(Activity_payment_cash.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            finish();


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
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String>
    {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_payment_cash.this);
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
            String paymentobject=Constants.payment_json.toString();

            JSONArray jsonArray = new JSONArray();

            try {

                JSONObject pay_obj = new JSONObject(paymentobject);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);
                prmsLogin.put("created_host",android_id.toString());
                prmsLogin.put("created_by", "shipper");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type","M");
                prmsLogin.put("Isupdatebillingadd", Constants.Isupdatebillingadd);

               // jsonArray.put(prmsLogin);

               // prms.put("PostOrderParameter", jsonArray);
                Log.e("prmsLogin", prmsLogin + "");
                JSONObject merged = new JSONObject();
                JSONObject[] objs = new JSONObject[] { pay_obj, prmsLogin };
                for (JSONObject obj : objs) {
                    Iterator it = obj.keys();
                    while (it.hasNext())
                    {
                        String key = (String)it.next();
                        merged.put(key, obj.get(key));
                    }
                }

                Log.e("merged........",merged.toString());
                json_save = UF.RegisterUser("payment/SaveTransactionDetails", merged);

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
                            Constants.Isupdatebillingadd="N";
                            Constants.payment_json="";
                             UF.msg(message + "");



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
}