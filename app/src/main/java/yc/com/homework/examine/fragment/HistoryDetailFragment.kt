package yc.com.homework.examine.fragment

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import kotlinx.android.synthetic.main.fragment_wall_detail.*
import yc.com.base.BaseActivity
import yc.com.base.BaseFragment
import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.examine.activity.ExamDetailActivity
import yc.com.homework.examine.activity.PreviewActivity
import yc.com.homework.examine.adapter.HistoryDetailAdapter
import yc.com.homework.examine.contract.HistoryDetailContract
import yc.com.homework.examine.presenter.HistoryDetailPresenter
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo

/**
 *
 * Created by wanglin  on 2018/11/27 08:27.
 */
class HistoryDetailFragment : BaseFragment<HistoryDetailPresenter>(), HistoryDetailContract.View {

    private var status: Int = 0
    private var page: Int = 1
    private val pageSize = 20


    private var historyDetailAdapter: HistoryDetailAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_wall_detail
    }

    override fun init() {

        if (arguments != null) {
            status = arguments!!.getInt("status")
        }

//        LogUtil.msg("status $status")

        mPresenter = HistoryDetailPresenter(activity as BaseActivity<*>, this)
        wall_detail_recyclerView.layoutManager = LinearLayoutManager(activity)

        historyDetailAdapter = HistoryDetailAdapter(null, status)
        wall_detail_recyclerView.adapter = historyDetailAdapter
        wall_detail_recyclerView.addItemDecoration(ItemDecorationHelper(activity as BaseActivity<*>, 0, 10, 0, 0))

        initListener()
        initRefresh()

    }

    fun initListener() {

        wall_detail_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val view = recyclerView.getChildAt(0)
                if (view != null)
                    if (view.top < 0) {
                        wall_detail_recyclerView.setPadding(0, 0, 0, 0)
                    }
            }
        })
        historyDetailAdapter?.setOnItemClickListener { _, view, position ->
            //            view.findViewById<ImageView>(R.id.iv_read).visibility = View.GONE
            val data = historyDetailAdapter!!.getItem(position)
            data?.isRead = 1
            historyDetailAdapter?.notifyItemChanged(position)
            when (status) {
                0, 1, 3 -> {
                    if (status == 3) mPresenter.setReadState(data?.task_id)
                    val intent = Intent(activity, PreviewActivity::class.java)
                    intent.putExtra("img", data?.image)
                    startActivity(intent)
                }
                2 -> {
                    mPresenter.setReadState(data?.task_id)
                    val intent = Intent(activity, ExamDetailActivity::class.java)
                    intent.putExtra("index", 2)
                    intent.putExtra("taskId", data?.task_id)
                    startActivity(intent)
                }
            }
        }

        historyDetailAdapter?.setOnLoadMoreListener({
            getData(false)
        }, wall_detail_recyclerView)

    }

    private fun initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式

        smartRefreshLayout.setRefreshHeader(ClassicsHeader(activity))
        smartRefreshLayout.setPrimaryColorsId(R.color.gray_dddddd)
        smartRefreshLayout.setEnableLoadMore(false)
//        smartRefreshLayout.autoRefresh()
        smartRefreshLayout.setOnRefreshListener {

            page = 1
            getData(true)
        }
    }

    override fun fetchData() {
        super.fetchData()
        getData(false)
    }

    override fun showHistoryDetailInfos(t: List<HomeworkDetailInfo>) {
        if (page == 1)
            historyDetailAdapter?.setNewData(t)
        else {
            historyDetailAdapter?.addData(t)
        }
        if (t.size == pageSize) {
            page++
            historyDetailAdapter?.loadMoreComplete()
        } else {
            historyDetailAdapter?.loadMoreEnd()
        }
       smartRefreshLayout.finishRefresh()

    }

    override fun showLoading() {
        stateView.showLoading(smartRefreshLayout)
    }

    override fun showNoNet() {
        stateView.showNoNet(smartRefreshLayout) { getData(false) }
        smartRefreshLayout.finishRefresh()
    }

    override fun showNoData() {
        stateView.showNoData(smartRefreshLayout, "上传作业进行检查才有记录哦！请上传作业")
        smartRefreshLayout.finishRefresh()
    }

    override fun hide() {
        stateView.hide()
        smartRefreshLayout.finishRefresh()
    }

    private fun getData(isRefresh: Boolean) {
        mPresenter.getHistoryDetailInfos(status, page, pageSize, isRefresh)
    }
}