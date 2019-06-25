package yc.com.homework.read.activity

import android.content.Intent
import android.support.v7.widget.LinearLayoutManager
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.kk.utils.LogUtil
import kotlinx.android.synthetic.main.activity_book_outline.*
import kotlinx.android.synthetic.main.book_outline_head_view.*
import yc.com.base.BaseActivity
import yc.com.base.ObservableManager
import yc.com.blankj.utilcode.util.LogUtils
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.R
import yc.com.homework.base.HomeworkApp
import yc.com.homework.base.config.SpConstant
import yc.com.homework.base.dao.BookInfoDao
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.base.utils.Preference
import yc.com.homework.read.adapter.BookOutlineAdapter
import yc.com.homework.read.contract.BookInfoContract
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.domain.bean.BookUnitInfo
import yc.com.homework.read.domain.bean.BookUnitInfoWrapper
import yc.com.homework.read.fragment.BookReadUnlockFragment
import yc.com.homework.read.presenter.BookInfoPresenter
import yc.com.homework.read.widget.ItemHeaderDecoration
import java.util.*

/**
 *
 * Created by wanglin  on 2019/1/10 17:46.
 */
class BookOutlineActivity : BaseActivity<BookInfoPresenter>(), BookInfoContract.View, Observer {


    var gradeId: String? = ""
    var subjectId: String? = ""
    var volumesId: String? = ""
    var versionId: String? = ""
    var bookOutlineAdapter: BookOutlineAdapter? = null

    private val bookInfoDao = HomeworkApp.daoSession?.bookInfoDao
    private var readCount by Preference(this@BookOutlineActivity, SpConstant.read_count, 0)

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_book_outline
    }

    override fun init() {
        ObservableManager.get().addMyObserver(this)
        mainToolBar.showNavigationIcon()
        mainToolBar.init(this)
        mainToolBar.setRightContainerVisible(false)

        mPresenter = BookInfoPresenter(this, this)
        if (intent != null) {
            gradeId = intent.getStringExtra("gradeId")
            subjectId = intent.getStringExtra("subjectId")
            volumesId = intent.getStringExtra("volumesId")
            versionId = intent.getStringExtra("versionId")
        }
        getData()

        outline_recyclerView.layoutManager = LinearLayoutManager(this)
        bookOutlineAdapter = BookOutlineAdapter(this, null)
        outline_recyclerView.adapter = bookOutlineAdapter

        initListener()

    }


    fun initListener() {
        bookOutlineAdapter?.setOnItemClickListener(object : BookOutlineAdapter.OnItemClickListener {
            override fun onItemClick(view: View?, position: Int) {
                val bookUnitInfo = bookOutlineAdapter?.getItem(position)



                Log.e("tag", "can_see : ${bookUnitInfo?.can_see}   position $position  readCount= $readCount")

                bookUnitInfo?.let {
                    if (bookUnitInfo.can_see == 1 || UserInfoHelper.isVip()) {
                        if (readCount < 2 || !UserInfoHelper.isGotoLogin(this@BookOutlineActivity)) {
                            val intent = Intent(this@BookOutlineActivity, BookDetailInfoActivity::class.java)
                            intent.putExtra("bookId", bookUnitInfo.bookId)
                            intent.putExtra("page", bookUnitInfo.page)
                            intent.putExtra("pageTotal", pageTotal)
                            intent.putExtra("pageIndex", bookUnitInfo.pageIndex)
                            intent.putExtra("singleTotal", bookUnitInfo.pageTotal)
                            intent.putParcelableArrayListExtra("classList", newDatas)
                            startActivity(intent)
                            readCount++


                        }

                    } else {
                        val bookReadUnlockFragment = BookReadUnlockFragment()
                        bookReadUnlockFragment.show(supportFragmentManager, "")

                    }
                }

//              ToastUtils.showCenterToast(this@BookOutlineActivity, bookUnitInfo?.title!!)

            }
        })
    }


    private var pageTotal = 0
    private var mDatas: ArrayList<BookUnitInfo>? = null

    private var newDatas: ArrayList<BookUnitInfo>? = null

    override fun showBookUnitInfo(data: BookUnitInfoWrapper?) {
        data?.let {
            mainToolBar.setTitle(data.bookInfo?.name)
            pageTotal = data.pageTotal
            mDatas = data.classList
            bookOutlineAdapter?.setNewData(data.classList)
            outline_recyclerView.addItemDecoration(ItemHeaderDecoration(this, data.classList))
            GlideHelper.circleIvator(this, iv_booK_cover, data.bookInfo?.coverImg, R.drawable.wall_item_placehodler, R.drawable.wall_item_placehodler, false)
            tv_book_name.text = data.bookInfo?.name
            tv_book_publisher.text = data.bookInfo?.versionName
            tv_book_sentences.text = String.format(getString(R.string.book_read_chapter), mDatas?.size)
            newDatas = arrayListOf()
            mDatas?.forEach {
                it.bookUnitInfos?.forEach {
                    val bookUnitInfo = BookUnitInfo()
                    bookUnitInfo.can_see = it.can_see
                    bookUnitInfo.page = it.page
                    newDatas?.add(bookUnitInfo)
                }


            }

            saveToLocal(data.bookInfo)
        }
    }

    private fun saveToLocal(bookInfo: BookInfo?) {
        bookInfo?.let {
            val result = queryBookById(bookInfo.id)
            if (result == null) {
                bookInfo.saveTime = System.currentTimeMillis()
                bookInfoDao?.insert(bookInfo)
            } else {
                result.saveTime = System.currentTimeMillis()
                bookInfoDao?.update(result)
            }
            ObservableManager.get().notifyMyObserver("save book")
        }
    }

    private fun queryBookById(id: Long?): BookInfo? {

        return bookInfoDao?.queryBuilder()?.where(BookInfoDao.Properties.Id.eq(id))?.build()?.unique()
    }

    override fun showNoData() {
        stateView.showNoData(container)
    }

    override fun showNoNet() {
        stateView.showNoNet(container) {
            getData()
        }
    }

    override fun showLoading() {
        stateView.showLoading(container)
    }

    override fun hide() {
        stateView.hide()
    }

    fun getData() {
        mPresenter.getBookUnitInfo(volumesId, versionId, gradeId, subjectId)
    }

    override fun update(o: Observable?, arg: Any?) {
        if (arg != null) {
            if (arg is String) {
                if (TextUtils.equals("share_success", arg)) {
                    bookOutlineAdapter?.notifyDataSetChanged()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get().deleteMyObserver(this)
    }
}