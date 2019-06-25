package yc.com.homework.mine.presenter

import android.content.Context
import android.graphics.Bitmap
import com.alibaba.fastjson.TypeReference
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Observer
import rx.Subscriber
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.CommonInfoHelper
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.EngineUtils
import yc.com.homework.base.utils.Preference
import yc.com.homework.index.domain.bean.IndexInfo
import yc.com.homework.mine.contract.MineMainContract
import yc.com.homework.mine.domain.bean.UnreadMsgInfo
import yc.com.homework.mine.utils.GlideCircleTransformation
import yc.com.homework.mine.utils.ImageUtils

/**
 *
 * Created by wanglin  on 2018/11/29 10:19.
 */
class MineMainPresenter(context: Context, view: MineMainContract.View) : BasePresenter<BaseEngine, MineMainContract.View>(context, view), MineMainContract.Presenter {

    private var face_path by Preference(context, SpConstant.face_path, "")

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {

        if (!isForceUI) return
        getUserInfo()
        getUnReadMessageCount()
        getEverDays()

    }


    override fun getUserInfo() {
        val subscription = EngineUtils.getUserInfo(mContext).subscribe(object : Subscriber<ResultInfo<UserInfo>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(userInfoResultInfo: ResultInfo<UserInfo>?) {
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    val userInfo = userInfoResultInfo.data
                    UserInfoHelper.saveUserInfo(userInfoResultInfo.data)
                    mView.showUserInfo(userInfo)
                    covertPathToBitmap(userInfo!!.face)
                    //                    ObservableManager.get().notifyMyObserver(userInfoResultInfo.data);
                } else {
                    mView.showNoLogin()
                }
            }
        })
        mSubscriptions.add(subscription)

    }

    fun covertPathToBitmap(path: String?) {
        try {
            val request = Glide.with(mContext).load(path).asBitmap()
            val transformation = GlideCircleTransformation(mContext)
            request.transform(transformation).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                    val result = ImageUtils.compressBitmap(bitmap)
                    val facePath = ImageUtils.convertBitmapToPath(mContext, result)
//                    SPUtils.getInstance().put(SpConstant.face_path, facePath)
                    face_path = facePath
                    mView.showBitmap(result)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }


    fun getUnReadMessageCount() {
        val subscription = EngineUtils.getUnReadMessageCount(mContext).subscribe(object : Observer<ResultInfo<UnreadMsgInfo>> {

            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {

            }

            override fun onNext(t: ResultInfo<UnreadMsgInfo>?) {
                if (t != null && t.code == HttpConfig.STATUS_OK && t.data != null) {
                    mView.showUnreadMsg(t.data)
                }
            }


        })
        mSubscriptions.add(subscription)
    }

    private fun getEverDays() {
        CommonInfoHelper.getO(mContext, SpConstant.index_info, object : TypeReference<ResultInfo<IndexInfo>>() {}.type, object : CommonInfoHelper.onParseListener<ResultInfo<IndexInfo>> {
            override fun onParse(o: ResultInfo<IndexInfo>?) {
                if (o?.data != null) {
                    mView.showIndexInfo(o.data)

                }
            }

            override fun onFail(json: String?) {
            }
        })
    }

}