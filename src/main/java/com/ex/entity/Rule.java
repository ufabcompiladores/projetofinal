package com.ex.entity;

import java.util.ArrayList;
import java.util.List;

public final class Rule {
	
	private Symbol producer;
	private List<Symbol> production;

	/**
	 * Cria a regra a partir do seu produtor, e a lista de símbolos, que serão a produção daquela regra.
	 * @param producer
	 * @param production
	 */
	public Rule(Symbol producer, ArrayList<Symbol> production) {
		super();
		this.producer = producer;
		this.production = production;
	}

	/**
	 * Cria a regra a partir de duas Strings (produtor e produção). Em geral só é utilizada para criar regras a partir da entrada.
	 * Quando já existir a gramática, adicionar pelo construtor {@link #Rule(Symbol, ArrayList) Rule} constructor
	 * @param producer
	 * @param rightHandSide
	 * @throws Exception
	 */
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
	
}
