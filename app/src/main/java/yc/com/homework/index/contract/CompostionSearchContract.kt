package yc.com.homework.index.contract

import yc.com.base.*
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.SearchRecordInfo

/**
 *
 * Created by wanglin  on 2019/3/29 10:48.
 */
interface CompostionSearchContract {

    interface View : IView,ILoading,INoData,INoNet,IHide {
        fun showHotSearchRecords(data: ArrayList<SearchRecordInfo>?)
        fun showSearchResultInfos(data: ArrayList<CompositionInfo>?)
        fun showRecentRecords(list: List<SearchRecordInfo>?)
    }

    interface Presenter : IPresenter {
        fun getHotSearchRecords()
    }
}