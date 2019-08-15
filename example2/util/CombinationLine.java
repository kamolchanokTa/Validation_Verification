package com.wuerth.phoenix.cis.university.example2.util;

import java.util.HashMap;

import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ImportAssignmentType;

/**
 * Adapter for a Line of an Import File
 */
public class CombinationLine {

	private HashMap<IFRS16ImportAssignmentType, CombinationItem<?>> dataMap = new HashMap<>();

	
	public HashMap<IFRS16ImportAssignmentType, CombinationItem<?>> getDataMap() {
		return dataMap;
	}
}
