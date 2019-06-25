package yc.com.homework.mine.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.mine.domain.bean.RechargeItemInfo

/**
 *
 * Created by wanglin  on 2018/11/24 11:45.
 */
class RechargeItemAdapter(rechargeList: List<RechargeItemInfo>?) : BaseQuickAdapter<RechargeItemInfo, BaseViewHolder>(R.layout.recharge_item, rechargeList) {
    override fun convert(helper: BaseViewHolder?, item: RechargeItemInfo?) {

        helper?.let {
            helper.setText(R.id.tv_name, item?.name)
                    .setText(R.id.tv_money, "${item?.money}")

            val adapterPosition = helper.adapterPosition
            if (adapterPosition == 0) {
                helper.itemView.isSelected = true
            }
        }

    }
}