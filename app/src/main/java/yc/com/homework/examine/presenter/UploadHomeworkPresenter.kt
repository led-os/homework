package yc.com.homework.examine.presenter

import android.content.Context
import android.content.Intent
import android.text.TextUtils
import android.view.TextureView
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.base.config.Constant
import yc.com.base.ObservableManager
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.examine.contract.UploadHomeworkContract
import yc.com.homework.examine.domain.bean.UploadInfo
import yc.com.homework.examine.domain.engine.HomeworkEngine
import yc.com.homework.mine.activity.RechargeActivity
import java.io.File

/**
 *
 * Created by wanglin  on 2018/12/4 19:23.
 */
class UploadHomeworkPresenter(context: Context, view: UploadHomeworkContract.View) : BasePresenter<HomeworkEngine, UploadHomeworkContract.View>(context, view), UploadHomeworkContract.Presenter {


    init {
        mEngine = HomeworkEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
    }

    override fun uploadHomework(file: File?, subject_id: String?, type: String?, grade_id: String?, task_id: String?) {
        if (file == null) {
            ToastUtils.showCenterToast(mContext, "上传图片不能为空")
            return
        }
        if (TextUtils.isEmpty(subject_id)) {
            ToastUtils.showCenterToast(mContext, "请先选择学科")
            return
        }
        mView.showLoadingDialog("正在上传作业，请稍候...")

        val subscription = mEngine.uploadHomework(file, subject_id, type, grade_id, task_id).subscribe(object : Subscriber<ResultInfo<UploadInfo>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<UploadInfo>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        //上传成功
                        ObservableManager.get().notifyMyObserver(Constant.UPLOAD_SUCCESS)
                        UserInfoHelper.getNewUserInfo(mContext)
                        ToastUtils.showCenterToast(mContext, t.message)
                        mView.finish()
                    } else {
                        if (t.message.contains("金币不足") || t.message.contains("余额不足") || t.message.contains("没有金币")) {
                            mView.showCountNotEnough()
                        }
                        ToastUtils.showCenterToast(mContext, t.message)
                    }
                }
            }
        })
        mSubscriptions.add(subscription)
    }
}