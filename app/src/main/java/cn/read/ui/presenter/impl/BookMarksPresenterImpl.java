package cn.read.ui.presenter.impl;

import java.util.List;

import javax.inject.Inject;

import cn.read.ui.interactor.BookMarksInteractor;
import cn.read.ui.interactor.PhotosInteractor;
import cn.read.ui.interactor.impl.BookMarksInteractorImpl;
import cn.read.ui.interactor.impl.PhotosInteractorImpl;
import cn.read.ui.presenter.BookMarksPresenter;
import cn.read.ui.presenter.PhotosPresenter;
import cn.read.ui.view.BookMarksView;
import cn.read.ui.view.PhotosView;

/**
 * Created by lw on 2017-03-10.
 */

public class BookMarksPresenterImpl extends BasePresenterImpl<BookMarksView, List<String>>
        implements BookMarksPresenter {

    private BookMarksInteractor<List<String>> mBookMarksInteractor;

    @Inject
    public BookMarksPresenterImpl(BookMarksInteractorImpl bookMarksInteractor) {
        mBookMarksInteractor = bookMarksInteractor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        loadBookMarksChannels();
    }

    @Override
    public void success(List<String> data) {
        super.success(data);
        mView.initViewPager(data);
    }

    private void loadBookMarksChannels() {
        mSubscription = mBookMarksInteractor.lodeBookMarksChannels(this);
    }
}
