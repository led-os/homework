package yc.com.homework.index.adapter

import android.view.ViewGroup
import android.widget.FrameLayout
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kk.utils.LogUtil
import com.qq.e.ads.nativ.NativeExpressADView
import yc.com.homework.R
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.index.domain.bean.NewsInfo
import java.util.HashMap

/**
 *
 * Created by wanglin  on 2019/3/1 16:44.
 */
class IndexReadAdapter(newsInfos: List<NewsInfo>?) : BaseMultiItemQuickAdapter<NewsInfo, BaseViewHolder>(newsInfos) {

    private lateinit var mAdViewPositionMap: HashMap<NativeExpressADView?, Int>

    constructor(newsInfos: List<NewsInfo>?, adViewPositionMap: HashMap<NativeExpressADView?, Int>) : this(newsInfos) {
        this.mAdViewPositionMap = adViewPositionMap
        addItemType(NewsInfo.ADV, R.layout.item_express_ad)
        addItemType(NewsInfo.CONTENT, R.layout.item_news_info)
    }


    fun addADViewToPosition(position: Int, newsInfo: NewsInfo) {
        if (mData != null && position >= 0 && position < mData.size && newsInfo.view != null) {
            mData.add(position, newsInfo)
            notifyDataSetChanged()
        }
    }

    // 移除NativeExpressADView的时候是一条一条移除的
    fun removeADView(position: Int, adView: NativeExpressADView) {
        mData.removeAt(position)
        notifyItemRemoved(position) // position为adView在当前列表中的位置
    }

    override fun convert(helper: BaseViewHolder?, item: NewsInfo?) {
        helper?.let {
            item?.let {
                when (item.itemType) {
                    NewsInfo.ADV -> {
                        val adView = item.view
                        mAdViewPositionMap[adView] = helper.adapterPosition// 广告在列表中的位置是可以被更新的

                        val container = helper.getView(R.id.express_ad_container) as FrameLayout
                        if (container.childCount > 0 && container.getChildAt(0) === adView) {
                            return
                        }

                        if (container.childCount > 0) {
                            container.removeAllViews()
                        }

                        if (adView?.parent != null) {
                            (adView.parent as ViewGroup).removeView(adView)
                        }
                        adView?.render() // 调用render方法后sdk才会开始展示广告
                        container.addView(adView)

                    }

                    NewsInfo.CONTENT -> {
                        var readCount = item.readCount
                        if (readCount < 200) readCount = 200

                        helper.setText(R.id.tv_title, item.title).setText(R.id.tv_sub_title, item.subTitle)
                                .setText(R.id.tv_read_count, String.format(mContext.getString(R.string.read_count), readCount))
                        GlideHelper.cornerPic(mContext, helper.getView(R.id.iv_news_icon), item.img, 0, 0, false, false)


                        val pos = helper.adapterPosition
                        if (pos == mData.size - 1) {
                            helper.setVisible(R.id.divider, false)

                        }
                    }
                }
            }
        }


    }
}