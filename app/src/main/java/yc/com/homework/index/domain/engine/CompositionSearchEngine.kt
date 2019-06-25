package yc.com.homework.index.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import yc.com.answer.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.SearchRecordInfo

/**
 *
 * Created by wanglin  on 2019/3/29 10:42.
 */
class CompositionSearchEngine(context: Context) : BaseEngine(context) {


    fun getHotSearchRecords(): Observable<ResultInfo<ArrayList<SearchRecordInfo>>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.composition_hot_tag, object : TypeReference<ResultInfo<ArrayList<SearchRecordInfo>>>() {}.type,
                null, true, true, true) as Observable<ResultInfo<ArrayList<SearchRecordInfo>>>
    }

    fun compositionSearch(page: Int, pageSize: Int, title: String?, grade: String?, version: String?, unit: String?): Observable<ResultInfo<ArrayList<CompositionInfo>>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.composition_search, object : TypeReference<ResultInfo<ArrayList<CompositionInfo>>>() {}.type, mutableMapOf(
                "page" to "$page",
                "page_size" to "$pageSize",
                "title" to title,
                "grade" to grade,
                "version" to version,
                "unit" to unit
        ), true, true, true) as Observable<ResultInfo<ArrayList<CompositionInfo>>>
    }
}