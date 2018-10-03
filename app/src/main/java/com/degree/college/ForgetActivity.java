package com.degree.college;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgetActivity extends AppCompatActivity {
    TextView title, cancel;
    ImageView imageback;
    EditText editText;Button btnForget;
    String email;
 ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.signtool);
        title = (TextView) findViewById(R.id.titlesign);
        cancel = (TextView) findViewById(R.id.cancelsign);
        imageback = (ImageView) findViewById(R.id.imgBack);
        progressDialog=new ProgressDialog(ForgetActivity.this);
        progressDialog.setMessage("Processing....");
        editText=(EditText)findViewById(R.id.editText);
        btnForget=(Button)findViewById(R.id.button2);
        title.setText("Forgot Password");
        imageback.setImageResource(R.drawable.ic_back);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgetActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        imageback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ForgetActivity.this, LoginActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                finish();
            }
        });
        btnForget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                forgotPass();
            }
        });
    }
    public void forgotPass(){

        email=editText.getText().toString();

        StringRequest stringRequest= new StringRequest(Request.Method.GET, "http://100degreesapp.com/API_One.aspx?fn=forgot-password&EmailID="+email, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonObject=new JSONObject(response);
                    Log.d("forgot",response);
                    if(jsonObject.getString("Status").equals("True")){
                        progressDialog.dismiss();
                        editText.setText("");
                      Toast.makeText(getApplicationContext(),jsonObject.getString("Msg"),Toast.LENGTH_SHORT).show();
                    }
                    if(jsonObject.getString("Status").equals("False try")){
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(),jsonObject.getString("Msg"),Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> forgetmap=new HashMap<>();
              //  forgetmap.put("EmailID",email);
                return forgetmap;
            }
        };
        MySingleton.getInstance(ForgetActivity.this).addToRequestQueue(stringRequest);


    }
}
