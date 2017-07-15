package com.yuyin.myclass.teamhead.support;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.yuyin.myclass.teamhead.configs.Config;
import com.yuyin.myclass.teamhead.data.MultiImageData;
import com.yuyin.myclass.teamhead.utils.BitmapUtils;
import com.yuyin.myclass.teamhead.utils.MD5Utils;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * 群聊头像合成器
 * Created by yangjianxin on 17/7/14.
 */

public class TeamHeadSynthesizer implements Synthesizer {

    String currentTargeID;//当前多图合成的唯一ID，用来做缓存处理，以及判断合成图片是否需要变更，如果多个url有变动，currentTargetID也会发生变动，需要重新生成
    /**
     * 多图片数据
     */
    MultiImageData multiImageData;
    Context mContext;
    int targetImageSize;//目标图片宽高
    int maxWidth, maxHeight;//最大宽度，最大高度
    private int mRowCount; //行数
    private int mColumnCount;  //列数
    ImageView imageView;
    int bgColor = Color.GRAY;

    private int mGap; //宫格间距

    public TeamHeadSynthesizer(Context mContext, ImageView imageView) {
        this.mContext = mContext;
        this.imageView = imageView;
        init(mContext);
    }

    private void init(Context context) {
        multiImageData = new MultiImageData();
        mGap = dp2px(2, context);
        //图片合成的背景图片颜色
        bgColor = Color.parseColor("#FFDDDDDD");
    }

    public int getMaxWidth() {
        return maxWidth;
    }

