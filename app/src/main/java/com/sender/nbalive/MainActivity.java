package com.sender.nbalive;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends FragmentActivity implements View.OnClickListener {
    private ImageButton gameListButton, topButton, webButton, bbsButton, accountButton;
    private ImageView welImg;
    private TextView page;
    private ViewPager fragmentContainer;
    private List<Fragment> fragmentList;
    private ContentFragment fragmentContent;
    private NewsFragment fragmentNews;
    private WebFragment fragmentLive, fragmentBbs;
    private WebView webTop, webLive, webBbs;
    private int currentSelect;
    private boolean isExit = false;
    //    private float startX, startY, endX, endY;
//    private long startTime, endTime;
    private final String liveUrl = "http://nba.tmiaoo.com/ipad.html";
    private final String bbsUrl = "https://m.hupu.com/bbs";
    private final String topUrl = "https://m.hupu.com/nba/standings";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        fragmentList = new ArrayList<>();
        initView();
        initEvent();
        setSelect(0);
    }


    //  初始化控件
    private void initView() {
        fragmentContainer = (ViewPager) findViewById(R.id.fragment_container);
        gameListButton = (ImageButton) findViewById(R.id.game_list_button);
        topButton = (ImageButton) findViewById(R.id.top_button);
        webButton = (ImageButton) findViewById(R.id.web_button);
        bbsButton = (ImageButton) findViewById(R.id.bbs_button);
        accountButton = (ImageButton) findViewById(R.id.account_button);
        page = (TextView) findViewById(R.id.page);
        welImg = (ImageView) findViewById(R.id.wel_img);
        //初始化Fragment
        fragmentContent = new ContentFragment();
//        fragmentTop = WebFragment.getInstance(topUrl);
        fragmentNews = NewsFragment.getInstance(0);
        fragmentLive = WebFragment.getInstance(liveUrl);
        fragmentBbs = WebFragment.getInstance(bbsUrl);
        fragmentList.add(fragmentContent);
//        fragmentList.add(fragmentTop);
        fragmentList.add(fragmentNews);
        fragmentList.add(fragmentLive);
        fragmentList.add(fragmentBbs);
        fragmentContainer.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });


        final FrameLayout welBg = (FrameLayout) findViewById(R.id.bg_wel);
        hideWel(welBg);
    }

    //  隐藏欢迎界面
    private void hideWel(final FrameLayout frameLayout) {
        AnimationSet outSet = getOutSet();
        outSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                frameLayout.startAnimation(AnimationUtils.loadAnimation(MainActivity.this, R.anim.content_out));
                frameLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        welImg.startAnimation(outSet);
    }

    //  设置监听器
    private void initEvent() {
        gameListButton.setOnClickListener(this);
        topButton.setOnClickListener(this);
        webButton.setOnClickListener(this);
        bbsButton.setOnClickListener(this);
        accountButton.setOnClickListener(this);
        fragmentContainer.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setSelect(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        webTop.setOnTouchListener(this);
//        webLive.setOnTouchListener(this);
//        webBbs.setOnTouchListener(this);

    }

    //  选择显示的页面
    private void setSelect(int i) {
        switch (i) {

            case 0:
                reset();
                gameListButton.setImageResource(R.drawable.game_list_pressed);
                page.setText("赛程");
                break;
            case 1:
                reset();
                topButton.setImageResource(R.drawable.top_pressed);
                page.setText("新闻");
//                page.setText("球队排名");
//                if (webTop == null) {
//                    webTop = fragmentTop.getWebView();
//                }
                break;

            case 2:
                reset();
                page.setText("直播");
                if (webLive == null) {
                    webLive = fragmentLive.getWebView();
                }

                break;
            case 3:
                reset();

                bbsButton.setImageResource(R.drawable.bbs_pressed);
                page.setText("论坛");
                if (webBbs == null) {
                    webBbs = fragmentBbs.getWebView();
                }
                break;


        }

        currentSelect = i;
    }

    //  将所有Tab按钮设置成灰色
    private void reset() {
        gameListButton.setImageResource(R.drawable.game_list_normal);
        topButton.setImageResource(R.drawable.top_normal);
        bbsButton.setImageResource(R.drawable.bbs_normal);
        accountButton.setImageResource(R.drawable.account_normal);
    }


    //  获取欢迎页面动画
    private AnimationSet getOutSet() {
        AnimationSet outSet = new AnimationSet(true);

        RotateAnimation animOut1 = new RotateAnimation(
                0.0f, 360.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        ScaleAnimation animOut2 = new ScaleAnimation(
                1.0f, 0.0f, 1.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        outSet.addAnimation(animOut1);
        outSet.addAnimation(animOut2);
        outSet.setDuration(1000);
        outSet.setStartOffset(1000);
        outSet.setFillAfter(true);
        return outSet;
    }

    //  设置Tab点击事件
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.game_list_button:
                if (currentSelect != 0) {
                    fragmentContainer.setCurrentItem(0, true);
                    setSelect(0);
                }
                break;
            case R.id.top_button:
                if (currentSelect != 1) {
                    fragmentContainer.setCurrentItem(1, true);
                    setSelect(1);
//                } else {
//                    fragmentTop.refresh();
//                    webTop.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_refresh));
                }
                break;
            case R.id.web_button:
                if (currentSelect != 2) {
                    fragmentContainer.setCurrentItem(2, true);
                    setSelect(2);
                } else {
                    fragmentLive.refresh();
                    webLive.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_refresh));
                }
                break;
            case R.id.bbs_button:
                if (currentSelect != 3) {

                    fragmentContainer.setCurrentItem(3, true);

                    setSelect(3);
                } else {
                    fragmentBbs.refresh();
                    webBbs.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_refresh));
                }
                break;
            case R.id.account_button:
                break;

        }
    }

    //  设置返回键事件
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            switch (currentSelect) {
                case 0:
                    dblClickExit();
                    break;
                case 1:
//                    if (webTop.canGoBack()) {
//                        webTop.goBack();
//                    } else {
                        dblClickExit();
//                    }
                    break;
                case 2:
                    if (webLive.canGoBack()) {
                        webLive.goBack();
                    } else {
                        dblClickExit();
                    }
                    break;
                case 3:
                    if (webBbs.canGoBack()) {
                        webBbs.goBack();
                    } else {
                        dblClickExit();
                    }
                    break;
            }
        }
        return false;
    }

    //  双击返回键退出
    private void dblClickExit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
            Timer tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            finish();
            System.exit(0);
        }
    }

//    //  设置webView右划返回
//    @Override
//    public boolean onTouch(View view, MotionEvent motionEvent) {
//        WebView webView = (WebView) view;
//            switch (motionEvent.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    startX = motionEvent.getX();
//                    startY = motionEvent.getY();
//                    startTime = System.currentTimeMillis();
//
//                    break;
//                case MotionEvent.ACTION_UP:
//                    endX = motionEvent.getX();
//                    endY = motionEvent.getY();
//                    endTime = System.currentTimeMillis();
//                    if (endX - startX > 200 && endTime - startTime < 500) {
//                        final int limitY = 50;
//                        if (endY > startY && endY - startY < limitY || endY < startY && startY - endY < limitY) {
//                            if (webView.canGoBack()) {
//                                webView.goBack();
//                            }
//                        }
//                    }
//                    break;
//            }
//        return false;
//    }


}
