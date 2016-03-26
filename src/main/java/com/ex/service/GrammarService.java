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
		Grammar g = new Grammar();
		
		g.addRules("A -> b | B c\nB -> c | d");
		
		GrammarService service = new GrammarService(g);
		
		System.out.println(service.getFirst("A"));
	}
}
