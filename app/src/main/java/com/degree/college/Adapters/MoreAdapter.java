package com.degree.college.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.degree.college.Pojo.More_pojo;
import com.degree.college.R;

import java.util.List;

/**
 * Created by USER on 24-04-2018.
 */

public class MoreAdapter extends BaseAdapter {
    List<More_pojo> list;
    Context context;
    int layout;

    public MoreAdapter(List<More_pojo> list, Context context, int layout) {
        this.list = list;
        this.context = context;
        this.layout = layout;
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

    private  class ViewHolder{
        ImageView imageView;
        TextView textView;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view=convertView;
        MoreAdapter.ViewHolder viewHolder=new ViewHolder();
        if(view==null){
            LayoutInflater layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view=layoutInflater.inflate(layout,null);
            viewHolder.imageView=(ImageView)view.findViewById(R.id.layout_icon);
            viewHolder.textView=(TextView) view.findViewById(R.id.layout_title);
            view.setTag(viewHolder);
        }else{
            viewHolder= (MoreAdapter.ViewHolder) view.getTag();
        }
        More_pojo more_pojo=list.get(position);
        viewHolder.textView.setText(more_pojo.getName());
        viewHolder.imageView.setImageResource(more_pojo.getImage());


        return view;
    }
}
