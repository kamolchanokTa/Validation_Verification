package com.wuerth.phoenix.cis.university.example2.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;

import com.wuerth.phoenix.cis.university.example2.adapters.AEnumerator;
import com.wuerth.phoenix.cis.university.example2.adapters.Company;
import com.wuerth.phoenix.cis.university.example2.adapters.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16Condition;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ConditionData;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ConditionType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16Contract;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ContractData;
import com.wuerth.phoenix.cis.university.example2.adapters.ImplementedCompany;

public class Checker {

	protected static final String CONTAINERNAME_CONTRACT 			= "IFRS16Contract";
	protected static final String CONTAINERNAME_CONTRACTDATA 		= "IFRS16ContractData";
	protected static final String CONTAINERNAME_CONDITION 			= "IFRS16Condition";
	protected static final String CONTAINERNAME_CONDITIONDATA 		= "IFRS16ConditionData";
	protected static final String CONTAINERNAME_COMPANY 			= "Company";
	protected static final String CONTAINERNAME_IMPLEMENTEDCOMPANY 	= "ImplementedCompany";
	protected static final String CONTAINERNAME_CONCRETEACCOUNT 	= "ConcreteAccount";
	protected static final String CONTAINERNAME_CONDITIONTYPE 		= "IFRS16ConditionType";
	
	protected static final String ATTRIBUTENAME_CONTRACT_ContractEnd 				= "ContractEnd";
	protected static final String ATTRIBUTENAME_CONTRACT_ContractNumber 			= "ContractNumber";
	protected static final String ATTRIBUTENAME_CONTRACT_ContractStart 				= "ContractStart";
	protected static final String ATTRIBUTENAME_CONTRACT_CostCenterType				= "CostCenterType";
	protected static final String ATTRIBUTENAME_CONTRACT_CreditorName 				= "CreditorName";
	protected static final String ATTRIBUTENAME_CONTRACT_CreditorNumber 			= "CreditorNumber";
	protected static final String ATTRIBUTENAME_CONTRACT_LeasedObject 				= "LeasedObject";
	protected static final String ATTRIBUTENAME_CONTRACT_PartnerCompany 			= "PartnerCompany";
	protected static final String ATTRIBUTENAME_CONTRACTDATA_InterestRate 			= "InterestRate";
	protected static final String ATTRIBUTENAME_CONTRACTDATA_Month 					= "Month";
	protected static final String ATTRIBUTENAME_CONTRACTDATA_ProbableContractEnd 	= "ProbableContractEnd";
	protected static final String ATTRIBUTENAME_CONTRACTDATA_Year 					= "Year";
	protected static final String ATTRIBUTENAME_CONDITION_LongVersion 				= "LongVersion";
	protected static final String ATTRIBUTENAME_CONDITION_PaymentCycle 				= "PaymentCycle";
	protected static final String ATTRIBUTENAME_CONDITION_PaymentDateType 			= "PaymentDateType";
	protected static final String ATTRIBUTENAME_CONDITIONDATA_AmountWithoutVAT 		= "getAmountWithoutVAT";
	protected static final String ATTRIBUTENAME_CONDITIONDATA_CostCenter 			= "getCostCenter";
	protected static final String ATTRIBUTENAME_CONDITIONDATA_FromDate 				= "getFromDate";
	protected static final String ATTRIBUTENAME_CONDITIONDATA_Month 				= "getMonth";
	protected static final String ATTRIBUTENAME_CONDITIONDATA_Order 				= "getOrder";
	protected static final String ATTRIBUTENAME_CONDITIONDATA_UntilDate 			= "getUntilDate";
	protected static final String ATTRIBUTENAME_CONDITIONDATA_VATRateType 			= "getVATRateType";
	protected static final String ATTRIBUTENAME_CONDITIONDATA_Year 					= "getYear";
	protected static final String ATTRIBUTENAME_COMPANY_Code 						= "Code";
	protected static final String ATTRIBUTENAME_IMPLEMENTEDCOMPANY_Type 			= "Type";
	protected static final String ATTRIBUTENAME_CONCRETEACCOUNT_Code 				= "Code";
	protected static final String ATTRIBUTENAME_CONDITIONTYPE_Code 					= "Code";

