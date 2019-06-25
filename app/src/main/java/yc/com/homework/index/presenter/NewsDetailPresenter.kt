package yc.com.homework.index.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.index.contract.NewsDetailContract
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.domain.bean.NewsInfoWrapper
import yc.com.homework.index.domain.engine.NewsDetailEngine

/**
 *
 * Created by wanglin  on 2019/3/2 11:51.
 */
class NewsDetailPresenter(context: Context, view: NewsDetailContract.View) : BasePresenter<NewsDetailEngine, NewsDetailContract.View>(context, view), NewsDetailContract.Presenter {


    init {
        mEngine = NewsDetailEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
    }

    override fun getNewsDetailInfo(newsId: String) {
        mView.showLoading()
        val subscription = mEngine.getNewsDetailInfo(newsId).subscribe(object : Subscriber<ResultInfo<NewsInfoWrapper>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<NewsInfoWrapper>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.hide()
                        mView.showNewsDetailInfo(t.data.info)
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
}