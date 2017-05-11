package com.trukker.trukkershipperuae.adapter;

import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.model.Document_method;

import java.util.ArrayList;

public class DocumentAdapter extends ArrayAdapter<Document_method> {
	ArrayList<Document_method> actorList;
	LayoutInflater vi;
	Context context;

	public DocumentAdapter(Context context, ArrayList<Document_method> objects) {
		super(context,  R.layout.dialog_list_hometype, objects);
		this.context = context;
		this.vi = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//Resource = resource;
		this.actorList = objects;
	}
 
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// convert view = design
		//View v = convertView;
		View rowView;
	    ViewHolder vh;
		if (convertView  == null) {
			//holder = new ViewHolder();
			rowView = vi.inflate(R.layout.dialog_list_hometype, null);
			setViewHolder(rowView);
		} else 
		{
			rowView = convertView;
			//holder = (ViewHolder) v.getTag();
		}
		vh = (ViewHolder)rowView.getTag();
		Log.i("dddd", "ddd");
		String s=actorList.get(position).getName().toString();
		Log.i("dddd", s);
		vh.Name.setText(Html.fromHtml(actorList.get(position).getName().trim()));
	    
		return rowView;

	}

	static class ViewHolder
	{

		public TextView Name;

	}
	private void setViewHolder(View rowView) 
	    	{
	    			ViewHolder vh = new ViewHolder();

					vh.Name = (TextView) rowView.findViewById(R.id.text_hometype);
					rowView.setTag(vh);
			
	    	}


}