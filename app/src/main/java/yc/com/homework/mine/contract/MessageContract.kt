package yc.com.homework.mine.contract

import yc.com.base.*
import yc.com.homework.mine.domain.bean.MessageInfo

/**
 *
 * Created by wanglin  on 2018/11/23 17:57.
 */
interface MessageContract {

    interface View : IView, ILoading, INoData, INoNet, IHide {
        fun showMessageInfos(t: List<MessageInfo>)
        fun showMessageInfo(data: MessageInfo?)
    }

    interface Presenter : IPresenter {
        fun getMessageInfo(page: Int, pageSize: Int,isRefresh: Boolean)
        fun getMessageDetailInfo(id: String?)
    }
}