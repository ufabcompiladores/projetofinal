package com.ex.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Shift extends ActionWithNextState {

	public Shift(int currentStateNumber, Set<RuleWithDot> state, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> itemSets, SLR slr) {
		super(currentStateNumber, state, ruleWithDot, itemSets, slr);
		Set<Symbol> columnsToStoreActionInTable = new HashSet<Symbol>();
		columnsToStoreActionInTable.add(ruleWithDot.firstSymbolAfterDot());
		this.setColumnToStoreActionInTable(columnsToStoreActionInTable);
	}

	@Override
	public String toString() {
		return String.format("Shift %s\n", getGotoStateNumber());
	}
}
