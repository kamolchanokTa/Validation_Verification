package com.wuerth.phoenix.cis.university.example1.input;

import java.util.ArrayList;

import com.wuerth.phoenix.cis.university.example1.types.AccountType;

public class Rule {
	
	public String AccountName;
	
	public ArrayList<String> ProfitCenters;
	
	public ArrayList<String> CRComponents;
	
	public ArrayList<Boolean> IsExternals;
	
	public ArrayList<String> DataScenarioTypes;
	
	public String Partner;
	
	public String Currency;
	
	public Rule(String accountName, ArrayList<String> profitCenters, ArrayList<String> cRComponents
			, ArrayList<Boolean> isExternals,ArrayList<String> dataScenarioTypes,
			String partner, String currency) {
		this.AccountName = accountName;
		this.CRComponents = cRComponents;
		this.Currency = currency;
		this.DataScenarioTypes = dataScenarioTypes;
		this.IsExternals = isExternals;
		this.Partner = partner;
		this.ProfitCenters = profitCenters;
	}
}
