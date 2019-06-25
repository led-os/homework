package yc.com.soundmark.category.activity;

import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.jakewharton.rxbinding.view.RxView;
import com.xinqu.videoplayer.XinQuVideoPlayer;
import com.xinqu.videoplayer.XinQuVideoPlayerStandard;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.StatusBarUtil;
import yc.com.blankj.utilcode.util.LogUtils;
import yc.com.blankj.utilcode.util.NetworkUtils;
import yc.com.blankj.utilcode.util.SizeUtils;
import yc.com.homework.R;
import yc.com.homework.base.user.UserInfo;
import yc.com.homework.base.user.UserInfoHelper;
import yc.com.homework.base.widget.CommonToolBar;
import yc.com.homework.base.widget.StateView;
import yc.com.homework.read.fragment.BookReadUnlockFragment;
import yc.com.soundmark.base.constant.BusAction;
import yc.com.soundmark.category.contract.WeiKeDetailContract;
import yc.com.soundmark.category.model.domain.CourseInfo;
import yc.com.soundmark.category.presenter.WeiKeDetailPresenter;


/**
 * Created by wanglin  on 2017/9/6 08:32.
 */

public class WeiKeDetailActivity extends BaseActivity<WeiKeDetailPresenter> implements WeiKeDetailContract.View {

    private static final String TAG = "NewsDetailActivity";

    @BindView(R.id.commonToolBar)
    CommonToolBar commonToolBar;
    @BindView(R.id.mJCVideoPlayer)
    XinQuVideoPlayerStandard mJCVideoPlayer;

    @BindView(R.id.tv_learn_count)
    TextView mLearnCountTextView;


    @BindView(R.id.tv_title)
    TextView mTextViewTitle;
    @BindView(R.id.tv_now_price)
    TextView mNowPriceTextView;
    @BindView(R.id.tv_old_price)
    TextView mOldPriceTextView;

    @BindView(R.id.webView)
    WebView webView;

    @BindView(R.id.layout_buy_now)
    LinearLayout mBuyNowLayout;
    @BindView(R.id.ll_rootView)
    RelativeLayout llRootView;
    @BindView(R.id.stateView)
    StateView stateView;


    private String title;


    private String id;

    private long startTime;

    private CourseInfo currentCourseInfo;

    private UserInfo userInfo;
    private SensorManager mSensorManager;
    private XinQuVideoPlayer.XinQuAutoFullscreenListener mSensorEventListener;

    @Override
    public int getLayoutId() {
        return R.layout.activity_weike_detail_soundmark;
    }

    @Override
    public void init() {

        commonToolBar.setTitle("视频微课学习");
        commonToolBar.showNavigationIcon();
        commonToolBar.init(this);
        StatusBarUtil.setStatusTextColor1(true, this);

        mPresenter = new WeiKeDetailPresenter(this, this);

        mOldPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

        startTime = System.currentTimeMillis();


        if (getIntent() != null) {
            id = getIntent().getStringExtra("pid");
        }
        userInfo = UserInfoHelper.getUserInfo();
        mPresenter.getWeikeCategoryInfo(id);
        initListener();
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        mSensorEventListener = new XinQuVideoPlayer.XinQuAutoFullscreenListener();
        mJCVideoPlayer.widthRatio = 16;
        mJCVideoPlayer.heightRatio = 9;


    }


