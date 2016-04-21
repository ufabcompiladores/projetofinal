package com.ex.entity;
import java.util.HashSet;
import java.util.Set;

public class Follow {
	private Set<Symbol> firstSets;
	private Set<Symbol> firstSetsWithoutEps;
	private Set<Symbol> followSets;
	private Set<Symbol> terminals;
	private boolean hasEOF;
	private Set<Symbol> elements;



	public Follow() {
		super();
		this.firstSets = new HashSet<Symbol>();
		this.firstSetsWithoutEps = new HashSet<Symbol>();
		this.followSets = new HashSet<Symbol>();
		this.terminals = new HashSet<Symbol>();
		this.elements = new HashSet<Symbol>();
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
		for (Symbol sym : elements){
			string.append(String.format(" %s ", sym));
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


	public Set<Symbol> getElements() {
		return elements;
	}
	
	

}
