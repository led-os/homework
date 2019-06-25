package yc.com.homework.mine.fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.support.v4.content.ContextCompat
import android.text.Spannable
import android.text.SpannableString
import android.text.TextPaint
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.URLSpan
import android.text.style.UnderlineSpan
import android.view.View
import com.jakewharton.rxbinding.view.RxView
import com.umeng.analytics.MobclickAgent
import com.vondear.rxtools.RxAppTool
import com.vondear.rxtools.RxTimeTool
import kotlinx.android.synthetic.main.fragment_mine_main.*
import yc.com.base.BaseActivity
import yc.com.base.BaseFragment
import yc.com.homework.R
import yc.com.homework.base.user.UserInfo
import yc.com.homework.base.user.UserInfoHelper
import yc.com.homework.base.utils.GlideHelper
import yc.com.base.ObservableManager
import yc.com.homework.base.config.Constant
import yc.com.homework.base.utils.ToastUtils
import yc.com.homework.index.domain.bean.IndexInfo
import yc.com.homework.mine.activity.*
import yc.com.homework.mine.contract.MineMainContract
import yc.com.homework.mine.domain.bean.ShareInfo
import yc.com.homework.mine.domain.bean.UnreadMsgInfo
import yc.com.homework.mine.presenter.MineMainPresenter
import yc.com.homework.mine.utils.ImageUtils
import yc.com.homework.mine.utils.MyUrlSpan
import yc.com.homework.mine.utils.PhoneUtils
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Created by wanglin  on 2018/11/23 13:47.
 */
class MineMainFragment : BaseFragment<MineMainPresenter>(), MineMainContract.View, Observer {


    private var mBitmap: Bitmap? = null
    override fun getLayoutId(): Int {
        return R.layout.fragment_mine_main
    }

    override fun init() {

        ObservableManager.get().addMyObserver(this)
        mPresenter = MineMainPresenter(activity as BaseActivity<*>, this)


//        //修改链接颜色，并去掉下划线
//        tv_phone.setLinkTextColor(ContextCompat.getColor(activity, R.color.app_selected_color))
//
//        val spannableString = tv_phone.text as Spannable
//        val underlineSpan = MyUrlSpan(tv_phone.text.toString().trim())
//
//        spannableString.setSpan(underlineSpan, 0, spannableString.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)


        RxView.clicks(ll_message).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(activity, MessageActivity::class.java)
            startActivity(intent)
        }

