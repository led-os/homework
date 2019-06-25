package yc.com.homework.read.domain.engine

import android.content.Context
import com.alibaba.fastjson.TypeReference
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.engin.HttpCoreEngin
import rx.Observable
import yc.com.base.BaseEngine
import yc.com.homework.base.config.UrlConfig
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.read.domain.bean.BookUnitInfoWrapper

/**
 *
 * Created by wanglin  on 2019/1/11 11:04.
 */
class BookInfoEngine(context: Context) : BaseEngine(context) {

    fun getBookUnitInfo(volumes_id: String?, version_id: String?, grade_id: String?, subject_id: String?): Observable<ResultInfo<BookUnitInfoWrapper>> {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.book_get_unit, object : TypeReference<ResultInfo<BookUnitInfoWrapper>>() {}.type, mutableMapOf(
                "user_id" to UserInfoHelper.getUid(),
                "volumes_id" to volumes_id,
                "version_id" to version_id,
                "grade" to grade_id,
                "subject_id" to subject_id
        ), true, true, true) as Observable<ResultInfo<BookUnitInfoWrapper>>

    }
}