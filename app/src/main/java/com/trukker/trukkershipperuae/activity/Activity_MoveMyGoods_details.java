package com.trukker.trukkershipperuae.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.DirectionsJSONParser;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by admin on 8/31/2016.
 */

public class Activity_MoveMyGoods_details extends AppCompatActivity

{
    String page;
    TextView billing_title;
    String gmail_sub,gmail_body;
    public String json_save;
    String android_id;
    ImageView call_driver;
    UserFunctions UF;
    TextView reshedule_btn;
    TextView back,cancleorder_submit_orange;
    SessionManager sm;
    String driver_mno,driver_name,driver_photo;
    ConnectionDetector cd;
    LatLngBounds bounds;
    private GoogleMap googleMap;
    public Polyline line;
    Runnable runnable;
    boolean islineFlag=false;
    String messdata;
    TextView sharestatus_submit_nodrier;
    Thread t;
    String trackurl;
    String status="0";
    private Handler handler;
    String source_addr,destination_addr,sizeTypeDesc;
    String id;
    String source_lat_main,source_long_main,dest_lat_main,dest_long_main;
    ImageView driver_image_iv;
    String feedback_rating;
    String feedback_msgs;
    TextView destinationadd_text,drivername_text,drivermno_text,trucktype_text,trucknumber_text,movingfrom_text,movingto_text,distance_text
            ,eta_text,goodsdetails_text;
    TextView cancleorder_submit,starestatus_submit,id_text;
    LinearLayout form_details;
    String goods_details,TotalDistance,TotalTravelingRate,vehicle_reg_no;
    private final static int INTERVAL = 5000; //2 minutes
    Handler mHandler = new Handler();
    CardView showdatils;
    MarkerOptions marker1=null ;
    String shipper_id, load_inquiry_shipping_date;
    private ViewGroup infoWindow;
    TextView title1, value1, title,value2;
    String load_inquiry_shipping_time;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            stopRepeatingTask();
            finish();


        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movemygoods_details);
      //  this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        Constants.back=1;
        Constants.cancel_conformation="";
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stopRepeatingTask();
                finish();
            }
        });

        drivername_text.setText(driver_name);
        drivermno_text.setText(driver_mno);
        trucktype_text.setText(sizeTypeDesc);
        trucknumber_text.setText(vehicle_reg_no);
        movingfrom_text.setText(source_addr);
        movingto_text.setText(destination_addr);
        distance_text.setText(TotalDistance+" KM");
        int d=00,d1=00;
     //   Toast.makeText(getApplicationContext(),TotalTravelingRate.toString(),Toast.LENGTH_SHORT).show();
        d=Integer.parseInt(TotalTravelingRate)/60;
        d1=Integer.parseInt(TotalTravelingRate)%60;
        eta_text.setText(String.valueOf(d)+" hr "+String.valueOf(d1)+" min");
        goodsdetails_text.setText(goods_details);
        id_text.setText(id);
        showdatils.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(page.equalsIgnoreCase("HireTruck"))
                {

                }
                else
                {
                    if(form_details.getVisibility() == View.VISIBLE)
                    {

                        // Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down2);
                        // form_details.startAnimation(anm);
                        form_details.setVisibility(View.GONE);
                    }
                    else
                    {
                        form_details.setVisibility(View.VISIBLE);
                        Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                        form_details.startAnimation(anm);
                    }
                }


            }
        });
        if(driver_name.equalsIgnoreCase(""))
        {
            if(page.equalsIgnoreCase("HireTruck"))
            {
                showdatils.setVisibility(View.GONE);
                form_details.setVisibility(View.GONE);
                sharestatus_submit_nodrier.setVisibility(View.GONE);
            }
            else
            {
                showdatils.setVisibility(View.GONE);
                form_details.setVisibility(View.VISIBLE);
                sharestatus_submit_nodrier.setVisibility(View.GONE);
            }


        }

        if(driver_photo.equalsIgnoreCase(""))
        {
            driver_image_iv.setBackgroundResource(R.drawable.ic_profile);
        }
        else
        {
            UrlImageViewHelper.setUrlDrawable(driver_image_iv, UserFunctions.URLIMG.toString()+ driver_photo.toString(), R.drawable.ic_profile);

            //Toast.makeText(getApplicationContext(),UserFunctions.URLIMG.toString()+ driver_photo,Toast.LENGTH_SHORT).show();
           // Picasso.with(getApplicationContext()).load(UserFunctions.URLIMG.toString()+ driver_photo).into(driver_image_iv);
        }
        cancleorder_submit_orange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Activity_MoveMyGoods_details.this,Activity_MOveMyHome_Cancel.class);
                i.putExtra("shipper_id",shipper_id);
                i.putExtra("load_inquiry_no",id);
               // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                Activity_MoveMyGoods_details.this.overridePendingTransition( R.anim.slide_enter, R.anim.slide_exit);
            }
        });
        starestatus_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                final Dialog dialog = new Dialog(Activity_MoveMyGoods_details.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.share_status_dialog);
                LinearLayout  sms_button=(LinearLayout)dialog.findViewById(R.id.sms_button);
                LinearLayout  email_button=(LinearLayout)dialog.findViewById(R.id.email_button);
                sms_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendSMS();
                    }
                });
                email_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendemail();
                    }
                });
                dialog.show();

            }
        });
        call_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                try {

                    Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + driver_mno));
                    startActivity(callIntent);
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();
                }
            }
        });
        reshedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CustomDialogClass_reshedule timeclass = new  CustomDialogClass_reshedule(Activity_MoveMyGoods_details.this);
                timeclass.show();


            }
        });

    }
    public void init()
    {

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sm=new SessionManager(Activity_MoveMyGoods_details.this);
        UF=new UserFunctions(Activity_MoveMyGoods_details.this);
        cd= new ConnectionDetector(Activity_MoveMyGoods_details.this);




        final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        googleMap = mapFragment.getMap();


        mapWrapperLayout.init(googleMap , getPixelsFromDp(this, 39 + 20));


        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window_layout, null);
        this.title = (TextView)infoWindow.findViewById(R.id.title);
        this.title1 = (TextView)infoWindow.findViewById(R.id.title1);
        this.value1 = (TextView)infoWindow.findViewById(R.id.value1);
        this.value2 = (TextView)infoWindow.findViewById(R.id.value2);

        googleMap .setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter()
        {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {

                title.setText(marker.getTitle());

                Log.e("marker.getSnippet()", marker.getSnippet().toString());


                List<String> myList = new ArrayList<String>(Arrays.asList(marker.getSnippet().toString().trim().split(",")));

                System.out.println(myList);
                title1.setText(myList.get(0));
                if (myList.get(1).toString().equalsIgnoreCase("s")) {
                    value1.setVisibility(View.GONE);
                } else {
                    value1.setText(myList.get(1));
                    value1.setVisibility(View.VISIBLE);
                }
                if (myList.get(2).toString().equalsIgnoreCase("s")) {
                    value2.setVisibility(View.GONE);
                } else {
                    value2.setText(myList.get(2));
                    value2.setVisibility(View.VISIBLE);
                }

                mapWrapperLayout.setMarkerWithInfoWindow(marker, infoWindow);


                return infoWindow;
            }
        });

        ////


        googleMap.setMyLocationEnabled(false);

        googleMap.getUiSettings().setMyLocationButtonEnabled(false);


        Intent i = getIntent();
        id = i.getStringExtra("id");


        source_addr = i.getStringExtra("source_addr");
        destination_addr = i.getStringExtra("destination_addr");
        sizeTypeDesc = i.getStringExtra("sizeTypeDesc");
        source_lat_main= i.getStringExtra("inquiry_source_lat");
        source_long_main= i.getStringExtra("inquiry_source_lng");
        dest_lat_main= i.getStringExtra("inquiry_destionation_lat");
        dest_long_main= i.getStringExtra("inquiry_destionation_lng");
        String status= i.getStringExtra("status");
        feedback_rating= i.getStringExtra("feedback_rating");
        feedback_msgs= i.getStringExtra("feedback_msg");
        driver_mno= i.getStringExtra("driver_mno");
        driver_name= i.getStringExtra("driver_name");
        driver_photo= i.getStringExtra("driver_photo");
        trackurl= i.getStringExtra("trackurl");

        goods_details= i.getStringExtra("goods_details");
        TotalDistance= i.getStringExtra("TotalDistance");
        TotalTravelingRate= i.getStringExtra("TotalTravelingRate");
        vehicle_reg_no= i.getStringExtra("vehicle_reg_no");
        shipper_id= i.getStringExtra("shipper_id");
        String aed=i.getStringExtra("aed");
        load_inquiry_shipping_date=i.getStringExtra("load_inquiry_shipping_date");
        load_inquiry_shipping_time=i.getStringExtra("load_inquiry_shipping_time");
        billing_title=(TextView)findViewById(R.id.billing_title);
       page=i.getStringExtra("page");

        reshedule_btn=(TextView)findViewById(R.id.reshedule_btn);
        call_driver=(ImageView)findViewById(R.id.call_driver);
        destinationadd_text=(TextView)findViewById(R.id.destinationadd_text);
        drivername_text=(TextView)findViewById(R.id.drivername_text);
        drivermno_text=(TextView)findViewById(R.id.drivermno_text);
        trucktype_text=(TextView)findViewById(R.id.trucktype_text);
        trucknumber_text=(TextView)findViewById(R.id.trucknumber_text);
         movingfrom_text=(TextView)findViewById(R.id.movingfrom_text);
        movingto_text=(TextView)findViewById(R.id.movingto_text);
        distance_text=(TextView)findViewById(R.id.distance_text);
        eta_text=(TextView)findViewById(R.id.eta_text);
        id_text=(TextView)findViewById(R.id.title_text);
        goodsdetails_text=(TextView)findViewById(R.id.goodsdetails_text);
        cancleorder_submit=(TextView)findViewById(R.id.cancleorder_submit);
        cancleorder_submit_orange=(TextView)findViewById(R.id.cancleorder_submit_orange);
        starestatus_submit=(TextView)findViewById(R.id.starestatus_submit);
        form_details=(LinearLayout)findViewById(R.id.form);
        driver_image_iv=(ImageView)findViewById(R.id. driver_image_iv);
        form_details.setVisibility(View.GONE);
        showdatils=(CardView)findViewById(R.id.showdetails);
        back=(TextView)findViewById(R.id.back);
        sharestatus_submit_nodrier=(TextView)findViewById(R.id.sharestatus_submit_nodrier);
        sharestatus_submit_nodrier.setVisibility(View.GONE);
        try
        {
            if(page.equalsIgnoreCase("HireTruck"))
            {
                drowmappath_hiretruck();
            }
            else
            {
                drowmappath();
            }

        }
        catch (Exception e)
        {

        }
        if(page.equalsIgnoreCase("HireTruck"))
        {
            billing_title.setText("Moving From");
            destinationadd_text.setText(source_addr);

        }
        else
        {
            destinationadd_text.setText(destination_addr);
        }

        if(page.equalsIgnoreCase("HireTruck"))
        {
            gmail_sub = "Trukker " + destination_addr + " Date:" + load_inquiry_shipping_date;
            gmail_body = "Hello,\n" +
                    "\tYour shipment detail are as follows,\n" +
                    "\t\n" +
                    "\tTrip ID: " + id + "\n" +
                    "\tSource:" + source_addr + "\n" +

                    "\tDate Of Dispatch:" + load_inquiry_shipping_date + "\n" +
                    "\tTruck Number:" + vehicle_reg_no + "\n" +
                    "\tDriver Name:" + driver_name + "\n" +
                    "\t\n" +
                    "\t\tYou can track the same by clicking on the link below...\n" +
                    "\t\t\n" +
                    "\t\t" + trackurl + "\n" +
                    "\t\t\n" +
                    "Thanks,\n" +
                    "Team TrukkerShipper.\n";
        }
        else
        {
            gmail_sub = "Trukker " + source_addr + " to " + destination_addr + " Date:" + load_inquiry_shipping_date;
            gmail_body = "Hello,\n" +
                    "\tYour shipment detail are as follows,\n" +
                    "\t\n" +
                    "\tTrip ID: " + id + "\n" +
                    "\tSource:" + source_addr + "\n" +
                    "\tDestination:" + destination_addr + "\n" +
                    "\tDate Of Dispatch:" + load_inquiry_shipping_date + "\n" +
                    "\tTruck Number:" + vehicle_reg_no + "\n" +
                    "\tDriver Name:" + driver_name + "\n" +
                    "\t\n" +
                    "\t\tYou can track the same by clicking on the link below...\n" +
                    "\t\t\n" +
                    "\t\t" + trackurl + "\n" +
                    "\t\t\n" +
                    "Thanks,\n" +
                    "Team TrukkerShipper.\n";
        }




        if(status.equalsIgnoreCase("02"))
        {
            cancleorder_submit_orange.setVisibility(View.VISIBLE);
            cancleorder_submit.setVisibility(View.GONE);
            reshedule_btn.setVisibility(View.VISIBLE);
        }
        else if(status.equalsIgnoreCase("26"))
        {
            cancleorder_submit_orange.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.GONE);
            cancleorder_submit.setVisibility(View.GONE);
        }
        else if(status.equalsIgnoreCase("05"))
        {
            cancleorder_submit_orange.setVisibility(View.GONE);
            cancleorder_submit.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.GONE);
        }
        else if(status.equalsIgnoreCase("06"))
        {
            cancleorder_submit_orange.setVisibility(View.GONE);
            cancleorder_submit.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.GONE);
        }
        else if(status.equalsIgnoreCase("07"))
        {
            cancleorder_submit_orange.setVisibility(View.GONE);
            cancleorder_submit.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.GONE);
        }
        else if(status.equalsIgnoreCase("08"))

        {
            cancleorder_submit_orange.setVisibility(View.GONE);
            cancleorder_submit.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.GONE);
        }
        else if(status.equalsIgnoreCase("25"))
        {
            cancleorder_submit_orange.setVisibility(View.GONE);
            cancleorder_submit.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.GONE);

        }
        else if(status.equalsIgnoreCase("45"))
        {
            cancleorder_submit_orange.setVisibility(View.GONE);
            cancleorder_submit.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.GONE);
        }

        startRepeatingTask();

    }
    public void SyncMethod_putlatlon(final String GetUrl) {
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
                        Log.e("detail data of truck", "screen>>" + aResponse);
                        // aResponse = aResponse.trim();
                        // aResponse = aResponse.substring(1, aResponse.length() - 1);
                        // aResponse = aResponse.replace("\\", "");
                        //  Log.e("1posting data truck ","screen>>"+aResponse);
                        //  JSONArray json_Array=new JSONArray(aResponse);
                        //  Log.e("2posting data truck ","screen>>"+json_Array);
                        // for(int i=0;i<json_Array.length();i++)
                        //   {
                        //    JSONObject json_data = json_Array.getJSONObject(i);
                        //     String jsonvalues =  json_data.getString("truck_lat");

                        //         Log.i("DARE", jsonvalues);
                        //  }

                        JSONArray jsonarray = new JSONArray(aResponse);
                        if(jsonarray.length()==0)
                        {

                        }
                        else
                        {
                            googleMap.clear();
                            marker1=null;

                        }
                        if(page.equalsIgnoreCase("HireTruck"))
                        {

                            /////
                            for (int i = 0; i < jsonarray.length(); i++)
                            {
                                JSONObject obj = jsonarray.getJSONObject(i);

                                String lat = obj.getString("truck_lat");
                                String lng = obj.getString("truck_lng");
                                String status = obj.getString("status");

                                if (lat.equalsIgnoreCase("null") && lng.equalsIgnoreCase("null")) {

                                } else
                                {

                                    if (status.equalsIgnoreCase("02") || status.equalsIgnoreCase("05") || status.equalsIgnoreCase("07") || status.equalsIgnoreCase("45")) {
                                        String ans = null;
                                        String pdate = null, Started_destination_Time, Unloading_start, loadingstart_time = null;
                                        String driver_name = obj.getString("driver_name");
                                        String driver_mno = obj.getString("pickup_by");
                                        String driver_tno = obj.getString("vehicle_reg_no");



                                        if (status.equalsIgnoreCase("05"))
                                        {
                                            pdate = obj.getString("pickup_time").toString();

                                            String dated = pdate.substring(0, pdate.lastIndexOf('T'));
                                            String result[] = pdate.split("T");
                                            String timed = result[result.length - 1];


                                            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat DesiredFormat = new SimpleDateFormat("dd MMM yy");
                                            Date date1 = sourceFormat.parse(dated);
                                            String formattedDate = DesiredFormat.format(date1);
                                            Log.e("date  Heenali", formattedDate);

                                            SimpleDateFormat sourceFormat1 = new SimpleDateFormat("HH:mm:ss");
                                            SimpleDateFormat DesiredFormat1 = new SimpleDateFormat("HH:mm a");
                                            Date date11 = sourceFormat1.parse(timed);
                                            String date_from  =gettimezone(timed);

                                            String show_date = formattedDate + " " + date_from;

                                            // String show_date = formattedDate + " " + gmtFormate.format(date11);
                                            Log.e("show_date-->", show_date);

                                            googleMap.addMarker(new MarkerOptions()
                                                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                                                    .icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track)))
                                                    .title("Name :" + driver_name)
                                                    .snippet("Pickup Time :" + show_date.toString() + "," + "Mobile No : " + driver_mno + "," + "Truck Reg.No : " + driver_tno)

                                                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));


                                        }
                                       else if (status.equalsIgnoreCase("07")) {
                                            Started_destination_Time = obj.getString("start_time").toString();


                                            String dated = Started_destination_Time.substring(0, Started_destination_Time.lastIndexOf('T'));
                                            String result[] = Started_destination_Time.split("T");
                                            String timed = result[result.length - 1];


                                            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat DesiredFormat = new SimpleDateFormat("dd MMM yy");
                                            Date date1 = sourceFormat.parse(dated);
                                            String formattedDate = DesiredFormat.format(date1);
                                            Log.e("call-formattedDate--->", formattedDate);

                                            String date_from  =gettimezone(timed);

                                            String show_date = formattedDate + " " + date_from;

                                            // String show_date = formattedDate + " " + gmtFormate.format(date11);
                                            Log.e("show_date-->", show_date);

                                            googleMap.addMarker(new MarkerOptions()
                                                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                                                    .icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track)))
                                                    .title("Name :" + driver_name)
                                                    .snippet("Ongoing Time :" + show_date.toString() + "," + "Mobile No : " + driver_mno + "," + "Truck Reg.No : " + driver_tno)

                                                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));


                                        }
                                        // final LatLng lat_lng1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));


                                        // marker1 = new MarkerOptions().position(lat_lng1).title(driver_name).icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track))).snippet(ans);

                                        //googleMap.addMarker(marker1);

                                        // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_lng1, 15));
                                    }

                                }


                        }

                                drowmappath_second_hiretruck();

                            /////
                        }
                        else
                        {
                            /////
                            for (int i = 0; i < jsonarray.length(); i++)
                            {
                                JSONObject obj = jsonarray.getJSONObject(i);

                                String lat = obj.getString("truck_lat");
                                String lng = obj.getString("truck_lng");
                                String status = obj.getString("status");

                                if (lat.equalsIgnoreCase("null") && lng.equalsIgnoreCase("null")) {

                                } else
                                {

                                    if (status.equalsIgnoreCase("05") || status.equalsIgnoreCase("06") || status.equalsIgnoreCase("07") || status.equalsIgnoreCase("08")) {
                                        String ans = null;
                                        String pdate = null, Started_destination_Time, Unloading_start, loadingstart_time = null;
                                        String driver_name = obj.getString("driver_name");
                                        String driver_mno = obj.getString("pickup_by");
                                        String driver_tno = obj.getString("vehicle_reg_no");

                                        if (status.equalsIgnoreCase("05")) {
                                            pdate = obj.getString("pickup_time").toString();

                                            String dated = pdate.substring(0, pdate.lastIndexOf('T'));
                                            String result[] = pdate.split("T");
                                            String timed = result[result.length - 1];


                                            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat DesiredFormat = new SimpleDateFormat("dd MMM yy");
                                            Date date1 = sourceFormat.parse(dated);
                                            String formattedDate = DesiredFormat.format(date1);
                                            Log.e("date  Heenali", formattedDate);

                                            SimpleDateFormat sourceFormat1 = new SimpleDateFormat("HH:mm:ss");
                                            SimpleDateFormat DesiredFormat1 = new SimpleDateFormat("HH:mm a");
                                            Date date11 = sourceFormat1.parse(timed);
                                            String date_from  =gettimezone(timed);

                                            String show_date = formattedDate + " " + date_from;

                                            // String show_date = formattedDate + " " + gmtFormate.format(date11);
                                            Log.e("show_date-->", show_date);

                                            googleMap.addMarker(new MarkerOptions()
                                                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                                                    .icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track)))
                                                    .title("Name :" + driver_name)
                                                    .snippet("Pickup Time :" + show_date.toString() + "," + "Mobile No : " + driver_mno + "," + "Truck Reg.No : " + driver_tno)
                                                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));


                                        }
                                        else if (status.equalsIgnoreCase("06"))
                                        {
                                            loadingstart_time = obj.getString("loadingstart_time").toString();

                                            String dated = loadingstart_time.substring(0, loadingstart_time.lastIndexOf('T'));
                                            String result[] = loadingstart_time.split("T");
                                            String timed = result[result.length - 1];


                                            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat DesiredFormat = new SimpleDateFormat("dd MMM yy");
                                            Date date1 = sourceFormat.parse(dated);
                                            String formattedDate = DesiredFormat.format(date1);


                                            String date_from  =gettimezone(timed);

                                            String show_date = formattedDate + " " + date_from;



                                            // String show_date = formattedDate + " " + gmtFormate.format(date11);
                                            Log.e("show_date-->", show_date);

                                            googleMap.addMarker(new MarkerOptions()
                                                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                                                    .icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track)))
                                                    .title("Name :" + driver_name)
                                                    .snippet("Loading started Time :" + show_date.toString() + "," + "Mobile No : " + driver_mno + "," + "Truck Reg.No : " + driver_tno)

                                                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));




                                        } else if (status.equalsIgnoreCase("07")) {
                                            Started_destination_Time = obj.getString("start_time").toString();


                                            String dated = Started_destination_Time.substring(0, Started_destination_Time.lastIndexOf('T'));
                                            String result[] = Started_destination_Time.split("T");
                                            String timed = result[result.length - 1];


                                            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat DesiredFormat = new SimpleDateFormat("dd MMM yy");
                                            Date date1 = sourceFormat.parse(dated);
                                            String formattedDate = DesiredFormat.format(date1);
                                            Log.e("call-formattedDate--->", formattedDate);

                                            String date_from  =gettimezone(timed);

                                            String show_date = formattedDate + " " + date_from;

                                            // String show_date = formattedDate + " " + gmtFormate.format(date11);
                                            Log.e("show_date-->", show_date);

                                            googleMap.addMarker(new MarkerOptions()
                                                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                                                    .icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track)))
                                                    .title("Name :" + driver_name)
                                                    .snippet("Start Time :" + show_date.toString() + "," + "Mobile No : " + driver_mno + "," + "Truck Reg.No : " + driver_tno)
                                                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));


                                        } else if (status.equalsIgnoreCase("08")) {
                                            Unloading_start = obj.getString("unloadingstart_time").toString();

                                            String dated = Unloading_start.substring(0, Unloading_start.lastIndexOf('T'));
                                            String result[] = Unloading_start.split("T");
                                            String timed = result[result.length - 1];


                                            SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd");
                                            SimpleDateFormat DesiredFormat = new SimpleDateFormat("dd MMM yy");
                                            Date date1 = sourceFormat.parse(dated);
                                            String formattedDate = DesiredFormat.format(date1);


                                            String date_from  =gettimezone(timed);

                                            String show_date = formattedDate + " " + date_from;

                                            // String show_date = formattedDate + " " + gmtFormate.format(date11);
                                            Log.e("show_date-->", show_date);
                                            googleMap.addMarker(new MarkerOptions()
                                                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                                                    .icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track)))
                                                    .title("Name :" + driver_name)
                                                    .snippet("Unloadingstart Time :" + show_date + "," + "Mobile No : " + driver_mno + "," + "Truck Reg.No : " + driver_tno)

                                                    .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));


                                        }


                                        // final LatLng lat_lng1 = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));


                                        // marker1 = new MarkerOptions().position(lat_lng1).title(driver_name).icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track))).snippet(ans);

                                        //googleMap.addMarker(marker1);

                                        // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_lng1, 15));
                                    }

                                }


                            }


                                drowmappath_second();

                            /////
                        }

                    } catch (Exception e) {

                    }


                }
            };

        });
        // Start Thread
        background.start();
    }

    public String fetchResult(String urlString) throws JSONException
    {
        StringBuilder builder;
        BufferedReader reader;
        URLConnection connection=null;
        URL url=null;
        String line;
        builder=new StringBuilder();
        reader=null;
        try {
            url=new URL(urlString);
            connection=url.openConnection();

            reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
            while((line=reader.readLine())!=null)
            {
                builder.append(line);
            }
            //Log.d("DATA", builder.toString());
        } catch (Exception e)
        {

        }
        //JSONArray arr=new JSONArray(builder.toString());
        return builder.toString();
    }
    public void drowmappath()
    {

        googleMap.clear();

        final LatLng lat_lng1= new LatLng(Double.parseDouble(source_lat_main),Double.parseDouble(source_long_main));
        final LatLng lat_lng2= new LatLng(Double.parseDouble(dest_lat_main),Double.parseDouble(dest_long_main));

        MarkerOptions marker1=null,marker2=null;

        marker1 = new MarkerOptions().position(lat_lng1).title("Source Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)).snippet(source_addr);
        //  marker2 = new MarkerOptions().position(lat_lng2).title("Destination Location").icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).snippet(destination_addr);
        marker2 = new MarkerOptions().position(lat_lng2).title("Destination Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.red_marker_map)).snippet(destination_addr);
        googleMap.addMarker(new MarkerOptions()
                // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                .icon((BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)))
                .title("Source Location")
                .snippet(source_addr + "," + "s" + "," + "s")
                .position(new LatLng(Double.parseDouble(source_lat_main), Double.parseDouble(source_long_main))));


        googleMap.addMarker(new MarkerOptions()
                // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                .icon((BitmapDescriptorFactory.fromResource(R.mipmap.red_marker_map)))
                .title("Destination Location")
                .snippet(destination_addr+","+"s"+","+"s")
                .position(new LatLng(Double.parseDouble(dest_lat_main), Double.parseDouble(dest_long_main))));
        // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_lng2, 13));
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(lat_lng1);
        boundsBuilder.include(lat_lng2);
        bounds = boundsBuilder.build();

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded()
            {
                    try
                    {
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
                    }
                    catch (Exception e)
                    {

                    }

            }
        });
      /*  if (cd.isConnectingToInternet())
        {
            String url = getDirectionsUrl(lat_lng1, lat_lng2);
            DownloadTask downloadTask = new DownloadTask();
            downloadTask.execute(url);


        }
        else {
            UF.msg("Please check your internet connection.");
        }*/


    }
    public void drowmappath_hiretruck()
    {

        googleMap.clear();

        final LatLng lat_lng1 = new LatLng(Double.parseDouble(source_lat_main), Double.parseDouble(source_long_main));


        MarkerOptions marker1 = null;

        marker1 = new MarkerOptions().position(lat_lng1).title("Source Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)).snippet(source_addr);

        googleMap.addMarker(new MarkerOptions()
                // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                .icon((BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)))
                .title("Source Location")
                .snippet(source_addr + "," + "s" + "," + "s")
                .position(new LatLng(Double.parseDouble(source_lat_main), Double.parseDouble(source_long_main))));

        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_lng1,10));

    }
    private String getDirectionsUrl(LatLng origin,LatLng dest){

        String str_origin = "origin="+origin.latitude+ "," + origin.longitude;
        String str_dest = "destination="+dest.latitude+","+dest.longitude;
        String sensor = "sensor=false";
        String key = "key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+key;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/json?"+ parameters;
        Log.e("direction url", url);
        return url;
    }
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
			/*progressDialog = new ProgressDialog(NavigationActivity.this);
			progressDialog.setMessage("Please wait...");
			progressDialog.setIndeterminate(true);
			progressDialog.show();*/
        }
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try{
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            }catch(Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try{
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb  = new StringBuffer();

            String line = "";
            while( ( line = br.readLine())  != null){
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        }catch(Exception e){
            Log.d("Exception url", "" + e.toString());
        }finally{
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String,String>>> >{

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try{
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            }catch(Exception e){
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            if(result!=null)
            {
                if(result.size() > 0)
                {

                    for(int i=0;i<result.size();i++){
                        points = new ArrayList<LatLng>();
                        lineOptions = new PolylineOptions();

                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);

                        // Fetching all the points in i-th route
                        for(int j=0;j<path.size();j++)
                        {
                            HashMap<String,String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        if(!islineFlag)
                        {
                            lineOptions.width(15);
                            lineOptions.color(Color.parseColor("#FF0000")).geodesic(true);
                            islineFlag=true;
                        }
                        else
                        {
                            lineOptions.width(15);
                            lineOptions.color(Color.parseColor("#FF0000")).geodesic(true);
                        }
                        //lineOptions.color(Color.parseColor("#59c1e3")).geodesic(true);


                    }

                    line = googleMap.addPolyline(lineOptions);	// Drawing polyline in the Google Map for the i-th route


                }
            }


        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    Runnable mHandlerTask = new Runnable()
    {
        @Override
        public void run()
        {
            Log.e("screen taskkkk...", "updationg");
            String url_putlatlong = UserFunctions.URL + "truck/GetTruckCurrentLocation?loadinq=" + id;
            SyncMethod_putlatlon(url_putlatlong);
            mHandler.postDelayed(mHandlerTask, INTERVAL);
        }
    };

    void startRepeatingTask()
    {
        mHandlerTask.run();
    }

    void stopRepeatingTask()
    {
        mHandler.removeCallbacks(mHandlerTask);


    }
    @Override
    public void onResume()
    {
        super.onResume();
        if(Constants.cancel_conformation=="Yes")
        {
            cancleorder_submit_orange.setVisibility(View.GONE);
             cancleorder_submit.setVisibility(View.VISIBLE);

        }

    }

    protected void sendSMS() {
        Log.i("Send SMS", "");
        Intent smsIntent = new Intent(Intent.ACTION_VIEW);

        smsIntent.setData(Uri.parse("smsto:"));
        //  smsIntent.setType("vnd.android-dir/mms-sms");
        smsIntent.putExtra("address", new String(driver_mno));
        smsIntent.putExtra("sms_body", gmail_body);

        try {
            startActivity(smsIntent);
            finish();
            Log.i("Finished sending SMS...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Activity_MoveMyGoods_details.this,"SMS faild, please try again later.", Toast.LENGTH_SHORT).show();
        }
    }

    public void sendemail() {


        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{""});


        i.putExtra(Intent.EXTRA_SUBJECT, gmail_sub);
        i.putExtra(Intent.EXTRA_TEXT, gmail_body);
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Activity_MoveMyGoods_details.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void drowmappath_second()
    {
        try
        {
            final LatLng lat_lng1= new LatLng(Double.parseDouble(source_lat_main),Double.parseDouble(source_long_main));
            final LatLng lat_lng2= new LatLng(Double.parseDouble(dest_lat_main),Double.parseDouble(dest_long_main));

            //  marker11 = new MarkerOptions().position(lat_lng1).title("Source Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)).snippet(source_addr);
            googleMap.addMarker(new MarkerOptions()
                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                    .icon((BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)))
                    .title("Source Location")
                    .snippet(source_addr + "," + "s" + "," + "s")
                    .position(new LatLng(Double.parseDouble(source_lat_main), Double.parseDouble(source_long_main))));


            googleMap.addMarker(new MarkerOptions()
                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                    .icon((BitmapDescriptorFactory.fromResource(R.mipmap.red_marker_map)))
                    .title("Destination Location")
                    .snippet(destination_addr + "," + "s" + "," + "s")
                    .position(new LatLng(Double.parseDouble(dest_lat_main), Double.parseDouble(dest_long_main))));
        }
        catch (Exception e)
        {

        }



    }
    public void drowmappath_second_hiretruck()
    {
        try
        {
            final LatLng lat_lng1= new LatLng(Double.parseDouble(source_lat_main),Double.parseDouble(source_long_main));

            //  marker11 = new MarkerOptions().position(lat_lng1).title("Source Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)).snippet(source_addr);
            googleMap.addMarker(new MarkerOptions()
                    // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                    .icon((BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)))
                    .title("Source Location")
                    .snippet(source_addr + "," + "s" + "," + "s")
                    .position(new LatLng(Double.parseDouble(source_lat_main), Double.parseDouble(source_long_main))));


        }
        catch (Exception e)
        {

        }



    }
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
    }
    class CustomDialogClass_reshedule extends Dialog
    {

        public Context context;
        public Dialog d;
        ProgressDialog loading;
        TextView submit_btn;
        EditText date_edittext;
        EditText time_edittext;
        TextView close_btn;
        Calendar cal;
        DatePickerDialog.OnDateSetListener datePicker;

        public CustomDialogClass_reshedule(Context a)
        {
            super(a);
            this.context = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_reshedule);
            getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            context = this.getContext();

            time_edittext = (EditText) findViewById(R.id.time_edittext);
            date_edittext = (EditText) findViewById(R.id.date_edittext);
            submit_btn = (TextView) findViewById(R.id.submit_btn);
           close_btn= (TextView) findViewById(R.id.close_btn);

            try
            {
                String date=load_inquiry_shipping_date;
                SimpleDateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
                SimpleDateFormat DesiredFormat  = new SimpleDateFormat("dd/MM/yyyy");

                Date date1 = sourceFormat.parse(date);
                String formattedDate = DesiredFormat.format(date1);

                date_edittext.setText(formattedDate);

                String time=load_inquiry_shipping_time;
                SimpleDateFormat sourceFormattime = new SimpleDateFormat("HH:mm:ss");
                SimpleDateFormat DesiredFormattime  = new SimpleDateFormat("HH:mm");

                Date time1 = sourceFormattime.parse(time);
                String formattedtime = DesiredFormattime.format(time1);
                time_edittext.setText(formattedtime);
            }
            catch (Exception e) {
            }
            createDatePicker();

            close_btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    dismiss();
                }
            });
            submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messdata = time_edittext.getText().toString();
                    if (messdata.equalsIgnoreCase("")) {
                        time_edittext.setError("Enter valid reason");
                    } else {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Activity_MoveMyGoods_details.this);
                        builder.setMessage("Are you sure Cancel Order?");
                        // builder.setTitle("New Order");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(submit_btn.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);

                        new GetJson_save_cancleorder().execute();
                    }


                }
            });
            date_edittext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(date_edittext.getWindowToken(), 0);

                    DatePickerDialog datePicker1 = new DatePickerDialog(Activity_MoveMyGoods_details.this, R.style.AppTheme_Dark_Dialog, datePicker, cal
                            .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));

                    datePicker1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePicker1.show();
                    date_edittext.setError(null);
                }
            });
            time_edittext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    int minute = mcurrentTime.get(Calendar.MINUTE);
                    TimePickerDialog mTimePicker;
                    mTimePicker = new TimePickerDialog(Activity_MoveMyGoods_details.this, R.style.AppTheme_Dark_Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            if(page.equalsIgnoreCase("HireTruck"))
                            {
                                time_edittext.setText(pad(selectedHour) + ":" + pad(00));
                            }else
                            {
                                time_edittext.setText(pad(selectedHour) + ":" + pad(selectedMinute));
                            }

                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();

                }
            });

        }
        public void createDatePicker() {
            cal = Calendar.getInstance();
            datePicker = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {

                    // TODO Auto-generated method stub
                    cal.set(Calendar.YEAR, year);
                    cal.set(Calendar.MONTH, monthOfYear);
                    cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                    String selected_year = String.valueOf(cal.get(Calendar.YEAR) + "");
                    String month = String.valueOf(cal.get(Calendar.MONTH) + 1);

                    String day = String.valueOf(cal
                            .get(Calendar.DAY_OF_MONTH));

                    if (month.length() == 1) {
                        month = "0" + month;
                    } else {
                        //tv_dig_MM.setText(month);
                    }

                    if (day.length() == 1) {
                        //tv_dig_DD.setText("0" + day);
                        day = "0" + day;
                    } else {
                        //tv_dig_DD.setText(day);
                    }
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    String s = sdf.format(cal.getTime());
                    date_edittext.setText(s);
                }

            };
        }

        private class GetJson_save_cancleorder extends AsyncTask<Void, Void, String> {
            //ProgressHUD mProgressHUD;
            private ProgressDialog  loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(Activity_MoveMyGoods_details.this);
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

                    prmsLogin.put("load_inquiry_no",id);
                    prmsLogin.put("shipper_id",sm.getUniqueId().toString());
                    prmsLogin.put("load_inquiry_shipping_date",date_edittext.getText().toString());
                    prmsLogin.put("load_inquiry_shipping_time",time_edittext.getText().toString());
                    prmsLogin.put("created_host","");
                    prmsLogin.put("created_by","");
                    prmsLogin.put("device_id",android_id.toString());
                    prmsLogin.put("device_type","Android");


                    jsonArray.put(prmsLogin);
                    prms.put("order", jsonArray);


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Log.e("feedback json >>>>>",  prms + "");
                json_save = UF.RegisterUser("shipper/SendOrderRescheduleRequestByUser",  prms);

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
                    if (json_save.equalsIgnoreCase("0"))
                    {
                        UF.msg("Invalid");
                    } else {
                        try {
                            JSONObject jobj = new JSONObject(json_save);

                            Log.e("cancel  data get ", jobj.toString());
                            String status = jobj.getString("status");
                            String message = jobj.getString("message").toString();
                            if (status.equalsIgnoreCase("1"))
                            {
                                UF.msg(message + "");


                                dismiss();
                                finish();

                            }
                            else
                            {
                                UF.msg(message + "");
                                dismiss();
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
    private static String pad(int c)
    {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }
    public  String gettimezone(String datetime) throws ParseException
    {
        String newFormat=datetime;
        System.out.println(".....Date..."+newFormat);

        newFormat=newFormat.substring(0, 5);
        System.out.println("....time..." + newFormat);
        String hr = newFormat.substring(0, Math.min(newFormat.length(), 2));
        String min= newFormat.substring(newFormat.length() - 2);
        Log.e("HR", hr);
        Log.e("min", min);

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
        String g=localTime.toString();
        return  g;
    }
}