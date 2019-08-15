package com.wuerth.phoenix.cis.university.example1.adapters;

import com.wuerth.phoenix.cis.university.example1.types.DataScenarioType;

public interface IData {

	public ICompany getCompany();
	public IProfitCenter getProfitCenter();
	public ICRComponent getCRComponent();
	public boolean isExtern();
	
	public DataScenarioType getScenario();
	
	public IAccount getAccount();
	public String getParternCode();
	public String getCurrencyCode();
	
	public int getYear();
	public double[] getData();
	
	public void setAccount(IAccount account);
	public void setProfitCenter(IProfitCenter profitCenter);
	public void setCRComponent(ICRComponent crComponent);
	public void setExternal(boolean external);
	public void setDataScenarioType(DataScenarioType scenarioType);
	public void setParternCode(String partnerCode);
	public void setCurrencyCode(String currencyCode);
}
