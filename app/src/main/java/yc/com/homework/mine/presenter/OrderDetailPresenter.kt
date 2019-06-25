package yc.com.homework.mine.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.mine.contract.OrderDetailContract
import yc.com.homework.mine.domain.bean.OrderDetailInfo
import yc.com.homework.mine.domain.engine.OrderDetailEngine

/**
 *
 * Created by wanglin  on 2019/1/24 11:40.
 */
class OrderDetailPresenter(context: Context, view: OrderDetailContract.View) : BasePresenter<OrderDetailEngine, OrderDetailContract.View>(context, view), OrderDetailContract.Presenter {
    init {
        mEngine = OrderDetailEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
    }

    override fun getOrderDetailList(page: Int, page_size: Int) {
        if (page == 1) {
            mView.showLoading()
        }
        val subscription = mEngine.getOrderDetailList(page, page_size).subscribe(object : Subscriber<ResultInfo<ArrayList<OrderDetailInfo>>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                if (page == 1) {
                    mView.showNoNet()
                }
            }

            override fun onNext(t: ResultInfo<ArrayList<OrderDetailInfo>>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.size > 0) {
                        mView.hide()
                        mView.showOrderDetailList(t.data)
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