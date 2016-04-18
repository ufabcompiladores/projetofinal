package com.ex.entity;

import java.util.Map;
import java.util.Set;

public class ParseTable {

	private Set<Tuple> cells;
	private Map<Tuple, Set<Rule>> table;

	public boolean isAmbiguous () {
		for (Tuple cell : cells) {
			if (table.get(cell).size() > 1) {
				return true;
			}
		}
		
		return false;
	}
	
	public void addRule(Symbol nt, Symbol t, Rule r) {
		table.get(new Tuple(nt, t)).add(r);
	}

	public Set<Tuple> getCells() {
		return cells;
	}

	public void setCells(Set<Tuple> cells) {
		this.cells = cells;
	}

	public Map<Tuple, Set<Rule>> getTable() {
		return table;
	}

	public void setTable(Map<Tuple, Set<Rule>> table) {
		this.table = table;
	}

}
