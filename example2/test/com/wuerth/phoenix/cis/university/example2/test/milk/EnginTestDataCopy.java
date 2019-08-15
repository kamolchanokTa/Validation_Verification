package com.wuerth.phoenix.cis.university.example2.test.milk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import com.sun.javafx.util.Utils;
import com.wuerth.phoenix.cis.university.example2.adapters.Company;
import com.wuerth.phoenix.cis.university.example2.adapters.CompanyType;
import com.wuerth.phoenix.cis.university.example2.adapters.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example2.adapters.DigitGroupingSymbol;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ConditionType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16Contract;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16CostCenterType;
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
import com.wuerth.phoenix.cis.university.example2.util.Scenario;
import com.wuerth.phoenix.cis.university.example2.util.Settings;
import com.wuerth.phoenix.cis.university.example2.util.Util;

public abstract class EngineTestDataCopy implements IEngineTestData {

	private final String COMPANY_CODE_IMPORT = "2407";
	private final String[] COMPANY_CODES = new String[] {
			COMPANY_CODE_IMPORT,
			"1000",
			"8999"
	};
	private final int YEAR 	= 2018;
	private final int MONTH = Calendar.MARCH;

	private final boolean CHECK_ERROR 		= false;
	private final boolean CHECK_COMBINATION = false;
	private final boolean TEST 				= true;
	private final boolean REDUCTION			= true;
	private final int MAX_ROW_COUNT			= 1;




	@Override
	public boolean start() {
		if(REDUCTION) {
			return startReduction();
		}
		else {
			return startAll(null);
		}
	}


	private ArrayList<ArrayList<IFRS16ImportAssignmentType>> createListOfAllCombinationsOfTypes() {
		ArrayList<ArrayList<IFRS16ImportAssignmentType>> listOfAllPossibleCombinationsOfTypes = new ArrayList<ArrayList<IFRS16ImportAssignmentType>>();
		ArrayList<IFRS16ImportAssignmentType> allTypeList = new ArrayList<>();
		boolean isLongVersion = true;
		boolean isAcceptOrders = true;



		for(IFRS16ImportAssignmentType type : IFRS16ImportAssignmentType.getAllValues()) {
			if(!IFRS16ImportAssignmentType.NULL.equals(type)) {
				if(Util.isAvailable(type, isLongVersion, isAcceptOrders)) {
					allTypeList.add(type);
				}
			}
		}



		for(boolean[] flags : createCombinations(allTypeList.size())) {

			// ...create the list of types
			ArrayList<IFRS16ImportAssignmentType> typeList = new ArrayList<>();
			for(int index=0; index<flags.length; index++) {
				if(flags[index]) {
					typeList.add(allTypeList.get(index));
					if(Util.isValid(isLongVersion, isAcceptOrders, typeList)) {
						listOfAllPossibleCombinationsOfTypes.add(typeList);
					}

				}
			}
		}
		return listOfAllPossibleCombinationsOfTypes;
	}






	private boolean startAll(ArrayList<IFRS16ImportAssignmentType> allTypeList) {


		int count = 0;
		int countValid = 0;

		ArrayList<Company> companyList = createCompanyList();

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

		ObjectIterator<Settings> settingsIterator = new ObjectIterator<>(createSettingsList());

		for(boolean isLongVersion : new boolean[] {false, true}) {
			for(boolean isAcceptOrders : new boolean[] {false, true}) {


				// Filter the available types
				if(allTypeList == null)  {
					allTypeList = new ArrayList<>();
					for(IFRS16ImportAssignmentType type : IFRS16ImportAssignmentType.getAllValues()) {
						if(!IFRS16ImportAssignmentType.NULL.equals(type)) {
							if(Util.isAvailable(type, isLongVersion, isAcceptOrders)) {
								allTypeList.add(type);
							}
						}
					}
				}
				//ArrayList<IFRS16ImportAssignmentType> allTypeList = new ArrayList<>();


				// Check the errors

				ArrayList<IFRS16ImportAssignmentType> filteredTypeList = new ArrayList<>(allTypeList);
				if(filteredTypeList.contains(IFRS16ImportAssignmentType.COSTCENTERTYPE)) {
					filteredTypeList.remove(IFRS16ImportAssignmentType.COSTCENTER);
					filteredTypeList.remove(IFRS16ImportAssignmentType.ORDER);
				}

				if(CHECK_ERROR) {
					for(IFRS16ImportCompanyDataErrorType errorType : IFRS16ImportCompanyDataErrorType.values()) {
						if(!new CheckExecutor() {

							@Override
							protected Combination createCombination(Settings settings) {
								switch(errorType) {
								case ConditionTypeNotAvailable:
									return createCombinationGroupPositionConditionType(settings);
								case DifferentAttributeCondition:
									return createCombinationErrorDifferentAttribute(settings, IFRS16ImportCompanyDataColumnSectionType.Condition);
								case DifferentAttributeConditionData:
									return createCombinationErrorDifferentAttribute(settings, IFRS16ImportCompanyDataColumnSectionType.ConditionData);
								case DifferentAttributeContract:
									return createCombinationErrorDifferentAttribute(settings, IFRS16ImportCompanyDataColumnSectionType.Contract);
								case DifferentAttributeContractData:
									return createCombinationErrorDifferentAttribute(settings, IFRS16ImportCompanyDataColumnSectionType.ContractData);
								case Empty:
									return createCombinationEmpty(settings);
								case ExceedingText:
									return createCombinationExceedingText(settings);
								case Historical:
									return createCombinationHistorical(companyList, settings);
								case NoGroupPosition:
									// Error type checked with 'ConditionTypeNotAvailable'
									return null;
								case NotEditable:
									return createCombinationNotEditable(companyList, settings);
								case Unparsable:
									return createCombinationUnparsable(settings);
								default:
									return null;
								}
							}
						}.execute(isLongVersion + " " + isAcceptOrders + " Error Check - " + errorType.name(), companyList, implementedCompany, isLongVersion, isAcceptOrders, settingsIterator.next(), filteredTypeList)) {
							return false;
						}
					}
				}

				boolean skip = false;
				int stopSkip = 0;

				if(CHECK_COMBINATION) {
					// For each combination...
					System.out.println("numb of execs " + createCombinations(allTypeList.size()).length);
					for(boolean[] flags : createCombinations(allTypeList.size())) {

						count++;

						// ...create the list of types
						ArrayList<IFRS16ImportAssignmentType> typeList = new ArrayList<>();
						for(int index=0; index<flags.length; index++) {
							if(flags[index]) {
								typeList.add(allTypeList.get(index));
							}
						}

						// If the combination is valid...
						if(Util.isValid(isLongVersion, isAcceptOrders, typeList)) {

							countValid++;

							if(skip) {
								if(countValid == stopSkip) {
									skip = false;
								}
								else {
									continue;
								}
							}

							if(!new CheckExecutor() {

								@Override
								protected Combination createCombination(Settings settings) {
									return EngineTestData.this.createCombination(settings, partnerCompanyCodeList);
								}
							}.execute(isLongVersion + " " + isAcceptOrders + " Valid Check - " + countValid + " of " + count, companyList, implementedCompany, isLongVersion, isAcceptOrders, settingsIterator.next(), typeList)) {
								return false;
							}
						}
					}
				}
			}
		}

		return true;
	}

