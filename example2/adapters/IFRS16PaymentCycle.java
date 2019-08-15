package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class IFRS16PaymentCycle extends AEnumerator {
    
    private static HashMap<Short, IFRS16PaymentCycle> _map = new HashMap<>();
	private static ArrayList<IFRS16PaymentCycle> _list = new ArrayList<>();

    public static final short _NULL = 0;
    public static final IFRS16PaymentCycle NULL = new IFRS16PaymentCycle(_NULL,"IFRS16PaymentCycle." + 0, "IFRS16PaymentCycle.NULL");

    public static final short _NONRECURRING = 1;
    public static final IFRS16PaymentCycle NONRECURRING = new IFRS16PaymentCycle(_NONRECURRING, "IFRS16PaymentCycle.1", "IFRS16PaymentCycle.NONRECURRING");

    public static final short _MONTHLY = 2;
    public static final IFRS16PaymentCycle MONTHLY = new IFRS16PaymentCycle(_MONTHLY, "IFRS16PaymentCycle.2", "IFRS16PaymentCycle.MONTHLY");

    public static final short _QUARTERLY = 3;
    public static final IFRS16PaymentCycle QUARTERLY = new IFRS16PaymentCycle(_QUARTERLY, "IFRS16PaymentCycle.3", "IFRS16PaymentCycle.QUARTERLY");

    public static final short _SEMIANNUALLY = 4;
    public static final IFRS16PaymentCycle SEMIANNUALLY = new IFRS16PaymentCycle(_SEMIANNUALLY, "IFRS16PaymentCycle.4", "IFRS16PaymentCycle.SEMIANNUALLY");

    public static final short _ANNUALLY = 5;
    public static final IFRS16PaymentCycle ANNUALLY = new IFRS16PaymentCycle(_ANNUALLY, "IFRS16PaymentCycle.5", "IFRS16PaymentCycle.ANNUALLY");

    public static List<IFRS16PaymentCycle> getAllValues() {
        return _list;
    }

    public static IFRS16PaymentCycle getValue(short key) {
    	return _map.containsKey(key) ? _map.get(key) : IFRS16PaymentCycle.NULL;
    }

    
    private IFRS16PaymentCycle(short key, String literalName, String asTextName) {
		super(key, literalName, asTextName);
		_map.put(key, this);
		_list.add(this);
	}
}
