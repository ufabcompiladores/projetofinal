package com.ex.entity;

import java.util.List;
import java.util.Set;

public class Reduce extends Action {
	
	public Reduce(int currentStateNumber, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> itemSets) {
		super(currentStateNumber, ruleWithDot, itemSets);
		this.ruleNumber = ruleWithDot.getNumber();
	}

	private int ruleNumber;

	@Override
	public String toString() {
		return String.format("Reduce %s\n", ruleNumber);
	}

}