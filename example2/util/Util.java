package com.wuerth.phoenix.cis.university.example2.util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;

import com.wuerth.phoenix.cis.university.example2.adapters.Company;
import com.wuerth.phoenix.cis.university.example2.adapters.ConcreteAccount;
import com.wuerth.phoenix.cis.university.example2.adapters.DigitGroupingSymbol;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ConditionType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16CostCenterType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16ImportAssignmentType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16PaymentCycle;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16PaymentDateType;
import com.wuerth.phoenix.cis.university.example2.adapters.IFRS16VATRateType;
import com.wuerth.phoenix.cis.university.example2.util.Constants.IFRS16ImportCompanyDataColumnSectionType;

public class Util {

	private static HashSet<ConcreteAccount> concreteAccountSet;
	private static HashSet<IFRS16ConditionType> ifrs16ConditionTypeSet;
	
	
	/*
	 * CACHE
	 */
	
	/**
	 * Get the set of the ConcreteAccount objects
	 * @return the set
	 */
	public static HashSet<ConcreteAccount> getAllConcreteAccount() {
		if(concreteAccountSet == null) {
			concreteAccountSet = new HashSet<>();
			for(String code : Constants.IFRS16_GROUP_POSITION_ACCOUNT_CODES) {
				concreteAccountSet.add(new ConcreteAccount(code));
			}
		}
		return concreteAccountSet;
	}
	
	/**
	 * Get the set of the IFRS16ConditionType objects
	 * @return the set
	 */
	public static HashSet<IFRS16ConditionType> getAllIFRS16ConditionType() {
		if(ifrs16ConditionTypeSet == null) {
			ifrs16ConditionTypeSet = new HashSet<>();
			for(String code : Constants.CONDITIONTYPE_CODES) {
				ifrs16ConditionTypeSet.add(new IFRS16ConditionType(code, true));
			}
		}
		return ifrs16ConditionTypeSet;
	}
	
	
	/*
	 * TRANSLATIONS
	 */
	
	/**
	 * Get the name of a Company object
	 * @param company the object
	 * @return the name
	 */
	public static String getName(Company company) {
		return company == null ? null : company.getCode();
	}
	
	/**
	 * Get the name of a ConcreteAccount object
	 * @param concreteAccount the object
	 * @param locale the locale (GERMAN or ENGLISH)
	 * @return the name
	 */
	public static String getName(ConcreteAccount concreteAccount, Locale locale) {
		if(concreteAccount != null) {
			switch(concreteAccount.getCode()) {
	        case Constants.IFRS16_RIGHT_OF_USE_LAND_AND_BUILDINGS:
	        	return tr("Nutzungsrecht Grundstücke und Gebäude", "Right of use land and buildings", locale);
	        case Constants.IFRS16_RIGHT_OF_USE_TECHNICAL_EQUIPMENT:
	        	return tr("Nutzungsrecht Technische Anlagen und Maschinen", "Right of use technical equipment and machines", locale);
	        case Constants.IFRS16_RIGHT_OF_USE_EQUIPMENT:
	        	return tr("Nutzungsrecht Betriebs-/Geschäftsausstattung, Büroeinrichtung", "Right of use fixtures, fittings and office equipment", locale);
	        case Constants.IFRS16_RIGHT_OF_USE_VEHICLE_FLEET:
	        	return tr("Nutzungsrecht Fuhrpark", "Right of use vehicle fleet", locale);
	        case Constants.IFRS16_RIGHT_OF_USE_IT_HARDWARE:
	        	return tr("Nutzungsrecht IT Hardware", "Right of use IT hardware", locale);
			}
		}
    	return null;
	}
	
