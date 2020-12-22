package cn.hicolin.webview.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import cn.hicolin.webview.R;
import cn.hicolin.webview.utils.WebViewUtils;

public class VideoActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle;
    private WebView webView;

    /** 视频全屏参数 **/
    protected static final FrameLayout.LayoutParams COVER_SCREEN_PARAMS = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    private View customView;
    private FrameLayout fullscreenContainer;
    private WebChromeClient.CustomViewCallback customViewCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        bindView();
    }

    @SuppressLint({"SetJavaScriptEnabled", "JavascriptInterface", "AddJavascriptInterface"})
    private void bindView() {
        RelativeLayout back = findViewById(R.id.back);
        webView = findViewById(R.id.web_view);
        mTitle = findViewById(R.id.top_bar_title);
        final RelativeLayout loading = findViewById(R.id.loading);

        back.setOnClickListener(this);

        // 加载 WebView
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            String url = bundle.getString("url");

            webView.getSettings().setJavaScriptEnabled(true);
            WebViewUtils.setLocalStorageEnable(this, webView);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                WebView.setWebContentsDebuggingEnabled(true);
            }

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageCommitVisible(WebView view, String url) {
                    super.onPageCommitVisible(view, url);
                    loading.setVisibility(View.GONE);
                }
            });
            webView.setWebChromeClient(new WebChromeClient() {
                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public View getVideoLoadingProgressView() {
                    FrameLayout frameLayout = new FrameLayout(getApplicationContext());
                    frameLayout.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    return frameLayout;
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onShowCustomView(View view, CustomViewCallback callback) {
                    showCustomView(view, callback);
                }

                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                @Override
                public void onHideCustomView() {
                    hideCustomView();
                }
            });
            webView.addJavascriptInterface(this, "app");
            webView.requestFocus(View.FOCUS_DOWN);
            webView.loadUrl(url);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showCustomView(View view, WebChromeClient.CustomViewCallback callback) {
        if (customView != null) {
            callback.onCustomViewHidden();
            return;
        }

        // 设置横屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        fullscreenContainer = new VideoActivity.FullscreenHolder(getApplicationContext());
        fullscreenContainer.addView(view, COVER_SCREEN_PARAMS);
        decor.addView(fullscreenContainer, COVER_SCREEN_PARAMS);
        customView = view;
        customViewCallback = callback;
        setStatusBarVisibility(false);
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void hideCustomView() {
        if (customView == null) {
            return;
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        FrameLayout decor = (FrameLayout) getWindow().getDecorView();
        decor.removeView(fullscreenContainer);
        fullscreenContainer = null;
        customView = null;
        customViewCallback.onCustomViewHidden();
        webView.setVisibility(View.VISIBLE);
        setStatusBarVisibility(true);
    }

    private void setStatusBarVisibility(boolean visible) {
        int flag = visible ? 0 : WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    class FullscreenHolder extends FrameLayout {

        public FullscreenHolder(@NonNull Context context) {
            super(context);
            setBackgroundColor(context.getResources().getColor(android.R.color.black));
        }

        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return true;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        @Override
        public boolean onKeyUp(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                if (customView != null) {
                    hideCustomView();
                } else if (webView.canGoBack()) {
                    webView.goBack();
                } else {
                    finish();
                }
            }

            return super.onKeyUp(keyCode, event);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.back) {
            super.onBackPressed();
        }
    }

    @JavascriptInterface
    public void setTitle(String title) {
        mTitle.setText(title);
    }

}
