package cn.read.ui.presenter.impl;

import java.util.List;

import javax.inject.Inject;

import cn.read.bean.NewsChannelTable;
import cn.read.ui.interactor.PhotosInteractor;
import cn.read.ui.interactor.impl.PhotosInteractorImpl;
import cn.read.ui.presenter.PhotosPresenter;
import cn.read.ui.view.PhotosView;

/**
 * Created by lw on 2017-03-10.
 */

public class PhotosPresenterImpl extends BasePresenterImpl<PhotosView, List<String>>
        implements PhotosPresenter {

    private PhotosInteractor<List<String>> mPhotosInteractor;

    @Inject
    public PhotosPresenterImpl(PhotosInteractorImpl photosInteractor) {
        mPhotosInteractor = photosInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadNewsChannels();
    }

    @Override
    public void success(List<String> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    private void loadNewsChannels() {
        mSubscription = mPhotosInteractor.lodePhotosChannels(this);
    }
}
