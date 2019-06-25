package yc.com.homework.index.activity

import android.content.Intent
import android.os.Build
import android.util.Log
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.kk.securityhttp.net.contains.HttpConfig
import com.vondear.rxtools.RxTimeTool
import kotlinx.android.synthetic.main.activity_news_detail.*
import yc.com.base.BaseActivity
import yc.com.base.StatusBarUtil
import yc.com.blankj.utilcode.util.LogUtils
import yc.com.homework.R
import yc.com.homework.examine.widget.CommonScrollView
import yc.com.homework.index.contract.NewsDetailContract
import yc.com.homework.index.domain.bean.NewsInfo
import yc.com.homework.index.presenter.NewsDetailPresenter
import java.util.*

/**
 *
 * Created by wanglin  on 2019/3/2 11:37.
 */
class NewsDetailActivity : BaseActivity<NewsDetailPresenter>(), NewsDetailContract.View {

    private lateinit var newsId: String
    private lateinit var title: String

    private val imageList = ArrayList<String>()

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_news_detail
    }

    override fun init() {
        if (intent != null) {
            newsId = intent.getStringExtra("newsId")
        }

        mPresenter = NewsDetailPresenter(this, this)
        mPresenter.getNewsDetailInfo(newsId)
        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle("")
        commonToolBar.setRightShareVisable(true)

        initListener()
    }

    private fun initListener() {

        commonScrollView.setOnScrollListener(object : CommonScrollView.OnCommonScrollListener {
            override fun onScroll(l: Int, scrollY: Int, oldl: Int, oldScrollY: Int) {
                if (scrollY > mTextViewTitle.measuredHeight) {
                    commonToolBar.setTitle(title)
                } else {
                    commonToolBar.setTitle("")
                }
            }
        })
    }

    override fun showNewsDetailInfo(info: NewsInfo) {
        title = info.title
        mTextViewTitle.text = title
        mTextViewFrom.text = info.subTitle
        mTextViewTime.text = RxTimeTool.simpleDateFormat("yyyy-MM-dd", Date(info.addTime * 1000))
        var readCount = info.readCount
        if (readCount < 200) {
            readCount = 200
        }
        mTextViewReadCount.text = String.format(getString(R.string.read_count), readCount)
        initWebView(info.body)
    }

    private fun initWebView(body: String) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR1)
            commonWebView.addJavascriptInterface(JavascriptInterface(), "HTML")

        //            val webSettings = commonWebView.settings
//            webSettings.blockNetworkImage = true//设置是否加载网络图片 true 为不加载 false 为加载
        commonWebView.loadDataWithBaseURL(null, body, "text/html", "utf-8", null)
        commonWebView.webViewClient = object : WebViewClient() {

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

//                webSettings.blockNetworkImage = false
                commonWebView.loadUrl("javascript:(function(){"
                        + "var imgs=document.getElementsByTagName(\"img\");"
                        + "for(var i=0;i<imgs.length;i++) " + "{"
                        + "  imgs[i].onclick=function() " + "{ "
                        + "    window.HTML.openImg(this.src);  "
                        + "   }  " + "}" + "}())")
            }

        }
        commonWebView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                if (newProgress > 50) {
                    stateView.hide()
                }
            }

        }

    }

    override fun showLoading() {
        stateView.showLoading(commonScrollView)
    }

    override fun showNoNet() {
        stateView.showNoNet(commonScrollView, HttpConfig.NET_ERROR) {
            mPresenter.getNewsDetailInfo(newsId)
        }
    }

    override fun showNoData() {
        stateView.showNoData(commonScrollView)
    }

    override fun hide() {

    }

    private inner class JavascriptInterface {

        @android.webkit.JavascriptInterface
        fun openImg(imgPath: String) {
            if (imageList.indexOf(imgPath) == -1) {
                imageList.add(imgPath)
            }
            Log.e(TAG, "openImg: $imgPath")
            val intent = Intent(this@NewsDetailActivity, NewsPictureDetailActivity::class.java)
            intent.putExtra("mList", imageList)
            intent.putExtra("position", imageList.indexOf(imgPath))
            startActivity(intent)

        }

        @android.webkit.JavascriptInterface
        fun getImgs(imgPaths: String) {
            LogUtils.e("getImgs $imgPaths")
        }

    }

    companion object {
        val TAG = "newsDetailActivity"
    }
}