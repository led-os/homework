package yc.com.homework.mine.fragment

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_coin_detail.*
import yc.com.base.BaseActivity
import yc.com.base.BaseFragment
import yc.com.homework.R
import yc.com.homework.base.config.Constant
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.GlideHelper
import yc.com.base.ObservableManager
import yc.com.homework.mine.activity.RechargeActivity
import yc.com.homework.mine.adapter.CoinDetailAdapter
import yc.com.homework.mine.contract.CoinDetailContract
import yc.com.homework.mine.domain.bean.CoinDetailInfoWrapper
import yc.com.homework.mine.presenter.CoinDetailPresenter
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/24 08:26.
 */
class CoinDetailFragment : BaseFragment<CoinDetailPresenter>(), CoinDetailContract.View, Observer {


    private var page = 1
    private val pageSize = 20

    private var coinAdapter: CoinDetailAdapter? = null
    override fun getLayoutId(): Int {
        return R.layout.activity_coin_detail
    }

    override fun init() {

        ObservableManager.get().addMyObserver(this)
//        toolbar.setNavigationIcon(R.mipmap.base_back)
//        toolbar.setNavigationOnClickListener { finish() }
        mPresenter = CoinDetailPresenter(activity as BaseActivity<*>, this)

        coin_detail_recyclerView.layoutManager = LinearLayoutManager(activity)
        coinAdapter = CoinDetailAdapter(null)
        coin_detail_recyclerView.adapter = coinAdapter
        val headerView = layoutInflater.inflate(R.layout.coin_detail_header_view, null)

        coinAdapter?.addHeaderView(headerView)

        setUserInfo(UserInfoHelper.getUserInfo())
        getData()
        initListener()


    }


    fun initListener() {
        RxView.clicks(ll_recharge).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (!UserInfoHelper.isGotoLogin(activity)) {
                val intent = Intent(activity, RechargeActivity::class.java)
                startActivity(intent)
            }
        }
        coinAdapter?.setOnLoadMoreListener({ getData() }, coin_detail_recyclerView)

    }

    override fun showCoinDetailInfos(t: CoinDetailInfoWrapper) {
        if (t.bill_list != null) {
            val billList = t.bill_list
            if (page == 1)
                coinAdapter?.setNewData(billList)
            else {
                coinAdapter?.addData(billList!!)
            }
        }

        if (t.bill_list?.size == pageSize) {
            page++
            coinAdapter?.loadMoreComplete()
        } else {
            coinAdapter?.loadMoreEnd()
        }

    }


    private fun setUserInfo(userInfo: UserInfo?) {

        userInfo?.let {
            if (!TextUtils.isEmpty(userInfo.face)) {
                GlideHelper.circleIvator(activity as BaseActivity<*>, iv_avator, userInfo.face, true)
            } else {
                iv_avator.setImageResource(R.mipmap.deafult_avator)
            }
            val uid = String.format(getString(R.string.user_id), userInfo.id)

            tv_id.text = uid
            tv_grade.text = userInfo.getGrade()


//
            tv_left_coin.text = "${userInfo.coin}"
        }

    }

    override fun showLoading() {
        stateView.showLoading(coin_detail_recyclerView)
    }

    override fun showNoData() {
        stateView.showNoData(coin_detail_recyclerView)
    }

    override fun showNoNet() {
        stateView.showNoNet(coin_detail_recyclerView) {
            getData()
        }
    }

    override fun hide() {
        stateView.hide()
    }

    private fun getData() {
        mPresenter.getCoinDetailList(page, pageSize)
    }


    override fun update(o: Observable?, arg: Any?) {

        arg?.let {
            when (arg) {
                is String -> {
                    if (TextUtils.equals(Constant.RECHARGE_SUCCESS, arg)) {
                        setUserInfo(UserInfoHelper.getUserInfo())
                        page = 1
                        getData()
                    }
                }
            }
        }
    }

}