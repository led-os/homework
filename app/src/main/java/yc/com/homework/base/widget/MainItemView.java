package yc.com.homework.base.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.kk.utils.LogUtil;

import butterknife.BindView;
import yc.com.base.BaseView;
import yc.com.homework.R;

/**
 * Created by wanglin  on 2018/11/20 09:40.
 */
public class MainItemView extends BaseView {
    @BindView(R.id.iv_item_icon)
    ImageView ivItemIcon;
    @BindView(R.id.tv_item_name)
    TextView tvItemName;
    private Drawable selectDrawable;
    private Drawable normalDrawable;
    private String title;
    private int selectColor;
    private int normalColor;

    public MainItemView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.MainItemView);
        try {
            selectDrawable = ta.getDrawable(R.styleable.MainItemView_select_icon);
            normalDrawable = ta.getDrawable(R.styleable.MainItemView_normal_icon);

            title = ta.getString(R.styleable.MainItemView_title);
            selectColor = ta.getColor(R.styleable.MainItemView_select_titleColor, ContextCompat.getColor(context, R.color.app_normal_color));
            normalColor = ta.getColor(R.styleable.MainItemView_normal_titleColor, ContextCompat.getColor(context, R.color.app_normal_color));


            if (!TextUtils.isEmpty(title)) {
                tvItemName.setText(title);
            }
            setSelect(false);

            ta.recycle();
        } catch (Exception e) {
            LogUtil.msg("mainItemView:->>" + e.getMessage());
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.view_main_item;
    }


    public void setDrawable(Drawable drawable) {

        ivItemIcon.setImageDrawable(drawable);
    }

    public void setTitle(String title) {
        this.title = title;
        tvItemName.setText(title);
    }


    public void setSelect(boolean flag) {
        if (flag) {
            if (selectDrawable != null)
                ivItemIcon.setImageDrawable(selectDrawable);
            tvItemName.setTextColor(selectColor);

        } else {
            if (normalDrawable != null)
                ivItemIcon.setImageDrawable(normalDrawable);
            tvItemName.setTextColor(normalColor);
        }
    }


}
