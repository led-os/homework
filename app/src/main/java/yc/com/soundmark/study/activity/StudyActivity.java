package yc.com.soundmark.study.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import yc.com.base.BaseActivity;
import yc.com.homework.R;
import yc.com.homework.wall.utils.ReflexUtils;
import yc.com.soundmark.base.utils.UIUtils;
import yc.com.soundmark.base.widget.MainToolBar;
import yc.com.soundmark.category.activity.CategoryFragment;
import yc.com.soundmark.index.activity.PhoneticActivity;
import yc.com.soundmark.study.adapter.StudyAdapter;
import yc.com.soundmark.study.fragment.StudyFragment;

/**
 * Created by wanglin  on 2018/10/24 17:21.
 */
public class StudyActivity extends BaseActivity {


    @BindView(R.id.main_toolbar)
    MainToolBar mainToolbar;
    @BindView(R.id.ll_top_tint)
    LinearLayout llTopTint;
    @BindView(R.id.study_tabLayout)
    TabLayout studyTabLayout;
    @BindView(R.id.study_viewPager)
    ViewPager studyViewPager;
    @BindView(R.id.container)
    LinearLayout container;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    public int getLayoutId() {
        return R.layout.activity_study;
    }

    @Override
    public void init() {


        mainToolbar.setTitle(getString(R.string.soundmark_teach));
        mainToolbar.showNavigationIcon();
        mainToolbar.init(this, PhoneticActivity.class);
        mainToolbar.setTvRightTitleAndIcon(getString(R.string.phonetic_introduce), R.mipmap.soundmark_introduce_icon);

        UIUtils.getInstance(this).measureViewLoction(llTopTint);

        fragments.add(new StudyFragment());
        fragments.add(new CategoryFragment());
        StudyAdapter studyAdapter = new StudyAdapter(this, getSupportFragmentManager(), fragments);
        studyViewPager.setAdapter(studyAdapter);
        studyTabLayout.setupWithViewPager(studyViewPager);
        ReflexUtils.INSTANCE.reflex(studyTabLayout);

    }


    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

}
