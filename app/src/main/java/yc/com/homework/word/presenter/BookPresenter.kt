package yc.com.homework.word.presenter

import android.content.Context

import java.util.ArrayList

import rx.Subscriber
import rx.Subscription
import yc.com.base.BasePresenter
import yc.com.blankj.utilcode.util.UIUitls
import yc.com.homework.word.contract.BookContract
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.engin.BookEngin

/**
 * Created by admin on 2017/8/7.
 */

class BookPresenter(context: Context, view: BookContract.View) : BasePresenter<BookEngin, BookContract.View>(context, view), BookContract.Presenter {

    init {
        mEngine = BookEngin(context)
    }

    override fun bookList(currentPage: Int, pageCount: Int) {
        mView.showLoading()
        val subscribe = mEngine.bookList(currentPage, pageCount).subscribe(object : Subscriber<ArrayList<BookWordInfo>>() {
            override fun onCompleted() {
                UIUitls.postDelayed(500) { mView.hide() }
            }

            override fun onError(e: Throwable) {
                mView.hide()
            }

            override fun onNext(bookInfos: ArrayList<BookWordInfo>) {
                UIUitls.post { mView.showBookListData(bookInfos, false) }
            }
        })

        mSubscriptions.add(subscribe)
    }


    override fun addBook(bookInfo: BookWordInfo?) {
        val subscribe = mEngine.addBook(bookInfo).subscribe(object : Subscriber<ArrayList<BookWordInfo>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(bookInfos: ArrayList<BookWordInfo>) {
                UIUitls.post { mView.showBookListData(bookInfos, false) }
            }
        })
        mSubscriptions.add(subscribe)
    }

    override fun deleteBook(bookInfo: BookWordInfo?) {
        val subscribe = mEngine.deleteBook(bookInfo).subscribe(object : Subscriber<ArrayList<BookWordInfo>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(bookInfos: ArrayList<BookWordInfo>) {
                UIUitls.post { mView.showBookListData(bookInfos, true) }
            }
        })
        mSubscriptions.add(subscribe)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {
        if (!forceUpdate) return
//        bookList(0, 0)
    }
}
