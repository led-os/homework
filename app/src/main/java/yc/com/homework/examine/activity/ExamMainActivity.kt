package yc.com.homework.examine.activity

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation

import com.jakewharton.rxbinding.view.RxView

import java.util.concurrent.TimeUnit

import com.app.hubert.guide.NewbieGuide
import com.app.hubert.guide.core.Controller
import com.app.hubert.guide.listener.OnGuideChangedListener
import com.app.hubert.guide.model.GuidePage
import com.app.hubert.guide.model.HighLight
import com.umeng.analytics.MobclickAgent
import com.vondear.rxtools.RxNetTool
import kotlinx.android.synthetic.main.fragment_exam_main_new.*
import yc.com.base.BaseActivity
import yc.com.homework.R
import yc.com.homework.base.config.Constant
import yc.com.base.ObservableManager
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.AnimationUtil
import yc.com.homework.base.utils.Preference
import yc.com.homework.examine.contract.ExamMainContract
import yc.com.homework.examine.domain.bean.UploadStatusInfo
import yc.com.homework.examine.presenter.ExamMainPresenter
import yc.com.homework.wall.fragment.WallMainActivity
import java.util.*

/**
 * Created by wanglin  on 2018/11/23 13:47.
 * 拍照上传作业
 */
class ExamMainActivity : BaseActivity<ExamMainPresenter>(), ExamMainContract.View, Observer {


    private var isFirst by Preference(SpConstant.is_first, true)
    override fun getLayoutId(): Int {
        return R.layout.fragment_exam_main_new
    }

    override fun init() {

        main_toolbar.showNavigationIcon()
        main_toolbar.init(this)
        main_toolbar.setTitle("")
        main_toolbar.setBackgroundCorlor(ContextCompat.getColor(this, R.color.transparant))
        main_toolbar.setRightContainerVisible(false)

        ObservableManager.get()?.addMyObserver(this)

        mPresenter = ExamMainPresenter(this, this)


        RxView.clicks(ll_history).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, HistoryMainActivity::class.java)
            startActivity(intent)
        }
        RxView.clicks(iv_camera_start).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            if (!UserInfoHelper.isGotoLogin(this)) {
                MobclickAgent.onEvent(this, "photoupload_id", "拍照上传")
                val intent = Intent(this, PhotographActivity::class.java)
                startActivity(intent)
            }
        }

        RxView.clicks(ll_excellence).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, WallMainActivity::class.java)
            startActivity(intent)
        }

        showGuide()
    }


    private fun startAnimation() {
        val animation1 = AnimationUtil.startRippleAnimation(1.0f, 1.2f, 1.0f, 1.2f, 1.0f, 0.5f, 1500, Animation.INFINITE)
        iv_camera1.startAnimation(animation1)
        val animation2 = AnimationUtil.startRippleAnimation(1.2f, 1.5f, 1.2f, 1.5f, 0.5f, 1.0f, 1500, Animation.INFINITE)
        iv_camera2.startAnimation(animation2)


    }


    private fun stopAnimation() {
        iv_camera1.clearAnimation()
        iv_camera2.clearAnimation()

    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun onResume() {
        super.onResume()
        if (!isFirst)
            startAnimation()
    }

    override fun onPause() {
        super.onPause()
        stopAnimation()
    }

    private fun showGuide() {

        NewbieGuide.with(this)
                .setLabel("label")
                .alwaysShow(false)
                .addGuidePage(GuidePage.newInstance().addHighLight(iv_camera2, HighLight.Shape.CIRCLE)
                        .setEverywhereCancelable(false).setLayoutRes(R.layout.camera_guide, R.id.iv_next))
                .setOnGuideChangedListener(object : OnGuideChangedListener {
                    override fun onShowed(p0: Controller?) {

                    }

                    override fun onRemoved(p0: Controller?) {
                        isFirst = false
                        startAnimation()
                    }
                }).show()

    }


    override fun update(o: Observable?, arg: Any?) {
        if (arg != null) {
            when (arg) {
                is String -> {
                    if (TextUtils.equals(Constant.UPLOAD_SUCCESS, arg)) {
                        tv_upload_success.visibility = View.VISIBLE
                        mPresenter.loadData(true)
                        finish()
                    } else if (TextUtils.equals(Constant.EXAM_FINISH, arg)) {
                        mPresenter.loadData(true)
                    }
                }
                is UserInfo -> {
                    mPresenter.getUploadStatus(arg.id)
                }
                is Int -> {
                    if (arg == RxNetTool.NETWORK_NO || arg == RxNetTool.NETWORK_UNKNOWN) {//没有网络
                    } else if (arg == RxNetTool.NETWORK_WIFI || arg == RxNetTool.NETWORK_2G || arg == RxNetTool.NETWORK_3G || arg == RxNetTool.NETWORK_4G) {
                        mPresenter.loadData(true)
                    }
                }

            }
        }
    }


    override fun showUploadStatusInfo(data: UploadStatusInfo?) {
        ll_status.visibility = View.VISIBLE
        tv_be_marked.text = String.format(getString(R.string.be_marked), data?.status1)
        tv_midnight.text = String.format(getString(R.string.midnight), data?.status2)
        setModifyState(data)
        iv_new_icon.visibility = if (data!!.status1 > 0 || data.status2 > 0 || data.status3 > 0) View.VISIBLE else View.GONE

    }


    private fun setModifyState(data: UploadStatusInfo?) {
        tv_modify_success.visibility = if (data?.status3 == 0) View.GONE else View.VISIBLE
        tv_modify_reback.visibility = if (data?.status4 == 0) View.GONE else View.VISIBLE
        tv_modify_success.text = String.format(getString(R.string.modify_success), data?.status3)
        tv_modify_reback.text = String.format(getString(R.string.modify_reback), data?.status4)
    }

    override fun showNoData() {
        tv_upload_success.text = getString(R.string.please_upload_homework)
        ll_status.visibility = View.INVISIBLE
        iv_new_icon.visibility = View.GONE
    }

    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get()?.deleteMyObserver(this)
    }

}
