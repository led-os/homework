package yc.com.homework.index.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

/**
 * Created by wanglin  on 2019/3/21 09:27.
 */
@Entity
public class CompositionInfo {
    @Id(autoincrement = true)
    private Long id;

    @Index
    @JSONField(name = "id")
    private String compostion_id;
    @JSONField(name = "title")
    private String name;

    private String version;
    private String grade;
    private String part;
    private String unit;
    @JSONField(name = "subtitle")
    private String content;

    private long saveTime;
    private int isCollect;

    @Transient
    private String comment;

    private String img;

    private int pv_num;//阅读数量



    public CompositionInfo() {
    }

    public CompositionInfo(String name, String version, String grade, String part, String unit, String content, String comment) {
        this.name = name;
        this.version = version;
        this.grade = grade;
        this.part = part;
        this.unit = unit;
        this.content = content;
        this.comment = comment;
    }

    @Generated(hash = 1840966564)
    public CompositionInfo(Long id, String compostion_id, String name, String version, String grade, String part, String unit,
            String content, long saveTime, int isCollect, String img, int pv_num) {
        this.id = id;
        this.compostion_id = compostion_id;
        this.name = name;
        this.version = version;
        this.grade = grade;
        this.part = part;
        this.unit = unit;
        this.content = content;
        this.saveTime = saveTime;
        this.isCollect = isCollect;
        this.img = img;
        this.pv_num = pv_num;
    }
    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCompostion_id() {
        return compostion_id;
    }

    public void setCompostion_id(String compostion_id) {
        this.compostion_id = compostion_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getPart() {
        return part;
    }

    public void setPart(String part) {
        this.part = part;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public int getIsCollect() {
        return isCollect;
    }

    public void setIsCollect(int isCollect) {
        this.isCollect = isCollect;
    }

    public int getPv_num() {
        return pv_num;
    }

    public void setPv_num(int pv_num) {
        this.pv_num = pv_num;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
