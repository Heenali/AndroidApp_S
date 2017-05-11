package com.trukker.trukkershipperuae.fragment;
//contact us from (5 menu)

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.activity.MainActivity;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ravi on 29/07/15.
 */
public class Fragment_Contactus extends Fragment {

    public Fragment_Contactus() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    UserFunctions UF;
    ConnectionDetector cd;
    SessionManager sm;

    String json_save;

    View rootView;
    Button submit;
    EditText mess_edittext,name_edittext,subject_edittext,mno_edittext;
    String name_value,mno_value;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_contactus, container, false);
        Log.e("********************", "******************************");
        Log.e("********************", "Contact us page 1 api call");
        Log.e("********************", "******************************");

        init();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                signup();
            }
        });

        return rootView;
    }

    public void init()
    {

        sm = new SessionManager(Fragment_Contactus.this.getActivity());
        UF = new UserFunctions(Fragment_Contactus.this.getActivity());
        cd = new ConnectionDetector(Fragment_Contactus.this.getActivity());

        mno_edittext= (EditText) rootView.findViewById(R.id.mno_edittext);
        name_edittext= (EditText) rootView.findViewById(R.id.name_edittext);
        subject_edittext= (EditText) rootView.findViewById(R.id.subject_edittext);
        mess_edittext= (EditText) rootView.findViewById(R.id.mess_edittext);

        /*mess_edittext.setOnKeyListener(new View.OnKeyListener(){
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) return true;
                return false;
            }
        });*/
        submit= (Button) rootView.findViewById(R.id.submit_btn);
        mno_edittext.setCursorVisible(false);
        if(sm.isLoggedIn())
        {
            mno_value=sm.getUserId().toString();
            name_value=sm.getUserName().toString();

            mno_edittext.setText(mno_value);
            name_edittext.setText(name_value);
        }
        else
        {

        }

    }
    public boolean validate() {
        boolean valid = true;

        String mnu = mno_edittext.getText().toString().trim();
        String name = name_edittext.getText().toString().trim();
        String mess = mess_edittext.getText().toString().trim();
        String subject = subject_edittext.getText().toString().trim();


        if (mnu.isEmpty() ) {
            mno_edittext.setError("Enter Valid Mobile Number");

            valid = false;
        } else {
            mno_edittext.setError(null);
        }

        if (name.isEmpty() || name.length() < 3) {
            name_edittext.setError("at least 3 characters");

            valid = false;
        } else {
            name_edittext.setError(null);
        }

        if (mess.isEmpty() ) {
            mess_edittext.setError("Enter Message");

            valid = false;
        } else {
            mess_edittext.setError(null);
        }

        if (subject.isEmpty() ) {
            subject_edittext.setError("Enter Subject");

            valid = false;
        } else {
            subject_edittext.setError(null);
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

        submit.setEnabled(false);


        onSignupSuccess();

    }
    public void onSignupFailed() {
        Toast.makeText(getActivity(), " failed", Toast.LENGTH_LONG).show();

        submit.setEnabled(true);
    }
    public void onSignupSuccess() {
        submit.setEnabled(true);
        //setResult(RESULT_OK, null);
        if (cd.isConnectingToInternet())
        {
            new GetJson_save().execute();
        } else {
            UF.msg("Check your internet connection.");
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    private class GetJson_save extends AsyncTask<Void, Void, String> {

        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Fragment_Contactus.this.getActivity());
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

                prmsLogin.put("EmailID", sm.getemailid());
                prmsLogin.put("mobile_no", mno_edittext.getText().toString());
                prmsLogin.put("Message", mess_edittext.getText().toString());
                prmsLogin.put("Source","Contact");
                prmsLogin.put("shipper_id", sm.getUniqueId());
                prmsLogin.put("name", name_edittext.getText().toString());
                prmsLogin.put("subject", subject_edittext.getText().toString());

                jsonArray.put(prmsLogin);

                prms.put("Email", jsonArray);

            } catch (JSONException e)
            {

                e.printStackTrace();
            }
            Log.e("--------------------", "----------------------------------");
            Log.e("Contactus Post--\n", prms.toString());
            Log.e("Contactus URL--\n", "mailer/SaveContactUsDetails");
            json_save = UF.RegisterUser("mailer/SaveContactUsDetails", prms);


            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();

            Log.e("Contactus Get--\n", json_save.toString());
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
                        Log.e("contaus status......>",status.toString() );
                        Log.e("contaus message......>",message.toString() );
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("1"))
                        {

                            UF.msg(message + "");
                            Intent i = new Intent(Fragment_Contactus.this.getActivity(),MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            getActivity().overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            getActivity().finish();


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

    @Override
    public void onResume()
    {
        super.onResume();
        //clear all data
       mno_edittext.setText("");
        name_edittext.setText("");
        subject_edittext.setText("");
        mess_edittext.setText("");

        if(sm.isLoggedIn())
        {
            mno_value=sm.getUserId().toString();
            name_value=sm.getUserName().toString();
             mno_edittext.setText(mno_value);
            name_edittext.setText(name_value);
        }
    }

}
