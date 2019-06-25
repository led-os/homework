package yc.com.answer.index.ui.fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jakewharton.rxbinding.view.RxView;
import com.kk.utils.ScreenUtil;
import com.qq.e.ads.nativ.NativeExpressADView;
import com.vondear.rxtools.RxSPTool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import rx.functions.Action1;
import yc.com.answer.base.Constant;
import yc.com.answer.constant.SpConstant;
import yc.com.answer.index.contract.BookConditionContract;
import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.index.presenter.BookConditionPresenter;
import yc.com.answer.index.ui.activity.AnswerDetailActivity;
import yc.com.answer.index.ui.adapter.SearchResultItemAdapter;
import yc.com.answer.index.ui.widget.FilterPopWindow;
import yc.com.base.BaseFragment;
import yc.com.base.ObservableManager;
import yc.com.homework.R;
import yc.com.homework.base.user.UserInfoHelper;
import yc.com.homework.base.widget.StateView;
import yc.com.homework.read.domain.bean.BookInfo;
import yc.com.tencent_adv.AdvDispatchManager;
import yc.com.tencent_adv.AdvType;
import yc.com.tencent_adv.OnAdvStateListener;

/**
 * Created by wanglin  on 2018/3/10 10:09.
 */

public class SearchFragment extends BaseFragment<BookConditionPresenter> implements BookConditionContract.View, OnAdvStateListener, Observer {

    @BindView(R.id.tv_grade)
    TextView tvGrade;
    @BindView(R.id.iv_grade)
    ImageView ivGrade;
    @BindView(R.id.ll_grade)
    LinearLayout llGrade;
    @BindView(R.id.tv_subject)
    TextView tvSubject;
    @BindView(R.id.iv_subject)
    ImageView ivSubject;
    @BindView(R.id.ll_subject)
    LinearLayout llSubject;
    @BindView(R.id.tv_part)
    TextView tvPart;
    @BindView(R.id.iv_part)
    ImageView ivPart;
    @BindView(R.id.ll_part)
    LinearLayout llPart;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.iv_version)
    ImageView ivVersion;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.stateView)
    StateView stateView;
    @BindView(R.id.search_recyclerView)
    RecyclerView searchRecyclerView;
    @BindView(R.id.ll_top_guide)
    LinearLayout llTopGuide;
    @BindView(R.id.rl_feedback)
    RelativeLayout rlFeedback;


    private String grade;//年级
    private String subject;//学科
    private String part;//上下册
    private String version;//版本

    private String mName;//书名
    private String code;//条形码

    private int page = 1;
    private final int LIMIT = 20;
    private SearchResultItemAdapter itemAdapter;
    private TextView textView;

    public static final int AD_COUNT = 2;// 加载广告的条数，取值范围为[1, 10]
    private static final String TAG = "SearchFragment";

    public static int FIRST_AD_POSITION = 2; // 第一条广告的位置
    public static int SECOND_AD_POSITION = 8; // 第一条广告的位置
    private HashMap<NativeExpressADView, Integer> mAdViewPositionMap = new HashMap<>();

    @Override
    public int getLayoutId() {
        return R.layout.fragment_new_search;
    }

    public String getName() {

        return mName;
    }

    public void setName(String name) {
        this.mName = name;
        code = "";
        page = 1;
        getData();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public void init() {
        ObservableManager.get().addMyObserver(this);
        mPresenter = new BookConditionPresenter(getActivity(), this);
        code = getArguments().getString("code");

        initView();
        initAdapter();
        initListener();
        List<Integer> positions = new ArrayList<>();
        positions.add(FIRST_AD_POSITION);
        positions.add(SECOND_AD_POSITION);
        AdvDispatchManager.getManager().init(getActivity(), AdvType.ORIGIN_PIC, null, null, Constant.TENCENT_ADV_ID, Constant.NATIVE_ADV_ID, AD_COUNT, positions, this);

    }

    private void initView() {
        grade = RxSPTool.getString(getActivity(), SpConstant.SELECT_GRADE);
        subject = RxSPTool.getString(getActivity(), SpConstant.SELECT_SUBJECT);
        part = RxSPTool.getString(getActivity(), SpConstant.SELECT_PART);
        version = RxSPTool.getString(getActivity(), SpConstant.SELECT_VERSION);

        if (!TextUtils.isEmpty(grade)) tvGrade.setText(grade);
        if (!TextUtils.isEmpty(subject)) tvSubject.setText(subject);
        if (!TextUtils.isEmpty(part)) tvPart.setText(part);
        if (!TextUtils.isEmpty(version)) tvVersion.setText(version);
        getData();
    }


    private void initListener() {
        itemAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                getData();
            }
        }, searchRecyclerView);

        itemAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                BookAnswerInfo bookInfo = itemAdapter.getItem(position);
                if (bookInfo != null) {
                    AnswerDetailActivity.startActivity(getActivity(), bookInfo.getName(), bookInfo.getBookId());
                }
            }
        });

        itemAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

                textView = (TextView) view;
//                if (UserInfoHelper.isLogin()) {
//                    mPresenter.favoriteAnswer((BookAnswerInfo) adapter.getItem(position));
//                } else {
                    mPresenter.saveBook((BookAnswerInfo) adapter.getItem(position));
