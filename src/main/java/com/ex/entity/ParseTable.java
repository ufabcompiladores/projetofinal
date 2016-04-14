package com.ex.entity;

import java.util.Map;
import java.util.Set;

public class ParseTable {

	private Set<Tuple<Symbol, Symbol>> cells;
	private Map<Tuple<Symbol, Symbol>, Set<Rule>> table;

	public boolean isAmbiguous () {
		for (Tuple<Symbol, Symbol> cell : cells) {
			if (table.get(cell).size() > 1) {
				return true;
			}
		}
		
		return false;
	}

	public Set<Tuple<Symbol, Symbol>> getCells() {
		return cells;
	}

	public void setCells(Set<Tuple<Symbol, Symbol>> cells) {
		this.cells = cells;
	}

	public Map<Tuple<Symbol, Symbol>, Set<Rule>> getTable() {
		return table;
	}

	public void setTable(Map<Tuple<Symbol, Symbol>, Set<Rule>> table) {
		this.table = table;
	}

}
