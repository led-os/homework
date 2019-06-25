package yc.com.homework.word.view.adapter

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.domain.WordUnitInfo


class ReadWordUnitItemClickAdapter(data: List<WordUnitInfo>?) : BaseMultiItemQuickAdapter<WordUnitInfo, BaseViewHolder>(data) {

    init {
        addItemType(BookWordInfo.CLICK_ITEM_VIEW, R.layout.read_word_unit_item)
    }

    override fun convert(helper: BaseViewHolder, item: WordUnitInfo) {
        helper.setText(R.id.tv_book_unit_name, item.name)
                .setText(R.id.tv_book_unit_total, item.wordCount + mContext.getString(R.string.word_sentence_count_text))
    }
}