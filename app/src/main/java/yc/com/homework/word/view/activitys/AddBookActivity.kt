package yc.com.homework.word.view.activitys

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.widget.LinearLayout
import butterknife.BindView
import com.chad.library.adapter.base.BaseQuickAdapter
import yc.com.base.BaseActivity
import yc.com.base.ObservableManager
import yc.com.homework.R
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.base.widget.MainToolBar
import yc.com.homework.base.widget.StateView
import yc.com.homework.word.contract.AddBookContract
import yc.com.homework.word.model.domain.BookWordInfo
import yc.com.homework.word.model.domain.CourseVersionInfo
import yc.com.homework.word.model.domain.GradeInfo
import yc.com.homework.word.presenter.AddBookPresenter
import yc.com.homework.word.view.adapter.CourseVersionItemClickAdapter
import yc.com.homework.word.view.adapter.GradeItemClickAdapter

/**
 * Created by admin on 2017/7/26.
 */

class AddBookActivity : BaseActivity<AddBookPresenter>(), AddBookContract.View {

    @BindView(R.id.sv_loading)
    lateinit var mStateView: StateView

    @BindView(R.id.layout_content)
    lateinit var mLayoutContext: LinearLayout

    @BindView(R.id.rv_grade_list)
    lateinit var mGradeRecyclerView: RecyclerView

    @BindView(R.id.rv_grade_book_list)
    lateinit var mGradeBookRecyclerView: RecyclerView
    @BindView(R.id.toolbar)
    lateinit var toolbar: MainToolBar


    private var mGradeAdapter: GradeItemClickAdapter? = null

    private var mGradeDatas: List<GradeInfo>? = null

    private var mCourseVersionAdapter: CourseVersionItemClickAdapter? = null

    private var viewType = 1

    lateinit var gradeGridLayoutManager: GridLayoutManager

    private var lastGradePosition = -1

    private var lastVersionPosition = -1


    override fun getLayoutId(): Int {
        return R.layout.read_activity_add_book
    }

    override fun init() {


        val bundle = intent.extras
        if (bundle != null) {
            viewType = bundle.getInt("view_type", 1)
        }

        toolbar.setTitle(getString(R.string.read_study_grade_text))
        toolbar.showNavigationIcon()
        toolbar.setRightContainerVisible(false)
        toolbar.init(this)

        gradeGridLayoutManager = GridLayoutManager(this, 4)
        mGradeRecyclerView.layoutManager = gradeGridLayoutManager
        mGradeAdapter = GradeItemClickAdapter(null)
        mGradeRecyclerView.adapter = mGradeAdapter

        mGradeBookRecyclerView.layoutManager = GridLayoutManager(this, 3)
        mCourseVersionAdapter = CourseVersionItemClickAdapter(null)
        mGradeBookRecyclerView.adapter = mCourseVersionAdapter

        mPresenter = AddBookPresenter(this, this)

        //mPresenter.getGradeListFromLocal();

        //获取年级集合
        mPresenter.gradeList()
        mPresenter.getCVListByGradeId(null, null)//获取所有的教材版本

        //选择年级
        mGradeAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->

            mGradeDatas?.let {

                if (lastGradePosition > -1) {
                    mGradeAdapter?.getItem(lastGradePosition)?.setSelected(false)
                }
                val item = mGradeAdapter?.getItem(position)
                item?.setSelected(true)

                if (lastGradePosition != position) {
                    lastGradePosition = position
                    mGradeAdapter?.notifyDataSetChanged()


                    //获取教材版本
                    val gradeId = item?.grade
                    val partType = item?.partType
                    mPresenter.getCVListByGradeId(gradeId, partType)
                }
            }
        }

        //选择教材版本
        mCourseVersionAdapter?.onItemClickListener = BaseQuickAdapter.OnItemClickListener { adapter, view, position ->
            if (viewType == 1) {

                if (!(adapter.data[position] as CourseVersionInfo).isAdd) {
                    return@OnItemClickListener
                }

                if (lastGradePosition == -1) {
                    ToastUtils.showCenterToast(this@AddBookActivity, "请先选择年级")
                    return@OnItemClickListener
                }


                mGradeDatas?.let {
                    if (lastVersionPosition > -1) {
                        mCourseVersionAdapter?.getItem(lastVersionPosition)?.isSelected = false
                    }
                    val courseVersionInfo = mCourseVersionAdapter?.getItem(position)

                    courseVersionInfo?.isSelected = true
                    if (lastVersionPosition != position) {
                        lastVersionPosition = position
                    }
                    mCourseVersionAdapter?.notifyDataSetChanged()


                    val bookId = courseVersionInfo?.bookId
                    val bookImageUrl = courseVersionInfo?.bookImageUrl
                    val gradeName = courseVersionInfo?.gradeName
                    val versionName = courseVersionInfo?.versionName

                    val bookInfo = BookWordInfo(BookWordInfo.CLICK_ITEM_VIEW)
                    bookInfo.bookId = bookId
                    bookInfo.coverImg = bookImageUrl
                    bookInfo.gradeName = gradeName
                    bookInfo.versionName = versionName

//                    mPresenter.getCVListByGradeId()
                    ObservableManager.get().notifyMyObserver(bookInfo)

                    val intent = Intent(this@AddBookActivity, WordUnitActivity::class.java)
                    intent.putExtra("book_id", bookId)
                    startActivity(intent)
                    this@AddBookActivity.finish()
                }


            }
        }
    }

    override fun hide() {
        mStateView.hide()
    }

    override fun showNoNet() {
        mStateView.showNoNet(mLayoutContext, "网络不给力") {
            //获取年级集合
            mPresenter.gradeList()
            mPresenter.getCVListByGradeId(null, null)//获取所有的教材版本
        }
    }

    override fun showNoData() {
        mStateView.showNoData(mLayoutContext)
    }

    override fun showLoading() {
        mStateView.showLoading(mLayoutContext)
    }

    override fun showGradeListData(gradeInfos: List<GradeInfo>?) {
        mGradeDatas = gradeInfos
        mGradeAdapter?.setNewData(mGradeDatas)
//            mGradeAdapter?.notifyDataSetChanged()
//        }
    }

    override fun showCVListData(list: List<CourseVersionInfo>) {
        mCourseVersionAdapter?.setNewData(list)
    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

}
