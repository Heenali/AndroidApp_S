package com.trukker.trukkershipperuae.activity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
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
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.SessionManager;
import com.trukker.trukkershipperuae.helper.UserFunctions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Activity_Feedback extends AppCompatActivity
{
//
    TextView id_txt;
    EditText comment_txt;
    TextView submit_btn;
    RatingBar rating;
    private GoogleMap googleMap;
    String android_id;
    UserFunctions UF;
    SessionManager sm;
    String json_save;
    ConnectionDetector cd;
    String id;
    ////
    TextView back;
    LatLngBounds bounds;
    String rating_value="0.0";

    String source_lat_main,source_long_main,dest_lat_main,dest_long_main;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
//
        Log.e("********************", "******************************");
        Log.e("********************", "Home feedback page 1 api call");
        Log.e("********************", "******************************");

        init();
        Intent i = getIntent();
        id = i.getStringExtra("id");
        String rate= i.getStringExtra("feedback_rating");
        String feedback_rating= i.getStringExtra("feedback_rating")+"f";
        String feedback_msg = i.getStringExtra("feedback_msg");

        //Snackbar.make(View, "Here's a Snackbar", Snackbar.LENGTH_LONG).setAction("Action", null).show();
        source_lat_main = i.getStringExtra("source_lat_main");
        source_long_main = i.getStringExtra("source_long_main");
        dest_lat_main = i.getStringExtra("dest_lat_main");
        dest_long_main= i.getStringExtra("dest_long_main");

        //Snackbar.make(back, "dddd", Snackbar.LENGTH_SHORT).show();
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();

            }
        });
        if(feedback_msg.equalsIgnoreCase("") && rate.equalsIgnoreCase(""))
        {

        }
        else
        {
            submit_btn.setVisibility(View.GONE);
            comment_txt.setFocusable(false);
            rating.setFocusable(false);
            rating.setRating(Float.parseFloat(feedback_rating));
            comment_txt.setText(feedback_msg);
            rating.setEnabled(false);
        }

        id_txt.setText("ID "+id);
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());
        if(status!= ConnectionResult.SUCCESS)
        {
            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        }
        else
        {
            SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            googleMap = fm.getMap();
            googleMap.setMyLocationEnabled(false);
            //googleMap.setOnMyLocationChangeListener(this);

            double latitude = 25.276987;
            double longitude = 55.296249;
            LatLng latLng = new LatLng(latitude, longitude);
            //googleMap.addMarker(new MarkerOptions().position(latLng));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(5));
            //locationTv.setText("Latitude:" + latitude + ", Longitude:" + longitude);

        }
       rating.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating,boolean fromUser)
            {
                // TODO Auto-generated method stub

                //Toast.makeText(getApplicationContext(),String.valueOf(rating), Toast.LENGTH_SHORT).show();
                 rating_value=String.valueOf(rating);
            }
        });
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cd.isConnectingToInternet())
                {
                    if(comment_txt.getText().toString().equalsIgnoreCase(""))
                    {
                        comment_txt.setError("Enter valid Comment");
                    }
                    else if(rating_value.equalsIgnoreCase("0.0"))
                    {
                       Toast.makeText(getApplicationContext(),"Select rating value",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(Activity_Feedback.this);
                        builder.setMessage("Are you sure want Give Feedback?");
                        // builder.setTitle("New Order");

                        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int which)
                            {
                                new GetJson_save().execute();

                            }
                        });

                        builder.setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener()
                                {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        dialog.cancel();
                                    }
                                });
                        builder.create().show();
                    }


                    }

                }


        });
        drowmappath();
    }
    public void init()
    {
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        sm = new SessionManager(Activity_Feedback.this);
        UF = new UserFunctions(Activity_Feedback.this);
        cd = new ConnectionDetector(Activity_Feedback.this);
        id_txt=(TextView)findViewById(R.id.id_txt);
        submit_btn=(TextView)findViewById(R.id.submit_btn);
        comment_txt=(EditText)findViewById(R.id.comments_edittext);
        rating=(RatingBar)findViewById(R.id.ratingBar);
        back = (TextView)findViewById(R.id.back);

    }
    private class GetJson_save extends AsyncTask<Void, Void, String> {
        //ProgressHUD mProgressHUD;
        //ProgressDialog pDialog = new ProgressDialog(Activity_Feedback.this);
        private ProgressDialog loading;

        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
            loading = new ProgressDialog(Activity_Feedback.this);
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
                prmsLogin.put("star_rating",rating_value);
                prmsLogin.put("feedback", comment_txt.getText().toString());
                prmsLogin.put("created_by",android_id.toString() );
                prmsLogin.put("created_host", android_id.toString());


                jsonArray.put(prmsLogin);
                prms.put("feedback", prmsLogin);
                Log.e("--------------------", "----------------------------------");
                Log.e("Homefeedback Post--", prms.toString());

            }
            catch (JSONException e)
            {

                e.printStackTrace();
            }

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
                }
                else
                    {
                    try
                    {
                        JSONObject jobj = new JSONObject(json_save);

                        Log.e("Homefeedback Get--", json_save.toString());
                        String status = jobj.getString("status");
                        String message = jobj.getString("message").toString();
                        Log.e("--------------------", "----------------------------------");
                        if (status.equalsIgnoreCase("1"))
                        {


                            UF.msg(message + "");

                            Intent i = new Intent(Activity_Feedback.this,MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            Constants.feedback_flow="Order";
                            startActivity(i);
                            overridePendingTransition(R.anim.left_in, R.anim.right_out);
                            finish();


                        } else {
                            UF.msg(message + "");
                        }
                    } catch (JSONException e) {

                        e.printStackTrace();
                    }
                }
            }

        }
    }
    public void drowmappath()
    {
        googleMap.clear();

        final LatLng lat_lng1= new LatLng(Double.parseDouble(source_lat_main),Double.parseDouble(source_long_main));
        final LatLng lat_lng2= new LatLng(Double.parseDouble(dest_lat_main),Double.parseDouble(dest_long_main));

        MarkerOptions marker1=null,marker2=null;

        marker1 = new MarkerOptions().position(lat_lng1).title("Source Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.blue_marker_map)).snippet("");
        //  marker2 = new MarkerOptions().position(lat_lng2).title("Destination Location").icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_CYAN))).snippet(destination_addr);
        marker2 = new MarkerOptions().position(lat_lng2).title("Destination Location").icon(BitmapDescriptorFactory.fromResource(R.mipmap.red_marker_map)).snippet("");
        googleMap.addMarker(marker1);
        googleMap.addMarker(marker2);
        // googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lat_lng2, 13));
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
    public class LinearLayoutManagerWithSmoothScroller extends LinearLayoutManager
    {

        public LinearLayoutManagerWithSmoothScroller(Context context) {
            super(context, VERTICAL, false);
        }

        public LinearLayoutManagerWithSmoothScroller(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        @Override
        public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state,
                                           int position)
        {
            RecyclerView.SmoothScroller smoothScroller = new TopSnappedSmoothScroller(recyclerView.getContext());
            smoothScroller.setTargetPosition(position);
            startSmoothScroll(smoothScroller);
        }

        private class TopSnappedSmoothScroller extends LinearSmoothScroller
        {
            public TopSnappedSmoothScroller(Context context)
            {
                super(context);
            }

            @Override
            public PointF computeScrollVectorForPosition(int targetPosition)
            {
                return LinearLayoutManagerWithSmoothScroller.this .computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected int getVerticalSnapPreference()
            {
                return SNAP_TO_START;
            }
        }
    }
}