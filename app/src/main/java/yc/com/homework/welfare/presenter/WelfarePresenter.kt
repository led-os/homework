package yc.com.homework.welfare.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.welfare.contract.WelfareContract
import yc.com.homework.welfare.domain.bean.WelfareInfoWrapper
import yc.com.homework.welfare.domain.engine.WelfareEngine

/**
 *
 * Created by wanglin  on 2019/3/12 15:53.
 */
class WelfarePresenter(context: Context, view: WelfareContract.View) : BasePresenter<WelfareEngine, WelfareContract.View>(context, view), WelfareContract.Presenter {
    init {
        mEngine = WelfareEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
        getWelfareInfo()
    }

    private fun getWelfareInfo() {
        val subscription = mEngine.getWelfareInfo().subscribe(object : Subscriber<ResultInfo<WelfareInfoWrapper>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<WelfareInfoWrapper>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showWelfareInfo(t.data.h5page)
                }
            }
        })

        mSubscriptions.add(subscription)
    }
}