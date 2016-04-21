package com.ex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ex.entity.Grammar;
import com.ex.entity.ParseTable;
import com.ex.entity.Rule;
import com.ex.entity.Symbol;
import com.ex.entity.Tuple;

@Service("LL")
public class LLService {
	
	private final Grammar grammar;
	
	public LLService(Grammar grammar) {
		this.grammar = grammar;
	}

	public ParseTable buildParseTable() throws Exception {
		ParseTable table;
		
		if (isLL1Grammar()) {
			HashSet<Tuple> set = new HashSet<Tuple>();
			for (Symbol n : grammar.getNonTerminals()) {
				for (Symbol t : grammar.getTerminals()) {
					set.add(new Tuple(n, t));
				}
				set.add(new Tuple(n, Symbol.DefaultSymbols.EMPTY.getSymbol()));
			}
			
			table = new ParseTable(set, grammar.getTerminals());
			
			

			for (Symbol nonTerminal : grammar.getNonTerminals()) {
				Set<Rule> rules = grammar.getRulesBySymbol(nonTerminal);
				//Se não contiver palavra vazia procura firsts, se tiver, follows.
				if (!rules.contains(Symbol.DefaultSymbols.EMPTY.getSymbol())) {
					for (Rule r : rules) {
						Set<Symbol> firsts = grammar.first(r);
						
						for (Symbol terminalInFirst : firsts) {
							table.addRule(nonTerminal, terminalInFirst, r);
						}
					}
				} else {
					for (Rule r : rules) {
						Set<Symbol> follows = grammar.follow(r);
						
						for (Symbol terminalInFollow : follows) {
							table.addRule(nonTerminal, terminalInFollow, r);
						}
					}
				}
			}
			
			for (Tuple t : table.getTable().keySet()) {
				if (table.getTable().get(t) == null) {
					table.addRule(t, Rule.ERROR);
				}
			}
		} else {
			throw new Exception("Gramática não é LL1.");
		}

		return table;
	}

	private boolean isLL1Grammar() {

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
					Set<Symbol> firstSet = grammar.first(r);

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
		 * Confere se, quando existe regra b -> eps, então First(a) intersecção
		 * Follow(S) = null
		 */
		for (Symbol s : map.keySet()) {
			if (map.get(s).equals(true)) {
				Set<Symbol> followSet = grammar.getFollowSets().get(s);

				for (Rule rule : grammar.getRules().get(s)) {
					for (Symbol symbol : rule.getProduction()) {
						if (!symbol.isEmptyString()) {
							Set<Symbol> firstSet = grammar.first(symbol);

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
}
