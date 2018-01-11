package cn.read.db;

import com.socks.library.KLog;

import org.greenrobot.greendao.query.Query;

import java.util.Arrays;
import java.util.List;

import cn.read.R;
import cn.read.base.App;
import cn.read.bean.NewsChannelTable;
import cn.read.common.ApiConstants;
import cn.read.common.Constants;
import cn.read.dao.NewsChannelTableDao;
import cn.read.utils.MyUtils;


/**
 * Created by lw on 2017/1/16.
 */

public class NewsChannelTableManager {

    /**
     * 首次打开程序初始化db
     */
    public static void initDB(boolean isUpdate) {
        if (!MyUtils.getSharedPreferences().getBoolean(Constants.INIT_DB, false) || isUpdate) {
            NewsChannelTableDao dao = App.getNewsChannelTableDao();
            List<String> channelName = Arrays.asList(App.getContext().getResources()
                    .getStringArray(R.array.news_channel_name));
            List<String> channelId = Arrays.asList(App.getContext().getResources()
                    .getStringArray(R.array.news_channel_id));
            if (isUpdate) {
                List<NewsChannelTable> newsChannelTables = dao.loadAll();
                for (NewsChannelTable newsChannelTable : newsChannelTables) {
                    for (int i = 0; i < channelName.size(); i++) {
                        if (channelId.get(i).equals(newsChannelTable.getNewsChannelId())) {
                            newsChannelTable.setNewsChannelName(channelName.get(i));
                            dao.update(newsChannelTable);
                            KLog.i("key：" + dao.getKey(newsChannelTable));
                        }
                    }
                }
            } else {
                for (int i = 0; i < channelName.size(); i++) {
                    NewsChannelTable entity = new NewsChannelTable(MyUtils.getUUIDStr(), channelName.get(i), channelId.get(i)
                            , ApiConstants.getNewsType(channelId.get(i)), i <= 10, i, i == 0);
                    dao.insert(entity);
                    KLog.i("key：" + dao.getKey(entity));
                }
            }
            MyUtils.getSharedPreferences().edit().putBoolean(Constants.INIT_DB, true).apply();
        }
    }

    public static List<NewsChannelTable> loadNewsChannelsMine() {
        Query<NewsChannelTable> newsChannelTableQuery = App.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(true))
                .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsMore() {
        Query<NewsChannelTable> newsChannelTableQuery = App.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(false))
                .orderAsc(NewsChannelTableDao.Properties.NewsChannelIndex).build();
        return newsChannelTableQuery.list();
    }

    public static NewsChannelTable loadNewsChannel(int position) {
        return App.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex.eq(position)).build().unique();
    }

    public static void update(NewsChannelTable newsChannelTable) {
        App.getNewsChannelTableDao().update(newsChannelTable);
    }

    public static List<NewsChannelTable> loadNewsChannelsWithin(int from, int to) {
        Query<NewsChannelTable> newsChannelTableQuery = App.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex
                        .between(from, to)).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsIndexGt(int channelIndex) {
        Query<NewsChannelTable> newsChannelTableQuery = App.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex
                        .gt(channelIndex)).build();
        return newsChannelTableQuery.list();
    }

    public static List<NewsChannelTable> loadNewsChannelsIndexLtAndIsUnselect(int channelIndex) {
        Query<NewsChannelTable> newsChannelTableQuery = App.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelIndex.lt(channelIndex),
                        NewsChannelTableDao.Properties.NewsChannelSelect.eq(false)).build();
        return newsChannelTableQuery.list();
    }

    public static int getAllSize() {
        return App.getNewsChannelTableDao().loadAll().size();
    }

    public static int getNewsChannelSelectSize() {
        return (int) App.getNewsChannelTableDao().queryBuilder()
                .where(NewsChannelTableDao.Properties.NewsChannelSelect.eq(true))
                .buildCount().count();
    }
}
