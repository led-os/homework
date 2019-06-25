package yc.com.homework.mine.adapter

import android.content.Context
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import yc.com.homework.R

/**
 *
 * Created by wanglin  on 2019/1/22 13:54.
 */
class OrderAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    private var mContext: Context? = null

    private var mFragments: List<Fragment>? = null
    private var titleArray: Array<String>? = null

    constructor(fragmentManager: FragmentManager, context: Context, fragments: List<Fragment>) : this(fragmentManager) {
        this.mContext = context
        this.mFragments = fragments
        titleArray = context.resources.getStringArray(R.array.order_detail)
    }

    override fun getItem(position: Int): Fragment? {

        return if (mFragments != null) return mFragments!![position] else null
    }

    override fun getCount(): Int {

        return if (mFragments == null) 0 else mFragments!!.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return titleArray!![position]
    }

}