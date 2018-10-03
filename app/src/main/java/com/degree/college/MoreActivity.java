package com.degree.college;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.degree.college.Adapters.MoreAdapter;
import com.degree.college.Fragment.CommFragment;
import com.degree.college.Fragment.EventFragment;
import com.degree.college.Fragment.MusicFragment;
import com.degree.college.Fragment.ProfileFragment;
import com.degree.college.Fragment.SportFragment;
import com.degree.college.Fragment.TutorFragment;
import com.degree.college.Pojo.More_pojo;
import com.degree.college.bottomhelper.BottomNavigationHelper;

import java.util.ArrayList;
import java.util.List;

public class MoreActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BottomNavigationHelper bottomNavigationHelper;
    ListView listView;
    List<More_pojo> list;
    MoreAdapter moreAdapter;
    More_pojo more_pojo;
    ImageView imgback;
    Button btn;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.coordinate_toolbar);
        bottomNavigationHelper = new BottomNavigationHelper();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationHelper.removeShiftMode(bottomNavigationView);
        textView = (TextView) findViewById(R.id.textcen);
        imgback = (ImageView) findViewById(R.id.id);
        //imgback.setImageResource(R.drawable.ic_back);

        btn = (Button) findViewById(R.id.toolEdit);
        bottomNavigationView.setSelectedItemId(R.id.more);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.events) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.moreactivity, new EventFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.sports) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.moreactivity, new SportFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.comm) {
//                    fragment= getSupportFragmentManager().findFragmentById(R.id.frame);
//                    if (fragment instanceof CommFragment ) {
//                        onBackPressed();
//                    }
                    // FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    getSupportFragmentManager().beginTransaction().replace(R.id.moreactivity, new CommFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.music) {

                    //  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    getSupportFragmentManager().beginTransaction().replace(R.id.moreactivity, new MusicFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.more) {

                    // getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MoreFragment()).addToBackStack(null).commit();
                  finish();
                  startActivity(getIntent());

                }
                return true;
            }
        });


        btn.setText("");
        textView.setText("More");
        listView = (ListView) findViewById(R.id.list_item);
        list = new ArrayList<>();

        moreAdapter = new MoreAdapter(list, MoreActivity.this, R.layout.list_value);
        listView.setAdapter(moreAdapter);
        list.add(new More_pojo(R.drawable.ic_tutors, "Tutors"));
        list.add(new More_pojo(R.drawable.ic_profile, "Profile"));

        moreAdapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.moreactivity, new TutorFragment()).addToBackStack(null).commit();
                }
                if (position == 1) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.moreactivity, new ProfileFragment()).addToBackStack(null).commit();
                }
            }
        });
    }

//    @Override
//    public boolean onBack() {
//        Intent i=new Intent(MoreActivity.this, NavigationActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//        return true;
//    }

    @Override
    public void onBackPressed() {
        Fragment fragments = getSupportFragmentManager().findFragmentById(R.id.moreactivity);
        if (!(fragments instanceof OnBackInterface) || !((OnBackInterface) fragments).onBack()) {
            Intent i = new Intent(MoreActivity.this, NavigationActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
        }else{
            super.onBackPressed();
        }
    }
}
