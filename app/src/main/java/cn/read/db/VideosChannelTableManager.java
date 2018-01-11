package cn.read.db;

import com.socks.library.KLog;

import org.greenrobot.greendao.query.Query;

import java.util.Arrays;
import java.util.List;

import cn.read.R;
import cn.read.base.App;
import cn.read.bean.VideosChannelTable;
import cn.read.common.ApiConstants;
import cn.read.common.Constants;
import cn.read.dao.VideosChannelTableDao;
import cn.read.utils.MyUtils;


/**
 * Created by lw on 2017/1/16.
 */

public class VideosChannelTableManager {

    /**
     * 首次打开程序初始化db
     */
    public static void initDB(boolean isUpdate) {
        if (!MyUtils.getSharedPreferences().getBoolean(Constants.INIT_DB, false) || isUpdate) {
            VideosChannelTableDao dao = App.getVideosChannelTableDao();
            List<String> channelName = Arrays.asList(App.getContext().getResources()
                    .getStringArray(R.array.videos_channel_name));
            List<String> channelId = Arrays.asList(App.getContext().getResources()
                    .getStringArray(R.array.videos_channel_id));
            if (isUpdate) {
                List<VideosChannelTable> videosChannelTables = dao.loadAll();
                for (VideosChannelTable videosChannelTable : videosChannelTables) {
                    for (int i = 0; i < channelName.size(); i++) {
                        if (channelId.get(i).equals(videosChannelTable.getVideosChannelId())) {
                            videosChannelTable.setVideosChannelName(channelName.get(i));
                            dao.update(videosChannelTable);
                            KLog.i("key：" + dao.getKey(videosChannelTable));
                        }
                    }
                }
            } else {
                for (int i = 0; i < channelName.size(); i++) {
                    VideosChannelTable entity = new VideosChannelTable(MyUtils.getUUIDStr(), channelName.get(i), channelId.get(i)
                            , ApiConstants.getVideosType(channelId.get(i)), i <= 10, i, i == 0);
                    dao.insert(entity);
                }
            }
            MyUtils.getSharedPreferences().edit().putBoolean(Constants.INIT_DB, true).apply();
        }
    }

    public static List<VideosChannelTable> loadVideosChannelsMine() {
        Query<VideosChannelTable> videosChannelTableQuery = App.getVideosChannelTableDao().queryBuilder()
                .where(VideosChannelTableDao.Properties.VideosChannelSelect.eq(true))
                .orderAsc(VideosChannelTableDao.Properties.VideosChannelIndex).build();
        return videosChannelTableQuery.list();
    }

    public static List<VideosChannelTable> loadVideosChannelsMore() {
        Query<VideosChannelTable> videosChannelTableQuery = App.getVideosChannelTableDao().queryBuilder()
                .where(VideosChannelTableDao.Properties.VideosChannelSelect.eq(false))
                .orderAsc(VideosChannelTableDao.Properties.VideosChannelIndex).build();
        return videosChannelTableQuery.list();
    }

    public static VideosChannelTable loadVideosChannel(int position) {
        return App.getVideosChannelTableDao().queryBuilder()
                .where(VideosChannelTableDao.Properties.VideosChannelIndex.eq(position)).build().unique();
    }

    public static void update(VideosChannelTable videosChannelTable) {
        App.getVideosChannelTableDao().update(videosChannelTable);
    }

    public static List<VideosChannelTable> loadVideosChannelsWithin(int from, int to) {
        Query<VideosChannelTable> videosChannelTableQuery = App.getVideosChannelTableDao().queryBuilder()
                .where(VideosChannelTableDao.Properties.VideosChannelIndex
                        .between(from, to)).build();
        return videosChannelTableQuery.list();
    }

    public static List<VideosChannelTable> loadVideosChannelsIndexGt(int channelIndex) {
        Query<VideosChannelTable> videosChannelTableQuery = App.getVideosChannelTableDao().queryBuilder()
                .where(VideosChannelTableDao.Properties.VideosChannelIndex
                        .gt(channelIndex)).build();
        return videosChannelTableQuery.list();
    }

    public static List<VideosChannelTable> loadVideosChannelsIndexLtAndIsUnselect(int channelIndex) {
        Query<VideosChannelTable> videosChannelTableQuery = App.getVideosChannelTableDao().queryBuilder()
                .where(VideosChannelTableDao.Properties.VideosChannelIndex.lt(channelIndex),
                        VideosChannelTableDao.Properties.VideosChannelSelect.eq(false)).build();
        return videosChannelTableQuery.list();
    }

    public static int getAllSize() {
        return App.getVideosChannelTableDao().loadAll().size();
    }

    public static int getVideosChannelSelectSize() {
        return (int) App.getVideosChannelTableDao().queryBuilder()
                .where(VideosChannelTableDao.Properties.VideosChannelSelect.eq(true))
                .buildCount().count();
    }
}
