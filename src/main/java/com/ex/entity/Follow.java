package com.ex.entity;
import java.util.HashSet;
import java.util.Set;

import com.ex.entity.Symbol.SymbolType;

public class Follow {
	private Set<Symbol> firstSets;
	private Set<Symbol> firstSetsWithoutEps;
	private Set<Symbol> followSets;
	private Set<Symbol> terminals;
	private boolean hasEOF;
	private Set<Symbol> elements;


	public void buildFollowDescriptions(Symbol sym, Grammar grammar){
		// TODO: add {$} to first symbol
		for (Symbol nonTerminal : grammar.getNonTerminals()){
			for (Rule rule : grammar.getRules().get(nonTerminal)){
				int i = 0;
				while (i < rule.getProduction().size()) {
					// achou sym no RHS da producao
					if (rule.getProduction().get(i).equals(sym)) {
						int indexRightSideOfSym = i;
						Symbol symbolOnRightSide = rule.getProduction().get(indexRightSideOfSym);
						// caso: simbolo seguinte eh nao terminal que gera ε
						while(symbolOnRightSide.isNonTerminal() && 
								grammar.firstSetFromSymbol(sym).contains(new Symbol(SymbolType.EMPTYSTRING, "")) &&
								indexRightSideOfSym < rule.getProduction().size()){
							firstSetsWithoutEps.add(symbolOnRightSide);
							indexRightSideOfSym++;
							symbolOnRightSide = rule.getProduction().get(indexRightSideOfSym);
						}
						// caso: eh o ultimo simbolo da producao
						if (indexRightSideOfSym == rule.getProduction().size() - 1) {
							followSets.add(rule.getProducer());
						}
						// caso: simbolo seguinte eh terminal
						if (symbolOnRightSide.isTerminal()) {
							terminals.add(symbolOnRightSide);
						}
						// caso: simbolo seguinte eh nao terminal nao que gera ε
						if (symbolOnRightSide.isNonTerminal()) {
							firstSets.add(symbolOnRightSide);
						}
					}
					i++;	
				}
			}
		}
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
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
		string.append(" = {");
		for (Symbol sym : elements){
			string.append(String.format(" %s ", sym));
		}
		return string.toString();
	}

}
