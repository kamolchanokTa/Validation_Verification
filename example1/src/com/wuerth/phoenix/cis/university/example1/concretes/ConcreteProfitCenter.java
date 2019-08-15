package com.wuerth.phoenix.cis.university.example1.concretes;

import com.wuerth.phoenix.cis.university.example1.adapters.IProfitCenter;
import com.wuerth.phoenix.cis.university.example1.types.AccountClass;
import com.wuerth.phoenix.cis.university.example1.types.AccountType;

public class ConcreteProfitCenter implements IProfitCenter {

	private String name;
	private boolean isNotAllocated;
	
	public ConcreteProfitCenter(String name, boolean isNotAllocated) {
		super();
		this.name = name;
		this.isNotAllocated = isNotAllocated;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isNotAllocated() {
		return isNotAllocated;
	}

}
