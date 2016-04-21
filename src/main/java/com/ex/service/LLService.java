package com.ex.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
			
			for (Symbol nonTerminal : grammar.getNonTerminals()) {
				for (Symbol terminal : grammar.getTerminals()) {
					set.add(new Tuple(nonTerminal, terminal));
				}
			}
			
			table = new ParseTable(set, grammar.getTerminals());
			
			

			for (Symbol nonTerminal : grammar.getNonTerminals()) {
				Set<Rule> rules = grammar.getRulesBySymbol(nonTerminal);
				//Se não contiver palavra vazia procura firsts, se tiver, follows.
				for (Rule r : rules) {
					Set<Symbol> firsts = grammar.first(r);
						
					if (!r.getProduction().contains(Symbol.DefaultSymbols.EMPTY.getSymbol())) {	
						for (Symbol terminalInFirst : firsts) {
							table.addRule(new Tuple(nonTerminal, terminalInFirst), r);
						}
					} else {
						Set<Symbol> follows = grammar.follow(nonTerminal);
						
						for (Symbol terminalInFollow : follows) {
							table.addRule(new Tuple(nonTerminal, terminalInFollow), r);
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

	/**
	 * Checa: Para qualquer S -> a | b, First(a) intersecção First(b) = null
	 * E se b = vazio, então verifica com Follow(b).
	 */
	private boolean isLL1Grammar() {
		for (Set<Rule> rules : grammar.getRules().values()) {
			List<Set<Symbol>> list = new ArrayList<Set<Symbol>>();

			for (Rule r : rules) {
				if (!r.getProduction().contains(Symbol.DefaultSymbols.EMPTY.getSymbol())) {
					Set<Symbol> first = grammar.first(r);
					
					if (list.contains(first)) {
						return false;
					} else {
						list.add(first);
					}
				} else {
					Set<Symbol> follow = grammar.follow(r.getProducer());
					
					if (list.contains(follow)) {
						return false;
					} else {
						list.add(follow);
					}
				}
				
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		Grammar grammar = new Grammar("A -> B e C B B B d \nB -> b | \n C -> a | f");
		
		LLService service = new LLService(grammar);
		
		System.out.println(service.buildParseTable());
		
	}
}
