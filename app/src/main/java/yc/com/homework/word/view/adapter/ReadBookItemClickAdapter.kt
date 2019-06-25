package yc.com.homework.word.view.adapter

import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.word.model.domain.BookWordInfo


class ReadBookItemClickAdapter(data: List<BookWordInfo>?) : BaseMultiItemQuickAdapter<BookWordInfo, BaseViewHolder>(data) {

    var editState = false


    init {
        addItemType(BookWordInfo.CLICK_ITEM_VIEW, R.layout.read_book_item)
    }

    override fun convert(helper: BaseViewHolder, item: BookWordInfo) {

        helper.addOnClickListener(R.id.iv_delete_book)

        val mBookCover = helper.getView<ImageView>(R.id.iv_book_cover)
        val mDeleteBook = helper.getView<ImageView>(R.id.iv_delete_book)

        helper.setText(R.id.tv_book_version_name, item.versionName).setText(R.id.tv_book_grade_name, item.gradeName)

        if (helper.adapterPosition > 0) {
            GlideHelper.cornerPic(mContext, mBookCover, item.coverImg, 0, 0, true, false)
        } else {
            mBookCover.setImageResource(R.mipmap.read_book_add)
        }

        if (this.editState) {
            if (helper.adapterPosition > 0) {
                mDeleteBook.visibility = View.VISIBLE
            } else {
                mDeleteBook.visibility = View.INVISIBLE
            }
        } else {
            mDeleteBook.visibility = View.INVISIBLE
        }
    }

}