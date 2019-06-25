package yc.com.homework.mine.contract

import yc.com.base.IDialog
import yc.com.base.IFinish
import yc.com.base.IPresenter
import yc.com.base.IView

/**
 *
 * Created by wanglin  on 2018/11/26 13:50.
 */
interface UserInfoContract {

    interface View : IView, IDialog, IFinish {
        fun showMobileError()
        fun showCodeError()
        fun showPwdError()
        fun getCode()//获取验证码
        fun showLoginSuccess()
        fun showEnd()
        fun showLoginFail()
    }

    interface Presenter : IPresenter {
        fun register(mobile: String, password: String, code: String, invite_code: String?)

        fun login(phone: String, pwd: String)
        fun modifyPwd(mobile: String, code: String, new_password: String, repeat_pwd: String)
        fun bindPhone(user_id: String, mobile: String?, code: String?)

        fun thridLogin(access_token: String?, account_type: Int, openid: String?)
    }
}