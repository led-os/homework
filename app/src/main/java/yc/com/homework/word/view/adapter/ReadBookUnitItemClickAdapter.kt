package yc.com.homework.word.view.adapter

import android.content.Context

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder

import yc.com.blankj.utilcode.util.StringUtils
import yc.com.homework.R
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.domain.UnitInfo


class ReadBookUnitItemClickAdapter(data: List<UnitInfo>?) : BaseMultiItemQuickAdapter<UnitInfo, BaseViewHolder>(data) {

    init {
        addItemType(BookWordInfo.CLICK_ITEM_VIEW, R.layout.read_book_unit_item)
    }

    override fun convert(helper: BaseViewHolder, item: UnitInfo) {
        helper.setText(R.id.tv_book_unit_name, item.name)
                .setText(R.id.tv_book_unit_total, if (StringUtils.isEmpty(item.sentenceCount)) "0" else item.sentenceCount + mContext.getString(R.string.read_word_sentence_text))
        if (item.free == 1) {
            helper.setVisible(R.id.iv_course_vip, false)
        } else {

            if (UserInfoHelper.isVip()) {
                helper.setVisible(R.id.iv_course_vip, false)
            } else {
                helper.setVisible(R.id.iv_course_vip, true)
            }

        }
    }
}