package yc.com.homework.index.activity

import android.text.TextUtils
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_mathematical_main.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.index.contract.MathematicalContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.domain.bean.MathematicalInfo
import yc.com.homework.index.presenter.MathematicalPresenter
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/3/21 13:48.
 */
class MathematicalMainActivity : BaseActivity<MathematicalPresenter>(), MathematicalContract.View {


    private var page = 1
    private val PAGESIZE = 10

    private var mMathematicalList: ArrayList<CompositionInfo>? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_mathematical_main
    }

    override fun init() {
        mainToolBar.setTitle(getString(R.string.mathematics_formula))
        mainToolBar.showNavigationIcon(R.mipmap.back)
        mainToolBar.init(this)
        mainToolBar.setRightContainerVisible(false)
        mPresenter = MathematicalPresenter(this, this)
        mPresenter.getMathematicalList(page, PAGESIZE)
        initListener()
    }

    fun initListener() {
        RxView.clicks(baseMathView_unit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            clickItem(baseMathView_unit.getTextTag(), baseMathView_unit.getTextName())
        }

        RxView.clicks(baseMathView_graph).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            clickItem(baseMathView_graph.getTextTag(), baseMathView_graph.getTextName())
        }

        RxView.clicks(baseMathView_graph_conversion).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            clickItem(baseMathView_graph_conversion.getTextTag(), baseMathView_graph_conversion.getTextName())
        }

        RxView.clicks(baseMathView_law).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            clickItem(baseMathView_law.getTextTag(), baseMathView_law.getTextName())
        }
        RxView.clicks(baseMathView_math_formula).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            clickItem(baseMathView_math_formula.getTextTag(), baseMathView_math_formula.getTextName())
        }
        RxView.clicks(baseMathView_olympiad_trees).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            clickItem(baseMathView_olympiad_trees.getTextTag(), baseMathView_olympiad_trees.getTextName())
        }

    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun showMathematicalList(data: ArrayList<CompositionInfo>?) {
        mMathematicalList = data
    }

    private fun clickItem(tag: CharSequence, name: CharSequence) {
        mMathematicalList?.let {

            mMathematicalList!!.forEach {
                if (TextUtils.equals(tag, it.name) || it.name.contains(tag) || TextUtils.equals(name, it.name)) {
                    CompositionDetailActivity.startActivity(this@MathematicalMainActivity, it.compostion_id, it.name, true)
                    return@forEach
                }
            }
        }
    }
}