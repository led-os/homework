package yc.com.homework.mine.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.domain.bean.ApplyRuleInfo
import yc.com.homework.mine.domain.bean.TeacherApplyState
import yc.com.homework.mine.domain.bean.UploadResultInfo

/**
 *
 * Created by wanglin  on 2018/11/26 08:50.
 */
class ApplyTeacherDetailEngine(context: Context) : BaseEngine(context) {

    fun getApplyTeacherRuleInfo(): Observable<ResultInfo<ApplyRuleInfo>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.apply_teacher_rule,
                object : TypeReference<ResultInfo<ApplyRuleInfo>>() {}.type, null, true, true, true) as Observable<ResultInfo<ApplyRuleInfo>>
    }

    fun applyTeacher(name: String?, sex: Int, mobile: String?, subject_id: Int, job: Int, teacher_cert: String?, diploma: String?): Observable<ResultInfo<String>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.apply_teacher, object : TypeReference<ResultInfo<String>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "name" to name,
                "sex" to "$sex",
                "mobile" to mobile,
                "subject_id" to subject_id,
                "job" to "$job",
                "teacher_cert" to teacher_cert,
                "diploma" to diploma

        ), true, true, true) as Observable<ResultInfo<String>>
    }

    fun uploadPic(file: String): Observable<ResultInfo<UploadResultInfo>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.common_upload, object : TypeReference<ResultInfo<UploadResultInfo>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "file" to file), false, false, true) as Observable<ResultInfo<UploadResultInfo>>
    }

    fun getApplyState(): Observable<ResultInfo<TeacherApplyState>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.teacher_status,object :TypeReference<ResultInfo<TeacherApplyState>>(){}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid()
        ),true,true,true) as Observable<ResultInfo<TeacherApplyState>>
    }


}