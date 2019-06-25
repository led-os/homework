package yc.com.homework.read.adapter

import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kk.utils.ScreenUtil
import com.vondear.rxtools.RxDeviceTool
import com.vondear.rxtools.RxImageTool
import yc.com.homework.R
import yc.com.homework.read.domain.bean.GradeVersionInfo

/**
 *
 * Created by wanglin  on 2019/1/10 16:11.
 */
class BookSelectItemAdapter(gradeInfos: List<GradeVersionInfo>?) : BaseQuickAdapter<GradeVersionInfo, BaseViewHolder>(R.layout.item_book_select, gradeInfos) {

    var mType: Int = 0

    constructor(gradeInfos: List<GradeVersionInfo>?, type: Int) : this(gradeInfos) {
        this.mType = type
    }

    override fun convert(helper: BaseViewHolder?, item: GradeVersionInfo?) {

        helper?.let {
            val tvItem = helper.getView<TextView>(R.id.tv_item_name)

            val layoutParams = tvItem.layoutParams

            layoutParams.width = (ScreenUtil.getWidth(mContext) - RxImageTool.dip2px(40f)) / 3 - 10

            tvItem.layoutParams = layoutParams

            item?.let {
                when (mType) {
                    1 -> tvItem.text = item.grade_name
                    2 -> tvItem.text = item.version_name
                    3 -> {
                        tvItem.text = item.subject_name
                        if (helper.adapterPosition == 0) {
                            tvItem.isSelected = true
                        }
                    }
                    else -> tvItem.text = item.version_name

                }

            }
        }


    }
}