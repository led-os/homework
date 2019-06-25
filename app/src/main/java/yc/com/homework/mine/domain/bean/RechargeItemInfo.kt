package yc.com.homework.mine.domain.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by wanglin  on 2018/11/24 11:46.
 */
class RechargeItemInfo {
    var id: String? = ""
    var name: String? = null
    var type_id: Int? = 5
    var price: Double? = 0.0
    var m_price: Double? = 0.0
    var vip_price: Double? = 0.0
    @JSONField(name = "pay_price")
    var money: Double? = 0.0


    /**
     * 点读解锁字段
     */
    var type_relate_val: Int? = 0

    var desp: String? = ""

    var unit: String? = ""
    var use_time_limit: Int? = 0
    var sort: Int? = 1
    var status: Int? = 1
    var num: Int? = 0


}