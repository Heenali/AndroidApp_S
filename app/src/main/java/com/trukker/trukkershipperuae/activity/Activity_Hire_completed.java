package com.trukker.trukkershipperuae.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;


public class Activity_Hire_completed extends AppCompatActivity
{
    TextView orderid_text,back;
    Button continue_booklater;
    String json_save,android_id;

    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog alertDialog = new AlertDialog.Builder(Activity_Hire_completed.this, R.style.AppTheme_Dark_Dialog).create();
            alertDialog.setTitle("Alert");
            alertDialog.setMessage("You can not update Order");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
            alertDialog.show();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goodscompleted);
        init();
        Constants.storemail_send_goods="0";

        Intent i = getIntent();
        String pagename = i.getStringExtra("page");
        if(pagename.equalsIgnoreCase("online"))
        {
            new GetJsonmovemyhome_save().execute();
        }
        orderid_text.setText(Constants.store_loadinquiry_no);

        continue_booklater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.goodscompleted_moving = "Hire_congratulations";
                Intent i = new Intent(Activity_Hire_completed.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();

            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                AlertDialog alertDialog = new AlertDialog.Builder(Activity_Hire_completed.this, R.style.AppTheme_Dark_Dialog).create();
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
        });

    }

    //call service for send mobile no & get OTP number for change password
    public void init()
    {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm=new SessionManager(Activity_Hire_completed.this);
        UF=new UserFunctions(Activity_Hire_completed.this);
        cd= new ConnectionDetector(Activity_Hire_completed.this);
        orderid_text=(TextView)findViewById(R.id.orderid_text);
        back=(TextView)findViewById(R.id.back);
        continue_booklater=(Button)findViewById(R.id. continue_booklater);
    }
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_Hire_completed.this);
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
            String paymentobject= Constants.payment_json.toString();
            Log.e("prmsLogin payment111111", paymentobject.toString());
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
                Log.e("prmsLogin payment", prmsLogin + "");
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

                        Log.e("payment data get ", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1")) {
                            Constants.Isupdatebillingadd="N";
                            Constants.payment_json="";

                        }
                        if(status.equalsIgnoreCase("0"))
                        {
                            AlertDialog alertDialog = new AlertDialog.Builder(Activity_Hire_completed.this, R.style.AppTheme_Dark_Dialog).create();
                            alertDialog.setTitle("payment failed");
                            alertDialog.setCancelable(false);
                            alertDialog.setMessage("Error : "+message);
                            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which)
                                        {
                                            dialog.dismiss();
                                        }
                                    });
                            alertDialog.show();
                        }
                        else
                        {
                            UF.msg(message + "");
                        }

                    }
                    catch (Exception e)
                    {

                    }
                }
            }

        }
    }
}


