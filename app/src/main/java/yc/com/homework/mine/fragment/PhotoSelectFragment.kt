package yc.com.homework.mine.fragment

import android.widget.TextView
import butterknife.BindView
import com.jakewharton.rxbinding.view.RxView
import com.vondear.rxtools.RxPhotoTool
import kotlinx.android.synthetic.main.fragment_select_photo.*
import yc.com.base.*
import yc.com.homework.R
import yc.com.homework.mine.utils.AvatarHelper
import java.util.concurrent.TimeUnit

/**
 * Created by wanglin  on 2018/12/6 09:54.
 */
class PhotoSelectFragment : BaseBottomSheetDialogFragment<BasePresenter<BaseEngine, BaseView>>() {

    private var mType = 0

    @BindView(R.id.tv_take_photo)
    lateinit var tvTakePhoto:TextView
    @BindView(R.id.tv_take_album)
    lateinit var tvTakeAlbum:TextView

    fun setType(type: Int) {
        this.mType = type
    }

    override fun getLayoutId(): Int {
        return R.layout.fragment_select_photo
    }

    override fun init() {
        RxView.clicks(tvTakePhoto).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //拍照
            AvatarHelper.setType(mType)
            RxPhotoTool.openCameraImage(activity)
            dismiss()
        }
        RxView.clicks(tvTakeAlbum).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe {
            //从相册选择
            AvatarHelper.setType(mType)
            AvatarHelper.openAlbum(activity as BaseActivity<*>)
            dismiss()
        }

    }

}
