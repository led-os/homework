package yc.com.answer.index.ui.adapter;

import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.SparseArray;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.vondear.rxtools.RxSPTool;

import java.util.List;

import yc.com.answer.index.model.bean.VersionDetailInfo;
import yc.com.homework.R;

/**
 * Created by wanglin  on 2018/3/8 16:21.
 */

public class FilterItemDetailAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {

    private SparseArray<TextView> sparseArray;
    private String mFlag;

    public FilterItemDetailAdapter(@Nullable List<T> data, String flag) {
        super(R.layout.popwindow_filter_item_detail, data);
        this.mFlag = flag;
        sparseArray = new SparseArray<>();

    }

    @Override
    protected void convert(BaseViewHolder helper, T item) {
        String name = "";
        if (item instanceof VersionDetailInfo) {
            VersionDetailInfo info = (VersionDetailInfo) item;
            name = info.getName();
        } else if (item instanceof String) {
            name = (String) item;
        }
        helper.setText(R.id.tv_item_detail, name);
        sparseArray.put(helper.getLayoutPosition(), (TextView) helper.getView(R.id.tv_item_detail));
        setSelectState(helper);

    }


    private void setSelectState(BaseViewHolder helper) {
        String saveData = RxSPTool.getString(mContext, mFlag);
        int currentPos = 0;
        for (int i = 0; i < mData.size(); i++) {
            String name = "";
            if (mData.get(i) instanceof VersionDetailInfo) {
                name = ((VersionDetailInfo) mData.get(i)).getName();
            } else if (mData.get(i) instanceof String) {
                name = ((String) mData.get(i));
            }

            if (name.equals(saveData)) {
                currentPos = i;
                break;
            }
        }
        if (helper.getLayoutPosition() == currentPos) {
            helper.getView(R.id.tv_item_detail).setSelected(true);
        }
    }

    public TextView getView(int position) {

        for (int i = 0; i < sparseArray.size(); i++) {
            TextView textView = sparseArray.get(i);
            textView.setTextColor(ContextCompat.getColor(mContext, R.color.black));
            textView.setSelected(false);
        }
        return sparseArray.get(position);
    }

    public void onClick(int position) {

        TextView textView = getView(position);
        textView.setTextColor(ContextCompat.getColor(mContext, R.color.red_f0333a));
        textView.setSelected(true);

    }
}
