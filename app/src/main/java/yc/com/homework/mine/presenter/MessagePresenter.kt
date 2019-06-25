package yc.com.homework.mine.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.mine.contract.MessageContract
import yc.com.homework.mine.domain.bean.MessageInfo
import yc.com.homework.mine.domain.engine.MessageEngine

/**
 *
 * Created by wanglin  on 2018/11/23 17:50.
 */
class MessagePresenter(context: Context, view: MessageContract.View) : BasePresenter<MessageEngine, MessageContract.View>(context, view), MessageContract.Presenter {

    init {
        mEngine = MessageEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return

    }

    override fun getMessageInfo(page: Int, pageSize: Int, isRefresh: Boolean) {
        if (page == 1 && !isRefresh)
            mView.showLoading()
        val subscription = mEngine.getMessageInfo(page, pageSize).subscribe(object : Subscriber<ResultInfo<ArrayList<MessageInfo>>>() {

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                if (page == 1 && !isRefresh)
                    mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<ArrayList<MessageInfo>>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.size > 0) {
                        mView.hide()
                        mView.showMessageInfos(t.data)
                    } else {
                        if (page == 1 && !isRefresh)
                            mView.showNoData()
                    }
                } else {
                    if (page == 1 && !isRefresh)
                        mView.showNoNet()
                }

            }

        })
        mSubscriptions.add(subscription)
    }

    override fun getMessageDetailInfo(id: String?) {
        mView.showLoading()
        val subscription = mEngine.getMessageDetailInfo(id).subscribe(object : Subscriber<ResultInfo<MessageInfo>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<MessageInfo>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        mView.hide()
                        mView.showMessageInfo(t.data)
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