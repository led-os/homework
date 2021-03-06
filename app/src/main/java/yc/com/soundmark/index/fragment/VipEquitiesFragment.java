package yc.com.soundmark.index.fragment;

import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.homework.R;

/**
 * Created by wanglin  on 2018/11/12 15:36.
 */
public class VipEquitiesFragment extends BaseDialogFragment {

    @BindView(R.id.iv_close)
    ImageView ivClose;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_vip_equities;
    }

    @Override
    public void init() {
        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                dismiss();
            }
        });

    }


    @Override
    protected float getWidth() {
        return 0.9f;
    }

    @Override
    public int getAnimationId() {
        return 0;
    }

    @Override
    public int getHeight() {
        return ScreenUtil.getHeight(getActivity()) * 9 / 10;
    }


}
