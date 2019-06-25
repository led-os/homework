package yc.com.homework.word.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.umeng.socialize.utils.DeviceConfig.context
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.HomeworkApp
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.word.model.domain.*
import yc.com.homework.word.utils.BookHelper
import java.util.*

/**
 * Created by admin on 2017/8/7.
 */

class BookEngin(context: Context) : BaseEngine(context) {
    //
    //
    private val gradeInfoDao = HomeworkApp.daoSession?.gradeInfoDao

    fun getGradeListFromDB(): List<GradeInfo>? {
        return gradeInfoDao?.queryBuilder()?.build()?.list()
    }

    fun getBookInfoId(bookId: String?): Observable<ResultInfo<BookInfoWarpper>> {
        return BookHelper.getBookInfoId(mContext, bookId)
    }

    fun bookList(currentPage: Int, pageCount: Int): Observable<ArrayList<BookWordInfo>> {
        return BookHelper.bookList(currentPage, pageCount)
    }


    fun gradeList(context: Context): Observable<ResultInfo<GradeInfoList>> {

        return HttpCoreEngin.get(context).rxpost(UrlConfig.GRADE_LIST_URL, object : TypeReference<ResultInfo<GradeInfoList>>() {

        }.type, null,
                true, true,
                true) as Observable<ResultInfo<GradeInfoList>>
    }

    fun getCVListByGradeId(context: Context, gradeId: String?, partType: String?): Observable<ResultInfo<CourseVersionInfoList>> {

        return HttpCoreEngin.get(context).rxpost(UrlConfig.COURSE_VERSION_LIST_URL, object : TypeReference<ResultInfo<CourseVersionInfoList>>() {}.type, mutableMapOf(
                "grade" to gradeId,
                "part_type" to partType
        ), true, true, true) as Observable<ResultInfo<CourseVersionInfoList>>
    }

    fun bookUnitInfo(currentPage: Int, pageCount: Int, bookId: String?): Observable<ResultInfo<UnitInfoList>> {

        return HttpCoreEngin.get(context).rxpost(UrlConfig.WORD_UNIT_LIST_URL, object : TypeReference<ResultInfo<UnitInfoList>>() {}.type, mutableMapOf(
                "current_page" to "$currentPage",
                "book_id" to bookId
        ), true, true, true) as Observable<ResultInfo<UnitInfoList>>
    }


    fun addBook(bookInfo: BookWordInfo?): Observable<ArrayList<BookWordInfo>> {
        return BookHelper.addBook(bookInfo)
    }

    fun deleteBook(bookInfo: BookWordInfo?): Observable<ArrayList<BookWordInfo>> {
        return BookHelper.deleteBook(bookInfo)
    }

    fun saveGradeListToDB(list: List<GradeInfo>?) {
        list?.forEach {
            gradeInfoDao?.insertOrReplace(it)
        }
    }

    fun queryBook(bookId: String?): BookWordInfo? {
        return BookHelper.queryBook(bookId)
    }
}
