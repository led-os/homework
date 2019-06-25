package yc.com.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.kk.utils.LogUtil;

import java.util.List;

import yc.com.answer.index.model.bean.BookAnswerInfo;
import yc.com.answer.utils.GlideHelper;
import yc.com.answer.utils.SubjectHelper;
import yc.com.homework.R;

/**
 * Created by wanglin  on 2018/3/7 19:29.
 */

public class IndexBookAdapter extends BaseQuickAdapter<BookAnswerInfo, BaseViewHolder> {
    public IndexBookAdapter(@Nullable List<BookAnswerInfo> data) {
        super(R.layout.fragment_book_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookAnswerInfo item) {
        helper.setText(R.id.tv_book_name, "        " + item.getName());


        Glide.with(mContext).load(item.getCover_img()).asBitmap().centerCrop().diskCacheStrategy(DiskCacheStrategy.RESULT)
                .thumbnail(0.1f).into((ImageView) helper.getView(R.id.iv_book));
//
        SubjectHelper.setSubject(helper, item, R.id.iv_grade_tag);
    }
}
