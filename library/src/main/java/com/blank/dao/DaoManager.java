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
import java.util.Comparator;
import java.util.List;

public abstract class DaoManager extends DaoHelper {

    private static final String ERROR_INPUT_PARAM = "Input param error!!!";

    // region constructor
    protected DaoManager(Context context, int version) {
        super(context, version);
    }
    // endregion

    // region get methods
    public <T extends DaoBaseObject> List<T> getAll(T obj) {
        List<T> list = new ArrayList<>();
        if (obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return list;
        }

        try {
            list = getAll(getReadableDatabase(), obj);
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            close();
        }

        return list;
    }

    protected <T extends DaoBaseObject> List<T> getAll(SQLiteDatabase db, T obj) {
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

    public <T extends DaoBaseObject> List<T> getFiltered(T filter) {
        List<T> list = new ArrayList<>();
        if (filter == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return list;
        }

        Cursor c = null;
        try {
            String query = getQuerySelectAndFrom(filter) + getQueryWhere(filter) + getQueryOrderBy(filter) + getQueryLimit(filter);
            c = getReadableDatabase().rawQuery(query, null);
            
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
            close();
        }
        // Return object.
        return list;
    }

    public <T extends DaoBaseObject> void get(T obj) {
        if (obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        Cursor c = null;
        try {
            String query = getQuerySelectAndFrom(obj) + getQueryWhere(obj) + getQueryOrderBy(obj);
            c = getReadableDatabase().rawQuery(query, null);

            if (c.moveToFirst()) {
                putCursorValuesInObject(c, obj);
            }
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            if (c != null) {
                c.close();
            }
            close();
        }
    }

    public <T extends DaoBaseObject> void getById(T obj) {
        if (obj == null || obj.getId() == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        Cursor c = null;
        try {
            int id = obj.getId();
            resetObject(obj);
            obj.setId(id);

            String query = getQuerySelectAndFrom(obj) + getQueryWhere(obj) + getQueryOrderBy(obj);
            c = getReadableDatabase().rawQuery(query, null);
            if (c.moveToFirst()) {
                putCursorValuesInObject(c, obj);
            }

        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            if (c != null) {
                c.close();
            }
            close();
        }
    }
    // endregion

    // region delete methods
    public <T extends DaoBaseObject> void delete(List<T> list) {
        if (list == null || list.isEmpty()) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        SQLiteDatabase writableDatabase = getWritableDatabase();
        try {
            writableDatabase.beginTransaction();

            for (T obj : list) {
                if (obj != null && obj.getId() != null) {
                    writableDatabase.delete(getTableName(obj), DaoBaseObject.ID + " = " + obj.getId(), null);
                }
            }
            writableDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            writableDatabase.endTransaction();
            close();
        }

    }

    public <T extends DaoBaseObject> void delete(T obj) {
        if (obj == null || obj.getId() == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        try {
            getWritableDatabase().delete(getTableName(obj), DaoBaseObject.ID + " = " + obj.getId(), null);
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            close();
        }
    }

    public <T extends DaoBaseObject> void deleteTable(T obj) {
        if (obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        try {
            getWritableDatabase().delete(getTableName(obj), null, null);
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            close();
        }
    }
    // endregion

    // region saveOrUpdate methods
    public <T extends DaoBaseObject> void saveOrUpdate(List<T> list) {
        if (list == null || list.isEmpty() || list.get(0) == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        try {
            saveOrUpdate(getWritableDatabase(), list);
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            close();
        }
    }

    protected <T extends DaoBaseObject> void saveOrUpdate(SQLiteDatabase db, List<T> list) {
        if (list == null || list.isEmpty() || list.get(0) == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        try {
            db.beginTransaction();

            for (T obj : list) {
                ContentValues values = getContentValues(obj);

                if (values != null) {
                    if (ParseObj.isNullOrEmpty(ParseObj.toString(values.get(DaoBaseObject.ID)))) {
                        Long newId = db.insert(getTableName(obj), null, values);
                        obj.setId(ParseObj.toInteger(newId));
                    } else {
                        values.remove(DaoBaseObject.ID);
                        db.update(getTableName(obj), values, DaoBaseObject.ID + " = " + obj.getId(), null);
                    }
                }
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            db.endTransaction();
            close();
        }
    }

    public <T extends DaoBaseObject> void saveOrUpdate(T obj) {
        if (obj == null) {
            BlankLog.error(new Exception(ERROR_INPUT_PARAM));
            return;
        }

        try {
            ContentValues values = getContentValues(obj);

            if (values != null) {
                if (ParseObj.isNullOrEmpty(ParseObj.toString(values.get(DaoBaseObject.ID)))) {
                    Long newId = getWritableDatabase().insert(getTableName(obj), null, values);
                    obj.setId(ParseObj.toInteger(newId));
                } else {
                    values.remove(DaoBaseObject.ID);
                    getWritableDatabase().update(getTableName(obj), values, DaoBaseObject.ID + " = " + obj.getId(), null);
                }
            }
        } catch (Exception e) {
            BlankLog.error(e);
        } finally {
            close();
        }
    }
    // endregion

    // region query methods
    protected <T extends DaoBaseObject> String getQueryCreateTable(T obj) {
        String create = "CREATE TABLE IF NOT EXISTS " + getTableName(obj) + " ( ";

        List<Field> fields = getOrderedFields(obj);
        for (int i = 0; i < fields.size(); i++) {
            String column = fields.get(i).getName();

            if (fields.get(i).getType().equals(Float.class) || fields.get(i).getType().equals(Double.class)) {
                column += " REAL ";
            } else if (fields.get(i).getType().equals(String.class)) {
                column += " TEXT ";
            } else {
                column += " INTEGER ";
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

    public <T extends DaoBaseObject> String getQueryDeleteTable(T obj) {
        return "DROP TABLE IF EXISTS " + getTableName(obj) + " ";
    }

    public <T extends DaoBaseObject> String getQuerySelectAndFrom(T obj) {
        String selectAndFrom = "SELECT ";

        if (!ParseObj.isNullOrEmpty(obj.customSelect)) {
            selectAndFrom += obj.customSelect + " ";
        } else {
            selectAndFrom += "* ";
        }

        selectAndFrom += "FROM ";

        if (!ParseObj.isNullOrEmpty(obj.customFrom)) {
            selectAndFrom += obj.customFrom + " ";

        } else {
            selectAndFrom += getTableName(obj) + " ";
        }

        return selectAndFrom;
    }

    protected <T extends DaoBaseObject> String getQueryWhere(T obj) {
        String where = "";

        if (!ParseObj.isNullOrEmpty(obj.customWhere)) {
            where += "WHERE " + obj.customWhere + " ";
        }

        List<Field> fields = getOrderedFields(obj);

        if (!DaoBaseObject.FILTER_TYPE_AND.equals(obj.filterType) && !DaoBaseObject.FILTER_TYPE_OR.equals(obj.filterType)) {
            obj.filterType = DaoBaseObject.FILTER_TYPE_AND;
        }

        for (int i = 0; i < fields.size(); i++) {
            try {
                Field field = fields.get(i);
                field.setAccessible(Boolean.TRUE);

                if (field.get(obj) != null) {
                    String fieldName = field.getName();
                    String fieldValue;

                    Object value = field.get(obj);
                    if (value instanceof String) {
                        fieldValue = "'" + ParseObj.toString(value) + "'";
                    } else if (value instanceof DaoBaseObject) {
                        Integer id = ((DaoBaseObject) value).getId();
                        if (id != null) {
                            fieldValue = ParseObj.toString(id);
                        } else {
                            continue;
                        }
                    } else {
                        fieldValue = ParseObj.toString(value);
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

        return where;
    }

    public <T extends DaoBaseObject> String getQueryOrderBy(T obj) {
        if (obj.orderBy == null) {
            obj.orderBy = DaoBaseObject.ORDER_BY_DEFAULT;
        }

        if (!DaoBaseObject.ORDER_TYPE_ASC.equals(obj.orderType) && !DaoBaseObject.ORDER_TYPE_DESC.equals(obj.orderType)) {
            obj.orderType = DaoBaseObject.ORDER_TYPE_ASC;
        }
        return "ORDER BY " + obj.orderBy + " " + obj.orderType + " ";
    }

    public <T extends DaoBaseObject> String getQueryLimit(T obj) {
        if (obj.limit != null) {
            return " LIMIT " + obj.limit;
        } else {
            return "";
        }
    }
    // endregion

    // region annotation methods
    private BlankId getBlankIdAnnotation(Field field) {
        for(Annotation annotation : field.getDeclaredAnnotations()){
            if(annotation instanceof BlankId){
                return (BlankId)annotation;
            }
        }
        return null;
    }

    private boolean containsBlankTransientAnnotation(Field field) {
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
    private <T extends DaoBaseObject> T createNewInstance(T obj) {
        try {
            Constructor<? extends DaoBaseObject> con = obj.getClass().getDeclaredConstructor();
            con.setAccessible(true);
            T aux = (T) con.newInstance();
            return aux;
        } catch (Exception e) {
            BlankLog.error(e);
        }
        return null;
    }

    private <T extends DaoBaseObject> void resetObject(T obj) {
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

    private <T extends DaoBaseObject> ContentValues getContentValues(T obj) {
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
                        values.put(name, ParseObj.toBoolean(value));
                    } else if (value instanceof String) {
                        values.put(name, ParseObj.toString(value));
                    } else if (value instanceof Integer) {
                        values.put(name, ParseObj.toInteger(value));
                    } else if (value instanceof Long) {
                        values.put(name, ParseObj.toLong(value));
                    } else if (value instanceof Double) {
                        values.put(name, ParseObj.toDouble(value));
                    } else if (value instanceof Float) {
                        values.put(name, ParseObj.toFloat(value));
                    } else if (value instanceof DaoBaseObject) {
                        values.put(name, ((DaoBaseObject)value).getId());
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
    protected <T extends DaoBaseObject> void putCursorValuesInObject(Cursor c, T obj) {
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
                                field.set(obj, ParseObj.toBoolean(c.getInt(columIndex)));
                            } else if (field.getType().equals(String.class)) {
                                field.set(obj, c.getString(columIndex));
                            } else if (field.getType().equals(Integer.class)) {
                                field.set(obj, c.getInt(columIndex));
                            } else if (field.getType().equals(Long.class)) {
                                field.set(obj, ParseObj.toLong(c.getLong(columIndex)));
                            } else if (field.getType().equals(Float.class)) {
                                field.set(obj, ParseObj.toFloat(c.getFloat(columIndex)));
                            } else if (field.getType().equals(Double.class)) {
                                field.set(obj, ParseObj.toDouble(c.getDouble(columIndex)));
                            } else if (DaoBaseObject.class.isAssignableFrom(field.getType())) {
                                Class<?> clazz = field.getType();
                                Constructor<? extends DaoBaseObject> con = (Constructor<? extends DaoBaseObject>) clazz.getDeclaredConstructor();
                                DaoBaseObject aux = (DaoBaseObject) con.newInstance();
                                aux.setId(c.getInt(columIndex));
                                field.set(obj, aux);
                            }
                        }
                    } catch (Exception e) {
                        Log.e(getTableName(obj), e.toString());
                    }
                }
            }
        } else {
            obj.setId(null);
        }
    }

    private <T extends DaoBaseObject> String getTableName(T obj) {
        return obj.getClass().getSimpleName();
    }

    private void closeDatabase(DaoHelper blankDatabaseManagement) {
        if (blankDatabaseManagement != null) {
            blankDatabaseManagement.close();
        }
    }
    // endregion

    // region object fields
    public <T extends DaoBaseObject> List<Field> getOrderedFields(T obj) {
        List<Field> list = getInheritedFields(obj.getClass());
        Collections.sort(list, new BlankComparatorField());
        return list;
    }

    private List<Field> getInheritedFields(Class<?> type) {
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

    private class BlankComparatorField implements Comparator<Field> {

        /**
         * Constructor
         */
        public BlankComparatorField() {
            super();
        }

        @Override
        public int compare(Field obj1, Field obj2) {
            return obj1.getName().compareTo(obj2.getName());
        }
    }
    // endregion
}
