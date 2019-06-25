package yc.com.homework.mine.adapter

import android.view.View
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kk.utils.LogUtil
import yc.com.homework.R
import yc.com.homework.mine.domain.bean.CoinDetailInfo
import yc.com.homework.wall.utils.UiUtils

/**
 *
 * Created by wanglin  on 2018/11/24 09:10.
 */
class CoinDetailAdapter(coinDetailInfos: List<CoinDetailInfo>?) : BaseQuickAdapter<CoinDetailInfo, BaseViewHolder>(R.layout.coin_detail_item, coinDetailInfos) {
    override fun convert(helper: BaseViewHolder?, item: CoinDetailInfo?) {
        var coin = "${item!!.coin}"
        if (item.coin > 0) {
            coin = "+${item.coin}"
        }
        helper!!.setText(R.id.tv_title, item.title)
                .setText(R.id.tv_consume, coin)
                .setText(R.id.tv_date, UiUtils.longToString(item.add_time))
                .setText(R.id.tv_total, "${item.total}")

        val position = helper.adapterPosition
        val tintTextView = helper.getView<TextView>(R.id.tv_title_tint)

        if (position == 1) {
            tintTextView.visibility = View.VISIBLE
        } else {
            tintTextView.visibility = View.GONE
        }
        if (position == mData.size) {
            helper.setVisible(R.id.divider, false)
        }
    }
}