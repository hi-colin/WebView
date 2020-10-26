package cn.hicolin.webview.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.blankj.utilcode.util.ToastUtils;

import cn.hicolin.webview.R;
import cn.hicolin.webview.fragments.CenterFragment;
import cn.hicolin.webview.fragments.HomeFragment;
import cn.hicolin.webview.fragments.MineFragment;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView topBarTitle;
    private LinearLayout tabHome, tabCenter, tabMine;
    private FragmentManager fragmentManager;

    private HomeFragment homeFragment;
    private CenterFragment centerFragment;
    private MineFragment mineFragment;

    private static int FRAGMENT_INDEX;
    private static final int HOME_FRAGMENT_INDEX = 0;
    private static final int CENTER_FRAGMENT_INDEX = 1;
    private static final int MINE_FRAGMENT_INDEX = 2;

    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentManager = getSupportFragmentManager();
        bindView();
        tabHome.performClick();

    }

    private void bindView() {
        topBarTitle = findViewById(R.id.top_bar_title);
        tabHome = findViewById(R.id.tab_home);
        tabCenter = findViewById(R.id.tab_center);
        tabMine = findViewById(R.id.tab_mine);

        tabHome.setOnClickListener(this);
        tabCenter.setOnClickListener(this);
        tabMine.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        hideAllFragments(fragmentTransaction);

        switch (v.getId()) {
            case R.id.tab_home:
                resetSelected();
                tabHome.setSelected(true);
                topBarTitle.setText(getString(R.string.txt_home));
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    fragmentTransaction.add(R.id.main_content, homeFragment);
                } else {
                    fragmentTransaction.show(homeFragment);
                }
                FRAGMENT_INDEX = HOME_FRAGMENT_INDEX;
                break;

            case R.id.tab_center:
                resetSelected();
                tabCenter.setSelected(true);
                topBarTitle.setText(getString(R.string.txt_center));
                if (centerFragment == null) {
                    centerFragment = new CenterFragment();
                    fragmentTransaction.add(R.id.main_content, centerFragment);
                } else {
                    fragmentTransaction.show(centerFragment);
                }
                FRAGMENT_INDEX = CENTER_FRAGMENT_INDEX;
                break;

            case R.id.tab_mine:
                resetSelected();
                tabMine.setSelected(true);
                topBarTitle.setText(R.string.txt_mine);
                if (mineFragment == null) {
                    mineFragment = new MineFragment();
                    fragmentTransaction.add(R.id.main_content, mineFragment);
                } else {
                    fragmentTransaction.show(mineFragment);
                }
                FRAGMENT_INDEX = MINE_FRAGMENT_INDEX;
                break;
        }

        fragmentTransaction.commit();
    }

    private void hideAllFragments(FragmentTransaction fragmentTransaction) {
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (centerFragment != null) {
            fragmentTransaction.hide(centerFragment);
        }
        if (mineFragment != null) {
            fragmentTransaction.hide(mineFragment);
        }
    }

    private void resetSelected() {
        tabHome.setSelected(false);
        tabCenter.setSelected(false);
        tabMine.setSelected(false);
    }

    @Override
    public void onBackPressed() {
        switch (FRAGMENT_INDEX) {
            case HOME_FRAGMENT_INDEX:
                if (homeFragment.webView != null && homeFragment.webView.canGoBack()) {
                    homeFragment.webView.goBack();
                } else {
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        ToastUtils.showShort(getString(R.string.press_again_to_exit));
                        exitTime = System.currentTimeMillis();
                    } else {
                        super.onBackPressed();
                    }
                }
                break;

            case CENTER_FRAGMENT_INDEX:
                if (centerFragment.webView != null && centerFragment.webView.canGoBack()) {
                    centerFragment.webView.goBack();
                } else {
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        ToastUtils.showShort(getString(R.string.press_again_to_exit));
                        exitTime = System.currentTimeMillis();
                    } else {
                        super.onBackPressed();
                    }
                }
                break;
            case MINE_FRAGMENT_INDEX:
                if (mineFragment.webView != null && mineFragment.webView.canGoBack()) {
                    mineFragment.webView.goBack();
                } else {
                    if (System.currentTimeMillis() - exitTime > 2000) {
                        ToastUtils.showShort(getString(R.string.press_again_to_exit));
                        exitTime = System.currentTimeMillis();
                    } else {
                        super.onBackPressed();
                    }
                }
                break;
        }
    }

}
