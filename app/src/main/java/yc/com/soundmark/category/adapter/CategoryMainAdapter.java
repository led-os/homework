package yc.com.soundmark.category.adapter;

import android.text.Html;
import android.text.Spanned;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.homework.R;
import yc.com.soundmark.category.model.domain.WeiKeCategory;

/**
 * Created by wanglin  on 2018/10/25 11:31.
 */
public class CategoryMainAdapter extends BaseQuickAdapter<WeiKeCategory, BaseViewHolder> {
    public CategoryMainAdapter(List<WeiKeCategory> data) {
        super(R.layout.weikecategory_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WeiKeCategory item) {
        Glide.with(mContext).load(item.getImg()).asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.RESULT)
                .skipMemoryCache(true).into((ImageView) helper.getView(R.id.iv_item_cover));
        Spanned spanned = Html.fromHtml("总共<font color='#ff0000'>" + item.getUnitNum() + "</font>单元");
        helper.setText(R.id.tv_item_title, item.getTitle())
                .setText(R.id.tv_item_total, spanned);
    }


}
