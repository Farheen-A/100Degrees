package com.degree.college.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.degree.college.LocationActivity;
import com.degree.college.MySingleton;
import com.degree.college.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Comm_detail extends Fragment  {
    ImageView img, imageback, imageCon;
    CircleImageView imgorg;
    String itemName="", buy,venue;
    TextView tname, ttime, tlocation, tdate, toragName, tdescription, title;

    int fetchId;
    String Id, commName, commAddress, Commdate, CommS_time, CommE_time, CommBanner, About_service, OrgName, OrgImag, BannerPic,buyTicket;
    String comm_url = "http://100Degreesapp.com/API.aspx?fn=fetch_all_services";
    String event_date_format, eve_month, e_month, new_date = "";
    Button BuyButton;

    public Comm_detail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_comm_detail, container, false);

        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        imgorg = (CircleImageView) v.findViewById(R.id.organizer);
        img = (ImageView) v.findViewById(R.id.photo);
        tname = (TextView) v.findViewById(R.id.dname);
        ttime = (TextView) v.findViewById(R.id.dtime);
        tdate = (TextView) v.findViewById(R.id.ddate);
        tlocation = (TextView) v.findViewById(R.id.dlocation);
        toragName = (TextView) v.findViewById(R.id.dorgName);
        tdescription = (TextView) v.findViewById(R.id.ddescription);

        BuyButton = (Button) v.findViewById(R.id.buybtn);
        BuyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BuyFragment buyFragment = new BuyFragment();
                Bundle args = new Bundle();
                args.putString("buy", buy);
                buyFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.commdetail, buyFragment).commit();

            }
        });
        title = (TextView) v.findViewById(R.id.textcen);
        title.setText("Community Service Detail");
           imageCon=(ImageView)v.findViewById(R.id.toolchat);
        imageCon.setImageResource(R.drawable.location);
        imageCon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LocationActivity.class);
                i.putExtra("location",venue);
                startActivity(i);
            }
        });

        imageback = (ImageView) v.findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.commdetail,new CommFragment()).addToBackStack(null).commit();
            }
        });

        itemName = getArguments().getString("list");

        StringRequest commRequest = new StringRequest(Request.Method.GET, comm_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //
                try {
                    JSONArray jcomm = new JSONArray(response);
                    for (int i = 0; i < jcomm.length(); i++) {
                        JSONObject jobj = jcomm.getJSONObject(i);
                        commName = jobj.getString("ServiceTitle");
                        Id = jobj.getString("ID");
                        commAddress = jobj.getString("Venue");
                        Commdate = jobj.getString("StartDate");
                        CommS_time = jobj.getString("StartTime");
                        CommE_time = jobj.getString("EndTime");
                        CommBanner = jobj.getString("BannerPic");
                        OrgName = jobj.getString("Organizer");
                        OrgImag = jobj.getString("OrganizerImg");
                        About_service = jobj.getString("AboutService");
                        BannerPic = jobj.getString("BannerPic");
                        if (itemName.equals(commName)) {
                            Picasso.with(getContext()).load(BannerPic).into(img);
                            tname.setText(commName);
                            event_date_format = Cal_date(Commdate);
                            tdate.setText(event_date_format);
                            ttime.setText(CommS_time + "-" + CommE_time);
                            tlocation.setText(commAddress);
                            toragName.setText(OrgName);
                            tdescription.setText(About_service);
                            Picasso.with(getContext()).load(OrgImag).into(imgorg);
                            venue=commAddress;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        MySingleton.getInstance(getContext()).addToRequestQueue(commRequest);
        return v;
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

//    @Override
//    public boolean onBack() {
//
//        return false;
//    }
}
