package com.wuerth.phoenix.cis.university.example2.adapters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DigitGroupingSymbol extends AEnumerator {
    
    private static HashMap<Short, DigitGroupingSymbol> _map = new HashMap<>();
	private static ArrayList<DigitGroupingSymbol> _list = new ArrayList<>();

    public static final short _NULL = 0;
    public static final DigitGroupingSymbol NULL = new DigitGroupingSymbol(_NULL,"DigitGroupingSymbol." + 0, "DigitGroupingSymbol.NULL");

    public static final short _POINT = 1;
    public static final DigitGroupingSymbol POINT = new DigitGroupingSymbol(_POINT, "DigitGroupingSymbol.1", "DigitGroupingSymbol.POINT");

    public static final short _COMMA = 2;
    public static final DigitGroupingSymbol COMMA = new DigitGroupingSymbol(_COMMA, "DigitGroupingSymbol.2", "DigitGroupingSymbol.COMMA");

    public static final short _NO_BREAK_SPACE = 3;
    public static final DigitGroupingSymbol NO_BREAK_SPACE = new DigitGroupingSymbol(_NO_BREAK_SPACE, "DigitGroupingSymbol.3", "DigitGroupingSymbol.NO_BREAK_SPACE");

    public static final short _SINGLE_QUOTATION_MARK = 4;
    public static final DigitGroupingSymbol SINGLE_QUOTATION_MARK = new DigitGroupingSymbol(_SINGLE_QUOTATION_MARK, "DigitGroupingSymbol.4", "DigitGroupingSymbol.SINGLE_QUOTATION_MARK");

    public static final short _NORMAL_SPACE = 5;
    public static final DigitGroupingSymbol NORMAL_SPACE = new DigitGroupingSymbol(_NORMAL_SPACE, "DigitGroupingSymbol.5", "DigitGroupingSymbol.NORMAL_SPACE");

    public static List<DigitGroupingSymbol> getAllValues() {
        return _list;
    }

    public static DigitGroupingSymbol getValue(short key) {
    	return _map.containsKey(key) ? _map.get(key) : DigitGroupingSymbol.NULL;
    }

    
    private DigitGroupingSymbol(short key, String literalName, String asTextName) {
		super(key, literalName, asTextName);
		_map.put(key, this);
		_list.add(this);
	}
}
