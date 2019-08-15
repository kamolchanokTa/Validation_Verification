package com.wuerth.phoenix.cis.university.example2.util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import com.wuerth.phoenix.cis.university.example2.adapters.Company;
import com.wuerth.phoenix.cis.university.example2.adapters.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16Condition;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ConditionData;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ConditionType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16Contract;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ContractData;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16CostCenterType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ImportAssignmentType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16PaymentCycle;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16PaymentDateType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16VATRateType;
import com.wuerth.phoenix.cis.university.example2.adapters.ImplementedCompany;
import com.wuerth.phoenix.cis.university.example2.util.Constants.IFRS16ImportCompanyDataColumnSectionType;

public class CombinationParser {
	
	private Settings settings;
	private boolean merged; 
	private boolean afterImport;
	private Checker checker;
	
	private ArrayList<IFRS16Contract> ifrs16ContractList = new ArrayList<>();

	private HashMap<String, IFRS16Contract> ifrs16ContractIdMap = new HashMap<>();
	private HashMap<String, IFRS16ContractData> ifrs16ContractDataIdMap = new HashMap<>();
	private HashMap<String, IFRS16Condition> ifrs16ConditionIdMap = new HashMap<>();
	private HashMap<String, IFRS16ConditionData> ifrs16ConditionDataIdMap = new HashMap<>();
	
