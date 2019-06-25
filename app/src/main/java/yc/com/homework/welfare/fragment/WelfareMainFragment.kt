package yc.com.homework.welfare.fragment

import android.os.Handler
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.fragment_welfare_main.*
import yc.com.base.BaseActivity
import yc.com.base.BaseFragment
import yc.com.homework.R
import yc.com.homework.welfare.contract.WelfareContract
import yc.com.homework.welfare.domain.bean.WelfareInfo
import yc.com.homework.welfare.presenter.WelfarePresenter

/**
 *
 * Created by wanglin  on 2019/1/30 10:01.
 */
class WelfareMainFragment : BaseFragment<WelfarePresenter>(), WelfareContract.View {


    private var url = "https://www.acadsoc.com.cn/lps/express/register-dengB.htm?search=2562284"
    private var mHandler: Handler? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_welfare_main
    }

    override fun init() {

        mPresenter = WelfarePresenter(activity as BaseActivity<*>, this)
        mHandler = Handler()
        mainToolBar.setRightContainerVisible(false)
        mainToolBar.setTitle(getString(R.string.welfare))
        mainToolBar.visibility = View.GONE
        top_view.visibility = View.VISIBLE
//        initWebview(h5page)
    }

    override fun showWelfareInfo(h5page: WelfareInfo) {
        initWebview(h5page)
    }

    private fun initWebview(info: WelfareInfo) {

        progressBar.max = 100

//        val slideInfo = JSON.parseObject(SPUtils.getInstance().getString(Constant.INDEX_MENU_STATICS), SlideInfo::class.java)
//        if (null != slideInfo) {
        url = info.url
//        }
        //        url += "?time=" + System.currentTimeMillis();


//        wvMain.addJavascriptInterface(JavascriptInterface(), "study")


        //        wvMain.loadUrl("file:///android_asset/m/ssyy.html");

        wvMain.loadUrl(url)
        wvMain.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
            }

        }
        wvMain.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                super.onProgressChanged(view, newProgress)

                mHandler?.post {
                    // TODO Auto-generated method stub
                    if (progressBar.visibility == ProgressBar.GONE) {
                        progressBar.visibility = ProgressBar.VISIBLE
                    }
                    progressBar.progress = newProgress
                    progressBar.postInvalidate()
                    if (newProgress == 100) {
                        progressBar.visibility = View.GONE
                    }
                }


            }
        }

        wvMain.setOnKeyListener(View.OnKeyListener { v, keyCode, event ->
            if (event?.action == KeyEvent.ACTION_DOWN) {
                if (keyCode == KeyEvent.KEYCODE_BACK && wvMain.canGoBack()) {
                    wvMain.goBack()
                    return@OnKeyListener true
                }
            }
            false
        })

    }




}