	/**
	 * Get the name of a IFRS16ConditionType object
	 * @param ifrs16ConditionType the object
	 * @param locale the locale (GERMAN or ENGLISH)
	 * @return the name
	 */
	public static String getName(IFRS16ConditionType ifrs16ConditionType, Locale locale) {
		if(ifrs16ConditionType != null) {
			switch(ifrs16ConditionType.getCode()) {
	        case Constants.CONDITIONTYPE_CODE_RentLandAndBuildings:
	        	return tr("Miete Grundstücke und Gebäude", "Rent land and buildings", locale);
	        case Constants.CONDITIONTYPE_CODE_GrantForRemodelingCosts:
	        	return tr("Umbaukostenzuschuss", "Grant for remodeling costs", locale);
	        case Constants.CONDITIONTYPE_CODE_Utilities:
	        	return tr("Mietnebenkosten", "Utilities", locale);
	        case Constants.CONDITIONTYPE_CODE_Power:
	        	return tr("Strom", "Power", locale);
	        case Constants.CONDITIONTYPE_CODE_Water:
	        	return tr("Wasser", "Water", locale);
	        case Constants.CONDITIONTYPE_CODE_Heating:
	        	return tr("Heizung", "Heating", locale);
	        case Constants.CONDITIONTYPE_CODE_PropertyTax:
	        	return tr("Grundsteuer", "Property tax", locale);
	        case Constants.CONDITIONTYPE_CODE_RentParkingArea:
	        	return tr("Parkplatzmiete", "Rent parking area", locale);
	        case Constants.CONDITIONTYPE_CODE_RentTechnicalEquipmentAndMachines:
	        	return tr("Miete Technische Anlagen und Maschinen", "Rent technical equipment and machines", locale);
	        case Constants.CONDITIONTYPE_CODE_ServiceFeeTechnicalEquipmentAndMachines:
	        	return tr("Service Technische Anlagen und Maschinen", "Service fee technical equipment and machines", locale);
	        case Constants.CONDITIONTYPE_CODE_RentFixturesFittingsAndOfficeEquipment:
	        	return tr("Miete Betriebs- / Geschäftsausstattung, Büroeinrichtung", "Rent fixtures, fittings and office equipment", locale);
	        case Constants.CONDITIONTYPE_CODE_ServiceFeeFixturesFittingsAndOfficeEquipment:
	        	return tr("Service Betriebs- / Geschäftsausstattung, Büroeinrichtung", "Service fee fixtures, fittings and office equipment", locale);
	        case Constants.CONDITIONTYPE_CODE_RentVehicleFleet:
	        	return tr("Miete Fuhrpark", "Rent vehicle fleet", locale);
	        case Constants.CONDITIONTYPE_CODE_ServiceFeeVehicleFleet:
	        	return tr("Service Fuhrpark", "Service fee vehicle fleet", locale);
	        case Constants.CONDITIONTYPE_CODE_RentITHardware:
	        	return tr("Miete IT Hardware", "Rent IT hardware", locale);
	        case Constants.CONDITIONTYPE_CODE_ServiceFeeITHardware:
	        	return tr("Service IT Hardware", "Service fee IT hardware", locale);
			}
		}
    	return null;
	}
	
