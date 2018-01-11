package cn.read.ui.presenter.impl;

import javax.inject.Inject;

import cn.read.bean.BookMarks;
import cn.read.common.LoadNewsType;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.impl.BooKMarksListInteractorImpl;
import cn.read.ui.interactor.BookMarksListInteractor;
import cn.read.ui.presenter.BookMarksListPresenter;
import cn.read.ui.view.BookMarksListView;

/**
 * Created by lw on 2017/1/17.
 */

public class BookMarksListPresenterImpl extends BasePresenterImpl<BookMarksListView, BookMarks>
        implements BookMarksListPresenter, RequestCallBack<BookMarks> {

    private BookMarksListInteractor<BookMarks> mBookMarksListInteractor;
    private String mBookMarksType;
    private String mBookMarksId;
    private int mStartPage;

    private boolean misFirstLoad;
    private boolean mIsRefresh = true;

    @Inject
    public BookMarksListPresenterImpl(BooKMarksListInteractorImpl booKMarksListInteractor) {
        mBookMarksListInteractor = booKMarksListInteractor;
    }

    @Override
    public void onCreate() {
        mStartPage = 0;
        if (mView != null) {
            loadBookMarksData();
        }
    }

    @Override
    public void beforeRequest() {
        if (!misFirstLoad) {
            mView.showProgress();
        }
    }

    @Override
    public void onError(String errorMsg) {
        super.onError(errorMsg);
        if (mView != null) {
            int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_ERROR : LoadNewsType.TYPE_LOAD_MORE_ERROR;
            mView.setBookMarksesList(null, loadType);
        }
    }

    @Override
    public void success(BookMarks bookMarks) {
        misFirstLoad = true;
        if (bookMarks != null) {
            mStartPage++;
        }
        int loadType = mIsRefresh ? LoadNewsType.TYPE_REFRESH_SUCCESS : LoadNewsType.TYPE_LOAD_MORE_SUCCESS;
        if (mView != null) {
            mView.setBookMarksesList(bookMarks, loadType);
            mView.hideProgress();
        }

    }

    @Override
    public void setBookMarksTypeAndId(String bookMarksType, String bookMarksId) {
        this.mBookMarksType = bookMarksType;
        this.mBookMarksId = bookMarksId;
    }

    @Override
    public void refreshData() {
        mStartPage = 0;
        mIsRefresh = true;
        loadBookMarksData();
    }

    @Override
    public void loadMore() {
        mIsRefresh = false;
        loadBookMarksData();
    }

    private void loadBookMarksData() {
        mSubscription = mBookMarksListInteractor.loadBookMarks(this, mBookMarksType, mBookMarksId, mStartPage);
    }
}
