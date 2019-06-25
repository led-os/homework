package yc.com.homework.index.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.answer.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.index.domain.bean.CompositionInfo

/**
 *
 * Created by wanglin  on 2019/4/2 10:48.
 */
class MathematicalEngine(context: Context) : BaseEngine(context) {

    fun getMathematicalList(page: Int, pageSize: Int): Observable<ResultInfo<ArrayList<CompositionInfo>>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.mathematical_list, object : TypeReference<ResultInfo<ArrayList<CompositionInfo>>>() {}.type, mutableMapOf(
                "page" to "$page",
                "page_size" to "$pageSize"
        ), true, true, true) as Observable<ResultInfo<ArrayList<CompositionInfo>>>
    }
}