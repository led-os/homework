package yc.com.homework.base.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 *
 * Created by wanglin  on 2018/11/23 13:54.
 */
class MainAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {
    var mFragmentList: List<Fragment>? = null

    constructor(fm: FragmentManager?, fragmentList: List<Fragment>?) : this(fm) {
        mFragmentList = fragmentList
    }


    override fun getItem(position: Int): Fragment? {
        return if (mFragmentList == null) null else mFragmentList!![position]
    }

    override fun getCount(): Int {
        return mFragmentList!!.size
    }
}