	private boolean startReduction() {

		/**
		 *  *** Create List of all possible values ***
		 */
		
		ArrayList<ArrayList<IFRS16ImportAssignmentType>> listOfAllPossibleCombinationsOfTypes  = createListOfAllCombinationsOfTypes();
		System.out.println("Initial" + listOfAllPossibleCombinationsOfTypes.size());

		//Filling the the allTypeList Array
		ArrayList<IFRS16ImportAssignmentType> allTypeList = new ArrayList<>();
		for(boolean isLongVersion : new boolean[] {false, true}) {
			for(boolean isAcceptOrders : new boolean[] {false, true}) {
				// Filter the available types
				boolean islongVersion = true;
				for(IFRS16ImportAssignmentType type : IFRS16ImportAssignmentType.getAllValues()) {
					if(!IFRS16ImportAssignmentType.NULL.equals(type)) {
						if(Util.isAvailable(type, true, islongVersion)) {
							allTypeList.add(type);
						}
					}
				}
			}
		}
		
		ArrayList<Company> companyList = createCompanyList();

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

		ObjectIterator<Settings> settingsIterator = new ObjectIterator<>(createSettingsList());

		boolean[][] completeBitMap = ReductionHelper.createCombinations(allTypeList.size());

		/**
		 * *** Run set of predefined !UNIQUE! combinations and reduce map of all poss. combinations ***
		 */

		for(boolean isLongVersion : new boolean[] {false, true}) {
			for(boolean isAcceptOrders : new boolean[] {false, true}) {

				if(!(isLongVersion && isAcceptOrders)) {
					continue; //TODO complete
				}

				for(int combinationType : new int[] {1, 2, 3, 4, 5, 6}) {

					ArrayList<IFRS16ImportAssignmentType> typeList = new ArrayList<>();
					switch(combinationType) {
					case 1:
						typeList.addAll(generateTypeList1());
						break;
					case 2:
						typeList.addAll(generateTypeList2());
						break;
					case 3:
						typeList.addAll(generateTypeList3());
						break;
					case 4:
						typeList.addAll(generateTypeList4());
						break;
					case 5:
						typeList.addAll(generateTypeList5());
						break;
					case 6:
						typeList.addAll(generateTypeList6());
						break;
					}

					if(!new CheckExecutor() {

						@Override
						protected Combination createCombination(Settings settings) {

							switch(combinationType) {
							case 1:
								return generateCombination1(settings);
							case 2:
								return generateCombination2(settings);
							case 3:
								return generateCombination3(settings);
							case 4:
								return generateCombination4(settings);
							case 5:
								return generateCombination5(settings);
							case 6:
								return generateCombination6(settings);
							default:
								return null;
							}
						}
					}.execute(isLongVersion + " " + isAcceptOrders + " Reduction Check - " + combinationType, companyList, implementedCompany, isLongVersion, isAcceptOrders, settingsIterator.next(), typeList)) {
						return false;
					}

					// After executing each iteration -> remove all combinations that contain the executed combination
					completeBitMap = ReductionHelper.generateReducedBitMap(completeBitMap, ReductionHelper.generateBitmapForTypeList(allTypeList, typeList));

				}
			}
		}

		/**
		 * *** Run remaining combinations ***
		 */
		
		for(boolean isLongVersion : new boolean[] {false, true}) {
			for(boolean isAcceptOrders : new boolean[] {false, true}) {
				int count = 0;
				int countValid = 0;

				boolean skip = false;
				int stopSkip = 0;


				// For each combination still left in the (REDUCED) map of all combinations
				System.out.println("# of leftover combinations " + completeBitMap.length);
				for(boolean[] flags : completeBitMap) {

					count++;

					// ...create the list of types
					ArrayList<IFRS16ImportAssignmentType> typeList = new ArrayList<>();
					for(int index=0; index<flags.length; index++) {
						if(flags[index]) {
							typeList.add(allTypeList.get(index));
						}
					}

					// If the combination is valid...
					if(Util.isValid(isLongVersion, isAcceptOrders, typeList)) {

						countValid++;

						if(skip) {
							if(countValid == stopSkip) {
								skip = false;
							}
							else {
								continue;
							}
						}

						if(!new CheckExecutor() {

							@Override
							protected Combination createCombination(Settings settings) {
								return EngineTestData.this.createCombination(settings, partnerCompanyCodeList);
							}
						}.execute(isLongVersion + " " + isAcceptOrders + " Valid Check - " + countValid + " of " + count, companyList, implementedCompany, isLongVersion, isAcceptOrders, settingsIterator.next(), typeList)) {
							return false;
						}
					}
				}
			}
		}

		return true;
	}



