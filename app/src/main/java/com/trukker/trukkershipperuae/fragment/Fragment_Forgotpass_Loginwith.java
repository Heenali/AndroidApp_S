package com.trukker.trukkershipperuae.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
public class Fragment_Forgotpass_Loginwith extends Fragment
{

    public Fragment_Forgotpass_Loginwith() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    UserFunctions UF;
    ConnectionDetector cd;
    SessionManager sm;

    String json_save;

    View rootView;
    Button submit;
    EditText confopass_text,pass_edittext,oldpass_edittext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_forgotpass_loginwith, container, false);
        Log.e("********************", "******************************");
        Log.e("********************", "Forgot pass page 1 api call");
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

        sm = new SessionManager(Fragment_Forgotpass_Loginwith.this.getActivity());
        UF = new UserFunctions(Fragment_Forgotpass_Loginwith.this.getActivity());
        cd = new ConnectionDetector(Fragment_Forgotpass_Loginwith.this.getActivity());

        oldpass_edittext= (EditText) rootView.findViewById(R.id.oldpass_edittext);
        pass_edittext= (EditText) rootView.findViewById(R.id.pass_edittext);
        confopass_text= (EditText) rootView.findViewById(R.id.conpass_edittext);
        submit= (Button) rootView.findViewById(R.id.submit);
    }
    public boolean validate() {
        boolean valid = true;


        String oldpass = oldpass_edittext.getText().toString().trim();
        String password = pass_edittext.getText().toString().trim();
        String conpass = confopass_text.getText().toString().trim();


        if (oldpass.isEmpty() || oldpass.length() < 3 || oldpass.length() > 10)
        {
            oldpass_edittext.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        }
        else
        {
            oldpass_edittext.setError(null);
        }

        if (password.isEmpty() || password.length() < 3 || password.length() > 10) {
            pass_edittext.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            pass_edittext.setError(null);
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
    public void onSignupSuccess()
    {
        submit.setEnabled(true);

        String password = pass_edittext.getText().toString().trim();
        String confirmPassword = confopass_text.getText().toString().trim();
        if (confirmPassword.equalsIgnoreCase(password))
        {
            confopass_text.setError(null);
            if (cd.isConnectingToInternet())
            {
                new GetJson_save().execute();
            }
            else
            {
                UF.msg("Check your internet connection.");
            }

        }
        else
        {
            // Toast.makeText(getActivity(), "Password not matched", Toast.LENGTH_SHORT).show();
            confopass_text.setError("Password not matched");
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

        ProgressDialog pDialog = new ProgressDialog(Fragment_Forgotpass_Loginwith.this.getActivity());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected String doInBackground(Void... params)
        {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();

            try
            {


                prmsLogin.put("user_id", sm.getUserId());
                prmsLogin.put("password",pass_edittext.getText().toString());
                prmsLogin.put("old_password", oldpass_edittext.getText().toString());

                jsonArray.put(prmsLogin);

                prms.put("User", jsonArray);



            } catch (JSONException e)
            {

                e.printStackTrace();
            }
            Log.e("--------------------", "----------------------------------");
            Log.e("Forgot pass Post--\n", prms.toString());
            Log.e("Forgot pass URl--\n", "login/ChangePassword");
            json_save = UF.RegisterUser("login/ChangePassword", prms);


            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pDialog.dismiss();
            Log.e("Forgot pass Get--\n", json_save.toString());

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
                        Log.e("forgotpass status...>", status.toString());
                        Log.e("forgotpass message...>", message.toString());
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("1"))
                        {

                            UF.msg(message + "");
                            Intent i = new Intent(Fragment_Forgotpass_Loginwith.this.getActivity(),MainActivity.class);
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
        oldpass_edittext.setText("");
        pass_edittext.setText("");
        confopass_text.setText("");
        //clear all data

    }
}
