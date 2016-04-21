package com.ex.entity;

import java.util.ArrayList;
import java.util.List;

public final class Rule {
	
	public static final Rule ERROR = new Rule(new Symbol(Symbol.SymbolType.TERMINAL, "ERROR"), new ArrayList<Symbol>());

	private Symbol producer;
	private List<Symbol> production;

	public Rule(Symbol producer, ArrayList<Symbol> production) {
		super();
		this.producer = producer;
		this.production = production;
	}

	public Rule(String producer, String rightHandSide) throws Exception {
		this.production = new ArrayList<Symbol>();
		this.producer = new Symbol(producer);
		String[] symbols = rightHandSide.split("\\s");
		for (int i = 0; i < symbols.length; i++) {
			production.add(new Symbol(symbols[i]));
		}
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(String.format("%s -> ", producer));
		for (Symbol sym : production){
			string.append(String.format(" %s ", sym));
		}
		return string.toString();
	}

	public boolean producesEmptyString() {
		return this.production.get(0).isEmptyString();
	}

	public Symbol getProducer() {
		return producer;
	}

	public List<Symbol> getProduction() {
		return production;
	}
	
	public static void main(String[] args) {
		System.out.println(new Rule (new Symbol(Symbol.SymbolType.NONTERMINAL, "A"), new ArrayList<Symbol>(){{add(new Symbol(Symbol.SymbolType.TERMINAL, "a"));}}));
	}
}
