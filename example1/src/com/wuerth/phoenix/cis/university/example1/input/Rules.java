package com.wuerth.phoenix.cis.university.example1.input;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.wuerth.phoenix.cis.university.example1.types.*;

public class Rules {

	public ArrayList<Boolean> isPartnerAlloweds = new ArrayList<Boolean>();
	public ArrayList<Boolean> isPCNotAllocateds= new  ArrayList<Boolean>();
	public ArrayList<Boolean> isCRNotAllocateds= new  ArrayList<Boolean>();
	public ArrayList<Boolean> isVKAlloweds= new  ArrayList<Boolean>();
	public ArrayList<Boolean> isSEANAlloweds= new  ArrayList<Boolean>();
	public ArrayList<Boolean> isExternals= new  ArrayList<Boolean>();
	public ArrayList<String> currencyCodes= new  ArrayList<String>();
	public ArrayList<String> partnerCodes= new  ArrayList<String>();
	public ArrayList<Map<String, String>>validLogistics = new ArrayList<Map<String, String>>();
	public ArrayList<Map<String, String>>invalidLogistics = new ArrayList<Map<String, String>>();
	public ArrayList<Map<String, String>>validBalanceSheets = new ArrayList<Map<String, String>>();
	public ArrayList<Map<String, String>>invalidBalanceSheets = new ArrayList<Map<String, String>>();
	public ArrayList<DataScenarioType> validDataScenarioTypesAllocationFormula;
	public ArrayList<DataScenarioType> invalidDataScenarioTypesAllocationFormula;
	public ArrayList<Map<String, String>> validPLStatements = new ArrayList<Map<String, String>>();
	public ArrayList<Map<String, String>> invalidPLStatements = new ArrayList<Map<String, String>>();
	public ArrayList<Map<String, String>> validSalesReportings = new ArrayList<Map<String, String>>();;
	public ArrayList<Map<String, String>> invalidSalesReportings = new ArrayList<Map<String, String>>();;
	
	public Rules() {
		super();
		setValueofColumns();
		setValidScenarioTypesAllocationFormula();
		setInvalidScenarioTypesAllocationFormula();
		setLogistic();
		setBalanceSheet();
		setPLStatements();
		setSalesReporting();
	}
	
