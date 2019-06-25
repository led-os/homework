package yc.com.homework.examine.contract

import yc.com.base.*
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo

/**
 *
 * Created by wanglin  on 2018/11/27 15:11.
 */
interface HistoryDetailContract {

    interface View : IView,ILoading,INoNet,INoData,IHide {
        fun showHistoryDetailInfos(t: List<HomeworkDetailInfo>)
    }

    interface Presenter : IPresenter {
        fun getHistoryDetailInfos(status: Int?,page:Int,page_size:Int,isRefresh: Boolean)

        fun setReadState(task_id:String?)
    }
}