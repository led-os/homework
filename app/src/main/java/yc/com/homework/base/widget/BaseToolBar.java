package yc.com.homework.base.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.umeng.analytics.MobclickAgent;
import com.vondear.rxtools.RxImageTool;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.base.BaseView;
import yc.com.homework.R;
import yc.com.homework.index.activity.CompositionCollectActivity;


public abstract class BaseToolBar extends BaseView {
    private BaseActivity mActivity;
    protected boolean isShowNavigationIcon;


    @BindView(R.id.toolbar_sub)
    Toolbar mToolbar;

    @Nullable
    @BindView(R.id.toolbarWarpper)
    FrameLayout mtoolbarWarpper;


    @BindView(R.id.toolbar_introduce)
    FrameLayout toolbarIntroduce;


    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.tv_right_title)
    TextView tvRightTitle;

    @BindView(R.id.iv_left_icon)
    ImageView ivLeftIcon;
    @BindView(R.id.ll_container)
    LinearLayout llContainer;
    @BindView(R.id.iv_right_pic)
    ImageView ivRightPic;

    @BindView(R.id.viewStub_right_collect)
    ViewStub viewStubRightCollect;
    @BindView(R.id.tv_tb_title)
    TextView mTitleTextView;

    public FrameLayout getToolbarWarpper() {
        return mtoolbarWarpper;
    }

    public Toolbar getToolbar() {
        return mToolbar;
    }


    private Context mContext;

    public BaseToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }

    public void init(final BaseActivity activity, final Class clazz) {
        mToolbar.setTitle("");
        mActivity = activity;
        activity.setSupportActionBar(mToolbar);
        if (isShowNavigationIcon) {
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mActivity.finish();
                }
            });
        }

        if (backClickListener != null) {
            mToolbar.setNavigationOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    backClickListener.onClick(v);
                }
            });
        }

//        Glide.with(mActivity).load(R.mipmap.ic_launcher).asBitmap().centerCrop().into(ivLeftIcon);

        RxView.clicks(toolbarIntroduce).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
            }
        });
        RxView.clicks(ivLeftIcon).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        });

        RxView.clicks(ivRightPic).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                // TODO: 2019/1/24 分享
            }
        });


    }


    public void init(final BaseActivity activity) {
        this.init(activity, null);
    }

    private OnClickListener backClickListener;

    public void setBackOnClickListener(final OnClickListener onClickListener) {
        backClickListener = onClickListener;
    }

    public void setOnMenuItemClickListener() {
        if (onItemClickLisener != null) {
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    onItemClickLisener.onClick();
                    return false;
                }
            });
        }
    }

    public void setTitle(String title) {
        mTitleTextView.setText(title);
    }

    public void setTitleSize(float titleSize) {
        mTitleTextView.getPaint().setTextSize(RxImageTool.sp2px(titleSize));

    }


    public void setTvRightTitleAndIcon(String title, int resId) {
        tvRightTitle.setText(title);
        ivRightIcon.setImageResource(resId);
    }

    public void setTitleGravity(int gravity) {
        mTitleTextView.setGravity(gravity);
    }

    public void showNavigationIcon() {
        showNavigationIcon(R.mipmap.base_back);
    }

    public void showNavigationIcon(int iconId) {
        mToolbar.setNavigationIcon(iconId);
        isShowNavigationIcon = true;
        ivLeftIcon.setVisibility(GONE);
    }

    public void clear() {
        mToolbar.getMenu().clear();
    }

    protected boolean hasMenu;

    protected int mIconResid = 0;

    protected String mMenuTitle;

    private OnItemClickLisener onItemClickLisener;

    public int getmIconResid() {
        return mIconResid;
    }

    public void setMenuIcon(int iconResid) {
        hasMenu = true;
        this.mIconResid = iconResid;
    }

    public String getMenuTitle() {
        return mMenuTitle;
    }

    public void setMenuTitle(String mMenuTitle) {
        hasMenu = true;
        this.mMenuTitle = mMenuTitle;
    }

    public void setBackgroundCorlor(int corlor){
        mtoolbarWarpper.setBackgroundColor(corlor);
    }

    public void setTitleColor(int color) {
        mTitleTextView.setTextColor(color);
    }

    public boolean isHasMenu() {
        return hasMenu;
    }

    public void setOnItemClickLisener(OnItemClickLisener onItemClickLisener) {
        this.onItemClickLisener = onItemClickLisener;
    }

    public void setMenuTitleColor(int color) {

    }

    public interface OnItemClickLisener {
        void onClick();
    }

    /**
     * 设置音标简介隐藏
     *
     * @param flag
     */

    public void setRightContainerVisible(boolean flag) {
        toolbarIntroduce.setVisibility(flag ? VISIBLE : GONE);
    }

    public void setRightPicVisible() {
        toolbarIntroduce.setVisibility(VISIBLE);
        llContainer.setVisibility(GONE);
        viewStubRightCollect.setVisibility(GONE);
        ivRightPic.setVisibility(VISIBLE);

    }

    public void setRightCollectVisible() {
        toolbarIntroduce.setVisibility(VISIBLE);
        TextView tv = (TextView) viewStubRightCollect.inflate();
        RxView.clicks(tv).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                mContext.startActivity(new Intent(mContext, CompositionCollectActivity.class));

            }
        });
        llContainer.setVisibility(GONE);
        ivRightPic.setVisibility(GONE);

    }


}
