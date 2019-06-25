package yc.com.answer.index.presenter;

import android.content.Context;
import android.text.TextUtils;

import com.alibaba.fastjson.TypeReference;
import com.kk.securityhttp.domain.ResultInfo;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.vondear.rxtools.RxSPTool;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.Subscription;
import yc.com.answer.constant.SpConstant;
import yc.com.answer.index.contract.IndexContract;
import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.index.model.bean.BookAnswerInfoWrapper;
import yc.com.answer.index.model.bean.SlideInfo;
import yc.com.answer.index.model.bean.VersionDetailInfo;
import yc.com.answer.index.model.bean.VersionInfo;
import yc.com.answer.index.model.engine.IndexEngine;

import yc.com.answer.utils.EngineUtils;
import yc.com.base.BasePresenter;
import yc.com.base.CommonInfoHelper;
import yc.com.homework.base.HomeworkApp;
import yc.com.homework.base.dao.BookAnswerInfoDao;
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo;
import yc.com.homework.wall.domain.bean.HomeworkDetailInfoWrapper;
import yc.com.homework.wall.domain.bean.HomeworkInfo;

/**
 * Created by wanglin  on 2018/2/27 14:46.
 */

public class IndexPresenter extends BasePresenter<IndexEngine, IndexContract.View> implements IndexContract.Presenter {

    private BookAnswerInfoDao answerInfoDao;

    public IndexPresenter(Context context, IndexContract.View mView) {
        super(context, mView);
        mEngine = new IndexEngine(context);
        answerInfoDao = HomeworkApp.Companion.getDaoSession().getBookAnswerInfoDao();
    }


    @Override
    public void loadData(boolean forceUpdate, boolean showLoadingUI) {
        if (!forceUpdate) return;
//        getSlideInfo("home");
//        getVersionList();
//        getConditionList();
//        getHotBooks("2");

    }


