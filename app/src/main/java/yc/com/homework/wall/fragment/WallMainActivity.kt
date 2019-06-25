package yc.com.homework.wall.fragment

import android.os.Bundle
import android.support.design.widget.TabLayout
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import kotlinx.android.synthetic.main.fragment_wall_main.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.wall.adapter.WallMainAdapter
import yc.com.homework.wall.utils.ReflexUtils

/**
 * Created by wanglin  on 2018/11/23 13:47.
 */
class WallMainActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {


    private var fragmentList: ArrayList<Fragment>? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_wall_main
    }

    override fun init() {

        main_ToolBar.setTitle(getString(R.string.hot_homework_show))
        main_ToolBar.setRightContainerVisible(false)
        main_ToolBar.showNavigationIcon()
        main_ToolBar.init(this)

        fragmentList = arrayListOf()
        val subjects = resources.getStringArray(R.array.wall_subject_array)
        for (k in subjects.indices) {//withIndex() 同时遍历角标和元素 indices 只遍历角标
            val wallDetailFragment = WallDetailFragment()
            val bundle = Bundle()
            bundle.putInt("subject_id", k)
            wallDetailFragment.arguments = bundle
            fragmentList?.add(wallDetailFragment)
        }

        val wallMainAdapter = WallMainAdapter(supportFragmentManager, fragmentList, subjects)
        wall_viewPager.adapter = wallMainAdapter

        wall_tabLayout.setupWithViewPager(wall_viewPager)
        wall_tabLayout.tabMode = TabLayout.MODE_FIXED
        wall_viewPager.offscreenPageLimit = 3

        ReflexUtils.reflex(wall_tabLayout)
        wall_viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {

            }
        })
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

}
