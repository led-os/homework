package yc.com.homework.base.activity

import android.content.Intent
import android.os.Handler
import android.view.View
import com.qq.e.ads.nativ.NativeExpressADView
import kotlinx.android.synthetic.main.activity_splash.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.base.config.Constant
import yc.com.tencent_adv.AdvDispatchManager
import yc.com.tencent_adv.AdvType
import yc.com.tencent_adv.OnAdvStateListener

/**
 *
 * Created by wanglin  on 2018/11/23 10:48.
 */
class SplashActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>(), OnAdvStateListener {
    //AppIntro()


    private var mHandler: Handler? = null

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val sliderPage1 = SliderPage()
//        sliderPage1.title = "Welcome!"
//        sliderPage1.description = "This is a demo of the AppIntro library."
//        sliderPage1.imageDrawable = R.mipmap.main_splash
//        sliderPage1.bgColor = Color.TRANSPARENT
//        addSlide(AppIntro3Fragment.newInstance(sliderPage1))
//        val sliderPage2 = SliderPage()
//        sliderPage2.title = "Clean App Intros"
//        sliderPage2.description = "This library offers developers the ability to add clean app intros at the start of their apps."
//        sliderPage2.imageDrawable = R.mipmap.service_card_bg
//        sliderPage2.bgColor = Color.TRANSPARENT
//        addSlide(AppIntro3Fragment.newInstance(sliderPage2))
//        setDepthAnimation()
//        mHandler = Handler()
//
//
//    }

    private fun switchMain(delay: Long) {
        iv_splash.postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, delay)
    }

//    override fun onSkipPressed(currentFragment: Fragment) {
//        super.onSkipPressed(currentFragment)
//        switchMain(1000)
//        // Do something when users tap on Skip button.
//    }
//
//    override fun onDonePressed(currentFragment: Fragment) {
//        super.onDonePressed(currentFragment)
//        switchMain(0)
//    }

    override fun init() {
        AdvDispatchManager.getManager().init(this, AdvType.SPLASH, splash_container, skip_view, Constant.TENCENT_ADV_ID, Constant.SPLASH_ADV_ID, this)
//        switchMain(1000)
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_splash
    }

    override fun onResume() {
        super.onResume()
        AdvDispatchManager.getManager().onResume()
    }

    override fun onPause() {
        super.onPause()
        AdvDispatchManager.getManager().onPause()
    }

    override fun onNativeExpressShow(mDatas: MutableMap<NativeExpressADView, Int>?) {

    }

    override fun onNativeExpressDismiss(view: NativeExpressADView?) {

    }

    override fun onDismiss(delayTime: Long) {
        switchMain(delayTime)
    }

    override fun onError() {

    }

    override fun onShow() {
        iv_splash.visibility = View.GONE
        skip_view.visibility = View.VISIBLE
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        AdvDispatchManager.getManager().onRequestPermissionsResult(requestCode,permissions,grantResults)
    }
}