package yc.com.homework.mine.activity


import android.annotation.SuppressLint
import android.content.Intent
import android.support.v7.view.ContextThemeWrapper
import android.support.v7.widget.PopupMenu
import android.view.Gravity
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_apply_teacher_detail.*
import yc.com.base.BaseActivity
import yc.com.base.StatusBarUtil
import yc.com.homework.R
import yc.com.homework.base.utils.GlideHelper
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.mine.contract.ApplyTeacherDetailContract
import yc.com.homework.mine.domain.bean.ApplyRuleInfo
import yc.com.homework.mine.domain.bean.TeacherApplyState
import yc.com.homework.mine.domain.bean.UploadResultInfo
import yc.com.homework.mine.fragment.PhotoSelectFragment
import yc.com.homework.mine.presenter.ApplyTeacherDetailPresenter
import yc.com.homework.mine.utils.AvatarHelper
import java.util.concurrent.TimeUnit


/**
 *
 * Created by wanglin  on 2018/11/26 08:49.
 */
class ApplyTeacherDetailActivity : BaseActivity<ApplyTeacherDetailPresenter>(), ApplyTeacherDetailContract.View {


    private var sex: Int = 1
    private var job: Int = 1
    private var teacher_url: String? = null
    private var diploma: String? = null
    private var isAgree: Boolean = true

    override fun getLayoutId(): Int {
        return R.layout.activity_apply_teacher_detail
    }

    override fun init() {
        mPresenter = ApplyTeacherDetailPresenter(this, this)

        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.setTitle(getString(R.string.input_apply_info))
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)

        iv_protocol_select.isSelected = true

        initListener()


    }

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    private fun initListener() {

        RxView.clicks(iv_protocol_select).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            var isSelect = iv_protocol_select.isSelected
            isSelect = !isSelect
            iv_protocol_select.isSelected = isSelect
        }

        RxView.clicks(tv_protocol).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, ProtocolActivity::class.java)
            startActivity(intent)
        }
        RxView.clicks(rl_subject).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { showPopMenu() }

        radioGroup_sex.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton_male -> {
                    radioButton_female.isChecked = false
                    sex = 1
                }
                R.id.radioButton_female -> {
                    radioButton_male.isChecked = false
                    sex = 2
                }
            }
        }
        radioGroup_job.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radioButton_education -> {
                    radioButton_online.isChecked = false
                    job = 1
                }
                R.id.radioButton_online -> {
                    radioButton_education.isChecked = false
                    job = 2
                }
            }

        }

        RxView.clicks(tv_submit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {


            val name = et_name.text.toString().trim()
            val phone = et_phone.text.toString().trim()
            val subject = tv_subject.text.toString().trim()

            if (isAgree)
                mPresenter.applyTeacher(name, sex, phone, getSubjectId(subject), job, teacher_url, diploma)
            else
                ToastUtils.showCenterToast(this, "请先勾选同意" + getString(R.string.app_name) + "兼职协议")
        }

        RxView.clicks(iv_teacher_licence).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            val photoSelectFragment = PhotoSelectFragment()
            photoSelectFragment.setType(1)
            photoSelectFragment.show(supportFragmentManager, "")

        }
        RxView.clicks(iv_diploma).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            val photoSelectFragment = PhotoSelectFragment()
            photoSelectFragment.setType(2)
            photoSelectFragment.show(supportFragmentManager, "")
        }

        RxView.clicks(iv_protocol_select).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            isAgree = !isAgree
            iv_protocol_select.isSelected = isAgree
        }

    }

    private fun getSubjectId(subject: String): Int {
        return when (subject) {
            getString(R.string.yuwen) -> 1
            getString(R.string.mathematics) -> 2
            getString(R.string.english) -> 3
            else -> 0
        }
    }

    private var popupMenu: PopupMenu? = null

    @SuppressLint("RestrictedApi")
    private fun showPopMenu() {
        if (popupMenu == null) {
            popupMenu = PopupMenu(this, rl_subject)


            val context = ContextThemeWrapper(this, R.style.popupMenuStyle)


            popupMenu = PopupMenu(context, rl_subject, Gravity.RIGHT)

            val menuInflater = popupMenu!!.menuInflater
            menuInflater.inflate(R.menu.select_subject_menu, popupMenu!!.menu)

            popupMenu!!.setOnMenuItemClickListener { item ->
                ToastUtils.showCenterToast(this@ApplyTeacherDetailActivity, item!!.title)
                tv_subject.text = item.title
                false
            }
        }
        popupMenu!!.show()
    }

    override fun showTeacherRuleInfo(data: ApplyRuleInfo?) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        AvatarHelper.onActivityForResult(this@ApplyTeacherDetailActivity, requestCode, resultCode, data, object : AvatarHelper.IAvatar {
            override fun uploadAvatar(image: String) {
                mPresenter.uploadPic(image)
            }
        }, false)
    }

    override fun showUploadResult(data: UploadResultInfo?) {
        when (AvatarHelper.getType()) {
            1 -> {
                GlideHelper.circleIvator(this, iv_teacher_licence, data!!.file_path, R.mipmap.pic_add_icon, R.mipmap.pic_add_icon, false)
                teacher_url = data.file_path
            }
            2 -> {
                GlideHelper.circleIvator(this, iv_diploma, data!!.file_path, R.mipmap.pic_add_icon, R.mipmap.pic_add_icon, false)
                diploma = data.file_path
            }
        }
    }

    override fun showState(data: TeacherApplyState?) {

    }

    override fun showLoading() {

    }

    override fun showNoNet() {
    }

    override fun showNoData() {
    }

    override fun hide() {
    }
}