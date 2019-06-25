package yc.com.homework.mine.domain.bean

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField

/**
 * Created by wanglin  on 2018/3/15 14:56.
 */

class ShareInfo() : Parcelable {


    @JSONField(name = "share_url")
    var url: String? = null
    @JSONField(name = "share_title")
    var title: String? = null
    @JSONField(name = "share_content")
    var content: String? = null
    @JSONField(name = "share_img")
    var img: String? = null// 分享图片;
    var task_id: String? = null
    var bitmap: Bitmap? = null

    constructor(parcel: Parcel) : this() {
        url = parcel.readString()
        title = parcel.readString()
        content = parcel.readString()
        img = parcel.readString()
        task_id = parcel.readString()
        bitmap = parcel.readParcelable(Bitmap::class.java.classLoader)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(url)
        parcel.writeString(title)
        parcel.writeString(content)
        parcel.writeString(img)
        parcel.writeString(task_id)
        parcel.writeParcelable(bitmap, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ShareInfo> {
        override fun createFromParcel(parcel: Parcel): ShareInfo {
            return ShareInfo(parcel)
        }

        override fun newArray(size: Int): Array<ShareInfo?> {
            return arrayOfNulls(size)
        }
    }


}
