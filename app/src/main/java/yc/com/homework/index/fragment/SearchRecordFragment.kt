package yc.com.homework.index.fragment

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.fragment_composition_search_record.*
import yc.com.base.BaseActivity
import yc.com.base.BaseFragment
import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.index.activity.CompositionSearchActivity
import yc.com.homework.index.adapter.HotSearchAdapter
import yc.com.homework.index.adapter.RecentSearchAdapter
import yc.com.homework.index.contract.CompostionSearchContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.SearchRecordInfo
import yc.com.homework.index.presenter.CompositionSearchPresenter
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/3/29 09:55.
 */
class SearchRecordFragment : BaseFragment<CompositionSearchPresenter>(), CompostionSearchContract.View {


    private var hotSearchAdapter: HotSearchAdapter? = null
    private var recentSearchAdapter: RecentSearchAdapter? = null
    private lateinit var mActivity: CompositionSearchActivity
    private var mList: ArrayList<SearchRecordInfo>? = null
    private var degree = 360f
    private var i = 1

    override fun getLayoutId(): Int {
        return R.layout.fragment_composition_search_record
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is CompositionSearchActivity) {
            mActivity = context
        }
    }

    override fun init() {
        mPresenter = CompositionSearchPresenter(activity as BaseActivity<*>, this)
        mPresenter.getHotSearchRecords()
        mPresenter.getSearchRecords()
        hot_search_recyclerView.layoutManager = GridLayoutManager(activity, 3)
        hotSearchAdapter = HotSearchAdapter(null)

        hot_search_recyclerView.adapter = hotSearchAdapter
        hot_search_recyclerView.addItemDecoration(ItemDecorationHelper(activity as BaseActivity<*>, 15, 12))

        val flexboxLayoutManager = FlexboxLayoutManager(activity)
        flexboxLayoutManager.flexWrap = FlexWrap.WRAP
        flexboxLayoutManager.alignItems = AlignItems.STRETCH
        recent_search_recyclerView.layoutManager = flexboxLayoutManager
        recentSearchAdapter = RecentSearchAdapter(null)
        recent_search_recyclerView.adapter = recentSearchAdapter
        recent_search_recyclerView.addItemDecoration(ItemDecorationHelper(activity as BaseActivity<*>, 10, 10))

        initListener()

    }

    private fun initListener() {

        hotSearchAdapter?.setOnItemClickListener { _, _, position ->
            val item = hotSearchAdapter?.getItem(position)
            val resultFragment = SearchResultFragment()
            mActivity.replaceFragment(resultFragment, item?.name)
            mPresenter.saveSearchRecord(item?.name)
        }
        RxView.clicks(ll_refresh).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val rotateAnimation = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
            rotateAnimation.duration = 300
            rotateAnimation.interpolator = AccelerateInterpolator()
            iv_refresh.startAnimation(rotateAnimation)

//            iv_refresh.animate().rotation(degree * i).setDuration(300).setListener(object : Animator.AnimatorListener {
//                override fun onAnimationEnd(animation: Animator?) {
//                    i++
//                }
//
//                override fun onAnimationCancel(animation: Animator?) {
//                }
//
//                override fun onAnimationStart(animation: Animator?) {
////                    degree = 360f
//                }
//
//                override fun onAnimationRepeat(animation: Animator?) {
//
//                }
//            }).start()

            mPresenter.getHotSearchRecords()
        }
        recentSearchAdapter?.setOnItemClickListener { _, _, position ->
            val item = recentSearchAdapter?.getItem(position)
            val resultFragment = SearchResultFragment()

            mActivity.replaceFragment(resultFragment, item?.name)
        }
        RxView.clicks(ll_clear).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            mPresenter.clearSearchRecord()
            mList?.clear()
            recentSearchAdapter?.notifyDataSetChanged()
            ll_recent_search.visibility = View.GONE
        }

    }

    override fun showHotSearchRecords(data: ArrayList<SearchRecordInfo>?) {
        hotSearchAdapter?.setNewData(data)
    }

    override fun showSearchResultInfos(data: ArrayList<CompositionInfo>?) {

    }

    override fun showRecentRecords(list: List<SearchRecordInfo>?) {
        mList = list as ArrayList<SearchRecordInfo>?
        if (mList != null && mList!!.size > 0) {
            ll_recent_search.visibility = View.VISIBLE
            recentSearchAdapter?.setNewData(mList)
        } else {
            ll_recent_search.visibility = View.GONE
        }
    }

    override fun showLoading() {

    }

    override fun showNoData() {

    }

    override fun showNoNet() {

    }

    override fun hide() {

    }

}