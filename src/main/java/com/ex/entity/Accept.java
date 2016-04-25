package com.ex.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Accept extends Action {

	public Accept(int currentStateNumber, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> itemSets) {
		super(currentStateNumber, ruleWithDot, itemSets);

		Set<Symbol> columnsToStoreActionInTable = new HashSet<Symbol>();
		columnsToStoreActionInTable.add(Symbol.createEOFSymbol());
		this.setColumnToStoreActionInTable(columnsToStoreActionInTable);
	}

	@Override
	public String toString() {
		return "<Accept>"; 
	}

}
