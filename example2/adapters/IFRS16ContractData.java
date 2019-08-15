package com.wuerth.phoenix.cis.university.example2.adapters;

import com.wuerth.phoenix.cis.university.example2.util.Util;

public class IFRS16ContractData extends APersistentObject {

	private int year;
	private int month;
	private Long probableContractEnd;
	private Double interestRate;

	private IFRS16Contract parentIFRS16Contract;

	
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

	public Long getProbableContractEnd() {
		return probableContractEnd;
	}

	public void setProbableContractEnd(Long probableContractEnd) {
		this.probableContractEnd = probableContractEnd;
	}

	public Double getInterestRate() {
		return interestRate;
	}

	public void setInterestRate(Double interestRate) {
		this.interestRate = interestRate;
	}

	
	public IFRS16Contract getParentIFRS16Contract() {
		return parentIFRS16Contract;
	}

	public void setParentIFRS16Contract(IFRS16Contract parentIFRS16Contract) {
		this.parentIFRS16Contract = parentIFRS16Contract;
	}

	
	@Override
	protected void onDelete() {
		
	}
	
	public boolean equal(IFRS16ContractData ifrs16ContractData, boolean checkEditability) {
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.INTERESTRATE)) && !equal(getInterestRate(), ifrs16ContractData.getInterestRate())) {
			return false;
		}
		if(!equal(getMonth(), ifrs16ContractData.getMonth())) {
			return false;
		}
		if((!checkEditability || !Util.isEditableByCompany(IFRS16ImportAssignmentType.PROBABLEENDOFCONTRACT)) && !equal(getProbableContractEnd(), ifrs16ContractData.getProbableContractEnd())) {
			return false;
		}
		if(!equal(getYear(), ifrs16ContractData.getYear())) {
			return false;
		}
		return true;
	}

	public void overwrite(IFRS16ContractData ifrs16ContractData) {
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.INTERESTRATE) && !equal(getInterestRate(), ifrs16ContractData.getInterestRate())) {
			setInterestRate(ifrs16ContractData.getInterestRate());
		}
		if(!Util.isEditableByCompany(IFRS16ImportAssignmentType.PROBABLEENDOFCONTRACT) && !equal(getProbableContractEnd(), ifrs16ContractData.getProbableContractEnd())) {
			setProbableContractEnd(ifrs16ContractData.getProbableContractEnd());
		}
	}
	
	@Override
	public IFRS16ContractData clone() {
		IFRS16ContractData ifrs16ContractDataClone = new IFRS16ContractData();
		ifrs16ContractDataClone.setInterestRate(getInterestRate());
		ifrs16ContractDataClone.setMonth(getMonth());
		ifrs16ContractDataClone.setProbableContractEnd(getProbableContractEnd());
		ifrs16ContractDataClone.setYear(getYear());
		return ifrs16ContractDataClone;
	}
}
