package com.wuerth.phoenix.cis.university.example1.input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.wuerth.phoenix.cis.university.example1.adapters.*;
import com.wuerth.phoenix.cis.university.example1.concretes.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example1.concretes.ConcreteData;
import com.wuerth.phoenix.cis.university.example1.types.AccountClass;
import com.wuerth.phoenix.cis.university.example1.types.AccountType;
import com.wuerth.phoenix.cis.university.example1.types.DataScenarioType;

public class InputGeneratorFromRulesDoc {
	private static boolean currentBoolValue = true;
	private static CSVReader csvReader = new CSVReader();
	private static final String VALID_COMBINATIONS_RESULT = "valid_combinations.csv";
	
	private static final String ACCOUNT_DATA = "Account.csv";
	private static final String CRCOMPONENT_DATA = "CRComponent.csv";
	private static final String PROFITCENTERT_DATA = "ProfitCenter.csv";
	private static final String RULES = "MergedRules.csv";
	
	private static ArrayList<IAccount> accounts_data;
	private static ArrayList<ICRComponent> crcomponent_data;
	private static ArrayList<IProfitCenter> profitcenter_data;
	private static ArrayList<Rule> rule_data;


	public static void main(String [ ] args) {
		readCSV();
		generateRule();
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}
	
	private  static void readCSV() {
		// Read all the csv files
		accounts_data = csvReader.readAccountCSV(ACCOUNT_DATA, 4);
		crcomponent_data = csvReader.readCRCompoentCSV(CRCOMPONENT_DATA, 4);
		profitcenter_data = csvReader.readProfitCenterCSV(PROFITCENTERT_DATA, 2);
		rule_data = csvReader.readRulesCSV(RULES, 7);
	}

