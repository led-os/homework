package yc.com.homework.wall.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.wall.domain.bean.HomeworkDetailInfoWrapper
import yc.com.homework.wall.domain.bean.HomeworkInfo

/**
 *
 * Created by wanglin  on 2018/11/27 10:18.
 */
class WallEngine(context: Context) : BaseEngine(context) {

    fun getHistoryDetailInfo(task_id: String?): Observable<ResultInfo<HomeworkDetailInfoWrapper>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.history_detail, object : TypeReference<ResultInfo<HomeworkDetailInfoWrapper>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "task_id" to task_id
        ), true, true, true) as Observable<ResultInfo<HomeworkDetailInfoWrapper>>
    }

    fun feedHomework(task_id: String?): Observable<ResultInfo<String>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.question_feed, object : TypeReference<ResultInfo<HomeworkDetailInfoWrapper>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "task_id" to task_id
        ), true, true, true) as Observable<ResultInfo<String>>
    }


}