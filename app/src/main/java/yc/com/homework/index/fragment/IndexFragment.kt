package yc.com.homework.index.fragment

import android.app.Activity
import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.LogUtil
import com.qq.e.ads.nativ.NativeExpressADView
import com.scwang.smartrefresh.layout.header.ClassicsHeader
import com.tencent.mm.opensdk.modelbiz.WXLaunchMiniProgram
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import com.umeng.analytics.MobclickAgent
import com.youth.banner.BannerConfig
import kotlinx.android.synthetic.main.fragment_index.*
import yc.com.answer.index.ui.fragment.IndexAnswerFragment
import yc.com.base.BaseActivity
import yc.com.base.BaseFragment
import yc.com.base.StatusBarUtil
import yc.com.homework.R
import yc.com.homework.base.activity.WebViewActivity
import yc.com.homework.base.config.Constant
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.index.activity.*
import yc.com.homework.index.adapter.IndexReadAdapter
import yc.com.homework.index.contract.IndexContract
import yc.com.homework.index.domain.bean.IndexInfo
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.presenter.IndexPresenter
import yc.com.homework.index.utils.BannerImageLoader
import yc.com.homework.read.activity.BookReadMainActivity
import yc.com.homework.word.view.activitys.BookActivity
import yc.com.soundmark.study.activity.StudyActivity
import yc.com.tencent_adv.AdvDispatchManager
import yc.com.tencent_adv.AdvType
import yc.com.tencent_adv.OnAdvStateListener
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList


/**
 *
 * Created by wanglin  on 2019/2/13 10:57.
 */
class IndexFragment : BaseFragment<IndexPresenter>(), IndexContract.View, OnAdvStateListener {


    private var indexInfo: IndexInfo? = null
    private var indexRecommendAdapter: IndexReadAdapter? = null
    private var page: Int = 1
    private val PAGESIZE = 4

    private val mAdViewPositionMap = HashMap<NativeExpressADView?, Int>()

    private var FIRST_AD_POSITION = 2 // 第一条广告的位置

    private var mAdViewList: MutableMap<NativeExpressADView, Int>? = null

    override fun getLayoutId(): Int {
        return R.layout.fragment_index
    }

    override fun init() {

        StatusBarUtil.setStatusTextColor1(true, activity)
        mPresenter = IndexPresenter(activity as BaseActivity<*>, this)

        index_recyclerView.layoutManager = LinearLayoutManager(activity)
        indexRecommendAdapter = IndexReadAdapter(null, mAdViewPositionMap)
        index_recyclerView.adapter = indexRecommendAdapter
        index_recyclerView.addItemDecoration(ItemDecorationHelper(activity as BaseActivity<*>, 5))
        mPresenter.getNewsInfos(page, PAGESIZE,false)

        initListener()
        initRefresh()
        val positions = ArrayList<Int>()
        positions.add(FIRST_AD_POSITION)
        AdvDispatchManager.getManager().init(activity, AdvType.ORIGIN_PIC, null, null, Constant.TENCENT_ADV_ID, Constant.NATIVE_ADV_ID, 1, positions, this)

//        val payFragment = PayFragment()
//        payFragment.show(fragmentManager, "")
    }

    private fun initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式

        smartRefreshLayout.setRefreshHeader(ClassicsHeader(activity))
        smartRefreshLayout.setPrimaryColorsId(R.color.gray_dddddd)
        smartRefreshLayout.setEnableLoadMore(false)
//        smartRefreshLayout.autoRefresh()
        smartRefreshLayout.setOnRefreshListener {
            mPresenter.getIndexInfo()
            page = 1
            mPresenter.getNewsInfos(page, PAGESIZE,true)
        }
    }


    private fun initListener() {


        RxView.clicks(baseIndexView_book_read).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(activity, BookReadMainActivity::class.java)
            startActivity(intent)
        }

        RxView.clicks(baseIndexView_answer_look).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //todo 答案圈网站点击事件统计
            MobclickAgent.onEvent(activity, "answersfinding_id", "答案圈网站")
//            WebViewActivity.startActivity(activity, "http://m.mxqe.com/", getString(R.string.answer_look))

            val intent = Intent(activity, IndexAnswerFragment::class.java)
            startActivity(intent)
        }

        RxView.clicks(baseIndexView_soundmark).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //音标学习小助手
