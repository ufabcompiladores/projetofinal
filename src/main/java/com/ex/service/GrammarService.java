package com.ex.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.ex.entity.Grammar;

@Service
public class GrammarService {
	
	private Grammar grammar;
	
	public GrammarService(Grammar grammar) throws Exception {
		this.grammar = grammar;
		
//		if (!isValidGrammar()) {
//			throw new Exception("Gramática inválida");
//		}
	}

	// TODO: create function
	private boolean producesEps(String rule) {
		return false;
	}

	// Descrevemos cada conjunto First como a uniao de outros conjuntos First em setsWhoseUnionIsFirstSet.
	// Por exemplo, se a regra eh A -> BCD | EF | G | a, e se B e C tem 'eps' em suas producoes, mas
	// E e G nao, entao
	// setsWhoseUnionIsFirstSet(A) eh {B, C, D, E, G, 'a'}
	// Em seguida, iteramos ate achar um ponto fixo, adicionando os elementos em firstSets.
	public Map<String, Set<String>> getAllFirstSets(){
		Map<String, Set<String>> setsWhoseUnionIsFirstSet = new HashMap<String, Set<String>>();
		Map<String, Set<String>> firstSets = new HashMap<String, Set<String>>();
		for (String NonTerminal: grammar.getNonTerminals()){
			setsWhoseUnionIsFirstSet.put(NonTerminal, new HashSet<String>());
			firstSets.put(NonTerminal, new HashSet<String>());
		}
		for (String NonTerminal: grammar.getNonTerminals()){
			for (String rule : grammar.getRules().get(NonTerminal)) {
				int i = 0;
				String[] arrRule = rule.split("\\s");
				while(!isTerminal(arrRule[i]) && producesEps(arrRule[i]) && i < arrRule.length){
					setsWhoseUnionIsFirstSet.get(NonTerminal).add(arrRule[i]);
					i++;
				}
				// se while iterou por todos os simbolos da producao,
				// entao todos os simbolos produzem eps
				// TODO: definir como representar eps
				if(i >= arrRule.length){
					setsWhoseUnionIsFirstSet.get(NonTerminal).add("eps");
				}
				else{
					if (isTerminal(arrRule[i])){
						setsWhoseUnionIsFirstSet.get(NonTerminal).add(arrRule[i]);
					}
					else{
						setsWhoseUnionIsFirstSet.get(NonTerminal).add(arrRule[i]);
					}
				}
			}
		}
		System.out.println("Description of first rules: " + setsWhoseUnionIsFirstSet);

		boolean changeOccurried = true;
		while (changeOccurried){
			System.out.println("New Iteration \n ------------");
			changeOccurried = false;
			// TODO: install apache commons, make newFirstSets get all elements from FirstSets
			Map<String, Set<String>> newFirstSets = new HashMap<String, Set<String>>();

			for (String nonTerminal: grammar.getNonTerminals()){
				Set<String> newSet = new HashSet<String>();
				newSet.addAll(firstSets.get(nonTerminal));
				newFirstSets.put(nonTerminal, newSet);
			}

			System.out.println("newFirstSets: " + newFirstSets);
			System.out.println("FirstSets: " + firstSets);

			for (String nonTerminal: grammar.getNonTerminals()){
				int numElementsBefore = firstSets.get(nonTerminal).size();
				for (String element : setsWhoseUnionIsFirstSet.get(nonTerminal)) {
					System.out.println("Element: " + element);
					if (isTerminal(element)){
						System.out.println("element is terminal: adding to set");
						newFirstSets.get(nonTerminal).add(element);
					} else if (element.equals("eps")){
						System.out.println("element is void set");
						newFirstSets.get(nonTerminal).add("eps");
					} else {
						System.out.println("element is non terminal");
						boolean setHadEps = newFirstSets.get(nonTerminal).contains("eps");
						newFirstSets.get(element).addAll(firstSets.get(nonTerminal));
						System.out.println("Adding elements: " + firstSets.get(nonTerminal));
						boolean setHasEps = newFirstSets.get(element).contains("eps");
						if (!setHadEps && setHasEps){
							newFirstSets.get(element).remove("eps");
						}
					}
				}
				int numElementsAfter = newFirstSets.get(nonTerminal).size();
				if (numElementsBefore != numElementsAfter){
					changeOccurried = true;
				}
			}

			System.out.println("Old first sets: "+ firstSets);
			System.out.println("New first sets: "+ newFirstSets);
			firstSets = newFirstSets;


		}
		System.out.println("------");
		System.out.println("Final result: ");
		System.out.println(firstSets);
		return firstSets;
	}


	/**
	 * Função utilizada para encontrar o first de um símbolo
	 * @param arg - Símbolo para aplicar o First
	 * @return Set<String> símbolos
	 * @throws Exception quando a gramática não possui o argumento
	 */
	public Set<String> getFirst(String arg) throws Exception {
		if (grammarHas(arg)) {
			Set<String> firsts = new HashSet<String>();
			
			//Se for uma regra, pega o primeiro dado da regra
			if (arg.split("\\s").length > 1) {
				String[] split = arg.split("\\s");
				
				String[] rest = new String[split.length-1];
				for (int i = 1; i < split.length; i++) {
					rest[i-1] = split[i];
				}
				
				firsts.addAll(disambiguate(getFirst(split[0]), getFirst(String.join(" ", rest))));
			} else {
				//Se for terminal, e estiver na gramática, adiciona ele mesmo
				if (isTerminal(arg)) {
					firsts.add(arg);
				} else {
					//Se não for terminal, vai chamar first para todas as regras daquele não terminal.
					for (String s : grammar.getRules().get(arg)) {
						firsts.addAll(getFirst(s));
					}
				}
			}
			
			return firsts;
		} else {
			throw new Exception();
		}
	}
	
	/**
	 * Encontra o follow daquele argumento
	 * @param arg - Símbolo para encontrar o follow
	 * @return Set<String> Follow de arg
	 */
	public Set<String> getFollow (String arg) {
		return null;
	}
	
	/**
	 * 
	 * @param a
	 * @param b
	 * @return
	 */
	public Set<String> disambiguate(Set<String> a, Set<String> b) {
		return null;
	}
	
	/**
	 * Checa se o argumento enviado é terminal
	 * @param arg
	 * @return boolean
	 */
	private boolean isTerminal(String arg) {
		return !arg.matches("^[A-Z].*");
	}
	
	/**
	 * Confere se a gramática é válida 
	 * @return boolean
	 */
	private boolean isValidGrammar() {
		return (grammar.getLeftTerminals().containsAll(grammar.getNonTerminals()));
	}
	
	/**
	 * Confere se a gramática possui aquela regra
	 * @param arg
	 * @return boolean
	 */
	private boolean grammarHas(String arg) {
		for (String a : arg.split("\\s")) {
			if (!(grammar.getTerminals().contains(a) || grammar.getNonTerminals().contains(a))) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		Grammar g = new Grammar("A -> b | B c\nB -> A | a");
		
		g.getRules().put("A", new HashSet<String>());
		g.getRules().get("A").add("b");
		g.getRules().get("A").add("B c");

		g.getRules().put("B", new HashSet<String>());
		g.getRules().get("B").add("A");
		g.getRules().get("B").add("a");
		
		g.getNonTerminals().add("A");
		g.getNonTerminals().add("B");
	
		GrammarService service = new GrammarService(g);
		service.getAllFirstSets();
	}
}
