package cn.read.ui.view;

import java.util.List;

import cn.read.bean.BookMarks;
import cn.read.bean.NewsSummary;
import cn.read.common.LoadNewsType;

/**
 * Created by lw on 2017-03-08.
 */

public interface BookMarksListView extends BaseView {
    void setBookMarksesList(BookMarks bookMarks, @LoadNewsType.checker int loadType);
}
