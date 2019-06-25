package yc.com.homework.word.presenter

import android.content.Context

import com.kk.securityhttp.domain.ResultInfo

import rx.Subscriber
import rx.Subscription
import yc.com.base.BasePresenter
import yc.com.homework.word.contract.WordUnitContract
import yc.com.homework.word.model.domain.BookInfoWarpper
import yc.com.homework.word.model.domain.WordUnitInfoList
import yc.com.homework.word.model.engin.WordEngin

/**
 * Created by admin on 2017/8/7.
 */

class WordUnitPresenter(context: Context, view: WordUnitContract.View) : BasePresenter<WordEngin, WordUnitContract.View>(context, view), WordUnitContract.Presenter {

    init {
        mEngine = WordEngin(context)
    }

    override fun getBookInfoById(bookId: String?) {
        mView.showLoading()
        val subscribe = mEngine.getBookInfoId(bookId).subscribe(object : Subscriber<ResultInfo<BookInfoWarpper>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                mView.showNoNet()
            }

            override fun onNext(infoWarpper: ResultInfo<BookInfoWarpper>?) {
                if (infoWarpper != null) {
                    mEngine.getWordUnitByBookId(0, 0, bookId).subscribe(object : Subscriber<ResultInfo<WordUnitInfoList>>() {
                        override fun onCompleted() {

                        }

                        override fun onError(e: Throwable) {
                            mView.showNoNet()
                        }

                        override fun onNext(resultInfo: ResultInfo<WordUnitInfoList>) {

                            mView.showBookInfo(infoWarpper.data.info)
                            mView.showWordUnitListData(resultInfo.data)
                            mView.hide()
                        }
                    })
                }

            }
        })

        mSubscriptions.add(subscribe)
    }

    override fun getWordUnitByBookId(currentPage: Int, pageCount: Int, bookId: String) {
        val subscribe = mEngine.getWordUnitByBookId(currentPage, pageCount, bookId).subscribe(object : Subscriber<ResultInfo<WordUnitInfoList>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(resultInfo: ResultInfo<WordUnitInfoList>?) {
                if (resultInfo != null) {
                    mView.showWordUnitListData(resultInfo.data)
                }
            }
        })

        mSubscriptions.add(subscribe)
    }

    override fun loadData(forceUpdate: Boolean, showLoadingUI: Boolean) {

    }
}
