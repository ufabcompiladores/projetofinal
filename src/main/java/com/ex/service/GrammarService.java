package com.ex.service;

import java.util.*;

import org.springframework.stereotype.Service;

import com.ex.entity.Grammar;
import com.ex.entity.Rule;
import com.ex.entity.Symbol;
import com.ex.entity.Symbol.SymbolType;

@Service
public class GrammarService {

	private Grammar grammar;

	public GrammarService(Grammar grammar) throws Exception {
		this.grammar = grammar;
	}

	private boolean producesEps(Symbol symbol) {
		for (Rule rule : this.grammar.getRules().get(symbol)){
			if (rule.producesEmptyString()) {
				return true;	
			}
		}
		return false;
	}
	
	private void addAllElementsFromSetExceptEmptyString(Set<Symbol> setFrom, Set<Symbol> setTo){
		for (Symbol symbol : setFrom) {
			if (!symbol.isEmptyString()){
				setTo.add(symbol);
			}
		}
	}
	
	// Descrevemos cada conjunto First como a uniao de outros conjuntos First em setsWhoseUnionIsFirstSet.
	// Por exemplo, se a regra eh A -> BCD | EF | G | a, e se B e C tem 'eps' em suas producoes, mas
	// E e G nao, entao
	// setsWhoseUnionIsFirstSet(A) eh {B, C, D, E, G, 'a'}
	private Map<Symbol, Set<Symbol>> buildUnionOfSetsThatRepresetFirstSets(){
		// Initialize set
		Map<Symbol, Set<Symbol>> setsWhoseUnionIsFirstSet = new HashMap<Symbol, Set<Symbol>>();
		for (Symbol nonTerminal: grammar.getNewNonTerminals()){
			setsWhoseUnionIsFirstSet.put(nonTerminal, new HashSet<Symbol>());
		}
		
		// Build set
		for (Symbol nonTerminal: grammar.getNewNonTerminals()){
			for (Rule rule : grammar.getRules().get(nonTerminal)) {
				int i = 0;
				while(rule.production.get(i).isNonTerminal() && 
						producesEps(rule.production.get(i)) &&
						i < rule.production.size()){
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(rule.production.get(i));
					i++;
				}
				// se while percorreu todos os simbolos da producao,
				// entao todos os nao terminais iterados produzem cadeia vazia
				if(i >= rule.production.size()){
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(new Symbol(SymbolType.EMPTYSTRING, ""));
				}
				else{
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(rule.production.get(i));
				}
			}
		}
		System.out.println("Description of first rules: " + setsWhoseUnionIsFirstSet);
		return setsWhoseUnionIsFirstSet;
	}
	

	// Iteramos ate achar um ponto fixo, adicionando os elementos em firstSets.
	public Map<Symbol, Set<Symbol>> buildAllFirstSets(){
		// Initialize set.
		Map<Symbol, Set<Symbol>> firstSets = new HashMap<Symbol, Set<Symbol>>();
		for (Symbol nonTerminal: grammar.getNewNonTerminals()){
			firstSets.put(nonTerminal, new HashSet<Symbol>());
		}
		
		// Get union of sets that represent each first set.
		Map <Symbol, Set<Symbol>> setsWhoseUnionIsFirstSet = this.buildUnionOfSetsThatRepresetFirstSets();

		// Iterates until fixed point is found
		boolean someFirstSetHasChanged = true;
		while (someFirstSetHasChanged){
			System.out.println("\n New Iteration \n ------------");
			someFirstSetHasChanged = false;
			Map<Symbol, Set<Symbol>> newFirstSets = new HashMap<Symbol, Set<Symbol>>();

			for (Symbol nonTerminal: grammar.getNewNonTerminals()){
				Set<Symbol> newSet = new HashSet<Symbol>();
				newSet.addAll(firstSets.get(nonTerminal));
				newFirstSets.put(nonTerminal, newSet);
			}

			System.out.println("newFirstSets: " + newFirstSets);
			System.out.println("FirstSets: " + firstSets);

			for (Symbol nonTerminal: grammar.getNewNonTerminals()){
				System.out.println("---- \nUpdating set " + nonTerminal);
				System.out.format("Sets whose union is first(%s): %s\n", nonTerminal, setsWhoseUnionIsFirstSet.get(nonTerminal));
				int numElementsBefore = firstSets.get(nonTerminal).size();
				for (Symbol element : setsWhoseUnionIsFirstSet.get(nonTerminal)) {
					System.out.println("Element: " + element);
					if (element.isTerminal()){
						System.out.println("element is terminal: adding to set");
						newFirstSets.get(nonTerminal).add(element);
					} else if (element.type == SymbolType.EMPTYSTRING){
						System.out.println("element is empty string");
						newFirstSets.get(nonTerminal).add(new Symbol(SymbolType.EMPTYSTRING, ""));
					} else {
						System.out.println("element is non terminal");
						addAllElementsFromSetExceptEmptyString(firstSets.get(element), newFirstSets.get(nonTerminal));
						System.out.println("Adding elements: " + firstSets.get(element) + " (except eps)");
					}
				}
				int numElementsAfter = newFirstSets.get(nonTerminal).size();
				if (numElementsBefore != numElementsAfter){
					someFirstSetHasChanged = true;
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
	 * Encontra o follow daquele argumento
	 * @param arg - Símbolo para encontrar o follow
	 * @return Set<String> Follow de arg
	 */
	public Set<String> getFollow (String arg) {
		return null;
	}

	public static void main(String[] args) throws Exception {
//		Grammar g = new Grammar("A -> b | B c\nB -> A | a");
		Grammar g = new Grammar("A -> B C d \nB -> b | \n C -> a | ");
		GrammarService service = new GrammarService(g);
		System.out.println(g.getNewNonTerminals());
		service.buildAllFirstSets();
		//		g.readAllRules("A -> Baaa888f | B c | \nB -> A | a | D d e F \n J->aaa|b");
		//		System.out.println(g.getNewRules());
	}
}
