package com.yuyin.myclass.teamhead.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.yuyin.myclass.teamhead.support.TeamHeadSynthesizer;

import java.util.List;

/**
 * 合成图片的view
 * Created by yangjianxin on 17/7/14.
 */

public class SynthesizedImageView extends AppCompatImageView {
    /**
     * 群聊头像合成器
     */
    TeamHeadSynthesizer teamHeadSynthesizer;

    public SynthesizedImageView(Context context) {
        super(context);
        init(context);
    }

    public SynthesizedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SynthesizedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        teamHeadSynthesizer = new TeamHeadSynthesizer(context, this);
    }

    public SynthesizedImageView displayImage(List<String> imageUrls) {
        teamHeadSynthesizer.getMultiImageData().setImageUrls(imageUrls);
        return this;
    }

    public SynthesizedImageView defaultImage(int defaultImage) {
        teamHeadSynthesizer.setDefaultImage(defaultImage);
        return this;
    }

    public SynthesizedImageView synthesizedWidthHeight(int maxWidth, int maxHeight) {
        teamHeadSynthesizer.setMaxWidthHeight(maxWidth, maxHeight);
        return this;
    }

    public void load() {
        teamHeadSynthesizer.load();
    }

}
