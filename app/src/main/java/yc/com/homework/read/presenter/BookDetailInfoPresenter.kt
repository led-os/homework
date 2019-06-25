package yc.com.homework.read.presenter

import android.content.Context
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Observable
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.read.contract.BookDetailInfoContract
import yc.com.homework.read.domain.bean.BookDetailInfo
import yc.com.homework.read.domain.engine.BookDetailInfoEngine

/**
 *
 * Created by wanglin  on 2019/1/14 13:51.
 */
class BookDetailInfoPresenter(context: Context, view: BookDetailInfoContract.View) : BasePresenter<BookDetailInfoEngine, BookDetailInfoContract.View>(context, view), BookDetailInfoContract.Presenter {


    init {
        mEngine = BookDetailInfoEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {


    }

    override fun getBookDetailInfo(book_id: String?, page: Int) {
        mView.showLoading()
        val subscription = mEngine.getBookDetailInfo(book_id, page).subscribe(object : Subscriber<ResultInfo<BookDetailInfo>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<BookDetailInfo>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val bookDetailInfo = t.data
                        mView.hide()
                        mView.showBookDetailInfo(bookDetailInfo)
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