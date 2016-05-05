package com.ex.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ex.entity.Grammar;
import com.ex.entity.ParseTable;
import com.ex.entity.Rule;
import com.ex.entity.Symbol;
import com.ex.entity.Tuple;

@Service("LL")
public class LLService {

	private Grammar grammar;
	private ParseTable parse;

	/**
	 * Constrói a tabela do parse do grammar do serviço.
	 * 
	 * @return ParseTable
	 * @throws Exception
	 *             - Caso a gramática não seja LL1.
	 */
	public ParseTable buildParseTable() throws Exception {
		ParseTable table;

		table = new ParseTable(grammar.getTerminals(), grammar.getNonTerminals());

		for (Symbol producer : grammar.getRules().keySet()) {
			Set<Rule> rules = grammar.getRulesBySymbol(producer);

			for (Rule rule : rules) {
				Set<Symbol> firsts = grammar.first(rule);

				for (Symbol terminalInFirst : firsts) {
					addRule(new Tuple(producer, terminalInFirst), rule);
				}

				if (rule.getProduction().contains(Symbol.DefaultSymbols.EMPTY.getSymbol())) {
					Set<Symbol> follows = grammar.follow(producer);

					for (Symbol terminalInFollow : follows) {
						addRule(new Tuple(producer, terminalInFollow), rule);
					}
				}
			}
		}

		return table;
	}

	/**
	 * Checa: Para qualquer S -> a | b, First(a) intersecção First(b) = null E
	 * se b = vazio, então verifica com Follow(b).
	 */
	public boolean isLL1Grammar() {
		for (Set<Rule> rules : grammar.getRules().values()) {
			Set<Symbol> list = new HashSet<Symbol>();

			//Não foi testado após alteração para arrumar como a prof. disse
			for (Rule r : rules) {
				if (!r.getProduction().contains(Symbol.DefaultSymbols.EMPTY.getSymbol())) {
					Set<Symbol> first = grammar.first(r);

					if (first.stream().anyMatch(f -> list.contains(f))) {
						return false;
					} else {
						list.addAll(first);
					}
				} else {
					Set<Symbol> follow = grammar.follow(r.getProducer());

					if (follow.stream().anyMatch(f -> list.contains(f))) {
						return false;
					} else {
						list.addAll(follow);
					}
				}

			}
		}

		return true;
	}

	public void setGrammar(Grammar grammar2) {
		this.grammar = grammar2;
	}

	/**
	 * Confere se existem regras duplicadas para um mesmo produtor e terminal.
	 * 
	 * @return
	 */
	public boolean isAmbiguous() {
		for (Symbol s : parse.getTable().keySet()) {
			if (parse.getTable().get(s).values().stream().anyMatch(p -> p.size() > 1))
				return true;
		}

		return false;
	}

	/**
	 * Adiciona regra a partir de uma tupla (Conjunto do produtor e o terminal
	 * [que está no first])
	 * 
	 * @param t
	 *            - Tupla com o valor do produtor e terminal que entrará aquela
	 *            regra.
	 * @param r
	 *            - Regra a ser inserida.
	 */
	private void addRule(Tuple t, Rule r) {
		if (!t.getUpSymbol().equals(Symbol.DefaultSymbols.EMPTY.getSymbol()))
			parse.getTable().get(t.getLeftSymbol()).get(t.getUpSymbol()).add(r);
	}

}
