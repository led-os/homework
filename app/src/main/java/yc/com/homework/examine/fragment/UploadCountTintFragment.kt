package yc.com.homework.examine.fragment

import android.content.Intent
import com.jakewharton.rxbinding.view.RxView
import com.vondear.rxtools.RxDeviceTool
import kotlinx.android.synthetic.main.fragment_book_read_unlock.*
import yc.com.base.BaseDialogFragment
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.mine.activity.RechargeActivity
import yc.com.homework.mine.activity.ServiceCardActivity
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/1/24 15:54.
 */
class UploadCountTintFragment : BaseDialogFragment<BasePresenter<BaseEngine, BaseView>>() {
    override fun init() {

        RxView.clicks(iv_close).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //关闭对话框
            dismiss()
        }

        RxView.clicks(tv_unlock_read).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //todo 暂时换未实现
            val intent = Intent(activity, ServiceCardActivity::class.java)
            activity?.startActivity(intent)
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
        return R.layout.fragment_upload_count_tint
    }
}