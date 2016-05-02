package com.ex.entity;
import java.util.HashSet;
import java.util.Set;

/**
 * TODO: extend this description.
 * Represents a follow set.
 * @author andre0991
 *
 */
public final class Follow {
	private Set<Symbol> firstSets;
	private Set<Symbol> firstSetsWithoutEps;
	private Set<Symbol> followSets;
	private Set<Symbol> terminals;
	private boolean hasEOF;


	public Follow(boolean hasEOF) {
		super();
		this.firstSets = new HashSet<Symbol>();
		this.firstSetsWithoutEps = new HashSet<Symbol>();
		this.followSets = new HashSet<Symbol>();
		this.terminals = new HashSet<Symbol>();
		this.hasEOF = hasEOF;
	}


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
		for (Symbol sym : followSets){
			string.append(String.format("Follow(%s) ∪ ", sym));
		}
		for (Symbol sym : terminals){
			string.append(String.format("{%s} ∪ ", sym));
		}
		if (hasEOF){
			string.append("{$}");
		}
		// TODO: if end is " union ", then delete it
		if (string.substring(string.length() - 3, string.length()).equals(" ∪ ")) {
			string.delete(string.length() - 3, string.length());
		}
		string.append("}");
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


	
	/**
	 * Compute the set of all symbols according to the description of this follow set.
	 * For example, if the follow object represents "First(A) union Follow(B)", then
	 * First(A) union Follow(B) will be computed.
	 * @param grammar
	 * @return
	 */
	public Set<Symbol> getAllElements(Grammar grammar){
		Set<Symbol> elements = new HashSet<Symbol>();	
		StringBuilder stringb = new StringBuilder();

		for (Symbol sym : firstSets){
			stringb.append(String.format("First(%s) = %s \n", sym, grammar.first(sym)));
			elements.addAll(grammar.first(sym));
		}
		for (Symbol sym : firstSetsWithoutEps){
			stringb.append(String.format("First(%s) - {ε} = %s - {ε}\n", sym, grammar.first(sym)));
			Grammar.addAllElementsFromSetExceptEmptyString(grammar.first(sym), elements);
		}
		for (Symbol sym : followSets){
			stringb.append(String.format("Follow(%s)  = %s\n", sym, grammar.follow(sym)));
			elements.addAll(grammar.follow(sym));
		}
		for (Symbol sym : terminals){
			stringb.append(String.format("Adding {%s} \n", sym));
			elements.add(sym);
		}

		// TODO: check if it's correct
		if (hasEOF){
			stringb.append("{$}");
			elements.add(Symbol.createEOFSymbol());
		}

		stringb.append(String.format("All added elements: %s \n\n", elements));
		grammar.outputString.get("follow").add(stringb.toString());
		return elements;
	}
	

}
