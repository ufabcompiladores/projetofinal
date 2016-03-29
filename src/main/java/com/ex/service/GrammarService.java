package com.ex.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.ex.entity.Grammar;

@Service
public class GrammarService {
	
	private Grammar grammar;
	
	public GrammarService(Grammar grammar) throws Exception {
		this.grammar = grammar;
		
		if (!isValidGrammar()) {
			throw new Exception("Gramática inválida");
		}
	}

	// TODO: finish function
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
			setsWhoseUnionIsFirstSet.put(NonTerminal, Collections.<String> emptySet());
			firstSets.put(NonTerminal, Collections.<String> emptySet());
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
				if(i > arrRule.length){
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

		boolean changeOccurried = true;
		while (changeOccurried){
			changeOccurried = false;
			// TODO: install apache commons, make newFirstSets get all elements from FirstSets
			Map<String, Set<String>> newFirstSets = new HashMap<String, Set<String>>();

			for (String NonTerminal: grammar.getNonTerminals()){
				newFirstSets.put(NonTerminal, Collections.<String> emptySet());
			}
			for (String nonTerminal: grammar.getNonTerminals()){
				int numElementsBefore = firstSets.get(nonTerminal).size();
				for (String element : setsWhoseUnionIsFirstSet.get(nonTerminal)) {
					if (isTerminal(element)){
						newFirstSets.get(nonTerminal).add(element);
					} else if (element.equals("eps")){
						newFirstSets.get(nonTerminal).add("eps");
					} else {
						boolean setHadEps = newFirstSets.get(nonTerminal).contains("eps");
						newFirstSets.get(nonTerminal).addAll(firstSets.get(nonTerminal));
						boolean setHasEps = newFirstSets.get(nonTerminal).contains("eps");
						if (!setHadEps && setHasEps){
							newFirstSets.get(nonTerminal).remove("eps");
						}
					}
				}
				int numElementsAfter = newFirstSets.get(nonTerminal).size();
				if (numElementsBefore != numElementsAfter){
					changeOccurried = true;
				}
			}

			firstSets = newFirstSets;


		}
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
		Grammar g = new Grammar("a -> b | B c\nB -> c | d");
		
		// g.addRules("A -> b | B c\nB -> c | d");
		g.setNonTerminalsTest(new HashSet<String>());
//		g.setRulesTest
//		g.getNonTerminals().add("A");
//		g.getNonTerminals().add("B");
		
		
		
		GrammarService service = new GrammarService(g);
		
//		service.getAllFirstSets();
		 System.out.println(service.getFirst("A"));
	}
}