	public static void generateRule() {
		
		BufferedWriter bwValid = null;
		FileWriter fwValid = null;
		System.out.print("accounts_data: " +accounts_data.size());
		System.out.print(" crcomponent_data: " +crcomponent_data.size());
		System.out.print(" profitcenter_data: " +profitcenter_data.size());
		System.out.print(" rule_data: " +rule_data.size());
		try {

			File fileValid = new File(VALID_COMBINATIONS_RESULT);

			if (!fileValid.exists()) {
				fileValid.createNewFile();
			}
			ArrayList<IData> datas = new ArrayList<IData>();
			for(Rule rule: rule_data ) {
				for(String cRComponent: rule.CRComponents) {
					for(String profitCenter: rule.ProfitCenters) {
						for(Boolean isExternal: rule.IsExternals) {
							for(String dataScenarioType: rule.DataScenarioTypes) {
								IData data = new ConcreteData();
								IAccount account = getAccount(rule.AccountName.trim());
								ICRComponent crComponent = getCRComponent(cRComponent.trim());
								IProfitCenter profitCenterConcrete = getProfitCenter(profitCenter.trim());
								String currencyCode = rule.Currency.equals("No") ? "": "currencyCode";
								String partnerCode = rule.Partner.equals("No") ? "" : "partnerCode";
								// set valid data 
								if(account == null || crComponent == null || profitCenterConcrete== null) {
									if(account == null) {
										System.out.println("Account:"+ rule.AccountName);
									}
									
									
								}else {
									
									data.setAccount(account);
									data.setDataScenarioType(DataScenarioType.valueOf(dataScenarioType.trim()));
									data.setCRComponent(crComponent);
									data.setCurrencyCode(currencyCode);
									data.setParternCode(partnerCode);
									data.setExternal(isExternal);
									data.setProfitCenter(profitCenterConcrete);
									if(isValid(data)) {
										datas.add(data);
									}
									//datas.add(data);
								}
								
							}
						}
					}
				}
			}
			
			fwValid = new FileWriter(fileValid.getAbsoluteFile(), false);
			bwValid = new BufferedWriter(fwValid);
			System.out.println(" DATA: " + datas.size());
			for(IData validData: datas) {
				String dataLine ="";
				dataLine = csvReader.combineDataCSV(validData); 
				bwValid.write(dataLine);
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {

				if (bwValid != null)
					bwValid.close();

				if (fwValid != null)
					fwValid.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}

	private static boolean isValid(IData data) {
		boolean flag= false;
		switch(data.getAccount().getAccountClass()) {
		case SalesReporting:
			flag = isValidSaleReporting(data);
			break;
		case PLStatement:
			flag= true;
			break;
		case AllocationFormula:
			flag= true;
			break;
		case BalanceSheet:
			flag= isValidBalanceSheet(data);
			//flag= true;
			break;
		case Logistics:
			flag= true;
			break;
		}
		return flag;
	}

	private static boolean isValidBalanceSheet(IData data) {
		
		if(!data.getParternCode().isEmpty()) {
			switch(data.getAccount().getAccountType()) {
			case AssetPartner:
				break;
			default:
				if(!data.getProfitCenter().isNotAllocated()) {
					return false;
				}
				break;
			}
		}
		
		// C/R Component
		if(!data.getCRComponent().isNotAllocated()) {
			return false;
		}

		// Internal/External
		if(!data.isExtern()) {
			return false;
		}

		// Scenario
		switch(data.getScenario()) {
		case Deferral:
			if(!data.getParternCode().isEmpty()) {
				switch(data.getAccount().getAccountType()) {
				case AssetPartner:
					break;
				default:
					return false;
				}
			}
			break;
		}
		
		// Currency
		if(!data.getParternCode().isEmpty()) {
			switch(data.getAccount().getAccountType()) {
			case AssetPartner:
				break;
			default:
				if(data.getCurrencyCode().isEmpty()) {
					return false;
				}
				break;
			}
		}
		else {
			if(!data.getCurrencyCode().isEmpty()) {
				return false;
			}
		}
		
		return true;
	}
	private static boolean isValidSaleReporting(IData data) {
		boolean flag=true;
		// Profit Center
					switch(data.getAccount().getAccountType()) {
					case BranchOffice:
					case SpecialAnalyses:
					case VK:
						if(!data.getProfitCenter().isNotAllocated()) {
							return false;
						}
						break;
					}
					
					// C/R Component
					switch(data.getAccount().getAccountType()) {
					case VK:
						if(!data.getCRComponent().isVKAllowed() || !data.getCRComponent().isNotAllocated()) {
							return false;
						}
						break;
					case SEAN:
						if(!data.getCRComponent().isSEANAllowed() || !data.getCRComponent().isNotAllocated()) {
							return false;
						}
						break;
					case BranchOffice:
					case SpecialAnalyses:
					case Employees:
						if(!data.getCRComponent().isNotAllocated()) {
							return false;
						}
						break;
					case SML:
					case Customer:
						if(data.getCRComponent().isNotAllocated()) {
							return false;
						}
						break;
					}
					
					// Internal/External
					switch(data.getAccount().getAccountType()) {
						case BranchOffice:
						case SpecialAnalyses:
						case VK:
							if(!data.isExtern()) {
								return false;
							}
							break;
						}
					
					// Scenario
					switch(data.getScenario()) {
					case Deferral:
						switch(data.getAccount().getAccountType()) {
						case BranchOffice:
							break;
						default:
							return false;
						}
					case Extrapolation:
					case Target:
					case Plan:
						switch(data.getAccount().getAccountType()) {
						case SEAN:
						case SpecialAnalyses:
						case SMLGrossProfit:
						case SMLPotential:
							return false;
						}
						break;
					}

					// Partner
					if(!data.getParternCode().isEmpty()) {
						return false;
					}
					
					// Currency
					if(!data.getCurrencyCode().isEmpty()) {
						return false;
					}
		return true;
		
	}
	private static IProfitCenter getProfitCenter(String profitCenter) {
		for(IProfitCenter profitcenter: profitcenter_data) {
			if(profitcenter.getName().equals(profitCenter)) {
				return profitcenter;
			}
		}
		return null;
	}

	private static ICRComponent getCRComponent(String cRComponent) {
		for(ICRComponent crcomponent: crcomponent_data) {
			if(crcomponent.getName().equals(cRComponent)) {
				return crcomponent;
			}
		}
		return null;
	}

	private static IAccount getAccount(String accountName) {
		if(accountName.equals("NDS_AF52100200")) {
			System.out.println("FUCK U");
		}
		for(IAccount account: accounts_data) {
			if(account.getCode().trim().equals(accountName)) {
				return account;
			}
		}

		return null;
		
	}
}
