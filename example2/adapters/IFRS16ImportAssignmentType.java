package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IFRS16ImportAssignmentType extends AEnumerator {
    
    private static HashMap<Short, IFRS16ImportAssignmentType> _map = new HashMap<>();
	private static ArrayList<IFRS16ImportAssignmentType> _list = new ArrayList<>();

    public static final short _NULL = 0;
    public static final IFRS16ImportAssignmentType NULL = new IFRS16ImportAssignmentType(_NULL,"IFRS16ImportAssignmentType." + 0, "IFRS16ImportAssignmentType.NULL");

    public static final short _CREDITORNUMBER = 1;
    public static final IFRS16ImportAssignmentType CREDITORNUMBER = new IFRS16ImportAssignmentType(_CREDITORNUMBER, "IFRS16ImportAssignmentType.1", "IFRS16ImportAssignmentType.CREDITORNUMBER");

    public static final short _CREDITORNAME = 2;
    public static final IFRS16ImportAssignmentType CREDITORNAME = new IFRS16ImportAssignmentType(_CREDITORNAME, "IFRS16ImportAssignmentType.2", "IFRS16ImportAssignmentType.CREDITORNAME");

    public static final short _CONTRACTNUMBER = 3;
    public static final IFRS16ImportAssignmentType CONTRACTNUMBER = new IFRS16ImportAssignmentType(_CONTRACTNUMBER, "IFRS16ImportAssignmentType.3", "IFRS16ImportAssignmentType.CONTRACTNUMBER");

    public static final short _GROUPPOSITION = 4;
    public static final IFRS16ImportAssignmentType GROUPPOSITION = new IFRS16ImportAssignmentType(_GROUPPOSITION, "IFRS16ImportAssignmentType.4", "IFRS16ImportAssignmentType.GROUPPOSITION");

    public static final short _DESIGNATIONLEASEDOBJECT = 5;
    public static final IFRS16ImportAssignmentType DESIGNATIONLEASEDOBJECT = new IFRS16ImportAssignmentType(_DESIGNATIONLEASEDOBJECT, "IFRS16ImportAssignmentType.5", "IFRS16ImportAssignmentType.DESIGNATIONLEASEDOBJECT");

    public static final short _STARTDATEOFCONTRACT = 6;
    public static final IFRS16ImportAssignmentType STARTDATEOFCONTRACT = new IFRS16ImportAssignmentType(_STARTDATEOFCONTRACT, "IFRS16ImportAssignmentType.6", "IFRS16ImportAssignmentType.STARTDATEOFCONTRACT");

    public static final short _ENDDATEOFCONTRACT = 7;
    public static final IFRS16ImportAssignmentType ENDDATEOFCONTRACT = new IFRS16ImportAssignmentType(_ENDDATEOFCONTRACT, "IFRS16ImportAssignmentType.7", "IFRS16ImportAssignmentType.ENDDATEOFCONTRACT");

    public static final short _PROBABLEENDOFCONTRACT = 8;
    public static final IFRS16ImportAssignmentType PROBABLEENDOFCONTRACT = new IFRS16ImportAssignmentType(_PROBABLEENDOFCONTRACT, "IFRS16ImportAssignmentType.8", "IFRS16ImportAssignmentType.PROBABLEENDOFCONTRACT");

    public static final short _INTERESTRATE = 9;
    public static final IFRS16ImportAssignmentType INTERESTRATE = new IFRS16ImportAssignmentType(_INTERESTRATE, "IFRS16ImportAssignmentType.9", "IFRS16ImportAssignmentType.INTERESTRATE");

    public static final short _CONDITIONTYPE = 10;
    public static final IFRS16ImportAssignmentType CONDITIONTYPE = new IFRS16ImportAssignmentType(_CONDITIONTYPE, "IFRS16ImportAssignmentType.10", "IFRS16ImportAssignmentType.CONDITIONTYPE");

    public static final short _PAYMENTCYCLE = 11;
    public static final IFRS16ImportAssignmentType PAYMENTCYCLE = new IFRS16ImportAssignmentType(_PAYMENTCYCLE, "IFRS16ImportAssignmentType.11", "IFRS16ImportAssignmentType.PAYMENTCYCLE");

    public static final short _AMOUNTWITHOUTVALUEADDEDTAX = 12;
    public static final IFRS16ImportAssignmentType AMOUNTWITHOUTVALUEADDEDTAX = new IFRS16ImportAssignmentType(_AMOUNTWITHOUTVALUEADDEDTAX, "IFRS16ImportAssignmentType.12", "IFRS16ImportAssignmentType.AMOUNTWITHOUTVALUEADDEDTAX");

    public static final short _VATRATETYPE = 13;
    public static final IFRS16ImportAssignmentType VATRATETYPE = new IFRS16ImportAssignmentType(_VATRATETYPE, "IFRS16ImportAssignmentType.13", "IFRS16ImportAssignmentType.VATRATETYPE");

    public static final short _PAYMENTDATETYPE = 14;
    public static final IFRS16ImportAssignmentType PAYMENTDATETYPE = new IFRS16ImportAssignmentType(_PAYMENTDATETYPE, "IFRS16ImportAssignmentType.14", "IFRS16ImportAssignmentType.PAYMENTDATETYPE");

    public static final short _FROMDATE = 15;
    public static final IFRS16ImportAssignmentType FROMDATE = new IFRS16ImportAssignmentType(_FROMDATE, "IFRS16ImportAssignmentType.15", "IFRS16ImportAssignmentType.FROMDATE");

    public static final short _UNTILDATE = 16;
    public static final IFRS16ImportAssignmentType UNTILDATE = new IFRS16ImportAssignmentType(_UNTILDATE, "IFRS16ImportAssignmentType.16", "IFRS16ImportAssignmentType.UNTILDATE");

    public static final short _COSTCENTER = 17;
    public static final IFRS16ImportAssignmentType COSTCENTER = new IFRS16ImportAssignmentType(_COSTCENTER, "IFRS16ImportAssignmentType.17", "IFRS16ImportAssignmentType.COSTCENTER");

    public static final short _PARTNERCOMPANY = 18;
    public static final IFRS16ImportAssignmentType PARTNERCOMPANY = new IFRS16ImportAssignmentType(_PARTNERCOMPANY, "IFRS16ImportAssignmentType.18", "IFRS16ImportAssignmentType.PARTNERCOMPANY");

    public static final short _COSTCENTERTYPE = 19;
    public static final IFRS16ImportAssignmentType COSTCENTERTYPE =  new IFRS16ImportAssignmentType(_COSTCENTERTYPE, "IFRS16ImportAssignmentType.19", "IFRS16ImportAssignmentType.COSTCENTERTYPE");

    public static final short _ORDER = 20;
    public static final IFRS16ImportAssignmentType ORDER =  new IFRS16ImportAssignmentType(_ORDER, "IFRS16ImportAssignmentType.20", "IFRS16ImportAssignmentType.ORDER");

    public static final short _COSTCENTERORORDER = 21;
    public static final IFRS16ImportAssignmentType COSTCENTERORORDER =  new IFRS16ImportAssignmentType(_COSTCENTERORORDER, "IFRS16ImportAssignmentType.21", "IFRS16ImportAssignmentType.COSTCENTERORORDER");

    public static List<IFRS16ImportAssignmentType> getAllValues() {
        return _list;
    }

    public static IFRS16ImportAssignmentType getValue(short key) {
    	return _map.containsKey(key) ? _map.get(key) : IFRS16ImportAssignmentType.NULL;
    }

    
    private IFRS16ImportAssignmentType(short key, String literalName, String asTextName) {
		super(key, literalName, asTextName);
		_map.put(key, this);
		_list.add(this);
	}
}
