package yc.com.homework.base.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

import yc.com.homework.index.domain.bean.CompositionInfo;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "COMPOSITION_INFO".
*/
public class CompositionInfoDao extends AbstractDao<CompositionInfo, Long> {

    public static final String TABLENAME = "COMPOSITION_INFO";

    /**
     * Properties of entity CompositionInfo.<br/>
     * Can be used for QueryBuilder and for referencing column names.
     */
    public static class Properties {
        public final static Property Id = new Property(0, Long.class, "id", true, "_id");
        public final static Property Compostion_id = new Property(1, String.class, "compostion_id", false, "COMPOSTION_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Version = new Property(3, String.class, "version", false, "VERSION");
        public final static Property Grade = new Property(4, String.class, "grade", false, "GRADE");
        public final static Property Part = new Property(5, String.class, "part", false, "PART");
        public final static Property Unit = new Property(6, String.class, "unit", false, "UNIT");
        public final static Property Content = new Property(7, String.class, "content", false, "CONTENT");
        public final static Property SaveTime = new Property(8, long.class, "saveTime", false, "SAVE_TIME");
        public final static Property IsCollect = new Property(9, int.class, "isCollect", false, "IS_COLLECT");
        public final static Property Img = new Property(10, String.class, "img", false, "IMG");
        public final static Property Pv_num = new Property(11, int.class, "pv_num", false, "PV_NUM");
    }


    public CompositionInfoDao(DaoConfig config) {
        super(config);
    }
    
    public CompositionInfoDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"COMPOSITION_INFO\" (" + //
                "\"_id\" INTEGER PRIMARY KEY AUTOINCREMENT ," + // 0: id
                "\"COMPOSTION_ID\" TEXT," + // 1: compostion_id
                "\"NAME\" TEXT," + // 2: name
                "\"VERSION\" TEXT," + // 3: version
                "\"GRADE\" TEXT," + // 4: grade
                "\"PART\" TEXT," + // 5: part
                "\"UNIT\" TEXT," + // 6: unit
                "\"CONTENT\" TEXT," + // 7: content
                "\"SAVE_TIME\" INTEGER NOT NULL ," + // 8: saveTime
                "\"IS_COLLECT\" INTEGER NOT NULL ," + // 9: isCollect
                "\"IMG\" TEXT," + // 10: img
                "\"PV_NUM\" INTEGER NOT NULL );"); // 11: pv_num
        // Add Indexes
        db.execSQL("CREATE INDEX " + constraint + "IDX_COMPOSITION_INFO_COMPOSTION_ID ON \"COMPOSITION_INFO\"" +
                " (\"COMPOSTION_ID\" ASC);");
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"COMPOSITION_INFO\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, CompositionInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String compostion_id = entity.getCompostion_id();
        if (compostion_id != null) {
            stmt.bindString(2, compostion_id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(4, version);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(5, grade);
        }
 
        String part = entity.getPart();
        if (part != null) {
            stmt.bindString(6, part);
        }
 
        String unit = entity.getUnit();
        if (unit != null) {
            stmt.bindString(7, unit);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
        stmt.bindLong(9, entity.getSaveTime());
        stmt.bindLong(10, entity.getIsCollect());
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(11, img);
        }
        stmt.bindLong(12, entity.getPv_num());
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, CompositionInfo entity) {
        stmt.clearBindings();
 
        Long id = entity.getId();
        if (id != null) {
            stmt.bindLong(1, id);
        }
 
        String compostion_id = entity.getCompostion_id();
        if (compostion_id != null) {
            stmt.bindString(2, compostion_id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String version = entity.getVersion();
        if (version != null) {
            stmt.bindString(4, version);
        }
 
        String grade = entity.getGrade();
        if (grade != null) {
            stmt.bindString(5, grade);
        }
 
        String part = entity.getPart();
        if (part != null) {
            stmt.bindString(6, part);
        }
 
        String unit = entity.getUnit();
        if (unit != null) {
            stmt.bindString(7, unit);
        }
 
        String content = entity.getContent();
        if (content != null) {
            stmt.bindString(8, content);
        }
        stmt.bindLong(9, entity.getSaveTime());
        stmt.bindLong(10, entity.getIsCollect());
 
        String img = entity.getImg();
        if (img != null) {
            stmt.bindString(11, img);
        }
        stmt.bindLong(12, entity.getPv_num());
    }

    @Override
    public Long readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0);
    }    

    @Override
    public CompositionInfo readEntity(Cursor cursor, int offset) {
        CompositionInfo entity = new CompositionInfo( //
            cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0), // id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // compostion_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // version
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // grade
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // part
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // unit
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // content
            cursor.getLong(offset + 8), // saveTime
            cursor.getInt(offset + 9), // isCollect
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // img
            cursor.getInt(offset + 11) // pv_num
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, CompositionInfo entity, int offset) {
        entity.setId(cursor.isNull(offset + 0) ? null : cursor.getLong(offset + 0));
        entity.setCompostion_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setVersion(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setGrade(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setPart(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setUnit(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setContent(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setSaveTime(cursor.getLong(offset + 8));
        entity.setIsCollect(cursor.getInt(offset + 9));
        entity.setImg(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setPv_num(cursor.getInt(offset + 11));
     }
    
    @Override
    protected final Long updateKeyAfterInsert(CompositionInfo entity, long rowId) {
        entity.setId(rowId);
        return rowId;
    }
    
    @Override
    public Long getKey(CompositionInfo entity) {
        if(entity != null) {
            return entity.getId();
        } else {
            return null;
        }
    }

    @Override
    public boolean hasKey(CompositionInfo entity) {
        return entity.getId() != null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
