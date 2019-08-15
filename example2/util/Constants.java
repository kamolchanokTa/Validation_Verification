package com.wuerth.phoenix.cis.university.example2.util;

public class Constants {
   
	public enum IFRS16ImportCompanyDataColumnSectionType {
		None,
		Contract,
		ContractData,
		Condition,
		ConditionData,
	}
	
	public enum IFRS16ImportCompanyDataErrorType {
		DifferentAttributeContract,
		DifferentAttributeContractData,
		DifferentAttributeCondition,
		DifferentAttributeConditionData,
		NoGroupPosition,
		ConditionTypeNotAvailable,
		Empty,
		ExceedingText,
		Unparsable,
		NotEditable,
		Historical,
	}

	public static final int MAX_LENGHT_CREDITORNUMBER 			= 20;
	public static final int MAX_LENGHT_CREDITORNAME 			= 100;
	public static final int MAX_LENGHT_CONTRACTNUMBER 			= 20;
	public static final int MAX_LENGHT_DESIGNATIONLEASEDOBJECT	= 250;
	public static final int MAX_LENGHT_COSTCENTER 				= 50;

	public static final int DECIMALS_INTERESTRATE 	= 2;
	public static final int DECIMALS_AMOUNT 		= 2;

	public static final char SEPARATOR = ',';

	public static final char SEPARATOR_COMMA 				= ',';
	public static final char SEPARATOR_POINT 				= '.';
	public static final char SEPARATOR_NOBREAKSPACE 		= (char)160;
	public static final char SEPARATOR_SINGLEQUOTATIONMARK	= '\'';
	public static final char SEPARATOR_SPACE 				= ' ';

	public static final String DATE_DE = "dd mm yy";
	public static final String DATE_EN = "mm dd yy";

	public static final String SETTTING_DATE_DE = "dd-MM-yyyy";
	public static final String SETTTING_DATE_EN = "MM-dd-yyyy";

    public static final String IFRS16_RIGHT_OF_USE_LAND_AND_BUILDINGS  = "NDS_BS11320111";
    public static final String IFRS16_RIGHT_OF_USE_TECHNICAL_EQUIPMENT = "NDS_BS11320201";
    public static final String IFRS16_RIGHT_OF_USE_EQUIPMENT           = "NDS_BS11320311";
    public static final String IFRS16_RIGHT_OF_USE_VEHICLE_FLEET       = "NDS_BS11320321";
    public static final String IFRS16_RIGHT_OF_USE_IT_HARDWARE         = "NDS_BS11320331";

    public static final String[] IFRS16_GROUP_POSITION_ACCOUNT_CODES =
        {
                IFRS16_RIGHT_OF_USE_LAND_AND_BUILDINGS,
                IFRS16_RIGHT_OF_USE_TECHNICAL_EQUIPMENT,
                IFRS16_RIGHT_OF_USE_EQUIPMENT,
                IFRS16_RIGHT_OF_USE_VEHICLE_FLEET,
                IFRS16_RIGHT_OF_USE_IT_HARDWARE
        };
    
    public static final String CONDITIONTYPE_CODE_RentLandAndBuildings                         = "IFRS16ConditionType.rentLand";
    public static final String CONDITIONTYPE_CODE_GrantForRemodelingCosts                      = "IFRS16ConditionType.grantRemodel";
    public static final String CONDITIONTYPE_CODE_Utilities                                    = "IFRS16ConditionType.utilities";
    public static final String CONDITIONTYPE_CODE_Power                                        = "IFRS16ConditionType.power";
    public static final String CONDITIONTYPE_CODE_Water                                        = "IFRS16ConditionType.water";
    public static final String CONDITIONTYPE_CODE_Heating                                      = "IFRS16ConditionType.heating";
    public static final String CONDITIONTYPE_CODE_PropertyTax                                  = "IFRS16ConditionType.propertyTax";
	public static final String CONDITIONTYPE_CODE_RentParkingArea                              = "IFRS16ConditionType.rentParking";
	public static final String CONDITIONTYPE_CODE_RentTechnicalEquipmentAndMachines            = "IFRS16ConditionType.rentTech";
    public static final String CONDITIONTYPE_CODE_ServiceFeeTechnicalEquipmentAndMachines      = "IFRS16ConditionType.feeTech";
    public static final String CONDITIONTYPE_CODE_RentFixturesFittingsAndOfficeEquipment       = "IFRS16ConditionType.rentOffice";
    public static final String CONDITIONTYPE_CODE_ServiceFeeFixturesFittingsAndOfficeEquipment = "IFRS16ConditionType.feeOffice";
	public static final String CONDITIONTYPE_CODE_RentVehicleFleet                             = "IFRS16ConditionType.rentVehicle";
	public static final String CONDITIONTYPE_CODE_ServiceFeeVehicleFleet                       = "IFRS16ConditionType.feeVehicle";
    public static final String CONDITIONTYPE_CODE_RentITHardware                               = "IFRS16ConditionType.rentHardware";
    public static final String CONDITIONTYPE_CODE_ServiceFeeITHardware                         = "IFRS16ConditionType.feeHardware";

	public static final String[] CONDITIONTYPE_CODES = {
			CONDITIONTYPE_CODE_RentLandAndBuildings,
			CONDITIONTYPE_CODE_GrantForRemodelingCosts,
			CONDITIONTYPE_CODE_Utilities,
			CONDITIONTYPE_CODE_Power,
			CONDITIONTYPE_CODE_Water,
			CONDITIONTYPE_CODE_Heating,
			CONDITIONTYPE_CODE_PropertyTax,
			CONDITIONTYPE_CODE_RentParkingArea,
			CONDITIONTYPE_CODE_RentTechnicalEquipmentAndMachines,
			CONDITIONTYPE_CODE_ServiceFeeTechnicalEquipmentAndMachines,
			CONDITIONTYPE_CODE_RentFixturesFittingsAndOfficeEquipment,
			CONDITIONTYPE_CODE_ServiceFeeFixturesFittingsAndOfficeEquipment,
			CONDITIONTYPE_CODE_RentVehicleFleet,
			CONDITIONTYPE_CODE_ServiceFeeVehicleFleet,
			CONDITIONTYPE_CODE_RentITHardware,
			CONDITIONTYPE_CODE_ServiceFeeITHardware,
	};
	
	public static final String KEYWORD_ERROR_COLUMNASSOCIATION_DUPLICATED 	= "IFRS16ImportCompanyDataWizard.Error.ColumnAssociation.Duplicated";
	public static final String KEYWORD_ERROR_COLUMNASSOCIATION_NO 			= "IFRS16ImportCompanyDataWizard.Error.ColumnAssociation.No";
	public static final String KEYWORD_ERROR_COLUMNASSOCIATION_TOREMOVE		= "IFRS16ImportCompanyDataWizard.Error.ColumnAssociation.ToRemove";
}
