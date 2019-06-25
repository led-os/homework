package yc.com.answer.index.ui.fragment;

import android.content.Intent;
import android.widget.ImageView;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.vondear.rxtools.RxDeviceTool;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseDialogFragment;
import yc.com.homework.R;
import yc.com.homework.examine.activity.ExamMainActivity;

/**
 * Created by wanglin  on 2019/2/22 16:12.
 */
public class HomeworkGuideFragment extends BaseDialogFragment {
    @BindView(R.id.tv_unlock_read)
    TextView tvUnlockRead;
    @BindView(R.id.iv_close)
    ImageView ivClose;

    @Override
    protected float getWidth() {
        return 0.8f;
    }

    @Override
    public int getAnimationId() {
        return R.style.share_anim;
    }

    @Override
    public int getHeight() {
        return RxDeviceTool.getScreenHeight(getActivity()) / 2;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_homework_guide;
    }

    @Override
    public void init() {
        RxView.clicks(ivClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //关闭对话框
                dismiss();
            }
        });

        RxView.clicks(tvUnlockRead).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo 暂时换未实现
                Intent intent = new Intent(getActivity(), ExamMainActivity.class);
//                intent.putExtra("url", "http://m.upkao.com/zylxc.html");
//                intent.putExtra("pos", 1);
                startActivity(intent);

                dismiss();
            }
        });
    }

}
