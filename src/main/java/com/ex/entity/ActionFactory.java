package com.ex.entity;

import java.util.List;
import java.util.Set;

/**
 * Implements the Factory Pattern to create Action objects.
 * @author andre0991
 *
 */
public final class ActionFactory {

	/**
	 * Creates Action object based on the given rule.
	 * @param currentStateNumber Number of the state set that given rule belongs to (can also be viewed as line number for generated Action on SLR table)
	 * @param state RuleWithDot belongs to this state.
	 * @param ruleWithDot RuleWithDot that will be analysed to decided which type of Action will be created.
	 * @param allStates List of known states so far.
	 * @param slr
	 * @return An Accept, Shift, Goto or Reduce object.
	 */
	public Action getAction(int currentStateNumber, Set<RuleWithDot> state, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> allStates, SLR slr) {
		Symbol symbolAfterDot = ruleWithDot.firstSymbolAfterDot();
		if (symbolAfterDot.isEmptyString()) {
			System.out.println("Will create Reduce action");
			return new Reduce(currentStateNumber, ruleWithDot, allStates, slr);	
		}
		if (symbolAfterDot.isEOF()) {
			System.out.println("Will create Accept action");
			return new Accept(currentStateNumber, ruleWithDot, allStates);
		}
		if (symbolAfterDot.isTerminal()) {
			System.out.println("Will create Shift action");
			return new Shift(currentStateNumber, state, ruleWithDot, allStates, slr);
		}
		if (symbolAfterDot.isNonTerminal()) {
			System.out.println("Will create Goto action");
			return new Goto(currentStateNumber, state, ruleWithDot, allStates, slr);
		}
		return null;
	}
}
