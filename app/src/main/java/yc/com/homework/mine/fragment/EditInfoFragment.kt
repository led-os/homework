package yc.com.homework.mine.fragment

import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.chad.library.adapter.base.BaseQuickAdapter
import com.jakewharton.rxbinding.view.RxView
import com.vondear.rxtools.view.RxTextViewVertical
import kotlinx.android.synthetic.main.frament_edit_info.*
import yc.com.base.BaseActivity
import yc.com.base.BaseDialogFragment
import yc.com.homework.R
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.ItemDecorationHelper
import yc.com.homework.mine.adapter.GradeSelectAdapter
import yc.com.homework.mine.contract.EditInfoContract
import yc.com.homework.mine.domain.bean.GradeInfo
import yc.com.homework.mine.presenter.EditInfoPresenter
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/30 11:33.
 */
class EditInfoFragment : BaseDialogFragment<EditInfoPresenter>(), EditInfoContract.View {


    private var gradeSelectAdapter: GradeSelectAdapter? = null

    private var gradeInfo: GradeInfo? = null


    override fun getWidth(): Float {
        return 0.8f
    }

    override fun getAnimationId(): Int {
        return 0
    }

    override fun getHeight(): Int {
        return ViewGroup.LayoutParams.WRAP_CONTENT
    }

    override fun getLayoutId(): Int {
        return R.layout.frament_edit_info

    }

    override fun init() {
        mPresenter = EditInfoPresenter(activity as BaseActivity<*>, this)
        tv_info_title.text = mTitle


        if (mIsShow) {
            grade_recyclerView.visibility = View.VISIBLE
            et_info.visibility = View.GONE

            grade_recyclerView.layoutManager = GridLayoutManager(activity, 3)

            gradeSelectAdapter = GradeSelectAdapter(null)
            grade_recyclerView.adapter = gradeSelectAdapter

            grade_recyclerView.addItemDecoration(ItemDecorationHelper(activity as BaseActivity<*>, 10, 10))

            gradeSelectAdapter!!.onItemClickListener = BaseQuickAdapter.OnItemClickListener { _, view, position ->
                resetState()
                gradeInfo = gradeSelectAdapter!!.getItem(position)
                view!!.isSelected = true
            }
        } else {
            grade_recyclerView.visibility = View.GONE
            et_info.visibility = View.VISIBLE
            et_info.hint = mTitle
        }
        RxView.clicks(tv_cancel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { dismiss() }

        RxView.clicks(tv_confirm).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (mIsShow)
                mPresenter.updateInfo(UserInfoHelper.getUid(), "", "", "", gradeInfo?.grade_id)
            else {
                val nickname = et_info.text.toString().trim()
                mPresenter.updateInfo(UserInfoHelper.getUid(), nickname, "", "", "")
            }
        }


    }

    private fun resetState() {
        val count = grade_recyclerView.childCount
        for (i in 0..count) {
            val view = grade_recyclerView.getChildAt(i)
            view?.isSelected = false
        }

    }


    override fun showGradeInfos(t: List<GradeInfo>?) {
        gradeSelectAdapter!!.setNewData(t)
        var gradeId = UserInfoHelper.getUserInfo().grade_id
        if (gradeId == 0) {
            gradeId = 1
        }

        gradeInfo = t!![gradeId - 1]
    }


    override fun showSuccess() {


        dismiss()
    }

    override fun showLoadingDialog(mess: String?) {
        val activity = activity as BaseActivity<*>
        activity.showLoadingDialog(mess)


    }

    override fun dismissDialog() {
        val activity = activity as BaseActivity<*>
        activity.dismissDialog()
    }

    var mIsShow: Boolean = true
    var mTitle: String? = ""

    fun setShowList(isShow: Boolean, title: String?) {
        this.mIsShow = isShow
        this.mTitle = title
    }


}