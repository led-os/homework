package yc.com.homework.word.view.adapter

import android.view.View
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.domain.CourseVersionInfo


class CourseVersionItemClickAdapter(data: List<CourseVersionInfo>?) : BaseMultiItemQuickAdapter<CourseVersionInfo, BaseViewHolder>(data) {

    init {
        addItemType(BookWordInfo.CLICK_ITEM_VIEW, R.layout.read_add_book_item)
    }

    override fun convert(helper: BaseViewHolder?, item: CourseVersionInfo?) {

        helper?.let {
            item?.let {
                helper.setText(R.id.btn_read_grade_name, item.versionName)

                if (!item.isAdd) {
                    helper.setBackgroundRes(R.id.btn_read_grade_name, R.drawable.read_add_book_checked_btn)
                    helper.itemView.isClickable = false
                } else {
                    helper.itemView.isClickable = true
                    if (item.isSelected) {
                        helper.getView<View>(R.id.btn_read_grade_name).setBackgroundResource(R.drawable.read_add_book_select_line_btn)
                    } else {
                        helper.getView<View>(R.id.btn_read_grade_name).setBackgroundResource(R.drawable.read_add_book_line_btn)
                    }

                }
            }

        }

    }
}