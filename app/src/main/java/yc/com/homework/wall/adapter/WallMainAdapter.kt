package yc.com.homework.wall.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 *
 * Created by wanglin  on 2018/11/27 08:32.
 */
class WallMainAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    var mFragmentList: List<Fragment>? = null
    var mTitles: Array<String>? = null


    constructor(fm: FragmentManager, list: List<Fragment>?, titles: Array<String>) : this(fm) {
        this.mFragmentList = list
        this.mTitles = titles

    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList!![position]
    }

    override fun getCount(): Int {

        return if (mFragmentList == null) 0 else mFragmentList!!.size
    }


    override fun getPageTitle(position: Int): CharSequence {

        return mTitles!![position]
    }
}