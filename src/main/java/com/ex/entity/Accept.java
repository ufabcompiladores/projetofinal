package com.ex.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Accept extends Action {

	public Accept(int currentStateNumber, RuleWithDot ruleWithDot, List<State> allStates) {
		super(currentStateNumber, ruleWithDot, allStates);

		Set<Symbol> columnsToStoreActionInTable = new HashSet<Symbol>();
		columnsToStoreActionInTable.add(Symbol.createEOFSymbol());
		this.setColumnToStoreActionInTable(columnsToStoreActionInTable);
	}

	@Override
	public String toString() {
		return "<Accept>"; 
	}

}
