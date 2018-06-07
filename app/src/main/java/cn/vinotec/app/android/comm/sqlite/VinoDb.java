package cn.vinotec.app.android.comm.sqlite;

import android.content.Context;
import cn.vinotec.app.android.comm.VinoApplication;
import cn.vinotec.app.android.comm.utils.ApplicationUtil;
import net.tsz.afinal.FinalDb;

import java.util.List;

public class VinoDb {

    private static boolean debugMode = false;

    private static String DB_NAME;
    private static int DB_VERSION;

    private FinalDb db;

    static {
        debugMode = ApplicationUtil.getApplicationMetaDataBoolean(VinoApplication.getContext(), "DEBUG", false);
        DB_NAME = ApplicationUtil.getApplicationMetaData(VinoApplication.getContext(), "DATABASE_NAME");
        DB_VERSION = ApplicationUtil.getApplicationMetaDataInt(VinoApplication.getContext(), "DATABASE_VERSION");
    }

    public VinoDb(Context context)
    {
        if(debugMode)
        {
            db = FinalDb.create(context, DB_NAME, true, DB_VERSION, null);
        }else
        {
            db = FinalDb.create(context, DB_NAME, false, DB_VERSION, null);
        }
    }

    public void save(Object entity) {
        db.save(entity);
    }

    public boolean saveBindId(Object entity) {
        return db.saveBindId(entity);
    }

    public void update(Object entity) {
        db.update(entity);
    }

    public void update(Object entity, String strWhere) {
        db.update(entity, strWhere);
    }

    public void delete(Object entity) {
        db.delete(entity);
    }

    public void deleteById(Class<?> clazz, Object id) {
        db.deleteById(clazz, id);
    }

    public void deleteByWhere(Class<?> clazz, String strWhere) {
        db.deleteById(clazz, strWhere);
    }

    public void deleteAll(Class<?> clazz) {
        db.deleteAll(clazz);
    }

    public void dropTable(Class<?> clazz) {
        db.dropTable(clazz);
    }

    public <T> T findById(Object id, Class<T> clazz) {
        return db.findById(id, clazz);
    }

    public <T> List<T> findAll(Class<T> clazz) {
        return db.findAll(clazz);
    }

    public <T> List<T> findAll(Class<T> clazz, String orderBy) {
        return db.findAll(clazz, orderBy);
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere) {
        return db.findAllByWhere(clazz, strWhere);
    }

    public <T> List<T> findAllByWhere(Class<T> clazz, String strWhere, String orderBy) {
        return db.findAllByWhere(clazz, strWhere, orderBy);
    }

    public <T> int findCountByWhere(Class<T> clazz, String strWhere) {
        return db.findCountByWhere(clazz, strWhere);
    }
}
