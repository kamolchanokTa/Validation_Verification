package com.wuerth.phoenix.cis.university.example2.test.milk;

import java.util.ArrayList;

import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ImportAssignmentType;
import com.wuerth.phoenix.cis.university.example2.util.Util;


public class ReductionTest {

	public static void main(String[] args) {
		ArrayList<IFRS16ImportAssignmentType> allTypeList = new ArrayList<>();
			// Filter the available types
			boolean islongVersion = true;
			for(IFRS16ImportAssignmentType type : IFRS16ImportAssignmentType.getAllValues()) {
				if(!IFRS16ImportAssignmentType.NULL.equals(type)) {
					if(Util.isAvailable(type, islongVersion)) {
						allTypeList.add(type);
					}
				}
			}
		boolean[][] allCombinationsBitmap = ReductionHelper.createCombinations(allTypeList.size());
		System.out.println((long)allCombinationsBitmap.length);
		
		
		ArrayList<IFRS16ImportAssignmentType> currentTypeList = new ArrayList<IFRS16ImportAssignmentType>();
		currentTypeList.add(IFRS16ImportAssignmentType.CONTRACTNUMBER);
		currentTypeList.add(IFRS16ImportAssignmentType.CREDITORNAME);
		currentTypeList.add(IFRS16ImportAssignmentType.CREDITORNUMBER);
		currentTypeList.add(IFRS16ImportAssignmentType.DESIGNATIONLEASEDOBJECT);
		currentTypeList.add(IFRS16ImportAssignmentType.ENDDATEOFCONTRACT);
		currentTypeList.add(IFRS16ImportAssignmentType.PARTNERCOMPANY);
		currentTypeList.add(IFRS16ImportAssignmentType.STARTDATEOFCONTRACT);
		currentTypeList.add(IFRS16ImportAssignmentType.INTERESTRATE);
		currentTypeList.add(IFRS16ImportAssignmentType.PROBABLEENDOFCONTRACT);
		
		
		boolean[] typeListBitMap = ReductionHelper.generateBitmapForTypeList(allTypeList, currentTypeList) ;
		
		System.out.println(ReductionHelper.generateReducedBitMap(allCombinationsBitmap, typeListBitMap).length);
	}





}
