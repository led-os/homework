package yc.com.homework.mine.domain.bean

/**
 *
 * Created by wanglin  on 2018/11/24 09:11.
 */
class CoinDetailInfo {
    var title: String? = null

    var date: String? = null

    /**
     * id : 1
     * title : 充值
     * coin : 100
     * cash : 99
     * user_id : 8
     * type_id : 1
     * add_time : 1544411297
     * hidden : 0
     * total : 100
     */

    var id: Int = 0

    var coin: Int = 0
    var cash: String? = ""
    var user_id: String? = null
    var type_id: Int = 0
    var add_time: Long = 0
    var hidden: Int = 0
    var total: Int = 0

}