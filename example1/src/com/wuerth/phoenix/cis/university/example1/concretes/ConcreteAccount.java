package com.wuerth.phoenix.cis.university.example1.concretes;

import com.wuerth.phoenix.cis.university.example1.adapters.IAccount;
import com.wuerth.phoenix.cis.university.example1.types.AccountClass;
import com.wuerth.phoenix.cis.university.example1.types.AccountType;

public class ConcreteAccount implements IAccount {
	
	public String code;
	
	public AccountClass accountClass;
	
	public AccountType accountType;
	
	public boolean partnerAllowed;
	
	

	public ConcreteAccount(AccountClass accountClass, AccountType accountType, boolean partnerAllowed) {
		super();
		this.accountClass = accountClass;
		this.accountType = accountType;
		this.partnerAllowed = partnerAllowed;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public AccountClass getAccountClass() {
		return accountClass;
	}

	public void setAccountClass(AccountClass accountClass) {
		this.accountClass = accountClass;
	}

	public AccountType getAccountType() {
		return accountType;
	}

	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	public boolean isPartnerAllowed() {
		return partnerAllowed;
	}

	public void setPartnerAllowed(boolean partnerAllowed) {
		this.partnerAllowed = partnerAllowed;
	}

	
}
