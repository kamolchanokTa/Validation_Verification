package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IFRS16VATRateType extends AEnumerator {
    
    private static HashMap<Short, IFRS16VATRateType> _map = new HashMap<>();
	private static ArrayList<IFRS16VATRateType> _list = new ArrayList<>();

    public static final short _NULL = 0;
    public static final IFRS16VATRateType NULL = new IFRS16VATRateType(_NULL,"IFRS16VATRateType." + 0, "IFRS16VATRateType.NULL");

    public static final short _FULLVAT = 1;
    public static final IFRS16VATRateType FULLVAT = new IFRS16VATRateType(_FULLVAT, "IFRS16VATRateType.1", "IFRS16VATRateType.FULLVAT");

    public static final short _REDUCEDVAT = 2;
    public static final IFRS16VATRateType REDUCEDVAT = new IFRS16VATRateType(_REDUCEDVAT, "IFRS16VATRateType.2", "IFRS16VATRateType.REDUCEDVAT");

    public static final short _NOVAT = 3;
    public static final IFRS16VATRateType NOVAT = new IFRS16VATRateType(_NOVAT, "IFRS16VATRateType.3", "IFRS16VATRateType.NOVAT");

    public static List<IFRS16VATRateType> getAllValues() {
        return _list;
    }

    public static IFRS16VATRateType getValue(short key) {
    	return _map.containsKey(key) ? _map.get(key) : IFRS16VATRateType.NULL;
    }

    
    private IFRS16VATRateType(short key, String literalName, String asTextName) {
		super(key, literalName, asTextName);
		_map.put(key, this);
		_list.add(this);
	}
}
