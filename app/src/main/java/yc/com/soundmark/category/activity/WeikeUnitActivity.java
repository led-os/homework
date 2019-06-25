package yc.com.soundmark.category.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.utils.ScreenUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import yc.com.base.BaseActivity;
import yc.com.blankj.utilcode.util.SPUtils;
import yc.com.homework.R;
import yc.com.homework.base.config.SpConstant;
import yc.com.homework.base.user.UserInfoHelper;
import yc.com.homework.base.utils.ItemDecorationHelper;
import yc.com.soundmark.base.widget.MainToolBar;
import yc.com.soundmark.category.adapter.WeiKeInfoItemAdapter;
import yc.com.soundmark.category.contract.CategoryMainContract;
import yc.com.soundmark.category.model.domain.WeiKeCategory;
import yc.com.soundmark.category.presenter.CategoryMainPresenter;

/**
 * 微课单元列表
 */

public class WeikeUnitActivity extends BaseActivity<CategoryMainPresenter> implements CategoryMainContract.View {


    private MainToolBar mainToolbar;

    private RecyclerView categoryRecyclerView;


    private SmartRefreshLayout smartRefreshLayout;


    private WeiKeInfoItemAdapter mWeiKeInfoItemAdapter;

    private String type = "";

    private String pid = "";

    private int page = 1;

    private int pageSize = 20;

    @Override
    public void init() {
        initView();
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this);
        mPresenter = new CategoryMainPresenter(this, this);
        final Intent intent = getIntent();
        if (intent != null) {
            pid = intent.getStringExtra("category_id");
        }

        mainToolbar.showNavigationIcon();
        mainToolbar.setTitle("微课学习");
        mainToolbar.setRightContainerVisible(false);

        categoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mWeiKeInfoItemAdapter = new WeiKeInfoItemAdapter(null, type);
        categoryRecyclerView.setAdapter(mWeiKeInfoItemAdapter);
        categoryRecyclerView.addItemDecoration(new ItemDecorationHelper(this, 6, 6));


        mWeiKeInfoItemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                int count = SPUtils.getInstance().getInt(SpConstant.category_count, 0);
                if (count < 2 || !UserInfoHelper.isGotoLogin(WeikeUnitActivity.this)) {
                    WeiKeCategory item = mWeiKeInfoItemAdapter.getItem(position);
                    if (item != null) {
                        Intent intent = new Intent(WeikeUnitActivity.this, WeiKeDetailActivity.class);
                        intent.putExtra("pid", item.getId());
                        startActivity(intent);
                        count++;
                        SPUtils.getInstance().put(SpConstant.category_count, count);
                    }

                }
            }
        });

        mWeiKeInfoItemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        }, categoryRecyclerView);

        categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = categoryRecyclerView.getChildAt(0);
                if (view.getTop() < 0) {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(WeikeUnitActivity.this, 6), 0, 0, 0);
                } else {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(WeikeUnitActivity.this, 6), ScreenUtil.dip2px(WeikeUnitActivity.this, 6), 0, 0);
                }
            }
        });

        getData(false);
        initRefresh();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    private void initView() {
        mainToolbar = findViewById(R.id.main_toolbar);
        categoryRecyclerView = findViewById(R.id.category_recyclerView);
        smartRefreshLayout = findViewById(R.id.smartRefreshLayout);
    }

    @Override
    public void showNoNet() {

        smartRefreshLayout.finishRefresh();

    }

    private void initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式

        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(this));
        smartRefreshLayout.setPrimaryColorsId(R.color.gray_dddddd);
        smartRefreshLayout.setEnableLoadMore(false);
//        smartRefreshLayout.autoRefresh()
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                page = 1;
                getData(true);
            }
        });
    }

    @Override
    public void showNoData() {

        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void showLoading() {

    }


    private void getData(boolean isRefresh) {
        mPresenter.getCategoryInfos(page, pageSize, pid, isRefresh);
    }

    @Override
    public void showWeiKeCategoryInfos(List<WeiKeCategory> weiKeCategoryList) {
        if (page == 1) {

            mWeiKeInfoItemAdapter.setNewData(weiKeCategoryList);
        } else {
            mWeiKeInfoItemAdapter.addData(weiKeCategoryList);
        }
        if (pageSize == weiKeCategoryList.size()) {
            page++;
            mWeiKeInfoItemAdapter.loadMoreComplete();
        } else {
            mWeiKeInfoItemAdapter.loadMoreEnd();
        }

        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void hide() {

    }

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

}
