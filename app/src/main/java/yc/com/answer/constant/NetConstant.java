package yc.com.answer.constant;


import yc.com.base.BaseNetConstant;

/**
 * Created by wanglin  on 2018/3/7 14:38.
 */

public interface NetConstant {

    /**
     * 上传图片
     */
    String upload_url = BaseNetConstant.getBaseUrl() + "upload";

    /**
     * 发送验证码
     */
    String user_code_url = BaseNetConstant.getBaseUrl() + "user/code";



    /**
     * 书本版本列表
     */
    String book_version_url = BaseNetConstant.getBaseUrl() + "book/version";
    /**
     * 书本列表
     **/
    String book_index_url = BaseNetConstant.getBaseUrl() + "book/index";
    /**
     * 书本答案
     */
    String book_answer_url = BaseNetConstant.getBaseUrl() + "book/answer";

    /**
     * 分享书本
     */
    String book_share_url = BaseNetConstant.getBaseUrl() + "book/share";
    /**
     * 收藏书本
     */
    String book_favorite_url = BaseNetConstant.getBaseUrl() + "book/favorite";
    /**
     * 收藏列表
     */
    String book_myfavorite_url = BaseNetConstant.getBaseUrl() + "book/myfavorite";

    /**
     * 幻灯展示
     */
    String slide_index_url = BaseNetConstant.getBaseUrl() + "slide/index";


    /**
     * 热门搜索
     */
    String book_tag_url = BaseNetConstant.getBaseUrl() + "book/tag";

    /**
     * 用户分享
     */

    String user_share_url = BaseNetConstant.getBaseUrl() + "user/share";

    /**
     * 搜索下拉
     */
    String book_tip_url = BaseNetConstant.getBaseUrl() + "book/tip";

    /**
     * 阿里云获取AccessKeyId SecretKeyId SecurityToken
     */
    String oss_api_url = "http://answer.bshu.com/api/ali_oss/osstmpkey";

    String user_img_upload_book_url = BaseNetConstant.getBaseUrl() + "user_img/upload_book";

    /**
     * 上传新书的列表
     */
    String upload_list_url = BaseNetConstant.getBaseUrl() + "user_img/check_list";



    /**
     * 好评获取奖励
     */
    String task_good_comment_url = BaseNetConstant.getBaseUrl() + "task/good_comment";


    String adv_url = BaseNetConstant.getBaseUrl() + "index/h5page";
}
