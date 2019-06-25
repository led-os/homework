package yc.com.homework.mine.contract

import yc.com.base.IDialog
import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.mine.domain.bean.GradeInfo

/**
 *
 * Created by wanglin  on 2018/11/30 13:46.
 */
interface EditInfoContract {

    interface View : IView,IDialog {
        fun showSuccess()
        fun showGradeInfos(t: List<GradeInfo>?)
    }

    interface Presenter : IPresenter {
        fun updateInfo(user_id: String, nick_name: String?, face: String?, password: String?, grade_id: String?)
    }
}