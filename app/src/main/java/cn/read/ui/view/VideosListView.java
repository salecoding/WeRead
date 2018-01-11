package cn.read.ui.view;

import java.util.List;

import cn.read.bean.NewsSummary;
import cn.read.bean.VideosSummary;
import cn.read.common.LoadNewsType;

/**
 * Created by lw on 2017-03-08.
 */

public interface VideosListView extends BaseView {
    void setVideosList(List<VideosSummary> newsSummary, @LoadNewsType.checker int loadType);
}
