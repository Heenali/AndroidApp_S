package com.trukker.trukkershipperuae.activity;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
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


public class Activity_Forgotpass extends AppCompatActivity
{
    static public ViewPager pager;
    String jsonLogin;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Constants.registration_moving = "Login";
            Intent i = new Intent(Activity_Forgotpass.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.right_out, R.anim.left_in);
            finish();

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass_viewpager);

        Log.e("********************", "******************************");
        Log.e("********************", "Forgotpass page 1 api call");
        Log.e("********************", "******************************");


        pager = (ViewPager) findViewById(R.id.pager);
        assert pager != null;
        pager.setAdapter(new PageFragment.EmptyPagerAdapter(getSupportFragmentManager()));

        ///////////////////
        //pager.setCurrentItem(2, true);
        ///////////////////

        StepperIndicator indicator = (StepperIndicator) findViewById(R.id.stepper_indicator);

        assert indicator != null;
        indicator.setViewPager(pager, true);
        pager.beginFakeDrag();
        if (Constants.forgot_point == 0)
        {
            indicator.setCurrentStep(1);
        }
        if (Constants.forgot_point == 1) {
            indicator.setCurrentStep(2);
        }
        if (Constants.forgot_point == 2) {
            indicator.setCurrentStep(3);
        }
        ///////////////////
        //indicator.setCurrentStep(2);
        ///////////////////
    }

    public static class PageFragment extends Fragment {

        LinearLayout otp_layout_forgotpw, change_pw_layout_forgotpw, mobile_no_layout_forgotpw, resend_otp, movinghome_blinkingtext;
        EditText ed_mobileno_forgotpw, ed_password_forgotpw, ed_confirmpw_forgotpw, ed1_otp_forgotpw, ed2_otp_forgotpw, ed3_otp_forgotpw, ed4_otp_forgotpw;
        String json, mobileNumber, passWord, confirmPw, otp1, otp2, otp3, otp4, otp_no;
        String json_save;
        TextView blinking_text;
        UserFunctions UF;
        SessionManager sm;
        ConnectionDetector cd;
        AppCompatButton send_mobno_forgotpw, send_otp_forgotpw, submit_newpw_forgotpw;
        // DataBaseHelper db;
        Context c;

        int forgot_point = 0;
        private String jsonLogin;

        public static PageFragment newInstance(int page, boolean isLast) {
            Bundle args = new Bundle();
            args.putInt("page", page);
            if (isLast)
                args.putBoolean("isLast", true);
            final PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            final View view = inflater.inflate(R.layout.activity_forgotpass, container, false);
            movinghome_blinkingtext = (LinearLayout) view.findViewById(R.id.movinghome_blinkingtext);
            blinking_text = (TextView) view.findViewById(R.id.blinking_text);
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

            otp_layout_forgotpw = (LinearLayout) view.findViewById(R.id.otp_layout_forgotpw);
            otp_layout_forgotpw.setVisibility(View.GONE);
            mobile_no_layout_forgotpw = (LinearLayout) view.findViewById(R.id.mobile_no_layout_forgotpw);
            change_pw_layout_forgotpw = (LinearLayout) view.findViewById(R.id.change_pw_layout_forgotpw);
            resend_otp = (LinearLayout) view.findViewById(R.id.resend_otp);
            //resend_otp.setOnClickListener((View.OnClickListener) getActivity().getApplication());
            ed_mobileno_forgotpw = (EditText) view.findViewById(R.id.ed_mobileno_forgotpw);
            ed_password_forgotpw = (EditText) view.findViewById(R.id.ed_password_forgotpw);
            ed_confirmpw_forgotpw = (EditText) view.findViewById(R.id.ed_confirmpw_forgotpw);
            send_mobno_forgotpw = (AppCompatButton) view.findViewById(R.id.send_mobno_forgotpw);
            // send_mobno_forgotpw.setOnClickListener((View.OnClickListener) getActivity().getApplication());
            send_otp_forgotpw = (AppCompatButton) view.findViewById(R.id.send_otp_forgotpw);
            //  send_otp_forgotpw.setOnClickListener((View.OnClickListener) getActivity().getApplication());
            submit_newpw_forgotpw = (AppCompatButton) view.findViewById(R.id.submit_newpw_forgotpw);
            // submit_newpw_forgotpw.setOnClickListener((View.OnClickListener) getActivity().getApplication());

            ed1_otp_forgotpw = (EditText) view.findViewById(R.id.ed1_otp_forgotpw);
            ed2_otp_forgotpw = (EditText) view.findViewById(R.id.ed2_otp_forgotpw);
            ed3_otp_forgotpw = (EditText) view.findViewById(R.id.ed3_otp_forgotpw);
            ed4_otp_forgotpw = (EditText) view.findViewById(R.id.ed4_otp_forgotpw);

            if (Constants.forgot_point == 1) {
                mobile_no_layout_forgotpw.setVisibility(View.GONE);
                otp_layout_forgotpw.setVisibility(View.VISIBLE);
            } else if (Constants.forgot_point == 2) {
                mobile_no_layout_forgotpw.setVisibility(View.GONE);
                otp_layout_forgotpw.setVisibility(View.VISIBLE);
                change_pw_layout_forgotpw.setVisibility(View.VISIBLE);
                otp_layout_forgotpw.setVisibility(View.GONE);
            } else if (Constants.forgot_point == 3)
            {

            }

            init();
            UF = new UserFunctions(getActivity().getApplication());
            sm = new SessionManager(getActivity().getApplication());
            cd = new ConnectionDetector(getActivity().getApplication());
            // db = new DataBaseHelper(getActivity().getApplication());


            resend_otp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sendMobNo();
                }
            });

            send_mobno_forgotpw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    mobileNumber = ed_mobileno_forgotpw.getText().toString();
                    Constants.fortgot_mobileNumber = mobileNumber;
                    sendMobNo();

                }
            });
            send_otp_forgotpw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    matchOTP();

                }
            });
            submit_newpw_forgotpw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    matchnewpw();

                }
            });


            return view;
        }

        @Override
        public void onActivityCreated(@Nullable Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            final int page = getArguments().getInt("page", 0);

            if (page == 1) {

            }

            if (page == 2) {

            }
            if (page == 3) {

            }
        }

        public static class EmptyPagerAdapter extends FragmentPagerAdapter {

            public EmptyPagerAdapter(FragmentManager fm) {
                super(fm);
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public Fragment getItem(int position) {
                return PageFragment.newInstance(position + 1, position == getCount() - 1);
            }

        }

        private void init() {


            //otp edittext focus on next ed
            final StringBuilder sb = new StringBuilder();
            ed1_otp_forgotpw.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (sb.length() == 1) {

                        sb.deleteCharAt(0);

                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (sb.length() == 0 & ed1_otp_forgotpw.length() == 1) {
                        sb.append(s);
                        ed1_otp_forgotpw.clearFocus();
                        ed2_otp_forgotpw.requestFocus();
                        ed2_otp_forgotpw.setCursorVisible(true);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (sb.length() == 0) {

                        ed1_otp_forgotpw.requestFocus();
                    }
                }
            });
            ed2_otp_forgotpw.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (sb.length() == 1) {

                        sb.deleteCharAt(0);

                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (sb.length() == 0 & ed2_otp_forgotpw.length() == 1) {
                        sb.append(s);
                        ed2_otp_forgotpw.clearFocus();
                        ed3_otp_forgotpw.requestFocus();
                        ed3_otp_forgotpw.setCursorVisible(true);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (sb.length() == 0) {

                        ed2_otp_forgotpw.requestFocus();
                    }
                }
            });
            ed3_otp_forgotpw.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    if (sb.length() == 1) {

                        sb.deleteCharAt(0);

                    }
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (sb.length() == 0 & ed3_otp_forgotpw.length() == 1) {
                        sb.append(s);
                        ed3_otp_forgotpw.clearFocus();
                        ed4_otp_forgotpw.requestFocus();
                        ed4_otp_forgotpw.setCursorVisible(true);

                    }
                }

                @Override
                public void afterTextChanged(Editable s) {
                    if (sb.length() == 0) {

                        ed3_otp_forgotpw.requestFocus();
                    }
                }
            });
