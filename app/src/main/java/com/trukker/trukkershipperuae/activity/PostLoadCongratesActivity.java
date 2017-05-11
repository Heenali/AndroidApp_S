package com.trukker.trukkershipperuae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;

public class PostLoadCongratesActivity extends AppCompatActivity implements View.OnClickListener {
    View toolbar_header;
    ImageView back_click;
    TextView activtiy_title;

    LinearLayout driver_detail_layout;
    String from;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_load_congrates);

        init();

        try {
            Intent i = getIntent();
            from = i.getStringExtra("from");
            if (from.equalsIgnoreCase("BookNow")) {
                driver_detail_layout.setVisibility(View.VISIBLE);
            }
            if (from.equalsIgnoreCase("BookLater")) {
                driver_detail_layout.setVisibility(View.GONE);
            } else {

            }
        } catch (Exception e) {

        }
    }

    private void init() {
        //toolbar
        toolbar_header = findViewById(R.id.toolbar_header);
        back_click = (ImageView) toolbar_header.findViewById(R.id.back_click);
        back_click.setVisibility(View.GONE);
        activtiy_title = (TextView) toolbar_header.findViewById(R.id.activtiy_title);
        activtiy_title.setText("Congratulations");

        driver_detail_layout = (LinearLayout) findViewById(R.id.driver_detail_layout);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_click:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        PostLoadCongratesActivity.this.overridePendingTransition(
                R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }
}
