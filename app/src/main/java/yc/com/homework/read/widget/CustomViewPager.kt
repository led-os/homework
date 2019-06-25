package yc.com.homework.read.widget

import android.content.Context
import android.support.v4.view.ViewPager
import android.util.AttributeSet
import android.view.MotionEvent

/**
 *
 * Created by wanglin  on 2019/1/29 10:33.
 */
class CustomViewPager : ViewPager {


    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    private var scrollable: Boolean = true

    fun setScrollable(scrollable: Boolean) {
        this.scrollable = scrollable
    }


    override fun onInterceptHoverEvent(event: MotionEvent?): Boolean {
        return scrollable && super.onInterceptHoverEvent(event)

    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return scrollable && super.onTouchEvent(ev)
    }
}