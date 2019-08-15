package com.wuerth.phoenix.cis.university.example2.util;

import java.util.ArrayList;
import java.util.Locale;

import com.wuerth.phoenix.cis.university.example2.adapters.DigitGroupingSymbol;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ImportAssignmentType;
import com.wuerth.phoenix.cis.university.example2.adapters.ImplementedCompany;

public class Settings {

	private Locale locale;
	
	private ImplementedCompany implementedCompany;
	private boolean longVersion;
	private boolean acceptOrders;
	private int year;
	private int month;

	private String filePath;
	
	private String name;
	
	private DigitGroupingSymbol digitGroupingSymbol;
	private DigitGroupingSymbol decimalSeparator;
	private String dateFormat;
	
	private ArrayList<IFRS16ImportAssignmentType> typeList = new ArrayList<>();

	
	public Settings(Locale locale, DigitGroupingSymbol digitGroupingSymbol, DigitGroupingSymbol decimalSeparator, String dateFormat) {
		setLocale(locale);
		setDigitGroupingSymbol(digitGroupingSymbol);
		setDecimalSeparator(decimalSeparator);
		setDateFormat(dateFormat);
	}

	
	public Locale getLocale() {
		return locale;
	}

	private void setLocale(Locale locale) {
		this.locale = locale;
	}

	public ImplementedCompany getImplementedCompany() {
		return implementedCompany;
	}

	public void setImplementedCompany(ImplementedCompany implementedCompany) {
		this.implementedCompany = implementedCompany;
	}

	public boolean isLongVersion() {
		return longVersion;
	}

	public void setLongVersion(boolean longVersion) {
		this.longVersion = longVersion;
	}
	
	public boolean isAcceptOrders() {
		return acceptOrders;
	}

	public void setAcceptOrders(boolean acceptOrders) {
		this.acceptOrders = acceptOrders;
	}

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

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DigitGroupingSymbol getDigitGroupingSymbol() {
		return digitGroupingSymbol;
	}
	
	public void setDigitGroupingSymbol(DigitGroupingSymbol digitGroupingSymbol) {
		this.digitGroupingSymbol = digitGroupingSymbol;
	}
	
	public DigitGroupingSymbol getDecimalSeparator() {
		return decimalSeparator;
	}
	
	public void setDecimalSeparator(DigitGroupingSymbol decimalSeparator) {
		this.decimalSeparator = decimalSeparator;
	}
	
	public String getDateFormat() {
		return dateFormat;
	}
	
	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public ArrayList<IFRS16ImportAssignmentType> getTypeList() {
		return typeList;
	}
}
