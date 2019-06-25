package yc.com.homework.read.fragment

import com.jakewharton.rxbinding.view.RxView
import com.vondear.rxtools.RxDeviceTool
import kotlinx.android.synthetic.main.fragment_book_read_unlock.*
import yc.com.base.BaseDialogFragment
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.mine.fragment.ShareFragment
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/1/24 15:54.
 */
class BookReadUnlockFragment : BaseDialogFragment<BasePresenter<BaseEngine, BaseView>>() {


    private var shareFragment: ShareFragment? = null
    override fun init() {

        if (arguments != null) {
            val content = arguments!!.getString("content")
            val title = arguments!!.getString("title")
            tv_content.text = content
            tv_unlock_read.text = title
        }


        RxView.clicks(iv_close).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //关闭对话框
            dismiss()
        }

        RxView.clicks(tv_unlock_read).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //todo 暂时换未实现
            if (shareFragment == null)
                shareFragment = ShareFragment()
//
//            val shareInfo = ShareInfo()
//
//            val result = ImageUtils.getShareBitmap(activity)
//            shareInfo.bitmap = result
//            shareFragment?.setShareInfo(shareInfo)
            if (!shareFragment!!.isVisible) {
                val ft = activity?.supportFragmentManager?.beginTransaction()
                ft?.add(shareFragment!!, "")
                ft?.commitAllowingStateLoss()
//                            shareFragment.show(supportFragmentManager, "")
            }
            dismiss()
        }

    }

    override fun getWidth(): Float {
        return 0.8f
    }

    override fun getAnimationId(): Int {
        return R.style.share_anim
    }

    override fun getHeight(): Int {
        return RxDeviceTool.getScreenHeight(activity) / 2
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_book_read_unlock
    }
}