package yc.com.homework.read.utils

/**
 *
 * Created by wanglin  on 2019/1/28 11:36.
 */
interface OnUIChangeListener {

    fun show()

    fun onDownLoadChangeListener(max: Int, progress: Int)

    fun onComplete()

    fun onPlayChangeListener(duration: Int)
}