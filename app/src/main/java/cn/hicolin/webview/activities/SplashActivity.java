package cn.hicolin.webview.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.gyf.immersionbar.ImmersionBar;

import cn.hicolin.webview.R;

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    private Button spBtn;
    private ImageView spBg;

    private final CountDownTimer countDownTimer = new CountDownTimer(3200, 1000) {
        @SuppressLint("SetTextI18n")
        @Override
        public void onTick(long millisUntilFinished) {
            spBtn.setText("跳过（" + millisUntilFinished / 1000 + "s）");
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onFinish() {
            spBtn.setText("跳过（" + 0 + "s）");
            gotoMainActivity();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImmersionBar.with(this).fullScreen(true).init();
        // ImmersionBar.hideStatusBar(getWindow());
        bindView();

        String src = "https://up.enterdesk.com/edpic/f2/22/ac/f222acee0112fc7eac90a5f9a584fefe.jpg";
        Glide.with(getBaseContext()).load(src).into(spBg);
        // requestSplash();

        spBtn.setVisibility(View.VISIBLE);
        countDownTimer.start();
    }

    private void bindView() {
        spBg = findViewById(R.id.sp_bg);
        spBtn = findViewById(R.id.sp_btn);

        spBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoMainActivity();

                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void gotoMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

}
