package com.trukker.trukkershipperuae.fragment;
//listing of goods(2 menu)
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.squareup.picasso.Picasso;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.activity.Activity_HirePayment;
import com.trukker.trukkershipperuae.activity.Activity_MoveMyGoods_details;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.model.Order_method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;



/**
 * Created by Ravi on 29/07/15.
 */
public class Fragment_Myorder_HireTruck_draft extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{
    //class object
    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;

    //all variables
    String json_save;
    private static String TEXT_FRAGMENT="Moving Goods";
    String android_id;
    LinearLayout norecord;
    //all controls
    private SwipeRefreshLayout swipeRefreshLayout;
    ListView order_listview;

    ArrayList<Order_method> actorsList;
    OrderGoods_Adapter adapter;
    View rootView;


    public Fragment_Myorder_HireTruck_draft()
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
        Log.e("********************", "MyorderGoods page 1 api call drfat");
        Log.e("********************", "******************************");
        init();



        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);
                                        try {
                                            if (cd.isConnectingToInternet())
                                            {
                                                Log.e("Url draft goods",UserFunctions.URL + "postorder/GetDraftOrderDetails?shipperid=" + sm.getUniqueId() + "&inqno=");
                                                SyncMethod(UserFunctions.URL + "postorder/GetDraftOrderDetails?shipperid=" + sm.getUniqueId() + "&inqno=");
                                            }
                                            else
                                            {
                                                UF.msg("Check Your Internet Connection.");
                                            }
                                        } catch (Exception e) {

                                        }

                                    }
                                }
        );
        order_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {

                Constants.Draft="Y";
                Constants.paymentpage_point="N";
                String array = actorsList.get(position).getshipper_id();
                Log.e("array to pass payment", array);
                Intent i = new Intent(getActivity().getApplicationContext(), Activity_HirePayment.class);

                Constants.HomeDraft_array=array;
                Constants.HomeDraft_array_value=1;
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
        sm = new SessionManager(Fragment_Myorder_HireTruck_draft.this.getActivity());
        UF = new UserFunctions(Fragment_Myorder_HireTruck_draft.this.getActivity());
        cd = new ConnectionDetector(Fragment_Myorder_HireTruck_draft.this.getActivity());

        order_listview=(ListView)rootView.findViewById(R.id.listView_order);
        swipeRefreshLayout = (SwipeRefreshLayout)rootView. findViewById(R.id.swipe_refresh_layout);
        norecord=(LinearLayout)rootView.findViewById(R.id.norecord);
        norecord.setVisibility(View.GONE);

        actorsList = new ArrayList<Order_method>();
    }

    @Override
    public void onRefresh()
    {
        try
        {
            if (cd.isConnectingToInternet())
            {
                Log.e("Url draft",UserFunctions.URL + "postorder/GetDraftOrderDetails?shipperid=" + sm.getUniqueId() + "&inqno=");
                SyncMethod(UserFunctions.URL + "postorder/GetDraftOrderDetails?shipperid=" + sm.getUniqueId() + "&inqno=");
            }
            else
            {
                UF.msg("Check Your Internet Connection.");
            }
        }
        catch (Exception e)
        {

        }


    }

    private class GetJson_save extends AsyncTask<Void, Void, String> {

        ProgressDialog loading ;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loading = new ProgressDialog(Fragment_Myorder_HireTruck_draft.this.getActivity());
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
            swipeRefreshLayout.setRefreshing(true);

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
            Log.e("--------------------", "----------------------------------");
            Log.e("Goods Post--", prms.toString());
            Log.e("Goods url--", "shipper/GetShipperOrdersDetails");
            json_save = UF.RegisterUser("shipper/GetShipperOrdersDetails", prms);


            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            swipeRefreshLayout.setRefreshing(false);




            if (json_save.equals("lost")) {
                UF.msg("Connection Problem.");
            } else {
                if (json_save.equalsIgnoreCase("0")) {
                    UF.msg("Invalid");
                } else {
                    try {

                        JSONObject jobj = new JSONObject(json_save);
                        Log.e("Goods Get--", json_save.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
//                        actorsList = new ArrayList<Order_method>();

                        Log.e("MyorderGoods status...>", status.toString());
                        // Log.e("MyorderGoods message..>", message.toString());
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

                                //excluding all ''H'' data get only goods data
                                if(array.getJSONObject(i).getString("order_type_flag").toString().equalsIgnoreCase("HT"))
                                {
                                    actorsList.add(new Order_method(
                                            array.getJSONObject(i).getString("load_inquiry_no"),
                                            array.getJSONObject(i).getString("SizeTypeDesc"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_date"),
                                            array.getJSONObject(i).getString("Total_cost"),
                                            array.getJSONObject(i).getString("inquiry_source_addr"),
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),
                                            array.getJSONObject(i).getString("drivername"),//truck
                                            array.getJSONObject(i).getString("driver_photo"),//driver
                                            array.getJSONObject(i).getString("mobile_no"),//labour
                                            array.getJSONObject(i).getString("goods_details"),//handimante
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("TotalPackingCharge"),
                                            array.getJSONObject(i).getString("status"),
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("final_status"),//extra2
                                            array.getJSONObject(i).getString("TimeToTravelInMinute"),//3
                                            array.getJSONObject(i).getString("vehicle_reg_no"),//4
                                            array.getJSONObject(i).getString("TotalPackingCharge"),
                                            array.getJSONObject(i).getString("inquiry_source_lat"),
                                            array.getJSONObject(i).getString("inquiry_source_lng"),
                                            array.getJSONObject(i).getString("Hiretruck_NoofDay"),//d_lat
                                            array.getJSONObject(i).getString("Hiretruck_To_datetime"),//d_lng

                                            array.getJSONObject(i).getString("trackurl"),
                                            array.getJSONObject(i).getString("feedback_rating"),
                                            array.getJSONObject(i).getString("feedback_msg"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_time"),
                                            array.getJSONObject(i).toString()

                                    ));
                                }



                            }
                            if(adapter==null){

                                adapter = new  OrderGoods_Adapter(getActivity().getApplicationContext(),actorsList,getActivity());
                                order_listview.setAdapter(adapter);
                            }else{
                                adapter.notifyDataSetChanged();
                            }
                            if(actorsList.size()==0)
                            {
                                norecord.setVisibility(View.VISIBLE);
                            }


                        }
                        else
                        {
                            // UF.msg(message + "");
                            norecord.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
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
    /*private class GetJson_save_refresh extends AsyncTask<Void, Void, String> {

        ProgressDialog loading ;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loading = new ProgressDialog(Fragment_Myorder_Goods.this.getActivity());
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);
            swipeRefreshLayout.setRefreshing(true);

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
            Log.e("--------------------", "----------------------------------");
            Log.e("Goods Post--", prms.toString());
            Log.e("Goods url--", "shipper/GetShipperOrdersDetails");
            json_save = UF.RegisterUser("shipper/GetShipperOrdersDetails", prms);


            return json_save;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            loading.dismiss();
            swipeRefreshLayout.setRefreshing(false);




            if (json_save.equals("lost")) {
                UF.msg("Connection Problem.");
            } else {
                if (json_save.equalsIgnoreCase("0")) {
                    UF.msg("Invalid");
                } else {
                    try {

                        JSONObject jobj = new JSONObject(json_save);
                        Log.e("Goods Get--", json_save.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        actorsList = new ArrayList<Order_method>();

                        Log.e("MyorderGoods status...>", status.toString());
                        // Log.e("MyorderGoods message..>", message.toString());
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("1"))
                        {

                            JSONArray array = new JSONArray();
                            array = jobj.getJSONArray("message");

                            for(int i=0;i<array.length();i++)
                            {

                                //excluding all ''H'' data get only goods data
                                if(array.getJSONObject(i).getString("order_type_flag").toString().equalsIgnoreCase("H"))
                                {

                                }
                                else
                                {
                                    actorsList.add(new Order_method(
                                            array.getJSONObject(i).getString("load_inquiry_no"),
                                            array.getJSONObject(i).getString("SizeTypeDesc"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_date"),
                                            array.getJSONObject(i).getString("Total_cost"),
                                            array.getJSONObject(i).getString("inquiry_source_addr"),
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),
                                            array.getJSONObject(i).getString("drivername"),//truck
                                            array.getJSONObject(i).getString("driver_photo"),//driver
                                            array.getJSONObject(i).getString("mobile_no"),//labour
                                            array.getJSONObject(i).getString("goods_details"),//handimante
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("TotalPackingCharge"),
                                            array.getJSONObject(i).getString("status"),
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("final_status"),//extra2
                                            array.getJSONObject(i).getString("TimeToTravelInMinute"),//3
                                            array.getJSONObject(i).getString("vehicle_reg_no"),//4
                                            array.getJSONObject(i).getString("TotalPackingCharge"),
                                            array.getJSONObject(i).getString("inquiry_source_lat"),
                                            array.getJSONObject(i).getString("inquiry_source_lng"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lng"),

                                            array.getJSONObject(i).getString("trackurl"),
                                            array.getJSONObject(i).getString("feedback_rating"),
                                            array.getJSONObject(i).getString("feedback_msg"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_time"),
                                            array.getJSONObject(i).getString("shipper_id")

                                    ));
                                }



                            }
//                            adapter = new  OrderGoods_Adapter(getActivity().getApplicationContext(),actorsList,getActivity());
//                            order_listview.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                        }
                        else
                        {
                            UF.msg(message + "");
                            norecord.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    } catch (JSONException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        swipeRefreshLayout.setRefreshing(false);
                    }


                }
            }


        }
    }*/
    public static Fragment newFreeTruck(String s)
    {
        // its for tab
        Fragment_Myorder_HireTruck_draft mFragment = new Fragment_Myorder_HireTruck_draft();
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
            Log.e("Url draft",UserFunctions.URL + "postorder/GetDraftOrderDetails?shipperid=" + sm.getUniqueId() + "&inqno=");
            SyncMethod(UserFunctions.URL + "postorder/GetDraftOrderDetails?shipperid=" + sm.getUniqueId() + "&inqno=");
        }
        else
        {
            UF.msg("Check Your Internet Connection.");
        }


    }
    public class OrderGoods_Adapter extends ArrayAdapter<Order_method>
    {
        ArrayList<Order_method> actorList;
        LayoutInflater vi;
        Context context;
        private Activity parentActivity;
        String json_save;
        String  id_value,rating_value="0.0",feedback_value;
        Dialog dialog;
        String feddbackrating_store,feedbackmsg_store,lodeid_store;
        public  OrderGoods_Adapter(Context context, ArrayList<Order_method> objects, Activity a)
        {
            super(context,  R.layout.list_order_hiretruck, objects);
            this.context = context;
            this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.actorList = objects;
            this.parentActivity=a;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // convert view = design
            //View v = convertView;
            View rowView;
            ViewHolder vh;

            if (convertView  == null)
            {

                rowView = vi.inflate(R.layout.list_order_hiretruck, null);
                setViewHolder(rowView);
            }
            else
            {
                rowView = convertView;

            }
            vh = (ViewHolder)rowView.getTag();

            vh.ratingdetail_layout.setVisibility(View.GONE);
            vh.driverdetail_layout.setVisibility(View.GONE);
            vh.cancleorder_layout.setVisibility(View.GONE);
            vh.noofdays_txt.setText(actorList.get(position).getd_lat()+" Days");
            vh.trucktype_txt.setText(Html.fromHtml(actorList.get(position).getSizeTypeDesc()));

            vh.load_inquiry_no.setText(Html.fromHtml(actorList.get(position).getload_inquiry_no()));
            vh.inquiry_source_addr.setText(Html.fromHtml(actorList.get(position).getinquiry_source_addr()));
            try
            {

                String date=actorList.get(position).getd_lng();
                SimpleDateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
                SimpleDateFormat DesiredFormat  = new SimpleDateFormat("dd-MMM-yy");


                Date date1 = sourceFormat.parse(date);
                String formattedDate = DesiredFormat.format(date1);
                // Log.e("date Time Heenali",formattedDate);



                // Log.e("date Time Heenali",formattedDate+" "+formattedDate1);
                vh.SizeTypeDesc.setText(formattedDate);
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }

            try
            {

                String date=actorList.get(position).getload_inquiry_shipping_date();

                SimpleDateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
                SimpleDateFormat DesiredFormat  = new SimpleDateFormat("dd-MMM-yy");


                Date date1 = sourceFormat.parse(date);
                String formattedDate = DesiredFormat.format(date1);
                // Log.e("date Time Heenali",formattedDate);


                SimpleDateFormat sourceFormat1 = new SimpleDateFormat("HH:mm");
                SimpleDateFormat DesiredFormat1  = new SimpleDateFormat("HH:mm");
                Date date11 = sourceFormat1.parse(actorList.get(position).getload_inquiry_shipping_time());
                String formattedDate1 = DesiredFormat1.format(date11);
                // Log.e("date Time Heenali",formattedDate+" "+formattedDate1);
                vh.load_inquiry_shipping_date.setText(formattedDate+" "+formattedDate1);
                vh.load_inquiry_shipping_date1.setText(formattedDate+" "+formattedDate1);

            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }


            vh.driver_name.setText(Html.fromHtml(actorList.get(position).getNoOfTruck()));
            String phoone=actorList.get(position).getextra3();

            //String photo=actorList.get(position).getNoOfDriver();
            if(actorList.get(position).getNoOfTruck().toString().equalsIgnoreCase(""))
            {
                vh.driver_name.setText("Driver Not Assigned");

                Picasso.with(context).load(R.drawable.ic_profile).into(vh.driver_photo);

            }
            else
            {
                if(actorList.get(position).getNoOfDriver().equalsIgnoreCase(""))
                {
                    Picasso.with(context).load(R.drawable.ic_profile).into(vh.driver_photo);

                }
                else
                {
                    Log.i("Image path", UserFunctions.URLIMG + actorList.get(position).getNoOfDriver().toString());
                    //Picasso.with(context).load(UserFunctions.URLIMG+actorList.get(position).getNoOfDriver()).into(vh.driver_photo);
                    UrlImageViewHelper.setUrlDrawable(vh.driver_photo, UserFunctions.URLIMG + actorList.get(position).getNoOfDriver().toString(), R.drawable.ic_profile);

                }
            }



            // vh.Total_cost.setText(Html.fromHtml("AED "+actorList.get(position).getTotal_cost()));

            String status=actorList.get(position).getstatus();

            if(status.equalsIgnoreCase("02"))
            {
                vh.load_inquiry_shipping_date.setVisibility(View.VISIBLE);
                vh.sheduletitle_txt.setTextColor(Color.parseColor("#B2B2B2"));
                vh.sheduletitle_txt.setText("Start Date ");

                vh.progress_status_icon.setVisibility(View.GONE);
                vh.id_layout.setBackgroundColor(Color.parseColor("#00A651"));
                vh.datetime_layout.setBackgroundColor(Color.parseColor("#00A651"));

                vh.ratingdetail_layout.setVisibility(View.GONE);
                vh.cancleorder_layout.setVisibility(View.GONE);
                vh.driverdetail_layout.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ordergoods_upcoming));

            }
            else if(status.equalsIgnoreCase("05"))
            {

                vh.load_inquiry_shipping_date.setVisibility(View.VISIBLE);
                vh.sheduletitle_txt.setTextColor(Color.parseColor("#B2B2B2"));
                vh.sheduletitle_txt.setText("Start Date ");

                vh.progress_status_icon.setVisibility(View.GONE);
                vh.id_layout.setBackgroundColor(Color.parseColor("#00A651"));
                vh.datetime_layout.setBackgroundColor(Color.parseColor("#00A651"));

                vh.ratingdetail_layout.setVisibility(View.GONE);
                vh.cancleorder_layout.setVisibility(View.GONE);
                vh.driverdetail_layout.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ordergoods_upcoming));


            }
            else if(status.equalsIgnoreCase("06"))
            {

                vh.load_inquiry_shipping_date.setVisibility(View.VISIBLE);
                vh.sheduletitle_txt.setTextColor(Color.parseColor("#B2B2B2"));
                vh.sheduletitle_txt.setText("Start Date ");

                vh.progress_status_icon.setVisibility(View.GONE);
                vh.id_layout.setBackgroundColor(Color.parseColor("#00A651"));
                vh.datetime_layout.setBackgroundColor(Color.parseColor("#00A651"));

                vh.ratingdetail_layout.setVisibility(View.GONE);
                vh.cancleorder_layout.setVisibility(View.GONE);
                vh.driverdetail_layout.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ordergoods_upcoming));

            }
            else if(status.equalsIgnoreCase("07"))
            {
                vh.load_inquiry_shipping_date.setVisibility(View.VISIBLE);
                vh.sheduletitle_txt.setTextColor(Color.parseColor("#B2B2B2"));
                vh.sheduletitle_txt.setText("Start Date ");

                vh.progress_status_icon.setVisibility(View.GONE);
                vh.id_layout.setBackgroundColor(Color.parseColor("#00A651"));
                vh.datetime_layout.setBackgroundColor(Color.parseColor("#00A651"));

                vh.ratingdetail_layout.setVisibility(View.GONE);
                vh.cancleorder_layout.setVisibility(View.GONE);
                vh.driverdetail_layout.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ordergoods_upcoming));

            }
            else if(status.equalsIgnoreCase("08"))
            {

                vh.load_inquiry_shipping_date.setVisibility(View.VISIBLE);
                vh.sheduletitle_txt.setTextColor(Color.parseColor("#B2B2B2"));
                vh.sheduletitle_txt.setText("Start Date ");

                vh.progress_status_icon.setVisibility(View.GONE);
                vh.id_layout.setBackgroundColor(Color.parseColor("#00A651"));
                vh.datetime_layout.setBackgroundColor(Color.parseColor("#00A651"));

                vh.ratingdetail_layout.setVisibility(View.GONE);
                vh.cancleorder_layout.setVisibility(View.GONE);
                vh.driverdetail_layout.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ordergoods_upcoming));

            }
            else if(status.equalsIgnoreCase("25"))
            {

                vh.load_inquiry_shipping_date.setVisibility(View.VISIBLE);
                vh.sheduletitle_txt.setTextColor(Color.parseColor("#B2B2B2"));
                vh.sheduletitle_txt.setText("Start Date ");

                vh.progress_status_icon.setVisibility(View.GONE);
                vh.id_layout.setBackgroundColor(Color.parseColor("#00A651"));
                vh.datetime_layout.setBackgroundColor(Color.parseColor("#00A651"));

                vh.ratingdetail_layout.setVisibility(View.GONE);
                vh.cancleorder_layout.setVisibility(View.GONE);
                vh.driverdetail_layout.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ordergoods_upcoming));

                //vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.movehome_area));
            }
            else if(status.equalsIgnoreCase("45"))
            {
                vh.load_inquiry_shipping_date.setVisibility(View.VISIBLE);
                vh.sheduletitle_txt.setTextColor(Color.parseColor("#B2B2B2"));
                vh.sheduletitle_txt.setText("Start Date ");

                vh.progress_status_icon.setVisibility(View.GONE);
                vh.id_layout.setBackgroundColor(Color.parseColor("#00A651"));
                vh.datetime_layout.setBackgroundColor(Color.parseColor("#00A651"));

                vh.ratingdetail_layout.setVisibility(View.GONE);
                vh.cancleorder_layout.setVisibility(View.GONE);
                vh.driverdetail_layout.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.ordergoods_upcoming));

            }
            vh.setrating.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    feddbackrating_store=actorList.get(position).getfeedback_rating();
                    feedbackmsg_store=actorList.get(position).getfeedback_msg();
                    lodeid_store=actorList.get(position).getload_inquiry_no();
                    CustomDialogClass_hometype hometypeclass = new CustomDialogClass_hometype(parentActivity, position);
                    hometypeclass.show();

                    // dialog = new Dialog(parentActivity);
                    //dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    //dialog.setContentView(R.layout.dialog_goodsrating);

                    // dialog.show();

                }
            });
            vh.driver_call.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    String mno=actorList.get(position).getNoOfLabour();
                    if(mno.equalsIgnoreCase(""))
                    {

                    }
                    else
                    {
                        try
                        {

                            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + actorList.get(position).getNoOfLabour().toString()));
                            callIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(callIntent);
                        }

                        catch (android.content.ActivityNotFoundException ex)
                        {
                            Toast.makeText(context, "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                        }

                    }

                }
            });

            return rowView;

        }

        class ViewHolder {

            TextView cancleorder_layout,sheduletitle_txt,load_inquiry_no,inquiry_source_addr,SizeTypeDesc,load_inquiry_shipping_date,load_inquiry_shipping_date1,driver_name;
            ImageView progress_status_icon,driver_photo,driver_call;
            LinearLayout datetime_layout,id_layout;
            LinearLayout driverdetail_layout,ratingdetail_layout;
            TextView rating_txt,totalcost_txt,setrating;
            LinearLayout reschule_layout;
            ImageView rating_icon;
            TextView noofdays_txt;
            TextView trucktype_txt;

        }
        private void setViewHolder(View rowView)
        {
            ViewHolder vh = new ViewHolder();

            vh.load_inquiry_no = (TextView) rowView.findViewById(R.id.id_txt);
            vh.noofdays_txt = (TextView) rowView.findViewById(R.id.noofdays_txt);
            vh.trucktype_txt = (TextView) rowView.findViewById(R.id.trucktype_txt);
            vh.inquiry_source_addr = (TextView) rowView.findViewById(R.id.movingfrom_txt);
            vh.SizeTypeDesc = (TextView) rowView.findViewById(R.id.enddate_txt);

            vh.load_inquiry_shipping_date = (TextView) rowView.findViewById(R.id.startdate_txt);
            vh.load_inquiry_shipping_date1 = (TextView) rowView.findViewById(R.id.datetime_txt);
            vh.driver_name = (TextView) rowView.findViewById(R.id.drivername_txt);


            vh.driver_photo= (ImageView) rowView.findViewById(R.id.driver_icon);
            vh.driver_call = (ImageView) rowView.findViewById(R.id.calling_icon);
            vh.progress_status_icon= (ImageView) rowView.findViewById(R.id.progress_status_icon);

            vh.id_layout=(LinearLayout)rowView.findViewById(R.id.id_layout);
            vh.datetime_layout=(LinearLayout)rowView.findViewById(R.id.datetime_layout);
            vh.sheduletitle_txt= (TextView) rowView.findViewById(R.id.startdatetitle_txt);

            vh.ratingdetail_layout=(LinearLayout)rowView.findViewById(R.id.ratingdetail_layout);
            vh.driverdetail_layout=(LinearLayout)rowView.findViewById(R.id.driverdetail_layout);
            vh.cancleorder_layout=(TextView)rowView.findViewById(R.id.cancle_button);
            vh.rating_txt=(TextView)rowView.findViewById(R.id.rating_txt);
            vh.totalcost_txt=(TextView)rowView.findViewById(R.id.totalcost_txt);
            vh.rating_icon=(ImageView)rowView.findViewById(R.id.rating_icon);
            vh.setrating=(TextView)rowView.findViewById(R.id.setrating);
            vh.reschule_layout=(LinearLayout)rowView.findViewById(R.id.reschule_layout);
            rowView.setTag(vh);

        }
        private void call()
        {

        }


        class CustomDialogClass_hometype extends Dialog
        {

            public Context context;
            public Dialog d;
            EditText comment_txt;
            ProgressDialog loading;
            RatingBar rating;
            TextView submit_btn;
            int pos;

            public CustomDialogClass_hometype(Context a, int pos)
            {
                super(a);
                this.context = a;
                this.pos = pos;
            }

            @Override
            protected void onCreate(Bundle savedInstanceState)
            {
                super.onCreate(savedInstanceState);
                this.requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.dialog_goodsrating);
                context=this.getContext();
                submit_btn=(TextView)findViewById(R.id.submit_btn);
                comment_txt=(EditText)findViewById(R.id.feddback_edittext);
                rating=(RatingBar)findViewById(R.id.ratingBar);
                rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser)
                    {
                        // TODO Auto-generated method stub

                        //Toast.makeText(getApplicationContext(),String.valueOf(rating),Toast.LENGTH_SHORT).show();
                        rating_value=String.valueOf(rating);

                        actorsList.get(pos).setfeedback_rating(rating_value);

                    }
                });

                if(feedbackmsg_store.equalsIgnoreCase("") && feddbackrating_store.equalsIgnoreCase(""))
                {
                    comment_txt.setText("");
                }
                else
                {

                    comment_txt.setText(feedbackmsg_store);
                    submit_btn.setVisibility(View.GONE);
                    comment_txt.setFocusable(false);
                    rating.setFocusable(false);
                    rating.setRating(Float.parseFloat(feddbackrating_store + "f"));
                    rating.setEnabled(false);
                }
                submit_btn.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if( comment_txt.getText().toString().equalsIgnoreCase(""))
                        {
                            comment_txt.setError("Enter Valid Feedback");
                        }
                        else if(rating_value.equalsIgnoreCase("0.0"))
                        {
                            Toast.makeText(parentActivity,"Select rating value",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            id_value=lodeid_store;

                            feedback_value=comment_txt.getText().toString();

                            actorsList.get(pos).setfeedback_msg(feedback_value);

                            try
                            {
                                new GetJson_save_feedback().execute();

                                adapter.notifyDataSetChanged();

                            }
                            catch (Exception e)
                            {

                            }

                        }

                    }
                });

            }


            private class GetJson_save_feedback extends AsyncTask<Void, Void, String>
            {
                UserFunctions UF = new UserFunctions(parentActivity);
                ProgressDialog pDialog = new ProgressDialog(parentActivity);
                String android_id = Settings.Secure.getString(parentActivity.getContentResolver(), Settings.Secure.ANDROID_ID);
                private ProgressDialog loading;
                @Override
                protected void onPreExecute()
                {
                    super.onPreExecute();
                    loading = new ProgressDialog(parentActivity);
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

                    try
                    {

                        prmsLogin.put("load_inquiry_no",id_value);
                        prmsLogin.put("star_rating",rating_value);
                        prmsLogin.put("feedback", feedback_value);
                        prmsLogin.put("created_by",android_id.toString());
                        prmsLogin.put("created_host", android_id.toString());


                        jsonArray.put(prmsLogin);
                        prms.put("feedback", prmsLogin);


                    }
                    catch (JSONException e)
                    {

                        e.printStackTrace();
                    }
                    Log.e("feedback json >>>>>", prms + "");
                    json_save = UF.RegisterUser("login/SaveFeedback", prms);

                    return json_save;
                }

                @Override
                protected void onPostExecute(String result)
                {
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

                                Log.e("feedback  data get ", jobj.toString());
                                String status = jobj.getString("status");
                                String message = jobj.getString("message").toString();
                                if (status.equalsIgnoreCase("1"))
                                {

                                    UF.msg(message + "");
                                    if (cd.isConnectingToInternet())
                                    {
                                        Log.e("Url draft",UserFunctions.URL + "postorder/GetDraftOrderDetails?shipperid=" + sm.getUniqueId() + "&inqno=");
                                        SyncMethod(UserFunctions.URL + "postorder/GetDraftOrderDetails?shipperid=" + sm.getUniqueId() + "&inqno=");
                                    }
                                    else
                                    {
                                        UF.msg("Check Your Internet Connection.");
                                    }


                                    dismiss();


                                } else {
                                    UF.msg(message + "");
                                }
                            }
                            catch (JSONException e)
                            {

                                e.printStackTrace();
                            }
                        }
                    }

                }
            }

        }
    }
    public void SyncMethod(final String GetUrl)
    {

        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
            // After call for background.start this run method call
            public void run() {
                try {
                    String url = GetUrl;
                    String SetServerString = "";
                    // document all_stuff = null;

                    SetServerString = fetchResult(url);
                    threadMsg(SetServerString);
                } catch (Throwable t) {
                    Log.e("Animation", "Thread  exception " + t);
                }
            }

            private void threadMsg(String msg) {

                if (!msg.equals(null) && !msg.equals("")) {
                    Message msgObj = handler11.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler11.sendMessage(msgObj);
                }
            }

            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler11 = new Handler() {
                public void handleMessage(Message msg) {
                    try {

                        swipeRefreshLayout.setRefreshing(false);
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam", "screen>>" + aResponse);
                        aResponse = aResponse.trim();
                        aResponse = aResponse.substring(1, aResponse.length() - 1);
                        aResponse = aResponse.replace("\\", "");

                        //swipeRefreshLayout.setRefreshing(false);
                        JSONObject jobj = new JSONObject(aResponse);
                        Log.e("Goods Get draft--", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
//                        actorsList = new ArrayList<Order_method>();

                        Log.e(" status. hh..>", status.toString());
                        // Log.e("MyorderGoods message..>", message.toString());
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("1"))
                        {

                            JSONArray array = new JSONArray();
                            array = jobj.getJSONArray("message");
                            Log.e("array draft", array.toString());
                            if(actorsList.size()>0){
                                actorsList.clear();
                            }

                            for(int i=0;i<array.length();i++)
                            {

                                //excluding all ''H'' data get only goods data
                                if(array.getJSONObject(i).getString("order_type_flag").toString().equalsIgnoreCase("HT"))
                                {


                                    actorsList.add(new Order_method(
                                            array.getJSONObject(i).getString("load_inquiry_no"),
                                            array.getJSONObject(i).getString("SizeTypeDesc"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_date"),
                                            array.getJSONObject(i).getString("Total_cost"),
                                            array.getJSONObject(i).getString("inquiry_source_addr"),
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),//truck
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),//driver
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),//labour
                                            array.getJSONObject(i).getString("goods_details"),//handimante
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("TotalPackingCharge"),
                                            array.getJSONObject(i).getString("status"),
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),//extra2
                                            array.getJSONObject(i).getString("TimeToTravelInMinute"),//3
                                            array.getJSONObject(i).getString("TimeToTravelInMinute"),//4
                                            array.getJSONObject(i).getString("TotalPackingCharge"),
                                            array.getJSONObject(i).getString("inquiry_source_lat"),
                                            array.getJSONObject(i).getString("inquiry_source_lng"),
                                            array.getJSONObject(i).getString("Hiretruck_NoofDay"),//d_lat
                                            array.getJSONObject(i).getString("Hiretruck_To_datetime"),//d_lng

                                            array.getJSONObject(i).getString("trackurl"),
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),
                                            array.getJSONObject(i).getString("inquiry_destination_addr"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_time"),
                                            array.getJSONObject(i).toString()

                                    ));
                                }


                            }
                            if (getActivity() != null) {

                                if (adapter == null) {

                                    adapter = new OrderGoods_Adapter(getActivity().getApplicationContext(), actorsList, getActivity());
                                    order_listview.setAdapter(adapter);
                                } else {
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
                            // UF.msg(message + "");
                            norecord.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);
                        }


                    } catch (Exception e) {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }
    public String fetchResult(String urlString) throws JSONException {
        StringBuilder builder;
        BufferedReader reader;
        URLConnection connection = null;
        URL url = null;
        String line;
        builder = new StringBuilder();
        reader = null;
        try {
            url = new URL(urlString);
            connection = url.openConnection();

            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            //Log.d("DATA", builder.toString());
        } catch (Exception e) {

        }
        //JSONArray arr=new JSONArray(builder.toString());
        return builder.toString();
    }
}