	private HashSet<String> ifrs16ContractIdErrorSet = new HashSet<>();
	private HashSet<String> ifrs16ContractDataIdErrorSet = new HashSet<>();
	private HashSet<String> ifrs16ConditionIdErrorSet = new HashSet<>();
	private HashSet<String> ifrs16ConditionDataIdErrorSet = new HashSet<>();
	
	
	public CombinationParser(Settings settings, boolean merged, boolean afterImport, Checker checker) {
		this.settings = settings;
		this.merged = merged;
		this.afterImport = afterImport;
		this.checker = checker;
	}
	
	
	/**
	 * The list of the cached data
	 * @return the cached data
	 */
	public ArrayList<IFRS16Contract> getIFRS16ContractList() {
		return ifrs16ContractList;
	}
	
	
	/**
	 * Create an IFRS16Contract from a CombinationList and add it to the cached list
	 * @param companyList the existing company list 
	 * @param combinationLine the CombinationLine
	 */
	public void addIFRS16Contract(ArrayList<Company> companyList, CombinationLine combinationLine) {
		
		boolean isLongVersion = settings.isLongVersion();
		boolean isAcceptOrders = settings.isAcceptOrders();
		int year = settings.getYear();
		int month = settings.getMonth();
		

		HashSet<IFRS16ImportCompanyDataColumnSectionType> columnSectionTypeSet = new HashSet<>();
		if(afterImport) {
			for(IFRS16ImportAssignmentType type : IFRS16ImportAssignmentType.getAllValues()) {
				if(hasType(type)) {
					//TODO don't add section type for empty cell
					columnSectionTypeSet.add(Util.getColumnSectionType(type));
				}
			}
		}

		
		/*
		 * IFRS16Contract
		 */
		
		ConcreteAccount concreteAccount = getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.GROUPPOSITION, ConcreteAccount.class, null);
		IFRS16ConditionType ifrs16ConditionType = getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.CONDITIONTYPE, IFRS16ConditionType.class, null);

		if(afterImport) {
			
			// Error: NoGroupPosition
			if(concreteAccount == null && ifrs16ConditionType != null) {
				return;
			}
		}
		
		String contractNumber = getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.CONTRACTNUMBER, String.class, null);
		
		if(afterImport) {
			
			// Error: 'Contract Number' Empty
			if(contractNumber == null || contractNumber.length() == 0) {
				return;
			}
			
			// Error: 'Contract Number' ExceedingText
			if(contractNumber != null && contractNumber.length() > Constants.MAX_LENGHT_CONTRACTNUMBER) {
				return;
			}
		}

		IFRS16Contract ifrs16Contract = new IFRS16Contract();
		ifrs16Contract.setConcreteAccount(concreteAccount);
		ifrs16Contract.setContractEnd(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.ENDDATEOFCONTRACT, Long.class, null));
		ifrs16Contract.setContractNumber(contractNumber);
		ifrs16Contract.setContractStart(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.STARTDATEOFCONTRACT, Long.class, null));
		ifrs16Contract.setCreditorName(manageExceedingText(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.CREDITORNAME, String.class, null), IFRS16ImportAssignmentType.CREDITORNAME));
		ifrs16Contract.setCreditorNumber(manageExceedingText(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.CREDITORNUMBER, String.class, null), IFRS16ImportAssignmentType.CREDITORNUMBER));
		ifrs16Contract.setLeasedObject(manageExceedingText(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.DESIGNATIONLEASEDOBJECT, String.class, null), IFRS16ImportAssignmentType.DESIGNATIONLEASEDOBJECT));
		
		String partnerCompany = getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.PARTNERCOMPANY, String.class, null);
		boolean partnerCompanyExists = false;
		for(Company company : companyList) {
			if(company.getCode().equals(partnerCompany)) {
				partnerCompanyExists = true;
				break;
			}
		}
		ifrs16Contract.setPartnerCompany(partnerCompanyExists ? partnerCompany : "");
		
		String ifrs16ContractId = checker.getId(ifrs16Contract);
		
		IFRS16CostCenterType ifrs16CostCenterType = manageNullValue(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.COSTCENTERTYPE, IFRS16CostCenterType.class, null), IFRS16CostCenterType.NULL, IFRS16ImportAssignmentType.COSTCENTERTYPE, isLongVersion, isAcceptOrders);
		String costCenter = manageExceedingText(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.COSTCENTER, String.class, null), IFRS16ImportAssignmentType.COSTCENTER);
		String order = manageExceedingText(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.ORDER, String.class, null), IFRS16ImportAssignmentType.ORDER);
		String costCenterOrOrder = manageExceedingText(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.COSTCENTERORORDER, String.class, null), IFRS16ImportAssignmentType.COSTCENTERORORDER);
		String costCenterOrOrderId = null;

		if(ifrs16CostCenterType == null || IFRS16CostCenterType.NULL.equals(ifrs16CostCenterType)) {

			boolean hasCostCenter = costCenter != null && costCenter.length() > 0;
			boolean existCostCenter = hasType(IFRS16ImportAssignmentType.COSTCENTER);

			if(isAcceptOrders) {

				// CostCenterOrOrders
				if(hasType(IFRS16ImportAssignmentType.COSTCENTERORORDER)) {

					if(hasType(IFRS16ImportAssignmentType.COSTCENTERTYPE) && hasCombinationItem(combinationLine, IFRS16ImportAssignmentType.COSTCENTERTYPE) && !isEmpty(combinationLine, IFRS16ImportAssignmentType.COSTCENTERTYPE, IFRS16CostCenterType.class) && !isParsable(combinationLine, IFRS16ImportAssignmentType.COSTCENTERTYPE, IFRS16CostCenterType.class)) {
						ifrs16CostCenterType = null;
						costCenterOrOrderId = costCenterOrOrder;
					}
					else {
						ifrs16CostCenterType = IFRS16CostCenterType.NULL;
						costCenterOrOrderId = costCenterOrOrder;
					}
					
					if(afterImport) {
						// Error: CostCenterAndOrders
						ifrs16ContractIdErrorSet.add(ifrs16ContractId);
					}
				}
				
				// CostCenter or Orders
				else {

					boolean hasOrder = order != null && order.length() > 0;
					boolean existOrder = hasType(IFRS16ImportAssignmentType.ORDER);
					if(hasCostCenter == hasOrder) {
						
						// CostCenter and Order
						if(hasCostCenter && hasOrder) {

							// Error: CostCenterAndOrders
							ifrs16ContractIdErrorSet.add(ifrs16ContractId);
							
							ifrs16CostCenterType = afterImport ? IFRS16CostCenterType.NULL : null;
							costCenterOrOrderId = "";
						}
						
						// No CostCenter and No Order
						else {
							
							if(existCostCenter && existOrder) {
								if(!hasCostCenter && !hasOrder) {
									ifrs16CostCenterType = null;
									if(afterImport) {
										// Error: No CostCenter and No Order
										ifrs16ContractIdErrorSet.add(ifrs16ContractId);
									}
								}
								else {
									ifrs16CostCenterType = afterImport ? IFRS16CostCenterType.NULL : null;
								}
							}
							else if(!existCostCenter && existOrder) {
								ifrs16CostCenterType = IFRS16CostCenterType.ORDER;
							}
							else if(existCostCenter && !existOrder) {
								ifrs16CostCenterType = IFRS16CostCenterType.COSTCENTER;
							}
							else {
								ifrs16CostCenterType = afterImport ? IFRS16CostCenterType.NULL : null;
							}
							costCenterOrOrderId = "";
						}
					}
					
					// CostCenter or Order
					else {
						ifrs16CostCenterType = hasOrder ? IFRS16CostCenterType.ORDER : IFRS16CostCenterType.COSTCENTER;
						costCenterOrOrderId = hasOrder ? order : costCenter;
					}
				}
			}
			
			else {

				if(hasCostCenter) {
					ifrs16CostCenterType = IFRS16CostCenterType.COSTCENTER;
					costCenterOrOrderId = costCenter;
				}
				else {
					if(existCostCenter) {
						ifrs16CostCenterType = IFRS16CostCenterType.COSTCENTER;
					}
					else {
						if(afterImport) {
							ifrs16CostCenterType = IFRS16CostCenterType.NULL;
						}
						else {
							ifrs16CostCenterType = null;
						}
					}
					costCenterOrOrderId = "";
				}
			}
		}
		else {
			switch(ifrs16CostCenterType.getShortValue()) {
			case IFRS16CostCenterType._COSTCENTER:
				if(costCenterOrOrder != null && costCenterOrOrder.length() > 0) {
					costCenter = costCenterOrOrder;
					order = null;
					costCenterOrOrderId = costCenter;
				}
				break;
			case IFRS16CostCenterType._ORDER:
				if(costCenterOrOrder != null && costCenterOrOrder.length() > 0) {
					costCenter = null;
					order = costCenterOrOrder;
					costCenterOrOrderId = order;
				}
				break;
			}
		}
		ifrs16Contract.setCostCenterType(ifrs16CostCenterType);

		
		if(!ifrs16ContractIdMap.containsKey(ifrs16ContractId)) {
			ifrs16ContractIdMap.put(ifrs16ContractId, ifrs16Contract);
			getIFRS16ContractList().add(ifrs16Contract);
		}
		else {
			if(ifrs16Contract.equal(ifrs16ContractIdMap.get(ifrs16ContractId), false)) {
				ifrs16Contract = ifrs16ContractIdMap.get(ifrs16ContractId);
			}
			else {
				
				// Error: DifferentAttributeContract
				ifrs16ContractIdErrorSet.add(ifrs16ContractId);
				return;
			}
		}

		
		/*
		 * IFRS16ContractData
		 */
		
		boolean forceInterestRate = true;
		if(combinationLine.getDataMap().get(IFRS16ImportAssignmentType.INTERESTRATE) != null) {
			if(!(combinationLine.getDataMap().get(IFRS16ImportAssignmentType.INTERESTRATE).getValue() instanceof Double)) {
				forceInterestRate = false;
			}
		}
		
		IFRS16ContractData ifrs16ContractData = new IFRS16ContractData();
		ifrs16ContractData.setInterestRate(manageNullValue(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.INTERESTRATE, Double.class, 0.0), forceInterestRate));
		ifrs16ContractData.setMonth(month);
		ifrs16ContractData.setProbableContractEnd(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.PROBABLEENDOFCONTRACT, Long.class, null));
		ifrs16ContractData.setYear(year);
		
		boolean addContractData = true;
		if(afterImport) {
			boolean parsable = false;
			if(isEmpty(combinationLine, IFRS16ImportAssignmentType.INTERESTRATE, Double.class) || isParsable(combinationLine, IFRS16ImportAssignmentType.INTERESTRATE, Double.class)) {
				parsable = true;
			}
			if(isParsable(combinationLine, IFRS16ImportAssignmentType.PROBABLEENDOFCONTRACT, Long.class)) {
				parsable = true;
			}
			
			if(isEmpty(combinationLine, IFRS16ImportAssignmentType.CONDITIONTYPE, IFRS16ConditionType.class) || isParsable(combinationLine, IFRS16ImportAssignmentType.CONDITIONTYPE, IFRS16ConditionType.class)) {
				parsable = true;
			}
			if(isParsable(combinationLine, IFRS16ImportAssignmentType.PAYMENTCYCLE, IFRS16PaymentCycle.class)) {
				parsable = true;
			}
			if(isParsable(combinationLine, IFRS16ImportAssignmentType.PAYMENTDATETYPE, IFRS16PaymentDateType.class)) {
				parsable = true;
			}
				
			if(!parsable) {
				addContractData = false;
			}
		}
		if(addContractData) {
			String ifrs16ContractDataId = checker.getId(ifrs16ContractData, ifrs16Contract);
			if(!ifrs16ContractDataIdMap.containsKey(ifrs16ContractDataId)) {
				ifrs16ContractDataIdMap.put(ifrs16ContractDataId, ifrs16ContractData);
				ifrs16Contract.createChildIFRS16ContractData(ifrs16ContractData);
			}
			else {
				if(ifrs16ContractData.equal(ifrs16ContractDataIdMap.get(ifrs16ContractDataId), false)) {
					ifrs16ContractData = ifrs16ContractDataIdMap.get(ifrs16ContractDataId);
				}
				else {

					// Error: DifferentAttributeContractData
					ifrs16ContractDataIdErrorSet.add(ifrs16ContractDataId);
				}
			}
		}
		
		if(afterImport) {
			
			// Error: ConditionTypeNotAvailable
			if(concreteAccount != null && ifrs16ConditionType != null && !Util.isValid(concreteAccount.getCode(), ifrs16ConditionType.getCode())) {
				return;
			}
			
			if(IFRS16CostCenterType.NULL.equals(ifrs16CostCenterType)) {
				if(!columnSectionTypeSet.contains(IFRS16ImportCompanyDataColumnSectionType.Condition)) {
					return;
				}
			}
		}

		
		/*
		 * IFRS16Condition
		 */
		
		IFRS16Condition ifrs16Condition = new IFRS16Condition();
		ifrs16Condition.setIFRS16ConditionType(ifrs16ConditionType);
		ifrs16Condition.setLongVersion(isLongVersion);
		ifrs16Condition.setPaymentCycle(manageNullValue(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.PAYMENTCYCLE, IFRS16PaymentCycle.class, IFRS16PaymentCycle.NULL), IFRS16PaymentCycle.NULL, IFRS16ImportAssignmentType.PAYMENTCYCLE, isLongVersion, isAcceptOrders));
		ifrs16Condition.setPaymentDateType(manageNullValue(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.PAYMENTDATETYPE, IFRS16PaymentDateType.class, IFRS16PaymentDateType.NULL), IFRS16PaymentDateType.NULL, IFRS16ImportAssignmentType.PAYMENTDATETYPE, isLongVersion, isAcceptOrders));
		
		Long fromDate = getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.FROMDATE, Long.class, null);
		String ifrs16ConditionId = checker.getId(ifrs16Condition, ifrs16Contract, fromDate, costCenterOrOrderId);
		if(!ifrs16ConditionIdMap.containsKey(ifrs16ConditionId)) {
			ifrs16ConditionIdMap.put(ifrs16ConditionId, ifrs16Condition);
			ifrs16Contract.createChildIFRS16Condition(ifrs16Condition);
		}
		else {
			if(ifrs16Condition.equal(ifrs16ConditionIdMap.get(ifrs16ConditionId), false)) {
				ifrs16Condition = ifrs16ConditionIdMap.get(ifrs16ConditionId);
			}
			else {
				
				// Error: DifferentAttributeCondition
				ifrs16ConditionIdErrorSet.add(ifrs16ConditionId);
				return;
			}
		}
		
		if(afterImport) {
			if(!columnSectionTypeSet.contains(IFRS16ImportCompanyDataColumnSectionType.ConditionData)) {
				return;
			}
		}

		
		/*
		 * IFRS16ConditionData
		 */
		
		IFRS16ConditionData ifrs16ConditionData = new IFRS16ConditionData();
		ifrs16ConditionData.setAmountWithoutVAT(manageNullValue(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.AMOUNTWITHOUTVALUEADDEDTAX, Double.class, 0.0), false));
		ifrs16ConditionData.setCostCenter(costCenter);
		ifrs16ConditionData.setFromDate(fromDate);
		ifrs16ConditionData.setMonth(month);
		ifrs16ConditionData.setOrder(order);
		ifrs16ConditionData.setUntilDate(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.UNTILDATE, Long.class, null));
		ifrs16ConditionData.setVATRateType(manageNullValue(getCombinationItemValue(combinationLine, IFRS16ImportAssignmentType.VATRATETYPE, IFRS16VATRateType.class, IFRS16VATRateType.NULL), IFRS16VATRateType.NULL, IFRS16ImportAssignmentType.VATRATETYPE, isLongVersion, isAcceptOrders));
		ifrs16ConditionData.setYear(year);

		boolean addConditionData = true;
		if(afterImport) {
			boolean parsable = false;
			if(isParsable(combinationLine, IFRS16ImportAssignmentType.AMOUNTWITHOUTVALUEADDEDTAX, Double.class)) {
				parsable = true;
			}
			if(costCenter != null && costCenter.length() > 0) {
				parsable = true;
			}
			if(fromDate != null) {
				parsable = true;
			}
			if(order != null && order.length() > 0) {
				parsable = true;
			}
			if(isParsable(combinationLine, IFRS16ImportAssignmentType.UNTILDATE, Long.class)) {
				parsable = true;
			}
			if(isParsable(combinationLine, IFRS16ImportAssignmentType.VATRATETYPE, IFRS16VATRateType.class)) {
				parsable = true;
			}
				
			if(!parsable) {
				addConditionData = false;
			}
		}
		if(addConditionData) {
			String ifrs16ConditionDataId = checker.getId(ifrs16ConditionData, ifrs16Condition);
			if(!ifrs16ConditionDataIdMap.containsKey(ifrs16ConditionDataId)) {
				ifrs16ConditionDataIdMap.put(ifrs16ConditionDataId, ifrs16ConditionData);
				ifrs16Condition.createChildIFRS16ConditionData(ifrs16ConditionData);
			}
			else {
				if(ifrs16ConditionData.equal(ifrs16ConditionDataIdMap.get(ifrs16ConditionDataId), false)) {
					ifrs16ConditionData = ifrs16ConditionDataIdMap.get(ifrs16ConditionDataId);
				}
				else {
					ifrs16ConditionDataIdErrorSet.add(ifrs16ConditionDataId);
					
					// Error: DifferentAttributeConditionData
					if(!isLongVersion) {
						ifrs16ConditionIdErrorSet.add(ifrs16ConditionId);
					}
				}
			}
		}
	}
	
	
	/**
	 * Manage the cached data
	 * @param companyList the list of the companies
	 */
	public void manageCachedData(ArrayList<Company> companyList) {
		
		ImplementedCompany implementedCompany = null;
		if(companyList != null) {
			for(Company company : companyList) {
				if(company.getCode().equals(settings.getImplementedCompany().getParentCompany().getCode())) {
					implementedCompany = company.lookupImplementedCompany(settings.getImplementedCompany().getType());
					break;
				}
			}
		}

		if(merged) {

			// Replace the data with the 'historical' data, if existing
			replaceWithHistoricalData(implementedCompany);

			// Set the 'not null' data for the 'null' AEnumerator data
			setNotNullValue(implementedCompany);

			// Overwrite the not editable data with the existing data
			overwriteNotEditableData(implementedCompany);
			
			// Manage the CostCenter and Order values on merge
			manageCostCenter(implementedCompany, true);
		}
		
		else {
			
			// Cache the existing 'Condition' data not included into the import file
			addExistingCondition(implementedCompany);
			
			// Manage the CostCenter and Order values on parse
			manageCostCenter(implementedCompany, false);
		}
		
		if(afterImport) {
			
			// Remove the wrong data from the cache
			removeWrongData();

			// Cache the existing data not included into the import file
			addExistingData(implementedCompany);
			
			// Manage the CostCenter value
			manageCostCenterAfterImport(implementedCompany);
		}
	}
	
	
	/**
	 * Remove the wrong data from the cache
	 */
	private void removeWrongData() {
		
		// IFRS16Contract
		for(IFRS16Contract ifrs16Contract : new ArrayList<>(getIFRS16ContractList())) {
			if(ifrs16ContractIdErrorSet.contains(checker.getId(ifrs16Contract))) {
				getIFRS16ContractList().remove(ifrs16Contract);
			}
			else {
				
				// IFRS16ContractData
				for(IFRS16ContractData ifrs16ContractData : new ArrayList<>(ifrs16Contract.getAllChildIFRS16ContractData())) {
					if(ifrs16ContractDataIdErrorSet.contains(checker.getId(ifrs16ContractData))) {
						ifrs16Contract.deleteChildIFRS16ContractData(ifrs16ContractData);
					}
				}
				
				// IFRS16Condition
				for(IFRS16Condition ifrs16Condition : new ArrayList<>(ifrs16Contract.getAllChildIFRS16Condition())) {
					if(ifrs16ConditionIdErrorSet.contains(checker.getId(ifrs16Condition))) {
						ifrs16Contract.deleteChildIFRS16Condition(ifrs16Condition);
					}
					else {
						
						// IFRS16ConditionData
						for(IFRS16ConditionData ifrs16ConditionData : new ArrayList<>(ifrs16Condition.getAllChildIFRS16ConditionData())) {
							if(ifrs16ConditionDataIdErrorSet.contains(checker.getId(ifrs16ConditionData))) {
								ifrs16Condition.deleteChildIFRS16ConditionData(ifrs16ConditionData);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Replace the data with the 'historical' data, if existing
	 * @param implementedCompany the target ImplementedCompany
	 */
	private void replaceWithHistoricalData(ImplementedCompany implementedCompany) {
	
		Calendar calendarImport = GregorianCalendar.getInstance();
		calendarImport.set(Calendar.DAY_OF_MONTH, 	1);
		calendarImport.set(Calendar.YEAR, 			settings.getYear());
		calendarImport.set(Calendar.MONTH, 			settings.getMonth());
		calendarImport.set(Calendar.HOUR_OF_DAY, 	0);
		calendarImport.set(Calendar.MINUTE, 		0);
		calendarImport.set(Calendar.SECOND, 		0);
		calendarImport.set(Calendar.MILLISECOND, 	0);

		// IFRS16Contract
		for(IFRS16Contract ifrs16Contract : new ArrayList<>(getIFRS16ContractList())) {
			IFRS16Contract ifrs16ContractExisting = implementedCompany.lookupIFRS16Contract(ifrs16Contract.getContractNumber());
			if(ifrs16ContractExisting != null && ifrs16ContractExisting.getContractEnd() != null) {
				Calendar calendarContractEnd = GregorianCalendar.getInstance();
				calendarContractEnd.setTimeInMillis(ifrs16ContractExisting.getContractEnd());
				if(calendarContractEnd.before(calendarImport)) {
					getIFRS16ContractList().remove(ifrs16Contract);
					getIFRS16ContractList().add(ifrs16ContractExisting.clone());
				}
			}
		}
	}
	
	/**
	 * Set the 'not null' data for the 'null' AEnumerator data
	 * @param implementedCompany the target ImplementedCompany
	 */
	private void setNotNullValue(ImplementedCompany implementedCompany) {
		
		// IFRS16Contract
		for(IFRS16Contract ifrs16Contract : new ArrayList<>(getIFRS16ContractList())) {
			IFRS16Contract ifrs16ContractExisting = implementedCompany.lookupIFRS16Contract(ifrs16Contract.getContractNumber());
			if(ifrs16ContractExisting != null) {
				
				// IFRS16Condition
				for(IFRS16Condition ifrs16Condition : ifrs16Contract.getAllChildIFRS16Condition()) {
					if(ifrs16Condition.getPaymentCycle() == null) {
						ifrs16Condition.setPaymentCycle(IFRS16PaymentCycle.NULL);
					}
					if(ifrs16Condition.getPaymentDateType() == null) {
						ifrs16Condition.setPaymentDateType(IFRS16PaymentDateType.NULL);
					}
					
					// IFRS16ConditionData
					for(IFRS16ConditionData ifrs16ConditionData : ifrs16Condition.getAllChildIFRS16ConditionData()) {
						if(ifrs16ConditionData.getVATRateType() == null) {
							ifrs16ConditionData.setVATRateType(IFRS16VATRateType.NULL);
						}
					}
				}
			}
		}
	}
	
	/**
	 * Overwrite the not editable data with the existing data
	 * @param implementedCompany the target ImplementedCompany
	 */
	private void overwriteNotEditableData(ImplementedCompany implementedCompany) {
		
		// IFRS16Contract
		for(IFRS16Contract ifrs16Contract : new ArrayList<>(getIFRS16ContractList())) {
			IFRS16Contract ifrs16ContractExisting = implementedCompany.lookupIFRS16Contract(ifrs16Contract.getContractNumber());
			if(ifrs16ContractExisting != null) {
				if(!ifrs16Contract.equal(ifrs16ContractExisting, true)) {
					ifrs16Contract.overwrite(ifrs16ContractExisting);
				}
				else {
					
					// IFRS16ContractData
					for(IFRS16ContractData ifrs16ContractData : new ArrayList<>(ifrs16Contract.getAllChildIFRS16ContractData())) {
						IFRS16ContractData ifrs16ContractDataExisting = ifrs16ContractExisting.lookupIFRS16ContractData(ifrs16ContractData.getYear(), ifrs16ContractData.getMonth());
						if(ifrs16ContractDataExisting != null && !ifrs16ContractData.equal(ifrs16ContractDataExisting, true)) {
							ifrs16ContractData.overwrite(ifrs16ContractDataExisting);
						}
					}
				
					HashMap<String, IFRS16Condition> conditionMap = new HashMap<>();
					for(IFRS16Condition ifrs16Condition : ifrs16Contract.getAllChildIFRS16Condition()) {
						conditionMap.put(checker.getId(ifrs16Condition), ifrs16Condition);
					}
					HashMap<String, IFRS16Condition> conditionExistingMap = new HashMap<>();
					for(IFRS16Condition ifrs16Condition : ifrs16ContractExisting.getAllChildIFRS16Condition()) {
						conditionExistingMap.put(checker.getId(ifrs16Condition), ifrs16Condition);
					}
					
					// IFRS16Condition
					for(Entry<String, IFRS16Condition> entry : conditionMap.entrySet()) {
						IFRS16Condition ifrs16Condition = entry.getValue();
						IFRS16Condition ifrs16ConditionExisting = conditionExistingMap.get(entry.getKey());
						if(ifrs16ConditionExisting != null) {
							if(!ifrs16Condition.equal(ifrs16ConditionExisting, true)) {
								ifrs16Condition.overwrite(ifrs16ConditionExisting);
							}
							
							// IFRS16ConditionData
							for(IFRS16ConditionData ifrs16ConditionData : new ArrayList<>(ifrs16Condition.getAllChildIFRS16ConditionData())) {
								IFRS16ConditionData ifrs16ConditionDataExisting = ifrs16ConditionExisting.lookupIFRS16ConditionData(ifrs16ConditionData.getYear(), ifrs16ConditionData.getMonth());
								if(ifrs16ConditionDataExisting != null && !ifrs16ConditionData.equal(ifrs16ConditionDataExisting, true)) {
									ifrs16ConditionData.overwrite(ifrs16ConditionDataExisting);
								}
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Cache the existing 'Condition' data not included into the import file
	 * @param implementedCompany the target ImplementedCompany
	 */
	private void addExistingCondition(ImplementedCompany implementedCompany) {
		
		HashMap<String, IFRS16Contract> ifrs16ContractMap = new HashMap<>();
		for(IFRS16Contract ifrs16Contract : getIFRS16ContractList()) {
			ifrs16ContractMap.put(ifrs16Contract.getContractNumber(), ifrs16Contract);
		}
		
		// IFRS16Contract
		for(IFRS16Contract ifrs16ContractExisting : implementedCompany.getAllChildIFRS16Contract()) {
			IFRS16Contract ifrs16Contract = ifrs16ContractMap.get(ifrs16ContractExisting.getContractNumber());
			if(ifrs16Contract != null) {
				
				HashMap<String, IFRS16Condition> conditionExistingMap = new HashMap<>();
				for(IFRS16Condition ifrs16Condition : ifrs16ContractExisting.getAllChildIFRS16Condition()) {
					conditionExistingMap.put(checker.getId(ifrs16Condition), ifrs16Condition);
				}
				HashMap<String, IFRS16Condition> conditionMap = new HashMap<>();
				for(IFRS16Condition ifrs16Condition : ifrs16Contract.getAllChildIFRS16Condition()) {
					conditionMap.put(checker.getId(ifrs16Condition), ifrs16Condition);
				}

				// IFRS16Condition
				for(Entry<String, IFRS16Condition> entry : conditionExistingMap.entrySet()) {
					IFRS16Condition ifrs16ConditionExisting = entry.getValue();
					IFRS16Condition ifrs16Condition = conditionMap.get(entry.getKey());
					if(ifrs16Condition == null) {
						ifrs16Contract.createChildIFRS16Condition(ifrs16ConditionExisting.clone());
					}
					else {
						
						// IFRS16ConditionData
						for(IFRS16ConditionData ifrs16ConditionDataExisting : ifrs16ConditionExisting.getAllChildIFRS16ConditionData()) {
							if(ifrs16Condition.lookupIFRS16ConditionData(ifrs16ConditionDataExisting.getYear(), ifrs16ConditionDataExisting.getMonth()) == null) {
								ifrs16Condition.createChildIFRS16ConditionData(ifrs16ConditionDataExisting.clone());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Cache the existing data not included into the import file
	 * @param implementedCompany the target ImplementedCompany
	 */
	private void addExistingData(ImplementedCompany implementedCompany) {
		
		HashMap<String, IFRS16Contract> ifrs16ContractMap = new HashMap<>();
		for(IFRS16Contract ifrs16Contract : getIFRS16ContractList()) {
			ifrs16ContractMap.put(ifrs16Contract.getContractNumber(), ifrs16Contract);
		}
		
		// IFRS16Contract
		for(IFRS16Contract ifrs16ContractExisting : implementedCompany.getAllChildIFRS16Contract()) {
			IFRS16Contract ifrs16Contract = ifrs16ContractMap.get(ifrs16ContractExisting.getContractNumber());
			if(ifrs16Contract == null) {
				getIFRS16ContractList().add(ifrs16ContractExisting.clone());
			}
			else {
				
				// IFRS16ContractData
				for(IFRS16ContractData ifrs16ContractDataExisting : ifrs16ContractExisting.getAllChildIFRS16ContractData()) {
					if(ifrs16Contract.lookupIFRS16ContractData(ifrs16ContractDataExisting.getYear(), ifrs16ContractDataExisting.getMonth()) == null) {
						ifrs16Contract.createChildIFRS16ContractData(ifrs16ContractDataExisting.clone());
					}
				}
			
				HashMap<String, IFRS16Condition> conditionExistingMap = new HashMap<>();
				for(IFRS16Condition ifrs16Condition : ifrs16ContractExisting.getAllChildIFRS16Condition()) {
					conditionExistingMap.put(checker.getId(ifrs16Condition), ifrs16Condition);
				}
				HashMap<String, IFRS16Condition> conditionMap = new HashMap<>();
				for(IFRS16Condition ifrs16Condition : ifrs16Contract.getAllChildIFRS16Condition()) {
					conditionMap.put(checker.getId(ifrs16Condition), ifrs16Condition);
				}
				
				// IFRS16Condition
				for(Entry<String, IFRS16Condition> entry : conditionExistingMap.entrySet()) {
					IFRS16Condition ifrs16ConditionExisting = entry.getValue();
					IFRS16Condition ifrs16Condition = conditionMap.get(entry.getKey());
					if(ifrs16Condition == null) {
						ifrs16Contract.createChildIFRS16Condition(ifrs16ConditionExisting.clone());
					}
					else {
						
						// IFRS16ConditionData
						for(IFRS16ConditionData ifrs16ConditionDataExisting : ifrs16ConditionExisting.getAllChildIFRS16ConditionData()) {
							if(ifrs16Condition.lookupIFRS16ConditionData(ifrs16ConditionDataExisting.getYear(), ifrs16ConditionDataExisting.getMonth()) == null) {
								ifrs16Condition.createChildIFRS16ConditionData(ifrs16ConditionDataExisting.clone());
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Manage the CostCenter and Order values
	 * @param implementedCompany the target ImplementedCompany
	 * @param onMerge true (on merge) false (on parse)
	 */
	private void manageCostCenter(ImplementedCompany implementedCompany, boolean onMerge) {
		
		// IFRS16Contract
		for(IFRS16Contract ifrs16Contract : new ArrayList<>(getIFRS16ContractList())) {
			
			// IFRS16Condition
			for(IFRS16Condition ifrs16Condition : ifrs16Contract.getAllChildIFRS16Condition()) {
				
				// IFRS16ConditionData
				for(IFRS16ConditionData ifrs16ConditionData : ifrs16Condition.getAllChildIFRS16ConditionData()) {
					
					if(ifrs16Contract.getCostCenterType() == null || IFRS16CostCenterType.NULL.equals(ifrs16Contract.getCostCenterType())) {
						boolean hasOrder = ifrs16ConditionData.getOrder() != null && ifrs16ConditionData.getOrder().length() > 0;
						boolean hasCostCenter = ifrs16ConditionData.getCostCenter() != null && ifrs16ConditionData.getCostCenter().length() > 0;
						if(hasOrder != hasCostCenter) {
							ifrs16Contract.setCostCenterType(hasOrder ? IFRS16CostCenterType.ORDER : IFRS16CostCenterType.COSTCENTER);
						}
						if(onMerge) {
							if(hasOrder && hasCostCenter) {
								ifrs16ConditionData.setCostCenter(null);
								ifrs16ConditionData.setOrder(null);
							}
						}
					}
				}
			}
		}
	}
	
	/**
	 * Manage the CostCenter and Order values
	 * @param implementedCompany the target ImplementedCompany
	 */
	private void manageCostCenterAfterImport(ImplementedCompany implementedCompany) {
		
		// IFRS16Contract
		for(IFRS16Contract ifrs16Contract : new ArrayList<>(getIFRS16ContractList())) {
			
			// IFRS16Condition
			for(IFRS16Condition ifrs16Condition : ifrs16Contract.getAllChildIFRS16Condition()) {
				
				// IFRS16ConditionData
				for(IFRS16ConditionData ifrs16ConditionData : ifrs16Condition.getAllChildIFRS16ConditionData()) {
					
					if(ifrs16Contract.getCostCenterType() != null) {
						switch(ifrs16Contract.getCostCenterType().getShortValue()) {
						case IFRS16CostCenterType._ORDER:
							ifrs16ConditionData.setCostCenter(ifrs16ConditionData.getOrder());
							ifrs16ConditionData.setOrder(null);
							break;
						}
					}
				}
			}
		}
	}

	
	/*
	 * COMBINATION 
	 */
	
	private boolean hasType(IFRS16ImportAssignmentType type) {
		return settings.getTypeList().contains(type);
	}
	
	private boolean hasCombinationItem(CombinationLine combinationLine, IFRS16ImportAssignmentType type) {
		if(hasType(type)) {
			return combinationLine.getDataMap().get(type) != null;
		}
		return false;
	}
	
	private <E> boolean isEmpty(CombinationLine combinationLine, IFRS16ImportAssignmentType type, Class<E> classs) {
		if(hasCombinationItem(combinationLine, type)) {
			CombinationItem<?> combinationItem = combinationLine.getDataMap().get(type);
			if(combinationItem.getName() != null && combinationItem.getName().length() == 0) {
				return true;
			}
		}
		return false;
	}

	private <E> boolean isParsable(CombinationLine combinationLine, IFRS16ImportAssignmentType type, Class<E> classs) {
		if(hasCombinationItem(combinationLine, type)) {
			try {
				combinationLine.getDataMap().get(type).getValue(classs);
				return true;
			}
			catch(ClassCastException exception) {

			}
		}
		return false;
	}
	
	/**
	 * Get the value of a CombinationItem
	 * @param combinationLine the CombinationLine
	 * @param type the type of the data
	 * @param classs the class of the value
	 * @param valueOnEmpty the value to return on empty
	 * @return the value or null
	 */
	private <E> E getCombinationItemValue(CombinationLine combinationLine, IFRS16ImportAssignmentType type, Class<E> classs, E valueOnEmpty) {
		CombinationItem<?> combinationItem = combinationLine.getDataMap().get(type);
		if(combinationItem == null) {
			if(hasType(type)) {
				switch(type.getShortValue()) {
				case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
					return (E)Double.valueOf(0);
				case IFRS16ImportAssignmentType._PAYMENTCYCLE:
					return (E)IFRS16PaymentCycle.NULL;
				case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
					return (E)IFRS16PaymentDateType.NULL;
				case IFRS16ImportAssignmentType._VATRATETYPE:
					return (E)IFRS16VATRateType.NULL;
				case IFRS16ImportAssignmentType._COSTCENTERTYPE:
					return (E)IFRS16CostCenterType.NULL;
				}
			}
		}
		else {

			if(combinationItem.getName() != null && combinationItem.getName().length() == 0 && valueOnEmpty != null) {
				return valueOnEmpty;
			}

			try {
				return combinationItem.getValue(classs);
			}
			catch(ClassCastException exception) {
				return null;
			}
		}
		return null;
	}
	
	/**
	 * Manage a Double 'null' values
	 * @param value the value
	 * @param forceNotNull true (force the return as 'not null') false (manage the return)
	 * @return the managed value
	 */
	private Double manageNullValue(Double value, boolean forceNotNull) {
		if(forceNotNull) {
			return value == null ? 0.0 : value;
		}
		else {
			if(merged) {
				if(afterImport) {
					return value == null ? 0.0 : value;
				}
				else {
					return value;
				}
			}
			else {
				return value;
			}
		}
	}
	
	/**
	 * Manage a generic 'null' values
	 * @param value the value
	 * @param defaultValue the default value
	 * @param type the type of the data
	 * @param isLongVersion true (long version) false (short version)
	 * @param isAcceptOrders true (accept orders) false (not accept orders)
	 * @return the managed value
	 */
	private <E> E manageNullValue(E value, E defaultValue, IFRS16ImportAssignmentType type, boolean isLongVersion, boolean isAcceptOrders) {
		if(merged) {
			if(afterImport) {
				return value == null ? defaultValue : value;
			}
			else {
				return Util.isAvailable(type, isLongVersion, isAcceptOrders) ? value : (hasType(type) ? defaultValue : value);
			}
		}
		else {
			return value;
		}
	}

	/**
	 * Manage an 'exceeding' text
	 * @param text the text
	 * @param type the type of the data
	 * @return the managed text
	 */
	private String manageExceedingText(String text, IFRS16ImportAssignmentType type) {
		if(text != null) {
			int maxLenght = -1;
			switch(type.getShortValue()) {
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
				text = text.length() > maxLenght ? text.substring(0, maxLenght) : text;
			}
		}
		return text;
	}
}
