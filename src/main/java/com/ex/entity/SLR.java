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

	public SLR(Grammar grammar) {
		super();
		this.grammar = grammar;
		this.itemSets = new ArrayList<>();
		this.grammarWithDots = buildGrammarWithDots(grammar);
	}
	
//	public enum ACTION{
//		ACCEPT, GOTO(n)
//	}
	
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
				grammarWithDots.get(nonTerminal).add(new RuleWithDot(rule));
			}
		}
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
	
	
	private Set<RuleWithDot> gotoSet(Set<RuleWithDot> itemSet, Symbol sym) {
		System.out.println("-------------");
		System.out.format("goto(%s, %s) = \n", itemSet, sym);
		Set<RuleWithDot> newItemSet = new HashSet<RuleWithDot>();
		for (RuleWithDot ruleWithDot : itemSet) {
			if (ruleWithDot.firstSymbolAfterDot().equals(sym)) {
				newItemSet.add(RuleWithDot.generateRuleWithShiftedDot(ruleWithDot));
			}
			else {
				newItemSet.add(ruleWithDot);
			}
		}
		return closure(newItemSet);
	}
	
	
	public void testGotos() throws Exception {
		for (Symbol nonTerminal : grammar.getNonTerminals()){
			for (RuleWithDot rule : this.grammarWithDots.get(nonTerminal)){
				Set<RuleWithDot> s = new HashSet<RuleWithDot>();
				s.add(rule);
				this.gotoSet(s, new Symbol("f"));
			}
		}
	}
	
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
	
	private int getStateNumber(Set<RuleWithDot> state) {
		int stateNumber = 0;
		for (Set<RuleWithDot> existingState : itemSets) {
			if (state.equals(existingState)) {
				return stateNumber;
			}
			stateNumber++;
		}
		return stateNumber;
	}
	

}
