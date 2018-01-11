package cn.read.ui.interactor.impl;

import com.socks.library.KLog;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import cn.read.R;
import cn.read.base.App;
import cn.read.bean.VideosChannelTable;
import cn.read.common.Constants;
import cn.read.db.VideosChannelTableManager;
import cn.read.listener.RequestCallBack;
import cn.read.ui.interactor.VideosChannelInteractor;
import cn.read.utils.TransformUtils;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by lw on 2017/1/20.
 */

public class VideosChannelInteractorImpl implements VideosChannelInteractor<Map<Integer, List<VideosChannelTable>>> {

    private ExecutorService mSingleThreadPool;

    @Inject
    public VideosChannelInteractorImpl() {

    }
    private Map<Integer, List<VideosChannelTable>> getVideosChannelData() {
        Map<Integer, List<VideosChannelTable>> map = new HashMap<>();
        List<VideosChannelTable> channelListMine = VideosChannelTableManager.loadVideosChannelsMine();
        List<VideosChannelTable> channelListMore = VideosChannelTableManager.loadVideosChannelsMore();
        map.put(Constants.VIDEOS_CHANNEL_MINE, channelListMine);
        map.put(Constants.VIDEOS_CHANNEL_MORE, channelListMore);
        return map;
    }

    @Override
    public Subscription lodeVideosChannels(final RequestCallBack<Map<Integer, List<VideosChannelTable>>> callback) {
        return Observable.create(new Observable.OnSubscribe<Map<Integer, List<VideosChannelTable>>>() {
            @Override
            public void call(Subscriber<? super Map<Integer, List<VideosChannelTable>>> subscriber) {
                Map<Integer, List<VideosChannelTable>> videosChannelListMap = getVideosChannelData();
                subscriber.onNext(videosChannelListMap);
                subscriber.onCompleted();
            }

        }).compose(TransformUtils.<Map<Integer, List<VideosChannelTable>>>defaultSchedulers())
                .subscribe(new Subscriber<Map<Integer, List<VideosChannelTable>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        callback.onError(App.getContext().getString(R.string.db_error));
                    }

                    @Override
                    public void onNext(Map<Integer, List<VideosChannelTable>> videosChannelListMap) {
                        callback.success(videosChannelListMap);
                    }
                });
    }

    @Override
    public void swapDb(final int fromPosition, final int toPosition) {
        createThreadPool();
        mSingleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                KLog.d(Thread.currentThread().getName());
                KLog.d("fromPosition: " + fromPosition + "； toPosition: " + toPosition);

                VideosChannelTable fromVideosChannel = VideosChannelTableManager.loadVideosChannel(fromPosition);
                VideosChannelTable toVideosChannel = VideosChannelTableManager.loadVideosChannel(toPosition);

                if (isAdjacent(fromPosition, toPosition)) {
                    swapAdjacentIndexAndUpdate(fromVideosChannel, toVideosChannel);
                } else if (fromPosition - toPosition > 0) {
                    List<VideosChannelTable> videosChannelTables = VideosChannelTableManager
                            .loadVideosChannelsWithin(toPosition, fromPosition - 1);

                    increaseOrReduceIndexAndUpdate(videosChannelTables, true);
                    changeFromChannelIndexAndUpdate(fromVideosChannel, toPosition);
                } else if (fromPosition - toPosition < 0) {
                    List<VideosChannelTable> videosChannelTables = VideosChannelTableManager
                            .loadVideosChannelsWithin(fromPosition + 1, toPosition);

                    increaseOrReduceIndexAndUpdate(videosChannelTables, false);
                    changeFromChannelIndexAndUpdate(fromVideosChannel, toPosition);
                }
            }

            private boolean isAdjacent(int fromChannelIndex, int toChannelIndex) {
                return Math.abs(fromChannelIndex - toChannelIndex) == 1;
            }

            private void swapAdjacentIndexAndUpdate(VideosChannelTable fromVideosChannel,
                                                    VideosChannelTable toVideosChannel) {
                fromVideosChannel.setVideosChannelIndex(toPosition);
                toVideosChannel.setVideosChannelIndex(fromPosition);

                VideosChannelTableManager.update(fromVideosChannel);
                VideosChannelTableManager.update(toVideosChannel);
            }
        });
    }

    private void increaseOrReduceIndexAndUpdate(List<VideosChannelTable> videosChannels, boolean isIncrease) {
        for (VideosChannelTable videosChannelTable : videosChannels) {
            increaseOrReduceIndex(isIncrease, videosChannelTable);
            VideosChannelTableManager.update(videosChannelTable);
        }
    }

    private void increaseOrReduceIndex(boolean isIncrease, VideosChannelTable videosChannelTable) {
        int targetIndex;
        if (isIncrease) {
            targetIndex = videosChannelTable.getVideosChannelIndex() + 1;
        } else {
            targetIndex = videosChannelTable.getVideosChannelIndex() - 1;
        }
        videosChannelTable.setVideosChannelIndex(targetIndex);
    }

    private void changeFromChannelIndexAndUpdate(VideosChannelTable fromVideosChannel, int toPosition) {
        fromVideosChannel.setVideosChannelIndex(toPosition);
        VideosChannelTableManager.update(fromVideosChannel);
    }

    @Override
    public void updateDb(final VideosChannelTable videosChannelTable, final boolean isChannelMine) {
        createThreadPool();
        mSingleThreadPool.execute(new Runnable() {
            @Override
            public void run() {
                KLog.d(Thread.currentThread().getName());

                int channelIndex = videosChannelTable.getVideosChannelIndex();
                if (isChannelMine) {
                    List<VideosChannelTable> videosChannelTables = VideosChannelTableManager.loadVideosChannelsIndexGt(channelIndex);
                    increaseOrReduceIndexAndUpdate(videosChannelTables, false);

                    int targetIndex = VideosChannelTableManager.getAllSize();
                    ChangeIsSelectAndIndex(targetIndex, false);
                } else {
                    List<VideosChannelTable> videosChannelTables = VideosChannelTableManager.loadVideosChannelsIndexLtAndIsUnselect(channelIndex);
                    increaseOrReduceIndexAndUpdate(videosChannelTables, true);

                    int targetIndex = VideosChannelTableManager.getVideosChannelSelectSize();
                    ChangeIsSelectAndIndex(targetIndex, true);
                }

            }

            private void ChangeIsSelectAndIndex(int targetIndex, boolean isSelect) {
                videosChannelTable.setVideosChannelSelect(isSelect);
                changeFromChannelIndexAndUpdate(videosChannelTable, targetIndex);
            }
        });
    }

    private void createThreadPool() {
        if (mSingleThreadPool == null) {
            mSingleThreadPool = Executors.newSingleThreadExecutor();
        }
    }
}
