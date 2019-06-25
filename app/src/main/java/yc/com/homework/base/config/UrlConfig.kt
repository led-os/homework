package yc.com.homework.base.config

/**
 * Created by wanglin  on 2018/11/19 11:24.
 * 访问地址
 */
interface UrlConfig {
    companion object {

        private const val debug_url = "http://zyl.wk2.com/api/"

        private const val base_url = ""

//        val index_url = (if (Constant.DEBUG) debug_url else base_url) + "/init"

        //登录
        val login = (if (Constant.DEBUG) debug_url else base_url) + "user/login"

        //游客登录
        val device_reg = (if (Constant.DEBUG) debug_url else base_url) + "user/device_reg"

        //注册
        val register = (if (Constant.DEBUG) debug_url else base_url) + "user/phone_reg"

        //第三方登录
        val thrid_login = (if (Constant.DEBUG) debug_url else base_url) + "user/tencent_reg"

        //绑定手机号
        val bind_phone = (if (Constant.DEBUG) debug_url else base_url) + "user/bind_mobile"

        //修改密码
        val modify_pwd = (if (Constant.DEBUG) debug_url else base_url) + "user/reset_password"

        //发送验证码
        val send_code = (if (Constant.DEBUG) debug_url else base_url) + "user/send_code"

        //用户更新信息
        val update_info = (if (Constant.DEBUG) debug_url else base_url) + "user/update"

        //修改图像
        val upload = (if (Constant.DEBUG) debug_url else base_url) + "user/upload_base64_face"

        //商品列表
        val goods_info = (if (Constant.DEBUG) debug_url else base_url) + "goods/index"

        //创建订单
        val orders_init = (if (Constant.DEBUG) debug_url else base_url) + "orders/init"

        //上传作业
        val upload_homework = (if (Constant.DEBUG) debug_url else base_url) + "homework/upload"

        //老师申请规则
        val apply_teacher_rule = (if (Constant.DEBUG) debug_url else base_url) + "teacher/apply_page"

        //申请老师
        val apply_teacher = (if (Constant.DEBUG) debug_url else base_url) + "teacher/apply_for_teacher"

        //上传证书图像
        val common_upload = (if (Constant.DEBUG) debug_url else base_url) + "common/upload"

        //申请状态
        val teacher_status = (if (Constant.DEBUG) debug_url else base_url) + "teacher/teacher_status"

        //作业墙列表
        val homework_list = (if (Constant.DEBUG) debug_url else base_url) + "homework_task/lists"
        //作业详情
        val homework_detail = (if (Constant.DEBUG) debug_url else base_url) + "homework_task/detail"
        //作业记录
        val homework_history = (if (Constant.DEBUG) debug_url else base_url) + "homework_task/history_shows"
        //作业记录详情
        val history_detail = (if (Constant.DEBUG) debug_url else base_url) + "homework_task/history_detail"
        //作业反馈
        val question_feed = (if (Constant.DEBUG) debug_url else base_url) + "homework/question"
        //金币明细
        val coin_bill = (if (Constant.DEBUG) debug_url else base_url) + "coin/bill"
        //用户信息
        val user_info = (if (Constant.DEBUG) debug_url else base_url) + "user/user_info"
        //上传状态
        val upload_status = (if (Constant.DEBUG) debug_url else base_url) + "homework_task/count_status"
        //作业上报已读状态
        val homework_isRead = (if (Constant.DEBUG) debug_url else base_url) + "homework_task/is_read"
        //分享作业
        val share_reward = (if (Constant.DEBUG) debug_url else base_url) + "share/reward"
        //消息列表
        val message_list = (if (Constant.DEBUG) debug_url else base_url) + "jpush/index"
        //消息详情
        val message_info = (if (Constant.DEBUG) debug_url else base_url) + "jpush/info"
        //未读消息条数
        val unread_message = (if (Constant.DEBUG) debug_url else base_url) + "jpush/notsee"
        //选择年级和版本
        val book_condition = (if (Constant.DEBUG) debug_url else base_url) + "book/index"
        //根据年级获取相应版本
        val book_version_list = (if (Constant.DEBUG) debug_url else base_url) + "book/version_list"
        //根据年级和版本获取书本信息
        val book_search = (if (Constant.DEBUG) debug_url else base_url) + "book/search_book"
        //
        val book_get_unit = (if (Constant.DEBUG) debug_url else base_url) + "book/get_unit"

        //点读详情页
        val book_get_detail = (if (Constant.DEBUG) debug_url else base_url) + "book/get_page"

        //订单明细列表
        val orders_lists = (if (Constant.DEBUG) debug_url else base_url) + "orders/lists"
        //首页
        val index_info = (if (Constant.DEBUG) debug_url else base_url) + "user/homepage"
        //精品推荐咨询
        val index_recommend = (if (Constant.DEBUG) debug_url else base_url) + "news/recommand"
        //新闻详情
        val news_detail = (if (Constant.DEBUG) debug_url else base_url) + "news/detail"
        //新闻点击次数统计
        val read_pv = (if (Constant.DEBUG) debug_url else base_url) + "news/read_pv"
        //分享内容
        val share_info = (if (Constant.DEBUG) debug_url else base_url) + "share/info"
        //广告接口
        val adv_info = (if (Constant.DEBUG) debug_url else base_url) + "menuadv/info"
        //作文首页推荐
        val composition_index_info = (if (Constant.DEBUG) debug_url else base_url) + "news/zwlist"
        //作文选择条件列表
        val composition_version_attr = (if (Constant.DEBUG) debug_url else base_url) + "news/zwattr"
        //作文搜索接口
        val composition_search = (if (Constant.DEBUG) debug_url else base_url) + "news/zwsearch"
        //热门搜索标签
        val composition_hot_tag = (if (Constant.DEBUG) debug_url else base_url) + "news/hot_search"

        //数学公式
        val mathematical_list = (if (Constant.DEBUG) debug_url else base_url) + "news/gongsi"


        val GRADE_LIST_URL = (if (Constant.DEBUG) debug_url else base_url) + "words/grade_list"

        val COURSE_VERSION_LIST_URL = (if (Constant.DEBUG) debug_url else base_url) + "words/version_list"

        val WORD_UNIT_LIST_URL = (if (Constant.DEBUG) debug_url else base_url) + "words/unit_list"

        val BOOK_INFO_URL = (if (Constant.DEBUG) debug_url else base_url) + "words/book_info"

        val WORD_LIST_URL = (if (Constant.DEBUG) debug_url else base_url) + "words/words_list"

    }
}
