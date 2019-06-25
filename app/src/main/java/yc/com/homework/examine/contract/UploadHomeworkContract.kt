package yc.com.homework.examine.contract

import yc.com.base.IDialog
import yc.com.base.IFinish
import yc.com.base.IPresenter
import yc.com.base.IView
import java.io.File

/**
 *
 * Created by wanglin  on 2018/12/4 19:24.
 */
interface UploadHomeworkContract {

    interface View : IView, IDialog, IFinish {
        fun showCountNotEnough()
    }

    interface Presenter : IPresenter {
        fun uploadHomework(file: File?, subject_id: String?, type: String?, grade_id: String?, task_id: String?)
    }
}