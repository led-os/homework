package yc.com.homework.mine.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.domain.bean.RechargeItemInfo
import yc.com.homework.pay.OrderInfo

/**
 *
 * Created by wanglin  on 2018/11/24 12:01.
 */
class RechargeEngine(context: Context) : BaseEngine(context) {

    fun getRechargeItemInfos(type_id: String): Observable<ResultInfo<ArrayList<RechargeItemInfo>>> {


        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.goods_info, object : TypeReference<ResultInfo<ArrayList<RechargeItemInfo>>>() {}.type, mutableMapOf(
                "type_id" to type_id
        ), true, true, true) as Observable<ResultInfo<ArrayList<RechargeItemInfo>>>
    }


    fun createOrder(goods_num: Int?, payway_name: String?, money: String?, goods_id: String?,title:String?): Observable<ResultInfo<OrderInfo>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.orders_init, object : TypeReference<ResultInfo<OrderInfo>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "num" to "$goods_num",
                "pay_way_name" to payway_name,
                "money" to money,
                "goods_id" to goods_id,
                "title" to title
        ), true, true, true) as Observable<ResultInfo<OrderInfo>>
    }
}