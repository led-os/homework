package yc.com.homework.read.adapter

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kk.utils.LogUtil
import com.kk.utils.ScreenUtil
import yc.com.homework.R
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.read.domain.bean.BookInfo

/**
 *
 * Created by wanglin  on 2019/1/9 13:58.
 */
class BookReadMainAdapter(bookInfos: List<BookInfo>?) : BaseQuickAdapter<BookInfo, BaseViewHolder>(R.layout.item_book_read_main, bookInfos) {


    private var mIsEdit: Boolean = false


    fun setEdit(isEdit: Boolean) {
        this.mIsEdit = isEdit
        notifyDataSetChanged()
    }

    fun getEdit(): Boolean {
        return mIsEdit
    }


    override fun convert(helper: BaseViewHolder?, item: BookInfo?) {
        val position = helper?.adapterPosition

        val ivCover = helper?.getView<ImageView>(R.id.iv_book_cover)
        val ivDelete = helper?.getView<ImageView>(R.id.iv_delete)
//        LogUtil.msg("position: $position ")

        item?.let {
            if (position == 0) {
                ivCover!!.setImageResource(item.resId)
                helper.setVisible(R.id.tv_book_title, false).setVisible(R.id.tv_book_grade, false)
                ivDelete?.visibility = View.INVISIBLE
            } else {
                helper?.setText(R.id.tv_book_title, item.versionName)?.addOnClickListener(R.id.iv_delete)
                setItemGrade(item, helper)
                GlideHelper.cornerPic(mContext, ivCover!!, item.coverImg, 0, 0, false, false)
                if (mIsEdit) {
                    ivDelete?.visibility = View.VISIBLE
                } else {
                    ivDelete?.visibility = View.INVISIBLE
                }

            }


//            setItemSpace(helper, position!!)
        }


    }

    private fun setItemGrade(item: BookInfo, helper: BaseViewHolder?) {
        val grade = when (item.grade) {
            "1" -> "一年级"
            "2" -> "二年级"
            "3" -> "三年级"
            "4" -> "四年级"
            "5" -> "五年级"
            "6" -> "六年级"
            "7" -> "七年级"
            "8" -> "八年级"
            "9" -> "九年级"
            else -> ""
        }
        val part = when (item.volumesId) {
            "1" -> "上册"
            "2" -> "下册"
            else -> ""
        }
        helper?.setText(R.id.tv_book_grade, "$grade$part")
    }


    private fun setItemSpace(helper: BaseViewHolder?, position: Int) {
        helper?.let {
            val itemView = helper.itemView
            val layoutParams = itemView.layoutParams as ViewGroup.MarginLayoutParams
            if (position % 2 == 0) {
                layoutParams.rightMargin = ScreenUtil.dip2px(mContext, 10f)
            } else {
                layoutParams.leftMargin = ScreenUtil.dip2px(mContext, 10f)
            }
            itemView.layoutParams = layoutParams

        }
    }
}