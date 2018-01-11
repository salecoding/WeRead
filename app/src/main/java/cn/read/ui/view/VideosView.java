package cn.read.ui.view;

import java.util.List;

import cn.read.bean.NewsChannelTable;
import cn.read.bean.VideosChannelTable;

/**
 * Created by lw on 2017-03-07.
 */

public interface VideosView extends BaseView {
    void initViewPager(List<VideosChannelTable> videosChannelTables);
}
