package yc.com.homework.read.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.FragmentStatePagerAdapter

/**
 *
 * Created by wanglin  on 2019/1/14 15:29.
 */
class BookDetailInfoAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var fragmentList: List<Fragment>? = null

    constructor(list: List<Fragment>?, fm: FragmentManager) : this(fm) {
        this.fragmentList = list
    }

    override fun getItem(position: Int): Fragment? {
        fragmentList?.let {
            return fragmentList!![position]
        }

        return null

    }

    override fun getCount(): Int {
        return if (fragmentList == null) 0 else fragmentList!!.size

    }
}