package yc.com.answer.index.contract;


import java.util.ArrayList;
import java.util.List;

import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.index.model.bean.VersionDetailInfo;
import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo;
import yc.com.homework.wall.domain.bean.HomeworkInfo;


/**
 * Created by wanglin  on 2018/2/27 14:46.
 */

public interface IndexContract {

    interface View extends IView, ILoading, INoData, INoNet, IHide {
        void showVersionList(List<VersionDetailInfo> data);


        void showImgList(List<String> imgList);

        void showHotBooks(List<BookAnswerInfo> lists);

        void showConditionList(List<String> data);

        void showCollectBookInfos(List<BookAnswerInfo> bookAnswerInfos);

        void showWallDetailInfos(ArrayList<HomeworkInfo> data);

        void showHomeworkInfo(HomeworkDetailInfo homeworkDetailInfo);
    }

    interface Presenter extends IPresenter {


        void getSlideInfo(String group);

        void getVersionList();
    }
}
