package com.yuyin.myclass.teamhead.support;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * 合成器
 * Created by yangjianxin on 17/7/14.
 */

public interface Synthesizer {

    /**
     * 图片合成
     */
    Bitmap synthesizeImageList();

    /**
     * 异步下载图片列表
     */
    boolean asyncLoadImageList();

    /**
     * 画合成的图片
     *
     * @param canvas
     */
    void drawDrawable(Canvas canvas);

}
