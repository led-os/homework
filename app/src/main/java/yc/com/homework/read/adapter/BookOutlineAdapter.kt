package yc.com.homework.read.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.kk.utils.LogUtil

import yc.com.homework.R
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.read.domain.bean.BookUnitInfo

/**
 *
 * Created by wanglin  on 2019/1/11 09:04.
 */
class BookOutlineAdapter(context: Context, bookUnitInfos: List<BookUnitInfo>?) : RecyclerView.Adapter<BookOutlineAdapter.MyHolder>() {

    var mContext: Context? = null
    var mDatas: List<BookUnitInfo>? = null

    companion object {
        const val ITEM_TYPE_TITLE = 1
        const val ITEM_TYPE_CONTENT = 2
    }


    fun setNewData(datas: List<BookUnitInfo>?) {
        this.mDatas = datas
        notifyDataSetChanged()
    }

    init {
        this.mContext = context
        this.mDatas = bookUnitInfos
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        val view = if (viewType == ITEM_TYPE_TITLE) {
            LayoutInflater.from(mContext).inflate(R.layout.book_outlin_item, parent, false)
        } else {
            LayoutInflater.from(mContext).inflate(R.layout.outline_item_view, parent, false)
        }

        return MyHolder(view, viewType)
    }

    override fun getItemCount(): Int {
        return if (mDatas == null) 0 else mDatas!!.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        val datas = mDatas
        datas?.let {
            val bookUnitInfo = datas[position]
            holder.itemTextView?.text = bookUnitInfo.title

            if (getItemViewType(position) == ITEM_TYPE_CONTENT) {
//                LogUtil.msg("can_see  ${bookUnitInfo.can_see}")
                holder.ivLock?.visibility = if (bookUnitInfo.can_see == 1 || UserInfoHelper.isVip()) View.GONE else View.VISIBLE
                holder.itemView.setOnClickListener {
                    clickListener?.let {
                        clickListener?.onItemClick(holder.itemView, position)
                    }
                }
            }
        }


    }

    override fun getItemViewType(position: Int): Int {

        val datas = mDatas

        if (position >= 0 && (datas != null && position < datas.size)) {
            return if (datas[position].headTitle) ITEM_TYPE_TITLE else ITEM_TYPE_CONTENT
        }

        return ITEM_TYPE_CONTENT
    }


    class MyHolder(view: View, itemType: Int) : RecyclerView.ViewHolder(view) {

        var itemTextView: TextView? = null
        var ivLock: ImageView? = null

        init {

            if (itemType == BookOutlineAdapter.ITEM_TYPE_TITLE) {
                itemTextView = view.findViewById(R.id.tv_item_title)
            } else if (itemType == BookOutlineAdapter.ITEM_TYPE_CONTENT) {
                itemTextView = view.findViewById(R.id.tv_item)
                ivLock = view.findViewById(R.id.iv_lock)

            }
        }

    }

    fun getItem(position: Int): BookUnitInfo? {
        return mDatas?.get(position)
    }

    interface OnItemClickListener {
        fun onItemClick(view: View?, position: Int)
    }

    var clickListener: OnItemClickListener? = null

    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.clickListener = listener
    }


}