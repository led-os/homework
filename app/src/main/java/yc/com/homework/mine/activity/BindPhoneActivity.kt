package yc.com.homework.mine.activity

import android.support.v4.content.ContextCompat
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_bindphone.*
import yc.com.base.BaseActivity
import yc.com.base.StatusBarUtil
import yc.com.homework.R
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.contract.UserInfoContract
import yc.com.homework.mine.presenter.UserInfoPresenter
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/26 13:46.
 */
class BindPhoneActivity : BaseActivity<UserInfoPresenter>(), UserInfoContract.View {



    override fun getLayoutId(): Int {
        return R.layout.activity_bindphone
    }

    override fun init() {

        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle(getString(R.string.bind_phone))

        mPresenter = UserInfoPresenter(this, this)

        RxView.clicks(tv_bind_phone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val phone = et_phone.text.toString().trim()
            val code = et_code.text.toString().trim()

            mPresenter.bindPhone(UserInfoHelper.getUid(), phone, code)


        }
        RxView.clicks(tv_get_code).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val phone = et_phone.text.toString().trim()
            mPresenter.getCode(phone)
        }


    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getCode() {
        showGetCodeDisplay(tv_get_code, ContextCompat.getColor(this, R.color.white), R.drawable.bind_get_code_bg, ContextCompat.getColor(this, R.color.app_selected_color), R.drawable.recharge_pay_bg)
    }

    override fun showMobileError() {
    }

    override fun showCodeError() {
    }

    override fun showPwdError() {
    }

    override fun showLoginSuccess() {
    }

    override fun showEnd() {
    }
    override fun showLoginFail() {

    }
}