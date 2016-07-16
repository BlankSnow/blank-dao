package com.blank.dao;

import java.lang.reflect.Field;
import java.util.Comparator;

public class BlankComparatorField implements Comparator<Field> {

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
