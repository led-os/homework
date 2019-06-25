package yc.com.homework.mine.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.domain.bean.CoinDetailInfo
import yc.com.homework.mine.domain.bean.CoinDetailInfoWrapper

/**
 *
 * Created by wanglin  on 2018/11/24 09:43.
 */
class CoinDetailEngine(context: Context) : BaseEngine(context) {

    fun getCoinDetailList(page: Int, page_size: Int): Observable<ResultInfo<CoinDetailInfoWrapper>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.coin_bill, object : TypeReference<ResultInfo<CoinDetailInfoWrapper>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "page" to "$page",
                "page_size" to "$page_size"
        ), true, true, true) as Observable<ResultInfo<CoinDetailInfoWrapper>>


    }
}