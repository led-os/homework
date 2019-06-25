package yc.com.homework.mine.fragment

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import com.jakewharton.rxbinding.view.RxView
import com.kk.share.UMShareImpl
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.UMShareListener
import com.umeng.socialize.bean.SHARE_MEDIA

import java.util.ArrayList
import java.util.concurrent.TimeUnit

import butterknife.BindView
import cn.forward.androids.utils.LogUtil
import kotlinx.android.synthetic.main.fragment_share.*
import rx.functions.Action1
import yc.com.base.BaseActivity
import yc.com.base.BaseBottomSheetDialogFragment
import yc.com.homework.R
import yc.com.base.ObservableManager
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.ShareInfoHelper
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.mine.contract.ShareContract
import yc.com.homework.mine.domain.bean.ShareInfo
import yc.com.homework.mine.presenter.SharePresenter

/**
 * Created by wanglin  on 2018/3/15 14:32.
 */

class ShareFragment : BaseBottomSheetDialogFragment<SharePresenter>(), ShareContract.View {

    @BindView(R.id.ll_wx)
    lateinit var llWx: LinearLayout
    @BindView(R.id.ll_circle)
    lateinit var llCircle: LinearLayout
    @BindView(R.id.ll_qq)
    lateinit var llQq: LinearLayout
    @BindView(R.id.tv_cancel_share)
    lateinit var tvCancelShare: TextView


    private var mShareInfo: ShareInfo? = null
    private var mIsShareMoney: Boolean = false


    private val umShareListener = object : UMShareListener {
        override fun onStart(share_media: SHARE_MEDIA) {
            loadingView.setMessage("正在分享...")
            loadingView.show()

        }

        override fun onResult(share_media: SHARE_MEDIA) {

            loadingView.dismiss()
            ToastUtils.showCenterToast(mContext, "发送成功")
            //            RxSPTool.putBoolean(mContext, SpConstant.SHARE_SUCCESS, true);

            if (mShareInfo == null)
                mShareInfo = ShareInfoHelper.shareInfo

            mShareInfo?.let {
                if (mIsShareMoney) {
                    //                    mPresenter.shareMoney();
                } else {
//                    if (!TextUtils.isEmpty(mShareInfo?.task_id)) {
                    mPresenter.share(mShareInfo?.task_id)
//                    }
                }
            }

        }

        override fun onError(share_media: SHARE_MEDIA, throwable: Throwable) {
            ObservableManager.get().notifyMyObserver("share_fail")
            loadingView.dismiss()
            ToastUtils.showCenterToast(mContext, "分享有误")
        }

        override fun onCancel(share_media: SHARE_MEDIA) {
            ObservableManager.get().notifyMyObserver("share_fail")
            loadingView.dismiss()
            ToastUtils.showCenterToast(mContext, "取消发送")
        }
    }


    fun setIsShareMoney(isShareMoney: Boolean) {
        this.mIsShareMoney = isShareMoney
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_share
    }

    override fun init() {
        mPresenter = SharePresenter(activity as BaseActivity<*>, this)
        RxView.clicks(tvCancelShare).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { dismiss() }
        val shareItemViews = ArrayList<View>()
        shareItemViews.add(llWx)
        shareItemViews.add(llCircle)
        shareItemViews.add(llQq)

        for (i in shareItemViews.indices) {
            val view = shareItemViews[i]
            view.tag = i
            RxView.clicks(view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { shareInfo(i) }
        }

    }

    private fun getShareMedia(tag: String): SHARE_MEDIA {
        if (tag == "0") {
            return SHARE_MEDIA.WEIXIN
        }

        if (tag == "1") {
            return SHARE_MEDIA.WEIXIN_CIRCLE
        }

        return if (tag == "2") {
            SHARE_MEDIA.QQ
        } else SHARE_MEDIA.WEIXIN

    }


    private fun shareInfo(tag: Int) {
        val shareInfo = ShareInfoHelper.shareInfo

        var title: String? = shareInfo?.title
        var url: String? = shareInfo?.url
        var desc: String? = shareInfo?.content
        var bitmap: Bitmap? = null

        mShareInfo?.let {
            if (!TextUtils.isEmpty(mShareInfo?.url)) {
                url = mShareInfo?.url
            }
            if (!TextUtils.isEmpty(mShareInfo?.title)) {
                title = mShareInfo?.title
            }
            if (!TextUtils.isEmpty(mShareInfo?.content)) {
                desc = mShareInfo?.content
            }

            bitmap = mShareInfo?.bitmap

        }

        dismiss()

        if (bitmap != null) {
            UMShareImpl.get().setCallback(mContext, umShareListener).shareImage("app", bitmap, bitmap, getShareMedia(tag.toString() + ""))
        } else {
            UMShareImpl.get().setCallback(mContext, umShareListener).shareUrl(title, url, desc, R.mipmap.ic_launcher, getShareMedia(tag.toString() + ""))
        }
    }


    override fun showSuccess() {
//        val userInfo = UserInfoHelper.getUserInfo()
//        userInfo.has_vip = 1
//        UserInfoHelper.saveUserInfo(userInfo)
        ObservableManager.get().notifyMyObserver("share_success")

    }


    fun getmShareInfo(): ShareInfo? {
        return mShareInfo
    }

    fun setShareInfo(mShareInfo: ShareInfo) {
        this.mShareInfo = mShareInfo
    }
}