    @Override
    public void showLoading() {
        stateView.showLoading(llRootView);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(llRootView);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(llRootView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getWeikeCategoryInfo(id);
            }
        });
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showWeikeInfo(CourseInfo courseInfo) {

        initData(courseInfo);
    }


    private void initData(CourseInfo courseInfo) {

        if (courseInfo != null) {
            currentCourseInfo = courseInfo;

            title = courseInfo.getTitle();
            mTextViewTitle.setText(title);

            playVideo(courseInfo);
            initWebView(courseInfo);
            mNowPriceTextView.setText("微课 ¥" + courseInfo.getVipPrice());
            mOldPriceTextView.setText("微课 原价:¥" + courseInfo.getMPrice());
            mLearnCountTextView.setText(courseInfo.getUserNum());


        }
    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }


    private void initListener() {
//        mToolbar.setOnItemClickLisener(new BaseToolBar.OnItemClickLisener() {
//            @Override
//            public void onClick() {
//                SharePopupWindow sharePopupWindow = new SharePopupWindow(NewsWeiKeDetailActivity.this);
//                sharePopupWindow.show(llRootView);
//            }
//        });

        RxView.clicks(mBuyNowLayout).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (currentCourseInfo != null) {
                    if (UserInfoHelper.getUserInfo() != null) {
                        currentCourseInfo.setUserId(UserInfoHelper.getUserInfo().getId());
                        showBuyDialog();
                    }
                }
            }
        });

    }


    private void initWebView(final CourseInfo data) {

        final WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//        webView.addJavascriptInterface(new JavascriptInterface(), "HTML");

        //其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存 //优先使用缓存:
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        webSettings.setBlockNetworkImage(true);//设置是否加载网络图片 true 为不加载 false 为加载

//        String body = data.getInfo().getBody();
        webView.loadDataWithBaseURL(null, data.getDesp(), "text/html", "utf-8", null);

        webView.setWebViewClient(new WebViewClient() {

            @Override
            public void onPageFinished(WebView view, String url) {
//                view.loadUrl("javascript:window.HTML.getContentHeight(document.getElementsByTagName('html')[0].scrollHeight);");

                llRootView.setVisibility(View.VISIBLE);

                webSettings.setBlockNetworkImage(false);

                LogUtils.e("startTime-->" + (System.currentTimeMillis() - startTime));

                view.loadUrl("javascript:(function(){"
                        + "var imgs=document.getElementsByTagName(\"img\");"
                        + "for(var i=0;i<imgs.length;i++) " + "{"
                        + "  imgs[i].onclick=function() " + "{ "
                        + "    window.HTML.openImg(this.src); "
                        + "   }  " + "}" + "}())");

            }
        });
    }


    /**
     * 播放视频
     *
     * @param courseInfo
     */
    private void playVideo(CourseInfo courseInfo) {
        Glide.with(this).load(courseInfo.getImg()).thumbnail(0.1f).into(mJCVideoPlayer.thumbImageView);

        mJCVideoPlayer.setUp(courseInfo.getUrl(), XinQuVideoPlayer.SCREEN_WINDOW_LIST, false, null == courseInfo.getTitle() ? "" : courseInfo.getTitle());

        mJCVideoPlayer.backButton.setVisibility(View.GONE);
        mJCVideoPlayer.tinyBackImageView.setVisibility(View.GONE);


        if (judgeVip()) {
            if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI)
                mJCVideoPlayer.startVideo();
            else {
                click();
            }
        } else {
            click();
        }


    }


    private boolean judgeVip() {
        boolean isPlay = false;

        if (UserInfoHelper.isVip() || currentCourseInfo.getIs_vip() == 0) {
            isPlay = true;
        }

        if (isPlay) {
            mBuyNowLayout.setVisibility(View.GONE);
        } else {
            mBuyNowLayout.setVisibility(View.VISIBLE);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) webView.getLayoutParams();
            layoutParams.bottomMargin = SizeUtils.dp2px(45);
            webView.setLayoutParams(layoutParams);
        }

        return isPlay;
    }

    @Subscribe(
            thread = EventThread.MAIN_THREAD,
            tags = {
                    @Tag(BusAction.PAY_SUCCESS)
            }
    )
    public void paySuccess(String info) {
        judgeVip();
    }

    private void click() {
        RxView.clicks(mJCVideoPlayer.startButton).throttleFirst(1000, TimeUnit.MICROSECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startOrBuy();
            }
        });
    }


    private void startOrBuy() {
        if (userInfo != null) {
            if (judgeVip()) {
                mJCVideoPlayer.startVideo();
            } else {
                showBuyDialog();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    protected void onPause() {
        super.onPause();

        XinQuVideoPlayerStandard.releaseAllVideos();

        mSensorManager.unregisterListener(mSensorEventListener);
        XinQuVideoPlayerStandard.clearSavedProgress(this, null);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null && llRootView != null) {
            webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            webView.clearHistory();

            llRootView.removeView(webView);
            webView.destroy();
        }
    }


    @Override
    public void onBackPressed() {
        if (XinQuVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    private BookReadUnlockFragment unlockFragment;

    //显示支付弹窗

    private void showBuyDialog() {
//        BasePayFragment basePayFragment = new BasePayFragment();
//        basePayFragment.show(getSupportFragmentManager(), "");
        if (unlockFragment == null)
            unlockFragment = new BookReadUnlockFragment();

        Bundle bundle = new Bundle();
        bundle.putString("content", "成功分享给好友并邀请好友注册作业啦，便可以解锁所有的微课学习哦！");
        bundle.putString("title", "分享好友，解锁微课");
        unlockFragment.setArguments(bundle);

        if (!unlockFragment.isVisible())
            unlockFragment.show(getSupportFragmentManager(), "");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
