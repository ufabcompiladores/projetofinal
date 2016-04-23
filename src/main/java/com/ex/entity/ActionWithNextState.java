package com.ex.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ActionWithNextState extends Action {

	private int gotoStateNumber;
	
	public ActionWithNextState(int currentStateNumber, Set<RuleWithDot> state, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> itemSets, SLR slr) {
		super(currentStateNumber, ruleWithDot, itemSets);
		List<Set<RuleWithDot>> newItemSets = new ArrayList<Set<RuleWithDot>>();
		newItemSets.addAll(itemSets);

		Set<RuleWithDot> nextState;
		nextState = slr.gotoSet(state, ruleWithDot.firstSymbolAfterDot());
		this.gotoStateNumber = slr.getStateNumber(nextState);
		
		if (gotoStateNumber == itemSets.size()) {
			newItemSets.add(nextState);
		}
		
		setNextItemSets(newItemSets);
	}

	public int getGotoStateNumber() {
		return gotoStateNumber;
	}


}
