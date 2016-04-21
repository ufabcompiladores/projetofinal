package com.ex.entity;

import java.util.Set;

import javax.persistence.Entity;

@Entity
public class SLRTable {

	private Set<Symbol> symbols;
	private Grammar grammar;

	public Set<Symbol> getSymbols() {
		return symbols;
	}

	public void setSymbols(Set<Symbol> symbols) {
		this.symbols = symbols;
	}

	public Grammar getGrammar() {
		return grammar;
	}

	public void setGrammar(Grammar grammar) {
		this.grammar = grammar;
	}

}
