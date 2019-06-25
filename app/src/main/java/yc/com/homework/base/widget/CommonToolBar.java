package yc.com.homework.base.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ScreenUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseView;
import yc.com.homework.R;
import yc.com.homework.mine.fragment.ShareFragment;

/**
 * Created by wanglin  on 2018/11/24 10:43.
 * 白色背景toolbar
 */
public class CommonToolBar extends BaseView {
    @BindView(R.id.tv_tb_title)
    TextView tvTbTitle;
    @BindView(R.id.toolbar_sub)
    Toolbar mToolbar;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.iv_back)
    RelativeLayout ivBack;
    @BindView(R.id.iv_right_collect)
    ImageView ivRightCollect;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    private BaseActivity mActivity;


    public CommonToolBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_toolbar;
    }

    public void showNavigationIcon() {
        ivBack.setVisibility(VISIBLE);

    }

    public void init(final BaseActivity activity) {
        mToolbar.setTitle("");
        mActivity = activity;
        RxView.clicks(ivBack).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                activity.finish();
            }
        });

        RxView.clicks(ivRightIcon).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                ShareFragment shareFragment = new ShareFragment();
                shareFragment.show(mActivity.getSupportFragmentManager(), "");
            }
        });
        RxView.clicks(ivRightCollect).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (collectClickListener != null) {
                    collectClickListener.onCollectClick();
                }
            }
        });
    }

    public void setTitle(String title) {
        tvTbTitle.setText(title);
    }

    public void setTitleSize(float titleSize) {
        tvTbTitle.getPaint().setTextSize(ScreenUtil.dip2px(mContext, titleSize));
    }

    public void setRightShareVisable(boolean flag) {
        ivRightIcon.setVisibility(flag ? VISIBLE : GONE);
    }

    public void setRightCollectVisible(boolean flag) {
        ivRightCollect.setVisibility(flag ? VISIBLE : GONE);
    }

    public void setRightCollectSelected(boolean flag) {

        if (flag) {
            ivRightCollect.setImageResource(R.mipmap.toolbar_collected_icon);
        } else {
            ivRightCollect.setImageResource(R.mipmap.toolbar_collect_icon);
        }
    }


    public OnCollectClickListener collectClickListener;

    public void setCollectClickListener(OnCollectClickListener collectClickListener) {
        this.collectClickListener = collectClickListener;
    }

    public interface OnCollectClickListener {
        void onCollectClick();
    }
}
