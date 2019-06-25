package yc.com.homework.index.activity

import android.support.v7.widget.LinearLayoutManager
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.activity_composition_collect.*

import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.index.adapter.CompositionAdapter
import yc.com.homework.index.contract.CompositionCollectContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.presenter.CompositionCollectPresenter

/**
 *
 * Created by wanglin  on 2019/3/19 15:37.
 */
class CompositionCollectActivity : BaseActivity<CompositionCollectPresenter>(), CompositionCollectContract.View {


    private var compositionAdapter: CompositionAdapter? = null
    private var page = 1
    private val PAGESIZE = 10

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_composition_collect
    }

    override fun init() {

        mainToolBar.setTitle(getString(R.string.main_collect))
        mainToolBar.setRightContainerVisible(false)
        mainToolBar.showNavigationIcon(R.mipmap.back)
        mainToolBar.init(this)
        mPresenter = CompositionCollectPresenter(this, this)
        mPresenter.getCompositionCollectInfos(page, PAGESIZE)
        recyclerView.layoutManager = LinearLayoutManager(this)
        compositionAdapter = CompositionAdapter(null)
        recyclerView.adapter = compositionAdapter
        recyclerView.addItemDecoration(ItemDecorationHelper(this, 8))
        initRefresh()
        initListener()
    }

    fun initListener() {
        compositionAdapter?.setOnItemClickListener { _, _, position ->
            val item = compositionAdapter?.getItem(position)
            CompositionDetailActivity.startActivity(this@CompositionCollectActivity, item?.compostion_id, item?.content)
        }

        compositionAdapter?.setOnLoadMoreListener({
            mPresenter.getCompositionCollectInfos(page, PAGESIZE)
        }, recyclerView)
    }

    override fun showCollectInfos(list: List<CompositionInfo>?) {
        if (page == 1) {
            compositionAdapter?.setNewData(list)
        } else {
            list?.let {
                compositionAdapter?.addData(list)
            }
        }

        if (list?.size == PAGESIZE) {
            compositionAdapter?.loadMoreComplete()
            page++
        } else {
            compositionAdapter?.loadMoreEnd()
        }
        smartRefreshLayout.finishRefresh()

    }

    private fun initRefresh() {
        smartRefreshLayout.setRefreshHeader(ClassicsHeader(this))
        smartRefreshLayout.setPrimaryColorsId(R.color.gray_dddddd)
        smartRefreshLayout.setEnableLoadMore(false)
//        smartRefreshLayout.autoRefresh()
        smartRefreshLayout.setOnRefreshListener {

            page = 1
            mPresenter.getCompositionCollectInfos(page, PAGESIZE)
        }

    }

    override fun showNoData() {
        stateView.showNoData(smartRefreshLayout)
    }

    override fun hide() {
        stateView.hide()
    }
}