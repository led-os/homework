package yc.com.homework.read.domain.bean;

import com.alibaba.fastjson.annotation.JSONField;
import com.chad.library.adapter.base.entity.MultiItemEntity;
import com.qq.e.ads.nativ.NativeExpressADView;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;


/**
 * Created by wanglin  on 2019/1/10 13:54.
 */
@Entity
public class BookInfo implements MultiItemEntity{

    public static final int ADV = 1;
    public static final int CONTENT = 2;
    @Id
    private Long id;

    private String bookId;

    @JSONField(name = "book_name")
    private String name;


    @JSONField(name = "cover")
    private String coverImg;

    @JSONField(name = "version_id")
    private String versionId;

    private String period;

    @JSONField(name = "part_type")
    private String partType;

    private String grade;
    @JSONField(name = "subject_id")
    private String subject;
    @JSONField(name = "volumes_id")
    private String volumesId;//上下册

    @JSONField(name = "is_del")
    private String isDel;

    private String sort;

    private String press;

    @JSONField(name = "sentence_count")
    private String sentenceCount;

    private String gradeName;

    @JSONField(name = "version_name")
    private String versionName;
    private String tag;

    private long saveTime;
    @Transient
    private int resId;
    private int favorite;
    @Transient
    private NativeExpressADView adView;
    @Transient
    private int itemType;
    @Transient
    private int access;

    @Generated(hash = 2114694168)
    public BookInfo(Long id, String bookId, String name, String coverImg,
            String versionId, String period, String partType, String grade,
            String subject, String volumesId, String isDel, String sort,
            String press, String sentenceCount, String gradeName,
            String versionName, String tag, long saveTime, int favorite) {
        this.id = id;
        this.bookId = bookId;
        this.name = name;
        this.coverImg = coverImg;
        this.versionId = versionId;
        this.period = period;
        this.partType = partType;
        this.grade = grade;
        this.subject = subject;
        this.volumesId = volumesId;
        this.isDel = isDel;
        this.sort = sort;
        this.press = press;
        this.sentenceCount = sentenceCount;
        this.gradeName = gradeName;
        this.versionName = versionName;
        this.tag = tag;
        this.saveTime = saveTime;
        this.favorite = favorite;
    }

    @Generated(hash = 1952025412)
    public BookInfo() {
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

    public String getCoverImg() {
        return coverImg;
    }

    public void setCoverImg(String coverImg) {
        this.coverImg = coverImg;
    }

    public String getVersionId() {
        return versionId;
    }

    public void setVersionId(String versionId) {
        this.versionId = versionId;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getPartType() {
        return partType;
    }

    public void setPartType(String partType) {
        this.partType = partType;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getPress() {
        return press;
    }

    public void setPress(String press) {
        this.press = press;
    }

    public String getSentenceCount() {
        return sentenceCount;
    }

    public void setSentenceCount(String sentenceCount) {
        this.sentenceCount = sentenceCount;
    }

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getVolumesId() {
        return volumesId;
    }

    public void setVolumesId(String volumesId) {
        this.volumesId = volumesId;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public long getSaveTime() {
        return saveTime;
    }

    public void setSaveTime(long saveTime) {
        this.saveTime = saveTime;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public int getFavorite() {
        return favorite;
    }

    public void setFavorite(int favorite) {
        this.favorite = favorite;
    }

    public NativeExpressADView getAdView() {
        return adView;
    }

    public void setAdView(NativeExpressADView adView) {
        this.adView = adView;
    }

    @Override
    public int getItemType() {
        return 0;
    }

    public void setItemType(int itemType) {
        this.itemType = itemType;
    }

    public int getAccess() {
        return access;
    }

    public void setAccess(int access) {
        this.access = access;
    }
}
