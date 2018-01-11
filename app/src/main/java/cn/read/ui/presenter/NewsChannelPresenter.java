package cn.read.ui.presenter;

import cn.read.bean.NewsChannelTable;

/**
 * Created by lw on 2017/1/19.
 */

public interface NewsChannelPresenter extends BasePresenter {
    void onItemSwap(int fromPosition, int toPosition);

    void onItemAddOrRemove(NewsChannelTable newsChannel, boolean isChannelMine);
}
