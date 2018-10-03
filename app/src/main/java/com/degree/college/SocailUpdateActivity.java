package com.degree.college;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SocailUpdateActivity extends AppCompatActivity {
    JSONArray result2, result;
    String major_id, major_name, major_status, university_id, university_name, university_status;
    List<String> major_list, university_list;
    Spinner majSpin, uniSpin;
    String id, nam, maj, age, uni, about, univer, Email, Pass, y, radiovalue, img, major, encodedImage;
    SharedPreferences sharedPreferences;
    EditText name, agein, aboutme, passsword;
    RadioGroup rGrp;
    ImageView imageback;
    Button save;
    TextView title;
    CircleImageView imageView;
    RadioButton rmale, rfemale;
    private int REQUEST_CODE_GALLERY = 999;
    Uri selectedImageUri;
    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_socail_update);
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.usertool);
        sharedPreferences = getSharedPreferences("degreelogin", Context.MODE_PRIVATE);

        title = (TextView) findViewById(R.id.textcen);
        title.setText("Edit Profile");
        save = (Button) findViewById(R.id.toolEdit);
        imageback = (ImageView) findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);
        save.setText("Done");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    editPic();
                    edit_Profile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        major_list = new ArrayList<>();
        university_list = new ArrayList<>();


        majSpin = (Spinner) findViewById(R.id.maj_spin);
        uniSpin = (Spinner) findViewById(R.id.uni_spin);
        agein = (EditText) findViewById(R.id.editage);
        imageView = (CircleImageView) findViewById(R.id.imgprofile);
        passsword = (EditText) findViewById(R.id.editpass);
        name = (EditText) findViewById(R.id.nameprofile);
        nam = name.getText().toString().trim();
        aboutme = (EditText) findViewById(R.id.editabout);
        about = aboutme.getText().toString().trim();
        rGrp = (RadioGroup) findViewById(R.id.radiogrp);
        rmale = (RadioButton) findViewById(R.id.radiomale);
        rfemale = (RadioButton) findViewById(R.id.radiofemale);
        rGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radiofemale) {
                    radiovalue = rfemale.getText().toString();
                    rfemale.setButtonDrawable(R.drawable.ic_radio_on);
                    rmale.setButtonDrawable(R.drawable.ic_radio_off);
                } else if (checkedId == R.id.radiomale) {
                    radiovalue = rmale.getText().toString();
                    rmale.setButtonDrawable(R.drawable.ic_radio_on);
                    rfemale.setButtonDrawable(R.drawable.ic_radio_off);

                }
            }
        });
        getMajor();
        get_University();
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        disply();

        uniSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                univer = uni_getID(position);
                Log.d("uni_get", univer);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        majSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                //   pm = position;
                // fetch_major = (String) parent.getItemAtPosition(position);
                major = getID(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void getMajor() {
        //progressDialog.show();
        StringRequest majorRequest = new StringRequest(Request.Method.GET, "http://100degreesapp.com/API.aspx?fn=fetch_all_majors", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  progressDialog.dismiss();

                try {
                    JSONArray jsonArray = new JSONArray(response);
                    result2 = jsonArray;

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonMaj = jsonArray.getJSONObject(i);
                        major_id = jsonMaj.getString("ID");
                        major_name = jsonMaj.getString("Major");
                        major_status = jsonMaj.getString("Status");

                        major_list.add(major_name);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                majSpin.setAdapter(new ArrayAdapter<String>(SocailUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, major_list));

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> major_map = new HashMap<>();
                return major_map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(majorRequest);
    }


    public String getID(int app) {
        String major_ID = "";
        try {
            JSONObject jsonId = result2.getJSONObject(app);
            major_ID = jsonId.getString("ID");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return major_ID;
    }

    public void get_University() {
        final String university = null;


        //  progressDialog.show();
        StringRequest majorRequest = new StringRequest(Request.Method.GET, "http://100degreesapp.com/API.aspx?fn=fetch_all_universities", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // progressDialog.dismiss();
                // Toast.makeText(getApplicationContext(),response,Toast.LENGTH_LONG).show();
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    result = jsonArray;
                    Log.d("response", result.toString());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonMaj = jsonArray.getJSONObject(i);
                        university_id = jsonMaj.getString("ID");
                        university_name = jsonMaj.getString("University");
                        university_status = jsonMaj.getString("Status");
                        university_list.add(university_name);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                uniSpin.setAdapter(new ArrayAdapter<String>(SocailUpdateActivity.this, R.layout.support_simple_spinner_dropdown_item, university_list));
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> uni_map = new HashMap<>();
                return uni_map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SocailUpdateActivity.this);
        requestQueue.add(majorRequest);
    }

    public String uni_getID(int test) {
        String univer_ID = "";
        try {
            JSONObject jsonId = result.getJSONObject(test);
            univer_ID = jsonId.getString("ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return univer_ID;
    }

    public void disply() {
//        sharedPreferences = getSharedPreferences("degreelogin", Context.MODE_PRIVATE);
//       // Email = sharedPreferences.getString("user", "Not Available");
        id = sharedPreferences.getString("id", "Not Available");
      //  Pass = sharedPreferences.getString("pass", "Not Avilable");
       // Toast.makeText(getApplicationContext(), "Shared Edit" + Email + id + Pass, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://100degreesapp.com/API_One.aspx?fn=user-profile&UserID=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //  progressDialog.dismiss();
                Log.e("res of display", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("UserName") != null) {
                        name.setText(jsonObject.getString("UserName"));
                    } else {
                        name.setText("");
                    }
                    if (jsonObject.getString("Age") != null) {
                        agein.setText(jsonObject.getString("Age"));
                    } else {
                        agein.setText("");
                    }
                    if (jsonObject.getString("Major") != null) {

                        major = jsonObject.getString("Major");
                        majSpin.setSelection(getIndex(majSpin, major));

                    } else {
                        majSpin.setPrompt("");
                    }
                    if (jsonObject.getString("AboutMe") != null) {
                        aboutme.setText(jsonObject.getString("AboutMe"));
                    } else {
                        aboutme.setText("");
                    }

                    String gender = jsonObject.getString("Gender");
                    if (gender != null) {
                        if (gender.equals("Female")) {
                            rGrp.check(R.id.radiofemale);
                        }
                        if (gender.equals("Male")) {
                            rGrp.check(R.id.radiomale);
                        }
                    } else {
                        rGrp.check(R.id.radiofemale);
                    }
                    if (jsonObject.getString("University") != null) {

                        uniSpin.setSelection(getIndex(uniSpin, jsonObject.getString("University")));

                    } else {
                        uniSpin.setPrompt("");
                    }
                    img = jsonObject.getString("ProfilePic");

                    if (img.equals("")) {
                        //   imageView.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.with(SocailUpdateActivity.this).load(img).into(imageView);
                    }
                    Log.d("Image", img);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("response", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //   progressDialog.dismiss();

                Log.d("error", error.toString());
            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                //here pass where clause's condition eg. where id=$id
                //params.put("id",id);
                return params;
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                if (response.headers == null) {
                    // cant just set a new empty map because the member is final.
                    response = new NetworkResponse(
                            response.statusCode,
                            response.data,
                            Collections.<String, String>emptyMap(), // this is the important line, set an empty but non-null map.
                            response.notModified,
                            response.networkTimeMs);


                }

                return super.parseNetworkResponse(response);
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(SocailUpdateActivity.this);
        requestQueue.add(stringRequest);

    }

    private int getIndex(Spinner spinner, String myString) {

        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(myString)) {
                index = i;
            }
        }
        return index;
    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(SocailUpdateActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(SocailUpdateActivity.this, android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(SocailUpdateActivity.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
    }

    private void selectImage() {
        final CharSequence[] imgItem = {"Take Photo", "Choose from Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(SocailUpdateActivity.this);
        builder.setTitle("Add photo !");
        builder.setItems(imgItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (imgItem[which].equals("Take Photo")) {
                    Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(camera, 1);
                } else if (imgItem[which].equals("Choose from Gallery")) {
                    if (isReadStorageAllowed()) {
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        startActivityForResult(intent, REQUEST_CODE_GALLERY);
                    } else {
                        requestStoragePermission();
                    }
                } else if (imgItem[which].equals("Cancel")) {
                    dialog.dismiss();
                } else {
                    dialog.dismiss();
                }

            }

        });
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("image/*");
                    startActivityForResult(intent, REQUEST_CODE_GALLERY);
                }
            }

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            // selectedImageUri=null;
            if (requestCode == REQUEST_CODE_GALLERY) {
                selectedImageUri = data.getData();
                //imagepath = getPaths(selectedImageUri);
                // Log.d("selectedPath",getPaths(selectedImageUri));
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                imageView.setImageBitmap(bitmap);
                //encodedImage=encodeFromString(bitmap);
                encodedImage = Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.NO_WRAP);

                //    encodedImage=getStringImage(bitmap);
                //  Log.d("converted",encodedImage);
                // file=new File(selectedImageUri.getPath());

                //file = decodeUri(selectedImageUri);
            }
            if (requestCode == 1) {
                Bundle extras = data.getExtras();
                bitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(bitmap);
                selectedImageUri = Uri.parse(String.valueOf(extras.get("data")));
                encodedImage = Base64.encodeToString(getBytesFromBitmap(bitmap), Base64.NO_WRAP);
                Log.d("uri", selectedImageUri.toString());
            }
        }


    }

    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    public void editPic() {
       // sharedPreferences = getSharedPreferences("degreelogin", Context.MODE_PRIVATE);
        id = sharedPreferences.getString("id", "Not Available");

        StringRequest request = new StringRequest(Request.Method.POST, "http://100degreesapp.com/API_One.aspx?fn=upload-profile-picture", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equals("True")) {
                        Toast.makeText(getApplicationContext(), "Profile has been updated successfully.", Toast.LENGTH_SHORT).show();
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
                Map<String, String> picmap = new HashMap<>();
                picmap.put("userId", id);
                picmap.put("image", encodedImage);
                return picmap;
            }
        };
        MySingleton.getInstance(SocailUpdateActivity.this).addToRequestQueue(request);

    }

    public void edit_Profile() throws IOException {
        sharedPreferences = getSharedPreferences("degreelogin", Context.MODE_PRIVATE);
       // Email = sharedPreferences.getString("user", "Not Available");
        id = sharedPreferences.getString("id", "Not Available");
        Pass = passsword.getText().toString();
        age = agein.getText().toString().trim();
        about = aboutme.getText().toString();
        nam = name.getText().toString().trim();
//        progressDialog.setMessage("Updating");
//       progressDialog.show();
        StringRequest stringRequest = null;


        stringRequest = new StringRequest(Request.Method.POST,
                "http://100degreesapp.com/API_One.aspx?fn=update-social-user-details", new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Msg").equals("Profile has been updated successfully.")) {
                        //progressDialog.dismiss();

                        Intent i=new Intent(SocailUpdateActivity.this,NavigationActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    } else {
                        // progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Profile not updated successfully !", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                Log.d("response of Edit2", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ID", id);
                params.put("Password", Pass);
                params.put("FName", nam);
                params.put("MajorID", major);
                params.put("UniversityID", univer);
                params.put("Age", age);
                params.put("Gender", radiovalue);
                params.put("AboutMe", about);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(SocailUpdateActivity.this);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(SocailUpdateActivity.this).setTitle("100 Degrees").setMessage("Do you want to exit? ").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
               @Override
              public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    finish();
                    System.exit(0);
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).show();

    }
}
