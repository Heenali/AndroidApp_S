package com.trukker.trukkershipperuae.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by admin on 8/31/2016.
 */
public class Activity_payment_online extends AppCompatActivity
{
    String page;
    EditText emailid_edittext,cardname_edittext,cvv_edittext,cardno_edittext;
    Spinner cardexpmonth_edittext,cardexpyear_edittext;;
    TextView cancle_btn,pay_btn;
    String android_id;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    String json_save;
    List<Integer> list_year;
    String monthdigit_store;
    String total_cost;
    String ex_year;
    String ex_month;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

            Intent i = new Intent(Activity_payment_online.this,Activity_payment.class);
            i.putExtra("page",page);
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
        setContentView(R.layout.activity_payment_online);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();
        if(page.equalsIgnoreCase("standard"))
        {
            total_cost=Constants.store_totalcost_s;
            pay_btn.setText("Pay AED "+Constants.store_totalcost_s);
        }
        else
        {
            total_cost=Constants.store_totalcost_p;
            pay_btn.setText("Pay AED "+Constants.store_totalcost_p);
        }

        pay_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        cardexpmonth_edittext.setPrompt("SELECT YEAR");
        ArrayAdapter adapter4 = ArrayAdapter.createFromResource(this, R.array.spinner_year, R.layout.list_item);
        cardexpmonth_edittext.setAdapter(adapter4);
        cardexpmonth_edittext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                ex_month = cardexpmonth_edittext.getSelectedItem().toString();
                //Toast.makeText(getBaseContext(), ex_month, Toast.LENGTH_SHORT).show();
                 monthdigit_store=checkmonth(ex_month);
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });

        int year=2016;
        list_year = new ArrayList<>();
        for(int i=0;i<=20;i++)
        {
            list_year.add(year+i);
        }
        ArrayAdapter<Integer> adp1 = new ArrayAdapter<Integer>
                (getApplicationContext(), R.layout.list_year, list_year);

        cardexpyear_edittext.setAdapter(adp1);

        cardexpyear_edittext.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub

                ex_year=list_year.get(arg2).toString();
               //Toast.makeText(getBaseContext(), list_year.get(arg2),Toast.LENGTH_SHORT).show();
            }

            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Activity_payment_online.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });


    }
    public void init()
    {

        Intent i = getIntent();
        page= i.getStringExtra("page");

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_payment_online.this);
        UF = new UserFunctions(Activity_payment_online.this);
        cd = new ConnectionDetector(Activity_payment_online.this);

        emailid_edittext=(EditText)findViewById(R.id.emailid_edittext);
        cardname_edittext=(EditText)findViewById(R.id.cardname_edittext);
        cvv_edittext=(EditText)findViewById(R.id.cvv_edittext);
        cardno_edittext=(EditText)findViewById(R.id.cardno_edittext);

        cardexpmonth_edittext=(Spinner)findViewById(R.id.cardexpmonth_edittext);
        cardexpyear_edittext=(Spinner)findViewById(R.id.cardexpyear_edittext);
        cancle_btn=(TextView)findViewById(R.id.cancle_btn);
        pay_btn=(TextView)findViewById(R.id.pay_btn);




    }
    private class GetJson_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_payment_online.this);
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

                prmsLogin.put("email", emailid_edittext.getText().toString());
                prmsLogin.put("Cvc", cvv_edittext.getText().toString());
                prmsLogin.put("Name", cardname_edittext.getText().toString());


                if(Constants.promocode_discountstore.equalsIgnoreCase(""))
                {
                    prmsLogin.put("amount",total_cost);
                    Log.e("dddddd",total_cost);

                }
                else
                {
                    prmsLogin.put("amount", Constants.promocode_discountstore);
                    Log.e("ffffff",  Constants.promocode_discountstore);

                }
                /////////////////////////////////

                if(page.equalsIgnoreCase("standard"))
                {
                    prmsLogin.put("Total_cost",Constants.store_totalcost_s);
                }
                else
                {
                    prmsLogin.put("Total_cost", Constants.store_totalcost_p);
                }


                prmsLogin.put("exp_month",monthdigit_store);
                prmsLogin.put("exp_year",ex_year);
                prmsLogin.put("load_inquiry_no", Constants.store_loadinquiry_no);
                prmsLogin.put("number",cardno_edittext.getText().toString());
                prmsLogin.put("shipper_id",Constants.store_shipperid);
                prmsLogin.put("promocode", Constants.promocode_store);
                prmsLogin.put("created_by", "shipper");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");




                jsonArray.put(prmsLogin);

                prms.put("CardDetails", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("login/CreatePayment", prms);

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

                            JSONArray array = new JSONArray();
                           // array = jobj.getJSONArray("data");

                            UF.msg( message + "");
                            Intent i = new Intent(Activity_payment_online.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            finish();

                        }
                        else {
                            UF.msg("failed" + "");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


        }
    }
    public boolean validate()
    {
        boolean valid = true;

       // EditText emailid_edittext,cardname_edittext,cvv_edittext;
        String name = cardname_edittext.getText().toString().trim();
        String email = emailid_edittext.getText().toString().trim();
        String cardno = cardno_edittext.getText().toString().trim();
        String cvv = cvv_edittext.getText().toString();

        if (cvv.isEmpty())
        {
            cvv_edittext.setError("Enter CVV");

            valid = false;
        } else {
            cvv_edittext.setError(null);
        }

        if (name.isEmpty() || name.length() < 3)
        {
            cardname_edittext.setError("at least 3 characters");

            valid = false;
        } else {
            cardname_edittext.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailid_edittext.setError("enter a valid email address");
            valid = false;
        } else {
            emailid_edittext.setError(null);
        }

        if (cardno.isEmpty() || cardno.length() < 10)
        {
            cardno_edittext.setError("Please enter valid card number");
            valid = false;
        } else {
            cardno_edittext.setError(null);
        }

        return valid;
    }
    public void signup()
    {
        Log.d("mess", "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        pay_btn.setEnabled(false);


        onSignupSuccess();

    }
    public void onSignupFailed()
    {
        Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();

        pay_btn.setEnabled(true);
    }
    public void onSignupSuccess()
    {
        pay_btn.setEnabled(true);
        setResult(RESULT_OK, null);
        if (cd.isConnectingToInternet())
        {

            android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(Activity_payment_online.this);
            builder.setMessage("Are you sure want to online Payment?");
            builder.setTitle("New Order");

            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
            {
                public void onClick(DialogInterface dialog, int which)
                {
                    new GetJson_save().execute();

                }
            });

            builder.setNegativeButton("Cancel",new DialogInterface.OnClickListener()
                    {
                        public void onClick(DialogInterface dialog, int which)
                        {
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
    @Override
    public void onResume()
    {
        super.onResume();
        //clear all data



    }


    public String checkmonth(String ex_month_digit)
    {
        if(ex_month.equalsIgnoreCase("January"))
        {
            ex_month_digit="1";
        }
        else  if(ex_month.equalsIgnoreCase("February"))
        {
            ex_month_digit="2";
        }
        else  if(ex_month.equalsIgnoreCase("March"))
        {
            ex_month_digit="3";
        }
        else  if(ex_month.equalsIgnoreCase("April"))
        {
            ex_month_digit="4";
        }
        else  if(ex_month.equalsIgnoreCase("May"))
        {
            ex_month_digit="5";
        }
        else  if(ex_month.equalsIgnoreCase("June"))
        {
            ex_month_digit="6";
        }
        else  if(ex_month.equalsIgnoreCase("July"))
        {
            ex_month_digit="7";
        }
        else  if(ex_month.equalsIgnoreCase("August"))
        {
            ex_month_digit="8";
        }
        else  if(ex_month.equalsIgnoreCase("September"))
        {
            ex_month_digit="9";
        } else  if(ex_month.equalsIgnoreCase("October"))
        {
            ex_month_digit="10";
        } else  if(ex_month.equalsIgnoreCase("November"))
        {
            ex_month_digit="11";
        } else  if(ex_month.equalsIgnoreCase("December"))
        {
            ex_month_digit="12";
        }


return  ex_month_digit;


    }


}