	private void setPLStatements() {
		ArrayList<AccountType> selectedAccountType = new ArrayList<AccountType>();
		selectedAccountType.add(AccountType.PrognosisNumOfAdmDecember);
		selectedAccountType.add(AccountType.PrognosisSales);
		selectedAccountType.add(AccountType.Empty);
		ArrayList<DataScenarioType> selectedDataScenarioType = new ArrayList<DataScenarioType>();
		selectedDataScenarioType.add(DataScenarioType.Actual);
		for(AccountType accountType: selectedAccountType) {
			for(boolean isPartnerAllowed: isPartnerAlloweds) {
				for(boolean isPCNotAllocated: isPCNotAllocateds) {
					for(boolean isCRNotAllocated: isCRNotAllocateds) { 
						for(boolean isVKAllowed: isVKAlloweds) { 
							for(boolean isSEANAllowed: isSEANAlloweds) { 
								for(boolean isExternal: isExternals) {
									for(DataScenarioType dataScenarioType: selectedDataScenarioType) {
										for(String partnerCode: partnerCodes) {
										for(String currencyCode: currencyCodes) {
											Map<String, String> data= new HashMap<String, String>();
											data.put("accountClass", AccountClass.PLStatement.toString());
											data.put("accountType",String.valueOf(accountType));
											data.put("isPartnerAllowed",String.valueOf(isPartnerAllowed));
											data.put("PCName","PCName");
											data.put("isPCNotAllocated",String.valueOf(isPCNotAllocated));
											data.put("CRName","CRName");
											data.put("isCRNotAllocated",String.valueOf(isCRNotAllocated));
											data.put("isVKAllowed",String.valueOf(isVKAllowed));
											data.put("isSEANAllowed",String.valueOf(isSEANAllowed));
											data.put("isExternal",String.valueOf(isExternal));
											data.put("dataScenarioType",String.valueOf(dataScenarioType));
											data.put("partnerCode",partnerCode);
											data.put("currencyCode",currencyCode);
											if(!dataScenarioType.equals(DataScenarioType.Actual)) {
												 invalidPLStatements.add(data);
											}
											if(accountType.equals(AccountType.PrognosisNumOfAdmDecember) || accountType.equals(AccountType.PrognosisSales)){
												if(!isPCNotAllocated || !isCRNotAllocated || !partnerCode.isEmpty() || !currencyCode.isEmpty()) {
													invalidPLStatements.add(data);
												}
												else if(accountType.equals(AccountType.PrognosisNumOfAdmDecember) ) {
													if(!isExternal) {
														invalidPLStatements.add(data);
													}
													else {
														validPLStatements.add(data);
													}
												}
												else {
													validPLStatements.add(data);
												}
											}
											else {
												if(!partnerCode.isEmpty()) {
													if(!dataScenarioType.equals(DataScenarioType.Actual) || !isPCNotAllocated || !isCRNotAllocated || !isExternal || !isPartnerAllowed || currencyCode.isEmpty()){
														 invalidPLStatements.add(data);
													}
													else {
														validPLStatements.add(data);
													}
												} else {
													if(!currencyCode.isEmpty()) {
														invalidPLStatements.add(data);
													}
													else {
														 validPLStatements.add(data);
													}
												}
											}
										}
											
										}
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("validPLStatements: " + validPLStatements.size());
		System.out.println("invalidPLStatements: " + invalidPLStatements.size());
		
	}
	
	private void setSalesReporting() {
		String partnerCode = "";
		currencyCodes.remove(1);
		ArrayList<AccountType> selectedAccountType = new ArrayList<AccountType>();
		selectedAccountType.add(AccountType.VK);
		selectedAccountType.add(AccountType.SEAN);
		selectedAccountType.add(AccountType.BranchOffice);
		selectedAccountType.add(AccountType.SpecialAnalyses);
		selectedAccountType.add(AccountType.SMLPotential);
		selectedAccountType.add(AccountType.SMLGrossProfit);
		selectedAccountType.add(AccountType.Customer);
		ArrayList<DataScenarioType> selectedDataScenarioType = new ArrayList<DataScenarioType>();
		selectedDataScenarioType.add(DataScenarioType.Actual);
		selectedDataScenarioType.add(DataScenarioType.Deferral);
		selectedDataScenarioType.add(DataScenarioType.Extrapolation);
		selectedDataScenarioType.add(DataScenarioType.Target);
		selectedDataScenarioType.add(DataScenarioType.Plan);
		for(AccountType accountType: selectedAccountType) {
			for(boolean isPartnerAllowed: isPartnerAlloweds) {
				for(boolean isPCNotAllocated: isPCNotAllocateds) {
					for(boolean isCRNotAllocated: isCRNotAllocateds) { 
						for(boolean isVKAllowed: isVKAlloweds) { 
							for(boolean isSEANAllowed: isSEANAlloweds) { 
								for(boolean isExternal: isExternals) {
									for(DataScenarioType dataScenarioType: selectedDataScenarioType) {
										for(String currencyCode: currencyCodes) {
											Map<String, String> data= new HashMap<String, String>();
											data.put("accountClass", AccountClass.SalesReporting.toString());
											data.put("accountType",String.valueOf(accountType));
											data.put("isPartnerAllowed",String.valueOf(isPartnerAllowed));
											data.put("PCName","PCName");
											data.put("isPCNotAllocated",String.valueOf(isPCNotAllocated));
											data.put("CRName","CRName");
											data.put("isCRNotAllocated",String.valueOf(isCRNotAllocated));
											data.put("isVKAllowed",String.valueOf(isVKAllowed));
											data.put("isSEANAllowed",String.valueOf(isSEANAllowed));
											data.put("isExternal",String.valueOf(isExternal));
											data.put("dataScenarioType",String.valueOf(dataScenarioType));
											data.put("partnerCode",partnerCode);
											data.put("currencyCode",currencyCode);
											boolean flag=true;
											if((!partnerCode.isEmpty() || !currencyCode.isEmpty())) {
												flag= false;
											}else {
												// Profit Center
												if(accountType.equals(AccountType.BranchOffice) || accountType.equals(AccountType.SpecialAnalyses) || accountType.equals(AccountType.VK) || accountType.equals(AccountType.Employees)) {
													if((accountType.equals(AccountType.BranchOffice) || accountType.equals(AccountType.SpecialAnalyses) || accountType.equals(AccountType.VK) ) && (!isPCNotAllocated || !isExternal)) {
														flag= false;
													}
													if(accountType.equals(AccountType.VK) && (!isCRNotAllocated || !isVKAllowed)) {
														flag= false;
													}
													if((accountType.equals(AccountType.BranchOffice) || accountType.equals(AccountType.SpecialAnalyses) || accountType.equals(AccountType.Employees)) && !isCRNotAllocated) {
														flag= false;
													}
												// C/R Component
												}else if(accountType.equals(AccountType.SEAN) && (!isCRNotAllocated || !isSEANAllowed)) {
													flag= false;
												}
												else if((accountType.equals(AccountType.SML) || accountType.equals(AccountType.Customer)) && isCRNotAllocated) {
													flag= false;
												// Internal/External
												}else if((accountType.equals(AccountType.BranchOffice) || accountType.equals(AccountType.SpecialAnalyses) || accountType.equals(AccountType.VK) && !isExternal)) {
													flag= false;
												} 
												// Scenario
												if(dataScenarioType.equals(DataScenarioType.Deferral ) && !accountType.equals(AccountType.BranchOffice)) {
													flag= false;
												}else if((dataScenarioType.equals(DataScenarioType.Extrapolation ) || dataScenarioType.equals(DataScenarioType.Target) || dataScenarioType.equals(DataScenarioType.Plan)) && (accountType.equals(AccountType.SEAN) || accountType.equals(AccountType.SpecialAnalyses) || accountType.equals(AccountType.SMLGrossProfit) || accountType.equals(AccountType.SMLPotential))) {
													flag= false;
												}
											}
											if(flag) {
												validSalesReportings.add(data);
											}
											else {
												invalidSalesReportings.add(data);
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("validSalesReportings: " + validSalesReportings.size());
		System.out.println("invalidSalesReportings: " + invalidSalesReportings.size());
		
	}

	public void setBalanceSheet() {
		ArrayList<AccountType> selectedAccountType = new ArrayList<AccountType>();
		selectedAccountType.add(AccountType.AssetPartner);
		selectedAccountType.add(AccountType.Empty);
		ArrayList<DataScenarioType> selectedDataScenarioType = new ArrayList<DataScenarioType>();
		selectedDataScenarioType.add(DataScenarioType.Actual);
		selectedDataScenarioType.add(DataScenarioType.Deferral);
		for(AccountType accountType: selectedAccountType) {
			for(boolean isPartnerAllowed: isPartnerAlloweds) {
				for(boolean isPCNotAllocated: isPCNotAllocateds) {
					for(boolean isCRNotAllocated: isCRNotAllocateds) { 
						for(boolean isVKAllowed: isVKAlloweds) { 
							for(boolean isSEANAllowed: isSEANAlloweds) { 
								for(boolean isExternal: isExternals) {
									for(DataScenarioType dataScenarioType: selectedDataScenarioType) {
										for(String currencyCode: currencyCodes) {
											Map<String, String> data= new HashMap<String, String>();
											data.put("accountClass", "BalanceSheet");
											data.put("accountType",String.valueOf(accountType));
											data.put("isPartnerAllowed",String.valueOf(isPartnerAllowed));
											data.put("PCName","PCName");
											data.put("isPCNotAllocated",String.valueOf(isPCNotAllocated));
											data.put("CRName","CRName");
											data.put("isCRNotAllocated",String.valueOf(isCRNotAllocated));
											data.put("isVKAllowed",String.valueOf(isVKAllowed));
											data.put("isSEANAllowed",String.valueOf(isSEANAllowed));
											data.put("isExternal",String.valueOf(isExternal));
											data.put("dataScenarioType",String.valueOf(dataScenarioType));
											data.put("partnerCode","partnerCode");
											data.put("currencyCode",currencyCode);
											if(!isCRNotAllocated || !isExternal) {
												invalidBalanceSheets.add(data);
											}
											else if(accountType.equals(AccountType.AssetPartner)) {
												validBalanceSheets.add(data);
											}
											else {
												if(!accountType.equals(AccountType.AssetPartner) &&  !isPCNotAllocated)
													invalidBalanceSheets.add(data);
												else if(currencyCode.isEmpty())
													invalidBalanceSheets.add(data);
												else if(!accountType.equals(AccountType.AssetPartner)  && dataScenarioType.equals(DataScenarioType.Deferral)) {
													invalidBalanceSheets.add(data);
												}
												else {
													validBalanceSheets.add(data);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("validBalanceSheets: " + validBalanceSheets.size());
		System.out.println("invalidBalanceSheets: " + invalidBalanceSheets.size());
	}
	public void setValueofColumns() {
		isPartnerAlloweds.add(true);
		isPartnerAlloweds.add(false);
		isPCNotAllocateds.add(true);
		isPCNotAllocateds.add(false);
		isCRNotAllocateds.add(true);
		isCRNotAllocateds.add(false);
		isVKAlloweds.add(true);
		isVKAlloweds.add(false);
		isSEANAlloweds.add(true);
		isSEANAlloweds.add(false);
		isExternals.add(true);
		isExternals.add(false);
		currencyCodes.add("");
		currencyCodes.add("currencyCode");
		partnerCodes.add("");
		partnerCodes.add("partnerCodes");
	}
	
	public void setLogistic() {
		for(boolean isPartnerAllowed: isPartnerAlloweds) {
			for(boolean isPCNotAllocated: isPCNotAllocateds) {
				for(boolean isCRNotAllocated: isCRNotAllocateds) { 
					for(boolean isVKAllowed: isVKAlloweds) { 
						for(boolean isSEANAllowed: isSEANAlloweds) { 
							for(boolean isExternal: isExternals) {
								for(DataScenarioType dataScenarioType: DataScenarioType.values()) { 
									Map<String, String> invalidLogistic= new HashMap<String, String>();
									invalidLogistic.put("accountClass", "Logistics");
									invalidLogistic.put("accountType","Empty");
									invalidLogistic.put("isPartnerAllowed",String.valueOf(isPartnerAllowed));
									invalidLogistic.put("PCName","WP");
									invalidLogistic.put("isPCNotAllocated",String.valueOf(isPCNotAllocated));
									invalidLogistic.put("CRName","WP");
									invalidLogistic.put("isCRNotAllocated",String.valueOf(isCRNotAllocated));
									invalidLogistic.put("isVKAllowed",String.valueOf(isVKAllowed));
									invalidLogistic.put("isSEANAllowed",String.valueOf(isSEANAllowed));
									invalidLogistic.put("isExternal",String.valueOf(isExternal));
									invalidLogistic.put("dataScenarioType",String.valueOf(dataScenarioType));
									invalidLogistic.put("partnerCode","");
									invalidLogistic.put("currencyCode","");
									if(isPCNotAllocated && isCRNotAllocated && isExternal && dataScenarioType.equals(DataScenarioType.Actual)) {
										validLogistics.add(invalidLogistic);
									}
									else {
										invalidLogistics.add(invalidLogistic);
									}
								}
							}
						}
					}
				}
			}
		}
		System.out.println("validLogistics: " + validLogistics.size());
		System.out.println("invalidLogistics: " + invalidLogistics.size());
	}
	public void setValidScenarioTypesAllocationFormula() {
		validDataScenarioTypesAllocationFormula = new ArrayList<DataScenarioType>();
		validDataScenarioTypesAllocationFormula.add(DataScenarioType.Actual);
	}
	
	public void setInvalidScenarioTypesAllocationFormula() {
		invalidDataScenarioTypesAllocationFormula = new ArrayList<DataScenarioType>();
		invalidDataScenarioTypesAllocationFormula.add(DataScenarioType.Deferral);
		invalidDataScenarioTypesAllocationFormula.add(DataScenarioType.Extrapolation);
	}
	public ArrayList<DataScenarioType> getValidScenarioTypesAllocationFormula() {
		return validDataScenarioTypesAllocationFormula;
	}
	public ArrayList<DataScenarioType> getInvalidScenarioTypesAllocationFormula() {
		return invalidDataScenarioTypesAllocationFormula;
	}
}
