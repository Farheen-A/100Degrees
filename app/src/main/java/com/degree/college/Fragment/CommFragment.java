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
public class CommFragment extends Fragment implements OnBackInterface {
RecyclerView lists;
  //  ListView lists;
    ArrayList<Event> list;
    EventsAdapter eventAdapter = null;
    ImageView imgCon;
    JSONArray event_result = null;
    String comm_url = "http://100Degreesapp.com/API.aspx?fn=fetch_all_services";
    TextView title;
    ImageView grp;
    String Id, commName, commAddress, Commdate, CommS_time, CommE_time, CommBanner,venue;
    ImageView imageback, imageconnect;
    ProgressDialog progressDialog;

    public CommFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_comm, container, false);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        lists = (RecyclerView) v.findViewById(R.id.EventList);
        list = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        lists.setLayoutManager(mLayoutManager);
        OnItemClickInterface onItemClickInterface=new OnItemClickInterface() {
            @Override
            public void setonItemClickListener(View v, int position) {

                String name = getName(position);
                Comm_detail ldf = new Comm_detail();
                Bundle args = new Bundle();
                args.putString("list", name);
                ldf.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.comm, ldf).addToBackStack(null).commit();
            }
        };

        eventAdapter = new EventsAdapter(getContext(), R.layout.events_row, list,onItemClickInterface);
        lists.setAdapter(eventAdapter);

        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        title = (TextView) v.findViewById(R.id.textcen);
        title.setText("Community Services");
        imageback = (ImageView) v.findViewById(R.id.id);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imageconnect = (ImageView) v.findViewById(R.id.toolchat);
        imageconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ChatActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
            }
        });


        StringRequest eventRequest = new StringRequest(Request.Method.GET, comm_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    event_result = jsonArray;
                    Log.e("length", response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jobj = jsonArray.getJSONObject(i);
                        commName = jobj.getString("ServiceTitle");
                        Id = jobj.getString("ID");
                        commAddress = jobj.getString("Venue");
                        Commdate = jobj.getString("StartDate");
                        CommS_time = jobj.getString("StartTime");
                        CommE_time = jobj.getString("EndTime");
                        CommBanner = jobj.getString("BannerPic");

                        list.add(new Event(CommBanner, commName, Commdate, CommS_time, CommE_time, commAddress));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                eventAdapter.notifyDataSetChanged();

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


//        if (lists != null) {
//            lists.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    String name = getName(position);
//                    Comm_detail comm = new Comm_detail();
//                    Bundle args = new Bundle();
//                    args.putString("list", name);
//                    comm.setArguments(args);
//                    getFragmentManager().beginTransaction().replace(R.id.comm, comm).commit();
//                }
//            });
//        } else {
//            Toast.makeText(getContext(), "There is no item in Event ", Toast.LENGTH_LONG).show();
//        }
//        lists.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
//            @Override
//            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
//
//                return false;
//            }
//        });

        return v;
    }


    public String getName(int app) {
        String Name = "", date = "";
        try {
            JSONObject jsonId = event_result.getJSONObject(app);
            Name = jsonId.getString("ServiceTitle");
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
