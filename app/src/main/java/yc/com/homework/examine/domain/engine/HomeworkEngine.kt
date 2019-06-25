package yc.com.homework.examine.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.kk.securityhttp.net.entry.UpFileInfo
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.examine.domain.bean.UploadInfo
import java.io.File

/**
 *
 * Created by wanglin  on 2018/12/4 19:03.
 */
class HomeworkEngine(context: Context) : BaseEngine(context) {


    fun uploadHomework(file: File?, subject_id: String?, type: String?, grade_id: String?, task_id: String?): Observable<ResultInfo<UploadInfo>> {

        val upFileInfo = UpFileInfo()
        upFileInfo.name = "image"
        upFileInfo.filename = "image"
        upFileInfo.file = file

        return HttpCoreEngin.get(mContext).rxuploadFile(UrlConfig.upload_homework, object : TypeReference<ResultInfo<UploadInfo>>() {}.type, upFileInfo, mutableMapOf(
                "subject_id" to subject_id,
                "type" to type,
                "grade_id" to grade_id,
                "user_id" to UserInfoHelper.getUid(),
                "task_id" to task_id), true) as Observable<ResultInfo<UploadInfo>>

    }
}