    @Override
    public void getSlideInfo(String group) {
        CommonInfoHelper.getO(mContext, SpConstant.SLIDE_INFO, new TypeReference<List<SlideInfo>>() {
        }.getType(), new CommonInfoHelper.onParseListener<List<SlideInfo>>() {
            @Override
            public void onParse(List<SlideInfo> o) {
                if (o != null) {
                    showImagList(o);
                }
            }

            @Override
            public void onFail(String json) {

            }
        });

        Subscription subscription = mEngine.getSlideInfos(group).subscribe(new Subscriber<ResultInfo<List<SlideInfo>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<SlideInfo>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {
                    CommonInfoHelper.setO(mContext, listResultInfo.data, SpConstant.SLIDE_INFO);
                    showImagList(listResultInfo.data);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public SlideInfo getSlideInfo(int postion) {
        if (mSlideInfos != null) {
            return mSlideInfos.get(postion);
        }
        return null;

    }

    private List<SlideInfo> mSlideInfos;

    private void showImagList(List<SlideInfo> slideInfos) {
        List<String> imgList = new ArrayList<>();
        if (slideInfos.size() > 0) {
            mSlideInfos = slideInfos;
            for (SlideInfo slideInfo : slideInfos) {
                imgList.add(slideInfo.getImg());
            }
            mView.showImgList(imgList);
        }
    }


    @Override
    public void getVersionList() {

        Subscription subscription = mEngine.getVersionList().subscribe(new Subscriber<ResultInfo<VersionInfo>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                getCacheVersion();
            }

            @Override
            public void onNext(ResultInfo<VersionInfo> resultInfoResultInfo) {
                if (resultInfoResultInfo != null && resultInfoResultInfo.code == HttpConfig.STATUS_OK && resultInfoResultInfo.data != null) {
                    showNewData(resultInfoResultInfo.data);
                    CommonInfoHelper.setO(mContext, resultInfoResultInfo.data, SpConstant.INDEX_VERSION);

                } else {
                    getCacheVersion();
                }
            }
        });
        mSubscriptions.add(subscription);


    }


    private void getCacheVersion() {
        CommonInfoHelper.getO(mContext, SpConstant.INDEX_VERSION, new TypeReference<VersionInfo>() {
        }.getType(), new CommonInfoHelper.onParseListener<VersionInfo>() {
            @Override
            public void onParse(VersionInfo versionInfo) {
                if (versionInfo != null) {
                    showNewData(versionInfo);
                }
            }

            @Override
            public void onFail(String json) {

            }
        });
    }


    private void showNewData(VersionInfo versionInfo) {
        if (versionInfo.getSubject() != null) {
            List<VersionDetailInfo> dataSubject = versionInfo.getSubject();

            dataSubject.add(0, new VersionDetailInfo("", "全部"));

            if (dataSubject.size() > 5) {
                dataSubject = dataSubject.subList(0, 5);
            }
            mView.showVersionList(dataSubject);

        }

    }


    public void getHotBooks() {//1: 推荐；2: 热门
        String grade = RxSPTool.getString(mContext, SpConstant.SELECT_GRADE);
        if (TextUtils.isEmpty(grade)) {
            grade = "全部";
        }

        CommonInfoHelper.getO(mContext, SpConstant.HOT_BOOK, new TypeReference<List<BookAnswerInfo>>() {
        }.getType(), new CommonInfoHelper.onParseListener<List<BookAnswerInfo>>() {
            @Override
            public void onParse(List<BookAnswerInfo> bookInfoList) {
                if (bookInfoList != null) {
                    mView.showHotBooks(bookInfoList);
                }
            }

            @Override
            public void onFail(String json) {

            }
        });

        Subscription subscription = EngineUtils.getBookInfoList(mContext, 1, 3, "", "", "", grade, "", "", "", "", "", "", "", "", "", "").subscribe(new Subscriber<ResultInfo<BookAnswerInfoWrapper>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<BookAnswerInfoWrapper> bookInfoWrapperResultInfo) {
                if (bookInfoWrapperResultInfo != null && bookInfoWrapperResultInfo.code == HttpConfig.STATUS_OK && bookInfoWrapperResultInfo.data != null) {
                    mView.showHotBooks(bookInfoWrapperResultInfo.data.getLists());
                    CommonInfoHelper.setO(mContext, bookInfoWrapperResultInfo.data.getLists(), SpConstant.HOT_BOOK);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getConditionList() {

        CommonInfoHelper.getO(mContext, SpConstant.HOT_RECOMMOND, new TypeReference<List<String>>() {
        }.getType(), new CommonInfoHelper.onParseListener<List<String>>() {
            @Override
            public void onParse(List<String> o) {
                if (o != null) {
                    mView.showConditionList(o);
                }
            }

            @Override
            public void onFail(String json) {

            }
        });
        Subscription subscription = EngineUtils.getConditionList(mContext).subscribe(new Subscriber<ResultInfo<List<String>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ResultInfo<List<String>> listResultInfo) {
                if (listResultInfo != null && listResultInfo.code == HttpConfig.STATUS_OK && listResultInfo.data != null) {

                    mView.showConditionList(listResultInfo.data);
                    CommonInfoHelper.setO(mContext, listResultInfo.data, SpConstant.HOT_RECOMMOND);
                }
            }
        });
        mSubscriptions.add(subscription);
    }


    public void getCollectBookInfos(int page, int pageSize) {

        List<BookAnswerInfo> bookAnswerInfos = answerInfoDao.queryBuilder().limit(pageSize).offset((page - 1) * pageSize).orderDesc(BookAnswerInfoDao.Properties.SaveTime).build().list();
        if (bookAnswerInfos != null) {
            mView.showCollectBookInfos(bookAnswerInfos);
        } else {
            mView.showNoData();
        }
    }

    public void getWalInfos(int subject_id, final int page, int page_size, boolean isRefresh) {
        if (page == 1 && !isRefresh)
            mView.showLoading();
        Subscription subscription = yc.com.homework.base.utils.EngineUtils.Companion.getWalInfos(mContext, subject_id, page, page_size)
                .subscribe(new Subscriber<ResultInfo<ArrayList<HomeworkInfo>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultInfo<ArrayList<HomeworkInfo>> arrayListResultInfo) {
                        if (arrayListResultInfo != null) {
                            if (arrayListResultInfo.code == HttpConfig.STATUS_OK && arrayListResultInfo.data != null) {
                                mView.hide();
                                mView.showWallDetailInfos(arrayListResultInfo.data);

                            } else {
                                if (page == 1)
                                    mView.showNoData();
                            }
                        } else {
                            if (page == 1)
                                mView.showNoNet();
                        }

                    }
                });

        mSubscriptions.add(subscription);
    }

    public void getWallDetailInfo(String task_id) {
        mView.showLoading();
        Subscription subscription = yc.com.homework.base.utils.EngineUtils.Companion.getWallDetailInfo(mContext, task_id)
                .subscribe(new Subscriber<ResultInfo<HomeworkDetailInfoWrapper>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ResultInfo<HomeworkDetailInfoWrapper> t) {
                        if (t != null) {
                            if (t.code == HttpConfig.STATUS_OK && t.data != null && t.data.getTask_detail() != null && !t.data.getTask_detail().isEmpty()) {
                                String remark = t.data.getRemark();
                                HomeworkDetailInfo homeworkDetailInfo = t.data.getTask_detail().get(0);
                                homeworkDetailInfo.setRemark(remark);
                                mView.showHomeworkInfo(homeworkDetailInfo);

                            } else {
                                mView.showNoData();
                            }
                        } else {
                            mView.showNoNet();
                        }
                    }
                });

        mSubscriptions.add(subscription);
    }

}
