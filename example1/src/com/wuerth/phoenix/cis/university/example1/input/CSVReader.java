package com.wuerth.phoenix.cis.university.example1.input;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import com.wuerth.phoenix.cis.university.example1.adapters.*;
import com.wuerth.phoenix.cis.university.example1.concretes.*;
import com.wuerth.phoenix.cis.university.example1.types.*;


public class CSVReader {
	
/**
 * Reads the given csv file
 * @param resourcePath 
 * @param lineLength numbers of column
 * @param accountClassPos Position of the accountClass column (from 0)
 * @param accountTypePos Position of the accountType column  (from 0)
 * @param isPartnerAllowedPos Position of the accountType column (from 0)
 * @return ArrayList of concrete Accounts extracted from csv file
 */
	public ArrayList<ConcreteAccount> readAccountCSV(String resourcePath, int lineLength, int accountClassPos, int accountTypePos, int isPartnerAllowedPos) {

		File file = new File(resourcePath);
		ArrayList<ConcreteAccount> accounts = new ArrayList<ConcreteAccount>();

		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			br.readLine();
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] account = line.split(cvsSplitBy);
				
				if(account.length == lineLength) {
					String isPartnerAllowedRaw = account[isPartnerAllowedPos];
					String accountClass = account[accountClassPos];
					String accountType = account[accountTypePos];

					boolean isValidLine = !accountClass.isEmpty() && (isPartnerAllowedRaw.equals("false") || isPartnerAllowedRaw.equals("true"));
					if(isValidLine) {
						boolean partnerAllowed = Boolean.parseBoolean(account[isPartnerAllowedPos].toLowerCase());
						accounts.add(new ConcreteAccount(AccountClass.valueOf(accountClass), accountType.equals("") ? AccountType.Empty :  AccountType.valueOf(accountType), partnerAllowed));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return accounts;
	}
	
	/**
	 * Reads the given csv file
	 * @param resourcePath 
	 * @param lineLength numbers of column
	 * @return ArrayList of concrete CRComonent extracted from csv file
	 */
		public ArrayList<ICRComponent> readCRCompoentCSV(String resourcePath, int lineLength) {
			File file = new File(resourcePath);
			ArrayList<ICRComponent> crComponents = new ArrayList<ICRComponent>();

			String cvsSplitBy = ",";
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				br.readLine();
				String line = null;
				while ((line = br.readLine()) != null) {
					// use comma as separator
					String[] readData = line.split(cvsSplitBy);
					
					if(readData.length == lineLength) {
						ICRComponent crComponent = new ConcreteCRComponent(readData[0],
								Boolean.parseBoolean(readData[1].toLowerCase()),
								Boolean.parseBoolean(readData[2].toLowerCase()),
								Boolean.parseBoolean(readData[3].toLowerCase()));
						crComponents.add(crComponent);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return crComponents;
		}
		
		/**
		 * Reads the given csv file
		 * @param resourcePath 
		 * @param lineLength numbers of column
		 * @return ArrayList of concrete ProfitCenter extracted from csv file
		 */
		public ArrayList<IProfitCenter> readProfitCenterCSV(String resourcePath, int lineLength) {
			File file = new File(resourcePath);
			ArrayList<IProfitCenter> profitCenters = new ArrayList<IProfitCenter>();

			String cvsSplitBy = ",";
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				br.readLine();
				String line = null;
				while ((line = br.readLine()) != null) {
					// use comma as separator
					String[] readData = line.split(cvsSplitBy);
					
					if(readData.length == lineLength) {
						IProfitCenter profitCenter = new ConcreteProfitCenter(readData[0], Boolean.parseBoolean(readData[1].toLowerCase()));
						profitCenters.add(profitCenter);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return profitCenters;
		}
		
		/**
		 * Reads the given csv file
		 * @param resourcePath 
		 * @param lineLength numbers of column
		 * @return ArrayList of concrete account extracted from csv file
		 */
		public ArrayList<IAccount> readAccountCSV(String resourcePath, int lineLength) {
			File file = new File(resourcePath);
			ArrayList<IAccount> accounts = new ArrayList<IAccount>();

			String cvsSplitBy = ",";
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				br.readLine();
				String line = null;
				while ((line = br.readLine()) != null) {
					// use comma as separator
					String[] readData = line.split(cvsSplitBy);
					if(readData.length == lineLength) {
						String isPartnerAllowedRaw = readData[3];
						String accountClass = readData[1];
						String accountType = readData[2];

						boolean isValidLine = !accountClass.isEmpty() && (isPartnerAllowedRaw.equals("false") || isPartnerAllowedRaw.equals("true"));
						if(isValidLine) {
							boolean partnerAllowed = Boolean.parseBoolean(isPartnerAllowedRaw.toLowerCase());
							IAccount account = new ConcreteAccount(AccountClass.valueOf(accountClass), accountType.equals("") ? AccountType.Empty :  AccountType.valueOf(accountType), partnerAllowed);
							account.setCode(readData[0].toString());
							accounts.add(account);
						}
						else {
							System.out.println("Account missing: " + readData[0].toString());
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return accounts;
		}
		
		/**
		 * Reads the given csv file
		 * @param resourcePath 
		 * @param lineLength numbers of column
		 * @return ArrayList of concrete data extracted from csv file
		 */
		public ArrayList<Rule> readRulesCSV(String resourcePath, int lineLength) {
			File file = new File(resourcePath);
			ArrayList<Rule> rules = new ArrayList<Rule>();

			String cvsSplitBy = ",";
			String listSplitBy =";";
			try (BufferedReader br = new BufferedReader(new FileReader(file))) {
				String line = null;
				while ((line = br.readLine()) != null) {
					// use comma as separator
					String[] readData = line.split(cvsSplitBy);
					
					if(readData.length == lineLength) {
						String[] profileCenterArray = readData[1].split(listSplitBy);
						ArrayList<String> profitCenters = new ArrayList<String>(Arrays.asList(profileCenterArray));
						
						String[] crComponentArray = readData[2].split(listSplitBy);
						ArrayList<String> CRComponents= new ArrayList<String>(Arrays.asList(crComponentArray));
						
						String[] isExternalArray = readData[3].split(listSplitBy);
						
						ArrayList<Boolean> IsExternals= new ArrayList<Boolean>();
						for (int i = 0; i < isExternalArray.length; i++) {
							IsExternals.add(Boolean.parseBoolean(isExternalArray[i]));
						}
						
						String[] dataTypesArray = readData[4].split(listSplitBy);
						ArrayList<String> DataScenarioTypes = new ArrayList<String>(Arrays.asList(dataTypesArray));
						
						Rule rule = new Rule(readData[0],profitCenters,CRComponents,IsExternals,DataScenarioTypes,readData[5], readData[6]);
						rules.add(rule);
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			return rules;
		}
	/**
	 * Reads the given csv file
	 * @param resourcePath 
	 * @param lineLength numbers of column
	 * @return ArrayList of concrete data extracted from csv file
	 */
	public ArrayList<IData> readCSV(String resourcePath, int lineLength) {
		File file = new File(resourcePath);
		ArrayList<IData> datas = new ArrayList<IData>();

		String cvsSplitBy = ",";
		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			String line = null;
			while ((line = br.readLine()) != null) {
				// use comma as separator
				String[] readData = line.split(cvsSplitBy);
				
				if(readData.length == lineLength) {
					
					IAccount account = new ConcreteAccount(AccountClass.valueOf(readData[1]),
							AccountType.valueOf(readData[0]),
							Boolean.parseBoolean(readData[2].toLowerCase()));
					IProfitCenter profitCenter = new ConcreteProfitCenter(readData[3], Boolean.parseBoolean(readData[4].toLowerCase()));
					ICRComponent crComponent = new ConcreteCRComponent(readData[5],
							Boolean.parseBoolean(readData[6].toLowerCase()),
							Boolean.parseBoolean(readData[7].toLowerCase()),
							Boolean.parseBoolean(readData[8].toLowerCase()));
					IData data = new ConcreteData();
					data.setAccount(account);
					data.setCRComponent(crComponent);
					data.setProfitCenter(profitCenter);
					data.setExternal(Boolean.parseBoolean(readData[9].toLowerCase()));
					data.setDataScenarioType(DataScenarioType.valueOf(readData[10]));
					data.setParternCode(readData[11]);
					data.setCurrencyCode(readData[12]);
					datas.add(data);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return datas;
	}
	
	/**
	 * Combines a line (account parameters) for the accounts class
	 * @param outputFile
	 * @param accountType
	 * @param accountClass
	 * @param isPartnerAllowed
	 * @throws IOException 
	 */
	public String combineAccountCSVLine(String accountType, String accountClass, String isPartnerAllowed) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(accountType);
        sb.append(',');
        sb.append(accountClass);
        sb.append(',');
        sb.append(isPartnerAllowed);
        sb.append('\n');
        return sb.toString();
	}
	/**
	 * Combines a line (IData parameters) for the data class
	 * * @param data
	 */
	public String combineDataCSV(IData data) throws IOException {
		String str =  combineDataCSVLine(data.getAccount().getAccountType().toString(),
				data.getAccount().getAccountClass().toString(),
				String.valueOf(data.getAccount().isPartnerAllowed()),
				data.getProfitCenter().getName().toString(),
				String.valueOf(data.getProfitCenter().isNotAllocated()),
				data.getCRComponent().getName().toString(),
				String.valueOf(data.getCRComponent().isNotAllocated()),
				String.valueOf(data.getCRComponent().isVKAllowed()),
				String.valueOf(data.getCRComponent().isSEANAllowed()),
				String.valueOf(data.isExtern()),
				String.valueOf(data.getScenario()),
				data.getParternCode(),
				data.getCurrencyCode(),
				String.valueOf(data.getYear()));
		return str;
	}
	
	/**
	 * Combines a line (IData parameters) for the data class
	 * @param accountType
	 * @param accountClass
	 * @param isPartnerAllowed
	 * @param PCName
	 * @param isPCNotAllocated
	 * @param CRName
	 * @param isCRNotAllocated
	 * @param isVKAllowed
	 * @param isSEANAllowed
	 * @param isExternal
	 * @param dataScenarioType
	 * @param partnerCode
	 * @param currencyCode
	 * @param year
	 * @throws IOException 
	 */
	public String combineDataCSVLine(String accountType, String accountClass, String isPartnerAllowed,
			String PCName,String isPCNotAllocated, String CRName, String isCRNotAllocated, String isVKAllowed,
			String isSEANAllowed, String isExternal, String dataScenarioType, String partnerCode,
			String currencyCode, String year) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append(accountType);
        sb.append(',');
        sb.append(accountClass);
        sb.append(',');
        sb.append(isPartnerAllowed);
        sb.append(',');
        sb.append(PCName);
        sb.append(',');
        sb.append(isPCNotAllocated);
        sb.append(',');
        sb.append(CRName);
        sb.append(',');
        sb.append(isCRNotAllocated);
        sb.append(',');
        sb.append(isVKAllowed);
        sb.append(',');
        sb.append(isSEANAllowed);
        sb.append(',');
        sb.append(isExternal);
        sb.append(',');
        sb.append(dataScenarioType);
        sb.append(',');
        sb.append(partnerCode);
        sb.append(',');
        sb.append(currencyCode);
        sb.append(',');
        sb.append(year);
        sb.append('\n');
        return sb.toString();
	}
	

}
