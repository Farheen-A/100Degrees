package com.degree.college.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.degree.college.MySingleton;
import com.degree.college.R;
import com.degree.college.bottomhelper.BottomNavigationHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class EditFragment extends Fragment {
    final int REQUEST_CODE_GALLERY = 999;
    private int REQUEST_CAMERA = 100;
    Toolbar toolbar;
    TextView title;
    File file;
    String imagepath;
    Uri selectedImageUri;
    private Bitmap bitmap;
    String imgfile_name, upload_name;
    byte[] image;
    CircleImageView imageView;
    private int mYear, mMonth, mDay, mHour, mMinute, pm, pu, m, dob_year;
    EditText name;
    String major, fetch_major;
    EditText agein;
    EditText aboutme;
    RadioGroup rGrp;
    RadioButton rbtn;
    String radiovalue;
    int rbtnid, calage;
    String id, images;
    ImageView imageback;
    String nam, maj, age, uni, about, univer, Email, Pass, y, dateToStr;
    Button save;
    BottomNavigationView bottomNavigationView;
    BottomNavigationHelper bottomNavigationHelper = null;
    JSONArray result = null, result2 = null;
    ArrayList<String> university_list = null, major_list = null;

    Bitmap bitmap1;
    String university_id, university_name, university_status, uni_get, major_id, major_name, major_status,myname,myage;
    ProgressDialog progressDialog;
    String imagefileName, encodedImage;
    Spinner uniSpin, majSpin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    RadioButton rmale, rfemale;
    String img;
    File folder;

    public EditFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit, container, false);
        sharedPreferences = getActivity().getSharedPreferences("degreelogin", Context.MODE_PRIVATE);
        imageView = (CircleImageView) v.findViewById(R.id.imgprofile);
        //ActivityCompat.requestPermissions(this, new String[]{READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
        getActivity().getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.coordinate_toolbar);
        folder = new File(Environment.getExternalStorageDirectory(), "100Degrees");
        if (!folder.exists()) {
            folder.mkdir();
        }
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        title = (TextView) v.findViewById(R.id.textcen);
        title.setText("Edit Profile");
        save = (Button) v.findViewById(R.id.toolEdit);
        imageback = (ImageView) v.findViewById(R.id.id);
        imageback.setImageResource(R.drawable.ic_back);
        save.setText("Done");
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.edit_profile, new ProfileFragment()).addToBackStack(null).commit();
            }
        });
        university_list = new ArrayList<>();
        major_list = new ArrayList<>();
        uniSpin = (Spinner) v.findViewById(R.id.uni_spin);
        majSpin = (Spinner) v.findViewById(R.id.maj_spin);
        name = (EditText) v.findViewById(R.id.nameprofile);
        nam = name.getText().toString().trim();
        aboutme = (EditText) v.findViewById(R.id.editabout);
        about = aboutme.getText().toString().trim();
        rGrp = (RadioGroup) v.findViewById(R.id.radiogrp);
        rmale = (RadioButton) v.findViewById(R.id.radiomale);
        rfemale = (RadioButton) v.findViewById(R.id.radiofemale);
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

        get_University();
        getMajor();
        agein = (EditText) v.findViewById(R.id.editage);
        uniSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                univer = uni_getID(position);
                Log.d("uni_get", univer);
                pu = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        majSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                pm = position;
                fetch_major = (String) parent.getItemAtPosition(position);
                major = getID(pm);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        agein.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                edit_Dob();
