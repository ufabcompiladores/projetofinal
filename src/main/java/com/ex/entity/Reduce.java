package com.ex.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Reduce extends Action {
	
	public Reduce(int currentStateNumber, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> itemSets, SLR slr) {
		super(currentStateNumber, ruleWithDot, itemSets);
		this.ruleNumber = ruleWithDot.getNumber();
		
		// set columns to follow from producer
		Set<Symbol> columnsToStoreActionInTable = new HashSet<Symbol>();
		columnsToStoreActionInTable.addAll(slr.follow(ruleWithDot.getProducer()));
		this.setColumnToStoreActionInTable(columnsToStoreActionInTable);
	}

	private int ruleNumber;

	@Override
	public String toString() {
		return String.format("Reduce %s\n", ruleNumber);
	}

}