package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

import com.wuerth.phoenix.cis.university.example2.util.Util;

public class IFRS16Contract extends APersistentObject {

	private String contractNumber;
	private String creditorNumber;
	private String creditorName;
	private String leasedObject;
	private Long contractStart;
	private Long contractEnd;
	private String partnerCompany;
	private ConcreteAccount concreteAccount;
	private IFRS16CostCenterType costCenterType;
	
	private ImplementedCompany parentImplementedCompany;
	private HashMap<Key, IFRS16ContractData> ifrs16ContractDataMap = new HashMap<>();
	private HashSet<IFRS16Condition> ifrs16ConditionSet = new HashSet<>();
	
	
	public String getContractNumber() {
		return contractNumber;
	}
	
	public void setContractNumber(String contractNumber) {
		this.contractNumber = contractNumber;
	}
	
	public String getCreditorNumber() {
		return creditorNumber;
	}
	
	public void setCreditorNumber(String creditorNumber) {
		this.creditorNumber = creditorNumber;
	}
	
	public String getCreditorName() {
		return creditorName;
	}
	
	public void setCreditorName(String creditorName) {
		this.creditorName = creditorName;
	}
	
	public String getLeasedObject() {
		return leasedObject;
	}
	
	public void setLeasedObject(String leasedObject) {
		this.leasedObject = leasedObject;
	}
	
	public Long getContractStart() {
		return contractStart;
	}
	
	public void setContractStart(Long contractStart) {
		this.contractStart = contractStart;
	}
	
	public Long getContractEnd() {
		return contractEnd;
	}
	
	public void setContractEnd(Long contractEnd) {
		this.contractEnd = contractEnd;
	}
	
	public String getPartnerCompany() {
		return partnerCompany;
	}
	
	public void setPartnerCompany(String partnerCompany) {
		this.partnerCompany = partnerCompany;
	}
	
	public ConcreteAccount getConcreteAccount() {
		return concreteAccount;
	}

	public void setConcreteAccount(ConcreteAccount concreteAccount) {
		this.concreteAccount = concreteAccount;
	}
	
	public IFRS16CostCenterType getCostCenterType() {
		return costCenterType;
	}

	public void setCostCenterType(IFRS16CostCenterType costCenterType) {
		this.costCenterType = costCenterType;
	}

	
	public ImplementedCompany getParentImplementedCompany() {
		return parentImplementedCompany;
	}

	public void setParentImplementedCompany(ImplementedCompany parentImplementedCompany) {
		this.parentImplementedCompany = parentImplementedCompany;
	}

	
	public Collection<IFRS16ContractData> getAllChildIFRS16ContractData() {
		return ifrs16ContractDataMap.values();
	}
	
	public IFRS16ContractData lookupIFRS16ContractData(int year, int month) {
		return ifrs16ContractDataMap.get(new Key(year, month));
	}
	
	public void createChildIFRS16ContractData(IFRS16ContractData ifrs16ContractData) {
		ifrs16ContractDataMap.put(new Key(ifrs16ContractData.getYear(), ifrs16ContractData.getMonth()), ifrs16ContractData);
		ifrs16ContractData.setParentIFRS16Contract(this);
	}
	
	public void deleteChildIFRS16ContractData(IFRS16ContractData ifrs16ContractData) {
		ifrs16ContractDataMap.remove(new Key(ifrs16ContractData.getYear(), ifrs16ContractData.getMonth()));
		ifrs16ContractData.setParentIFRS16Contract(null);
		ifrs16ContractData.onDelete();
	}
	
	public void deleteAllChildIFRS16ContractData() {
		for(IFRS16ContractData ifrs16ContractData : new ArrayList<>(getAllChildIFRS16ContractData())) {
			deleteChildIFRS16ContractData(ifrs16ContractData);
		}
	}

	
	public HashSet<IFRS16Condition> getAllChildIFRS16Condition() {
		return ifrs16ConditionSet;
	}
	
	public void createChildIFRS16Condition(IFRS16Condition ifrs16Condition) {
		ifrs16ConditionSet.add(ifrs16Condition);
		ifrs16Condition.setParentIFRS16Contract(this);
	}
	
	public void deleteChildIFRS16Condition(IFRS16Condition ifrs16Condition) {
		ifrs16ConditionSet.remove(ifrs16Condition);
		ifrs16Condition.setParentIFRS16Contract(null);
		ifrs16Condition.onDelete();
	}
	
