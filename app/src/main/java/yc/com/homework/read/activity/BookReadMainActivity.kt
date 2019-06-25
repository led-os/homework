package yc.com.homework.read.activity

import android.content.Intent
import android.support.v4.content.ContextCompat
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jakewharton.rxbinding.view.RxView
import com.umeng.analytics.MobclickAgent
import kotlinx.android.synthetic.main.fragment_book_read.*
import yc.com.base.BaseActivity
import yc.com.base.ObservableManager
import yc.com.homework.R
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.examine.fragment.ExitFragment
import yc.com.homework.read.adapter.BookReadMainAdapter
import yc.com.homework.read.contract.BookReadMainContract
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.presenter.BookReadMainPresenter
import java.util.*
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2019/1/9 09:41.
 */
class BookReadMainActivity : BaseActivity<BookReadMainPresenter>(), BookReadMainContract.View, Observer {


    private var bookReadMainAdapter: BookReadMainAdapter? = null
    private var isEdit = false
    private var layoutManager: GridLayoutManager? = null
    private var bookInfos: List<BookInfo>? = null


    override fun getLayoutId(): Int {
        return R.layout.fragment_book_read
    }

    override fun init() {
        mainToolBar.setTitle(getString(R.string.book_read))


        mainToolBar.setRightContainerVisible(false)
        mainToolBar.showNavigationIcon()
        mainToolBar.init(this)
        ObservableManager.get().addMyObserver(this)
        mPresenter = BookReadMainPresenter(this, this)

        layoutManager = GridLayoutManager(this, 2)
        read_recyclerView.layoutManager = layoutManager

        bookReadMainAdapter = BookReadMainAdapter(null)
        read_recyclerView.adapter = bookReadMainAdapter

        initListener()

    }

    private fun initListener() {
        bookReadMainAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            run {
                if (!bookReadMainAdapter?.getEdit()!!) {
                    if (position == 0) {//添加书本
                        val intent = Intent(this, BookReadSelectActivity::class.java)
                        startActivity(intent)
                    } else {
                        //todo 课本点读事件统计
                        MobclickAgent.onEvent(this, "textbookreading_id", "课本点读")
                        val bookInfo = bookReadMainAdapter?.getItem(position)
                        val intent = Intent(this, BookOutlineActivity::class.java)
                        intent.putExtra("gradeId", bookInfo?.grade)
                        intent.putExtra("subjectId", bookInfo?.subject)
                        intent.putExtra("volumesId", bookInfo?.volumesId)
                        intent.putExtra("versionId", bookInfo?.versionId)
                        startActivity(intent)
                    }
                }
            }
        }

        bookReadMainAdapter?.onItemChildClickListener = BaseQuickAdapter.OnItemChildClickListener { adapter, view, position ->
            run {
                val bookInfo = bookReadMainAdapter?.getItem(position)
                if (view.id == R.id.iv_delete) {//删除

                    val exitFragment = ExitFragment()
                    exitFragment.setTitle("确认删除课本").setOnConfirmListener(object : ExitFragment.OnConfirmListener {
                        override fun onConfirm() {
                            deleteLocalBook(bookInfo, position)
                            exitFragment.dismiss()
                        }
                    }).show(supportFragmentManager, "")

                }
            }
        }

        RxView.clicks(tv_edit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //点击编辑
            isEdit = !isEdit
            if (isEdit) {
                showDoneState()
            } else {
                showEditState()

            }

        }
        RxView.clicks(iv_switch_wx).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //todo 没在这里实现了 干瞪眼
            ToastUtils.showCenterToast(this, "从这里进不去了，换个地方吧...")
        }


    }


    override fun isStatusBarMateria(): Boolean {
        return true
    }


    override fun showBookInfos(t: List<BookInfo>?) {
        bookReadMainAdapter?.setNewData(t)
        bookInfos = t
    }


    private fun deleteLocalBook(bookInfo: BookInfo?, position: Int) {

        mPresenter.deleteBook(bookInfo)
        val infos = bookInfos as ArrayList
        infos.removeAt(position)

        bookReadMainAdapter?.notifyItemRemoved(position)
        if (infos.size == 1) {
            showEditState()
        }


    }


    private fun showEditState() {
        tv_edit.setBackgroundResource(R.drawable.read_eidt_bg)
        tv_edit.setTextColor(ContextCompat.getColor(this, R.color.app_selected_color))
        tv_edit.text = getString(R.string.edit)

        bookReadMainAdapter?.setEdit(false)
    }

    private fun showDoneState() {
        tv_edit.setBackgroundResource(R.drawable.read_done_bg)
        tv_edit.setTextColor(ContextCompat.getColor(this, R.color.white))
        tv_edit.text = getString(R.string.done)
        bookReadMainAdapter?.setEdit(true)
    }


    override fun update(o: Observable?, arg: Any?) {
        arg?.let {
            when (arg) {
                is String -> if (TextUtils.equals("save book", arg)) {
                    mPresenter.loadData(true)
                }
            }
        }
    }

}