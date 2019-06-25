package yc.com.homework.base.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.support.v4.view.ViewPager
import android.support.v7.app.AlertDialog
import com.tencent.bugly.Bugly
import com.umeng.socialize.UMShareAPI
import com.vondear.rxtools.RxPermissionsTool
import kotlinx.android.synthetic.main.activity_main.*
import yc.com.answer.index.ui.fragment.IndexAnswerFragment
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.base.adapter.MainAdapter
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.examine.fragment.ExitFragment
import yc.com.homework.index.fragment.IndexFragment
import yc.com.homework.mine.fragment.MineMainFragment
import yc.com.homework.welfare.fragment.WelfareMainFragment


/**
 *
 * Created by wanglin  on 2018/11/19 11:20.
 */
class MainActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {

    private var fragmentList: ArrayList<Fragment>? = null

    override fun init() {
//        LogUtil.msg("height:   $statusHeight")
        applyPermission()
        Bugly.init(this, "e457b6710d", false)
        fragmentList = arrayListOf()
        fragmentList?.add(IndexFragment())
        fragmentList?.add(IndexAnswerFragment())
        fragmentList?.add(WelfareMainFragment())
        fragmentList?.add(MineMainFragment())

        viewPager.adapter = MainAdapter(supportFragmentManager, fragmentList)
        viewPager.offscreenPageLimit = 3

        viewPager.currentItem = 0
        UserInfoHelper.setAlias(this)

        initListener()


    }


    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data)
    }

    private fun initListener() {

        viewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                mainBottomBar.setItem(position)
            }
        })
        mainBottomBar.setOnItemSelectListener {
            viewPager.currentItem = it
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        ToastUtils.cancel()
        UMShareAPI.get(this).release()
    }


    private fun applyPermission() {
        RxPermissionsTool.with(this)
                .addPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .addPermission(Manifest.permission.CAMERA)
                .addPermission(Manifest.permission.RECORD_AUDIO).initPermission()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {

            var isGrant = true//设置权限是否同意
            if (grantResults.isNotEmpty()) {
                grantResults.forEach {
                    if (it != PackageManager.PERMISSION_GRANTED) {
                        isGrant = false
                    }
                }
            }
            var alertDialog: AlertDialog? = null
            if (!isGrant) {

                var isShouldShowRequest = false
                permissions.forEach {
                    //用户不同意，向用户展示该权限作用
                    val rationale = ActivityCompat.shouldShowRequestPermissionRationale(this, it)
//                    if (requestPermissionCount >= 1 && !rationale) {
//                        val intent = Intent()
//
//                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                        intent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
//                        intent.data = Uri.fromParts("package", packageName, null)
//
//                        startActivity(intent)
//                    }

                    if (rationale) {
                        isShouldShowRequest = true
                    }
                }
                if (isShouldShowRequest) {
                    val dialog = AlertDialog.Builder(this)
                    dialog.setTitle(getString(R.string.tint))?.setMessage("必须先允许相关权限才能使用")
                    dialog.setPositiveButton("去设置") { _, _ ->
                        ActivityCompat.requestPermissions(this, permissions, 1)
                    }
                    alertDialog = dialog.create()
                    alertDialog?.setCanceledOnTouchOutside(false)
                    alertDialog?.setCancelable(false)
                    if (!alertDialog?.isShowing!!)
                        alertDialog.show()
                }

            } else {
                if (alertDialog != null && alertDialog.isShowing) {
                    alertDialog.dismiss()
                }
            }
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let {
            val pos = intent.getIntExtra("pos", 1)
            viewPager.currentItem = pos
        }
    }




    override fun onBackPressed() {
        val exitFragment = ExitFragment()
        val mTitle = String.format(getString(R.string.is_exit), getString(R.string.app_name))
        exitFragment.setTitle(mTitle).setOnConfirmListener(object : ExitFragment.OnConfirmListener {
            override fun onConfirm() {
                exitFragment.dismiss()
                finish()
                System.exit(0)
            }
        }).show(supportFragmentManager, "")
    }


}