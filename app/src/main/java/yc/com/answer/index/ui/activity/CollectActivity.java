package yc.com.answer.index.ui.activity;


import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.hwangjr.rxbus.thread.EventThread;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Observable;
import java.util.Observer;

import butterknife.BindView;
import yc.com.answer.constant.BusAction;
import yc.com.answer.index.contract.CollectContract;
import yc.com.answer.index.model.bean.BookAnswerInfoWrapper;
import yc.com.answer.index.presenter.CollectPresenter;
import yc.com.answer.index.ui.adapter.IndexBookAdapter;
import yc.com.base.BaseActivity;
import yc.com.base.ObservableManager;
import yc.com.homework.R;
import yc.com.homework.base.user.UserInfo;
import yc.com.homework.base.utils.ItemDecorationHelper;
import yc.com.homework.base.widget.MainToolBar;
import yc.com.homework.base.widget.StateView;
import yc.com.homework.read.domain.bean.BookInfo;

/**
 * Created by wanglin  on 2018/3/7 13:53.
 */

public class CollectActivity extends BaseActivity<CollectPresenter> implements CollectContract.View, Observer {


    @BindView(R.id.mainToolBar)
    MainToolBar mainToolBar;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.collect_recyclerView)
    RecyclerView collectRecyclerView;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;
    private IndexBookAdapter indexBookAdapter;

    private int page = 1;
    private int limit = 15;
    private TextView tvCollectCount;
    private int count;//总共收藏总数


    private static final String TAG = "CollectActivity";

    @Override
    public int getLayoutId() {
        return R.layout.fragment_collect;
    }

    @Override
    public void init() {

        ObservableManager.get().addMyObserver(this);
        mPresenter = new CollectPresenter(this, this);
        mainToolBar.setTitle(getString(R.string.main_collect));
        mainToolBar.setRightContainerVisible(false);
        mainToolBar.showNavigationIcon(R.mipmap.back);
        mainToolBar.init(this);

        collectRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        indexBookAdapter = new IndexBookAdapter(null);
        collectRecyclerView.setAdapter(indexBookAdapter);
        collectRecyclerView.addItemDecoration(new ItemDecorationHelper(this, 10, 10));

        addHeaderView();
        initListener();
        initRefresh();
        getData(false);


    }

    private void addHeaderView() {
        View headerView = View.inflate(this, R.layout.collect_header_view, null);
        tvCollectCount = headerView.findViewById(R.id.tv_header_view);
        indexBookAdapter.addHeaderView(headerView);
    }

    private void initListener() {
        indexBookAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);

            }
        }, collectRecyclerView);

        indexBookAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookInfo bookInfo = (BookInfo) adapter.getItem(position);
                if (bookInfo != null) {
                    AnswerDetailActivity.startActivity(CollectActivity.this, bookInfo.getName(), bookInfo.getBookId());
                }
            }
        });

    }


    @Override
    public void showCollectList(BookAnswerInfoWrapper data) {

        if (data != null && data.getLists() != null) {
            count += data.getCount();

            String str = "共收藏 <font color=\"#FF0000\"> " + count + "</font> 个";

            tvCollectCount.setText(Html.fromHtml(str));

            if (page == 1) {
                indexBookAdapter.setNewData(data.getLists());
            } else {
                indexBookAdapter.addData(data.getLists());
            }
            if (data.getLists().size() == limit) {
                page++;
                indexBookAdapter.loadMoreComplete();
            } else {
                indexBookAdapter.loadMoreEnd();
            }

        } else {
            indexBookAdapter.loadMoreEnd();
        }

        if (smartRefreshLayout != null) {
            smartRefreshLayout.finishRefresh();
        }


    }

    @Override
    public void showEnd() {
        indexBookAdapter.loadMoreEnd();
    }

    @Override
    public void showTintInfo(CharSequence tint) {

        stateView.showNoData(smartRefreshLayout, String.valueOf(tint));
    }


    public void getData(boolean isRefresh) {

        mPresenter.getCollectList(page, limit, isRefresh);
    }


    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(smartRefreshLayout, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData(false);
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(smartRefreshLayout);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(smartRefreshLayout);
    }


    private void initRefresh() {
        //设置 Header 为 BezierRadar 样式
        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setEnableLoadMore(false);
//        smartRefreshLayout.autoRefresh();
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(true);

            }
        });
    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg instanceof UserInfo) {
                getData(false);
            } else if (arg instanceof String) {
                String result = (String) arg;
                if (TextUtils.equals("collect", result)) {
                    page = 1;
                    getData(true);
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ObservableManager.get().deleteMyObserver(this);
    }
}
