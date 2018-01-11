package cn.read.ui.view;


import java.util.List;

import cn.read.bean.NewsChannelTable;

/**
 * Created by lw on 2017/1/19.
 */

public interface NewsChannelView extends BaseView {
    void initRecyclerViews(List<NewsChannelTable> newsChannelsMine, List<NewsChannelTable> newsChannelsMore);
}
