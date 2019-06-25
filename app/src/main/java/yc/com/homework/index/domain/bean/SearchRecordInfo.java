package yc.com.homework.index.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by wanglin  on 2019/3/29 14:08.
 */
@Entity
public class SearchRecordInfo {
    @Id(autoincrement = true)
    private Long id;
    @JSONField(name = "text")
    private String name;

    private long saveTime;

    @Generated(hash = 1788947692)
    public SearchRecordInfo(Long id, String name, long saveTime) {
        this.id = id;
        this.name = name;
        this.saveTime = saveTime;
    }

    @Generated(hash = 615406953)
    public SearchRecordInfo() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }
}
