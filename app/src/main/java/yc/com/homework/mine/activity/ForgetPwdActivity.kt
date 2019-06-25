package yc.com.homework.mine.activity

import android.content.Intent
import android.support.v4.content.ContextCompat
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_forget_pwd.*


import yc.com.base.BaseActivity
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.R
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.AnimationUtil
import yc.com.homework.base.utils.Preference
import yc.com.homework.mine.contract.UserInfoContract
import yc.com.homework.mine.presenter.UserInfoPresenter
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/26 20:28.
 */
class ForgetPwdActivity : BaseActivity<UserInfoPresenter>(), UserInfoContract.View {

    private var mobile by Preference(this, SpConstant.phone, "")

    override fun init() {
//        val mobile = SPUtils.getInstance().getString(SpConstant.phone, "")
        et_phone.setText(mobile)
        et_phone.setSelection(mobile.length)

        mPresenter = UserInfoPresenter(this, this)

        RxView.clicks(tv_register).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        RxView.clicks(tv_confirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            val phone = et_phone.text.toString().trim()
            val code = et_code.text.toString().trim()
            val pwd = et_pwd.text.toString().trim()

            mPresenter.forgetPwd(phone, code, pwd)
        }
        RxView.clicks(tv_get_code).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val phone = et_phone.text.toString().trim()
            mPresenter.getCode(phone)
        }
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_forget_pwd
    }

    override fun showMobileError() {
        AnimationUtil.startShakeAnim(this, ll_phone)
    }

    override fun showCodeError() {
        AnimationUtil.startShakeAnim(this, ll_code)
    }

    override fun showPwdError() {
        AnimationUtil.startShakeAnim(this, ll_pwd)
    }

    override fun getCode() {
        showGetCodeDisplay(tv_get_code, ContextCompat.getColor(this, R.color.white), R.drawable.get_code_bg, ContextCompat.getColor(this, R.color.app_selected_color), R.drawable.login_white_bg)
    }

    override fun showLoginSuccess() {

    }

    override fun showEnd() {

    }

    override fun showLoginFail() {
    }

}