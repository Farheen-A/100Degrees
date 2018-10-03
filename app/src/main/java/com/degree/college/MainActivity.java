package com.degree.college;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String major_url = "http://100degreesapp.com/API.aspx?fn=fetch_all_majors";
    EditText edname, edemail, edpass, eddob;
    TextView title, cancel;
    ImageView imageBack;
    String name;
    String email;
    String pass;
    String dob;
    String major;
    String Id;
    String dateToStr;
    int dob_year;
    String age;
    int m, mYear, mDay, mMonth;
    Spinner spinner;
    Button btnSign;
    JSONArray result2;
    List<String> major_list;
    String emailpattern;
    ArrayList<String> uselList;
    ProgressDialog progressDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.signtool);
        title = (TextView) findViewById(R.id.titlesign);
        cancel = (TextView) findViewById(R.id.cancelsign);
        imageBack = (ImageView) findViewById(R.id.imgBack);
        progressDialog = new ProgressDialog(MainActivity.this);
        builder=new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Network Connection");

        progressDialog.setMessage("Please Wait...");
        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        title.setText("SignUp");
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        progressDialog.show();
        spinner = (Spinner) findViewById(R.id.spinMajor);
        edname = (EditText) findViewById(R.id.s_name);
        eddob = (EditText) findViewById(R.id.s_dob);
        edemail = (EditText) findViewById(R.id.s_Email);
        edpass = (EditText) findViewById(R.id.s_pass);
        btnSign = (Button) findViewById(R.id.s_signbtn);
        major_list = new ArrayList<>();
       if(isNetworkConnected()) {
            getAllMajor();
        }
        else{
           builder.setMessage("Please Check your Internet Connection");
           builder.setPositiveButton("ok", new DialogInterface.OnClickListener() {
               @Override
               public void onClick(DialogInterface dialog, int which) {
                   dialog.dismiss();
               }
           });
           builder.show();

       }
        eddob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edit_Dob();
            }
        });
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = edname.getText().toString();
                email = edemail.getText().toString();
                pass = edpass.getText().toString();
                dob = eddob.getText().toString();
                if (name.equals("") || name.length() == 0) {
                    edname.setError("Enter your name");
                } else if (dob.equals("") || dob.length() == 0) {
                    eddob.setError("Enter your Dob");
                } else if (email.equals("") || email.length() == 0) {
                    edemail.setError("Enter your email");
                } else if (pass.equals("") || pass.length() < 6) {
                    edpass.setError("Enter your name");
                } else {
                    StringRequest strRequest = new StringRequest(Request.Method.GET, "http://100degreesapp.com/API.aspx?fn=register&sFName=" + name + "&sLName=" + name + "&EmailID=" + email + "&MajorID=" + Id + "&DOB=" + age + "&Password=" + pass, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                Log.d("response", response);
                                if (jsonObject.getString("Msg").equals("You have successfully registered. Please verfy your email.")) {
                                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                                    alert.setTitle("Welcome to 100 Degrees");
                                    alert.setMessage("You have successfully registered. Please verfy your email.");
                                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                            clear();
                                        }
                                    });
                                    alert.show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> params = new HashMap<>();
                            params.put("sFName", name);
                            params.put("sLName", name);
                            params.put("EmailID", email);
                            params.put("DOB", dob);
                            params.put("MajorID", Id);
                            params.put("Password", pass);
                            Log.d("Myid", Id+"  "+name+" "+email+"  "+dob+"  "+pass);
                            return params;
                        }
                    };
                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(strRequest);
                }

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Id = getID(position);
                Log.d("id", getID(position));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void clear() {
        eddob.setText("");
        edpass.setText("");
        edemail.setText("");
        edname.setText("");
    }

    public void getAllMajor() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, major_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
progressDialog.dismiss();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    result2 = jsonArray;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        major = jsonObject.getString("Major");
                        Id = jsonObject.getString("ID");
                        int id = Integer.parseInt(Id);
                        major_list.add(major);
                    }
                    ArrayAdapter arrayAdapter = new ArrayAdapter(MainActivity.this, R.layout.support_simple_spinner_dropdown_item, major_list);
                    spinner.setAdapter(arrayAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);
    }

    public String getID(int postion) {
        String major_ID = "";
        try {
            JSONObject jsonId = result2.getJSONObject(postion);
            major_ID = jsonId.getString("ID");
            Log.d("maj", major_ID);
            // position=Integer.parseInt(null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return major_ID;
    }

    public void onLogin(View view) {
        Intent i = new Intent(MainActivity.this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    public String edit_Dob() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar today = Calendar.getInstance();
                        int yr = today.get(Calendar.YEAR);
                        int month = 1 + today.get(Calendar.MONTH);
                        int day = today.get(Calendar.DATE);
                        switch (monthOfYear) {
                            case 0:
                                dateToStr = "Jan";
                                m = 1;
                                break;
                            case 1:
                                dateToStr = "Feb";
                                m = 2;
                                break;
                            case 2:
                                dateToStr = "Mar";
                                m = 3;
                                break;
                            case 3:
                                dateToStr = "Apr";
                                m = 4;
                                break;
                            case 4:
                                dateToStr = "May";
                                m = 5;
                                break;
                            case 5:
                                dateToStr = "Jun";
                                m = 6;
                                break;
                            case 6:
                                dateToStr = "Jul";
                                m = 7;
                                break;
                            case 7:
                                dateToStr = "Aug";
                                m = 8;
                                break;
                            case 8:
                                dateToStr = "Sep";
                                m = 9;
                                break;
                            case 9:
                                dateToStr = "Oct";
                                m = 10;
                                break;
                            case 10:
                                dateToStr = "Nov";
                                m = 11;
                                break;
                            case 11:
                                dateToStr = "Dec";
                                m = 12;
                                break;
                        }
                        if (dayOfMonth < 10) {
                            eddob.setText("0" + dayOfMonth + " " + dateToStr + " " + year);
                            age = m + "/" + 0 + dayOfMonth + "/" + year;
                            dob_year = yr - year;
                            if (m > month) {
                                dob_year--;
                            }

                        } else {
                            eddob.setText(dayOfMonth + " " + dateToStr + " " + year);
                            age = m + "/" + dayOfMonth + "/" + year;
                            age = m + "-" + year;
                            dob_year = yr - year;

                            if (m > month) {
                                dob_year--;
                            }

                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();
        return age;

    }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


}
