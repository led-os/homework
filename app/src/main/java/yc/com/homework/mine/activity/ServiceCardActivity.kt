package yc.com.homework.mine.activity

import android.content.Intent
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_service_card.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.base.user.UserInfoHelper
import java.util.concurrent.TimeUnit


/**
 *
 * Created by wanglin  on 2019/1/22 09:41.
 */
class ServiceCardActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {
    override fun init() {
        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle(getString(R.string.service_card))
        initListener()
    }

    fun initListener() {


        RxView.clicks(iv_experience_card).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            switchRecharge()
        }
        RxView.clicks(iv_month_card).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            switchRecharge()

        }
        RxView.clicks(iv_year_card).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            switchRecharge()
        }
    }

    private fun switchRecharge() {
        if (!UserInfoHelper.isGotoLogin(this)) {
            val intent = Intent(this, RechargeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_service_card
    }
}