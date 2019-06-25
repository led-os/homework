package yc.com.soundmark.study.activity;

import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import yc.com.base.BaseActivity;
import yc.com.homework.R;
import yc.com.homework.examine.widget.ZoomImageView;

/**
 * Created by wanglin  on 2018/11/5 16:03.
 */
public class PreviewActivity extends BaseActivity {

    private ZoomImageView zoomImageView;

    @Override
    public boolean isStatusBarMateria() {
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_preview_picture;
    }

    @Override
    public void init() {
        zoomImageView = findViewById(R.id.zoomImageView);
        String img = getIntent().getStringExtra("img");

        Glide.with(this).load(img).asBitmap().diskCacheStrategy(DiskCacheStrategy.RESULT).into(zoomImageView);
        zoomImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
