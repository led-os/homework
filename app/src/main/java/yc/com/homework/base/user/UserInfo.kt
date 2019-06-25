package yc.com.homework.base.user

import com.alibaba.fastjson.annotation.JSONField


/**
 * Created by wanglin  on 2018/11/29 16:28.
 */
class UserInfo {

    @JSONField(name = "user_id")
    var id: String? = null

    var name: String? = null
    @JSONField(name = "mobile")
    var tel: String? = null

    var imeil: String? = null

    @JSONField(name = "nick_name")
    var nickname: String? = null

    var face: String? = null

    /**
     * 1.游客登录 2 手机号登录 3 qq  4 微信
     */
    var type: Int = 0

    var email: String? = null

    var pwd: String = ""

    @JSONField(name = "invite_code")
    var inviteCode: String? = ""

    var grade_id: Int = 0
    var coin: Int? = 0

    var upload_times: Int? = 0

    var vip_start_time: Long? = 0
    var vip_end_time: Long? = 0

    var has_vip: Int = 0//0非vip 1vip

    fun getGrade(): String? {
        return when (grade_id) {
            1 -> "一年级"
            2 -> "二年级"
            3 -> "三年级"
            4 -> "四年级"
            5 -> "五年级"
            6 -> "六年级"
            else -> ""
        }

    }


}
