package com.trukker.trukkershipperuae.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.model.Notification_method;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.TimeZone;

public class NotificationAdapter extends ArrayAdapter<Notification_method> {
    ArrayList<Notification_method> actorList;
    LayoutInflater vi;
    Context context;

    public NotificationAdapter(Context context, ArrayList<Notification_method> objects) {
        super(context, R.layout.list_notification, objects);
        this.context = context;
        this.vi = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.actorList = objects;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // convert view = design
        //View v = convertView;
        View rowView;
        ViewHolder vh;
        if (convertView == null) {

            rowView = vi.inflate(R.layout.list_notification, null);
            setViewHolder(rowView);
        } else {
            rowView = convertView;

        }
        vh = (ViewHolder) rowView.getTag();

        vh.title.setText(Html.fromHtml(actorList.get(position).gettitle()));
        vh.subtitle.setText(Html.fromHtml(actorList.get(position).getsubtitle()));
        vh.date.setText(actorList.get(position).getdate());



        return rowView;

    }

    static class ViewHolder {

        public TextView title, subtitle, date;
    }

    private void setViewHolder(View rowView) {
        ViewHolder vh = new ViewHolder();

        vh.title = (TextView) rowView.findViewById(R.id.title);
        vh.subtitle = (TextView) rowView.findViewById(R.id.subtitle);
        vh.date = (TextView) rowView.findViewById(R.id.date);
        rowView.setTag(vh);

    }
    private static String getLocalTime( int hr, int min )
    {
        String localTime = "";

        Calendar gmt = Calendar.getInstance();

        gmt.set( Calendar.HOUR, hr );
        gmt.set( Calendar.MINUTE, min );
        gmt.setTimeZone( TimeZone.getTimeZone( "GMT" ) );

        Calendar local = Calendar.getInstance();
        local.setTimeZone( TimeZone.getDefault() );
        local.setTime(gmt.getTime() );


        int hour = local.get( Calendar.HOUR );
        int minutes = local.get( Calendar.MINUTE );
        boolean am = local.get( Calendar.AM_PM ) == Calendar.AM;
        String str_hr = "";
        String str_min = "";
        String am_pm = "";

        if ( hour < 10 )
            str_hr = "0";
        if ( minutes < 10 )
            str_min = "0";
        if( am )
            am_pm = "AM";
        else
            am_pm = "PM";

        localTime = str_hr + hour + ":" + str_min + minutes + " " + am_pm ;
        return localTime;
    }

}