package yc.com.homework.word.view.adapter

import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.domain.GradeInfo


class GradeItemClickAdapter(data: List<GradeInfo>?) : BaseMultiItemQuickAdapter<GradeInfo, BaseViewHolder>(data) {


    init {
        addItemType(BookWordInfo.CLICK_ITEM_VIEW, R.layout.read_add_book_item)
    }

    override fun convert(helper: BaseViewHolder, item: GradeInfo) {
        helper.setText(R.id.btn_read_grade_name, item.name)
        if (item.isSelected()) {
            helper.getView<View>(R.id.btn_read_grade_name).setBackgroundResource(R.drawable.read_add_book_select_line_btn)
        } else {
            helper.getView<View>(R.id.btn_read_grade_name).setBackgroundResource(R.drawable.read_add_book_line_btn)
        }
    }
}