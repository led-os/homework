package yc.com.homework.mine.activity

import android.support.v4.content.ContextCompat
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_modify_pwd.*
import yc.com.base.BaseActivity
import yc.com.base.StatusBarUtil
import yc.com.homework.R
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.mine.contract.UserInfoContract
import yc.com.homework.mine.presenter.UserInfoPresenter
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/26 18:51.
 */
class ModifyPwdActivity : BaseActivity<UserInfoPresenter>(), UserInfoContract.View {



    override fun init() {
        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle(getString(R.string.modify_pwd))


        et_phone.setText(UserInfoHelper.getUserInfo()?.tel)

        UserInfoHelper.getUserInfo()?.tel?.length?.let { et_phone.setSelection(it) }

        mPresenter = UserInfoPresenter(this, this)

        initListener()
    }

    private fun initListener() {
        RxView.clicks(tv_get_code).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val phone = et_phone.text.toString().trim()

            mPresenter.getCode(phone)
        }

        RxView.clicks(tv_confirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val phone = et_phone.text.toString().trim()
            val code = et_code.text.toString().trim()
            val newPwd = et_new_pwd.text.toString().trim()
            val repeatPwd = et_repeat_pwd.text.toString().trim()

            mPresenter.modifyPwd(phone, code, newPwd, repeatPwd)

        }

    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_modify_pwd
    }

    override fun showLoginSuccess() {
    }

    override fun getCode() {
        showGetCodeDisplay(tv_get_code, ContextCompat.getColor(this, R.color.white), R.drawable.bind_get_code_bg, ContextCompat.getColor(this, R.color.white), R.drawable.recharge_pay_bg)
    }

    override fun showMobileError() {
    }

    override fun showCodeError() {
    }

    override fun showPwdError() {
    }

    override fun showEnd() {
    }
    override fun showLoginFail() {
    }
}