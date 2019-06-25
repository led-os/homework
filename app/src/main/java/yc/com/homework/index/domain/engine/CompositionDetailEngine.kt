package yc.com.homework.index.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.NewsInfoWrapper
import yc.com.homework.index.utils.CompositionHelper

/**
 *
 * Created by wanglin  on 2019/3/21 09:35.
 */
class CompositionDetailEngine(context: Context) : BaseEngine(context) {

    fun getCompositionDetail(id: String): Observable<ResultInfo<NewsInfoWrapper>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.news_detail, object : TypeReference<ResultInfo<NewsInfoWrapper>>() {}.type, mutableMapOf(
                "id" to id
        ), true, true, true) as Observable<ResultInfo<NewsInfoWrapper>>
    }

}