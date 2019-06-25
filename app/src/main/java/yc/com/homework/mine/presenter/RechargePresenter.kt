package yc.com.homework.mine.presenter

import android.content.Context
import android.graphics.Bitmap
import android.media.Image
import android.text.TextUtils
import com.bumptech.glide.Glide
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.base.ObservableManager
import yc.com.homework.mine.contract.RechargeContract
import yc.com.homework.mine.domain.bean.RechargeItemInfo
import yc.com.homework.mine.domain.engine.RechargeEngine
import yc.com.homework.mine.utils.GlideCircleTransformation
import yc.com.homework.mine.utils.ImageUtils
import yc.com.homework.pay.OrderInfo

/**
 *
 * Created by wanglin  on 2018/11/24 12:00.
 */
class RechargePresenter(context: Context, view: RechargeContract.View) : BasePresenter<RechargeEngine, RechargeContract.View>(context, view), RechargeContract.Presenter {


    init {
        mEngine = RechargeEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
//        covertPathToBitmap(UserInfoHelper.getUserInfo().face)
        getRechargeItemInfos("5")
    }

    override fun getRechargeItemInfos(type_id: String) {
        mView.showLoading()
        val subscription = mEngine.getRechargeItemInfos(type_id).subscribe(object : Subscriber<ResultInfo<ArrayList<RechargeItemInfo>>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.showNoNet()
            }

            override fun onNext(t: ResultInfo<ArrayList<RechargeItemInfo>>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.isNotEmpty()) {
                        mView.hide()
                        mView.showRechargeInfos(t.data)
                    } else {
                        mView.showNoData()
                    }
                } else {
                    mView.showNoNet()
                }
            }
        })
        mSubscriptions.add(subscription)
    }

    override fun createOrder(goods_num: Int?, payway_name: String?, money: String?, goods_id: String?, title: String?) {
        mView.showLoadingDialog("正在创建订单，请稍候...")
        val subscription = mEngine.createOrder(goods_num, payway_name, money, goods_id, title).subscribe(object : Subscriber<ResultInfo<OrderInfo>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {
                mView.dismissDialog()
            }

            override fun onNext(orderInfoResultInfo: ResultInfo<OrderInfo>?) {
                mView.dismissDialog()
                if (orderInfoResultInfo != null && orderInfoResultInfo.code == HttpConfig.STATUS_OK && orderInfoResultInfo.data != null) {
                    val orderInfo = orderInfoResultInfo.data
                    orderInfo.money = money?.toFloat()
                    orderInfo.name = title
                    orderInfo.goodId = goods_id
                    mView.showOrderInfo(orderInfo)
                }

            }
        })
        mSubscriptions.add(subscription)
    }

    private fun covertPathToBitmap(path: String?) {
        if (TextUtils.isEmpty(path)) return
        try {
            val request = Glide.with(mContext).load(path).asBitmap()
            val transformation = GlideCircleTransformation(mContext)
            request.transform(transformation).into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                    val result = ImageUtils.compressBitmap( bitmap)
                    mView.showBitmap(result)
                }
            })
        } catch (e: Exception) {
            e.printStackTrace()

        }

    }
}