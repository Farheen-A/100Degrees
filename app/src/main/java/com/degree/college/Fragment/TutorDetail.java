package com.degree.college.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.degree.college.MySingleton;
import com.degree.college.OnBackInterface;
import com.degree.college.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class TutorDetail extends Fragment implements OnBackInterface {

    CircleImageView circleImageView;
    TextView txtname, txtemail, txtphone, txtmajor, txtlocation, txtdescription, title;
    String tutorname, tutorEmail, phone, major, address, description, profileImg, tutorId, Status, Msg;
    FrameLayout frameLayout;
    String Itemname;
    String tutorUrl = "http://100Degreesapp.com/API.aspx?fn=fetch_all_tutors";
    ImageView imageback, imageconnect;
    ProgressDialog progressDialog;

    public TutorDetail() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragmentu67i
        View view = inflater.inflate(R.layout.fragment_tutor_detail, container, false);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        title = (TextView) view.findViewById(R.id.textcen);
        title.setText("Tutor");
        frameLayout=(FrameLayout)view.findViewById(R.id.mytutor);
        frameLayout.requestFocus();
        progressDialog = new ProgressDialog(getContext());
        imageback = view.findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);

        Itemname = getArguments().getString("list");
        Toast.makeText(getContext(), Itemname, Toast.LENGTH_SHORT).show();
        circleImageView = view.findViewById(R.id.tutorimg);
        txtname = view.findViewById(R.id.tutName);
        txtemail = view.findViewById(R.id.tutemail);
        txtphone = view.findViewById(R.id.tutphone);
        txtmajor = view.findViewById(R.id.tutmaj);
        txtlocation = view.findViewById(R.id.tutloc);
        txtdescription = view.findViewById(R.id.tutdesc);
        show();
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mytutor, new TutorFragment()).addToBackStack(null).commit();
            }
        });
        imageconnect = view.findViewById(R.id.toolchat);
//        imageconnect.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i = new Intent(getContext(), ChatActivity.class);
//                startActivity(i);
//            }
//        });
        return view;
    }

    public void show() {
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, tutorUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    progressDialog.dismiss();
                    JSONArray jsonArray = new JSONArray(response);
                    Log.e("length", response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonTut = jsonArray.getJSONObject(i);
                        tutorId = jsonTut.getString("ID");
                        tutorname = jsonTut.getString("TutorName");
                        tutorEmail = jsonTut.getString("EmailID");
                        phone = jsonTut.getString("PhoneNo");
                        major = jsonTut.getString("Major");
                        profileImg = jsonTut.getString("ProfilePicPath");
                        address = jsonTut.getString("Address");
                        description = jsonTut.getString("Description");
                        Status = jsonTut.getString("Status");
                        Msg = jsonTut.getString("Msg");
                        if (Itemname.equals(tutorname)) {
                            progressDialog.dismiss();
                            Picasso.with(getContext()).load(profileImg).into(circleImageView);
                            txtname.setText(tutorname);
                            txtmajor.setText(major);
                            txtemail.setText(tutorEmail);
                            txtdescription.setText(description);
                            txtphone.setText(phone);
                            txtlocation.setText(address);
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
        }
        );
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

    @Override
    public boolean onBack() {
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.mytutor, new TutorFragment()).addToBackStack(null).commit();
        return false;
    }
}
