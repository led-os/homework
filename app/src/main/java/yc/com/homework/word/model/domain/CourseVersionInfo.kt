package yc.com.homework.word.model.domain

import com.alibaba.fastjson.annotation.JSONField
import com.chad.library.adapter.base.entity.MultiItemEntity

/**
 * Created by admin on 2017/7/26.
 * 教材版本
 */

class CourseVersionInfo : MultiItemEntity {

    var type = CLICK_ITEM_VIEW

    var id: String? = null

    @JSONField(name = "version_id")
    var versionId: String? = null

    var period: String? = null

    var grade: String? = null

    @JSONField(name = "part_type")
    var partType: String? = null

    var sort: String? = null

    @JSONField(name = "name")
    var versionName: String? = null

    @JSONField(name = "book_id")
    var bookId: String? = null

    @JSONField(name = "cover_img")
    var bookImageUrl: String? = null

    @JSONField(name = "grade_name")
    var gradeName: String? = null

    var isSelected = false

    var isAdd = true

    constructor(type: Int) {
        this.type = type
    }

    constructor() {}

    override fun getItemType(): Int {
        return type
    }

    companion object {

        val CLICK_ITEM_VIEW = 1
    }

}
