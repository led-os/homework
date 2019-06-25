package yc.com.homework.base.activity

import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import kotlinx.android.synthetic.main.fragment_welfare_main.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R

/**
 *
 * Created by wanglin  on 2019/2/13 11:56.
 */
class WebViewActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {
    private var url: String? = null
    private var title: String? = null

    companion object {
        fun startActivity(context: Context, url: String, title: String?) {
            val intent = Intent(context, WebViewActivity::class.java)
            intent.putExtra("url", url)
            intent.putExtra("title", title)
            context.startActivity(intent)
        }
    }


    override fun init() {
        if (intent != null) {
            url = intent.getStringExtra("url")
            title = intent.getStringExtra("title")
        }

        mainToolBar.setRightContainerVisible(false)
        mainToolBar.setTitle(title)
        mainToolBar.showNavigationIcon()
        mainToolBar.init(this)
        initWebview()
    }

    private fun initWebview() {

        progressBar.max = 100

//        val slideInfo = JSON.parseObject(SPUtils.getInstance().getString(Constant.INDEX_MENU_STATICS), SlideInfo::class.java)
//        if (null != slideInfo) {
//            url = slideInfo.getUrl()
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

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_welfare_main
    }
}