package com.trukker.trukkershipperuae.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ravi on 29/07/15.
 */


public class Dialog_postlodedetail extends Activity
{

    //class object declaration
    UserFunctions UF;
    ///////////////
    //all reqired string declartion
    String json_save;
    /////////////////////

    //all control declartion
    EditText fullname,emailid,mobno,date;
    TextView btn_signup,btn_cancel;
    String android_id;
    TextView link_login;
    Calendar cal;
    DatePickerDialog.OnDateSetListener datePicker;
    ///////////////////
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_postlodedetails);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        //init() contains all control declaration
        init();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(btn_signup.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);
                signup();
            }
        });
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();

            }
        });
        date.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(date.getWindowToken(), 0);

                DatePickerDialog datePicker1 = new DatePickerDialog(Dialog_postlodedetail.this, R.style.AppTheme_Dark_Dialog, datePicker, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                datePicker1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker1.show();
                date.setError(null);
                // time_edittext.setText("");

            }
        });

    }
    public  void init()
    {
        UF = new UserFunctions(Dialog_postlodedetail.this);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        fullname=(EditText)findViewById(R.id.fullname_edittext);
        emailid=(EditText)findViewById(R.id.emailid_edittext);
        mobno=(EditText)findViewById(R.id.mobileno_edittext);
        date=(EditText)findViewById(R.id.shippingdate_edittext);
        btn_signup=(TextView)findViewById(R.id.submit_btn);
        btn_cancel=(TextView)findViewById(R.id.cancle_btn);
        createDatePicker();
        //links clicks



    }
    private class GetJson_save extends AsyncTask<Void, Void, String>
    {

        private ProgressDialog  loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Dialog_postlodedetail.this);
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

                prmsLogin.put("EmailID", emailid.getText().toString());
                prmsLogin.put("Message", "");
                prmsLogin.put("Source", "ORDER_REQ");
                prmsLogin.put("mobile_no", android_id.toString());
                prmsLogin.put("name", fullname.getText().toString());
                prmsLogin.put("subject", "Request for Moving Home");
                prmsLogin.put("ShippingDatetime", date.getText().toString());
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM",Constants.store_unit);

                jsonArray.put(prmsLogin);

                prms.put("Email", jsonArray);


            }
            catch (JSONException e)
            {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("mailer/SaveQuotationRequestMail", prms);

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
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1"))
                        {

                            UF.msg(message + "");

                            Intent intent = new Intent(Dialog_postlodedetail.this, MainActivity.class);
                            startActivity(intent);
                            overridePendingTransition(R.anim.slide_out_up, R.anim.slide_in_up);
                            finish();
                            //UF.msg("Registration Successfully.");

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
    //validation function
    public boolean validate()
    {
        boolean valid = true;

       /* fullname=(EditText)findViewById(R.id.fullname_text);
        emailid=(EditText)findViewById(R.id.emailid_text);
        mobno=(EditText)findViewById(R.id.mobileno_text);
        pass=(EditText)findViewById(R.id.pass_text);
        conpass=(EditText)findViewById(R.id.confopass_text);
        btn_signup=(Button)findViewById(R.id.btn_signup);
        link_login=(TextView)findViewById(R.id.link_login);*/

        String name = fullname.getText().toString().trim();
        String email = emailid.getText().toString().trim();
        String sdate = date.getText().toString().trim();
        String mobile_number = mobno.getText().toString();



        if (name.isEmpty() )
        {
            fullname.setError("Enter a valid Name");
            valid = false;
        }
        else
        {
            fullname.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailid.setError("Enter a valid email address");
            valid = false;
        } else {
            emailid.setError(null);
        }

        if (sdate.isEmpty() ) {
            date.setError("Enter valid Date");
            valid = false;
        } else {
            date.setError(null);
        }


        if (mobile_number.isEmpty() || mobile_number.length() < 10) {
            mobno.setError("Please enter valid mobile number");
            valid = false;
        } else {
            mobno.setError(null);
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

        btn_signup.setEnabled(false);


        onSignupSuccess();

    }
    public void onSignupFailed()
    {
        Toast.makeText(getBaseContext(), "failed", Toast.LENGTH_LONG).show();
        btn_signup.setEnabled(true);

    }
    public void onSignupSuccess()
    {
        btn_signup.setEnabled(true);
        setResult(RESULT_OK, null);
         new GetJson_save().execute();


    }

    @Override
    public void onResume()
    {
        super.onResume();

        //clear all data

        fullname.setText("");
        emailid.setText("");
        mobno.setText("");
       date.setText("");


    }
    public void createDatePicker()
    {
        cal = Calendar.getInstance();
        datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                // TODO Auto-generated method stub
                cal.set(Calendar.YEAR, year);
                cal.set(Calendar.MONTH, monthOfYear);
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String selected_year = String.valueOf(cal.get(Calendar.YEAR) + "");
                String month = String.valueOf(cal.get(Calendar.MONTH) + 1);

                String day = String.valueOf(cal
                        .get(Calendar.DAY_OF_MONTH));

                if (month.length() == 1) {
                    month = "0" + month;
                } else {
                    //tv_dig_MM.setText(month);
                }

                if (day.length() == 1) {
                    //tv_dig_DD.setText("0" + day);
                    day = "0" + day;
                } else {
                    //tv_dig_DD.setText(day);
                }
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                String s = sdf.format(cal.getTime());
                date.setText(s);
            }

        };
    }
}
