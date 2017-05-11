package com.trukker.trukkershipperuae.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trukker.trukkershipperuae.R;

public class OngoingGoodsOrderTrack extends AppCompatActivity implements View.OnClickListener {

    GoogleMap mMap;
    Marker my_Marker;

    LinearLayout layout_toview, bottom_layout, layout_to_visible;
    AppCompatButton cancel_order_btn, share_status_btn;
    ImageView call_driver;
    RelativeLayout botto_layout_header;

    View toolbar_header;
    ImageView back_click;
    TextView activtiy_title,detail_tv;
    int width, height, height2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ongoing_goods_order_track);

        init();

        loadMapView();
    }

    private void loadMapView() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.ongoin_tracking_map);
        // Getting reference to the Google Map
        mMap = mapFragment.getMap();
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(false);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(23.0146966, 72.5036792), 15));
        my_Marker = mMap.addMarker(new MarkerOptions().position(new LatLng(23.0146966, 72.5036792)).title("My Location").icon(BitmapDescriptorFactory.fromResource(R.drawable.truck_track)));

    }

    private void init() {
        //toolbar
        toolbar_header = findViewById(R.id.toolbar_header);
        back_click = (ImageView) toolbar_header.findViewById(R.id.back_click);
        back_click.setOnClickListener(this);
        activtiy_title = (TextView) toolbar_header.findViewById(R.id.activtiy_title);
        activtiy_title.setText("8905467");

        //main layout

        //dialog layout
        layout_toview = (LinearLayout) findViewById(R.id.layout_toview);
        layout_toview.setVisibility(View.GONE);
        bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);

        botto_layout_header = (RelativeLayout) findViewById(R.id.botto_layout_header);
        botto_layout_header.setOnClickListener(this);

        share_status_btn = (AppCompatButton) findViewById(R.id.share_status_btn);
        share_status_btn.setOnClickListener(this);
        cancel_order_btn = (AppCompatButton) findViewById(R.id.cancel_order_btn);
        cancel_order_btn.setOnClickListener(this);

        detail_tv= (TextView) findViewById(R.id.detail_tv);
        detail_tv.setMovementMethod(new ScrollingMovementMethod());

        layout_to_visible = (LinearLayout) findViewById(R.id.layout_to_visible);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_click:
                onBackPressed();
                break;

            case R.id.botto_layout_header:
                animationDialog();
                break;

            case R.id.share_status_btn:

                final Dialog dialog = new Dialog(OngoingGoodsOrderTrack.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.share_status_dialog);
                dialog.show();
                break;

            case R.id.cancel_order_btn:

                Intent i1 = new Intent(getApplicationContext(), CancelPostOrderActivity.class);
                startActivity(i1);
                OngoingGoodsOrderTrack.this.overridePendingTransition(
                        R.anim.slide_enter, R.anim.slide_exit);
                break;

            default:
                break;
        }
    }

    private void animationDialog() {
        ViewTreeObserver vto = layout_to_visible.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                layout_to_visible.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                width = layout_to_visible.getMeasuredWidth();
                height = layout_to_visible.getMeasuredHeight();
                height2 = bottom_layout.getMeasuredHeight();
            }
        });
        if (layout_toview.getVisibility() == View.VISIBLE) {
            /*Animation anm2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down2);
            bottom_layout.startAnimation(anm2);*/

            layout_toview.setVisibility(View.GONE);
        } else {
            /*Animation anm = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up2);
            bottom_layout.startAnimation(anm);*/

            bottom_layout.bringToFront();
            layout_toview.setVisibility(View.VISIBLE);
            bottom_layout.setClickable(true);
            layout_toview.setClickable(true);

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        OngoingGoodsOrderTrack.this.overridePendingTransition(
                R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }
}
