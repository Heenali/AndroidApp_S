package com.trukker.trukkershipperuae.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;

public class BookNowActivity extends AppCompatActivity implements View.OnClickListener {
    View toolbar_header;
    ImageView back_click;
    TextView activtiy_title;

    AppCompatButton continue_bookNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_now);

        init();
    }

    private void init() {

        //toolbar
        toolbar_header = findViewById(R.id.toolbar_header);
        back_click = (ImageView) toolbar_header.findViewById(R.id.back_click);
        back_click.setOnClickListener(this);
        activtiy_title = (TextView) toolbar_header.findViewById(R.id.activtiy_title);
        activtiy_title.setText("Book Later");

        continue_bookNow = (AppCompatButton) findViewById(R.id.continue_bookNow);
        continue_bookNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_click:
                onBackPressed();
                break;

            case R.id.continue_bookNow:
                Intent i1 = new Intent(getApplicationContext(), PostLoadCongratesActivity.class);
                i1.putExtra("from", "BookNow");
                startActivity(i1);

                BookNowActivity.this.overridePendingTransition(
                        R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BookNowActivity.this.overridePendingTransition(
                R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);

    }
}
