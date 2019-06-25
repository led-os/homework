package yc.com.homework.mine.utils

import android.Manifest
import android.annotation.TargetApi
import android.app.Activity
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.Base64
import android.util.Log
import com.kk.utils.ToastUtil
import com.vondear.rxtools.RxPhotoTool
import yc.com.base.BaseActivity
import yc.com.blankj.utilcode.util.LogUtils
import yc.com.homework.base.utils.ToastUtils
import java.io.ByteArrayOutputStream
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 *
 * Created by wanglin  on 2018/12/3 13:53.
 */
object AvatarHelper {
    private const val OPEN_IMG = 1//打开相册

    private const val CROP_IMG = 2//裁剪图片
    private var imagePathUri: Uri? = null

    private const val SELECT_IMG = 3


    interface IAvatar {
        fun uploadAvatar(image: String)
    }

    private var mType: Int? = 0

    /**
     * 中缀表达式
     */
    infix fun setType(type: Int?) {
        this.mType = type
    }

    fun getType(): Int? {
        return mType
    }

    fun openAlbum(context: Activity) {
        val intent = Intent(Intent.ACTION_PICK) // 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
        context.startActivityForResult(intent, OPEN_IMG)
    }

    fun selectAlbum(context: Activity) {
        val intent = Intent(Intent.ACTION_PICK) // 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*")
        context.startActivityForResult(intent, SELECT_IMG)
    }


