package yc.com.homework.examine.adapter

import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.kk.utils.LogUtil
import yc.com.homework.R
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.examine.widget.VerticalTextView
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo
import yc.com.homework.wall.utils.UiUtils
import java.math.BigDecimal

/**
 *
 * Created by wanglin  on 2018/11/27 11:49.
 */
class HistoryDetailAdapter(list: List<HomeworkDetailInfo>?) : BaseQuickAdapter<HomeworkDetailInfo, BaseViewHolder>(R.layout.history_detail_item, list) {
    private var mStatus: Int = 0

    constructor(list: List<HomeworkDetailInfo>?, status: Int) : this(list) {
        this.mStatus = status
    }

    override fun convert(helper: BaseViewHolder?, item: HomeworkDetailInfo?) {

        helper?.let {
            val llTopUnCheck = helper.getView<LinearLayout>(R.id.ll_top_uncheck)
            val llCheck = helper.getView<LinearLayout>(R.id.ll_check)
            val rlNoCheck = helper.getView<RelativeLayout>(R.id.rl_no_check)
            val tvTeacher1 = helper.getView<TextView>(R.id.tv_teacher1)

            item?.let {
                when (mStatus) {
                    0, 1, 3 -> {
                        llTopUnCheck.visibility = View.GONE
                        llCheck.visibility = View.GONE
                        rlNoCheck.visibility = View.VISIBLE
                        tvTeacher1.visibility = if (mStatus == 0) View.GONE else View.VISIBLE
                        helper.setVisible(R.id.iv_read, false)
                        helper.setText(R.id.tv_date1, UiUtils.longToString(item.createTime))
                        helper.setText(R.id.tv_subject, UiUtils.getSubjectById(mContext, item.subjectId))
                        val tvSubject = helper.getView<TextView>(R.id.tv_subject)
                        setSubjectBg(item.subjectId, tvSubject)
                        tvTeacher1.text = if (mStatus == 1) String.format(mContext.getString(R.string.teacher_checking), item.teacher_name) else item.reason
                    }
                    2 -> {
                        llTopUnCheck.visibility = View.VISIBLE
                        llCheck.visibility = View.VISIBLE
                        rlNoCheck.visibility = View.GONE


                        val score = BigDecimal(item.score)
                                .setScale(0, BigDecimal.ROUND_HALF_UP).intValueExact()
                        helper.setText(R.id.verticalTextView, UiUtils.getSubjectById(mContext, item.subjectId))
                                .setText(R.id.tv_date, UiUtils.longToString(item.correct_time))
                                .setText(R.id.tv_week, UiUtils.getWeek(item.correct_time))
                                .setText(R.id.tv_accuracy, "$score%")
                                .setText(R.id.tv_correct, "对 ${item.right_num} 题")
                                .setText(R.id.tv_error, "错 ${item.error_num} 题")
                                .setText(R.id.tv_teacher, String.format(mContext.getString(R.string.teacher_check), item.teacher_name))

                        if (!TextUtils.isEmpty(item.remark)) helper.setText(R.id.tv_remark, item.remark)
                        GlideHelper.circleIvator(mContext, helper.getView(R.id.iv_teacher_avator), item.teacher_face, R.mipmap.default_teacher_icon, R.mipmap.default_teacher_icon, true)
                        val tvSubject = helper.getView<VerticalTextView>(R.id.verticalTextView)
                        setSubjectBg(item.subjectId, tvSubject)
                        if (item.isRead == 1) {
                            helper.setVisible(R.id.iv_read, false)
                        } else {
                            helper.setVisible(R.id.iv_read, true)
                        }

//                        LogUtil.msg("is_read  ${item.isRead}")

                        helper.setImageResource(R.id.iv_score, UiUtils.getScorePic(score))


                    }
                }

                GlideHelper.circleIvator(mContext, helper.getView(R.id.iv_homework), item.image, R.mipmap.history_homework_exam, R.mipmap.history_homework_exam, false)
            }
        }

    }

    private fun setSubjectBg(subjectId: Int, view: View) {
        when (subjectId) {
            1 -> view.background = ContextCompat.getDrawable(mContext, R.drawable.subject_yellow_bg)
            2 -> view.background = ContextCompat.getDrawable(mContext, R.drawable.subject_blue_bg)
            3 -> view.background = ContextCompat.getDrawable(mContext, R.drawable.subject_green_bg)
        }
    }
}