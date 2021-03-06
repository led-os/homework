package yc.com.soundmark.study.model.engine;

import android.content.Context;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.engin.HttpCoreEngin;

import java.util.HashMap;
import java.util.Map;

import rx.Observable;
import yc.com.base.BaseEngine;
import yc.com.soundmark.base.constant.UrlConfig;
import yc.com.soundmark.study.model.domain.StudyInfoWrapper;
import yc.com.soundmark.study.model.domain.StudyPages;

/**
 * Created by wanglin  on 2018/10/30 16:36.
 */
public class StudyEngine extends BaseEngine {
    public StudyEngine(Context context) {
        super(context);
    }

    public Observable<ResultInfo<StudyPages>> getStudyPages() {

        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.study_list_url, new TypeReference<ResultInfo<StudyPages>>() {
        }.getType(), null, true, true, true);
    }


    public Observable<ResultInfo<StudyInfoWrapper>> getStudyDetail(int page) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        return HttpCoreEngin.get(mContext).rxpost(UrlConfig.study_detail_url, new TypeReference<ResultInfo<StudyInfoWrapper>>() {
        }.getType(), params, true, true, true);

    }
}