	protected static final String ID_SEPARATOR = "|";


	/**
	 * Create a Container containing the existing data 
	 * @param companyList the list of the company
	 * @return the Container
	 */
	protected Container createContainer(ArrayList<Company> companyList) {
		Container container = createContainerRoot();
		
		for(Company company : companyList) {
			Container containerCompany = new Container(CONTAINERNAME_COMPANY, company.getCode(), container);
			
			new Attribute(ATTRIBUTENAME_COMPANY_Code, format(company.getCode()), containerCompany);
		
			for(ImplementedCompany implementedCompany : company.getAllChildImplementedCompany()) {
				Container containerImplementedCompany = new Container(CONTAINERNAME_IMPLEMENTEDCOMPANY, implementedCompany.getType().getAsText(), containerCompany);
				
				new Attribute(ATTRIBUTENAME_IMPLEMENTEDCOMPANY_Type, format(implementedCompany.getType()), containerImplementedCompany);
			
				fillContainer(implementedCompany.getAllChildIFRS16Contract(), containerImplementedCompany);
			}
		}
		
		return container;
	}
	
	/**
	 * Fill a Container with IFRS16Contract data
	 * @param ifrs16ContractCollection the list of IFRS16Contract
	 * @param containerImplementedCompany the the Container of a 'ImplementedCompany'
	 */
	private void fillContainer(Collection<IFRS16Contract> ifrs16ContractCollection, Container containerImplementedCompany) {
		
		ArrayList<IFRS16Contract> ifrs16ContractList = new ArrayList<>(ifrs16ContractCollection);
		Collections.sort(ifrs16ContractList, new Comparator<IFRS16Contract>() {

			@Override
			public int compare(IFRS16Contract o1, IFRS16Contract o2) {
				return format(o1.getContractNumber()).compareTo(format(o2.getContractNumber()));
			}
		});
		for(IFRS16Contract ifrs16Contract : ifrs16ContractList) {
			Container containerIFRS16Contract = new Container(CONTAINERNAME_CONTRACT, getId(ifrs16Contract), containerImplementedCompany);

			new Attribute(ATTRIBUTENAME_CONTRACT_ContractEnd, format(ifrs16Contract.getContractEnd()), containerIFRS16Contract);
			new Attribute(ATTRIBUTENAME_CONTRACT_ContractNumber, format(ifrs16Contract.getContractNumber()), containerIFRS16Contract);
			new Attribute(ATTRIBUTENAME_CONTRACT_ContractStart, format(ifrs16Contract.getContractStart()), containerIFRS16Contract);
			new Attribute(ATTRIBUTENAME_CONTRACT_CostCenterType, format(ifrs16Contract.getCostCenterType()), containerIFRS16Contract);
			new Attribute(ATTRIBUTENAME_CONTRACT_CreditorName, format(ifrs16Contract.getCreditorName()), containerIFRS16Contract);
			new Attribute(ATTRIBUTENAME_CONTRACT_CreditorNumber, format(ifrs16Contract.getCreditorNumber()), containerIFRS16Contract);
			new Attribute(ATTRIBUTENAME_CONTRACT_LeasedObject, format(ifrs16Contract.getLeasedObject()), containerIFRS16Contract);
			new Attribute(ATTRIBUTENAME_CONTRACT_PartnerCompany, format(ifrs16Contract.getPartnerCompany()), containerIFRS16Contract);

			if(ifrs16Contract.getConcreteAccount() != null) {
				Container containerConcreteAccount = new Container(CONTAINERNAME_CONCRETEACCOUNT, getId(ifrs16Contract.getConcreteAccount()), containerIFRS16Contract);
				
				new Attribute(ATTRIBUTENAME_CONCRETEACCOUNT_Code, format(ifrs16Contract.getConcreteAccount().getCode()), containerConcreteAccount);
			}

			for(IFRS16ContractData ifrs16ContractData : ifrs16Contract.getAllChildIFRS16ContractData()) {
				Container containerIFRS16ContractData = new Container(CONTAINERNAME_CONTRACTDATA, getId(ifrs16ContractData), containerIFRS16Contract);
				
				new Attribute(ATTRIBUTENAME_CONTRACTDATA_InterestRate, format(ifrs16ContractData.getInterestRate()), containerIFRS16ContractData);
				new Attribute(ATTRIBUTENAME_CONTRACTDATA_Month, format(ifrs16ContractData.getMonth()), containerIFRS16ContractData);
				new Attribute(ATTRIBUTENAME_CONTRACTDATA_ProbableContractEnd, format(ifrs16ContractData.getProbableContractEnd()), containerIFRS16ContractData);
				new Attribute(ATTRIBUTENAME_CONTRACTDATA_Year, format(ifrs16ContractData.getYear()), containerIFRS16ContractData);
			}
			
			for(IFRS16Condition ifrs16Condition : ifrs16Contract.getAllChildIFRS16Condition()) {
				Container containerIFRS16Condition = new Container(CONTAINERNAME_CONDITION, getId(ifrs16Condition), containerIFRS16Contract);
				
				new Attribute(ATTRIBUTENAME_CONDITION_LongVersion, format(ifrs16Condition.getLongVersion()), containerIFRS16Condition);
				new Attribute(ATTRIBUTENAME_CONDITION_PaymentCycle, format(ifrs16Condition.getPaymentCycle()), containerIFRS16Condition);
				new Attribute(ATTRIBUTENAME_CONDITION_PaymentDateType, format(ifrs16Condition.getPaymentDateType()), containerIFRS16Condition);
				
				if(ifrs16Condition.getIFRS16ConditionType() != null) {
					Container containerIFRS16ConditionType = new Container(CONTAINERNAME_CONDITIONTYPE, getId(ifrs16Condition.getIFRS16ConditionType()), containerIFRS16Condition);
					
					new Attribute(ATTRIBUTENAME_CONDITIONTYPE_Code, format(ifrs16Condition.getIFRS16ConditionType().getCode()), containerIFRS16ConditionType);
				}

				for(IFRS16ConditionData ifrs16ConditionData : ifrs16Condition.getAllChildIFRS16ConditionData()) {
					Container containerIFRS16ConditionData = new Container(CONTAINERNAME_CONDITIONDATA, getId(ifrs16ConditionData), containerIFRS16Condition);
					
					new Attribute(ATTRIBUTENAME_CONDITIONDATA_AmountWithoutVAT, format(ifrs16ConditionData.getAmountWithoutVAT()), containerIFRS16ConditionData);
					new Attribute(ATTRIBUTENAME_CONDITIONDATA_CostCenter, format(ifrs16ConditionData.getCostCenter()), containerIFRS16ConditionData);
					new Attribute(ATTRIBUTENAME_CONDITIONDATA_FromDate, format(ifrs16ConditionData.getFromDate()), containerIFRS16ConditionData);
					new Attribute(ATTRIBUTENAME_CONDITIONDATA_Month, format(ifrs16ConditionData.getMonth()), containerIFRS16ConditionData);
					new Attribute(ATTRIBUTENAME_CONDITIONDATA_Order, format(ifrs16ConditionData.getOrder()), containerIFRS16ConditionData);
					new Attribute(ATTRIBUTENAME_CONDITIONDATA_UntilDate, format(ifrs16ConditionData.getUntilDate()), containerIFRS16ConditionData);
					new Attribute(ATTRIBUTENAME_CONDITIONDATA_VATRateType, format(ifrs16ConditionData.getVATRateType()), containerIFRS16ConditionData);
					new Attribute(ATTRIBUTENAME_CONDITIONDATA_Year, format(ifrs16ConditionData.getYear()), containerIFRS16ConditionData);
				}
			}
		}
	}
	
