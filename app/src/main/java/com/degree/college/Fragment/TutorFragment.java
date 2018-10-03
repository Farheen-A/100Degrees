package com.degree.college.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.degree.college.Adapters.RecyclerTutor;
import com.degree.college.ChatActivity;
import com.degree.college.MoreActivity;
import com.degree.college.OnBackInterface;
import com.degree.college.OnItemClickInterface;
import com.degree.college.Pojo.Tutor;
import com.degree.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class TutorFragment extends Fragment implements OnBackInterface {
    RecyclerView recyclerView;
    RecyclerTutor recyclerTutor;
    List<Tutor> tutorList;
    String tutorUrl = "http://100Degreesapp.com/API.aspx?fn=fetch_all_tutors";
    JSONArray event_result = null;
    String tutorId, tutorname, tutorEmail, phone, major, profileImg, Status, Msg, address, description;
    TextView title;
    ProgressDialog progressDialog;
    ImageView imageback, imageconnect;
    FrameLayout frameLayout,frameLayout2;

    public TutorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_tutor, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        title = (TextView) view.findViewById(R.id.textcen);
        title.setText("Tutors");
        frameLayout=view.findViewById(R.id.lists);
        imageback = (ImageView) view.findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        imageconnect = (ImageView) view.findViewById(R.id.toolchat);
        imageconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), ChatActivity.class);
                startActivity(i);
            }
        });
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.tfragment,new MoreFragment());
                ft.commit();
            }
        });
        tutorList = new ArrayList<>();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(mLayoutManager);
        OnItemClickInterface onItemClickInterface = new OnItemClickInterface() {
            @Override
            public void setonItemClickListener(View v, int position) {
                frameLayout.setVisibility(View.INVISIBLE);
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();

                String name = getName(position);
                TutorDetail ldf=new TutorDetail();
                Bundle args = new Bundle();
                args.putString("list", name);
                ldf.setArguments(args);
                ft.replace(R.id.tryframe, ldf).commit();
            }
        };
        recyclerTutor = new RecyclerTutor(getContext(), tutorList, onItemClickInterface);

        recyclerView.setAdapter(recyclerTutor);

        StringRequest eventRequest = new StringRequest(Request.Method.GET, tutorUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    event_result = jsonArray;
                    Log.e("length", response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonEve = jsonArray.getJSONObject(i);
                        tutorId = jsonEve.getString("ID");
                        tutorname = jsonEve.getString("TutorName");
                        tutorEmail = jsonEve.getString("EmailID");
                        phone = jsonEve.getString("PhoneNo");
                        major = jsonEve.getString("Major");
                        profileImg = jsonEve.getString("ProfilePicPath");
                        address = jsonEve.getString("Address");
                        description = jsonEve.getString("Description");
                        Status = jsonEve.getString("Status");
                        Msg = jsonEve.getString("Msg");

                        tutorList.add(new Tutor(profileImg, tutorname, major, address));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                recyclerTutor.notifyDataSetChanged();

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


        return view;
    }

    public String getName(int app) {
        String Name = "";
        try {
            JSONObject jsonId = event_result.getJSONObject(app);
            Name = jsonId.getString("TutorName");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return Name;
    }

    @Override
    public boolean onBack() {
        Intent i=new Intent(getContext(), MoreActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        return true;
    }
}
