package yc.com.homework.word.model.engin

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.umeng.socialize.utils.DeviceConfig.context
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.word.model.domain.BookInfoWarpper
import yc.com.homework.word.model.domain.WordInfoList
import yc.com.homework.word.model.domain.WordUnitInfo
import yc.com.homework.word.model.domain.WordUnitInfoList
import yc.com.homework.word.utils.BookHelper

/**
 * Created by admin on 2017/8/7.
 */

class WordEngin(context: Context) : BaseEngine(context) {

    fun getBookInfoId(bookId: String?): Observable<ResultInfo<BookInfoWarpper>> {
        return BookHelper.getBookInfoId(mContext, bookId)
    }



    fun getWordUnitByBookId(currentPage: Int, pageCount: Int, bookId: String?): Observable<ResultInfo<WordUnitInfoList>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.WORD_UNIT_LIST_URL, object : TypeReference<ResultInfo<WordUnitInfoList>>() {}.type, mutableMapOf(
                "current_page" to "$currentPage",
                "page_count" to "$pageCount",
                "book_id" to bookId
        ), true, true, true) as Observable<ResultInfo<WordUnitInfoList>>
    }


    fun getWordListByUnitId(currentPage: Int, pageCount: Int, unitId: String?): Observable<ResultInfo<WordInfoList>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.WORD_LIST_URL, object : TypeReference<ResultInfo<WordInfoList>>() {

        }.type, mutableMapOf(
                "current_page" to "$currentPage",
                "page_count" to "$pageCount",
                "unit_id" to unitId
        ), true, true, true) as Observable<ResultInfo<WordInfoList>>
    }


}
