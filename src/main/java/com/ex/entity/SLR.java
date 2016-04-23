package com.ex.entity;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.Entity;

@Entity
public class SLR {

	private Grammar grammar;
	private List<Set<RuleWithDot>> itemSets;
	//	private table<Map<Pair<Integer, Symbol>, Set<Rule>>> slrTable;
	private Map<Symbol, Set<RuleWithDot>> grammarWithDots;

	public SLR(Grammar grammar) throws Exception {
		super();
		Grammar grammarWithExtraStartSymbol = grammar.grammarWithExtraStartSymbol();
		this.grammar = grammarWithExtraStartSymbol;
		this.grammarWithDots = buildGrammarWithDots(grammarWithExtraStartSymbol);
		this.itemSets = new ArrayList<Set<RuleWithDot>>();
		
		buildAllItemSets();
	}
	
	
	public Set<Symbol> follow(Symbol sym) {
		return grammar.follow(sym);
	}
	
	private List<Set<RuleWithDot>> buildAllItemSets() {
		System.out.println("==============================");
		System.out.println("Building all state sets");
		
		// adding q0 (first state)
		List<Set<RuleWithDot>> allStatesBeforeIteration = new ArrayList<Set<RuleWithDot>>();
		Set<RuleWithDot> firstRuleSet = grammarWithDots.get(grammar.getStartSymbol());
		Set<RuleWithDot> firstState = new HashSet<RuleWithDot>();
		firstState = closure(firstRuleSet);
		allStatesBeforeIteration.add(firstState);
		
		ActionFactory actionFactory = new ActionFactory();

		int indexOfLastStateInWhichAllRulesWereAnalysed = -1;
		boolean itemSetsHasChanged = true;
		while (itemSetsHasChanged) {
			System.out.println("^^^^^^^^^^^^^^^^^^");
			System.out.println("New Iteration (building all state sets)");
			itemSetsHasChanged = false;
			List<Set<RuleWithDot>> allStatesAfterIteration = new ArrayList<Set<RuleWithDot>>();
			allStatesAfterIteration.addAll(allStatesBeforeIteration);

			for (int i = indexOfLastStateInWhichAllRulesWereAnalysed + 1; i < allStatesBeforeIteration.size(); i++) {
				Set<RuleWithDot> state = allStatesAfterIteration.get(i);
				System.out.format("Analysing state %s: %s\n", i, state);
				for (RuleWithDot ruleWithDot : state) {
					System.out.format("Analysing rule: %s\n", ruleWithDot);
					Action act = actionFactory.getAction(i, state, ruleWithDot, allStatesAfterIteration, this);
					System.out.format("Creating action: \n %s", act);
					System.out.format("Action position\n Line: %s \n Columns: %s\n\n", act.getLineToStoreActionInTable(), act.getColumnToStoreActionInTable());
					allStatesAfterIteration = act.getNextItemSets();
				}
				indexOfLastStateInWhichAllRulesWereAnalysed++;
			}
			
			if (allStatesAfterIteration.size() != allStatesBeforeIteration.size()) {
				itemSetsHasChanged = true;
			}
			
			allStatesBeforeIteration = allStatesAfterIteration;
		}
		this.itemSets = allStatesBeforeIteration;
		System.out.format("All state sets found: %s", itemSets);
		return null;
	}

	private Map<Symbol, Set<RuleWithDot>> buildGrammarWithDots(Grammar grammar) {
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

	private void buildSLRTable(){
		for(Set<RuleWithDot> itemSet : itemSets) {
			analyseItemSet(itemSet);
		}
	}

	private void analyseItemSet(Set<RuleWithDot> itemSet){
		for(RuleWithDot ruleWithDot : itemSet){
			//			getAction(ruleWithDot);
			//			fillTable();
		}
	}

	//	private void getAction(RuleWithDot ruleWithDot){
	//		Symbol firstSymAfterDot = ruleWithDot.firstSymbolAfterDot();
	//		if (!ruleWithDot.firstSymbolAfterDot) {
	//		}
	//	}




//	public void testGotos() throws Exception {
//		for (Symbol nonTerminal : grammar.getNonTerminals()){
//			for (RuleWithDot rule : this.grammarWithDots.get(nonTerminal)){
//				Set<RuleWithDot> s = new HashSet<RuleWithDot>();
//				s.add(rule);
//				this.gotoSet(s, new Symbol("B"));
//			}
//		}
//	}

	public void testShifts() {
		for (Symbol nonTerminal : grammar.getNonTerminals()){
			for (RuleWithDot rule : this.grammarWithDots.get(nonTerminal)){
				//				Set<RuleWithDot> s = new HashSet<RuleWithDot>();
				//				s.add(rule);
				//				this.gotoSet(s, nonTerminal);
				System.out.println("before: " + rule); 
				RuleWithDot after1 = RuleWithDot.generateRuleWithShiftedDot(rule);
				System.out.println("after" + after1);
				System.out.println("after" + RuleWithDot.generateRuleWithShiftedDot(after1));
				System.out.println("-----------");
			}
		}
	}


	public Set<RuleWithDot> gotoSet(Set<RuleWithDot> itemSet, Symbol sym) {
		System.out.println("-------------");
		System.out.format("goto(%s, %s) = \n", itemSet, sym);
		Set<RuleWithDot> newItemSet = new HashSet<RuleWithDot>();
		for (RuleWithDot ruleWithDot : itemSet) {
			if (ruleWithDot.firstSymbolAfterDot().equals(sym)) {
				newItemSet.add(RuleWithDot.generateRuleWithShiftedDot(ruleWithDot));
			}
//			else {
//				newItemSet.add(ruleWithDot);
//			}
		}
		return closure(newItemSet);
	}
	
		private Set<RuleWithDot> closure(Set<RuleWithDot> itemSet) {
		System.out.format("closure(%s) = \n", itemSet);
		Set<RuleWithDot> itemSetBeforeIteration = new HashSet<RuleWithDot>();
		itemSetBeforeIteration.addAll(itemSet);
		boolean itemSetHasChanged = true;

		while (itemSetHasChanged) {
			System.out.println("New iteration");
			itemSetHasChanged = false;	
			Set<RuleWithDot> itemSetAfterIteration = new HashSet<RuleWithDot>();
			itemSetAfterIteration.addAll(itemSet);

			for (RuleWithDot ruleWithDot : itemSetBeforeIteration) {
				Symbol symAfterDot = ruleWithDot.firstSymbolAfterDot();
				itemSetAfterIteration.addAll(rulesWhoseProducerIsSymbol(symAfterDot));
			}

			System.out.println("Set before iteration: " + itemSetBeforeIteration);
			System.out.println("Set after iteration: " + itemSetAfterIteration);
			System.out.println("--");

			if (!itemSetAfterIteration.equals(itemSetBeforeIteration)) {
				itemSetHasChanged = true;	
				itemSetBeforeIteration.addAll(itemSetAfterIteration);
			}
		}
		System.out.println("Final: " + itemSetBeforeIteration);
		return itemSetBeforeIteration;
	}


	private Set<RuleWithDot> rulesWhoseProducerIsSymbol(Symbol sym) {
		if (sym.isNonTerminal()) {
			return grammarWithDots.get(sym);
		}
		return new HashSet<RuleWithDot>();
	}

	public int getStateNumber(Set<RuleWithDot> state, List<Set<RuleWithDot>> allStates) {
		int stateNumber = 0;
		for (Set<RuleWithDot> existingState : allStates) {
			if (state.equals(existingState)) {
				return stateNumber;
			}
			stateNumber++;
		}
		return stateNumber;
	}


}
