package com.wuerth.phoenix.cis.university.example2.adapters;

public class AEnumerator {
	
	private short key;
	private String literalName;
	private String asTextName;
	
	
	protected AEnumerator(short key, String literalName, String asTextName) {
		setKey(key);
		setLiteralName(literalName);
		setAsTextName(asTextName);
	}


	public short getShortValue() {
		return key;
	}
	
	private void setKey(short key) {
		this.key = key;
	}
	
	public String getLiteralName() {
		return literalName;
	}
	
	private void setLiteralName(String literalName) {
		this.literalName = literalName;
	}
	
	public String getAsText() {
		return asTextName;
	}
	
	private void setAsTextName(String asTextName) {
		this.asTextName = asTextName;
	}
}
