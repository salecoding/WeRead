package cn.read.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import cn.read.dao.BeautyPhotoInfoDao;
import cn.read.dao.ChatLogDao;
import cn.read.dao.DaoMaster;
import cn.read.dao.NewsChannelTableDao;
import cn.read.dao.NewsSummaryDao;
import cn.read.dao.VideosChannelTableDao;
import cn.read.dao.VideosSummaryDao;

/**
 * Created by lw on 2016/12/20.
 */
public class DBHelper extends DaoMaster.OpenHelper {
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     *
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //操作数据库的更新
        MigrationHelper.migrate(db,
                NewsChannelTableDao.class,
                VideosChannelTableDao.class,
                ChatLogDao.class,
                BeautyPhotoInfoDao.class,
                NewsSummaryDao.class,
                VideosSummaryDao.class);

    }
}
