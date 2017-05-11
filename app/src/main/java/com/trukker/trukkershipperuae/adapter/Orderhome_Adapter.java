
package com.trukker.trukkershipperuae.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.model.Order_method;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Orderhome_Adapter extends ArrayAdapter<Order_method>
{
	ArrayList<Order_method> actorList;
	LayoutInflater vi;
	Context context;
	Activity a;

	public Orderhome_Adapter(Context context, ArrayList<Order_method> objects)
	{
		super(context,  R.layout.list_movehome_order, objects);
		this.context = context;
		this.vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.actorList = objects;
	}


	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// convert view = design
		//View v = convertView;
		View rowView;
		ViewHolder vh;
		if (convertView  == null)
		{

			rowView = vi.inflate(R.layout.list_movehome_order, null);
			setViewHolder(rowView);
		}
		else
		{
			rowView = convertView;

		}
		vh = (ViewHolder)rowView.getTag();

		vh.SizeTypeDesc.setText(Html.fromHtml("Home "+actorList.get(position).getSizeTypeDesc()));
		vh.load_inquiry_no.setText(Html.fromHtml(actorList.get(position).getload_inquiry_no()));

		try
		{
			String date=actorList.get(position).getload_inquiry_shipping_date();
			SimpleDateFormat sourceFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss a");
			SimpleDateFormat DesiredFormat  = new SimpleDateFormat("dd MMM,yyyy");

			Date date1 = sourceFormat.parse(date);
			String formattedDate = DesiredFormat.format(date1);

			String time=actorList.get(position).getextra2();
			SimpleDateFormat sourceFormattime = new SimpleDateFormat("HH:mm:ss");
			SimpleDateFormat DesiredFormattime  = new SimpleDateFormat("HH:mm");

			Date time1 = sourceFormattime.parse(time);
			String formattedtime = DesiredFormattime.format(time1);
			//Log.e("ssssssss", formattedtime);
			vh.load_inquiry_shipping_date.setText(formattedDate+" "+formattedtime);

		}
		catch (ParseException e)
		{
			e.printStackTrace();
		}
		vh.cbmlink.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(actorList.get(position).getextra5().equalsIgnoreCase(""))
				{

				}
				else
				{
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent = new Intent(Intent.ACTION_VIEW, Uri.parse(actorList.get(position).getextra5()));
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					context.getApplicationContext().startActivity(intent);
				}

				//Toast.makeText(getContext(), "Hello", Toast.LENGTH_LONG).show();

			}
		});



		vh.Total_cost.setText(Html.fromHtml("AED "+actorList.get(position).getTotal_cost()));
		vh.inquiry_source_addr.setText(Html.fromHtml(actorList.get(position).getinquiry_source_addr()));
		vh.inquiry_destination_addr.setText(Html.fromHtml(actorList.get(position).getinquiry_destination_addr()));

		vh.NoOfTruck.setText(Html.fromHtml(actorList.get(position).getNoOfTruck()));
		vh.NoOfLabour.setText(Html.fromHtml(actorList.get(position).getNoOfLabour()));
		vh.NoOfHandiman.setText(Html.fromHtml(actorList.get(position).getNoOfHandiman()));
		vh.TotalDistance.setText(Html.fromHtml(actorList.get(position).getextra1()+" KM"));

		String status=actorList.get(position).getstatus();

		if(status.equalsIgnoreCase("02"))
		{
			vh.reschule_layout.setVisibility(View.GONE);
			vh.cancle_layout.setVisibility(View.GONE);
			vh.success_icon.setVisibility(View.GONE);
			vh.progress_status_icon.setVisibility(View.VISIBLE);
			vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));
		}
		else if(status.equalsIgnoreCase("05"))
		{
			vh.reschule_layout.setVisibility(View.GONE);
			vh.cancle_layout.setVisibility(View.GONE);
			vh.progress_status_icon.setVisibility(View.VISIBLE);
			vh.success_icon.setVisibility(View.GONE);
			vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_ongoing));
		}
		else if(status.equalsIgnoreCase("06"))
		{
			vh.reschule_layout.setVisibility(View.GONE);
			vh.cancle_layout.setVisibility(View.GONE);
			vh.success_icon.setVisibility(View.GONE);
			vh.progress_status_icon.setVisibility(View.VISIBLE);
			//vh.success_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.success));
			vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_ongoing));
		}
		else if(status.equalsIgnoreCase("07"))
		{
			vh.reschule_layout.setVisibility(View.GONE);
			vh.cancle_layout.setVisibility(View.GONE);
			vh.success_icon.setVisibility(View.GONE);
			vh.progress_status_icon.setVisibility(View.VISIBLE);
			vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_ongoing));
		}
		else if(status.equalsIgnoreCase("08"))
		{
			vh.reschule_layout.setVisibility(View.GONE);
			vh.cancle_layout.setVisibility(View.GONE);
			vh.success_icon.setVisibility(View.GONE);
			vh.progress_status_icon.setVisibility(View.VISIBLE);
			vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_unloading));
		}
		else if(status.equalsIgnoreCase("25"))
		{
			vh.reschule_layout.setVisibility(View.GONE);
			vh.success_icon.setVisibility(View.GONE);
			vh.cancle_layout.setVisibility(View.VISIBLE);
			vh.progress_status_icon.setVisibility(View.GONE);

			//vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.movehome_area));
		}
		else if(status.equalsIgnoreCase("45"))
		{
			vh.reschule_layout.setVisibility(View.GONE);
			vh.cancle_layout.setVisibility(View.GONE);
			vh.progress_status_icon.setVisibility(View.VISIBLE);
			vh.success_icon.setVisibility(View.VISIBLE);
			vh.success_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_success));
			vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_completed));
		}
		else if(status.equalsIgnoreCase("26"))
		{
			vh.reschule_layout.setVisibility(View.VISIBLE);
			vh.cancle_layout.setVisibility(View.GONE);
			vh.success_icon.setVisibility(View.GONE);
			vh.progress_status_icon.setVisibility(View.VISIBLE);
			vh.progress_status_icon.setBackgroundDrawable(getContext().getResources().getDrawable(R.drawable.order_upcoming));
		}

		return rowView;

	}

	static class ViewHolder
	{

		public TextView SizeTypeDesc,load_inquiry_no,load_inquiry_shipping_date,Total_cost,inquiry_source_addr,inquiry_destination_addr,
				NoOfTruck,NoOfLabour,NoOfHandiman,TotalDistance,cbmlink;
		ImageView progress_status_icon,success_icon;
		TextView cancle_layout;
		LinearLayout reschule_layout;

	}
	private void setViewHolder(View rowView)
	{
		ViewHolder vh = new ViewHolder();

		vh.SizeTypeDesc = (TextView) rowView.findViewById(R.id.txt_hometype);
		vh.load_inquiry_no = (TextView) rowView.findViewById(R.id.id_txt);
		vh.load_inquiry_shipping_date = (TextView) rowView.findViewById(R.id.datetime_txt);

		vh.Total_cost = (TextView) rowView.findViewById(R.id.price_txt);
		vh.inquiry_source_addr = (TextView) rowView.findViewById(R.id.sourceadd_txt);
		vh.inquiry_destination_addr = (TextView) rowView.findViewById(R.id.destinationadd_txt);

		vh.NoOfTruck= (TextView) rowView.findViewById(R.id.truck_text);
		vh.NoOfLabour = (TextView) rowView.findViewById(R.id.helper_txt);
		vh.NoOfHandiman = (TextView) rowView.findViewById(R.id.installer_txt);
		vh.TotalDistance = (TextView) rowView.findViewById(R.id.km_txt);
		vh.progress_status_icon = (ImageView) rowView.findViewById(R.id.progress_status_icon );
		vh.success_icon= (ImageView) rowView.findViewById(R.id.success_icon );
		vh.cbmlink= (TextView) rowView.findViewById(R.id.txt_cbmlink);
		vh.cancle_layout= (TextView) rowView.findViewById(R.id.cancle_button);
		vh.reschule_layout= (LinearLayout) rowView.findViewById(R.id.reschule_layout);
		rowView.setTag(vh);

	}


}

