package cn.read.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import cn.read.bean.BeautyPhotoInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "BEAUTY_PHOTO_INFO".
*/
public class BeautyPhotoInfoDao extends AbstractDao<BeautyPhotoInfo, String> {

    public static final String TABLENAME = "BEAUTY_PHOTO_INFO";

    /**
     * Properties of entity BeautyPhotoInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Adtype = new Property(0, int.class, "adtype", false, "ADTYPE");
        public final static Property Boardid = new Property(1, String.class, "boardid", false, "BOARDID");
        public final static Property ClkNum = new Property(2, int.class, "clkNum", false, "CLK_NUM");
        public final static Property Digest = new Property(3, String.class, "digest", false, "DIGEST");
        public final static Property Docid = new Property(4, String.class, "docid", false, "DOCID");
        public final static Property DownTimes = new Property(5, int.class, "downTimes", false, "DOWN_TIMES");
        public final static Property Img = new Property(6, String.class, "img", false, "IMG");
        public final static Property ImgType = new Property(7, int.class, "imgType", false, "IMG_TYPE");
        public final static Property Imgsrc = new Property(8, String.class, "imgsrc", true, "IMGSRC");
        public final static Property Imgsum = new Property(9, int.class, "imgsum", false, "IMGSUM");
        public final static Property PicCount = new Property(10, int.class, "picCount", false, "PIC_COUNT");
        public final static Property Pixel = new Property(11, String.class, "pixel", false, "PIXEL");
        public final static Property Program = new Property(12, String.class, "program", false, "PROGRAM");
        public final static Property Prompt = new Property(13, String.class, "prompt", false, "PROMPT");
        public final static Property RecNews = new Property(14, int.class, "recNews", false, "REC_NEWS");
        public final static Property RecType = new Property(15, int.class, "recType", false, "REC_TYPE");
        public final static Property RefreshPrompt = new Property(16, int.class, "refreshPrompt", false, "REFRESH_PROMPT");
        public final static Property ReplyCount = new Property(17, int.class, "replyCount", false, "REPLY_COUNT");
        public final static Property Replyid = new Property(18, String.class, "replyid", false, "REPLYID");
        public final static Property Source = new Property(19, String.class, "source", false, "SOURCE");
        public final static Property Title = new Property(20, String.class, "title", false, "TITLE");
        public final static Property UpTimes = new Property(21, int.class, "upTimes", false, "UP_TIMES");
        public final static Property IsLove = new Property(22, boolean.class, "isLove", false, "IS_LOVE");
        public final static Property IsPraise = new Property(23, boolean.class, "isPraise", false, "IS_PRAISE");
        public final static Property IsDownload = new Property(24, boolean.class, "isDownload", false, "IS_DOWNLOAD");
    };


    public BeautyPhotoInfoDao(DaoConfig config) {
        super(config);
    }
    
    public BeautyPhotoInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BEAUTY_PHOTO_INFO\" (" + //
                "\"ADTYPE\" INTEGER NOT NULL ," + // 0: adtype
                "\"BOARDID\" TEXT," + // 1: boardid
                "\"CLK_NUM\" INTEGER NOT NULL ," + // 2: clkNum
                "\"DIGEST\" TEXT," + // 3: digest
                "\"DOCID\" TEXT," + // 4: docid
                "\"DOWN_TIMES\" INTEGER NOT NULL ," + // 5: downTimes
                "\"IMG\" TEXT," + // 6: img
                "\"IMG_TYPE\" INTEGER NOT NULL ," + // 7: imgType
                "\"IMGSRC\" TEXT PRIMARY KEY NOT NULL ," + // 8: imgsrc
                "\"IMGSUM\" INTEGER NOT NULL ," + // 9: imgsum
                "\"PIC_COUNT\" INTEGER NOT NULL ," + // 10: picCount
                "\"PIXEL\" TEXT," + // 11: pixel
                "\"PROGRAM\" TEXT," + // 12: program
                "\"PROMPT\" TEXT," + // 13: prompt
                "\"REC_NEWS\" INTEGER NOT NULL ," + // 14: recNews
                "\"REC_TYPE\" INTEGER NOT NULL ," + // 15: recType
                "\"REFRESH_PROMPT\" INTEGER NOT NULL ," + // 16: refreshPrompt
                "\"REPLY_COUNT\" INTEGER NOT NULL ," + // 17: replyCount
                "\"REPLYID\" TEXT," + // 18: replyid
                "\"SOURCE\" TEXT," + // 19: source
                "\"TITLE\" TEXT," + // 20: title
                "\"UP_TIMES\" INTEGER NOT NULL ," + // 21: upTimes
                "\"IS_LOVE\" INTEGER NOT NULL ," + // 22: isLove
                "\"IS_PRAISE\" INTEGER NOT NULL ," + // 23: isPraise
                "\"IS_DOWNLOAD\" INTEGER NOT NULL );"); // 24: isDownload
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BEAUTY_PHOTO_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BeautyPhotoInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getAdtype());
 
        String boardid = entity.getBoardid();
        if (boardid != null) {
            stmt.bindString(2, boardid);
        }
        stmt.bindLong(3, entity.getClkNum());
 
        String digest = entity.getDigest();
        if (digest != null) {
            stmt.bindString(4, digest);
        }
 
        String docid = entity.getDocid();
        if (docid != null) {
            stmt.bindString(5, docid);
        }
        stmt.bindLong(6, entity.getDownTimes());
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(7, img);
        }
        stmt.bindLong(8, entity.getImgType());
 
        String imgsrc = entity.getImgsrc();
        if (imgsrc != null) {
            stmt.bindString(9, imgsrc);
        }
        stmt.bindLong(10, entity.getImgsum());
        stmt.bindLong(11, entity.getPicCount());
 
        String pixel = entity.getPixel();
        if (pixel != null) {
            stmt.bindString(12, pixel);
        }
 
        String program = entity.getProgram();
        if (program != null) {
            stmt.bindString(13, program);
        }
 
        String prompt = entity.getPrompt();
        if (prompt != null) {
            stmt.bindString(14, prompt);
        }
        stmt.bindLong(15, entity.getRecNews());
        stmt.bindLong(16, entity.getRecType());
        stmt.bindLong(17, entity.getRefreshPrompt());
        stmt.bindLong(18, entity.getReplyCount());
 
        String replyid = entity.getReplyid();
        if (replyid != null) {
            stmt.bindString(19, replyid);
        }
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(20, source);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(21, title);
        }
        stmt.bindLong(22, entity.getUpTimes());
        stmt.bindLong(23, entity.getIsLove() ? 1L: 0L);
        stmt.bindLong(24, entity.getIsPraise() ? 1L: 0L);
        stmt.bindLong(25, entity.getIsDownload() ? 1L: 0L);
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BeautyPhotoInfo entity) {
        stmt.clearBindings();
        stmt.bindLong(1, entity.getAdtype());
 
        String boardid = entity.getBoardid();
        if (boardid != null) {
            stmt.bindString(2, boardid);
        }
        stmt.bindLong(3, entity.getClkNum());
 
        String digest = entity.getDigest();
        if (digest != null) {
            stmt.bindString(4, digest);
        }
 
        String docid = entity.getDocid();
        if (docid != null) {
            stmt.bindString(5, docid);
        }
        stmt.bindLong(6, entity.getDownTimes());
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(7, img);
        }
        stmt.bindLong(8, entity.getImgType());
 
        String imgsrc = entity.getImgsrc();
        if (imgsrc != null) {
            stmt.bindString(9, imgsrc);
        }
        stmt.bindLong(10, entity.getImgsum());
        stmt.bindLong(11, entity.getPicCount());
 
        String pixel = entity.getPixel();
        if (pixel != null) {
            stmt.bindString(12, pixel);
        }
 
        String program = entity.getProgram();
        if (program != null) {
            stmt.bindString(13, program);
        }
 
        String prompt = entity.getPrompt();
        if (prompt != null) {
            stmt.bindString(14, prompt);
        }
        stmt.bindLong(15, entity.getRecNews());
        stmt.bindLong(16, entity.getRecType());
        stmt.bindLong(17, entity.getRefreshPrompt());
        stmt.bindLong(18, entity.getReplyCount());
 
        String replyid = entity.getReplyid();
        if (replyid != null) {
            stmt.bindString(19, replyid);
        }
 
        String source = entity.getSource();
        if (source != null) {
            stmt.bindString(20, source);
        }
 
        String title = entity.getTitle();
        if (title != null) {
            stmt.bindString(21, title);
        }
        stmt.bindLong(22, entity.getUpTimes());
        stmt.bindLong(23, entity.getIsLove() ? 1L: 0L);
        stmt.bindLong(24, entity.getIsPraise() ? 1L: 0L);
        stmt.bindLong(25, entity.getIsDownload() ? 1L: 0L);
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8);
    }    

    @Override
    public BeautyPhotoInfo readEntity(Cursor cursor, int offset) {
        BeautyPhotoInfo entity = new BeautyPhotoInfo( //
            cursor.getInt(offset + 0), // adtype
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // boardid
            cursor.getInt(offset + 2), // clkNum
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // digest
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // docid
            cursor.getInt(offset + 5), // downTimes
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // img
            cursor.getInt(offset + 7), // imgType
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // imgsrc
            cursor.getInt(offset + 9), // imgsum
            cursor.getInt(offset + 10), // picCount
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // pixel
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12), // program
            cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13), // prompt
            cursor.getInt(offset + 14), // recNews
            cursor.getInt(offset + 15), // recType
            cursor.getInt(offset + 16), // refreshPrompt
            cursor.getInt(offset + 17), // replyCount
            cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18), // replyid
            cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19), // source
            cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20), // title
            cursor.getInt(offset + 21), // upTimes
            cursor.getShort(offset + 22) != 0, // isLove
            cursor.getShort(offset + 23) != 0, // isPraise
            cursor.getShort(offset + 24) != 0 // isDownload
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BeautyPhotoInfo entity, int offset) {
        entity.setAdtype(cursor.getInt(offset + 0));
        entity.setBoardid(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setClkNum(cursor.getInt(offset + 2));
        entity.setDigest(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setDocid(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setDownTimes(cursor.getInt(offset + 5));
        entity.setImg(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setImgType(cursor.getInt(offset + 7));
        entity.setImgsrc(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setImgsum(cursor.getInt(offset + 9));
        entity.setPicCount(cursor.getInt(offset + 10));
        entity.setPixel(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setProgram(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
        entity.setPrompt(cursor.isNull(offset + 13) ? null : cursor.getString(offset + 13));
        entity.setRecNews(cursor.getInt(offset + 14));
        entity.setRecType(cursor.getInt(offset + 15));
        entity.setRefreshPrompt(cursor.getInt(offset + 16));
        entity.setReplyCount(cursor.getInt(offset + 17));
        entity.setReplyid(cursor.isNull(offset + 18) ? null : cursor.getString(offset + 18));
        entity.setSource(cursor.isNull(offset + 19) ? null : cursor.getString(offset + 19));
        entity.setTitle(cursor.isNull(offset + 20) ? null : cursor.getString(offset + 20));
        entity.setUpTimes(cursor.getInt(offset + 21));
        entity.setIsLove(cursor.getShort(offset + 22) != 0);
        entity.setIsPraise(cursor.getShort(offset + 23) != 0);
        entity.setIsDownload(cursor.getShort(offset + 24) != 0);
     }
    
    @Override
    protected final String updateKeyAfterInsert(BeautyPhotoInfo entity, long rowId) {
        return entity.getImgsrc();
    }
    
    @Override
    public String getKey(BeautyPhotoInfo entity) {
        if(entity != null) {
            return entity.getImgsrc();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}