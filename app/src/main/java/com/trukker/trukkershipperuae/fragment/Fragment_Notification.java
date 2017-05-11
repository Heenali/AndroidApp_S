package com.trukker.trukkershipperuae.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.adapter.NotificationAdapter;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.model.Notification_method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Ravi on 29/07/15.
 */
public class Fragment_Notification extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    //class object

    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
    //string variable
    String android_id;
    String json_save;
    LinearLayout norecord;
    //controls all
    ListView notification_listview;
    ArrayList<Notification_method> actorsList;
    NotificationAdapter adapter;
    View rootView;
    private SwipeRefreshLayout swipeRefreshLayout;

    public Fragment_Notification()
    {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        Log.e("********************", "******************************");
        Log.e("********************", "Notification page 1 api call");
        Log.e("********************", "******************************");
        init();

        swipeRefreshLayout.setOnRefreshListener(this);
        // swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

                                        if (cd.isConnectingToInternet()) {
                                            new GetJson_save().execute();
                                        } else {
                                            UF.msg("Check Your Internet Connection.");
                                        }


                                    }
                                }
        );




        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
    public void init()
    {
        Constants.notification_open=0;
        android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Fragment_Notification.this.getActivity());
        UF = new UserFunctions(Fragment_Notification.this.getActivity());
        cd = new ConnectionDetector(Fragment_Notification.this.getActivity());
        norecord=(LinearLayout)rootView.findViewById(R.id.norecord);

        notification_listview=(ListView)rootView.findViewById(R.id.listView_notification);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView. findViewById(R.id.swipe_refresh_layout);
        android_id = Settings.Secure.getString(getActivity().getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setRefreshing(false);
         if(cd.isConnectingToInternet())
            {
                new GetJson_save().execute();
            }
            else
            {
                UF.msg("Check Your Internet Connection.");
            }



    }
    private static String pad(int c)
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    private class GetJson_save extends AsyncTask<Void, Void, String> {

         ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* loading = new ProgressDialog(Fragment_Notification.this.getActivity());
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
            swipeRefreshLayout.setRefreshing(true);*/
            // pDialog.setMessage("Please Wait...");
            // pDialog.setCancelable(false);
            //pDialog.show();

        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();


            try {


                prmsLogin.put("AppName", "TrukkerUAE");
                prmsLogin.put("LastMessageDateTime", "");

                if (sm.isLoggedIn())
                {
                    prmsLogin.put("UniqueId", sm.getUniqueId().toString());
                }
                else
                {
                    prmsLogin.put("UniqueId", "0");
                }


            } catch (JSONException e)
            {

                e.printStackTrace();
            }
            Log.e("--------------------", "----------------------------------");
            Log.e("Notification Post--\n", prmsLogin.toString());
            json_save = UF.RegisterUser("Login/GetMessages", prmsLogin);

            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            //loading.dismiss();


            if (json_save.equals("lost")) {
                UF.msg("Connection Problem.");
            } else {
                if (json_save.equalsIgnoreCase("0")) {
                    UF.msg("Invalid");
                } else {
                    try {
                        Log.e("Notification Get--\n", json_save.toString());
                        JSONObject jobj = new JSONObject(json_save);
                        Log.e("Notification status ", json_save .toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        swipeRefreshLayout.setRefreshing(false);
                        Log.e("Notification status .>", status .toString());
                       //   Log.e("Notification message.>", message.toString());
                        Log.e("--------------------", "----------------------------------");

                        if (status.equalsIgnoreCase("1"))
                        {
                            actorsList = new ArrayList<Notification_method>();
                            JSONArray array = new JSONArray();
                            array = jobj.getJSONArray("message");

                            for(int i=0;i<array.length();i++)
                            {
                                String newFormat=array.getJSONObject(i).getString("Date").toString();
                                System.out.println(".....Date..."+newFormat);

                                String a=newFormat.substring(newFormat.length() - 2);
                                Log.e("geting am pm",a);
                                newFormat = newFormat.substring(0, newFormat.length()-6);
                                newFormat=newFormat.substring(newFormat.length() - 5);
                                System.out.println("....time..." + newFormat);
                                String hr = newFormat.substring(0, Math.min(newFormat.length(), 2));
                                String min= newFormat.substring(newFormat.length() - 2);
                                Log.e("HR", hr);
                                Log.e("min", min);
                                String c=hr+":"+min+" "+a;
                                SimpleDateFormat sourceFormat1 = new SimpleDateFormat("hh:mm a");
                                SimpleDateFormat DesiredFormat1  = new SimpleDateFormat("hh:mm");
                                Date date11 = sourceFormat1.parse(c);
                                c = DesiredFormat1.format(date11);

                                Log.e("Heenali Lakhani ",c);

                                int h=Integer.parseInt(hr.trim());
                                int m=Integer.parseInt(min.trim());
                                String localTime = "";

                                Calendar gmt = Calendar.getInstance();

                                gmt.set( Calendar.HOUR, h);
                                gmt.set( Calendar.MINUTE, m);
                                gmt.setTimeZone( TimeZone.getTimeZone("GMT") );

                                Calendar local = Calendar.getInstance();
                                local.setTimeZone( TimeZone.getDefault() );
                                local.setTime(gmt.getTime() );


                                int hour = local.get( Calendar.HOUR );
                                int minutes = local.get( Calendar.MINUTE );
                                boolean am = local.get( Calendar.AM_PM ) == Calendar.AM;
                                String str_hr = "";
                                String str_min = "";
                                String am_pm = "";

                                if ( hour < 10 )
                                    str_hr = "0";
                                if ( minutes < 10 )
                                    str_min = "0";
                                if( am )
                                    am_pm = "PM";
                                else
                                    am_pm = "AM";

                                localTime = str_hr + hour + ":" + str_min + minutes + " " + am_pm;

                                Log.e("Local time...", localTime);


                                SimpleDateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
                                SimpleDateFormat DesiredFormat  = new SimpleDateFormat("dd MMM, yyyy");
                                Date date1 = sourceFormat.parse(array.getJSONObject(i).getString("Date").toString());
                                String formattedDate = DesiredFormat.format(date1);
                                String g=formattedDate+" "+localTime.toString();


                                actorsList.add(new Notification_method(array.getJSONObject(i).getString("MessageId"),array.getJSONObject(i).getString("Subject"),array.getJSONObject(i).getString("Message"),g));

                            }
                            adapter = new NotificationAdapter(getActivity().getApplicationContext(),actorsList);
                            notification_listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }
                        else
                        {
                            norecord.setVisibility(View.VISIBLE);
                            notification_listview.setVisibility(View.GONE);
                            UF.msg(message + "");
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch blockswipeRefreshLayout.setRefreshing(false);
                        swipeRefreshLayout.setRefreshing(false);
                    } catch (Exception e) {
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
            }


        }

    }
    @Override
    public void onResume()
    {
        super.onResume();

        swipeRefreshLayout.setRefreshing(false);
            if(cd.isConnectingToInternet())
            {
                new GetJson_save().execute();
            }
            else
            {
                UF.msg("Check Your Internet Connection.");
            }

    }
    public static String convertTo24HoursFormat(String twelveHourTime)
            throws ParseException {
        return TWENTY_FOUR_TF.format(
                TWELVE_TF.parse(twelveHourTime));
    }
    private static final DateFormat TWELVE_TF = new SimpleDateFormat("hh:mm a");
    private static final DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("HH:mm");
}
