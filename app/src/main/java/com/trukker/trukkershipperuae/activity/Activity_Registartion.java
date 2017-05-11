package com.trukker.trukkershipperuae.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ravi on 29/07/15.
 */


public class Activity_Registartion extends AppCompatActivity {

    //class object declaration
    UserFunctions UF;
    ///////////////

    //all reqired string declartion
    String json_save;
    /////////////////////

    //all control declartion
    EditText fullname, emailid, pass, conpass, mobno;
    Button btn_signup;
    String android_id;
    TextView link_login, blinking_text;
    LinearLayout movinghome_blinkingtext;

    ///////////////////
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Constants.registration_moving = "Login";
            Intent i = new Intent(Activity_Registartion.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.right_out, R.anim.left_in);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        //init() contains all control declaration
        init();

        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signup();
            }
        });
        String htmlText = "<font color='#000000'>Already a member?</font><font color='#FF7421'> Login</font>";
        link_login.setText(Html.fromHtml(htmlText));
        link_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.registration_moving = "Login";
                Intent i = new Intent(Activity_Registartion.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.right_out, R.anim.left_in);
                finish();
            }
        });

    }

    public void init() {
        UF = new UserFunctions(Activity_Registartion.this);

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);


        fullname = (EditText) findViewById(R.id.fullname_text);
        emailid = (EditText) findViewById(R.id.emailid_text);
        mobno = (EditText) findViewById(R.id.mobileno_text);
        pass = (EditText) findViewById(R.id.pass_text);
        conpass = (EditText) findViewById(R.id.confopass_text);
        btn_signup = (Button) findViewById(R.id.btn_signup);

        //links clicks
        link_login = (TextView) findViewById(R.id.link_login);

        movinghome_blinkingtext = (LinearLayout) findViewById(R.id.movinghome_blinkingtext);
        blinking_text = (TextView) findViewById(R.id.blinking_text);
        movinghome_blinkingtext.setVisibility(View.GONE);
        if (Constants.getquote_moving.equalsIgnoreCase("Login")) {
            movinghome_blinkingtext.setVisibility(View.VISIBLE);

            blinking_text.setText(" AED " + Constants.store_totalcost_saving);
            ObjectAnimator textColorAnim;
            textColorAnim = ObjectAnimator.ofInt(blinking_text, "textColor", Color.GREEN, Color.rgb(255, 99, 71));
            textColorAnim.setDuration(1000);
            textColorAnim.setEvaluator(new ArgbEvaluator());
            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
            textColorAnim.start();

        } else {

            movinghome_blinkingtext.setVisibility(View.GONE);
        }

    }

    private class GetJson_save extends AsyncTask<Void, Void, String> {

        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_Registartion.this);
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

                prmsLogin.put("company_name", "");
                prmsLogin.put("created_by", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("email_id", emailid.getText().toString());
                prmsLogin.put("first_name", fullname.getText().toString());
                prmsLogin.put("last_name", "");
                prmsLogin.put("load_inquiry_no", "");
                prmsLogin.put("password", pass.getText().toString());
                prmsLogin.put("reg_type", "");
                prmsLogin.put("user_id", mobno.getText().toString());
                prmsLogin.put("address", "Test");

                jsonArray.put(prmsLogin);

                prms.put("Shipper_master", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("shipper/RegisterShipper", prms);

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
                        Log.e("json of regi:",jobj.toString());
                        if (status.equalsIgnoreCase("1")) {

                            UF.msg(message + "");
                            Intent i = new Intent(Activity_Registartion.this, Activity_OTPRegistration.class);
                            i.putExtra("mob_no", mobno.getText().toString());
                            startActivity(i);
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
    public boolean validate() {
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
        String password = pass.getText().toString().trim();
        String mobile_number = mobno.getText().toString();
        String confirmPassword = conpass.getText().toString().trim();


        if (name.isEmpty() || name.length() < 3) {
            fullname.setError("at least 3 characters");

            valid = false;
        } else {
            fullname.setError(null);
        }


        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailid.setError("enter a valid email address");
            valid = false;
        } else {
            emailid.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            pass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            pass.setError(null);
        }

        if (confirmPassword.isEmpty() || confirmPassword.length() < 4 || confirmPassword.length() > 10) {
            conpass.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            conpass.setError(null);
        }


        if (mobile_number.isEmpty() || mobile_number.length() < 9) {
            mobno.setError("Please enter Maximum 10 Digit and Minimum 9 Digit");
            valid = false;
        } else {
            mobno.setError(null);
            char first = mobile_number.charAt(0);
            if (String.valueOf(first).equalsIgnoreCase("0")) {
                mobno.setError("First Digit '0' not allow");
                valid = false;
            }

        }

        return valid;
    }

    public void signup() {
        Log.d("mess", "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        btn_signup.setEnabled(false);


        onSignupSuccess();

    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();

        btn_signup.setEnabled(true);
    }

    public void onSignupSuccess() {
        btn_signup.setEnabled(true);
        setResult(RESULT_OK, null);

        String password = pass.getText().toString().trim();

        String confirmPassword = conpass.getText().toString().trim();

        if (confirmPassword.equalsIgnoreCase(password)) {
            new GetJson_save().execute();
        } else {
            Toast.makeText(getApplicationContext(), "Password not matched", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onResume() {
        super.onResume();

        //clear all data

        fullname.setText("");
        emailid.setText("");
        mobno.setText("");
        pass.setText("");
        conpass.setText("");

    }

}