    fun onActivityForResult(activity: Activity, requestCode: Int, resultCode: Int, intent: Intent?, iAvatar: IAvatar, isEquals: Boolean = true) {

        when (requestCode) {
            OPEN_IMG -> if (resultCode == Activity.RESULT_OK && null != intent) {
                cropImag(activity, intent.data, isEquals)
            }

            RxPhotoTool.GET_IMAGE_BY_CAMERA -> if (resultCode == Activity.RESULT_OK)
                cropImag(activity, RxPhotoTool.imageUriFromCamera, isEquals)

            CROP_IMG -> {
                val path = getImageAbsolutePath(activity, imagePathUri)
                val photo = BitmapFactory.decodeFile(path)

                if (photo == null) {
                    ToastUtils.showCenterToast(activity, "获取图片失败")
                    return
                }
                val stream = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)// (0 - 100)压缩文件
                val byteArray = stream.toByteArray()
                val streamStr = Base64.encodeToString(byteArray, Base64.DEFAULT)
                val image = "data:image/png;base64,$streamStr"
                iAvatar.uploadAvatar(image)
            }

            SELECT_IMG -> {
                val uri = intent?.data

                val path = getImageAbsolutePath(activity, uri)
                val photo = BitmapFactory.decodeFile(path)
                if (photo == null) {
                    ToastUtils.showCenterToast(activity, "获取图片失败")
                    return
                }
                iAvatar.uploadAvatar(path!!)
            }

        }
    }


    /**
     * 这种方法不适用像小米这样返回大图片的机型
     *
     * @param context
     * @param iAvatar
     * @param requestCode
     * @param resultCode
     * @param data
     */

    @Deprecated("")
    fun uploadAvatar(context: BaseActivity<*>, iAvatar: IAvatar, requestCode: Int, resultCode: Int,
                     data: Intent?) {
        if (resultCode == Activity.RESULT_OK && null != data) {

            try {
                val extras = data.extras
                var photo: Bitmap? = null
                if (extras != null) {
                    photo = extras.getParcelable("data")
                }
                if (photo == null) {
                    val selectedImage = data.data
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = context.contentResolver.query(selectedImage,
                            filePathColumn, null, null, null)
                    var picturePath = ""
                    if (cursor != null) {
                        cursor.moveToFirst()
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        picturePath = cursor.getString(columnIndex)
                        cursor.close()
                    } else {
                        picturePath = selectedImage!!.path
                    }
                    if (requestCode == 1) {
                        val intent = Intent("com.android.camera.action.CROP")
                        intent.setDataAndType(selectedImage, "image/*")
                        intent.putExtra("crop", "true")
                        intent.putExtra("aspectX", 1)
                        intent.putExtra("aspectY", 1)
                        intent.putExtra("outputX", 160)
                        intent.putExtra("outputY", 160)
                        intent.putExtra("return-data", true)

                        context.startActivityForResult(intent, 1)
                        return
                    }
                    photo = BitmapFactory.decodeFile(picturePath)
                }

                if (photo == null) {
                    ToastUtils.showCenterToast(context, "获取图片失败")
                    return
                }

                val stream = ByteArrayOutputStream()
                photo.compress(Bitmap.CompressFormat.JPEG, 100, stream)// (0 - 100)压缩文件
                val byteArray = stream.toByteArray()
                val streamStr = Base64.encodeToString(byteArray, Base64.DEFAULT)
                val image = "data:image/png;base64,$streamStr"
                iAvatar.uploadAvatar(image)
            } catch (e: Exception) {
                context.dismissDialog()
                ToastUtils.showCenterToast(context, "修改失败$e")
                LogUtils.i("修改失败$e")
            }

        }
    }


    private fun cropImag(activity: Activity, uri: Uri?, equals: Boolean) {
        //        imagePathUri = createImagePathUri(activity);
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        if (equals) {
//            if (Build.MANUFACTURER == "HUAWEI") {
//                intent.putExtra("aspectX", 9998)
//                intent.putExtra("aspectY", 9999)
//            } else {
            intent.putExtra("aspectX", 1)
            intent.putExtra("aspectY", 1)
//            }
        } else {
            if (Build.MANUFACTURER == "HUAWEI") {
                intent.putExtra("aspectX", 9998)
                intent.putExtra("aspectY", 7499)
            } else {
                intent.putExtra("aspectX", 4)
                intent.putExtra("aspectY", 3)
            }
        }
        intent.putExtra("outputX", 300)
        intent.putExtra("outputY", 300)
        intent.putExtra("return-data", false)
        intent.putExtra("scale", true)//去除黑边
        intent.putExtra("scaleUpIfNeeded", true)//去除黑边

        val status = Environment.getExternalStorageState()

        val cropTempName: String
        cropTempName = if (status == Environment.MEDIA_MOUNTED) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
            (Environment.getExternalStorageDirectory().path
                    + "/" + System.currentTimeMillis() + "_crop_temp.jpg")
        } else {
            activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.path +
                    File.separator + System.currentTimeMillis() + "_crop_temp.jpg"
        }
        imagePathUri = Uri.fromFile(File(cropTempName))
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imagePathUri)
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())

        activity.startActivityForResult(intent, CROP_IMG)

    }


    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @param context
     * @return 图片的uri
     */
    fun createImagePathUri(context: Context): Uri {
        val imageFilePath = arrayOf<Uri>()

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(context as Activity, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
            imageFilePath[0] = Uri.parse("")
            ToastUtil.toast(context, "请先获取写入SDCard权限")
        } else {
            val status = Environment.getExternalStorageState()
            val timeFormatter = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINA)
            val time = System.currentTimeMillis()
            val imageName = timeFormatter.format(Date(time))
            // ContentValues是我们希望这条记录被创建时包含的数据信息
            val values = ContentValues(3)
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName)
            values.put(MediaStore.Images.Media.DATE_TAKEN, time)
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")

            if (status == Environment.MEDIA_MOUNTED) {// 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
                imageFilePath[0] = context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
            } else {
                imageFilePath[0] = context.contentResolver.insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values)
            }
        }

        Log.i("", "生成的照片输出路径：" + imageFilePath[0].toString())
        return imageFilePath[0]
    }


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    private fun getImageAbsolutePath(context: Context?, imageUri: Uri?): String? {
        if (context == null || imageUri == null)
            return null
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return Environment.getExternalStorageDirectory().toString() + "/" + split[1]
                }
            } else if (isDownloadsDocument(imageUri)) {
                val id = DocumentsContract.getDocumentId(imageUri)
                val contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), java.lang.Long.valueOf(id))
                return getDataColumn(context, contentUri, null, null)
            } else if (isMediaDocument(imageUri)) {
                val docId = DocumentsContract.getDocumentId(imageUri)
                val split = docId.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = MediaStore.Images.Media._ID + "=?"
                val selectionArgs = arrayOf(split[1])
                return getDataColumn(context, contentUri, selection, selectionArgs)
            }
        } // MediaStore (and general)
        else if ("content".equals(imageUri.scheme, ignoreCase = true)) {
            // Return the remote address
            return if (isGooglePhotosUri(imageUri)) imageUri.lastPathSegment else getDataColumn(context, imageUri, null, null)
        } else if ("file".equals(imageUri.scheme, ignoreCase = true)) {
            return imageUri.path
        }// File
        return null
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    private fun isGooglePhotosUri(uri: Uri): Boolean {
        return "com.google.android.apps.photos.content" == uri.authority
    }

    private fun getDataColumn(context: Context, uri: Uri?, selection: String?, selectionArgs: Array<String>?): String? {
        var cursor: Cursor? = null
        val column = MediaStore.Images.Media.DATA
        val projection = arrayOf(column)
        try {
            cursor = context.contentResolver.query(uri!!, projection, selection, selectionArgs, null)
            if (cursor != null && cursor.moveToFirst()) {
                val index = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(index)
            }
        } finally {
            if (cursor != null)
                cursor.close()
        }
        return null
    }
}