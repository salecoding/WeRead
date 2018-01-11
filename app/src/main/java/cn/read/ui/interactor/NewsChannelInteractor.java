package cn.read.ui.interactor;
import cn.read.bean.NewsChannelTable;
import cn.read.listener.RequestCallBack;
import rx.Subscription;

/**
 * Created by lw on 2017/1/20.
 */

public interface NewsChannelInteractor<T> {
    Subscription lodeNewsChannels(RequestCallBack<T> callback);

    void swapDb(int fromPosition, int toPosition);

    void updateDb(NewsChannelTable newsChannel, boolean isChannelMine);
}
