package cn.hicolin.webview.utils;

import android.content.Context;
import android.webkit.WebSettings;
import android.webkit.WebView;

public class WebViewUtils {

    // 设置localStorage可用
    public static void setLocalStorageEnable(Context context, WebView webView) {
        String appCachePath = context.getCacheDir().getAbsolutePath();
        WebSettings webSettings = webView.getSettings();
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAppCacheMaxSize(1024 * 1024 * 8);
        webSettings.setAppCachePath(appCachePath);
        webSettings.setAllowFileAccess(true);
        webSettings.setAppCacheEnabled(true);
    }

}
