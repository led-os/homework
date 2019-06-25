package yc.com.homework.base.utils

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.domain.bean.ReadCountInfo
import yc.com.homework.mine.domain.bean.ShareInfo
import yc.com.homework.mine.domain.bean.UnreadMsgInfo
import yc.com.homework.wall.domain.bean.HomeworkDetailInfoWrapper
import yc.com.homework.wall.domain.bean.HomeworkInfo

/**
 *
 * Created by wanglin  on 2018/11/28 19:42.
 */
class EngineUtils {

    companion object {

        /**
         * 游客登录
         */
        fun guestLogin(context: Context): Observable<ResultInfo<UserInfo>> {
            return HttpCoreEngin.get(context).rxpost(
                    UrlConfig.device_reg,
                    object : TypeReference<ResultInfo<UserInfo>>() {}.type,
                    null,
                    true,
                    true,
                    true
            ) as Observable<ResultInfo<UserInfo>>
        }

        /**
         * 用户手机号、密码登录
         */
        fun phoneLogin(context: Context, phone: String, pwd: String): Observable<ResultInfo<UserInfo>> {

            return HttpCoreEngin.get(context).rxpost(
                    UrlConfig.login,
                    object : TypeReference<ResultInfo<UserInfo>>() {}.type,
                    mutableMapOf("mobile" to phone, "password" to pwd),
                    true,
                    true,
                    true
            ) as Observable<ResultInfo<UserInfo>>
        }


        fun getUserInfo(context: Context): Observable<ResultInfo<UserInfo>> {
            return HttpCoreEngin.get(context).rxpost(UrlConfig.user_info, object : TypeReference<ResultInfo<UserInfo>>() {}.type, mutableMapOf(
                    "user_id" to UserInfoHelper.getUid()
            ), true, true, true) as Observable<ResultInfo<UserInfo>>
        }


        fun getUnReadMessageCount(context: Context): Observable<ResultInfo<UnreadMsgInfo>> {
            return HttpCoreEngin.get(context).rxpost(UrlConfig.unread_message, object : TypeReference<ResultInfo<UnreadMsgInfo>>() {}.type, mutableMapOf(
                    "user_id" to UserInfoHelper.getUid()
            ), true, true, true) as Observable<ResultInfo<UnreadMsgInfo>>
        }

        fun getNewsInfos(context: Context, page: Int, pageSize: Int): Observable<ResultInfo<ArrayList<NewsInfo>>> {

            return HttpCoreEngin.get(context).rxpost(UrlConfig.index_recommend, object : TypeReference<ResultInfo<ArrayList<NewsInfo>>>() {}.type, mutableMapOf(
                    "page" to "$page",
                    "page_size" to "$pageSize"
            ), true, true, true) as Observable<ResultInfo<ArrayList<NewsInfo>>>
        }

        fun statisticsCount(context: Context, id: String?): Observable<ResultInfo<ReadCountInfo>> {

            return HttpCoreEngin.get(context).rxpost(UrlConfig.read_pv, object : TypeReference<ResultInfo<ReadCountInfo>>() {}.type, mutableMapOf(
                    "id" to id
            ), true, true, true) as Observable<ResultInfo<ReadCountInfo>>
        }

        fun getShareInfos(context: Context): Observable<ResultInfo<ShareInfo>> {
            return HttpCoreEngin.get(context).rxpost(UrlConfig.share_info, object : TypeReference<ResultInfo<ShareInfo>>() {}.type,
                    null, true, true, true) as Observable<ResultInfo<ShareInfo>>
        }


        fun getWalInfos(context: Context, subject_id: Int, page: Int, page_size: Int): Observable<ResultInfo<ArrayList<HomeworkInfo>>> {

            return HttpCoreEngin.get(context).rxpost(UrlConfig.homework_list, object : TypeReference<ResultInfo<ArrayList<HomeworkInfo>>>() {}.type, mutableMapOf(
                    "subject_id" to "$subject_id",
                    "page" to "$page",
                    "page_size" to "$page_size"

            ), true, true, true) as Observable<ResultInfo<ArrayList<HomeworkInfo>>>


        }

        fun getWallDetailInfo(context: Context, task_id: String?): Observable<ResultInfo<HomeworkDetailInfoWrapper>> {

            return HttpCoreEngin.get(context).rxpost(UrlConfig.homework_detail, object : TypeReference<ResultInfo<HomeworkDetailInfoWrapper>>() {}.type, mutableMapOf(
                    "task_id" to task_id
            ), true, true, true) as Observable<ResultInfo<HomeworkDetailInfoWrapper>>
        }

    }
}