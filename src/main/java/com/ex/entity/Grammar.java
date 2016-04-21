package com.ex.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ex.entity.Symbol.SymbolType;

public final class Grammar {


	private Map<Symbol, Set<Rule>> rules;
	private Set<Symbol> terminals;
	private Set<Symbol> nonTerminals;
	private Map<Symbol, Set<Symbol>> firstSets;

	public Grammar (String inputGrammar) throws Exception {
		this.rules = new HashMap<Symbol, Set<Rule>>();
		this.terminals = new HashSet<Symbol>();
		this.nonTerminals = new HashSet<Symbol>();

		isValidGrammar(inputGrammar);
		addNonTerminals(inputGrammar);
		readAllRules(inputGrammar);
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("Grammar rules: \n");
		for (Symbol nonTerminal : nonTerminals){
			for (Rule rule : rules.get(nonTerminal)){
				string.append(String.format("%s \n", rule));
			}
		}
		return string.toString();
	}

	private void addRuleLine(String rulesText) throws Exception{
		String[] splitLine = rulesText.split("->");
		String producerText = splitLine[0].trim();
		Symbol producerSymbol = new Symbol(producerText);
		String[] rightSideTextProductions = splitLine[1].split("\\|");

		for (int i = 0; i < rightSideTextProductions.length; i++) {
			System.out.println("producer text: " + producerText);
			System.out.println("Right side text production" + rightSideTextProductions[i]);
			if (!rules.containsKey(producerSymbol)){
				rules.put(producerSymbol, new HashSet<Rule>());
			}
			rules.get(producerSymbol).add(new Rule(producerText, rightSideTextProductions[i].trim()));
		}
	}
	private void readAllRules(String rulesText) throws Exception{
		String[] rules = rulesText.split("\n");
		for (String rule : rules) {
			System.out.println("Add rule: " + rule);
			addRuleLine(rule);
		}
	}

	private void isValidGrammar(String inputGrammar) throws Exception{

		Set<Symbol> nonTerminalsLHS = new HashSet<Symbol>();
		Set<Symbol> nonTerminalsRHS = new HashSet<Symbol>();
		
		String[] lines = inputGrammar.split("\n");
		
		for (String line : lines) {
			// Verifica se ha "->"
			String[] splitLine = line.split("->");
			if (splitLine.length < 2) {
				throw new Exception("Regra deveria ter ->: " + line);
			}

			// Verifica se parte esquerda eh nao terminal
			String LHS = splitLine[0].trim();
			if (!LHS.matches(Symbol.NONTERMINAL_REGEX)) {
				throw new Exception("Símbolo do lado esquerdo não é um não terminal: " + LHS);
			} else {
				nonTerminalsLHS.add(new Symbol(SymbolType.NONTERMINAL, LHS));
			}
			
			// 	Verifica se parte direita representa sequencias de simbolos
			// (possivelmente dividos por |)
			String RHS = splitLine[1];
			String[] productions = RHS.split("\\|");
			for (int i = 0; i < productions.length; i++) {
				String [] sequenceOfSymbols = productions[i].split("\\s");
				for (int j = 0; j < sequenceOfSymbols.length; j++) {
					String symbolText = sequenceOfSymbols[j].trim();
					if (!symbolText.matches(Symbol.EMPTY_STRING_REGEX) &&
							!symbolText.matches(Symbol.TERMINAL_REGEX) &&
							!symbolText.matches(Symbol.NONTERMINAL_REGEX)) {
						throw new Exception("Símbolo não segue formato válido: " + symbolText);
					} else if (symbolText.matches(Symbol.NONTERMINAL_REGEX)) {
						nonTerminalsRHS.add(new Symbol(SymbolType.NONTERMINAL, symbolText));
					}
				}
			}
		}
		
//		if (!nonTerminalsLHS.equals(nonTerminalsRHS)) {
//			throw new Exception ("Quantidade de não-terminais à esquerda diferente da quantidade à direita.");
//		}
	}

	private void addNonTerminals(String inputGrammar) throws Exception{
		String[] lines = inputGrammar.split("\n");
		for (String line : lines) {
			String[] splitLine = line.split("->");
			String LHS = splitLine[0].trim();
			this.nonTerminals.add(new Symbol(LHS));
		}
	}
	
	public Set<Symbol> firstSetFromSymbol(Symbol sym){
		return firstSets.get(sym);
	}

	public Set<Symbol> getNonTerminals() {
		return nonTerminals;
	}

	public Set<Symbol> getTerminals() {
		return terminals;
	}

	public Map<Symbol, Set<Rule>> getRules() {
		return rules;
	}

	public static void main(String[] args) {
	}
}
