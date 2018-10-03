package com.degree.college;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks{
    ImageView imageView;
    Button loginbtn, signOut;
    TextView textView;
    EditText edpass;
    String uname, upass;
    String url = "http://100degreesapp.com/API.aspx?fn=login&EmailID=" + uname + "&Password= " + upass;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ArrayList<String> uselList;
    AutoCompleteTextView edlog;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;
    private LoginButton loginButton;
    private String firstName;
    private String lastName;
    private String email,Email;
    private String birthday;
    private String gender;
    private AccessToken accessToken;
    private URL profilePicture;
    private String userId;
    private String TAG = "Fblogin";
    private static final String EMAIL = "email";
    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;
    final static int RC_SIGN_IN = 000;
    GoogleSignInAccount acct;
    private SignInButton btnSignIn;
    String userid;
    ProgressDialog progressDialog;
    GoogleSignInOptions gso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        callbackManager = CallbackManager.Factory.create();

        sharedPreferences = LoginActivity.this.getSharedPreferences("degreelogin", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        imageView = (ImageView) findViewById(R.id.logo);
        imageView.setImageResource(R.drawable.logo_login);
        signOut = (Button) findViewById(R.id.signout);


        uselList = new ArrayList<>();
        textView = (TextView) findViewById(R.id.move_signup);


        //facebook

        loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.setHeight(100);
        loginButton.setTextColor(Color.WHITE);
        loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        loginButton.setCompoundDrawablePadding(0);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                facebook_login();
            }
        });


        //google signin

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                google_login();
            }
        });
        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signOut();
            }
        });

        loginbtn = (Button) findViewById(R.id.button);

        edlog = (AutoCompleteTextView) findViewById(R.id.log_name);
        edpass = (EditText) findViewById(R.id.log_pass);
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        uselList.add(sharedPreferences.getString("user", null));
        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");

        edlog.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String email = edlog.getText().toString();
                if (email.equals(sharedPreferences.getString("user", null))) {
                    edpass.setText(sharedPreferences.getString("pass", null));
                }
                if (email.equals("")) {
                    edpass.setText("");
                }
            }
        });
        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, uselList);
        Log.d("useList", uselList.toString());
        edlog.setAdapter(adapter);
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                uname = edlog.getText().toString().trim();
                upass = edpass.getText().toString().trim();
                StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://100degreesapp.com/API.aspx?fn=login&EmailID=" + uname + "&Password=" + upass, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("res", response);
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getString("Msg").equals("Successfully login")) {
                                editor.putString("id", jsonObject.getString("UserID"));
                                editor.putString("user", uname);
                                editor.putString("pass", upass);
                                editor.apply();
                                uselList.add(sharedPreferences.getString("user", null));
                                progressDialog.dismiss();
                                startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                            }
                            if(jsonObject.getString("Status").equals("False"))
                            {
                                progressDialog.dismiss();
                                new AlertDialog.Builder(LoginActivity.this).setTitle("100 Degrees").setMessage(jsonObject.getString("Msg")).setCancelable(false).setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        edlog.requestFocus();
                                        edlog.setText("");
                                        edpass.setText("");
                                    }
                                }).show();
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
                ) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> map = new HashMap<>();
                        //map.put("EmailID", uname);
                       // map.put("Password", upass);
                        return map;
                    }
                };
                MySingleton.getInstance(LoginActivity.this).addToRequestQueue(stringRequest);


            }
        });
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }

    public void onForgot(View view) {
        Intent i = new Intent(LoginActivity.this, ForgetActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();

//        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
//        if (opr.isDone()) {
//            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
//            // and the GoogleSignInResult will be available instantly.
//            Log.d(TAG, "Got cached sign-in");
//            GoogleSignInResult result = opr.get();
//            handleSignInResult(result);
//        } else {
//            // If the user has not previously signed in on this device or the sign-in has expired,
//            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
//            // single sign-on will occur in this branch.
//            // showProgressDialog();
//            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(GoogleSignInResult googleSignInResult) {
//                    // hideProgressDialog();
//                    handleSignInResult(googleSignInResult);
//                }
//            });
 //       }
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        Intent i = new Intent(LoginActivity.this, SocailUpdateActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//    }

//    public void onSign(View view) {
//        Intent i = new Intent(LoginActivity.this, SocailUpdateActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(i);
//    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(LoginActivity.this);
        mGoogleApiClient.disconnect();
    }

    public void facebook_login() {

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {

                        try {
                            userId = object.getString("id");
                            firstName = object.getString("name");
                            Email = object.getString("email");
                            Log.e("response", userId + " " + firstName + "  " + Email);
                            progressDialog.show();
                            socialLog("social", "facebook", userId, Email, firstName);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...)


        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if(result.isSuccess()) {
                handleSignInResult(result);
            }else{
                Toast.makeText(getApplicationContext(),"Something went wrong",Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void socialLog(final String Authtype, final String Auth, final String AuthId, final String EmailId, final String fname) {
        StringRequest string_Request = new StringRequest(Request.Method.POST, "http://100degreesapp.com/API_One.aspx?fn=social-login", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("resSoc", response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean checksocail = jsonObject.getBoolean("isPasswordField");

                    editor.putString("id", jsonObject.getString("UserID"));
                    editor.commit();
                    if (checksocail) {
                       // Toast.makeText(getApplicationContext(), "redirect to social page", Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, SocailUpdateActivity.class));
                    } else {
                        progressDialog.dismiss();
                        startActivity(new Intent(LoginActivity.this, NavigationActivity.class));
                        //Toast.makeText(getApplicationContext(), "redirect to NavigationActivity", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
               // Log.d("response", error.getMessage());

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("AuthType", Authtype);
                map.put("Auth", Auth);
                map.put("AuthId", AuthId);
                map.put("EmailId", EmailId);
                map.put("Name", fname);
                return map;
            }
        };
        MySingleton.getInstance(LoginActivity.this).addToRequestQueue(string_Request);

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    public void google_login() {


        // Customizing G+ button
        btnSignIn.setSize(SignInButton.SIZE_STANDARD);
         btnSignIn.setScopes(gso.getScopeArray());


        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    @Override
    public void onStop() {
        super.onStop();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            mGoogleApiClient.stopAutoManage(LoginActivity.this);
            mGoogleApiClient.disconnect();
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            acct = result.getSignInAccount();
            userid = acct.getId();
            String personName = acct.getDisplayName();
            Email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + Email+ " ,id" + userid);
            progressDialog.show();
            socialLog("social", "google", userid, Email, personName);
            updateUI(true);
        } else {
            // Signed out, show unauthenticated UI.
            updateUI(false);
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            signOut.setVisibility(View.VISIBLE);

        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            signOut.setVisibility(View.INVISIBLE);

        }
    }

    private void signOut() {
        if (Email != null) {
            Toast.makeText(getApplicationContext(), " Logged in", Toast.LENGTH_SHORT).show();
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                            updateUI(false);
//                            Intent i=new Intent(LoginActivity.this,LoginActivity.class);
//                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                            startActivity(i);
//                            finish();
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "Not Logged in", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(LoginActivity.this,LoginActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
       // finish();
    }
}
