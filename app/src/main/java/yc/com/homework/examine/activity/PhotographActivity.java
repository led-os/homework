package yc.com.homework.examine.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.hardware.Camera;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.edmodo.cropper.CropImageView;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.LogUtil;
import com.kk.utils.ScreenUtil;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseActivity;
import yc.com.homework.R;
import yc.com.homework.base.HomeworkApp;
import yc.com.homework.examine.utils.CameraManger;
import yc.com.homework.examine.utils.DataUtils;
import yc.com.homework.examine.utils.SensorManger;
import yc.com.homework.examine.widget.FocusView;
import yc.com.homework.mine.utils.AvatarHelper;
import yc.com.homework.mine.utils.ImageUtils;

/**
 * Created by wanglin  on 2018/11/28 14:26.
 */
public class PhotographActivity extends BaseActivity implements SensorManger.OnAccelSensorListener, Camera.AutoFocusCallback, Camera.PictureCallback {
    private static final String TAG = "TAG";
    @BindView(R.id.surfaceView)
    SurfaceView surfaceView;
    @BindView(R.id.iv_light)
    ImageView ivLight;
    @BindView(R.id.ll_album)
    LinearLayout llAlbum;
    @BindView(R.id.ll_sure)
    LinearLayout llSure;
    @BindView(R.id.ll_close)
    LinearLayout llClose;
    @BindView(R.id.root_layout)
    RelativeLayout rootLayout;
    @BindView(R.id.iv_album)
    ImageView ivAlbum;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_take_picture)
    ImageView ivTakePicture;
    @BindView(R.id.iv_sure)
    ImageView ivSure;

    @BindView(R.id.cropImageView)
    CropImageView cropImageView;


    private SurfaceHolder holder;
    private int screenWidth;
    private int screenHeight;
    private String screenRate;
    private OrientationEventListener orientationEventListener;
    private FocusView focusView;
    private boolean isTouch;
    private Point point;

    private float startDis;
    /**
     * 记录是拖拉照片模式还是放大缩小照片模式
     */

    private static final int MODE_INIT = 0;
    /**
     * 放大缩小照片模式
     */
    private static final int MODE_ZOOM = 1;
    private int mode = MODE_INIT;// 初始状态


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_photograph;
    }

    @Override
    public void init() {
        startCamera();

        initListener();
    }

    private void startCamera() {
        holder = surfaceView.getHolder();

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        holder.setKeepScreenOn(true);//屏幕长亮

        screenWidth = ScreenUtil.getWidth(this);
        screenHeight = ScreenUtil.getHeight(this);
        holder.addCallback(callBack);////为SurfaceView的句柄添加一个回调函数


        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(160, 160);
        focusView = new FocusView(this);
        focusView.setLayoutParams(layoutParams);

        SensorManger.getIntance().initSensorManager(this);
        SensorManger.getIntance().setAccelSensorListener(this);
        screenRate = getSurfaceViewSize(screenWidth, screenHeight);
        setSurfaceViewSize(screenRate);

        orientationEventListener = new OrientationEventListener(this) {
            @Override
            public void onOrientationChanged(int orientation) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) {
                    return;  //手机平放时，检测不到有效的角度
                }
                //只检测是否有四个角度的改变
                if (orientation > 350 || orientation < 10) { //0度
                    orientation = 90;
                } else if (orientation > 80 && orientation < 100) { //90度
                    orientation = 0;
                } else if (orientation > 170 && orientation < 190) { //180度
                    orientation = 270;
                } else if (orientation > 260 && orientation < 280) { //270度
                    orientation = 180;
                } else {
                    orientation = 0;
                }
                DataUtils.degree = orientation;
            }
        };
        SensorManger.getIntance().lockFocus();


    }


    private void initListener() {
        RxView.clicks(ivLight).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                CameraManger.getInstance().flashlight();

            }
        });

        //打开相册
        RxView.clicks(llAlbum).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (ivAlbum.getVisibility() == View.VISIBLE) {//打开相册
                    AvatarHelper.INSTANCE.selectAlbum(PhotographActivity.this);

                } else if (ivBack.getVisibility() == View.VISIBLE) {//返回
                    CameraManger.getInstance().startPreview();
                    setViewState(true);
                }
            }
        });

        //拍照
        RxView.clicks(llSure).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                if (ivTakePicture.getVisibility() == View.VISIBLE) {
                    //锁定焦点
                    SensorManger.getIntance().lockFocus();
                    //拍照
                    CameraManger.getInstance().takePicture(PhotographActivity.this, PhotographActivity.this);
                } else {
                    cropView();
                }


            }
        });

        RxView.clicks(llClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                destroyCamera();
                finish();
            }
        });

    }

    /**
     * @param flag true 原始状态
     *             false 已拍照状态
     */
    private void setViewState(boolean flag) {
        ivBack.setVisibility(flag ? View.GONE : View.VISIBLE);
        ivAlbum.setVisibility(flag ? View.VISIBLE : View.GONE);
        ivSure.setVisibility(flag ? View.GONE : View.VISIBLE);
        ivTakePicture.setVisibility(flag ? View.VISIBLE : View.GONE);
        ivLight.setVisibility(flag ? View.VISIBLE : View.GONE);
        cropImageView.setVisibility(flag ? View.GONE : View.VISIBLE);

    }


    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    private int getPreviewDegree() {
        // 获得手机的方向
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    /**
     * 根据分辨率设置预览SurfaceView的大小以防止变形
     *
     * @param surfaceSize
     */
    private void setSurfaceViewSize(String surfaceSize) {
        ViewGroup.LayoutParams params = surfaceView.getLayoutParams();
        if (surfaceSize.equals("16:9")) {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (surfaceSize.equals("4:3")) {
            params.height = 4 * screenWidth / 3;
        }
        surfaceView.setLayoutParams(params);
    }


    public String getSurfaceViewSize(int width, int height) {
        if (equalRate(width, height, 1.33f)) {
            return "4:3";
        } else {
            return "16:9";
        }
    }

    public boolean equalRate(int width, int height, float rate) {
        float r = (float) width / (float) height;
        if (Math.abs(r - rate) <= 0.2) {
            return true;
        } else {
            return false;
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (orientationEventListener.canDetectOrientation()) {
            orientationEventListener.enable();
        }
        SensorManger.getIntance().registerListener();

    }

    @Override
    protected void onPause() {
        super.onPause();
        orientationEventListener.disable();
        SensorManger.getIntance().unRegisterListener();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = MODE_INIT;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                if (event.getPointerCount() < 2)
                    return super.onTouchEvent(event);
                mode = MODE_ZOOM;
                startDis = spacing(event);
                break;
            case MotionEvent.ACTION_MOVE:
                if (mode == MODE_ZOOM) {
                    if (event.getPointerCount() < 2)
                        return super.onTouchEvent(event);
                    float endDis = spacing(event);
                    int scale = (int) ((endDis - startDis) / 10f);
                    if (scale >= 1 || scale <= -1) {
                        CameraManger.getInstance().setCameraZoom(scale);
                        startDis = endDis;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                if (mode == MODE_INIT) {
                    point = new Point((int) event.getX(), (int) event.getY());
                    SensorManger.getIntance().lockFocus();
                    isTouch = CameraManger.getInstance().setCameraFocusAreas(point);
                    if (isTouch) {
                        CameraManger.getInstance().setCameraAutoFocus(PhotographActivity.this);
                    }
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 两点的距离
     */
    private float spacing(MotionEvent event) {
        if (event == null) {
            return 0;
        }
        float x = event.getX(0) - event.getX(1);
        float y = event.getY(0) - event.getY(1);

        return (float) Math.sqrt(x * x + y * y);
    }


    private void destroyCamera() {
        CameraManger.getInstance().destroyCamera();
        holder.getSurface().release();
        surfaceView = null;
    }

    @Override
    public void onAccelSensor() {
        CameraManger.getInstance().setCameraAutoFocus(this);
    }

    @Override
    public void onAutoFocus(boolean success, Camera camera) {
        if (success) {
            SensorManger.getIntance().unLockFocus();
            rootLayout.removeView(focusView);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) focusView.getLayoutParams();
            if (!isTouch) {
                params.addRule(RelativeLayout.CENTER_IN_PARENT);
            } else {
                isTouch = false;
                if (point != null) {
                    params.leftMargin = point.x - 30;
                    params.topMargin = point.y - 30;

                } else {
                    params.addRule(RelativeLayout.CENTER_IN_PARENT);
                }
            }
            rootLayout.addView(focusView, params);

            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    rootLayout.removeView(focusView);
                }
            }, 1000);

        }

    }

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        CameraManger.getInstance().stopPreview();
        //将拍摄到的照片给自定义的对象
        DataUtils.tempImageData = data;

        SensorManger.getIntance().unLockFocus();

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        Bitmap bm = ImageUtils.rotateImageView(90, bitmap);
//        Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bm, null, null));
        showCropView(bm);

    }

    private void showCropView(Bitmap bitmap) {
        setViewState(false);

        cropImageView.setVisibility(View.VISIBLE);
        cropImageView.setAspectRatio(1, 1);
        cropImageView.setGuidelines(1);
        cropImageView.animate().alpha(1).setDuration(300).setInterpolator(new AccelerateInterpolator());
        cropImageView.setImageBitmap(bitmap);


    }


    public Uri bitmap2uri(Bitmap b) {



        File cacheFile;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cacheFile = getExternalCacheDir();
        } else {
            cacheFile = getCacheDir();
        }

        File path = new File(cacheFile + File.separator + System.currentTimeMillis() + ".png");

        OutputStream os = null;
        try {
            os = new FileOutputStream(path);
            b.compress(Bitmap.CompressFormat.JPEG, 100, os);
            return Uri.fromFile(path);
        } catch (Exception e) {
            LogUtil.msg("uri error-->" + e.getMessage());
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    private void cropView() {
        Bitmap croppedImage = cropImageView.getCroppedImage();

        Uri uri = bitmap2uri(croppedImage);
        setIntent(uri.getPath());
        LogUtil.msg("path: " + uri.getPath());

    }


    private void setIntent(String path) {
        Intent intent = new Intent(PhotographActivity.this, PhotoUploadActivity.class);
        intent.putExtra("result", path);
        startActivity(intent);
        finish();
    }

    private SurfaceHolder.Callback callBack = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            CameraManger.getInstance().openCamera(holder, getPreviewDegree());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            LogUtil.msg("screenWidth =" + screenWidth + "  width =" + width + "  screenHeight  =" + screenHeight + "  height= " + height);
            CameraManger.getInstance().setCameraParameters(screenWidth, screenHeight, PhotographActivity.this);
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            destroyCamera();
        }
    };


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        AvatarHelper.INSTANCE.onActivityForResult(PhotographActivity.this, requestCode, resultCode, data, new AvatarHelper.IAvatar() {
            @Override
            public void uploadAvatar(@NotNull String image) {
                CameraManger.getInstance().stopPreview();
                SensorManger.getIntance().unLockFocus();

                Bitmap bitmap = BitmapFactory.decodeFile(image);
                int size = bitmap.getByteCount();
                LogUtil.msg("bitmap  before: width:  " + bitmap.getWidth() + "   height:  " + bitmap.getHeight() + " size " + size);
                Bitmap result = ImageUtils.compressBitmap(image, screenWidth, screenHeight);
                showCropView(result);

            }
        }, false);
    }


}
