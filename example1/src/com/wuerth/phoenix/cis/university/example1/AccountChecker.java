package com.wuerth.phoenix.cis.university.example1;

import com.wuerth.phoenix.cis.university.example1.concretes.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example1.types.DataScenarioType;

public class AccountChecker {
	
	
	
	public static void main(String [ ] args)
	{
		
	}

	/**
	 * Check if the parameters of a data are valid
	 * @param company the Company
	 * @param profitCenter the Profit Center
	 * @param crComponent the C/R Component
	 * @param external true (external) false (internal)
	 * @param scenarioType the type of the Scenario
	 * @param account the Account
	 * @param partnerCode the code of the partner or null
	 * @param currencyCode the code of the currency or null
	 * @return true (valid) false (not valid)
	 */
	public boolean isValid(ConcreteAccount account, DataScenarioType scenarioType) {
		
		// No Account
		if(account == null) {
			return false;
		}
		
		
//		boolean hasPartnerCode = !isEmpty(partnerCode);
		
		
		switch(account.getAccountClass()) {
		
		case SalesReporting:

			// Profit Center
			switch(account.getAccountType()) {
			case BranchOffice:
			case SpecialAnalyses:
			case VK:
				break;
			}
			
			// C/R Component
			switch(account.getAccountType()) {
			case VK:
				break;
			case SEAN:
				break;
			case BranchOffice:
			case SpecialAnalyses:
			case Employees:
				break;
			case SML:
			case Customer:
				break;
			}
			
			// Internal/External
			switch(account.getAccountType()) {
			case BranchOffice:
			case SpecialAnalyses:
			case VK:
				break;
			}
			
			// Scenario
			switch(scenarioType) {
			case Deferral:
				switch(account.getAccountType()) {
				case BranchOffice:
					break;
				default:
					return false;
				}
			case Extrapolation:
			case Target:
			case Plan:
				switch(account.getAccountType()) {
				case SEAN:
				case SpecialAnalyses:
				case SMLGrossProfit:
				case SMLPotential:
					return false;
				}
				break;
			}

			// Partner
			if(account.isPartnerAllowed()) {
				return false;
			}
			

			break;
			
		case PLStatement:

			switch(account.getAccountType()) {
			case PrognosisSales:
			case PrognosisOperatingResult:
			case PrognosisNumOfAdmMPlus1:
			case PrognosisNumOfAdmMPlus2:
			case PrognosisNumOfAdmDecember:

				

				// Internal/External
				switch(account.getAccountType()) {
				case PrognosisOperatingResult:
				case PrognosisNumOfAdmMPlus1:
				case PrognosisNumOfAdmMPlus2:
				case PrognosisNumOfAdmDecember:
					break;
				}

				// Scenario
				switch(scenarioType) {
				case Actual:
					break;
				default:
					return false;
				}

				// Partner
				if(account.isPartnerAllowed()) {
					return false;
				}
				
				break;
				
			default:
				
				if(account.isPartnerAllowed()) {
				
					// Scenario
					switch(scenarioType) {
					case Actual:
						break;
					default:
						return false;
					}

					
				}
				
				else {

				}
				break;
			}
			break;
			
		case AllocationFormula:

			// Scenario
			switch(scenarioType) {
			case Deferral:
			case Extrapolation:
				return false;
			}
			
			// Partner
			if(account.isPartnerAllowed()) {
				return false;
			}
			
			break;
			
		case BalanceSheet:

			// Profit Center
			if(account.isPartnerAllowed()) {
				switch(account.getAccountType()) {
				case AssetPartner:
					break;
				default:
					break;
				}
			}
			

			// Scenario
			switch(scenarioType) {
			case Deferral:
				if(account.isPartnerAllowed()) {
					switch(account.getAccountType()) {
					case AssetPartner:
						break;
					default:
						return false;
					}
				}
				break;
			}
			
			// Currency
			if(account.isPartnerAllowed()) {
				switch(account.getAccountType()) {
				case AssetPartner:
					break;
				default:
					break;
				}
			}
			else {
			}
			break;
			
		case Logistics:
			
			
			// Scenario
			switch(scenarioType) {
			case Actual:
				break;
			default:
				return false;
			}
			
			// Partner
			if(account.isPartnerAllowed()) {
				return false;
			}

			break;
		}

		return true;
	}
	

	
	/**
	 * Check if a code is empty
	 * @param code the code
	 * @return true (empty) false (not empty)
	 */
	private boolean isEmpty(String code) {
		return code == null || code.trim().length() == 0;
	}
}
