package yc.com.answer.index.model.engine;


import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import rx.Observable;
import yc.com.answer.base.BaseEngine;
import yc.com.answer.constant.NetConstant;
import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.index.model.bean.SlideInfo;
import yc.com.answer.index.model.bean.VersionInfo;


/**
 * Created by wanglin  on 2018/2/27 13:34.
 */

public class IndexEngine extends BaseEngine {

    public IndexEngine(Context context) {
        super(context);

    }


    public Observable<ResultInfo<List<SlideInfo>>> getSlideInfos(String group) {

        Map<String, String> params = new HashMap<>();

        params.put("group", group);

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.slide_index_url, new TypeReference<ResultInfo<List<SlideInfo>>>() {
        }.getType(), params, getHeaders(), false, false, false);

    }


    public Observable<ResultInfo<VersionInfo>> getVersionList() {

        return HttpCoreEngin.get(mContext).rxpost(NetConstant.book_version_url, new TypeReference<ResultInfo<VersionInfo>>() {
        }.getType(), null, getHeaders(), false, false, false);
    }





}
