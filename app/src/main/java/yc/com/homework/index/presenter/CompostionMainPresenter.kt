package yc.com.homework.index.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.base.CommonInfoHelper
import yc.com.homework.base.config.SpConstant
import yc.com.homework.index.contract.CompostionContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.VersionInfo
import yc.com.homework.index.domain.engine.CompostionMainEngine

/**
 *
 * Created by wanglin  on 2019/3/20 13:54.
 */
class CompostionMainPresenter(context: Context, view: CompostionContract.View) : BasePresenter<CompostionMainEngine, CompostionContract.View>(context, view), CompostionContract.Presenter {

    init {
        mEngine = CompostionMainEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
//        getCompostionInfos()
        getCompositionConditions()

    }

    fun getCompostionInfos() {
        val subscription = mEngine.getCompostionInfos().subscribe(object : Subscriber<ResultInfo<ArrayList<CompositionInfo>>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<ArrayList<CompositionInfo>>?) {
                if (t != null) {
                    mView.showCompostionInfos(t.data)
                }
            }
        })
        mSubscriptions.add(subscription)
    }

    fun getCompostionIndexInfos(page: Int, pageSize: Int) {
        val subscription = mEngine.getCompostionIndexInfos(page, pageSize).subscribe(object : Subscriber<ResultInfo<ArrayList<CompositionInfo>>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<ArrayList<CompositionInfo>>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showCompostionInfos(t.data)
                }
            }
        })
        mSubscriptions.add(subscription)
    }

    fun getCompositionConditions() {
        val subscription = mEngine.getCompositionConditions().subscribe(object : Subscriber<ResultInfo<VersionInfo>>() {
            override fun onNext(t: ResultInfo<VersionInfo>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    val versionInfo = t.data
                    CommonInfoHelper.setO(mContext, versionInfo, SpConstant.composition_version)
                }
            }

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }
        })
        mSubscriptions.add(subscription)
    }
}