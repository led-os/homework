package yc.com.homework.mine.fragment


import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_order_detail.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.mine.adapter.OrderDetailAdapter
import yc.com.homework.mine.contract.OrderDetailContract
import yc.com.homework.mine.domain.bean.OrderDetailInfo
import yc.com.homework.mine.presenter.OrderDetailPresenter

/**
 *
 * Created by wanglin  on 2019/1/22 14:12.
 */
class OrderDetailFragment : BaseFragment<OrderDetailPresenter>(), OrderDetailContract.View {


    private var orderDetailAdapter: OrderDetailAdapter? = null

    private var page = 1
    private var pageSize = 20


    override fun getLayoutId(): Int {
        return R.layout.fragment_order_detail
    }

    override fun init() {
        mPresenter = OrderDetailPresenter(activity as BaseActivity<*>, this)

        order_recyclerView.layoutManager = LinearLayoutManager(activity)
        orderDetailAdapter = OrderDetailAdapter(null)
        order_recyclerView.adapter = orderDetailAdapter
        order_recyclerView.addItemDecoration(ItemDecorationHelper(activity as BaseActivity<*>, 8))

        initListener()
    }

    fun initListener() {
        orderDetailAdapter?.setOnLoadMoreListener({
            getData()
        }, order_recyclerView)
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(activity as BaseActivity<*>, R.color.app_selected_color))
        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData()
        }

    }

    override fun showOrderDetailList(data: ArrayList<OrderDetailInfo>) {
        if (page == 1) {
            orderDetailAdapter?.setNewData(data)
        } else {
            orderDetailAdapter?.addData(data)
        }
        if (data.size == pageSize) {
            orderDetailAdapter?.loadMoreComplete()
            page++
        } else {
            orderDetailAdapter?.loadMoreEnd()
        }
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false

    }

    override fun showLoading() {
        stateView.showLoading(swipeRefreshLayout)
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showNoNet() {
        stateView.showNoNet(swipeRefreshLayout) {
            getData()
        }
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showNoData() {
        stateView.showNoData(swipeRefreshLayout)
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun hide() {
        stateView.hide()
    }

    override fun fetchData() {
        super.fetchData()
        getData()
    }

    fun getData() {
        mPresenter.getOrderDetailList(page, pageSize)
    }
}