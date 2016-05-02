package com.ex.entity;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO: extend this description.
 * Represents a first set.
 * @author andre0991
 *
 */
public final class First {
	
	public First(Set<Symbol> firstSets, Set<Symbol> firstSetsWithoutEps, boolean hasEps) {
		super();
		this.firstSets = firstSets;
		this.firstSetsWithoutEps = firstSetsWithoutEps;
		this.hasEps = hasEps;
	}

	private Set<Symbol> firstSets;
	private Set<Symbol> firstSetsWithoutEps;
	private boolean hasEps;

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("{");
		// TODO: nao printar ultimo ∪
		for (Symbol sym : firstSets){
			string.append(String.format("First(%s) ∪ ", sym));
		}
		for (Symbol sym : firstSetsWithoutEps){
			string.append(String.format("First(%s) - {ε} ∪ ", sym));
		}
		if (hasEps){
			string.append("{ε}");
		}
		// TODO: if end is " union ", then delete it
		if (string.substring(string.length() - 3, string.length()).equals(" ∪ ")) {
			string.delete(string.length() - 3, string.length());
		}
		string.append(" }");
		return string.toString();
	}
	
	public Set<Symbol> getAllElements(Map<Symbol, Set<Symbol>> currentFirstSets){
		Set<Symbol> elements = new HashSet<Symbol>();	
		StringBuilder stringb = new StringBuilder();
		stringb.append(String.format("First set description: %s \n", this));

		for (Symbol sym : firstSets){
			stringb.append(String.format("First(%s) = %s \n", sym, currentFirstSets.get(sym)));
			elements.addAll(currentFirstSets.get(sym));
		}
		for (Symbol sym : firstSetsWithoutEps){
			stringb.append(String.format("First(%s) - {ε} = %s - {ε}\n", sym, currentFirstSets.get(sym)));
			Grammar.addAllElementsFromSetExceptEmptyString(currentFirstSets.get(sym), elements);
		}
		if (hasEps) {
			stringb.append("{ε}");
			elements.add(new Symbol(Symbol.SymbolType.EMPTYSTRING, ""));
		}
		return elements;
	}
	

}
