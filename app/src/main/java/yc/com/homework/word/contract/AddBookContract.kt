package yc.com.homework.word.contract

import yc.com.base.*
import yc.com.homework.word.model.domain.CourseVersionInfo
import yc.com.homework.word.model.domain.GradeInfo

/**
 * Created by zhangkai on 2017/7/25.
 */

interface AddBookContract {
    interface View : IView, IDialog, IFinish, ILoading, INoData, INoNet, IHide {

        fun showGradeListData(gradeInfos: List<GradeInfo>?)

        fun showCVListData(list: List<CourseVersionInfo>)
    }

    interface Presenter : IPresenter {

        fun gradeList() //获取年级集合

        fun getCVListByGradeId(gradeId: String?, partType: String?) //根据查询条件获取教材集合

        fun getGradeListFromLocal() //获取本地数据
    }
}