	/**
	 * Create a Container containing the import data
	 * @param scenario the Scenario
	 * @param merged true (merged date) false (parsed data)
	 * @param afterImport true (data after import) false (data before import)
	 * @return the Container
	 */
	protected Container createContainer(Scenario scenario, boolean merged, boolean afterImport) {
		
		CombinationParser combinationParser = new CombinationParser(scenario.getSettings(), merged, afterImport, this);
		
		for(CombinationLine combinationLine : scenario.getCombination().getLineList()) {
			combinationParser.addIFRS16Contract(scenario.getCompanyList(), combinationLine);	
		}
		
		combinationParser.manageCachedData(scenario.getCompanyList());
		
		Container container = createContainerRoot();

		if(afterImport) {
			
			for(Company company : scenario.getCompanyList()) {
				Container containerCompany = new Container(CONTAINERNAME_COMPANY, company.getCode(), container);
				
				new Attribute(ATTRIBUTENAME_COMPANY_Code, format(company.getCode()), containerCompany);
			
				for(ImplementedCompany implementedCompany : company.getAllChildImplementedCompany()) {
					Container containerImplementedCompany = new Container(CONTAINERNAME_IMPLEMENTEDCOMPANY, implementedCompany.getType().getAsText(), containerCompany);
					
					new Attribute(ATTRIBUTENAME_IMPLEMENTEDCOMPANY_Type, format(implementedCompany.getType()), containerImplementedCompany);
				
					if(scenario.getSettings().getImplementedCompany().getParentCompany().getCode().equals(company.getCode())) {
						if(scenario.getSettings().getImplementedCompany().getType().equals(implementedCompany.getType())) {
							fillContainer(combinationParser.getIFRS16ContractList(), containerImplementedCompany);
						}
					}
				}
			}
		}
		
		else {
			fillContainer(combinationParser.getIFRS16ContractList(), container);
		}

		return container;
	}
	
