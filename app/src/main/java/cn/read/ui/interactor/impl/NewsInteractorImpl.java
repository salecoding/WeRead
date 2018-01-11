package cn.read.ui.interactor.impl;
import java.util.List;

import javax.inject.Inject;

import cn.read.R;
import cn.read.base.App;
import cn.read.bean.NewsChannelTable;
import cn.read.db.NewsChannelTableManager;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.NewsInteractor;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lw on 2017/1/15.
 */

public class NewsInteractorImpl implements NewsInteractor<List<NewsChannelTable>> {

    @Inject
    public NewsInteractorImpl() {

    }
    @Override
    public Subscription lodeNewsChannels(final RequestCallBack<List<NewsChannelTable>> callBack) {
        return Observable.create(new Observable.OnSubscribe<List<NewsChannelTable>>() {
            @Override
            public void call(Subscriber<? super List<NewsChannelTable>> subscriber) {
                NewsChannelTableManager.initDB(false);
                subscriber.onNext(NewsChannelTableManager.loadNewsChannelsMine());
                subscriber.onCompleted();
            }
        })
                .compose(TransformUtils.<List<NewsChannelTable>>defaultSchedulers())
                .subscribe(new Subscriber<List<NewsChannelTable>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(App.getContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<NewsChannelTable> newsChannelTables) {
                        callBack.success(newsChannelTables);
                    }
                });
    }
}
