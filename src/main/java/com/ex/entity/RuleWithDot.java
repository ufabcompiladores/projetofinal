package com.ex.entity;

import java.util.ArrayList;
import java.util.List;

import com.ex.entity.Symbol.SymbolType;

public final class RuleWithDot {
	private Symbol producer;
	private List<Symbol> symbolsBeforeDot;
	private List<Symbol> symbolsAfterDot;
	private int number;
	
	public int getNumber() {
		return number;
	}

	private RuleWithDot(){
		this.symbolsBeforeDot = new ArrayList<Symbol>();
		this.symbolsAfterDot = new ArrayList<Symbol>();
	}
	
	public RuleWithDot(Rule rule) {
		this();
		this.number = rule.getNumber();
		this.producer = rule.getProducer();
		this.symbolsAfterDot = rule.getProduction();
		this.symbolsBeforeDot.add(new Symbol(SymbolType.EMPTYSTRING, ""));
	}

	public RuleWithDot(Symbol producer, List<Symbol> symbolsBeforeDot, List<Symbol> symbolsAfterDot, int number) {
		super();
		this.producer = producer;
		this.symbolsBeforeDot = symbolsBeforeDot;
		this.symbolsAfterDot = symbolsAfterDot;
		this.number = number;
	}

	@Override
	public String toString() {
		return String.format("(%s) %s -> %s . %s", number, producer, symbolsBeforeDot, symbolsAfterDot);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + number;
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
		if (number != other.number)
			return false;
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

	public boolean hasNonEmptySymbolAfterDot() {
		return !symbolsAfterDot.get(0).isEmptyString();
	}
	
	public boolean hasNonEmptySymbolBeforeDot() {
		return !symbolsBeforeDot.get(0).isEmptyString();
	}

	public Symbol firstSymbolAfterDot() {
		return symbolsAfterDot.get(0);
	}
	
	public static RuleWithDot generateRuleWithShiftedDot(RuleWithDot ruleWithDot) {
		// we can't shift dot because there's nothing after it
		if (!ruleWithDot.hasNonEmptySymbolAfterDot()) {
			return ruleWithDot;
		}

		// new symbols before dot
		List<Symbol> newSymbolsBeforeDot = new ArrayList<Symbol>();
		if (ruleWithDot.hasNonEmptySymbolBeforeDot()) {
			newSymbolsBeforeDot.addAll(ruleWithDot.symbolsBeforeDot);
		}
		newSymbolsBeforeDot.add(ruleWithDot.firstSymbolAfterDot());
		
		// new symbols after dot
		List<Symbol> newSymbolsAfterDot = new ArrayList<Symbol>();
		if (ruleWithDot.symbolsAfterDot.size() == 1) {
			newSymbolsAfterDot.add(new Symbol(SymbolType.EMPTYSTRING, ""));
		}
		else {
			newSymbolsAfterDot.addAll(ruleWithDot.symbolsAfterDot);
			newSymbolsAfterDot.remove(0);
		}
		return new RuleWithDot(ruleWithDot.producer, newSymbolsBeforeDot, newSymbolsAfterDot, ruleWithDot.number);
	}

	public Symbol getProducer() {
		return this.producer;
	}
	
}
