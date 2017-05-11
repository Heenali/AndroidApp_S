package com.trukker.trukkershipperuae.fragment;
//listing home(2 menu)

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.activity.Activity_MoveMyHome_Detail;
import com.trukker.trukkershipperuae.adapter.Orderhome_Adapter;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.model.Order_method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ravi on 29/07/15.
 */
public class Fragment_Myorder_Home extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    //all class objects
    UserFunctions UF;
    String json_save;
    SessionManager sm;
    ConnectionDetector cd;
    GetJson_save getJson_save;
    //all controls
    private SwipeRefreshLayout swipeRefreshLayout;
    ListView order_listview;

    //all variales
    private static String TEXT_FRAGMENT="Moving Home";
    String android_id;


    ArrayList<Order_method> actorsList;
    Orderhome_Adapter adapter;
    View rootView;
    LinearLayout norecord;

    public Fragment_Myorder_Home()
    {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_order, container, false);

        Log.e("********************", "******************************");
        Log.e("********************", "MyorderHome page 1 api call");
        Log.e("********************", "******************************");
        init();

        //for swipr refresh function
        swipeRefreshLayout.setOnRefreshListener(Fragment_Myorder_Home.this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run()
                                    {

                                        swipeRefreshLayout.setRefreshing(true);
                                        if (cd.isConnectingToInternet()) {
                                            getJson_save = new GetJson_save();
                                            getJson_save.execute();
                                        } else
                                        {
                                            UF.msg("Check Your Internet Connection.");
                                        }
                                    }
                                }
        );

        order_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String postloadid = actorsList.get(position).getload_inquiry_no();
                String source_addr = actorsList.get(position).getinquiry_source_addr();
                String destination_addr = actorsList.get(position).getinquiry_destination_addr();
                String sizeTypeDesc = actorsList.get(position).getSizeTypeDesc();

                Intent i = new Intent(getActivity().getApplicationContext(), Activity_MoveMyHome_Detail.class);
                i.putExtra("id", postloadid);
                i.putExtra("source_addr", source_addr);
                i.putExtra("destination_addr", destination_addr);
                i.putExtra("sizeTypeDesc", sizeTypeDesc);
                i.putExtra("status", actorsList.get(position).getstatus());
                i.putExtra("inquiry_source_lat", actorsList.get(position).gets_lat());
                i.putExtra("inquiry_source_lng", actorsList.get(position).gets_lng());
                i.putExtra("inquiry_destionation_lat", actorsList.get(position).getd_lat());
                i.putExtra("inquiry_destionation_lng", actorsList.get(position).getd_lng());
                i.putExtra("feedback_rating", actorsList.get(position).getextra3());
                i.putExtra("load_inquiry_shipping_date", actorsList.get(position).getload_inquiry_shipping_date());
                i.putExtra("load_inquiry_shipping_time", actorsList.get(position).getextra2());
                i.putExtra("SizeTypeCode", actorsList.get(position).getload_inquiry_shipping_time());
                i.putExtra("feedback_msg", actorsList.get(position).getextra4());
                i.putExtra("aed", actorsList.get(position).getshipper_id());

                startActivity(i);
                getActivity().overridePendingTransition(R.anim.right_in, R.anim.left_out);


            }
        });

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

        android_id = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Fragment_Myorder_Home.this.getActivity());
        UF = new UserFunctions(Fragment_Myorder_Home.this.getActivity());
        cd = new ConnectionDetector(Fragment_Myorder_Home.this.getActivity());

        order_listview=(ListView)rootView.findViewById(R.id.listView_order);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView. findViewById(R.id.swipe_refresh_layout);
        norecord=(LinearLayout)rootView.findViewById(R.id.norecord);
        norecord.setVisibility(View.GONE);

        actorsList = new ArrayList<Order_method>();

    }

    @Override
    public void onRefresh()
    {
        swipeRefreshLayout.setRefreshing(false);

        if (cd.isConnectingToInternet())
        {
            getJson_save = new GetJson_save();
            getJson_save.execute();

        }
        else
        {
            UF.msg("Check Your Internet Connection.");
        }

    }

    private class GetJson_save extends AsyncTask<Void, Void, String> {

       ProgressDialog loading ;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
           /* loading = new ProgressDialog(Fragment_Myorder_Home.this.getActivity());
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);*/


        }

        @Override
        protected String doInBackground(Void... params) {

            JSONObject prms = new JSONObject();
            JSONObject prmsLogin = new JSONObject();
            JSONArray jsonArray = new JSONArray();


            try {

                prmsLogin.put("fromdate", "");
                prmsLogin.put("inquiry_destination_city", "");
                prmsLogin.put("inquiry_source_city", "");
                prmsLogin.put("owner_id", sm.getUniqueId().toString());
                prmsLogin.put("status", "");
                prmsLogin.put("todate","");
                jsonArray.put(prmsLogin);
                prms.put("shipper_order", jsonArray);


            } catch (JSONException e)
            {

                e.printStackTrace();
            }

            json_save = UF.RegisterUser("shipper/GetShipperOrdersDetails", prms);
            Log.e("--------------------", "----------------------------------");
            Log.e("Home Post--", prms.toString());
            Log.e("Home url---", "shipper/GetShipperOrdersDetails");

            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            swipeRefreshLayout.setRefreshing(false);

            if (json_save.equals("lost"))
            {
                UF.msg("Connection Problem.");

            } else
            {
                if (json_save.equalsIgnoreCase("0"))
                {
                    UF.msg("Invalid");
                } else
                {
                    try
                    {



                        JSONObject jobj = new JSONObject(json_save);
                        Log.e("Home Get--", json_save.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();

                        Log.e("Myorder Home status >",status);
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("1"))
                        {

                            JSONArray array = new JSONArray();
                            array = jobj.getJSONArray("message");
                            if(actorsList.size()>0){
                                actorsList.clear();
                            }
                            for(int i=0;i<array.length();i++)
                            {
                                //order type ...H ... display only move type home datya
                                if(array.getJSONObject(i).getString("order_type_flag").toString().equalsIgnoreCase("H"))
                                {
                                    actorsList.add(new Order_method(
                                            array.getJSONObject(i).getString("load_inquiry_no"),
                                            array.getJSONObject(i).getString("SizeTypeDesc"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_date"),
                                            array.getJSONObject(i).getString("Total_cost"),
                                            array.getJSONObject(i).getString("inquiry_source_addr"),
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),
                                            array.getJSONObject(i).getString("NoOfTruck"),
                                            array.getJSONObject(i).getString("NoOfDriver"),
                                            array.getJSONObject(i).getString("NoOfLabour"),
                                            array.getJSONObject(i).getString("NoOfHandiman"),
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("TotalPackingCharge"),
                                            array.getJSONObject(i).getString("status"),
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_time"),
                                            array.getJSONObject(i).getString("feedback_rating"),//extre
                                            array.getJSONObject(i).getString("feedback_msg"),//extre
                                            array.getJSONObject(i).getString("cbmlink"),//extre
                                            array.getJSONObject(i).getString("inquiry_source_lat"),//extre
                                            array.getJSONObject(i).getString("inquiry_source_lng"),//extre
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lng"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("SizeTypeCode"),
                                            array.getJSONObject(i).getString("rem_amt_to_receive")));

                                }

                            }
                            if (getActivity() != null)
                            {
                                if(adapter==null)
                                {
                                    adapter = new  Orderhome_Adapter(getActivity(),actorsList);
                                    order_listview.setAdapter(adapter);

                                }
                                else{
                                    adapter.notifyDataSetChanged();
                                }
                            }

                            if(actorsList.size()==0)
                            {
                                norecord.setVisibility(View.VISIBLE);
                            }


                        }
                        else
                        {
                            swipeRefreshLayout.setRefreshing(false);
                            norecord.setVisibility(View.VISIBLE);
                           // UF.msg(message + "");
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                    }


                }
            }


        }
    }

    //its for tab
    public static Fragment newFreeTruck(String s)
    {
        Fragment_Myorder_Home mFragment = new  Fragment_Myorder_Home();
        Bundle mBundle = new Bundle();
        mBundle.putString(TEXT_FRAGMENT, s);
        mFragment.setArguments(mBundle);
        return mFragment;
    }
    @Override
    public void onResume()
    {
        super.onResume();
        swipeRefreshLayout.setRefreshing(false);
            if (cd.isConnectingToInternet())
            {
                getJson_save = new GetJson_save();
                getJson_save.execute();
            } else {
                UF.msg("Check Your Internet Connection.");
            }


    }


}
