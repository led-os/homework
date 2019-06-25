package yc.com.homework.index.activity

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.activity_composition_detail.*
import yc.com.base.BaseActivity
import yc.com.base.StatusBarUtil
import yc.com.homework.R
import yc.com.homework.examine.widget.CommonScrollView
import yc.com.homework.index.contract.CompositionDetailContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.presenter.CompositionDetailPresenter

/**
 *
 * Created by wanglin  on 2019/3/20 16:12.
 */
class CompositionDetailActivity : BaseActivity<CompositionDetailPresenter>(), CompositionDetailContract.View {


    private lateinit var id: String
    private lateinit var content: String

    private var newsInfo: NewsInfo? = null

    private var title: String? = ""
    private var isCollect: Int = 0
    private var isHide: Boolean = false

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_composition_detail
    }

    override fun init() {
        mPresenter = CompositionDetailPresenter(this, this)
        if (intent != null) {
            id = intent.getStringExtra("id")
            content = intent.getStringExtra("content")

            isHide = intent.getBooleanExtra("isHide", false)

        }

        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.showNavigationIcon()
        commonToolBar.setTitle("")
        commonToolBar.init(this)
        if (!isHide) {
            commonToolBar.setRightCollectVisible(true)
            commonToolBar.setRightShareVisable(true)
            tv_comment.visibility = View.VISIBLE
            iv_remark.visibility = View.VISIBLE
            ll_grade.visibility = View.VISIBLE
        }
        commonWebView.setBackgroundColor(0) // 设置webView背景色
        mPresenter.getCompositionDetailInfo(id, isHide)
        initListener()
    }

    fun initListener() {
        commonToolBar.setCollectClickListener {
            //收藏
            val compositionInfo = CompositionInfo()
            compositionInfo.compostion_id = newsInfo?.id
            compositionInfo.name = newsInfo?.title
            compositionInfo.version = newsInfo?.version
            compositionInfo.grade = newsInfo?.grade
            compositionInfo.unit = newsInfo?.unit
            compositionInfo.content = content
            compositionInfo.isCollect = isCollect

            mPresenter.collectComposition(compositionInfo)

        }

        commonScrollView.setOnScrollListener(object : CommonScrollView.OnCommonScrollListener {
            override fun onScroll(l: Int, scrollY: Int, oldl: Int, oldScrollY: Int) {

                if (scrollY > mTextViewTitle.measuredHeight) {
                    commonToolBar.setTitle(title)
                } else {
                    commonToolBar.setTitle("")
                }

//                LogUtil.msg("top: $top scrollY:  $scrollY")
            }
        })


    }

    override fun showCompositionDetailInfo(data: NewsInfo?) {
        data?.let {
            newsInfo = data
            mTextViewTitle.text = data.title
            title = data.title
            if (!isHide) {
                mTextViewVersion.text = data.version
                mTextViewGrade.text = data.grade
                mTextViewReadUnit.text = data.unit
                tv_comment.text = data.comment
                commonToolBar.setRightCollectSelected(data.isCollect == 1)
                isCollect = data.isCollect
            }
            commonWebView.loadDataWithBaseURL(null, data.body, "text/html", "utf-8", "")
            if (!isHide) {
                commonWebView.post {

                    val height = tv_comment.height
                    val layoutParams = iv_remark.layoutParams as ViewGroup.MarginLayoutParams
                    val ivHeight = iv_remark.height + layoutParams.bottomMargin
                    val lp = commonWebView.layoutParams as ViewGroup.MarginLayoutParams
                    lp.bottomMargin = ivHeight + height
                    commonWebView.layoutParams = lp

//                LogUtil.msg("height:  $height  ivHeight:  $ivHeight topMargin: ${layoutParams.bottomMargin}")
                }
            }

        }
    }

    override fun showLoading() {
        stateView.showLoading(rl_container)
    }

    override fun showNoData() {
        stateView.showNoData(rl_container)
    }

    override fun showNoNet() {
        stateView.showNoNet(rl_container) {
            mPresenter.getCompositionDetailInfo(id, isHide)
        }
    }

    override fun hide() {
        stateView.hide()
    }

    companion object {
        fun startActivity(context: Context, id: String?, content: String?, isHide: Boolean = false) {
            val intent = Intent(context, CompositionDetailActivity::class.java)
            intent.putExtra("id", id)
            intent.putExtra("content", content)
            intent.putExtra("isHide", isHide)

            context.startActivity(intent)
        }
    }

    override fun showCompositionCollectState(collect: Int) {
        commonToolBar.setRightCollectSelected(collect == 1)
        isCollect = collect
    }
}