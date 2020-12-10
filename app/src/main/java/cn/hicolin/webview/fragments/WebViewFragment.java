package cn.hicolin.webview.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.hicolin.webview.R;
import cn.hicolin.webview.activities.WebViewActivity;
import cn.hicolin.webview.utils.WebViewUtils;

public abstract class WebViewFragment extends Fragment implements View.OnClickListener {

    public WebView webView;
    public RelativeLayout mLoading;

    @SuppressLint({"SetJavaScriptEnabled", "AddJavascriptInterface"})
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);

        webView = view.findViewById(R.id.web_view);
        mLoading = view.findViewById(R.id.loading);

        webView.getSettings().setJavaScriptEnabled(true);
        if (getActivity() != null) {
            WebViewUtils.setLocalStorageEnable(getActivity(), webView);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true);
        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return urlLoading(view, url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                mLoading.setVisibility(View.INVISIBLE);
            }
        });

        webView.setWebChromeClient(new WebChromeClient());
        webView.requestFocus(View.FOCUS_DOWN);
        webView.addJavascriptInterface(this, "app");
        loadUrl(webView);

        return view;
    }

    // 实现 webView.loadUrl() 方法
    abstract public void loadUrl(WebView webView);

    public boolean urlLoading(WebView view, String url) {
        if (getActivity() != null) {
            Intent intent = new Intent();
            intent.setClass(getActivity(), WebViewActivity.class);
            intent.putExtra("url", url);
            startActivity(intent);
        }
        return true;
    }

    @Override
    public void onClick(View view) {

    }

    @JavascriptInterface
    public void reload() {
        if (getActivity() != null) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.reload();
                }
            });
        }
    }



}
