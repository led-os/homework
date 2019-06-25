package yc.com.homework.index.activity


import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.kk.securityhttp.net.contains.HttpConfig
import com.kk.utils.ScreenUtil
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.activity_more_news.*
import yc.com.base.*

import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.index.adapter.IndexRecommendAdapter
import yc.com.homework.index.contract.NewMoreContract
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.presenter.NewMorePresenter
import java.util.*

/**
 *
 * Created by wanglin  on 2019/3/2 10:22.
 */
class NewsMoreActivity : BaseActivity<NewMorePresenter>(), NewMoreContract.View {


    private var indexRecommendAdapter: IndexRecommendAdapter? = null
    private var page: Int = 1
    private val pageSize: Int = 15
//    private var isReload = false

    override fun getLayoutId(): Int {
        return R.layout.activity_more_news
    }

    override fun init() {


        mainToolBar.setTitle(getString(R.string.reading_style))
        mainToolBar.setRightContainerVisible(false)
        mainToolBar.showNavigationIcon()
        mainToolBar.init(this)
        mPresenter = NewMorePresenter(this, this)
        more_recyclerView.layoutManager = LinearLayoutManager(this)
        indexRecommendAdapter = IndexRecommendAdapter(null)
        more_recyclerView.adapter = indexRecommendAdapter
        more_recyclerView.addItemDecoration(ItemDecorationHelper(this, 5))
        mPresenter.getMoreNews(page, pageSize, false)

        initRefresh()
        initListener()


    }

    private fun initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式

        smartRefreshLayout.setRefreshHeader(ClassicsHeader(this))
        smartRefreshLayout.setPrimaryColorsId(R.color.gray_dddddd)
        smartRefreshLayout.setEnableLoadMore(false)
//        smartRefreshLayout.autoRefresh()
        smartRefreshLayout.setOnRefreshListener {
            page = 1
            mPresenter.getMoreNews(page, pageSize, true)
        }
    }

    fun initListener() {

        indexRecommendAdapter?.setOnLoadMoreListener({
            mPresenter.getMoreNews(page, pageSize, false)
        }, more_recyclerView)

        indexRecommendAdapter?.setOnItemClickListener { _, _, position ->
            val newsInfo = indexRecommendAdapter?.getItem(position)
            val intent = Intent(this, NewsDetailActivity::class.java)
            mPresenter.statisticsCount(newsInfo?.id)
            intent.putExtra("newsId", newsInfo?.id)
            startActivity(intent)
//            isReload = true

        }
        more_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val child = more_recyclerView.getChildAt(0)
                if (child.top < 0) {
                    more_recyclerView.setPadding(more_recyclerView.paddingLeft, 0, more_recyclerView.paddingRight, more_recyclerView.paddingBottom)
                } else {
                    more_recyclerView.setPadding(more_recyclerView.paddingLeft, ScreenUtil.dip2px(this@NewsMoreActivity, 8f), more_recyclerView.paddingRight, more_recyclerView.paddingBottom)
                }
            }
        })

    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun showNewsInfos(data: ArrayList<NewsInfo>) {

        if (page == 1) {
            indexRecommendAdapter?.setNewData(data)
        } else {
            data.let { indexRecommendAdapter?.addData(it) }
        }

        if (data.size == pageSize) {
            indexRecommendAdapter?.loadMoreComplete()
            page++
        } else {
            indexRecommendAdapter?.loadMoreEnd()
        }

    }


    override fun showLoading() {
        stateView.showLoading(smartRefreshLayout)
    }

    override fun showNoData() {
        stateView.showNoData(smartRefreshLayout)
        smartRefreshLayout.finishRefresh()
    }

    override fun showNoNet() {
        stateView.showNoNet(smartRefreshLayout, HttpConfig.NET_ERROR) {
            page = 1
            mPresenter.getMoreNews(page, pageSize, false)
        }
        smartRefreshLayout.finishRefresh()
    }

    override fun hide() {
        stateView.hide()
        smartRefreshLayout.finishRefresh()
    }
}