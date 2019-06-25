package yc.com.homework.wall.contract

import yc.com.base.*
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo
import yc.com.homework.wall.domain.bean.HomeworkInfo

/**
 *
 * Created by wanglin  on 2018/11/27 10:23.
 */
interface WallDetailContract {

    interface View : IView, ILoading, INoData, INoNet, IHide {
        fun showWallDetailInfos(t: List<HomeworkInfo>)
        fun showHomeworkInfo(data: HomeworkDetailInfo)
    }

    interface Presenter : IPresenter {
        fun getWalInfos(subject_id: Int, page: Int, page_size: Int, isRefresh: Boolean)
        fun getWallDetailInfo(task_id: String?)
        fun getHistoryDetailInfo(task_id: String?)
        fun feedHomework(task_id: String?)
    }
}