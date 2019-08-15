package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import com.wuerth.phoenix.cis.university.example2.util.Util;

public class IFRS16Condition extends APersistentObject {

	private boolean longVersion;
	private IFRS16PaymentCycle paymentCycle;
	private IFRS16PaymentDateType paymentDateType;
	private IFRS16ConditionType ifrs16ConditionType;
	
	private IFRS16Contract parentIFRS16Contract;
	private HashMap<Key, IFRS16ConditionData> ifrs16ContractMap = new HashMap<>();

	
	public boolean getLongVersion() {
		return longVersion;
	}
	
	public void setLongVersion(boolean longVersion) {
		this.longVersion = longVersion;
	}
	
	public IFRS16PaymentCycle getPaymentCycle() {
		return paymentCycle;
	}

	public void setPaymentCycle(IFRS16PaymentCycle paymentCycle) {
		this.paymentCycle = paymentCycle;
	}
	
	public IFRS16PaymentDateType getPaymentDateType() {
		return paymentDateType;
	}
	
	public void setPaymentDateType(IFRS16PaymentDateType paymentDateType) {
		this.paymentDateType = paymentDateType;
	}
	
	public IFRS16ConditionType getIFRS16ConditionType() {
		return ifrs16ConditionType;
	}
	
	public void setIFRS16ConditionType(IFRS16ConditionType ifrs16ConditionType) {
		this.ifrs16ConditionType = ifrs16ConditionType;
	}
	
	
	public IFRS16Contract getParentIFRS16Contract() {
		return parentIFRS16Contract;
	}
	
	public void setParentIFRS16Contract(IFRS16Contract parentIFRS16Contract) {
		this.parentIFRS16Contract = parentIFRS16Contract;
	}
	
	
	public Collection<IFRS16ConditionData> getAllChildIFRS16ConditionData() {
		return ifrs16ContractMap.values();
	}
	
	public IFRS16ConditionData lookupIFRS16ConditionData(int year, int month) {
		return ifrs16ContractMap.get(new Key(year, month));
	}
	
	public void createChildIFRS16ConditionData(IFRS16ConditionData ifrs16ConditionData) {
		ifrs16ContractMap.put(new Key(ifrs16ConditionData.getYear(), ifrs16ConditionData.getMonth()), ifrs16ConditionData);
		ifrs16ConditionData.setParentIFRS16Condition(this);
	}
	
	public void deleteChildIFRS16ConditionData(IFRS16ConditionData ifrs16ConditionData) {
		ifrs16ContractMap.remove(new Key(ifrs16ConditionData.getYear(), ifrs16ConditionData.getMonth()));
		ifrs16ConditionData.setParentIFRS16Condition(null);
		ifrs16ConditionData.onDelete();
	}
	
	public void deleteAllChildIFRS16ConditionData() {
		for(IFRS16ConditionData ifrs16ConditionData : new ArrayList<>(getAllChildIFRS16ConditionData())) {
			deleteChildIFRS16ConditionData(ifrs16ConditionData);
		}
	}

	
	@Override
	protected void onDelete() {
		deleteAllChildIFRS16ConditionData();
	}
	
	public boolean equal(IFRS16Condition ifrs16Condition, boolean checkEditability) {
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.CONDITIONTYPE)) && !equal(getIFRS16ConditionType(), ifrs16Condition.getIFRS16ConditionType())) {
			return false;
		}
		if(!equal(getLongVersion(), ifrs16Condition.getLongVersion())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.PAYMENTCYCLE)) && !equal(getPaymentCycle(), ifrs16Condition.getPaymentCycle())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.PAYMENTDATETYPE)) && !equal(getPaymentDateType(), ifrs16Condition.getPaymentDateType())) {
			return false;
		}
		return true;
	}

	public void overwrite(IFRS16Condition ifrs16Condition) {
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.CONDITIONTYPE) && !equal(getIFRS16ConditionType(), ifrs16Condition.getIFRS16ConditionType())) {
			setIFRS16ConditionType(ifrs16Condition.getIFRS16ConditionType());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.PAYMENTCYCLE) && !equal(getPaymentCycle(), ifrs16Condition.getPaymentCycle())) {
			setPaymentCycle(ifrs16Condition.getPaymentCycle());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.PAYMENTDATETYPE) && !equal(getPaymentDateType(), ifrs16Condition.getPaymentDateType())) {
			setPaymentDateType(ifrs16Condition.getPaymentDateType());
		}
	}
	
	@Override
	public IFRS16Condition clone() {
		IFRS16Condition ifrs16ConditionClone = new IFRS16Condition();
		ifrs16ConditionClone.setLongVersion(getLongVersion());
		ifrs16ConditionClone.setIFRS16ConditionType(getIFRS16ConditionType());
		ifrs16ConditionClone.setPaymentCycle(getPaymentCycle());
		ifrs16ConditionClone.setPaymentDateType(getPaymentDateType());
		for(IFRS16ConditionData ifrs16ConditionData : getAllChildIFRS16ConditionData()) {
			ifrs16ConditionClone.createChildIFRS16ConditionData(ifrs16ConditionData.clone());
		}
		return ifrs16ConditionClone;
	}
}
