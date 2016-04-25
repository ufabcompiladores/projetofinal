package com.ex.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class ActionWithNextState extends Action {

	private int gotoOrShiftStateNumber;
	
	public ActionWithNextState(int currentStateNumber, Set<RuleWithDot> state, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> allStates, SLR slr) {
		super(currentStateNumber, ruleWithDot, allStates);
		List<Set<RuleWithDot>> newItemSets = new ArrayList<Set<RuleWithDot>>();
		newItemSets.addAll(allStates);

		Set<RuleWithDot> nextState;
		nextState = slr.gotoSet(state, ruleWithDot.firstSymbolAfterDot());
		this.gotoOrShiftStateNumber = slr.getStateNumber(nextState, allStates);
		
		if (gotoOrShiftStateNumber == allStates.size()) {
			newItemSets.add(nextState);
		}
		
		setNextItemSets(newItemSets);
	}

	public int getGotoStateNumber() {
		return gotoOrShiftStateNumber;
	}


}
