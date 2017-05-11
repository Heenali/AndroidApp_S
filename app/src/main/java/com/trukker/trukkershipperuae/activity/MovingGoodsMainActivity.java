package com.trukker.trukkershipperuae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.helper.ConnectionDetector;
import com.trukker.trukkershipperuae.helper.Constants;
import com.trukker.trukkershipperuae.helper.GPSTracker;
import com.trukker.trukkershipperuae.helper.UserFunctions;

public class MovingGoodsMainActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
    GoogleMap mMap;
    Marker my_Marker;

    TextView activtiy_title, bottom_lay_title;
    ImageView back_click;
    LinearLayout layout_moredetail, bottom_lay_click, get_fare_estimate;
    CheckBox ten_axcel_ck, open_body_ck, light_weight_ck;
    boolean check1 = false, check2 = false, check3 = false;
    AppCompatButton book_later_btn, book_nw_btn;
    EditText input_dest_loc, input_my_loc;
    ImageView img_tofrom;
    CardView bottom_layout;

    UserFunctions UF;
    ConnectionDetector cd;
    GPSTracker gps;

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";
    private static final String API_KEY = "AIzaSyD_Hvp-mAjMMMS_OgPFEIxqX-AsffaYK0E";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moing_goods_main);

        init();
        loadMapLocation();

        CurrentLocation();
    }

    private void CurrentLocation() {

        //current location
        Constants.LatLong_source = new LatLng(gps.getLatitude(), gps.getLongitude());

        //by autocomplete location
        try {
            if (Constants.LatLong_source == null) {
                input_my_loc.setText("");
            } else {
                input_my_loc.setText(Constants.source_add);
            }

            if (Constants.LatLong_destination == null) {
                input_dest_loc.setText("");
            } else {
                input_dest_loc.setText(Constants.destination_add);
            }
        } catch (Exception e) {

        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (Constants.LatLong_source == null) {
                input_my_loc.setText("");
            } else {
                input_my_loc.setText(Constants.source_add);
            }

            if (Constants.LatLong_destination == null) {
                input_dest_loc.setText("");
            } else {
                input_dest_loc.setText(Constants.destination_add);
            }
        } catch (Exception e) {

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        MovingGoodsMainActivity.this.overridePendingTransition(
                R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }

    private void loadMapLocation() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.tracking_map);
        // Getting reference to the Google Map
        mMap = mapFragment.getMap();
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        if (cd.isConnectingToInternet()) {
            //for Gps Setting
            if (gps.canGetLocation()) {
                gps.getLocation();

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(gps.getLatitude(), gps.getLongitude()), 15));
                my_Marker = mMap.addMarker(new MarkerOptions().position(new LatLng(gps.getLatitude(), gps.getLongitude())).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_shadow)));

            } else {
                gps.showSettingsAlert();
                if (gps.canGetLocation()) {
                    gps.getLocation();
                }
            }
        } else {
            UF.msg("Check your internet connection");
        }

    }

    private void init() {

        gps = new GPSTracker(MovingGoodsMainActivity.this);
        cd = new ConnectionDetector(MovingGoodsMainActivity.this);
        UF = new UserFunctions(MovingGoodsMainActivity.this);
        //toolbar
        activtiy_title = (TextView) findViewById(R.id.activtiy_title);
        activtiy_title.setText("Moving Goods");
        back_click = (ImageView) findViewById(R.id.back_click);
        back_click.setOnClickListener(this);

        //bottom drawer
        bottom_layout = (CardView) findViewById(R.id.bottom_layout);
        layout_moredetail = (LinearLayout) findViewById(R.id.layout_moredetail);
        bottom_lay_click = (LinearLayout) findViewById(R.id.bottom_lay_click);
        bottom_lay_click.setOnClickListener(this);
        bottom_lay_title = (TextView) findViewById(R.id.bottom_lay_title);

        ten_axcel_ck = (CheckBox) findViewById(R.id.ten_axcel_ck);
        ten_axcel_ck.setOnCheckedChangeListener(this);
        open_body_ck = (CheckBox) findViewById(R.id.open_body_ck);
        open_body_ck.setOnCheckedChangeListener(this);
        light_weight_ck = (CheckBox) findViewById(R.id.light_weight_ck);
        light_weight_ck.setOnCheckedChangeListener(this);
        book_later_btn = (AppCompatButton) findViewById(R.id.book_later_btn);
        book_later_btn.setOnClickListener(this);
        book_nw_btn = (AppCompatButton) findViewById(R.id.book_nw_btn);
        book_nw_btn.setOnClickListener(this);
        get_fare_estimate = (LinearLayout) findViewById(R.id.get_fare_estimate);
        get_fare_estimate.setOnClickListener(this);

        img_tofrom = (ImageView) findViewById(R.id.img_tofrom);
        img_tofrom.setOnClickListener(this);

        //main activity
        input_dest_loc = (EditText) findViewById(R.id.input_dest_loc);
        input_dest_loc.setOnClickListener(this);
        input_dest_loc.setEnabled(true);
        input_my_loc = (EditText) findViewById(R.id.input_my_loc);
        input_my_loc.setOnClickListener(this);
        input_my_loc.setEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_click:
                onBackPressed();
                break;
            case R.id.book_later_btn:
                Intent i1 = new Intent(getApplicationContext(), BookLaterActivity.class);
                startActivity(i1);
                MovingGoodsMainActivity.this.overridePendingTransition(
                        R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.book_nw_btn:
                Intent i2 = new Intent(getApplicationContext(), BookNowActivity.class);
                startActivity(i2);
                MovingGoodsMainActivity.this.overridePendingTransition(
                        R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            case R.id.get_fare_estimate:
                Intent i3 = new Intent(getApplicationContext(), FareEstimateActivity.class);
                startActivity(i3);
                MovingGoodsMainActivity.this.overridePendingTransition(
                        R.anim.slide_enter, R.anim.slide_exit);
                break;

            case R.id.input_dest_loc:
                Intent i4 = new Intent(getApplicationContext(), PlaceAutocompleteActivity.class);
                i4.putExtra("click", "2");
                startActivity(i4);
                break;
            case R.id.input_my_loc:
                Intent i5 = new Intent(getApplicationContext(), PlaceAutocompleteActivity.class);
                i5.putExtra("click", "1");
                startActivity(i5);
                break;
            default:
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.ten_axcel_ck:

                if (isChecked == false) {
                    check1 = false;
                } else {
                    //check
                    open_body_ck.setChecked(false);
                    light_weight_ck.setChecked(false);
                    check1 = true;

                }
                hideLayout();
                break;
            case R.id.open_body_ck:
                if (isChecked == false) {
                    check2 = false;
                } else {
                    //check
                    ten_axcel_ck.setChecked(false);
                    light_weight_ck.setChecked(false);
                    check2 = true;

                }
                hideLayout();
                break;
            case R.id.light_weight_ck:
                if (isChecked == false) {
                    check3 = false;
                } else {
                    //check
                    ten_axcel_ck.setChecked(false);
                    open_body_ck.setChecked(false);
                    check3 = true;

                }
                hideLayout();
                // Toast.makeText(getApplicationContext(),check1+","+check2+","+check3,1).show();
                break;
            default:
                break;

        }
    }

    private void hideLayout() {
        if (layout_moredetail.getVisibility() == View.VISIBLE && (check1 == false && check2 == false && check3 == false)) {

          /*  Animation anm2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down2);
            bottom_layout.startAnimation(anm2);*/
            layout_moredetail.setVisibility(View.GONE);
            bottom_lay_title.setVisibility(View.VISIBLE);

        } else if (layout_moredetail.getVisibility() == View.GONE) {
           /* Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
            bottom_layout.startAnimation(anm);*/
            bottom_layout.bringToFront();
            layout_moredetail.setVisibility(View.VISIBLE);
            bottom_lay_title.setVisibility(View.GONE);
            bottom_layout.setClickable(true);
            layout_moredetail.setClickable(true);

        } else {

        }
    }

}
