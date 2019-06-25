package yc.com.homework.read.fragment

import android.content.Context
import android.graphics.*
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import android.view.MotionEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.kk.utils.LogUtil
import com.kk.utils.ScreenUtil
import com.vondear.rxtools.RxDeviceTool
import kotlinx.android.synthetic.main.fragment_book_detail_info.*
import yc.com.base.BaseActivity
import yc.com.base.BaseFragment
import yc.com.base.ObservableManager
import yc.com.homework.R
import yc.com.homework.read.activity.BookDetailInfoActivity
import yc.com.homework.read.contract.BookDetailInfoContract
import yc.com.homework.read.domain.bean.BookDetailInfo
import yc.com.homework.read.domain.bean.BookDetailTrackInfo
import yc.com.homework.read.domain.bean.PlayData
import yc.com.homework.read.listener.OnAVManagerListener
import yc.com.homework.read.presenter.BookDetailInfoPresenter
import yc.com.homework.read.utils.DataManager
import yc.com.homework.read.utils.OnUIChangeListener
import yc.com.homework.read.utils.PpAudioManager
import java.net.URLEncoder
import java.util.*

/**
 *
 * Created by wanglin  on 2019/1/14 15:35.
 */
class BookDetailInfoFragment : BaseFragment<BookDetailInfoPresenter>(), BookDetailInfoContract.View, Observer, OnUIChangeListener {


    private var bookId = 0
    private var mPage = 0
    private var singleTotal = 1

    private var manager: OnAVManagerListener? = null

    private var detailInfo: BookDetailTrackInfo? = null
    private var mp3_file: String? = null

    private var mBitmap: Bitmap? = null
    private var mDatas: List<BookDetailTrackInfo>? = null

    private val TAG = "tag"


    override fun getLayoutId(): Int {
        return R.layout.fragment_book_detail_info
    }

    override fun init() {

        if (arguments != null) {
            bookId = arguments!!.getInt("bookId")
            singleTotal = arguments!!.getInt("singleTotal")
        }
        mPresenter = BookDetailInfoPresenter(activity as BaseActivity<*>, this)
        manager = PpAudioManager(activity, this)
        initListener()
        ObservableManager.get().addMyObserver(this)

//        Log.e(TAG, "init ${getPage()}")
    }


    fun initListener() {
        iv_detail.setOnTouchListener { v, ev ->
            when (ev.action) {
                MotionEvent.ACTION_DOWN -> {
                    val startX = ev.x
                    val startY = ev.y
//                    LogUtil.msg("x:startX  $startX  y:startY  $startY")
                    drawRectLine(mBitmap, mDatas, true, startX, startY)
                }
            }

            false
        }


    }

    private var last_end: Float? = 0f
    private var first_start: Float? = 0f

