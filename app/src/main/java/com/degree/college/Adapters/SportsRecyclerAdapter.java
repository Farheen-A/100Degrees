package com.degree.college.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.degree.college.OnItemClickInterface;
import com.degree.college.Pojo.Event;
import com.degree.college.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by USER on 26-03-2018.
 */

public class SportsRecyclerAdapter extends RecyclerView.Adapter<SportsRecyclerAdapter.MyViewHolder> {
    private OnItemClickInterface onItemClickInterface;
    int layout;
    ArrayList<Event> sportsList;
    private Context context;
    String event_date_format, eve_month, e_month, new_date, Banner;
    public SportsRecyclerAdapter(Context context, int layout, ArrayList<Event> sportsList, OnItemClickInterface onItemClickInterface) {
        this.onItemClickInterface = onItemClickInterface;
        this.layout = layout;
        this.sportsList = sportsList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.sports_row, parent, false);
        return new SportsRecyclerAdapter.MyViewHolder(itemView, onItemClickInterface);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Event sportspojo = sportsList.get(position);
        Picasso.with(context).load(sportspojo.getBannerImg()).into(holder.imageView);
        holder.name.setText(sportspojo.getNam());

        event_date_format=Cal_date(sportspojo.getDat());
        holder.date.setText(event_date_format);
        holder.time.setText(sportspojo.getTim()+ "-" + sportspojo.getTime_end());
        holder.location.setText(sportspojo.getPlace());
        holder.sportstype.setText(sportspojo.getSportsType());
    }

    @Override
    public int getItemCount() {
        return sportsList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView name, date, time, location, sportstype;
        private OnItemClickInterface onItemClickInterface;

        public MyViewHolder(View itemView, OnItemClickInterface onInterface) {
            super(itemView);
            onItemClickInterface=onInterface;
            imageView = (ImageView) itemView.findViewById(R.id.eventrowImg);
            name = (TextView) itemView.findViewById(R.id.eveName);
            date = (TextView) itemView.findViewById(R.id.evedate);
            time = (TextView) itemView.findViewById(R.id.timeeve);
            location = (TextView) itemView.findViewById(R.id.evelocation);
            sportstype = (TextView) itemView.findViewById(R.id.sportsnam);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemClickInterface.setonItemClickListener(v,getAdapterPosition());

        }
    }
    public String Cal_date(String date) {
        String dat = "";
        eve_month = date.substring(0, 2);
        Log.d("event_date_format", date);
        switch (eve_month) {
            case "01":
                e_month = "Jan";
                break;
            case "02":
                e_month = "Feb";
                break;
            case "03":
                e_month = "Mar";
                break;
            case "04":
                e_month = "Apr";
                break;
            case "05":
                e_month = "May";
                break;
            case "06":
                e_month = "June";
                break;
            case "07":
                e_month = "Jul";
                break;
            case "08":
                e_month = "Aug";
                break;
            case "09":
                e_month = "Sep";
                break;
            case "10":
                e_month = "Oct";
                break;
            case "11":
                e_month = "Nov";
                break;
            case "12":
                e_month = "Dec";
                break;
            default:
                Log.d("month", "wrong");
        }
        new_date = e_month + " " + date.substring(3, 5);
        Log.d("Testing", dat);
        return new_date;
    }

}
