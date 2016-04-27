package com.ex.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Goto extends ActionWithNextState {


	public Goto(int currentStateNumber, State state, RuleWithDot ruleWithDot, List<State> allStates, SLR slr) {
		super(currentStateNumber, state, ruleWithDot, allStates, slr);

		Set<Symbol> columnsToStoreActionInTable = new HashSet<Symbol>();
		columnsToStoreActionInTable.add(ruleWithDot.firstSymbolAfterDot());
		this.setColumnToStoreActionInTable(columnsToStoreActionInTable);
	}

	@Override
	public String toString() {
		return String.format("<Goto %s>", getGotoStateNumber());
	}

}
