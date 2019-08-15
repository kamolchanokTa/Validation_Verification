package com.wuerth.phoenix.cis.university.example1.input;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import com.wuerth.phoenix.cis.university.example1.adapters.*;
import com.wuerth.phoenix.cis.university.example1.concretes.*;
import com.wuerth.phoenix.cis.university.example1.types.*;

public class InputGenerator {
	private static CSVReader csvReader = new CSVReader();
	
	private static final String INVALID_COMBINATIONS_RESULT = "invalid_combinations.csv";
	private static final String VALID_COMBINATIONS_RESULT = "valid_combinations.csv";
	
	private static Rules rules = new Rules();


	public static void main(String [ ] args) {
		generateData();
	}
	
	public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
	    Set<Object> seen = ConcurrentHashMap.newKeySet();
	    return t -> seen.add(keyExtractor.apply(t));
	}

	public static void generateData() {
		// Read all the valid accounts

		BufferedWriter bwValid = null;
		FileWriter fwValid = null;
		BufferedWriter bwInvalid = null;
		FileWriter fwInvalid = null;

		try {

			File fileValid = new File(VALID_COMBINATIONS_RESULT);
			File fileInvalid = new File(INVALID_COMBINATIONS_RESULT);

			if (!fileValid.exists()) {
				fileValid.createNewFile();
			}

			if (!fileInvalid.exists()) {
				fileInvalid.createNewFile();
			}

			fwValid = new FileWriter(fileValid.getAbsoluteFile(), false);
			bwValid = new BufferedWriter(fwValid);

			fwInvalid = new FileWriter(fileInvalid.getAbsoluteFile(), false);
			bwInvalid = new BufferedWriter(fwInvalid);
			ArrayList<IData> validDatas;
			ArrayList<IData> invalidDatas;
			// Loop over all possible combinations of  accountClass
			for (AccountClass accountClass : AccountClass.values()) {
				validDatas = new ArrayList<IData>();
				invalidDatas = new ArrayList<IData>();
				// get valid and invalid combination for each accountClass
				switch(accountClass){
					case SalesReporting:
						validDatas = generateValidSalesReporting();
						invalidDatas = generateInvalidSalesReporting();
						break;
					case PLStatement:
						validDatas = generateValidPLStatementData();
						invalidDatas = generateInvalidPLStatementData();
						break;
					case AllocationFormula:
						validDatas = generateValidAllocationFormulaData();
						invalidDatas = generateInvalidAllocationFormulaData();
						break;
					case BalanceSheet:
						validDatas = generateValidBalanceSheetData();
						invalidDatas = generateInvalidBalanceSheetData();
						break;
					case Logistics:
						validDatas = generateValidLogisticsData();
						invalidDatas = generateInvalidLogisticsData();
						break;
				}
				for(IData validData: validDatas) {
					String dataLine ="";
					dataLine = csvReader.combineDataCSV(validData); 
					bwValid.write(dataLine);
				}
				for(IData invalidData: invalidDatas) {
					String dataLine ="";
					dataLine = csvReader.combineDataCSV(invalidData); 
					bwInvalid.write(dataLine);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();

		} finally {

			try {

				if (bwValid != null)
					bwValid.close();

				if (fwValid != null)
					fwValid.close();

				if (bwInvalid != null)
					bwInvalid.close();

				if (fwInvalid != null)
					fwInvalid.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
	
	private static ArrayList<IData> generateInvalidPLStatementData() {
		ArrayList<IData> validDatas = transformMaptoData(rules.invalidPLStatements);
		return validDatas;
	}

	private static ArrayList<IData> generateValidPLStatementData() {
		ArrayList<IData> invalidDatas = transformMaptoData(rules.validPLStatements);
		return invalidDatas;
	}
	
	private static ArrayList<IData> generateValidSalesReporting() {
		ArrayList<IData> invalidDatas = transformMaptoData(rules.validSalesReportings);
		return invalidDatas;
	}
	
	private static ArrayList<IData> generateInvalidSalesReporting() {
		ArrayList<IData> invalidDatas = transformMaptoData(rules.invalidSalesReportings);
		return invalidDatas;
	}

	private static ArrayList<IData> transformMaptoData(ArrayList<Map<String, String>>mapDatas) {
		ArrayList<IData> datas = new ArrayList<IData>();
		for(Map<String, String> mapdata: mapDatas) {
			IData data = new ConcreteData();
			IAccount account = new ConcreteAccount(AccountClass.valueOf(mapdata.get("accountClass")),AccountType.valueOf(mapdata.get("accountType")),Boolean.parseBoolean(mapdata.get("isPartnerAllowed")));
			ICRComponent crComponent = new ConcreteCRComponent("WP",
					Boolean.parseBoolean(mapdata.get("isCRNotAllocated")),
					Boolean.parseBoolean(mapdata.get("isVKAllowed")),
					Boolean.parseBoolean(mapdata.get("isSEANAllowed")));
			IProfitCenter profitCenter = new ConcreteProfitCenter("WP",Boolean.parseBoolean(mapdata.get("isPCNotAllocated")));
			
			// set valid data 
			data.setAccount(account);
			data.setDataScenarioType(DataScenarioType.valueOf(mapdata.get("dataScenarioType")));
			data.setCRComponent(crComponent);
			data.setCurrencyCode(mapdata.get("currencyCode"));
			data.setParternCode(mapdata.get("partnerCode"));
			data.setExternal(Boolean.parseBoolean(mapdata.get("isExternal")));
			data.setProfitCenter(profitCenter);
			datas.add(data);
		}
		return datas;
	}
	
	private static ArrayList<IData> generateValidBalanceSheetData() {
		ArrayList<IData> validDatas = transformMaptoData(rules.validBalanceSheets);
		return validDatas;
	}

	private static ArrayList<IData> generateInvalidBalanceSheetData() {
		ArrayList<IData> invalidDatas = transformMaptoData(rules.invalidBalanceSheets);
		return invalidDatas;
	}

	private static ArrayList<IData> generateInvalidLogisticsData() {
		ArrayList<IData> invalidDatas = transformMaptoData(rules.invalidLogistics);
		return invalidDatas;
	}

	private static ArrayList<IData> generateValidLogisticsData() {
		ArrayList<IData> validDatas = transformMaptoData(rules.validLogistics);
		return validDatas;
	}

	/**
	 * Generate valid combination of AllocationFormula depending on the rules 
	 * @return ArrayList of concrete data extracted from rule of valid AllocationFormula
	 */
	public static ArrayList<IData> generateValidAllocationFormulaData() {
		ArrayList<IData> validDatas = new ArrayList<IData>();
		for(DataScenarioType scenarioType: rules.getValidScenarioTypesAllocationFormula()) {
			IData validData = new ConcreteData();
			IAccount account = new ConcreteAccount(AccountClass.AllocationFormula,AccountType.Empty,false);
			ICRComponent crComponent = new ConcreteCRComponent("WP",false,false,false);
			IProfitCenter profitCenter = new ConcreteProfitCenter("WP",false);
			
			// set valid data 
			validData.setAccount(account);
			validData.setDataScenarioType(scenarioType);
			validData.setCRComponent(crComponent);
			validData.setCurrencyCode("");
			validData.setParternCode("");
			validData.setExternal(false);
			validData.setProfitCenter(profitCenter);
			validDatas.add(validData);
		}
		return validDatas;
	}
	
	/**
	 * Generate invalid combination of AllocationFormula depending on rules 
	 * @return ArrayList of concrete data extracted from rule of invalid AllocationFormula
	 */
	public static ArrayList<IData> generateInvalidAllocationFormulaData() {
		ArrayList<IData> invalidData = new ArrayList<IData>();
		for(DataScenarioType scenarioType: rules.getInvalidScenarioTypesAllocationFormula()) {
			IData data = new ConcreteData();
			IAccount account = new ConcreteAccount(AccountClass.AllocationFormula,AccountType.Empty,false);
			ICRComponent crComponent = new ConcreteCRComponent("WP",false,false,false);
			IProfitCenter profitCenter = new ConcreteProfitCenter("WP",false);
			
			// set valid data 
			data.setAccount(account);
			data.setDataScenarioType(scenarioType);
			data.setCRComponent(crComponent);
			data.setCurrencyCode("");
			data.setParternCode("");
			data.setExternal(false);
			data.setProfitCenter(profitCenter);
			invalidData.add(data);
		}
		return invalidData;
	}
}
