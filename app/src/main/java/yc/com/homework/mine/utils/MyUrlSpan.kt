package yc.com.homework.mine.utils

import android.text.TextPaint
import android.text.style.URLSpan

/**
 *
 * Created by wanglin  on 2019/1/9 10:46.
 */
class MyUrlSpan(url: String) : URLSpan(url) {
    override fun updateDrawState(ds: TextPaint?) {
        super.updateDrawState(ds)
        ds?.color = ds!!.linkColor
        ds.isUnderlineText = false
    }
}