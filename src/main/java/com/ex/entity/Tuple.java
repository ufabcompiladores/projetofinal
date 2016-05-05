package com.ex.entity;

public final class Tuple {

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
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((leftSymbol == null) ? 0 : leftSymbol.hashCode());
		result = prime * result + ((upSymbol == null) ? 0 : upSymbol.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Tuple other = (Tuple) obj;
		if (leftSymbol == null) {
			if (other.leftSymbol != null)
				return false;
		} else if (!leftSymbol.equals(other.leftSymbol))
			return false;
		if (upSymbol == null) {
			if (other.upSymbol != null)
				return false;
		} else if (!upSymbol.equals(other.upSymbol))
			return false;
		return true;
	}
	
	@Override
	public String toString() {
		return  String.format("Tupla: [%s, %s]", this.leftSymbol, this.upSymbol);
	}

	
}
