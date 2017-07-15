package com.yuyin.myclass.teamhead;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yuyin.myclass.teamhead.utils.DensityUtils;
import com.yuyin.myclass.teamhead.view.SynthesizedImageView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    Context mContext;
    SynthesizedImageView mMergePic1;
    SynthesizedImageView mMergePic2;
    SynthesizedImageView mMergePic3;
    SynthesizedImageView mMergePic4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        initPicViews();
        initData();
    }

    void initPicViews() {
        mMergePic1 = (SynthesizedImageView) findViewById(R.id.iv_merge_pic1);
        mMergePic2 = (SynthesizedImageView) findViewById(R.id.iv_merge_pic2);
        mMergePic3 = (SynthesizedImageView) findViewById(R.id.iv_merge_pic3);
        mMergePic4 = (SynthesizedImageView) findViewById(R.id.iv_merge_pic4);
    }

    void initData() {
        int imageSize = DensityUtils.dp2px(mContext, 120);
        List<String> imageUrls = new ArrayList<>();
        imageUrls.add(null);
        imageUrls.add("http://dynamic-image.yesky.com/740x-/uploadImages/2014/289/01/IGS09651F94M.jpg");
        mMergePic1.displayImage(imageUrls)
                .synthesizedWidthHeight(imageSize, imageSize)
                .defaultImage(R.mipmap.ic_launcher_round)
                .load();
        imageUrls = new ArrayList<>();
        imageUrls.add("http://pic.qiantucdn.com/58pic/22/06/55/57b2d98e109c6_1024.jpg");
        imageUrls.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/15/14/14/18e58PICMwt_1024.jpg");
        imageUrls.add("http://dynamic-image.yesky.com/740x-/uploadImages/2014/289/01/IGS09651F94M.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/13/61/00/61a58PICtPr_1024.jpg");
        mMergePic2.displayImage(imageUrls)
                .synthesizedWidthHeight(imageSize, imageSize)
                .defaultImage(R.mipmap.ic_launcher_round)
                .load();
        imageUrls = new ArrayList<>();
        imageUrls.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/15/14/14/18e58PICMwt_1024.jpg");
        imageUrls.add("http://dynamic-image.yesky.com/740x-/uploadImages/2014/289/01/IGS09651F94M.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/13/61/00/61a58PICtPr_1024.jpg");
        imageUrls.add("http://www.bz55.com/uploads/allimg/150701/140-150F1142638.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/15/36/00/73b58PICgvY_1024.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/15/35/96/97j58PICUhD_1024.jpg");
        mMergePic3.displayImage(imageUrls)
                .synthesizedWidthHeight(imageSize, imageSize)
                .defaultImage(R.mipmap.ic_launcher_round)
                .load();
        imageUrls = new ArrayList<>();
        imageUrls.add("http://pic.qiantucdn.com/58pic/22/06/55/57b2d98e109c6_1024.jpg");
        imageUrls.add("http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/15/14/14/18e58PICMwt_1024.jpg");
        imageUrls.add("http://dynamic-image.yesky.com/740x-/uploadImages/2014/289/01/IGS09651F94M.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/13/61/00/61a58PICtPr_1024.jpg");
        imageUrls.add("http://www.bz55.com/uploads/allimg/150701/140-150F1142638.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/15/36/00/73b58PICgvY_1024.jpg");
        imageUrls.add("http://pic.58pic.com/58pic/15/35/96/97j58PICUhD_1024.jpg");
        imageUrls.add("http://pic.yesky.com/uploadImages/2015/131/58/62KPG7ZYL453.jpg");
        mMergePic4.displayImage(imageUrls)
                .synthesizedWidthHeight(imageSize, imageSize)
                .defaultImage(R.mipmap.ic_launcher_round)
                .load();
    }

}
