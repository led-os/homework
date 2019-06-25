package yc.com.homework.wall.adapter

import android.support.v4.content.ContextCompat
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import yc.com.homework.R
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.wall.domain.bean.HomeworkInfo
import yc.com.homework.wall.utils.UiUtils

/**
 *
 * Created by wanglin  on 2018/11/27 09:58.
 */
class WallDetailAdapter(homeworkInfos: List<HomeworkInfo>?) : BaseQuickAdapter<HomeworkInfo, BaseViewHolder>(R.layout.homework_wall_item, homeworkInfos) {
    override fun convert(helper: BaseViewHolder?, item: HomeworkInfo?) {
        helper?.let {
            helper.setText(R.id.tv_subject, UiUtils.getSubjectById(mContext, item?.subjectId))
                    .setText(R.id.tv_date, UiUtils.longToString(item?.addTime))
                    .setText(R.id.tv_week, UiUtils.getWeek(item?.addTime))
                    .setText(R.id.tv_grade, UiUtils.getGradeById(item?.gradeId))



            GlideHelper.cornerPic(mContext, helper.getView(R.id.iv_homework), item?.pic, R.drawable.wall_item_placehodler, R.drawable.wall_item_placehodler, true, false)//item?.pic

            val tvSubject = helper.getView<TextView>(R.id.tv_subject)
            when (item?.subjectId) {
                1 -> tvSubject.background = ContextCompat.getDrawable(mContext, R.drawable.subject_yellow_bg)
                2 -> tvSubject.background = ContextCompat.getDrawable(mContext, R.drawable.subject_blue_bg)
                3 -> tvSubject.background = ContextCompat.getDrawable(mContext, R.drawable.subject_green_bg)
            }

        }

    }
}