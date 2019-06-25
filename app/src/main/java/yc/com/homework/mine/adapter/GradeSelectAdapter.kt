package yc.com.homework.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kk.utils.LogUtil
import yc.com.homework.R
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.domain.bean.GradeInfo

/**
 *
 * Created by wanglin  on 2018/11/30 16:28.
 */
class GradeSelectAdapter(grades: List<GradeInfo>?) : BaseQuickAdapter<GradeInfo, BaseViewHolder>(R.layout.grade_select_item, grades) {
    var gradeId: Int? = 0

    init {
        gradeId = UserInfoHelper.getUserInfo()?.grade_id
        if (gradeId == 0) {
            gradeId = 1
        }

    }

    override fun convert(helper: BaseViewHolder?, item: GradeInfo?) {
        helper!!.setText(R.id.tv_title, item!!.name)
        val position = helper.adapterPosition

        if (position == (gradeId!! - 1)) helper.itemView.isSelected = true

    }
}