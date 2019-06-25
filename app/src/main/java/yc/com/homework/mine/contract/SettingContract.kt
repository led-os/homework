package yc.com.homework.mine.contract

import yc.com.base.IDialog
import yc.com.base.IPresenter
import yc.com.base.IView

/**
 *
 * Created by wanglin  on 2018/11/27 17:16.
 */
interface SettingContract {

    interface View : IView, IDialog {
        fun showCacheSize(cacheSize: String?)
        fun showLogout()
    }

    interface Presenter : IPresenter {
        fun uploadAvator(user_id: String, file: String)
    }
}