package yc.com.soundmark.study.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.jakewharton.rxbinding.view.RxView;
import com.kk.securityhttp.net.contains.HttpConfig;
import com.kk.utils.ToastUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.xinqu.videoplayer.XinQuVideoPlayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.base.BaseFragment;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.homework.R;
import yc.com.homework.base.config.SpConstant;
import yc.com.homework.base.user.UserInfoHelper;
import yc.com.homework.base.widget.StateView;
import yc.com.homework.mine.fragment.PayFragment;
import yc.com.homework.read.fragment.BookReadUnlockFragment;
import yc.com.soundmark.base.constant.Config;
import yc.com.soundmark.base.widget.MainToolBar;
import yc.com.soundmark.study.adapter.StudyMainAdapter;
import yc.com.soundmark.study.contract.StudyContract;
import yc.com.soundmark.study.model.domain.StudyInfoWrapper;
import yc.com.soundmark.study.presenter.StudyPresenter;
import yc.com.soundmark.study.utils.AVMediaManager;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class StudyFragment extends BaseFragment<StudyPresenter> implements StudyContract.View, OnAdvStateListener {


    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.study_viewPager)
    ViewPager studyViewPager;
    @BindView(R.id.iv_show_vowel)
    ImageView ivShowVowel;
    @BindView(R.id.iv_next)
    ImageView ivNext;
    @BindView(R.id.iv_pre)
    ImageView ivPre;
    @BindView(R.id.bottom_container)
    FrameLayout bottomContainer;
    @BindView(R.id.fl_container)
    FrameLayout flContainer;


    private List<Fragment> fragments = new ArrayList<>();
    private int currentPos = 0;
    private int totalPages;//总页码


    @Override
    public int getLayoutId() {
        return R.layout.fragment_study;
    }

    @Override
    public void init() {
        mPresenter = new StudyPresenter(getActivity(), this);
        mPresenter.getStudyPages();
        initListener();
//        mainToolbar.init(((BaseActivity) getActivity()), WebActivity.class);
        mainToolbar.setTvRightTitleAndIcon(getString(R.string.diandu), R.mipmap.diandu);

        if (!(TextUtils.equals("Xiaomi", Build.BRAND) || TextUtils.equals("xiaomi", Build.BRAND)))
            AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, bottomContainer, null, Config.TENCENT_ADV_ID, Config.BANNER_BOTTOM_ADV_ID, this);

    }

    private void initListener() {
        RxView.clicks(ivShowVowel).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                StudyVowelFragment studyVowelFragment = new StudyVowelFragment();
                studyVowelFragment.setOnClickListener(new StudyVowelFragment.onClickListener() {
                    @Override
                    public void onClick(int pos) {
                        int count = SPUtils.getInstance().getInt(SpConstant.soundmark_count);
                        if (count < 2 || !UserInfoHelper.isGotoLogin(getActivity())) {
                            if (pos < totalPages) {

                                studyViewPager.setCurrentItem(pos);
                                currentPos = pos;

                                if (pos == 0) {
                                    ivPre.setImageResource(R.mipmap.study_pre_normal);
                                } else if (pos == totalPages - 1) {
                                    ivNext.setImageResource(R.mipmap.study_next_normal_);
                                }
                            } else {
                                studyViewPager.setCurrentItem(currentPos);
                            }
                            count++;
                            SPUtils.getInstance().put(SpConstant.soundmark_count, count);
                        }


                    }
                });

                studyVowelFragment.show(getFragmentManager(), "");
            }
        });

        RxView.clicks(ivNext).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //todo 下一页
                int count = SPUtils.getInstance().getInt(SpConstant.soundmark_count);
                if (count < 2 || !UserInfoHelper.isGotoLogin(getActivity())) {
                    currentPos++;
                    if (currentPos < totalPages) {
                        if (isCanNext(currentPos)) {


                            next(currentPos);


                        } else {
                            currentPos--;
                            showPayDialog();
                        }
                    } else {
                        currentPos--;
                        ToastUtil.toast2(getActivity(), "已经是最后一页了");
                    }
                    count++;
                    SPUtils.getInstance().put(SpConstant.soundmark_count, count);
                }
