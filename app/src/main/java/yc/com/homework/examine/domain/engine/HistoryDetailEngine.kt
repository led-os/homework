package yc.com.homework.examine.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo

/**
 *
 * Created by wanglin  on 2018/11/27 15:05.
 */
class HistoryDetailEngine(context: Context) : BaseEngine(context) {

    fun getHistoryDetailInfos(status: Int?, page: Int, page_size: Int): Observable<ResultInfo<ArrayList<HomeworkDetailInfo>>> {


        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.homework_history, object : TypeReference<ResultInfo<ArrayList<HomeworkDetailInfo>>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "status" to "$status",
                "page" to "$page",
                "page_size" to "$page_size"
        ), true, true, true) as Observable<ResultInfo<ArrayList<HomeworkDetailInfo>>>

    }

    fun setReadState(task_id:String?):Observable<ResultInfo<String>>{
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.homework_isRead,object :TypeReference<ResultInfo<String>>(){}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "task_id" to task_id
        ),true,true,true) as Observable<ResultInfo<String>>

    }


}