package com.wuerth.phoenix.cis.university.example2.test.milk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import com.wuerth.phoenix.cis.university.example2.adapters.Company;
import com.wuerth.phoenix.cis.university.example2.adapters.CompanyType;
import com.wuerth.phoenix.cis.university.example2.adapters.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example2.adapters.DigitGroupingSymbol;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ConditionType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16Contract;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ImportAssignmentType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16PaymentCycle;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16PaymentDateType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16VATRateType;
import com.wuerth.phoenix.cis.university.example2.adapters.ImplementedCompany;
import com.wuerth.phoenix.cis.university.example2.test.IEngineTestData;
import com.wuerth.phoenix.cis.university.example2.util.Checker;
import com.wuerth.phoenix.cis.university.example2.util.Combination;
import com.wuerth.phoenix.cis.university.example2.util.CombinationItem;
import com.wuerth.phoenix.cis.university.example2.util.CombinationLine;
import com.wuerth.phoenix.cis.university.example2.util.Constants;
import com.wuerth.phoenix.cis.university.example2.util.Constants.IFRS16ImportCompanyDataColumnSectionType;
import com.wuerth.phoenix.cis.university.example2.util.Constants.IFRS16ImportCompanyDataErrorType;
import com.wuerth.phoenix.cis.university.example2.util.FileGenerator;
import com.wuerth.phoenix.cis.university.example2.util.Scenario;
import com.wuerth.phoenix.cis.university.example2.util.Settings;
import com.wuerth.phoenix.cis.university.example2.util.Util;

public abstract class EngineTestData implements IEngineTestData {

	private final static String COMPANY_CODE_IMPORT = "2407";
	private final static String[] COMPANY_CODES = new String[] {
			COMPANY_CODE_IMPORT,
			"1000",
			"8999"
	};
	private final int YEAR 	= 2018;
	private final int MONTH = 15;

	@Override
	public boolean start() {
		ArrayList<Company> companyList = createCompanyList();
		ArrayList<Settings> settingsList = createSettingsList();
		ObjectIterator<Settings> settingsIterator = new ObjectIterator<>(settingsList);

		ImplementedCompany implementedCompany = null;
		ArrayList<String> partnerCompanyCodeList = new ArrayList<>();
		for(Company company : companyList) {
			if(COMPANY_CODE_IMPORT.equals(company.getCode())) {
				implementedCompany = company.lookupImplementedCompany(CompanyType.PRODUCTIVE);
			}
			else {
				partnerCompanyCodeList.add(company.getCode());
			}
		}

		// Filter the available types
		ArrayList<IFRS16ImportAssignmentType> allTypeList = new ArrayList<>();
		boolean islongVersion = true;
		for(IFRS16ImportAssignmentType type : IFRS16ImportAssignmentType.getAllValues()) {
			if(!IFRS16ImportAssignmentType.NULL.equals(type)) {
				if(Util.isAvailable(type, islongVersion)) {
					allTypeList.add(type);
				}
			}
		}


		// Check the set combinations
		for(CombinationValueType combinationValueType: CombinationValueType.values()) {
			for(int i = 0; i <= 6; i++) {
				ArrayList<IFRS16ImportAssignmentType> currentTypeList = new ArrayList<IFRS16ImportAssignmentType>();
				switch(i) {
				case 1:
					currentTypeList.add(IFRS16ImportAssignmentType.CONTRACTNUMBER);
					currentTypeList.add(IFRS16ImportAssignmentType.CREDITORNAME);
					currentTypeList.add(IFRS16ImportAssignmentType.CREDITORNUMBER);
					currentTypeList.add(IFRS16ImportAssignmentType.DESIGNATIONLEASEDOBJECT);
					currentTypeList.add(IFRS16ImportAssignmentType.ENDDATEOFCONTRACT);
					currentTypeList.add(IFRS16ImportAssignmentType.PARTNERCOMPANY);
					currentTypeList.add(IFRS16ImportAssignmentType.STARTDATEOFCONTRACT);
					currentTypeList.add(IFRS16ImportAssignmentType.INTERESTRATE);
					currentTypeList.add(IFRS16ImportAssignmentType.PROBABLEENDOFCONTRACT);
				}

				if(!new CheckExecutor() {

					@Override
					protected Combination createCombination(Settings settings) {
						return createSingleCombination(currentTypeList, combinationValueType);

					}
				}.execute("Single Combination Exec: " + i + "-" + combinationValueType, companyList, implementedCompany, true, settingsIterator.next(), currentTypeList)) {
					return true;
				}

				// remove columns that have been covered
				allTypeList.removeAll(currentTypeList);

			}	
		}


		System.out.println(allTypeList.size());
		if(Util.isValid(true, allTypeList)) {
			for(int i=0; i < settingsList.size(); i++) {
				if(!new CheckExecutor() {

					@Override
					protected Combination createCombination(Settings settings) {
						return EngineTestData.this.createCombinationPairWise(settings, partnerCompanyCodeList);
					}
				}.execute("Valid", companyList, implementedCompany, true, settingsIterator.next(), allTypeList)) {
					return false;
				}	
			}
		}
		return true;

	}

