package yc.com.homework.word.view.activitys

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.Button
import android.widget.LinearLayout
import butterknife.BindView
import butterknife.OnClick
import yc.com.base.BaseActivity
import yc.com.base.ObservableManager
import yc.com.homework.R
import yc.com.homework.base.widget.MainToolBar
import yc.com.homework.base.widget.StateView
import yc.com.homework.examine.fragment.ExitFragment
import yc.com.homework.word.contract.BookContract
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.presenter.BookPresenter
import yc.com.homework.word.view.adapter.ReadBookItemClickAdapter
import java.util.*

/**
 * Created by admin on 2017/7/25.
 */

class BookActivity : BaseActivity<BookPresenter>(), BookContract.View, Observer {

    @BindView(R.id.rv_book_list)
    lateinit var mBookRecyclerView: RecyclerView

    @BindView(R.id.btn_edit_books)
    lateinit var mEditBooksButton: Button

    @BindView(R.id.sv_loading)
    lateinit var mLoadingStateView: StateView

    @BindView(R.id.ll_content)
    lateinit var mContentLinearLayout: LinearLayout


    lateinit var mItemAdapter: ReadBookItemClickAdapter

    @BindView(R.id.toolbar)
    lateinit var toolbar: MainToolBar

    private var page = 1

    private var PAGESIZE = 100


    override fun getLayoutId(): Int {
        return R.layout.read_activity_book
    }

    override fun init() {

        ObservableManager.get().addMyObserver(this)
        mPresenter = BookPresenter(this, this)
        //        Properties pt = PropertyUtil.getProperties(this);

        mPresenter.bookList(page, PAGESIZE)
        toolbar.setTitle(getString(R.string.english_word))
        toolbar.showNavigationIcon()
        toolbar.init(this)
        toolbar.setRightContainerVisible(false)

        mBookRecyclerView.layoutManager = GridLayoutManager(this, 2)
        mItemAdapter = ReadBookItemClickAdapter(null)
        mBookRecyclerView.adapter = mItemAdapter


        initListener()


    }

    private fun initListener() {
        mItemAdapter.setOnItemClickListener { adapter, view, position ->
            if (!mItemAdapter.editState) {
                if (position == 0) {
                    val intent = Intent(this@BookActivity, AddBookActivity::class.java)
                    startActivity(intent)
                } else {
                    val item = mItemAdapter.getItem(position)
                    toUnitActivity(item, WordUnitActivity::class.java)
                }
            }
        }

        mItemAdapter.setOnItemChildClickListener { adapter, view, position ->
            val exitFragment = ExitFragment()
            exitFragment.setTitle("确认删除课本").setOnConfirmListener(object : ExitFragment.OnConfirmListener {
                override fun onConfirm() {
                    exitFragment.dismiss()
                    val bookInfo = mItemAdapter.getItem(position)
                    mPresenter.deleteBook(bookInfo)
                }
            }).show(supportFragmentManager, "")

        }


    }


    //页面跳转
    private fun toUnitActivity(wordInfo: BookWordInfo?, cls: Class<*>) {
        val intent = Intent(this, cls)
        intent.putExtra("book_id", wordInfo?.bookId)
        startActivity(intent)
    }

    @OnClick(R.id.btn_edit_books)
    fun editBooks() {
        val editState = mItemAdapter.editState

        mItemAdapter.editState = !editState
        mItemAdapter.notifyDataSetChanged()
        if (mItemAdapter.editState) {
            mEditBooksButton.text = getString(R.string.done)
            mEditBooksButton.setBackgroundResource(R.drawable.read_done_bg)
            mEditBooksButton.setTextColor(ContextCompat.getColor(this@BookActivity, R.color.white))
        } else {
            mEditBooksButton.text = getString(R.string.edit)
            mEditBooksButton.setBackgroundResource(R.drawable.read_border_line_btn)
            mEditBooksButton.setTextColor(ContextCompat.getColor(this@BookActivity, R.color.app_selected_color))
        }
    }

    override fun showBookListData(bookInfos: ArrayList<BookWordInfo>?, isEdit: Boolean) {
        var infos = bookInfos
//        //TODO,数据处理待完成
//        LogUtils.e("Add Book --->")
        if (infos == null) {
            infos = ArrayList()
        }
        mItemAdapter.editState = isEdit
        infos.add(0, BookWordInfo(BookWordInfo.CLICK_ITEM_VIEW))
        if (infos.size == 1) {
            mItemAdapter.editState = true
            editBooks()
        }
        mItemAdapter.setNewData(infos)
    }

    override fun deleteBookRefresh() {
        //TipsHelper.tips(BookActivity.this, "删除成功");
    }

    override fun hide() {
        mLoadingStateView.hide()
    }

    override fun showLoading() {
        mLoadingStateView.showLoading(mContentLinearLayout)
    }


    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun update(o: Observable?, arg: Any?) {
        arg?.let {
            if (arg is BookWordInfo) {
                mPresenter.addBook(arg)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get().deleteMyObserver(this)
    }

}