	/**
	 * Create a IFRS16Contract from a CombinationLine
	 * @param companyList the existing company list
	 * @param settings the Settings
	 * @param combinationLine the CombinationLine
	 * @return the IFRS16Contract
	 */
	public IFRS16Contract createIFRS16Contract(ArrayList<Company> companyList, Settings settings, CombinationLine combinationLine) {
		
		CombinationParser combinationParser = new CombinationParser(settings, true, true, this);
		
		combinationParser.addIFRS16Contract(companyList, combinationLine);	
		
		return combinationParser.getIFRS16ContractList().size() == 0 ? null : combinationParser.getIFRS16ContractList().get(0);
	}

	
	/*
	 * ID
	 */

	/**
	 * Generate the ID for a IFRS16Contract
	 * @param ifrs16Contract the IFRS16Contract
	 * @return the ID
	 */
	protected String getId(IFRS16Contract ifrs16Contract) {
		return format(ifrs16Contract.getContractNumber());
	}

	/**
	 * Generate the ID for a IFRS16ContractData
	 * @param ifrs16ContractData the IFRS16ContractData
	 * @return the ID
	 */
	protected String getId(IFRS16ContractData ifrs16ContractData) {
		return getId(ifrs16ContractData, ifrs16ContractData.getParentIFRS16Contract());
	}

	/**
	 * Generate the ID for a IFRS16ContractData linked to the passed IFRS16Contract
	 * @param ifrs16ContractData the IFRS16ContractData
	 * @param ifrs16Contract the IFRS16Contract
	 * @return the ID
	 */
	protected String getId(IFRS16ContractData ifrs16ContractData, IFRS16Contract ifrs16Contract) {
		return new StringBuffer()
				.append(getId(ifrs16Contract))
				.append(ID_SEPARATOR)
				.append(format(ifrs16ContractData.getYear()))
				.append(ID_SEPARATOR)
				.append(format(ifrs16ContractData.getMonth()))
				.toString();
	}

