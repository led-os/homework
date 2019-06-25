package yc.com.homework.read.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.read.domain.bean.BookConditonInfo
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.domain.bean.GradeVersionInfo

/**
 *
 * Created by wanglin  on 2019/1/9 16:24.
 */
class BookReadSelectEngine(context: Context) : BaseEngine(context) {

    fun getBookReadSelectInfo(): Observable<ResultInfo<BookConditonInfo>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.book_condition, object : TypeReference<ResultInfo<BookConditonInfo>>() {}.type,
                null, true, true, true) as Observable<ResultInfo<BookConditonInfo>>

    }


    fun getVersionsByGrade(grade: String?, subject_id: String?, volumes_id: String?): Observable<ResultInfo<ArrayList<GradeVersionInfo>>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.book_version_list, object : TypeReference<ResultInfo<ArrayList<GradeVersionInfo>>>() {}.type, mutableMapOf(
                "grade" to grade,
                "subject_id" to subject_id,
                "volumes_id" to volumes_id
                ), true, true, true) as Observable<ResultInfo<ArrayList<GradeVersionInfo>>>
    }

    fun getBookInfoByGradeVersion(volumes_id: String, version_id: String, grade: String): Observable<ResultInfo<ArrayList<BookInfo>>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.book_search, object : TypeReference<ResultInfo<ArrayList<BookInfo>>>() {}.type, mutableMapOf(
                "volumes_id" to volumes_id,
                "version_id" to version_id,
                "grade" to grade
        ), true, true, true) as Observable<ResultInfo<ArrayList<BookInfo>>>

    }
}