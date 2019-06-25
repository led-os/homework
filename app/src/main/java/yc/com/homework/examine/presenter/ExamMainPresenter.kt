package yc.com.homework.examine.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.examine.contract.ExamMainContract
import yc.com.homework.examine.domain.bean.UploadStatusInfo
import yc.com.homework.examine.domain.engine.ExamMainEngine

/**
 *
 * Created by wanglin  on 2018/12/12 16:18.
 */
class ExamMainPresenter(context: Context, view: ExamMainContract.View) : BasePresenter<ExamMainEngine, ExamMainContract.View>(context, view), ExamMainContract.Presenter {


    init {
        mEngine = ExamMainEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
        getUploadStatus(UserInfoHelper.getUid())
    }

    override fun getUploadStatus(user_id: String?) {
        val subscription = mEngine.getUploadStatus(user_id).subscribe(object : Subscriber<ResultInfo<UploadStatusInfo>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.showNoData()
            }

            override fun onNext(t: ResultInfo<UploadStatusInfo>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showUploadStatusInfo(t.data)
                } else {
                    mView.showNoData()
                }

            }
        })
        mSubscriptions.add(subscription)
    }
}