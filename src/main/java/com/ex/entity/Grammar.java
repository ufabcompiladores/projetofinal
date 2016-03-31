package com.ex.entity;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammar {

	private Map<Symbol, Set<Rule>> rules;
	private Set<String> terminals;
	private Set<Symbol> nonTerminals;
	
	//Possui as variáveis colocadas à esquerda, para garantirmos que não existem terminais à direita que 
	//Não estão à esquerda
	private Set<String> leftTerminals;
	
	public Grammar (String inputGrammar) throws Exception {
		this.rules = new HashMap<Symbol, Set<Rule>>();
		this.terminals = new HashSet<String>();
		this.nonTerminals = new HashSet<Symbol>();
		this.leftTerminals = new HashSet<String>();
		
		addNonTerminals(inputGrammar);
		readAllRules(inputGrammar);
	}
	
	public void addRuleLine(String rulesText) throws Exception{
		// TODO: assert that it follows correct format
		String[] splitLine = rulesText.split("->");
		String producerText = splitLine[0].trim();
		Symbol producerSymbol = new Symbol(producerText);
		if (producerSymbol.isTerminal()) {
			throw new Exception("Encontrou símbolo terminal no lado esquerdo da regra: " + producerSymbol.literalRepresentation);
		}
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
	public void readAllRules(String rulesText) throws Exception{
		String[] rules = rulesText.split("\n");
		for (String rule : rules) {
			System.out.println("Add rule: " + rule);
			addRuleLine(rule);
		}
	}
	
	private boolean isValidGrammar(String inputGrammar) throws Exception{

		String[] lines = inputGrammar.split("\n");
		for (String line : lines) {
			String[] splitLine = line.split("->");
			if (line.length() < 2) {
				throw new Exception("Regra deveria ter ->: " + line);
			}
		}
		return true;
	}

	private void addNonTerminals(String inputGrammar) throws Exception{
		String[] lines = inputGrammar.split("\n");
		for (String line : lines) {
			String[] splitLine = line.split("->");
//			if (line.length() < 2) {
//				throw new Exception("Regra deveria ter ->: " + line);
//			}
			String LHS = splitLine[0].trim();
			this.nonTerminals.add(new Symbol(LHS));
		}
	}
	
	public Set<Symbol> getNewNonTerminals() {
		return nonTerminals;
	}

	public Set<String> getTerminals() {
		return terminals;
	}
	
	public Set<String> getLeftTerminals() {
		return leftTerminals;
	}
	
	public Map<Symbol, Set<Rule>> getRules() {
		return rules;
	}
	
	public static void main(String[] args) {
	}
}
