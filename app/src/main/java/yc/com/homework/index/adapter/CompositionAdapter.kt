package yc.com.homework.index.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.index.domain.bean.CompositionInfo

/**
 *
 * Created by wanglin  on 2019/3/20 14:14.
 */
class CompositionAdapter(compositionInfos: List<CompositionInfo>?) : BaseQuickAdapter<CompositionInfo, BaseViewHolder>(R.layout.composition_item_view, compositionInfos) {


    override fun convert(helper: BaseViewHolder?, item: CompositionInfo?) {
        helper?.let {
            helper.setText(R.id.tv_composition_title, item?.name)
                    .setText(R.id.tv_composition_version, item?.version)
                    .setText(R.id.tv_composition_grade, item?.grade)
                    .setText(R.id.tv_composition_unit, item?.unit)
                    .setText(R.id.tv_composition_content, item?.content)
        }
    }

}