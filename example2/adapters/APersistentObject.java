package com.wuerth.phoenix.cis.university.example2.adapters;

public abstract class APersistentObject {

	protected abstract void onDelete();


	protected boolean equal(ConcreteAccount o0, ConcreteAccount o1) {
		if(equalExist(o0, o1)) {
			return o0 == null ? true : o0.getCode().equals(o1.getCode());
		}
		return false;
	}
	
	protected boolean equal(IFRS16ConditionType o0, IFRS16ConditionType o1) {
		if(equalExist(o0, o1)) {
			return o0 == null ? true : o0.getCode().equals(o1.getCode());
		}
		return false;
	}
	
	protected boolean equal(AEnumerator o0, AEnumerator o1) {
		if(equalExist(o0, o1)) {
			return o0 == null ? true : Short.valueOf(o0.getShortValue()).equals(Short.valueOf(o1.getShortValue()));
		}
		return false;
	}
	
	protected boolean equal(Boolean o0, Boolean o1) {
		if(equalExist(o0, o1)) {
			return o0 == null ? true : o0.equals(o1);
		}
		return false;
	}
	
	protected boolean equal(Integer o0, Integer o1) {
		if(equalExist(o0, o1)) {
			return o0 == null ? true : o0.equals(o1);
		}
		return false;
	}
	
	protected boolean equal(Long o0, Long o1) {
		if(equalExist(o0, o1)) {
			return o0 == null ? true : o0.equals(o1);
		}
		return false;
	}
	
	protected boolean equal(Double o0, Double o1) {
		if(equalExist(o0, o1)) {
			return o0 == null ? true : o0.equals(o1);
		}
		return false;
	}
	
	protected boolean equal(String o0, String o1) {
		if(equalExist(o0, o1)) {
			return o0 == null ? true : o0.equals(o1);
		}
		return false;
	}

	private boolean equalExist(Object o0, Object o1) {
		return Boolean.valueOf(o0 == null).equals(Boolean.valueOf(o1 == null));
	}
}
