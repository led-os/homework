package yc.com.homework.mine.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.ExifInterface;
import android.os.Environment;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.text.TextUtils;

import com.kk.utils.LogUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.homework.R;
import yc.com.homework.base.config.SpConstant;
import yc.com.homework.base.user.UserInfo;
import yc.com.homework.base.user.UserInfoHelper;

/**
 * Created by wanglin  on 2018/11/27 17:58.
 * <p>
 * 合并图片并保存
 */
public class ImageUtils {

    public static Bitmap compoundPic(Bitmap bitmapBg, Bitmap bitmap1, Bitmap bitmap2, String path, String text) {
        Bitmap bitmap = null;
        //将合并后的bitmap3保存为png图片到本地
        FileOutputStream out = null;
        try {
            bitmap = Bitmap.createBitmap(bitmapBg.getWidth(), bitmapBg.getHeight(), bitmapBg.getConfig());
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(bitmapBg, new Matrix(), null);
            int bitmapHeight = bitmapBg.getHeight();
            int bitmapWidth = bitmapBg.getWidth();

            canvas.drawBitmap(bitmap1, bitmapWidth / 2 - bitmap1.getWidth() / 2 - 2, bitmapHeight / 3 - bitmap1.getHeight() / 3 - 3, null);  //120、350为bitmap2写入点的x、y坐标
            canvas.drawBitmap(bitmap2, bitmapWidth / 2 - bitmap2.getWidth() / 2, bitmapHeight * 3 / 5 - 10, null);
            Paint paint = new Paint();
            paint.setColor(Color.parseColor("#b13028"));
            paint.setAntiAlias(true);
            paint.setTextSize(20.0f);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            String paintText = "我的邀请码  " + text;
            float textWidth = paint.measureText(paintText);

//            LogUtil.msg("textWidth: " + textWidth);
            canvas.drawText(paintText, bitmapWidth / 2 - textWidth / 2, bitmapHeight * 3 / 5 + bitmap2.getHeight() + 15, paint);
            out = new FileOutputStream(path + File.separator + "image3.png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

        } catch (Exception e) {
            LogUtil.msg("compoundPic error: " + e.getMessage());
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (bitmap == null) {
            bitmap = bitmapBg;
        }
        if (bitmapBg != bitmap) {
            bitmapBg.recycle();
        }
        return bitmap;

    }


    public static Bitmap compressBitmap(Bitmap bitmap) {
        Matrix matrix = new Matrix();
        float standardSize = 136f;

        int realSize;
        if (bitmap.getWidth() >= bitmap.getHeight()) {
            realSize = bitmap.getHeight();
        } else {
            realSize = bitmap.getWidth();
        }
        float scale = standardSize / realSize;

        LogUtil.msg("scale: " + scale);
        matrix.postScale(scale, scale);
        Bitmap returnBm = null;
        try {
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (Exception e) {
            LogUtil.msg("compress error: " + e.getMessage());
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }

        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    public static Bitmap compressBitmap(String image, int screenWidth, int screenHeight) {
        if (TextUtils.isEmpty(image)) return null;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(image, options);

        int sampleSize = calculateInSampleSize(options, screenWidth, screenHeight);
        options.inJustDecodeBounds = false;
        options.inSampleSize = sampleSize;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        Bitmap scaledBitmap = BitmapFactory.decodeFile(image, options);


        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        if (scaledBitmap.compress(Bitmap.CompressFormat.WEBP, 80, bos)) {

//
            Bitmap bitmap = rotateImageView(getOriginalDegree(image), scaledBitmap);

            int size = bitmap.getByteCount();

            LogUtil.msg("bitmap  after: width:  " + bitmap.getWidth() + "   height:  " + bitmap.getHeight() + " size " + size);
            return bitmap;
        }

        return rotateImageView(getOriginalDegree(image), scaledBitmap);

    }


    private static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;


        LogUtil.msg("bitmap before after: width:  " + width + "   height:  " + height + " screenWidth " + reqWidth + "  screenHeight " + reqHeight);
        float scaleSize;

        if (width > height) {
            scaleSize = height / reqHeight * 1f;
        } else {

            scaleSize = width / reqWidth * 1f;
        }

        BigDecimal bd = new BigDecimal(scaleSize + 0.5f);
        inSampleSize = bd.setScale(0, RoundingMode.HALF_UP).intValue();


        if (inSampleSize < 1) {
            inSampleSize = 1;
        }

        LogUtil.msg("inSampleSize: " + inSampleSize);

        return inSampleSize;
    }

    private static int getOriginalDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }


    /**
     * 旋转图片
     *
     * @param angle  被旋转角度
     * @param bitmap 图片对象
     * @return 旋转后的图片
     */
    public static Bitmap rotateImageView(int angle, Bitmap bitmap) {
        Bitmap returnBm = null;
        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            LogUtil.msg("rotateImageView error: " + e.getMessage());
        }
        if (returnBm == null) {
            returnBm = bitmap;
        }
        if (bitmap != returnBm) {
            bitmap.recycle();
        }
        return returnBm;
    }

    /**
     * 高斯模糊
     *
     * @param context
     * @param bitmap
     * @return
     */
    public static Bitmap blurBitmap(Context context, Bitmap bitmap) {

        if (bitmap == null) return null;
        //用需要创建高斯模糊bitmap创建一个空的bitmap
        Bitmap outBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        // 初始化Renderscript，该类提供了RenderScript context，创建其他RS类之前必须先创建这个类，其控制RenderScript的初始化，资源管理及释放
        RenderScript rs = RenderScript.create(context);
        // 创建高斯模糊对象
        ScriptIntrinsicBlur blurScript = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        // 创建Allocations，此类是将数据传递给RenderScript内核的主要方 法，并制定一个后备类型存储给定类型
        Allocation allIn = Allocation.createFromBitmap(rs, bitmap);
        Allocation allOut = Allocation.createFromBitmap(rs, outBitmap);
        //设定模糊度(注：Radius最大只能设置25.f)
        blurScript.setRadius(22f);
        // Perform the Renderscript
        blurScript.setInput(allIn);
        blurScript.forEach(allOut);
        // Copy the final bitmap created by the out Allocation to the outBitmap
        allOut.copyTo(outBitmap);
        // recycle the original bitmap
        // bitmap.recycle();
        // After finishing everything, we destroy the Renderscript.
        rs.destroy();
        return outBitmap;
    }


    public static String convertBitmapToPath(Context context, Bitmap bitmap) {


        String path;
        File file = null;
        if (bitmap != null) {
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                path = Environment.getExternalStorageDirectory().getAbsolutePath();
            } else {
                path = context.getExternalCacheDir().getAbsolutePath();
            }

            file = new File(path, File.pathSeparator + "/face.png");
            FileOutputStream fos = null;
            try {
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    file.createNewFile();
                }
                fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.WEBP, 100, fos);
                fos.flush();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return file.getAbsolutePath();

    }


    public static Bitmap getShareBitmap(Context context) {
        Bitmap originBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.share_code_bg);
        Bitmap codeBitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.download_code);
        UserInfo userInfo = UserInfoHelper.getUserInfo();
        String path = SPUtils.getInstance().getString(SpConstant.face_path);
//                        LogUtil.msg("path:  $path")
        Bitmap bitmap;
        if (!TextUtils.isEmpty(path)) {
            bitmap = BitmapFactory.decodeFile(path);
        } else {
            bitmap = ImageUtils.compressBitmap(BitmapFactory.decodeResource(context.getResources(), R.mipmap.deafult_avator));
        }
        return ImageUtils.compoundPic(originBitmap, bitmap, codeBitmap, Environment.getExternalStorageDirectory().getAbsolutePath(), userInfo.getInviteCode());

    }


}
