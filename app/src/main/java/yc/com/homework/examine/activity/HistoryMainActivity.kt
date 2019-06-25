package yc.com.homework.examine.activity

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.widget.LinearLayout

import kotlinx.android.synthetic.main.fragment_wall_main.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.examine.fragment.HistoryDetailFragment
import yc.com.homework.wall.adapter.WallMainAdapter
import yc.com.homework.wall.utils.ReflexUtils

/**
 * Created by wanglin  on 2018/11/23 13:47.
 */
class HistoryMainActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {

    private var fragmentList: ArrayList<Fragment>? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_wall_main
    }

    override fun init() {
        main_ToolBar.showNavigationIcon(R.mipmap.history_back)
        main_ToolBar.init(this)
        main_ToolBar.setTitle(getString(R.string.history))
        main_ToolBar.setRightContainerVisible(false)

        fragmentList = arrayListOf()
        val subjects = resources.getStringArray(R.array.homework_state)
        for (i in subjects.indices) {
            val historyDetailFragment = HistoryDetailFragment()
            val bundle = Bundle()
            bundle.putInt("status", i)
            historyDetailFragment.arguments = bundle
            fragmentList!!.add(historyDetailFragment)
        }

        val wallMainAdapter = WallMainAdapter(supportFragmentManager, fragmentList!!, subjects)
        wall_viewPager.adapter = wallMainAdapter

        wall_tabLayout.setupWithViewPager(wall_viewPager)
        wall_tabLayout.tabMode = TabLayout.MODE_SCROLLABLE
        wall_viewPager.offscreenPageLimit = 3
        wall_viewPager.currentItem = 2
        ReflexUtils.reflex(wall_tabLayout)
        val layoutParams = wall_viewPager.layoutParams as LinearLayout.LayoutParams

        layoutParams.bottomMargin = 0
        wall_viewPager.layoutParams = layoutParams


    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }


}
