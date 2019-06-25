package yc.com.soundmark.category.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.kk.utils.ScreenUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import yc.com.base.BaseFragment;
import yc.com.homework.R;
import yc.com.homework.base.utils.ItemDecorationHelper;
import yc.com.soundmark.base.constant.Config;
import yc.com.soundmark.base.widget.MainToolBar;
import yc.com.soundmark.category.adapter.CategoryMainAdapter;
import yc.com.soundmark.category.contract.CategoryMainContract;
import yc.com.soundmark.category.model.domain.WeiKeCategory;
import yc.com.soundmark.category.presenter.CategoryMainPresenter;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;


/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class CategoryFragment extends BaseFragment<CategoryMainPresenter> implements CategoryMainContract.View, OnAdvStateListener {


    @BindView(R.id.category_recyclerView)
    RecyclerView categoryRecyclerView;
    @BindView(R.id.bottom_container)
    FrameLayout bottomContainer;

    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.smartRefreshLayout)
    SmartRefreshLayout smartRefreshLayout;


    private CategoryMainAdapter categoryMainAdapter;
    private int page = 1;

    private int PAGE_SIZE = 20;
    private RecyclerView.ItemDecoration itemDecoration;


    @Override
    public int getLayoutId() {
        return R.layout.fragment_category;
    }

    @Override
    public void init() {


        mPresenter = new CategoryMainPresenter(getActivity(), this);
        mainToolbar.setVisibility(View.GONE);
//        mainToolbar.init(getActivity(), null);
//        mainToolbar.setTitle(getString(R.string.main_category));
//        mainToolbar.setRightContainerVisible(false);
//        mainToolbar.setTvRightTitleAndIcon(getString(R.string.main_category), R.mipmap.diandu);

        if (!(TextUtils.equals("Xiaomi", Build.BRAND) || TextUtils.equals("xiaomi", Build.BRAND)))
            AdvDispatchManager.getManager().init(getActivity(), AdvType.BANNER, bottomContainer, null, Config.TENCENT_ADV_ID, Config.BANNER_TOP_ADV_ID, this);
        getData(false);
        categoryRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        categoryRecyclerView.setHasFixedSize(true);

        categoryMainAdapter = new CategoryMainAdapter(null);

        categoryRecyclerView.setAdapter(categoryMainAdapter);

        itemDecoration = new ItemDecorationHelper(getActivity(), 6, 6);

        categoryRecyclerView.addItemDecoration(itemDecoration);


        categoryMainAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData(false);
            }
        }, categoryRecyclerView);


        categoryMainAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), WeikeUnitActivity.class);
                intent.putExtra("category_id", categoryMainAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        categoryRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View view = recyclerView.getChildAt(0);

                if (view.getTop() < 0) {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(getActivity(), 6), 0, 0, 0);
                } else {
                    categoryRecyclerView.setPadding(ScreenUtil.dip2px(getActivity(), 6), ScreenUtil.dip2px(getActivity(), 6), 0, 0);
                }
            }
        });

        initRefresh();
    }


    @Override
    public void showWeiKeCategoryInfos(List<WeiKeCategory> weiKeCategoryList) {
        if (page == 1) {
            categoryMainAdapter.setNewData(weiKeCategoryList);
        } else {
            categoryMainAdapter.addData(weiKeCategoryList);
        }

        if (weiKeCategoryList.size() == PAGE_SIZE) {
            page++;
            categoryMainAdapter.loadMoreComplete();
        } else {
            categoryMainAdapter.loadMoreEnd();
        }
        smartRefreshLayout.finishRefresh();
    }

    @Override
    public void hide() {
    }

    @Override
    public void showLoading() {
    }

    @Override
    public void showNoData() {
        smartRefreshLayout.finishRefresh();

    }

    @Override
    public void showNoNet() {
        smartRefreshLayout.finishRefresh();
    }

    private void getData(boolean isRefresh) {
        mPresenter.getCategoryInfos(page, PAGE_SIZE, "0", isRefresh);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (itemDecoration != null) {
            categoryRecyclerView.removeItemDecoration(itemDecoration);
        }
    }

    private void initRefresh() {
        //设置 Header 为 贝塞尔雷达 样式

        smartRefreshLayout.setRefreshHeader(new ClassicsHeader(getActivity()));
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
