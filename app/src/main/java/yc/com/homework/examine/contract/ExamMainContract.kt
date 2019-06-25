package yc.com.homework.examine.contract

import yc.com.base.INoData
import yc.com.base.IPresenter
import yc.com.base.IView
import yc.com.homework.examine.domain.bean.UploadStatusInfo

/**
 *
 * Created by wanglin  on 2018/12/12 16:25.
 */
interface ExamMainContract {

    interface View : IView,INoData {
        fun showUploadStatusInfo(data: UploadStatusInfo?)
    }
    interface Presenter : IPresenter {
        fun getUploadStatus(user_id :String?)
    }
}