package yc.com.homework.mine.widget

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.util.AttributeSet
import android.view.View
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.LogUtil
import kotlinx.android.synthetic.main.view_main_mine_item.view.*
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.mine.activity.RechargeActivity
import java.util.concurrent.TimeUnit

/**
 * Created by wanglin  on 2018/11/23 16:30.
 */
class MainMineItemView(context: Context, attrs: AttributeSet?) : BaseView(context, attrs) {


    private var drawable: Drawable? = null
    private var title: String? = null
    private var isShowBtn: Boolean = false
    private var isShowArrow: Boolean = false

    init {

        val ta = context.obtainStyledAttributes(attrs, R.styleable.MainMineItemView)
        try {

            drawable = ta.getDrawable(R.styleable.MainMineItemView_mine_icon)
            title = ta.getString(R.styleable.MainMineItemView_mine_title)
            isShowBtn = ta.getBoolean(R.styleable.MainMineItemView_show_button, false)
            isShowArrow = ta.getBoolean(R.styleable.MainMineItemView_show_arrow, true)
            if (drawable != null) {
                iv_icon.setImageDrawable(drawable)
            }
            if (!TextUtils.isEmpty(title)) {
                tv_title.text = title
            }
            tv_recharge.visibility = if (isShowBtn) View.VISIBLE else View.GONE
            iv_arrow.visibility = if (isShowArrow) View.VISIBLE else View.GONE

            RxView.clicks(tv_recharge).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
                val intent = Intent(mContext, RechargeActivity::class.java)
                mContext.startActivity(intent)
            }

            ta.recycle()
        } catch (e: Exception) {
            LogUtil.msg(TAG + " error-->" + e.message)
        }

    }

    override fun getLayoutId(): Int {
        return R.layout.view_main_mine_item
    }

    fun setDrawable(drawable: Drawable) {
        this.drawable = drawable
        iv_icon.setImageDrawable(drawable)
    }

    fun setTitle(title: String) {
        this.title = title
        tv_title.text = title
    }

    fun setShowBtn(isShowBtn: Boolean) {
        this.isShowBtn = isShowBtn
        tv_recharge.visibility = if (isShowBtn) View.VISIBLE else View.GONE
    }

    companion object {
        private val TAG = "MainMineItemView"
    }
}
