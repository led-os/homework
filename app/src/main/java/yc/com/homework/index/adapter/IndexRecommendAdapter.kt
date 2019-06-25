package yc.com.homework.index.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kk.utils.LogUtil
import yc.com.homework.R
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.index.domain.bean.NewsInfo

/**
 *
 * Created by wanglin  on 2019/3/1 16:44.
 */
class IndexRecommendAdapter(newsInfos: List<NewsInfo>?) : BaseQuickAdapter<NewsInfo, BaseViewHolder>(R.layout.item_news_info, newsInfos) {

    private var mIsIndex: Boolean = false

    constructor(newsInfos: List<NewsInfo>?, isIndex: Boolean) : this(newsInfos) {
        this.mIsIndex = isIndex
    }

    override fun convert(helper: BaseViewHolder?, item: NewsInfo?) {
        helper?.let {
            item?.let {
                var readCount = item.readCount
                if (readCount < 200) readCount = 200

                helper.setText(R.id.tv_title, item.title).setText(R.id.tv_sub_title, item.subTitle)
                        .setText(R.id.tv_read_count, String.format(mContext.getString(R.string.read_count), readCount))
                GlideHelper.cornerPic(mContext, helper.getView(R.id.iv_news_icon), item.img, 0, 0, false, false)

                if (mIsIndex) {
                    val pos = helper.adapterPosition
                    if (pos == mData.size - 1) {
                        helper.setVisible(R.id.divider, false)
                    }
                }
                }
            }
        }
    }