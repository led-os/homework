package yc.com.homework.index.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.index.domain.bean.SearchRecordInfo

/**
 *
 * Created by wanglin  on 2019/3/29 10:23.
 */
class RecentSearchAdapter(records: List<SearchRecordInfo>?) : BaseQuickAdapter<SearchRecordInfo, BaseViewHolder>(R.layout.item_recent_search, records) {
    override fun convert(helper: BaseViewHolder?, item: SearchRecordInfo?) {
        helper?.let {
            item?.let {
//                LogUtil.msg("item:  ${item.name}")
                helper.setText(R.id.tv_content, item.name)
            }
        }
    }
}