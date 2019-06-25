package yc.com.homework.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.vondear.rxtools.RxTimeTool
import yc.com.homework.R
import yc.com.homework.mine.domain.bean.OrderDetailInfo
import java.util.*

/**
 *
 * Created by wanglin  on 2019/1/24 11:09.
 */
class OrderDetailAdapter(datas: List<OrderDetailInfo>?) : BaseQuickAdapter<OrderDetailInfo, BaseViewHolder>(R.layout.item_order_detail, datas) {
    override fun convert(helper: BaseViewHolder?, item: OrderDetailInfo?) {

        item?.let {
            helper?.setText(R.id.tv_order_sn, item.orderId)
                    ?.setText(R.id.tv_order_money, "¥${item.money}")
                    ?.setText(R.id.tv_order_title, item.title)
                    ?.setText(R.id.tv_order_state, String.format(mContext.getString(R.string.order_state), item.status_text))

            val time = item.time
            time?.let {
                val dateStr = RxTimeTool.date2String(Date(time))
                helper?.setText(R.id.tv_order_time, dateStr)
            }

        }
    }
}