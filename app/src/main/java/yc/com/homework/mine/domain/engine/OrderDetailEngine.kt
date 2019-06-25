package yc.com.homework.mine.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.domain.bean.OrderDetailInfo

/**
 *
 * Created by wanglin  on 2019/1/24 11:41.
 */
class OrderDetailEngine(context: Context) : BaseEngine(context) {

    fun getOrderDetailList(page: Int, page_size: Int): Observable<ResultInfo<ArrayList<OrderDetailInfo>>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.orders_lists, object : TypeReference<ResultInfo<ArrayList<OrderDetailInfo>>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "page" to "$page",
                "page_size" to "$page_size"
        ), true, true, true) as Observable<ResultInfo<ArrayList<OrderDetailInfo>>>
    }
}