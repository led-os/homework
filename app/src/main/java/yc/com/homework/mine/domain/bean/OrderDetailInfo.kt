package yc.com.homework.mine.domain.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by wanglin  on 2019/1/24 11:10.
 */
class OrderDetailInfo {

    @JSONField(name = "order_sn")
    var orderId: String? = ""
    @JSONField(name = "add_time")
    var time: Long? = 0
    var money: Double? = 0.0
    var state: Int? = 0
    var title: String? = ""
    var type: Int? = 0
    var status: Int? = 0
    var status_text: String? = ""


}