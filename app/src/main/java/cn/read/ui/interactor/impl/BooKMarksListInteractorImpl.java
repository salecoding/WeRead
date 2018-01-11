package cn.read.ui.interactor.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import cn.read.R;
import cn.read.base.App;
import cn.read.bean.BeautyPhotoInfo;
import cn.read.bean.BookMarks;
import cn.read.bean.NewsSummary;
import cn.read.bean.VideosSummary;
import cn.read.common.Constants;
import cn.read.dao.BeautyPhotoInfoDao;
import cn.read.dao.NewsSummaryDao;
import cn.read.dao.VideosSummaryDao;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.BookMarksListInteractor;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;


/**
 * Created by lw on 2017/1/17.
 */

public class BooKMarksListInteractorImpl implements BookMarksListInteractor<BookMarks> {

    //    private boolean mIsNetError;
    private BookMarks bookMarks;
    private Map<String, List<NewsSummary>> newsSummaryListMap;
    private Map<String, List<BeautyPhotoInfo>> beautyPhotoInfoListMap;
    private Map<String, List<VideosSummary>> videosSummaryListMap;
    private int pageSize = 10;

    @Inject
    public BooKMarksListInteractorImpl() {
        bookMarks = new BookMarks();
        newsSummaryListMap = new HashMap<>();
        beautyPhotoInfoListMap = new HashMap<>();
        videosSummaryListMap = new HashMap<>();
    }

    @Override
    public Subscription loadBookMarks(final RequestCallBack<BookMarks> listener, final String type, String id, final int startPage) {
        return Observable.create(new Observable.OnSubscribe<BookMarks>() {
            @Override
            public void call(Subscriber<? super BookMarks> subscriber) {
                if (type.equals(Constants.BOOKMARKS_PHOTO)) {
                    List<BeautyPhotoInfo> beautyPhotoInfos = App.getBeautyPhotoInfoDao()
                            .queryBuilder()
                            .where(BeautyPhotoInfoDao.Properties.IsLove.eq(true))
                            .offset(startPage * pageSize).limit(pageSize).list();
                    beautyPhotoInfoListMap.put(type, beautyPhotoInfos);
                } else if (type.equals(Constants.BOOKMARKS_NEWS)) {
                    List<NewsSummary> newsSummaries = App.getNewsSummaryDao()
                            .queryBuilder()
                            .where(NewsSummaryDao.Properties.IsFavorites.eq(true))
                            .offset(startPage * pageSize).limit(pageSize).list();
                    newsSummaryListMap.put(type, newsSummaries);
                } else {
                    List<VideosSummary> videosSummaries = App.getVideosSummaryDao()
                            .queryBuilder()
                            .where(VideosSummaryDao.Properties.IsFavorites.eq(true))
                            .offset(startPage * pageSize).limit(pageSize).list();
                    videosSummaryListMap.put(type, videosSummaries);
                }
                bookMarks.setBeautyPhotoInfoListMap(beautyPhotoInfoListMap);
                bookMarks.setNewsSummaryListMap(newsSummaryListMap);
                bookMarks.setVideosSummaryListMap(videosSummaryListMap);
                subscriber.onNext(bookMarks);
                subscriber.onCompleted();
            }
        }).compose(TransformUtils.<BookMarks>defaultSchedulers())
                .subscribe(new Subscriber<BookMarks>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        listener.onError(App.getContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(BookMarks bookMarks) {
                        listener.success(bookMarks);
                    }
                });
    }
}