        RxView.clicks(mainMineItemView_coin).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            val intent = Intent(activity, ServiceCardActivity::class.java)
            startActivity(intent)

        }
        RxView.clicks(iv_avator).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (!UserInfoHelper.isGotoLogin(activity)) {
                val intent = Intent(activity, SettingActivity::class.java)
                startActivity(intent)
            }
        }

        RxView.clicks(mainMineItemView_apply).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(activity, ApplyTeacherActivity::class.java)
            startActivity(intent)
        }
        RxView.clicks(mainMineItemView_setting).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            if (!UserInfoHelper.isGotoLogin(activity)) {
                val intent = Intent(activity, SettingActivity::class.java)
                startActivity(intent)
            }
        }

        RxView.clicks(ll_login).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe { UserInfoHelper.isGotoLogin(activity) }
        RxView.clicks(rl_edit).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val editInfoFragment = EditInfoFragment()
            editInfoFragment.setShowList(true, getString(R.string.select_grade))

            editInfoFragment.show(fragmentManager, "")
        }
        RxView.clicks(ll_service_phone).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(Intent.ACTION_DIAL)
            val uri = Uri.parse("tel:${tv_phone.text.toString().trim()}")
            intent.data = uri
            startActivity(intent)
        }

        RxView.clicks(ll_service_wx).throttleFirst(200,TimeUnit.MILLISECONDS).subscribe {
            val cm = activity?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

            cm.primaryClip = ClipData.newPlainText("text", tv_phone.text.toString().trim())
            ToastUtils.showCenterToast(activity as BaseActivity<*>, "复制成功, 正在前往微信")
            val weixin = "com.tencent.mm"
            if (RxAppTool.isInstallApp(activity, weixin)) {
                val intent = activity?.packageManager?.getLaunchIntentForPackage(weixin)
                startActivity(intent)
            } else {
                ToastUtils.showCenterToast(activity as BaseActivity<*>, "还没安装微信，请先安装")
            }
        }


        RxView.clicks(ll_qq).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //加群
            if (!joinQQGroup("ej7D0bBZAxQTY8ej8dIkuRN_JP0kjuvr")) {
                ToastUtils.showCenterToast(activity as BaseActivity<*>, "您的手机未安装qq,请先安装")
            }

        }
        RxView.clicks(mainMineItemView_share).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {

            //            if (!UserInfoHelper.isGotoLogin(activity)) {
            //todo 邀请好友统计事件
            MobclickAgent.onEvent(activity, "friendsinviting_id", "邀请好友")
            val shareFragment = ShareFragment()

//            val shareInfo = ShareInfo()
//            val originBitmap = BitmapFactory.decodeResource(activity.resources, R.mipmap.share_code_bg)
//            val codeBitmap = BitmapFactory.decodeResource(activity.resources, R.mipmap.download_code)
//            val userInfo = UserInfoHelper.getUserInfo()
//            val avatorBitmap: Bitmap? = if (TextUtils.isEmpty(userInfo.face)) {
//                ImageUtils.compressBitmap(BitmapFactory.decodeResource(activity.resources, R.mipmap.deafult_avator))
//            } else {
//                mBitmap
//            }
//            val newBitmap = ImageUtils.compoundPic(originBitmap, avatorBitmap, codeBitmap, Environment.getExternalStorageDirectory().absolutePath, userInfo.inviteCode)
//            shareInfo.bitmap = newBitmap
//            shareFragment.setShareInfo(shareInfo)
            shareFragment.show(childFragmentManager, "")
//            }
        }
        RxView.clicks(mainMineItemView_order).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            val intent = Intent(activity, OrderActivity::class.java)
            startActivity(intent)
        }
    }


    /****************
     *
     * 发起添加群流程。群号：作业啦交流群(856914094) 的 key 为： ej7D0bBZAxQTY8ej8dIkuRN_JP0kjuvr
     * 调用 joinQQGroup(ej7D0bBZAxQTY8ej8dIkuRN_JP0kjuvr) 即可发起手Q客户端申请加群 作业啦交流群(856914094)
     *
     * @param key 由官网生成的key
     * @return 返回true表示呼起手Q成功，返回fals表示呼起失败
     */
    private fun joinQQGroup(key: String): Boolean {
        val intent = Intent()
        intent.data = Uri.parse("mqqopensdkapi://bizAgent/qm/qr?url=http%3A%2F%2Fqm.qq.com%2Fcgi-bin%2Fqm%2Fqr%3Ffrom%3Dapp%26p%3Dandroid%26k%3D$key")
        // 此Flag可根据具体产品需要自定义，如设置，则在加群界面按返回，返回手Q主界面，不设置，按返回会返回到呼起产品界面    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        return try {
            startActivity(intent)
            true
        } catch (e: Exception) {
            // 未安装手Q或安装的版本不支持
            false
        }

    }

    override fun showBitmap(bitmap: Bitmap?) {
        this.mBitmap = bitmap
    }


    override fun showUserInfo(userInfo: UserInfo?) {
        setUserInfo(userInfo)
    }

    override fun showNoLogin() {

    }

    override fun update(o: Observable, arg: Any) {
        when (arg) {
            is UserInfo -> {
                setUserInfo(arg)
                mPresenter.getUnReadMessageCount()
            }
            is String -> {
                if (arg.startsWith("face")) {
                    val face = arg.split("-")[1]
                    GlideHelper.circleIvator(activity as BaseActivity<*>, iv_avator, face, true)
                    mPresenter.covertPathToBitmap(face)
                } else if (TextUtils.equals(Constant.READ_MESSAGE, arg)) {
//                    mPresenter.getUnReadMessageCount()
                    if (mUnreadCount > 0) {
                        mUnreadCount--
                        showUnreadCount(mUnreadCount)
                    }
                }
            }

        }

    }

    private fun setUserInfo(userInfo: UserInfo?) {
        if (userInfo != null) {

            if (!TextUtils.isEmpty(userInfo.face))
                GlideHelper.circleIvator(activity as BaseActivity<*>, iv_avator, userInfo.face, true)
            else {
                iv_avator.setImageResource(R.mipmap.deafult_avator)
            }
            var nickName: String? = userInfo.nickname
            tv_upload_count.text = String.format(getString(R.string.upload_count), userInfo.upload_times)
            if (userInfo.type == 1) {
                if (TextUtils.isEmpty(nickName)) nickName = "游客"
                tv_grade.text = getString(R.string.first_register)
                rl_edit.visibility = View.GONE
                tv_time_limit.visibility = View.GONE
            } else {
                if (TextUtils.isEmpty(nickName)) nickName = PhoneUtils.replacePhone(activity as BaseActivity<*>, userInfo.tel)
                rl_edit.visibility = View.VISIBLE
                if (TextUtils.isEmpty(userInfo.getGrade())) {
                    tv_grade.text = getString(R.string.input_grade)
                } else {
                    tv_grade.text = userInfo.getGrade()
                }

                val vipStartTime = userInfo.vip_start_time
                val vipEndTime = userInfo.vip_end_time

                val sm = SimpleDateFormat("yyyy.MM.dd", Locale.getDefault())
                var starTime = ""
                var endTime = ""
                vipStartTime?.let {
                    if (vipStartTime != 0L)
                        starTime = RxTimeTool.date2String(Date(vipStartTime * 1000), sm)
                }
                vipEndTime?.let {
                    if (vipEndTime != 0L)
                        endTime = RxTimeTool.date2String(Date(vipEndTime * 1000), sm)
                }

                if (TextUtils.isEmpty(starTime) || TextUtils.isEmpty(endTime)) {
                    tv_time_limit.visibility = View.GONE
                } else {
                    tv_time_limit.visibility = View.GONE
                    tv_time_limit.text = String.format(getString(R.string.time_limit), "$starTime-$endTime")
                }
            }
            tv_id.text = nickName
        }


    }

    private var mUnreadCount = 0
    override fun showUnreadMsg(data: UnreadMsgInfo?) {
        if (data != null) {
            val unReadCount = data.notsee
            mUnreadCount = unReadCount
            showUnreadCount(unReadCount)
        }
    }

    private fun showUnreadCount(unReadCount: Int) {
        val message: String
        if (unReadCount <= 0) {
            tv_unread_msg.visibility = View.GONE
        } else {
            tv_unread_msg.visibility = View.VISIBLE
            if (unReadCount > 99) {
                message = "$unReadCount+"
                tv_unread_msg.background = ContextCompat.getDrawable(activity as BaseActivity<*>, R.drawable.message_oval_bg)
            } else {
                message = "$unReadCount"
                tv_unread_msg.background = ContextCompat.getDrawable(activity as BaseActivity<*>, R.drawable.message_circle_bg)
            }

            tv_unread_msg.text = message

        }
    }

    override fun showIndexInfo(data: IndexInfo?) {
        tv_starlight.text = data?.day_words?.words
        tv_teach_count.text = "${data?.day_words?.login_count}"
    }

    override fun onDestroy() {
        super.onDestroy()
        ObservableManager.get().deleteMyObserver(this)
    }


}
