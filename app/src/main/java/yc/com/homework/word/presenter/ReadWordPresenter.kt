package yc.com.homework.word.presenter

import android.content.Context

import com.kk.securityhttp.domain.ResultInfo

import rx.Subscriber
import rx.Subscription
import yc.com.base.BasePresenter
import yc.com.homework.word.contract.ReadWordContract
import yc.com.homework.word.model.domain.WordInfoList
import yc.com.homework.word.model.engin.WordEngin

/**
 * Created by admin on 2017/8/7.
 */

class ReadWordPresenter(context: Context, view: ReadWordContract.View) : BasePresenter<WordEngin, ReadWordContract.View>(context, view), ReadWordContract.Presenter {

    init {
        mEngine = WordEngin(context)
    }

    override fun getWordListByUnitId(currentPage: Int, pageCount: Int, unitId: String?) {
        mView.showLoading()
        val subscribe = mEngine.getWordListByUnitId(currentPage, pageCount, unitId).subscribe(object : Subscriber<ResultInfo<WordInfoList>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                mView.showNoNet()
            }

            override fun onNext(resultInfo: ResultInfo<WordInfoList>?) {


                if (resultInfo != null && resultInfo.data != null) {
                    mView.showWordListData(resultInfo.data.list)
                    mView.hide()
                }
            }

        })

        mSubscriptions.add(subscribe)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }
}
