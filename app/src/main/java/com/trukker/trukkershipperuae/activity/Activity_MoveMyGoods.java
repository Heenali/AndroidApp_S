package com.trukker.trukkershipperuae.activity;

/**
 * Created by admin on 8/30/2016.
 */

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
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
import com.trukker.trukkershipperuae.model.MyCustomAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Activity_MoveMyGoods extends AppCompatActivity
{

    ListView listview_hometype_ho;
    ArrayList<Document_method> actorsList_ho;
    DocumentAdapter adapter_ho;
    BubbleLayout bubbleLayout_hometype;

    ImageView hometype_icon;
    ProgressDialog progressDialog;
    //variable using
    RelativeLayout goodsdetail_layout, arae_layout;
    TextView back;
    TextView trucktype_text;
    int seconds, hors;
    ProgressDialog  loading;
    String formattedDate;
    String posting_shippingid = "";
    public Polyline line;
    UserFunctions UF;
    TextView Activity_title;
    String store_shipperid = "", store_email = "";
    SessionManager sm;
    ConnectionDetector cd;
    String Hometypename_store = "", Hometypeid_store = "", timename_store = "", timeid_store = "";
    String json_save;
    String android_id;
    String countryISOCode;
    int formopenclose_flow = 0;
    boolean islineFlag = false;
    String store_time;
    //all controls
    private GoogleMap googleMap;
    LinearLayout form_open;
    TextView form_close;
    LinearLayout form;
    TextView hometype_edittext, time_edittext, date_edittext;
    EditText arae_edittext;
    AutoCompleteTextView movefrom_edittext, moveto_edittext;
    Calendar cal, cal1;
    TextView getquote;
    DatePickerDialog.OnDateSetListener datePicker, datePicker1;
    TextView distance_editetext;
    int height;

    //all string use for calculating lat long related proocess
    double source_lat_main = 0.00, source_long_main = 0.00, dest_lat_main = 0.00, dest_long_main = 0.00;
    double Store_distance = 0.00;
    String first_step_store = "";
    String first_step_store_des = "";
    String country_source, country_destination;
    String[] country_api;
    String[] country_api_des;

    String[] long_name;
    String[] long_name_des;

    String api_lat_des = "", api_longname_des = "", api_lng_des = "";
    String api_longname_so, api_lat_so, api_lng_so;
    String[] compare_name_desc;
    String[] compare_id_desc;
    String[] compare_name;
    String[] compare_id;
    String store_lastpoint_source = "", store_lastpoint_dest = "";
    LatLngBounds bounds;
    EditText goodsdetail_edittext;
    //all string use for authocompleted text
    private static final String LOG_TAG = "ExampleApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Constants.storemail_send_goods = "0";
            Intent i = new Intent(Activity_MoveMyGoods.this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            overridePendingTransition(R.anim.left_in, R.anim.right_out);
            finish();


        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movemyhome);

        Constants.paymentpage_point="N";//use in payment back option
        init(); //declare all controls
        loading = new ProgressDialog(getApplicationContext());

        createDatePicker();
        createDatePickerDeliveryDate();

        //iscode use for autocompleted textview fetching default contry
        countryISOCode = Constants.countryISOCod;

        //call adater of authocompletedtextview
        try {
            movefrom_edittext.setThreshold(1);
            movefrom_edittext.setAdapter(new GooglePlacesAutocompleteAdapter_so(this.getApplicationContext(), R.layout.list_item));

            moveto_edittext.setThreshold(1);
            moveto_edittext.setAdapter(new GooglePlacesAutocompleteAdapter_des(this.getApplicationContext(), R.layout.list_item));
        } catch (Exception e) {

        }
        //////////////
        movefrom_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movefrom_edittext.setCursorVisible(true);
            }
        });
        moveto_edittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Toast.makeText(getApplicationContext(),"to",Toast.LENGTH_SHORT).show();
                store_lastpoint_dest = "D";
                moveto_edittext.setSelection(0);
                callDestinationMethod();

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(moveto_edittext.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }
        });

        movefrom_edittext.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //  Toast.makeText(getApplicationContext(),"from",Toast.LENGTH_SHORT).show();

                store_lastpoint_source = "S";
                movefrom_edittext.setSelection(0);
                callSourceMethod();
                // Store_distance = distance(source_lat_main, source_long_main, dest_lat_main, dest_long_main, 'K');
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(movefrom_edittext.getWindowToken(),
                        InputMethodManager.RESULT_UNCHANGED_SHOWN);

            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Constants.storemail_send_goods = "0";
                Intent i = new Intent(Activity_MoveMyGoods.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();


            }
        });
        getquote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                Constants.promocode_discount_value_store="";
                Constants.promocode_store="";
                //calling all validation related button
                //calling login if not
                // String s="http://maps.googleapis.com/maps/api/directions/json?origin="+movefrom_edittext.getText().toString()+"&destination="+moveto_edittext.getText().toString()+"&sensor=false";
                //  s=s.replace(" ", "%20").trim();

                //Log.e("sssssss", s.trim());
                // SyncMethod_distance(s);
                getGetquote_click();


            }
        });
        moveto_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // callDestinationMethod();

                } else {


                }
            }
        });

        movefrom_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                boolean y = hasFocus;

                if (!hasFocus) {
                    // callSourceMethod();
                } else {

                }
            }
        });

        hometype_edittext.setOnClickListener(new View.OnClickListener()
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
                    try {
                        loading = new ProgressDialog(Activity_MoveMyGoods.this);
                        loading.getWindow().setBackgroundDrawable(new

                                ColorDrawable(android.graphics.Color.TRANSPARENT));
                        loading.setIndeterminate(true);
                        loading.setCancelable(true);
                        loading.show();
                        loading.setContentView(R.layout.my_progress);

                        String url = UserFunctions.URL + "truck/GetTrucksBodyType?typecode=t";
                        SyncMethod_hometype(url);


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

                            Hometypename_store = CatName;
                            Hometypeid_store = CatID;
                            hometype_edittext.setText(Hometypename_store);
                            Constants.store_Hometypeid = Hometypeid_store;
                            Constants.store_Hometypename = Hometypename_store;
                            hometype_edittext.setError(null);
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
        form_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setAnimationDialog();


            }
        });

        form_open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                form.setVisibility(View.VISIBLE);
                form_open.setVisibility(View.GONE);
                Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
                form_open.startAnimation(anm);
            }
        });
        date_edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(date_edittext.getWindowToken(), 0);

                DatePickerDialog datePicker1 = new DatePickerDialog(Activity_MoveMyGoods.this, R.style.AppTheme_Dark_Dialog, datePicker, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                datePicker1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker1.show();
                date_edittext.setError(null);
                // time_edittext.setText("");

            }
        });
        time_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Activity_MoveMyGoods.this, R.style.AppTheme_Dark_Dialog, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                        time_edittext.setText(pad(selectedHour) + ":" + pad(selectedMinute));
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();


                //CustomDialogClass_time timeclass = new CustomDialogClass_time(Activity_MoveMyGoods.this);
                // timeclass.show();
            }
        });

////////current location
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if (status != ConnectionResult.SUCCESS) {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(false);
            //googleMap.setOnMyLocationChangeListener(this);

            double latitude = 25.276987;
            double longitude = 55.296249;
            LatLng latLng = new LatLng(latitude, longitude);
            //googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
            //locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);

        }
