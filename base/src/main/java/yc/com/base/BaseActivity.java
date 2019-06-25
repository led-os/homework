package yc.com.base;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.debug.hv.ViewServer;
import com.hwangjr.rxbus.RxBus;
import com.umeng.analytics.MobclickAgent;
import com.vondear.rxtools.RxLogTool;
import com.vondear.rxtools.RxNetTool;

import butterknife.ButterKnife;

/**
 * Created by wanglin  on 2018/3/6 10:14.
 */

public abstract class BaseActivity<P extends BasePresenter> extends AppCompatActivity implements IView, IDialog, NetChangeListener {


    public static NetChangeListener listener;
    protected P mPresenter;
    protected BaseLoadingView baseLoadingView;
    protected Handler mHandler;
    private MyRunnable taskRunnable;
    private int statusHeight;
    private int netMobile;


    public int getStatusHeight() {
        return statusHeight;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        listener = this;
        RxBus.get().register(this);
        try {
            ButterKnife.bind(this);
        } catch (Exception e) {
            RxLogTool.e("-->: 初始化失败 " + e.getMessage());
        }
        statusHeight = StatusBarUtil.getStatusBarHeight(this);

        baseLoadingView = new BaseLoadingView(this);
        mHandler = new Handler();
        //顶部透明
//        registerReveiver();
        inspectNet();
        beforeInit();

        ViewServer.get(this).addWindow(this);

        if (isStatusBarMateria())
            setStatusBarMateria();
        init();

    }

    public void beforeInit() {
    }



    /**
     * * 初始化时判断有没有网络     
     */
    private boolean inspectNet() {
        this.netMobile = RxNetTool.getNetWorkType(this);
        showNetErrorState(netMobile);
        return isNetConnect();
    }

    private boolean isShow = false;

    private void showNetErrorState(int status) {
        if (!isShow && (status == RxNetTool.NETWORK_NO || status == RxNetTool.NETWORK_UNKNOWN)) {
            Snackbar.make(findViewById(android.R.id.content), "网络错误，请检查网络", Snackbar.LENGTH_SHORT).show();
            isShow = true;
        } else {
            isShow = false;
        }
    }

    private boolean isNetConnect() {
        switch (netMobile) {
            case RxNetTool.NETWORK_NO:
            case RxNetTool.NETWORK_UNKNOWN:
                return false;
            case RxNetTool.NETWORK_WIFI:
            case RxNetTool.NETWORK_2G:
            case RxNetTool.NETWORK_3G:
            case RxNetTool.NETWORK_4G:
                return true;
        }

        return false;
    }

    protected void setStatusBar() {

        StatusBarUtil.setTranslucentForImageView(this, null);
    }

    public abstract boolean isStatusBarMateria();

    protected void setStatusBarMateria() {
        StatusBarUtil.setMaterialStatus(this);
    }

    public void setToolbarTopMargin(View view) {
        if (view.getLayoutParams() instanceof FrameLayout.LayoutParams) {
            FrameLayout.LayoutParams l = (FrameLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusHeight, 0, 0);
        } else if (view.getLayoutParams() instanceof RelativeLayout.LayoutParams) {
            RelativeLayout.LayoutParams l = (RelativeLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusHeight, 0, 0);
        } else if (view.getLayoutParams() instanceof LinearLayout.LayoutParams) {
            LinearLayout.LayoutParams l = (LinearLayout.LayoutParams) view.getLayoutParams();
            l.setMargins(0, statusHeight, 0, 0);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.subscribe();
        }
        ViewServer.get(this).setFocusedWindow(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (EmptyUtils.isNotEmpty(mPresenter)) {
            mPresenter.unsubscribe();
        }
//        if (netBroadcastReceiver != null) {
//            unregisterReceiver(netBroadcastReceiver);
//        }
        mHandler.removeCallbacks(taskRunnable);
        taskRunnable = null;
        mHandler = null;
        RxBus.get().unregister(this);
        ViewServer.get(this).removeWindow(this);

    }

    @Override
    public void showLoadingDialog(String mess) {
        if (!this.isFinishing()) {
            if (null != baseLoadingView) {
                baseLoadingView.setMessage(mess);
                baseLoadingView.show();
            }
        }
    }

    @Override
    public void dismissDialog() {
        try {
            if (!this.isFinishing()) {
                if (null != baseLoadingView && baseLoadingView.isShowing()) {
                    baseLoadingView.dismiss();
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * 改变获取验证码按钮状态
     */
    public void showGetCodeDisplay(TextView textView, @ColorInt int textColor, @DrawableRes int backGround,
                                   @ColorInt int textOriginColr, @DrawableRes int backOriginGround) {
        taskRunnable = new MyRunnable(textView, textOriginColr, backOriginGround);
        if (null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
            totalTime = 60;
            textView.setClickable(false);
            textView.setTextColor(textColor);
            textView.setBackgroundResource(backGround);
            mHandler.postDelayed(taskRunnable, 0);
        }
    }

    /**
     * 定时任务，模拟倒计时广告
     */
    private int totalTime = 60;


    @Override
    public void onNetChangeListener(int status) {
        this.netMobile = status;
        NetworkManager.getDefault().showNetErrorState(status, this);
    }


    private class MyRunnable implements Runnable {
        TextView mTv;
        int originColor;
        int originGround;

        private MyRunnable(TextView textView, @ColorInt int textOriginColor, @DrawableRes int backOriginGround) {
            this.mTv = textView;
            this.originColor = textOriginColor;
            this.originGround = backOriginGround;
        }

        @Override
        public void run() {
            mTv.setText(String.valueOf(totalTime));
            totalTime--;
            if (totalTime < 0) {
                //还原
                initGetCodeBtn(mTv, originColor, originGround);
                return;
            }
            if (null != mHandler) mHandler.postDelayed(this, 1000);
        }
    }


    /**
     * 还原获取验证码按钮状态
     */
    private void initGetCodeBtn(TextView textView, @ColorInt int textColor,
                                @DrawableRes int backGround) {
        totalTime = 0;
        if (null != taskRunnable && null != mHandler) {
            mHandler.removeCallbacks(taskRunnable);
            mHandler.removeMessages(0);
        }
        textView.setText("重新获取");
        textView.setClickable(true);
        textView.setTextColor(textColor);
        textView.setBackgroundResource(backGround);
    }

}