//
        }

        private void matchnewpw() {

            passWord = ed_password_forgotpw.getText().toString().trim();
            confirmPw = ed_confirmpw_forgotpw.getText().toString().trim();
            if (!validate()) {

            } else {
                if (cd.isConnectingToInternet()) {
                    if (confirmPw.equalsIgnoreCase(passWord))
                    {
                        new PwUpdate().execute();
                    }
                    else
                    {
                        Toast.makeText((getActivity().getApplication()), "Password Not Match", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    UF.msg("Please check your internet connection.");
                }
            }

        }

        private void matchOTP() {
            otp1 = ed1_otp_forgotpw.getText().toString();
            otp2 = ed2_otp_forgotpw.getText().toString();
            otp3 = ed3_otp_forgotpw.getText().toString();
            otp4 = ed4_otp_forgotpw.getText().toString();
            otp_no = otp1 + "" + otp2 + "" + "" + otp3 + "" + otp4;
            Log.e("OTP...", "" + otp_no);
            if (!otp_no.equalsIgnoreCase("")) {
                if (!otp1.equalsIgnoreCase("") && !otp2.equalsIgnoreCase("") && !otp3.equalsIgnoreCase("") && !otp4.equalsIgnoreCase("")) {
                    if (cd.isConnectingToInternet()) {
                        json_save = "";
                        new OtpsCheck().execute();

                    } else {
                        UF.msg("Please check your internet connection.");
                    }
                } else {
                    Toast.makeText(getActivity(), "Please Enter Valid OTP", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getActivity(), "Please Enter OTP", Toast.LENGTH_SHORT).show();
            }


        }

        private void sendMobNo() {


            //UF.msg(Constants.fortgot_mobileNumber);
            if (!validate_mobno()) {
                //  UF.msg("validation error");
            } else {
                if (cd.isConnectingToInternet()) {
                    new GetJson().execute();
                } else {
                    UF.msg("Please check your internet connection.");
                }
            }


        }

        //call service for send mobile no & get OTP number for change password
        private class GetJson extends AsyncTask<Void, Void, String> {
            //ProgressHUD mProgressHUD;
            private ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getActivity());
                loading.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setIndeterminate(true);
                loading.setCancelable(false);
                loading.show();
                loading.setContentView(R.layout.my_progress);
            }

            @Override
            protected String doInBackground(Void... params) {
                try {

                    JSONArray jsonArray = new JSONArray();
                    JSONObject prms = new JSONObject();
                    JSONObject prmsLogin = new JSONObject();
                    prmsLogin.put("user_id", Constants.fortgot_mobileNumber);


                    jsonArray.put(prmsLogin);

                    prms.put("User", jsonArray);
                    // Log.e("step1 ....", prms + "");

                    Log.e("--------------------", "----------------------------------");
                    Log.e("Forgotpass step1 Post--", prms.toString());

                    json = UF.LoginUser("login/ValidateuserAndsendOTP", prms);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return json;
            }

            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    Log.e("Forgotpass step1 Get--", json.toString());
                    if (json != null) {
                        if (json.equals("lost")) {
                            UF.msg("Connection Problem.");
                        } else {
                            if (json.equalsIgnoreCase("0")) {
                                UF.msg("Please Enter Register Mobile Number");

                            } else {
                                JSONObject jobj = new JSONObject(json);
                                String status = jobj.getString("status");
                                if (status.equalsIgnoreCase("1"))
                                {
                                    String servermsg = jobj.getString("message");
                                    UF.msg(servermsg);
                                    mobile_no_layout_forgotpw.setVisibility(View.GONE);
                                    otp_layout_forgotpw.setVisibility(View.VISIBLE);
                                    Constants.forgot_point = 1;

                                    Intent intent = new Intent(getActivity(), Activity_Forgotpass.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.anim_slide_in_left,
                                            R.anim.anim_slide_out_left);

                                    getActivity().finish();


                                }
                                else
                                {
                                    String servermsg = jobj.getString("message");
                                    UF.msg(servermsg);


                                }

                            }
                        }
                    } else {
                        UF.msg("Something went wrong");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("--------------------", "----------------------------------");
                loading.dismiss();
            }
        }

        //otp service call
        public class OtpsCheck extends AsyncTask<Void, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getActivity());
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

                    prmsLogin.put("user_id", Constants.fortgot_mobileNumber);
                    prmsLogin.put("OTP", otp_no);
                    prms.put("User", prmsLogin);

                    Log.e("prmsLogin", prms + "");
                    Log.e("otp_ed", otp_no);

                    Log.e("--------------------", "----------------------------------");
                    Log.e("Forgotpass step2 Post--", prms.toString());

                    json_save = UF.LoginUser("Login/ValidateOTP", prms);


                } catch (Exception e) {

                }
                Log.e("jsonLogin", json_save + "");
                return json_save;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                Log.e("json", json_save + "");
                try {

                    Log.e("Forgotpass step2 Get--", json_save.toString());
                    Log.e("--------------------", "----------------------------------");
                    if (json_save != null) {
                        if (json_save.equals("lost")) {
                            UF.msg("Connection Problem.");
                        } else {
                            if (json_save.equalsIgnoreCase("0")) {
                                UF.msg("Invalid OTP");

                            } else {
                                JSONObject jobj = new JSONObject(json_save);
                                String status = jobj.getString("status");
                                if (status.equalsIgnoreCase("1")) {
                                    String servermsg = jobj.getString("message");

                                    // UF.msg("" + servermsg);

                                    change_pw_layout_forgotpw.setVisibility(View.VISIBLE);
                                    otp_layout_forgotpw.setVisibility(View.GONE);

                                    Constants.forgot_point = 2;


                                    Intent intent = new Intent(getActivity(), Activity_Forgotpass.class);
                                    startActivity(intent);
                                    getActivity().overridePendingTransition(R.anim.anim_slide_in_left,
                                            R.anim.anim_slide_out_left);
                                    getActivity().finish();


                                } else {
                                    String servermsg = jobj.getString("message");
                                    UF.msg("" + servermsg);

                                }

                            }
                        }
                    } else {
                        UF.msg("Something went wrong");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        //service for update password
        private class PwUpdate extends AsyncTask<Void, Void, String> {
            //ProgressHUD mProgressHUD;
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = new ProgressDialog(getActivity());
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

                    JSONArray jsonArray = new JSONArray();
                    JSONObject prms = new JSONObject();
                    JSONObject prmsLogin = new JSONObject();
                    prmsLogin.put("user_id", Constants.fortgot_mobileNumber);
                    prmsLogin.put("password", confirmPw);

                    jsonArray.put(prmsLogin);

                    prms.put("User", jsonArray);

                    Log.e("--------------------", "----------------------------------");
                    Log.e("Forgotpass step3 Post--", prms.toString());

                    json = UF.LoginUser("Login/UpdateForgotpwd", prms);


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return json;
            }


            @Override
            protected void onPostExecute(String result) {
                super.onPostExecute(result);

                try {
                    Log.e("Forgotpass step3 Get--", json.toString());
                    Log.e("--------------------", "----------------------------------");
                    if (json != null) {
                        if (json.equals("lost")) {
                            UF.msg("Connection Problem.");
                        } else {
                            if (json.equalsIgnoreCase("0")) {
                                UF.msg("Password Not Update");

                            } else {
                                JSONObject jobj = new JSONObject(json);
                                String status = jobj.getString("status");
                                if (status.equalsIgnoreCase("1")) {
                                    String servermsg = jobj.getString("message");

                                    UF.msg("" + servermsg);
                                    //goto login page
                                    Intent i = new Intent(getActivity(), MainActivity.class);
                                    startActivity(i);
                                    getActivity().finish();

                                } else {
                                    String servermsg = jobj.getString("message");
                                    UF.msg(servermsg);
                                }

                            }
                        }
                    } else {
                        UF.msg("Something went wrong");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                loading.dismiss();
            }
        }

        public boolean validate() {
            boolean valid = true;
            if (passWord.isEmpty() || passWord.length() < 3 || passWord.length() > 10) {
                ed_password_forgotpw.setError("Please Enter Valid Password");
                valid = false;
            } else {
                ed_password_forgotpw.setError(null);
            }
            if (confirmPw.isEmpty() || confirmPw.length() < 3 || confirmPw.length() > 10) {
                ed_confirmpw_forgotpw.setError("Please Enter Valid Password");
                valid = false;
            } else {
                ed_confirmpw_forgotpw.setError(null);
            }

            return valid;
        }

        public boolean validate_mobno()
        {
           // Toast.makeText(getContext(),Constants.fortgot_mobileNumber,Toast.LENGTH_SHORT).show();
            boolean valid = true;
            if (Constants.fortgot_mobileNumber.equalsIgnoreCase("") || Constants.fortgot_mobileNumber.length() < 10) {
                ed_mobileno_forgotpw.setError("Please Enter Valid Mobile Number");
                valid = false;
            } else
            {
                ed_mobileno_forgotpw.setError(null);
            }

            return valid;
        }

    }


}
