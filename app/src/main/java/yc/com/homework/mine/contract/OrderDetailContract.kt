package yc.com.homework.mine.contract

import yc.com.base.*
import yc.com.homework.mine.domain.bean.OrderDetailInfo

/**
 *
 * Created by wanglin  on 2019/1/24 11:47.
 */
interface OrderDetailContract {
    interface View : IView, ILoading, INoNet, INoData, IHide {
        fun showOrderDetailList(data: ArrayList<OrderDetailInfo>)
    }

    interface Presenter : IPresenter {
        fun getOrderDetailList(page: Int, page_size: Int)
    }
}