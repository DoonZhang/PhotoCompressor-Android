package cn.mepstudio.photocompressor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.xiaomi.mistatistic.sdk.MiStatInterface;
import com.xiaomi.mistatistic.sdk.URLStatsRecorder;
import com.xiaomi.mistatistic.sdk.controller.HttpEventFilter;
import com.xiaomi.mistatistic.sdk.data.HttpEvent;

/**
 * Created by ZhangDong on 2017/10/19.
 * E-Mail: DoonZhang@qq.com
 * Copyright (c) 2017。 ZhangDong All rights reserved.
 */


public class SplashActivity extends AppCompatActivity {

    protected Context mContext;
    public boolean isfirst;
    private String appID = "2882303761517626159";
    private String appKey = "5291762698159";
    public static final String TAG = "cn.mepstudio.photocompressor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //去掉标题栏
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_splash);
        //判断是否第一次启动程序
        isFirst();

        //初始化小米统计服务
        MiStatInterface.initialize(this, appID, appKey,"original");
        MiStatInterface.setUploadPolicy(
                MiStatInterface.UPLOAD_POLICY_WHILE_INITIALIZE, 0);
        MiStatInterface.enableLog();
        // enable exception catcher.
        MiStatInterface.enableExceptionCatcher(true);
        // enable network monitor
        URLStatsRecorder.enableAutoRecord();
        URLStatsRecorder.setEventFilter(new HttpEventFilter() {

            @Override
            public HttpEvent onEvent(HttpEvent event) {
                // returns null if you want to drop this event.
                // you can modify it here too.
                return event;
            }
        });

        //方法2：在Activity的layout文件里添加webview控件：
        WebView webview = (WebView) findViewById(R.id.ad_web);
        webview.setWebViewClient(new WebViewClient()
        {
            //覆盖shouldOverrideUrlLoading 方法
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url)
            {
                view.loadUrl(url);
                return true;
            }
        });
      //  webview.loadUrl("http://s.moreplay.cn");
        WebSettings settings = webview.getSettings();
        settings.setUserAgentString("Mozilla/5.0 (Linux; U; Android 6.0; zh-cn) AppleWebKit/537.36 (KHTML, like Gecko)Version/4.0 Chrome/37.0.0.0 MQQBrowser/7.9 Mobile Safari/537.36");//添加UA,  “app/XXX”：是与h5商量好的标识，h5确认UA为app/XXX就认为该请求的终端为App
        settings.setJavaScriptEnabled(true);

        //设置参数
        settings.setBuiltInZoomControls(true);
        settings.setAppCacheEnabled(true);// 设置缓存
        webview.setWebChromeClient(new WebChromeClient());
    }

    protected void isFirst(){

        SharedPreferences userSettings = getSharedPreferences("setting", MODE_PRIVATE);
        SharedPreferences.Editor editor = userSettings.edit();
        boolean isFirstRun = userSettings.getBoolean("isFirstRun", true);
        if (isFirstRun) {
            //如果是第一次跳转到介绍界面
            Intent intent = new Intent(SplashActivity.this, GuideActivity.class);
            isfirst = true;
            startActivity(intent);
            //使用editor保存数据，（键值名，数值）
            editor.putBoolean("isFirstRun", false);
            //默认储存文件夹名
            editor.putString("user_defined_files","照片压缩大师");
            editor.putInt("user_defined_quality",100);
            editor.putInt("user_defined_bat_quality",80);
            editor.putInt("user_defined_size",0);
            editor.putBoolean("no_save_shot_key",false);
            editor.putBoolean("always_ask_key",false);
            editor.putBoolean("defalt_key",true);
            editor.putBoolean("share_now_key",false);
            //初始总共节约内存为0
            editor.putLong("sum_save",0);
            editor.commit();
        } else
        {
           //延迟两秒启动到首页
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setClass(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }, 2000);

        }
    }
    /**
     * 小米统计生命周期
     */
    @Override
    protected void onResume() {
        super.onResume();
        MiStatInterface.recordPageStart(this, "闪图界面");
    }
    protected void onPause() {
        super.onPause();
        MiStatInterface.recordPageEnd();
    }

}