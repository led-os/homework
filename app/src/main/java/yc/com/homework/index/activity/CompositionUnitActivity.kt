package yc.com.homework.index.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.jakewharton.rxbinding.view.RxView
import com.vondear.rxtools.RxKeyboardTool
import kotlinx.android.synthetic.main.activity_composition_search.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.index.fragment.SearchRecordFragment
import yc.com.homework.index.fragment.SearchResultFragment
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/3/29 08:52.
 * 单元作文
 */
class CompositionUnitActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {


    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_composition_search

    }

    override fun init() {
        ll_unit_composition.visibility = View.VISIBLE
        toolbar.visibility = View.GONE
        mainToolBar.setTitle("")
        mainToolBar.showNavigationIcon()
        mainToolBar.init(this)
        mainToolBar.setRightContainerVisible(false)
        replaceFragment(SearchResultFragment(), "")
//        initListener()
    }


    fun replaceFragment(fragment: Fragment, title: String?) {
        val bt = supportFragmentManager.beginTransaction()
        if (fragment is SearchResultFragment) {
//            divider.visibility = View.GONE
            val bundle = Bundle()
            bundle.putString("name", title)
            fragment.arguments = bundle
            toolbar.setBackgroundResource(R.drawable.toolbar_bg_color)
            StatusBarUtil.setStatusTextColor1(false, this)
        }
        bt.replace(R.id.fl_container, fragment)
        bt.commit()
    }
}