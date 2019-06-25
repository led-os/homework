package yc.com.homework.read.presenter

import android.content.Context
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.read.contract.BookReadMainContract
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.domain.engine.BookReadMainEngine

/**
 *
 * Created by wanglin  on 2019/2/15 10:10.
 */
class BookReadMainPresenter(context: Context, view: BookReadMainContract.View) : BasePresenter<BookReadMainEngine, BookReadMainContract.View>(context, view), BookReadMainContract.Presenter {


    init {
        mEngine = BookReadMainEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
        getBookInfos()
    }

    override fun getBookInfos() {
        val subscription = mEngine.bookInfos?.subscribe(object : Subscriber<List<BookInfo>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: List<BookInfo>?) {
                mView.showBookInfos(t)
            }
        })

        mSubscriptions.add(subscription)

    }

    override fun deleteBook(bookInfo: BookInfo?) {
        mEngine.deleteLocalBook(bookInfo)

    }
}