    public void setMaxWidthHeight(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public MultiImageData getMultiImageData() {
        return multiImageData;
    }

    public void setDefaultImage(int defaultImageResId) {
        multiImageData.setDefaultImageResId(defaultImageResId);
    }

    public int getDefaultImage() {
        return multiImageData.getDefaultImageResId();
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dp2px(float dpValue, Context mContext) {
        float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 设置宫格参数
     *
     * @param imagesSize 图片数量
     * @return 宫格参数 gridParam[0] 宫格行数 gridParam[1] 宫格列数
     */
    protected int[] calculateGridParam(int imagesSize) {
        int[] gridParam = new int[2];
        if (imagesSize < 3) {
            gridParam[0] = 1;
            gridParam[1] = imagesSize;
        } else if (imagesSize <= 4) {
            gridParam[0] = 2;
            gridParam[1] = 2;
        } else {
            gridParam[0] = imagesSize / 3 + (imagesSize % 3 == 0 ? 0 : 1);
            gridParam[1] = 3;
        }
        return gridParam;
    }

    @Override
    public Bitmap synthesizeImageList() {
        Bitmap mergeBitmap = Bitmap.createBitmap(maxWidth, maxHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(mergeBitmap);
        drawDrawable(canvas);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return mergeBitmap;
    }

    @Override
    public boolean asyncLoadImageList() {
        boolean loadSuccess = true;
        List<String> imageUrls = multiImageData.getImageUrls();
        for (int i = 0; i < imageUrls.size(); i++) {
            String imageUrl = imageUrls.get(i);
            if (TextUtils.isEmpty(imageUrl)) {
                //图片链接不存在
                continue;
            } else {
                //下载图片
                try {
                    Bitmap bitmap = asyncLoadImage(imageUrl, targetImageSize);
                    multiImageData.putBitmap(bitmap, i);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    multiImageData.putBitmap(null, i);
                    loadSuccess = false;
                } catch (ExecutionException e) {
                    e.printStackTrace();
                    multiImageData.putBitmap(null, i);
                    loadSuccess = false;
                }
            }
        }
        //下载完毕
        return loadSuccess;
    }

    @Override
    public void drawDrawable(Canvas canvas) {
        //画背景
        canvas.drawColor(bgColor);
        //画组合图片
        int size = multiImageData.size();
        int t_center = (maxHeight + mGap) / 2;//中间位置以下的顶点（有宫格间距）
        int b_center = (maxHeight - mGap) / 2;//中间位置以上的底部（有宫格间距）
        int l_center = (maxWidth + mGap) / 2;//中间位置以右的左部（有宫格间距）
        int r_center = (maxWidth - mGap) / 2;//中间位置以左的右部（有宫格间距）
        int center = (maxHeight - targetImageSize) / 2;//中间位置以上顶部（无宫格间距）
        for (int i = 0; i < size; i++) {
            int rowNum = i / mColumnCount;//当前行数
            int columnNum = i % mColumnCount;//当前列数

            int left = ((int) (targetImageSize * (mColumnCount == 1 ? columnNum + 0.5 : columnNum) + mGap * (columnNum + 1)));
            int top = ((int) (targetImageSize * (mColumnCount == 1 ? rowNum + 0.5 : rowNum) + mGap * (rowNum + 1)));
            int right = left + targetImageSize;
            int bottom = top + targetImageSize;

            Bitmap bitmap = multiImageData.getBitmap(i);
            if (size == 1) {
                drawBitmapAtPosition(canvas, left, top, right, bottom, bitmap);
            } else if (size == 2) {
                drawBitmapAtPosition(canvas, left, center, right, center + targetImageSize, bitmap);
            } else if (size == 3) {
                if (i == 0) {
                    drawBitmapAtPosition(canvas, center, top, center + targetImageSize, bottom, bitmap);
                } else {
                    drawBitmapAtPosition(canvas, mGap * i + targetImageSize * (i - 1), t_center, mGap * i + targetImageSize * i, t_center + targetImageSize, bitmap);
                }
            } else if (size == 4) {
                drawBitmapAtPosition(canvas, left, top, right, bottom, bitmap);
            } else if (size == 5) {
                if (i == 0) {
                    drawBitmapAtPosition(canvas, r_center - targetImageSize, r_center - targetImageSize, r_center, r_center, bitmap);
                } else if (i == 1) {
                    drawBitmapAtPosition(canvas, l_center, r_center - targetImageSize, l_center + targetImageSize, r_center, bitmap);
                } else {
                    drawBitmapAtPosition(canvas, mGap * (i - 1) + targetImageSize * (i - 2), t_center, mGap * (i - 1) + targetImageSize * (i - 1), t_center +
                            targetImageSize, bitmap);
                }
            } else if (size == 6) {
                if (i < 3) {
                    drawBitmapAtPosition(canvas, mGap * (i + 1) + targetImageSize * i, b_center - targetImageSize, mGap * (i + 1) + targetImageSize * (i + 1), b_center, bitmap);
                } else {
                    drawBitmapAtPosition(canvas, mGap * (i - 2) + targetImageSize * (i - 3), t_center, mGap * (i - 2) + targetImageSize * (i - 2), t_center +
                            targetImageSize, bitmap);
                }
            } else if (size == 7) {
                if (i == 0) {
                    drawBitmapAtPosition(canvas, center, mGap, center + targetImageSize, mGap + targetImageSize, bitmap);
                } else if (i > 0 && i < 4) {
                    drawBitmapAtPosition(canvas, mGap * i + targetImageSize * (i - 1), center, mGap * i + targetImageSize * i, center + targetImageSize, bitmap);
                } else {
                    drawBitmapAtPosition(canvas, mGap * (i - 3) + targetImageSize * (i - 4), t_center + targetImageSize / 2, mGap * (i - 3) + targetImageSize * (i - 3), t_center + targetImageSize / 2 + targetImageSize, bitmap);
                }
            } else if (size == 8) {
                if (i == 0) {
                    drawBitmapAtPosition(canvas, r_center - targetImageSize, mGap, r_center, mGap + targetImageSize, bitmap);
                } else if (i == 1) {
                    drawBitmapAtPosition(canvas, l_center, mGap, l_center + targetImageSize, mGap + targetImageSize, bitmap);
                } else if (i > 1 && i < 5) {
                    drawBitmapAtPosition(canvas, mGap * (i - 1) + targetImageSize * (i - 2), center, mGap * (i - 1) + targetImageSize * (i - 1), center + targetImageSize, bitmap);
                } else {
                    drawBitmapAtPosition(canvas, mGap * (i - 4) + targetImageSize * (i - 5), t_center + targetImageSize / 2, mGap * (i - 4) + targetImageSize * (i - 4), t_center + targetImageSize / 2 + targetImageSize, bitmap);
                }
            } else if (size == 9) {
                drawBitmapAtPosition(canvas, left, top, right, bottom, bitmap);
            }
        }
    }

    /**
     * 根据坐标画图
     *
     * @param canvas
     * @param left
     * @param top
     * @param right
     * @param bottom
     * @param bitmap
     */
    public void drawBitmapAtPosition(Canvas canvas, int left, int top, int right, int bottom, Bitmap bitmap) {
        if (null == bitmap) {
            //图片为空用默认图片
            if (multiImageData.getDefaultImageResId() > 0) {
                //设置过默认id
                bitmap = BitmapFactory.decodeResource(mContext.getResources(), multiImageData.getDefaultImageResId());
            }
        }
        if (null != bitmap) {
            Rect rect = new Rect(left, top, right, bottom);
            canvas.drawBitmap(bitmap, null, rect, null);
        }
    }

    /**
     * 同步加载图片
     *
     * @param imageUrl
     * @param targetImageSize
     * @return
     * @throws InterruptedException
     * @throws java.util.concurrent.ExecutionException
     */
    private Bitmap asyncLoadImage(String imageUrl, int targetImageSize) throws InterruptedException, java.util.concurrent.ExecutionException {
        return Glide.with(mContext)
                .load(imageUrl)
                .asBitmap()
                .centerCrop()
                .into(targetImageSize, targetImageSize)
                .get();
    }

    public void load() {
        //初始化图片信息
        int[] gridParam = calculateGridParam(multiImageData.size());
        mRowCount = gridParam[0];
        mColumnCount = gridParam[1];
        targetImageSize = (maxWidth - (mColumnCount + 1) * mGap) / (mColumnCount == 1 ? 2 : mColumnCount);//图片尺寸
        currentTargeID = buildTargetSynthesizedId();
        new Thread() {
            @Override
            public void run() {
                super.run();
                final String targetID = currentTargeID;
                //根据id获取存储的文件路径
                String absolutePath = mContext.getFilesDir().getAbsolutePath();
                final File file = new File(absolutePath + File.separator + Config.dir_synthesized_image + File.separator + currentTargeID);
                boolean cacheBitmapExists = false;
                if (file.exists() && file.isFile()) {
                    //文件存在，加载到内存
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(file.getPath(), options);
                    if (options.outWidth > 0 && options.outHeight > 0) {
                        //当前文件是图片
                        cacheBitmapExists = true;
                    }
                }
                if (!cacheBitmapExists) {
                    //缓存文件不存在，需要加载读取
                    boolean loadSuccess = asyncLoadImageList();
                    final Bitmap bitmap = synthesizeImageList();
                    //保存合成的图片文件
                    if (loadSuccess) {
                        //所有图片加载成功，则保存合成图片
                        BitmapUtils.storeBitmap(file, bitmap);
                    }
                    //执行回调
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCall(bitmap, targetID);
                        }
                    });
                } else {
                    imageView.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onCall(file, targetID);
                        }
                    });
                }
            }
        }.start();
    }

    Callback callback = new Callback() {
        @Override
        public void onCall(Object obj, String targetID) {
            //判断回调结果的任务id是否为同一批次的任务
            if (!TextUtils.equals(currentTargeID, targetID)) return;
            if (obj instanceof File) {
                Glide.with(mContext)
                        .load(((File) obj))
                        .into(imageView);
            } else if (obj instanceof Bitmap) {
                imageView.setImageBitmap(((Bitmap) obj));
            }
        }
    };

    /**
     * 生成合成图片的id，保证唯一性
     */

    public String buildTargetSynthesizedId() {
        int size = multiImageData.size();
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < size; i++) {
            String imageUrl = multiImageData.getImageUrls().get(i);
            buffer.append(i + imageUrl);
        }
        return MD5Utils.getMD5String(buffer.toString());
    }

    interface Callback {
        void onCall(Object object, String targetID);
    }

}