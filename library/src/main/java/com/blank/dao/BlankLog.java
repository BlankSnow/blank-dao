package com.blank.dao;

import android.util.Log;

public class BlankLog {

    private static final String START_MARK = "@Start > ";
    private static final String END_MARK = "@End > ";
    private static final String PAD = ".";
    private static final String LINE = "#";
    private static final String PARAMETER_SEPARATOR = " ,";
    
    public static void error(Exception e) {
    	Log.e(getCurrentFileAndLine(), getCurrentMethodName(), e);
    }
    
    /**
     * Log the start of method.
     */
    public static void markMethodStart(Log logger, Object ... objects) {
        Log.d(getCurrentFileAndLine(), START_MARK + getCurrentMethodName() + markParams(objects));
    }
    
    /**
     * Log the end of method.
     */
    public static void markMethodEnd(Log logger, Object ... objects) {
    	Log.d(getCurrentFileAndLine(), END_MARK + getCurrentMethodName() + markParams(objects));
    }
    
    /**
     * Returns the file name and line from where this method is called.
     * @return Qualified method name
     */
    private static String getCurrentFileAndLine() {
        StackTraceElement stk = Thread.currentThread().getStackTrace()[2];
        return stk.getFileName() + LINE + stk.getLineNumber();
    }
    
    /**
     * Returns the full qualified class and method name of the calling method.
     * E.g. "com.blank.library.MyClass#myMethod"
     *
     * @return Qualified method name
     */
    private static String getCurrentMethodName() {
        StackTraceElement stk = Thread.currentThread().getStackTrace()[2];
        return stk.getClassName() + PAD + stk.getMethodName();
    }

    /**
     * Log the start of method with params.
     */
    private static String markParams(Object ... objects) {
        StringBuilder logText = new StringBuilder();
        logText.append("(");

        for (int i = 0; i < objects.length; i++) {
            if (i > 0) {
                logText.append(PARAMETER_SEPARATOR);
            }
            logText.append(toString(objects[i]));
        }

        // Put close method parentheses: ')'
        logText.append(")");
        
        return logText.toString();
    }

    /**
     * Method that returns the string "null" when the object is null.
     */
    private static String toString(Object obj) {
        return String.valueOf(obj);
    }
}