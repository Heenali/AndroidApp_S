package com.trukker.trukkershipperuae.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.LatLngBounds.Builder;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;
import com.trukker.trukkershipperuae.Bubble_layout.ArrowDirection;
import com.trukker.trukkershipperuae.Bubble_layout.BubbleLayout;
import com.trukker.trukkershipperuae.Bubble_layout.BubblePopupHelper;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.adapter.DocumentAdapter;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.DirectionsJSONParser;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.model.Document_method;

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
public class Activity_MoveMyHome_Detail extends AppCompatActivity
{

    ListView listview_hometype_ho;
    ArrayList<Document_method> actorsList_ho;
    DocumentAdapter adapter_ho;
    BubbleLayout bubbleLayout_hometype;



    private ViewGroup infoWindow;
    TextView title1, value1, title,value2;
    public String json_save;
    int length=0;
    String time_store="";
    LinearLayout layout1,layout2;
    LinearLayout container,nodatadisplay_layout,container1;
    TextView feedback_btn,cancle_btn;
    ImageView arraowmove_btn;
    TextView  hometype_txt,id_txt,destinationadd_txt,sourceadd_txt;
    String android_id;
    UserFunctions UF;
    TextView aeddisplay;
    MarkerOptions marker11=null,marker2=null;
    SessionManager sm;
    ImageView completed_icon;
    ConnectionDetector cd;
    LatLngBounds bounds;
    private GoogleMap googleMap;
    public Polyline line;
    Runnable runnable;
    boolean islineFlag=false;
    String messdata;
    String status_feddbackinfo="";
    Thread t;
    TextView reshedule_btn;
    String status="0";
    private Handler handler;
    String source_addr,destination_addr,sizeTypeDesc;
    String id;
    String source_lat_main,source_long_main,dest_lat_main,dest_long_main;
    RelativeLayout showdatils;
    String feedback_rating;
    String feedback_msgs;
    MarkerOptions marker1 = null;
    String Hometypeid_store;

