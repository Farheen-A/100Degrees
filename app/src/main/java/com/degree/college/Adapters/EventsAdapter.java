package com.degree.college.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.degree.college.OnItemClickInterface;
import com.degree.college.OnLoadMoreListener;
import com.degree.college.Pojo.Event;
import com.degree.college.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by USER on 23-03-2018.
 */

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {
    Context context;
    LinearLayout linearLayout;
    int layout, i;
    ArrayList<Event> list;

    String event_date_format, eve_month, e_month, new_date, Banner;
    // private EventsAdapter.onItemClick onItemClick;
    private OnItemClickInterface onItemClickInterface;
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;


    // The minimum amount of items to have below your current scroll position
// before loading more.
    private int visibleThreshold = 5;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public EventsAdapter(Context context, int layout, ArrayList<Event> list, OnItemClickInterface onItemClickInterface) {
        this.context = context;
        this.layout = layout;
        this.list = list;
        this.onItemClickInterface = onItemClickInterface;

    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView, orgImage;
        TextView namerow, time_row, date_row, location_row, org_name;
        private OnItemClickInterface onItemClick;

        public ViewHolder(View itemView, OnItemClickInterface onItemClickInterface) {
            super(itemView);
            this.onItemClick = onItemClickInterface;
            imageView = (ImageView) itemView.findViewById(R.id.eventrowImg);
            namerow = (TextView) itemView.findViewById(R.id.eveName);
            time_row = (TextView) itemView.findViewById(R.id.timeeve);
            date_row = (TextView) itemView.findViewById(R.id.evedate);
            location_row = (TextView) itemView.findViewById(R.id.evelocation);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemClick.setonItemClickListener(v, getAdapterPosition());
        }
    }


    @Override
    public EventsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.events_row, parent, false);
        return new ViewHolder(itemView, onItemClickInterface);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Event eventpojo = list.get(position);
        //((EventsAdapter.ViewHolder) holder).bind(context, list.get(position), onItemClick);
        Banner = eventpojo.getBannerImg();
        Picasso.with(context).load(Banner).into(holder.imageView);
        holder.namerow.setText(eventpojo.getNam());
        holder.time_row.setText(eventpojo.getTim() + "-" + eventpojo.getTime_end());
        event_date_format = eventpojo.getDat();
        holder.date_row.setText(Cal_date(eventpojo.getDat()));
        holder.location_row.setText(eventpojo.getPlace());
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,"clicked"+position,Toast.LENGTH_SHORT).show();
//                EventDetail ldf = new EventDetail();
//                  Bundle args = new Bundle();
//                   ldf.setArguments(args);
//
//
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return list.size();
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
