package com.degree.college.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.degree.college.OnItemClickInterface;
import com.degree.college.Pojo.Tutor;
import com.degree.college.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by USER on 29-03-2018.
 */

public class RecyclerTutor extends RecyclerView.Adapter<RecyclerTutor.TutorHolder> {
    Context context;
    List<Tutor> tutors;
    private OnItemClickInterface onItemClickInterface;
    String bannerurl;
    public RecyclerTutor(Context context, List<Tutor> tutors, OnItemClickInterface onItemClickInterface) {
        this.context = context;
        this.tutors = tutors;
        this.onItemClickInterface = onItemClickInterface;
    }

    @Override
    public TutorHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.tutor_row, parent, false);

        return new RecyclerTutor.TutorHolder(itemView, onItemClickInterface);
    }

    @Override
    public void onBindViewHolder(TutorHolder holder, int position) {
        Tutor tutor=tutors.get(position);
        bannerurl=tutor.getImageurl();
        Picasso.with(context).load(bannerurl).into(holder.imageView);
        holder.txtname.setText(tutor.getName());
        holder.txtsubject.setText(tutor.getSubject());
        holder.txtlocation.setText(tutor.getLocation());

    }

    @Override
    public int getItemCount() {
        return tutors.size();
    }

    public class TutorHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView imageView;
        TextView txtname, txtmobile, txtemail, txtlocation, txtsubject, description;
        private OnItemClickInterface onItemClick;

        public TutorHolder(View itemView, OnItemClickInterface onItemClickInterface) {
            super(itemView);
            txtname = (TextView) itemView.findViewById(R.id.namerow);
            txtlocation = (TextView) itemView.findViewById(R.id.location_row);
            txtsubject = (TextView) itemView.findViewById(R.id.uni_row);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
            onItemClick = onItemClickInterface;

        }

        @Override
        public void onClick(View v) {
            onItemClick.setonItemClickListener(v, getAdapterPosition());

        }
    }
}
