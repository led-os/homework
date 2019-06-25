package yc.com.answer.utils;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;
import com.kk.securityhttp.net.entry.UpFileInfo;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import yc.com.answer.base.UploadInfo;
import yc.com.answer.constant.NetConstant;
import yc.com.answer.index.model.bean.AdvInfo;
import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.index.model.bean.BookAnswerInfoWrapper;
import yc.com.homework.base.user.UserInfoHelper;
import yc.com.homework.mine.domain.bean.ShareInfo;

/**
 * Created by wanglin  on 2018/3/7 15:13.
 */

public class EngineUtils {



    /**
     * 获取验证码
     */
    public static Observable<ResultInfo<String>> getCode(Context context, String phone) {
        Map<String, String> params = new HashMap<>();
        params.put("mobile", phone);
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());

        return HttpCoreEngin.get(context).rxpost(NetConstant.user_code_url, new TypeReference<ResultInfo<String>>() {
        }.getType(), params, headers, false, false, false);

    }

    /**
     * 获取书本列表
     * page: 页码
     * limit: 数量
     * [name]: 书名
     * [code]: 唯一码
     * <p>
     * [grade_id]: 年级。取值: 1~12
     * [grade]: 年级。任选一个做条件
     * <p>
     * [part_type_id]: 上下册。取值: 0: 上册，1: 下册，2:  全册
     * [part_type]: 上下册。任选一个做条件
     * <p>
     * [version_id]: 书本版本ID。
     * [version]: 书本版本ID。任选一个做条件
     * <p>
     * [subject_id]: 学科ID
     * [subject]: 学科。任选一个做条件
     * <p>
     * [flag_id]: 标记。取值: 1: 推荐；2: 热门
     * [flag]: 标记。任选一个做条件
     * <p>
     * [year]: 年份
     * [user_id]: 用户ID
     **/
    public static Observable<ResultInfo<BookAnswerInfoWrapper>> getBookInfoList(Context context, int page, int limit, String name, String code, String grade_id, String grade,
                                                                                String part_type_id, String part_type, String version_id, String version, String subject_id,
                                                                                String subject, String flag_id, String year, String latitude, String longitude) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("limit", limit + "");
        if (!TextUtils.isEmpty(name)) params.put("name", name);
        if (!TextUtils.isEmpty(code)) params.put("code", code);
        if (!TextUtils.isEmpty(grade_id)) params.put("grade_id", grade_id);
        if (!TextUtils.isEmpty(grade)) params.put("grade", grade);
        if (!TextUtils.isEmpty(part_type_id)) params.put("part_type_id", part_type_id);
        if (!TextUtils.isEmpty(part_type)) params.put("part_type", part_type);
        if (!TextUtils.isEmpty(version_id)) params.put("version_id", version_id);
        if (!TextUtils.isEmpty(version)) params.put("version", version);
        if (!TextUtils.isEmpty(subject_id)) params.put("subject_id", subject_id);
        if (!TextUtils.isEmpty(subject)) params.put("subject", subject);
        if (!TextUtils.isEmpty(flag_id)) params.put("flag_id", flag_id);
        if (!TextUtils.isEmpty(year)) params.put("year", year);
        if (!TextUtils.isEmpty(latitude)) params.put("latitude", latitude);
        if (!TextUtils.isEmpty(longitude)) params.put("longitude", longitude);
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());

        return HttpCoreEngin.get(context).rxpost(NetConstant.book_index_url, new TypeReference<ResultInfo<BookAnswerInfoWrapper>>() {
        }.getType(), params, headers, false, false, false);


    }


    /**
     * 获取答案详情
     *
     * @param context
     * @param book_id
     * @return
     */
    public static Observable<ResultInfo<BookAnswerInfo>> getBookDetailInfo(Context context, String book_id) {
        Map<String, String> params = new HashMap<>();
        params.put("book_id", book_id);
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.book_answer_url, new TypeReference<ResultInfo<BookAnswerInfo>>() {
        }.getType(), params, headers, false, false, false);
    }




    /**
     * 上传图像
     *
     * @param file
     * @param fileName
     * @return
     */
    public static Observable<ResultInfo<UploadInfo>> uploadInfo(Context context, File file, String fileName) {

        UpFileInfo upFileInfo = new UpFileInfo();
        upFileInfo.file = file;
        upFileInfo.filename = fileName;
        upFileInfo.name = "image";
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxuploadFile(NetConstant.upload_url, new TypeReference<ResultInfo<UploadInfo>>() {
        }.getType(), upFileInfo, null, headers, false);

    }


    public static Observable<ResultInfo<List<String>>> getConditionList(Context context) {

        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.book_tag_url, new TypeReference<ResultInfo<List<String>>>() {
        }.getType(), null, headers, false, false, false);

    }


    public static Observable<ResultInfo<ShareInfo>> getShareInfo(Context context) {
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.user_share_url, new TypeReference<ResultInfo<ShareInfo>>() {
        }.getType(), null, headers, false, false, false);
    }

    /**
     * 好评
     *
     * @return
     */
    public static Observable<ResultInfo<String>> comment(Context context, String userid) {
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());
        return HttpCoreEngin.get(context).rxpost(NetConstant.task_good_comment_url, new TypeReference<ResultInfo<String>>() {
                }.getType(),
                null,headers, false, false, false);
    }






    public static Observable<ResultInfo<AdvInfo>> getAdvInfo(Context context){
        Map<String, String> headers = new HashMap<>();
        if (!TextUtils.isEmpty(UserInfoHelper.getToken()))
            headers.put("Authorization", "Bearer " + UserInfoHelper.getToken());

        return HttpCoreEngin.get(context).rxpost(NetConstant.adv_url, new TypeReference<ResultInfo<AdvInfo>>() {
        }.getType(), null,headers, false, false, false);
    }

}
