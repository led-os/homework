package yc.com.homework.mine.activity

import kotlinx.android.synthetic.main.activity_protocol.*
import yc.com.base.*
import yc.com.homework.R

/**
 *
 * Created by wanglin  on 2018/11/26 11:02.
 */
class ProtocolActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {


    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_protocol
    }
    override fun init() {
        StatusBarUtil.setStatusTextColor1(true,this)
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle(getString(R.string.cooperation_protocol))
    }
}