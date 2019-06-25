package yc.com.homework.word.view.activitys

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView

import com.chad.library.adapter.base.BaseQuickAdapter
import com.jakewharton.rxbinding.view.RxView

import java.util.concurrent.TimeUnit

import butterknife.BindView
import rx.functions.Action1
import yc.com.base.BaseActivity
import yc.com.homework.R
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.base.widget.MainToolBar
import yc.com.homework.base.widget.StateView
import yc.com.homework.mine.fragment.ShareFragment
import yc.com.homework.word.contract.WordUnitContract
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.domain.WordUnitInfoList
import yc.com.homework.word.presenter.WordUnitPresenter
import yc.com.homework.word.view.adapter.ReadWordUnitItemClickAdapter

/**
 * Created by admin on 2017/7/26.
 */

class WordUnitActivity : BaseActivity<WordUnitPresenter>(), WordUnitContract.View {

    @BindView(R.id.iv_book_top)
    lateinit var mBookGradeImageView: ImageView

    @BindView(R.id.tv_book_grade_name)
    lateinit var mBookGradeNameTextView: TextView

    @BindView(R.id.tv_book_press)
    lateinit var mBookPressTextView: TextView

    @BindView(R.id.rv_word_unit_list)
    lateinit var mWordUnitRecyclerView: RecyclerView

    @BindView(R.id.sv_loading)
    lateinit var mStateView: StateView

    @BindView(R.id.ll_content)
    lateinit var mContentLinearLayout: LinearLayout

    @BindView(R.id.btn_share_classmate)
    lateinit var mShareButton: Button

    lateinit var mItemAdapter: ReadWordUnitItemClickAdapter
    @BindView(R.id.toolbar)
    lateinit var toolbar: MainToolBar

    private var bookId: String? = null

    override fun getLayoutId(): Int {
        return R.layout.read_activity_word_unit
    }

    override fun init() {

        val bundle = intent.extras
        if (bundle != null) {
            bookId = bundle.getString("book_id")
        }

        mPresenter = WordUnitPresenter(this, this)

        toolbar.setTitle(getString(R.string.read_book_unit_text))
        toolbar.showNavigationIcon()
        toolbar.init(this)
        toolbar.setRightContainerVisible(false)

        mWordUnitRecyclerView.layoutManager = GridLayoutManager(this, 2)

        mItemAdapter = ReadWordUnitItemClickAdapter(null)
        mWordUnitRecyclerView.adapter = mItemAdapter

        mItemAdapter.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->


            val intent = Intent(this@WordUnitActivity, ReadWordActivity::class.java)
            val item = mItemAdapter.getItem(position)
            intent.putExtra("unit_id", item?.id)
            intent.putExtra("unit_title", item?.name)
            startActivity(intent)

        }

        //分享
        RxView.clicks(mShareButton).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val sharePopupWindow = ShareFragment()
            sharePopupWindow.show(supportFragmentManager, "")
        }

        mPresenter.getBookInfoById(bookId)
    }

    override fun showWordUnitListData(wordUnitInfoList: WordUnitInfoList?) {
        if (wordUnitInfoList != null) {
            mItemAdapter.setNewData(wordUnitInfoList.list)
        }
    }

    override fun showBookInfo(bookInfo: BookWordInfo?) {
        if (bookInfo != null) {
            GlideHelper.cornerPic(this@WordUnitActivity, mBookGradeImageView, bookInfo.coverImg, 0, 0, true, false)
            mBookGradeNameTextView.text = bookInfo.name
            mBookPressTextView.text = bookInfo.press
        }
    }

    override fun hide() {
        mStateView.hide()
    }

    override fun showNoNet() {
        mStateView.showNoNet(mContentLinearLayout, "网络不给力") { mPresenter.getBookInfoById(bookId) }
    }

    override fun showNoData() {
        mStateView.showNoData(mContentLinearLayout)
    }

    override fun showLoading() {
        mStateView.showLoading(mContentLinearLayout)
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }


}