	private enum CombinationValueType {
		Empty,
		Wrong,
		Valid
	}

	private ArrayList<IFRS16ImportAssignmentType> generateTypeList1() {
		return new ArrayList<>(Arrays.asList(
				IFRS16ImportAssignmentType.CONTRACTNUMBER,
				IFRS16ImportAssignmentType.CREDITORNAME,
				IFRS16ImportAssignmentType.CREDITORNUMBER,
				IFRS16ImportAssignmentType.DESIGNATIONLEASEDOBJECT,
				IFRS16ImportAssignmentType.ENDDATEOFCONTRACT,
				IFRS16ImportAssignmentType.PARTNERCOMPANY,
				IFRS16ImportAssignmentType.STARTDATEOFCONTRACT,
				IFRS16ImportAssignmentType.INTERESTRATE,
				IFRS16ImportAssignmentType.PROBABLEENDOFCONTRACT
				));
	}

	private Combination generateCombination1(Settings settings) {

		Combination combination = new Combination();

		// ContractNumber empty, not valid, valid; Others valid
		for(CombinationValueType valueType : CombinationValueType.values()) {
			CombinationLine combinationLine = new CombinationLine();
			combination.getLineList().add(combinationLine);
			for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
				CombinationItem<?> combinationItem = null;
				switch(type.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
					combinationItem = generateCombinationItem(settings, combinationLine, type, valueType);
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

		// ContractNumber valid; Others empty, not valid, valid
		for(CombinationValueType valueType : CombinationValueType.values()) {
			CombinationLine combinationLine = new CombinationLine();
			combination.getLineList().add(combinationLine);
			for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
				CombinationItem<?> combinationItem = null;
				switch(type.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
					combinationItem = createCombinationItem(settings, combinationLine, type);
					break;
				default:
					combinationItem = generateCombinationItem(settings, combinationLine, type, valueType);
					break;
				}
				if(combinationItem != null) {
					combinationLine.getDataMap().put(type, combinationItem);
				}
			}
		}

		return combination;
	}

	private List<IFRS16ImportAssignmentType> generateTypeList2() {
		return new ArrayList<>(Arrays.asList(
				IFRS16ImportAssignmentType.CONTRACTNUMBER,
				IFRS16ImportAssignmentType.GROUPPOSITION,
				IFRS16ImportAssignmentType.CONDITIONTYPE,
				IFRS16ImportAssignmentType.FROMDATE,
				IFRS16ImportAssignmentType.COSTCENTER
				));
	}

	private Combination generateCombination2(Settings settings) {

		Combination combination = new Combination();

		// ContractNumber, GroupPosition, ConditionType valid; Others empty, not valid, valid
		for(CombinationValueType valueType : CombinationValueType.values()) {
			CombinationLine combinationLine = new CombinationLine();
			combination.getLineList().add(combinationLine);
			for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
				CombinationItem<?> combinationItem = null;
				switch(type.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
				case IFRS16ImportAssignmentType._GROUPPOSITION:
				case IFRS16ImportAssignmentType._CONDITIONTYPE:
					combinationItem = createCombinationItem(settings, combinationLine, type);
					break;
				default:
					combinationItem = generateCombinationItem(settings, combinationLine, type, valueType);
					break;
				}
				if(combinationItem != null) {
					combinationLine.getDataMap().put(type, combinationItem);
				}
			}
		}

		// All combination of GroupPosition-ConditionType; Others valid
		ArrayList<ConcreteAccount> concreteAccountList = new ArrayList<>();
		concreteAccountList.add(null);
		concreteAccountList.addAll(getConcreteAccountList());
		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = new ArrayList<>();
		ifrs16ConditionTypeList.add(null);
		ifrs16ConditionTypeList.addAll(getIFRS16ConditionTypeList());
		for(ConcreteAccount concreteAccount : concreteAccountList) {
			for(IFRS16ConditionType ifrs16ConditionType : ifrs16ConditionTypeList) {
				CombinationLine combinationLine = new CombinationLine();
				combination.getLineList().add(combinationLine);
				for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
					CombinationItem<?> combinationItem = null;
					switch(type.getShortValue()) {
					case IFRS16ImportAssignmentType._CONTRACTNUMBER:
						combinationItem = createCombinationItem(settings, combinationLine, type);
						break;
					case IFRS16ImportAssignmentType._GROUPPOSITION:
						if(concreteAccount == null) {
							combinationItem = CombinationItem.getNewCombinationItem("");
						}
						else {
							combinationItem = createCombinationItemConcreteAccount(concreteAccount, settings);
						}
						break;
					case IFRS16ImportAssignmentType._CONDITIONTYPE:
						if(ifrs16ConditionType == null) {
							combinationItem = CombinationItem.getNewCombinationItem("");
						}
						else {
							combinationItem = createCombinationItemConditionType(ifrs16ConditionType, settings);
						}
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

		return combination;
	}

	private List<IFRS16ImportAssignmentType> generateTypeList3() {
		return new ArrayList<>(Arrays.asList(
				IFRS16ImportAssignmentType.CONTRACTNUMBER,
				IFRS16ImportAssignmentType.GROUPPOSITION,
				IFRS16ImportAssignmentType.CONDITIONTYPE,
				IFRS16ImportAssignmentType.FROMDATE,
				IFRS16ImportAssignmentType.ORDER
				));
	}

	private Combination generateCombination3(Settings settings) {

		Combination combination = new Combination();

		// ContractNumber, GroupPosition, ConditionType, FromDate valid; Others empty, not valid, valid
		for(CombinationValueType valueType : CombinationValueType.values()) {
			CombinationLine combinationLine = new CombinationLine();
			combination.getLineList().add(combinationLine);
			for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
				CombinationItem<?> combinationItem = null;
				switch(type.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
				case IFRS16ImportAssignmentType._GROUPPOSITION:
				case IFRS16ImportAssignmentType._CONDITIONTYPE:
				case IFRS16ImportAssignmentType._FROMDATE:
					combinationItem = createCombinationItem(settings, combinationLine, type);
					break;
				default:
					combinationItem = generateCombinationItem(settings, combinationLine, type, valueType);
					break;
				}
				if(combinationItem != null) {
					combinationLine.getDataMap().put(type, combinationItem);
				}
			}
		}

		return combination;
	}

	private List<IFRS16ImportAssignmentType> generateTypeList4() {
		return new ArrayList<>(Arrays.asList(
				IFRS16ImportAssignmentType.CONTRACTNUMBER,
				IFRS16ImportAssignmentType.GROUPPOSITION,
				IFRS16ImportAssignmentType.CONDITIONTYPE,
				IFRS16ImportAssignmentType.FROMDATE,
				IFRS16ImportAssignmentType.COSTCENTER,
				IFRS16ImportAssignmentType.ORDER
				));
	}

	private Combination generateCombination4(Settings settings) {

		Combination combination = new Combination();

		// ContractNumber, GroupPosition, ConditionType, FromDate valid; Others empty, not valid, valid
		for(CombinationValueType valueTypeCostCenter : CombinationValueType.values()) {
			for(CombinationValueType valueTypeOrder : CombinationValueType.values()) {
				CombinationLine combinationLine = new CombinationLine();
				combination.getLineList().add(combinationLine);
				for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
					CombinationItem<?> combinationItem = null;
					switch(type.getShortValue()) {
					case IFRS16ImportAssignmentType._CONTRACTNUMBER:
					case IFRS16ImportAssignmentType._GROUPPOSITION:
					case IFRS16ImportAssignmentType._CONDITIONTYPE:
					case IFRS16ImportAssignmentType._FROMDATE:
						combinationItem = createCombinationItem(settings, combinationLine, type);
						break;
					case IFRS16ImportAssignmentType._COSTCENTER:
						combinationItem = generateCombinationItem(settings, combinationLine, type, valueTypeCostCenter);
						break;
					case IFRS16ImportAssignmentType._ORDER:
						combinationItem = generateCombinationItem(settings, combinationLine, type, valueTypeOrder);
						break;
					}
					if(combinationItem != null) {
						combinationLine.getDataMap().put(type, combinationItem);
					}
				}
			}
		}

		return combination;
	}

	private List<IFRS16ImportAssignmentType> generateTypeList5() {
		return new ArrayList<>(Arrays.asList(
				IFRS16ImportAssignmentType.CONTRACTNUMBER,
				IFRS16ImportAssignmentType.GROUPPOSITION,
				IFRS16ImportAssignmentType.CONDITIONTYPE,
				IFRS16ImportAssignmentType.FROMDATE,
				IFRS16ImportAssignmentType.COSTCENTERTYPE,
				IFRS16ImportAssignmentType.COSTCENTERORORDER
				));
	}

	private Combination generateCombination5(Settings settings) {

		Combination combination = new Combination();

		// ContractNumber, GroupPosition, ConditionType, FromDate valid; Others empty, not valid, valid
		ArrayList<IFRS16CostCenterType> ifrs16CostCenterTypeList = new ArrayList<>();
		ifrs16CostCenterTypeList.add(null);
		ifrs16CostCenterTypeList.addAll(IFRS16CostCenterType.getAllValues());
		for(IFRS16CostCenterType ifrs16CostCenterType : ifrs16CostCenterTypeList) {
			for(CombinationValueType valueTypeCostCenterOrOrder : CombinationValueType.values()) {
				CombinationLine combinationLine = new CombinationLine();
				combination.getLineList().add(combinationLine);
				for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
					CombinationItem<?> combinationItem = null;
					switch(type.getShortValue()) {
					case IFRS16ImportAssignmentType._CONTRACTNUMBER:
					case IFRS16ImportAssignmentType._GROUPPOSITION:
					case IFRS16ImportAssignmentType._CONDITIONTYPE:
					case IFRS16ImportAssignmentType._FROMDATE:
						combinationItem = createCombinationItem(settings, combinationLine, type);
						break;
					case IFRS16ImportAssignmentType._COSTCENTERTYPE:
						if(ifrs16CostCenterType == null) {
							combinationItem = CombinationItem.getNewCombinationItem("");
						}
						else {
							switch(ifrs16CostCenterType.getShortValue()) {
							case IFRS16CostCenterType._NULL:
								combinationItem = CombinationItem.getNewCombinationItem("Abc");
								break;
							default:
								combinationItem = createCombinationItem(settings, combinationLine, type);
								break;
							}
						}
						break;
					case IFRS16ImportAssignmentType._COSTCENTERORORDER:
						combinationItem = generateCombinationItem(settings, combinationLine, type, valueTypeCostCenterOrOrder);
						break;
					}
					if(combinationItem != null) {
						combinationLine.getDataMap().put(type, combinationItem);
					}
				}
			}
		}

		return combination;
	}

