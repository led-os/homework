package yc.com.soundmark.study.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import yc.com.homework.R;
import yc.com.homework.base.user.UserInfoHelper;
import yc.com.soundmark.study.model.domain.WordInfo;

/**
 * Created by wanglin  on 2018/11/1 09:12.
 */
public class StudyVowelAdapter extends BaseQuickAdapter<WordInfo, BaseViewHolder> {
    public StudyVowelAdapter(List<WordInfo> data) {
        super(R.layout.study_vowel_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WordInfo item) {
        helper.setText(R.id.tv_vowel, item.getName());
        setItemState(helper, item);


    }

    private void setItemState(BaseViewHolder helper, WordInfo item) {

        if (UserInfoHelper.isVip()){
            helper.setVisible(R.id.iv_cover_layer, false);
            helper.setVisible(R.id.iv_lock, false);
        }else {
            helper.setVisible(R.id.iv_cover_layer,item.getIs_vip()==1);
            helper.setVisible(R.id.iv_lock, item.getIs_vip()==1);
        }


    }

}
