package yc.com.homework.mine.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import rx.Scheduler
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.domain.bean.MessageInfo

/**
 *
 * Created by wanglin  on 2018/11/23 17:50.
 */
class MessageEngine(context: Context) : BaseEngine(context) {


    fun getMessageInfo(page: Int, pageSize: Int): Observable<ResultInfo<ArrayList<MessageInfo>>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.message_list, object : TypeReference<ResultInfo<ArrayList<MessageInfo>>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "page" to "$page",
                "page_size" to pageSize), true, true, true) as Observable<ResultInfo<ArrayList<MessageInfo>>>


    }

    fun getMessageDetailInfo(id: String?): Observable<ResultInfo<MessageInfo>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.message_info, object : TypeReference<ResultInfo<MessageInfo>>() {}.type, mutableMapOf(
                "id" to id
        ), true, true, true) as Observable<ResultInfo<MessageInfo>>
    }
}