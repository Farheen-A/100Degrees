package com.degree.college.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.degree.college.Pojo.Event;
import com.degree.college.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SportsAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    ArrayList<Event> Sportlst;
    String event_date_format, eve_month, e_month, new_date;

    public SportsAdapter(Context context, int layout, ArrayList<Event> sportlst) {
        this.context = context;
        this.layout = layout;
        Sportlst = sportlst;
    }

    @Override
    public int getCount() {
        return Sportlst.size();
    }

    @Override
    public Object getItem(int position) {
        return Sportlst.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Viewholder {
        ImageView imageView;
        TextView name, date, time, location, sportstype;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        SportsAdapter.Viewholder holder = new SportsAdapter.Viewholder();
        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(layout, null);
            holder.imageView = (ImageView) row.findViewById(R.id.eventrowImg);
            holder.name = (TextView) row.findViewById(R.id.eveName);
            holder.date = (TextView) row.findViewById(R.id.evedate);
            holder.time = (TextView) row.findViewById(R.id.timeeve);
            holder.location = (TextView) row.findViewById(R.id.evelocation);
            holder.sportstype = (TextView) row.findViewById(R.id.sportsnam);
            row.setTag(holder);
        } else {
            holder = (SportsAdapter.Viewholder) row.getTag();
        }
        Event sportspojo = Sportlst.get(position);
        Picasso.with(context).load(sportspojo.getBannerImg()).into(holder.imageView);
        holder.name.setText(sportspojo.getNam());

        event_date_format=Cal_date(sportspojo.getDat());
        holder.date.setText(event_date_format);
        holder.time.setText(sportspojo.getTim()+ "-" + sportspojo.getTime_end());
        holder.location.setText(sportspojo.getPlace());
        holder.sportstype.setText(sportspojo.getSportsType());
        return row;
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
        new_date = e_month +"\n" + date.substring(3, 5);
        Log.d("Testing", dat);
        return new_date;
    }
}