//            }
//        });

        disply();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });

        return v;
    }

    public boolean validate() {
        progressDialog.setMessage("Updating...");
        progressDialog.show();
        nam = name.getText().toString().trim();
        age = agein.getText().toString().trim();
        if (nam.length() == 0) {
            name.setError("Please insert name");
            return false;
        } else if (age.length() == 0) {
            agein.setError("please insert age");
            agein.requestFocus();
            return false;
        } else {

            try {
                edit_Profile();
                editPic();
            } catch (IOException e) {
                e.printStackTrace();
                Log.d("error", e.toString());
            }

        }

        return true;
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

                uniSpin.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, university_list));
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> uni_map = new HashMap<>();
                return uni_map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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
                majSpin.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, major_list));

            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> major_map = new HashMap<>();
                return major_map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

    public void editPic() {

        id = sharedPreferences.getString("id", "Not Available");

        StringRequest request = new StringRequest(Request.Method.POST, "http://100degreesapp.com/API_One.aspx?fn=upload-profile-picture", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("response", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Status").equals("True")) {
                        Toast.makeText(getContext(), "Profile has been updated successfully.", Toast.LENGTH_SHORT).show();
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
        MySingleton.getInstance(getContext()).addToRequestQueue(request);

    }

    public void edit_Profile() throws IOException {
       // sharedPreferences = getActivity().getSharedPreferences("degreelogin", Context.MODE_PRIVATE);
       // Email = sharedPreferences.getString("user", "Not Available");
        id = sharedPreferences.getString("id", "Not Available");
     //   Pass = sharedPreferences.getString("pass", "Not Avilable");
        age = agein.getText().toString().trim();
        about = aboutme.getText().toString();
        nam = name.getText().toString().trim();
        myname=nam.replaceAll("\\s","+");
//        progressDialog.setMessage("Updating");
//       progressDialog.show();
        StringRequest stringRequest = null;


        stringRequest = new StringRequest(Request.Method.GET,
                "http://100degreesapp.com/API.aspx?fn=update_user_details&ID=" + id + "&FName=" + myname + "&MajorID=" + major + "&UniversityID=" + univer + "&Age=" + age + "&Gender=" + radiovalue
                        + "&AboutMe=" + about, new Response.Listener<String>() {


            @Override
            public void onResponse(String response) {


                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.getString("Msg").equals("Profile has been updated successfully.")) {
                        progressDialog.dismiss();
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.edit_profile, new ProfileFragment()).addToBackStack(null).commit();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(getContext(), "Profile not updated successfully !", Toast.LENGTH_SHORT).show();

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
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
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

    //http://100degreesapp.com/API.aspx?fn=login
    public void disply() {
        sharedPreferences = getActivity().getSharedPreferences("degreelogin", Context.MODE_PRIVATE);
       // Email = sharedPreferences.getString("user", "Not Available");
        id = sharedPreferences.getString("id", "Not Available");
      //  Pass = sharedPreferences.getString("pass", "Not Avilable");
      //  Toast.makeText(getContext(), "Shared Edit" + Email + id + Pass, Toast.LENGTH_LONG).show();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://100degreesapp.com/API_One.aspx?fn=user-profile&UserID=" + id, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
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
                           // rfemale.setBackgroundResource(R.drawable.ic_radio_on);

                        }
                        if (gender.equals("Male")) {
                            rGrp.check(R.id.radiomale);
                            //rmale.setBackgroundResource(R.drawable.ic_radio_on);
                        }
                    } else {
                        rGrp.check(R.id.radiofemale);
                       // rfemale.setBackgroundResource(R.drawable.ic_radio_on);
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
                        Picasso.with(getContext()).load(img).into(imageView);
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
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);

    }

    public String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }




    private String convertToBase64(String imagePath)

    {

        Bitmap bm = BitmapFactory.decodeFile(imagePath);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);

        byte[] byteArrayImage = baos.toByteArray();

        String encodedImage = Base64.encodeToString(byteArrayImage, Base64.DEFAULT);

        return encodedImage;

    }

    //We are calling this method to check the permission status
    private boolean isReadStorageAllowed() {
        //Getting the permission status
        int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE);

        //If permission is granted returning true
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        //If permission is not granted returning false
        return false;
    }

    //Requesting permission
    private void requestStoragePermission() {

        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //If the user has denied the permission previously your code will come to this block
            //Here you can explain why you need this permission
            //Explain here why you need this permission
        }

        //And finally ask for the permission
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_GALLERY);
    }


    private void selectImage() {
        final CharSequence[] imgItem = {"Take Photo", "Choose from Gallery", "Cancel"};
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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
                imagepath = getPaths(selectedImageUri);
                // Log.d("selectedPath",getPaths(selectedImageUri));
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
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

//    public String getStringImage(Bitmap bmp) {
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
//            byte[] imageBytes = baos.toByteArray();
//            encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
//            Log.d("decode",encodedImage);
//            return encodedImage;
//        } catch (Exception e) {
//
//        }
//        return encodedImage;
//
//    }


    public byte[] getBytesFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        return stream.toByteArray();
    }

    // Get Path of selected image
    private String getPaths(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getContext(), contentUri, proj, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(column_index);
        cursor.close();
        Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
        return result;
    }

//    public void edit_Dob() {
//        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year,
//                                          int monthOfYear, int dayOfMonth) {
//                        Calendar today = Calendar.getInstance();
//                        int yr = today.get(Calendar.YEAR);
//                        int month = 1 + today.get(Calendar.MONTH);
//                        int day = today.get(Calendar.DATE);
//                        switch (monthOfYear) {
//                            case 0:
//                                dateToStr = "Jan";
//                                m = 1;
//                                break;
//                            case 1:
//                                dateToStr = "Feb";
//                                m = 2;
//                                break;
//                            case 2:
//                                dateToStr = "Mar";
//                                m = 3;
//                                break;
//                            case 3:
//                                dateToStr = "Apr";
//                                m = 4;
//                                break;
//                            case 4:
//                                dateToStr = "May";
//                                m = 5;
//                                break;
//                            case 5:
//                                dateToStr = "Jun";
//                                m = 6;
//                                break;
//                            case 6:
//                                dateToStr = "Jul";
//                                m = 7;
//                                break;
//                            case 7:
//                                dateToStr = "Aug";
//                                m = 8;
//                                break;
//                            case 8:
//                                dateToStr = "Sep";
//                                m = 9;
//                                break;
//                            case 9:
//                                dateToStr = "Oct";
//                                m = 10;
//                                break;
//                            case 10:
//                                dateToStr = "Nov";
//                                m = 11;
//                                break;
//                            case 11:
//                                dateToStr = "Dec";
//                                m = 12;
//                                break;
//                        }
//                        if (dayOfMonth < 10) {
//                            agein.setText("0" + dayOfMonth + " " + dateToStr + " " + year);
//                            age = m + "/" + 0 + dayOfMonth + "/" + year;
//                            dob_year = yr - year;
//                            Log.d("dob", age);
//                            Log.d("dob", String.valueOf(age));
//                            if (m > month) {
//                                dob_year--;
//                            }
//
//                        } else {
//                            agein.setText(dayOfMonth + " " + dateToStr + " " + year);
//                            age = m + "/" + dayOfMonth + "/" + year;
//                            age = m + "-" + year;
//                            dob_year = yr - year;
//                            Log.d("dob", String.valueOf(dob_year));
//                            if (m > month) {
//                                dob_year--;
//                            }
//
//                        }
//                    }
//                }, mYear, mMonth, mDay);
//        datePickerDialog.show();
//
//    }
}

