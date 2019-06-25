package yc.com.homework.wall.fragment

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.vondear.rxtools.RxNetTool
import kotlinx.android.synthetic.main.fragment_wall_detail.*
import yc.com.base.BaseActivity
import yc.com.base.BaseFragment
import yc.com.base.ObservableManager
import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.examine.activity.ExamDetailActivity
import yc.com.homework.wall.adapter.WallDetailAdapter
import yc.com.homework.wall.contract.WallDetailContract
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo
import yc.com.homework.wall.domain.bean.HomeworkInfo
import yc.com.homework.wall.presenter.WallDetailPresenter
import java.util.*

/**
 *
 * Created by wanglin  on 2018/11/27 08:27.
 */
class WallDetailFragment : BaseFragment<WallDetailPresenter>(), WallDetailContract.View, Observer {


    private var wallDetailAdapter: WallDetailAdapter? = null
    private var subjectId: Int = 0
    private var page: Int = 1
    private val pageSize = 20
    override fun getLayoutId(): Int {
        return R.layout.fragment_wall_detail
    }

    override fun init() {
        ObservableManager.get().addMyObserver(this)

        if (arguments != null) {
            subjectId = arguments!!.getInt("subject_id")
        }

        mPresenter = WallDetailPresenter(activity as BaseActivity<*>, this)
        wall_detail_recyclerView.layoutManager = LinearLayoutManager(activity)

        wallDetailAdapter = WallDetailAdapter(null)
        wall_detail_recyclerView.adapter = wallDetailAdapter
        wall_detail_recyclerView.addItemDecoration(ItemDecorationHelper(activity as BaseActivity<*>, 0, 10, 0, 0))
        initListener()
        initRefresh()

    }

    private fun initListener() {


        wall_detail_recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val view = recyclerView.getChildAt(0)
                if (view != null) {
                    if (view.top < 0) {
                        wall_detail_recyclerView.setPadding(0, 0, 0, 0)
                    }
                }

            }
        })

        wallDetailAdapter?.setOnItemClickListener { _, _, position ->
            val homeworkInfo = wallDetailAdapter?.getItem(position)
            val intent = Intent(activity, ExamDetailActivity::class.java)
            intent.putExtra("index", 1)
            intent.putExtra("taskId", homeworkInfo?.taskId)
            startActivity(intent)
        }

        wallDetailAdapter?.setOnLoadMoreListener({ getData(false) }, wall_detail_recyclerView)


    }

    private fun getData(isRefresh: Boolean) {
        mPresenter.getWalInfos(subjectId, page, pageSize, isRefresh)
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

    override fun showWallDetailInfos(t: List<HomeworkInfo>) {
        if (page == 1) wallDetailAdapter?.setNewData(t)
        else wallDetailAdapter?.addData(t)

        if (t.size == pageSize) {
            page++
            wallDetailAdapter?.loadMoreComplete()
        } else {
            wallDetailAdapter?.loadMoreEnd()
        }

        smartRefreshLayout.finishRefresh()
    }

    override fun showHomeworkInfo(data: HomeworkDetailInfo) {

    }


    override fun showLoading() {
        stateView.showLoading(smartRefreshLayout)
    }

    override fun showNoData() {
        stateView.showNoData(smartRefreshLayout)
        smartRefreshLayout.finishRefresh()
    }

    override fun showNoNet() {
        stateView.showNoNet(smartRefreshLayout) {
            getData(false)
        }
        smartRefreshLayout.finishRefresh()
    }

    override fun hide() {
        stateView.hide()
    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg != null) {
            when (arg) {
                is Int -> {
                    if (arg == RxNetTool.NETWORK_NO || arg == RxNetTool.NETWORK_UNKNOWN) {//没有网络
                    } else if (arg == RxNetTool.NETWORK_WIFI || arg == RxNetTool.NETWORK_2G || arg == RxNetTool.NETWORK_3G || arg == RxNetTool.NETWORK_4G) {
                        getData(false)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get().deleteMyObserver(this)
    }
}