	/**
	 * Get the name of a IFRS16ImportAssignmentType enumerator
	 * @param ifrs16ImportAssignmentType the enumerator
	 * @param locale the locale (GERMAN or ENGLISH)
	 * @return the name
	 */
	public static String getName(IFRS16ImportAssignmentType ifrs16ImportAssignmentType, Locale locale) {
		switch(ifrs16ImportAssignmentType.getShortValue()) {
		case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
			return tr("Nettobetrag", "Net Amount (without Value Added Tax)", locale);
		case IFRS16ImportAssignmentType._CONDITIONTYPE:
			return tr("Konditionsart", "Type of Condition", locale);
		case IFRS16ImportAssignmentType._CONTRACTNUMBER:
			return tr("Vertragsnummer", "Contract Number", locale);
		case IFRS16ImportAssignmentType._COSTCENTER:
			return tr("Kostenstelle", "Cost Center", locale);
		case IFRS16ImportAssignmentType._CREDITORNAME:
			return tr("Kreditorenname", "Creditor Name", locale);
		case IFRS16ImportAssignmentType._CREDITORNUMBER:
			return tr("Kreditorennummer", "Creditor Number", locale);
		case IFRS16ImportAssignmentType._DESIGNATIONLEASEDOBJECT:
			return tr("Bezeichnung Leasinggut", "Designation Leased Object", locale);
		case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
			return tr("Vertragsende", "End Date of Contract", locale);
		case IFRS16ImportAssignmentType._FROMDATE:
			return tr("Zahlungsbeginn", "Start Date of Payment", locale);
		case IFRS16ImportAssignmentType._GROUPPOSITION:
			return tr("Konzernposition", "Group Position", locale);
		case IFRS16ImportAssignmentType._INTERESTRATE:
			return tr("Zinssatz in %", "Interest Rate in %", locale);
		case IFRS16ImportAssignmentType._NULL:
			return tr("Nicht Importieren", "Don't Import", locale);
		case IFRS16ImportAssignmentType._PARTNERCOMPANY:
			return tr("Partner", "Partner", locale);
		case IFRS16ImportAssignmentType._PAYMENTCYCLE:
			return tr("Zahlungsrhythmus", "Payment cycle", locale);
		case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
			return tr("Zahlungsdatum", "Date of Payment", locale);
		case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
			return tr("Wahrscheinliches Vertragsende", "Probable End of Contract", locale);
		case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
			return tr("Vertragsbeginn", "Start Date of Contract", locale);
		case IFRS16ImportAssignmentType._UNTILDATE:
			return tr("Zahlungsende", "End Date of Payment", locale);
		case IFRS16ImportAssignmentType._VATRATETYPE:
			return tr("Umsatzsteuersatz", "Value Added Tax Rate", locale);
		case IFRS16ImportAssignmentType._COSTCENTERTYPE:
			return tr("Kostenstellentyp", "Cost Center Type", locale);
		case IFRS16ImportAssignmentType._ORDER:
			return tr("Auftrag", "Order", locale);
		case IFRS16ImportAssignmentType._COSTCENTERORORDER:
			return tr("Kostenstelle / Auftrag", "Cost Center / Order", locale);
		default:
			return null;
		}
	}
	
	/**
	 * Get the name of a IFRS16PaymentCycle enumerator
	 * @param ifrs16PaymentCycle the enumerator
	 * @param locale the locale (GERMAN or ENGLISH)
	 * @return the name
	 */
	public static String getName(IFRS16PaymentCycle ifrs16PaymentCycle, Locale locale) {
		switch(ifrs16PaymentCycle.getShortValue()) {
		case IFRS16PaymentCycle._ANNUALLY:
			return tr("jährlich", "annually", locale);
		case IFRS16PaymentCycle._MONTHLY:
			return tr("monatlich", "monthly", locale);
		case IFRS16PaymentCycle._NONRECURRING:
			return tr("einmal", "non-recurring", locale);
		case IFRS16PaymentCycle._NULL:
			return tr("-", "-", locale);
		case IFRS16PaymentCycle._QUARTERLY:
			return tr("vierteljährlich", "quarterly", locale);
		case IFRS16PaymentCycle._SEMIANNUALLY:
			return tr("halbjährlich", "semiannually", locale);
		default:
			return null;
		}
	}
	
	/**
	 * Get the name of a IFRS16PaymentDateType enumerator
	 * @param ifrs16PaymentDateType the enumerator
	 * @param locale the locale (GERMAN or ENGLISH)
	 * @return the name
	 */
	public static String getName(IFRS16PaymentDateType ifrs16PaymentDateType, Locale locale) {
		switch(ifrs16PaymentDateType.getShortValue()) {
		case IFRS16PaymentDateType._INADVANCE:
			return tr("vorschüssig", "in advance", locale);
		case IFRS16PaymentDateType._INARREAR:
			return tr("nachschüssig", "in arrear", locale);
		case IFRS16PaymentDateType._MIDTERM:
			return tr("mittelschüssig", "midterm", locale);
		case IFRS16PaymentDateType._NULL:
			return tr("-", "-", locale);
		default:
			return null;
		}
	}
	
	/**
	 * Get the name of a IFRS16CostCenterType enumerator
	 * @param ifrs16CostCenterType the enumerator
	 * @param locale the locale (GERMAN or ENGLISH)
	 * @return the name
	 */
	public static String getName(IFRS16CostCenterType ifrs16CostCenterType, Locale locale) {
		switch(ifrs16CostCenterType.getShortValue()) {
		case IFRS16CostCenterType._COSTCENTER:
			return tr("Kostenstelle", "Cost Center", locale);
		case IFRS16CostCenterType._NULL:
			return tr("-", "-", locale);
		case IFRS16CostCenterType._ORDER:
			return tr("Auftrag", "Order", locale);
		default:
			return null;
		}
	}
	
