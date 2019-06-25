package yc.com.homework.mine.presenter

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.base.ObservableManager
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.mine.contract.SettingContract
import yc.com.homework.mine.domain.engine.UserInfoEngine
import yc.com.homework.mine.utils.GlideCacheHelper
import yc.com.homework.mine.utils.GlideCircleTransformation

/**
 *
 * Created by wanglin  on 2018/11/27 17:16.
 */
class SettingPresenter(context: Context, view: SettingContract.View) : BasePresenter<UserInfoEngine, SettingContract.View>(context, view), SettingContract.Presenter {


    init {
        mEngine = UserInfoEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
        getCacheSize()
    }

    private fun getCacheSize() {
        mView.showCacheSize(GlideCacheHelper.getInstance(mContext).cacheSize)
    }

    fun clearCache(): Boolean {
        return GlideCacheHelper.getInstance(mContext).clearCache()
    }

    fun logout() {
        try {
            val userInfo = UserInfo()
            userInfo.id = SPUtils.getInstance().getString(SpConstant.guest_id)
            userInfo.type = 1

            UserInfoHelper.saveUserInfo(userInfo)
            mView.showLogout()
            ObservableManager.get()?.notifyMyObserver(userInfo)
        } catch (e: Exception) {
            e.printStackTrace()
            System.exit(0)
        }

    }

    override fun uploadAvator(user_id: String, file: String) {

        mView.showLoadingDialog("正在上传图像,请稍候...")
        val subscription = mEngine.uploadAvator(user_id, file).subscribe(object : Subscriber<ResultInfo<UserInfo>>() {

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<UserInfo>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val userInfo = t.data
                        UserInfoHelper.saveUserInfo(userInfo)

                        ObservableManager.get()!!.notifyMyObserver("face-${userInfo.face!!}")

                    } else {
                        ToastUtils.showCenterToast(mContext, t.message)

                    }
                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR)
                }
            }

        })

        mSubscriptions.add(subscription)
    }


}