package yc.com.homework.mine.activity

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_message.*
import yc.com.base.BaseActivity
import yc.com.base.ObservableManager
import yc.com.homework.R
import yc.com.homework.base.config.Constant
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.mine.adapter.MessageAdapter
import yc.com.homework.mine.contract.MessageContract
import yc.com.homework.mine.domain.bean.MessageInfo
import yc.com.homework.mine.presenter.MessagePresenter

/**
 *
 * Created by wanglin  on 2018/11/23 17:10.
 */
class MessageActivity : BaseActivity<MessagePresenter>(), MessageContract.View {


    private var messageAdapter: MessageAdapter? = null
    private var page = 1
    private val pageSize = 20

    override fun getLayoutId(): Int {
        return R.layout.activity_message
    }

    override fun init() {
        mainToolBar.showNavigationIcon()
        mainToolBar.init(this)
        mainToolBar.setRightContainerVisible(false)
        mainToolBar.setTitle(getString(R.string.message))


        mPresenter = MessagePresenter(this, this)

        getData(false)

        message_recyclerView.layoutManager = LinearLayoutManager(this)
        messageAdapter = MessageAdapter(null)
        message_recyclerView.adapter = messageAdapter
        message_recyclerView.addItemDecoration(ItemDecorationHelper(this, 10))
        initListener()

    }

    private fun initListener() {
        swipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(this, R.color.app_selected_color))

        messageAdapter!!.setOnItemClickListener { _, _, position ->

            val messageInfo = messageAdapter!!.getItem(position)
            messageInfo!!.read = 1
            messageAdapter!!.notifyItemChanged(position)
            ObservableManager.get().notifyMyObserver(Constant.READ_MESSAGE)
            val intent = Intent(this, MessageDetailActivity::class.java)
            intent.putExtra("id", messageInfo.id)
            startActivity(intent)


        }

        messageAdapter!!.setOnLoadMoreListener({
            getData(false)
        }, message_recyclerView)

        swipeRefreshLayout.setOnRefreshListener {
            page = 1
            getData(true)
        }
    }

    override fun showMessageInfos(t: List<MessageInfo>) {
        if (page == 1) {
            messageAdapter!!.setNewData(t)
        } else {
            messageAdapter!!.addData(t)
        }
        if (t.size == pageSize) {
            page++
            messageAdapter!!.loadMoreComplete()
        } else {
            messageAdapter!!.loadMoreEnd()
        }

        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }


    override fun isStatusBarMateria(): Boolean {
        return true
    }


    override fun showLoading() {
        stateView.showLoading(message_recyclerView)
    }

    override fun showNoData() {
        stateView.showNoData(message_recyclerView)
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun showNoNet() {
        stateView.showNoNet(message_recyclerView, {
            page = 1
            getData(false)
        })
        if (swipeRefreshLayout.isRefreshing) swipeRefreshLayout.isRefreshing = false
    }

    override fun hide() {
        stateView.hide()
    }

    override fun showMessageInfo(data: MessageInfo?) {
    }

    private fun getData(isRefresh: Boolean) {
        mPresenter.getMessageInfo(page, pageSize, isRefresh)
    }

}