package yc.com.homework.index.activity

import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_more_moudle.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.base.config.Constant
import yc.com.homework.base.utils.SmallProcedureUtils
import yc.com.homework.base.utils.ToastUtils
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/3/1 17:55.
 */
class MoreModuleActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {
    override fun init() {
        mainToolBar.showNavigationIcon()
        mainToolBar.setRightContainerVisible(false)
        mainToolBar.init(this)
        mainToolBar.setTitle(getString(R.string.more))

        RxView.clicks(iv_pinyin_btn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //todo
            SmallProcedureUtils.switchSmallProcedure(this, Constant.PINGYIN_ID, Constant.WX_APP_ID)
//            ToastUtils.showCenterToast(this,"功能正在开发中...")
        }
        RxView.clicks(iv_jiajian_btn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //todo
            SmallProcedureUtils.switchSmallProcedure(this, "", "")
            ToastUtils.showCenterToast(this, "功能正在开发中...")
        }
        RxView.clicks(iv_memory_word_btn).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //todo
            SmallProcedureUtils.switchSmallProcedure(this, Constant.MEMORY_WORD_ID, Constant.WX_APP_ID)
        }
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_more_moudle
    }
}