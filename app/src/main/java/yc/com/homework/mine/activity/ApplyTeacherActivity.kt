package yc.com.homework.mine.activity

import android.content.Intent
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_apply_teacher_guide.*


import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.mine.contract.ApplyTeacherDetailContract
import yc.com.homework.mine.domain.bean.ApplyRuleInfo
import yc.com.homework.mine.domain.bean.TeacherApplyState
import yc.com.homework.mine.domain.bean.UploadResultInfo
import yc.com.homework.mine.presenter.ApplyTeacherDetailPresenter
import java.util.concurrent.TimeUnit


/**
 *
 * Created by wanglin  on 2018/11/24 12:15.
 */
class ApplyTeacherActivity : BaseActivity<ApplyTeacherDetailPresenter>(), ApplyTeacherDetailContract.View {


    override fun init() {

        mPresenter = ApplyTeacherDetailPresenter(this, this)
        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle(getString(R.string.apply_teacher))

        RxView.clicks(tv_apply).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            mPresenter.getApplyState()

        }

    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_teacher_guide
    }

    override fun showTeacherRuleInfo(data: ApplyRuleInfo?) {
        tv_qualification.text = data!!.rjzg?.desp
        tv_characteristic.text = data.gztd?.desp
    }

    override fun showState(data: TeacherApplyState?) {
        when (data!!.status) {
            1, 2 -> {
                ToastUtils.showCenterToast(this, "您已申请，请耐心等待...")
            }
            else -> {
                val intent = Intent(this, ApplyTeacherDetailActivity::class.java)
                startActivity(intent)
            }
        }

    }

    override fun showUploadResult(data: UploadResultInfo?) {

    }

    override fun showLoading() {
        stateView.showLoading(rl_content)
    }

    override fun showNoNet() {
        stateView.showNoNet(rl_content) {
            mPresenter.loadData(true)
        }
    }

    override fun showNoData() {
        stateView.showNoData(rl_content)
    }

    override fun hide() {
        stateView.hide()
    }
}