    TextView hometype_txt_nodatadisplay,destinationadd_txt1_nodatadisplay,sourceadd_txt_nodatadisplay,id_txt_nodatadisplay;
    ImageView completed_icon_nodatadisplay;
    TextView back;
    private final static int INTERVAL = 5000; //2 minutes
    Handler mHandler = new Handler();
    private ProgressDialog loading;
    ProgressDialog loading1;
    String load_inquiry_shipping_date,load_inquiry_shipping_time;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movemyhome_details);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();
        Constants.back_home=1;
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                stopRepeatingTask();
                finish();
            }
        });

        handler = new Handler();


        String url= UserFunctions.URL+"driver/GetDriverTruckDetails?loadinqno="+id;
        loading = new ProgressDialog(Activity_MoveMyHome_Detail.this);
        loading.getWindow().setBackgroundDrawable(new

                ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading.setIndeterminate(true);
        loading.setCancelable(false);
        loading.show();
        loading.setContentView(R.layout.my_progress);
        SyncMethod(url);
        startRepeatingTask();
        /////////////////////////////////////////
       /* t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run()
                            {
                                Log.e("Updating screen...","updationg");
                                String url_putlatlong= UserFunctions.URL+"truck/GetTruckCurrentLocation?loadinq="+id;
                                SyncMethod_putlatlon(url_putlatlong);
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();*/


        arraowmove_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(length==1)
                {//single record display
                    if (nodatadisplay_layout.getVisibility() == View.VISIBLE)
                    {
                        nodatadisplay_layout.setVisibility(View.GONE);
                        arraowmove_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.order_downarraow));
                        Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                        nodatadisplay_layout.startAnimation(anm);
                    }
                    else
                    {
                        nodatadisplay_layout.setVisibility(View.VISIBLE);
                        container1.setVisibility(View.VISIBLE);
                        arraowmove_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.uparrow));
                        Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                        nodatadisplay_layout.startAnimation(anm);
                    }
                }
                else
                {
                    if(status.equalsIgnoreCase("0"))
                    {
                        if (nodatadisplay_layout.getVisibility() == View.VISIBLE)
                        {
                            nodatadisplay_layout.setVisibility(View.GONE);
                            arraowmove_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.order_downarraow));
                            Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                            nodatadisplay_layout.startAnimation(anm);
                        }
                        else
                        {
                            nodatadisplay_layout.setVisibility(View.VISIBLE);
                            arraowmove_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.uparrow));
                            Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                            nodatadisplay_layout.startAnimation(anm);
                        }
                    }else
                    {
                        if (layout1.getVisibility() == View.VISIBLE)
                        {
                            layout1.setVisibility(View.GONE);
                            arraowmove_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.order_downarraow));
                            Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                            layout1.startAnimation(anm);

                        }
                        else
                        {
                            layout1.setVisibility(View.VISIBLE);
                            arraowmove_btn.setBackgroundDrawable(getResources().getDrawable(R.drawable.uparrow));
                            Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                            layout1.startAnimation(anm);
                        }
                    }

                }


            }
        });
        cancle_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(Activity_MoveMyHome_Detail.this);
                builder.setMessage("Are you sure want to cancel Order?");
                // builder.setTitle("New Order");

                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        CustomDialogClass_time timeclass = new CustomDialogClass_time(Activity_MoveMyHome_Detail.this);
                        timeclass.show();

                    }
                });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.create().show();


            }
        });
        reshedule_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {


                CustomDialogClass_reshedule timeclass = new  CustomDialogClass_reshedule(Activity_MoveMyHome_Detail.this);
                timeclass.show();



            }
        });
        feedback_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(Activity_MoveMyHome_Detail.this,Activity_Feedback.class);
                i.putExtra("id",id);
                i.putExtra("feedback_rating", feedback_rating);
                i.putExtra("feedback_msg", feedback_msgs);
                i.putExtra("source_lat_main",source_lat_main);
                i.putExtra("source_long_main",source_long_main);
                i.putExtra("dest_lat_main",dest_lat_main);
                i.putExtra("dest_long_main",dest_long_main);
                startActivity(i);
                overridePendingTransition(R.anim.right_in, R.anim.left_out);

            }
        });
      /*  googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker arg0) {

                View v = getLayoutInflater().inflate(R.layout.dialog_mappoint, null);



                return v;

            }
        });*/

    }


    public void init()
    {

        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sm=new SessionManager(Activity_MoveMyHome_Detail.this);
        UF=new UserFunctions(Activity_MoveMyHome_Detail.this);
        cd= new ConnectionDetector(Activity_MoveMyHome_Detail.this);

        ////
        final MapFragment mapFragment = (MapFragment)getFragmentManager().findFragmentById(R.id.map);
        final MapWrapperLayout mapWrapperLayout = (MapWrapperLayout)findViewById(R.id.map_relative_layout);
        googleMap = mapFragment.getMap();


        mapWrapperLayout.init(googleMap , getPixelsFromDp(this, 39 + 20));


        this.infoWindow = (ViewGroup)getLayoutInflater().inflate(R.layout.info_window_layout, null);
        this.title = (TextView)infoWindow.findViewById(R.id.title);
        this.title1 = (TextView)infoWindow.findViewById(R.id.title1);
        this.value1 = (TextView)infoWindow.findViewById(R.id.value1);
        this.value2 = (TextView)infoWindow.findViewById(R.id.value2);

        googleMap .setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
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

        layout1=(LinearLayout)findViewById(R.id.layout1);
        layout2=(LinearLayout)findViewById(R.id.layout2);
        container = (LinearLayout)findViewById(R.id.container);
        container1 = (LinearLayout)findViewById(R.id.container_singleecord);
        cancle_btn= (TextView)findViewById(R.id.cancle_btn);
        nodatadisplay_layout=(LinearLayout)findViewById(R.id.nodatadisplay_layout);
        nodatadisplay_layout.setVisibility(View.GONE);
        feedback_btn= (TextView)findViewById(R.id.feedback);
        reshedule_btn= (TextView)findViewById(R.id.reshedule_btn);
        back= (TextView)findViewById(R.id.back);
        arraowmove_btn=(ImageView)findViewById(R.id.arraowmove_btn);
        hometype_txt= (TextView)findViewById(R.id.txt_hometype);
        id_txt= (TextView)findViewById(R.id.id_txt);
        destinationadd_txt= (TextView)findViewById(R.id.destinationadd_txt);
        sourceadd_txt= (TextView)findViewById(R.id.sourceadd_txt);
        completed_icon= (ImageView)findViewById(R.id.success_icon);
        aeddisplay= (TextView)findViewById(R.id.aeddisplay);

        hometype_txt_nodatadisplay= (TextView)findViewById(R.id.txt_hometype1);
        destinationadd_txt1_nodatadisplay= (TextView)findViewById(R.id.destinationadd_txt1);
        sourceadd_txt_nodatadisplay= (TextView)findViewById(R.id.sourceadd_txt1);
        completed_icon_nodatadisplay= (ImageView)findViewById(R.id.success_icon1);
        id_txt_nodatadisplay= (TextView)findViewById(R.id.id_txt1);

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
        String aed=i.getStringExtra("aed");
        aeddisplay.setText("Remaining payment " + aed +" AED");
        load_inquiry_shipping_date=i.getStringExtra("load_inquiry_shipping_date");
        load_inquiry_shipping_time=i.getStringExtra("load_inquiry_shipping_time");
        Hometypeid_store=i.getStringExtra("SizeTypeCode");


        if(status.equalsIgnoreCase("02"))
        {
            cancle_btn.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.VISIBLE);
            aeddisplay.setVisibility(View.VISIBLE);
            feedback_btn.setVisibility(View.GONE);
        }
        else if(status.equalsIgnoreCase("05"))
        {
            cancle_btn.setVisibility(View.GONE);
            reshedule_btn.setVisibility(View.GONE);
            aeddisplay.setVisibility(View.VISIBLE);
            feedback_btn.setVisibility(View.VISIBLE);
            feedback_btn.setEnabled(false);

        }
        else if(status.equalsIgnoreCase("06"))
        {
            cancle_btn.setVisibility(View.GONE);
            reshedule_btn.setVisibility(View.GONE);
            aeddisplay.setVisibility(View.VISIBLE);
            feedback_btn.setVisibility(View.VISIBLE);
            feedback_btn.setEnabled(false);

        }
        else if(status.equalsIgnoreCase("07"))
        {
            cancle_btn.setVisibility(View.GONE);
            reshedule_btn.setVisibility(View.GONE);
            aeddisplay.setVisibility(View.VISIBLE);
            feedback_btn.setVisibility(View.VISIBLE);
            feedback_btn.setEnabled(false);

        }
        else if(status.equalsIgnoreCase("08"))
        {
            cancle_btn.setVisibility(View.GONE);
            reshedule_btn.setVisibility(View.GONE);
            aeddisplay.setVisibility(View.VISIBLE);
            feedback_btn.setVisibility(View.VISIBLE);
            feedback_btn.setEnabled(false);

        }
        else if(status.equalsIgnoreCase("25"))
        {
            cancle_btn.setVisibility(View.GONE);
            reshedule_btn.setVisibility(View.GONE);
            aeddisplay.setVisibility(View.VISIBLE);
            feedback_btn.setVisibility(View.VISIBLE);
            feedback_btn.setEnabled(false);


        }
        else if(status.equalsIgnoreCase("45"))
        {
            status_feddbackinfo=status;
            cancle_btn.setVisibility(View.GONE);
            reshedule_btn.setVisibility(View.GONE);
            aeddisplay.setVisibility(View.VISIBLE);
            feedback_btn.setVisibility(View.VISIBLE);
            feedback_btn.setEnabled(true);
            // if(feedback_rating.toString().equalsIgnoreCase(""))
            //{

            feedback_btn.setBackgroundResource(R.drawable.round_mainbutton);

            // }
            //  else
            // {
            //     feedback_btn.setBackgroundResource(R.drawable.round_mainbutton_black);
            // }

            completed_icon.setBackgroundResource(R.drawable.success);
        }
        else if(status.equalsIgnoreCase("26"))
        {
            status_feddbackinfo=status;
            cancle_btn.setVisibility(View.VISIBLE);
            reshedule_btn.setVisibility(View.GONE);
            aeddisplay.setVisibility(View.VISIBLE);
            feedback_btn.setVisibility(View.GONE);
            feedback_btn.setEnabled(false);



        }
        ////////////////

        sourceadd_txt.setText(source_addr);
        id_txt.setText(id);
        destinationadd_txt.setText(destination_addr);
        hometype_txt.setText("Home "+sizeTypeDesc);
        layout1.setVisibility(View.GONE);

        sourceadd_txt_nodatadisplay.setText(source_addr);
        id_txt_nodatadisplay.setText(id);
        destinationadd_txt1_nodatadisplay.setText(destination_addr);
        hometype_txt_nodatadisplay.setText("Home " + sizeTypeDesc);

        drowmappath();


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
                                        Log.e("date ", formattedDate);

                                        Log.e("Time ", timed.toString());
                                        String date_from  =gettimezone(timed);
                                        ans = formattedDate+" "+date_from;
                                        Log.e("ptimek", ans);


                                        googleMap.addMarker(new MarkerOptions()
                                                // .icon(BitmapDescriptorFactory.fromBitmap(array.getJSONObject(aa).getString("shopphoto").toString()))
                                                .icon((BitmapDescriptorFactory.fromResource(R.drawable.truck_track)))
                                                .title("Name :" + driver_name)
                                                .snippet("Pickup Time :" + ans + "," + "Mobile No : " + driver_mno + "," + "Truck Reg.No : " + driver_tno)

                                                .position(new LatLng(Double.parseDouble(lat), Double.parseDouble(lng))));


                                    }
                                    else if (status.equalsIgnoreCase("06")) {
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

                            drowmappath_second();
                        }

                    } catch (Exception e) {

                    }


                }
            };

        });
        // Start Thread
        background.start();
    }

    public void SyncMethod(final String GetUrl)
    {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable()
        {
            // After call for background.start this run method call
            public void run()
            {
                try
                {
                    String url=GetUrl;
                    String SetServerString = "";
                    // document all_stuff = null;

                    SetServerString=fetchResult(url);
                    threadMsg(SetServerString);
                }
                catch (Throwable t)
                {
                    Log.e("Animation", "Thread  exception " + t);
                }
            }
            private void threadMsg(String msg)
            {

                if (!msg.equals(null) && !msg.equals(""))
                {
                    Message msgObj = handler11.obtainMessage();
                    Bundle b = new Bundle();
                    b.putString("message", msg);
                    msgObj.setData(b);
                    handler11.sendMessage(msgObj);
                }
            }
            // Define the Handler that receives messages from the thread and update the progress
            private final Handler handler11 = new Handler()
            {
                public void handleMessage(Message msg)
                {
                    try
                    {
                        loading.dismiss();
                        String aResponse = msg.getData().getString("message");
                        Log.e("Exam ","screen>>"+aResponse);
                        aResponse = aResponse.trim();
                        aResponse = aResponse.substring(1, aResponse.length() - 1);
                        aResponse = aResponse.replace("\\", "");
                        Log.e("movemyhomedetaild data ","screen>>"+aResponse);
                        JSONObject get_res=new JSONObject(aResponse);

                        status = get_res.getString("status");
                        if(status.equalsIgnoreCase("0"))
                        {
                            //arraowmove_btn.setEnabled(false);
                            //arraowmove_btn.setEnabled(false);

                        }
                        if(status.equalsIgnoreCase("1"))
                        {

                            // arraowmove_btn.setEnabled(true);


                            JSONArray array = new JSONArray();

                            array = get_res.getJSONArray("message");
                            Log.e("mess", "screen>>" + array.length());
                            Log.e("mess", "screen>>" + array.toString());

                            for (int aa = 0; aa < array.length(); aa++)
                            {
                                String Name = array.getJSONObject(aa).getString("Name").toString();
                                String driver_photo = array.getJSONObject(aa).getString("driver_photo").toString();
                                final String mobile_no = array.getJSONObject(aa).getString("mobile_no").toString();
                                String License_no1 = array.getJSONObject(aa).getString("vehicle_reg_no").toString();
                                String baseCharge = array.getJSONObject(aa).getString("baseCharge").toString();
                                String NoOfHandiman = array.getJSONObject(aa).getString("NoOfHandiman").toString();
                                String NoOfLabour = array.getJSONObject(aa).getString("NoOfLabour").toString();
                                String status_info = array.getJSONObject(aa).getString("status").toString();

                                LayoutInflater layoutInflater =
                                        (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                                final View addView = layoutInflater.inflate(R.layout.list_movemyhomedetail, null);

                                TextView Name_txt = (TextView) addView.findViewById(R.id.title_text);
                                TextView mobile_no_txt = (TextView) addView.findViewById(R.id.description_txt);
                                TextView License_no1_txt = (TextView) addView.findViewById(R.id.counter_name);
                                TextView counter_txt = (TextView) addView.findViewById(R.id.counter_txt);
                                TextView status_txt = (TextView) addView.findViewById(R.id.status_text);
                                TextView NoOfHandiman_txt = (TextView) addView.findViewById(R.id.helper_text);
                                TextView NoOfLabour_txt = (TextView) addView.findViewById(R.id.installer_text);
                                ImageView driver_image = (ImageView) addView.findViewById(R.id.driver_pic);
                                ImageView counter_image = (ImageView) addView.findViewById(R.id.counter_icon);
                                TextView line_color = (TextView) addView.findViewById(R.id.line);
                                ImageView call_icon = (ImageView) addView.findViewById(R.id.call_icon);

                                call_icon.setOnClickListener(new View.OnClickListener()
                                {

                                    @Override
                                    public void onClick(View v)
                                    {

                                        try {

                                            Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile_no.toString()));
                                            startActivity(callIntent);
                                        } catch (android.content.ActivityNotFoundException ex) {
                                            Toast.makeText(getApplicationContext(), "yourActivity is not founded", Toast.LENGTH_SHORT).show();

                                        }

                                    }
                                });


                                Name_txt.setText(Name.toString());
                                mobile_no_txt.setText(mobile_no.toString());
                                License_no1_txt.setText(License_no1.toString());
                                NoOfHandiman_txt.setText(NoOfLabour.toString());
                                NoOfLabour_txt.setText(NoOfHandiman.toString());
                                counter_txt.setText(String.valueOf(aa + 1));

                                if(driver_photo.equalsIgnoreCase(""))
                                {
                                    driver_image.setBackgroundResource(R.drawable.ic_profile);
                                }
                                else
                                {
                                    //Picasso.with(getApplicationContext()).load(UserFunctions.URLIMG + driver_photo).into(driver_image);
                                    UrlImageViewHelper.setUrlDrawable(driver_image, UserFunctions.URLIMG + driver_photo.toString(), R.drawable.ic_profile);

                                }

                                if((aa + 1)%2==0)
                                {
                                    counter_image.setBackgroundResource(R.drawable.orange_flag);
                                    line_color.setBackgroundColor(Color.parseColor("#FFCFBF"));
                                }
                                else
                                {
                                    counter_image.setBackgroundResource(R.drawable.blue_flag);
                                    line_color.setBackgroundColor(Color.parseColor("#99CCFF"));
                                }



                                if (status_info.equalsIgnoreCase("02"))
                                {
                                    status_txt.setText("Upcoming");
                                    status_txt.setBackgroundColor(Color.rgb(7, 52, 120));


                                } else if (status_info.equalsIgnoreCase("05"))
                                {
                                    status_txt.setBackgroundColor(Color.rgb(235,0,56));
                                    status_txt.setText("Start For Pick Up");
                                    cancle_btn.setVisibility(View.GONE);
                                    reshedule_btn.setVisibility(View.GONE);
                                    aeddisplay.setVisibility(View.VISIBLE);
                                    feedback_btn.setVisibility(View.VISIBLE);
                                    feedback_btn.setEnabled(false);
                                }
                                else if (status_info.equalsIgnoreCase("06"))
                                {
                                    status_txt.setBackgroundColor(Color.rgb(148,47,4));
                                    status_txt.setText("Loading Started");
                                    cancle_btn.setVisibility(View.GONE);
                                    reshedule_btn.setVisibility(View.GONE);
                                    aeddisplay.setVisibility(View.VISIBLE);
                                    feedback_btn.setVisibility(View.VISIBLE);
                                    feedback_btn.setEnabled(false);
                                } else if (status_info.equalsIgnoreCase("07"))

                                {
                                    status_txt.setBackgroundColor(Color.rgb(101,26,127));
                                    status_txt.setText("Start For Destination");
                                    cancle_btn.setVisibility(View.GONE);
                                    reshedule_btn.setVisibility(View.GONE);
                                    aeddisplay.setVisibility(View.VISIBLE);
                                    feedback_btn.setVisibility(View.VISIBLE);
                                    feedback_btn.setEnabled(false);
                                }
                                else if (status_info.equalsIgnoreCase("08"))
                                {
                                    status_txt.setBackgroundColor(Color.rgb(6,98,101));
                                    status_txt.setText("Unloading Started");
                                    cancle_btn.setVisibility(View.GONE);
                                    reshedule_btn.setVisibility(View.GONE);
                                    aeddisplay.setVisibility(View.VISIBLE);
                                    feedback_btn.setVisibility(View.VISIBLE);
                                    feedback_btn.setEnabled(false);
                                }
                                else if (status_info.equalsIgnoreCase("45"))
                                {
                                    status_txt.setBackgroundColor(Color.rgb(244, 67, 54));
                                    status_txt.setText("Completed");
                                    cancle_btn.setVisibility(View.GONE);
                                    reshedule_btn.setVisibility(View.GONE);
                                    feedback_btn.setVisibility(View.VISIBLE);


                                    if(status_feddbackinfo.equalsIgnoreCase("45"))
                                    {
                                        feedback_btn.setEnabled(true);
                                    }
                                    else
                                    {
                                        feedback_btn.setEnabled(false);
                                    }

                                }
                                if(array.length()==1)
                                {
                                    container1.addView(addView);
                                    layout1.setVisibility(View.GONE);
                                    container1.setVisibility(View.GONE);
                                    length=1;
                                }
                                else
                                {
                                    container.addView(addView);
                                }



                            }
                        }

                    }
                    catch(Exception e)
                    {

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
        } catch (Exception e) {

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
        Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(lat_lng1);
        boundsBuilder.include(lat_lng2);
        bounds = boundsBuilder.build();

        googleMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 30));
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
    class CustomDialogClass_reshedule extends Dialog
    {

        public Context context;
        public Dialog d;
        ProgressDialog loading;
        TextView submit_btn;
        EditText date_edittext;
        EditText time_edittext;
        Calendar cal;
        TextView close_btn;
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
            close_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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

                    DatePickerDialog datePicker1 = new DatePickerDialog(Activity_MoveMyHome_Detail.this, R.style.AppTheme_Dark_Dialog, datePicker, cal
                            .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                            cal.get(Calendar.DAY_OF_MONTH));

                    datePicker1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePicker1.show();
                    date_edittext.setError(null);
                }
            });
            time_edittext.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    if (cd.isConnectingToInternet())
                    {
                        bubbleLayout_hometype = (BubbleLayout) LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_hometype, null);
                        final PopupWindow popupWindow;
                        popupWindow = BubblePopupHelper.create(getApplicationContext(), bubbleLayout_hometype);

                        listview_hometype_ho = (ListView) bubbleLayout_hometype.findViewById(R.id.listView);
                        bubbleLayout_hometype.setVisibility(v.GONE);
                        loading = new ProgressDialog(getApplication());
                        try
                        {
                            loading1 = new ProgressDialog(Activity_MoveMyHome_Detail.this);
                            loading1.getWindow().setBackgroundDrawable(new

                                    ColorDrawable(android.graphics.Color.TRANSPARENT));
                            loading1.setIndeterminate(true);
                            loading1.setCancelable(true);
                            loading1.show();
                            loading1.setContentView(R.layout.my_progress);

                            String url = UserFunctions.URL + "shipper/GetShippingTimeById?sizetypecode=" + Hometypeid_store;
                            Log.e("fffff",url);
                            SyncMethod_time(url);


                        }
                        catch (Exception e)
                        {

                        }

                        listview_hometype_ho.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                            @Override
                            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                                    long id) {
                                String CatID = actorsList_ho.get(position).getId();
                                String CatName = actorsList_ho.get(position).getName();

                                time_store=CatName;
                                time_edittext.setText(time_store);
                                popupWindow.dismiss();

                                // Toast.makeText(getContext(),Hometypename_store+"  "+Hometypeid_store,Toast.LENGTH_SHORT).show();


                            }
                        });


                        int[] location = new int[2];
                        v.getLocationInWindow(location);

                        bubbleLayout_hometype.setArrowDirection(ArrowDirection.TOP);

                        popupWindow.showAtLocation(v, Gravity.NO_GRAVITY, location[0], v.getHeight() + location[1]);

                    }
                    else
                    {

                        UF.msg("Check Your Internet Connection.");
                    }

                }
            });

        }
        public class CustomDialogClass_timepicker extends Dialog

        {

            public Context context;
            public Dialog d;
            ListView listview_time;
            private ProgressDialog loading;
            Button title;

            ArrayList<Document_method> actorsList;
            DocumentAdapter adapter;

            public CustomDialogClass_timepicker(Context a) {
                super(a);
                this.context = a;
            }

            @Override
            protected void onCreate(Bundle savedInstanceState) {

                super.onCreate(savedInstanceState);
                requestWindowFeature(Window.FEATURE_NO_TITLE);
                setContentView(R.layout.dialog_hometype);
                context = this.getContext();
                title = (Button) findViewById(R.id.title);
                title.setText("Select Time");
                listview_time = (ListView) findViewById(R.id.listView);

                loading = new ProgressDialog(context);
                try {


                    loading = new ProgressDialog(Activity_MoveMyHome_Detail.this);
                    loading.getWindow().setBackgroundDrawable(new

                            ColorDrawable(android.graphics.Color.TRANSPARENT));
                    loading.setIndeterminate(true);
                    loading.setCancelable(false);
                    loading.show();
                    loading.setContentView(R.layout.my_progress);

                    String url = UserFunctions.URL + "shipper/GetShippingTimeById?sizetypecode=" + Hometypeid_store;
                    SyncMethod(url);

                } catch (Exception e) {

                }
                listview_time.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long id) {
                        String CatID = actorsList.get(position).getId();
                        String CatName = actorsList.get(position).getName();

                        time_store=CatName;
                        time_edittext.setText(time_store);
                  /*  timename_store = CatName;
                    timeid_store = CatID;
                    time_edittext.setText(timename_store);
                    time_edittext.setError(null);
                    try
                    {
                        converttimeformate(CatName);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }*/
                        // Constants.store_Hometypeid=Hometypeid_store;
                        //hometype_edittext.setError(null);
                        // Toast.makeText(getContext(),Hometypename_store+"  "+Hometypeid_store,Toast.LENGTH_SHORT).show();
                        dismiss();

                    }
                });


            }


            public void SyncMethod(final String GetUrl) {
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
                                loading.cancel();
                                title.setVisibility(View.VISIBLE);
                                listview_time.setVisibility(View.VISIBLE);
                                JSONObject get_res = new JSONObject(aResponse);

                                actorsList = new ArrayList<Document_method>();
                                JSONArray array = new JSONArray();

                                array = get_res.getJSONArray("message");
                                Log.e("mess", "screen>>" + array.toString());
                                for (int aa = 0; aa < array.length(); aa++) {


                                    Log.e("mess", "screen>>" + array.getJSONObject(aa).getString("Sr_no").toString());
                                    actorsList.add(new Document_method(array.getJSONObject(aa).getString("Sr_no"), array.getJSONObject(aa).getString("ShippingTime"), array.getJSONObject(aa).getString("Sr_no")));


                                }
                                adapter = new DocumentAdapter(Activity_MoveMyHome_Detail.this, actorsList);
                                listview_time.setAdapter(adapter);
                                adapter.notifyDataSetChanged();

                            } catch (Exception e) {

                            }


                        }
                    };
                });
                // Start Thread
                background.start();
            }

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

                loading = new ProgressDialog(Activity_MoveMyHome_Detail.this);
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
                    String formattedtime="";
                    try
                    {
                        String time = time_edittext.getText().toString();
                        formattedtime= time.substring(0,1);
                        formattedtime="0"+formattedtime+":00";
                    }
                    catch (Exception e)
                    {
                        formattedtime=time_edittext.getText().toString();
                    }

                    prmsLogin.put("load_inquiry_no",id);
                    prmsLogin.put("shipper_id",sm.getUniqueId().toString());
                    prmsLogin.put("load_inquiry_shipping_date",date_edittext.getText().toString());
                    if(formattedtime.equalsIgnoreCase("00:00"))
                    {
                        prmsLogin.put("load_inquiry_shipping_time",time_edittext.getText().toString());
                    }
                    else
                    {
                        prmsLogin.put("load_inquiry_shipping_time",formattedtime);
                    }
                    prmsLogin.put("created_host","");
                    prmsLogin.put("created_by","");
                    prmsLogin.put("device_id",android_id.toString());
                    prmsLogin.put("device_type","M");


                    jsonArray.put(prmsLogin);
                    prms.put("order", jsonArray);


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Log.e("feedback json >>>>>",  prmsLogin + "");
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
                    if (json_save.equalsIgnoreCase("0")) {
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
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
                }


            }
        }

    }
    //////
    class CustomDialogClass_time extends Dialog
    {

        public Context context;
        public Dialog d;
        ProgressDialog loading;
        Button submit_btn;
        EditText mess_edittext;


        public CustomDialogClass_time(Context a)
        {
            super(a);
            this.context = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_cancelorder);
            context = this.getContext();
            mess_edittext = (EditText) findViewById(R.id.mess_edittext);
            submit_btn = (Button) findViewById(R.id.submit_btn);


            submit_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    messdata = mess_edittext.getText().toString();
                    if(messdata.equalsIgnoreCase(""))
                    {
                        mess_edittext.setError("Enter valid reason");
                    }
                    else
                    {
                        android.support.v7.app.AlertDialog.Builder builder  = new android.support.v7.app.AlertDialog.Builder(Activity_MoveMyHome_Detail.this);
                        builder.setMessage("Are you sure Cancel Order?");
                        // builder.setTitle("New Order");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(submit_btn.getWindowToken(),
                                InputMethodManager.RESULT_UNCHANGED_SHOWN);

                        new GetJson_save_cancleorder().execute();
                    }



                }
            });


        }
        private class GetJson_save_cancleorder extends AsyncTask<Void, Void, String> {
            //ProgressHUD mProgressHUD;
            private ProgressDialog  loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

                loading = new ProgressDialog(Activity_MoveMyHome_Detail.this);
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
                    prmsLogin.put("Reason",messdata);

                    //jsonArray.put(prmsLogin);
                    // prms.put("Feedback", jsonArray);


                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                Log.e("feedback json >>>>>",  prmsLogin + "");
                json_save = UF.RegisterUser("shipper/SendCancelOrderRequestByShipper",  prmsLogin);

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
                        } catch (JSONException e) {

                            e.printStackTrace();
                        }
                    }
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
            Log.e("lat Long marks info url",url_putlatlong);
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
    public static int getPixelsFromDp(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int)(dp * scale + 0.5f);
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
    public void SyncMethod_time(final String GetUrl) {
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
                        loading1.cancel();
                       bubbleLayout_hometype.setVisibility(View.VISIBLE);

                        JSONObject get_res = new JSONObject(aResponse);

                        actorsList_ho= new ArrayList<Document_method>();
                        JSONArray array = new JSONArray();

                        array = get_res.getJSONArray("message");
                        Log.e("mess", "screen>>" + array.toString());
                        for (int aa = 0; aa < array.length(); aa++) {


                            Log.e("mess", "screen>>" + array.getJSONObject(aa).getString("Sr_no").toString());
                            actorsList_ho.add(new Document_method(array.getJSONObject(aa).getString("Sr_no"), array.getJSONObject(aa).getString("ShippingTime"), array.getJSONObject(aa).getString("Sr_no")));


                        }
                        adapter_ho = new DocumentAdapter(Activity_MoveMyHome_Detail.this, actorsList_ho);
                        listview_hometype_ho.setAdapter(adapter_ho);
                        adapter_ho.notifyDataSetChanged();

                    } catch (Exception e) {

                    }


                }
            };
        });
        // Start Thread
        background.start();
    }

}