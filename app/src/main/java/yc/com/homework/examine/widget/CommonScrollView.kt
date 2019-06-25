package yc.com.homework.examine.widget

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration

/**
 * Created by wanglin  on 2018/11/16 16:29.
 */
class CommonScrollView : NestedScrollView {
    private var scaledTouchSlop: Int = 0
    private var y: Int = 0
    private var x: Int = 0

    private var onScrollListener: OnCommonScrollListener? = null

    @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : super(context, attrs) {
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        scaledTouchSlop = ViewConfiguration.get(context).scaledTouchSlop
    }


    override fun onScrollChanged(l: Int, scrollY: Int, oldl: Int, oldScrollY: Int) {
        super.onScrollChanged(l, scrollY, oldl, oldScrollY)
        if (onScrollListener != null) {
            onScrollListener!!.onScroll(l, scrollY, oldl, oldScrollY)
        }


    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                y = ev.y.toInt()
                x = ev.x.toInt()
            }

            MotionEvent.ACTION_UP -> {
                val curY = ev.y.toInt()
                val curX = ev.x.toInt()
                if (Math.abs(curY - this.y) > scaledTouchSlop) {
                    return true
                }
            }
        }

        return super.onInterceptTouchEvent(ev)
    }

    fun setOnScrollListener(onScrollListener: CommonScrollView.OnCommonScrollListener) {
        this.onScrollListener = onScrollListener
    }

     interface OnCommonScrollListener {
        fun onScroll(l: Int, scrollY: Int, oldl: Int, oldScrollY: Int)
    }
}
