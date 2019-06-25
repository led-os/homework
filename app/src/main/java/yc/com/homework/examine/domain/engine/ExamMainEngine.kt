package yc.com.homework.examine.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.examine.domain.bean.UploadStatusInfo

/**
 *
 * Created by wanglin  on 2018/12/12 16:18.
 */
class ExamMainEngine(context: Context) : BaseEngine(context) {

    fun getUploadStatus(user_id :String?): Observable<ResultInfo<UploadStatusInfo>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.upload_status, object : TypeReference<ResultInfo<UploadStatusInfo>>() {}.type, mutableMapOf(
                "user_id" to user_id
        ), true, true, true) as Observable<ResultInfo<UploadStatusInfo>>
    }
}