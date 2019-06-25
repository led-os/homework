package yc.com.homework.examine.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.*
import android.net.Uri
import android.provider.MediaStore
import android.support.v4.content.ContextCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.LogUtil
import com.kk.utils.ScreenUtil
import kotlinx.android.synthetic.main.activity_exam_detail.*
import yc.com.base.BaseActivity
import yc.com.homework.R
import yc.com.homework.base.config.Constant
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.wall.contract.WallDetailContract
import yc.com.homework.wall.domain.bean.AnswerInfo
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo
import yc.com.homework.wall.domain.bean.HomeworkInfo
import yc.com.homework.wall.presenter.WallDetailPresenter
import yc.com.homework.wall.utils.UiUtils
import java.math.BigDecimal
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/27 15:56.
 */
class ExamDetailActivity : BaseActivity<WallDetailPresenter>(), WallDetailContract.View {


    private var taskId: String = ""
    private var index: Int = 0

    private var rectF: RectF? = null


    override fun init() {
        mainToolBar.showNavigationIcon()
        mainToolBar.init(this)
        mainToolBar.setRightContainerVisible(false)

        mPresenter = WallDetailPresenter(this, this)

        index = intent.getIntExtra("index", 0)
        taskId = intent.getStringExtra("taskId")


        getData()

        initListener()
    }

    private fun getData() {
        when (index) {
            1 -> {
                mPresenter.getWallDetailInfo(taskId)
                mainToolBar.setTitle(getString(R.string.hot_homework_show))
                iv_service.visibility = View.GONE
                iv_share.visibility = View.GONE
            }
            2 -> {
                mPresenter.getHistoryDetailInfo(taskId)
                mainToolBar.setTitle(getString(R.string.homework_exam))
                iv_service.visibility = View.VISIBLE
                iv_share.visibility = View.VISIBLE
            }
        }
    }

    private var isDown: Boolean = false
    private fun initListener() {

        RxView.clicks(iv_service).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            startQQChat(Constant.QQ)
            mPresenter.feedHomework(mData?.task_id)
        }

        iv_exam.setOnTouchListener { v, ev ->

            when (ev!!.action) {
                MotionEvent.ACTION_UP -> {
                    isDown = !isDown
                    val startX = ev.x
                    val startY = ev.y
                    drawBitmap(mBitmap, mData!!, startX, startY)
                }

            }

            false
        }

