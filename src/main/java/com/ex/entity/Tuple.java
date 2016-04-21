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
	
	@Override
	public String toString() {
		return  String.format("Tupla: [%s, %s]", this.leftSymbol, this.upSymbol);
	}

	public static void main(String[] args) {
		System.out.println(new Tuple(Symbol.DefaultSymbols.EMPTY.getSymbol(), Symbol.DefaultSymbols.FINAL.getSymbol()));
	}
}
