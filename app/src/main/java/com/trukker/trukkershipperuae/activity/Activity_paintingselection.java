package com.trukker.trukkershipperuae.activity;

import android.app.ProgressDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by admin on 8/31/2016.
 */
public class Activity_paintingselection extends AppCompatActivity
{

    TextView name_txt;
    EditText cno_edittext,yourrequirements_edittext;
    TextView submit;
    String json_save;
    String  android_id;
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    TextView maintitle;
    ImageView subject_icon;
    String subject_value="";
    TextView back;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            Intent i = new Intent(Activity_paintingselection.this,MainActivity.class);
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
        setContentView(R.layout.activity_paintingselection);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Activity_paintingselection.this,MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();
            }
        });

        Intent i = getIntent();
        String pagename = i.getStringExtra("pagename");
        String maintitle_text="";
        if(pagename.equalsIgnoreCase("pest control"))
        {
            maintitle_text="Thanks for choosing <font color='#FF7421'> PEST CONTROL </font> from Trukker";
            subject_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.dash_others));
            subject_value="Pest Control";
        }
        else if(pagename.equalsIgnoreCase("other"))
        {
            maintitle_text="Thanks for choosing <font color='#FF7421'> STORAGE </font> from Trukker";
            subject_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.dash_storage));
            subject_value="Other Services";
        }
        else if(pagename.equalsIgnoreCase("cleaning"))
        {
            maintitle_text="Thanks for choosing <font color='#FF7421'> CLEANING </font> from Trukker";
            subject_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.dash_cleaning));
            subject_value="Cleaning";
        }
        else if(pagename.equalsIgnoreCase("painting"))
        {
            maintitle_text="Thanks for choosing <font color='#FF7421'> PAINTING </font> from Trukker";
            subject_icon.setBackgroundDrawable(getResources().getDrawable(R.drawable.dash_painting));
            subject_value="painting";
        }

        maintitle.setText(Html.fromHtml(maintitle_text));
        if(sm.isLoggedIn())
        {
            name_txt.setText("Hi," + sm.getUserName());
        }
        else
        {
            name_txt.setText("Hi" + "");
        }


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit_click();
            }
        });


    }
    public void init()
    {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_paintingselection.this);
        UF = new UserFunctions(Activity_paintingselection.this);
        cd = new ConnectionDetector(Activity_paintingselection.this);
        back=(TextView)findViewById(R.id.back);
        name_txt=(TextView)findViewById(R.id.name_txt);
        cno_edittext=(EditText)findViewById(R.id.name_edittext);
        yourrequirements_edittext=(EditText)findViewById(R.id.yourrequirements_edittext);
        submit=(TextView)findViewById(R.id.submit_btn);
        maintitle=(TextView)findViewById(R.id.maintitle);
        subject_icon=(ImageView)findViewById(R.id.subject_icon);
        if(sm.isLoggedIn())
        {
            cno_edittext.setText(sm.getUserId());
            cno_edittext.setCursorVisible(false);
        }

    }
    private class GetJson_save extends AsyncTask<Void, Void, String>
    {

        private ProgressDialog  loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_paintingselection.this);
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


            try {

                if(sm.isLoggedIn())
                {
                    prmsLogin.put("EmailID", sm.getemailid());
                    prmsLogin.put("name", sm.getUserName());
                }
                else
                {
                    prmsLogin.put("EmailID", "");
                    prmsLogin.put("name", "");
                }

                prmsLogin.put("Message", yourrequirements_edittext.getText().toString());
                prmsLogin.put("Source", "ADDON");
                prmsLogin.put("mobile_no", cno_edittext.getText().toString());
                prmsLogin.put("subject", subject_value);


                jsonArray.put(prmsLogin);

                prms.put("Email", jsonArray);


            } catch (JSONException e)
            {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("Mailer/SaveAddOnMailLogDetails", prms);

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
                            Intent i = new Intent(Activity_paintingselection.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.right_in, R.anim.left_out);
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
    public boolean validate() {
        boolean valid = true;

        String cno_txt = cno_edittext.getText().toString();
        String requ_txt = yourrequirements_edittext.getText().toString();

        if (cno_txt.isEmpty() || cno_txt.length() < 10)
        {
            cno_edittext.setError("Please enter valid mobile number");
            valid = false;
        }
        else {
            cno_edittext.setError(null);
        }

        if (requ_txt.isEmpty() ) {
            yourrequirements_edittext.setError("Enter  Your requirements");
            valid = false;
        } else {
            yourrequirements_edittext.setError(null);
        }

        return valid;
    }
    public void submit_click() {
        Log.d("mess", "Login");

        if (!validate())
        {
            onLoginFailed();
            return;
        }

       submit.setEnabled(false);

        // TODO: Implement your own authentication logic here.

        onLoginSuccess();
    }
    public void onLoginFailed()
    {
       // Toast.makeText(getApplication(), "Login failed", Toast.LENGTH_LONG).show();

        submit.setEnabled(true);
    }
    public void onLoginSuccess()
    {
        submit.setEnabled(true);

        if (cd.isConnectingToInternet()) {

            new GetJson_save().execute();
        } else {
            UF.msg("Check Your Internet Connection.");
        }

    }
}