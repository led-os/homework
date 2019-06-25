package yc.com.homework.examine.activity

import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_exam_detail_preview.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.wall.domain.bean.AnswerInfo
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo

/**
 *
 * Created by wanglin  on 2018/12/24 15:12.
 */
class ExamDetailPreviewActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {
    private var homeworkDetailInfo: HomeworkDetailInfo? = null
    override fun init() {
        homeworkDetailInfo = intent.getParcelableExtra("data")
        val answers = intent.getParcelableArrayListExtra<AnswerInfo>("answers")
        Glide.with(this).load(homeworkDetailInfo?.image).asBitmap().into(iv_exam_detail)

    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_exam_detail_preview

    }
}