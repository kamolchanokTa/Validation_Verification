package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CompanyType extends AEnumerator {
    
    private static HashMap<Short, CompanyType> _map = new HashMap<>();
	private static ArrayList<CompanyType> _list = new ArrayList<>();

	public static final short _NULL = 0;
    public static final CompanyType NULL = new CompanyType(_NULL,"CompanyType." + 0, "CompanyType.NULL");

    public static final short _PRODUCTIVE = 1;
    public static final CompanyType PRODUCTIVE = new CompanyType(_PRODUCTIVE, "CompanyType.1", "CompanyType.PRODUCTIVE");

    public static final short _CONSOLIDATED = 2;
    public static final CompanyType CONSOLIDATED = new CompanyType(_CONSOLIDATED, "CompanyType.2", "CompanyType.CONSOLIDATED");

    public static final short _TEMPORARY = 3;
    public static final CompanyType TEMPORARY = new CompanyType(_TEMPORARY, "CompanyType.3", "CompanyType.TEMPORARY");

    public static List<CompanyType> getAllValues() {
        return _list;
    }

    public static CompanyType getValue(short key) {
    	return _map.containsKey(key) ? _map.get(key) : CompanyType.NULL;
    }

    
    private CompanyType(short key, String literalName, String asTextName) {
		super(key, literalName, asTextName);
		_map.put(key, this);
		_list.add(this);
	}
}
