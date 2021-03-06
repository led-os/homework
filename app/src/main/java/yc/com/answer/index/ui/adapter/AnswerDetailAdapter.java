package yc.com.answer.index.ui.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.github.chrisbanes.photoview.PhotoViewAttacher;

import java.util.ArrayList;
import java.util.List;

import yc.com.homework.R;

/**
 * Created by wanglin  on 2018/3/12 11:25.
 */

public class AnswerDetailAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImageList;
    private List<String> mTitles;

    private boolean isClick = false;

    private boolean isDoubleClick = false;

    public AnswerDetailAdapter(Context context, List<String> imageList) {
        this.mImageList = imageList;
        this.mContext = context;
        mTitles = new ArrayList<>();
        if (imageList != null) {
            for (int i = 0; i < imageList.size(); i++) {
                mTitles.add((i + 1) + "");
            }
        }
    }

    @Override
    public int getCount() {
        return mImageList == null ? 0 : mImageList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_answer_detail_item, container, false);

        String path = mImageList.get(position);

        final PhotoView scaleImageView = view.findViewById(R.id.xImageView);

        Glide.with(mContext).load(path).asBitmap().fitCenter()
                .placeholder(R.mipmap.big_placeholder).error(R.mipmap.big_placeholder).diskCacheStrategy(DiskCacheStrategy.RESULT).skipMemoryCache(true).thumbnail(0.1f).into(scaleImageView);
        container.addView(view);

        scaleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isClick = !isClick;
                if (onViewClickListener != null) {
                    onViewClickListener.onViewSingleClick(isClick);
                }
            }
        });
        scaleImageView.setOnTouchImageViewListener(new PhotoViewAttacher.OnTouchImageViewListener() {
            @Override
            public void onDoubleClick() {
                isDoubleClick = !isDoubleClick;
                if (onViewClickListener != null) {
                    onViewClickListener.onViewDoubleClick(isDoubleClick);
                }
            }
        });

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position);
    }

    public interface onViewClickListener {
        void onViewSingleClick(boolean isClick);

        void onViewDoubleClick(boolean isClick);

    }

    public onViewClickListener onViewClickListener;


    public void setOnViewClickListener(AnswerDetailAdapter.onViewClickListener onViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }
}