	/**
	 * Generate the ID for a IFRS16Condition
	 * @param ifrs16Condition the IFRS16Condition
	 * @return the ID
	 */
	protected String getId(IFRS16Condition ifrs16Condition) {
		IFRS16ConditionData ifrs16ConditionData = ifrs16Condition.getAllChildIFRS16ConditionData().size() == 0 ? null : ifrs16Condition.getAllChildIFRS16ConditionData().iterator().next();
		return getId(ifrs16Condition, ifrs16Condition.getParentIFRS16Contract(), ifrs16ConditionData == null ? null : ifrs16ConditionData.getFromDate(), ifrs16ConditionData == null ? null : ifrs16ConditionData.getCostCenterOrOrder());
	}

	/**
	 * Generate the ID for a IFRS16Condition linked to the passed IFRS16Contract, From Date, Cost Center
	 * @param ifrs16Condition the IFRS16Contract
	 * @param ifrs16Contract the IFRS16Contract
	 * @param fromDate the From Date
	 * @param costCenter the Cost Center
	 * @return the ID
	 */
	protected String getId(IFRS16Condition ifrs16Condition, IFRS16Contract ifrs16Contract, Long fromDate, String costCenter) {
		return new StringBuffer()
				.append(getId(ifrs16Contract))
				.append(ID_SEPARATOR)
				.append(ifrs16Condition.getIFRS16ConditionType() == null ? "" : getId(ifrs16Condition.getIFRS16ConditionType()))
				.append(ID_SEPARATOR)
				.append(format(fromDate))
				.append(ID_SEPARATOR)
				.append(format(costCenter))
				.toString();
	}

	/**
	 * Generate the ID for a IFRS16ConditionData
	 * @param ifrs16ConditionData the IFRS16ConditionData
	 * @return the ID
	 */
	protected String getId(IFRS16ConditionData ifrs16ConditionData) {
		return getId(ifrs16ConditionData, ifrs16ConditionData.getParentIFRS16Condition());
	}

	/**
	 * Generate the ID for a IFRS16ConditionData linked to the passed IFRS16Condition
	 * @param ifrs16ConditionData the IFRS16ConditionData
	 * @param ifrs16Condition the IFRS16Condition
	 * @return the ID
	 */
	protected String getId(IFRS16ConditionData ifrs16ConditionData, IFRS16Condition ifrs16Condition) {
		return new StringBuffer()
				.append(getId(ifrs16Condition, ifrs16Condition.getParentIFRS16Contract(), ifrs16ConditionData.getFromDate(), ifrs16ConditionData.getCostCenterOrOrder()))
				.append(ID_SEPARATOR)
				.append(format(ifrs16ConditionData.getYear()))
				.append(ID_SEPARATOR)
				.append(format(ifrs16ConditionData.getMonth()))
				.toString();
	}

	/**
	 * Generate the ID for a ConcreteAccount
	 * @param concreteAccount the ConcreteAccount
	 * @return the ID
	 */
	private String getId(ConcreteAccount concreteAccount) {
		return format(concreteAccount.getCode());
	}

	/**
	 * Generate the ID for a IFRS16ConditionType
	 * @param ifrs16ConditionType the IFRS16ConditionType
	 * @return the ID
	 */
	private String getId(IFRS16ConditionType ifrs16ConditionType) {
		return format(ifrs16ConditionType.getCode());
	}

	
	/*
	 * FORMATTER
	 */

	/**
	 * Format a Boolean value to a text
	 * @param value the value or null
	 * @return the formatted text
	 */
	protected String format(Boolean value) {
		return value == null ? "" : value.toString();
	}

