package com.ex.entity;

import java.util.ArrayList;
import java.util.List;

public final class Rule {
	
	public static final Rule ERROR = new Rule(new Symbol(Symbol.SymbolType.TERMINAL, "ERROR"), new ArrayList<Symbol>(), -1);

	private Symbol producer;
	private List<Symbol> production;
	private int number;

	public Rule(Symbol producer, ArrayList<Symbol> production, int number) {
		super();
		this.producer = producer;
		this.production = production;
		this.number = number;
	}

	public Rule(String producer, String rightHandSide, int number) throws Exception {
		this.production = new ArrayList<Symbol>();
		this.producer = new Symbol(producer);
		this.number = number;
		String[] symbols = rightHandSide.split("\\s");
		for (int i = 0; i < symbols.length; i++) {
			production.add(new Symbol(symbols[i]));
		}
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(String.format("(%s) %s -> ", number, producer));
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
	
	public int getNumber() {
		return this.number;
	}
	
	public static void main(String[] args) {
//		System.out.println(new Rule (new Symbol(Symbol.SymbolType.NONTERMINAL, "A"), new ArrayList<Symbol>(){{add(new Symbol(Symbol.SymbolType.TERMINAL, "a"));}}));
	}
}
