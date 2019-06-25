package yc.com.homework.word.view.adapter

import android.view.View
import android.widget.LinearLayout

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder


import yc.com.homework.R
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.utils.DrawableUtils
import yc.com.homework.word.model.domain.LetterInfo


class ReadLetterItemClickAdapter(data: List<LetterInfo>?) : BaseMultiItemQuickAdapter<LetterInfo, BaseViewHolder>(data) {

    private val isEdit = false

    init {
        addItemType(BookWordInfo.CLICK_ITEM_VIEW, R.layout.read_letter_item)
    }


    override fun convert(helper: BaseViewHolder, item: LetterInfo) {
        helper.setText(R.id.tv_letter, item.letterName)
        when (helper.adapterPosition) {
            0 -> (helper.getView<View>(R.id.layout_letter) as LinearLayout).background = DrawableUtils.getBgColor(mContext, 3, R.color.read_letter_color_col1)
            1 -> (helper.getView<View>(R.id.layout_letter) as LinearLayout).background = DrawableUtils.getBgColor(mContext, 3, R.color.read_letter_color_col2)
            2 -> (helper.getView<View>(R.id.layout_letter) as LinearLayout).background = DrawableUtils.getBgColor(mContext, 3, R.color.read_letter_color_col3)
            3 -> (helper.getView<View>(R.id.layout_letter) as LinearLayout).background = DrawableUtils.getBgColor(mContext, 3, R.color.read_letter_color_col4)
            4 -> (helper.getView<View>(R.id.layout_letter) as LinearLayout).background = DrawableUtils.getBgColor(mContext, 3, R.color.read_letter_color_col5)
            else -> {
            }
        }
    }

}