package yc.com.homework.examine.fragment

import android.view.ViewGroup
import android.widget.TextView

import com.jakewharton.rxbinding.view.RxView

import java.util.concurrent.TimeUnit

import butterknife.BindView
import kotlinx.android.synthetic.main.fragment_exit.*
import yc.com.base.BaseDialogFragment
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R


/**
 * Created by wanglin  on 2018/3/16 18:53.
 */

class ExitFragment : BaseDialogFragment<BasePresenter<BaseEngine, BaseView>>() {


    private var mTitle: String? = ""
    private var onConfirmListener: OnConfirmListener? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_exit
    }

    override fun init() {

        tv_tint?.text = mTitle
        RxView.clicks(tv_confirm!!).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (onConfirmListener != null) {
                onConfirmListener!!.onConfirm()
            }
        }

        RxView.clicks(tv_cancel!!).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { dismiss() }
    }

    fun setTitle(title: String): ExitFragment {
        this.mTitle = title
        return this

    }


    override fun getWidth(): Float {
        return 0.8f
    }

    override fun getAnimationId(): Int {
        return R.style.share_anim
    }

    override fun getHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }


    /**
     * internal kotlin修饰符 表示内部类对象可以在任何地方调用
     */
    internal interface OnConfirmListener {
        fun onConfirm()
    }


    internal fun setOnConfirmListener(onConfirmListener: OnConfirmListener): ExitFragment {
        this.onConfirmListener = onConfirmListener
        return this

    }
}
