package com.degree.college;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocationActivity extends FragmentActivity implements OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = "myCheck";
    private GoogleMap mMap;
    Bundle bundle;
    String deslocation;
    GoogleApiClient mGoogleApiClient;
    Marker currLocationMarker;
    LocationRequest mLocationRequest;
    LatLng latLng, desLatlang;
    String origin, output, from, to;
    Location Location;
    //LinearLayout lineDis, lineTime;
    TextView txtDes, txtTime, txtTitle;
    PolylineOptions options;
    Toolbar toolbar;
    ImageView imgBack,ImageConn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        toolbar = (Toolbar) findViewById(R.id.toolbar_group_channel);
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.detail_tool);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        ImageConn=findViewById(R.id.toolchat);
        ImageConn.setImageResource(0);
        imgBack = (ImageView) findViewById(R.id.id);
        txtTitle = (TextView) findViewById(R.id.title);
        imgBack.setImageResource(R.drawable.ic_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        bundle = getIntent().getExtras();

        checkLocationPermission(); deslocation = bundle.getString("location");
        // txtDes = findViewById(R.id.edDis);
        // txtTime = findViewById(R.id.edTime);
        // lineDis = (LinearLayout) findViewById(R.id.LinDis);
        //  lineTime = findViewById(R.id.LinTime);
        // lineTime.setVisibility(View.GONE);
        //lineDis.setVisibility(View.INVISIBLE);

        buildGoogleApiClient();
        // disTime();


    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    private boolean checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {


            if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.
                    Manifest.permission.ACCESS_FINE_LOCATION)) {


                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(LocationActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {

                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                    if (ContextCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        finish();
                        startActivity(getIntent());
                        //  mMap.setMyLocationEnabled(true);
                    }

                } else {


                    Toast.makeText(this, "permission denied", Toast.LENGTH_LONG).show();
                }
                return;
            }


        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            return;
        }
        mMap.setMyLocationEnabled(true);
        // Add a marker in Sydney and move the camera
        buildGoogleApiClient();
        mGoogleApiClient.connect();
        //getAddressFromLocation(Location, LocationActivity.this, new GeoCoderHandler());
        try {
            // String location = "theNameOfTheLocation";
            Geocoder gc = new Geocoder(this);
            List<Address> addresses = gc.getFromLocationName(deslocation, 5); // get the found Address Objects
            // List<Address> addresses = gc.getFromLocationName("Noida,New Delhi", 5);
            List<LatLng> ll = new ArrayList<LatLng>(addresses.size()); // A list to save the coordinates if they are available
            for (Address a : addresses) {
                if (a.hasLatitude() && a.hasLongitude()) {
                    ll.add(new LatLng(a.getLatitude(), a.getLongitude()));
                    desLatlang = new LatLng(a.getLatitude(), a.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(desLatlang).title("Event is here"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(desLatlang, 5));
                    to = a.getLatitude() + "," + a.getLongitude();
                }

            }
        } catch (IOException e) {
            // handle the exception
            Log.d("arrayAddress", e.toString());
        }
     //   polyDraw(mMap, from);

        // lineDraw(mMap);
    }


    @SuppressLint("RestrictedApi")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            checkLocationPermission();
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {

            latLng = new LatLng(mLastLocation.getLatitude(), mLastLocation.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("Current Position");
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
            currLocationMarker = mMap.addMarker(markerOptions);
            //polyDraw(mMap);
       //     from = mLastLocation.getLatitude() + "," + mLastLocation.getLongitude();

        }

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    public void lineDraw(GoogleMap googleMap) {
//        Polyline line = googleMap.addPolyline(new PolylineOptions()
//                .add(desLatlang,new LatLng(40.7128,74.0060))
//               // .add( new LatLng(40.7128,74.0060),new LatLng(38.9072,77.0369))
//                .width(5)
//                .color(Color.RED).geodesic(true));
      //  polyDraw(googleMap, from);


    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {
        Location = location;
        if (currLocationMarker != null) {
            currLocationMarker.remove();
        }
        latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Position");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        currLocationMarker = mMap.addMarker(markerOptions);

        // mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        getAddressFromLocation(location, LocationActivity.this, new LocationActivity.GeoCoderHandler());
//        if (from != null) {
//            from = location.getLatitude() + "," + location.getLongitude();
//        }
        from=location.getLatitude()+","+location.getLongitude();
        //disTime();
        polyDraw(mMap,from);

    }

    public void getAddressFromLocation(final Location loc, final Context context, final Handler handler) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                String result = null;

                try {
                    List<Address> list = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                    if (list != null && list.size() > 0) {
                        Address address = list.get(0);
                        // from=loc.getLatitude()+","+loc.getLongitude();
                        result = address.getAddressLine(0) + " ," + address.getLocality() + " ," + address.getCountryName();
                    }
                } catch (IOException e) {
                    Log.e("Location", "Impossible to connect to Geocoder", e);
                } finally {
                    Message msg = Message.obtain();
                    msg.setTarget(handler);
                    if (result != null) {
                        msg.what = 1;
                        Bundle bundle = new Bundle();
                        bundle.putString("address", result);
                        msg.setData(bundle);
                    } else
                        msg.what = 0;
                    msg.sendToTarget();
                }
            }
        };
        thread.start();


    }
