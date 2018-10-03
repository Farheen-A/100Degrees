package com.degree.college;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.degree.college.Fragment.BuyFragment;
import com.degree.college.Fragment.CommFragment;
import com.degree.college.Fragment.EventFragment;
import com.degree.college.Fragment.MusicFragment;
import com.degree.college.Fragment.SportFragment;
import com.degree.college.bottomhelper.BottomNavigationHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EventDetailActivity extends AppCompatActivity {
    TextView textdate, texttime, textname, textplace, textdes, title, org_name;
    ImageView imageView, imageback, imageconnect, org_image;
    int ItemId;
    String event_url = "http://100degreesapp.com/API.aspx/API.aspx?fn=fetch_all_events";
    String EventId, Eventname, EventVenu, Eventdate, Start_time, Endtime, bannerImg, AboutEvent, Buyticket, Organizer, OrgImag, Status, Msg, Itemname, buy, name;
    JSONArray result2 = null;
    String event_date_format, eve_month, e_month, new_date = "", organizer, orgImage,venue;
    Button buyTicket;
    Bundle bundle;
    BottomNavigationView bottomNavigationView;
    BottomNavigationHelper bottomNavigationHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_detail);
        bundle = getIntent().getExtras();
        Itemname = bundle.getString("list");

        // Itemname = getArguments().getString("list");

        textdate = (TextView) findViewById(R.id.EdateDetail);
        texttime = (TextView) findViewById(R.id.eDetailTime);
        textname = (TextView) findViewById(R.id.eDetailName);
        textplace = (TextView) findViewById(R.id.eDetailPlace);
        textdes = (TextView) findViewById(R.id.eDetailDescription);
        imageView = (ImageView) findViewById(R.id.imageEvent);
        title = (TextView) findViewById(R.id.textcen);
        org_image = (ImageView) findViewById(R.id.organizer);
        org_name = (TextView) findViewById(R.id.dorgName);
        buyTicket = (Button) findViewById(R.id.buybtn);
        bottomNavigationHelper = new BottomNavigationHelper();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationHelper.removeShiftMode(bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.events) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.eventdetail, new EventFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.sports) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.eventdetail, new SportFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.comm) {
                 getSupportFragmentManager().beginTransaction().replace(R.id.eventdetail, new CommFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.music) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.eventdetail, new MusicFragment()).addToBackStack(null).commit();
                }
                if (item.getItemId() == R.id.more) {

                    Intent i=new Intent( getApplicationContext(),MoreActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }
                return true;
            }
        });

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        title = (TextView) findViewById(R.id.textcen);
        title.setText("Event Details");
        imageback = (ImageView) findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.eventdetail, new EventFragment()).addToBackStack(null).commit();
            }
        });

        imageconnect = (ImageView) findViewById(R.id.toolchat);
        imageconnect.setImageResource(R.drawable.location);
        imageconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EventDetailActivity.this, LocationActivity.class);
                i.putExtra("location",venue);
                startActivity(i);
            }
        });



//

        StringRequest eventRequest = new StringRequest(Request.Method.GET, event_url, new Response.Listener<String>() {
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
                        if (Itemname.equals(Eventname)) {
                            textname.setText(Eventname);
                            event_date_format = Cal_date(jsonEve.getString("StartDate"));
                            textdate.setText(event_date_format);
                            texttime.setText(Start_time + "-" + Endtime);
                            textplace.setText(EventVenu);
                            textdes.setText(AboutEvent);
                            org_name.setText(Organizer);
                            Picasso.with(EventDetailActivity.this).load(bannerImg).into(imageView);
                            Picasso.with(EventDetailActivity.this).load(OrgImag).into(org_image);
                            buy = Buyticket;
                            venue=EventVenu;
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
                        Toast.makeText(EventDetailActivity.this, error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> uni_map = new HashMap<>();
                return uni_map;
            }
        };
        MySingleton.getInstance(EventDetailActivity.this).addToRequestQueue(eventRequest);


        buyTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BuyFragment buyFragment = new BuyFragment();

                Bundle args = new Bundle();
                args.putString("buy", buy);
                buyFragment.setArguments(args);
                getSupportFragmentManager().beginTransaction().replace(R.id.eventdetail, buyFragment).commit();
            }
        });
    }
    public String venue(String location){
        String eveLocation=location;
        return location;
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

    @Override
    public void onBackPressed() {
        Intent i=new Intent( getApplicationContext(),NavigationActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}
