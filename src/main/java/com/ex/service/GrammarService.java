package com.ex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ex.entity.Follow;
import com.ex.entity.Grammar;
import com.ex.entity.ParseTable;
import com.ex.entity.Rule;
import com.ex.entity.Symbol;
import com.ex.entity.Symbol.SymbolType;
import com.ex.entity.Tuple;

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
				while(rule.getProduction().get(i).isNonTerminal() && 
						producesEps(rule.getProduction().get(i)) &&
						i < rule.getProduction().size()){
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(rule.getProduction().get(i));
					i++;
				}
				// se while percorreu todos os simbolos da producao,
				// entao todos os nao terminais iterados produzem cadeia vazia
				if(i >= rule.getProduction().size()){
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(new Symbol(SymbolType.EMPTYSTRING, ""));
				}
				else{
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(rule.getProduction().get(i));
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

	public List<Symbol> buildFirstSet (Rule rule) {
		//TODO
		return null;
	}

	/**
	 * Encontra o follow daquele argumento
	 * @param arg - Símbolo para encontrar o follow
	 * @return Set<String> Follow de arg
	 */
	public Map<Symbol, Set<Symbol>> buildAllFollowSets() {
		//TODO
		return new HashMap<Symbol, Set<Symbol>>();
	}
	
	public ParseTable buildParseTable () throws Exception {
		ParseTable table = new ParseTable();
		
		Map<Symbol, Set<Symbol>> firstSets = buildAllFirstSets();
		Map<Symbol, Set<Symbol>> followSets = buildAllFollowSets();
		
		if (isLL1Grammar()) {
			HashSet<Tuple> set = new HashSet<Tuple>();
			for (Symbol n : grammar.getNonTerminals()) {
				for (Symbol t : grammar.getTerminals()) {
					set.add(new Tuple(n, t));
				}
			}
			
			for (Symbol n : grammar.getNonTerminals()) {
				for (Symbol t : grammar.getTerminals()) {
					//Se não contém vazio, coloca as regras que estão no first.
					if (firstSets.get(n).contains(t)) {
						//TODO a função first/follow neste caso deve retornar a regra a ser colocada na tabela.
						table.addRule(n, t, first(n, t));
					} else if (firstSets.get(n).contains(Symbol.EMPTY_STRING_REGEX)) {
						table.addRule(n, t, follow(n, t));
					}
				}
			}
		} else {
			throw new Exception ("Gramática não é LL1.");
		}
		
		return table;
	}
	
	// TODO: criado para evitar erro no projeto
	private Rule follow(Symbol n, Symbol t) {
		// TODO Auto-generated method stub
		return null;
	}

	// TODO: criado para evitar erro no projeto
	private Rule first(Symbol n, Symbol t) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Função checará se a gramática é LL(1)
	 * @return
	 */
	public boolean isLL1Grammar () {
		
		Map<Symbol, Boolean> map = new HashMap<Symbol, Boolean>();
		
		for (Set<Rule> rules : grammar.getRules().values()) {
			for (Rule r : rules) {
				for (Symbol s : r.getProduction()) {
					if (s.isEmptyString()) {
						map.put(r.getProducer(), true);
						break;
					}
				}
				if (!map.isEmpty()) {
					break;
				}
			}
			if (!map.isEmpty()) {
				break;
			}
		}
		
		/*
		 * Checa: Para qualquer S -> a | b, First(a) intersecção First(b) = null
		 */
		for (Set<Rule> rules : grammar.getRules().values()) {
			List<Symbol> firstList = new ArrayList<Symbol>();
			
			for (Rule r : rules) {
				if (r.getProduction().size() != 1) {
					List<Symbol> firstSet = buildFirstSet(r);
					
					for (Symbol s : firstSet) {
						if (firstList.contains(s)) {
							return false;
						}
					}
					
					firstList.addAll(firstSet);
				}
			}
		}
		
		/*
		 * Confere se, quando existe regra b -> eps, então First(a) intersecção Follow(S) = null
		 */
		for (Symbol s : map.keySet()) {
			if (map.get(s).equals(true)) {
				List<Symbol> followSet = buildFollowSet(s);
				
				for (Rule rule : grammar.getRules().get(s)) {
					for (Symbol symbol : rule.getProduction()) {
						if (!symbol.isEmptyString()) {
							List<Symbol> firstSet = buildFirstSet(symbol);
							
							for (Symbol firstResult : firstSet) {
								if (followSet.contains(firstResult)) {
									return false;
								}
							}
						}
					}
				}
			}
		}
		
		return true;
	}
	
	public Set<Symbol> first(Grammar grammar, Rule rule){
		Set<Symbol> firstFromRule = new HashSet<Symbol>();
		if(rule.producesEmptyString()){
			firstFromRule.add(new Symbol(SymbolType.EMPTYSTRING, ""));
			return firstFromRule;
		}

		// 👀
		int indexSymbol = 0;
		Symbol sym = rule.getProduction().get(indexSymbol);
		while(sym.isNonTerminal() && 
				this.firstSetFromSymbol(sym).contains(new Symbol(SymbolType.EMPTYSTRING, "")) &&
				indexSymbol < rule.getProduction().size()){
			// todo: excluir eps
			firstFromRule.addAll(this.firstSetFromSymbol(sym));
			indexSymbol++;
			sym = rule.getProduction().get(indexSymbol);
		}
		
		// se chegou ate o final, todos produzem ε, assim first produz ε
		if (indexSymbol == rule.getProduction().size() - 1){
			firstFromRule.add(new Symbol(SymbolType.EMPTYSTRING, ""));
			return firstFromRule;
		}
		
		if (sym.isTerminal()){
			firstFromRule.add(sym);
			return firstFromRule;
		}
		
/*		se nao eh terminal
 *      nem ε 
 *      nem nao terminal que produz ε,
		so pode ser nao terminal que nao produz ε.
*/
		firstFromRule.addAll(firstSetFromSymbol(sym));
		return firstFromRule;
	}	

	// temp para evitar erros - vai ser mudada para grammar (confirmar)
	private Set<Symbol> firstSetFromSymbol(Symbol sym) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Deve construir o First de um símbolo
	 * @param symbol
	 * @return
	 */
	private List<Symbol> buildFirstSet(Symbol symbol) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Deve calcular o follow para um símbolo não terminal pertencente ao produtor da regra da gramática.
	 * @param s
	 */
	private List<Symbol> buildFollowSet(Symbol s) {
		// TODO Auto-generated method stub
		return null;
	}

	public static void main(String[] args) throws Exception {
		Grammar g = new Grammar("A -> B C d \nB -> b | \n C -> a | ");
		GrammarService service = new GrammarService(g);
		System.out.println(g.getNonTerminals());
		service.buildAllFirstSets();
		System.out.println(g);
	}
}
