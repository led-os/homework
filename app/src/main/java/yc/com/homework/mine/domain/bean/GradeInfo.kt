package yc.com.homework.mine.domain.bean

/**
 *
 * Created by wanglin  on 2018/11/30 16:28.
 */
class GradeInfo {


    var grade_id: String? = null
    var name: String? = null

    constructor(grade_id: String?, name: String?) {
        this.grade_id = grade_id
        this.name = name
    }

    constructor()
}