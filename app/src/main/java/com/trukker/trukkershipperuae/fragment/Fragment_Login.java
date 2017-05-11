package com.trukker.trukkershipperuae.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.activity.Activity_Forgotpass;
import com.trukker.trukkershipperuae.activity.Activity_HireQuote;
import com.trukker.trukkershipperuae.activity.Activity_HireTruck;
import com.trukker.trukkershipperuae.activity.Activity_OTPRegistration;
import com.trukker.trukkershipperuae.activity.Activity_Registartion;
import com.trukker.trukkershipperuae.activity.Dialog_postlodedetail;
import com.trukker.trukkershipperuae.activity.MainActivity;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fragment_Login extends Fragment {
    public Fragment_Login()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }
    //class object declaration
    UserFunctions UF;
    ConnectionDetector cd;
    SessionManager sm;
    String json_save;
    String android_id;
    //all reqired string declartion
    String jsonLogin;
    String jsonGcm1;
    String gcmRegID = "";

    //all control declartion
    TextView txt_registration;
    View rootView;
    EditText mobno_txt, pass_text;
    Button btn_login;
    TextView text_login;
    TextView txt_forgot_password;
    LinearLayout movinghome_blinkingtext;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        Log.e("********************", "******************************");
        Log.e("********************", "Login page 2 api call(login and Notification)");
        Log.e("********************", "******************************");

        Constants.registration_moving = "";

        //init() contains all control declaration
        init();

        txt_registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getActivity().getApplicationContext(), Activity_Registartion.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();

            }
        });

        txt_forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity().getApplicationContext(), Activity_Forgotpass.class);
                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                getActivity().finish();
            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        return rootView;
    }

    public void init() {


        Constants.forgot_point=0;
        UF = new UserFunctions(Fragment_Login.this.getActivity());
        cd = new ConnectionDetector(Fragment_Login.this.getActivity());
        sm = new SessionManager(Fragment_Login.this.getActivity());
        text_login = (TextView) rootView.findViewById(R.id.text_login);
        movinghome_blinkingtext = (LinearLayout) rootView.findViewById(R.id.movinghome_blinkingtext);
        TextView blinking_text = (TextView) rootView.findViewById(R.id.blinking_text);
        movinghome_blinkingtext.setVisibility(View.GONE);
        mobno_txt = (EditText) rootView.findViewById(R.id.input_mobile_no);
        pass_text = (EditText) rootView.findViewById(R.id.input_password);
        btn_login = (Button) rootView.findViewById(R.id.login_btn);
        android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        //links clicks
        txt_registration = (TextView) rootView.findViewById(R.id.txt_register);
        txt_forgot_password = (TextView) rootView.findViewById(R.id.txt_forgot_password);

        //display blinking text when without login getquote->home
        if (Constants.getquote_moving.equalsIgnoreCase("Login")) {

            movinghome_blinkingtext.setVisibility(View.VISIBLE);
            //You can move for as low as: AED 769. No Surprise Charges. Login / Register to See the Details.

            blinking_text.setText(" You can move for as low as: AED " + Constants.store_totalcost_saving+" No Surprise Charges. Login / Register to See the Details.");
            ObjectAnimator textColorAnim;
            textColorAnim = ObjectAnimator.ofInt(blinking_text, "textColor", Color.YELLOW, Color.rgb(255, 99, 71));
            textColorAnim.setDuration(1000);
            textColorAnim.setEvaluator(new ArgbEvaluator());
            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
            textColorAnim.start();

        }
       else if (Constants.getquote_moving.equalsIgnoreCase("Login_hire")) {

            movinghome_blinkingtext.setVisibility(View.VISIBLE);
            //You can move for as low as: AED 769. No Surprise Charges. Login / Register to See the Details.

            blinking_text.setText("Hire Truck Per Day Rate : " + Constants.store_BaseRate_s);
            ObjectAnimator textColorAnim;
            textColorAnim = ObjectAnimator.ofInt(blinking_text, "textColor", Color.YELLOW, Color.rgb(255, 99, 71));
            textColorAnim.setDuration(1000);
            textColorAnim.setEvaluator(new ArgbEvaluator());
            textColorAnim.setRepeatCount(ValueAnimator.INFINITE);
            textColorAnim.start();

        }
        else {

            movinghome_blinkingtext.setVisibility(View.GONE);
        }


        mobno_txt = (EditText) rootView.findViewById(R.id.input_mobile_no);
        pass_text = (EditText) rootView.findViewById(R.id.input_password);
        btn_login = (Button) rootView.findViewById(R.id.login_btn);

        //links clicks
        txt_registration = (TextView) rootView.findViewById(R.id.txt_register);
        String htmlText = "<font color='#ffffff'>Dont't have an Account ?</font><font color='#FF7421'> SIGN UP</font>";
        txt_registration.setText(Html.fromHtml(htmlText));

        txt_forgot_password = (TextView) rootView.findViewById(R.id.txt_forgot_password);
        String login_te = "<font color='#FF7421'>--------</font><font color='#ffffff'> Login </font><font color='#FF7421'>--------</font>";
        text_login.setText(Html.fromHtml(login_te));

        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mobno_txt.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);

    }
    private class GetJson extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Fragment_Login.this.getActivity());
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
                prmsLogin.put("user_id", mobno_txt.getText().toString());
                prmsLogin.put("password", pass_text.getText().toString());
                prmsLogin.put("load_inquiry_no", "");

                prms.put("Login", prmsLogin);

                Log.e("--------------------", "----------------------------------");
                Log.e("Login Post------", prms + "");
                Log.e("Login url------", "login/CheckIn");

                jsonLogin = UF.LoginUser("login/CheckIn", prms);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonLogin;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);


            //	Log.e("json", jsonLogin+"");
            try {

                Log.e("Login Get---", jsonLogin);
                Log.e("--------------------", "----------------------------------");
                if (jsonLogin != null) {
                    if (jsonLogin.equals("lost")) {
                        UF.msg("Connection Problem.");
                    } else {
                        if (jsonLogin.equalsIgnoreCase("0")) {
                            UF.msg("Invalid Username or password");

                        } else {
                            JSONObject jobj = new JSONObject(jsonLogin);
                            String status = jobj.getString("status");

                            if(status.equalsIgnoreCase("0"))
                            {
                                UF.msg(jobj.getString("message").toString());
                            }
                            if (status.equalsIgnoreCase("1")) {

                                loading.dismiss();
                                JSONArray jArray = jobj.getJSONArray("message");
                                JSONObject jobj1 = jArray.getJSONObject(0);
                                String first_name = jobj1.getString("first_name");
                                String email_id1 = jobj1.getString("email_id");

                                String user_id = jobj1.getString("user_id");
                                String role_id = jobj1.getString("role_id");
                                String unique_id = jobj1.getString("unique_id");

                                String password = jobj1.getString("password");
                                String client_type = jobj1.getString("client_type");

                                String middle_name = jobj1.getString("middle_name");
                                String last_name = jobj1.getString("last_name");

                                if (role_id.equalsIgnoreCase("SH"))
                                {
                                    //store session value
                                    sm.createLoginSession(user_id);
                                    sm.setUserId(user_id, first_name, last_name, middle_name, password, client_type, unique_id, role_id, email_id1);
                                    ///////////////////


                               /* Intent i = new Intent(Fragment_Login.this.getActivity(),MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                getActivity().finish();*/
                                    GetGCM();


                                    UF.msg("Login Successfully.");
                                } else {
                                    UF.msg("Invalid User name or password");
                                }


                            } else if (status.equalsIgnoreCase("2")) {
                                //first time login  asking otp
                                Intent i = new Intent(Fragment_Login.this.getActivity(), Activity_OTPRegistration.class);
                                i.putExtra("mob_no", mobno_txt.getText().toString());
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.right_out, R.anim.left_in);
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

        String mobile_number = mobno_txt.getText().toString();
        String password = pass_text.getText().toString();

        if (mobile_number.isEmpty()) {
            mobno_txt.setError("Please enter valid mobile number");
            valid = false;
        } else {
            mobno_txt.setError(null);
        }

        if (password.isEmpty()) {
            pass_text.setError("Please enter valid password");
            valid = false;
        } else {
            pass_text.setError(null);
        }

        return valid;
    }

    public void login() {
        Log.d("mess", "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        btn_login.setEnabled(false);

        // TODO: Implement your own authentication logic here.

        onLoginSuccess();
    }

    public void onLoginFailed() {
        Toast.makeText(getActivity(), "Login failed", Toast.LENGTH_LONG).show();

        btn_login.setEnabled(true);
    }

    public void onLoginSuccess() {
        btn_login.setEnabled(true);
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(btn_login.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        if (cd.isConnectingToInternet()) {
            new GetJson().execute();
        } else {
            UF.msg("Check your internet connection.");
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        //clear all data
        mobno_txt.setText("");
        pass_text.setText("");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mobno_txt.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);


    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void GetGCM() {

        gcmRegID = FirebaseInstanceId.getInstance().getToken();
        new RegGCMService().execute();

    }

    private class RegGCMService extends AsyncTask<Void, Void, String> {
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Fragment_Login.this.getActivity());
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
                TelephonyManager mngr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
                String imei_num = mngr.getDeviceId();
                JSONObject prmsLogin = new JSONObject();
                prmsLogin.put("UniqueId", sm.getUniqueId());
                prmsLogin.put("AppName", "TrukkerUAE");
                prmsLogin.put("DeviceId", gcmRegID.toString());
                prmsLogin.put("TokenId", "AIzaSyAM_KdqAGIrG9yyW8WO2HYvyq9ImUqyAjw");
                prmsLogin.put("DeviceInfo", "0");
                prmsLogin.put("OS", "Android");
                prmsLogin.put("IMEINo", imei_num);

                Log.e("--------------------", "----------------------------------");
                Log.e("Notification Url", "login/RegisterDevice");
                Log.e("Notification Post---", prmsLogin.toString());


                jsonGcm1 = UF.LoginUser("login/RegisterDevice", prmsLogin);


            } catch (Exception e) {

            }
            return jsonGcm1;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {


                Log.e("Notification Get--", jsonGcm1.toString());
                Log.e("--------------------", "----------------------------------");
                loading.dismiss();
                if (jsonGcm1 != null) {
                    if (jsonGcm1.equalsIgnoreCase("0")) {

                    } else {
                        JSONObject jobj = new JSONObject(jsonGcm1);
                        String status = jobj.getString("status");
                        if (status.equalsIgnoreCase("1")) {

                            //registation of notification done
                            SharedPreferences prefs;
                            String prefName = "Notification_regi";
                            prefs = getActivity().getSharedPreferences(prefName, getActivity().MODE_PRIVATE);
                            SharedPreferences.Editor editor = prefs.edit();
                            editor.putInt("id", 1);
                            editor.commit();

                            if (Constants.panding == 1) {
                                if (cd.isConnectingToInternet()) {

                                    new GetJsonmovemyhome_save().execute();
                                } else {
                                    UF.msg("Check Your Internet Connection.");
                                }
                            } else if (Constants.panding == 2) {
                                if (cd.isConnectingToInternet()) {

                                    new GetJsonmovemyhome_save_goods().execute();
                                } else {
                                    UF.msg("Check Your Internet Connection.");
                                }
                            }
                            else if (Constants.panding == 3) {
                                if (cd.isConnectingToInternet()) {

                                    new GetJsonmovemyhome_save_hire().execute();
                                } else {
                                    UF.msg("Check Your Internet Connection.");
                                }
                            }else {
                                Intent i = new Intent(Fragment_Login.this.getActivity(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                getActivity().finish();
                            }





                          /*  Intent i = new Intent(Fragment_Login.this.getActivity(),MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                            getActivity().finish();*/


                        } else {
                           /* String servermsg = jobj.getString("message");
                            UF.msg(servermsg);*/
                        }
                    }
                } else {
                    // UF.msg(String.valueOf(R.string.internet_slow));
                }
            } catch (Exception e) {
                //UF.msg(String.valueOf(R.string.internet_slow));
            }

        }
    }

    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Fragment_Login.this.getActivity());
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
                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("Area", Constants.store_area);
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");
                prmsLogin.put("NoOfTruck", "");
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", "");
                prmsLogin.put("NoOfHandiman", "");

                if (sm.isLoggedIn()) {
                    prmsLogin.put("shipper_id", sm.getUniqueId());
                } else {
                    prmsLogin.put("shipper_id", "");
                }
                if (sm.isLoggedIn()) {
                    prmsLogin.put("load_inquiry_no", "");
                } else {
                    // prmsLogin.put("load_inquiry_no", posting_shippingid);
                }


                prmsLogin.put("email_id", Constants.store_email);

                prmsLogin.put("required_price", "0");
                prmsLogin.put("inquiry_source_addr", Constants.store_movefrom);
                prmsLogin.put("inquiry_source_city", Constants.postload_source_city);
                prmsLogin.put("inquiry_source_lat", Constants.postloadsourceLat);
                prmsLogin.put("inquiry_source_lng", Constants.postloadsourceLong);
                prmsLogin.put("source_pincode", "");
                prmsLogin.put("load_inquiry_shipping_date", Constants.store_date);
                prmsLogin.put("load_inquiry_shipping_time", Constants.store_time);
                prmsLogin.put("inquiry_destination_addr", Constants.store_moveto);
                prmsLogin.put("inquiry_destination_city", Constants.postload_dest_city);
                prmsLogin.put("inquiry_destionation_lat", Constants.postloaddestLat);
                prmsLogin.put("inquiry_destionation_lng", Constants.postloaddestLong);
                prmsLogin.put("destination_pincode", "");
                prmsLogin.put("remarks", "");
                prmsLogin.put("payment_mode", "");
                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("Isfinalorder", "N");
                prmsLogin.put("rate_type_flag", "N");
                prmsLogin.put("IncludePackingCharge", "Y");

                prmsLogin.put("Isupdate", "N");

                prmsLogin.put("order_type_flag", "H");
                prmsLogin.put("TruckTypeCode", "");
                prmsLogin.put("goods_type_flag", "Y");
                prmsLogin.put("promocode", "");
                prmsLogin.put("Isupdatebillingadd", "N");


                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("postorder/SaveMovingHomeDetails", prms);

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

                        Log.e("getquotealldata ", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray array = new JSONArray();

                            array = jobj.getJSONArray("message");
                            Log.e("array", array.toString());
                            Log.e("array0", array.getJSONObject(0).getString("Total_cost").toString());
                            Log.e("array1", array.getJSONObject(1).getString("Total_cost").toString());

                            String NoOfTruck_p = array.getJSONObject(0).getString("NoOfTruck").toString();
                            String NoOfLabour_p = array.getJSONObject(0).getString("NoOfLabour").toString();
                            String NoOfHandiman_p = array.getJSONObject(0).getString("NoOfHandiman").toString();
                            String BaseRate_p = array.getJSONObject(0).getString("BaseRate").toString();
                            String TotalLabourRate_p = array.getJSONObject(0).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_p = array.getJSONObject(0).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_p = array.getJSONObject(0).getString("TotalPackingRate").toString();
                            String totlecost_pri = array.getJSONObject(0).getString("Total_cost").toString();

                            Log.e("NoOfTruck_p", NoOfTruck_p);
                            Log.e("NoOfLabour_p", NoOfLabour_p);
                            Log.e("NoOfHandiman_p", NoOfHandiman_p);
                            Log.e("BaseRate_p", BaseRate_p);
                            Log.e("TotalLabourRate_p", TotalLabourRate_p);
                            Log.e("TotalHandimanRate_p", TotalHandimanRate_p);
                            Log.e("TotalPackingRate_p", TotalPackingRate_p);

                            String NoOfTruck_s = array.getJSONObject(1).getString("NoOfTruck").toString();
                            String NoOfLabour_s = array.getJSONObject(1).getString("NoOfLabour").toString();
                            String NoOfHandiman_s = array.getJSONObject(1).getString("NoOfHandiman").toString();
                            String BaseRate_s = array.getJSONObject(1).getString("BaseRate").toString();
                            String TotalLabourRate_s = array.getJSONObject(1).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_s = array.getJSONObject(1).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_s = array.getJSONObject(1).getString("TotalPackingRate").toString();
                            String totlecost_stan = array.getJSONObject(1).getString("Total_cost").toString();


                            Log.e("NoOfTruck_p", NoOfTruck_p);
                            Log.e("NoOfLabour_p", NoOfLabour_p);
                            Log.e("NoOfHandiman_p", NoOfHandiman_p);
                            Log.e("BaseRate_p", BaseRate_p);
                            Log.e("TotalLabourRate_p", TotalLabourRate_p);
                            Log.e("TotalHandimanRate_p", TotalHandimanRate_p);
                            Log.e("TotalPackingRate_p", TotalPackingRate_p);

                            String NoOfTruck_saving = array.getJSONObject(2).getString("NoOfTruck").toString();
                            String NoOfLabour_saving = array.getJSONObject(2).getString("NoOfLabour").toString();
                            String NoOfHandiman_saving = array.getJSONObject(2).getString("NoOfHandiman").toString();
                            String BaseRate_saving = array.getJSONObject(2).getString("BaseRate").toString();
                            String TotalLabourRate_saving = array.getJSONObject(2).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_saving = array.getJSONObject(2).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_saving = array.getJSONObject(2).getString("TotalPackingRate").toString();
                            String totlecost_saving = array.getJSONObject(2).getString("Total_cost").toString();


                            if (sm.isLoggedIn()) {
                                String load_inquiry_no = array.getJSONObject(1).getString("load_inquiry_no").toString();
                                Constants.store_loadinquiry_no = load_inquiry_no;

                            } else {
                                //Constants.store_loadinquiry_no=posting_shippingid;
                            }

                            Constants.store_totalcost_p = totlecost_pri;
                            Constants.store_totalcost_s = totlecost_stan;
                            Constants.store_totalcost_p_getquotepage = totlecost_pri;
                            Constants.store_totalcost_s_getquotepage = totlecost_stan;

                            Constants.store_NoOfTruck_s = NoOfTruck_s;
                            Constants.store_NoOfLabour_s = NoOfLabour_s;
                            Constants.store_NoOfHandiman_s = NoOfHandiman_s;
                            Constants.store_BaseRate_s = BaseRate_s;
                            Constants.store_TotalLabourRate_s = TotalLabourRate_s;
                            Constants.store_TotalHandimanRate_s = TotalHandimanRate_s;
                            Constants.store_TotalPackingRate_s = TotalPackingRate_s;
                            Constants.static_labourvalue_s = NoOfLabour_s;
                            Constants.static_handymanvalue_s = NoOfHandiman_s;

                            Constants.store_NoOfLabour_incdec_s = NoOfLabour_s;
                            Constants.store_NoOfHandiman_incdec_s = NoOfHandiman_s;

                            Constants.store_NoOfTruck_p = NoOfTruck_p;
                            Constants.store_NoOfLabour_p = NoOfLabour_p;
                            Constants.store_NoOfHandiman_p = NoOfHandiman_p;
                            Constants.store_BaseRate_p = BaseRate_p;
                            Constants.store_TotalLabourRate_p = TotalLabourRate_p;
                            Constants.store_TotalHandimanRate_p = TotalHandimanRate_p;
                            Constants.store_TotalPackingRate_p = TotalPackingRate_p;

                            Constants.store_NoOfTruck_saving = NoOfTruck_saving;
                            Constants.store_NoOfLabour_saving = NoOfLabour_saving;
                            Constants.store_NoOfHandiman_saving = NoOfHandiman_saving;
                            Constants.store_BaseRate_saving = BaseRate_saving;
                            Constants.store_TotalLabourRate_saving = TotalLabourRate_saving;
                            Constants.store_TotalHandimanRate_saving = TotalHandimanRate_saving;
                            Constants.store_TotalPackingRate_saving = TotalPackingRate_saving;
                            Constants.static_labourvalue_saving = NoOfLabour_saving;
                            Constants.static_handymanvalue_saving = NoOfHandiman_saving;

                            Constants.store_NoOfLabour_incdec_saving = NoOfLabour_saving;
                            Constants.store_NoOfHandiman_incdec_saving = NoOfHandiman_saving;

                            Constants.store_totalcost_saving = totlecost_saving;
                            Constants.store_totalcost_saving_gequotepage = totlecost_saving;

                            if (sm.isLoggedIn()) {

                                Intent i = new Intent(Fragment_Login.this.getActivity(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                getActivity().finish();


                            }


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

    private class GetJsonmovemyhome_save_goods extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Fragment_Login.this.getContext());
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
                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("Area", "0");
                prmsLogin.put("TotalDistance", Constants.store_placedistace);
                prmsLogin.put("TotalDistanceUOM", Constants.store_unit);
                prmsLogin.put("TimeToTravelInMinute", "0");
                prmsLogin.put("NoOfTruck", "");
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", "");
                prmsLogin.put("NoOfHandiman", "");

                if (sm.isLoggedIn()) {
                    prmsLogin.put("shipper_id", sm.getUniqueId());
                } else {
                    prmsLogin.put("shipper_id", "");
                }
                if (sm.isLoggedIn()) {
                    prmsLogin.put("load_inquiry_no", "");
                } else {

                }


                prmsLogin.put("email_id", Constants.store_email);
                prmsLogin.put("load_inquiry_no", "");
                prmsLogin.put("required_price", "0");
                prmsLogin.put("inquiry_source_addr", Constants.store_movefrom);
                prmsLogin.put("inquiry_source_city", Constants.postload_source_city);
                prmsLogin.put("inquiry_source_lat", Constants.postloadsourceLat);
                prmsLogin.put("inquiry_source_lng", Constants.postloadsourceLong);
                prmsLogin.put("source_pincode", "");
                prmsLogin.put("load_inquiry_shipping_date", Constants.store_date);
                prmsLogin.put("load_inquiry_shipping_time", Constants.store_time);
                prmsLogin.put("inquiry_destination_addr", Constants.store_moveto);
                prmsLogin.put("inquiry_destination_city", Constants.postload_dest_city);
                prmsLogin.put("inquiry_destionation_lat", Constants.postloaddestLat);
                prmsLogin.put("inquiry_destionation_lng", Constants.postloaddestLong);
                prmsLogin.put("destination_pincode", "");
                prmsLogin.put("remarks", "");
                prmsLogin.put("payment_mode", "");
                prmsLogin.put("load_inquiry_truck_type", "");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id", android_id.toString());
                prmsLogin.put("device_type", "M");
                prmsLogin.put("Isfinalorder", "N");
                prmsLogin.put("IncludePackingCharge", "Y");
                prmsLogin.put("Isupdate", "N");
                prmsLogin.put("TruckTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("goods_type_flag", "Y");
                prmsLogin.put("promocode", "");

                prmsLogin.put("rate_type_flag", "B");
                prmsLogin.put("order_type_flag", "GL");
                prmsLogin.put("goods_details", "");
                prmsLogin.put("Isupdatebillingadd", "N");

                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("postorder/SaveMovingGoodsDetails", prms);

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

                        Log.e("getquotealldata ", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray array = new JSONArray();

                            array = jobj.getJSONArray("message");
                            Log.e("array", array.toString());
                            Log.e("array0", array.getJSONObject(0).getString("Total_cost").toString());

                            String NoOfTruck_g = array.getJSONObject(0).getString("NoOfTruck").toString();
                            String NoOfLabour_g = array.getJSONObject(0).getString("NoOfLabour").toString();
                            String NoOfHandiman_g = array.getJSONObject(0).getString("NoOfHandiman").toString();
                            String BaseRate_g = array.getJSONObject(0).getString("BaseRate").toString();
                            String TotalLabourRate_g = array.getJSONObject(0).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_g = array.getJSONObject(0).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_g = array.getJSONObject(0).getString("TotalPackingRate").toString();
                            String totlecost_g = array.getJSONObject(0).getString("Total_cost").toString();
                            String TimeForLoadingInMinute_g = array.getJSONObject(0).getString("TimeForLoadingInMinute").toString();
                            String TimeForUnloadingInMinute_g = array.getJSONObject(0).getString("TimeForLoadingInMinute").toString();
                            String load_inquiry_no = array.getJSONObject(0).getString("load_inquiry_no").toString();
                            String TimeToTravelInMinute = array.getJSONObject(0).getString("TimeToTravelInMinute").toString();

                            Constants.store_NoOfTruck_g = NoOfTruck_g;
                            Constants.store_NoOfLabour_g = NoOfLabour_g;
                            Constants.store_NoOfHandiman_g = NoOfHandiman_g;
                            Constants.store_BaseRate_g = BaseRate_g;
                            Constants.store_TotalLabourRate_g = TotalLabourRate_g;
                            Constants.store_TotalHandimanRate_g = TotalHandimanRate_g;
                            Constants.store_TotalPackingRate_g = TotalPackingRate_g;
                            Constants.totlecost_g = totlecost_g;
                            Constants.store_TotalHandimanRate_g_incdec = TotalHandimanRate_g;
                            Constants.TimeForLoadingInMinute_g = TimeForLoadingInMinute_g;
                            Constants.TimeForUnloadingInMinute_g = TimeForUnloadingInMinute_g;
                            Constants.store_loadinquiry_no = load_inquiry_no;
                            Constants.eta = TimeToTravelInMinute;

                            Log.e("NoOfTruck_p", NoOfTruck_g);
                            Log.e("NoOfLabour_p", NoOfLabour_g);
                            Log.e("NoOfHandiman_p", NoOfHandiman_g);
                            Log.e("BaseRate_p", BaseRate_g);
                            Log.e("TotalLabourRate_p", TotalLabourRate_g);
                            Log.e("TotalHandimanRate_p", TotalHandimanRate_g);
                            Log.e("TotalPackingRate_p", TotalPackingRate_g);
                            if (sm.isLoggedIn())
                            {

                                Constants.getquote_moving = "Login_Goods";
                                Intent i = new Intent(Fragment_Login.this.getActivity(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                getActivity().finish();

                            }


                        } else {
                            UF.msg(message + "");
                        }
                    }
                    catch (JSONException e)
                    {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }


        }
    }
    private class GetJsonmovemyhome_save_hire extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Fragment_Login.this.getActivity());
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

                prmsLogin.put("Hiretruck_NoofDay", Constants.store_noofdays);
                prmsLogin.put("Hiretruck_To_datetime", Constants.store_date2);
                prmsLogin.put("Hiretruck_IncludingFuel", "Y");
                prmsLogin.put("SizeTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("TotalDistance","0");
                prmsLogin.put("TotalDistanceUOM","KM");
                prmsLogin.put("TimeToTravelInMinute","0");
                prmsLogin.put("NoOfTruck", "1");
                prmsLogin.put("NoOfDriver", "");
                prmsLogin.put("NoOfLabour", "");
                prmsLogin.put("NoOfHandiman", "");
                prmsLogin.put("NoOfHelper", "");

                if (sm.isLoggedIn()) {
                    prmsLogin.put("load_inquiry_no", "");
                } else {
                    prmsLogin.put("load_inquiry_no",Constants.store_loadinquiry_no);
                }

                if (sm.isLoggedIn()) {
                    prmsLogin.put("shipper_id", sm.getUniqueId());
                } else {
                    prmsLogin.put("shipper_id", "");
                }



                prmsLogin.put("email_id", sm.getemailid());

                prmsLogin.put("required_price", "0");
                prmsLogin.put("inquiry_source_addr", Constants.store_movefrom);
                prmsLogin.put("inquiry_source_city", Constants.postload_source_city);
                prmsLogin.put("inquiry_source_lat", Constants.postloadsourceLat);
                prmsLogin.put("inquiry_source_lng", Constants.postloadsourceLong);
                prmsLogin.put("source_pincode","");
                prmsLogin.put("load_inquiry_shipping_date", Constants.store_date);
                prmsLogin.put("load_inquiry_shipping_time", Constants.store_time_display);
                prmsLogin.put("inquiry_destination_addr", "");
                prmsLogin.put("inquiry_destination_city", "");
                prmsLogin.put("inquiry_destionation_lat", "");
                prmsLogin.put("inquiry_destionation_lng","" );
                prmsLogin.put("destination_pincode","" );
                prmsLogin.put("remarks","");
                prmsLogin.put("load_inquiry_truck_type","");
                prmsLogin.put("created_host", android_id.toString());
                prmsLogin.put("created_by", "");
                prmsLogin.put("device_id",android_id.toString());
                prmsLogin.put("device_type","Android");
                prmsLogin.put("Isfinalorder", "N");
                prmsLogin.put("rate_type_flag", "B");
                prmsLogin.put("IncludePackingCharge", "N");
                prmsLogin.put("Isupdate", "N");
                prmsLogin.put("order_type_flag","HT");
                prmsLogin.put("TruckTypeCode", Constants.store_Hometypeid);
                prmsLogin.put("billing_add", "");
                prmsLogin.put("billing_name", "");
                prmsLogin.put("payment_mode","");
                prmsLogin.put("payment_status", "N");
                prmsLogin.put("destination_full_add","");
                prmsLogin.put("source_full_add", "");
                prmsLogin.put("promocode", Constants.promocode_store );
                prmsLogin.put("goods_details", "");
                prmsLogin.put("Isupdatebillingadd", Constants.store_Isupdatebillingadd);

                jsonArray.put(prmsLogin);

                prms.put("PostOrderParameter", jsonArray);


            } catch (JSONException e) {

                e.printStackTrace();
            }
            Log.e("prmsLogin", prms + "");
            json_save = UF.RegisterUser("postorder/SaveHireTruckDetails", prms);

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

                        Log.e("getquotealldata ", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        if (status.equalsIgnoreCase("1")) {
                            JSONArray array = new JSONArray();

                            array = jobj.getJSONArray("message");
                            Log.e("array", array.toString());

                            String NoOfTruck_s = array.getJSONObject(0).getString("NoOfTruck").toString();
                            String NoOfLabour_s = array.getJSONObject(0).getString("NoOfLabour").toString();
                            String NoOfHandiman_s = array.getJSONObject(0).getString("NoOfHandiman").toString();
                            String BaseRate_s = array.getJSONObject(0).getString("BaseRate").toString();
                            String TotalLabourRate_s = array.getJSONObject(0).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_s = array.getJSONObject(0).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_s = array.getJSONObject(0).getString("Hiretruck_TotalFuelRate").toString();
                            String totlecost_stan = array.getJSONObject(0).getString("Total_cost").toString();
                            String Hiretruck_MaxKM_s = array.getJSONObject(0).getString("Hiretruck_MaxKM").toString();



                            String load_inquiry_no = array.getJSONObject(0).getString("load_inquiry_no").toString();
                            Constants.store_loadinquiry_no = load_inquiry_no;



                            Constants.store_totalcost_s = totlecost_stan;
                            Constants.store_totalcost_s_getquotepage = totlecost_stan;
                            Constants.store_NoOfTruck_s = NoOfTruck_s;
                            Constants.store_NoOfLabour_s = NoOfLabour_s;
                            Constants.store_NoOfHandiman_s = NoOfHandiman_s;
                            Constants.store_BaseRate_s = BaseRate_s;
                            Constants.store_TotalLabourRate_s = TotalLabourRate_s;
                            Constants.store_TotalHandimanRate_s = TotalHandimanRate_s;
                            Constants.store_TotalPackingRate_s = TotalPackingRate_s;
                            Constants.static_labourvalue_s = NoOfLabour_s;
                            Constants.static_handymanvalue_s = NoOfHandiman_s;
                            Constants.store_Hiretruck_MaxKM_s=Hiretruck_MaxKM_s;
                            Constants.store_NoOfLabour_incdec_s = NoOfLabour_s;
                            Constants.store_NoOfHandiman_incdec_s = NoOfHandiman_s;

                            if (sm.isLoggedIn())
                            {

                                Constants.getquote_moving = "Login_hire";
                                Intent i = new Intent(Fragment_Login.this.getActivity(), MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);
                                getActivity().finish();

                            }


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
