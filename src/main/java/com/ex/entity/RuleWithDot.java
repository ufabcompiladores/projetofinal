package com.ex.entity;

import java.util.ArrayList;
import java.util.List;

public final class RuleWithDot {
	private Symbol producer;
	private List<Symbol> symbolsBeforeDot;
	private List<Symbol> symbolsAfterDot;
	
	public RuleWithDot(){
		this.symbolsBeforeDot = new ArrayList<Symbol>();
		this.symbolsAfterDot = new ArrayList<Symbol>();
	}
	
	public RuleWithDot(Rule rule) {
		this();
		this.producer = rule.getProducer();
		this.symbolsAfterDot = rule.getProduction();
	}

	@Override
	public String toString() {
		return String.format("%s -> %s . %s", producer, symbolsBeforeDot, symbolsAfterDot);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((producer == null) ? 0 : producer.hashCode());
		result = prime * result + ((symbolsAfterDot == null) ? 0 : symbolsAfterDot.hashCode());
		result = prime * result + ((symbolsBeforeDot == null) ? 0 : symbolsBeforeDot.hashCode());
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
		RuleWithDot other = (RuleWithDot) obj;
		if (producer == null) {
			if (other.producer != null)
				return false;
		} else if (!producer.equals(other.producer))
			return false;
		if (symbolsAfterDot == null) {
			if (other.symbolsAfterDot != null)
				return false;
		} else if (!symbolsAfterDot.equals(other.symbolsAfterDot))
			return false;
		if (symbolsBeforeDot == null) {
			if (other.symbolsBeforeDot != null)
				return false;
		} else if (!symbolsBeforeDot.equals(other.symbolsBeforeDot))
			return false;
		return true;
	}

	
}
