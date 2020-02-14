package yc.com.homework.index.fragment


import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v4.content.ContextCompat
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView

import com.jakewharton.rxbinding.view.RxView
import com.kk.utils.ScreenUtil
import com.kk.utils.ToastUtil

import java.util.concurrent.TimeUnit

import rx.functions.Action1
import yc.com.blankj.utilcode.util.SPUtils
import yc.com.homework.R
import yc.com.homework.base.config.SpConstant

/**
 * Created by wanglin  on 2019/4/12 14:40.
 */
class IndexDialogFragment : DialogFragment() {

    private var rootView: View? = null

    private var mChecked: Boolean = false

    protected val width: Float
        get() = 0.8f


    protected val animationId: Int
        get() = R.style.share_anim

    val height: Int
        get() = ScreenUtil.getHeight(activity) * 3 / 5

    protected val gravity: Int
        get() = Gravity.CENTER

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val window = dialog.window


        if (rootView == null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            rootView = inflater.inflate(R.layout.fragment_index_dialog, container, false)
            //            window.setLayout((int) (RxDeviceTool.getScreenWidth(getActivity()) * getWidth()), getHeight());//这2行,和上面的一样,注意顺序就行;
            window.setWindowAnimations(animationId)
        }
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)

        initView()


        return rootView

    }


    fun getView(resId: Int): View? {
        return rootView?.findViewById(resId)
    }

    override fun onStart() {
        super.onStart()
        val window = dialog.window
        if (window != null) {
            window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))//注意此处

            val layoutParams = window.attributes

            layoutParams.width = (ScreenUtil.getWidth(activity) * width).toInt()
            layoutParams.height = height
            window.attributes = layoutParams
        }

    }

    protected fun initView() {


        val cb = getView(R.id.cb_privacy) as CheckBox

        val tvEnterApp = getView(R.id.tv_enter_app) as TextView
        mChecked = cb.isChecked

        cb.setOnCheckedChangeListener { buttonView, isChecked ->
            mChecked = isChecked
            if (isChecked) {
                tvEnterApp.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.app_selected_color))
            } else {
                tvEnterApp.setBackgroundColor(ContextCompat.getColor(activity!!, R.color.gray_ddd))
            }
        }


        RxView.clicks(tvEnterApp).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            if (mChecked) {
                dismiss()
                //                SharePreferenceUtils.getInstance().putBoolean(Config.index_dialog, true);
                SPUtils.getInstance().put(SpConstant.index_dialog, true)
            } else {
                ToastUtil.toast2(activity, "请先同意用户协议")
            }
        }


        //        RxView.clicks(view).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> dismiss());
        //
        //        RxView.clicks(rootView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(aVoid -> {
        //            String str = PreferenceUtil.getImpl(getActivity()).getString(Config.ADV_INFO, "");
        //            final AdvInfo advInfo = JSON.parseObject(str, AdvInfo.class);
        //            if (null != advInfo) {
        //                Intent intent = new Intent(getActivity(), AdvInfoActivity.class);
        //                intent.putExtra("url", advInfo.getUrl());
        //                intent.putExtra("title", advInfo.getButton_txt());
        //                startActivity(intent);
        //            }
        //            dismiss();
        //        });
    }
}
