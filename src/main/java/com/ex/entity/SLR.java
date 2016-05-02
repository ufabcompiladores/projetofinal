package com.ex.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

@Entity
public final class SLR {

	private Grammar grammar;
	private List<State> allStates;
	private Map<Symbol, Set<RuleWithDot>> grammarWithDots;
	private List<Action> allActions;

	public SLR(Grammar grammar) throws Exception {
		super();
		Grammar grammarWithExtraStartSymbol = grammar.grammarWithExtraStartSymbol();
		this.grammar = grammarWithExtraStartSymbol;
		this.grammarWithDots = convertAllRulesToRulesWithDots(grammarWithExtraStartSymbol);
		buildAllItemSets();
	}

	/**
	 * Get the follow set from the given symbol.
	 * @param sym A symbol.
	 * @return An object that represents follow(X), where X is the given symbol.
	 */
	public Set<Symbol> follow(Symbol sym) {
		return grammar.follow(sym);
	}

	/**
	 * Initialises all states and gets all actions for the SLR table.
	 */
	private final void buildAllItemSets() {
		System.out.println("\n\n\n==============================");
		System.out.println("Building all states.");

		// adding first state
		System.out.println("Adding first state set:");
		List<State> allStatesBeforeIteration = new ArrayList<State>();
		Set<RuleWithDot> firstRuleSet = grammarWithDots.get(grammar.getStartSymbol());
		State firstState = closure(new State(firstRuleSet));
		allStatesBeforeIteration.add(firstState);

		ActionFactory actionFactory = new ActionFactory();

		int indexOfLastStateInWhichAllRulesWereAnalysed = -1;
		boolean setOfAllStatesHasChanged = true;
		while (setOfAllStatesHasChanged) {
			System.out.println("******* New iteration (building all state sets) *******");
			setOfAllStatesHasChanged = false;
			List<State> allStatesAfterIteration = new ArrayList<State>();
			allStatesAfterIteration.addAll(allStatesBeforeIteration);

			for (int currentStateNumber = indexOfLastStateInWhichAllRulesWereAnalysed + 1; currentStateNumber < allStatesBeforeIteration.size(); currentStateNumber++) {
				State state = allStatesAfterIteration.get(currentStateNumber);
				System.out.format("Analysing state %s: %s\n", currentStateNumber, state);
				for (RuleWithDot ruleWithDot : state.getRules()) {
					System.out.println("~~Analysing rule~~");
					System.out.format("Analysing rule: %s\n", ruleWithDot);
					Action act = actionFactory.getAction(currentStateNumber, state, ruleWithDot, allStatesAfterIteration, this);
					this.allActions.add(act);
					System.out.format("\nCreating action: \n %s\n", act);
					System.out.format("Action position:\n Line: %s \n Columns: %s\n\n", act.getLineToStoreActionInTable(), act.getColumnToStoreActionInTable());
					allStatesAfterIteration = act.getNextItemSets();
				}
				indexOfLastStateInWhichAllRulesWereAnalysed++;
			}

			if (allStatesAfterIteration.size() != allStatesBeforeIteration.size()) {
				setOfAllStatesHasChanged = true;
			}

			allStatesBeforeIteration = allStatesAfterIteration;
		}
		System.out.format("All state sets found: %s", allStatesBeforeIteration);
		this.allStates =  allStatesBeforeIteration;
	}

	/**
	 * Converts grammar rules to their version with dots.
	 * For example, A -> B C D would resulting in an object that represents A -> [ε] . [B C D]
	 * @param grammar A  grammar that has the rules to be converted.
	 * @return A Map from Symbolt to a set of RuleWithDot.
	 */
	private Map<Symbol, Set<RuleWithDot>> convertAllRulesToRulesWithDots(Grammar grammar) {
		// Initialise map
		Map<Symbol, Set<RuleWithDot>> grammarWithDots = new HashMap<Symbol, Set<RuleWithDot>>();
		for (Symbol nonTerminal : grammar.getNonTerminals()){
			for (Rule rule : grammar.getRules().get(nonTerminal)){
				grammarWithDots.put(nonTerminal, new HashSet<RuleWithDot>());
			}
		}
		// Convert Rule to RuleWithDot
		for (Symbol nonTerminal : grammar.getNonTerminals()){
			for (Rule rule : grammar.getRules().get(nonTerminal)){
				boolean isStartRule = nonTerminal.equals(grammar.getStartSymbol());
				grammarWithDots.get(nonTerminal).add(new RuleWithDot(rule, isStartRule));
			}
		}
		System.out.println("Grammar with dots: " + grammarWithDots);
		return grammarWithDots;
	}

	/**
	 * Executes Goto on the given state for the given symbol.
	 * @param state
	 * @param sym
	 * @return A new state that has the rules correspoding to the Goto operation applied on the given inputs.
	 */
	public State gotoSet(State state, Symbol sym) {
		System.out.println("-------------");
		System.out.format("goto(%s, %s) = \n", state, sym);
		Set<RuleWithDot> newItemSet = new HashSet<RuleWithDot>();
		for (RuleWithDot ruleWithDot : state.getRules()) {
			if (ruleWithDot.firstSymbolAfterDot().equals(sym)) {
				newItemSet.add(RuleWithDot.generateRuleWithShiftedDot(ruleWithDot));
			}
		}
		State resultingState = new State(newItemSet);
		return closure(resultingState);
	}

	/**
	 * Computes the closure set for the given state set. 
	 * @param itemSet
	 * @return
	 */
	private State closure(State itemSet) {
		System.out.format("closure(%s) = \n", itemSet);
		Set<RuleWithDot> itemSetBeforeIteration = new HashSet<RuleWithDot>();
		itemSetBeforeIteration.addAll(itemSet.getRules());
		boolean itemSetHasChanged = true;

		while (itemSetHasChanged) {
			System.out.println("  New iteration");
			itemSetHasChanged = false;	
			Set<RuleWithDot> itemSetAfterIteration = new HashSet<RuleWithDot>();
			itemSetAfterIteration.addAll(itemSet.getRules());

			for (RuleWithDot ruleWithDot : itemSetBeforeIteration) {
				Symbol symAfterDot = ruleWithDot.firstSymbolAfterDot();
				itemSetAfterIteration.addAll(rulesWhoseProducerIsSymbol(symAfterDot));
			}

			System.out.println("  Set before iteration: " + itemSetBeforeIteration);
			System.out.println("  Set after iteration: " + itemSetAfterIteration);
			System.out.println("  --");

			if (!itemSetAfterIteration.equals(itemSetBeforeIteration)) {
				itemSetHasChanged = true;	
				itemSetBeforeIteration.addAll(itemSetAfterIteration);
			}
		}
		System.out.println("  Final: " + itemSetBeforeIteration);
		return new State(itemSetBeforeIteration);
	}


	private Set<RuleWithDot> rulesWhoseProducerIsSymbol(Symbol sym) {
		if (sym.isNonTerminal()) {
			return grammarWithDots.get(sym);
		}
		return new HashSet<RuleWithDot>();
	}

	/**
	 * Given a set of known states, verifies whether the given state is contained in
	 * any existing state. If there is such a state, return its index on the list of all states.
	 * Otherwise, returns the index of the next position that will be used
	 * (that is, the size of allStates).
	 * @param state
	 * @param allStates
	 * @return
	 */
	public int getStateNumber(State state, List<State> allStates) {
		int stateNumber = 0;
		for (State existingState : allStates) {
			if (state.equals(existingState)) {
				return stateNumber;
			}
			stateNumber++;
		}
		return stateNumber;
	}

}
