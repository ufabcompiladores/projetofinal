package com.ex.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ex.entity.Grammar;
import com.ex.entity.ParseTable;
import com.ex.entity.Rule;
import com.ex.entity.Symbol;
import com.ex.entity.Tuple;

@Service("LL")
public class LLService {
	
	private Grammar grammar;
	
	/**
	 * Constrói a tabela do parse do grammar do serviço.
	 * @return ParseTable
	 * @throws Exception - Caso a gramática não seja LL1.
	 */
	public ParseTable buildParseTable() throws Exception {
		ParseTable table;
		
		if (isLL1Grammar()) {
			table = new ParseTable(grammar.getTerminals(), grammar.getNonTerminals());

			for (Symbol producer : grammar.getRules().keySet()) {
				Set<Rule> rules = grammar.getRulesBySymbol(producer);
				
				for (Rule rule : rules) {
					Set<Symbol> firsts = grammar.first(rule);
					
					if (!rule.getProduction().contains(Symbol.DefaultSymbols.EMPTY.getSymbol())) {	
						for (Symbol terminalInFirst : firsts) {
							table.addRule(new Tuple(producer, terminalInFirst), rule);
						}
					} else {
						Set<Symbol> follows = grammar.follow(producer);
						
						for (Symbol terminalInFollow : follows) {
							table.addRule(new Tuple(producer, terminalInFollow), rule);
						}
					}
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

	public void setGrammar(Grammar grammar2) {
		this.grammar = grammar2;
	}
	
}
