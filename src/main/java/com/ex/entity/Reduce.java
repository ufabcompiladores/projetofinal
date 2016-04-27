package com.ex.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Represents a Reduce action.
 * @author andre0991
 *
 */
public final class Reduce extends Action {
	
	private int ruleNumber;

	public Reduce(int currentStateNumber, RuleWithDot ruleWithDot, List<State> allStates, SLR slr) {
		super(currentStateNumber, ruleWithDot, allStates);
		this.ruleNumber = ruleWithDot.getNumber();
		
		// set columns to follow set from producer
		Set<Symbol> columnsToStoreActionInTable = new HashSet<Symbol>();
		columnsToStoreActionInTable.addAll(slr.follow(ruleWithDot.getProducer()));
		this.setColumnToStoreActionInTable(columnsToStoreActionInTable);
	}

	@Override
	public String toString() {
		return String.format("<Reduce %s>", ruleNumber);
	}

}