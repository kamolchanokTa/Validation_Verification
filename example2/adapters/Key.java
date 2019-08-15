package com.wuerth.phoenix.cis.university.example2.adapters;

public class Key {

	private int key;
	
	
	public Key(Object... items) {
		StringBuffer buffer = new StringBuffer();
		if(items != null) {
			boolean first = true;
			for(Object item : items) {
				if(first) {
					first = false;
				}
				else {
					buffer.append("|");
				}
				buffer.append(item);
			}
		}
		setKey(buffer.toString().hashCode());
	}


	private int getKey() {
		return key;
	}

	private void setKey(int key) {
		this.key = key;
	}


	@Override
	public int hashCode() {
		return getKey();
	}

	@Override
	public boolean equals(Object obj) {
		return hashCode() == obj.hashCode();
	}

	@Override
	public String toString() {
		return Integer.toString(hashCode());
	}
}
