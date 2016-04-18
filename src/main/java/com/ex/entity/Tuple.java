package com.ex.entity;

public class Tuple {

	private Symbol leftSymbol;
	private Symbol upSymbol;
	
	public Tuple (Symbol l, Symbol u) {
		this.leftSymbol = l;
		this.upSymbol = u;
	}

	public Symbol getLeftSymbol() {
		return leftSymbol;
	}

	public void setLeftSymbol(Symbol leftSymbol) {
		this.leftSymbol = leftSymbol;
	}

	public Symbol getUpSymbol() {
		return upSymbol;
	}

	public void setUpSymbol(Symbol upSymbol) {
		this.upSymbol = upSymbol;
	}
	
	@Override
	public boolean equals(Object tuple) {
		Tuple t = (Tuple) tuple;
		
		return t.leftSymbol.equals(this.leftSymbol) && t.upSymbol.equals(this.upSymbol);
	}

}
