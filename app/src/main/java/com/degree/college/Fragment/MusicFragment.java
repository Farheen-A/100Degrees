package com.degree.college.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.degree.college.Adapters.EventAdapter;
import com.degree.college.Adapters.EventsAdapter;
import com.degree.college.ChatActivity;
import com.degree.college.NavigationActivity;
import com.degree.college.OnBackInterface;
import com.degree.college.OnItemClickInterface;
import com.degree.college.Pojo.Event;
import com.degree.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicFragment extends Fragment implements OnBackInterface {
    RecyclerView lists;
    ArrayList<Event> list;
    EventAdapter eventAdapter = null;
    ImageView imgCon;
    JSONArray event_result = null;
    String music_url = "http://100degreesapp.com/API_One.aspx?fn=fetch-all-musics";
    TextView title;
    ImageView grp, imageback, imageconnect;
    String EventId, Eventname, EventVenu, Eventdate, Start_time, Endtime, bannerImg, AboutEvent, Buyticket, Organizer, OrgImag, Status, Msg;
    ProgressDialog progressDialog;
    EventsAdapter eventsAdapter;

    public MusicFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
    View v=inflater.inflate(R.layout.fragment_music, container, false);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        title = (TextView) v.findViewById(R.id.textcen);
        title.setText("Music & Arts");
        imageback = (ImageView) v.findViewById(R.id.id);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        imageconnect = (ImageView) v.findViewById(R.id.toolchat);
        imageconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ChatActivity.class);
                startActivity(i);
            }
        });

        lists = (RecyclerView) v.findViewById(R.id.EventList);
        list = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        lists.setLayoutManager(mLayoutManager);
        OnItemClickInterface onItemClickInterface=new OnItemClickInterface() {
            @Override
            public void setonItemClickListener(View v, int position) {

                String name = getName(position);
                MusicDetail ldf = new MusicDetail();
                Bundle args = new Bundle();
                args.putString("list", name);
                ldf.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.music, ldf).addToBackStack(null).commit();
            }
        };
        //eventAdapter = new EventAdapter(getContext(), R.layout.events_row, list);
     //   eventsAdapter = new EventsAdapter(getContext(), R.layout.events_row, list,onItemClickInterface);
        eventsAdapter=new EventsAdapter(getContext(),R.layout.events_row,list,onItemClickInterface);

        lists.setAdapter(eventsAdapter);


        StringRequest eventRequest = new StringRequest(Request.Method.GET, music_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    event_result = jsonArray;
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

                        list.add(new Event(bannerImg, Eventname, Eventdate, Start_time, Endtime, EventVenu));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                eventsAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> uni_map = new HashMap<>();
                return uni_map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(eventRequest);
        return v;

    }
    public String getName(int app) {
        String Name = "", date = "";
        try {
            JSONObject jsonId = event_result.getJSONObject(app);
            Name = jsonId.getString("MusicName");
            Log.d("maj", Name);
            // position=Integer.parseInt(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Testing", Name);
        return Name;
    }


    @Override
    public boolean onBack() {
        Intent i=new Intent(getContext(), NavigationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }
}
