package com.ex.entity;

import java.util.List;
import java.util.Set;

public class ActionFactory {

	public Action getAction(int currentStateNumber, Set<RuleWithDot> state, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> itemSets, SLR slr) {
		Symbol symbolAfterDot = ruleWithDot.firstSymbolAfterDot();
		if (symbolAfterDot.isEmptyString()) {
			System.out.println("Will create Reduce action");
			return new Reduce(currentStateNumber, ruleWithDot, itemSets);	
		}
		// TODO
//		if (symbolAfterDot.isEOF) {
//			System.out.println("Will create Accept action");
//			return new Accept(ruleWithDot, itemSets);
//		}
		if (symbolAfterDot.isTerminal()) {
			System.out.println("Will create Shift action");
			return new Shift(currentStateNumber, state, ruleWithDot, itemSets, slr);
		}
		if (symbolAfterDot.isNonTerminal()) {
			System.out.println("Will create Goto action");
			return new Goto(currentStateNumber, state, ruleWithDot, itemSets, slr);
		}
		return null;
	}
}
