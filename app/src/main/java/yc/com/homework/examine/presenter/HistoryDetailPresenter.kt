package yc.com.homework.examine.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.base.ObservableManager
import yc.com.homework.base.config.Constant
import yc.com.homework.examine.contract.HistoryDetailContract
import yc.com.homework.examine.domain.engine.HistoryDetailEngine
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo

/**
 *
 * Created by wanglin  on 2018/11/27 15:05.
 */
class HistoryDetailPresenter(context: Context, view: HistoryDetailContract.View) : BasePresenter<HistoryDetailEngine, HistoryDetailContract.View>(context, view), HistoryDetailContract.Presenter {


    init {
        mEngine = HistoryDetailEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
    }

    override fun getHistoryDetailInfos(status: Int?, page: Int, page_size: Int, isRefresh: Boolean) {
        if (page == 1 && !isRefresh)
            mView.showLoading()
        val subscription = mEngine.getHistoryDetailInfos(status, page, page_size).subscribe(object : Subscriber<ResultInfo<ArrayList<HomeworkDetailInfo>>>() {

            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                if (page == 1)
                    mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<ArrayList<HomeworkDetailInfo>>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.hide()
                        mView.showHistoryDetailInfos(t.data)
                    } else {
                        if (page == 1)
                            mView.showNoData()
                    }
                } else {
                    if (page == 1)
                        mView.showNoNet()
                }

            }
        })

        mSubscriptions.add(subscription)
    }

    override fun setReadState(task_id: String?) {
        val subscription = mEngine.setReadState(task_id).subscribe(object : Subscriber<ResultInfo<String>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<String>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK) {
                    ObservableManager.get().notifyMyObserver(Constant.EXAM_FINISH)
                }
            }
        })
        mSubscriptions.add(subscription)
    }
}