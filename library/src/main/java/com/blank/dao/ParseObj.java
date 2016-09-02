package com.blank.dao;

public class ParseObj {

	/**
	 * Method that converts an object to string.
	 */
	public static String toString(Object object) {
		if (object == null) {
			return "";
		} else if (object instanceof Boolean) {
			return String.valueOf((Boolean) object ? "1" : "0");
		} else {
			return object.toString();
		}
	}

	/**
	 * Method that converts an object to integer.
	 */
	public static Integer toInteger(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Boolean) {
			return Integer.valueOf((Boolean) object ? 1 : 0);
		} else if (object instanceof Integer) {
			return (Integer) object;
		} else if (object instanceof Long) {
			return ((Long) object).intValue();
		} else if (object instanceof Float) {
			return (Float.valueOf((Float) object + 0.5f)).intValue();
		} else if (object instanceof Double) {
			return (Double.valueOf((Double) object + 0.5)).intValue();
		} else {
			try {
				return Integer.valueOf(object.toString());
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	/**
	 * Method that converts an object to long.
	 */
	public static Long toLong(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Boolean) {
			return Long.valueOf((Boolean) object ? 1 : 0);
		} else if (object instanceof Integer) {
			return ((Integer) object).longValue();
		} else if (object instanceof Long) {
			return (Long) object;
		} else if (object instanceof Float) {
			return (Float.valueOf((Float) object + 0.5f)).longValue();
		} else if (object instanceof Double) {
			return (Double.valueOf((Double) object + 0.5)).longValue();
		} else {
			try {
				return Long.valueOf(object.toString());
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}
	
	/**
	 * Method that converts an object to float.
	 */
	public static Float toFloat(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Boolean) {
			return Float.valueOf((Boolean) object ? 1 : 0);
		} else if (object instanceof Integer) {
			return ((Float) object).floatValue();
		} else if (object instanceof Long) {
			return ((Long) object).floatValue();
		} else if (object instanceof Float) {
			return (Float) object;
		} else if (object instanceof Double) {
			return ((Double) object).floatValue();
		} else {
			try {
				return Float.valueOf(object.toString());
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}
	
	/**
	 * Method that converts an object to double.
	 */
	public static Double toDouble(Object object) {
		if (object == null) {
			return null;
		} else if (object instanceof Boolean) {
			return Double.valueOf((Boolean) object ? 1 : 0);
		} else if (object instanceof Integer) {
			return Double.valueOf(object.toString());
		} else if (object instanceof Long) {
			return ((Long) object).doubleValue();
		} else if (object instanceof Float) {
			return ((Float) object).doubleValue();
		} else if (object instanceof Double) {
			return (Double) object;
		} else {
			try {
				return Double.valueOf(object.toString());
			} catch (NumberFormatException e) {
				return null;
			}
		}
	}

	/**
	 * Method that concerts an object to boolean.
	 */
	public static Boolean toBoolean(Object object) {
		if (object == null) {
			return Boolean.FALSE;
		} else if (object instanceof Boolean) {
			return (Boolean) object;
		} else if (object instanceof Integer) {
			return ((Integer) object).intValue() == 1;
		} else if (object instanceof Long) {
			return ((Long) object).intValue() == 1;
		} else if (object instanceof Float) {
			return ((Float) object).intValue() == 1;
		} else if (object instanceof Double) {
			return ((Double) object).intValue() == 1;
		} else {
			return Boolean.FALSE;
		}
	}

	/**
	 * Method that return true if one string is null or empty.
	 */
	public static Boolean isNullOrEmpty(String text) {
		return text == null || text.isEmpty();
	}
}
