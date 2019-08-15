package com.wuerth.phoenix.cis.university.example1.concretes;

import com.wuerth.phoenix.cis.university.example1.adapters.ICRComponent;
import com.wuerth.phoenix.cis.university.example1.types.AccountClass;
import com.wuerth.phoenix.cis.university.example1.types.AccountType;

public class ConcreteCRComponent implements ICRComponent{

	private String name;
	private boolean isNotAllocated;
	private boolean isVKAllowed;
	private boolean isSEANAllowed;
	
	public ConcreteCRComponent(String name, boolean isNotAllocated, boolean isVKAllowed, boolean isSEANAllowed) {
		super();
		this.name = name;
		this.isNotAllocated = isNotAllocated;
		this.isVKAllowed = isVKAllowed;
		this.isSEANAllowed = isSEANAllowed;
	}
	@Override
	public String getName() {
		
		return name;
	}

	@Override
	public boolean isNotAllocated() {
		
		return isNotAllocated;
	}

	@Override
	public boolean isVKAllowed() {
		
		return isVKAllowed;
	}

	@Override
	public boolean isSEANAllowed() {
		
		return isSEANAllowed;
	}

}
