package yc.com.homework.index.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.kk.utils.LogUtil
import com.kk.utils.ScreenUtil
import kotlinx.android.synthetic.main.mathematical_item_view.view.*
import yc.com.base.BaseView
import yc.com.homework.R

/**
 *
 * Created by wanglin  on 2019/3/21 14:11.
 */
class BaseMathView(context: Context, attrs: AttributeSet?) : BaseView(context, attrs) {

    private lateinit var mDrawable: Drawable

    private lateinit var mName: CharSequence
    private lateinit var mTag: CharSequence

    private var isShowExtra: Boolean = false

    constructor(context: Context) : this(context, null)

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseMathView)

        try {
            mName = ta.getString(R.styleable.BaseMathView_math_name)
            mDrawable = ta.getDrawable(R.styleable.BaseMathView_math_icon)
            isShowExtra = ta.getBoolean(R.styleable.BaseMathView_math_show_extra, false)
            mTag = ta.getString(R.styleable.BaseMathView_math_tag)

            mDrawable.let { iv_math_icon.setImageDrawable(mDrawable) }

            if (!TextUtils.isEmpty(mName)) tv_math_name.text = mName


            if (isShowExtra) {
                val layoutParams = tv_math_name.layoutParams as MarginLayoutParams

                layoutParams.topMargin = ScreenUtil.dip2px(context, 3f)
                tv_math_name.layoutParams = layoutParams
                tv_other.visibility = View.VISIBLE
            }


        } catch (ex: Exception) {
            LogUtil.msg("e:  Exception  　${ex.message}")
        } finally {
            ta.recycle()
        }

    }

    fun getTextTag(): CharSequence {
        return mTag
    }

    fun getTextName(): CharSequence {
        return mName

    }


    override fun getLayoutId(): Int {

        return R.layout.mathematical_item_view
    }

}