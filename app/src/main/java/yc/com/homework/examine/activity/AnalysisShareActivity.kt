package yc.com.homework.examine.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.jakewharton.rxbinding.view.RxView
import com.umeng.socialize.UMShareAPI
import kotlinx.android.synthetic.main.activity_analysis_share.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.base.user.UserInfoHelper
import yc.com.base.ObservableManager
import yc.com.homework.mine.domain.bean.ShareInfo
import yc.com.homework.mine.fragment.ShareFragment
import yc.com.homework.wall.domain.bean.AnswerInfo
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo
import java.math.BigDecimal
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/12/7 15:24.
 */
class AnalysisShareActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>(), Observer {

    private var homeworkDetailInfo: HomeworkDetailInfo? = null
    private var APlus = intArrayOf(90, 91, 92, 93, 94, 95, 96, 97, 98, 99)
    private var A = intArrayOf(80, 81, 82, 83, 84, 85, 86, 87, 88, 89)

    private var B = intArrayOf(60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79)
    private var C = intArrayOf(40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 58, 59)


    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_analysis_share
    }


    @SuppressLint("SetTextI18n")
    override fun init() {
        ObservableManager.get().addMyObserver(this)

        homeworkDetailInfo = intent.getParcelableExtra("data")
        val answers = intent.getParcelableArrayListExtra<AnswerInfo>("answers")

        val score = BigDecimal(homeworkDetailInfo?.score)
                .setScale(0, BigDecimal.ROUND_HALF_UP).intValueExact()
        setStar(score = score)

        drawBitmap(homeworkDetailInfo?.image, homeworkDetailInfo!!, answers)
        tv_correct_num.text = String.format(getString(R.string.share_correct_num), homeworkDetailInfo!!.right_num)
        tv_error_num.text = String.format(getString(R.string.share_error_num), homeworkDetailInfo!!.error_num)
        tv_correct_rate.text = "正确率：$score%"

        if (!TextUtils.isEmpty(homeworkDetailInfo!!.remark))
            tv_send_word.text = "老师寄语：${homeworkDetailInfo!!.remark}"
        tv_invite_code.text = UserInfoHelper.getUserInfo().inviteCode



        RxView.clicks(rootView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            showShareFragment()
        }

    }


    private fun showShareFragment() {

        val shareFragment = ShareFragment()
        window.decorView.isDrawingCacheEnabled = true
        val bmp = window?.decorView?.drawingCache

        val shareInfo = ShareInfo()
        shareInfo.bitmap = bmp
        shareInfo.task_id = homeworkDetailInfo?.task_id
        shareFragment.setShareInfo(shareInfo)
        if (!shareFragment.isVisible)
            shareFragment.show(supportFragmentManager, "")
    }


    private fun drawBitmap(path: String?, data: HomeworkDetailInfo, answers: List<AnswerInfo>?) {

        Glide.with(this).load(path).asBitmap().fitCenter().into(object : BitmapImageViewTarget(iv_share_pic) {
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                super.onResourceReady(resource, glideAnimation)
                drawBitmap(resource, data, answers)
            }
        })
    }

    private fun drawBitmap(resource: Bitmap?, data: HomeworkDetailInfo, answers: List<AnswerInfo>?) {
        val bitmapHeight = resource!!.height
        val bitmapWidth = resource.width

        val bitmapBg = Bitmap.createBitmap(bitmapWidth, bitmapHeight, resource.config)
        val canvas = Canvas(bitmapBg)
        val matrix = Matrix()
        val picWidth = data.width
        val picHeight = data.height

        canvas.drawBitmap(resource, matrix, null)
        val scale = if (bitmapWidth / (picWidth * 1f) > bitmapHeight / (picHeight * 1f)) {
            bitmapHeight / (picHeight * 1f)
        } else {
            bitmapWidth / (picWidth * 1f)
        }

        val rightBm = BitmapFactory.decodeResource(resources, R.mipmap.right)
        val errorBm = BitmapFactory.decodeResource(resources, R.mipmap.error)
        val queryBm = BitmapFactory.decodeResource(resources, R.mipmap.query)
//        val answers = data.answer
        val paint = TextPaint()
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#58a1ed")
        paint.isAntiAlias = true

        paint.textSize = 18.0f


        if (answers != null)
            for ((i, value) in answers.withIndex()) {

                when (value.tagType) {
                    0 -> canvas.drawBitmap(rightBm, value.x * scale, value.y * scale, null)
                    1 -> canvas.drawBitmap(errorBm, value.x * scale, value.y * scale, null)
                    -1 -> {
                        canvas.drawBitmap(queryBm, value.x * scale, value.y * scale, null)

                    }
                }
            }

        iv_share_pic.setImageBitmap(bitmapBg)
        mHandler.postDelayed({
            showShareFragment()
        }, 500)
    }

    private fun setStar(score: Int) {
        var shareGrade = ""
        var result = 0
        when (score) {
            100 -> {
                shareGrade = "评级：A+"
                result = getRandom(APlus)
            }
            in IntRange(85, 100) -> {
                iv_star5.setImageResource(R.mipmap.dark_star)
                shareGrade = "评级：A"
                result = getRandom(A)
            }
            in IntRange(60, 85) -> {
                iv_star4.setImageResource(R.mipmap.dark_star)
                iv_star5.setImageResource(R.mipmap.dark_star)
                shareGrade = "评级：B"
                result = getRandom(B)
            }
            in IntRange(0, 60) -> {
                iv_star2.setImageResource(R.mipmap.dark_star)
                iv_star3.setImageResource(R.mipmap.dark_star)
                iv_star4.setImageResource(R.mipmap.dark_star)
                iv_star5.setImageResource(R.mipmap.dark_star)
                shareGrade = "评级：C"
                result = getRandom(C)
            }
        }
        tv_grade.text = shareGrade
        val title = "恭喜您本次作业正确率为$score%，已超过$result%的同学！继续加油哦!"

        tv_analysis_title.text = title

    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg != null) {
            when (arg) {
                is String -> {
                    if (TextUtils.equals("share_success", arg)) {
                        finish()
                    } else if (TextUtils.equals("share_fail", arg)) {
                        showShareFragment()
                    }
                }
            }
        }
    }

    private fun getRandom(arrays: IntArray): Int {
        val index = (Math.random() * arrays.size).toInt()
        return arrays[index]
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get().deleteMyObserver(this)
    }


}