package yc.com.homework.read.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.read.domain.bean.BookDetailInfo

/**
 *
 * Created by wanglin  on 2019/1/14 13:37.
 */
class BookDetailInfoEngine(context: Context) : BaseEngine(context) {

    fun getBookDetailInfo(book_id: String?, page: Int): Observable<ResultInfo<BookDetailInfo>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.book_get_detail, object : TypeReference<ResultInfo<BookDetailInfo>>() {}.type, mutableMapOf(
                "book_id" to book_id,
                "page" to "$page"
        ), true, true, true) as Observable<ResultInfo<BookDetailInfo>>
    }

}