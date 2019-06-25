package yc.com.homework.mine.domain.engine

import android.content.Context
import android.text.TextUtils
import com.alibaba.fastjson.TypeReference
import com.bumptech.glide.load.model.stream.StreamStringLoader
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import com.kk.securityhttp.net.entry.UpFileInfo
import rx.Observable
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfo
import yc.com.homework.mine.domain.bean.GradeInfo

/**
 *
 * Created by wanglin  on 2018/11/26 13:48.
 */
class UserInfoEngine(context: Context) : BaseEngine(context) {


    //注册
    fun register(mobile: String, password: String, code: String, invite_code: String?): Observable<ResultInfo<UserInfo>> {

        val params = hashMapOf<String, String?>()

        params["mobile"] = mobile
        params["password"] = password
        params["code"] = code
        if (!TextUtils.isEmpty(invite_code)) {
            params["invite_code"] = invite_code
        }

        return HttpCoreEngin.get(mContext)
                .rxpost(UrlConfig.register, object : TypeReference<ResultInfo<UserInfo>>() {}.type, params, true, true, true)
                as Observable<ResultInfo<UserInfo>>
    }

    //修改密码
    fun modifyPwd(mobile: String, code: String, new_password: String): Observable<ResultInfo<String>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.modify_pwd, object : TypeReference<ResultInfo<String>>() {}.type, mutableMapOf(
                "mobile" to mobile,
                "code" to code,
                "new_password" to new_password
        ), true, true, true) as Observable<ResultInfo<String>>

    }

    //绑定手机号
    fun bindPhone(user_id: String, mobile: String?, code: String?): Observable<ResultInfo<UserInfo>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.bind_phone, object : TypeReference<ResultInfo<UserInfo>>() {}.type, mutableMapOf(
                "user_id" to user_id,
                "mobile" to mobile,
                "code" to code
        ), true, true, true) as Observable<ResultInfo<UserInfo>>

    }

    fun thridLogin(access_token: String?, account_type: Int, openid: String?): Observable<ResultInfo<UserInfo>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.thrid_login, object : TypeReference<ResultInfo<UserInfo>>() {}.type, mutableMapOf(
                "access_token" to access_token,
                "type" to account_type,
                "openid" to openid
        ), true, true, true) as Observable<ResultInfo<UserInfo>>

    }

    fun sendCode(mobile: String): Observable<ResultInfo<String>> {
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.send_code, object : TypeReference<ResultInfo<String>>() {}.type, mutableMapOf(
                "mobile" to mobile
        ), true, true, true) as Observable<ResultInfo<String>>
    }

    //更新用户信息
    fun updateInfo(user_id: String, nick_name: String?, face: String?, password: String?, grade_id: String?): Observable<ResultInfo<UserInfo>> {
        val params = hashMapOf<String, String?>()
        params["user_id"] = user_id
        if (!TextUtils.isEmpty(nick_name)) params["nick_name"] = nick_name
        if (!TextUtils.isEmpty(face)) params["face"] = face
        if (!TextUtils.isEmpty(password)) params["password"] = password
        if (!TextUtils.isEmpty(grade_id)) params["grade_id"] = grade_id

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.update_info, object : TypeReference<ResultInfo<UserInfo>>() {}.type, params,
                true, true, true) as Observable<ResultInfo<UserInfo>>
    }


    fun getGradeInfo(): Observable<List<GradeInfo>> {


        return Observable.just("").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).map {

            val list = arrayListOf<GradeInfo>()

            list.add(GradeInfo("1", "一年级"))
            list.add(GradeInfo("2", "二年级"))
            list.add(GradeInfo("3", "三年级"))
            list.add(GradeInfo("4", "四年级"))
            list.add(GradeInfo("5", "五年级"))
            list.add(GradeInfo("6", "六年级"))

            return@map list

        }
    }

    fun uploadAvator(user_id: String, face: String): Observable<ResultInfo<UserInfo>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.upload, object : TypeReference<ResultInfo<UserInfo>>() {}.type, mutableMapOf(
                "user_id" to user_id,
                "face" to face), false, false, true) as Observable<ResultInfo<UserInfo>>
    }

}