	private List<IFRS16ImportAssignmentType> generateTypeList6() {
		return new ArrayList<>(Arrays.asList(
				IFRS16ImportAssignmentType.CONTRACTNUMBER,
				IFRS16ImportAssignmentType.GROUPPOSITION,
				IFRS16ImportAssignmentType.CONDITIONTYPE,
				IFRS16ImportAssignmentType.FROMDATE,
				IFRS16ImportAssignmentType.COSTCENTER,
				IFRS16ImportAssignmentType.PAYMENTCYCLE,
				IFRS16ImportAssignmentType.PAYMENTDATETYPE,
				IFRS16ImportAssignmentType.AMOUNTWITHOUTVALUEADDEDTAX,
				IFRS16ImportAssignmentType.UNTILDATE,
				IFRS16ImportAssignmentType.VATRATETYPE
				));
	}

	private Combination generateCombination6(Settings settings) {

		Combination combination = new Combination();

		// ContractNumber, GroupPosition, ConditionType, FromDate, CostCenter valid; Others empty, not valid, valid
		for(CombinationValueType valueType : CombinationValueType.values()) {
			CombinationLine combinationLine = new CombinationLine();
			combination.getLineList().add(combinationLine);
			for(IFRS16ImportAssignmentType type : settings.getTypeList()) {
				CombinationItem<?> combinationItem = null;
				switch(type.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
				case IFRS16ImportAssignmentType._GROUPPOSITION:
				case IFRS16ImportAssignmentType._CONDITIONTYPE:
				case IFRS16ImportAssignmentType._FROMDATE:
				case IFRS16ImportAssignmentType._COSTCENTER:
					combinationItem = createCombinationItem(settings, combinationLine, type);
					break;
				default:
					combinationItem = generateCombinationItem(settings, combinationLine, type, valueType);
					break;
				}
				if(combinationItem != null) {
					combinationLine.getDataMap().put(type, combinationItem);
				}
			}
		}

		return combination;
	}

