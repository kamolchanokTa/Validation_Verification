package com.wuerth.phoenix.cis.university.example2.test.milk;

import java.util.ArrayList;

import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ImportAssignmentType;
import com.wuerth.phoenix.cis.university.example2.util.Util;

public class ReductionHelper {

	/**
	 * Transforms a given arrayList of <code>IFRS16ImportAssignmentType</code> into a boolean array
	 * that represents a line in the bitmap of all possible values.
	 * 
	 * E.g.:
	 * Type1 Type2 Type3 Type4
	 * true  false  true true
	 * 
	 * @param allTypeList
	 * @param typeList
	 * @return the bitmap line for the given list of <code>IFRS16ImportAssignmentType</code>.
	 */
	static boolean[] generateBitmapForTypeList(ArrayList<IFRS16ImportAssignmentType> allTypeList, ArrayList<IFRS16ImportAssignmentType> typeList) {
		boolean[] typeListBitMap = new boolean[allTypeList.size()];
		for(IFRS16ImportAssignmentType type: typeList ) {
			int index = allTypeList.indexOf(type);
			typeListBitMap[index] = true;
		}		
		return typeListBitMap;
	}

	/**
	 * @param allCombinationsBitmap
	 * @param typeListBitMap
	 * @return the bitmap containing all possible combinations reduced by the combinations that contain the <code>typeListBitMap</code>.
	 */
	static boolean[][] generateReducedBitMap(boolean[][] allCombinationsBitmap, boolean[] typeListBitMap) {

		ArrayList<boolean[]> reducedList = new ArrayList<>();
		for(int i = 0; i < allCombinationsBitmap.length; i++) {
			// Current combination is not already covered by previous combination -> add it
			if(!Util.isSubset(allCombinationsBitmap[i], typeListBitMap)) {
				reducedList.add(allCombinationsBitmap[i]);
			}
		}
		boolean[][] reducedBitmap = new boolean[reducedList.size()][allCombinationsBitmap[0].length];

		return reducedList.toArray(reducedBitmap);
	}

	// create a bitmap for types
	// 0 1 0  one means type is set
	static boolean[][] createCombinations(int size) {
		int count = Double.valueOf(Math.pow(2,size)).intValue();
		boolean[][] map = new boolean[count][size];
		for(int columnIndex=0; columnIndex<size; columnIndex++) {
			int module = Double.valueOf(Math.pow(2,columnIndex)).intValue();
			boolean value = false;
			for(int index=0; index<count; index++) {
				map[index][columnIndex] = value;
				if((index+1) % module == 0) {
					value = !value;
				}
			}
		}
		return map;
	}
	
	/* Return true if arr2[] is a subset of arr1[] */
    public static boolean isSubset(boolean arr1[], boolean arr2[])
    {
        for (int i=0; i<arr2.length; i++)
        {
        	if(arr2[i] && !arr1[i]) {
        		return false;
        	}
               
        }
          
        /* If we reach here then all elements of arr2[] 
          are present in arr1[] */
        return true;
    }
}
