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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.degree.college.Adapters.SportsAdapter;
import com.degree.college.Adapters.SportsRecyclerAdapter;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SportFragment extends Fragment implements OnBackInterface{
    ListView listView;
    RecyclerView lists;
    TextView title;
    ArrayList<Event> Sportlst, list;
    SportsAdapter sportsAdapter;
    SportsRecyclerAdapter sportsRecyclerAdapter;
    ImageView imageback, imageconnect;
    String sports_url = "http://100Degreesapp.com/API.aspx/API.aspx?fn=fetch_all_sports";
    ImageView imgCon;
    int ID;
    JSONArray sport_result = null;
    String Sport_Id, SportName, SportDate, SportStime, SportEtime, Address, BannerImg, SportType, Teams, Team, TeamList;
    ProgressDialog progressDialog;

    public SportFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sport, container, false);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        //  listView = (ListView)v.findViewById(R.id.comm);
        Sportlst = new ArrayList<>();


        //   sportsAdapter = new SportsAdapter(getContext(), R.layout.sports_row, Sportlst);
        // listView.setAdapter(sportsAdapter);

        title = (TextView) v.findViewById(R.id.textcen);
        title.setText("Sports");
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
        OnItemClickInterface onItemClickInterface = new OnItemClickInterface() {
            @Override
            public void setonItemClickListener(View v, int position) {

                String name = getName(position);
                Sportsdetail ldf = new Sportsdetail();
                Bundle args = new Bundle();
                args.putString("list", name);
                ldf.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.sports, ldf).commit();
            }
        };

        sportsRecyclerAdapter = new SportsRecyclerAdapter(getContext(), R.layout.sports_row, list, onItemClickInterface);
        lists.setAdapter(sportsRecyclerAdapter);

        StringRequest sportRequest = new StringRequest(Request.Method.GET, sports_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONArray sportArray = new JSONArray(response);
                    sport_result = sportArray;
                    for (int i = 0; i < sportArray.length(); i++) {
                        JSONObject sportObj = sportArray.getJSONObject(i);
                        Sport_Id = sportObj.getString("ID");
                        SportName = sportObj.getString("SportName");
                        SportType = sportObj.getString("SportType");
                        SportDate = sportObj.getString("StartDate");
                        SportStime = sportObj.getString("StartTime");
                        SportEtime = sportObj.getString("EndTime");
                        Address = sportObj.getString("Address");
                        BannerImg = sportObj.getString("BannerImg");
                        Teams = sportObj.getString("Teams");
                      list.add(new Event(BannerImg, SportName, SportDate, SportType, Address, SportStime, SportEtime));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                sportsRecyclerAdapter.notifyDataSetChanged();

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(sportRequest);

    return v;
    }

    public String getName(int app) {
        String Name = "";
        try {
            JSONObject jsonId = sport_result.getJSONObject(app);
            Name = jsonId.getString("SportName");
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
