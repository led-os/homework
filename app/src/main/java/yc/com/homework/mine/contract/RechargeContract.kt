package yc.com.homework.mine.contract

import android.graphics.Bitmap
import yc.com.base.*
import yc.com.homework.mine.domain.bean.RechargeItemInfo
import yc.com.homework.pay.OrderInfo

/**
 *
 * Created by wanglin  on 2018/11/24 12:04.
 */
interface RechargeContract {

    interface View : IView, IDialog, ILoading, INoData, INoNet, IHide {
        fun showRechargeInfos(t: List<RechargeItemInfo>?)
        fun showOrderInfo(orderInfo: OrderInfo?)
        fun showBitmap(bitmap: Bitmap)
    }

    interface Presenter : IPresenter {
        fun getRechargeItemInfos(type_id: String)
        fun createOrder(goods_num: Int?, payway_name: String?, money: String?, goods_id: String?, title: String?)
    }
}