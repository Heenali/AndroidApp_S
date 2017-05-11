package com.trukker.trukkershipperuae.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;

public class CancelPostOrderActivity extends AppCompatActivity {

    View toolbar_header;
    ImageView back_click;
    TextView activtiy_title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_post_order);

        init();
    }

    private void init() {
        //toolbar
        toolbar_header = findViewById(R.id.toolbar_header);
        back_click = (ImageView) toolbar_header.findViewById(R.id.back_click);
        back_click.setImageResource(R.drawable.close_white);
        back_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        activtiy_title = (TextView) toolbar_header.findViewById(R.id.activtiy_title);
        activtiy_title.setText("Cancel Order");

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        CancelPostOrderActivity.this.overridePendingTransition(
                R.anim.slide_in, R.anim.slide_out);

    }
}