/////////////////////////
    }

    private double[] createRandLocation(double latitude, double longitude) {

        return new double[]{latitude + ((Math.random() - 0.5) / 500),
                longitude + ((Math.random() - 0.5) / 500),
                150 + ((Math.random() - 0.5) * 10)};
    }

    public void init() {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_MoveMyGoods.this);
        UF = new UserFunctions(Activity_MoveMyGoods.this);
        cd = new ConnectionDetector(Activity_MoveMyGoods.this);
        Activity_title = (TextView) findViewById(R.id.title);
        Activity_title.setText("Details");
        hometype_icon = (ImageView) findViewById(R.id.hometype_icon);
        hometype_icon.setBackgroundResource(R.drawable.trucktype);
        goodsdetail_layout = (RelativeLayout) findViewById(R.id.goodsdetail_layout);
        goodsdetail_layout.setVisibility(View.GONE);
        arae_layout = (RelativeLayout) findViewById(R.id.arae_layout);
        arae_layout.setVisibility(View.GONE);
        trucktype_text = (TextView) findViewById(R.id.noof_packing_title);
        trucktype_text.setText("Truck Type");
        back = (TextView) findViewById(R.id.back);
        TextView rowline = (TextView) findViewById(R.id.rowline);
        rowline.setVisibility(View.GONE);
        distance_editetext = (TextView) findViewById(R.id.distance_editetext);
        form_open = (LinearLayout) findViewById(R.id.form_open);
        form = (LinearLayout) findViewById(R.id.form);
        form_close = (TextView) findViewById(R.id.form_close);
        getquote = (TextView) findViewById(R.id.getquote);
        hometype_edittext = (TextView) findViewById(R.id.hometype_edittext_hh);
        arae_edittext = (EditText) findViewById(R.id.arae_edittext);
        goodsdetail_edittext = (EditText) findViewById(R.id.goodsdetail_edittext);
        time_edittext = (TextView) findViewById(R.id.time_edittext);
        date_edittext = (TextView) findViewById(R.id.date_edittext);
        movefrom_edittext = (AutoCompleteTextView) findViewById(R.id.movefrom_edittext);
        moveto_edittext = (AutoCompleteTextView) findViewById(R.id.moveto_edittext);

        hometype_edittext.setHint("Select Truck Type");

        form_open.setVisibility(View.GONE);

        movefrom_edittext.setCursorVisible(false);
        Calendar c = Calendar.getInstance();
        seconds = c.get(Calendar.MINUTE);
        hors = c.get(Calendar.HOUR);


        SimpleDateFormat sdf1= new SimpleDateFormat("HH:mm");
        String str = sdf1.format(new Date());
        time_edittext.setText(str);//default time display

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        formattedDate = df.format(c.getTime());
        date_edittext.setText(formattedDate.toString());//default date display
        // movefrom_edittext.setCursorVisible(false);


    }

    /////////
   /* class CustomDialogClass_hometype extends Dialog

    {

        public Context context;
        public Dialog d;
        ListView listview_hometype;
        ProgressDialog loading;
        Button title;
        ArrayList<Document_method> actorsList;
        DocumentAdapter adapter;

        public CustomDialogClass_hometype(Context a) {
            super(a);
            this.context = a;
        }

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.dialog_hometype);
            context = this.getContext();
            title = (Button) findViewById(R.id.title);
            listview_hometype = (ListView) findViewById(R.id.listView);
            title.setText("Select Truck Type");
            loading = new ProgressDialog(context);
            try {
                loading = new ProgressDialog(Activity_MoveMyGoods.this);
                loading.getWindow().setBackgroundDrawable(new

                        ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setIndeterminate(true);
                loading.setCancelable(true);
                loading.show();
                loading.setContentView(R.layout.my_progress);

                String url = UserFunctions.URL + "truck/GetTrucksBodyType?typecode=t";
                SyncMethod(url);
            } catch (Exception e) {

            }
            listview_hometype.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long id) {
                    String CatID = actorsList.get(position).getId();
                    String CatName = actorsList.get(position).getName();

                    Hometypename_store = CatName;
                    Hometypeid_store = CatID;
                    hometype_edittext.setText(Hometypename_store);
                    Constants.store_Hometypeid = Hometypeid_store;
                    Constants.store_Hometypename = Hometypename_store;
                    hometype_edittext.setError(null);

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
                            title.setText("Select Truck Type");
                            listview_hometype.setVisibility(View.VISIBLE);
                            JSONObject get_res = new JSONObject(aResponse);

                            actorsList = new ArrayList<Document_method>();
                            JSONArray array = new JSONArray();

                            array = get_res.getJSONArray("message");
                            Log.e("mess", "screen>>" + array.toString());
                            for (int aa = 0; aa < array.length(); aa++) {


                                Log.e("mess", "screen>>" + array.getJSONObject(aa).getString("truck_body_desc").toString());
                                actorsList.add(new Document_method(array.getJSONObject(aa).getString("truck_body_id"), array.getJSONObject(aa).getString("truck_body_desc"), array.getJSONObject(aa).getString("truck_body_desc")));


                            }
                            adapter = new DocumentAdapter(Activity_MoveMyGoods.this, actorsList);
                            listview_hometype.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        } catch (Exception e) {

                        }


                    }
                };
            });
            // Start Thread
            background.start();
        }

    }*/
