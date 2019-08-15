package com.wuerth.phoenix.cis.university.example2.adapters;

public class ConcreteAccount extends APersistentObject {

	private String code;

	
	public ConcreteAccount(String code) {
		setCode(code);
	}
	
	
	public String getCode() {
		return code;
	}

	private void setCode(String code) {
		this.code = code;
	}
	

	@Override
	protected void onDelete() {

	}
}
