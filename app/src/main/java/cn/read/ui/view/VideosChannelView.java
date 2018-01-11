package cn.read.ui.view;


import java.util.List;

import cn.read.bean.NewsChannelTable;
import cn.read.bean.VideosChannelTable;

/**
 * Created by lw on 2017/1/19.
 */

public interface VideosChannelView extends BaseView {
    void initRecyclerViews(List<VideosChannelTable> videosChannelsMine, List<VideosChannelTable> videosChannelsMore);
}
