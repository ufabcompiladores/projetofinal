package com.ex.entity;

import java.security.InvalidParameterException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Grammar {

	private Map<String, Set<String>> rules;
	private Map<Symbol, Set<Rule>> newRules;
	private Set<String> terminals;
	private Set<String> nonTerminals;
	
	//Possui as variáveis colocadas à esquerda, para garantirmos que não existem terminais à direita que 
	//Não estão à esquerda
	private Set<String> leftTerminals;
	
	public Grammar (String rulesText) {
		this.rules = new HashMap<String, Set<String>>();
		this.newRules = new HashMap<Symbol, Set<Rule>>();
//		this.addRules(rulesText);
		this.terminals = new HashSet<String>();
		this.nonTerminals = new HashSet<String>();
		this.leftTerminals = new HashSet<String>();
	}
	
	public void addRuleLine(String rulesText) throws Exception{
		// TODO: assert that it follows correct format
		String[] splitLine = rulesText.split("->");
		String producerText = splitLine[0].trim();
		Symbol producerSymbol = new Symbol(producerText);
		String[] rightSideTextProductions = splitLine[1].split("\\|");

		for (int i = 0; i < rightSideTextProductions.length; i++) {
			System.out.println("producer text: " + producerText);
			System.out.println("Right side text production" + rightSideTextProductions[i]);
			if (!newRules.containsKey(producerSymbol)){
				newRules.put(producerSymbol, new HashSet<Rule>());
			}
			newRules.get(producerSymbol).add(new Rule(producerText, rightSideTextProductions[i].trim()));
		}
	}
	public void readAllRules(String rulesText) throws Exception{
		String[] rules = rulesText.split("\n");
		for (String rule : rules) {
			System.out.println("Add rule: " + rule);
			addRuleLine(rule);
		}
	}

	public void addRule(String line) throws InvalidParameterException {
		//Expressão regular para validar a entrada dos dados
		String regexValidRule = "^[A-Z][a-z]*(\\s|)->(\\s|)(((\\w+)+\\s)+\\|\\s)*(\\w+(\\s|))+";

		Pattern pattern = Pattern.compile(regexValidRule);
		Matcher matcher = pattern.matcher(line);

		if (matcher.matches()) {
			Set<String> rules = new HashSet<String>();
			
			String[] splitLine = line.split("->");
			
			String[] rightSide = splitLine[1].split("\\|");

			for (int i = 0; i < rightSide.length; i++) {
				rules.add(rightSide[i].trim());
			}

			//Adiciona os terminais e não-terminais nas listas
			for (String s : rules) {
				String[] words = s.split("\\s");
				
				for (String w : words) {
					if (w.matches("^[A-Z].*")) {
						this.nonTerminals.add(w.trim());
					} else {
						this.terminals.add(w.trim());
					}
				}
			}
			
			this.nonTerminals.add(splitLine[0].trim());
			this.leftTerminals.add(splitLine[0].trim());
			this.rules.put(splitLine[0].trim(), rules);
		} else {
			throw new InvalidParameterException("O formato da entrada deve ser \"Variavel -> arg1 arg2 | arg3...");
		}
	}
	
	public void addRules (String text) throws InvalidParameterException {
		String[] rules = text.split("\n");
		
		for (String s : rules) {
			this.addRule(s);
		}
	}
	
	public Map<String, Set<String>> getRules() {
		return rules;
	}
	
	public Set<String> getTerminals() {
		return terminals;
	}
	
	public Set<String> getNonTerminals() {
		return nonTerminals;
	}
	
	public Set<String> getLeftTerminals() {
		return leftTerminals;
	}
	
	public Map<Symbol, Set<Rule>> getNewRules() {
		return newRules;
	}
	
	public static void main(String[] args) {
		String t = "A -> 1 | c";

		System.out.println(t.split("\n")[1]);
	}
}