//                }
            }
        });
        RxView.clicks(llGrade).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPopWindow(getString(R.string.grade), ivGrade, tvGrade);

            }
        });
        RxView.clicks(llSubject).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

                showPopWindow(getString(R.string.subject), ivSubject, tvSubject);

            }
        });
        RxView.clicks(llPart).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPopWindow(getString(R.string.part), ivPart, tvPart);

            }
        });
        RxView.clicks(llVersion).throttleFirst(200, TimeUnit.MILLISECONDS).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                showPopWindow(getString(R.string.version), ivVersion, tvVersion);
            }
        });

        searchRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                View child = searchRecyclerView.getChildAt(0);
                if (child != null) {
                    if (child.getTop() < 0) {
                        searchRecyclerView.setPadding(searchRecyclerView.getPaddingLeft(), 0, searchRecyclerView.getPaddingRight(), searchRecyclerView.getPaddingBottom());
                    } else {
                        searchRecyclerView.setPadding(searchRecyclerView.getPaddingLeft(), ScreenUtil.dip2px(getActivity(), 10f), searchRecyclerView.getPaddingRight(), searchRecyclerView.getPaddingBottom());

                    }
                }

            }
        });


    }

    private void showPopWindow(String name, final ImageView iv, final TextView tv) {
        final FilterPopWindow popWindow = new FilterPopWindow(getActivity(), name);
        popWindow.showAsDropDown(llTopGuide);
        iv.setImageResource(R.mipmap.search_up);
        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                iv.setImageResource(R.mipmap.search_down);
            }
        });
        popWindow.setOnPopClickListener(new FilterPopWindow.OnPopClickListener() {
            @Override
            public void onPopClick(String popName) {
                popWindow.dismiss();
                tv.setText(popName);
                grade = tvGrade.getText().toString();
                subject = tvSubject.getText().toString();
                part = tvPart.getText().toString();
                version = tvVersion.getText().toString();
                page = 1;
                code = "";
                mName = "";
                getData();

            }
        });

    }

    private void initAdapter() {
        searchRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        itemAdapter = new SearchResultItemAdapter(null, mAdViewPositionMap);
        searchRecyclerView.setAdapter(itemAdapter);

    }

    @Override
    public void showConditionList(List<String> data) {
    }

    @Override
    public void showBookInfoList(List<BookAnswerInfo> lists) {
        if (page == 1) {
            itemAdapter.setNewData(lists);
        } else {
            itemAdapter.addData(lists);
        }

        if (lists.size() > 0 && lists.size() == LIMIT) {
            page++;
            itemAdapter.loadMoreComplete();
        } else {
            itemAdapter.loadMoreEnd();
        }

    }

    @Override
    public void showFavoriteResult(boolean isCollect) {
        textView.setBackgroundResource(isCollect ? R.drawable.book_collect_gray_bg : R.drawable.book_collect_red_bg);
        textView.setText(isCollect ? "已收藏" : "收藏");
    }

    @Override
    public void showHomeworkGuide() {
        HomeworkGuideFragment homeworkGuideFragment = new HomeworkGuideFragment();
        homeworkGuideFragment.show(getChildFragmentManager(), "");
    }

    @Override
    public void hide() {
        stateView.hide();
    }

    @Override
    public void showNoNet() {
        stateView.showNoNet(searchRecyclerView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });
    }

    @Override
    public void showNoData() {
        stateView.showNoData(searchRecyclerView);
    }

    @Override
    public void showLoading() {
        stateView.showLoading(searchRecyclerView);
    }

    private void getData() {
        if (!TextUtils.isEmpty(code)) {
            mPresenter.getBookList(page, LIMIT, "", code, "", "", "", "", "", "", "", "", "", "");
        } else if (!TextUtils.isEmpty(mName)) {
            mPresenter.getBookList(page, LIMIT, mName, "", "", "", "", "", "", "", "", "", "", "");
        } else {
            mPresenter.getBookList(page, LIMIT, "", "", "", grade, "", part, "", version, "", subject, "", "");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        RxSPTool.putString(getActivity(), SpConstant.SELECT_GRADE, grade);
        RxSPTool.putString(getActivity(), SpConstant.SELECT_SUBJECT, subject);
        RxSPTool.putString(getActivity(), SpConstant.SELECT_PART, part);
        RxSPTool.putString(getActivity(), SpConstant.SELECT_VERSION, version);

        for (Map.Entry<NativeExpressADView, Integer> nativeExpressADViewIntegerEntry : mAdViewPositionMap.entrySet()) {
            if (nativeExpressADViewIntegerEntry != null)
                nativeExpressADViewIntegerEntry.getKey().destroy();
        }
        ObservableManager.get().deleteMyObserver(this);
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
    public void onNativeExpressShow(Map<NativeExpressADView, Integer> mDatas) {
        for (Map.Entry<NativeExpressADView, Integer> nativeExpressADView : mDatas.entrySet()) {

            mAdViewPositionMap.put(nativeExpressADView.getKey(), nativeExpressADView.getValue());
            BookAnswerInfo bookInfo = new BookAnswerInfo();
            bookInfo.setType(BookInfo.ADV);
//            bookInfo.set(nativeExpressADView.getKey());

            itemAdapter.addADViewToPosition(nativeExpressADView.getValue(), bookInfo);
        }
    }

    @Override
    public void onNativeExpressDismiss(NativeExpressADView nativeExpressADView) {
        if (itemAdapter != null) {
            int removedPosition = mAdViewPositionMap.get(nativeExpressADView);
            itemAdapter.removeADView(removedPosition, nativeExpressADView);
        }
    }


    @Override
    public void update(Observable o, Object arg) {
        if (arg != null) {
            if (arg instanceof String) {
                String result = (String) arg;
                if (TextUtils.equals("collect", result)) {
                    page = 1;
                    getData();
                }

            }
        }
    }
}
