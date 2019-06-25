package yc.com.homework.word.view.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.bumptech.glide.Glide
import yc.com.blankj.utilcode.util.ActivityUtils
import yc.com.homework.R
import yc.com.homework.word.model.domain.WordDetailInfo
import yc.com.homework.word.model.domain.WordInfo

/**
 * Created by admin on 2017/8/16.
 */

class ReadWordExpandAdapter(private val mContext: Context, wordInfos: List<WordInfo>?, private var wordDetailInfos: List<WordDetailInfo>?) : BaseExpandableListAdapter() {

    var wordInfos: List<WordInfo>? = null
        private set

    private var expandableListView: ExpandableListView? = null
    private var lastExpandPosition = -1

    lateinit var mItemClick: ItemViewClickListener

    fun setExpandableListView(expandableListView: ExpandableListView) {
        this.expandableListView = expandableListView
    }

    fun setLastExpandPosition(lastExpandPosition: Int) {
        this.lastExpandPosition = lastExpandPosition
    }

    interface ItemViewClickListener {
        fun groupWordClick(gPosition: Int)
    }

    fun setItemDetailClick(itemClick: ItemViewClickListener) {
        this.mItemClick = itemClick
    }

    init {
        this.wordInfos = wordInfos
    }

    fun setNewDatas(wordInfos: List<WordInfo>?, wordDetailInfos: List<WordDetailInfo>?) {
        this.wordInfos = wordInfos
        this.wordDetailInfos = wordDetailInfos
    }

    override fun getGroupCount(): Int {
        return if (wordInfos != null) wordInfos!!.size else 0
    }

    override fun getChildrenCount(groupPosition: Int): Int {
        return 1
    }

    override fun getGroup(groupPosition: Int): Any? {
        return wordInfos?.get(groupPosition)?.name
    }

    override fun getChild(groupPosition: Int, childPosition: Int): Any? {
        return wordDetailInfos?.get(groupPosition)?.wordExample
    }

    override fun getGroupId(groupPosition: Int): Long {
        return groupPosition.toLong()
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return childPosition.toLong()
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun getGroupView(groupPosition: Int, isExpanded: Boolean, convertView: View?, parent: ViewGroup): View {
        var itemView = convertView

        var gHolder: GroupViewHolder? = null
        if (itemView == null) {
            gHolder = GroupViewHolder()
            itemView = LinearLayout.inflate(mContext, R.layout.read_word_play_item, null) as View
            gHolder.wordNumberTv = itemView.findViewById(R.id.tv_word_number)
            gHolder.wordTv = itemView.findViewById(R.id.tv_en_word)
            gHolder.wordCnTv = itemView.findViewById(R.id.tv_cn_word)
            gHolder.wordAudioIv = itemView.findViewById(R.id.iv_read_word)
            gHolder.readWordLayout = itemView.findViewById(R.id.layout_read_word_audio)
            itemView.tag = gHolder
        } else {
            gHolder = itemView.tag as GroupViewHolder
        }

        gHolder.wordNumberTv?.text = "${groupPosition + 1}"
        gHolder.wordTv?.text = wordInfos?.get(groupPosition)?.name
        gHolder.wordCnTv?.text = wordInfos?.get(groupPosition)?.means

        if (isExpanded) {
            itemView.setBackgroundResource(R.mipmap.read_word_item_selected)
        } else {
            itemView.setBackgroundResource(R.mipmap.read_word_item_normal)
        }

        if (wordInfos?.get(groupPosition)?.isPlay!!) {
            Glide.with(mContext).load(R.mipmap.read_audio_gif_play).into(gHolder.wordAudioIv)
        } else {
            Glide.with(mContext).load(R.mipmap.read_word_default).into(gHolder.wordAudioIv)
        }

        gHolder.readWordLayout?.setOnClickListener { mItemClick.groupWordClick(groupPosition) }

        return itemView
    }

    fun setViewPlayState(position: Int, listView: ListView, isPlay: Boolean) {
        var position = position
        if (lastExpandPosition != -1 && expandableListView!!.isGroupExpanded(lastExpandPosition) && position > lastExpandPosition) {
            position += 1
        }

        val hCount = listView.headerViewsCount
        val fCount = listView.footerViewsCount
        val fVisiblePos = listView.firstVisiblePosition - hCount
        val lVisiblePos = listView.lastVisiblePosition - fCount

        var view: View? = null
        if (position in fVisiblePos..lVisiblePos) {
            view = listView.getChildAt(position - fVisiblePos)
        }

        if (view != null && ActivityUtils.isValidContext(mContext)) {
            if (isPlay) {
                Glide.with(mContext).load(R.mipmap.read_audio_gif_play).into(view.findViewById<View>(R.id.iv_read_word) as ImageView)
            } else {
                Glide.with(mContext).load(R.mipmap.read_word_default).into(view.findViewById<View>(R.id.iv_read_word) as ImageView)
            }
        }
    }

    fun setChildViewPlayState(view: View?, isPlay: Boolean) {

        if (view != null && ActivityUtils.isValidContext(mContext)) {
            if (isPlay) {
                Glide.with(mContext).load(R.mipmap.read_audio_gif_play).into(view.findViewById<View>(R.id.iv_word_detail_audio) as ImageView)
            } else {
                Glide.with(mContext).load(R.mipmap.read_word_default).into(view.findViewById<View>(R.id.iv_word_detail_audio) as ImageView)
            }
        }
    }

    override fun getChildView(groupPosition: Int, childPosition: Int, isLastChild: Boolean, childConvertView: View?, parent: ViewGroup): View {
        var childView = childConvertView

        var childHolder: GroupChildViewHolder?=null
        if (childView == null) {
            childHolder = GroupChildViewHolder()
            childView = View.inflate(mContext, R.layout.read_word_play_item_detail, null) as View
            childHolder.wordTv = childView.findViewById(R.id.tv_en_word_detail)
            childHolder.wordCnTv = childView.findViewById(R.id.tv_cn_word_detail)
            childHolder.wordAudioIv = childView.findViewById(R.id.iv_word_detail_audio)
            childView.tag = childHolder
        } else {
            childHolder = childView.tag as GroupChildViewHolder
        }
        val wordDetailInfo = wordDetailInfos?.get(groupPosition)
        childHolder.wordTv?.text = wordDetailInfo?.wordExample
        childHolder.wordCnTv?.text = wordDetailInfo?.wordCnExample
        if (wordDetailInfo?.isPlay!!) {
            Glide.with(mContext).load(R.mipmap.read_audio_gif_play).into(childHolder.wordAudioIv)
        } else {
            Glide.with(mContext).load(R.mipmap.read_word_default).into(childHolder.wordAudioIv)
        }
        return childView
    }

    override fun isChildSelectable(groupPosition: Int, childPosition: Int): Boolean {
        return true
    }

    internal inner class GroupViewHolder {
        var wordNumberTv: TextView? = null
        var wordTv: TextView? = null
        var wordCnTv: TextView? = null
        var wordAudioIv: ImageView? = null
        var readWordLayout: LinearLayout? = null
    }

    internal inner class GroupChildViewHolder {
        var wordTv: TextView? = null
        var wordCnTv: TextView? = null
        var wordAudioIv: ImageView? = null
    }

}
