package com.degree.college;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import com.degree.college.Fragment.CommFragment;
import com.degree.college.Fragment.EventFragment;
import com.degree.college.Fragment.MusicFragment;
import com.degree.college.Fragment.SportFragment;
import com.degree.college.bottomhelper.BottomNavigationHelper;

public class NavigationActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    BottomNavigationHelper bottomNavigationHelper;
    Fragment fragment;
    boolean doubleBackToExitPressedOnce = false;
    FragmentTransaction ft;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        bottomNavigationHelper = new BottomNavigationHelper();
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNavigationHelper.removeShiftMode(bottomNavigationView);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame, new EventFragment()).commit();
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() == R.id.events) {

                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new EventFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.sports) {

                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, new SportFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.comm) {
//                    fragment= getSupportFragmentManager().findFragmentById(R.id.frame);
//                    if (fragment instanceof CommFragment ) {
//                        onBackPressed();
//                    }
                   // FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame, new CommFragment()).addToBackStack(null).commit();

                }
                if (item.getItemId() == R.id.music) {

                  //  FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                   getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MusicFragment()).addToBackStack(null).commit();
//                    fragment= getSupportFragmentManager().findFragmentById(R.id.frame);
//                    if (fragment instanceof MusicFragment ) {
//                        onBackPressed();
//                    }

                }
                if (item.getItemId() == R.id.more) {

                  // getSupportFragmentManager().beginTransaction().replace(R.id.frame, new MoreFragment()).addToBackStack(null).commit();
                    Intent i=new Intent( NavigationActivity.this,MoreActivity.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);

                }
                return true;
            }
        });
//        getSupportFragmentManager().addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
//            @Override
//            public void onBackStackChanged() {
//                if(getSupportFragmentManager().getBackStackEntryCount()<0){
//
//                }
//                Toast.makeText(getApplicationContext(),"Fragment on backpress"+ getSupportFragmentManager().getFragments(),Toast.LENGTH_SHORT).show();
//
//            }
//        });

    }


    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (!(fragment instanceof OnBackInterface) || !((OnBackInterface) fragment).onBack()) {
           // finish();
          //  startActivity(getIntent());
            if (exit) {
                finish(); // finish activity
            } else {
                Toast.makeText(this, "Press Back again to Exit.",
                        Toast.LENGTH_SHORT).show();
                exit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        exit = false;
                    }
                }, 3 * 1000);

            }
         //   super.onBackPressed();
//            new AlertDialog.Builder(NavigationActivity.this).setTitle("100 Degrees").setMessage("Do you want to exit? ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    finish();
//                    System.exit(0);
//                }
//            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//
//                }
//            }).show();
            //
        }

//        else if(((OnBackInterface) fragment).onBack()){
//            super.onBackPressed();
//
//        }

    }
//
//    @Override
//    public void onBackPressed() {
//
//        MenuItem menuItem=bottomNavigationView.getMenu().getItem(0);
//
//        if(bottomNavigationView.getSelectedItemId()==menuItem.getItemId()) {
//            //selected(menuItem);
//            bottomNavigationView.setSelectedItemId(menuItem.getItemId());
//            if (menuItem.getItemId() == R.id.events) {
//                new AlertDialog.Builder(this)
//                        .setTitle("Close App?")
//                        .setMessage("Do you really want to close this app?")
//                        .setPositiveButton("YES",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        finish();
//                                    }
//                                })
//                        .setNegativeButton("NO",
//                                new DialogInterface.OnClickListener() {
//
//                                    @Override
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                    }
//                                }).show();
//
//            } else {
//                getSupportFragmentManager().popBackStackImmediate();
//            }
//        }

//        else{
//
//            super.onBackPressed();
//        }

//        if(bottomNavigationView.getSelectedItemId()!=menuItem.getItemId()){
//            mBottomNav.setSelectedItemId(homeItem.getItemId())
//        }
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//        this.doubleBackToExitPressedOnce = true;
//        getSupportFragmentManager().popBackStack();
//        Toast.makeText(this, "Press again to exit..", Toast.LENGTH_SHORT).show();
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 500);
//
  //  }



}
