package com.blank.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public abstract class DaoHelper extends SQLiteOpenHelper {

    // region attributes
	private static final int DATABASE_DEFAULT_VERSION = 1;
	private static final String DATABASE_SELECTED = "BLANK_DATABASE_SELECTED";
	
	public static final String DATABASE_START = "BLANK ";
	public static final String DATABASE_TYPE = ".db";

	private final Context context;
    // endregion

	// region constructor
	protected DaoHelper(Context context, int version) {
		super(context, getCurrentDatabaseName(context), null, getDatabaseVersion(version));
		this.context = context;
	}
    // endregion

	// region static utils methods
    public static String getDefaultDatabaseName(Context context) {
        return DATABASE_START + context.getString(context.getApplicationInfo().labelRes) + DATABASE_TYPE;
    }

	public static String getCurrentDatabaseName(Context context) {
		return DaoPreferences.getString(context, DATABASE_SELECTED, getDefaultDatabaseName(context));
	}

	public static void setCurrentDatabaseName(Context context, String name) {
		DaoPreferences.setString(context, DATABASE_SELECTED, formatDatabaseName(context, name));
	}

	private static String formatDatabaseName(Context context, String name) {
        if (name == null || name.isEmpty()) {
            name = getDefaultDatabaseName(context);
        }

		if (!name.startsWith(DATABASE_START)) {
			name = DATABASE_START + name;
		}
		if (!name.endsWith(DATABASE_TYPE)) {
			name += DATABASE_TYPE;
		}

		return name;
	}

    private static int getDatabaseVersion(int version) {
        if (version < DATABASE_DEFAULT_VERSION) {
            version = DATABASE_DEFAULT_VERSION;
        }
        return version;
    }

    public static void deleteDatabase(Context context, String name) {
        String dbName = formatDatabaseName(context, name);
        if (!context.deleteDatabase(dbName)) {
            context.deleteDatabase(dbName.replace(":", "_"));
        }
    }


    public static List<String> getDatabaseList(Context context) {
		String[] strDatabaseList = context.databaseList();
		
	    List<String> list = new ArrayList<>();
	    if (strDatabaseList != null) {
	    	for (String name : strDatabaseList) {
	    		if (name.startsWith(DATABASE_START) && name.endsWith(DATABASE_TYPE)) {
	    			list.add(name.substring(0, name.length() - DATABASE_TYPE.length()));
	    		}
	    	}
	    }
	    
	    return list;
	}
    // endregion

    // region override methods
    @Override
    public void onCreate(SQLiteDatabase db) {
        createAllTables(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onChange(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onChange(db);
    }

    protected void onChange(SQLiteDatabase db) {
        List<DaoBaseObject> deletedData = deleteAllTables(db);
        createAllTables(db);
        saveOrUpdate(db, deletedData);
    }

    protected void createAllTables(SQLiteDatabase db) {
        for (DaoBaseObject obj: getAllTableObjects()) {
            db.execSQL(getQueryCreateTable(obj));
        }
    }

    protected List<DaoBaseObject> deleteAllTables(SQLiteDatabase db) {
        List<DaoBaseObject> deletedData = new ArrayList<>();

        for (DaoBaseObject obj: getAllTableObjects()) {
            deletedData.addAll(getAll(db, obj));
            db.execSQL(getQueryDeleteTable(obj));
        }

        return deletedData;
    }
    // endregion

    // region get methods
    public Context getContext() {
        return context;
    }
    // endregion

    // region abstract methods
    protected abstract <T extends DaoBaseObject> void saveOrUpdate(SQLiteDatabase db, List<T> deletedData);

    protected abstract <T extends DaoBaseObject> String getQueryCreateTable(T obj);

    protected abstract <T extends DaoBaseObject> String getQueryDeleteTable(T obj);

    protected abstract <T extends DaoBaseObject> List<T> getAll(SQLiteDatabase db, T obj);

    protected abstract <T extends DaoBaseObject> List<T> getAllTableObjects();
    // endregion
}
