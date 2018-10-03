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

/**
 * Created by USER on 08-03-2018.
 */

public class EventAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Event> list;
    String event_date_format,eve_month,e_month,new_date,Banner;
    public EventAdapter(Context context, int event_row, ArrayList<Event> list) {
        this.context = context;
        this.layout = event_row;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private class Viewholder {
        ImageView imageView, orgImage;
        TextView namerow, time_row, date_row, location_row, org_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        EventAdapter.Viewholder viewholder = new EventAdapter.Viewholder();
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout, null);
            viewholder.imageView = (ImageView) view.findViewById(R.id.eventrowImg);
            viewholder.namerow = (TextView) view.findViewById(R.id.eveName);
            viewholder.time_row = (TextView) view.findViewById(R.id.timeeve);
            viewholder.date_row = (TextView) view.findViewById(R.id.evedate);
            viewholder.location_row = (TextView) view.findViewById(R.id.evelocation);
            view.setTag(viewholder);
        }
        else {
            viewholder = (EventAdapter.Viewholder) view.getTag();
        }
            Event eventpojo = list.get(position);
            Banner = eventpojo.getBannerImg();
            Picasso.with(context).load(Banner).into(viewholder.imageView);
            viewholder.namerow.setText(eventpojo.getNam());
            viewholder.time_row.setText(eventpojo.getTim() + "-" + eventpojo.getTime_end());
            event_date_format = eventpojo.getDat();
            viewholder.date_row.setText(Cal_date(eventpojo.getDat()));
            viewholder.location_row.setText(eventpojo.getPlace());

            return view;

    }
    public String Cal_date(String date)
    { String dat="";
        eve_month=date.substring(0,2);
        Log.d("event_date_format",date);
        switch (eve_month)
        {
            case "01":
                e_month="Jan";
                break;
            case "02":
                e_month="Feb";
                break;
            case "03":
                e_month="Mar";
                break;
            case "04":
                e_month="Apr";
                break;
            case "05":
                e_month="May";
                break;
            case "06":
                e_month="June";
                break;
            case "07":
                e_month="Jul";
                break;
            case "08":
                e_month="Aug";
                break;
            case "09":
                e_month="Sep";
                break;
            case "10":
                e_month="Oct";
                break;
            case "11":
                e_month="Nov";
                break;
            case "12":
                e_month="Dec";
                break;
            default:
                Log.d("month","wrong");
        }
        new_date=e_month+" "+date.substring(3,5);
        Log.d("Testing",dat);
        return new_date;
    }

}