//    public static void getAddressFromLocation(final String locationAddress,
//                                              final Context context, final Handler handler) {
//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//                String result = null;
//                try {
//                    List addressList = geocoder.getFromLocationName(locationAddress, 1);
//                    if (addressList != null && addressList.size() > 0) {
//                        Address address = (Address) addressList.get(0);
//                        StringBuilder sb = new StringBuilder();
//                        sb.append(address.getLatitude()).append("\n");
//                        sb.append(address.getLongitude()).append("\n");
//                        result = sb.toString();
//                    }
//                } catch (IOException e) {
//                    Log.e(TAG, "Unable to connect to Geocoder", e);
//                } finally {
//                    Message message = Message.obtain();
//                    message.setTarget(handler);
//                    if (result != null) {
//                        message.what = 1;
//                        Bundle bundle = new Bundle();
//                        result = "Address: " + locationAddress +
//                                "\n\nLatitude and Longitude :\n" + result;
//                        bundle.putString("address", result);
//                        message.setData(bundle);
//
//
//                    } else {
//                        message.what = 1;
//                        Bundle bundle = new Bundle();
//                        result = "Address: " + locationAddress +
//                                "\n Unable to get Latitude and Longitude for this address location.";
//                        bundle.putString("address", result);
//                        message.setData(bundle);
//                    }
//                    message.sendToTarget();
//                }
//            }
//        };
//        thread.start();
//    }


//    public void disTime() {
//        Log.i("disTime", origin + "   " + deslocation);
//        String desti = deslocation.replaceAll("\\s", "+");
//        desti = "Aligarh,Uttar+Pradesh,India";
//        //origin = "Washington DC USA";
//        String from = origin.replaceAll("\\s", "+");
//        // from="New+Delhi,India";
//        StringRequest stringRequest = new StringRequest(Request.Method.GET,
//                "https://maps.googleapis.com/maps/api/distancematrix/json?units=imperial&origins=" + from + "&destinations=" + desti + "&key=AIzaSyC9ZHECEi-T0lVltwCXOe7IoBb0Jen9mXU",
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Log.d("distance", response);
//                        try {
//
//                            JSONObject jsonRespRouteDistance = new JSONObject(response)
//                                    .getJSONArray("rows")
//                                    .getJSONObject(0)
//                                    .getJSONArray("elements")
//                                    .getJSONObject(0);
//
//
//                            String distance = jsonRespRouteDistance.getJSONObject("distance").get("text").toString();
//                            String time = jsonRespRouteDistance.getJSONObject("duration").get("text").toString();
//                            String status = jsonRespRouteDistance.getString("status");
//                            Log.e("jsonValues", distance + "time " + time + " " + status);
////                            if (status.equals("OK")) {
////                                lineDis.setVisibility(View.VISIBLE);
////                                lineTime.setVisibility(View.VISIBLE);
////                                txtDes.setText(distance);
////                                txtTime.setText(time);
////                            }
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                        }
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//        MySingleton.getInstance(LocationActivity.this).addToRequestQueue(stringRequest);
//
//
//    }

    public void polyDraw(final GoogleMap goglemap, String frm) {
        Log.i("disTime", this.from + "   " + to);
        final String desti = deslocation.replaceAll("\\s", "+");
        // origin = "Washington DC USA";
        String from = frm;
        final String[] distance = new String[1];
        final String[] duration = new String[1];
        String d = "Aligarh,Uttar+Pradesh,India";

        StringRequest stringRequest1 = new StringRequest(Request.Method.GET,
                "https://maps.googleapis.com/maps/api/directions/json?origin=" + "Chapel+Hill" + "&destination=" + "Durham" + "&mode=driving&key=AIzaSyCwhBYM5bBbCuGH035v9zPGkzxj5a8kTho",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("poly", response);
                        try {
                            // Tranform the string into a json object
                            final JSONObject json = new JSONObject(response);
                            JSONArray routeArray = json.getJSONArray("routes");

                            String status = json.getString("status");

                            for (int i = 0; i < routeArray.length(); i++) {
                                JSONObject routes = routeArray.getJSONObject(0);
                                JSONObject overviewPolylines = routes
                                        .getJSONObject("overview_polyline");
                                distance[0] = json.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("distance").getString("text");
                                duration[0] = json.getJSONArray("routes").getJSONObject(0).getJSONArray("legs").getJSONObject(0).getJSONObject("duration").getString("text");
                                Log.d("DD", distance[0] + "     " + duration[0]);

                                String encodedString = overviewPolylines.getString("points");
                                Log.d("poly", encodedString);
                                List<LatLng> list = decodePoly(encodedString);

                                //if(status.equals("ZERO_RESULTS")) {

//                                lineDis.setVisibility(View.INVISIBLE);
//                                lineTime.setVisibility(View.INVISIBLE);
//                                if (distance[0].length() != 0) {
//                                    lineDis.setVisibility(View.VISIBLE);
//                                    txtDes.setText(distance[0]);
//                                }
//                                if (duration[0].length() != 0) {
//                                    lineTime.setVisibility(View.VISIBLE);
//                                    txtTime.setText(duration[0]);
//                                }
                                options = new PolylineOptions().width(8).color(Color.BLACK).geodesic(true);
                                for (int z = 0; z < list.size(); z++) {
                                    LatLng point = list.get(z);
                                    options.add(point);
                                }
                                goglemap.addPolyline(options);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Log.d("poly",error.getMessage());

            }
        });
        MySingleton.getInstance(LocationActivity.this).addToRequestQueue(stringRequest1);


    }

    private List<LatLng> decodePoly(String encoded) {

        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng((((double) lat / 1E5)),
                    (((double) lng / 1E5)));
            poly.add(p);
        }

        return poly;
    }


    public class GeoCoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String result;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    result = bundle.getString("address");
                    origin = result;
                    Log.d(TAG, result);


                    break;
                default:
                    result = null;
            }


        }
    }


}
