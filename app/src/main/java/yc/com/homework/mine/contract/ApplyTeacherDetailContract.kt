package yc.com.homework.mine.contract

import yc.com.base.*
import yc.com.homework.mine.domain.bean.ApplyRuleInfo
import yc.com.homework.mine.domain.bean.TeacherApplyState
import yc.com.homework.mine.domain.bean.UploadResultInfo

/**
 *
 * Created by wanglin  on 2018/11/26 08:52.
 */
interface ApplyTeacherDetailContract {

    interface View : IView, IDialog,IFinish,ILoading,INoNet,INoData,IHide {
        fun showTeacherRuleInfo(data: ApplyRuleInfo?)
        fun showUploadResult(data: UploadResultInfo?)
        fun showState(data: TeacherApplyState?)
    }

    interface Presenter : IPresenter {
        fun getApplyTeacherRuleInfo()
        fun applyTeacher(name: String?, sex: Int, mobile: String?, subject_id: Int, job: Int, teacher_cert: String?, diploma: String?)
        fun uploadPic(file: String)
        fun getApplyState()
    }
}