package com.blank.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BlankBaseDaoManager extends SQLiteOpenHelper {

    // region attributes
	private static final int DATABASE_DEFAULT_VERSION = 1;
	private static final String DATABASE_SELECTED = "BLANK_DATABASE_SELECTED";
	
	public static final String DATABASE_START = "BLANK ";
	public static final String DATABASE_TYPE = ".db";

	public Context context;
    // endregion

	// region constructors
	protected BlankBaseDaoManager(Context context, Integer version) {
		this(context, getCurrentDatabaseName(context) , null, getDatabaseVersion(context, version));
	}

	private BlankBaseDaoManager(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);
		//super(context, BlankUtils.getDefaultDatabaseFolder(context) + File.separator + name, factory, version);
		this.context = context;
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
	// endregion

	// region utils methods
	public static String getCurrentDatabaseName(Context context) {
		return BlankPreferences.getString(context, DATABASE_SELECTED, context.getString(context.getApplicationInfo().labelRes) + DATABASE_TYPE);
	}

	public static void setCurrentDatabaseName(Context context, String name) {
		BlankPreferences.setString(context, DATABASE_SELECTED, formatDatabaseName(name));
	}
	
	public static void deleteDatabase(Context context, String name) {
		String dbName = formatDatabaseName(name);
		if (!context.deleteDatabase(dbName)) {
			context.deleteDatabase(dbName.replace(":", "_"));
		}
	}
	
	public static String formatDatabaseName(String name) {
		if (!name.startsWith(DATABASE_START)) {
			name = DATABASE_START + name;
		}
		if (!name.endsWith(DATABASE_TYPE)) {
			name += DATABASE_TYPE;
		}
		
		return name;
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

	private static int getDatabaseVersion(Context context, Integer version) {
		if (version == null || version < DATABASE_DEFAULT_VERSION) {
			version = DATABASE_DEFAULT_VERSION;
		}
		return version;
	}
    // endregion

    // region methods CRUD database
    protected void onChange(SQLiteDatabase db) {
        List<BlankBaseDaoObject> deletedData = deleteAllTables(db);
        createAllTables(db);
        BlankDao.saveOrUpdate(db, deletedData);
    }

	protected void createAllTables(SQLiteDatabase db) {
		for (BlankBaseDaoObject obj: getAllTableObjects()) {
			db.execSQL(BlankDao.getQueryCreateTable(obj));
		}
	}

	protected List<BlankBaseDaoObject> deleteAllTables(SQLiteDatabase db) {
		List<BlankBaseDaoObject> deletedData = new ArrayList<>();
		
		for (BlankBaseDaoObject obj: getAllTableObjects()) {
			deletedData.addAll(BlankDao.getAll(db, obj));
			db.execSQL(BlankDao.getQueryDeleteTable(obj));
		}
		
		return deletedData;
	}
    // endregion
	
	// region abstract methods
	protected abstract List<BlankBaseDaoObject> getAllTableObjects();
    // endregion
}
