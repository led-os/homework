package yc.com.soundmark.study.contract;

import yc.com.base.IHide;
import yc.com.base.ILoading;
import yc.com.base.INoData;
import yc.com.base.INoNet;
import yc.com.base.IPresenter;
import yc.com.base.IView;
import yc.com.soundmark.study.model.domain.StudyInfoWrapper;

/**
 * Created by wanglin  on 2018/10/30 16:40.
 */
public interface StudyContract {

    interface View extends IView, ILoading, INoData, INoNet, IHide {
        void showStudyPages(Integer data);

        void showStudyInfo(StudyInfoWrapper data);
    }

    interface Presenter extends IPresenter {
        void getStudyPages();

        void getStudyDetail(int page);
    }
}