	/**
	 * Get the name of a IFRS16VATRateType enumerator
	 * @param ifrs16vatRateType the enumerator
	 * @param locale the locale (GERMAN or ENGLISH)
	 * @return the name
	 */
	public static String getName(IFRS16VATRateType ifrs16vatRateType, Locale locale) {
		switch(ifrs16vatRateType.getShortValue()) {
		case IFRS16VATRateType._FULLVAT:
			return tr("Voller Steuersatz", "Full Value Added Tax", locale);
		case IFRS16VATRateType._NOVAT:
			return tr("Kein Steuersatz", "No Value Added Tax", locale);
		case IFRS16VATRateType._NULL:
			return tr("-", "-", locale);
		case IFRS16VATRateType._REDUCEDVAT:
			return tr("Geminderter Steuersatz", "Reduced Value Added Tax", locale);
		default:
			return null;
		}
	}

	/**
	 * Format a date
	 * @param date the date in milliseconds or null
	 * @param settings the settings
	 * @return the formatted date
	 */
	public static String toString(Long date, Settings settings) {
		if(date == null) {
			return "";
		}
		else {
			SimpleDateFormat format = (SimpleDateFormat)SimpleDateFormat.getInstance();
			format.applyPattern(settings.getDateFormat());
			return format.format(new Date(date));
		}
	}

	/**
	 * Format a value
	 * @param value the value or null
	 * @param settings the settings
	 * @return the formatted value
	 */
	public static String toString(Double value, Settings settings) {
		if(value == null) {
			return "";
		}
		else {
		   	DecimalFormat formatter = (DecimalFormat)DecimalFormat.getInstance(settings.getLocale());
		   	DecimalFormatSymbols symbols = formatter.getDecimalFormatSymbols();
		   	switch(settings.getDigitGroupingSymbol().getShortValue()) {
		   	case DigitGroupingSymbol._COMMA:
			   	symbols.setGroupingSeparator(Constants.SEPARATOR_COMMA);
		   		break;
		   	case DigitGroupingSymbol._NO_BREAK_SPACE:
			   	symbols.setGroupingSeparator(Constants.SEPARATOR_NOBREAKSPACE);
		   		break;
		   	case DigitGroupingSymbol._NORMAL_SPACE:
			   	symbols.setGroupingSeparator(Constants.SEPARATOR_SPACE);
		   		break;
		   	case DigitGroupingSymbol._NULL:
		   		break;
		   	case DigitGroupingSymbol._POINT:
			   	symbols.setGroupingSeparator(Constants.SEPARATOR_POINT);
		   		break;
		   	case DigitGroupingSymbol._SINGLE_QUOTATION_MARK:
			   	symbols.setGroupingSeparator(Constants.SEPARATOR_SINGLEQUOTATIONMARK);
		   		break;
		   	}
		   	switch(settings.getDecimalSeparator().getShortValue()) {
		   	case DigitGroupingSymbol._COMMA:
			   	symbols.setDecimalSeparator(Constants.SEPARATOR_COMMA);
		   		break;
		   	case DigitGroupingSymbol._POINT:
			   	symbols.setDecimalSeparator(Constants.SEPARATOR_POINT);
		   		break;
		   	}
		   	formatter.setDecimalFormatSymbols(symbols);
		   	formatter.setMinimumFractionDigits(2);
		   	formatter.setMaximumFractionDigits(2);
		   	formatter.setRoundingMode(RoundingMode.HALF_UP);
		   	return formatter.format(value);
		}
	}
	
	/**
	 * Get the translation of a text
	 * @param nameDe the german translation
	 * @param nameEn the english translation
	 * @param locale the locale (GERMAN or ENGLISH)
	 * @return the translation
	 */
	private static String tr(String nameDe, String nameEn, Locale locale) {
		if(Locale.GERMAN.equals(locale)) {
			return nameDe;
		}
		else if(Locale.ENGLISH.equals(locale)) {
			return nameEn;
		}
		else {
			return null;
		}
	}
	
	
	/*
	 * VALIDATION
	 */
	
