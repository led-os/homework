package yc.com.homework.examine.activity

import android.graphics.BitmapFactory
import butterknife.BindView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import kotlinx.android.synthetic.main.activity_preview_picture.*
import yc.com.base.BaseActivity
import yc.com.base.BaseEngine
import yc.com.base.BasePresenter
import yc.com.base.BaseView
import yc.com.homework.R
import yc.com.homework.examine.widget.ZoomImageView


/**
 * Created by wanglin  on 2018/11/5 16:03.
 */
class PreviewActivity : BaseActivity<BasePresenter<BaseEngine, BaseView>>() {

    override fun isStatusBarMateria(): Boolean {
        return true
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_preview_picture
    }

    override fun init() {
        val img = intent.getStringExtra("img")

        Glide.with(this).load(img).asBitmap().thumbnail(0.1f).diskCacheStrategy(DiskCacheStrategy.RESULT).into(zoomImageView)
//        zoomImageView.setImageBitmap(BitmapFactory.decodeFile(img))

        zoomImageView!!.setOnClickListener { finish() }

    }


}