	private Combination createCombinationPairWise(Settings settings, ArrayList<String> partnerCompanyCodeList) {

		boolean hasGroupPositionAndConditionType = true;

		ArrayList<ConcreteAccount> concreteAccountList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.GROUPPOSITION)) {
			concreteAccountList.addAll(Util.getAllConcreteAccount());
		}
		else {
			concreteAccountList.add(null);
			hasGroupPositionAndConditionType = false;
		}

		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.CONDITIONTYPE)) {
			ifrs16ConditionTypeList.addAll(Util.getAllIFRS16ConditionType());
		}
		else {
			ifrs16ConditionTypeList.add(null);
			hasGroupPositionAndConditionType = false;
		}

		ArrayList<IFRS16PaymentCycle> ifrs16PaymentCycleList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.PAYMENTCYCLE)) {
			ifrs16PaymentCycleList.addAll(IFRS16PaymentCycle.getAllValues());
			ifrs16PaymentCycleList.remove(IFRS16PaymentCycle.NULL);
		}
		else {
			ifrs16PaymentCycleList.add(null);
		}

		ArrayList<IFRS16PaymentDateType> ifrs16PaymentDateTypeList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.PAYMENTDATETYPE)) {
			ifrs16PaymentDateTypeList.addAll(IFRS16PaymentDateType.getAllValues());
			ifrs16PaymentDateTypeList.remove(IFRS16PaymentDateType.NULL);
		}
		else {
			ifrs16PaymentDateTypeList.add(null);
		}

		ArrayList<IFRS16VATRateType> ifrs16vatRateTypeList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.VATRATETYPE)) {
			ifrs16vatRateTypeList.addAll(IFRS16VATRateType.getAllValues());
			ifrs16vatRateTypeList.remove(IFRS16VATRateType.NULL);
		}
		else {
			ifrs16vatRateTypeList.add(null);
		}

		Combination combination = new Combination();
		System.out.println("concreteAccountList : " + concreteAccountList.size() + " ifrs16ConditionTypeList: " + ifrs16ConditionTypeList.size() + " ifrs16PaymentCycleList: "+ ifrs16PaymentCycleList.size() + " ifrs16PaymentDateTypeList: "+ ifrs16PaymentDateTypeList.size() + " ifrs16vatRateTypeList: " + ifrs16vatRateTypeList.size() );
		int pairwiseSwitchingCounter = 0;
		IFRS16PaymentCycle ifrs16PaymentCycle = IFRS16PaymentCycle.getValue(new Short((short) 0));

		for(ConcreteAccount concreteAccount : concreteAccountList) {
			for(IFRS16ConditionType ifrs16ConditionType : ifrs16ConditionTypeList) {

				if(!hasGroupPositionAndConditionType || Util.isValid(concreteAccount.getCode(), ifrs16ConditionType.getCode())) {

					// PAIRWISE MAGIC
					pairwiseSwitchingCounter = pairwiseSwitchingCounter % IFRS16PaymentCycle.getAllValues().size() + 1;
					ifrs16PaymentCycle = IFRS16PaymentCycle.getValue(new Short((short)pairwiseSwitchingCounter));
					// PAIRWISE MAGIC	

					for(IFRS16PaymentDateType ifrs16PaymentDateType : ifrs16PaymentDateTypeList) {
						for(IFRS16VATRateType ifrs16vatRateType : ifrs16vatRateTypeList) {

							CombinationLine combinationLine = new CombinationLine();
							combination.getLineList().add(combinationLine);

							for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
								CombinationItem<?> combinationItem = null;
								switch(type.getShortValue()) {
								case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
									combinationItem = createCombinationItemDouble(Constants.DECIMALS_AMOUNT, settings);
									break;
								case IFRS16ImportAssignmentType._CONDITIONTYPE:
									combinationItem = createCombinationItemConditionType(ifrs16ConditionType, settings);
									break;
								case IFRS16ImportAssignmentType._CONTRACTNUMBER:
									combinationItem = CombinationItem.getNewCombinationItem("0000001");
									//combinationItem = createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER);
									break;
								case IFRS16ImportAssignmentType._COSTCENTER:
									combinationItem = createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
									break;
								case IFRS16ImportAssignmentType._CREDITORNAME:
									combinationItem = createCombinationItemString(Constants.MAX_LENGHT_CREDITORNAME);
									break;
								case IFRS16ImportAssignmentType._CREDITORNUMBER:
									combinationItem = createCombinationItemString(Constants.MAX_LENGHT_CREDITORNUMBER);
									break;
								case IFRS16ImportAssignmentType._DESIGNATIONLEASEDOBJECT:
									combinationItem = createCombinationItemString(Constants.MAX_LENGHT_DESIGNATIONLEASEDOBJECT);
									break;
								case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
									//combinationItem = CombinationItem.getNewCombinationItem("Hello");
									combinationItem = createCombinationItemDate(settings, true);
									break;
								case IFRS16ImportAssignmentType._FROMDATE:
									combinationItem = createCombinationItemDate(settings, false);
									break;
								case IFRS16ImportAssignmentType._GROUPPOSITION:
									combinationItem =  CombinationItem.getNewCombinationItem(concreteAccount, settings);
									//										combinationItem = createCombinationItemConcreteAccount(concreteAccount, settings);
									//										break;
								case IFRS16ImportAssignmentType._INTERESTRATE:
									combinationItem = createCombinationItemDouble(Constants.DECIMALS_INTERESTRATE, settings);
									break;
								case IFRS16ImportAssignmentType._PARTNERCOMPANY:
									combinationItem = createCombinationItemString(partnerCompanyCodeList);
									break;
								case IFRS16ImportAssignmentType._PAYMENTCYCLE:
									combinationItem = CombinationItem.getNewCombinationItem(ifrs16PaymentCycle, settings);
									//										combinationItem = createCombinationItemIFRS16PaymentCycle(ifrs16PaymentCycle, settings);
									//										break;
								case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
									combinationItem = CombinationItem.getNewCombinationItem(ifrs16PaymentDateType, settings);
									//										combinationItem = createCombinationItemIFRS16PaymentDateType(ifrs16PaymentDateType, settings);
									//										break;
								case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
									combinationItem = createCombinationItemDate(settings, false);
									break;
								case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
									combinationItem = createCombinationItemDate(settings, false);
									break;
								case IFRS16ImportAssignmentType._UNTILDATE:
									combinationItem = createCombinationItemDate(settings, false);
									break;
								case IFRS16ImportAssignmentType._VATRATETYPE:
									combinationItem = CombinationItem.getNewCombinationItem(ifrs16vatRateType, settings);
									//										combinationItem = createCombinationItemIFRS16VATRateType(ifrs16vatRateType, settings);
									break;
								}
								if(combinationItem != null) {
									combinationLine.getDataMap().put(type, combinationItem);
								}
							}

							//								if(combination.getLineList().size() == 1) {
							//									return combination;
							//								}
						}
					}
				}
			}
		}

		return combination;
	}

	private Combination createCombination(Settings settings, ArrayList<String> partnerCompanyCodeList) {

		boolean hasGroupPositionAndConditionType = true;

		ArrayList<ConcreteAccount> concreteAccountList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.GROUPPOSITION)) {
			concreteAccountList.addAll(Util.getAllConcreteAccount());
		}
		else {
			concreteAccountList.add(null);
			hasGroupPositionAndConditionType = false;
		}

		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.CONDITIONTYPE)) {
			ifrs16ConditionTypeList.addAll(Util.getAllIFRS16ConditionType());
		}
		else {
			ifrs16ConditionTypeList.add(null);
			hasGroupPositionAndConditionType = false;
		}

		ArrayList<IFRS16PaymentCycle> ifrs16PaymentCycleList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.PAYMENTCYCLE)) {
			ifrs16PaymentCycleList.addAll(IFRS16PaymentCycle.getAllValues());
			ifrs16PaymentCycleList.remove(IFRS16PaymentCycle.NULL);
		}
		else {
			ifrs16PaymentCycleList.add(null);
		}

		ArrayList<IFRS16PaymentDateType> ifrs16PaymentDateTypeList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.PAYMENTDATETYPE)) {
			ifrs16PaymentDateTypeList.addAll(IFRS16PaymentDateType.getAllValues());
			ifrs16PaymentDateTypeList.remove(IFRS16PaymentDateType.NULL);
		}
		else {
			ifrs16PaymentDateTypeList.add(null);
		}

		ArrayList<IFRS16VATRateType> ifrs16vatRateTypeList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.VATRATETYPE)) {
			ifrs16vatRateTypeList.addAll(IFRS16VATRateType.getAllValues());
			ifrs16vatRateTypeList.remove(IFRS16VATRateType.NULL);
		}
		else {
			ifrs16vatRateTypeList.add(null);
		}

		Combination combination = new Combination();
		System.out.println("concreteAccountList : " + concreteAccountList.size() + " ifrs16ConditionTypeList: " + ifrs16ConditionTypeList.size() + " ifrs16PaymentCycleList: "+ ifrs16PaymentCycleList.size() + " ifrs16PaymentDateTypeList: "+ ifrs16PaymentDateTypeList.size() + " ifrs16vatRateTypeList: " + ifrs16vatRateTypeList.size() );
		for(ConcreteAccount concreteAccount : concreteAccountList) {
			for(IFRS16ConditionType ifrs16ConditionType : ifrs16ConditionTypeList) {

				if(!hasGroupPositionAndConditionType || Util.isValid(concreteAccount.getCode(), ifrs16ConditionType.getCode())) {

					for(IFRS16PaymentCycle ifrs16PaymentCycle : ifrs16PaymentCycleList) {
						for(IFRS16PaymentDateType ifrs16PaymentDateType : ifrs16PaymentDateTypeList) {
							for(IFRS16VATRateType ifrs16vatRateType : ifrs16vatRateTypeList) {

								CombinationLine combinationLine = new CombinationLine();
								combination.getLineList().add(combinationLine);

								for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
									CombinationItem<?> combinationItem = null;
									switch(type.getShortValue()) {
									case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
										combinationItem = createCombinationItemDouble(Constants.DECIMALS_AMOUNT, settings);
										break;
									case IFRS16ImportAssignmentType._CONDITIONTYPE:
										combinationItem = createCombinationItemConditionType(ifrs16ConditionType, settings);
										break;
									case IFRS16ImportAssignmentType._CONTRACTNUMBER:
										combinationItem = CombinationItem.getNewCombinationItem("1234567");
										//combinationItem = createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER);
										break;
									case IFRS16ImportAssignmentType._COSTCENTER:
										combinationItem = CombinationItem.getNewCombinationItem("COSTCENTER");
										//combinationItem = createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
										break;
									case IFRS16ImportAssignmentType._CREDITORNAME:
										combinationItem = CombinationItem.getNewCombinationItem("CREDITORNAME");
										//combinationItem = createCombinationItemString(Constants.MAX_LENGHT_CREDITORNAME);
										break;
									case IFRS16ImportAssignmentType._CREDITORNUMBER:
										combinationItem = CombinationItem.getNewCombinationItem("CREDITORNUMBER");
										//combinationItem = createCombinationItemString(Constants.MAX_LENGHT_CREDITORNUMBER);
										break;
									case IFRS16ImportAssignmentType._DESIGNATIONLEASEDOBJECT:
										combinationItem = CombinationItem.getNewCombinationItem("DESIGNATIONLEASEDOBJECT");
										//combinationItem = createCombinationItemString(Constants.MAX_LENGHT_DESIGNATIONLEASEDOBJECT);
										break;
									case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
										//combinationItem = CombinationItem.getNewCombinationItem("Hello");
										combinationItem = createCombinationItemDate(settings, true);
										break;
									case IFRS16ImportAssignmentType._FROMDATE:
										combinationItem = createCombinationItemDate(settings, false);
										break;
									case IFRS16ImportAssignmentType._GROUPPOSITION:
										combinationItem =  CombinationItem.getNewCombinationItem(concreteAccount, settings);
										//										combinationItem = createCombinationItemConcreteAccount(concreteAccount, settings);
										//										break;
									case IFRS16ImportAssignmentType._INTERESTRATE:
										combinationItem = createCombinationItemDouble(Constants.DECIMALS_INTERESTRATE, settings);
										break;
									case IFRS16ImportAssignmentType._PARTNERCOMPANY:
										combinationItem = createCombinationItemString(partnerCompanyCodeList);
										break;
									case IFRS16ImportAssignmentType._PAYMENTCYCLE:
										combinationItem = CombinationItem.getNewCombinationItem(ifrs16PaymentCycle, settings);
										//										combinationItem = createCombinationItemIFRS16PaymentCycle(ifrs16PaymentCycle, settings);
										//										break;
									case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
										combinationItem = CombinationItem.getNewCombinationItem(ifrs16PaymentDateType, settings);
										//										combinationItem = createCombinationItemIFRS16PaymentDateType(ifrs16PaymentDateType, settings);
										//										break;
									case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
										combinationItem = createCombinationItemDate(settings, false);
										break;
									case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
										combinationItem = createCombinationItemDate(settings, false);
										break;
									case IFRS16ImportAssignmentType._UNTILDATE:
										combinationItem = createCombinationItemDate(settings, false);
										break;
									case IFRS16ImportAssignmentType._VATRATETYPE:
										combinationItem = CombinationItem.getNewCombinationItem(ifrs16vatRateType, settings);
										//										combinationItem = createCombinationItemIFRS16VATRateType(ifrs16vatRateType, settings);
										break;
									}
									if(combinationItem != null) {
										combinationLine.getDataMap().put(type, combinationItem);
									}
								}

								//								if(combination.getLineList().size() == 1) {
								//									return combination;
								//								}
							}
						}
					}

				}
			}
		}

		return combination;
	}
	private HashMap<Integer, DoubleGenerator> doubleGeneratorMap = new HashMap<>();
	private CombinationItem<Double> createCombinationItemDouble(int decimals, Settings settings) {
		if(!doubleGeneratorMap.containsKey(decimals)) {
			doubleGeneratorMap.put(decimals, new DoubleGenerator(decimals));
		}
		return CombinationItem.getNewCombinationItem(doubleGeneratorMap.get(decimals).next(), settings);
	}

	private boolean[][] createCombinations(int size) {
		int count = Double.valueOf(Math.pow(2,size)).intValue();
		boolean[][] map = new boolean[count][size];
		for(int columnIndex=0; columnIndex<size; columnIndex++) {
			int module = Double.valueOf(Math.pow(2,columnIndex)).intValue();
			boolean value = false;
			for(int index=0; index<count; index++) {
				map[index][columnIndex] = value;
				if((index+1) % module == 0) {
					value = !value;
				}
			}
		}
		return map;
	}

	private abstract class CheckExecutor {

		protected boolean execute(String checkName, ArrayList<Company> companyList, ImplementedCompany implementedCompany, boolean isLongVersion, Settings settingsTemplate, ArrayList<IFRS16ImportAssignmentType> typeList) {

			// Clone the Settings
			Settings settings = cloneSettings(checkName,implementedCompany, isLongVersion, settingsTemplate, typeList);

			// Create the Combination
			Combination combination = createCombination(settings);
			if(combination != null && combination.getLineList().size() > 0) {

				// Create the scenario
				Scenario scenario = new Scenario(companyList, combination, settings);

				// Compose the Scenario
				System.out.println(checkName + ": " + combination.getLineList().size() + " lines, " + settings.getTypeList().size() + " columns");
				return check(scenario);
			}

			return true;
		}

		protected abstract Combination createCombination(Settings settings);

		private Settings cloneSettings(String checkName,ImplementedCompany implementedCompany, boolean isLongVersion, Settings settingsTemplate, ArrayList<IFRS16ImportAssignmentType> typeList) {
			Settings settings = new Settings(settingsTemplate.getLocale(), settingsTemplate.getDigitGroupingSymbol(), settingsTemplate.getDecimalSeparator(), settingsTemplate.getDateFormat());
			settings.setImplementedCompany(implementedCompany);
			settings.setLongVersion(isLongVersion);
			settings.setYear(YEAR);
			settings.setMonth(MONTH);
			settings.setFilePath(EngineTestData.class.getSimpleName()+checkName+ ".csv");
			settings.setName(EngineTestData.class.getSimpleName());
			settings.getTypeList().addAll(typeList);
			return settings;
		}
	}

	private Combination createSingleCombination(ArrayList<IFRS16ImportAssignmentType> currentTypeList, CombinationValueType combinationValueType) {
		Locale locale = Locale.GERMAN;
		DigitGroupingSymbol digitGroupingSymbol = DigitGroupingSymbol.POINT;
		DigitGroupingSymbol decimalSeparator = DigitGroupingSymbol.COMMA;
		String dateFormat =Constants.SETTTING_DATE_DE;

		IFRS16ImportAssignmentType importAssignmentType = IFRS16ImportAssignmentType.CREDITORNUMBER;

		Settings settings = new Settings(locale, digitGroupingSymbol, decimalSeparator, dateFormat);
		settings.getTypeList().add(importAssignmentType);
		settings.getTypeList().add(IFRS16ImportAssignmentType.COSTCENTER);
		CombinationItem<Double> combinationItem = CombinationItem.getNewCombinationItem(1.45, settings);

		CombinationLine combinationLine = new CombinationLine();


		for(IFRS16ImportAssignmentType type : currentTypeList) {

			if(combinationValueType.equals(CombinationValueType.EMPTY)) {

				createCombinationItem(settings, combinationLine, type);

			} else {

				switch(type.getShortValue()) {
				// *** COMBINATION 1 ***
				case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
				case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
					if(combinationValueType.equals(CombinationValueType.VALID)) {
						createCombinationItemDate(settings, true);
					}
					if(combinationValueType.equals(CombinationValueType.INVALID)) {
						createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER);
					} 
					break;
				case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
					if(combinationValueType.equals(CombinationValueType.VALID)) {
						createCombinationItemDate(settings, false);
					}
					if(combinationValueType.equals(CombinationValueType.INVALID)) {
						createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER);
					} 
					break;
				case IFRS16ImportAssignmentType._PARTNERCOMPANY:

					if(combinationValueType.equals(CombinationValueType.VALID)) {
						//createCombinationItemPartnerCode();
					}
					if(combinationValueType.equals(CombinationValueType.INVALID)) {
						// nonexisting comany code
					} 
					break;
				case IFRS16ImportAssignmentType._INTERESTRATE:
					if(combinationValueType.equals(CombinationValueType.VALID)) {
						createCombinationItemDouble(Constants.DECIMALS_INTERESTRATE, settings);
					}
					if(combinationValueType.equals(CombinationValueType.INVALID)) {
						createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER);
					} 

					break;
					// *** COMBINATION 2 ***

				default:
					if(combinationValueType.equals(CombinationValueType.VALID)) {
						createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER);
					}
					if(combinationValueType.equals(CombinationValueType.INVALID)) {
						createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER + 1);
					} 

					break;

				}
			}



		}



		combinationLine.getDataMap().put(importAssignmentType, combinationItem);





		//		CombinationItem<?> combinationItem = createCombinationItem(settings, combinationLine, type);

		Combination combination = new Combination();
		combination.getLineList().add(combinationLine);

		return combination;
	}

	private HashMap<Integer, StringGenerator> stringGeneratorMap = new HashMap<>();
	private CombinationItem<String> createCombinationItemString(int maxLenght) {
		if(!stringGeneratorMap.containsKey(maxLenght)) {
			stringGeneratorMap.put(maxLenght, new StringGenerator(maxLenght));
		}
		return CombinationItem.getNewCombinationItem(stringGeneratorMap.get(maxLenght).next());
	}

	private HashMap<Boolean, DateGenerator> dateGeneratorMap = new HashMap<>();
	private CombinationItem<Long> createCombinationItemDate(Settings settings, boolean endDate) {
		if(!dateGeneratorMap.containsKey(endDate)) {
			dateGeneratorMap.put(endDate, new DateGenerator(endDate));
		}
		return CombinationItem.getNewCombinationItem(dateGeneratorMap.get(endDate).next(), settings);
	}

	private static ArrayList<Company> createCompanyList() {

		ArrayList<Company> companyList = new ArrayList<>();

		for(String code : COMPANY_CODES) {

			Company company = new Company();
			company.setCode(code);
			companyList.add(company);

			for(CompanyType companyType : new CompanyType[] {CompanyType.PRODUCTIVE, CompanyType.CONSOLIDATED}) {

				ImplementedCompany implementedCompany = new ImplementedCompany();
				implementedCompany.setType(companyType);
				company.createChildImplementedCompany(implementedCompany);
			}
		}

		return companyList;
	}

	private ArrayList<Settings> createSettingsList() {

		ArrayList<Locale> localeList = new ArrayList<>();
		localeList.add(Locale.ENGLISH);
		localeList.add(Locale.GERMAN);

		ArrayList<DigitGroupingSymbol> digitGroupingSymbolList = new ArrayList<>(DigitGroupingSymbol.getAllValues());

		ArrayList<DigitGroupingSymbol> decimalSeparatorList = new ArrayList<>();
		//TODO		decimalSeparatorList.add(DigitGroupingSymbol.NULL);
		decimalSeparatorList.add(DigitGroupingSymbol.COMMA);
		decimalSeparatorList.add(DigitGroupingSymbol.POINT);

		ArrayList<String> dateFormatList = new ArrayList<>();
		dateFormatList.add(Constants.SETTTING_DATE_EN);
		dateFormatList.add(Constants.SETTTING_DATE_DE);

		ArrayList<Settings> settingsList = new ArrayList<>();

		for(Locale locale : localeList) {
			for(DigitGroupingSymbol digitGroupingSymbol : digitGroupingSymbolList) {
				for(DigitGroupingSymbol decimalSeparator : decimalSeparatorList) {

					if(!digitGroupingSymbol.equals(decimalSeparator)) {

						for(String dateFormat : dateFormatList) {

							settingsList.add(new Settings(locale, digitGroupingSymbol, decimalSeparator, dateFormat));
						}
					}
				}
			}
		}

		return settingsList;
	}

	private class ObjectIterator<T> {

		private ArrayList<T> _itemList;
		private int _nextIndex;

		private ObjectIterator(ArrayList<T> itemList) {
			_itemList = new ArrayList<>(itemList);
			_nextIndex = 0;
		}

		private T next() {
			T next = _itemList.get(_nextIndex);
			if(++_nextIndex == _itemList.size()) {
				_nextIndex = 0;
			}
			return next;
		}
	}

	private class DoubleGenerator {

		private int _decimals;
		private int _next;

		private DoubleGenerator(int decimals) {
			_decimals = decimals;
			_next = 1 * Double.valueOf(Math.pow(10, _decimals)).intValue();
		}

		private Double next() {
			Double next = Integer.valueOf(_next).doubleValue() / Math.pow(10, _decimals);
			_next++;
			return next;
		}
	}

	private class StringGenerator {

		private char[] _next;

		private StringGenerator(int maxLenght) {
			_next = new char[maxLenght];
			Arrays.fill(_next, 'a');
		}

		private String next() {
			String next = String.valueOf(_next);
			for(int index=_next.length-1; index>=0; index--) {
				if(_next[index] == 'z') {
					_next[index] = 'a';
				}
				else {
					_next[index]++;
					break;
				}
			}
			return next;
		}
	}

	private class DateGenerator {

		private Calendar _next;

		private DateGenerator(boolean endDate) {
			_next = GregorianCalendar.getInstance();
			_next.set(Calendar.DAY_OF_MONTH, 	1);
			if(endDate) {
				_next.set(Calendar.YEAR, 			1992);
				_next.set(Calendar.MONTH, 			MONTH);
			}
			else {
				_next.set(Calendar.YEAR, 			2015);
				_next.set(Calendar.MONTH, 			17);
			}
			_next.set(Calendar.HOUR_OF_DAY, 	0);
			_next.set(Calendar.MINUTE, 			0);
			_next.set(Calendar.SECOND, 			0);
			_next.set(Calendar.MILLISECOND, 	0);
		}
		private Long next() {
			Long next = _next.getTimeInMillis();
			_next.add(Calendar.DAY_OF_MONTH, 1);
			return next;
		}
	}

	private ObjectIterator<IFRS16ConditionType> IFRS16ConditionTypeIterator;
	private CombinationItem<IFRS16ConditionType> createCombinationItemConditionType(Settings settings, CombinationLine combinationLine) {
		if(IFRS16ConditionTypeIterator == null) {
			IFRS16ConditionTypeIterator = new ObjectIterator<>(getIFRS16ConditionTypeList());
		}
		IFRS16ConditionType ifrs16ConditionType;
		do {
			ifrs16ConditionType = IFRS16ConditionTypeIterator.next();
		}
		while(combinationLine.getDataMap().containsKey(IFRS16ImportAssignmentType.GROUPPOSITION) && !Util.isValid(combinationLine.getDataMap().get(IFRS16ImportAssignmentType.GROUPPOSITION).getValue(ConcreteAccount.class).getCode(), ifrs16ConditionType.getCode()));
		return createCombinationItemConditionType(ifrs16ConditionType, settings);
	}

	private ArrayList<IFRS16ConditionType> getIFRS16ConditionTypeList() {
		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = new ArrayList<>(Util.getAllIFRS16ConditionType());
		Collections.sort(ifrs16ConditionTypeList, new Comparator<IFRS16ConditionType>() {

			@Override
			public int compare(IFRS16ConditionType o1, IFRS16ConditionType o2) {
				return o1.getCode().compareTo(o2.getCode());
			}

		});
		return ifrs16ConditionTypeList;
	}

	private CombinationItem<IFRS16ConditionType> createCombinationItemConditionType(IFRS16ConditionType ifrs16ConditionType, Settings settings) {
		return CombinationItem.getNewCombinationItem(ifrs16ConditionType, settings);
	}

	private ObjectIterator<String> stringIterator;
	private CombinationItem<String> createCombinationItemString(ArrayList<String> stringList) {
		if(stringIterator == null) {
			stringIterator = new ObjectIterator<>(stringList);
		}
		return CombinationItem.getNewCombinationItem(stringIterator.next());
	}


	// -------------- invalid
	private Combination createCombinationUnparsable(Settings settings) {
		Combination combination = new Combination();

		ArrayList<ConcreteAccount> concreteAccountList = getConcreteAccountList();
		concreteAccountList.add(null);
		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = getIFRS16ConditionTypeList();
		ifrs16ConditionTypeList.add(null);
		for(IFRS16ImportAssignmentType typeExceedingText : settings.getTypeList()) {
			// generate wrong values
			switch(typeExceedingText.getShortValue()) {
			case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
			case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
			case IFRS16ImportAssignmentType._FROMDATE:
			case IFRS16ImportAssignmentType._INTERESTRATE:
			case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
			case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
			case IFRS16ImportAssignmentType._UNTILDATE:

				CombinationLine combinationLine = new CombinationLine();
				combination.getLineList().add(combinationLine);

				for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
					CombinationItem<?> combinationItem = null;
					if(type.equals(typeExceedingText)) {
						combinationItem = createCombinationItemString(4);
					}
					else {
						combinationItem = createCombinationItem(settings, combinationLine, type);
					}
					if(combinationItem != null) {
						combinationLine.getDataMap().put(type, combinationItem);
					}
				}
				break;
			}
		}

		return combination;
	}

	private CombinationItem createCombinationItem(Settings settings, CombinationLine combinationLine, IFRS16ImportAssignmentType type) {
		switch(type.getShortValue()) {
		case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
			return createCombinationItemDouble(Constants.DECIMALS_AMOUNT, settings);
		case IFRS16ImportAssignmentType._CONDITIONTYPE:
			return createCombinationItemConditionType(settings, combinationLine);
		case IFRS16ImportAssignmentType._CONTRACTNUMBER:
			return createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER);
		case IFRS16ImportAssignmentType._COSTCENTER:
			return createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
		case IFRS16ImportAssignmentType._CREDITORNAME:
			return createCombinationItemString(Constants.MAX_LENGHT_CREDITORNAME);
		case IFRS16ImportAssignmentType._CREDITORNUMBER:
			return createCombinationItemString(Constants.MAX_LENGHT_CREDITORNUMBER);
		case IFRS16ImportAssignmentType._DESIGNATIONLEASEDOBJECT:
			return createCombinationItemString(Constants.MAX_LENGHT_DESIGNATIONLEASEDOBJECT);
		case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
			return createCombinationItemDate(settings, true);
		case IFRS16ImportAssignmentType._FROMDATE:
			return createCombinationItemDate(settings, false);
		case IFRS16ImportAssignmentType._GROUPPOSITION:
			return createCombinationItemConcreteAccount(settings);
		case IFRS16ImportAssignmentType._INTERESTRATE:
			return createCombinationItemDouble(Constants.DECIMALS_INTERESTRATE, settings);
		case IFRS16ImportAssignmentType._PARTNERCOMPANY:
			//return createCombinationItemPartnerCode();
		case IFRS16ImportAssignmentType._PAYMENTCYCLE:
			//return createCombinationItemIFRS16PaymentCycle(settings);
		case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
			//return createCombinationItemIFRS16PaymentDateType(settings);
		case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
			return createCombinationItemDate(settings, false);
		case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
			return createCombinationItemDate(settings, false);
		case IFRS16ImportAssignmentType._UNTILDATE:
			return createCombinationItemDate(settings, false);
		case IFRS16ImportAssignmentType._VATRATETYPE:
			//return createCombinationItemIFRS16VATRateType(settings);
		default:
			return null;
		}
	}

	private ArrayList<ConcreteAccount> getConcreteAccountList() {
		ArrayList<ConcreteAccount> concreteAccountList = new ArrayList<>(Util.getAllConcreteAccount());
		Collections.sort(concreteAccountList, new Comparator<ConcreteAccount>() {

			@Override
			public int compare(ConcreteAccount o1, ConcreteAccount o2) {
				return o1.getCode().compareTo(o2.getCode());
			}
		});
		return concreteAccountList;
	}

	private ObjectIterator<ConcreteAccount> concreteAccountIterator;
	private CombinationItem<ConcreteAccount> createCombinationItemConcreteAccount(Settings settings) {
		if(concreteAccountIterator == null) {
			concreteAccountIterator = new ObjectIterator<>(getConcreteAccountList());
		}
		return createCombinationItemConcreteAccount(concreteAccountIterator.next(), settings);
	}

	private CombinationItem<ConcreteAccount> createCombinationItemConcreteAccount(ConcreteAccount concreteAccount, Settings settings) {
		return CombinationItem.getNewCombinationItem(concreteAccount, settings);
	}

	private Combination createCombinationEmpty(Settings settings) {
		Combination combination = new Combination();

		ArrayList<ConcreteAccount> concreteAccountList = getConcreteAccountList();
		concreteAccountList.add(null);
		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = getIFRS16ConditionTypeList();
		ifrs16ConditionTypeList.add(null);
		for(IFRS16ImportAssignmentType typeEmpty : settings.getTypeList()) {

			CombinationLine combinationLine = new CombinationLine();
			combination.getLineList().add(combinationLine);

			for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
				if(!type.equals(typeEmpty)) {
					CombinationItem<?> combinationItem = createCombinationItem(settings, combinationLine, type);
					if(combinationItem != null) {
						combinationLine.getDataMap().put(type, combinationItem);
					}
				}
			}
		}

		return combination;
	}

	private Combination createCombinationGroupPositionConditionType(Settings settings) {
		Combination combination = new Combination();

		ArrayList<ConcreteAccount> concreteAccountList = getConcreteAccountList();
		concreteAccountList.add(null);
		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = getIFRS16ConditionTypeList();
		ifrs16ConditionTypeList.add(null);
		for(ConcreteAccount concreteAccount : concreteAccountList) {
			for(IFRS16ConditionType ifrs16ConditionType : ifrs16ConditionTypeList) {
				if(concreteAccount == null || ifrs16ConditionType == null || !Util.isValid(concreteAccount.getCode(), ifrs16ConditionType.getCode())) {

					CombinationLine combinationLine = new CombinationLine();
					combination.getLineList().add(combinationLine);

					for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
						CombinationItem<?> combinationItem = null;
						switch(type.getShortValue()) {
						case IFRS16ImportAssignmentType._CONDITIONTYPE:
							combinationItem = createCombinationItemConditionType(ifrs16ConditionType, settings);
							break;
						case IFRS16ImportAssignmentType._GROUPPOSITION:
							combinationItem = createCombinationItemConcreteAccount(concreteAccount, settings);
							break;
						default:
							combinationItem = createCombinationItem(settings, combinationLine, type);
							break;
						}
						if(combinationItem != null) {
							combinationLine.getDataMap().put(type, combinationItem);
						}
					}
				}
			}
		}

		return combination;
	}

	private Combination createCombinationErrorDifferentAttribute(Settings settings, IFRS16ImportCompanyDataColumnSectionType sectionType) {
		Combination combination = new Combination();

		for(IFRS16ImportAssignmentType typeDifferent : settings.getTypeList()) {
			if(sectionType.equals(Util.getColumnSectionType(typeDifferent))) {
				switch(typeDifferent.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
					break;
				default:
					CombinationLine combinationLine0 = new CombinationLine();
					combination.getLineList().add(combinationLine0);
					CombinationLine combinationLine1 = new CombinationLine();
					combination.getLineList().add(combinationLine1);

					for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
						CombinationItem<?> combinationItem = createCombinationItem(settings, combinationLine0, type);
						if(combinationItem != null) {
							combinationLine0.getDataMap().put(type, combinationItem);
							if(type.equals(typeDifferent)) {
								combinationLine1.getDataMap().put(type, createCombinationItem(settings, combinationLine1, type));
							}
							else {
								combinationLine1.getDataMap().put(type, combinationItem);
							}
						}
					}
					break;
				}
			}
		}

		return combination;
	}

	private Combination createCombinationExceedingText(Settings settings) {
		Combination combination = new Combination();

		ArrayList<ConcreteAccount> concreteAccountList = getConcreteAccountList();
		concreteAccountList.add(null);
		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = getIFRS16ConditionTypeList();
		ifrs16ConditionTypeList.add(null);
		for(IFRS16ImportAssignmentType typeExceedingText : settings.getTypeList()) {

			int maxLenght = -1;
			switch(typeExceedingText.getShortValue()) {
			case IFRS16ImportAssignmentType._CREDITORNUMBER:
				maxLenght = Constants.MAX_LENGHT_CREDITORNUMBER;
				break;
			case IFRS16ImportAssignmentType._CREDITORNAME:
				maxLenght = Constants.MAX_LENGHT_CREDITORNAME;
				break;
			case IFRS16ImportAssignmentType._CONTRACTNUMBER:
				maxLenght = Constants.MAX_LENGHT_CONTRACTNUMBER;
				break;
			case IFRS16ImportAssignmentType._DESIGNATIONLEASEDOBJECT:
				maxLenght = Constants.MAX_LENGHT_DESIGNATIONLEASEDOBJECT;
				break;
			case IFRS16ImportAssignmentType._COSTCENTER:
				maxLenght = Constants.MAX_LENGHT_COSTCENTER;
				break;
			}
			if(maxLenght > 0) {

				CombinationLine combinationLine = new CombinationLine();
				combination.getLineList().add(combinationLine);

				for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
					CombinationItem<?> combinationItem = null;
					if(type.equals(typeExceedingText)) {
						combinationItem = createCombinationItemString(maxLenght+1);
					}
					else {
						combinationItem = createCombinationItem(settings, combinationLine, type);
					}
					if(combinationItem != null) {
						combinationLine.getDataMap().put(type, combinationItem);
					}
				}
			}
		}

		return combination;
	}

	private Combination createCombinationHistorical(Settings settings) {

		Combination combination = new Combination();

		Calendar calendar = GregorianCalendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.YEAR, 		YEAR);
		calendar.set(Calendar.MONTH, 		MONTH);
		calendar.set(Calendar.HOUR_OF_DAY, 	0);
		calendar.set(Calendar.MINUTE, 		0);
		calendar.set(Calendar.SECOND, 		0);
		calendar.set(Calendar.MILLISECOND, 	0);
		calendar.add(Calendar.MONTH, 		-2);
		Long contractEnd = calendar.getTimeInMillis();

		for(boolean change : new boolean[] {true, false}) {
			for(IFRS16ImportAssignmentType typeNotEditable : settings.getTypeList()) {
				switch(typeNotEditable.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
				case IFRS16ImportAssignmentType._CONDITIONTYPE://TODO
				case IFRS16ImportAssignmentType._FROMDATE://TODO
				case IFRS16ImportAssignmentType._COSTCENTER://TODO
					break;
				default:
					CombinationLine combinationLine = new CombinationLine();
					combination.getLineList().add(combinationLine);
					for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
						CombinationItem<?> combinationItem = createCombinationItem(settings, combinationLine, type);
						if(combinationItem != null) {
							combinationLine.getDataMap().put(type, combinationItem);
						}
					}
					IFRS16Contract ifrs16Contract = new Checker().createIFRS16Contract(settings, combinationLine);
					if(ifrs16Contract != null) {
						ifrs16Contract.setContractEnd(contractEnd);
						settings.getImplementedCompany().createChildIFRS16Contract(ifrs16Contract);
						if(change) {
							CombinationItem<?> combinationItem = createCombinationItem(settings, combinationLine, typeNotEditable);
							if(combinationItem != null) {
								combinationLine.getDataMap().put(typeNotEditable, combinationItem);
							}
						}
					}
					break;
				}
			}
		}

		return combination;
	}

	private Combination createCombinationNotEditable(Settings settings) {

		Combination combination = new Combination();

		for(boolean change : new boolean[] {true, false}) {
			for(IFRS16ImportAssignmentType typeNotEditable : settings.getTypeList()) {
				switch(typeNotEditable.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
				case IFRS16ImportAssignmentType._CONDITIONTYPE://TODO
				case IFRS16ImportAssignmentType._FROMDATE://TODO
				case IFRS16ImportAssignmentType._COSTCENTER://TODO
					break;
				default:
					CombinationLine combinationLine = new CombinationLine();
					combination.getLineList().add(combinationLine);

					for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
						CombinationItem<?> combinationItem = createCombinationItem(settings, combinationLine, type);
						if(combinationItem != null) {
							combinationLine.getDataMap().put(type, combinationItem);
						}
					}

					IFRS16Contract ifrs16Contract = new Checker().createIFRS16Contract(settings, combinationLine);
					if(ifrs16Contract != null) {

						settings.getImplementedCompany().createChildIFRS16Contract(ifrs16Contract);

						if(change) {
							CombinationItem<?> combinationItem = createCombinationItem(settings, combinationLine, typeNotEditable);
							if(combinationItem != null) {
								combinationLine.getDataMap().put(typeNotEditable, combinationItem);
							}
						}
					}
					break;
				}
			}
		}

		return combination;
	}

	/**
	 * 
	 * Enumeration that is used to differentiate between valid, invalid and empty
	 * values for the combinations
	 * 
	 *
	 */
	public enum CombinationValueType {
		VALID, INVALID, EMPTY;
	}
}
