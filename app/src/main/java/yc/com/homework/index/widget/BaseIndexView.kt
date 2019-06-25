package yc.com.homework.index.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.View
import com.kk.utils.LogUtil
import kotlinx.android.synthetic.main.base_index_view.view.*
import yc.com.base.BaseView
import yc.com.homework.R
import java.lang.Exception

/**
 *
 * Created by wanglin  on 2019/3/1 13:51.
 */
class BaseIndexView(context: Context, attrs: AttributeSet?) : BaseView(context, attrs) {
    private lateinit var mTitle: String
    private lateinit var mDrawable: Drawable
    private var mTitleColor: Int = 0


    constructor(context: Context) : this(context, null)

    init {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.BaseIndexView)
        try {
            mTitle = ta.getString(R.styleable.BaseIndexView_index_title)
            mDrawable = ta.getDrawable(R.styleable.BaseIndexView_index_icon)
            mTitleColor = ta.getColor(R.styleable.BaseIndexView_index_title_color, ContextCompat.getColor(context, R.color.gray_555555))


            if (!TextUtils.isEmpty(mTitle)) {
                tv_index_title.visibility =View.VISIBLE
                tv_index_title.text = mTitle
            }
            mDrawable.let { iv_index_icon.setImageDrawable(mDrawable) }
            tv_index_title.setTextColor(mTitleColor)



        } catch (e: Exception) {
            LogUtil.msg("e : ${e.message}")
        } finally {
            ta.recycle()
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.base_index_view
    }



}