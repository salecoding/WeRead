package cn.read.ui.interactor;
import cn.read.bean.NewsChannelTable;
import cn.read.bean.VideosChannelTable;
import cn.read.listener.RequestCallBack;
import rx.Subscription;

/**
 * Created by lw on 2017/1/20.
 */

public interface VideosChannelInteractor<T> {
    Subscription lodeVideosChannels(RequestCallBack<T> callback);

    void swapDb(int fromPosition, int toPosition);

    void updateDb(VideosChannelTable videosChannelTable, boolean isChannelMine);
}
