package yc.com.homework.wall.domain.bean

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by wanglin  on 2018/12/6 17:27.
 */
class AnswerInfo() :Parcelable{

    var src: String? = null
    var x: Float = 0.toFloat()
    var y: Float = 0.toFloat()
    var msg: String? = null
    var tagType: Int = 0
    var imgScale: Int = 0
    var isPress: Boolean = false

    constructor(parcel: Parcel) : this() {
        src = parcel.readString()
        x = parcel.readFloat()
        y = parcel.readFloat()
        msg = parcel.readString()
        tagType = parcel.readInt()
        imgScale = parcel.readInt()
        isPress = parcel.readByte() != 0.toByte()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(src)
        parcel.writeFloat(x)
        parcel.writeFloat(y)
        parcel.writeString(msg)
        parcel.writeInt(tagType)
        parcel.writeInt(imgScale)
        parcel.writeByte(if (isPress) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AnswerInfo> {
        override fun createFromParcel(parcel: Parcel): AnswerInfo {
            return AnswerInfo(parcel)
        }

        override fun newArray(size: Int): Array<AnswerInfo?> {
            return arrayOfNulls(size)
        }
    }


}
