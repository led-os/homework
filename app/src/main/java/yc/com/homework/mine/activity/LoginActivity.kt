package yc.com.homework.mine.activity

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.text.TextUtils
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.LogUtil
import com.umeng.socialize.UMShareAPI
import com.umeng.socialize.bean.SHARE_MEDIA
import kotlinx.android.synthetic.main.activity_login.*
import yc.com.base.BaseActivity
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.R
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.user.UserLoginManager
import yc.com.homework.base.utils.AnimationUtil
import yc.com.base.ObservableManager
import yc.com.homework.base.config.Constant
import yc.com.homework.base.utils.Preference
import yc.com.homework.mine.contract.UserInfoContract
import yc.com.homework.mine.presenter.UserInfoPresenter
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/26 19:16.
 */
class LoginActivity : BaseActivity<UserInfoPresenter>(), Observer, UserInfoContract.View {


    private var manager: UserLoginManager? = null
    private var phone by Preference(this, SpConstant.phone, "")

    override fun init() {

        mPresenter = UserInfoPresenter(this, this)
        ObservableManager.get()!!.addMyObserver(this)
        tv_forget_pwd.paint.flags = Paint.UNDERLINE_TEXT_FLAG
        tv_forget_pwd.paint.isAntiAlias = true//抗锯齿
//        val phone = SPUtils.getInstance().getString(SpConstant.phone)
        et_phone.setText(phone)

        manager = UserLoginManager.get()

        initListener()
    }


    private fun initListener() {
        RxView.clicks(tv_register).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        RxView.clicks(tv_forget_pwd).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, ForgetPwdActivity::class.java)
            startActivity(intent)
        }

        RxView.clicks(iv_qq).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            manager?.setCallBack(this) {
                LogUtil.msg("userinfo:  ${it.accessToken}--${it.openid}")

                mPresenter.thridLogin(it!!.accessToken, 3, it.openid)
            }?.oauthAndLogin(SHARE_MEDIA.QQ)
        }

        RxView.clicks(iv_wx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            manager?.setCallBack(this) {

                mPresenter.thridLogin(it!!.accessToken, 4, it.openid)
            }?.oauthAndLogin(SHARE_MEDIA.WEIXIN)
        }

        RxView.clicks(tv_login).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val phone = et_phone.text.toString().trim()
            val pwd = et_pwd.text.toString().trim()

            mPresenter.login(phone, pwd)
        }


    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_login
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        UMShareAPI.get(this).onSaveInstanceState(outState)
    }

    override fun update(o: Observable?, arg: Any?) {
        when (arg) {
            is String -> {
                if (TextUtils.equals(arg, Constant.FINISH)) {
                    finish()
                }
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get().deleteMyObserver(this)
        UMShareAPI.get(this).release()
    }


    override fun showLoginSuccess() {
        finish()
    }

    override fun showEnd() {
        manager?.closeProgressDialog()
    }

    override fun showMobileError() {
        AnimationUtil.startShakeAnim(this, ll_phone)
    }

    override fun showCodeError() {
    }

    override fun showPwdError() {
        AnimationUtil.startShakeAnim(this, ll_pwd)
    }

    override fun getCode() {
    }

    override fun showLoginFail() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}