package yc.com.homework.mine.fragment

import android.graphics.Bitmap
import android.text.TextUtils
import android.view.ViewGroup
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.fragment_pay.*
import yc.com.base.BaseActivity
import yc.com.base.BaseDialogFragment
import yc.com.homework.R
import yc.com.homework.base.config.Constant
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.contract.RechargeContract
import yc.com.homework.mine.domain.bean.RechargeItemInfo
import yc.com.homework.mine.presenter.RechargePresenter
import yc.com.homework.pay.IAliPay1Impl
import yc.com.homework.pay.IPayCallback
import yc.com.homework.pay.IWXPay1Impl
import yc.com.homework.pay.OrderInfo
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/4/30 16:56.
 */
class PayFragment : BaseDialogFragment<RechargePresenter>(), RechargeContract.View {


    private lateinit var iAliPay1Impl: IAliPay1Impl
    private lateinit var iWxPay1Impl: IWXPay1Impl
    private var mPayWay: String = Constant.WX_PAY
    private var rechargeItemInfo: RechargeItemInfo? = null
    override fun getWidth(): Float {
        return 0.75f
    }

    override fun getAnimationId(): Int {
        return R.style.share_anim
    }

    override fun getHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_pay
    }

    override fun init() {
        mPresenter = RechargePresenter(activity as BaseActivity<*>, this)
        iAliPay1Impl = IAliPay1Impl(activity)
        iWxPay1Impl = IWXPay1Impl(activity)
        iv_wx_pay.isSelected = true

        initListener()

    }

    fun initListener() {
        RxView.clicks(iv_close).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { dismiss() }
        RxView.clicks(iv_wx_pay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            resetPayWay()
            if (!iv_wx_pay.isSelected)
                iv_wx_pay.isSelected = true
            mPayWay = Constant.WX_PAY
        }
        RxView.clicks(iv_ali_pay).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            resetPayWay()
            if (!iv_ali_pay.isSelected)
                iv_ali_pay.isSelected = true
            mPayWay = Constant.ALI_PAY
        }
        RxView.clicks(rl_pay_btn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (rechargeItemInfo == null) return@subscribe
            mPresenter.createOrder(1, mPayWay, "${rechargeItemInfo?.money}", rechargeItemInfo?.id, rechargeItemInfo?.name)
        }

    }

    private fun resetPayWay() {
        iv_wx_pay.isSelected = false
        iv_ali_pay.isSelected = false
    }

    override fun showRechargeInfos(t: List<RechargeItemInfo>?) {
        t?.let {
            rechargeItemInfo = t[t.size - 1]
            tv_order_money.text = "¥ ${rechargeItemInfo?.money}"
        }

    }

    override fun showOrderInfo(orderInfo: OrderInfo?) {
        if (TextUtils.equals(mPayWay, Constant.ALI_PAY))
            aliPay(orderInfo)
        else {
            wxPay(orderInfo)
        }
    }

    private fun aliPay(orderInfo: OrderInfo?) {
        iAliPay1Impl.pay(orderInfo, iPayCallback)
    }

    private fun wxPay(orderInfo: OrderInfo?) {
        iWxPay1Impl.pay(orderInfo, iPayCallback)
    }

    private val iPayCallback = object : IPayCallback {
        override fun onSuccess(orderInfo: OrderInfo) {
//            UserInfoHelper.getNewUserInfo(activity)
            val userInfo = UserInfoHelper.getUserInfo()
            userInfo.has_vip = 1
            UserInfoHelper.saveUserInfo(userInfo)

            dismiss()
        }

        override fun onFailure(orderInfo: OrderInfo) {

        }
    }

    override fun showBitmap(bitmap: Bitmap) {
    }

    override fun showLoadingDialog(mess: String?) {
        (activity as BaseActivity<*>).showLoadingDialog(mess)
    }

    override fun dismissDialog() {
        (activity as BaseActivity<*>).dismissDialog()
    }

    override fun showLoading() {
    }

    override fun showNoData() {
    }

    override fun showNoNet() {
    }

    override fun hide() {
    }
}