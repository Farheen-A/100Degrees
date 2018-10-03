package com.degree.college.Fragment;


import android.app.ProgressDialog;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class Sportsdetail extends Fragment {

    ImageView imageView, imageback, imageconnect, imageteam1, imageteam2;
    TextView tname, tTime, Tdate, Tlocation, Tdescription, title, textView, textView2;

    String sports_url = "http://100Degreesapp.com/API.aspx/API.aspx?fn=fetch_all_sports";

    String ItemId, Sport_Id, SportName, SportDate, SportStime, SportEtime, Address, BannerImg, SportType, Sportteams, Teamlist, Team, logo, logo1, S_Status, S_Description, S_Msg, buy;
    JSONArray sport_result = null;
    String event_date_format, eve_month, e_month, new_date = "";
    Button buybtn;
    ProgressDialog progressDialog;

    public Sportsdetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sportsdetail, container, false);
        //  imageconnect = (ImageView) v.findViewById(R.id.connect);
        textView = (TextView) v.findViewById(R.id.textView2);
        tname = (TextView) v.findViewById(R.id.sportsName);
        tTime = (TextView) v.findViewById(R.id.sportsTime);
        Tdate = (TextView) v.findViewById(R.id.sportsDate);
        Tlocation = (TextView) v.findViewById(R.id.sportsLocation);
        Tdescription = (TextView) v.findViewById(R.id.sportsDesription);
        imageView = (ImageView) v.findViewById(R.id.imgSportsDetail);
        imageteam1 = (ImageView) v.findViewById(R.id.team1);
        imageteam2 = (ImageView) v.findViewById(R.id.team2);
        buybtn = (Button) v.findViewById(R.id.buybtn);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.signtool);
        title = (TextView) v.findViewById(R.id.textcen);
        title.setText("Sports Details");
        imageback = (ImageView) v.findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.sportsdetail, new SportFragment()).addToBackStack(null).commit();


            }
        });
        imageconnect = (ImageView) v.findViewById(R.id.toolchat);
        imageconnect.setImageResource(R.drawable.location);
        imageconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//
                Intent i = new Intent(getContext(), LocationActivity.class);
                i.putExtra("location",Address);
                startActivity(i);

            }
        });
//
        ItemId = getArguments().getString("list");

        StringRequest sportRequest = new StringRequest(Request.Method.GET, sports_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Toast.makeText(getApplicationContext(),response.toString(),Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                try {
                    JSONArray sportArray = new JSONArray(response);
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
                        Sportteams = sportObj.getString("Teams");
                        // Teamlist=sportObj.getString("TeamsList");
                        JSONArray jsport = sportObj.getJSONArray("TeamsList");
                        for (int j = 0; j < jsport.length(); j++) {
                            JSONObject jobj = jsport.getJSONObject(0);
                            Team = jobj.getString("Team");
                            logo = jobj.getString("Logo");

                            JSONObject jobj2 = jsport.getJSONObject(1);
                            Team = jobj2.getString("Team");
                            logo1 = jobj2.getString("Logo");

                            // Toast.makeText(getApplicationContext(),logo,Toast.LENGTH_LONG).show();
                        }
                        S_Status = sportObj.getString("Status");
                        S_Description = sportObj.getString("Description");
                        S_Msg = sportObj.getString("Msg");
                        if (ItemId.equals(SportName)) {
                            Picasso.with(getContext()).load(BannerImg).into(imageView);
                            tname.setText(SportName);
                            event_date_format = Cal_date(SportDate);
                            Tdate.setText(event_date_format);
                            tTime.setText(SportStime + "-" + SportEtime);
                            Tlocation.setText(Address);
                            Tdescription.setText(S_Description);
                            textView.setText(SportType);
                            // Picasso.with(getApplicationContext()).load(logo).into(imageteam2);
                            Picasso.with(getContext()).load(logo).into(imageteam1);
                            Picasso.with(getContext()).load(logo1).into(imageteam2);
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
        MySingleton.getInstance(getContext()).addToRequestQueue(sportRequest);
        buybtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyFragment buyFragment = new BuyFragment();
                Bundle args = new Bundle();
                args.putString("buy", buy);
                buyFragment.setArguments(args);
                getFragmentManager().beginTransaction().replace(R.id.sportsdetail, buyFragment).commit();
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
