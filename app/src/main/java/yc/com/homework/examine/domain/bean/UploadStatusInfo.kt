package yc.com.homework.examine.domain.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by wanglin  on 2018/12/12 16:22.
 */
class UploadStatusInfo {
    //待批改
    @JSONField(name = "paidui")
    var status1: Int = 0
    //批改中
    @JSONField(name = "pigai")
    var status2: Int = 0
    //批改完成
    @JSONField(name = "wancheng")
    var status3: Int = 0

    @JSONField(name = "reback_num")
    var status4: Int = 0

}