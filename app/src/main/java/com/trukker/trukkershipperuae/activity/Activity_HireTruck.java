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
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.provider.SyncStateContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
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
import com.trukker.trukkershipperuae.Bubble_layout.ArrowDirection;
import com.trukker.trukkershipperuae.Bubble_layout.BubbleLayout;
import com.trukker.trukkershipperuae.Bubble_layout.BubblePopupHelper;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.adapter.DocumentAdapter;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;
import com.trukker.trukkershipperuae.model.Document_method;
import com.trukker.trukkershipperuae.model.MyCustomAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Activity_HireTruck extends AppCompatActivity
{
    ListView listview_hometype_ho;
    ArrayList<Document_method> actorsList_ho;
    DocumentAdapter adapter_ho;
    BubbleLayout bubbleLayout_hometype;

    String country_source;
    String[] country_api;

    String[] long_name;
    String api_longname_so, api_lat_so, api_lng_so;

    String[] compare_name;
    String[] compare_id;

    LatLngBounds bounds;
    public Polyline line;

    UserFunctions UF;
    SessionManager sm;
    ConnectionDetector cd;
   String store_lastpoint_source="";
    String  Hometypename_store ,Hometypeid_store;
    Calendar cal, cal1;
    int seconds, hors;
    boolean islineFlag = false;
    DatePickerDialog.OnDateSetListener datePicker, datePicker1;
    DatePickerDialog.OnDateSetListener datePicker_end, datePicker1_end;
    double   source_lat_main = 0.00;
    double  source_long_main = 0.00;
    String posting_shippingid="";
    int height;
    TextView hometype_edittext,date_edittext,date2_edittext,time_edittext,form_close;
    AutoCompleteTextView movefrom_edittext;
    EditText noofdays_edittext;
     LinearLayout form,form_open;
    String countryISOCode,android_id;
    private GoogleMap googleMap;
    TextView getquote;
    String json_save;
    ProgressDialog  loading;
    TextView back;
    String first_step_store = "";
    String timename_store,timeid_store;
    //all string use for authocompleted text
    private static final String LOG_TAG = "ExampleApp";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

    private static final DateFormat TWELVE_TF = new SimpleDateFormat("hh:mm");
    // Replace with kk:mm if you want 1-24 interval
    private static final DateFormat TWENTY_FOUR_TF = new SimpleDateFormat("HH:mm");

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Constants.storemail_send = "0";
            Intent i = new Intent(Activity_HireTruck.this, MainActivity.class);
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
        setContentView(R.layout.movemyhome_hire);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        init(); //declare all controls
        loading = new ProgressDialog(getApplicationContext());

        createDatePicker();
        createDatePickerDeliveryDate();
        createDatePicker2();
        createDatePickerDeliveryDate2();

        countryISOCode = Constants.countryISOCod;


        try
        {
            movefrom_edittext.setThreshold(1);
            movefrom_edittext.setAdapter(new GooglePlacesAutocompleteAdapter_so(this.getApplicationContext(), R.layout.list_item));

        }
        catch (Exception e) {

        }


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
            public void onClick(View v)
            {

                Constants.storemail_send = "0";
                Intent i = new Intent(Activity_HireTruck.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                finish();


            }
        });


        getquote.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                Constants.promocode_discount_value_store="";
                Constants.promocode_store="";
                Constants.paymentpage_point = "N";
                getGetquote_click();


            }
        });

        hometype_edittext.setOnClickListener(new View.OnClickListener() {
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
                        loading = new ProgressDialog(Activity_HireTruck.this);
                        loading.getWindow().setBackgroundDrawable(new

                                ColorDrawable(android.graphics.Color.TRANSPARENT));
                        loading.setIndeterminate(true);
                        loading.setCancelable(true);
                        loading.show();
                        loading.setContentView(R.layout.my_progress);

                        String url = UserFunctions.URL + "truck/GetTrucksBodyType?typecode=HT";
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

        form_open.setOnClickListener(new View.OnClickListener()
        {
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

                DatePickerDialog datePicker1 = new DatePickerDialog(Activity_HireTruck.this, R.style.AppTheme_Dark_Dialog, datePicker, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));

                datePicker1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePicker1.show();
                date_edittext.setError(null);
                // time_edittext.setText("");

            }
        });
        date2_edittext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
                String date="";
                InputMethodManager im = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(date2_edittext.getWindowToken(), 0);

                DatePickerDialog datePicker12 = new DatePickerDialog(Activity_HireTruck.this, R.style.AppTheme_Dark_Dialog, datePicker_end, cal
                        .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH));
                 date =date_edittext.getText().toString();




                if(date.equalsIgnoreCase(""))
                {
                    datePicker12.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                    datePicker12.show();
                    date2_edittext.setError(null);
                }
                else
                {  SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    Date dateformare=null;
                    try
                    {
                        SimpleDateFormat format1 = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateformare1=null;
                        try {
                            dateformare1 = format1.parse(date);
                            Date dtStartDate=dateformare1;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar c = Calendar.getInstance();
                            c.setTime(dtStartDate);
                            Log.e("dtStartDate",dtStartDate.toString());
                            c.add(Calendar.DATE, 1);  // number of days to add
                            date = sdf.format(c.getTime());  // dt is now the new date


                        } catch (ParseException e)
                        {

                            e.printStackTrace();
                        }

                        dateformare = format.parse(date);
                        Date dtStartDate = dateformare;
                        long milli = dtStartDate.getTime();

                        datePicker12.getDatePicker().setMinDate(milli - 1000);
                        datePicker12.show();
                        date2_edittext.setError(null);
                    }
                    catch (Exception e )
                    {

                    }

                }

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
                    mTimePicker = new TimePickerDialog(Activity_HireTruck.this, R.style.AppTheme_Dark_Dialog, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {

                            time_edittext.setText(pad(selectedHour) + ":" + pad(00));
                        }
                    }, hour, minute, true);//Yes 24 hour time
                    mTimePicker.setTitle("Select Time");
                    mTimePicker.show();


            }
        });
        noofdays_edittext.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                {
                    if(Integer.parseInt(noofdays_edittext.getText().toString())>0)
                    {
                        String date =date_edittext.getText().toString();
                        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                        Date dateformare=null;
                        try {
                            dateformare = format.parse(date);
                            Date dtStartDate=dateformare;
                            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                            Calendar c = Calendar.getInstance();
                            c.setTime(dtStartDate);
                            Log.e("dtStartDate",dtStartDate.toString());
                            c.add(Calendar.DATE, (Integer.parseInt(noofdays_edittext.getText().toString())-1));  // number of days to add
                            String dt = sdf.format(c.getTime());  // dt is now the new date
                            date2_edittext.setText(dt);

                        } catch (ParseException e)
                        {

                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        date2_edittext.setText(date_edittext.getText().toString());
                        noofdays_edittext.setText("1");
                    }



                }
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
        noofdays_edittext.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                movefrom_edittext.setCursorVisible(true);
                return false;

            }
        });
