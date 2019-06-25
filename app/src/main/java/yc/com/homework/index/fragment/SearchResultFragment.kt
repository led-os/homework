package yc.com.homework.index.fragment

import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding.view.RxView
import com.kk.securityhttp.net.contains.HttpConfig

import kotlinx.android.synthetic.main.fragment_composition_search_result.*
import yc.com.base.BaseActivity

import yc.com.base.BaseFragment
import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.index.activity.CompositionDetailActivity
import yc.com.homework.index.adapter.CompositionAdapter
import yc.com.homework.index.contract.CompostionSearchContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.SearchRecordInfo
import yc.com.homework.index.presenter.CompositionSearchPresenter
import yc.com.homework.index.widget.CompositionFilterPop
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/3/29 11:07.
 */
class SearchResultFragment : BaseFragment<CompositionSearchPresenter>(), CompostionSearchContract.View {


    private var grade: String? = ""
    private var subject: String? = ""
    private var version: String? = ""
    private var page = 1
    private val PAGESIZE = 10
    private var compositionAdapter: CompositionAdapter? = null

    private var title: String? = ""

    override fun init() {

        arguments?.let {
            title = arguments!!.getString("name")
        }

        mPresenter = CompositionSearchPresenter(activity as BaseActivity<*>, this)
//        mPresenter.getCompostionInfos()
        searchComposition(page, PAGESIZE, title, "", "", "")
        result_recyclerView.layoutManager = LinearLayoutManager(activity)
        compositionAdapter = CompositionAdapter(null)
        result_recyclerView.adapter = compositionAdapter
        result_recyclerView.addItemDecoration(ItemDecorationHelper(activity as BaseActivity<*>, 8))
        initListener()
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_composition_search_result
    }


    override fun showHotSearchRecords(data: ArrayList<SearchRecordInfo>?) {
    }

    fun initListener() {
        RxView.clicks(ll_version).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            showPopWindow(getString(R.string.version), iv_version, tv_version)
        }
        RxView.clicks(ll_grade).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            showPopWindow(getString(R.string.grade), iv_grade, tv_grade)
        }
        RxView.clicks(ll_unit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            showPopWindow(getString(R.string.unit), iv_unit, tv_unit)
        }

        compositionAdapter?.setOnItemClickListener { _, _, position ->
            val compositionInfo = compositionAdapter?.getItem(position)
            CompositionDetailActivity.startActivity(activity as BaseActivity<*>, compositionInfo?.compostion_id, compositionInfo?.content)
        }

        compositionAdapter?.setOnLoadMoreListener({
            searchComposition(page, PAGESIZE, title, grade, version, subject)
        }, result_recyclerView)
    }

    private fun showPopWindow(name: String, iv: ImageView, tv: TextView) {
        val popWindow = CompositionFilterPop(activity as BaseActivity<*>, name)
        popWindow.showAsDropDown(ll_top_guide)
        iv.setImageResource(R.mipmap.up_arrow_icon)
        popWindow.setOnDismissListener { iv.setImageResource(R.mipmap.search_down) }
        popWindow.setOnPopClickListener(object : CompositionFilterPop.OnPopClickListener {
            override fun onPopClick(name: String?) {
                popWindow.dismiss()
                resetData()
                tv.text = name
                grade = tv_grade.text.toString()
                subject = tv_unit.text.toString()
                version = tv_version.text.toString()
                page = 1
//                LogUtil.msg("grade: $grade  subject: $subject  version: $version")
                searchComposition(page, PAGESIZE, title, grade, version, subject)
            }
        })

    }

    fun resetData() {
        title = ""
        grade = ""
        version = ""
        subject = ""
    }

    override fun showSearchResultInfos(data: ArrayList<CompositionInfo>?) {
        if (page == 1) {
            compositionAdapter?.setNewData(data)
        } else {
            data?.let {
                compositionAdapter?.addData(data)
            }
        }
        if (data?.size == PAGESIZE) {
            compositionAdapter?.loadMoreComplete()
            page++
        } else {
            compositionAdapter?.loadMoreEnd()
        }

    }

    override fun showRecentRecords(list: List<SearchRecordInfo>?) {
    }

    fun searchComposition(page: Int, pageSize: Int, title: String?, grade: String?, version: String?, unit: String?) {
        mPresenter.compositionSearch(page, pageSize, title, grade, version, unit)
    }

    override fun showLoading() {
        stateView.showLoading(result_recyclerView)
    }

    override fun showNoData() {
        stateView.showNoData(result_recyclerView)
    }

    override fun showNoNet() {
        stateView.showNoNet(result_recyclerView, HttpConfig.NET_ERROR) {
            searchComposition(page, PAGESIZE, title, grade, version, subject)
        }
    }

    override fun hide() {
        stateView.hide()
    }

}