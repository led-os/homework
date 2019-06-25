package yc.com.homework.wall.domain.bean

import com.alibaba.fastjson.annotation.JSONField

/**
 *
 * Created by wanglin  on 2018/11/27 09:58.
 */
class HomeworkInfo {
    /**
     *   "id": 16,
    "user_id": 9,
    "guest_id": "",
    "add_time": 1544075103,
    "correct_time": 0,
    "status": 1,
    "grade_id": 1,
    "teacher_id": 1,
    "subject_id": 2,
    "type": 2,
    "error_num": 0,
    "right_num": 30,
    "task_id": 16,
    "image": "http://zyl.wk2.com/uploads/20181206/458f855cb4fd432be9d767a6017b4d20.png"
     */
    @JSONField(name = "image")
    var pic: String? = null
    var subject: String? = null


    @JSONField(name = "right_num")
    var correct: Int = 0
    @JSONField(name = "error_num")
    var error: Int = 0
    @JSONField(name = "grade_id")
    var gradeId: Int = 0
    @JSONField(name = "teacher_id")
    var teacherId: Int = 0
    @JSONField(name = "subject_id")
    var subjectId: Int = 0

    var type: Int = 0
    @JSONField(name = "task_id")
    var taskId: String? = ""
    @JSONField(name = "add_time")
    var addTime: Long? = 0





}