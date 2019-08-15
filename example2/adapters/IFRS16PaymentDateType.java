package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IFRS16PaymentDateType extends AEnumerator {
    
    private static HashMap<Short, IFRS16PaymentDateType> _map = new HashMap<>();
	private static ArrayList<IFRS16PaymentDateType> _list = new ArrayList<>();

    public static final short _NULL = 0;
    public static final IFRS16PaymentDateType NULL = new IFRS16PaymentDateType(_NULL,"IFRS16PaymentDateType." + 0, "IFRS16PaymentDateType.NULL");

    public static final short _INADVANCE = 1;
    public static final IFRS16PaymentDateType INADVANCE = new IFRS16PaymentDateType(_INADVANCE, "IFRS16PaymentDateType.1", "IFRS16PaymentDateType.INADVANCE");

    public static final short _MIDTERM = 2;
    public static final IFRS16PaymentDateType MIDTERM = new IFRS16PaymentDateType(_MIDTERM, "IFRS16PaymentDateType.2", "IFRS16PaymentDateType.MIDTERM");

    public static final short _INARREAR = 3;
    public static final IFRS16PaymentDateType INARREAR = new IFRS16PaymentDateType(_INARREAR, "IFRS16PaymentDateType.3", "IFRS16PaymentDateType.INARREAR");

    public static List<IFRS16PaymentDateType> getAllValues() {
        return _list;
    }

    public static IFRS16PaymentDateType getValue(short key) {
    	return _map.containsKey(key) ? _map.get(key) : IFRS16PaymentDateType.NULL;
    }

    
    private IFRS16PaymentDateType(short key, String literalName, String asTextName) {
		super(key, literalName, asTextName);
		_map.put(key, this);
		_list.add(this);
	}
}
