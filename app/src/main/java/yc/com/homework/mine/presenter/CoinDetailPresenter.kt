package yc.com.homework.mine.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.mine.contract.CoinDetailContract
import yc.com.homework.mine.domain.bean.CoinDetailInfoWrapper
import yc.com.homework.mine.domain.engine.CoinDetailEngine

/**
 *
 * Created by wanglin  on 2018/11/24 09:43.
 */
class CoinDetailPresenter(context: Context, view: CoinDetailContract.View) : BasePresenter<CoinDetailEngine, CoinDetailContract.View>(context, view), CoinDetailContract.Presenter {

    init {
        mEngine = CoinDetailEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return

    }

    override fun getCoinDetailList(page: Int, page_size: Int) {
        if (page == 1)
            mView.showLoading()
        val subscription = mEngine.getCoinDetailList(page, page_size).subscribe(object : Subscriber<ResultInfo<CoinDetailInfoWrapper>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (page == 1) mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<CoinDetailInfoWrapper>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.bill_list != null && t.data.bill_list!!.isNotEmpty()) {
                        mView.hide()
//                        val userInfo = t.data.user_info
                        mView.showCoinDetailInfos(t.data)
                    } else {
                        if (page == 1) mView.showNoData()
                    }
                } else {
                    if (page == 1) mView.showNoNet()
                }


            }
        })

        mSubscriptions.add(subscription)
    }
}