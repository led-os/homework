package yc.com.homework.base.utils

import android.content.Context
import android.os.Build
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import android.widget.Toast

import yc.com.homework.R

/**
 * Created by wanglin  on 2018/11/20 15:21.
 */
object ToastUtils {
    private var toast: Toast? = null

    /**
     * 显示在屏幕中央
     *
     * @param charSequence
     */
    private fun showToast(context: Context, charSequence: CharSequence, gravity: Int, yOffset: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Toast.makeText(context, charSequence, Toast.LENGTH_SHORT).show()
        } else {

            if (toast == null) {
                toast = Toast(context)
            }

            val view = LayoutInflater.from(context).inflate(R.layout.layout_toast_view, null)
            val textView = view.findViewById<TextView>(R.id.tv_mess)
            textView.text = charSequence
            toast?.view = view
            toast?.duration = Toast.LENGTH_SHORT
            toast?.setGravity(gravity, 0, yOffset)
            toast?.show()
        }
    }


    fun showCenterToast(context: Context, charSequence: CharSequence) {
        showToast(context, charSequence, Gravity.CENTER, 0)
    }

    fun showBottomToast(context: Context, charSequence: CharSequence) {
        showToast(context, charSequence, Gravity.BOTTOM, 15)
    }


    /**
     * 取消toast
     */
    fun cancel() {
        if (toast != null) {
            toast?.cancel()
            toast = null
        }
    }

}
