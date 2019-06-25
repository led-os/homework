package yc.com.homework.mine.activity

import android.content.Intent
import android.text.TextUtils
import android.view.View
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.LogUtil
import com.tencent.bugly.beta.Beta
import kotlinx.android.synthetic.main.activity_setting.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.base.ObservableManager
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.mine.contract.SettingContract
import yc.com.homework.mine.fragment.EditInfoFragment
import yc.com.homework.mine.fragment.PhotoSelectFragment
import yc.com.homework.mine.presenter.SettingPresenter
import yc.com.homework.mine.utils.AvatarHelper
import yc.com.homework.mine.utils.PhoneUtils
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by wanglin  on 2018/11/24 12:54.
 */
class SettingActivity : BaseActivity<SettingPresenter>(), SettingContract.View, Observer {


    override fun init() {
        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle(getString(R.string.setting))
        ObservableManager.get().addMyObserver(this)

        mPresenter = SettingPresenter(this, this)

        val versionName = packageManager.getPackageInfo(packageName, 0).versionName
        baseSettingView_version_update.extraText = String.format(getString(R.string.current_version), versionName)

        setUserInfo(UserInfoHelper.getUserInfo())

        initListener()

    }

    private fun initListener() {
        RxView.clicks(baseSettingView_phone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, BindPhoneActivity::class.java)
            startActivity(intent)
        }

        RxView.clicks(baseSettingView_modify_pwd).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, ModifyPwdActivity::class.java)
            startActivity(intent)
        }

        RxView.clicks(baseSettingView_clear_cache).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (mPresenter.clearCache()) {
                baseSettingView_clear_cache.extraText = "0KB"
                ToastUtils.showCenterToast(this@SettingActivity, "缓存清除成功")
            }
        }

        RxView.clicks(tv_exit_login).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            mPresenter.logout()
        }

        RxView.clicks(baseSettingView_nickName).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val editInfoFragment = EditInfoFragment()
            editInfoFragment.setShowList(false, getString(R.string.input_nickname))

            editInfoFragment.show(supportFragmentManager, "")
        }

        RxView.clicks(baseSettingView_phone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            val userInfo = UserInfoHelper.getUserInfo()

            if (TextUtils.isEmpty(userInfo?.tel)) {
                val intent = Intent(this@SettingActivity, BindPhoneActivity::class.java)
                startActivity(intent)
            } else {
                ToastUtils.showCenterToast(this@SettingActivity, "您已经绑定过手机，不能重复绑定")
            }

        }

        RxView.clicks(baseSettingView_avtor).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //更换图像
//            AvatarHelper.openAlbum(this@SettingActivity)
            val photoSelectFragment = PhotoSelectFragment()
            photoSelectFragment.show(supportFragmentManager, "")

        }

        RxView.clicks(baseSettingView_version_update).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            Beta.checkUpgrade(true, false)
        }
        RxView.clicks(baseSettingView_grade).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val editInfoFragment = EditInfoFragment()
            editInfoFragment.setShowList(true, getString(R.string.select_grade))

            editInfoFragment.show(supportFragmentManager, "")
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AvatarHelper.onActivityForResult(this@SettingActivity, requestCode, resultCode, data, object : AvatarHelper.IAvatar {
            override fun uploadAvatar(image: String) {
                mPresenter.uploadAvator(UserInfoHelper.getUid(), image)
            }
        })

    }


    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_setting
    }

    override fun showCacheSize(cacheSize: String?) {
        baseSettingView_clear_cache.extraText = cacheSize
    }

    override fun showLogout() {
        tv_exit_login.visibility = View.GONE
        finish()
    }

    private fun setUserInfo(userInfo: UserInfo?) {
        if (userInfo != null) {
            baseSettingView_avtor.setIvIcon(userInfo.face)
            baseSettingView_Id.extraText = userInfo.id
            baseSettingView_nickName.extraText = userInfo.nickname
            baseSettingView_grade.extraText = userInfo.getGrade()
            var tel: String? = userInfo.tel

            if (TextUtils.isEmpty(tel)) {
                tel = getString(R.string.go_bind)
                baseSettingView_phone.showRightArrow(true)
            } else {
                baseSettingView_phone.showRightArrow(false)
            }
            baseSettingView_phone.extraText = PhoneUtils.replacePhone(this, tel)

        }

    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg != null) {
            when (arg) {
                is UserInfo -> setUserInfo(arg)
                is String -> {
                    if (arg.startsWith("face")) {
                        LogUtil.msg("face--${arg.split("-")[1]}")
                        baseSettingView_avtor.setIvIcon(arg.split("-")[1])
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get().deleteMyObserver(this)
    }

}