package com.blank.dao;

import android.content.Context;
import android.content.SharedPreferences;

public class DaoPreferences {

	/**
	 * Method that save an int preference of application.
	 */
	public static void setInt(Context context, String preference, int value) {
		String sharedPrefName = context.getString(context.getApplicationInfo().labelRes);
		SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putInt(preference, value);
		editor.apply();
	}
	
	/**
	 * Method that returns a int shared preference of application.
	 */
	public static int getInt(Context context, String preference, int defaultValue) {
		String sharedPrefName = context.getString(context.getApplicationInfo().labelRes);
		SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
		return sharedPreferences.getInt(preference, defaultValue);
	}
	
	/**
	 * Method that save an string preference of application.
	 */
	public static void setString(Context context, String preference, String value) {
		String sharedPrefName = context.getString(context.getApplicationInfo().labelRes);
		SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putString(preference, value);
		editor.apply();
	}
	
	/**
	 * Method that returns an string shared preference of application.
	 */
	public static String getString(Context context, String preference, String defaultValue) {
		String sharedPrefName = context.getString(context.getApplicationInfo().labelRes);
		SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
		return sharedPreferences.getString(preference, defaultValue);
	}
	
	/**
	 * Method that save a boolean preference of application.
	 */
	public static void setBoolean(Context context, String preference, Boolean value) {
		String sharedPrefName = context.getString(context.getApplicationInfo().labelRes);
		SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = sharedPreferences.edit();
		editor.putBoolean(preference, value);
		editor.apply();
	}
	
	/**
	 * Method that returns a boolean shared preference of application.
	 */
	public static Boolean getBoolean(Context context, String preference, Boolean defaultValue) {
		String sharedPrefName = context.getString(context.getApplicationInfo().labelRes);
		SharedPreferences sharedPreferences = context.getSharedPreferences(sharedPrefName, Context.MODE_PRIVATE);
		return sharedPreferences.getBoolean(preference, defaultValue);
	}
}