/////////////////////////
    }


    private double[] createRandLocation(double latitude, double longitude)
    {

        return new double[]{latitude + ((Math.random() - 0.5) / 500),longitude + ((Math.random() - 0.5) / 500),  150 + ((Math.random() - 0.5) * 10)};
    }

    public void init()  {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_HireTruck.this);
        UF = new UserFunctions(Activity_HireTruck.this);
        cd = new ConnectionDetector(Activity_HireTruck.this);


        back = (TextView) findViewById(R.id.back);
        form_open = (LinearLayout) findViewById(R.id.form_open);
        form = (LinearLayout) findViewById(R.id.form);
        form_close = (TextView) findViewById(R.id.form_close);
        getquote = (TextView) findViewById(R.id.getquote);

        hometype_edittext = (TextView) findViewById(R.id.hometype_edittext_hh);
        time_edittext = (TextView) findViewById(R.id.time_edittext);
        date_edittext = (TextView) findViewById(R.id.date_edittext);
        date2_edittext = (TextView) findViewById(R.id.date2_edittext);
        movefrom_edittext = (AutoCompleteTextView) findViewById(R.id.movefrom_edittext);

        noofdays_edittext = (EditText) findViewById(R.id.noofdays_edittext);
        noofdays_edittext.setCursorVisible(false);
        form_open.setVisibility(View.GONE);
        Calendar c = Calendar.getInstance();
        seconds = c.get(Calendar.MINUTE);
        hors = c.get(Calendar.HOUR);

        SimpleDateFormat sdf1= new SimpleDateFormat("HH:mm");
        String str = sdf1.format(new Date());


        try
        {

            if(seconds==00 ||seconds==0)
            {
                Date date = new Date();
                date.setHours(date.getHours());
                System.out.println(date);
                SimpleDateFormat simpDate;
                simpDate = new SimpleDateFormat("kk:mm");
                System.out.println(simpDate.format(date));

                time_edittext.setText(simpDate.format(date).toString());

            }
            else
            {
                Date date = new Date();
                date.setHours(date.getHours()+1);
                System.out.println(date);
                SimpleDateFormat simpDate;
                simpDate = new SimpleDateFormat("kk");
                System.out.println(simpDate.format(date)+":00");

                time_edittext.setText(simpDate.format(date).toString()+":00");
            }

        }catch(Exception e)
        {

        }



        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
        String  formattedDate = df.format(c.getTime());
        date_edittext.setText(formattedDate.toString());//default date display
        date2_edittext.setText(formattedDate.toString());//default date display
        noofdays_edittext.setText("1");

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

        public CustomDialogClass_hometype(Context a)
        {
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
                loading = new ProgressDialog(Activity_HireTruck.this);
                loading.getWindow().setBackgroundDrawable(new

                        ColorDrawable(android.graphics.Color.TRANSPARENT));
                loading.setIndeterminate(true);
                loading.setCancelable(true);
                loading.show();
                loading.setContentView(R.layout.my_progress);


                String url = UserFunctions.URL + "truck/GetTrucksBodyType?typecode=HT";
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
                            adapter = new DocumentAdapter(Activity_HireTruck.this, actorsList);
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

    private static String pad(int c)
    {
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
                try
                {
                    int final_display= getDaysDifference(date_edittext.getText().toString(), date2_edittext.getText().toString());
                    Log.e("Dtae Display", String.valueOf(final_display));
                    if(final_display==0)
                        final_display= final_display+1;
                    else if(final_display==-1)
                        final_display=1;
                    else if(final_display<=0)
                    {
                        date2_edittext.setText(date_edittext.getText().toString());
                        final_display=1;
                    }
                    else
                        final_display=final_display+1;
                    noofdays_edittext.setText(String.valueOf(final_display));

                }catch (Exception e)
                {

                }
            }

        };
    }
    public void createDatePicker2() {
        cal = Calendar.getInstance();
        datePicker_end = new DatePickerDialog.OnDateSetListener() {

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
                date2_edittext.setText(s);

                try
                {
                    int final_display= getDaysDifference(date_edittext.getText().toString(), date2_edittext.getText().toString());
                    Log.e("Dtae Display", String.valueOf(final_display));
                    if(final_display==0)
                        final_display=final_display+1;
                    else if(final_display==-1)
                        final_display=1;
                    else if(final_display<=0)
                        date2_edittext.setText(date_edittext.getText().toString());
                    else
                        final_display=final_display+1;
                    noofdays_edittext.setText(String.valueOf(final_display));
                }catch (Exception e)
                {

                }

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
    public void createDatePickerDeliveryDate2() {
        cal1 = Calendar.getInstance();
        datePicker1_end = new DatePickerDialog.OnDateSetListener() {

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
                if (isDateAfter(date2_edittext.getText().toString(), s.toString())) {
                    // etdeliveryDate.setText(s);
                } else {
                    Toast.makeText(getApplicationContext(), "Delivery date must be greater than shipping date.", Toast.LENGTH_SHORT).show();

                }

            }

        };
    }
    public static boolean isDateAfter(String startDate, String endDate)
    {
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

                          //  country_api_des = new String[a1.length()];
                           // long_name_des = new String[a1.length()];
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
                            putmark();

                            if (api_longname_so.equalsIgnoreCase(""))
                            {

                            }
                            else
                            {
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
                            putmark();

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








    public void drowmappath() {
        googleMap.clear();

        final LatLng lat_lng1 = new LatLng(Constants.postloadsourceLat, Constants.postloadsourceLong);


        MarkerOptions marker1 = null;

        marker1 = new MarkerOptions().position(lat_lng1).title("Source Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)).snippet(movefrom_edittext.getText().toString());
        // marker2 = new MarkerOptions().position(lat_lng2).title("Destination Location").icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.))).snippet("Destination Location");

        googleMap.addMarker(marker1);

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        boundsBuilder.include(lat_lng1);

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
        String date2 = date2_edittext.getText().toString().trim();
        String time = time_edittext.getText().toString();
        String movefrom = movefrom_edittext.getText().toString().trim();
        String noofdays = noofdays_edittext.getText().toString().trim();

        String source_city = Constants.postload_source_city;



        if (hometype.isEmpty()) {
            hometype_edittext.setError("Select Truck Type");

            valid = false;
        } else {
            hometype_edittext.setError(null);
        }


        if (date.isEmpty()) {
            date_edittext.setError("Select Starting Date");

            valid = false;
        } else {
            date_edittext.setError(null);
        }

        if (date2.isEmpty()) {
            date2_edittext.setError("Select Ending Date");

            valid = false;
        } else {
            date2_edittext.setError(null);
        }
        if (noofdays.isEmpty()) {
            noofdays_edittext.setError("Enter No of Days");

            valid = false;
        } else {
            noofdays_edittext.setError(null);
        }

        if (time.isEmpty()) {
            time_edittext.setError("Select Time");

            valid = false;
        } else {
            time_edittext.setError(null);
        }

        if (movefrom.isEmpty() || source_city.equalsIgnoreCase("")) {
            movefrom_edittext.setError("Enter valid Start Point Address");

            valid = false;
        } else {
            movefrom_edittext.setError(null);
        }




        return valid;
    }

    public void getGetquote_click() {
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


            Constants.store_shipperid = sm.getUniqueId();
            Constants.store_email = sm.getemailid();


            Constants.store_movefrom = movefrom_edittext.getText().toString();
            Constants.store_date = date_edittext.getText().toString();
            Constants.store_date2 = date2_edittext.getText().toString();
            Constants.store_time_display = time_edittext.getText().toString();
            Constants.store_noofdays = noofdays_edittext.getText().toString();

            if (cd.isConnectingToInternet())
            {

                new GetJsonmovemyhome_save().execute();
            }
            else
            {
                UF.msg("Check Your Internet Connection.");
            }


        }







    @Override
    public void onResume() {
        super.onResume();

        // Toast.makeText(getApplication(),String.valueOf(Constants.counter),Toast.LENGTH_SHORT).show();
        //clear all data
        movefrom_edittext.setText("");
        hometype_edittext.setText("");
        timename_store = "";
        timeid_store = "";
        store_lastpoint_source = "";
        Hometypename_store = "";
        Hometypeid_store = "";

        source_lat_main = 0.00;
        source_long_main = 0.00;


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

    private void setAnimationDialog() {

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

        /*if (formopenclose_flow == 0) {

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
        }
*/

    }

    public void converttimeformate(String timename_store) throws ParseException
    {

        try {

            String firstLetter = timename_store.substring(0, 1);
            String last = timename_store.substring(Math.max(timename_store.length() - 2, 0));
            timename_store = pad(Integer.parseInt(firstLetter)) + ":" + pad(00) + " " + last;

            System.out.println("time in 12 hour format : " + timename_store);
            SimpleDateFormat inFormat = new SimpleDateFormat("hh:mm aa");
            SimpleDateFormat outFormat = new SimpleDateFormat("HH:mm");
            //store_time = outFormat.format(inFormat.parse(timename_store));
        }catch (Exception e)
        {

        }

    }

    public void defaultvalueset() throws ParseException {

        if (Constants.counter >=1) {
            // Toast.makeText(getApplication(),String.valueOf(Constants.counter),Toast.LENGTH_SHORT).show();
            // Toast.makeText(getApplication(),String.valueOf(Constants.store_Hometypename),Toast.LENGTH_SHORT).show();
            hometype_edittext.setText(Constants.store_Hometypename);
            date2_edittext.setText(Constants.store_date2);
            date_edittext.setText(Constants.store_date);
            time_edittext.setText(Constants.store_time_display);
            movefrom_edittext.setText(Constants.store_movefrom);
            noofdays_edittext.setText(Constants.store_noofdays);

            store_lastpoint_source="S";


            source_lat_main=Constants.postloadsourceLat;
            source_long_main=Constants.postloadsourceLong;

           // converttimeformate(Constants.store_time_display);
            drowmappath();


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

            loading = new ProgressDialog(Activity_HireTruck.this);
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
                    prmsLogin.put("load_inquiry_no", posting_shippingid);
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

                            if(Constants.Draft.equalsIgnoreCase("Y"))
                            {

                            }
                            else
                            {
                                Constants.store_loadinquiry_no = load_inquiry_no;
                            }

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

                                Constants.counter = 1;
                                Intent i = new Intent(Activity_HireTruck.this, Activity_HireQuote.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);//


                            } else {
                                Constants.getquote_moving = "Login_hire";
                                Intent i = new Intent(Activity_HireTruck.this, MainActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(i);
                                overridePendingTransition(R.anim.left_in, R.anim.right_out);
                                finish();

                            }

                        } else if (status.equalsIgnoreCase("2")) {
                            posting_shippingid = message;
                            // getGetquote_click();
                            if (cd.isConnectingToInternet())
                            {

                                new GetJsonmovemyhome_save_second().execute();
                            } else {
                                UF.msg("Check Your Internet Connection.");
                            }


                        } else if (status.equalsIgnoreCase("3")) {

                            if (sm.isLoggedIn()) {
                                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();

                            } else {

                                Intent i = new Intent(Activity_HireTruck.this, Dialog_postlodedetail.class);
                                startActivity(i);
                                overridePendingTransition(R.anim.right_in, R.anim.left_out);


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

    private class GetJsonmovemyhome_save_second extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        private ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            loading = new ProgressDialog(Activity_HireTruck.this);
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

            try
            {
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
                    prmsLogin.put("shipper_id", sm.getUniqueId());
                } else {
                    prmsLogin.put("shipper_id", "");
                }
                if (sm.isLoggedIn()) {
                    prmsLogin.put("load_inquiry_no", "");
                } else {
                    prmsLogin.put("load_inquiry_no", posting_shippingid);
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
                Log.e("prmsLogin", prms + "");
                json_save = UF.RegisterUser("postorder/SaveHireTruckDetails", prms);




        } catch (JSONException e) {

                e.printStackTrace();
            }


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
                            String NoOfTruck_s = array.getJSONObject(0).getString("NoOfTruck").toString();
                            String NoOfLabour_s = array.getJSONObject(0).getString("NoOfLabour").toString();
                            String NoOfHandiman_s = array.getJSONObject(0).getString("NoOfHandiman").toString();
                            String BaseRate_s = array.getJSONObject(0).getString("BaseRate").toString();
                            String TotalLabourRate_s = array.getJSONObject(0).getString("TotalLabourRate").toString();
                            String TotalHandimanRate_s = array.getJSONObject(0).getString("TotalHandimanRate").toString();
                            String TotalPackingRate_s = array.getJSONObject(0).getString("Hiretruck_TotalFuelRate").toString();
                            String totlecost_stan = array.getJSONObject(0).getString("Total_cost").toString();
                            String Hiretruck_MaxKM_s = array.getJSONObject(0).getString("Hiretruck_MaxKM").toString();


                            if (sm.isLoggedIn()) {
                                String load_inquiry_no = array.getJSONObject(1).getString("load_inquiry_no").toString();
                                Constants.store_loadinquiry_no = load_inquiry_no;

                            } else {
                                Constants.store_loadinquiry_no = posting_shippingid;
                            }



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

                            Constants.panding = 3;
                            Constants.getquote_moving = "Login_hire";
                            Intent i = new Intent(Activity_HireTruck.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(i);
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            finish();

                        }
                        else
                        {
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

    public static int getDaysDifference(String fromDate,String toDate)
    {

        String dtStart = fromDate;
        String dtend = toDate;
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date datef=null,datet=null;
        try {
             datef = format.parse(dtStart);
             datet = format.parse(dtend);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if(fromDate==null||toDate==null)
            return 0;

        return (int)( (datet.getTime() - datef.getTime()) / (1000 * 60 * 60 * 24));
    }
    public void putmark()
    {
        googleMap.clear();

        final LatLng lat_lng1 = new LatLng(Constants.postloadsourceLat, Constants.postloadsourceLong);

        MarkerOptions marker1 = null;

        marker1 = new MarkerOptions().position(lat_lng1).title("Start Point").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)).snippet(movefrom_edittext.getText().toString());
        googleMap.addMarker(marker1);
        LatLng cur_Latlng=new LatLng(Constants.postloadsourceLat, Constants.postloadsourceLong); // giving your marker to zoom to your location area.
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(cur_Latlng));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }
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
                        adapter_ho = new DocumentAdapter(Activity_HireTruck.this, actorsList_ho);
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
    public static String convertTo24HoursFormat(String twelveHourTime)
            throws ParseException {
        return TWENTY_FOUR_TF.format(
                TWELVE_TF.parse(twelveHourTime));
    }

}


