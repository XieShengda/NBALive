package com.sender.nbalive;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;


/**
 * Created by XieShengda on 2016/12/28.
 */

public class WebFragment extends Fragment {
    private WebView webView;
    private String urlString;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        urlString = bundle.getString("content_url");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_web, container, false);
        initWebView(view);

        return view;
    }

    private void initWebView(View view) {
        webView = (WebView) view.findViewById(R.id.web_view);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);

        webView.loadUrl(urlString);
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                webView.loadUrl(url);
                return true;
            }
        });

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public View getVideoLoadingProgressView() {
                FrameLayout frameLayout = new FrameLayout(getActivity());
                frameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                return frameLayout;
            }
        });
    }

    public static WebFragment getInstance(String urlString){
        WebFragment webFragment = new WebFragment();
        Bundle bundle = new Bundle();
        bundle.putString("content_url", urlString);
        webFragment.setArguments(bundle);
        return webFragment;
    }

    public WebView getWebView() {
        return webView;
    }


    public void refresh(){
        webView.loadUrl(urlString);
    }
}
