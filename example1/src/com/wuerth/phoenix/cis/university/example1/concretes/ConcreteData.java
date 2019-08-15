package com.wuerth.phoenix.cis.university.example1.concretes;

import com.wuerth.phoenix.cis.university.example1.adapters.IAccount;
import com.wuerth.phoenix.cis.university.example1.adapters.ICRComponent;
import com.wuerth.phoenix.cis.university.example1.adapters.ICompany;
import com.wuerth.phoenix.cis.university.example1.adapters.IData;
import com.wuerth.phoenix.cis.university.example1.adapters.IProfitCenter;
import com.wuerth.phoenix.cis.university.example1.types.DataScenarioType;

public class ConcreteData implements IData {

	private ICompany company;
	private IAccount account;
	private IProfitCenter profitCenter;
	private ICRComponent crComponent;
	private boolean isExternal;
	private DataScenarioType dataScenarioType;
	private String partnerCode;
	private String currencyCode;
	private int year;
	
	public ConcreteData() {
		super();
		this.company = null;
		this.year = 2018;
	}
	public void setCompany(ICompany company) {
		this.company = company;
	}
	@Override
	public ICompany getCompany() {
		
		return company;
	}
	public void setProfitCenter(IProfitCenter profitCenter) {
		this.profitCenter = profitCenter;
	}
	@Override
	public IProfitCenter getProfitCenter() {
		
		return profitCenter;
	}
	public void setCRComponent(ICRComponent crComponent) {
		this.crComponent = crComponent;
	}
	@Override
	public ICRComponent getCRComponent() {
		
		return crComponent;
	}
	
	public void setExternal(boolean external) {
		this.isExternal = external;
	}
	@Override
	public boolean isExtern() {
		return isExternal;
	}
	
	public void setDataScenarioType(DataScenarioType scenarioType) {
		this.dataScenarioType = scenarioType;
	}
	@Override
	public DataScenarioType getScenario() {
		
		return dataScenarioType;
	}

	public void setAccount(IAccount account) {
		this.account = account;
	}
	@Override
	public IAccount getAccount() {
		
		return account;
	}
	public void setParternCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	@Override
	public String getParternCode() {
		
		return partnerCode;
	}
	public void setCurrencyCode(String currencyCode) {
		this.currencyCode = currencyCode;
	}
	@Override
	public String getCurrencyCode() {
		
		return currencyCode;
	}
	@Override
	public int getYear() {
		
		return year;
	}

	@Override
	public double[] getData() {
		
		return null;
	}

}