//            }     LogUtil.msg("currentPos: next--> " + currentPos);


            }
        });
        RxView.clicks(ivPre).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                // TODO: 2018/11/2 上一页
                if (currentPos > 0) {
                    currentPos--;
                    pre(currentPos);
                } else {
                    ivPre.setImageResource(R.mipmap.study_pre_normal);
                    ToastUtil.toast2(getActivity(), "已经是第一页了");
                }
//                LogUtil.msg("currentPos: pre--> " + currentPos);
            }
        });


    }


    private void next(int pos) {//下一页

        studyViewPager.setCurrentItem(pos);

        ivPre.setImageResource(R.mipmap.study_pre_selected);
        if (pos == totalPages - 1) {
            ivNext.setImageResource(R.mipmap.study_next_normal_);
        }

    }

    private void pre(int pos) {//上一页

        studyViewPager.setCurrentItem(pos);
        ivNext.setImageResource(R.mipmap.study_next_selected);
        if (pos == 0) {
            ivPre.setImageResource(R.mipmap.study_pre_normal);
        }
    }


    @Override
    public void showStudyPages(Integer data) {
        totalPages = data;
        initViewPager(data);
    }

    private void initViewPager(Integer data) {

        for (int i = 0; i < data; i++) {
            StudyMainFragment studyMainFragment = new StudyMainFragment();
            studyMainFragment.setPos(i);

            List<Fragment> fragmentList = new ArrayList<>();
            fragmentList.add(new StudyWordFragment());
            fragmentList.add(new StudyPhraseFragment());
            fragmentList.add(new StudySentenceFragment());
            studyMainFragment.setFragments(fragmentList);

            fragments.add(studyMainFragment);
        }

        StudyMainAdapter mainAdapter = new StudyMainAdapter(getChildFragmentManager(), fragments);
        studyViewPager.setAdapter(mainAdapter);
//        studyViewPager.setOffscreenPageLimit(2);
        studyViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                XinQuVideoPlayer.releaseAllVideos();
                AVMediaManager.getInstance().releaseAudioManager();

//                LogUtil.msg("currentPos: scroll-->" + currentPos + "--position-->" + position);
                int count = SPUtils.getInstance().getInt(SpConstant.soundmark_count, 0);
                if (count < 2 || !UserInfoHelper.isGotoLogin(getActivity())) {
                    if (isCanNext(position)) {

                        if (currentPos > position) {
                            pre(position);
                        } else {
                            next(position);
                        }

                        currentPos = position;
                    } else {
                        studyViewPager.setCurrentItem(currentPos);
                        showPayDialog();
                    }
                    count++;
                    SPUtils.getInstance().put(SpConstant.soundmark_count, count);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    @Override
    public void showStudyInfo(StudyInfoWrapper data) {

    }


    private boolean isCanNext(int pos) {
        boolean isNext = false;
        if (UserInfoHelper.isVip() || pos < 4) {
            isNext = true;
        }
        return isNext;

    }

    private BookReadUnlockFragment unlockFragment;

    private void showPayDialog() {
//        BasePayFragment basePayFragment = new BasePayFragment();
//        basePayFragment.show(getFragmentManager(), "");
        PayFragment payFragment = new PayFragment();
        payFragment.show(getChildFragmentManager(), "");


//        if (unlockFragment == null)
//            unlockFragment = new BookReadUnlockFragment();
//
//        Bundle bundle = new Bundle();
//        bundle.putString("content", "成功分享给好友并邀请好友注册作业啦，便可以解锁所有的音标学习哦！");
//        bundle.putString("title", "分享好友，解锁音标");
//        unlockFragment.setArguments(bundle);
//
//        if (!unlockFragment.isVisible())
//            unlockFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showLoading() {
        stateView.showLoading(flContainer);
    }

    @Override
    public void showNoData() {
        stateView.showNoData(flContainer);
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(flContainer, HttpConfig.NET_ERROR, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.getStudyPages();
            }
        });

    }


    @Override
    public void fetchData() {
        super.fetchData();

    }


    @Override
    public void onShow() {

    }

    @Override
    public void onDismiss(long delayTime) {

    }

    @Override
    public void onError() {

    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView view) {

    }

    @Override
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {

    }


}
