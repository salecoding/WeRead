package cn.read.ui.presenter;

import cn.read.bean.NewsChannelTable;
import cn.read.bean.VideosChannelTable;

/**
 * Created by lw on 2017/1/19.
 */

public interface VideosChannelPresenter extends BasePresenter {
    void onItemSwap(int fromPosition, int toPosition);

    void onItemAddOrRemove(VideosChannelTable videosChannelTable, boolean isChannelMine);
}
