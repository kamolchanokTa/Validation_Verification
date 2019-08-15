package com.wuerth.phoenix.cis.university.example1.adapters;

import com.wuerth.phoenix.cis.university.example1.types.AccountClass;
import com.wuerth.phoenix.cis.university.example1.types.AccountType;

public interface IAccount {

	public String getCode();
	
	public void setCode(String code);
	
	public AccountClass getAccountClass();
	
	public AccountType getAccountType();

	public boolean isPartnerAllowed();
}
