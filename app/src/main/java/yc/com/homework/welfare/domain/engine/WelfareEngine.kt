package yc.com.homework.welfare.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.welfare.domain.bean.WelfareInfoWrapper

/**
 *
 * Created by wanglin  on 2019/3/12 15:44.
 */
class WelfareEngine(context: Context) : BaseEngine(context) {

    fun getWelfareInfo(): Observable<ResultInfo<WelfareInfoWrapper>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.adv_info, object : TypeReference<ResultInfo<WelfareInfoWrapper>>() {}.type, null,
                true, true, true) as Observable<ResultInfo<WelfareInfoWrapper>>
    }
}