////////////////////////


    public class CustomDialogClass_time extends Dialog

    {

        public Context context;
        public Dialog d;
        ListView listview_time;
        private ProgressDialog loading;
        Button title;
        ArrayList<Document_method> actorsList;
        DocumentAdapter adapter;

        public CustomDialogClass_time(Context a) {
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


                loading = new ProgressDialog(Activity_MoveMyGoods.this);
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

                    timename_store = CatName;
                    timeid_store = CatID;
                    time_edittext.setText(timename_store);
                    time_edittext.setError(null);
                    try {
                        converttimeformate(CatName);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
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
                            adapter = new DocumentAdapter(Activity_MoveMyGoods.this, actorsList);
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

    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
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

    public void createDatePickerDeliveryDate() {
        cal1 = Calendar.getInstance();
        datePicker1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                // TODO Auto-generated method stub
                cal1.set(Calendar.YEAR, year);
                cal1.set(Calendar.MONTH, monthOfYear);
                cal1.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                //tv_dig_YY.setText(myCalendarmedFrom.get(Calendar.YEAR) + "");
                String selected_year = String.valueOf(cal1.get(Calendar.YEAR) + "");
                String month = String.valueOf(cal1.get(Calendar.MONTH) + 1);

                String day = String.valueOf(cal1
                        .get(Calendar.DAY_OF_MONTH));

                if (month.length() == 1) {
                    //tv_dig_MM.setText("0" + month);
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
                String s = sdf.format(cal1.getTime());
                if (isDateAfter(date_edittext.getText().toString(), s.toString())) {
                    // etdeliveryDate.setText(s);
                } else {
                    Toast.makeText(getApplicationContext(), "Delivery date must be greater than shipping date.", Toast.LENGTH_SHORT).show();

                }

            }

        };
    }

    public static boolean isDateAfter(String startDate, String endDate) {
        try {
            String myFormatString = "dd-MMM-yyyy"; // for example
            SimpleDateFormat df = new SimpleDateFormat(myFormatString);
            Date date1 = df.parse(endDate);
            Date startingDate = df.parse(startDate);

            if (date1.after(startingDate))
                return true;
            else
                return false;
        } catch (Exception e) {

            return false;
        }
    }

    class GooglePlacesAutocompleteAdapter_so extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter_so(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete_so(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList<String> autocomplete_so(String input) {
        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);

            sb.append("&input=" + URLEncoder.encode(input, "utf8") + "&components=country:" + Constants.countryISOCod);


            URL url = new URL(sb.toString());
            System.out.println("URL: " + url);
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    class GooglePlacesAutocompleteAdapter_des extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public GooglePlacesAutocompleteAdapter_des(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete_des(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                        //Toast.makeText(getContext(),"iiiiiii",Toast.LENGTH_SHORT).show();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    } else {
                        notifyDataSetInvalidated();
                    }
                }
            };
            return filter;
        }
    }

    public static ArrayList<String> autocomplete_des(String input) {
        ArrayList<String> resultList = null;
        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);

            sb.append("&input=" + URLEncoder.encode(input, "utf8") + "&components=country:" + Constants.countryISOCod);

           /* String finalurl= "https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+ input + "&components=country:"+Constants.countryISOCod+"&sensor=false&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

            URL url = new URL(finalurl.toString());
            Log.i("dddd", finalurl.toString());*/

            URL url = new URL(sb.toString());
            System.out.println("URL: " + url);

            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {

            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                System.out.println(predsJsonArray.getJSONObject(i).getString("description"));
                System.out.println("============================================================");
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));

            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    private void callDestinationMethod() {
        // Toast.makeText(getApplicationContext(),"desti",Toast.LENGTH_SHORT).show();
        if (first_step_store_des.equalsIgnoreCase("")) {

            if (moveto_edittext.getText().toString().length() > 9) {
                String s = moveto_edittext.getText().toString();
                s = s.replace(" ", "%20");

                String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + s + "&components=country:" + countryISOCode + "&sensor=false&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

                SyncMethod_placeapi_des(url);
            } else {
                if (moveto_edittext.getText().toString().length() >= 1)
                    Toast.makeText(getApplicationContext(), "Enter Valid Destination Address", Toast.LENGTH_SHORT).show();
            }

        } else {
            if (moveto_edittext.getText().toString().length() > 9) {

                String url_locality = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + first_step_store_des + "&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";
                SyncMethod_localityapi_des(url_locality);
            } else {
                if (moveto_edittext.getText().toString().length() >= 1)
                    Toast.makeText(getApplicationContext(), "Enter Valid Destination Address", Toast.LENGTH_SHORT).show();
            }


        }

        // Store_distance = distance(source_lat_main, source_long_main, dest_lat_main, dest_long_main, 'K');
    }

    private void callSourceMethod() {
        // Toast.makeText(getApplicationContext(),"source",Toast.LENGTH_SHORT).show();
        if (first_step_store.equalsIgnoreCase("")) {

            //  Toast.makeText(getContext(), String.valueOf(atvPlaces_so.getText().toString().length()), Toast.LENGTH_SHORT).show();
            if (movefrom_edittext.getText().toString().length() > 9) {

                String s = movefrom_edittext.getText().toString();
                s = s.replace(" ", "%20");
                // String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + s + "&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";
                String url = "https://maps.googleapis.com/maps/api/place/autocomplete/json?input=" + s + "&components=country:" + countryISOCode + "&sensor=false&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

                SyncMethod_placeapi_src(url);

            } else {
                if (movefrom_edittext.getText().toString().length() >= 1) {
                    Toast.makeText(getApplicationContext(), "Enter Valid Source Address", Toast.LENGTH_SHORT).show();
                }

            }

        } else {
            if (movefrom_edittext.getText().toString().length() > 9) {

                String url_locality = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + first_step_store + "&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";
                SyncMethod_localityapi_src(url_locality);
            } else {
                if (movefrom_edittext.getText().toString().length() >= 1)
                    Toast.makeText(getApplicationContext(), "Enter Valid Source Address", Toast.LENGTH_SHORT).show();

            }
        }


    }

    public void SyncMethod_placeapi_des(final String GetUrl) {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
            // After call for background.start this run method call
            public void run() {
                try {
                    String url = GetUrl;
                    String SetServerString = "";
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

                        JSONObject get_res = new JSONObject(aResponse);
                        JSONArray array = new JSONArray();
                        String status = get_res.getString("status");
                        if (status.equalsIgnoreCase("OK")) {
                            array = get_res.getJSONArray("predictions");

                            compare_name_desc = new String[array.length()];
                            compare_id_desc = new String[array.length()];

                            for (int aa = 0; aa < array.length(); aa++) {
                                String s = array.getJSONObject(aa).getString("description").toString();
                                s = s.replace(" ", "");
                                s = s.replace(",", "");
                                //  Toast.makeText(getActivity(),"mystring is :"+s, Toast.LENGTH_LONG).show();
                                compare_name_desc[aa] = s;
                                compare_id_desc[aa] = array.getJSONObject(aa).getString("place_id");
                            }
                            String id = "";
                            String match_string = moveto_edittext.getText().toString();
                            // Toast.makeText(getActivity(),"mystring is :"+atvPlaces_des.getText().toString(), Toast.LENGTH_LONG).show();
                            match_string = match_string.replace(" ", "");
                            match_string = match_string.replace(",", "");
                            for (int aa = 0; aa < array.length(); aa++) {
                                String s = compare_name_desc[aa].toString();
                                if (String.valueOf(compare_name_desc[aa]).equalsIgnoreCase(match_string)) {
                                    id = compare_id_desc[aa];

                                    Log.e("Check", compare_name_desc[aa]);
                                    Log.e("Check", compare_id_desc[aa]);
                                    //Toast.makeText(getActivity(),id, Toast.LENGTH_LONG).show();

                                    String url_locality = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

                                    SyncMethod_localityapi_des(url_locality);
                                }

                            }
                            if (id.equalsIgnoreCase("")) {
                                id = array.getJSONObject(0).getString("place_id");
                                //  Toast.makeText(getActivity(),id, Toast.LENGTH_LONG).show();
                                String url_locality = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

                                SyncMethod_localityapi_des(url_locality);
                            }

                        } else {
                            String s = moveto_edittext.getText().toString();
                            s = s.replace(" ", "%20");
                            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + s;

                            SyncMethod_gecodeapi_des(url);

                        }


                    } catch (Exception e) {

                    }

                }
            };
        });

        background.start();
    }

    public void SyncMethod_localityapi_des(final String GetUrl) {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
            // After call for background.start this run method call
            public void run() {
                try {
                    String url = GetUrl;
                    String SetServerString = "";
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

                        JSONObject get_res = new JSONObject(aResponse);
                        JSONArray array = new JSONArray();
                        String status = get_res.getString("status");
                        Log.e("status", "screen>>" + status);
                        if (status.equalsIgnoreCase("OK")) {
                            JSONObject result = get_res.getJSONObject("result");
                            Log.e("object first", "screen>>" + result.toString());

                            //////for lat log
                            JSONObject latlog = result.getJSONObject("geometry");
                            JSONObject latlog1 = latlog.getJSONObject("location");

                            api_lat_des = latlog1.getString("lat");
                            api_lng_des = latlog1.getString("lng");
                            /////////////////////////

                            ///////for long name
                            array = result.getJSONArray("address_components");
                            Log.e("array", "screen>>" + array.toString());
                            country_api_des = new String[array.length()];
                            long_name_des = new String[array.length()];
                            for (int aa = 0; aa < array.length(); aa++) {

                                String store1 = array.getJSONObject(aa).getString("types");
                                String store2 = array.getJSONObject(aa).getString("long_name");
                                country_api_des[aa] = store1.toString();
                                long_name_des[aa] = store2.toString();
                            }


                            StringBuilder sb = new StringBuilder();
                            for (String n : country_api_des) {
                                if (sb.length() > 0) sb.append(',');
                                sb.append("'").append(n).append("'");
                            }
                            Log.i("sssss", sb.toString());
                            if (sb.toString().contains("country")) {
                                for (int aa = 0; aa < array.length(); aa++) {
                                    if (array.getJSONObject(aa).getString("types").contains("country")) {
                                        country_destination = array.getJSONObject(aa).getString("long_name");
                                        Log.i("destination_country", country_destination);
                                        // UF.msg(country_destination);
                                        Constants.destination_country = country_destination;
                                        if (country_destination.equalsIgnoreCase("")) {
                                            country_destination = result.getString("formatted_address").toString();

                                        }
                                        //UF.msg(country_destination);
                                        Constants.destination_country = country_destination;
                                    }
                                }
                            } else {
                                Constants.destination_country = "";
                            }
                            if (sb.toString().contains("administrative_area_level_3")) {

                                for (int aa = 0; aa < array.length(); aa++) {
                                    if (array.getJSONObject(aa).getString("types").contains("administrative_area_level_3")) {
                                        api_longname_des = array.getJSONObject(aa).getString("long_name");
                                        Log.i("1sssss", api_longname_des.toString());
                                    }

                                }

                            } else {
                                if (sb.toString().contains("administrative_area_level_2")) {

                                    for (int aa = 0; aa < array.length(); aa++) {
                                        if (array.getJSONObject(aa).getString("types").contains("administrative_area_level_2")) {
                                            api_longname_des = array.getJSONObject(aa).getString("long_name");
                                            Log.i("2sssss", api_longname_des.toString());
                                        }

                                    }

                                } else {

                                    for (int aa = 0; aa < array.length(); aa++) {
                                        if (array.getJSONObject(aa).getString("types").contains("locality")) {
                                            api_longname_des = array.getJSONObject(aa).getString("long_name");
                                            Log.i("1sssss", api_longname_des.toString());
                                        }

                                    }
                                }
                            }

                            Constants.postload_dest_city = api_longname_des;
                            Constants.postloaddestLat = Double.parseDouble(api_lat_des);
                            Constants.postloaddestLong = Double.parseDouble(api_lng_des);
                            dest_lat_main = Double.parseDouble(api_lat_des);
                            dest_long_main = Double.parseDouble(api_lng_des);
                            //geometry
                            Log.i("api_lat_so", api_lat_des.toString());
                            Log.i("api_lng_so", api_lng_des.toString());

                            if (store_lastpoint_dest.equalsIgnoreCase("D") && store_lastpoint_source.equalsIgnoreCase("S")) {
                                Store_distance = distance(source_lat_main, source_long_main, dest_lat_main, dest_long_main, 'K');
                                //Toast.makeText(getApplicationContext(),"ssssss",Toast.LENGTH_SHORT).show();
                            }

                            // Toast.makeText(getActivity(), "long_name :" + api_longname_des + " Lat : " + api_lat_des.toString() + " lng : " +api_lng_des, Toast.LENGTH_SHORT).show();
                            if (api_longname_des.equalsIgnoreCase("")) {

                            } else {
                                Constants.postload_dest_city = api_longname_des;
                            }
                            if (api_lat_des.equalsIgnoreCase("") && api_lng_des.equalsIgnoreCase("")) {
                                // lldestpincode.setVisibility(View.VISIBLE);
                                UF.msg("Enter valid Destination Address");

                            } else {
                                // lldestpincode.setVisibility(View.GONE);
                                if (Constants.postload_dest_city.equalsIgnoreCase("")) {
                                    // destination_city_layout.setVisibility(View.VISIBLE);
                                    UF.msg(" Destination City Not Find Please Enter");
                                }
                            }
                            // Toast.makeText(getActivity(), "long_name :" + api_longname_des + " Lat : " + api_lat_des.toString() + " lng : " +api_lng_des, Toast.LENGTH_SHORT).show();

                        } else {

                            // Toast.makeText(getActivity(), "ZERO_RESULTS ", Toast.LENGTH_LONG).show();

                            // lldestpincode.setVisibility(View.VISIBLE);
                            UF.msg("Enter valid Destination Address");
                        }

                    } catch (Exception e) {

                    }

                }
            };
        });
        background.start();
    }

    public void SyncMethod_gecodeapi_des(final String GetUrl) {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
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

                        JSONObject get_res = new JSONObject(aResponse);

                        JSONArray array = new JSONArray();
                        String status = get_res.getString("status");
                        Log.e("status", "" + status);
                        if (status.equalsIgnoreCase("OK")) {
                            Log.e("status", "" + status);
                            JSONArray a = get_res.getJSONArray("results");
                            Log.e("status", "" + a.toString());

                            JSONObject j = a.getJSONObject(0);

                            JSONArray a1 = j.getJSONArray("address_components");


                            Log.e("object first", "screen>>" + a1.toString());

                            country_api_des = new String[a1.length()];
                            long_name_des = new String[a1.length()];
                            a1.getJSONObject(0).getString("types");
                            Log.e("object first", "screen>>" + a1.getJSONObject(0).getString("types").toString());

                            for (int aaa = 0; aaa < a1.length(); aaa++) {

                                if (a1.getJSONObject(aaa).getString("types").contains("administrative_area_level_3")) {
                                    api_longname_des = a1.getJSONObject(aaa).getString("long_name");
                                    Log.e("api_longname_des", "screen>>" + a1.getJSONObject(aaa).getString("long_name").toString());
                                } else {
                                    if (a1.getJSONObject(aaa).getString("types").contains("administrative_area_level_2")) {
                                        api_longname_des = a1.getJSONObject(aaa).getString("long_name");
                                        Log.e("api_longname_des", "screen>>" + a1.getJSONObject(aaa).getString("long_name").toString());
                                    } else {
                                        if (a1.getJSONObject(aaa).getString("types").contains("locality")) {
                                            api_longname_des = a1.getJSONObject(aaa).getString("long_name");
                                            Log.e("api_longname_des", "screen>>" + a1.getJSONObject(aaa).getString("long_name").toString());
                                        }
                                    }
                                }

                                if (a1.getJSONObject(aaa).getString("types").contains("country")) {
                                    country_destination = a1.getJSONObject(aaa).getString("long_name");
                                    Constants.destination_country = country_destination;
                                    // Toast.makeText(getContext(), Constants.destination_country, Toast.LENGTH_SHORT).show();
                                }

                            }
                            Log.i("api_longname_des", api_longname_des.toString());


                            JSONObject latlog = j.getJSONObject("geometry");
                            Log.i("api_longname_des", latlog.toString());
                            JSONObject latlog1 = latlog.getJSONObject("location");

                            api_lat_des = latlog1.getString("lat");
                            api_lng_des = latlog1.getString("lng");

                            Log.i("api_lat_so", api_lat_des.toString());
                            Log.i("api_lng_so", api_lng_des.toString());

                            Constants.postload_dest_city = api_longname_des;
                            Constants.postloaddestLat = Double.parseDouble(api_lat_des);
                            Constants.postloaddestLong = Double.parseDouble(api_lng_des);
                            dest_lat_main = Double.parseDouble(api_lat_des);
                            dest_long_main = Double.parseDouble(api_lng_des);
                            //geometry
                            Log.i("api_lat_so", api_lat_des.toString());
                            Log.i("api_lng_so", api_lng_des.toString());

                            //Toast.makeText(getActivity(), "long_name :" + api_longname_des + " Lat : " + api_lat_des.toString() + " lng : " +api_lng_des, Toast.LENGTH_SHORT).show();
                            if (api_longname_des.equalsIgnoreCase("")) {

                            } else {
                                Constants.postload_dest_city = api_longname_des;
                            }

                            if (api_lat_des.equalsIgnoreCase("") && api_lng_des.equalsIgnoreCase("")) {
                                // lldestpincode.setVisibility(View.VISIBLE);
                                UF.msg("Enter valid Destination Address");

                            } else {
                                // lldestpincode.setVisibility(View.GONE);
                                if (Constants.postload_dest_city.equalsIgnoreCase("")) {
                                    //destination_city_layout.setVisibility(View.VISIBLE);
                                    UF.msg("Destination City Not Find Please Enter");
                                }
                            }

                            // Toast.makeText(getActivity(), "long_name :" + api_longname_des + " Lat : " + api_lat_des.toString() + " lng : " +api_lng_des, Toast.LENGTH_SHORT).show();

                        } else {

                            //Toast.makeText(getActivity(), "ZERO_RESULTS ", Toast.LENGTH_LONG).show();

                            //lldestpincode.setVisibility(View.VISIBLE);
                            UF.msg("Enter valid Destination Address");
                        }

                    } catch (Exception e) {

                    }

                }
            };
        });

        background.start();
    }

    /////
    public void SyncMethod_placeapi_src(final String GetUrl) {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
            // After call for background.start this run method call
            public void run() {
                try {
                    String url = GetUrl;
                    String SetServerString = "";
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

                        JSONObject get_res = new JSONObject(aResponse);
                        JSONArray array = new JSONArray();
                        String status = get_res.getString("status");
                        if (status.equalsIgnoreCase("OK")) {
                            array = get_res.getJSONArray("predictions");

                            compare_name = new String[array.length()];
                            compare_id = new String[array.length()];

                            for (int aa = 0; aa < array.length(); aa++) {
                                String s = array.getJSONObject(aa).getString("description").toString();
                                s = s.replace(" ", "");
                                s = s.replace(",", "");
                                compare_name[aa] = s;
                                compare_id[aa] = array.getJSONObject(aa).getString("place_id");
                            }
                            String id = "";
                            String match_string = movefrom_edittext.getText().toString();
                            match_string = match_string.replace(" ", "");
                            match_string = match_string.replace(",", "");
                            for (int aa = 0; aa < array.length(); aa++) {
                                String s = compare_name[aa].toString();
                                if (String.valueOf(compare_name[aa]).equalsIgnoreCase(match_string)) {
                                    id = compare_id[aa];
                                    Log.e("Check", compare_name[aa]);
                                    Log.e("Check", compare_id[aa]);
                                    // Toast.makeText(getActivity(),id, Toast.LENGTH_LONG).show();

                                    String url_locality = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

                                    SyncMethod_localityapi_src(url_locality);
                                }

                            }
                            if (id.equalsIgnoreCase("")) {
                                // Toast.makeText(getActivity(), "ZERO_RESULTS ", Toast.LENGTH_LONG).show();
                                id = array.getJSONObject(0).getString("place_id");
                                // Toast.makeText(getActivity(),id, Toast.LENGTH_LONG).show();
                                String url_locality = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + id + "&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

                                SyncMethod_localityapi_src(url_locality);
                                // llsourcepincode.setVisibility(View.VISIBLE);
                                // UF.msg("Enter Source Pincode");

                            }
                        } else {
                            String s = movefrom_edittext.getText().toString();
                            s = s.replace(" ", "%20");
                            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + s;
                            SyncMethod_gecodeapi_src(url);

                        }
                    } catch (Exception e) {

                    }

                }
            };
        });

        background.start();
    }

    public void SyncMethod_gecodeapi_src(final String GetUrl) {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {
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

                        JSONObject get_res = new JSONObject(aResponse);

                        JSONArray array = new JSONArray();
                        String status = get_res.getString("status");
                        Log.e("status", "" + status);
                        if (status.equalsIgnoreCase("OK")) {
                            Log.e("status", "" + status);
                            JSONArray a = get_res.getJSONArray("results");
                            Log.e("status", "" + a.toString());

                            JSONObject j = a.getJSONObject(0);

                            JSONArray a1 = j.getJSONArray("address_components");


                            Log.e("object first", "screen>>" + a1.toString());

                            country_api_des = new String[a1.length()];
                            long_name_des = new String[a1.length()];
                            a1.getJSONObject(0).getString("types");
                            Log.e("object first", "screen>>" + a1.getJSONObject(0).getString("types").toString());

                            for (int aaa = 0; aaa < a1.length(); aaa++) {

                                if (a1.getJSONObject(aaa).getString("types").contains("administrative_area_level_3")) {
                                    api_longname_so = a1.getJSONObject(aaa).getString("long_name");
                                    Log.e("object first", "screen>>" + a1.getJSONObject(aaa).getString("long_name").toString());
                                } else {
                                    if (a1.getJSONObject(aaa).getString("types").contains("administrative_area_level_2")) {
                                        api_longname_so = a1.getJSONObject(aaa).getString("long_name");
                                        Log.e("object first", "screen>>" + a1.getJSONObject(aaa).getString("long_name").toString());
                                    } else {
                                        if (a1.getJSONObject(aaa).getString("types").contains("locality")) {
                                            api_longname_so = a1.getJSONObject(aaa).getString("long_name");
                                            Log.e("object first", "screen>>" + a1.getJSONObject(aaa).getString("long_name").toString());
                                        }
                                    }
                                }
                                if (a1.getJSONObject(aaa).getString("types").contains("country")) {
                                    country_source = a1.getJSONObject(aaa).getString("long_name");
                                    Constants.source_country = country_source;
                                    //  Toast.makeText(getContext(), Constants.source_country, Toast.LENGTH_SHORT).show();
                                }

                            }
                            Log.i("api_longname_des", api_longname_so.toString());


                            JSONObject latlog = j.getJSONObject("geometry");
                            Log.i("api_longname_des", latlog.toString());
                            JSONObject latlog1 = latlog.getJSONObject("location");

                            api_lat_so = latlog1.getString("lat");
                            api_lng_so = latlog1.getString("lng");

                            Log.i("api_lat_so", api_lat_so.toString());
                            Log.i("api_lng_so", api_lng_so.toString());

                            Constants.postloadsourceLat = Double.parseDouble(api_lat_so);
                            Constants.postloadsourceLong = Double.parseDouble(api_lng_so);
                            source_lat_main = Double.parseDouble(api_lat_so);
                            source_long_main = Double.parseDouble(api_lng_so);
                            if (api_longname_so.equalsIgnoreCase("")) {

                            } else {
                                Constants.postload_source_city = api_longname_so;
                            }
                            if (api_lat_so.equalsIgnoreCase("") && api_lng_so.equalsIgnoreCase("")) {
                                // llsourcepincode.setVisibility(View.VISIBLE);
                                UF.msg("Enter valid Source Address");

                            } else {
                                // llsourcepincode.setVisibility(View.GONE);


                                if (Constants.postload_source_city.equalsIgnoreCase("")) {
                                    // source_city_layout.setVisibility(View.VISIBLE);
                                    UF.msg("Source City Not Find Please Enter");
                                }
                            }


                            //  Toast.makeText(getActivity(), "long_name :" + api_longname_so + " Lat : " + api_lat_so.toString() + " lng : " + api_lng_so, Toast.LENGTH_SHORT).show();

                        } else {
                            // Toast.makeText(getActivity(), "ZERO_RESULTS ", Toast.LENGTH_LONG).show();

                            // llsourcepincode.setVisibility(View.VISIBLE);
                            UF.msg("Enter valid Source Address");
                        }

                    } catch (Exception e) {

                    }

                }
            };
        });

        background.start();
    }

    public void SyncMethod_localityapi_src(final String GetUrl) {
        Log.i("Url.............", GetUrl);
        final Thread background = new Thread(new Runnable() {

            public void run() {
                try {
                    String url = GetUrl;
                    String SetServerString = "";
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

                        JSONObject get_res = new JSONObject(aResponse);
                        JSONArray array = new JSONArray();
                        String status = get_res.getString("status");
                        Log.e("status", "screen>>" + status);
                        if (status.equalsIgnoreCase("OK")) {
                            JSONObject result = get_res.getJSONObject("result");
                            Log.e("object first", "screen>>" + result.toString());

                            //////for lat log
                            JSONObject latlog = result.getJSONObject("geometry");
                            JSONObject latlog1 = latlog.getJSONObject("location");

                            String api_lat_so = latlog1.getString("lat");
                            String api_lng_so = latlog1.getString("lng");
                            /////////////////////////

                            ///////for long name
                            array = result.getJSONArray("address_components");
                            Log.e("array", "screen>>" + array.toString());
                            country_api = new String[array.length()];
                            long_name = new String[array.length()];

                            for (int aa = 0; aa < array.length(); aa++) {

                                String store1 = array.getJSONObject(aa).getString("types");
                                String store2 = array.getJSONObject(aa).getString("long_name");
                                country_api[aa] = store1.toString();
                                long_name[aa] = store2.toString();
                            }


                            StringBuilder sb = new StringBuilder();
                            for (String n : country_api) {
                                if (sb.length() > 0) sb.append(',');
                                sb.append("'").append(n).append("'");
                            }
                            Log.i("sssss", sb.toString());
                            String api_longname_so = "";
                            if (sb.toString().contains("country")) {
                                for (int aa = 0; aa < array.length(); aa++) {
                                    if (array.getJSONObject(aa).getString("types").contains("country")) {
                                        country_source = array.getJSONObject(aa).getString("long_name");
                                        Log.i("country_source", country_source);
                                        //  UF.msg(country_source);
                                        Constants.source_country = country_source;

                                        if (country_source.equalsIgnoreCase("")) {
                                            country_source = result.getString("formatted_address").toString();

                                        }
                                        //  UF.msg(country_source);
                                        Constants.source_country = country_source;
                                    }
                                }
                            } else {
                                Constants.source_country = "";
                            }

                            if (sb.toString().contains("administrative_area_level_3")) {

                                for (int aa = 0; aa < array.length(); aa++) {
                                    if (array.getJSONObject(aa).getString("types").contains("administrative_area_level_3")) {
                                        api_longname_so = array.getJSONObject(aa).getString("long_name");
                                        Log.i("1sssss", api_longname_so.toString());
                                    }

                                }

                            } else {
                                if (sb.toString().contains("administrative_area_level_2")) {

                                    for (int aa = 0; aa < array.length(); aa++) {
                                        if (array.getJSONObject(aa).getString("types").contains("administrative_area_level_2")) {
                                            api_longname_so = array.getJSONObject(aa).getString("long_name");
                                            Log.i("2sssss", api_longname_so.toString());
                                        }

                                    }

                                } else {
                                    for (int aa = 0; aa < array.length(); aa++) {
                                        if (array.getJSONObject(aa).getString("types").contains("locality")) {
                                            api_longname_so = array.getJSONObject(aa).getString("long_name");
                                            Log.i("1sssss", api_longname_so.toString());
                                        }

                                    }
                                }
                            }
                            //geometry
                            Log.i("api_lat_so", api_lat_so.toString());
                            Log.i("api_lng_so", api_lng_so.toString());

                            Constants.postloadsourceLat = Double.parseDouble(api_lat_so);
                            Constants.postloadsourceLong = Double.parseDouble(api_lng_so);

                            source_lat_main = Double.parseDouble(api_lat_so);
                            source_long_main = Double.parseDouble(api_lng_so);

                            if (store_lastpoint_dest.equalsIgnoreCase("D") && store_lastpoint_source.equalsIgnoreCase("S")) {
                                Store_distance = distance(source_lat_main, source_long_main, dest_lat_main, dest_long_main, 'K');
                                //Toast.makeText(getApplicationContext(),"dddddd",Toast.LENGTH_SHORT).show();
                            }

                            if (api_longname_so.equalsIgnoreCase("")) {

                            } else {
                                Constants.postload_source_city = api_longname_so;
                            }
                            if (api_lat_so.equalsIgnoreCase("") && api_lng_so.equalsIgnoreCase("")) {
                                // llsourcepincode.setVisibility(View.VISIBLE);
                                UF.msg("Enter valid Source Address");

                            } else {
                                // llsourcepincode.setVisibility(View.GONE);


                                if (Constants.postload_source_city.equalsIgnoreCase("")) {
                                    //source_city_layout.setVisibility(View.VISIBLE);
                                    UF.msg("Source City Not Find Please Enter");
                                }
                            }

                            // Toast.makeText(getActivity(), "long_name :" + api_longname_so + " Lat : " + api_lat_so.toString() + " lng : " + api_lng_so, Toast.LENGTH_SHORT).show();

                        } else {

                            // Toast.makeText(getActivity(), "ZERO_RESULTS ", Toast.LENGTH_LONG).show();

                            //llsourcepincode.setVisibility(View.VISIBLE);
                            UF.msg("Enter valid Source Address");
                        }


                    } catch (Exception e) {

                    }

                }
            };
        });

        background.start();
    }

    private double distance(double lat1, double lon1, double lat2, double lon2, char unit) {
        // Toast.makeText(getApplicationContext(),"distance",Toast.LENGTH_SHORT).show();
       /* int Radius = 6371;// radius of earth in Km

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value", "" + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

       // return Radius * c;

        double dist=kmInDec;
        String d=String.format("%.2f", dist);
        distance_editetext.setText(String.valueOf(d)+" KM ");*/
        String locationUrl = makeURL(source_lat_main, source_long_main, dest_lat_main, dest_long_main);
        DownloadTask1 downloadTask = new DownloadTask1();
        downloadTask.execute(locationUrl);

        Double dist = 0.00;

        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
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

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
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

    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
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
            if (result != null) {
                if (result.size() > 0) {

                    for (int i = 0; i < result.size(); i++) {
                        points = new ArrayList<LatLng>();
                        lineOptions = new PolylineOptions();

                        // Fetching i-th route
                        List<HashMap<String, String>> path = result.get(i);

                        // Fetching all the points in i-th route
                        for (int j = 0; j < path.size(); j++) {
                            HashMap<String, String> point = path.get(j);

                            double lat = Double.parseDouble(point.get("lat"));
                            double lng = Double.parseDouble(point.get("lng"));
                            LatLng position = new LatLng(lat, lng);

                            points.add(position);
                        }

                        // Adding all the points in the route to LineOptions
                        lineOptions.addAll(points);
                        if (!islineFlag) {
                            lineOptions.width(15);
                            lineOptions.color(Color.parseColor("#FF0000")).geodesic(true);
                            islineFlag = true;
                        } else {
                            lineOptions.width(15);
                            lineOptions.color(Color.parseColor("#FF0000")).geodesic(true);
                        }
                        //lineOptions.color(Color.parseColor("#59c1e3")).geodesic(true);


                    }

                    line = googleMap.addPolyline(lineOptions);    // Drawing polyline in the Google Map for the i-th route


                }
            }


        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exception url", "" + e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        String sensor = "sensor=false";
        String key = "key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";
        String parameters = str_origin + "&" + str_dest + "&" + sensor + "&" + key;
        String output = "json";

        String url = "https://maps.googleapis.com/maps/api/directions/json?" + parameters;
        Log.e("direction url", url);
        return url;
    }

    public void drowmappath() {
        googleMap.clear();

        final LatLng lat_lng1 = new LatLng(Constants.postloadsourceLat, Constants.postloadsourceLong);
        final LatLng lat_lng2 = new LatLng(Constants.postloaddestLat, Constants.postloaddestLong);

        MarkerOptions marker1 = null, marker2 = null;

        marker1 = new MarkerOptions().position(lat_lng1).title("Source Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)).snippet(movefrom_edittext.getText().toString());
        // marker2 = new MarkerOptions().position(lat_lng2).title("Destination Location").icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).snippet("Destination Location");
        marker2 = new MarkerOptions().position(lat_lng2).title("Destination Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.red_marker_map)).snippet(moveto_edittext.getText().toString());
        googleMap.addMarker(marker1);
        googleMap.addMarker(marker2);
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
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

    public boolean validate() {
        boolean valid = true;

        String hometype = hometype_edittext.getText().toString().trim();
        String date = date_edittext.getText().toString().trim();
        String time = time_edittext.getText().toString();
        String movefrom = movefrom_edittext.getText().toString().trim();
        String moveto = moveto_edittext.getText().toString().trim();
        String source_city = Constants.postload_source_city;
        String desti_city = Constants.postload_dest_city;


        if (hometype.isEmpty()) {
            hometype_edittext.setError("Select Truck Type");

            valid = false;
        } else {
            hometype_edittext.setError(null);
        }


        if (date.isEmpty()) {
            date_edittext.setError("Select Date");

            valid = false;
        } else {
            date_edittext.setError(null);
        }

        if (time.isEmpty()) {
            time_edittext.setError("Select Time");

            valid = false;
        } else {
            time_edittext.setError(null);
        }

        if (movefrom.isEmpty() || source_city.equalsIgnoreCase("")) {
            movefrom_edittext.setError("Enter valid Movefrom Address");

            valid = false;
        } else {
            movefrom_edittext.setError(null);
        }

        if (moveto.isEmpty() || desti_city.equalsIgnoreCase("")) {
            moveto_edittext.setError("Enter valid Moveto Address");

            valid = false;
        } else {
            moveto_edittext.setError(null);
        }


        return valid;
    }

    public void getGetquote_click()
    {
        Log.d("mess", "getGetquote");

        if (!validate()) {
            onSignupFailed();
            return;
        }
        getquote.setEnabled(false);
        onSignupSuccess();


    }

    public void onSignupFailed() {
        //Toast.makeText(getBaseContext(), "Registration failed", Toast.LENGTH_LONG).show();

        getquote.setEnabled(true);
    }

    public void onSignupSuccess() {
        getquote.setEnabled(true);
        setResult(RESULT_OK, null);

        String distance = Constants.store_placedistace;

        if (distance.equalsIgnoreCase("0.00") || distance.equalsIgnoreCase("0.0")) {
            Toast.makeText(getApplicationContext(), "Distance not found", Toast.LENGTH_SHORT).show();
        } else {
            Constants.store_shipperid = sm.getUniqueId();
            Constants.store_email = sm.getemailid();

            Constants.store_distance = Constants.store_placedistace;
            Constants.store_movefrom = movefrom_edittext.getText().toString();
            Constants.store_moveto = moveto_edittext.getText().toString();
            Constants.store_date = date_edittext.getText().toString();
            Constants.store_time = time_edittext.getText().toString();
            Constants.store_goodsdetails = "";
            Constants.storepayment_goodsw="";
            Constants.storepayment_goodswunit="KG";

            if (cd.isConnectingToInternet())
            {
                new GetJsonmovemyhome_save().execute();

            }
            else
            {
                UF.msg("Check Your Internet Connection.");
            }


        }
        //finish();

    }


    public String makeURL(Double sourcelat, Double sourcelng, Double destlat, Double destlng) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("https://maps.googleapis.com/maps/api/distancematrix/json");
        urlString.append("?origins=");// from
        //urlString.append(source);
        //gps.getLatitude(), gps.getLongitude()

        urlString.append(Double.toString(source_lat_main));
        urlString.append(",");
        urlString
                .append(Double.toString(source_long_main));
        urlString.append("&destinations=");// to
        //urlString.append(destination);
        urlString
                .append(Double.toString(destlat));
        urlString.append(",");
        urlString.append(Double.toString(destlng));
        //old server key
        //urlString.append("&units=metric&mode=driving&key=AIzaSyCg8bBJ4D8q61YPN42Dz2yr3tIT2EqG76c");
        //new server key
        urlString.append("&units=metric&mode=driving&key=AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E");
        //    urlString.append("&sensor=false&mode=driving&waypoints=kathpur%20toll%20plaza&alternatives=true");
        Log.e("Location URL>>", urlString.toString() + "");
        return urlString.toString();
    }

    private class DownloadTask1 extends AsyncTask<String, Void, String> {
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_MoveMyGoods.this);
            loading.getWindow().setBackgroundDrawable(new

                    ColorDrawable(android.graphics.Color.TRANSPARENT));
            loading.setIndeterminate(true);
            loading.setCancelable(false);
            loading.show();
            loading.setContentView(R.layout.my_progress);


        }

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

		/*	ParserTask1 parserTask = new ParserTask1();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);*/

            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("OK")) {
                        loading.dismiss();
                        JSONArray jsonArray = jsonObject.getJSONArray("rows");
                        JSONObject jobj = jsonArray.getJSONObject(0);
                        JSONArray jsonArrayElements = jobj.getJSONArray("elements");
                        JSONObject jobj1 = jsonArrayElements.getJSONObject(0);
                        String inside_status = jobj1.getString("status");
                        if (inside_status.equalsIgnoreCase("OK")) {
                            JSONObject jobjDuration = jobj1.getJSONObject("duration");
                            String duration = jobjDuration.getString("text");
                            Constants.store_placetime = "";
                            //Toast.makeText(getApplication(),duration,Toast.LENGTH_SHORT).show();
                            Log.e("dddddddddd", duration);
                            Constants.duration = jobjDuration.getString("text");

                            JSONObject jobjDistance = jobj1.getJSONObject("distance");
                            String distance = jobjDistance.getString("text");
                            distance_editetext.setText(distance);

                            //Toast.makeText(getApplication(),distance,Toast.LENGTH_SHORT).show();

                            String power=distance.substring(distance.lastIndexOf(" ") + 1);
                            distance = distance.substring(0, distance.indexOf(' '));
                            //distance=Constants.store_placedistace;
                            // String  firstname1 = distance.replace("[0-9]", "");
                            //firstname1=distance.replaceAll(",", "");
                            Constants.store_placedistace = distance;
                            Constants.store_unitdistance = "KM";
                            Log.e("Distance counting", distance);
                            Log.e("Distance counting", power);
                            Constants.store_unit=power;

                            drowmappath();
                            //Toast.makeText(getApplicationContext(),distance,Toast.LENGTH_SHORT).show();
                            Constants.distance = jobjDistance.getString("text");
                        }

                    }
                } catch (JSONException e) {

                }

            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();


        //clear all data
        movefrom_edittext.setText("");
        moveto_edittext.setText("");
       // date_edittext.setText("");
      //  time_edittext.setText("");
        arae_edittext.setText("");
        hometype_edittext.setText("");
        distance_editetext.setText("");

        timename_store = "";
        timeid_store = "";
        store_time = "";
        // time_edittext.setText(pad(hors)+":"+pad(seconds));//default time display
        // date_edittext.setText(formattedDate.toString());//default date display

        store_lastpoint_source = "";
        store_lastpoint_dest = "";

        Hometypename_store = "";
        Hometypeid_store = "";

        source_lat_main = 0.00;
        source_long_main = 0.00;
        dest_lat_main = 0.00;
        dest_long_main = 0.00;
        // Store_distance=0.00;
        api_lat_des = "";
        api_longname_des = "";
        api_lng_des = "";
        api_longname_so = "";
        api_lat_so = "";
        api_lng_so = "";

      /*  Constants.store_placedistace="0.00";
        Constants.store_unitdistance="KM";
        Constants.postload_source_city="";
        Constants.postload_dest_city="";
        Constants.postloadsourceLat=0.00;
        Constants.postloadsourceLong=0.00;
        Constants.postloaddestLat=0.00;
        Constants.postloaddestLong=0.00;*/
        googleMap.clear();
        try {
            defaultvalueset();
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    private void setAnimationDialog()
    {
        if (form.getVisibility() == View.VISIBLE)
        {
            form_close.setText("Back to Details");
            MyCustomAnimation a = new MyCustomAnimation(form, 500, MyCustomAnimation.COLLAPSE);
            height = a.getHeight();
            form.startAnimation(a);
        }
        else
        {
            form_close.setText("Show on Map");
            MyCustomAnimation a = new MyCustomAnimation(form, 500, MyCustomAnimation.EXPAND);
            a.setHeight(height);
            form.startAnimation(a);
        }


       /* if (formopenclose_flow == 0) {

            Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down_anim);
            form.startAnimation(animation1);

            formopenclose_flow = 1;
            form.setVisibility(View.GONE);
            form_open.setVisibility(View.GONE);

        } else {

            formopenclose_flow = 0;
            form_open.setVisibility(View.GONE);
            form.setVisibility(View.VISIBLE);

            Animation animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_out);
            form.startAnimation(animation1);
        }*/
    }

    public void converttimeformate(String timename_store) throws ParseException {

        String firstLetter = timename_store.substring(0, 1);
        String last = timename_store.substring(Math.max(timename_store.length() - 2, 0));
        timename_store = pad(Integer.parseInt(firstLetter)) + ":" + pad(00) + " " + last;

        System.out.println("time in 12 hour format : " + timename_store);
        SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");

        store_time = outFormat.format(inFormat.parse(timename_store));


    }

    public void defaultvalueset() throws ParseException {

        if (Constants.counter >= 1) {
            // Toast.makeText(getApplication(),String.valueOf(Constants.counter),Toast.LENGTH_SHORT).show();
            // Toast.makeText(getApplication(),String.valueOf(Constants.store_Hometypename),Toast.LENGTH_SHORT).show();
            hometype_edittext.setText(Constants.store_Hometypename);
            arae_edittext.setText(Constants.store_area);
            date_edittext.setText(Constants.store_date);
            time_edittext.setText(Constants.store_time);
            movefrom_edittext.setText(Constants.store_movefrom);
            moveto_edittext.setText(Constants.store_moveto);
            distance_editetext.setText(Constants.store_placedistace+" "+Constants.store_unit);
            goodsdetail_edittext.setText(Constants.store_goodsdetails);
            // converttimeformate(Constants.store_time_display);
            drowmappath();
            store_lastpoint_source="S";
            store_lastpoint_dest="D";

            source_lat_main=Constants.postloadsourceLat;
            source_long_main=Constants.postloadsourceLong;
            dest_lat_main=Constants.postloaddestLat;
            dest_long_main=Constants.postloaddestLong;

        }


    }

    ////////////////////////////////////
    //posting getquote
    private class GetJsonmovemyhome_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_MoveMyGoods.this);
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
                    prmsLogin.put("shipper_id", Constants.store_shipperid);
                } else {
                    prmsLogin.put("shipper_id", "");
                }
                if (sm.isLoggedIn()) {
                    prmsLogin.put("load_inquiry_no", "");
                } else {
                    prmsLogin.put("load_inquiry_no", posting_shippingid);
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
                            String TimeForUnloadingInMinute_g = array.getJSONObject(0).getString("TimeForUnloadingInMinute").toString();
                           // String load_inquiry_no = array.getJSONObject(0).getString("load_inquiry_no").toString();
                            String TimeToTravelInMinute = array.getJSONObject(0).getString("TimeToTravelInMinute").toString();
                            String load_inquiry_no = array.getJSONObject(0).getString("load_inquiry_no").toString();
                            Constants.store_NoOfTruck_g = NoOfTruck_g;
                            Constants.store_NoOfLabour_g = NoOfLabour_g;
                            Constants.store_NoOfHandiman_g = NoOfHandiman_g;
                            Constants.store_BaseRate_g = BaseRate_g;
                            if(Constants.Draft.equalsIgnoreCase("Y"))
                            {

                            }
                            else
                            {
                                Constants.store_loadinquiry_no = load_inquiry_no;
                            }

                            Constants.store_TotalLabourRate_g = TotalLabourRate_g;
                            Constants.store_TotalLabourRate_g_incdec = TotalLabourRate_g;
                            Constants.store_TotalHandimanRate_g = TotalHandimanRate_g;
                            Constants.store_TotalHandimanRate_g_incdec = TotalHandimanRate_g;
                            Constants.store_TotalPackingRate_g = TotalPackingRate_g;
                            Constants.totlecost_g = totlecost_g;
                            Constants.TimeForLoadingInMinute_g = TimeForLoadingInMinute_g;
                            Constants.TimeForUnloadingInMinute_g = TimeForUnloadingInMinute_g;

                            Constants.eta = TimeToTravelInMinute;

                            Log.e("NoOfTruck_p", NoOfTruck_g);
                            Log.e("NoOfLabour_p", NoOfLabour_g);
                            Log.e("NoOfHandiman_p", NoOfHandiman_g);
                            Log.e("BaseRate_p", BaseRate_g);
                            Log.e("TotalLabourRate_p", TotalLabourRate_g);
                            Log.e("TotalHandimanRate_p", TotalHandimanRate_g);
                            Log.e("TotalPackingRate_p", TotalPackingRate_g);
                            if (sm.isLoggedIn()) {

                                Constants.counter = 1;
                                Intent i = new Intent(Activity_MoveMyGoods.this, Activity_GoodsQuote.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);//


                            } else {
                                Constants.getquote_moving = "Login_Goods";
                                Intent i = new Intent(Activity_MoveMyGoods.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                                finish();

                            }

                        }
                        else if (status.equalsIgnoreCase("2"))
                        {
                            posting_shippingid = message;
                            //getGetquote_click();
                            Constants.panding = 2;
                            Constants.getquote_moving = "Login_Goods";
                            Intent i = new Intent(Activity_MoveMyGoods.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            finish();


                        } else {
                            UF.msg(message + "");
                        }
                    } catch (JSONException e)
                    {

                        e.printStackTrace();
                    }
                }
            }


        }
    }

    /*
    * adapter where the list values will be set
    */
    public void SyncMethod_hometype(final String GetUrl) {
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


                        bubbleLayout_hometype.setVisibility(View.VISIBLE);
                        JSONObject get_res = new JSONObject(aResponse);

                        actorsList_ho = new ArrayList<Document_method>();
                        JSONArray array = new JSONArray();

                        array = get_res.getJSONArray("message");
                        Log.e("mess", "screen>>" + array.toString());
                        for (int aa = 0; aa < array.length(); aa++) {


                            Log.e("mess", "screen>>" + array.getJSONObject(aa).getString("truck_body_desc").toString());
                            actorsList_ho.add(new Document_method(array.getJSONObject(aa).getString("truck_body_id"), array.getJSONObject(aa).getString("truck_body_desc"), array.getJSONObject(aa).getString("truck_body_desc")));


                        }
                        adapter_ho = new DocumentAdapter(Activity_MoveMyGoods.this, actorsList_ho);
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


