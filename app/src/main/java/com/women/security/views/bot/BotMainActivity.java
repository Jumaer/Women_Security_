package com.women.security.views.bot;


import android.os.Bundle;
import android.webkit.WebView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.women.security.R;

public class BotMainActivity extends AppCompatActivity
{
    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bot);

        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyWebViewClient());

        String url = "https://console.dialogflow.com/api-client/demo/embedded/79f47883-d0e3-4fe5-ad97-fced1bc3e627";
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);
    }
}
