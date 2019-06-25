package yc.com.homework.mine.domain.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by wanglin  on 2018/11/23 17:25.
 */
class MessageInfo {
    var id: String? = ""
    var title: String? = null
    @JSONField(name = "content")
    var desc: String? = null
    var icon: String? = null
    @JSONField(name = "add_time")
    var time: Long? = 0
    @JSONField(name = "see")
    var read: Int? = 0


}