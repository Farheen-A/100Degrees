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
import com.degree.college.EventDetailActivity;
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
public class EventFragment extends Fragment implements OnBackInterface {

    // ListView lists;
    ArrayList<Event>loadList;
    RecyclerView lists;
    ArrayList<Event> list;
    EventAdapter eventAdapter = null;
    ImageView imgCon;
    JSONArray event_result = null;
    String event_url = "http://100degreesapp.com/API.aspx/API.aspx?fn=fetch_all_events";
    TextView title;
    ImageView grp, imageback, imageconnect;
    String EventId, Eventname, EventVenu, Eventdate, Start_time, Endtime, bannerImg, AboutEvent, Buyticket, Organizer, OrgImag, Status, Msg;
    ProgressDialog progressDialog;
    EventsAdapter eventsAdapter;
int c=0;int d=3;
    public EventFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_event, container, false);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        title = (TextView) v.findViewById(R.id.textcen);
        title.setText("Events");
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


        loadList=new ArrayList<>();
        lists = (RecyclerView) v.findViewById(R.id.EventList);
        list = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        lists.setLayoutManager(mLayoutManager);
        OnItemClickInterface onItemClickInterface=new OnItemClickInterface() {
            @Override
            public void setonItemClickListener(View v, int position) {

                String name = getName(position);
//                   EventDetail ldf = new EventDetail();
//                   Bundle args = new Bundle();
//                    args.putString("list", name);
//                    ldf.setArguments(args);
//                    getFragmentManager().beginTransaction().replace(R.id.container, ldf).addToBackStack(null).commit();
                    Intent i=new Intent(getContext(), EventDetailActivity.class);
                    i.putExtra("list",name);
                    startActivity(i);
            }
        };
        //ev
        // eventAdapter = new EventAdapter(getContext(), R.layout.events_row, list);
//        lists.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                if (!recyclerView.canScrollVertically(1))
//                    onScrolledToBottom();
//            }
//        });



        StringRequest eventRequest = new StringRequest(Request.Method.GET, event_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response",response);
                try {
                    progressDialog.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    event_result = jsonArray;
                    Log.e("length", response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonEve = jsonArray.getJSONObject(i);
                        EventId = jsonEve.getString("ID");
                        Eventname = jsonEve.getString("EventName");
                        Eventdate = jsonEve.getString("StartDate");
                        EventVenu = jsonEve.getString("Venu");
                        Start_time = jsonEve.getString("StartTime");
                        Endtime = jsonEve.getString("EndTime");
                        bannerImg = jsonEve.getString("BannerImg");
                        AboutEvent = jsonEve.getString("AboutEvent");
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
//        if(list.size()>2){
//            loadList=new ArrayList<>(list.subList(0,6));
//        }else
//        {
//            loadList=list;
//        }

        eventsAdapter = new EventsAdapter(getContext(), R.layout.events_row, list,onItemClickInterface);

        lists.setAdapter(eventsAdapter);
        return v;
    }


    public String getName(int app) {
        String Name = "", date = "";
        try {
            JSONObject jsonId = event_result.getJSONObject(app);
            Name = jsonId.getString("EventName");
            Log.d("maj", Name);
            // position=Integer.parseInt(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("Testing", Name);
        return Name;
    }

    private void onScrolledToBottom() {
        if (loadList.size() < list.size()) {
            int x, y;
            if ((list.size() - loadList.size()) >= 50) {
                x = loadList.size();
                y = x + 50;
            } else {
                x = loadList.size();
                y = x + list.size() - loadList.size();
            }
            for (int i = x; i < y; i++) {
                loadList.add(list.get(i));
            }
            eventsAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public boolean onBack() {
        return false;
    }
}
