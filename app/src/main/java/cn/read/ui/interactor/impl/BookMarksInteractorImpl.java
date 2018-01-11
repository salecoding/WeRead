package cn.read.ui.interactor.impl;

import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import cn.read.R;
import cn.read.base.App;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.BookMarksInteractor;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lw on 2017/1/15.
 */

public class BookMarksInteractorImpl implements BookMarksInteractor<List<String>> {

    @Inject
    public BookMarksInteractorImpl() {

    }

    @Override
    public Subscription lodeBookMarksChannels(final RequestCallBack<List<String>> callBack) {
        return Observable.create(new Observable.OnSubscribe<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                List<String> channelNames = Arrays.asList(App.getContext().getResources()
                        .getStringArray(R.array.bookmarks_channel_name));
                subscriber.onNext(channelNames);
                subscriber.onCompleted();
            }
        })
                .compose(TransformUtils.<List<String>>defaultSchedulers())
                .subscribe(new Subscriber<List<String>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callBack.onError(App.getContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(List<String> channelNames) {
                        callBack.success(channelNames);
                    }
                });
    }
}
