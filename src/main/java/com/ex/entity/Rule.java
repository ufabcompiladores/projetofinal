package com.ex.entity;

import java.util.ArrayList;
import java.util.List;

public final class Rule {
	
	private Symbol producer;
	private List<Symbol> production;
	private int number;

	/**
	 * Cria a regra a partir do seu produtor, e a lista de símbolos, que serão a produção daquela regra.
	 * @param producer
	 * @param production
	 */
	public Rule(Symbol producer, List<Symbol> production, int number) {
		super();
		this.producer = producer;
		this.production = production;
		this.number = number;
	}

	/**
	 * Cria a regra a partir de duas Strings (produtor e produção). Em geral só é utilizada para criar regras a partir da entrada.
	 * Quando já existir a gramática, adicionar pelo construtor {@link #Rule(Symbol, ArrayList) Rule} constructor
	 * @param producer
	 * @param rightHandSide
	 * @throws Exception
	 */
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
