package yc.com.homework.wall.domain.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.ArrayList;

/**
 * Created by wanglin  on 2018/12/13 15:41.
 */
public class HomeworkDetailInfo implements Parcelable {


    /**
     * task_id : 53
     * correct_time : 1544492799
     * error_num : 5
     * right_num : 1
     * score : 16.67
     * subject_id : 1
     * image : http://zyl.wk2.com/uploads/20181210/074a3de91b91316f48e84fc808dd7e4f.png
     * grade_id : 6
     * remark : 加油
     * type : 1
     * face : http://zyl.wk2.com/uploads/user/5c106e5a21375.png
     * nick_name : 哦哟哟
     * teacher_face : http://zyl.wk2.com//uploads/user/5c04fae90fc09.png
     * teacher_name : 578162
     * is_read : 1
     * rank : C
     */

    private String task_id;
    private long correct_time;
    private int error_num;
    private int right_num;
    private String score;
    @JSONField(name = "subject_id")
    private int subjectId;
    private String image;
    private int grade_id;
    private String remark;
    private int type;
    private String face;
    private String nick_name;
    private String teacher_face;
    private String teacher_name;
    @JSONField(name = "is_read")
    private int isRead;
    private String rank;

    private int width;
    private int height;
    private ArrayList<AnswerInfo> answer;
    @JSONField(name = "add_time")
    private long createTime;
    @JSONField(name = "reback")
    private String reason;


    public HomeworkDetailInfo() {
    }


    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public long getCorrect_time() {
        return correct_time;
    }

    public void setCorrect_time(long correct_time) {
        this.correct_time = correct_time;
    }

    public int getError_num() {
        return error_num;
    }

    public void setError_num(int error_num) {
        this.error_num = error_num;
    }

    public int getRight_num() {
        return right_num;
    }

    public void setRight_num(int right_num) {
        this.right_num = right_num;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getGrade_id() {
        return grade_id;
    }

    public void setGrade_id(int grade_id) {
        this.grade_id = grade_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public String getNick_name() {
        return nick_name;
    }

    public void setNick_name(String nick_name) {
        this.nick_name = nick_name;
    }

    public String getTeacher_face() {
        return teacher_face;
    }

    public void setTeacher_face(String teacher_face) {
        this.teacher_face = teacher_face;
    }

    public String getTeacher_name() {
        return teacher_name;
    }

    public void setTeacher_name(String teacher_name) {
        this.teacher_name = teacher_name;
    }

    public int getIsRead() {
        return isRead;
    }

    public void setIsRead(int isRead) {
        this.isRead = isRead;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public ArrayList<AnswerInfo> getAnswer() {
        return answer;
    }

    public void setAnswer(ArrayList<AnswerInfo> answer) {
        this.answer = answer;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.task_id);
        dest.writeLong(this.correct_time);
        dest.writeInt(this.error_num);
        dest.writeInt(this.right_num);
        dest.writeString(this.score);
        dest.writeInt(this.subjectId);
        dest.writeString(this.image);
        dest.writeInt(this.grade_id);
        dest.writeString(this.remark);
        dest.writeInt(this.type);
        dest.writeString(this.face);
        dest.writeString(this.nick_name);
        dest.writeString(this.teacher_face);
        dest.writeString(this.teacher_name);
        dest.writeInt(this.isRead);
        dest.writeString(this.rank);
        dest.writeInt(this.width);
        dest.writeInt(this.height);
        dest.writeTypedList(this.answer);
        dest.writeLong(this.createTime);
        dest.writeString(this.reason);
    }

    protected HomeworkDetailInfo(Parcel in) {
        this.task_id = in.readString();
        this.correct_time = in.readLong();
        this.error_num = in.readInt();
        this.right_num = in.readInt();
        this.score = in.readString();
        this.subjectId = in.readInt();
        this.image = in.readString();
        this.grade_id = in.readInt();
        this.remark = in.readString();
        this.type = in.readInt();
        this.face = in.readString();
        this.nick_name = in.readString();
        this.teacher_face = in.readString();
        this.teacher_name = in.readString();
        this.isRead = in.readInt();
        this.rank = in.readString();
        this.width = in.readInt();
        this.height = in.readInt();
        this.answer = in.createTypedArrayList(AnswerInfo.CREATOR);
        this.createTime = in.readLong();
        this.reason = in.readString();
    }

    public static final Parcelable.Creator<HomeworkDetailInfo> CREATOR = new Parcelable.Creator<HomeworkDetailInfo>() {
        @Override
        public HomeworkDetailInfo createFromParcel(Parcel source) {
            return new HomeworkDetailInfo(source);
        }

        @Override
        public HomeworkDetailInfo[] newArray(int size) {
            return new HomeworkDetailInfo[size];
        }
    };
}
