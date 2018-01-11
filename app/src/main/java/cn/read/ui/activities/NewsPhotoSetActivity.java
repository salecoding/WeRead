package cn.read.ui.activities;

import android.app.Activity;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.dl7.drag.DragSlopLayout;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import cn.read.BaseActivity;
import cn.read.R;
import cn.read.bean.PhotoSetInfo;
import cn.read.common.Constants;
import cn.read.common.LoadNewsType;
import cn.read.ui.adapter.PhotoSetPagerAdapter;
import cn.read.ui.presenter.impl.NewsPhotoSetPresenterImpl;
import cn.read.ui.view.NewsPhotoSetView;
import cn.read.widget.PhotoViewPager;

/**
 * Created by lw on 2017/3/12.
 */

public class NewsPhotoSetActivity extends BaseActivity implements NewsPhotoSetView {
    @Bind(R.id.vp_photo)
    PhotoViewPager mVpPhoto;
    @Bind(R.id.tv_title)
    TextView mTvTitle;
    @Bind(R.id.tv_index)
    TextView mTvIndex;
    @Bind(R.id.tv_count)
    TextView mTvCount;
    @Bind(R.id.tv_content)
    TextView mTvContent;
    @Bind(R.id.drag_layout)
    DragSlopLayout mDragLayout;
    @Inject
    NewsPhotoSetPresenterImpl mNewsPhotoSetPresenter;
    @Inject
    Activity mActivity;

    private String mPhotoSetId;
    private PhotoSetPagerAdapter mPhotoSetPagerAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_news_photo_set;
    }

    @Override
    public void initViews() {
        //去除标题
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initValue();
        mNewsPhotoSetPresenter.setPhotoSetId(mPhotoSetId);
        mPresenter = mNewsPhotoSetPresenter;
        mPresenter.attachView(this);
        mPresenter.onCreate();
    }

    private void initValue() {
        mPhotoSetId = getIntent().getStringExtra(Constants.NEWS_PHOTO_SET_KEY);
    }

    @Override
    public void initInjector() {
        mActivityComponent.inject(this);
    }

    @Override
    public void setNewsPhotoSetInfos(PhotoSetInfo photoSetInfo, @LoadNewsType.checker int loadType) {
        switch (loadType) {
            case LoadNewsType.TYPE_REFRESH_SUCCESS: {
                List<String> images = new ArrayList<>();
                final List<PhotoSetInfo.PhotosEntity> photoSetInfos = photoSetInfo.getPhotos();
                for (PhotoSetInfo.PhotosEntity photosEntity : photoSetInfos) {
                    images.add(photosEntity.getImgurl());
                }
                mPhotoSetPagerAdapter = new PhotoSetPagerAdapter(this, images);
                mVpPhoto.setAdapter(mPhotoSetPagerAdapter);
                mVpPhoto.setOffscreenPageLimit(images.size());
                mDragLayout.interactWithViewPager(true);

                mTvCount.setText(String.valueOf(photoSetInfos.size()));
                mTvTitle.setText(photoSetInfo.getSetname());
                mTvIndex.setText(1 + "/");
                mTvContent.setText(photoSetInfos.get(0).getNote());

                mVpPhoto.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageSelected(int position) {
                        mTvContent.setText(photoSetInfos.get(position).getNote());
                        mTvIndex.setText((position + 1) + "/");
                    }
                });
            }
            break;
            case LoadNewsType.TYPE_REFRESH_ERROR:

                break;
        }
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showMsg(String message) {

    }
}
