package yc.com.answer.index.model.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wanglin  on 2019/3/18 17:00.
 */
@Entity
public class BookAnswerInfo implements MultiItemEntity {


    /**
     * id : 36758
     * name : 学生双语报2018-2019年X版浙江专版八年级下第32期答案
     * cover_img : http://www.mxqe.com/d/file/20190318/f9971adbe83b1419d172661dc8f62d1a.jpg
     * year : 2018
     * subject : 英语
     * press :
     * code : 0
     * is_del : 0
     * sort : 50
     * share_num : 0
     * pv_num : 0
     * version : 中华书局版/ZHSJ
     * period : 初中
     * grade : 八年级
     * part_type : 下册
     * access : 0
     * favorite : 0
     * flag : []
     * share_content :
     * author :
     * sex : 0
     * time : 2019-03-18
     * answer_list : []
     */
    public static final int ADV = 1;
    public static final int CONTENT = 2;
    @Id
    private Long id;
    @Index
    @JSONField(name = "id")
    private String bookId;

    private String name;
    private String cover_img;
    private int year;
    private String subject;
    private String press;
    private String code;
    private int is_del;
    private int sort;
    private int share_num;
    private int pv_num;
    private String version;
    private String period;
    private String grade;
    private String part_type;
    private int access;
    private int favorite;
    private String share_content;
    private String author;
    private int sex;
    private String time;
    @Transient
    private List<String> flag;
    @Transient
    private List<String> answer_list;
    @Transient
    private int mType;

    private long saveTime;

    @Generated(hash = 165504304)
    public BookAnswerInfo(Long id, String bookId, String name, String cover_img, int year,
            String subject, String press, String code, int is_del, int sort, int share_num,
            int pv_num, String version, String period, String grade, String part_type,
            int access, int favorite, String share_content, String author, int sex,
            String time, long saveTime) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.cover_img = cover_img;
        this.year = year;
        this.subject = subject;
        this.press = press;
        this.code = code;
        this.is_del = is_del;
        this.sort = sort;
        this.share_num = share_num;
        this.pv_num = pv_num;
        this.version = version;
        this.period = period;
        this.grade = grade;
        this.part_type = part_type;
        this.access = access;
        this.favorite = favorite;
        this.share_content = share_content;
        this.author = author;
        this.sex = sex;
        this.time = time;
        this.saveTime = saveTime;
    }

    @Generated(hash = 15020202)
    public BookAnswerInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCover_img() {
        return cover_img;
    }

    public void setCover_img(String cover_img) {
        this.cover_img = cover_img;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getIs_del() {
        return is_del;
    }

    public void setIs_del(int is_del) {
        this.is_del = is_del;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public int getShare_num() {
        return share_num;
    }

    public void setShare_num(int share_num) {
        this.share_num = share_num;
    }

    public int getPv_num() {
        return pv_num;
    }

    public void setPv_num(int pv_num) {
        this.pv_num = pv_num;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPart_type() {
        return part_type;
    }

    public void setPart_type(String part_type) {
        this.part_type = part_type;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public String getShare_content() {
        return share_content;
    }

    public void setShare_content(String share_content) {
        this.share_content = share_content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<String> getFlag() {
        return flag;
    }

    public void setFlag(List<String> flag) {
        this.flag = flag;
    }

    public List<String> getAnswer_list() {
        return answer_list;
    }

    public void setAnswer_list(List<String> answer_list) {
        this.answer_list = answer_list;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    @Override
    public int getItemType() {
        return mType;
    }


    public void setType(int mType) {
        this.mType = mType;
    }
}