        RxView.clicks(iv_share).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, AnalysisShareActivity::class.java)
            intent.putExtra("data", mData)
            intent.putParcelableArrayListExtra("answers", mData?.answer)
            startActivity(intent)
        }

        RxView.clicks(iv_exam).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

        }

    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_exam_detail
    }

    private fun startQQChat(qq: String) {
        try {
            val url3521 = "mqqwpa://im/chat?chat_type=wpa&uin=$qq"
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url3521)))
        } catch (e: Exception) {
            LogUtil.msg(e.message)
            ToastUtils.showCenterToast(this, "你的手机还未安装qq,请先安装")
        }

    }

    override fun showWallDetailInfos(t: List<HomeworkInfo>) {

    }


    @SuppressLint("SetTextI18n")
    override fun showHomeworkInfo(data: HomeworkDetailInfo) {
        mData = data

        val score = BigDecimal(data.score)
                .setScale(0, BigDecimal.ROUND_HALF_UP).intValueExact()

        val accuracyRate = String.format(getString(R.string.accuracy_rate), score)
        tv_accuracy.text = "$accuracyRate%"
        tv_correct.text = String.format(getString(R.string.correct_num), data.right_num)
        tv_error.text = String.format(getString(R.string.error_num), data.error_num)
        iv_score.setImageResource(UiUtils.getScorePic(score = score))
        tv_remark.text = data.remark
        drawBitmap(data.image, data)

    }

    private var mBitmap: Bitmap? = null
    private var mData: HomeworkDetailInfo? = null

    private fun drawBitmap(path: String?, data: HomeworkDetailInfo) {

        Glide.with(this).load(path).asBitmap().fitCenter().into(object : BitmapImageViewTarget(iv_exam) {
            override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                super.onResourceReady(resource, glideAnimation)
                drawBitmap(resource, data)
                mBitmap = resource
            }
        })
    }

    private fun drawBitmap(resource: Bitmap?, data: HomeworkDetailInfo, pressX: Float = 0f, pressY: Float = 0f) {
        val bitmapHeight = resource!!.height
        val bitmapWidth = resource.width

        val bitmapBg = Bitmap.createBitmap(bitmapWidth, bitmapHeight, resource.config)
        val canvas = Canvas(bitmapBg)
        val matrix = Matrix()
        val picWidth = data.width
        val picHeight = data.height

        LogUtil.msg("bitmapWidth: $bitmapWidth  bitmapHeight: $bitmapHeight  picWidth: $picWidth  picHeight:$picHeight  iv width: ${iv_exam.width}  height:${iv_exam.height} screnWidth:${ScreenUtil.getWidth(this)}  screnHeight:${ScreenUtil.getHeight(this)}")
        canvas.drawBitmap(resource, matrix, null)

//        val scale = if (bitmapWidth < bitmapHeight) {
//            if (bitmapWidth / (picWidth * 1f) > bitmapHeight / (picHeight * 1f)) {
//                bitmapHeight / (picHeight * 1f)
//            } else {
//                bitmapWidth / (picWidth * 1f)
//            }
//        } else {
//            bitmapHeight / (picHeight * 1f)
//        }

        val scale = if (bitmapWidth / (picWidth * 1f) > bitmapHeight / (picHeight * 1f)) {
            bitmapHeight / (picHeight * 1f)
        } else {
            bitmapWidth / (picWidth * 1f)
        }

        LogUtil.msg("scale: $scale")

        val rightBm = BitmapFactory.decodeResource(resources, R.mipmap.right)
        val errorBm = BitmapFactory.decodeResource(resources, R.mipmap.error)
        val queryBm = BitmapFactory.decodeResource(resources, R.mipmap.query)

//        LogUtil.msg("queryBm  ${queryBm.width}  ${queryBm.height}")

        val answers = data.answer

        val paint = TextPaint()
        paint.style = Paint.Style.FILL
        paint.color = Color.parseColor("#58a1ed")
        paint.isAntiAlias = true
        paint.isDither = true
        paint.textSize = 30.0f

        if (answers != null)
            for ((i, value) in answers.withIndex()) {

                when (value.tagType) {
                    0 -> {
                        canvas.drawBitmap(rightBm, value.x * scale, value.y * scale, null)
                    }
                    1 -> {
                        canvas.drawBitmap(errorBm, value.x * scale, value.y * scale, null)
                    }
                    -1 -> {
                        canvas.drawBitmap(queryBm, value.x * scale, value.y * scale, null)
                        drawErrorAnalysis(value, scale, queryBm, canvas, paint, pressX, pressY, bitmapBg)
                    }
                }
            }

        iv_exam.setImageBitmap(bitmapBg)
        stateView.hide()

    }

    private fun drawErrorAnalysis(value: AnswerInfo, scale: Float, queryBm: Bitmap, canvas: Canvas, paint: TextPaint, pressX: Float, pressY: Float, bitmap: Bitmap) {
        rectF = RectF(value.x * scale, value.y * scale, value.x * scale + queryBm.width, value.y * scale + queryBm.height)

        if (rectF!!.contains(pressX, pressY) && !value.isPress) {

            canvas.save()
            val text = value.msg

            val sl = StaticLayout(text, paint, 491, Layout.Alignment.ALIGN_NORMAL, 1.0f, 2.0f, false)

            val textHeight = paint.fontMetrics.descent - paint.fontMetrics.ascent

            val roundHeight = textHeight * (sl.lineCount) + 25

            val startX = value.x * scale
            val startY = value.y * scale
            val bitWidth = bitmap.width
            val bitHeight = bitmap.height
            val roundRectF = RectF()
            val translateX: Float
            val translateY: Float
            if (startX + 491 > bitWidth) {
                var roundx = startX - 500
                var remanentX = 0
                if (roundx < 0) {
                    remanentX = Math.abs(roundx).toInt()
                    roundx = 0f
                }
                if (startY + roundHeight > bitHeight) {//下边界越界

                    roundRectF.set(roundx, startY - roundHeight, startX + remanentX, startY)
                    translateX = roundx + 15
                    translateY = startY - roundHeight + 10
                } else {//下边界不越界
                    roundRectF.set(roundx, startY + queryBm.height, startX + remanentX, startY + queryBm.height + roundHeight)
                    translateX = roundx + 15
                    translateY = startY + queryBm.height + 10
                }
            } else {
                if (startY + roundHeight > bitHeight) {//下边界越界
                    roundRectF.set(startX, startY - roundHeight, startX + 500, startY)
                    translateX = startX + 15
                    translateY = startY - roundHeight + 10
                } else {
                    roundRectF.set(startX, startY + queryBm.height, startX + 500, startY + queryBm.height + roundHeight)
                    translateX = startX + 15
                    translateY = startY + queryBm.height + 10
                }

            }
//            LogUtil.msg("pointx $startX  bitWidth : $bitWidth")
//            LogUtil.msg("textHeight: $textHeight")

            canvas.drawRoundRect(roundRectF, 10f, 10f, paint)

            paint.color = ContextCompat.getColor(this, R.color.white)

            canvas.translate(translateX, translateY)
            sl.draw(canvas)
            canvas.restore()

        }
        value.isPress = !value.isPress
//        else {
//            val intent = Intent(this, ExamDetailPreviewActivity::class.java)
//            intent.putExtra("data", mData)
//            intent.putParcelableArrayListExtra("answers", mData!!.answer)
//            startActivity(intent)
//        }


    }


    override fun showLoading() {
        stateView.showLoading(contentView)
    }

    override fun showNoData() {
        stateView.showNoData(contentView)
    }

    override fun showNoNet() {
        stateView.showNoNet(contentView, {
            getData()
        })
    }

    override fun hide() {
        stateView.hide()
    }
}