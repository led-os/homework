package yc.com.homework.mine.presenter

import android.content.Context
import android.text.TextUtils
import android.view.TextureView
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.base.ObservableManager
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.mine.contract.EditInfoContract
import yc.com.homework.mine.domain.bean.GradeInfo
import yc.com.homework.mine.domain.engine.UserInfoEngine

/**
 *
 * Created by wanglin  on 2018/11/30 13:41.
 */
class EditInfoPresenter(context: Context, view: EditInfoContract.View) : BasePresenter<UserInfoEngine, EditInfoContract.View>(context, view), EditInfoContract.Presenter {


    init {
        mEngine = UserInfoEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {

        if (!isForceUI) return
        getGradeInfo()
    }


    override fun updateInfo(user_id: String, nick_name: String?, face: String?, password: String?, grade_id: String?) {
        if (TextUtils.isEmpty(nick_name) && TextUtils.isEmpty(face) && TextUtils.isEmpty(password) && TextUtils.isEmpty(grade_id)) {
            ToastUtils.showCenterToast(mContext, "修改信息不能为空")
            return
        }

        mView.showLoadingDialog("正在修改信息，请稍候...")

        val subscription = mEngine.updateInfo(user_id, nick_name, face, password, grade_id).subscribe(object : Subscriber<ResultInfo<UserInfo>>() {


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
                        if (userInfo.type == 0) {
                            userInfo.type = UserInfoHelper.getUserInfo().type
                        }
                        if (userInfo.upload_times == 0) {
                            userInfo.upload_times = UserInfoHelper.getUserInfo().upload_times
                        }
                        if (userInfo.vip_start_time?.toInt() == 0) {
                            userInfo.vip_start_time = UserInfoHelper.getUserInfo().vip_start_time
                        }
                        if (userInfo.vip_end_time?.toInt() == 0) {
                            userInfo.vip_end_time = UserInfoHelper.getUserInfo().vip_end_time
                        }
                        if (userInfo.has_vip == 0) {
                            userInfo.has_vip = UserInfoHelper.getUserInfo().has_vip
                        }

                        UserInfoHelper.saveUserInfo(userInfo)
                        ObservableManager.get().notifyMyObserver(userInfo)
                        mView.showSuccess()
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


    private fun getGradeInfo() {
        val subscription = mEngine.getGradeInfo().subscribe(object : Subscriber<List<GradeInfo>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: List<GradeInfo>?) {
                mView.showGradeInfos(t)
            }
        })
        mSubscriptions.add(subscription)
    }

}