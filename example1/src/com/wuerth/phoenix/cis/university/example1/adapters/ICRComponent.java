package com.wuerth.phoenix.cis.university.example1.adapters;

public interface ICRComponent {

	public String getName();
	public boolean isNotAllocated();
	
	public boolean isVKAllowed(); //Constants.CORPORATE_CRC_VK_REPORTING
	public boolean isSEANAllowed(); //Constants.CORPORATE_CRC_SEAN
}
