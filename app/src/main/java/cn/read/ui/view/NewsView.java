package cn.read.ui.view;

import java.util.List;

import cn.read.bean.NewsChannelTable;

/**
 * Created by lw on 2017-03-07.
 */

public interface NewsView extends BaseView {
    void initViewPager(List<NewsChannelTable> newsChannelTables);
}
