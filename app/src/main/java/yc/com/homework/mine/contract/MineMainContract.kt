package yc.com.homework.mine.contract

import android.graphics.Bitmap
import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.base.user.UserInfo
import yc.com.homework.index.domain.bean.IndexInfo
import yc.com.homework.mine.domain.bean.UnreadMsgInfo

/**
 *
 * Created by wanglin  on 2018/11/29 10:22.
 */
interface MineMainContract {

    interface View : IView {
        fun showUserInfo(userInfo: UserInfo?)
        fun showNoLogin()
        fun showBitmap(bitmap: Bitmap?)
        fun showUnreadMsg(data: UnreadMsgInfo?)
        fun showIndexInfo(data: IndexInfo?)
    }

    interface Presenter : IPresenter {
        fun getUserInfo()
    }
}