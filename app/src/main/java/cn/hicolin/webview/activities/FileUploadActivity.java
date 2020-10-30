package cn.hicolin.webview.activities;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import cn.hicolin.webview.R;
import cn.hicolin.webview.utils.WebViewUtils;

public class FileUploadActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView mTitle;

    // 文件上传
    private ValueCallback<Uri[]> uploadMessage;
    private static final int FILE_CHOOSER_RESULT_CODE = 10000;

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
        WebView webView = findViewById(R.id.web_view);

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
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    Intent intent = new Intent();

                    if (url.contains("tel:")) {
                        intent.setAction(Intent.ACTION_DIAL);
                        Uri data = Uri.parse(url);
                        intent.setData(data);
                    } else {
                        intent.setClass(getApplicationContext(), WebViewActivity.class);
                        intent.putExtra("url", url);
                    }

                    startActivity(intent);

                    return true;
                }
            });

            webView.setWebChromeClient(new WebChromeClient() {
                @Override
                public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
                    uploadMessage = filePathCallback;
                    openFileChooser();
                    return true;
                }
            });
            webView.addJavascriptInterface(this, "app");
            webView.requestFocus(View.FOCUS_DOWN);
            webView.loadUrl(url);
        }
    }

    // 打开文件上传界面
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(Intent.createChooser(intent, "Image Chooser"), FILE_CHOOSER_RESULT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILE_CHOOSER_RESULT_CODE) {
            if (uploadMessage == null) return;
            Uri[] results = null;
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    String dataString = data.getDataString();
                    ClipData clipData = data.getClipData();
                    if (clipData != null) {
                        results = new Uri[clipData.getItemCount()];
                        for (int i = 0; i < clipData.getItemCount(); i++) {
                            ClipData.Item item = clipData.getItemAt(i);
                            results[i] = item.getUri();
                        }
                    }
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            uploadMessage.onReceiveValue(results);
            uploadMessage = null;
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
