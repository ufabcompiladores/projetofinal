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
	
	/**
	*  Descrevemos cada conjunto First como a uniao de outros conjuntos First.
	*  Por exemplo, se a regra eh A -> BCD | G | a,
	*  e se B e C tem cadeia vazia em suas producoes, mas G nao, entao
	*  First(A) eh {B, C, D, G, a}, em que cada elemento desse conjunto
	*  eh o first desse symbol. Ou seja,
	*  First(A) = First(B) ∪ First(C) ∪ First(D) ∪ First(G) ∪ First(a).
	*  @return Um map de Symbol para o conjunto de Symbols cuja união é o First desse Symbol.
	*/
	private Map<Symbol, Set<Symbol>> buildUnionOfSetsThatRepresetFirstSets(){
		// Initialize set
		Map<Symbol, Set<Symbol>> setsWhoseUnionIsFirstSet = new HashMap<Symbol, Set<Symbol>>();
		for (Symbol nonTerminal: grammar.getNonTerminals()){
			setsWhoseUnionIsFirstSet.put(nonTerminal, new HashSet<Symbol>());
		}
		
		// Build set
		for (Symbol nonTerminal: grammar.getNonTerminals()){
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
	

	/**
	 * Computa o conjunto First de cada não terminal.
	 * Isto é feito da mesma maneira usual na literatura,
	 * em que se usa uma tabela que é atualizada até
	 * encontrar um ponto fixo.
	 * @return Um Map de Symbol para o first set desse Symbol.
	 */
	private Map<Symbol, Set<Symbol>> buildAllFirstSets(){
		// Initialize set.
		Map<Symbol, Set<Symbol>> firstSetsBeforeIteration = new HashMap<Symbol, Set<Symbol>>();
		for (Symbol nonTerminal: grammar.getNonTerminals()){
			firstSetsBeforeIteration.put(nonTerminal, new HashSet<Symbol>());
		}
		
		// Get union of sets that represent each first set.
		Map <Symbol, Set<Symbol>> setsWhoseUnionIsFirstSet = this.buildUnionOfSetsThatRepresetFirstSets();

		// Iterate until fixed point is found
		boolean someFirstSetHasChanged = true;
		while (someFirstSetHasChanged){
			System.out.println("\n New Iteration \n ------------");
			someFirstSetHasChanged = false;
			Map<Symbol, Set<Symbol>> newFirstSets = new HashMap<Symbol, Set<Symbol>>();

			for (Symbol nonTerminal: grammar.getNonTerminals()){
				Set<Symbol> newSet = new HashSet<Symbol>();
				newSet.addAll(firstSetsBeforeIteration.get(nonTerminal));
				newFirstSets.put(nonTerminal, newSet);
			}

			System.out.println("newFirstSets: " + newFirstSets);
			System.out.println("FirstSets: " + firstSetsBeforeIteration);

			for (Symbol nonTerminal: grammar.getNonTerminals()){
				System.out.println("---- \nUpdating set " + nonTerminal);
				System.out.format("Sets whose union is first(%s): %s\n", nonTerminal, setsWhoseUnionIsFirstSet.get(nonTerminal));
				int numElementsBefore = firstSetsBeforeIteration.get(nonTerminal).size();
				for (Symbol element : setsWhoseUnionIsFirstSet.get(nonTerminal)) {
					System.out.println("Element: " + element);
					if (element.isTerminal()){
						System.out.println("element is terminal: adding to set");
						newFirstSets.get(nonTerminal).add(element);
					} else if (element.isEmptyString()){
						System.out.println("element is empty string");
						newFirstSets.get(nonTerminal).add(new Symbol(SymbolType.EMPTYSTRING, ""));
					} else {
						System.out.println("element is non terminal");
						addAllElementsFromSetExceptEmptyString(firstSetsBeforeIteration.get(element), newFirstSets.get(nonTerminal));
						System.out.println("Adding elements: " + firstSetsBeforeIteration.get(element) + " (except eps)");
					}
				}
				int numElementsAfter = newFirstSets.get(nonTerminal).size();
				if (numElementsBefore != numElementsAfter){
					someFirstSetHasChanged = true;
				}
			}
			System.out.println("Old first sets: "+ firstSetsBeforeIteration);
			System.out.println("New first sets: "+ newFirstSets);
			firstSetsBeforeIteration = newFirstSets;
		}
		System.out.println("------");
		System.out.println("Final result: ");
		System.out.println(firstSetsBeforeIteration);
		return firstSetsBeforeIteration;
	}



	/**
	 * Encontra o follow daquele argumento
	 * @param arg - Símbolo para encontrar o follow
	 * @return Set<String> Follow de arg
	 */
	public Set<String> buildAllFollowSets (String arg) {
		return null;
	}

	public static void main(String[] args) throws Exception {
		Grammar g = new Grammar("A -> B C d \nB -> b | \n C -> a | ");
		GrammarService service = new GrammarService(g);
		System.out.println(g.getNonTerminals());
		service.buildAllFirstSets();
	}
}
