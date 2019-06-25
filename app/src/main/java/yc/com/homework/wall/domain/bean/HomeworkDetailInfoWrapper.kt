package yc.com.homework.wall.domain.bean

/**
 * Created by wanglin  on 2018/12/10 11:48.
 */
class HomeworkDetailInfoWrapper {

    /**
     * task_id : 19
     * correct_time : 1544246800
     * error_num : 2
     * right_num : 3
     * score : 60.00
     * subject_id : 2
     * image : http://zyl.wk2.com/uploads/20181207/f0693ba5b5e1bd551fad364fa189b7da.png
     * grade_id : 9
     * remark : 该学生成绩优秀
     * type : 1
     * rank : B
     * task_detail : [{"id":44,"task_id":19,"image":"http://zyl.wk2.com/uploads/20181207/f0693ba5b5e1bd551fad364fa189b7da.png","add_time":1544154994,"correct_time":1544246800,"answer":"[{\"src\":\"\\/static\\/workedit\\/right.png\",\"x\":298,\"y\":193,\"msg\":\"\",\"tagType\":0,\"imgScale\":0.9486328125},{\"src\":\"\\/static\\/workedit\\/right.png\",\"x\":293,\"y\":429,\"msg\":\"\",\"tagType\":0,\"imgScale\":0.9486328125},{\"src\":\"\\/static\\/workedit\\/right.png\",\"x\":336,\"y\":623,\"msg\":\"\",\"tagType\":0,\"imgScale\":0.9486328125},{\"src\":\"\\/static\\/workedit\\/error.png\",\"x\":712,\"y\":314,\"msg\":\"\",\"tagType\":1,\"imgScale\":0.9486328125},{\"src\":\"\\/static\\/workedit\\/error.png\",\"x\":712,\"y\":523,\"msg\":\"\",\"tagType\":1,\"imgScale\":0.9486328125}]","right_num":3,"error_num":2,"score":"60.00","width":"1080","height":"1080"}]
     */

    var task_id: Int = 0
    var correct_time: Int = 0
    var error_num: Int = 0
    var right_num: Int = 0
    var score: String? = null
    var subject_id: Int = 0
    var image: String? = null
    var grade_id: Int = 0
    var remark: String? = null
    var type: Int = 0
    var rank: String? = null
    var task_detail: ArrayList<HomeworkDetailInfo>? = null


}
