package yc.com.answer.index.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.functions.Action1;
import yc.com.answer.base.Constant;
import yc.com.answer.index.contract.IndexContract;
import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.index.model.bean.VersionDetailInfo;
import yc.com.answer.index.presenter.IndexPresenter;
import yc.com.answer.index.ui.activity.AnswerDetailActivity;
import yc.com.answer.index.ui.activity.CollectActivity;
import yc.com.answer.index.ui.activity.SearchActivity;
import yc.com.answer.index.ui.adapter.IndexBookAdapter;
import yc.com.answer.index.ui.widget.BaseSearchView;
import yc.com.base.BaseActivity;
import yc.com.base.BaseFragment;
import yc.com.base.ObservableManager;
import yc.com.homework.R;
import yc.com.homework.base.utils.ItemDecorationHelper;
import yc.com.homework.base.widget.MainToolBar;
import yc.com.homework.examine.activity.ExamDetailActivity;
import yc.com.homework.examine.activity.HistoryMainActivity;
import yc.com.homework.wall.adapter.WallDetailAdapter;
import yc.com.homework.wall.domain.bean.HomeworkDetailInfo;
import yc.com.homework.wall.domain.bean.HomeworkInfo;
import yc.com.homework.wall.fragment.WallMainActivity;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/2/27 14:43.
 */

public class IndexAnswerFragment extends BaseFragment<IndexPresenter> implements IndexContract.View, OnAdvStateListener, Observer {


    @BindView(R.id.baseSearchView)
    BaseSearchView baseSearchView;
    @BindView(R.id.hot_recyclerView)
    RecyclerView hotRecyclerView;
    @BindView(R.id.collect_recyclerView)
    RecyclerView collectRecyclerView;

    @BindView(R.id.bottomContainer)
    FrameLayout bottomContainer;
    @BindView(R.id.iv_bottombanner_close)
    ImageView ivBottombannerClose;
    @BindView(R.id.rl_bottombanner)
    RelativeLayout rlBottombanner;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    @BindView(R.id.mainToolBar)
    MainToolBar mainToolBar;
    @BindView(R.id.tv_more_collect)
    TextView tvMoreCollect;
    @BindView(R.id.tv_more_homework)
    TextView tvMoreHomework;
    @BindView(R.id.homework_recyclerView)
    RecyclerView homeworkRecyclerView;
    @BindView(R.id.rl_upload)
    RelativeLayout rlUpload;


    private IndexBookAdapter hotItemAdapter;

    private IndexBookAdapter collectAdapter;

    private static final String TAG = "IndexAnswerFragment";
    private int page = 1;
    private int pageSize = 6;
    private WallDetailAdapter wallDetailAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.answer_fragment_index;
    }

    @Override
    public void init() {
        mainToolBar.setRightContainerVisible(false);
        mainToolBar.setTitle(getString(R.string.answer_look));
//        mainToolBar.showNavigationIcon(R.mipmap.back);
//        mainToolBar.init(((BaseActivity) getActivity()));

        mPresenter = new IndexPresenter(getActivity(), this);
        ObservableManager.get().addMyObserver(this);

        hotRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        hotItemAdapter = new IndexBookAdapter(null);
        hotRecyclerView.setAdapter(hotItemAdapter);

        hotRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 10, 10));

        collectRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        collectAdapter = new IndexBookAdapter(null);
        collectRecyclerView.setAdapter(collectAdapter);
        collectRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 10, 10));

        homeworkRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        wallDetailAdapter = new WallDetailAdapter(null);
        homeworkRecyclerView.setAdapter(wallDetailAdapter);
        homeworkRecyclerView.addItemDecoration(new ItemDecorationHelper(getActivity(), 10));

        getData();

        AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, bottomContainer, null, Constant.TENCENT_ADV_ID, Constant.BANNER_ADV_ID, this);
        initRefresh();
        initListener();

    }


    private void initListener() {


        RxView.clicks(baseSearchView).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), SearchActivity.class));
            }
        });


        hotItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                BookAnswerInfo bookInfo = (BookAnswerInfo) adapter.getItem(position);
                if (bookInfo != null) {
                    AnswerDetailActivity.startActivity(getActivity(), bookInfo.getName(), bookInfo.getBookId());

                }
            }
        });

        collectAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookAnswerInfo answerInfo = collectAdapter.getItem(position);
                if (answerInfo != null) {
                    AnswerDetailActivity.startActivity(getActivity(), answerInfo.getName(), answerInfo.getBookId());
                }
            }
        });

        RxView.clicks(ivBottombannerClose).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                rlBottombanner.setVisibility(View.GONE);
            }
        });

        RxView.clicks(tvMoreCollect).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                //更多收藏
                startActivity(new Intent(getActivity(), CollectActivity.class));
            }
        });

        RxView.clicks(rlUpload).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Intent intent = new Intent(getActivity(), HistoryMainActivity.class);
                startActivity(intent);
            }
        });

        RxView.clicks(tvMoreHomework).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                startActivity(new Intent(getActivity(), WallMainActivity.class));
            }
        });

        wallDetailAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                HomeworkInfo homeworkInfo = wallDetailAdapter.getItem(position);
                if (homeworkInfo != null) {
                    Intent intent = new Intent(getActivity(), ExamDetailActivity.class);
                    intent.putExtra("index", 1);
                    intent.putExtra("taskId", homeworkInfo.getTaskId());
                    startActivity(intent);
                }
            }
        });
    }


    @Override
    public void hide() {

    }

    @Override
    public void showNoNet() {

    }

    @Override
    public void showNoData() {

    }

    @Override
    public void showLoading() {

    }


    @Override
    public void showVersionList(List<VersionDetailInfo> data) {

    }

    @Override
    public void showImgList(List<String> imgList) {

    }


    @Override
    public void showHotBooks(List<BookAnswerInfo> lists) {
        if (lists != null) {
            hotItemAdapter.setNewData(lists);
        }
        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }

    }

    @Override
    public void showConditionList(List<String> data) {


    }

    @Override
    public void showCollectBookInfos(List<BookAnswerInfo> bookAnswerInfos) {
        collectAdapter.setNewData(bookAnswerInfos);
    }

    @Override
    public void showWallDetailInfos(ArrayList<HomeworkInfo> data) {
        wallDetailAdapter.setNewData(data);
    }

    @Override
    public void showHomeworkInfo(HomeworkDetailInfo homeworkDetailInfo) {

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void initRefresh() {
        //设置 Header 为 BezierRadar 样式
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
        smartRefreshLayout.setEnableLoadMore(false);
//        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                getData();
//                mPresenter.loadData(true);

            }
        });
    }

    private void getData() {
        mPresenter.getHotBooks();
        mPresenter.getCollectBookInfos(page, pageSize);
//        mPresenter.getSlideInfo("home");
        mPresenter.getVersionList();
        mPresenter.getWalInfos(0, 1, 4, false);
//        mPresenter.getConditionList();

    }


    @Override
    public void onShow() {
        ivBottombannerClose.setVisibility(View.VISIBLE);
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


    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg instanceof String) {
                String result = (String) arg;
                if (TextUtils.equals("collect", result)) {
                    //收藏
                    mPresenter.getCollectBookInfos(page, pageSize);
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ObservableManager.get().deleteMyObserver(this);
    }


}
