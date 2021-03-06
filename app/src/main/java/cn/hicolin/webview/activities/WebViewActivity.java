package cn.hicolin.webview.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.GeolocationPermissions;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.blankj.utilcode.util.DeviceUtils;

import cn.hicolin.webview.R;
import cn.hicolin.webview.utils.CleanDataUtils;
import cn.hicolin.webview.utils.WebViewUtils;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView mTitle;
    public RelativeLayout mLoading;
    public WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        bindView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface"})
    private void bindView() {
        RelativeLayout back = findViewById(R.id.back);
        mTitle = findViewById(R.id.top_bar_title);
        mLoading = findViewById(R.id.loading);
        webView = findViewById(R.id.web_view);

        back.setOnClickListener(this);

        // 加载 WebView
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String url = bundle.getString("url");

            webView.getSettings().setJavaScriptEnabled(true);
            WebViewUtils.setLocalStorageEnable(this, webView);

            // 定位相关
            webView.getSettings().setGeolocationDatabasePath(this.getFilesDir().getPath());
            webView.getSettings().setDatabaseEnabled(true);
            webView.getSettings().setGeolocationEnabled(true);
            webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Intent intent = new Intent();

                    if (url.contains("tel:")) { // 拨打电话
                        intent.setAction(Intent.ACTION_DIAL);
                        Uri data = Uri.parse(url);
                        intent.setData(data);
                    } else {
                        // 示例URL
                        if (url.contains("file_upload")) {
                            intent.setClass(getApplicationContext(), FileUploadActivity.class);
                        }
                        else if (url.contains("video")) {
                            intent.setClass(getApplicationContext(), VideoActivity.class);
                        }
                        else {
                            intent.setClass(getApplicationContext(), WebViewActivity.class);
                        }
                        intent.putExtra("url", url);
                    }

                    startActivity(intent);
                    return true;
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    mLoading.setVisibility(View.INVISIBLE);
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                    callback.invoke(origin, true, false);
                    super.onGeolocationPermissionsShowPrompt(origin, callback);
                }
            });
            webView.addJavascriptInterface(this, "app");
            webView.requestFocus(View.FOCUS_DOWN); // 解决 <textarea> 无法聚焦
            webView.loadUrl(url);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            super.onBackPressed();
        }
    }

    // 标题设置
    @JavascriptInterface
    public void setTitle(String title) {
        mTitle.setText(title);
    }

    // 缓存清理
    @JavascriptInterface
    public void clearCache() {
        CleanDataUtils.clearAllCache(this);
    }

    // 获取缓存大小
    @JavascriptInterface
    public String getCacheSize() {
        return CleanDataUtils.getTotalCacheSize(this);
    }

    // 获取设备ID（UUID）
    @JavascriptInterface
    public String getDeviceId() {
        return DeviceUtils.getUniqueDeviceId();
    }

    // 页面刷新
    @JavascriptInterface
    public void reload() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                webView.reload();
            }
        });
    }

}
