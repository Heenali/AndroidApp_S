package com.trukker.trukkershipperuae.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.trukker.trukkershipperuae.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class BookLaterActivity extends AppCompatActivity implements View.OnClickListener {

    View toolbar_header;
    ImageView back_click;
    TextView activtiy_title;
    EditText sel_date_booklater, sel_time_booklater;

    Calendar cal;
    DatePickerDialog.OnDateSetListener datePicker, datePicker1;
    AppCompatButton continue_booklater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_later);

        init();
    }

    private void init() {

        //toolbar
        toolbar_header = findViewById(R.id.toolbar_header);
        back_click = (ImageView) toolbar_header.findViewById(R.id.back_click);
        back_click.setOnClickListener(this);
        activtiy_title = (TextView) toolbar_header.findViewById(R.id.activtiy_title);
        activtiy_title.setText("Book Later");

        sel_date_booklater = (EditText) findViewById(R.id.sel_date_booklater);
        sel_date_booklater.setOnClickListener(this);
        sel_time_booklater = (EditText) findViewById(R.id.sel_time_booklater);
        sel_time_booklater.setOnClickListener(this);

        continue_booklater= (AppCompatButton) findViewById(R.id.continue_booklater);
        continue_booklater.setOnClickListener(this);

        createDatePicker();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_click:
                onBackPressed();
                break;
            case R.id.sel_date_booklater:
                DatePicker();
                break;
            case R.id.sel_time_booklater:
                TimePicker();
                break;
            case R.id.continue_booklater:
                Intent i1 = new Intent(getApplicationContext(), PostLoadCongratesActivity.class);
                i1.putExtra("from","BookLater");
                startActivity(i1);

                BookLaterActivity.this.overridePendingTransition(
                        R.anim.anim_slide_in_left, R.anim.anim_slide_out_left);
                break;
            default:
                break;
        }
    }

    private void TimePicker() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        TimePickerDialog mTimePicker;
        mTimePicker = new TimePickerDialog(this, R.style.DialogTheme, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                //strShippingHour=String.valueOf(selectedHour);

                sel_time_booklater.setText(pad(selectedHour) + ":" + pad(selectedMinute));
            }
        }, hour, minute, true);//Yes 24 hour time
        mTimePicker.setTitle("Select Time");
        mTimePicker.show();
    }

    private void DatePicker() {
        //date get code
        InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(sel_date_booklater.getWindowToken(), 0);
        DatePickerDialog datePicker1 = new DatePickerDialog(this, R.style.DialogTheme, datePicker, cal
                .get(Calendar.YEAR), cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH));

        datePicker1.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker1.show();
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

                //tv_dig_YY.setText(myCalendarmedFrom.get(Calendar.YEAR) + "");
                String selected_year = String.valueOf(cal.get(Calendar.YEAR) + "");
                String month = String.valueOf(cal.get(Calendar.MONTH) + 1);

                String day = String.valueOf(cal
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
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                String s = sdf.format(cal.getTime());
                sel_date_booklater.setText(s);
            }

        };
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BookLaterActivity.this.overridePendingTransition(R.anim.anim_slide_in_right, R.anim.anim_slide_out_right);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
