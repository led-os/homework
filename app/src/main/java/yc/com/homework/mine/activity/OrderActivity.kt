package yc.com.homework.mine.activity

import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.view.View

import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.LogUtil
import kotlinx.android.synthetic.main.activity_order_list.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R

import yc.com.homework.mine.adapter.OrderAdapter
import yc.com.homework.mine.fragment.CoinDetailFragment
import yc.com.homework.mine.fragment.OrderDetailFragment
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/1/22 11:26.
 */
class OrderActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {


    override fun init() {
        val fragmentList = arrayListOf<Fragment>()

//        fragmentList.add(CoinDetailFragment())
        fragmentList.add(OrderDetailFragment())

        viewPager.adapter = OrderAdapter(supportFragmentManager, this, fragmentList)
        order_tabLayout.setupWithViewPager(viewPager)
        order_tabLayout.tabMode = TabLayout.MODE_FIXED

        initListener()


    }

    private var currentState = ViewPager.SCROLL_STATE_IDLE
    fun initListener() {
        RxView.clicks(rl_back).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { finish() }

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                LogUtil.msg("position  state $state")
                currentState = state
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (currentState == ViewPager.SCROLL_STATE_DRAGGING && position == 0 && positionOffset.toInt() == 0 && positionOffsetPixels == 0) finish()
                LogUtil.msg("position: $position positionOffset :$positionOffset  positionOffsetPixels $positionOffsetPixels ")
            }

            override fun onPageSelected(position: Int) {

            }
        })


    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {

        return R.layout.activity_order_list
    }
}