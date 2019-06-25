package yc.com.homework.mine.domain.bean

/**
 *
 * Created by wanglin  on 2018/12/5 15:03.
 */
class ApplyRuleInfo {

    var rjzg: DetailInfo? = null
    var gztd: DetailInfo? = null

    class DetailInfo {
        var title: String? = ""
        var desp: String? = ""
    }
}