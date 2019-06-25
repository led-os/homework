package yc.com.homework.read.domain.bean

/**
 *
 * Created by wanglin  on 2019/1/14 13:41.
 */
class BookDetailInfo {
    var id: Int = 0
    var page_url: String? = ""
    var book_id: Int = 0
    var page: Int = 0
    var class_id: Int = 0
    var chapter_id: Int = 0
    var track_info: List<BookDetailTrackInfo>? = null
}