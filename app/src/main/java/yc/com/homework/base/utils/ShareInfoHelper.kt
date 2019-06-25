package yc.com.homework.base.utils

import android.content.Context

import com.alibaba.fastjson.JSON
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import com.kk.utils.LogUtil

import rx.Subscriber
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.mine.domain.bean.ShareInfo
import yc.com.soundmark.base.constant.SpConstant

/**
 * Created by wanglin  on 2018/10/29 09:48.
 */
object ShareInfoHelper {

    private val TAG = "ShareInfoHelper"
    private var mShareInfo: ShareInfo? = null

    val shareInfo: ShareInfo?
        get() {
            if (mShareInfo != null) {
                return mShareInfo
            }
            var shareInfo: ShareInfo? = null
            try {
                val str = SPUtils.getInstance().getString(SpConstant.SHARE_INFO)

                shareInfo = JSON.parseObject(str, ShareInfo::class.java)

            } catch (e: Exception) {

                LogUtil.msg(TAG + "  json parse error->" + e.message)

            }

            mShareInfo = shareInfo

            return mShareInfo
        }

    fun saveShareInfo(shareInfo: ShareInfo) {
        ShareInfoHelper.mShareInfo = shareInfo
        try {
            val str = JSON.toJSONString(shareInfo)

            SPUtils.getInstance().put(SpConstant.SHARE_INFO, str)
        } catch (e: Exception) {
            LogUtil.msg(TAG + "  to json error->" + e.message)
        }


    }


    fun getShareInfos(context: Context) {
        EngineUtils.getShareInfos(context).subscribe(object : Subscriber<ResultInfo<ShareInfo>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(shareInfoResultInfo: ResultInfo<ShareInfo>?) {
                if (shareInfoResultInfo != null && shareInfoResultInfo.code == HttpConfig.STATUS_OK && shareInfoResultInfo.data != null) {
                    saveShareInfo(shareInfoResultInfo.data)
                }
            }
        })
    }
}
