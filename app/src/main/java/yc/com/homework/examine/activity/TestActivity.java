package yc.com.homework.examine.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.vondear.rxtools.RxPhotoTool;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hzw.doodle.DoodleActivity;
import cn.hzw.doodle.DoodleParams;
import cn.hzw.doodle.DoodleView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.homework.R;
import yc.com.homework.examine.utils.IvAvatarHelper;
import yc.com.homework.examine.widget.ZoomImageView;

/**
 * Created by wanglin  on 2018/11/21 11:02.
 */
public class TestActivity extends BaseActivity {
    @BindView(R.id.zoomImageView)
    ZoomImageView zoomImageView;


    private static final String TAG = "TestActivity";

    public static final int REQ_CODE_DOODLE = 101;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    public void init() {
        RxPhotoTool.openCameraImage(this);

        initListener();

    }

    /**
     * listener操作
     */
    private void initListener() {

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case RxPhotoTool.GET_IMAGE_BY_CAMERA://选择照相机之后的处理
                if (resultCode == RESULT_OK) {
                    /* data.getExtras().get("data");*/
//                    RxPhotoTool.cropImage(TestActivity.this, RxPhotoTool.imageUriFromCamera, 9, 16, 1000, 1000);// 裁剪图片
                    IvAvatarHelper.initUCrop(TestActivity.this, RxPhotoTool.imageUriFromCamera);
                }

                break;
            case UCrop.REQUEST_CROP://UCrop裁剪之后的处理
                if (resultCode == RESULT_OK) {
                    Uri resultUri = UCrop.getOutput(data);
                    String path = RxPhotoTool.getImageAbsolutePath(TestActivity.this, resultUri);
                    startScrawl(data, path);
//                    Glide.with(TestActivity.this).load(path).asBitmap().into(zoomImageView);
                }
                break;
            case RxPhotoTool.CROP_IMAGE://普通裁剪后的处理
                String originPath = RxPhotoTool.getImageAbsolutePath(TestActivity.this, RxPhotoTool.cropImageUri);
                startScrawl(data, originPath);
//
//                Glide.with(TestActivity.this).load(originPath).asBitmap().into(zoomImageView);
                break;

            case REQ_CODE_DOODLE:
                if (data == null) {
                    return;
                }
                if (resultCode == DoodleActivity.RESULT_OK) {
                    String path = data.getStringExtra(DoodleActivity.KEY_IMAGE_PATH);
                    if (TextUtils.isEmpty(path)) {
                        return;
                    }

                    Glide.with(TestActivity.this).load(path).asBitmap().into(zoomImageView);
//                ImageLoader.getInstance(this).display(findViewById(R.id.img), path);

                } else if (resultCode == DoodleActivity.RESULT_ERROR) {
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }

    /**
     * 开始涂鸦
     */
    private void startScrawl(Intent data, String originPath) {
        if (data == null) {
            return;
        }
        // 涂鸦参数
        DoodleParams params = new DoodleParams();
        params.mIsFullScreen = true;
        // 图片路径
        params.mImagePath = originPath;
        // 初始画笔大小
        params.mPaintUnitSize = DoodleView.DEFAULT_SIZE;
        // 启动涂鸦页面
        DoodleActivity.startActivityForResult(TestActivity.this, params, REQ_CODE_DOODLE);


    }


}
