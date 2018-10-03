package com.degree.college.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.degree.college.Adapters.MoreAdapter;
import com.degree.college.NavigationActivity;
import com.degree.college.OnBackInterface;
import com.degree.college.Pojo.More_pojo;
import com.degree.college.R;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MoreFragment extends Fragment implements OnBackInterface {

    ListView listView;
    List<More_pojo> list;
    MoreAdapter moreAdapter;
    More_pojo more_pojo;
ImageView imgback;
Button btn;
TextView textView;
    public MoreFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_more, container, false);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.usertool);

        textView=(TextView)v.findViewById(R.id.textcen);
        imgback=(ImageView)v.findViewById(R.id.id);
        //imgback.setImageResource(R.drawable.ic_back);

        btn=(Button)v.findViewById(R.id.toolEdit);
        btn.setText("");
        textView.setText("More");
        listView = (ListView) v.findViewById(R.id.list_item);
        list = new ArrayList<>();

        moreAdapter = new MoreAdapter(list, getContext(), R.layout.list_value);
        listView.setAdapter(moreAdapter);
        list.add(new More_pojo(R.drawable.ic_tutors, "Tutors"));
        list.add(new More_pojo(R.drawable.ic_profile, "Profile"));

        moreAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.moreFrag,new TutorFragment()).addToBackStack(null).commit();
                }
                if(position==1){
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.moreFrag,new ProfileFragment()).addToBackStack(null).commit();
                }
            }
        });

        return v;
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
