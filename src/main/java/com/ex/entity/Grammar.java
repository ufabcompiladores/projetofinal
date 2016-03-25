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
	
	public Grammar () {
		rules = new HashMap<String, Set<String>>();
	}

	void addRule(String line) throws InvalidParameterException {
		String regex = "^[A-Z][a-z]*(\\s|)->(\\s|)(((\\w+)+\\s)+\\|\\s)*(\\w+(\\s|))+";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(line);

		if (matcher.matches()) {
			Set<String> rules = new HashSet<String>();
			String[] splittedLine = line.split("->");
			String[] rightSide = splittedLine[1].split("\\|");

			for (int i = 0; i < rightSide.length; i++) {
				rules.add(rightSide[i].trim());
			}

			this.rules.put(splittedLine[0].trim(), rules);
		} else {
			throw new InvalidParameterException("O formato da entrada deve ser \"Variavel -> arg1 arg2 | arg3...");
		}
	}
	
	void addRules (String text) throws InvalidParameterException {
		String regex = "^[A-Z][a-z]*(\\s|)->(\\s|)(((\\w+)+\\s)+\\|\\s)*(\\w+(\\s|))+";

		Pattern pattern = Pattern.compile(regex);
		
		for (String line : text.split("\n")) {
			Matcher matcher = pattern.matcher(line);
			
			if (matcher.matches()) {
				Set<String> rules = new HashSet<String>();
				String[] splittedLine = line.split("->");
				String[] rightSide = splittedLine[1].split("\\|");

				for (int i = 0; i < rightSide.length; i++) {
					rules.add(rightSide[i].trim());
				}

				this.rules.put(splittedLine[0].trim(), rules);
			} else {
				throw new InvalidParameterException(String.format("Sintaxe incorreta na linha \"%s\".", line));
			}
		}
	}
	
	public Map<String, Set<String>> getRules() {
		return rules;
	}
	
	public static void main(String[] args) {
		Grammar grammar = new Grammar();
		
		grammar.addRules("A -> b | c\nB-> a | d");
		
		System.out.println(grammar.getRules().toString());
	}
}
