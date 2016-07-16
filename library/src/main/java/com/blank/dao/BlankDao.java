package com.blank.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.blank.dao.annotations.BlankId;
import com.blank.dao.annotations.BlankTransient;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlankDao {

    private static final String ERROR_INPUT_PARAM = "Input param error!!!";

    // region get methods
    public static <T extends BlankBaseDaoObject> List<T> getAll(T obj) {
        List<T> list = new ArrayList<>();
        if (obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return list;
        }

        BlankBaseDaoManager database = openDatabase(obj);

        try {
            list = getAll(database.getReadableDatabase(), obj);
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            closeDatabase(database);
        }

        return list;
    }

    protected static <T extends BlankBaseDaoObject> List<T> getAll(SQLiteDatabase db, T obj) {
        List<T> list = new ArrayList<T>();
        if (db == null || obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return list;
        }

        Cursor c = null;

        try {
            String query = getQuerySelectAndFrom(obj) + getQueryOrderBy(obj);
            c = db.rawQuery(query, null);
            
            if (c.moveToFirst()) {
                do {
                    T aux = createNewInstance(obj);
                    putCursorValuesInObject(c, aux);
                    list.add(aux);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            if (c != null) {
                c.close();
            }
        }
        
        return list;
    }

    public static <T extends BlankBaseDaoObject> List<T> getFiltered(T filter) {
        List<T> list = new ArrayList<>();
        if (filter == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return list;
        }

        BlankBaseDaoManager database = openDatabase(filter);
        Cursor c = null;

        try {
            String query = getQuerySelectAndFrom(filter) + getQueryWhere(filter) + getQueryOrderBy(filter) + getQueryLimit(filter);
            c = database.getReadableDatabase().rawQuery(query, null);
            
            if (c.moveToFirst()) {
                do {
                    T aux = createNewInstance(filter);
                    putCursorValuesInObject(c, aux);
                    list.add(aux);
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            if (c != null) {
                c.close();
            }
            closeDatabase(database);
        }
        // Return object.
        return list;
    }

    public static <T extends BlankBaseDaoObject> void get(T obj) {
        if (obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        BlankBaseDaoManager database = openDatabase(obj);
        Cursor c = null;

        try {
            String query = getQuerySelectAndFrom(obj) + getQueryWhere(obj) + getQueryOrderBy(obj);
            c = database.getReadableDatabase().rawQuery(query, null);

            if (c.moveToFirst()) {
                putCursorValuesInObject(c, obj);
            }
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            if (c != null) {
                c.close();
            }
            closeDatabase(database);
        }
    }

    public static <T extends BlankBaseDaoObject> void getById(T obj) {
        if (obj == null || obj.id == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        BlankBaseDaoManager database = openDatabase(obj);
        Cursor c = null;

        try {
            int id = obj.id;
            resetObject(obj);
            obj.id = id;

            String query = getQuerySelectAndFrom(obj) + getQueryWhere(obj) + getQueryOrderBy(obj);
            c = database.getReadableDatabase().rawQuery(query, null);
            if (c.moveToFirst()) {
                putCursorValuesInObject(c, obj);
            }

        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            if (c != null) {
                c.close();
            }
            closeDatabase(database);
        }
    }
    // endregion

    // region delete methods
    public static <T extends BlankBaseDaoObject> void delete(List<T> list) {
        if (list == null || list.isEmpty()) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        BlankBaseDaoManager database = openDatabase(list.get(0));
        SQLiteDatabase writableDatabase = database.getWritableDatabase();

        try {
            writableDatabase.beginTransaction();

            for (T obj : list) {
                if (obj != null && obj.id != null) {
                    writableDatabase.delete(getTableName(obj), BlankBaseDaoObject.ID + " = " + obj.id, null);
                }
            }
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            writableDatabase.endTransaction();
            closeDatabase(database);
        }

    }

    public static <T extends BlankBaseDaoObject> void delete(T obj) {
        if (obj == null || obj.id == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        BlankBaseDaoManager database = openDatabase(obj);

        try {
            database.getWritableDatabase().delete(getTableName(obj), BlankBaseDaoObject.ID + " = " + obj.id, null);
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            closeDatabase(database);
        }
    }

    public static <T extends BlankBaseDaoObject> void deleteTable(T obj) {
        if (obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        BlankBaseDaoManager database = openDatabase(obj);

        try {
            database.getWritableDatabase().delete(getTableName(obj), null, null);
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            closeDatabase(database);
        }
    }
    // endregion

    // region saveOrUpdate methods
    public static <T extends BlankBaseDaoObject> void saveOrUpdate(List<T> list) {
        if (list == null || list.isEmpty() || list.get(0) == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        BlankBaseDaoManager database = openDatabase(list.get(0));

        try {
            saveOrUpdate(database.getWritableDatabase(), list);
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            closeDatabase(database);
        }
    }

    protected static <T extends BlankBaseDaoObject> void saveOrUpdate(SQLiteDatabase db, List<T> list) {
        if (list == null || list.isEmpty() || list.get(0) == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        BlankBaseDaoManager database = openDatabase(list.get(0));

        try {
            db.beginTransaction();

            for (T obj : list) {
                ContentValues values = getContentValues(obj);

                if (values != null) {
                    if (BlankObj.isNullOrEmpty(BlankObj.toString(values.get(BlankBaseDaoObject.ID)))) {
                        Long newId = db.insert(getTableName(obj), null, values);
                        obj.id = (BlankObj.toInteger(newId));
                    } else {
                        values.remove(BlankBaseDaoObject.ID);
                        db.update(getTableName(obj), values, BlankBaseDaoObject.ID + " = " + obj.id, null);
                    }
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            closeDatabase(database);
        }
    }

    public static <T extends BlankBaseDaoObject> void saveOrUpdate(T obj) {
        if (obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }
        BlankBaseDaoManager database = openDatabase(obj);

        try {
            ContentValues values = getContentValues(obj);

            if (values != null) {
                if (BlankObj.isNullOrEmpty(BlankObj.toString(values.get(BlankBaseDaoObject.ID)))) {
                    Long newId = database.getWritableDatabase().insert(getTableName(obj), null, values);
                    obj.id = (BlankObj.toInteger(newId));
                } else {
                    values.remove(BlankBaseDaoObject.ID);
                    database.getWritableDatabase().update(getTableName(obj), values, BlankBaseDaoObject.ID + " = " + obj.id, null);
                }
            }
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            closeDatabase(database);
        }
    }
    // endregion

    // region query methods
    protected static <T extends BlankBaseDaoObject> String getQueryCreateTable(T obj) {
        String create = "CREATE TABLE IF NOT EXISTS " + getTableName(obj) + " ( ";

        List<Field> fields = getOrderedFields(obj);
        for (int i = 0; i < fields.size(); i++) {
            String column = fields.get(i).getName();
            if (fields.get(i).getType().equals(Integer.class) || fields.get(i).getType().equals(Long.class) || BlankBaseDaoObject.class.isAssignableFrom(fields.get(i).getType())) {
                column += " INTEGER ";
            } else if (fields.get(i).getType().equals(Float.class) || fields.get(i).getType().equals(Double.class)) {
                column += " REAL ";
            } else if (fields.get(i).getType().equals(String.class)) {
                column += " TEXT ";
            }

            BlankId annotation = getBlankIdAnnotation(fields.get(i));
            if (annotation != null) {
                column += "PRIMARY KEY ";

                if (annotation.autoincrement()) {
                    column += "AUTOINCREMENT ";
                }
            }

            if (i == 0) {
                create += column;
            } else {
                create += ", " + column;
            }
        }
        create += " ) ";

        return create;
    }

    public static <T extends BlankBaseDaoObject> String getQueryDeleteTable(T obj) {
        return "DROP TABLE IF EXISTS " + getTableName(obj) + " ";
    }

    public static <T extends BlankBaseDaoObject> String getQuerySelectAndFrom(T obj) {
        String selectAndFrom = "SELECT ";

        if (!BlankObj.isNullOrEmpty(obj.customSelect)) {
            selectAndFrom += obj.customSelect + " ";
        } else {
            selectAndFrom += "* ";
        }

        selectAndFrom += "FROM ";

        if (!BlankObj.isNullOrEmpty(obj.customFrom)) {
            selectAndFrom += obj.customFrom + " ";

        } else {
            selectAndFrom += getTableName(obj) + " ";
        }

        return selectAndFrom;
    }

    protected static <T extends BlankBaseDaoObject> String getQueryWhere(T obj) {
        String where = "";

        if (!BlankObj.isNullOrEmpty(obj.customWhere)) {
            where += "WHERE " + obj.customWhere + " ";
        } else {
            List<Field> fields = getOrderedFields(obj);

            for (int i = 0; i < fields.size(); i++) {
                try {
                    Field field = fields.get(i);
                    field.setAccessible(Boolean.TRUE);

                    if (field.get(obj) != null) {
                        String fieldName = field.getName();
                        String fieldValue = null;

                        Object value = field.get(obj);
                        if (value instanceof String) {
                            fieldValue = "'" + BlankObj.toString(value) + "'";
                        } else if (value instanceof BlankBaseDaoObject) {
                            Integer id = ((BlankBaseDaoObject) value).id;
                            if (id != null) {
                                fieldValue = BlankObj.toString(id);
                            } else {
                                continue;
                            }
                        } else {
                            fieldValue = BlankObj.toString(value);
                        }

                        if ("".equals(where)) {
                            where += "WHERE " + fieldName + " = " + fieldValue + " ";
                        } else {
                            where += obj.filterType + " " + fieldName + " = " + fieldValue + " ";
                        }
                    }
                } catch (Exception e) {
                    BlankLog.error(e);
                }
            }
        }

        return where;
    }

    public static <T extends BlankBaseDaoObject> String getQueryOrderBy(T obj) {
        return "ORDER BY " + obj.orderBy + " " + obj.orderType + " ";
    }

    public static <T extends BlankBaseDaoObject> String getQueryLimit(T obj) {
        if (obj.limit != null) {
            return " LIMIT " + obj.limit;
        } else {
            return "";
        }
    }
    // endregion

    // region annotation methods
    private static BlankId getBlankIdAnnotation(Field field) {
        for(Annotation annotation : field.getDeclaredAnnotations()){
            if(annotation instanceof BlankId){
                return (BlankId)annotation;
            }
        }
        return null;
    }

    private static boolean containsBlankTransientAnnotation(Field field) {
        for(Annotation annotation : field.getDeclaredAnnotations()){
            if(annotation instanceof BlankTransient){
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }
    // endregion

    // region util methods
    @SuppressWarnings("unchecked")
    private static <T extends BlankBaseDaoObject> T createNewInstance(T obj) {
        try {
            Constructor<? extends BlankBaseDaoObject> con = obj.getClass().getDeclaredConstructor(Context.class);
            T aux = (T) con.newInstance(obj.context);
            return aux;
        } catch (Exception e) {
            BlankLog.error(e);
        }
        return null;
    }

    private static <T extends BlankBaseDaoObject> void resetObject(T obj) {
        List<Field> fields = getOrderedFields(obj);
        if (!fields.isEmpty()) {
            for (Field field : fields) {
                field.setAccessible(Boolean.TRUE);
                try {
                    field.set(obj, null);
                } catch (Exception e) {
                    Log.e(getTableName(obj), e.toString());
                }
            }
        }
    }

    private static <T extends BlankBaseDaoObject> ContentValues getContentValues(T obj) {
        // Content values.
        ContentValues values = null;

        List<Field> fields = getOrderedFields(obj);
        if (!fields.isEmpty()) {
            values = new ContentValues();

            for (Field field : fields) {
                field.setAccessible(Boolean.TRUE);

                try {
                    String name = field.getName();
                    Object value = field.get(obj);
                    if (value == null) {
                        values.putNull(name);
                    } else if (value instanceof Boolean) {
                        values.put(name, BlankObj.toBoolean(value));
                    } else if (value instanceof String) {
                        values.put(name, BlankObj.toString(value));
                    } else if (value instanceof Integer) {
                        values.put(name, BlankObj.toInteger(value));
                    } else if (value instanceof Long) {
                        values.put(name, BlankObj.toLong(value));
                    } else if (value instanceof Double) {
                        values.put(name, BlankObj.toDouble(value));
                    } else if (value instanceof Float) {
                        values.put(name, BlankObj.toFloat(value));
                    } else if (value instanceof BlankBaseDaoObject) {
                        values.put(name, ((BlankBaseDaoObject)value).id);
                    }

                } catch (Exception e) {
                    Log.e(getTableName(obj), e.toString());
                }
            }
        }
        // Return content values.
        return values;
    }

    @SuppressWarnings("unchecked")
    protected static <T extends BlankBaseDaoObject> void putCursorValuesInObject(Cursor c, T obj) {
        List<Field> fields = getOrderedFields(obj);

        if (!fields.isEmpty()) {
            for (Field field: fields) {
                field.setAccessible(Boolean.TRUE);
                int columIndex = c.getColumnIndex(field.getName());

                if (columIndex != -1) {
                    try {
                        Object value = field.get(obj);
                        if (value == null) {
                            if (c.isNull(columIndex)) {
                                field.set(obj, null);
                            } else if (field.getType().equals(Boolean.class)) {
                                field.set(obj, BlankObj.toBoolean(c.getInt(columIndex)));
                            } else if (field.getType().equals(String.class)) {
                                field.set(obj, c.getString(columIndex));
                            } else if (field.getType().equals(Integer.class)) {
                                field.set(obj, c.getInt(columIndex));
                            } else if (field.getType().equals(Long.class)) {
                                field.set(obj, BlankObj.toLong(c.getLong(columIndex)));
                            } else if (field.getType().equals(Float.class)) {
                                field.set(obj, BlankObj.toFloat(c.getFloat(columIndex)));
                            } else if (field.getType().equals(Double.class)) {
                                field.set(obj, BlankObj.toDouble(c.getDouble(columIndex)));
                            } else if (BlankBaseDaoObject.class.isAssignableFrom(field.getType())) {
                                Class<?> clazz = field.getType();
                                Constructor<? extends BlankBaseDaoObject> con = (Constructor<? extends BlankBaseDaoObject>) clazz.getDeclaredConstructor(Context.class);
                                BlankBaseDaoObject aux = (BlankBaseDaoObject) con.newInstance(obj.context);
                                aux.id = c.getInt(columIndex);
                                field.set(obj, aux);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(getTableName(obj), e.toString());
                    }
                }
            }
        } else {
            obj.id = null;
        }
    }

    private static <T extends BlankBaseDaoObject> String getTableName(T obj) {
        return obj.getClass().getSimpleName();
    }

    private static <T extends BlankBaseDaoObject> BlankBaseDaoManager openDatabase(T obj) {
        return obj.getBlankDaoManager();
    }

    private static void closeDatabase(BlankBaseDaoManager blankDatabaseManagement) {
        if (blankDatabaseManagement != null) {
            blankDatabaseManagement.close();
        }
    }
    // endregion

    // region object fields
    public static <T extends BlankBaseDaoObject> List<Field> getOrderedFields(T obj) {
        List<Field> list = getInheritedFields(obj.getClass());
        Collections.sort(list, new com.blank.dao.BlankComparatorField());
        return list;
    }

    private static List<Field> getInheritedFields(Class<?> type) {
        List<Field> fields = new ArrayList<>();

        for (Class<?> c = type; c != null && c != Object.class; c = c.getSuperclass()) {
            Field aux[] = c.getDeclaredFields();
            for (int i = 0; i < aux.length; i++) {
                if (!containsBlankTransientAnnotation(aux[i]) && !Modifier.isStatic(aux[i].getModifiers())) {
                    fields.add(aux[i]);
                }
            }
        }

        return fields;
    }
    // endregion
}
