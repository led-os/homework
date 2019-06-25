package yc.com.soundmark.category.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;
import yc.com.soundmark.base.constant.UrlConfig;
import yc.com.soundmark.category.model.domain.WeiKeCategoryWrapper;

/**
 * Created by wanglin  on 2018/10/25 14:01.
 */
public class CategoryMainEngine extends BaseEngine {
    public CategoryMainEngine(Context context) {
        super(context);
    }


    public Observable<ResultInfo<WeiKeCategoryWrapper>> getCategoryInfos(int page, int page_size, String pid) {

        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");

        params.put("page_size", page_size + "");

        params.put("pid", pid);


        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.category_url, new TypeReference<ResultInfo<WeiKeCategoryWrapper>>() {
        }.getType(), params, true, true, true);
    }
}
