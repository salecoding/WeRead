package cn.read.ui.view;

import java.util.List;

import cn.read.bean.NewsSummary;
import cn.read.common.LoadNewsType;

/**
 * Created by lw on 2017-03-08.
 */

public interface NewsListView extends BaseView {
    void setNewsList(List<NewsSummary> newsSummary, @LoadNewsType.checker int loadType);

    void setNewsHead(int position, NewsSummary newsSummary);
}
