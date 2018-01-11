package cn.read.ui.interactor.impl;

import java.util.List;

import javax.inject.Inject;

import cn.read.R;
import cn.read.base.App;
import cn.read.bean.VideosChannelTable;
import cn.read.db.VideosChannelTableManager;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.VideosInteractor;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lw on 2017/1/15.
 */

public class VideosInteractorImpl implements VideosInteractor<List<VideosChannelTable>> {

    @Inject
    public VideosInteractorImpl() {

    }

    @Override
    public Subscription lodeVideosChannels(final RequestCallBack<List<VideosChannelTable>> callBack) {
        return Observable.create(new Observable.OnSubscribe<List<VideosChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<VideosChannelTable>> subscriber) {
                VideosChannelTableManager.initDB(false);
                subscriber.onNext(VideosChannelTableManager.loadVideosChannelsMine());
                subscriber.onCompleted();
            }
        })
                .compose(TransformUtils.<List<VideosChannelTable>>defaultSchedulers())
                .subscribe(new Subscriber<List<VideosChannelTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(App.getContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<VideosChannelTable> videosChannelTables) {
                        callBack.success(videosChannelTables);
                    }
                });
    }
}
