package yc.com.homework.index.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.widget.ImageView
import android.widget.TextView
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_main_composition.*
import yc.com.answer.index.ui.widget.FilterPopWindow
import yc.com.base.BaseActivity
import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.index.adapter.CompositionAdapter
import yc.com.homework.index.contract.CompostionContract
import yc.com.homework.index.domain.bean.CompositionInfo
import yc.com.homework.index.presenter.CompostionMainPresenter
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/3/19 15:37.
 */
class CompositionMainActivity : BaseActivity<CompostionMainPresenter>(), CompostionContract.View {


    private var compositionAdapter: CompositionAdapter? = null
    private var page = 1
    private val PAGESIZE = 10

    override fun getLayoutId(): Int {
        return R.layout.activity_main_composition
    }

    override fun init() {
        mPresenter = CompostionMainPresenter(this, this)
        mPresenter.getCompostionIndexInfos(page, PAGESIZE)
        mainToolBar.setTitle(getString(R.string.chinese_composition))
        mainToolBar.setRightCollectVisible()
        mainToolBar.showNavigationIcon(R.mipmap.back)
        mainToolBar.init(this, CompositionCollectActivity::class.java)
        baseSearchView.setSearchTitle(getString(R.string.input_composition_title))

        composition_recyclerView.layoutManager = LinearLayoutManager(this)
        compositionAdapter = CompositionAdapter(null)
        composition_recyclerView.adapter = compositionAdapter
        composition_recyclerView.addItemDecoration(ItemDecorationHelper(this, 8))

        initListener()
    }

    fun initListener() {

        compositionAdapter?.setOnItemClickListener { adapter, view, position ->

            val compositionInfo = compositionAdapter?.getItem(position)
            CompositionDetailActivity.startActivity(this@CompositionMainActivity, compositionInfo?.compostion_id, compositionInfo?.content)
        }
        RxView.clicks(baseSearchView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            startActivity(Intent(this@CompositionMainActivity, CompositionSearchActivity::class.java))
        }
        RxView.clicks(ll_composition).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            startActivity(Intent(this@CompositionMainActivity, CompositionUnitActivity::class.java))
        }

    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }


    override fun showCompostionInfos(data: ArrayList<CompositionInfo>?) {
        compositionAdapter?.setNewData(data)
    }
}