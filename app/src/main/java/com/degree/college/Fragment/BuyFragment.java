package com.degree.college.Fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.degree.college.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class BuyFragment extends Fragment {
ProgressDialog progressDialog;
WebView webView;
WebViewClient webClient;
Bundle bundle;
String url;

    public BuyFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_buy, container, false);
        webView = (WebView)v.findViewById(R.id.webview);

        url=getArguments().getString("buy");
        Toast.makeText(getContext(),url,Toast.LENGTH_SHORT).show();


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMax(40);
        progressDialog.setMessage("Loading....");
        progressDialog.show();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setDomStorageEnabled(true);
        webView.setHorizontalScrollBarEnabled(false);
        webView.loadUrl(url);
        webClient=new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {

                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        };
        return v;
    }

}
