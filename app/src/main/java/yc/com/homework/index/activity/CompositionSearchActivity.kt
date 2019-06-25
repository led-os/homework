package yc.com.homework.index.activity

import android.os.Bundle
import android.support.v4.app.Fragment
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
 */
class CompositionSearchActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {


    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_composition_search

    }

    override fun init() {
        StatusBarUtil.setStatusTextColor1(true, this)
        replaceFragment(SearchRecordFragment(), "")
        initListener()
    }

    fun initListener() {
        RxView.clicks(iv_back).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { finish() }
        RxView.clicks(tv_search).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val title = et_title.text.toString().trim()
            val resultFragment = SearchResultFragment()

            replaceFragment(resultFragment, title)
            RxKeyboardTool.hideSoftInput(this@CompositionSearchActivity)
        }
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