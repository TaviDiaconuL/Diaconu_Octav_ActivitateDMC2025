package com.example.lab09;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class DetailActivity extends AppCompatActivity {
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        webView = findViewById(R.id.webView);
        String encodedUrl = getIntent().getStringExtra("webUrl");
        try {
            String decodedUrl = URLDecoder.decode(encodedUrl, "UTF-8");
            webView.getSettings().setJavaScriptEnabled(true);
            webView.loadUrl(decodedUrl);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }
}
