package yc.com.homework.mine.domain.engine

import android.content.Context
import android.text.TextUtils

import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin


import java.util.HashMap

import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper

/**
 * Created by wanglin  on 2018/3/22 08:59.
 */

class ShareEngine(context: Context) : BaseEngine(context) {


    fun share(task_id: String?): Observable<ResultInfo<String>> {


        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.share_reward, object : TypeReference<ResultInfo<String>>() {

        }.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "task_id" to task_id
        ), true, true, true) as Observable<ResultInfo<String>>

    }


    //    public Observable<ResultInfo<String>> shareMoney() {
    //        Map<String, String> headers = new HashMap<>();
    //        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
    //            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
    //        return HttpCoreEngin.get(mContext).rxpost(NetConstant.task_share_reward_url, new TypeReference<ResultInfo<String>>() {
    //        }.getType(), null, headers, false, false, false);
    //
    //    }
}
