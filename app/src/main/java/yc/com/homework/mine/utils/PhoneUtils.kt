package yc.com.homework.mine.utils

import android.content.Context
import android.text.TextUtils
import yc.com.homework.R

/**
 *
 * Created by wanglin  on 2018/12/3 13:32.
 */
object PhoneUtils {
    fun replacePhone(context: Context, phone: String?): String? {
        if (TextUtils.isEmpty(phone)||phone!!.contains(context.getString(R.string.go_bind))) return phone
        return phone.replaceRange(3..6, "****")
    }

}