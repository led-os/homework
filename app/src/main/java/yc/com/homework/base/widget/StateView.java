package yc.com.homework.base.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseView;
import yc.com.blankj.utilcode.util.SizeUtils;
import yc.com.homework.R;


public class StateView extends BaseView {
    @BindView(R.id.iv_loading)
    ImageView mLoadingImageView;

    @BindView(R.id.tv_message)
    TextView mMessageTextView;
    @BindView(R.id.root)
    LinearLayout root;

    ViewStub pbViewStub;


    private View mContextView;
    private Context mContext;

    public StateView(Context context) {
        super(context);
        this.mContext = context;
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
    }


    @Override
    public void init() {
        pbViewStub = findViewById(R.id.pb_viewStub);
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_view_state;
    }

    public void hide() {
        Glide.clear(mLoadingImageView);
        setVisibility(View.GONE);
        if (mContextView != null) {
            mContextView.setVisibility(View.VISIBLE);
        }
    }

    public void showLoading(View contextView, String message) {
//        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
//        layoutParams.width = SizeUtils.dp2px(540 / 3);
//        layoutParams.height = SizeUtils.dp2px(960 / 3);
//        mLoadingImageView.setLayoutParams(layoutParams);
//        mLoadingImageView.invalidate();
        mContextView = contextView;
        setVisibility(View.VISIBLE);
        mMessageTextView.setVisibility(View.VISIBLE);
        mLoadingImageView.setVisibility(GONE);
        mMessageTextView.setText(message);
        if (pbViewStub != null)
            pbViewStub.setVisibility(VISIBLE);


//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.override(SizeUtils.dp2px(1080 / 3), SizeUtils.dp2px(408 / 3));
//        if (type == 1) {


//        Glide.with(mContext).load(R.mipmap.base_loading).asGif().override(SizeUtils.dp2px(540 / 3), SizeUtils.dp2px(960 / 3)).into(mLoadingImageView);
//        } else {
//            Glide.with(mContext).load(R.mipmap.base_loading2).apply(requestOptions).into(mLoadingImageView);
//        }
    }


    public void showLoading(View contextView) {
        showLoading(contextView, "加载中，请稍候...");
    }

    public void showNoData(View contextView, String message) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(425 / 3);
        layoutParams.height = SizeUtils.dp2px(405 / 3);
        mLoadingImageView.setLayoutParams(layoutParams);
        mContextView = contextView;
        setVisibility(View.VISIBLE);

        mContextView.setVisibility(View.GONE);
        pbViewStub.setVisibility(GONE);
        mLoadingImageView.setVisibility(VISIBLE);
        mMessageTextView.setText(message);
        Glide.clear(mLoadingImageView);
//        RequestOptions requestOptions = new RequestOptions();
//        requestOptions.override(SizeUtils.dp2px(312 / 3), SizeUtils.dp2px(370 / 3));
        Glide.with(mContext).load(R.mipmap.base_no_data).asBitmap().override(SizeUtils.dp2px(425 / 3), SizeUtils.dp2px(405 / 3)).into(mLoadingImageView);
    }

    public void showNoData(View contextView) {
        showNoData(contextView, "暂无数据");
    }

    public void showNoNet(View contextView, String message, final OnClickListener onClickListener) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) mLoadingImageView.getLayoutParams();
        layoutParams.width = SizeUtils.dp2px(490 / 3);
        layoutParams.height = SizeUtils.dp2px(485 / 3);
        mLoadingImageView.setLayoutParams(layoutParams);
        mContextView = contextView;
        setVisibility(View.VISIBLE);
        mContextView.setVisibility(View.GONE);
        pbViewStub.setVisibility(GONE);
        mMessageTextView.setText(message);
        mLoadingImageView.setVisibility(VISIBLE);
        Glide.clear(mLoadingImageView);

        Glide.with(mContext).load(R.mipmap.base_no_wifi).asBitmap().override(SizeUtils.dp2px(490 / 3), SizeUtils.dp2px(485 / 3)).into(mLoadingImageView);
        RxView.clicks(root).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                onClickListener.onClick(root);
            }
        });
    }

    public void showNoNet(View contextView, final OnClickListener onClickListener) {
        showNoNet(contextView, HttpConfig.NET_ERROR, onClickListener);
    }
}
