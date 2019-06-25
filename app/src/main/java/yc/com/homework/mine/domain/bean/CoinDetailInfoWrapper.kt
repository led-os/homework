package yc.com.homework.mine.domain.bean

import yc.com.homework.base.user.UserInfo

/**
 * Created by wanglin  on 2018/12/11 11:33.
 */
class CoinDetailInfoWrapper {

    /**
     * bill_list : [{"id":1,"title":"充值","coin":100,"cash":99,"user_id":"8","type_id":1,"add_time":1544411297,"hidden":0,"total":100},{"id":2,"title":"检查作业","coin":-10,"cash":0,"user_id":"8","type_id":2,"add_time":1544416297,"hidden":0,"total":90},{"id":3,"title":"分享作业","coin":5,"cash":0,"user_id":"8","type_id":3,"add_time":1544417297,"hidden":0,"total":95}]
     * instructions : 金币用于批改作业,检查一张批改作业消耗1.2金币
     * user_info : {"face":"http://zyl.wk2.com/uploads/user/5c088f0f2ff2b.png","user_id":8,"name":"15623368710","mobile":"15623368710","nick_name":"哦哟哟","money":"0.00","imeil":"21cbf2bdfb777627","grade_id":2,"type":2,"coin":0,"invite_code":"F9WR"}
     */

    var instructions: String? = null
    var user_info: UserInfo? = null
    var bill_list: List<CoinDetailInfo>? = null


}
