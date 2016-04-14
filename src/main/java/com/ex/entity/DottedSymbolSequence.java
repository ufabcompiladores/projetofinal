package com.ex.entity;

import java.util.Set;

public final class DottedSymbolSequence {
	private Set<Symbol> symbolsBeforeDot;
	private Set<Symbol> symbolsAfterDot;

	public void addSymbolBeforeDot(Symbol symbol) {
		this.symbolsBeforeDot.add(symbol);
	}
	
	public void addSymbolAfterDot(Symbol symbol) {
		this.symbolsAfterDot.add(symbol);
	}

	public Set<Symbol> getSymbolsBeforeDot() {
		return symbolsBeforeDot;
	}

	public void setSymbolsBeforeDot(Set<Symbol> symbolsBeforeDot) {
		this.symbolsBeforeDot = symbolsBeforeDot;
	}

	public Set<Symbol> getSymbolsAfterDot() {
		return symbolsAfterDot;
	}

	public void setSymbolsAfterDot(Set<Symbol> symbolsAfterDot) {
		this.symbolsAfterDot = symbolsAfterDot;
	}

}