	/**
	 * Check the relation between an IFRS16ImportAssignmentType and the version of the company
	 * @param ifrs16ImportAssignmentType the IFRS16ImportAssignmentType enumerator
	 * @param isLongVersion true (long version) false (short version)
	 * @param isAcceptOrders true (accept orders) false (not accept orders)
	 * @return true (available) false (not available)
	 */
	public static boolean isAvailable(IFRS16ImportAssignmentType ifrs16ImportAssignmentType, boolean isLongVersion, boolean isAcceptOrders) {
		if(isLongVersion) {
			switch(ifrs16ImportAssignmentType.getShortValue()) {
			case IFRS16ImportAssignmentType._COSTCENTERTYPE:
			case IFRS16ImportAssignmentType._ORDER:
			case IFRS16ImportAssignmentType._COSTCENTERORORDER:
				return isAcceptOrders;
			default:
				return true;
			}
		}
		else {
			switch(ifrs16ImportAssignmentType.getShortValue()) {
			case IFRS16ImportAssignmentType._NULL:
			case IFRS16ImportAssignmentType._CONTRACTNUMBER:
			case IFRS16ImportAssignmentType._GROUPPOSITION:
			case IFRS16ImportAssignmentType._DESIGNATIONLEASEDOBJECT:
			case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
			case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
			case IFRS16ImportAssignmentType._PARTNERCOMPANY:
			case IFRS16ImportAssignmentType._INTERESTRATE:
			case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
			case IFRS16ImportAssignmentType._FROMDATE:
			case IFRS16ImportAssignmentType._UNTILDATE:
			case IFRS16ImportAssignmentType._COSTCENTER:
				return true;
			case IFRS16ImportAssignmentType._COSTCENTERTYPE:
			case IFRS16ImportAssignmentType._ORDER:
			case IFRS16ImportAssignmentType._COSTCENTERORORDER:
				return isAcceptOrders;
			default:
				return false;
			}
		}
	}

	/**
	 * Get the section type of an IFRS16ImportAssignmentType
	 * @param ifrs16ImportAssignmentType the IFRS16ImportAssignmentType enumerator
	 * @return the section type
	 */
	public static IFRS16ImportCompanyDataColumnSectionType getColumnSectionType(IFRS16ImportAssignmentType ifrs16ImportAssignmentType) {
		switch(ifrs16ImportAssignmentType.getShortValue()) {
		case IFRS16ImportAssignmentType._CREDITORNUMBER:
		case IFRS16ImportAssignmentType._CREDITORNAME:
		case IFRS16ImportAssignmentType._CONTRACTNUMBER:
		case IFRS16ImportAssignmentType._GROUPPOSITION:
		case IFRS16ImportAssignmentType._DESIGNATIONLEASEDOBJECT:
		case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
		case IFRS16ImportAssignmentType._ENDDATEOFCONTRACT:
		case IFRS16ImportAssignmentType._PARTNERCOMPANY:
		case IFRS16ImportAssignmentType._COSTCENTERTYPE:
			return IFRS16ImportCompanyDataColumnSectionType.Contract;
		case IFRS16ImportAssignmentType._PROBABLEENDOFCONTRACT:
		case IFRS16ImportAssignmentType._INTERESTRATE:
			return IFRS16ImportCompanyDataColumnSectionType.ContractData;
		case IFRS16ImportAssignmentType._CONDITIONTYPE:
		case IFRS16ImportAssignmentType._PAYMENTCYCLE:
		case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
			return IFRS16ImportCompanyDataColumnSectionType.Condition;
		case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
		case IFRS16ImportAssignmentType._VATRATETYPE:
		case IFRS16ImportAssignmentType._FROMDATE:
		case IFRS16ImportAssignmentType._UNTILDATE:
		case IFRS16ImportAssignmentType._COSTCENTER:
		case IFRS16ImportAssignmentType._ORDER:
		case IFRS16ImportAssignmentType._COSTCENTERORORDER:
			return IFRS16ImportCompanyDataColumnSectionType.ConditionData;
		default:
			return IFRS16ImportCompanyDataColumnSectionType.None;
		}
	}

