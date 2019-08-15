package com.wuerth.phoenix.cis.university.example2.util;

import com.wuerth.phoenix.cis.university.example2.adapters.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ConditionType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16CostCenterType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16PaymentCycle;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16PaymentDateType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16VATRateType;

/**
 * Adapter for a Cell of an Import File
 */
public class CombinationItem<T> {

	public static CombinationItem<String> getNewCombinationItem(String value) {
		return new CombinationItem<String>(value, value);
	}
	
	public static CombinationItem<Long> getNewCombinationItem(Long value, Settings settings) {
		return new CombinationItem<Long>(value, Util.toString(value, settings));
	}
	
	public static CombinationItem<Double> getNewCombinationItem(Double value, Settings settings) {
		return new CombinationItem<Double>(value, Util.toString(value, settings));
	}
	
	public static CombinationItem<ConcreteAccount> getNewCombinationItem(ConcreteAccount value, Settings settings) {
		return new CombinationItem<ConcreteAccount>(value, Util.getName(value, settings.getLocale()));
	}
	
	public static CombinationItem<IFRS16ConditionType> getNewCombinationItem(IFRS16ConditionType value, Settings settings) {
		return new CombinationItem<IFRS16ConditionType>(value, Util.getName(value, settings.getLocale()));
	}
	
	public static CombinationItem<IFRS16PaymentCycle> getNewCombinationItem(IFRS16PaymentCycle value, Settings settings) {
		return new CombinationItem<IFRS16PaymentCycle>(value, Util.getName(value, settings.getLocale()));
	}
	
	public static CombinationItem<IFRS16PaymentDateType> getNewCombinationItem(IFRS16PaymentDateType value, Settings settings) {
		return new CombinationItem<IFRS16PaymentDateType>(value, Util.getName(value, settings.getLocale()));
	}
	
	public static CombinationItem<IFRS16VATRateType> getNewCombinationItem(IFRS16VATRateType value, Settings settings) {
		return new CombinationItem<IFRS16VATRateType>(value, Util.getName(value, settings.getLocale()));
	}
	
	public static CombinationItem<IFRS16CostCenterType> getNewCombinationItem(IFRS16CostCenterType value, Settings settings) {
		return new CombinationItem<IFRS16CostCenterType>(value, Util.getName(value, settings.getLocale()));
	}

	
	private T value;
	private String name;

	
	public CombinationItem(T value, String name) {
		setValue(value);
		setName(name);
	}


	public T getValue() {
		return value;
	}

	public <E> E getValue(Class<E> classs) {
		return classs.cast(getValue());
	}

	private void setValue(T value) {
		this.value = value;
	}

	public String getName() {
		return name;
	}

	private void setName(String name) {
		this.name = name;
	}
}
