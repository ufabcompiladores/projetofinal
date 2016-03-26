package com.ex.service;

import java.util.HashSet;
import java.util.Set;

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
	
	public Set<String> getFirst(String arg) throws Exception {
		if (grammarHas(arg)) {
			Set<String> firsts = new HashSet<String>();
			
			//Se for uma regra, pega o primeiro dado da regra
			if (arg.split("\\s").length > 1) {
				arg = arg.split("\\s")[0];
			}
			
			//Se for terminal, e estiver na gramática, adiciona ele mesmo
			if (isTerminal(arg)) {
				firsts.add(arg);
			} else {
				//Se não for terminal, vai chamar first para todas as regras daquele não terminal.
				for (String s : grammar.getRules().get(arg)) {
					firsts.addAll(getFirst(s));
				}
			}
			
			return firsts;
		} else {
			throw new Exception();
		}
	}
	
	private boolean isTerminal(String arg) {
		return !arg.matches("^[A-Z].*");
	}
	
	private boolean isValidGrammar() {
		return (grammar.getLeftTerminals().containsAll(grammar.getNonTerminals()));
	}
	
	private boolean grammarHas(String arg) {
		for (String a : arg.split("\\s")) {
			if (!(grammar.getTerminals().contains(a) || grammar.getNonTerminals().contains(a))) {
				return false;
			}
		}
		
		return true;
	}
	
	public static void main(String[] args) throws Exception {
		Grammar g = new Grammar();
		
		g.addRules("A -> b | B c\nB -> c | d");
		
		GrammarService service = new GrammarService(g);
		
		System.out.println(service.getFirst("A"));
	}
}
