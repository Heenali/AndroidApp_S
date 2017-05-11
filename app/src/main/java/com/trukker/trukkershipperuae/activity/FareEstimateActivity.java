package com.trukker.trukkershipperuae.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;

public class FareEstimateActivity extends AppCompatActivity implements View.OnClickListener {
    View toolbar_header;
    ImageView back_click;
    TextView activtiy_title, fare_estimate_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fare_estimate);

        init();
    }

    private void init() {
        //toolbar
        toolbar_header = findViewById(R.id.toolbar_header);
        back_click = (ImageView) toolbar_header.findViewById(R.id.back_click);
        back_click.setImageResource(R.drawable.close_white);
        back_click.setOnClickListener(this);
        activtiy_title = (TextView) toolbar_header.findViewById(R.id.activtiy_title);
        activtiy_title.setText("Fare Estimate");

        fare_estimate_text = (TextView) findViewById(R.id.fare_estimate_text);
        fare_estimate_text.setMovementMethod(new ScrollingMovementMethod());
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
        FareEstimateActivity.this.overridePendingTransition(
                R.anim.slide_in, R.anim.slide_out);

    }
}
