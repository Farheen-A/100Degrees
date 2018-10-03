package com.degree.college.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.degree.college.MySingleton;
import com.degree.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MemberFragment extends Fragment {

LinearLayout linearLayout;
String majorId;
List<String> memberlist;
    public MemberFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_member, container, false);
        memberlist=new ArrayList<>();
        getMember();
//        linearLayout=(LinearLayout)v.findViewById(R.id.memberrow);
//
//        linearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//               // getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.member_fragment,new ConnectFragment()).commit();
//            }
//        });

        return v;
    }
    public void getMember(){

        StringRequest stringRequest=new StringRequest(Request.Method.GET, "http://100degreesapp.com/API_One.aspx?fn=major-member&MId=3", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray jsonArray=new JSONArray(response);
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject=jsonArray.getJSONObject(i);
                       memberlist.add(jsonObject.getString("UserName"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.d("resMamber",memberlist.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }){
//            @Override
//            protected Map<String, String> getParams() throws AuthFailureError {
//                Map<String,String>map=new HashMap<>();
//                map.put("MId","3");
//                return map;
//            }
        };
        MySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);
    }

}
