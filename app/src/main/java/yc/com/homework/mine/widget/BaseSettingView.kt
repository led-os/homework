package yc.com.homework.mine.widget

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.bumptech.glide.Glide

import butterknife.BindView
import kotlinx.android.synthetic.main.base_setting_view.view.*
import yc.com.base.BaseView
import yc.com.blankj.utilcode.util.ImageUtils
import yc.com.homework.R
import yc.com.homework.mine.utils.GlideCircleTransformation

/**
 * Created by wanglin  on 2018/3/9 13:32.
 */

class BaseSettingView(context: Context, attrs: AttributeSet?) : BaseView(context, attrs) {


    private var mTitle: CharSequence? = null
    private var mTitleSize: Float = 0.toFloat()
    private var isShowExtra: Boolean = false
    private var isShowArrow: Boolean = false

    var extraText: String? = null
        set(text) {
            tv_extra.text = text
            field = text
        }


    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseSettingView)

        try {

            mTitle = ta.getString(R.styleable.BaseSettingView_setting_title)

            mTitleSize = ta.getDimension(R.styleable.BaseSettingView_setting_titleSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f, resources.displayMetrics))
            isShowExtra = ta.getBoolean(R.styleable.BaseSettingView_setting_show_extra, false)
            isShowArrow = ta.getBoolean(R.styleable.BaseSettingView_setting_show_arrow, true)

            isClickable = true

            if (!TextUtils.isEmpty(mTitle))
                tv_title.text = mTitle
            tv_title.paint.textSize = mTitleSize
            fl_extra.visibility = if (isShowExtra) View.VISIBLE else View.GONE
            showRightArrow(isShowArrow)


            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                rl_container.setBackgroundResource(R.drawable.main_item_bg)
            } else {
                rl_container.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white))
            }

        } finally {
            ta.recycle()
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.base_setting_view
    }

    fun setTitle(mTitle: CharSequence) {
        this.mTitle = mTitle
        tv_title.text = mTitle
    }

    fun setExtraColor(extraColor: Int) {
        tv_extra.setTextColor(extraColor)
    }

    fun setIvIcon(path: String?) {

        val transformation = GlideCircleTransformation(mContext)

        Glide.with(mContext).load(path).asBitmap().transform(transformation).error(R.mipmap.deafult_avator).into(iv_avator)

    }

    fun setIVIcon(resId: Int) {
        //转换矩形为圆角矩形
        val bitmap = ImageUtils.toRound(BitmapFactory.decodeResource(mContext.resources, resId))
        iv_avator.setImageBitmap(bitmap)
    }

    fun clearIcon() {
        Glide.clear(iv_avator)
    }

    fun showRightArrow(isShow: Boolean) {
        this.isShowArrow = isShow
        iv_right_arrow.visibility = if (isShowArrow) View.VISIBLE else View.INVISIBLE
    }


}
