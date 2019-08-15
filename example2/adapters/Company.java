package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

public class Company extends APersistentObject {

	private String code;
	
	private HashMap<Key, ImplementedCompany> implementedCompanyMap = new HashMap<>();

	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	
	public Collection<ImplementedCompany> getAllChildImplementedCompany() {
		return implementedCompanyMap.values();
	}
	
	public ImplementedCompany lookupImplementedCompany(CompanyType companyType) {
		return implementedCompanyMap.get(new Key(companyType));
	}
	
	public void createChildImplementedCompany(ImplementedCompany implementedCompany) {
		implementedCompanyMap.put(new Key(implementedCompany.getType()), implementedCompany);
		implementedCompany.setParentCompany(this);
	}
	
	public void deleteChildImplementedCompany(ImplementedCompany implementedCompany) {
		implementedCompanyMap.remove(new Key(implementedCompany.getType()));
		implementedCompany.setParentCompany(null);
		implementedCompany.onDelete();
	}
	
	public void deleteAllChildImplementedCompany() {
		for(ImplementedCompany implementedCompany : new ArrayList<>(getAllChildImplementedCompany())) {
			deleteChildImplementedCompany(implementedCompany);
		}
	}


	@Override
	protected void onDelete() {
		deleteAllChildImplementedCompany();
	}
}