//            switchSmallProcedure(activity, Constant.WX_ORIGIN_ID, Constant.WX_APP_ID)
            val intent = Intent(activity, StudyActivity::class.java)
            startActivity(intent)

            //todo 音标小程序点击事件统计
            MobclickAgent.onEvent(activity, "phoneticsstuding_id", "音标小程序")
        }

        RxView.clicks(baseIndexView_more).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            MobclickAgent.onEvent(activity, "more_id", "更多")
            val intent = Intent(activity, MoreModuleActivity::class.java)
            startActivity(intent)

        }

        RxView.clicks(tv_more_news).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(activity, NewsMoreActivity::class.java)
            startActivity(intent)
        }
        banner.setOnBannerListener { position ->
            //todo bannar点击事件

            val bannerInfo = mPresenter.getBannerInfo(position, indexInfo)

            bannerInfo?.let {
                val appId = bannerInfo.app_id
                val url = bannerInfo.url
//                LogUtil.msg("position :$position  appid: $appId")
                if (!TextUtils.isEmpty(appId)) {
                    val strs = appId.split("|")
//                    LogUtil.msg("strs: ${strs[0]} ${strs[1]}")
                    switchSmallProcedure(activity as BaseActivity<*>, strs[1], strs[0])
                } else if (!TextUtils.isEmpty(url)) {
                    url?.let {
                        if (url.startsWith("http") || url.startsWith("https")) {
                            WebViewActivity.startActivity(activity as BaseActivity<*>, url, bannerInfo.title)
                        }
                    }
                }


            }


        }
        indexRecommendAdapter?.setOnItemClickListener { _, _, position ->
            val newsInfo = indexRecommendAdapter?.getItem(position)
            val intent = Intent(activity, NewsDetailActivity::class.java)
            mPresenter.statisticsCount(newsInfo?.id)
            intent.putExtra("newsId", newsInfo?.id)
            startActivity(intent)
//            isReload = true
        }

        RxView.clicks(baseIndexView_yuwen).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            MobclickAgent.onEvent(activity, "writing_id", "语文作文")
            startActivity(Intent(activity, CompositionMainActivity::class.java))
        }

        RxView.clicks(baseIndexView_answer_shuxue).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            MobclickAgent.onEvent(activity, "formula_id", "数学公式")
            startActivity(Intent(activity, MathematicalMainActivity::class.java))
        }
        RxView.clicks(baseIndexView_english).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            startActivity(Intent(activity, BookActivity::class.java))
        }

        RxView.clicks(iv_yuwen).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            MobclickAgent.onEvent(activity, "writing_id", "语文作文")
            startActivity(Intent(activity, CompositionMainActivity::class.java))
        }
        RxView.clicks(iv_soundmark).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(activity, StudyActivity::class.java)
            startActivity(intent)

            //todo 音标小程序点击事件统计
            MobclickAgent.onEvent(activity, "phoneticsstuding_id", "音标小程序")
        }

    }

    private fun initBanner(images: List<String>?) {  //设置图片加载器

        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR)
        banner.setImageLoader(BannerImageLoader())
        //设置图片集合
        banner.setImages(images)
        banner.setDelayTime(1500)
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER)
        //banner设置方法全部调用完毕时最后调用
        banner.start()

    }


    override fun showIndexInfo(t: IndexInfo?) {
        indexInfo = t
        val images = mPresenter.getBannerImages(t)
        initBanner(images)
        smartRefreshLayout.finishRefresh()

    }

    override fun showIndexRecommendInfos(t: ArrayList<NewsInfo>, fresh: Boolean) {
        smartRefreshLayout.finishRefresh()

        indexRecommendAdapter?.setNewData(t)
        if (fresh) {
            onNativeExpressShow(mAdViewList)
        }
    }


    /**
     * //todo 跳转到小程序
     */

    private fun switchSmallProcedure(activity: Activity, originId: String, appId: String) {
        try {
            val api = WXAPIFactory.createWXAPI(activity, appId)

            val req = WXLaunchMiniProgram.Req()
            req.userName = originId // 填小程序原始id
            //                    req.path = path;                  //拉起小程序页面的可带参路径，不填默认拉起小程序首页
            req.miniprogramType = WXLaunchMiniProgram.Req.MINIPTOGRAM_TYPE_RELEASE// 可选打开 开发版，体验版和正式版
            api.sendReq(req)

        } catch (e: Exception) {
//            LogUtil.msg("e  ${e.message}")
            ToastUtils.showCenterToast(activity, "您的手机未安装微信，请先安装")
        }

    }

    override fun showNoNet() {
        smartRefreshLayout.finishRefresh()
    }

    override fun onResume() {
        super.onResume()
        //开始轮播
        banner.startAutoPlay()


    }

    override fun onPause() {
        super.onPause()
        banner.stopAutoPlay()
    }


    override fun onNativeExpressShow(mDatas: MutableMap<NativeExpressADView, Int>?) {
        LogUtil.msg("size: " + mDatas?.size)
        mDatas?.let {
            mAdViewList = mDatas
            for ((key, value) in mDatas) {

                mAdViewPositionMap[key] = value
                val newsInfo = NewsInfo()

                newsInfo.type = NewsInfo.ADV
                newsInfo.view = key

                indexRecommendAdapter?.addADViewToPosition(value, newsInfo)
            }
        }

    }

    override fun onNativeExpressDismiss(view: NativeExpressADView?) {
        if (indexRecommendAdapter != null) {
            val removedPosition = mAdViewPositionMap[view]
            view?.let {
                indexRecommendAdapter?.removeADView(removedPosition!!, view)
            }


        }
    }

    override fun onDismiss(delayTime: Long) {

    }

    override fun onError() {

    }

    override fun onShow() {

    }

}