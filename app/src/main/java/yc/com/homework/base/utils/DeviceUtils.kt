package yc.com.homework.base.utils

import android.content.Context
import android.view.KeyCharacterMap
import android.view.KeyEvent

/**
 *
 * Created by wanglin  on 2019/2/18 11:03.
 */
class DeviceUtils {

    companion object {

        //获取底部虚拟导航的高度
        fun getNavibarHeight(context: Context): Int {
            val result = 0
            val rid = context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
            if (rid != 0) {
                val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
                return context.resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

        //是否有下方虚拟栏
        fun isNavigationBarAvailable(): Boolean {
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            val hasHomeKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_HOME)
            return !(hasBackKey && hasHomeKey)
        }


    }
}