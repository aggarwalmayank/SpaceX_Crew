package com.appsaga.spacex.Adapter;


//import android.support.v7.widget.RecyclerView;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.appsaga.spacex.R;
import com.appsaga.spacex.WebModel.Crew;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder>{
    private ArrayList<Crew> listdata;
    private Context context;
    // RecyclerView recyclerView;
    public MyListAdapter(ArrayList<Crew>  listdata, Context context) {
        this.listdata = listdata;
        this.context=context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Crew myListData = listdata.get(position);
        holder.name.setText("Name: "+listdata.get(position).getName());
        holder.agency.setText("Agency: "+listdata.get(position).getAgency());
        holder.status.setText("Status: "+listdata.get(position).getStatus());
        RequestOptions myOptions = new RequestOptions()
                .fitCenter() // or centerCrop
                .override(200,200);
        Glide.with(context).asBitmap().apply(myOptions).load(listdata.get(position).getImage()).placeholder(R.drawable.placeholder).into(holder.picture);
        holder.wiki.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = listdata.get(position).getWikipedia();
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView picture;
        public TextView name,agency,status,wiki;
        public RelativeLayout relativeLayout;
        public ViewHolder(View itemView) {
            super(itemView);
            this.picture = (ImageView) itemView.findViewById(R.id.image);
            this.name = (TextView) itemView.findViewById(R.id.name);
            this.agency = (TextView) itemView.findViewById(R.id.agency);
            this.status = (TextView) itemView.findViewById(R.id.status);
            this.wiki = (TextView) itemView.findViewById(R.id.link);
            this.wiki.setClickable(true);
            this.wiki.setMovementMethod(LinkMovementMethod.getInstance());
            relativeLayout = (RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}