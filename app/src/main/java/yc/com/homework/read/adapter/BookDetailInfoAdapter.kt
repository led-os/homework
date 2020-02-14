package yc.com.homework.read.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

/**
 *
 * Created by wanglin  on 2019/1/14 15:29.
 */
class BookDetailInfoAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {
    private var fragmentList: List<Fragment>? = null
    private var mFt = fm

    constructor(list: List<Fragment>?, fm: FragmentManager) : this(fm) {
        this.fragmentList = list
//        this.fm
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


//    override fun instantiateItem(container: ViewGroup, position: Int): Fragment {
//        val fragment = super.instantiateItem(container,
//                position) as Fragment
//        val fragmentTransaction = mFt.beginTransaction()
//        fragment.let {
//            fragmentTransaction.show(fragment)
//        }
//
//        /**
//         * 使用的 commit方法是在Activity的onSaveInstanceState()之后调用的，这样会出错，因为
//         * onSaveInstanceState方法是在该Activity即将被销毁前调用，来保存Activity数据的，如果在保存玩状态后
//         * 再给它添加Fragment就会出错。解决办法就是把commit（）方法替换成 commitAllowingStateLoss()就行
//         * 了，其效果是一样的。
//         */
//        //        fragmentTransaction.commit();
//        fragmentTransaction.commitAllowingStateLoss()
//        return fragment
//    }
//
//    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
//        val fragment = fragmentList?.get(position)
//        val fragmentTransaction = mFt.beginTransaction()
//
//        fragment?.let {
//            fragmentTransaction.hide(fragment)
//        }
//        fragmentTransaction.commitAllowingStateLoss()
//    }
}