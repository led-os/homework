package yc.com.homework.base.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.jakewharton.rxbinding.view.RxView;
import com.umeng.analytics.MobclickAgent;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseView;
import yc.com.homework.R;
import yc.com.homework.base.utils.AnimationUtil;

/**
 * Created by wanglin  on 2018/11/23 11:56.
 */
public class MainBottomBar extends BaseView {
    @BindView(R.id.mainItem_wall)
    MainItemView mainItemWall;
    @BindView(R.id.ll_wall_container)
    LinearLayout llWallContainer;

    @BindView(R.id.mainItem_mine)
    MainItemView mainItemMine;
    @BindView(R.id.ll_mine_container)
    LinearLayout llMineContainer;
    @BindView(R.id.ll_main_homework)
    RelativeLayout llMainHomework;
    @BindView(R.id.iv_carton1)
    ImageView ivCarton1;
    @BindView(R.id.iv_carton2)
    ImageView ivCarton2;
    @BindView(R.id.mainItem_check)
    MainItemView mainItemCheck;
    @BindView(R.id.ll_check_container)
    LinearLayout llCheckContainer;
    @BindView(R.id.mainItem_welfare)
    MainItemView mainItemWelfare;
    @BindView(R.id.ll_welfare_container)
    LinearLayout llWelfareContainer;


    public MainBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_main_bottombar;
    }

    @Override
    public void init() {
        super.init();
        setItem(0);
//        mainItemWall.setSelect(true);
//        startAnimation();
        RxView.clicks(llWallContainer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setItem(0);
//                mainItemWall.setSelected(true);
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(0);
                }

            }
        });
        RxView.clicks(llMineContainer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setItem(3);
//                mainItemMine.setSelected(true);

                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(3);
                }

            }
        });
//        RxView.clicks(llMainHomework).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
//            @Override
//            public void call(Void aVoid) {
//                setItem(1);
//
//                if (onItemSelectListener != null) {
//                    onItemSelectListener.onItemSelect(1);
//                }
//
//            }
//        });

        RxView.clicks(llCheckContainer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setItem(1);
//                mainItemCheck.setSelected(true);

                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(1);
                }
            }
        });

        RxView.clicks(llWelfareContainer).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                setItem(2);
                // TODO: 2019/2/13 添加福利点击事件统计
                MobclickAgent.onEvent(mContext, "welfare_id", "福利");
                if (onItemSelectListener != null) {
                    onItemSelectListener.onItemSelect(2);
                }
            }
        });

    }

    private void startAnimation() {
        Animation animation1 = AnimationUtil.INSTANCE.startRippleAnimation(1.0f, 1.05f, 1.0f, 1.05f, 1.0f, 0.5f, 1500, Animation.INFINITE);
        ivCarton1.startAnimation(animation1);
        Animation animation2 = AnimationUtil.INSTANCE.startRippleAnimation(1.05f, 1.1f, 1.05f, 1.1f, 0.5f, 1.0f, 1500, Animation.INFINITE);
        ivCarton2.startAnimation(animation2);
    }

    private void stopAnimation() {
        ivCarton1.clearAnimation();
        ivCarton2.clearAnimation();
    }

    private void resetState() {
        mainItemWall.setSelect(false);
        mainItemMine.setSelect(false);
        mainItemCheck.setSelect(false);
        mainItemWelfare.setSelect(false);
    }

    public void setItem(int position) {

        resetState();
        if (position == 0) {
            mainItemWall.setSelect(true);
        } else if (position == 1) {
            mainItemCheck.setSelect(true);
        } else if (position == 2) {
            mainItemWelfare.setSelect(true);
        } else if (position == 3) {
            mainItemMine.setSelect(true);
        }


//        if (position == 1) {
//            stopAnimation();
//        } else {
//            startAnimation();
//        }

    }


    private onItemSelectListener onItemSelectListener;

    public void setOnItemSelectListener(MainBottomBar.onItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public interface onItemSelectListener {
        void onItemSelect(int position);
    }


}
