package com.ex.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This kind of action has an operation that computes the next state that
 * the parsing will be at after reading the action.
 * The Shift and Goto classes descend from this class,
 * as they need to calculate the next state.
 * @author andre0991
 *
 */
public abstract class ActionWithNextState extends Action {

	private int gotoOrShiftStateNumber;
	
	public ActionWithNextState(int currentStateNumber, State state, RuleWithDot ruleWithDot, List<State> allStates, SLR slr) {
		super(currentStateNumber, ruleWithDot, allStates);
		List<State> newItemSets = new ArrayList<State>();
		newItemSets.addAll(allStates);
		
		// Sets next state number and the new list of states.
		State nextState = slr.gotoSet(state, ruleWithDot.firstSymbolAfterDot());
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
