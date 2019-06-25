package yc.com.homework.mine.presenter

import android.content.Context
import android.text.TextUtils
import com.kk.securityhttp.domain.ResultInfo
import com.kk.securityhttp.net.contains.HttpConfig
import rx.Subscriber
import yc.com.base.BasePresenter
import yc.com.blankj.utilcode.util.RegexUtils
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.user.UserInfo

import yc.com.homework.base.user.UserInfoHelper
import yc.com.base.ObservableManager
import yc.com.homework.base.config.Constant
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.base.utils.EngineUtils
import yc.com.homework.base.utils.Preference
import yc.com.homework.mine.contract.UserInfoContract
import yc.com.homework.mine.domain.engine.UserInfoEngine

/**
 *
 * Created by wanglin  on 2018/11/26 13:47.
 * 用户账号相关
 */
class UserInfoPresenter(context: Context, view: UserInfoContract.View) : BasePresenter<UserInfoEngine, UserInfoContract.View>(context, view), UserInfoContract.Presenter {

    private var savePhone by Preference(context, SpConstant.phone, "")

    private var savePwd by Preference(context, SpConstant.pwd, "")

    init {
        mEngine = UserInfoEngine(context)
    }

    override fun loadData(isForceUI: Boolean, isLoadingUI: Boolean) {
        if (!isForceUI) return
    }


