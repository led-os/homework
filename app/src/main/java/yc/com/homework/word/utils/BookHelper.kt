package yc.com.homework.word.utils

import android.content.Context

import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin

import java.util.ArrayList
import java.util.HashMap

import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.functions.Func1
import rx.schedulers.Schedulers
import yc.com.homework.base.HomeworkApp
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.dao.BookWordInfoDao
import yc.com.homework.word.model.domain.BookInfoWarpper
import yc.com.homework.word.model.domain.BookWordInfo

/**
 * Created by zhangkai on 2017/8/2.
 */

object BookHelper {

    private val bookInfoDao = HomeworkApp.daoSession!!.bookWordInfoDao

    fun getBookInfoId(context: Context, bookId: String?): Observable<ResultInfo<BookInfoWarpper>> {
        return HttpCoreEngin.get(context).rxpost(UrlConfig.BOOK_INFO_URL, object : TypeReference<ResultInfo<BookInfoWarpper>>() {}.type, mutableMapOf(
                "book_id" to bookId
        ), true, true, true) as Observable<ResultInfo<BookInfoWarpper>>
    }

    fun bookList(currentPage: Int, pageCount: Int): Observable<ArrayList<BookWordInfo>> {
        return Observable.just("").subscribeOn(Schedulers.io()).map { bookInfoDao.queryBuilder().limit(pageCount).offset((currentPage - 1) * pageCount).orderDesc(BookWordInfoDao.Properties.SaveTime).build().list() as ArrayList<BookWordInfo> }.observeOn(AndroidSchedulers.mainThread())

    }

    fun addBook(aBookInfo: BookWordInfo?): Observable<ArrayList<BookWordInfo>> {
        return Observable.just("").subscribeOn(Schedulers.io()).map {
            val result = queryBook(aBookInfo?.bookId)
            val saveTime = System.currentTimeMillis()
            if (result == null) {
                aBookInfo?.saveTime = saveTime
                bookInfoDao.insert(aBookInfo)
            } else {
                result.saveTime = saveTime
                bookInfoDao.update(result)
            }

            bookInfoDao.queryBuilder().orderDesc(BookWordInfoDao.Properties.SaveTime).build().list() as ArrayList<BookWordInfo>
        }.observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteBook(dBookInfo: BookWordInfo?): Observable<ArrayList<BookWordInfo>> {
        return Observable.just("").subscribeOn(Schedulers.io()).map {
            bookInfoDao.delete(dBookInfo)
            bookInfoDao.queryBuilder().orderDesc(BookWordInfoDao.Properties.SaveTime).list() as ArrayList<BookWordInfo>
        }.observeOn(AndroidSchedulers.mainThread())
    }

    fun queryBook(bookId: String?): BookWordInfo? {
        return bookInfoDao.queryBuilder().where(BookWordInfoDao.Properties.BookId.eq(bookId)).build().unique()
    }

}
