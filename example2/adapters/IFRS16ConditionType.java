package com.wuerth.phoenix.cis.university.example2.adapters;

public class IFRS16ConditionType extends APersistentObject {

	private String code;
	private boolean enabled;

	
	public IFRS16ConditionType(String code, boolean enabled) {
		setCode(code);
		setEnabled(enabled);
	}
	
	
	public String getCode() {
		return code;
	}

	private void setCode(String code) {
		this.code = code;
	}

	public boolean isEnabled() {
		return enabled;
	}

	private void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
	@Override
	protected void onDelete() {
		
	}
}
