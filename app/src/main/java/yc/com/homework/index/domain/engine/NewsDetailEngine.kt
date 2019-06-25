package yc.com.homework.index.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.index.domain.bean.NewsInfoWrapper

/**
 *
 * Created by wanglin  on 2019/3/2 11:51.
 */
class NewsDetailEngine(context: Context) : BaseEngine(context) {


    fun getNewsDetailInfo(newsId: String): Observable<ResultInfo<NewsInfoWrapper>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.news_detail, object : TypeReference<ResultInfo<NewsInfoWrapper>>() {}.type, mutableMapOf(
                "id" to newsId
        ), true, true, true) as Observable<ResultInfo<NewsInfoWrapper>>
    }
}