package yc.com.soundmark.category.model.domain;

import android.os.Parcel;
import android.os.Parcelable;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * Created by zhangkai on 2017/8/30.
 */


public class CourseInfo implements Parcelable {
    public CourseInfo() {
    }


    private String id;
    private String title;
    private String url;
    private String keywords;
    private String type_id;
    private String period;
    private String flag;
    private String author;
    private String add_time;
    private String add_date;
    private String img;
    private String html;
    private int url_type;
    private String userId;
    /**
     * add_date : 20170906
     * add_time : 1504669373
     * author :
     * body : 哈希表（Hash table，也叫散列表），是根据关键码值(Key value)而直接进行访问的数据结构。也就是说，它通过把关键码值映射到表中一个位置来访问记录，以加快查找的速度。这个映射函数叫做散列函数，存放记录的数组叫做散列表。哈希表hashtable(key，value) 的做法其实很简单，就是把Key通过一个固定的算法函数既所谓的哈希函数转换成一个整型数字，然后就将该数字对数组长度进行取余，取余结果就当作数组的下标，将value存储在以该数字为下标的数组空间里。
     * flag : 1
     * id : 81
     * img : http://en.qqtn.com/Upload/task/59af6ebd9fbd2.png
     * ip_num : 0
     * keywords : 1111111111111
     * period : 0
     * pv_num : 0
     * sort : 30
     * title : 什么是哈希表？1225
     * type_id : 7
     * url : http://en.qqtn.com/Upload/Picture/2017-08-16/59943ac3ed7af.voice
     */


    private String body;
    private String ip_num;
    private String pv_num;
    private String sort;

    private boolean isChecked;

    private float price;
    @JSONField(name = "original_price")//原价
    private float mPrice;
    @JSONField(name = "current_price")//vip价格
    private float vipPrice;
    @JSONField(name = "good_id")
    private String goodId;
    @JSONField(name = "is_pay")
    private int isPay;//0收费,1免费
    @JSONField(name = "is_vip")
    private int is_vip;//0对vip免费,1:对vip收费
    @JSONField(name = "user_has")
    private int userHas;//0未购买，1:已购买

    @JSONField(name = "user_num")
    private String userNum;

    @JSONField(name = "unit_num")
    private String unitNum;

    @JSONField(name = "pay_price")
    private float payPrice;

    @JSONField(name = "good_use_time_limit")
    private int goodUseTimeLimit;

    private String desp;

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAdd_time() {
        return add_time;
    }

    public void setAdd_time(String add_time) {
        this.add_time = add_time;
    }

    public String getAdd_date() {
        return add_date;
    }

    public void setAdd_date(String add_date) {
        this.add_date = add_date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return this.body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getIp_num() {
        return this.ip_num;
    }

    public void setIp_num(String ip_num) {
        this.ip_num = ip_num;
    }

    public String getPv_num() {
        return this.pv_num;
    }

    public void setPv_num(String pv_num) {
        this.pv_num = pv_num;
    }

    public String getSort() {
        return this.sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public int getUrl_type() {
        return this.url_type;
    }

    public void setUrl_type(int url_type) {
        this.url_type = url_type;
    }

    public boolean getIsChecked() {
        return this.isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }


    public String getGoodId() {
        return this.goodId;
    }

    public void setGoodId(String goodId) {
        this.goodId = goodId;
    }

    public int getIsPay() {
        return isPay;
    }

    public void setIsPay(int isPay) {
        this.isPay = isPay;
    }

    public int getIs_vip() {
        return is_vip;
    }

    public void setIs_vip(int is_vip) {
        this.is_vip = is_vip;
    }


    public String getUserNum() {
        return this.userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }

    public String getUnitNum() {
        return this.unitNum;
    }

    public void setUnitNum(String unitNum) {
        this.unitNum = unitNum;
    }

    public float getPrice() {
        return this.price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public float getMPrice() {
        return this.mPrice;
    }

    public void setMPrice(float mPrice) {
        this.mPrice = mPrice;
    }

    public float getVipPrice() {
        return this.vipPrice;
    }

    public void setVipPrice(float vipPrice) {
        this.vipPrice = vipPrice;
    }


    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getUserHas() {
        return this.userHas;
    }

    public void setUserHas(int userHas) {
        this.userHas = userHas;
    }

    public float getPayPrice() {
        return payPrice;
    }

    public void setPayPrice(float payPrice) {
        this.payPrice = payPrice;
    }

    public int getGoodUseTimeLimit() {
        return goodUseTimeLimit;
    }

    public void setGoodUseTimeLimit(int goodUseTimeLimit) {
        this.goodUseTimeLimit = goodUseTimeLimit;
    }

    public String getDesp() {
        return desp;
    }

    public void setDesp(String desp) {
        this.desp = desp;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.url);
        dest.writeString(this.keywords);
        dest.writeString(this.type_id);
        dest.writeString(this.period);
        dest.writeString(this.flag);
        dest.writeString(this.author);
        dest.writeString(this.add_time);
        dest.writeString(this.add_date);
        dest.writeString(this.img);
        dest.writeString(this.html);
        dest.writeInt(this.url_type);
        dest.writeString(this.userId);
        dest.writeString(this.body);
        dest.writeString(this.ip_num);
        dest.writeString(this.pv_num);
        dest.writeString(this.sort);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeFloat(this.price);
        dest.writeFloat(this.mPrice);
        dest.writeFloat(this.vipPrice);
        dest.writeString(this.goodId);
        dest.writeInt(this.isPay);
        dest.writeInt(this.is_vip);
        dest.writeInt(this.userHas);
        dest.writeString(this.userNum);
        dest.writeString(this.unitNum);
        dest.writeFloat(this.payPrice);
        dest.writeInt(this.goodUseTimeLimit);
    }

    protected CourseInfo(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.url = in.readString();
        this.keywords = in.readString();
        this.type_id = in.readString();
        this.period = in.readString();
        this.flag = in.readString();
        this.author = in.readString();
        this.add_time = in.readString();
        this.add_date = in.readString();
        this.img = in.readString();
        this.html = in.readString();
        this.url_type = in.readInt();
        this.userId = in.readString();
        this.body = in.readString();
        this.ip_num = in.readString();
        this.pv_num = in.readString();
        this.sort = in.readString();
        this.isChecked = in.readByte() != 0;
        this.price = in.readFloat();
        this.mPrice = in.readFloat();
        this.vipPrice = in.readFloat();
        this.goodId = in.readString();
        this.isPay = in.readInt();
        this.is_vip = in.readInt();
        this.userHas = in.readInt();
        this.userNum = in.readString();
        this.unitNum = in.readString();
        this.payPrice = in.readFloat();
        this.goodUseTimeLimit = in.readInt();
    }

    public static final Creator<CourseInfo> CREATOR = new Creator<CourseInfo>() {
        @Override
        public CourseInfo createFromParcel(Parcel source) {
            return new CourseInfo(source);
        }

        @Override
        public CourseInfo[] newArray(int size) {
            return new CourseInfo[size];
        }
    };
}
