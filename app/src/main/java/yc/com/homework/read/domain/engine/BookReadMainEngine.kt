package yc.com.homework.read.domain.engine

import android.content.Context
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import yc.com.base.BaseEngine
import yc.com.homework.R
import yc.com.homework.base.HomeworkApp
import yc.com.homework.base.dao.BookInfoDao
import yc.com.homework.read.domain.bean.BookInfo

/**
 *
 * Created by wanglin  on 2019/2/15 09:52.
 */
class BookReadMainEngine(context: Context) : BaseEngine(context) {

    private val bookInfoDao = HomeworkApp.daoSession!!.bookInfoDao

    val bookInfos: Observable<List<BookInfo>>?
        get() = Observable.just("").subscribeOn(Schedulers.io()).map {
            var list = bookInfoDao.queryBuilder().orderDesc(BookInfoDao.Properties.SaveTime).build().list()
            if (list == null) list = arrayListOf()
            val bookInfo = BookInfo()
            bookInfo.resId = R.mipmap.read_book_add
            list.add(0, bookInfo)

            list
        }.observeOn(AndroidSchedulers.mainThread())


    fun deleteLocalBook(bookInfo: BookInfo?) {
        bookInfoDao?.delete(bookInfo)
    }
}