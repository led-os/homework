package yc.com.answer.index.ui.adapter;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.answer.index.model.bean.VersionDetailInfo;
import yc.com.homework.R;

/**
 * Created by wanglin  on 2018/4/20 18:09.
 */

public class BookInfoItemAdapter extends BaseQuickAdapter<VersionDetailInfo, BaseViewHolder> {

    public BookInfoItemAdapter(@Nullable List<VersionDetailInfo> data) {
        super(R.layout.book_info_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, VersionDetailInfo item) {
        helper.setText(R.id.tv_item_detail, item.getName());

    }

}
