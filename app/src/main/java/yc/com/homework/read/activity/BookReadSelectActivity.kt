package yc.com.homework.read.activity

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.text.TextUtils
import com.chad.library.adapter.base.BaseQuickAdapter
import com.kk.utils.LogUtil
import kotlinx.android.synthetic.main.activity_book_read_select_grade.*
import kotlinx.android.synthetic.main.frament_edit_info.*
import yc.com.base.BaseActivity
import yc.com.homework.R
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.read.adapter.BookSelectItemAdapter
import yc.com.homework.read.contract.BookReadSelectContract
import yc.com.homework.read.domain.bean.BookConditonInfo
import yc.com.homework.read.domain.bean.BookInfo
import yc.com.homework.read.domain.bean.GradeVersionInfo
import yc.com.homework.read.presenter.BookReadSelectPresenter

/**
 *
 * Created by wanglin  on 2019/1/9 15:53.
 */
class BookReadSelectActivity : BaseActivity<BookReadSelectPresenter>(), BookReadSelectContract.View {


    private var gradeJuniorAdapter: BookSelectItemAdapter? = null
    private var gradeMiddleAdapter: BookSelectItemAdapter? = null
    private var bookVersionAdapter: BookSelectItemAdapter? = null
    private var bookSubjectAdapter: BookSelectItemAdapter? = null

    private var volumesId: String? = ""//上下册1上册2下册
    private var subjectId: String? = ""//科目
    private var gradeId: String? = ""//年级
    private var versionId: String? = ""//版本

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_book_read_select_grade
    }

    override fun init() {
        mainToolBar.setTitle(getString(R.string.add_book))
        mainToolBar.showNavigationIcon()
        mainToolBar.init(this)
        mainToolBar.setRightContainerVisible(false)
        mPresenter = BookReadSelectPresenter(this, this)


        //小学
        junior_recyclerView.layoutManager = GridLayoutManager(this, 3)
        gradeJuniorAdapter = BookSelectItemAdapter(null, 1)
        junior_recyclerView.adapter = gradeJuniorAdapter
        junior_recyclerView.isNestedScrollingEnabled = false
        junior_recyclerView.addItemDecoration(ItemDecorationHelper(this, 10))

        //初中
        gradeMiddleAdapter = BookSelectItemAdapter(null, 1)
        middle_recyclerView.layoutManager = GridLayoutManager(this, 3)
        middle_recyclerView.adapter = gradeMiddleAdapter
        middle_recyclerView.isNestedScrollingEnabled = false
        middle_recyclerView.addItemDecoration(ItemDecorationHelper(this, 10))

        //版本
        version_recyclerView.layoutManager = GridLayoutManager(this, 3)
        bookVersionAdapter = BookSelectItemAdapter(null, 2)
        version_recyclerView.adapter = bookVersionAdapter
        version_recyclerView.isNestedScrollingEnabled = false
        version_recyclerView.addItemDecoration(ItemDecorationHelper(this, 10))

        //学科
        subject_recyclerView.layoutManager = GridLayoutManager(this, 3)
        bookSubjectAdapter = BookSelectItemAdapter(null, 3)
        subject_recyclerView.adapter = bookSubjectAdapter
        subject_recyclerView.isNestedScrollingEnabled = false
        subject_recyclerView.addItemDecoration(ItemDecorationHelper(this, 10))

        initListener()

    }

    fun initListener() {
        gradeJuniorAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            run {
                resetGradeClick()
                view.isSelected = true
                val item = gradeJuniorAdapter?.getItem(position)
                item?.let {
                    volumesId = if (item.grade_name?.contains("上")!!) {
                        "1"
                    } else {
                        "2"
                    }
                    gradeId = "${item.grade}"
                    mPresenter.getVersionsByGrade(gradeId, subjectId, volumesId)
                }

            }
        }

        gradeMiddleAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            run {
                resetGradeClick()
                view.isSelected = true
                val item = gradeMiddleAdapter?.getItem(position)
                item?.let {
                    volumesId = if (item.grade_name?.contains("上")!!) {
                        "1"
                    } else {
                        "2"
                    }
                    gradeId = "${item.grade}"
                    mPresenter.getVersionsByGrade(gradeId, subjectId, volumesId)
                }

            }
        }

        bookSubjectAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            run {
                resetSubjectClick()
                view.isSelected = true
                val item = bookSubjectAdapter?.getItem(position)
                subjectId = "${item?.subject_id}"

                if (!TextUtils.isEmpty(gradeId) && !TextUtils.isEmpty(versionId)) {
                    goToIntent()
                } else {
                    mPresenter.getVersionsByGrade(gradeId, subjectId, volumesId)
                }
            }
        }


        bookVersionAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            run {
                if (TextUtils.isEmpty(gradeId)) {
                    ToastUtils.showCenterToast(this, "请先选择年级")
                    return@run
                }
                resetVersionClick()
                view.isSelected = true
                val item = bookVersionAdapter?.getItem(position)
                versionId = "${item?.version_id}"
                goToIntent()
//                mPresenter.getBookInfoByGradeVersion("1", "${item?.version_id}", "${currentBookInfo?.grade}")
            }
        }
    }


    private fun goToIntent() {
        val intent = Intent(this, BookOutlineActivity::class.java)
        intent.putExtra("gradeId", gradeId)
        intent.putExtra("subjectId", subjectId)
        intent.putExtra("volumesId", volumesId)
        intent.putExtra("versionId", versionId)
        startActivity(intent)
        finish()
    }


    override fun showBookConditionInfo(data: BookConditonInfo?) {
        data?.let {
            gradeJuniorAdapter?.setNewData(data.grades?.junior)
            gradeMiddleAdapter?.setNewData(data.grades?.middle)
            bookVersionAdapter?.setNewData(data.versions)
            val subjects = data.subjects
            bookSubjectAdapter?.setNewData(subjects)
            subjects?.let { subjectId = "${subjects[0].subject_id}" }
        }
    }

    override fun showLoading() {
        stateView.showLoading(container)
    }

    override fun showNoNet() {
        stateView.showNoNet(container, { mPresenter.loadData(true) })
    }

    override fun showNoData() {
        stateView.showNoData(container)
    }

    override fun hide() {
        stateView.hide()
    }


    private fun resetGradeClick() {
        for (i in 0..junior_recyclerView!!.childCount) {
            junior_recyclerView.getChildAt(i)?.isSelected = false
        }

        for (i in 0..middle_recyclerView!!.childCount) {
            middle_recyclerView.getChildAt(i)?.isSelected = false
        }
    }

    private fun resetVersionClick() {
        for (i in 0..version_recyclerView!!.childCount) {
            version_recyclerView.getChildAt(i)?.isSelected = false
        }

    }

    private fun resetSubjectClick() {
        for (i in 0..subject_recyclerView!!.childCount) {
            subject_recyclerView.getChildAt(i)?.isSelected = false
        }

    }

    override fun showVersionsByGrade(data: ArrayList<GradeVersionInfo>?) {
        bookVersionAdapter?.setNewData(data)
    }

    override fun showBookInfo(bookInfo: BookInfo) {

    }
}