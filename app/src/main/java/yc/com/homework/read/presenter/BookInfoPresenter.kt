package yc.com.homework.read.presenter

import android.content.Context
import com.alibaba.fastjson.JSON
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.kk.utils.LogUtil
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.read.contract.BookInfoContract
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.domain.bean.BookUnitInfo
import yc.com.homework.read.domain.bean.BookUnitInfoWrapper
import yc.com.homework.read.domain.engine.BookInfoEngine

/**
 *
 * Created by wanglin  on 2019/1/11 11:04.
 */
class BookInfoPresenter(context: Context, view: BookInfoContract.View) : BasePresenter<BookInfoEngine, BookInfoContract.View>(context, view), BookInfoContract.Presenter {


    init {
        mEngine = BookInfoEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {

    }

    override fun getBookUnitInfo(volumes_id: String?, version_id: String?, grade_id: String?, subject_id: String?) {
        mView.showLoading()
        val subscription = mEngine.getBookUnitInfo(volumes_id, version_id, grade_id, subject_id).subscribe(object : Subscriber<ResultInfo<BookUnitInfoWrapper>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<BookUnitInfoWrapper>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val data = t.data
                        mView.hide()
//                        val classList = data.classList
                        resetData(data)
//                        classList?.let {
//                            for ((i, value) in classList.withIndex()) {
//                                value.tag = i
//                                val bookUnitInfos = value.bookUnitInfos
//                                bookUnitInfos?.let {
//                                    for (result in bookUnitInfos) {
//                                        result.tag = i
//                                    }
//                                }
//                            }
//                            mView.showBookUnitInfo(data)
//                        }

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


    fun resetData(bookUnitInfoWrapper: BookUnitInfoWrapper) {
        val bookUnitInfos = bookUnitInfoWrapper.classList
        if (bookUnitInfos == null) {
            mView.showBookUnitInfo(bookUnitInfoWrapper)
            return
        }
        val list = ArrayList<BookUnitInfo>()
        val infoWrapper = BookUnitInfoWrapper()
        val bookInfo = BookInfo()
        bookInfo.id = bookUnitInfoWrapper.bookInfo?.id
        bookInfo.name = bookUnitInfoWrapper.bookInfo?.name
        bookInfo.coverImg = bookUnitInfoWrapper.bookInfo?.coverImg
        bookInfo.versionId = bookUnitInfoWrapper.bookInfo?.versionId
        bookInfo.versionName = bookUnitInfoWrapper.bookInfo?.versionName
        bookInfo.volumesId = bookUnitInfoWrapper.bookInfo?.volumesId
        bookInfo.grade = bookUnitInfoWrapper.bookInfo?.grade
        bookInfo.subject = bookUnitInfoWrapper.bookInfo?.subject
        infoWrapper.bookInfo = bookInfo
        var total = 0

        for ((i, value) in bookUnitInfos.withIndex()) {

            val bookUnitInfo = BookUnitInfo()
            bookUnitInfo.tag = i
            bookUnitInfo.headTitle = true
            bookUnitInfo.id = value.id
            bookUnitInfo.title = value.title
            bookUnitInfo.bookId = value.bookId
            bookUnitInfo.bookTitle = value.bookTitle
            bookUnitInfo.commonTitle = value.title

            total += value.pageTotal
            list.add(bookUnitInfo)

            val unitInfos = value.bookUnitInfos
            unitInfos?.let {
                for (result in unitInfos) {
                    val unitInfo = BookUnitInfo()
                    unitInfo.headTitle = false
                    unitInfo.tag = i
                    unitInfo.id = result.id
                    unitInfo.page = result.page
                    unitInfo.pageIndex = result.pageIndex
                    unitInfo.title = result.title
                    unitInfo.chapterId = result.chapterId
                    unitInfo.bookId = result.bookId
                    unitInfo.pageTotal = result.pageTotal
                    unitInfo.commonTitle = value.title
                    unitInfo.can_see = value.can_see

                    val newUnitInfos = arrayListOf<BookUnitInfo>()
                    for (j in 0 until result.pageTotal) {
                        val newUnitInfo = BookUnitInfo()
                        newUnitInfo.can_see = unitInfo.can_see
                        newUnitInfo.page = result.page + j
                        newUnitInfos.add(newUnitInfo)
                    }
                    unitInfo.bookUnitInfos = newUnitInfos

                    list.add(unitInfo)
                }
            }

        }
        infoWrapper.classList = list
        infoWrapper.pageTotal = total

        mView.showBookUnitInfo(infoWrapper)
//        val json = JSON.toJSONString(list)
//        LogUtil.msg("json:  $json")


    }


}