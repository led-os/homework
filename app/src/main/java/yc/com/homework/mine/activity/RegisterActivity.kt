package yc.com.homework.mine.activity

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.text.TextUtils
import android.view.View
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_register.*
import yc.com.base.BaseActivity
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.R
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.utils.AnimationUtil
import yc.com.homework.base.utils.Preference
import yc.com.homework.mine.contract.UserInfoContract
import yc.com.homework.mine.presenter.UserInfoPresenter
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/26 20:00.
 */
class RegisterActivity : BaseActivity<UserInfoPresenter>(), UserInfoContract.View {


    private val phone by Preference(this, SpConstant.phone, "")

    override fun getLayoutId(): Int {
        return R.layout.activity_register
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun init() {
        mPresenter = UserInfoPresenter(this, this)

//        val phone = SPUtils.getInstance().getString(SpConstant.phone)
        if (!TextUtils.isEmpty(phone)) {
            et_phone.setText(phone)
            et_phone.setSelection(phone.length)
        }


        initListener()


    }

    private fun initListener() {
        RxView.clicks(tv_invate_code).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            ll_invate_code.visibility = View.VISIBLE
            tv_invate_code.visibility = View.GONE
        }

        RxView.clicks(tv_login).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        RxView.clicks(tv_register).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val phone = et_phone.text.toString().trim()
            val code = et_code.text.toString().trim()
            val pwd = et_pwd.text.toString().trim()
            var invateCode: String? = null
            if (ll_invate_code.visibility == View.VISIBLE) {
                invateCode = et_invate_code.text.toString().trim()
            }
            mPresenter.register(phone, pwd, code, invite_code = invateCode)


        }

        RxView.clicks(tv_get_code).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val mobile = et_phone.text.toString().trim()
            mPresenter.getCode(mobile)

        }

        RxView.clicks(iv_invate_icon).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            ll_invate_code.visibility = View.GONE
            tv_invate_code.visibility = View.VISIBLE
        }
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