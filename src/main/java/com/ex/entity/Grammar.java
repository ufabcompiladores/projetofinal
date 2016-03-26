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
	private Set<String> terminals;
	private Set<String> nonTerminals;
	
	//Possui as variáveis colocadas à esquerda, para garantirmos que não existem terminais à direita que 
	//Não estão à esquerda
	private Set<String> leftTerminals;
	
	public Grammar () {
		this.rules = new HashMap<String, Set<String>>();
		this.terminals = new HashSet<String>();
		this.nonTerminals = new HashSet<String>();
		this.leftTerminals = new HashSet<String>();
	}

	public void addRule(String line) throws InvalidParameterException {
		//Expressão regular para validar a entrada dos dados
		String regex = "^[A-Z][a-z]*(\\s|)->(\\s|)(((\\w+)+\\s)+\\|\\s)*(\\w+(\\s|))+";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);

		if (matcher.matches()) {
			Set<String> rules = new HashSet<String>();
			
			//Divide a entrada em parte esquerda e direita
			String[] splittedLine = line.split("->");
			
			//Divide a parte direita por |
			String[] rightSide = splittedLine[1].split("\\|");

			//Para cada um da parte direita, adiciona a regra na lista de regras dessa variável.
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
			
			this.nonTerminals.add(splittedLine[0].trim());
			this.leftTerminals.add(splittedLine[0].trim());
			this.rules.put(splittedLine[0].trim(), rules);
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
	
	public static void main(String[] args) {
		String t = "A -> 1 | c";

		System.out.println(t.split("\n")[1]);
	}
}