    override fun showBookDetailInfo(bookDetailInfo: BookDetailInfo?) {
        mDatas = bookDetailInfo?.track_info
        mDatas?.let {
            mp3_file = it[0].mp3_file
            last_end = if (mDatas?.size!! > 1)
                it[mDatas?.size!! - 1].track_auend
            else
                it[0].track_auend

            first_start = it[0].track_austart
        }


        Glide.with(this).load(bookDetailInfo?.page_url).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true)
                .dontAnimate().into(object : BitmapImageViewTarget(iv_detail) {
                    override fun onResourceReady(resource: Bitmap?, glideAnimation: GlideAnimation<in Bitmap>?) {
                        super.onResourceReady(resource, glideAnimation)
                        drawRectLine(resource, mDatas, false)
                        mBitmap = resource

                        val playData = PlayData()
                        playData.start = first_start
                        playData.end = last_end
                        playData.mp3_file = mp3_file
                        playData.bitmap = resource
                        playData.datas = mDatas
                        DataManager.instance.addDatas(getPage(), playData)
                    }
                })
    }


    private fun drawRectLine(resource: Bitmap?, track_infos: List<BookDetailTrackInfo>?, isPress: Boolean, x: Float = 0f, y: Float = 0f, track_start: Float = 0f) {

//        LogUtil.msg("track_infos : ${track_infos?.size}  page : ${getPage()}")
        resource?.let {
            val bitmapWidth = resource.width
            val bitmapHeight = resource.height

            val bitmapBg = Bitmap.createBitmap(bitmapWidth, bitmapHeight, resource.config)
            val canvas = Canvas(bitmapBg)
            val matrix = Matrix()

            canvas.drawBitmap(resource, matrix, null)
            val textPaint = TextPaint()
            textPaint.style = Paint.Style.STROKE
            textPaint.color = Color.parseColor("#F14343")
            textPaint.isAntiAlias = true
            textPaint.isDither = true
            textPaint.strokeWidth = 2f

            var screenWidth = 0
            var screenHeight = 0

            activity?.let {
                if (!activity!!.isDestroyed) {
                    screenWidth = ScreenUtil.getWidth(activity)
                    screenHeight = ScreenUtil.getHeight(activity)
                }
            }




            track_infos?.let {

                track_infos.forEach {
                    val rectF1 = RectF()
                    val rectF2 = RectF()
                    it.track_left?.let {
                        rectF1.left = it * bitmapWidth
                        rectF2.left = it * screenWidth
                    }
                    it.track_top?.let {
                        rectF1.top = it * bitmapHeight
                        rectF2.top = it * screenHeight
                    }
                    it.track_right?.let {
                        rectF1.right = it * bitmapWidth
                        rectF2.right = it * screenWidth

                    }
                    it.track_bottom?.let {
                        rectF1.bottom = it * bitmapHeight
                        rectF2.bottom = it * screenHeight
                    }

                    canvas.drawRect(rectF1, textPaint)

                    if (isPress && rectF2.contains(x, y)) {
                        detailInfo = it

                        playAndDraw(textPaint, canvas, rectF1)
                        if (isReadThrough) {
                            playMp3(it.mp3_file, it.track_austart!! * 1000, last_end!! * 1000, last_end!! * 1000)
                        } else {
                            playMp3(it.mp3_file, it.track_austart!! * 1000, it.track_auend!! * 1000, 0f)
                        }
                    } else {
                        if (track_start > 0) {
                            if (it.track_austart!! <= track_start && it.track_auend!! >= track_start) {
                                playAndDraw(textPaint, canvas, rectF1)
                            }
                        }
                    }

                }

                if (isPlay && isReadThrough) {
//                    LogUtil.msg("isPlay: $isPlay isReadThrough  $isReadThrough ")
                    playMp3(it[0].mp3_file, it[0].track_austart!! * 1000, it[it.size - 1].track_auend!! * 1000, it[it.size - 1].track_auend!! * 1000)
                    isPlay = false
                }

            }
            if (null != iv_detail)
                iv_detail.setImageBitmap(bitmapBg)

        }


    }

    private var isPlay = false

    private fun playAndDraw(textPaint: TextPaint, canvas: Canvas, rectF1: RectF) {

        textPaint.style = Paint.Style.FILL
        textPaint.color = Color.parseColor("#66FF5D5D")
        canvas.drawRect(rectF1, textPaint)

        textPaint.style = Paint.Style.STROKE
        textPaint.color = Color.parseColor("#F14343")
        canvas.drawRect(rectF1, textPaint)

    }


    private fun playMp3(fileUrl: String?, startTime: Float, endTime: Float, lastEndTime: Float) {
        val result = replaceChar(fileUrl)

        manager?.playMusic(result, startTime, endTime, lastEndTime)

    }

    private fun replaceChar(url: String?): String? {
        val index = url?.lastIndexOf("/")

        index?.let {
            val preUrl = url.substring(0, it + 1)
            var afterUrl = url.substring(it + 1)
            afterUrl = URLEncoder.encode(afterUrl, "UTF-8")
            val str = afterUrl.replace("+", "%20")
//            LogUtil.msg("preUrl: $preUrl  afterUrl $afterUrl  result :$result")
            return "$preUrl$str"
        }
        return url

    }


    override fun fetchData() {
        super.fetchData()
        mPresenter.getBookDetailInfo("$bookId", getPage())

//        Log.e(TAG, "       fetchData  page  ${getPage()}")

    }

    fun setCurrentPage(page: Int) {
        this.mPage = page

        val data = DataManager.instance.getValue(page)
        data?.let {
            if (isPlay && isReadThrough && isVisibleToUser) {
                playMp3(data.mp3_file, data.start!! * 1000, data.end!! * 1000, data.end!! * 1000)
            }
//            Log.e(TAG, "  page  $page  list  ${data.mp3_file}  start  ${data.start}  end ${data.end}")
        }

//        Log.e(TAG, "       setCurrentPage  page  ${getPage()}")

    }

    private fun getPage(): Int {
        return mPage
    }


    override fun showNoData() {
        if (null != stateView)
            stateView.showNoData(iv_detail)
    }

    override fun showNoNet() {
        if (null != stateView)
            stateView.showNoNet(iv_detail) {
                mPresenter.getBookDetailInfo("$bookId", getPage())
            }
    }

    override fun hide() {
        if (null != stateView)
            stateView.hide()
    }

    override fun showLoading() {
        if (null != stateView)
            stateView.showLoading(iv_detail)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        manager = null
//        Log.e(TAG, "  onDestroyView  ${getPage()}")
    }


    private var isReadThrough = false//是否连读

    override fun update(o: Observable?, arg: Any?) {
        if (arg != null) {
            when (arg) {
                is Boolean -> {
                    isReadThrough = arg
                    val data = DataManager.instance.getValue(getPage())
                    if (arg && isVisibleToUser) {
//                        LogUtil.msg("mp3_file:   $mp3_file  arg:  $arg")
                        if (data != null) {
                            playMp3(data.mp3_file, data.start!! * 1000, data.end!! * 1000, data.end!! * 1000)
//                            Log.e(TAG, "  page  ${getPage()}  list  ${data.mp3_file}")
                        } else {
                            playMp3(mp3_file, first_start!! * 1000, last_end!! * 1000, last_end!! * 1000)
                        }
                    }
                }
                is String -> {
                    when {
                        TextUtils.equals(arg, "stop") -> { //停止播放
                            manager?.stopMusic()
                            isReadThrough = false
                        }
                        TextUtils.equals(arg, "pause") -> //暂停播放
                            manager?.pauseMusic()
                        TextUtils.equals(arg, "start") -> //继续播放
                            manager?.playMusic()
                        TextUtils.equals(arg, "next") -> {//下一页播放
                            isPlay = true
                            LogUtil.msg("TAG  next")
                        }
                        TextUtils.equals(arg, "play_stop") -> {
                            isReadThrough = false
                            LogUtil.msg("TAG  play_stop")
                        }

                    }
                }
            }

        }
    }

    private var bookReadBufferFragment: BookReadBufferFragment? = null

    override fun onDownLoadChangeListener(max: Int, progress: Int) {

        activity?.runOnUiThread {
            bookReadBufferFragment?.setProgress(progress)
            if (progress == 100) {
                bookReadBufferFragment?.dismiss()

                if (isReadThrough) {
                    playMp3(mp3_file, first_start!! * 1000, last_end!! * 1000, last_end!! * 1000)
                } else {
                    detailInfo?.let {
                        playMp3(detailInfo?.mp3_file, detailInfo?.track_austart!! * 1000, detailInfo?.track_auend!! * 1000, 0f)
                    }
                }

            }
        }


    }

    private var parent: BookDetailInfoActivity? = null
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BookDetailInfoActivity) {
            parent = context
        }
    }

    override fun show() {
        bookReadBufferFragment = BookReadBufferFragment()
        bookReadBufferFragment?.show(childFragmentManager, "")
    }

    override fun onComplete() {
        parent?.hidePlayState()
    }


    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get().deleteMyObserver(this)
        manager?.destroy()

    }


    override fun onPlayChangeListener(duration: Int) {
//        LogUtil.msg("duration　: ${duration / 1000f}")
//        var result = duration
//        if (last_end!! < duration) {
//            result -= 100
//        }
        //更新方框图
        val trackStart = duration / 1000f
//        val data = DataManager.instance.getValue(getPage())
//        if (data == null)
        drawRectLine(mBitmap, mDatas, false, 0f, 0f, trackStart)
//        else {
//            drawRectLine(data.bitmap, data.datas, false, 0f, 0f, trackStart)
//        }
    }


}