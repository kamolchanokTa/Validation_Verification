package com.wuerth.phoenix.cis.university.example2.util;

import java.util.ArrayList;

import com.wuerth.phoenix.cis.university.example2.adapters.Company;

public class Scenario {

	private ArrayList<Company> companyList = new ArrayList<>();
	private Combination combination;
	private Settings settings;
	
	
	public Scenario(ArrayList<Company> companyList, Combination combination, Settings settings) {
		getCompanyList().addAll(companyList);
		setCombination(combination);
		setSettings(settings);
	}
	
	
	public ArrayList<Company> getCompanyList() {
		return companyList;
	}

	public Combination getCombination() {
		return combination;
	}
	
	private void setCombination(Combination combination) {
		this.combination = combination;
	}
	
	public Settings getSettings() {
		return settings;
	}
	
	private void setSettings(Settings settings) {
		this.settings = settings;
	}
}
