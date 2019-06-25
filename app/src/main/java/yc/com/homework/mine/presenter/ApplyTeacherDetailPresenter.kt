package yc.com.homework.mine.presenter

import android.content.Context
import android.text.TextUtils
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.blankj.utilcode.util.RegexUtils
import yc.com.homework.base.config.Constant
import yc.com.base.ObservableManager
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.mine.contract.ApplyTeacherDetailContract
import yc.com.homework.mine.domain.bean.ApplyRuleInfo
import yc.com.homework.mine.domain.bean.TeacherApplyState
import yc.com.homework.mine.domain.bean.UploadResultInfo
import yc.com.homework.mine.domain.engine.ApplyTeacherDetailEngine

/**
 *
 * Created by wanglin  on 2018/11/26 08:50.
 */
class ApplyTeacherDetailPresenter(context: Context, view: ApplyTeacherDetailContract.View) : BasePresenter<ApplyTeacherDetailEngine, ApplyTeacherDetailContract.View>(context, view), ApplyTeacherDetailContract.Presenter {



    init {
        mEngine = ApplyTeacherDetailEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
        getApplyTeacherRuleInfo()
    }

    override fun getApplyTeacherRuleInfo() {
        mView.showLoading()
        val subscription = mEngine.getApplyTeacherRuleInfo().subscribe(object : Subscriber<ResultInfo<ApplyRuleInfo>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<ApplyRuleInfo>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.hide()
                        mView.showTeacherRuleInfo(t.data)
                    } else {
                        mView.showNoData()
                    }
                } else {
                    mView.showNoNet()
                }
            }
        })

        mSubscriptions.add(subscription)
    }

    override fun applyTeacher(name: String?, sex: Int, mobile: String?, subject_id: Int, job: Int, teacher_cert: String?, diploma: String?) {
        if (TextUtils.isEmpty(name)) {
            ToastUtils.showCenterToast(mContext, "请填写姓名")
            return
        }
        if (TextUtils.isEmpty(mobile)) {
            ToastUtils.showCenterToast(mContext, "请填写手机号")
            return
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            ToastUtils.showCenterToast(mContext, "填写的手机号码不正确")
            return
        }
        if (subject_id == 0) {
            ToastUtils.showCenterToast(mContext, "请选择学科")
            return
        }
        if (TextUtils.isEmpty(teacher_cert)) {
            ToastUtils.showCenterToast(mContext, "请上传教师资格证")
            return
        }
        if (TextUtils.isEmpty(diploma)) {
            ToastUtils.showCenterToast(mContext, "请上传学位证书")
            return
        }

        mView.showLoadingDialog("正在上传资料，请稍候...")


        val subscription = mEngine.applyTeacher(name, sex, mobile, subject_id, job, teacher_cert, diploma).subscribe(object : Subscriber<ResultInfo<String>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<String>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.finish()
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

    override fun uploadPic(file: String) {
        mView.showLoadingDialog("正在上传，请稍候...")
        val subscription = mEngine.uploadPic(file).subscribe(object : Subscriber<ResultInfo<UploadResultInfo>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<UploadResultInfo>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showUploadResult(t.data)
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

    override fun getApplyState() {
        val subscription = mEngine.getApplyState().subscribe(object : Subscriber<ResultInfo<TeacherApplyState>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: ResultInfo<TeacherApplyState>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.showState(t.data)
                    }
                }
            }
        })
        mSubscriptions.add(subscription)
    }
}