package com.wuerth.phoenix.cis.university.example2.adapters;

import com.wuerth.phoenix.cis.university.example2.util.Util;

public class IFRS16ConditionData extends APersistentObject {

	private int year;
	private int month;
	private Double amountWithoutVAT;
	private IFRS16VATRateType VATRateType;
	private Long fromDate;
	private Long untilDate;
	private String costCenter;
	private String order;
	
	private IFRS16Condition parentIFRS16Condition;

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public Double getAmountWithoutVAT() {
		return amountWithoutVAT;
	}

	public void setAmountWithoutVAT(Double amountWithoutVAT) {
		this.amountWithoutVAT = amountWithoutVAT;
	}

	public IFRS16VATRateType getVATRateType() {
		return VATRateType;
	}

	public void setVATRateType(IFRS16VATRateType vATRateType) {
		VATRateType = vATRateType;
	}

	public Long getFromDate() {
		return fromDate;
	}

	public void setFromDate(Long fromDate) {
		this.fromDate = fromDate;
	}

	public Long getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(Long untilDate) {
		this.untilDate = untilDate;
	}

	public String getCostCenter() {
		return costCenter;
	}

	public void setCostCenter(String costCenter) {
		this.costCenter = costCenter;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getCostCenterOrOrder() {
		if(getOrder() != null && getOrder().length() > 0) {
			return getOrder();
		}
		else {
			return getCostCenter();
		}
	}

	public IFRS16Condition getParentIFRS16Condition() {
		return parentIFRS16Condition;
	}

	public void setParentIFRS16Condition(IFRS16Condition parentIFRS16Condition) {
		this.parentIFRS16Condition = parentIFRS16Condition;
	}

	
	@Override
	protected void onDelete() {
		
	}
	
	public boolean equal(IFRS16ConditionData ifrs16ConditionData, boolean checkEditability) {
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.AMOUNTWITHOUTVALUEADDEDTAX)) && !equal(getAmountWithoutVAT(), ifrs16ConditionData.getAmountWithoutVAT())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.COSTCENTER)) && !equal(getCostCenter(), ifrs16ConditionData.getCostCenter())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.FROMDATE)) && !equal(getFromDate(), ifrs16ConditionData.getFromDate())) {
			return false;
		}
		if(!equal(getMonth(), ifrs16ConditionData.getMonth())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.ORDER)) && !equal(getOrder(), ifrs16ConditionData.getOrder())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.UNTILDATE)) && !equal(getUntilDate(), ifrs16ConditionData.getUntilDate())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.VATRATETYPE)) && !equal(getVATRateType(), ifrs16ConditionData.getVATRateType())) {
			return false;
		}
		if(!equal(getYear(), ifrs16ConditionData.getYear())) {
			return false;
		}
		return true;
	}

	public void overwrite(IFRS16ConditionData ifrs16ConditionData) {
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.AMOUNTWITHOUTVALUEADDEDTAX) && !equal(getAmountWithoutVAT(), ifrs16ConditionData.getAmountWithoutVAT())) {
			setAmountWithoutVAT(ifrs16ConditionData.getAmountWithoutVAT());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.COSTCENTER) && !equal(getCostCenter(), ifrs16ConditionData.getCostCenter())) {
			setCostCenter(ifrs16ConditionData.getCostCenter());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.FROMDATE) && !equal(getFromDate(), ifrs16ConditionData.getFromDate())) {
			setFromDate(ifrs16ConditionData.getFromDate());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.ORDER) && !equal(getOrder(), ifrs16ConditionData.getOrder())) {
			setOrder(ifrs16ConditionData.getOrder());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.UNTILDATE) && !equal(getUntilDate(), ifrs16ConditionData.getUntilDate())) {
			setUntilDate(ifrs16ConditionData.getUntilDate());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.VATRATETYPE) && !equal(getVATRateType(), ifrs16ConditionData.getVATRateType())) {
			setVATRateType(ifrs16ConditionData.getVATRateType());
		}
	}
	
	@Override
	public IFRS16ConditionData clone() {
		IFRS16ConditionData ifrs16ConditionDataClone = new IFRS16ConditionData();
		ifrs16ConditionDataClone.setAmountWithoutVAT(getAmountWithoutVAT());
		ifrs16ConditionDataClone.setCostCenter(getCostCenter());
		ifrs16ConditionDataClone.setFromDate(getFromDate());
		ifrs16ConditionDataClone.setMonth(getMonth());
		ifrs16ConditionDataClone.setOrder(getOrder());
		ifrs16ConditionDataClone.setUntilDate(getUntilDate());
		ifrs16ConditionDataClone.setVATRateType(getVATRateType());
		ifrs16ConditionDataClone.setYear(getYear());
		return ifrs16ConditionDataClone;
	}
}
