package yc.com.homework.wall.utils

import android.content.Context
import yc.com.homework.R
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Created by wanglin  on 2018/12/6 15:06.
 */
object UiUtils {

    //  星期
    private val week = arrayOf("星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六")

    fun getSubjectById(context: Context, id: Int?): String? {
        return when (id) {
            1 -> context.resources.getString(R.string.yuwen)
            2 -> context.resources.getString(R.string.mathematics)
            3 -> context.resources.getString(R.string.english)
            else -> ""
        }
    }

    fun getGradeById(id: Int?): String {
        return when (id) {
            1 -> "一年级"
            2 -> "二年级"
            3 -> "三年级"
            4 -> "四年级"
            5 -> "五年级"
            6 -> "六年级"
            else -> ""
        }
    }

    fun longToString(date: Long?): String {
        val sm = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        if (date != null) {
            return sm.format(Date(date * 1000))
        }
        return ""
    }


    /**
     * 获取星期几
     */
    fun getWeek(date: Long?): String {
        val calendar = Calendar.getInstance()
        if (date != null) {
            calendar.time = Date(date * 1000)
        }
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return week[dayOfWeek - 1]
    }

    fun getScorePic(score: Int): Int {
        return when (score) {
            in 90..100 -> R.mipmap.score_aa
            in 80..90 -> R.mipmap.score_a
            in 60..80 -> R.mipmap.score_b
            in 0..60 -> R.mipmap.score_c
            else -> 0
        }
    }

}