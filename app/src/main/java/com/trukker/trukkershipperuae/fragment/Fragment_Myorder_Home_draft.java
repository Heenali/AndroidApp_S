package com.trukker.trukkershipperuae.fragment;
//listing home(2 menu)

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.activity.Activity_payment;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.httpsrequest.HTTPUtils;
import com.trukker.trukkershipperuae.model.Order_method;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ravi on 29/07/15.
 */
public class Fragment_Myorder_Home_draft extends Fragment implements SwipeRefreshLayout.OnRefreshListener
{

    //all class objects
    UserFunctions UF;
    String json_save;
    SessionManager sm;
    ConnectionDetector cd;
    ProgressDialog loading ;
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

    public Fragment_Myorder_Home_draft()
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
        swipeRefreshLayout.setOnRefreshListener(this);

        swipeRefreshLayout.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        swipeRefreshLayout.setRefreshing(true);

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
                                }
        );

        order_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {



                Constants.Draft="Y";
                Constants.paymentpage_point="N";
                Constants.AddonServices="";
                String array = actorsList.get(position).getshipper_id();
                Log.e("array to pass payment", array);
                Intent i = new Intent(getActivity().getApplicationContext(), Activity_payment.class);

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
        sm = new SessionManager(Fragment_Myorder_Home_draft.this.getActivity());
        UF = new UserFunctions(Fragment_Myorder_Home_draft.this.getActivity());
        cd = new ConnectionDetector(Fragment_Myorder_Home_draft.this.getActivity());

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
                SyncMethod(UserFunctions.URL+"postorder/GetDraftOrderDetails?shipperid="+sm.getUniqueId()+"&inqno=");
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



    //its for tab
    public static Fragment newFreeTruck(String s)
    {
        Fragment_Myorder_Home_draft mFragment = new  Fragment_Myorder_Home_draft();
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
                SyncMethod(UserFunctions.URL+"postorder/GetDraftOrderDetails?shipperid="+sm.getUniqueId()+"&inqno=");
            }
            else
            {
                UF.msg("Check Your Internet Connection.");
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
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam", "screen>>" + aResponse);
                        aResponse = aResponse.trim();
                        aResponse = aResponse.substring(1, aResponse.length() - 1);
                        aResponse = aResponse.replace("\\", "");

                        swipeRefreshLayout.setRefreshing(false);

                        JSONObject jobj = new JSONObject(aResponse);
                        Log.e("Home Get draft--", jobj.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();

                        Log.e("Myorder Homestatusdraft",status);
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
                                            array.getJSONObject(i).getString("load_inquiry_no"),//6 getload_inquiry_no()
                                            array.getJSONObject(i).getString("SizeTypeDesc"),//1 getSizeTypeDesc()
                                            array.getJSONObject(i).getString("load_inquiry_shipping_date"),//2 getload_inquiry_shipping_date()
                                            array.getJSONObject(i).getString("Total_cost"),//5 getTotal_cost()
                                            array.getJSONObject(i).getString("inquiry_source_addr"),//3 getinquiry_source_addr()
                                            array.getJSONObject(i).getString("inquiry_destination_addr"), // 4 getinquiry_destination_addr()
                                            array.getJSONObject(i).getString("NoOfTruck"),//7 getNoOfTruck()
                                            array.getJSONObject(i).getString("NoOfDriver"),
                                            array.getJSONObject(i).getString("NoOfLabour"),//9 getNoOfLabour()
                                            array.getJSONObject(i).getString("NoOfHandiman"),//8 getNoOfHandiman()
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("TotalPackingCharge"),
                                            array.getJSONObject(i).getString("status"),
                                            array.getJSONObject(i).getString("TotalDistance"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_time"),
                                            array.getJSONObject(i).getString("load_inquiry_shipping_time"),// 10 getextra1()
                                            array.getJSONObject(i).getString("load_inquiry_shipping_time"),// 2 getextra2()
                                            array.getJSONObject(i).getString("cbmlink"),
                                            array.getJSONObject(i).getString("inquiry_source_lat"),
                                            array.getJSONObject(i).getString("inquiry_source_lng"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lng"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).getString("inquiry_destionation_lat"),
                                            array.getJSONObject(i).toString()
                                            ));

/*getextra2()
getload_inquiry_shipping_date()
getinquiry_source_addr()
getinquiry_destination_addr()
getTotal_cost()
getload_inquiry_no()
getNoOfTruck()
getNoOfHandiman()
getNoOfLabour()*/

                                }


                            }
                            if (getActivity() != null) {

                                if (adapter == null) {
                                    adapter = new Orderhome_Adapter(getActivity().getApplicationContext(), actorsList);
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
                            swipeRefreshLayout.setRefreshing(false);
                            norecord.setVisibility(View.VISIBLE);
                           // UF.msg(message + "");
                        }



                    } catch (Exception e) {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }
    public String fetchResult(String url) throws JSONException
    {
        String responseString = "";
        HttpClient httpClient = HTTPUtils.getNewHttpClient(url.startsWith("https"));
        HttpResponse response = null;
        InputStream in;
        URI newURI = URI.create(url);
        HttpGet getMethod = new HttpGet(newURI);
        try {
            response = httpClient.execute(getMethod);
            in = response.getEntity().getContent();
            responseString = convertStreamToString(in);
        } catch (Exception e) {}
        return responseString;
    }
    public static String convertStreamToString(InputStream is) throws Exception
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null)
        {
            sb.append(line);
        }
        is.close();
        return sb.toString();
    }

    public class Orderhome_Adapter extends ArrayAdapter<Order_method>
    {
        ArrayList<Order_method> actorList;
        LayoutInflater vi;
        Context context;
        Activity a;

        public Orderhome_Adapter(Context context, ArrayList<Order_method> objects)
        {
            super(context,  R.layout.list_movehome_order, objects);
            this.context = context;
            this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            this.actorList = objects;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            // convert view = design
            //View v = convertView;
            View rowView;
            ViewHolder vh;
            if (convertView  == null)
            {

                rowView = vi.inflate(R.layout.list_movehome_order, null);
                setViewHolder(rowView);
            }
            else
            {
                rowView = convertView;

            }
            vh = (ViewHolder)rowView.getTag();

            vh.SizeTypeDesc.setText(Html.fromHtml("Home " + actorList.get(position).getSizeTypeDesc()));
            vh.load_inquiry_no.setText(Html.fromHtml(actorList.get(position).getload_inquiry_no()));

            try
            {
                String date=actorList.get(position).getload_inquiry_shipping_date();
                SimpleDateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
                SimpleDateFormat DesiredFormat  = new SimpleDateFormat("dd MMM,yyyy");

                Date date1 = sourceFormat.parse(date);
                String formattedDate = DesiredFormat.format(date1);

                String time=actorList.get(position).getextra2();
                SimpleDateFormat sourceFormattime = new SimpleDateFormat("HH:mm");
                SimpleDateFormat DesiredFormattime  = new SimpleDateFormat("HH:mm");

                Date time1 = sourceFormattime.parse(time);
                String formattedtime = DesiredFormattime.format(time1);
              //  Log.e(" heenali ssssssss", formattedtime);
                vh.load_inquiry_shipping_date.setText(formattedDate+" "+formattedtime);

            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
            vh.cbmlink.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(actorList.get(position).getextra5().equalsIgnoreCase(""))
                    {

                    }
                    else
                    {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actorList.get(position).getextra5()));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.getApplicationContext().startActivity(intent);
                    }

                    //Toast.makeText(getContext(), "Hello", Toast.LENGTH_LONG).show();

                }
            });



            vh.Total_cost.setText(Html.fromHtml("AED "+actorList.get(position).getTotal_cost()));
            vh.inquiry_source_addr.setText(Html.fromHtml(actorList.get(position).getinquiry_source_addr()));
            vh.inquiry_destination_addr.setText(Html.fromHtml(actorList.get(position).getinquiry_destination_addr()));

            vh.NoOfTruck.setText(Html.fromHtml(actorList.get(position).getNoOfTruck()));
            vh.NoOfLabour.setText(Html.fromHtml(actorList.get(position).getNoOfLabour()));
            vh.NoOfHandiman.setText(Html.fromHtml(actorList.get(position).getNoOfHandiman()));
            vh.TotalDistance.setText(Html.fromHtml(actorList.get(position).getextra1()+" KM"));

            String status=actorList.get(position).getstatus();
            vh.reschule_layout.setVisibility(View.GONE);
            if(status.equalsIgnoreCase("02"))
            {
                vh.cancle_layout.setVisibility(View.GONE);
                vh.success_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));
            }
            else if(status.equalsIgnoreCase("05"))
            {
                vh.cancle_layout.setVisibility(View.GONE);
                vh.success_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));
            }
            else if(status.equalsIgnoreCase("06"))
            {
                vh.cancle_layout.setVisibility(View.GONE);
                vh.success_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));
            }
            else if(status.equalsIgnoreCase("07"))
            {
                vh.cancle_layout.setVisibility(View.GONE);
                vh.success_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));
            }
            else if(status.equalsIgnoreCase("08"))
            {
                vh.cancle_layout.setVisibility(View.GONE);
                vh.success_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));
            }
            else if(status.equalsIgnoreCase("25"))
            {
                vh.cancle_layout.setVisibility(View.GONE);
                vh.success_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));

                //vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.movehome_area));
            }
            else if(status.equalsIgnoreCase("45"))
            {
                vh.cancle_layout.setVisibility(View.GONE);
                vh.success_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setVisibility(View.GONE);
                vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));
            }

            vh.cbmlink.setVisibility(View.GONE);
            return rowView;

        }

      class ViewHolder
        {

            public TextView SizeTypeDesc,load_inquiry_no,load_inquiry_shipping_date,Total_cost,inquiry_source_addr,inquiry_destination_addr,
                    NoOfTruck,NoOfLabour,NoOfHandiman,TotalDistance,cbmlink;
            ImageView progress_status_icon,success_icon;
            TextView cancle_layout;
            LinearLayout reschule_layout;

        }
        private void setViewHolder(View rowView)
        {
            ViewHolder vh = new ViewHolder();

            vh.SizeTypeDesc = (TextView) rowView.findViewById(R.id.txt_hometype);
            vh.load_inquiry_no = (TextView) rowView.findViewById(R.id.id_txt);
            vh.load_inquiry_shipping_date = (TextView) rowView.findViewById(R.id.datetime_txt);

            vh.Total_cost = (TextView) rowView.findViewById(R.id.price_txt);
            vh.inquiry_source_addr = (TextView) rowView.findViewById(R.id.sourceadd_txt);
            vh.inquiry_destination_addr = (TextView) rowView.findViewById(R.id.destinationadd_txt);

            vh.NoOfTruck= (TextView) rowView.findViewById(R.id.truck_text);
            vh.NoOfLabour = (TextView) rowView.findViewById(R.id.helper_txt);
            vh.NoOfHandiman = (TextView) rowView.findViewById(R.id.installer_txt);
            vh.TotalDistance = (TextView) rowView.findViewById(R.id.km_txt);
            vh.progress_status_icon = (ImageView) rowView.findViewById(R.id.progress_status_icon );
            vh.success_icon= (ImageView) rowView.findViewById(R.id.success_icon );
            vh.cbmlink= (TextView) rowView.findViewById(R.id.txt_cbmlink);
            vh.cancle_layout= (TextView) rowView.findViewById(R.id.cancle_button);
            vh.reschule_layout=(LinearLayout)rowView.findViewById(R.id.reschule_layout);
            rowView.setTag(vh);

        }


    }
}


