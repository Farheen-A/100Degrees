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
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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

import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MusicDetail extends Fragment {
    TextView textdate, texttime, textname, textplace, textdes, title, org_name;
    ImageView imageView, imageback, imageconnect, org_image;
    int ItemId;
    String music_url = "http://100degreesapp.com/API_One.aspx?fn=fetch-all-musics";
    String EventId, Eventname, EventVenu, Eventdate, Start_time, Endtime, bannerImg, AboutEvent, Buyticket, Organizer, OrgImag, Status, Msg, Itemname, buy;
    JSONArray result2 = null;
    String event_date_format, eve_month, e_month, new_date = "", organizer, orgImage;
    Button buyTicket;


    public MusicDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_music_detail, container, false);
        Itemname = getArguments().getString("list");
        Toast.makeText(getContext(), Itemname, Toast.LENGTH_SHORT).show();


        textdate = (TextView) v.findViewById(R.id.EdateDetail);
        texttime = (TextView) v.findViewById(R.id.eDetailTime);
        textname = (TextView) v.findViewById(R.id.eDetailName);
        textplace = (TextView) v.findViewById(R.id.eDetailPlace);
        textdes = (TextView) v.findViewById(R.id.eDetailDescription);
        imageView =(ImageView) v.findViewById(R.id.imageEvent);
        title = (TextView) v.findViewById(R.id.textcen);
        org_image = (ImageView) v.findViewById(R.id.organizer);
        org_name = (TextView) v.findViewById(R.id.dorgName);
        buyTicket = (Button) v.findViewById(R.id.buybtn);

        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        title=(TextView)v.findViewById(R.id.textcen);
        title.setText("Music & Arts");
        imageback = (ImageView)v.findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.musicdetail,new MusicFragment()).addToBackStack(null).commit();
            }
        });

        imageconnect = (ImageView)v.findViewById(R.id.toolchat);
        imageconnect.setImageResource(R.drawable.location);
        imageconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), LocationActivity.class);
                i.putExtra("location",EventVenu);
                startActivity(i);
            }
        });
//

        StringRequest eventRequest = new StringRequest(Request.Method.GET,music_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // progressDialog.dismiss();
                // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    result2 = jsonArray;
                    Log.e("length", response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonEve = jsonArray.getJSONObject(i);
                        EventId = jsonEve.getString("ID");
                        Eventname = jsonEve.getString("MusicName");
                        Eventdate = jsonEve.getString("StartDate");
                        EventVenu = jsonEve.getString("Venue");
                        Start_time = jsonEve.getString("StartTime");
                        Endtime = jsonEve.getString("EndTime");
                        bannerImg = jsonEve.getString("BannerImg");
                        AboutEvent = jsonEve.getString("AboutMusic");
                        Buyticket = jsonEve.getString("BuyTicketURL");
                        Organizer = jsonEve.getString("Organizer");
                        OrgImag = jsonEve.getString("OrganizerImg");
                        Status = jsonEve.getString("Status");
                        Msg = jsonEve.getString("Msg");
                        if (Itemname.equals(Eventname)) {
                            textname.setText(Eventname);
                            event_date_format = Cal_date(jsonEve.getString("StartDate"));
                            textdate.setText(event_date_format);
                            texttime.setText(Start_time + "-" + Endtime);
                            textplace.setText(EventVenu);
                            textdes.setText(AboutEvent);
                            org_name.setText(Organizer);
                            Picasso.with(getContext()).load(bannerImg).into(imageView);
                            Picasso.with(getContext()).load(OrgImag).into(org_image);
                            buy = Buyticket;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> uni_map = new HashMap<>();
                return uni_map;
            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(eventRequest);


        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyFragment buyFragment = new BuyFragment();
                Bundle args = new Bundle();
                args.putString("buy", buy);
                buyFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.eventdetail, buyFragment).commit();
            }
        });

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


}