package cn.read.ui.view;

import java.util.List;

import cn.read.bean.PhotoSetInfo;
import cn.read.common.LoadNewsType;

/**
 * Created by lw on 2017/3/12.
 */
public interface NewsPhotoSetView extends BaseView {
    void setNewsPhotoSetInfos(PhotoSetInfo photoSetInfo, @LoadNewsType.checker int loadType);
}
