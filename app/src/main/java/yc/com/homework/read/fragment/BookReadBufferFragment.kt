package yc.com.homework.read.fragment


import com.vondear.rxtools.RxDeviceTool
import kotlinx.android.synthetic.main.fragment_book_read_buffer.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.base.utils.DeviceUtils


/**
 *
 * Created by wanglin  on 2019/1/24 15:54.
 */
class BookReadBufferFragment : BaseDialogFragment<BasePresenter<BaseEngine, BaseView>>() {

    private val minHeight = 230
    override fun init() {
//        dialog.setCancelable(true)
//        dialog.setCanceledOnTouchOutside(true)

    }

    override fun getWidth(): Float {
        return 0.8f
    }

    override fun getAnimationId(): Int {
        return R.style.share_anim
    }

    override fun getHeight(): Int {

        val isBarAvaibable = DeviceUtils.isNavigationBarAvailable()
        val navibarHeight = DeviceUtils.getNavibarHeight(activity as  BaseActivity<*>)
        var screenHeight = RxDeviceTool.getScreenHeight(activity)
        if (isBarAvaibable) {
            screenHeight -= navibarHeight
        }
        var height = screenHeight * 3 / 20

//        LogUtil.msg("height: $height")

        if (height < minHeight) {
            height = minHeight
        }

        return height
    }

    private var mProgress: Int = 0

    override fun getLayoutId(): Int {
        return R.layout.fragment_book_read_buffer
    }

    fun setProgress(progress: Int) {
        this.mProgress = progress
        progressBar?.max = 100
        progressBar?.progress = mProgress
        tv_progress?.text = "$mProgress%"
        tv_progress_total.text = "$mProgress/100"
//        LogUtil.msg("progress：  $progress")

    }


}