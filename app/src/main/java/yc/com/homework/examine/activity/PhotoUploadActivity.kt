package yc.com.homework.examine.activity

import android.content.Intent
import android.graphics.BitmapFactory
import com.jakewharton.rxbinding.view.RxView
import kotlinx.android.synthetic.main.activity_photo_upload.*
import yc.com.base.BaseActivity
import yc.com.base.StatusBarUtil
import yc.com.homework.R
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.examine.contract.UploadHomeworkContract
import yc.com.homework.examine.fragment.UploadCountTintFragment
import yc.com.homework.examine.presenter.UploadHomeworkPresenter
import java.io.File
import java.util.concurrent.TimeUnit

/**
 *
 * Created by wanglin  on 2018/11/28 18:45.
 */
class PhotoUploadActivity : BaseActivity<UploadHomeworkPresenter>(), UploadHomeworkContract.View {


    var result: String? = ""

    private var subject: String? = "1"

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_photo_upload
    }

    override fun init() {
        mPresenter = UploadHomeworkPresenter(this, this)
        StatusBarUtil.setStatusTextColor1(true, this)
        commonToolBar.showNavigationIcon()
        commonToolBar.init(this)
        commonToolBar.setTitle(getString(R.string.upload))

        result = intent.getStringExtra("result")

//        iv_result.setImageURI(Uri.parse(result))

        val bitmap = BitmapFactory.decodeFile(result)

        com.kk.utils.LogUtil.msg("width: ${bitmap.width}  height:  ${bitmap.height}")


        iv_result.setImageBitmap(bitmap)

        initListener()
        tv_yuwen.isSelected = true

    }

    fun initListener() {
        RxView.clicks(tv_yuwen).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            resetViewState()
            subject = "1"
            tv_yuwen.isSelected = true
        }
        RxView.clicks(tv_mathematics).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            resetViewState()
            subject = "2"
            tv_mathematics.isSelected = true
        }
        RxView.clicks(tv_english).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            resetViewState()
            subject = "3"
            tv_english.isSelected = true
        }

        RxView.clicks(tv_upload).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val file = File(result)
            mPresenter.uploadHomework(file, subject, "1", "${UserInfoHelper.getUserInfo().grade_id}", "0")
        }

        RxView.clicks(iv_result).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(this, PreviewActivity::class.java)
            intent.putExtra("img", result)
            startActivity(intent)

        }

    }


    private fun resetViewState() {
        tv_yuwen.isSelected = false
        tv_mathematics.isSelected = false
        tv_english.isSelected = false
    }

    override fun showCountNotEnough() {
        val uploadCountTintFragment = UploadCountTintFragment()
        uploadCountTintFragment.show(supportFragmentManager, "")
    }


}