	/**
	 * Check if a list of column associations is valid
	 * @param isLongVersion true (long version) false (short version)
	 * @param isAcceptOrders true (accept orders) false (not accept orders)
	 * @param columnTypeList the list of the IFRS16ImportAssignmentType enumerator
	 * @return true (valid) false (not valid)
	 */
	public static boolean isValid(boolean isLongVersion, boolean isAcceptOrders, Collection<IFRS16ImportAssignmentType> ifrs16ImportAssignmentTypeList) {
		HashMap<String, HashSet<IFRS16ImportAssignmentType>> errorMap = validateColumnAssociations(isLongVersion, isAcceptOrders, ifrs16ImportAssignmentTypeList);
		int count = 0;
		for(HashSet<IFRS16ImportAssignmentType> typeSet : errorMap.values()) {
			count += typeSet.size();
		}
		return count == 0;
	}

	/**
	 * Validate and get the error map of the column associations
	 * @param isLongVersion true (long version) false (short version)
	 * @param isAcceptOrders true (accept orders) false (not accept orders)
	 * @param columnTypeList the list of the IFRS16ImportAssignmentType enumerator
	 * @return the error map
	 */
	public static HashMap<String, HashSet<IFRS16ImportAssignmentType>> validateColumnAssociations(boolean isLongVersion, boolean isAcceptOrders, Collection<IFRS16ImportAssignmentType> ifrs16ImportAssignmentTypeList) {
		
		HashSet<IFRS16ImportAssignmentType> columnTypeSet = new HashSet<>();
		HashSet<IFRS16ImportAssignmentType> duplicatedColumnTypeSet = new HashSet<>();
		HashSet<IFRS16ImportAssignmentType> noColumnTypeSet = new HashSet<>();
		HashSet<IFRS16ImportAssignmentType> toRemoveColumnTypeSet = new HashSet<>();

		for(IFRS16ImportAssignmentType columnType : ifrs16ImportAssignmentTypeList) {
			if(!columnTypeSet.add(columnType)) {
				duplicatedColumnTypeSet.add(columnType);
			}
		}

//		boolean costCenter = columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTER);
//		boolean order = columnTypeSet.contains(IFRS16ImportAssignmentType.ORDER);
//		boolean costCenterOrOrder = columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTERORORDER);
//		boolean costCenterType = columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTERTYPE);
//		boolean skip = true;
//		if(costCenter && order && !costCenterOrOrder && !costCenterType) {
//			skip = true; // checked
//		}
//		if(!costCenter && !order && costCenterOrOrder && costCenterType) {
//			skip = true; // checked
//		}
//		if(costCenter && !order && !costCenterOrOrder && !costCenterType) {
//			skip = true; // checked
//		}
//		if(!costCenter && order && !costCenterOrOrder && !costCenterType) {
//			skip = true; // checked
//		}
//		if(skip) {
//			HashMap<String, HashSet<IFRS16ImportAssignmentType>> errorMap = new HashMap<>();
//			errorMap.put("", new HashSet<>());
//			errorMap.get("").add(IFRS16ImportAssignmentType.COSTCENTERTYPE);
//			return errorMap;
//		}
		
		if(!columnTypeSet.contains(IFRS16ImportAssignmentType.CONTRACTNUMBER)) {
			noColumnTypeSet.add(IFRS16ImportAssignmentType.CONTRACTNUMBER);
		}
		
		boolean hasCostCenterType = columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTERTYPE);
		boolean hasCostCenterOrOrder = columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTERORORDER);
		if(hasCostCenterType != hasCostCenterOrOrder) {
			if(!hasCostCenterType) {
				noColumnTypeSet.add(IFRS16ImportAssignmentType.COSTCENTERTYPE);
			}
			if(!hasCostCenterOrOrder) {
				noColumnTypeSet.add(IFRS16ImportAssignmentType.COSTCENTERORORDER);
			}
		}
		if(hasCostCenterType || hasCostCenterOrOrder) {
			if(columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTER)) {
				toRemoveColumnTypeSet.add(IFRS16ImportAssignmentType.COSTCENTER);
			}
			if(columnTypeSet.contains(IFRS16ImportAssignmentType.ORDER)) {
				toRemoveColumnTypeSet.add(IFRS16ImportAssignmentType.ORDER);
			}
		}

		if(isLongVersion) {
			
			// Condition key check
			boolean hasConditionType = columnTypeSet.contains(IFRS16ImportAssignmentType.CONDITIONTYPE);
			boolean hasFromDate = columnTypeSet.contains(IFRS16ImportAssignmentType.FROMDATE);
			boolean hasCostCenter = columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTER) || columnTypeSet.contains(IFRS16ImportAssignmentType.ORDER) || columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTERORORDER);
			if(hasConditionType || hasFromDate || hasCostCenter) {
				if(!hasConditionType) {
					noColumnTypeSet.add(IFRS16ImportAssignmentType.CONDITIONTYPE);
				}
				if(!hasFromDate) {
					noColumnTypeSet.add(IFRS16ImportAssignmentType.FROMDATE);
				}
				if(!hasCostCenter) {
					noColumnTypeSet.add(IFRS16ImportAssignmentType.COSTCENTER);
				}
			}

			// Group position check
			if(hasConditionType) {
				if(!columnTypeSet.contains(IFRS16ImportAssignmentType.GROUPPOSITION)) {
					noColumnTypeSet.add(IFRS16ImportAssignmentType.GROUPPOSITION);
				}
			}
			
			// Condition Data check
			for(IFRS16ImportAssignmentType columnType : columnTypeSet) {
				if(IFRS16ImportCompanyDataColumnSectionType.ConditionData.equals(getColumnSectionType(columnType))) {
					if(!hasConditionType && !hasFromDate && !hasCostCenter) {
						noColumnTypeSet.add(IFRS16ImportAssignmentType.CONDITIONTYPE);
						noColumnTypeSet.add(IFRS16ImportAssignmentType.FROMDATE);
						noColumnTypeSet.add(IFRS16ImportAssignmentType.COSTCENTER);
					}
					break;
				}
			}
		}
		
		else {

			// Condition key check
			boolean hasFromDate = columnTypeSet.contains(IFRS16ImportAssignmentType.FROMDATE);
			boolean hasCostCenter = columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTER) || columnTypeSet.contains(IFRS16ImportAssignmentType.ORDER) || columnTypeSet.contains(IFRS16ImportAssignmentType.COSTCENTERORORDER);
			if(hasFromDate || hasCostCenter) {
				if(!hasFromDate) {
					noColumnTypeSet.add(IFRS16ImportAssignmentType.FROMDATE);
				}
				if(!hasCostCenter) {
					if(isAcceptOrders) {
						noColumnTypeSet.add(IFRS16ImportAssignmentType.COSTCENTERORORDER);
					}
					else {
						noColumnTypeSet.add(IFRS16ImportAssignmentType.COSTCENTER);
					}
				}
			}
			
			// Condition Data check
			for(IFRS16ImportAssignmentType columnType : columnTypeSet) {
				if(IFRS16ImportCompanyDataColumnSectionType.ConditionData.equals(getColumnSectionType(columnType))) {
					if(!hasFromDate && !hasCostCenter) {
						noColumnTypeSet.add(IFRS16ImportAssignmentType.FROMDATE);
						noColumnTypeSet.add(IFRS16ImportAssignmentType.COSTCENTER);
					}
					break;
				}
			}
		}
		
		HashMap<String, HashSet<IFRS16ImportAssignmentType>> errorMap = new HashMap<>();
		errorMap.put(Constants.KEYWORD_ERROR_COLUMNASSOCIATION_DUPLICATED, duplicatedColumnTypeSet);
		errorMap.put(Constants.KEYWORD_ERROR_COLUMNASSOCIATION_NO, noColumnTypeSet);
		errorMap.put(Constants.KEYWORD_ERROR_COLUMNASSOCIATION_TOREMOVE, toRemoveColumnTypeSet);
		return errorMap;
	}

	/**
	 * Check the relation between a ConcreteAccount and an IFRS16ConditionType is valid
	 * @param concreteAccountCode the code of the ConcreteAccount
	 * @param ifrs16ConditionTypeCode the code of the IFRS16ConditionType
	 * @return true (valid) false (not valid)
	 */
	public static boolean isValid(String concreteAccountCode, String ifrs16ConditionTypeCode) {
	   	switch(concreteAccountCode) {
        case Constants.IFRS16_RIGHT_OF_USE_LAND_AND_BUILDINGS:
            switch(ifrs16ConditionTypeCode) {
            case Constants.CONDITIONTYPE_CODE_RentLandAndBuildings:
            case Constants.CONDITIONTYPE_CODE_GrantForRemodelingCosts:
            case Constants.CONDITIONTYPE_CODE_Utilities:
            case Constants.CONDITIONTYPE_CODE_Power:
            case Constants.CONDITIONTYPE_CODE_Water:
            case Constants.CONDITIONTYPE_CODE_Heating:
            case Constants.CONDITIONTYPE_CODE_PropertyTax:
            case Constants.CONDITIONTYPE_CODE_RentParkingArea:
                return true;
            default:
                return false;
            }
    	case Constants.IFRS16_RIGHT_OF_USE_TECHNICAL_EQUIPMENT:
    		switch(ifrs16ConditionTypeCode) {
    		case Constants.CONDITIONTYPE_CODE_RentTechnicalEquipmentAndMachines:
    		case Constants.CONDITIONTYPE_CODE_ServiceFeeTechnicalEquipmentAndMachines:
    			return true;
    		default:
    			return false;
    		}
    	case Constants.IFRS16_RIGHT_OF_USE_EQUIPMENT:
    		switch(ifrs16ConditionTypeCode) {
    		case Constants.CONDITIONTYPE_CODE_RentFixturesFittingsAndOfficeEquipment:
    		case Constants.CONDITIONTYPE_CODE_ServiceFeeFixturesFittingsAndOfficeEquipment:
    			return true;
    		default:
    			return false;
    		}
    	case Constants.IFRS16_RIGHT_OF_USE_VEHICLE_FLEET:
    		switch(ifrs16ConditionTypeCode) {
    		case Constants.CONDITIONTYPE_CODE_RentVehicleFleet:
    		case Constants.CONDITIONTYPE_CODE_ServiceFeeVehicleFleet:
    			return true;
    		default:
    			return false;
    		}
        case Constants.IFRS16_RIGHT_OF_USE_IT_HARDWARE:
            switch(ifrs16ConditionTypeCode) {
            case Constants.CONDITIONTYPE_CODE_RentITHardware:
            case Constants.CONDITIONTYPE_CODE_ServiceFeeITHardware:
                return true;
            default:
                return false;
            }
    	default:
    		return false;
    	}
	}
	
	public static boolean isEditableByCompany(IFRS16ImportAssignmentType columnType) {
		switch(columnType.getShortValue()) {
		case IFRS16ImportAssignmentType._CONTRACTNUMBER:
		case IFRS16ImportAssignmentType._GROUPPOSITION:
		case IFRS16ImportAssignmentType._STARTDATEOFCONTRACT:
		case IFRS16ImportAssignmentType._PARTNERCOMPANY:
		case IFRS16ImportAssignmentType._PAYMENTCYCLE:
		case IFRS16ImportAssignmentType._PAYMENTDATETYPE:
		case IFRS16ImportAssignmentType._CONDITIONTYPE:
		case IFRS16ImportAssignmentType._FROMDATE:
		case IFRS16ImportAssignmentType._COSTCENTER:
		case IFRS16ImportAssignmentType._ORDER:
		case IFRS16ImportAssignmentType._AMOUNTWITHOUTVALUEADDEDTAX:
		case IFRS16ImportAssignmentType._VATRATETYPE:
		case IFRS16ImportAssignmentType._COSTCENTERTYPE:
		case IFRS16ImportAssignmentType._COSTCENTERORORDER:
			return false;
		default:
			return true;
		}
	}
	

       

}

}