	private CombinationItem<?> generateCombinationItem(Settings settings, CombinationLine combinationLine, IFRS16ImportAssignmentType type, CombinationValueType valueType) {
		switch(valueType) {
		case Empty:
			return CombinationItem.getNewCombinationItem("");
		case Wrong:
			switch(type.getShortValue()) {
			case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._CONDITIONTYPE:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._CONTRACTNUMBER:
				return createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER+1);
			case IFRS16ImportAssignmentType._COSTCENTER:
				return createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER+1);
			case IFRS16ImportAssignmentType._COSTCENTERORORDER:
				return createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER+1);
			case IFRS16ImportAssignmentType._COSTCENTERTYPE:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._CREDITORNAME:
				return createCombinationItemString(Constants.MAX_LENGHT_CREDITORNAME+1);
			case IFRS16ImportAssignmentType._CREDITORNUMBER:
				return createCombinationItemString(Constants.MAX_LENGHT_CREDITORNUMBER+1);
			case IFRS16ImportAssignmentType._DESIGNATIONLEASEDOBJECT:
				return createCombinationItemString(Constants.MAX_LENGHT_DESIGNATIONLEASEDOBJECT+1);
			case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._FROMDATE:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._GROUPPOSITION:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._INTERESTRATE:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._ORDER:
				return createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER+1);
			case IFRS16ImportAssignmentType._PARTNERCOMPANY:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._PAYMENTCYCLE:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._UNTILDATE:
				return CombinationItem.getNewCombinationItem("Abc");
			case IFRS16ImportAssignmentType._VATRATETYPE:
				return CombinationItem.getNewCombinationItem("Abc");
			}
			break;
		case Valid:
			return createCombinationItem(settings, combinationLine, type);
		}
		return null;
	}


	private ArrayList<Company> createCompanyList() {

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
		localeList.add(Locale.GERMAN);
		localeList.add(Locale.ENGLISH);

		ArrayList<DigitGroupingSymbol> digitGroupingSymbolList = new ArrayList<>(DigitGroupingSymbol.getAllValues());

		ArrayList<DigitGroupingSymbol> decimalSeparatorList = new ArrayList<>();
		//TODO		decimalSeparatorList.add(DigitGroupingSymbol.NULL);
		decimalSeparatorList.add(DigitGroupingSymbol.COMMA);
		decimalSeparatorList.add(DigitGroupingSymbol.POINT);

		ArrayList<String> dateFormatList = new ArrayList<>();
		dateFormatList.add(Constants.SETTTING_DATE_DE);
		dateFormatList.add(Constants.SETTTING_DATE_EN);

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

		ArrayList<IFRS16CostCenterType> ifrs16CostCenterTypeList = new ArrayList<>();
		if(settings.getTypeList().contains(IFRS16ImportAssignmentType.COSTCENTERTYPE)) {
			ifrs16CostCenterTypeList.addAll(IFRS16CostCenterType.getAllValues());
			ifrs16CostCenterTypeList.remove(IFRS16CostCenterType.NULL);
		}
		else {
			ifrs16CostCenterTypeList.add(null);
		}

		boolean hasCostCenterAndOrders = settings.getTypeList().contains(IFRS16ImportAssignmentType.COSTCENTER) && settings.getTypeList().contains(IFRS16ImportAssignmentType.ORDER);
		int[] costCenterAndOrdersModes = hasCostCenterAndOrders ? new int[] {0, 1, 2} : new int[] {0};

		Combination combination = new Combination();

		for(ConcreteAccount concreteAccount : concreteAccountList) {
			for(IFRS16ConditionType ifrs16ConditionType : ifrs16ConditionTypeList) {

				if(!hasGroupPositionAndConditionType || Util.isValid(concreteAccount.getCode(), ifrs16ConditionType.getCode())) {

					for(IFRS16PaymentCycle ifrs16PaymentCycle : ifrs16PaymentCycleList) {
						for(IFRS16PaymentDateType ifrs16PaymentDateType : ifrs16PaymentDateTypeList) {
							for(IFRS16VATRateType ifrs16vatRateType : ifrs16vatRateTypeList) {
								for(IFRS16CostCenterType ifrs16CostCenterType : ifrs16CostCenterTypeList) {
									for(int costCenterAndOrdersMode : costCenterAndOrdersModes) {


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
												combinationItem = createCombinationItemString(Constants.MAX_LENGHT_CONTRACTNUMBER);
												break;
											case IFRS16ImportAssignmentType._COSTCENTER:
												if(settings.getTypeList().contains(IFRS16ImportAssignmentType.ORDER)) {
													switch(costCenterAndOrdersMode) {
													case 0:
													case 1:
														combinationItem = createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
														break;
													default:
														combinationItem = CombinationItem.getNewCombinationItem("");
														break;
													}
												}
												else {
													combinationItem = createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
												}
												break;
											case IFRS16ImportAssignmentType._COSTCENTERORORDER:
												combinationItem = createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
												break;
											case IFRS16ImportAssignmentType._COSTCENTERTYPE:
												combinationItem = createCombinationItemIFRS16CostCenterType(ifrs16CostCenterType, settings);
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
												combinationItem = createCombinationItemDate(settings, true);
												break;
											case IFRS16ImportAssignmentType._FROMDATE:
												combinationItem = createCombinationItemDate(settings, false);
												break;
											case IFRS16ImportAssignmentType._GROUPPOSITION:
												combinationItem = createCombinationItemConcreteAccount(concreteAccount, settings);
												break;
											case IFRS16ImportAssignmentType._INTERESTRATE:
												combinationItem = createCombinationItemDouble(Constants.DECIMALS_INTERESTRATE, settings);
												break;
											case IFRS16ImportAssignmentType._ORDER:
												if(settings.getTypeList().contains(IFRS16ImportAssignmentType.COSTCENTER)) {
													switch(costCenterAndOrdersMode) {
													case 1:
													case 2:
														combinationItem = createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
														break;
													default:
														combinationItem = CombinationItem.getNewCombinationItem("");
														break;
													}
												}
												else {
													combinationItem = createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
												}
												break;
												//											case IFRS16ImportAssignmentType._PARTNERCOMPANY:
												//												combinationItem = createCombinationItemString(partnerCompanyCodeList);
												//												break;
											case IFRS16ImportAssignmentType._PAYMENTCYCLE:
												combinationItem = createCombinationItemIFRS16PaymentCycle(ifrs16PaymentCycle, settings);
												break;
											case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
												combinationItem = createCombinationItemIFRS16PaymentDateType(ifrs16PaymentDateType, settings);
												break;
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
												combinationItem = createCombinationItemIFRS16VATRateType(ifrs16vatRateType, settings);
												break;
											}
											if(combinationItem != null) {
												combinationLine.getDataMap().put(type, combinationItem);
											}
										}

										if(MAX_ROW_COUNT != -1 && combination.getLineList().size() == MAX_ROW_COUNT * (hasCostCenterAndOrders ? 3 : 1)) {
											return combination;
										}
									}
								}
							}
						}
					}
				}
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
		case IFRS16ImportAssignmentType._COSTCENTERORORDER:
			return createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
		case IFRS16ImportAssignmentType._COSTCENTERTYPE:
			return createCombinationItemIFRS16CostCenterType(settings);
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
		case IFRS16ImportAssignmentType._ORDER:
			return createCombinationItemString(Constants.MAX_LENGHT_COSTCENTER);
		case IFRS16ImportAssignmentType._PARTNERCOMPANY:
			return createCombinationItemPartnerCode();
		case IFRS16ImportAssignmentType._PAYMENTCYCLE:
			return createCombinationItemIFRS16PaymentCycle(settings);
		case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
			return createCombinationItemIFRS16PaymentDateType(settings);
		case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
			return createCombinationItemDate(settings, false);
		case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
			return createCombinationItemDate(settings, false);
		case IFRS16ImportAssignmentType._UNTILDATE:
			return createCombinationItemDate(settings, false);
		case IFRS16ImportAssignmentType._VATRATETYPE:
			return createCombinationItemIFRS16VATRateType(settings);
		default:
			return null;
		}
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
			case IFRS16ImportAssignmentType._ORDER:
				maxLenght = Constants.MAX_LENGHT_COSTCENTER;
				break;
			case IFRS16ImportAssignmentType._COSTCENTERORORDER:
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

	private Combination createCombinationUnparsable(Settings settings) {
		Combination combination = new Combination();

		ArrayList<ConcreteAccount> concreteAccountList = getConcreteAccountList();
		concreteAccountList.add(null);
		ArrayList<IFRS16ConditionType> ifrs16ConditionTypeList = getIFRS16ConditionTypeList();
		ifrs16ConditionTypeList.add(null);
		for(IFRS16ImportAssignmentType typeExceedingText : settings.getTypeList()) {

			switch(typeExceedingText.getShortValue()) {
			case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
			case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
			case IFRS16ImportAssignmentType._FROMDATE:
				//TODO			case IFRS16ImportAssignmentType._INTERESTRATE:
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

	private Combination createCombinationNotEditable(ArrayList<Company> companyList, Settings settings) {

		Combination combination = new Combination();

		for(boolean change : new boolean[] {true, false}) {
			for(IFRS16ImportAssignmentType typeNotEditable : settings.getTypeList()) {
				switch(typeNotEditable.getShortValue()) {
				case IFRS16ImportAssignmentType._CONTRACTNUMBER:
				case IFRS16ImportAssignmentType._CONDITIONTYPE://TODO
				case IFRS16ImportAssignmentType._FROMDATE://TODO
				case IFRS16ImportAssignmentType._COSTCENTER://TODO
				case IFRS16ImportAssignmentType._ORDER://TODO
				case IFRS16ImportAssignmentType._COSTCENTERORORDER://TODO
				case IFRS16ImportAssignmentType._COSTCENTERTYPE://TODO
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

					IFRS16Contract ifrs16Contract = new Checker().createIFRS16Contract(companyList, settings, combinationLine);
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

	private Combination createCombinationHistorical(ArrayList<Company> companyList, Settings settings) {

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
				case IFRS16ImportAssignmentType._ORDER://TODO
				case IFRS16ImportAssignmentType._COSTCENTERORORDER://TODO
				case IFRS16ImportAssignmentType._COSTCENTERTYPE://TODO
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
					IFRS16Contract ifrs16Contract = new Checker().createIFRS16Contract(companyList, settings, combinationLine);
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

	private HashMap<Integer, DoubleGenerator> doubleGeneratorMap = new HashMap<>();
	private CombinationItem<Double> createCombinationItemDouble(int decimals, Settings settings) {
		if(!doubleGeneratorMap.containsKey(decimals)) {
			doubleGeneratorMap.put(decimals, new DoubleGenerator(decimals));
		}
		return CombinationItem.getNewCombinationItem(doubleGeneratorMap.get(decimals).next(), settings);
	}

	private CombinationItem<IFRS16ConditionType> createCombinationItemConditionType(IFRS16ConditionType ifrs16ConditionType, Settings settings) {
		return CombinationItem.getNewCombinationItem(ifrs16ConditionType, settings);
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

	private HashMap<Integer, StringGenerator> stringGeneratorMap = new HashMap<>();
	private CombinationItem<String> createCombinationItemString(int maxLenght) {
		if(!stringGeneratorMap.containsKey(maxLenght)) {
			stringGeneratorMap.put(maxLenght, new StringGenerator(maxLenght));
		}
		return CombinationItem.getNewCombinationItem(stringGeneratorMap.get(maxLenght).next());
	}

	private ObjectIterator<String> stringIterator;
	private CombinationItem<String> createCombinationItemString(ArrayList<String> stringList) {
		if(stringIterator == null) {
			stringIterator = new ObjectIterator<>(stringList);
		}
		return CombinationItem.getNewCombinationItem(stringIterator.next());
	}

	private ObjectIterator<String> partnerCodeIterator;
	private CombinationItem<String> createCombinationItemPartnerCode() {
		if(partnerCodeIterator == null) {
			ArrayList<String> partnerCodeList = new ArrayList<>();
			for(String partnerCode : COMPANY_CODES) {
				if(!partnerCode.equals(COMPANY_CODE_IMPORT)) {
					partnerCodeList.add(partnerCode);
				}
			}
			partnerCodeIterator = new ObjectIterator<>(partnerCodeList);
		}
		return CombinationItem.getNewCombinationItem(partnerCodeIterator.next());
	}

	private HashMap<Boolean, DateGenerator> dateGeneratorMap = new HashMap<>();
	private CombinationItem<Long> createCombinationItemDate(Settings settings, boolean endDate) {
		if(!dateGeneratorMap.containsKey(endDate)) {
			dateGeneratorMap.put(endDate, new DateGenerator(endDate));
		}
		return CombinationItem.getNewCombinationItem(dateGeneratorMap.get(endDate).next(), settings);
	}

	private CombinationItem<ConcreteAccount> createCombinationItemConcreteAccount(ConcreteAccount concreteAccount, Settings settings) {
		return CombinationItem.getNewCombinationItem(concreteAccount, settings);
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

	private CombinationItem<IFRS16PaymentCycle> createCombinationItemIFRS16PaymentCycle(IFRS16PaymentCycle ifrs16PaymentCycle, Settings settings) {
		return CombinationItem.getNewCombinationItem(ifrs16PaymentCycle, settings);
	}

	private ObjectIterator<IFRS16PaymentCycle> IFRS16PaymentCycleIterator;
	private CombinationItem<IFRS16PaymentCycle> createCombinationItemIFRS16PaymentCycle(Settings settings) {
		if(IFRS16PaymentCycleIterator == null) {
			ArrayList<IFRS16PaymentCycle> ifrs16PaymentCycleList = new ArrayList<>(IFRS16PaymentCycle.getAllValues());
			ifrs16PaymentCycleList.remove(IFRS16PaymentCycle.NULL);
			IFRS16PaymentCycleIterator = new ObjectIterator<>(ifrs16PaymentCycleList);
		}
		return createCombinationItemIFRS16PaymentCycle(IFRS16PaymentCycleIterator.next(), settings);
	}

	private CombinationItem<IFRS16PaymentDateType> createCombinationItemIFRS16PaymentDateType(IFRS16PaymentDateType ifrs16PaymentDateType, Settings settings) {
		return CombinationItem.getNewCombinationItem(ifrs16PaymentDateType, settings);
	}

	private ObjectIterator<IFRS16PaymentDateType> IFRS16PaymentDateTypeIterator;
	private CombinationItem<IFRS16PaymentDateType> createCombinationItemIFRS16PaymentDateType(Settings settings) {
		if(IFRS16PaymentDateTypeIterator == null) {
			ArrayList<IFRS16PaymentDateType> ifrs16PaymentDateTypeList = new ArrayList<>(IFRS16PaymentDateType.getAllValues());
			ifrs16PaymentDateTypeList.remove(IFRS16PaymentDateType.NULL);
			IFRS16PaymentDateTypeIterator = new ObjectIterator<>(ifrs16PaymentDateTypeList);
		}
		return createCombinationItemIFRS16PaymentDateType(IFRS16PaymentDateTypeIterator.next(), settings);
	}

	private CombinationItem<IFRS16VATRateType> createCombinationItemIFRS16VATRateType(IFRS16VATRateType ifrs16vatRateType, Settings settings) {
		return CombinationItem.getNewCombinationItem(ifrs16vatRateType, settings);
	}

	private ObjectIterator<IFRS16VATRateType> IFRS16VATRateTypeIterator;
	private CombinationItem<IFRS16VATRateType> createCombinationItemIFRS16VATRateType(Settings settings) {
		if(IFRS16VATRateTypeIterator == null) {
			ArrayList<IFRS16VATRateType> ifrs16vatRateTypeList = new ArrayList<>(IFRS16VATRateType.getAllValues());
			ifrs16vatRateTypeList.remove(IFRS16VATRateType.NULL);
			IFRS16VATRateTypeIterator = new ObjectIterator<>(ifrs16vatRateTypeList);
		}
		return createCombinationItemIFRS16VATRateType(IFRS16VATRateTypeIterator.next(), settings);
	}

	private CombinationItem<IFRS16CostCenterType> createCombinationItemIFRS16CostCenterType(IFRS16CostCenterType ifrs16CostCenterType, Settings settings) {
		return CombinationItem.getNewCombinationItem(ifrs16CostCenterType, settings);
	}

	private ObjectIterator<IFRS16CostCenterType> IFRS16CostCenterTypeIterator;
	private CombinationItem<IFRS16CostCenterType> createCombinationItemIFRS16CostCenterType(Settings settings) {
		if(IFRS16CostCenterTypeIterator == null) {
			ArrayList<IFRS16CostCenterType> ifrs16CostCenterTypeList = new ArrayList<>(IFRS16CostCenterType.getAllValues());
			ifrs16CostCenterTypeList.remove(IFRS16CostCenterType.NULL);
			IFRS16CostCenterTypeIterator = new ObjectIterator<>(ifrs16CostCenterTypeList);
		}
		return createCombinationItemIFRS16CostCenterType(IFRS16CostCenterTypeIterator.next(), settings);
	}




	private abstract class CheckExecutor {

		protected boolean execute(String checkName, ArrayList<Company> companyList, ImplementedCompany implementedCompany, boolean isLongVersion, boolean isAcceptOrders, Settings settingsTemplate, ArrayList<IFRS16ImportAssignmentType> typeList) {

			// Clone the Settings
			Settings settings = cloneSettings(implementedCompany, isLongVersion, isAcceptOrders, settingsTemplate, typeList);

			// Create the Combination
			Combination combination = createCombination(settings);
			if(combination != null && combination.getLineList().size() > 0) {

				// Create the scenario
				Scenario scenario = new Scenario(companyList, combination, settings);

				// Compose the Scenario
				System.out.println(checkName + ": " + combination.getLineList().size() + " lines, " + settings.getTypeList().size() + " columns");
				return TEST ? check(scenario) : true;
			}

			return true;
		}

		protected abstract Combination createCombination(Settings settings);

		private Settings cloneSettings(ImplementedCompany implementedCompany, boolean isLongVersion, boolean isAcceptOrders, Settings settingsTemplate, ArrayList<IFRS16ImportAssignmentType> typeList) {
			Settings settings = new Settings(settingsTemplate.getLocale(), settingsTemplate.getDigitGroupingSymbol(), settingsTemplate.getDecimalSeparator(), settingsTemplate.getDateFormat());
			settings.setImplementedCompany(implementedCompany);
			settings.setLongVersion(isLongVersion);
			settings.setAcceptOrders(isAcceptOrders);
			settings.setYear(YEAR);
			settings.setMonth(MONTH);
			settings.setFilePath(EngineTestData.class.getSimpleName() + ".csv");
			settings.setName(EngineTestData.class.getSimpleName());
			settings.getTypeList().addAll(typeList);
			return settings;
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
				_next.set(Calendar.YEAR, 			YEAR);
				_next.set(Calendar.MONTH, 			MONTH);
			}
			else {
				_next.set(Calendar.YEAR, 			2015);
				_next.set(Calendar.MONTH, 			Calendar.JANUARY);
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
}
