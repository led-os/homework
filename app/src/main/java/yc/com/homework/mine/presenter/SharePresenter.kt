package yc.com.homework.mine.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.contract.ShareContract
import yc.com.homework.mine.domain.engine.ShareEngine

/**
 * Created by wanglin  on 2018/3/22 08:59.
 */

class SharePresenter(context: Context, view: ShareContract.View) : BasePresenter<ShareEngine, ShareContract.View>(context, view), ShareContract.Presenter {
    init {
        mEngine = ShareEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
        //        getShareInfo();

    }


    fun share(task_id: String?) {
        val subscription = mEngine.share(task_id).subscribe(object : Subscriber<ResultInfo<String>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(stringResultInfo: ResultInfo<String>?) {
                if (stringResultInfo != null && stringResultInfo.code == HttpConfig.STATUS_OK) {
                    UserInfoHelper.getNewUserInfo(mContext)
                    mView.showSuccess()
                }
            }
        })
        mSubscriptions.add(subscription)
    }


}
