package yc.com.homework.index.domain.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by wanglin  on 2019/2/15 14:36.
 */
class IndexInfo {

    //banner图片
    @JSONField(name = "banners")
    var images: ArrayList<BannerInfo>? = null

    var day_words: DayWordInfo? = null


    var today_starlight: String? = ""
    var teach_numbers: Int? = 0


}