	public void deleteAllChildIFRS16Condition() {
		for(IFRS16Condition ifrs16Condition : new ArrayList<>(getAllChildIFRS16Condition())) {
			deleteChildIFRS16Condition(ifrs16Condition);
		}
	}
	
	
	@Override
	protected void onDelete() {
		deleteAllChildIFRS16ContractData();
		deleteAllChildIFRS16Condition();
	}
	
	public boolean equal(IFRS16Contract ifrs16Contract, boolean checkEditability) {
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.GROUPPOSITION)) && !equal(getConcreteAccount(), ifrs16Contract.getConcreteAccount())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.ENDDATEOFCONTRACT)) && !equal(getContractEnd(), ifrs16Contract.getContractEnd())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.CONTRACTNUMBER)) && !equal(getContractNumber(), ifrs16Contract.getContractNumber())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.STARTDATEOFCONTRACT)) && !equal(getContractStart(), ifrs16Contract.getContractStart())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.COSTCENTERTYPE)) && !equal(getCostCenterType(), ifrs16Contract.getCostCenterType())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.CREDITORNAME)) && !equal(getCreditorName(), ifrs16Contract.getCreditorName())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.CREDITORNUMBER)) && !equal(getCreditorNumber(), ifrs16Contract.getCreditorNumber())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.DESIGNATIONLEASEDOBJECT)) && !equal(getLeasedObject(), ifrs16Contract.getLeasedObject())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.PARTNERCOMPANY)) && !equal(getPartnerCompany(), ifrs16Contract.getPartnerCompany())) {
			return false;
		}
		return true;
	}

	public void overwrite(IFRS16Contract ifrs16Contract) {
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.GROUPPOSITION) && !equal(getConcreteAccount(), ifrs16Contract.getConcreteAccount())) {
			setConcreteAccount(ifrs16Contract.getConcreteAccount());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.ENDDATEOFCONTRACT) && !equal(getContractEnd(), ifrs16Contract.getContractEnd())) {
			setContractEnd(ifrs16Contract.getContractEnd());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.CONTRACTNUMBER) && !equal(getContractNumber(), ifrs16Contract.getContractNumber())) {
			setContractNumber(ifrs16Contract.getContractNumber());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.STARTDATEOFCONTRACT) && !equal(getContractStart(), ifrs16Contract.getContractStart())) {
			setContractStart(ifrs16Contract.getContractStart());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.COSTCENTERTYPE) && !equal(getCostCenterType(), ifrs16Contract.getCostCenterType())) {
			setCostCenterType(ifrs16Contract.getCostCenterType());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.CREDITORNAME) && !equal(getCreditorName(), ifrs16Contract.getCreditorName())) {
			setCreditorName(ifrs16Contract.getCreditorName());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.CREDITORNUMBER) && !equal(getCreditorNumber(), ifrs16Contract.getCreditorNumber())) {
			setCreditorNumber(ifrs16Contract.getCreditorNumber());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.DESIGNATIONLEASEDOBJECT) && !equal(getLeasedObject(), ifrs16Contract.getLeasedObject())) {
			setLeasedObject(ifrs16Contract.getLeasedObject());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.PARTNERCOMPANY) && !equal(getPartnerCompany(), ifrs16Contract.getPartnerCompany())) {
			setPartnerCompany(ifrs16Contract.getPartnerCompany());
		}
	}
	
	@Override
	public IFRS16Contract clone() {
		IFRS16Contract ifrs16ContractClone = new IFRS16Contract();
		ifrs16ContractClone.setConcreteAccount(getConcreteAccount());
		ifrs16ContractClone.setContractEnd(getContractEnd());
		ifrs16ContractClone.setContractNumber(getContractNumber());
		ifrs16ContractClone.setContractStart(getContractStart());
		ifrs16ContractClone.setCostCenterType(getCostCenterType());
		ifrs16ContractClone.setCreditorName(getCreditorName());
		ifrs16ContractClone.setCreditorNumber(getCreditorNumber());
		ifrs16ContractClone.setLeasedObject(getLeasedObject());
		ifrs16ContractClone.setPartnerCompany(getPartnerCompany());
		for(IFRS16ContractData ifrs16ContractData : getAllChildIFRS16ContractData()) {
			ifrs16ContractClone.createChildIFRS16ContractData(ifrs16ContractData.clone());
		}
		for(IFRS16Condition ifrs16Condition : getAllChildIFRS16Condition()) {
			ifrs16ContractClone.createChildIFRS16Condition(ifrs16Condition.clone());
		}
		return ifrs16ContractClone;
	}

}
