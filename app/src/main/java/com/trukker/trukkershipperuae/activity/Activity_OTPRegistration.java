package com.trukker.trukkershipperuae.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by user2 on 3/2/2016.
 */
public class Activity_OTPRegistration extends AppCompatActivity
{

    //all control declartion
    Button btn_submit;
    EditText input_otp1,input_otp2,input_otp3,input_otp4;
    View view;
    //////////////////

    //class object declaration
    SessionManager sm;
    UserFunctions UF;
    ConnectionDetector cd;
    //////////////////

    //all reqired string declartion
    String json_save;
    String user_id,otp_no;
    //////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_optregistration);

        //init() contains all control declaration
        init();

        requestFocusChangeListener();

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view = v;
                if (validate()) {

                    otp_no=input_otp1.getText().toString().trim()+input_otp2.getText().toString().trim()+input_otp3.getText().toString().trim()+input_otp4.getText().toString().trim();

                    if(input_otp1.getText().length()>0&&input_otp2.length()>0&&input_otp3.length()>0&&input_otp4.length()>0)
                    {
                        if(cd.isConnectingToInternet())
                        {
                            Save_Otp_Json save= new Save_Otp_Json();
                            save.execute();
                        }
                        else {
                            UF.msg("Check Your Internet Connection.");
                        }
                    }
                    else {
                        Toast.makeText(Activity_OTPRegistration.this,"OTP Number Is Wrong",Toast.LENGTH_LONG).show();
                    }


                }
                // checkPermission();
            }
        });


    }

    // this is validation method
    public boolean validate() {
        boolean valid = true;

        String otp1 = input_otp1.getText().toString().trim();
        String otp2 = input_otp1.getText().toString().trim();
        String otp3 = input_otp1.getText().toString().trim();
        String otp4 = input_otp1.getText().toString().trim();

//        || otp.length() < 4
        if (otp1.isEmpty()||otp2.isEmpty()||otp3.isEmpty()||otp4.isEmpty())
        {
            Toast.makeText(getApplication(),"Enter OTP",Toast.LENGTH_SHORT).show();
            valid = false;
        }
        else
        {
            input_otp1.setError(null);
        }

        return valid;
    }


    // this method is use for Focus Change
    public void requestFocusChangeListener() {
        input_otp1.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (input_otp1.getText().toString().length() == 1)     //size as per your requirement
                {
                    input_otp2.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        input_otp2.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (input_otp2.getText().toString().length() == 1)     //size as per your requirement
                {
                    input_otp3.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });


        input_otp3.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub
                if (input_otp3.getText().toString().length() == 1)     //size as per your requirement
                {
                    input_otp4.requestFocus();
                }
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
            }

        });
    }


    public class Save_Otp_Json extends AsyncTask<Void,Void,String> {
        private ProgressDialog  loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = new ProgressDialog(Activity_OTPRegistration.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);

        }

        @Override
        protected String doInBackground(Void... params) {

            try {

                JSONObject prms = new JSONObject();
                JSONObject prmsLogin = new JSONObject();

                prmsLogin.put("user_id", user_id);
                prmsLogin.put("otp", otp_no);
                prms.put("User", prmsLogin);
                Log.e("prmsLogin", prms + "");
                json_save = UF.LoginUser("Login/ValidateOTP", prms);
            }catch (Exception e){

            }
            Log.e("jsonLogin", json_save+"");
            return json_save;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           loading.dismiss();

            Log.e("json", json_save + "");
            try {
                if(json_save!=null)
                {
                    if (json_save.equals("lost")) {
                        UF.msg("Connection Problem.");
                    }else{
                        if(json_save.equalsIgnoreCase("0"))
                        {
                            UF.msg("OTP Number Not Match");
                            Toast.makeText(Activity_OTPRegistration.this,"OTP not Match Please send right OTP",Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            JSONObject jobj= new JSONObject(json_save);
                            String status=jobj.getString("status");
                            if(status.equalsIgnoreCase("1"))
                            {
                                JSONArray jArray=jobj.getJSONArray("message");
                                JSONObject jobj1=jArray.getJSONObject(0);
                                String first_name  = jobj1.getString("first_name");
                                String email_id1  =jobj1.getString("email_id");

                                String user_id=jobj1.getString("user_id");
                                String role_id  =jobj1.getString("role_id");
                                String unique_id = jobj1.getString("unique_id");

                                String password  =jobj1.getString("password");
                                String client_type = jobj1.getString("client_type");

                                String middle_name = jobj1.getString("middle_name");
                                String last_name = jobj1.getString("last_name");

                                //store session value its value same as in login form
                                sm.createLoginSession(user_id);
                                sm.setUserId(user_id, first_name, last_name, middle_name, password, client_type, unique_id, role_id, email_id1);

                                ////////////////////////


                                Constants.moveing_page="Login";
                                ////Login Successfully. moving
                                Intent i = new Intent(Activity_OTPRegistration.this,MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                finish();


                                UF.msg("Login Successfully.");
                            }
                            else {
                                Toast.makeText(Activity_OTPRegistration.this,"OTP not Match Please send right OTP",Toast.LENGTH_LONG).show();

                            }

                        }

                    }
                }
                else
                {
                    UF.msg("Something went wrong");
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

        }
    }
    public  void init()
    {
        sm=new SessionManager(Activity_OTPRegistration.this);
        UF=new UserFunctions(Activity_OTPRegistration.this);
        cd= new ConnectionDetector(Activity_OTPRegistration.this);

        //getting Intent object from login Activity or registation Activity class
        Intent i=getIntent();
        user_id=i.getStringExtra("mob_no");


        btn_submit=(Button)findViewById(R.id.btn_submit);
        input_otp1=(EditText)findViewById(R.id.input_otp1);
        input_otp2=(EditText)findViewById(R.id.input_otp2);
        input_otp3=(EditText)findViewById(R.id.input_otp3);
        input_otp4=(EditText)findViewById(R.id.input_otp4);

    }


}
