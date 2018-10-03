package com.degree.college.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.degree.college.MoreActivity;
import com.degree.college.OnBackInterface;
import com.degree.college.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment implements OnBackInterface{

    ImageView imgcon;Toolbar toolbar;
    int ItemId;
    String agejson;
    int age;
    Bitmap bitmap;

    String id, y, Pass, Email;
    CircleImageView imageView;
    ImageView imageback, imageconnect;


    BottomNavigationView bottomNavigationView;

    TextView proName, proMajor, proUni, proAge, progender, proabout, title;
    Button edittool;
    Context context;
    ProgressDialog progressDialog;
    String profile_url = "http://regaliainfotech.com/sandbox/college-app/app/profile.php?id= ";
    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View Rview=inflater.inflate(R.layout.fragment_profile, container, false);
        // Inflate the layout for this fragment

        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.coordinate_toolbar);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("degreelogin", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "Not Available");
      //  Email = sharedPreferences.getString("user", "Not Available");
        //Pass = sharedPreferences.getString("pass", "Not Available");
        imageView = (CircleImageView)Rview.findViewById(R.id.profileImage);
        imageback=(ImageView)Rview.findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frg_blank_layout,new MoreFragment());
                ft.commit();
            }
        });

        title = (TextView)Rview. findViewById(R.id.textcen);
        title.setText("Profile info");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        proName = (TextView) Rview.findViewById(R.id.proName); //proName.setText(intent.getStringExtra("email"));
        proMajor = (TextView) Rview.findViewById(R.id.proMajor);
        proUni = (TextView) Rview.findViewById(R.id.proUniversity);
        proAge = (TextView) Rview.findViewById(R.id.proAge);
        progender = (TextView) Rview.findViewById(R.id.progender);
        proabout = (TextView) Rview.findViewById(R.id.proabout);
        //imageView = (CircleImageView) findViewById(R.id.profileImage);
        edittool = (Button) Rview.findViewById(R.id.toolEdit);
        edittool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft=getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frg_blank_layout,new EditFragment());
                ft.commit();
            }
        });
        profile();
        return  Rview;
    }
    public void profile() {
        final StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://100degreesapp.com/API_One.aspx?fn=user-profile&UserID=" + id, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
               progressDialog.dismiss();
               try {
                    progressDialog.dismiss();
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("real", response);

                    proName.setText(jsonObject.getString("UserName"));
                    agejson = jsonObject.getString("Age");

                   // String present_age = ageCal(agejson);
                    proAge.setText(agejson+" "+"yrs");
                    String json_major = jsonObject.getString("Major");
                    if (json_major != null) {
                        proMajor.setText(json_major);
                    } else {
                        proMajor.setText("");
                    }
                    String json_About = jsonObject.getString("AboutMe");
                    if (json_About != null) {
                        proabout.setText(json_About);
                    } else {
                        proabout.setText("");
                    }
                    String json_Gender = jsonObject.getString("Gender");
                    if (json_Gender != null) {
                        progender.setText(json_Gender);
                    } else {
                        progender.setText("");
                    }
                    String json_University = jsonObject.getString("University");
                    if (json_University != null) {
                        proUni.setText(json_University);
                    } else {
                        proUni.setText("");
                    }
                    String img = jsonObject.getString("ProfilePic");

                    Picasso.with(getContext()).load(img).into(imageView);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }


        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("Password", Pass);
                return map;

            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }
    public String ageCal(String age) {
        String y=age.substring(7,11);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/mm/dd");

        String simple=simpleDateFormat.format(today);
        String year=simple.substring(0,4);
        int curyears= Integer.parseInt(year);
        int dob= Integer.parseInt(y);
        int curage = curyears-dob;
        Log.e("ye", String.valueOf(curyears));
        return String.valueOf(curage);
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
