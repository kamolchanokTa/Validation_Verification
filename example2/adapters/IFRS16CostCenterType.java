package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IFRS16CostCenterType extends AEnumerator {
    
    private static HashMap<Short, IFRS16CostCenterType> _map = new HashMap<>();
	private static ArrayList<IFRS16CostCenterType> _list = new ArrayList<>();

    public static final short _NULL = 0;
    public static final IFRS16CostCenterType NULL = new IFRS16CostCenterType(_NULL,"IFRS16CostCenterType." + 0, "IFRS16CostCenterType.NULL");

    public static final short _COSTCENTER = 1;
    public static final IFRS16CostCenterType COSTCENTER =  new IFRS16CostCenterType(_COSTCENTER, "IFRS16CostCenterType.1", "IFRS16CostCenterType.COSTCENTER");

    public static final short _ORDER = 2;
    public static final IFRS16CostCenterType ORDER =  new IFRS16CostCenterType(_ORDER, "IFRS16CostCenterType.2", "IFRS16CostCenterType.ORDER");

    public static List<IFRS16CostCenterType> getAllValues() {
        return _list;
    }

    public static IFRS16CostCenterType getValue(short key) {
    	return _map.containsKey(key) ? _map.get(key) : IFRS16CostCenterType.NULL;
    }

    
    private IFRS16CostCenterType(short key, String literalName, String asTextName) {
		super(key, literalName, asTextName);
		_map.put(key, this);
		_list.add(this);
	}
}
