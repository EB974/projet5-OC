package com.eric_b.mynews.views;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.eric_b.mynews.R;


public class NewsWebView extends AppCompatActivity {

    final String NEWS_URL = "News_URL";
    String url;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        Intent intent = getIntent();

        pd = new ProgressDialog(this);
        pd.setMessage("Please wait Loading...");


        WebView view = findViewById(R.id.webview);
        view.clearCache(true);

        view.getSettings().setLoadsImagesAutomatically(true);
        view.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        view.canGoBack();
        view.setWebViewClient(new MyBrowser());
        if (intent != null) {
            url = intent.getStringExtra(NEWS_URL);
            pd.show();
            view.loadUrl(url);
        }

    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (!pd.isShowing()) {
                pd.show();
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (pd.isShowing()) {
                pd.dismiss();
            }

        }

    }

}
