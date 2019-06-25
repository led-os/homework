package yc.com.homework.mine.activity

import kotlinx.android.synthetic.main.activity_message_detail.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.mine.contract.MessageContract
import yc.com.homework.mine.domain.bean.MessageInfo
import yc.com.homework.mine.presenter.MessagePresenter
import yc.com.homework.wall.utils.UiUtils

/**
 *
 * Created by wanglin  on 2018/12/15 09:22.
 */
class MessageDetailActivity : BaseActivity<MessagePresenter>(), MessageContract.View {

    private var newsId: String? = null

    override fun init() {

        commonToolBar.setTitle("消息")
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        StatusBarUtil.setStatusTextColor1(true, this)
        mPresenter = MessagePresenter(this, this)
        if (intent != null) {
            newsId = intent.getStringExtra("id")
        }

        getData()
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_message_detail
    }

    override fun showMessageInfos(t: List<MessageInfo>) {
    }


    override fun showMessageInfo(data: MessageInfo?) {
        tv_mess_title.text = data?.title
        tv_message_time.text = UiUtils.longToString(date = data?.time)
        tv_message_content.text = data?.desc

    }

    override fun showLoading() {
        stateView.showLoading(ll_content)
    }

    override fun showNoData() {
        stateView.showNoData(ll_content)
    }

    override fun showNoNet() {
        stateView.showNoNet(ll_content, {
            getData()
        })
    }

    override fun hide() {
        stateView.hide()
    }

    private fun getData() {
        mPresenter.getMessageDetailInfo(newsId)
    }
}