	/**
	 * Format an Integer value to a text
	 * @param value the value or null
	 * @return the formatted text
	 */
	protected String format(Integer value) {
		return value == null ? "" : value.toString();
	}

	/**
	 * Format a Long value to a text
	 * @param value the value or null
	 * @return the formatted text
	 */
	protected String format(Long value) {
		return value == null ? "" : value.toString();
	}

	/**
	 * Format a Double value to a text
	 * @param value the value or null
	 * @return the formatted text
	 */
	protected String format(Double value) {
		return value == null ? "" : value.toString();
	}

	/**
	 * Format a String value to a text
	 * @param value the value or null
	 * @return the formatted text
	 */
	protected String format(String value) {
		return value == null ? "" : value;
	}

	/**
	 * Format an AEnumerator value to a text
	 * @param value the value or null
	 * @return the formatted text
	 */
	protected String format(AEnumerator value) {
		return value == null ? "" : value.getAsText();
	}

	
	/*
	 * CONTAINER
	 */
	
	/**
	 * Create the ROOT Container
	 * @return the Container
	 */
	protected Container createContainerRoot() {
		return new Container("ROOT", "ROOT", null);
	}
	
	
	/**
	 * This class provides to collect the data about a class
	 */
	protected class Container implements Comparable<Container> {
		
		private String name;
		private String id;
		
		private HashSet<Attribute> attributeSet = new HashSet<>();
		private Container parent;
		private HashSet<Container> children = new HashSet<>();
		
		public Container(String name, String id, Container parent) {
			this.name = name;
			this.id = id;
			this.parent = parent;
			if(parent != null) {
				this.parent.children.add(this);
			}
		}

		public ArrayList<String> toLineList() {
			return toLineList(0);
		}

		private ArrayList<String> toLineList(int deep) {
			
			ArrayList<String> lineList = new ArrayList<>();
			lineList.add(getIndentation(deep) + toString());
			
			ArrayList<Attribute> attributeList = new ArrayList<>(this.attributeSet);
			Collections.sort(attributeList);
			for(Attribute attribute : attributeList) {
				lineList.add(getIndentation(deep) + "-" + attribute.toString());
			}
			
			ArrayList<Container> children = new ArrayList<>(this.children);
			Collections.sort(children);
			for(Container child : children) {
				lineList.addAll(child.toLineList(deep+1));
			}
			
			return lineList;
		}
		
		private String getIndentation(int deep) {
			StringBuffer buffer = new StringBuffer();
			for(int index=0; index<deep; index++) {
				buffer.append("---");
			}
			return buffer.toString();
		}

		@Override
		public String toString() {
			return new StringBuffer()
					.append("C: ")
					.append("name='").append(name).append("'")
					.append(",")
					.append("id='").append(id).append("'")
					.toString();
		}

		@Override
		public int compareTo(Container o) {
			int ret = Boolean.compare(name == null, o.name == null);
			if(ret == 0 && name != null) {
				ret = name.compareToIgnoreCase(o.name);
				if(ret == 0) {
					ret = id.compareToIgnoreCase(o.id);
				}
			}
			return ret;
		}
	}
	
	/**
	 * This class provides to collect the data about an attribute of a class
	 */
	protected class Attribute implements Comparable<Attribute> {
		
		private String name;
		private String value;
		
		private Container container;
		
		public Attribute(String name, String value, Container container) {
			this.name = name;
			this.value = value;
			this.container = container;
			this.container.attributeSet.add(this);
		}

		@Override
		public String toString() {
			return new StringBuffer()
					.append("A: ")
					.append("name='").append(name).append("'")
					.append(",")
					.append("value='").append(value).append("'")
					.toString();
		}

		@Override
		public int compareTo(Attribute o) {
			int ret = Boolean.compare(name == null, o.name == null);
			if(ret == 0 && name != null) {
				ret = name.compareToIgnoreCase(o.name);
			}
			return ret;
		}
	}
}
