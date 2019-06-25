package yc.com.homework.read.contract

import yc.com.base.*
import yc.com.homework.read.domain.bean.BookConditonInfo
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.domain.bean.GradeVersionInfo

/**
 *
 * Created by wanglin  on 2019/1/10 15:58.
 */
interface BookReadSelectContract {

    interface View : IView, ILoading, INoNet, INoData, IHide {
        fun showBookConditionInfo(data: BookConditonInfo?)
        fun showVersionsByGrade(data: ArrayList<GradeVersionInfo>?)
        fun showBookInfo(bookInfo: BookInfo)
    }

    interface Presenter : IPresenter {
        fun getBookReadSelectInfo()
        fun getVersionsByGrade(grade: String?, subject_id: String?, volumes_id: String?)
        fun getBookInfoByGradeVersion(volumes_id: String, version_id: String, grade: String)
    }
}