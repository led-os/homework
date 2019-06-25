package yc.com.homework.read.domain.bean

import android.os.Parcel
import android.os.Parcelable
import com.alibaba.fastjson.annotation.JSONField
/**
 * Created by wanglin  on 2019/1/11 10:11.
 */


class BookUnitInfo() :Parcelable {


    /**
     * id : 1
     * book_id : 14
     * title : 入学准备
     * book_title : 一年级语文（上）
     * class : [{"id":1,"page":1,"pageindex":0,"title":"01 小学生","chapter_id":1,"book_id":14},{"id":2,"page":2,"pageindex":1,"title":"02 学校","chapter_id":1,"book_id":14},{"id":3,"page":3,"pageindex":2,"title":"03 同学","chapter_id":1,"book_id":14},{"id":4,"page":4,"pageindex":3,"title":"04 老师","chapter_id":1,"book_id":14},{"id":5,"page":5,"pageindex":4,"title":"05 操场上","chapter_id":1,"book_id":14},{"id":6,"page":6,"pageindex":5,"title":"06 读书写字","chapter_id":1,"book_id":14},{"id":7,"page":8,"pageindex":7,"title":"07 读儿歌","chapter_id":1,"book_id":14},{"id":8,"page":9,"pageindex":8,"title":"08 问答","chapter_id":1,"book_id":14},{"id":9,"page":10,"pageindex":9,"title":"09 校园里","chapter_id":1,"book_id":14},{"id":10,"page":11,"pageindex":10,"title":"10 汉字多奇妙","chapter_id":1,"book_id":14},{"id":11,"page":12,"pageindex":11,"title":"11 识字真有趣","chapter_id":1,"book_id":14}]
     */

    var id: Int = 0
    @JSONField(name = "book_id")
    var bookId: Int = 0
    var title: String? = null
    @JSONField(name = "book_title")
    var bookTitle: String? = null

    var page: Int = 0
    @JSONField(name = "pageindex")
    var pageIndex: Int = 0
    @JSONField(name = "chapter_id")
    var chapterId: Int = 0
    @JSONField(name = "class")
    var bookUnitInfos: ArrayList<BookUnitInfo>? = null

    var tag: Int = 0
    var headTitle = false
    var commonTitle: String? = null
    @JSONField(name = "page_total")
    var pageTotal: Int = 0

    var can_see: Int? = 0// 0 标识不能看   1标识 可以看

    constructor(parcel: Parcel) : this() {
        id = parcel.readInt()
        bookId = parcel.readInt()
        title = parcel.readString()
        bookTitle = parcel.readString()
        page = parcel.readInt()
        pageIndex = parcel.readInt()
        chapterId = parcel.readInt()
        tag = parcel.readInt()
        headTitle = parcel.readByte() != 0.toByte()
        commonTitle = parcel.readString()
        pageTotal = parcel.readInt()
        can_see = parcel.readValue(Int::class.java.classLoader) as? Int
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeInt(bookId)
        parcel.writeString(title)
        parcel.writeString(bookTitle)
        parcel.writeInt(page)
        parcel.writeInt(pageIndex)
        parcel.writeInt(chapterId)
        parcel.writeInt(tag)
        parcel.writeByte(if (headTitle) 1 else 0)
        parcel.writeString(commonTitle)
        parcel.writeInt(pageTotal)
        parcel.writeValue(can_see)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<BookUnitInfo> {
        override fun createFromParcel(parcel: Parcel): BookUnitInfo {
            return BookUnitInfo(parcel)
        }

        override fun newArray(size: Int): Array<BookUnitInfo?> {
            return arrayOfNulls(size)
        }
    }


}
