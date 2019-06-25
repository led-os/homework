package yc.com.homework.mine.activity

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.jakewharton.rxbinding.view.RxView
import com.umeng.socialize.UMShareAPI
import kotlinx.android.synthetic.main.activity_recharge.*
import yc.com.base.BaseActivity
import yc.com.base.StatusBarUtil
import yc.com.homework.R
import yc.com.homework.base.config.Constant
import yc.com.homework.base.user.UserInfoHelper
import yc.com.base.ObservableManager
import yc.com.homework.base.utils.AnimationUtil
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.mine.adapter.RechargeItemAdapter
import yc.com.homework.mine.contract.RechargeContract
import yc.com.homework.mine.domain.bean.RechargeItemInfo
import yc.com.homework.mine.domain.bean.ShareInfo
import yc.com.homework.mine.fragment.ShareFragment
import yc.com.homework.mine.presenter.RechargePresenter
import yc.com.homework.mine.utils.ImageUtils
import yc.com.homework.pay.IAliPay1Impl
import yc.com.homework.pay.IPayCallback
import yc.com.homework.pay.IWXPay1Impl
import yc.com.homework.pay.OrderInfo
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/24 10:27.
 */
class RechargeActivity : BaseActivity<RechargePresenter>(), RechargeContract.View {

    private var rechargeAdapter: RechargeItemAdapter? = null
    private var iAliPay1Impl: IAliPay1Impl? = null
    private var iWxPay1Impl: IWXPay1Impl? = null
    private var mBitmap: Bitmap? = null

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_recharge
    }

    override fun init() {

        StatusBarUtil.setStatusTextColor1(true, this)//白色背景，黑色字体
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle(getString(R.string.recharge))

        mPresenter = RechargePresenter(this, this)

        recharge_recyclerView.layoutManager = LinearLayoutManager(this)
        rechargeAdapter = RechargeItemAdapter(null)

        recharge_recyclerView.adapter = rechargeAdapter
        recharge_recyclerView.addItemDecoration(ItemDecorationHelper(this, 10))
        ic_selected_zfb.isSelected = true
        initListener()
        iAliPay1Impl = IAliPay1Impl(this)
        iWxPay1Impl = IWXPay1Impl(this)
    }

    private var mPayway: String = Constant.ALI_PAY
    private var currentInfo: RechargeItemInfo? = null

    private fun initListener() {
        RxView.clicks(btn_zfb).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            resetPaywayState()
            ic_selected_zfb.isSelected = true
            mPayway = Constant.ALI_PAY
        }
        RxView.clicks(btn_wx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            resetPaywayState()
            ic_selected_wx.isSelected = true
            mPayway = Constant.WX_PAY
        }

        RxView.clicks(tv_recharge).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (currentInfo == null) return@subscribe
            mPresenter.createOrder(1, mPayway, "${currentInfo?.money}", currentInfo?.id, currentInfo?.name)
        }

        RxView.clicks(tv_invate).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val shareFragment = ShareFragment()

            val shareInfo = ShareInfo()
            val originBitmap = BitmapFactory.decodeResource(resources, R.mipmap.share_code_bg)
            val codeBitmap = BitmapFactory.decodeResource(resources, R.mipmap.download_code)
            val userInfo = UserInfoHelper.getUserInfo()
            val avatorBitmap: Bitmap? = if (TextUtils.isEmpty(userInfo.face)) {
                ImageUtils.compressBitmap(BitmapFactory.decodeResource(resources, R.mipmap.deafult_avator))
            } else {
                mBitmap
            }

            val newBitmap = ImageUtils.compoundPic(originBitmap, avatorBitmap, codeBitmap, Environment.getExternalStorageDirectory().absolutePath, userInfo.inviteCode)
            shareInfo.bitmap = newBitmap
            shareFragment.setShareInfo(shareInfo)
            shareFragment.show(supportFragmentManager, "")
        }

        rechargeAdapter?.setOnItemClickListener { adapter, view, position ->
            currentInfo = rechargeAdapter?.getItem(position)
            resetState()
            view.isSelected = true
        }


    }

    override fun showRechargeInfos(t: List<RechargeItemInfo>?) {
        rechargeAdapter?.setNewData(t)

        AnimationUtil.startTextAnim("${UserInfoHelper.getUserInfo().coin}", tv_my_coin)
//        tv_my_coin.text = "${UserInfoHelper.getUserInfo().coin}"
        if (t!!.isNotEmpty())
            currentInfo = t[0]
    }

    private fun resetPaywayState() {
        ic_selected_zfb.isSelected = false
        ic_selected_wx.isSelected = false
    }

    override fun showOrderInfo(orderInfo: OrderInfo?) {
        if (TextUtils.equals(mPayway, Constant.ALI_PAY))
            aliPay(orderInfo)
        else {
            wxPay(orderInfo)
        }
    }


    private fun aliPay(orderInfo: OrderInfo?) {
        iAliPay1Impl?.pay(orderInfo, iPayCallback)
    }

    private fun wxPay(orderInfo: OrderInfo?) {
        iWxPay1Impl?.pay(orderInfo, iPayCallback)
    }

    private val iPayCallback = object : IPayCallback {
        override fun onSuccess(orderInfo: OrderInfo) {
            UserInfoHelper.getNewUserInfo(this@RechargeActivity)
            finish()
        }

        override fun onFailure(orderInfo: OrderInfo) {

        }
    }

    override fun showBitmap(bitmap: Bitmap) {
        this.mBitmap = bitmap
    }

    override fun showLoading() {
        stateView.showLoading(ll_content)
    }

    override fun showNoData() {
        stateView.showNoData(ll_content)
    }

    override fun showNoNet() {
        stateView.showNoNet(ll_content) {
            mPresenter.getRechargeItemInfos("5")
        }
    }

    override fun hide() {
        stateView.hide()
    }


    private fun resetState() {
        recharge_recyclerView?.let {
            for (i in 0 until recharge_recyclerView!!.childCount) {
                recharge_recyclerView?.getChildAt(i)?.isSelected = false
            }
        }


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }


}