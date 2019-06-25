package yc.com.homework.index.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.ObservableManager
import yc.com.homework.base.utils.EngineUtils
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.index.contract.NewMoreContract
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.domain.bean.ReadCountInfo

/**
 *
 * Created by wanglin  on 2019/3/2 10:35.
 */
class NewMorePresenter(context: Context, view: NewMoreContract.View) : BasePresenter<BaseEngine, NewMoreContract.View>(context, view), NewMoreContract.Presenter {
    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {

    }


    fun getMoreNews(page: Int, pageSize: Int, isRefresh: Boolean) {
        if (page == 1 && !isRefresh) mView.showLoading()
        val subscription = EngineUtils.getNewsInfos(mContext, page, pageSize).subscribe(object : Subscriber<ResultInfo<ArrayList<NewsInfo>>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (page == 1 && !isRefresh)
                    mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<ArrayList<NewsInfo>>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.hide()
                        mView.showNewsInfos(t.data)
                    } else {
                        if (page == 1 && !isRefresh) {
                            mView.showNoData()
                        }
                    }
                } else {
                    if (page == 1 && !isRefresh) {
                        mView.showNoNet()
                    }
                }
            }
        })
        mSubscriptions.add(subscription)
    }

    fun statisticsCount(id: String?) {
        val subscription = EngineUtils.statisticsCount(mContext, id).subscribe(object : Subscriber<ResultInfo<ReadCountInfo>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<ReadCountInfo>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK) {
//                    ToastUtils.showCenterToast(mContext, "$id 统计成功")
//                    ObservableManager.get().notifyMyObserver("statistics_success")
                }
            }
        })
        mSubscriptions.add(subscription)
    }
}