    override fun register(mobile: String, password: String, code: String, invite_code: String?) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号码不正确")
            return
        }
        if (TextUtils.isEmpty(code)) {
            mView.showCodeError()
            ToastUtils.showCenterToast(mContext, "验证码不能为空")
            return
        }

        if (TextUtils.isEmpty(password)) {
            mView.showPwdError()
            ToastUtils.showCenterToast(mContext, "密码不能为空")
            return
        }

        mView.showLoadingDialog("正在注册，请稍候...")

        val subscription = mEngine.register(mobile, password, code, invite_code).subscribe(object : Subscriber<ResultInfo<UserInfo>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<UserInfo>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val userInfo = t.data

                        UserInfoHelper.saveUserInfo(userInfo)
                        val tel = userInfo.tel
                        tel?.let {
                            //                            SPUtils.getInstance().put(SpConstant.phone, it)
                            savePhone = it
                        }
                        savePwd = userInfo.pwd
//                        SPUtils.getInstance().put(SpConstant.pwd, userInfo.pwd)
                        getNewUserInfo(mContext)
                        ObservableManager.get().notifyMyObserver(Constant.FINISH)
//                        UserInfoHelper.setAlias(mContext)
                        mView.finish()

                    } else {
                        ToastUtils.showCenterToast(mContext, t.message)
                    }
                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR)
                }

            }

        })
        mSubscriptions.add(subscription)
    }


    override fun modifyPwd(mobile: String, code: String, new_password: String, repeat_pwd: String) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号码不正确")
            return
        }
        if (TextUtils.isEmpty(code)) {
            mView.showCodeError()
            ToastUtils.showCenterToast(mContext, "验证码不能为空")
            return
        }

        if (TextUtils.isEmpty(new_password)) {
            ToastUtils.showCenterToast(mContext, "新密码不能为空")
            return
        }
        if (TextUtils.isEmpty(repeat_pwd)) {
            ToastUtils.showCenterToast(mContext, "重复密码不能为空")
            return
        }
        if (!TextUtils.equals(new_password, repeat_pwd)) {
            ToastUtils.showCenterToast(mContext, "两次密码不一致")
            return
        }

        mView.showLoadingDialog("正在修改密码，请稍候...")
        val subscription = mEngine.modifyPwd(mobile, code, new_password).subscribe(object : Subscriber<ResultInfo<String>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<String>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
//                        SPUtils.getInstance().put(SpConstant.pwd, new_password)
                        savePwd = new_password
                        ToastUtils.showCenterToast(mContext, "密码修改成功")
                        mView.finish()
                    } else {
                        ToastUtils.showCenterToast(mContext, t.message)
                    }
                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR)
                }
            }
        })
        mSubscriptions.add(subscription)

    }


    fun forgetPwd(mobile: String, code: String, new_password: String) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号码不正确")
            return
        }
        if (TextUtils.isEmpty(code)) {
            mView.showCodeError()
            ToastUtils.showCenterToast(mContext, "验证码不能为空")
            return
        }

        if (TextUtils.isEmpty(new_password)) {
            ToastUtils.showCenterToast(mContext, "新密码不能为空")
            return
        }

        mView.showLoadingDialog("正在重置密码，请稍候...")
        val subscription = mEngine.modifyPwd(mobile, code, new_password).subscribe(object : Subscriber<ResultInfo<String>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<String>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
//                        SPUtils.getInstance().put(SpConstant.pwd, new_password)
                        savePwd= new_password
                        ToastUtils.showCenterToast(mContext, "密码修改成功")
                        mView.finish()
                    } else {
                        ToastUtils.showCenterToast(mContext, t.message)
                    }
                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR)
                }
            }
        })
        mSubscriptions.add(subscription)

    }

    override fun bindPhone(user_id: String, mobile: String?, code: String?) {
        if (TextUtils.isEmpty(mobile)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(mobile)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号码不正确")
            return
        }
        if (TextUtils.isEmpty(code)) {
            mView.showCodeError()
            ToastUtils.showCenterToast(mContext, "验证码不能为空")
            return
        }

        mView.showLoadingDialog("正在绑定手机号，请稍候...")
        val subscription = mEngine.bindPhone(user_id, mobile, code).subscribe(object : Subscriber<ResultInfo<UserInfo>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<UserInfo>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val userInfo = t.data
                        UserInfoHelper.saveUserInfo(userInfo)
                        getNewUserInfo(mContext)
                        mView.finish()
                    } else {
                        ToastUtils.showCenterToast(mContext, t.message)
                    }
                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR)
                }
            }
        })

        mSubscriptions.add(subscription)
    }

    override fun login(phone: String, pwd: String) {
        if (TextUtils.isEmpty(phone)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号码不正确")
            return
        }

        if (TextUtils.isEmpty(pwd)) {
            mView.showPwdError()
            ToastUtils.showCenterToast(mContext, "密码不能为空")
            return
        }

        mView.showLoadingDialog("正在登录，请稍候...")
        val subscription = EngineUtils.phoneLogin(mContext, phone, pwd).subscribe(object : Subscriber<ResultInfo<UserInfo>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.dismissDialog()
            }

            override fun onNext(t: ResultInfo<UserInfo>?) {
                mView.dismissDialog()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        val userInfo = t.data
                        val tel = userInfo.tel
                        tel?.let {
                            //                            SPUtils.getInstance().put(SpConstant.phone, it)
                            savePhone = it
                        }
//                        SPUtils.getInstance().put(SpConstant.pwd, pwd)
                        savePwd = pwd
                        UserInfoHelper.saveUserInfo(userInfo)
                        getNewUserInfo(mContext)

                        if (!TextUtils.isEmpty(userInfo.face)) ObservableManager.get().notifyMyObserver("face-${userInfo.face}")

                        mView.finish()
                    } else {
                        ToastUtils.showCenterToast(mContext, t.message)

                        if (t.message.contains("用户名不存在") || t.message.contains("注册")) {
//                            SPUtils.getInstance().put(SpConstant.phone, phone)
                            savePhone = phone
                            mView.showLoginFail()
                        }

                    }
                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR)
                }
            }
        })
        mSubscriptions.add(subscription)


    }


    fun getCode(phone: String) {
        if (TextUtils.isEmpty(phone)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号不能为空")
            return
        }
        if (!RegexUtils.isMobileExact(phone)) {
            mView.showMobileError()
            ToastUtils.showCenterToast(mContext, "手机号码不正确")
            return
        }
        mView.getCode()
        val subscription = mEngine.sendCode(phone).subscribe(object : Subscriber<ResultInfo<String>>() {


            override fun onCompleted() {
            }

            override fun onError(e: Throwable?) {
            }

            override fun onNext(t: ResultInfo<String>?) {
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK) {
                        ToastUtils.showCenterToast(mContext, "验证码已发送")
                    } else {
                        ToastUtils.showCenterToast(mContext, t.message)
                    }
                }
            }
        })

        mSubscriptions.add(subscription)

    }

    override fun thridLogin(access_token: String?, account_type: Int, openid: String?) {
        val subscription = mEngine.thridLogin(access_token, account_type, openid).subscribe(object : Subscriber<ResultInfo<UserInfo>>() {


            override fun onCompleted() {

            }

            override fun onError(e: Throwable?) {
                mView.showEnd()
            }

            override fun onNext(t: ResultInfo<UserInfo>?) {
                mView.showEnd()
                if (t != null) {
                    if (t.code == HttpConfig.STATUS_OK && t.data != null) {
                        ObservableManager.get()!!.notifyMyObserver(t.data)
                        mView.showLoginSuccess()
                        UserInfoHelper.saveUserInfo(t.data)
                        ObservableManager.get()!!.notifyMyObserver("face-${t.data.face}")
//                        UserInfoHelper.setAlias(mContext)
                    } else {
                        ToastUtils.showBottomToast(mContext, t.message)
                    }
                } else {
                    ToastUtils.showCenterToast(mContext, HttpConfig.NET_ERROR)
                }
            }
        })

        mSubscriptions.add(subscription)
    }

    fun getNewUserInfo(context: Context) {
        EngineUtils.getUserInfo(context).subscribe(object : Subscriber<ResultInfo<UserInfo>>() {
            override fun onCompleted() {

            }

            override fun onError(e: Throwable) {

            }

            override fun onNext(userInfoResultInfo: ResultInfo<UserInfo>?) {
                if (userInfoResultInfo != null && userInfoResultInfo.code == HttpConfig.STATUS_OK && userInfoResultInfo.data != null) {
                    val userInfo = userInfoResultInfo.data
                    UserInfoHelper.saveUserInfo(userInfo)
                    ObservableManager.get().notifyMyObserver(userInfo)

                }
            }
        })
    }

}