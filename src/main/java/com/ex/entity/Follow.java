package com.ex.entity;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ex.entity.Symbol.SymbolType;

public class Follow {
	private Set<Symbol> firstSets;
	private Set<Symbol> firstSetsWithoutEps;
	private Set<Symbol> followSets;
	private Set<Symbol> terminals;
	private boolean hasEOF;


	public Follow() {
		super();
		this.firstSets = new HashSet<Symbol>();
		this.firstSetsWithoutEps = new HashSet<Symbol>();
		this.followSets = new HashSet<Symbol>();
		this.terminals = new HashSet<Symbol>();
		this.hasEOF = false;
	}


	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(" = {");
		// TODO: nao printar ultimo ∪
		for (Symbol sym : firstSets){
			string.append(String.format("First(%s) ∪ ", sym));
		}
		for (Symbol sym : firstSetsWithoutEps){
			string.append(String.format("First(%s) - {ε} ∪ ", sym));
		}
		for (Symbol sym : followSets){
			string.append(String.format("Follow(%s) ∪ ", sym));
		}
		for (Symbol sym : terminals){
			string.append(String.format("{%s} ∪ ", sym));
		}
		if (hasEOF){
			string.append("{$}");
		}
		string.append(" }");
		return string.toString();
	}


	public Set<Symbol> getFirstSets() {
		return firstSets;
	}


	public Set<Symbol> getFirstSetsWithoutEps() {
		return firstSetsWithoutEps;
	}


	public Set<Symbol> getFollowSets() {
		return followSets;
	}


	public Set<Symbol> getTerminals() {
		return terminals;
	}


	public boolean isHasEOF() {
		return hasEOF;
	}


	
	public Set<Symbol> update(Grammar grammar){
		Set<Symbol> elements = new HashSet<Symbol>();	
		StringBuilder stringb = new StringBuilder();
		stringb.append(String.format("Follow set description: %s \n", this));

		for (Symbol sym : firstSets){
			stringb.append(String.format("First(%s) = %s \n", sym, grammar.first(sym)));
			elements.addAll(grammar.first(sym));
		}
		for (Symbol sym : firstSetsWithoutEps){
			stringb.append(String.format("First(%s) - {ε} = %s - {ε}\n", sym, grammar.first(sym)));
			grammar.addAllElementsFromSetExceptEmptyString(grammar.first(sym), elements);
		}
		for (Symbol sym : followSets){
			stringb.append(String.format("Follow(%s)  = %s\n", sym, grammar.follow(sym)));
			elements.addAll(grammar.follow(sym));
		}
		for (Symbol sym : terminals){
			stringb.append(String.format("Adding {%s} \n", sym));
			elements.add(sym);
		}
		// TODO: handle this case
		if (hasEOF){
			stringb.append("{$}");
		}
		stringb.append(String.format("\n Elements: %s \n", elements));
		System.out.println(stringb.toString());
		return elements;
	}
	

}
