package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class ImplementedCompany extends APersistentObject {

	private CompanyType type;
	
	private Company parentCompany;
	private HashMap<Key, IFRS16Contract> contractMap = new HashMap<>();
	
	public CompanyType getType() {
		return type;
	}

	public void setType(CompanyType type) {
		this.type = type;
	}

	
	public Company getParentCompany() {
		return parentCompany;
	}

	public void setParentCompany(Company parentCompany) {
		this.parentCompany = parentCompany;
	}

	
	public Collection<IFRS16Contract> getAllChildIFRS16Contract() {
		return contractMap.values();
	}
	
	public IFRS16Contract lookupIFRS16Contract(String contractNumber) {
		return contractMap.get(new Key(contractNumber));
	}
	
	public void createChildIFRS16Contract(IFRS16Contract contract) {
		contractMap.put(new Key(contract.getContractNumber()), contract);
		contract.setParentImplementedCompany(this);
	}
	
	public void deleteChildIFRS16Contract(IFRS16Contract contract) {
		contractMap.remove(new Key(contract.getContractNumber()));
		contract.setParentImplementedCompany(null);
		contract.onDelete();
	}
	
	public void deleteAllChildIFRS16Contract() {
		for(IFRS16Contract contract : new ArrayList<>(getAllChildIFRS16Contract())) {
			deleteChildIFRS16Contract(contract);
		}
	}

	
	@Override
	protected void onDelete() {
		deleteAllChildIFRS16Contract();
	}
}
