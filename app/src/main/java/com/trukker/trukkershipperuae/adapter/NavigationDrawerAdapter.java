package com.trukker.trukkershipperuae.adapter;

/**
 * Created by Ravi on 29/07/15.
 */
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trukker.trukkershipperuae.R;
import com.trukker.trukkershipperuae.model.NavDrawerItem;

import java.util.Collections;
import java.util.List;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.MyViewHolder> {
    List<NavDrawerItem> data = Collections.emptyList();
    private LayoutInflater inflater;
    private Context context;

    public NavigationDrawerAdapter(Context context, List<NavDrawerItem> data) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    public void delete(int position) {
        data.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.nav_drawer_row, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        NavDrawerItem current = data.get(position);
        holder.title.setText(current.getTitle());
        if(current.getTitle().toString().equalsIgnoreCase("Dashboard"))
        {
            holder.menuimage.setBackgroundResource(R.drawable.menu_dashboard);
        }
        if(current.getTitle().toString().equalsIgnoreCase("My Orders"))
        {
            holder.menuimage.setBackgroundResource(R.drawable.menu_myorder);
        }
        if(current.getTitle().toString().equalsIgnoreCase("My Draft Orders"))
        {
            holder.menuimage.setBackgroundResource(R.drawable.menu_myorder);
        }
        if(current.getTitle().toString().equalsIgnoreCase("Payment Details"))
        {
            holder.menuimage.setBackgroundResource(R.drawable.menu_payment);
        }
        if(current.getTitle().toString().equalsIgnoreCase("Change Password"))
        {
            holder.menuimage.setBackgroundResource(R.drawable.menu2_chnagepass);
        }

        if(current.getTitle().toString().equalsIgnoreCase("Notification"))
        {
            holder.menuimage.setBackgroundResource(R.drawable.menu_notification);
        }
        if(current.getTitle().toString().equalsIgnoreCase("Contact Us"))
        {
            holder.menuimage.setBackgroundResource(R.drawable.menu2_contactus);
        }
        if(current.getTitle().toString().equalsIgnoreCase("Login") || current.getTitle().toString().equalsIgnoreCase("Logout"))
        {
            holder.menuimage.setBackgroundResource(R.drawable.menu_login);
        }




    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView menuimage;

        public MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            menuimage = (ImageView) itemView.findViewById(R.id.menu_icon);
        }
    }
}
