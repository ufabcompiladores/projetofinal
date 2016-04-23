package com.ex.entity;

import java.util.Set;

public final class Symbol {

	public static final String TERMINAL_REGEX = "^[a-z][A-Za-z0-9]*";
	public static final String NONTERMINAL_REGEX = "^[A-Z][A-Za-z0-9]*";
	public static final String EMPTY_STRING_REGEX = "";

	// TODO: remove this regex (got error on DefaultSymbols)
	public static final String EOF_STRING_REGEX = "$";

	public static final String EOF_LITERAL_REPRESENTATION = "$";
	public static final String EMPTY_STRING_LITERAL_REPRESENTATION = "ε";

	public enum SymbolType {
		NONTERMINAL, TERMINAL, EMPTYSTRING, EOF,
	}
	
	
	/**
	 * Default symbols
	 * @author giulio
	 *
	 */
	public enum DefaultSymbols {
		EMPTY(new Symbol(SymbolType.EMPTYSTRING, EMPTY_STRING_REGEX)),
		FINAL(new Symbol(SymbolType.EOF, EOF_STRING_REGEX));
		
		private final Symbol symbol;
		
		DefaultSymbols (Symbol symbol) {
			this.symbol = symbol;
		}
		
		public Symbol getSymbol() {
			return symbol;
		}
		
	}

	private SymbolType type;
	private String literalRepresentation;

	public Symbol(SymbolType type, String literalRepresentation) {
		super();
		this.type = type;
		this.literalRepresentation = literalRepresentation;
	}

	public Symbol(String literalRepresentation) throws Exception {
		super();
		this.literalRepresentation = literalRepresentation;
		this.type = getType(literalRepresentation);
	}

	@Override
	public String toString() {
		if (this.type == SymbolType.EMPTYSTRING) {
			return "ε";
		} else {
			return literalRepresentation;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((literalRepresentation == null) ? 0 : literalRepresentation.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Symbol other = (Symbol) obj;
		if (literalRepresentation == null) {
			if (other.literalRepresentation != null)
				return false;
		} else if (!literalRepresentation.equals(other.literalRepresentation))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	public static SymbolType getType(String representation) throws Exception {
		if (representation.matches(TERMINAL_REGEX)) {
			return SymbolType.TERMINAL;
		}
		// TODO: add special characters
		else if (representation.matches(NONTERMINAL_REGEX)) {
			return SymbolType.NONTERMINAL;
		} else if (representation.equals(EMPTY_STRING_REGEX)) {
			return SymbolType.EMPTYSTRING;
		} else
			throw new Exception("Not a valid format for a symbol: " + representation);
	}

	public boolean isTerminal() {
		return this.type == SymbolType.TERMINAL;
	}

	public boolean isNonTerminal() {
		return this.type == SymbolType.NONTERMINAL;
	}

	public boolean isEmptyString() {
		return this.type == SymbolType.EMPTYSTRING;
	}

	public boolean isLastSymbolOfProduction(Rule rule) {
		int lastIndex = rule.getProduction().size() - 1;
		return rule.getProduction().get(lastIndex).equals(this);
	}

	public static Symbol createEOFSymbol() {
		return new Symbol(SymbolType.EOF, EOF_LITERAL_REPRESENTATION);
	}
	
	// todo: create createEmptyStringSymbol

	public static Symbol newVersionOfGivenSymbol(Symbol sym, Set<Symbol> existingNonTerminalsInGrammar) throws Exception {
		String oldLiteralRepresentation = sym.literalRepresentation;
		String newLiteralRepresentation = "new" + oldLiteralRepresentation;

		for (Symbol nonTerminal : existingNonTerminalsInGrammar) {
			if (nonTerminal.literalRepresentation.equals(newLiteralRepresentation)) {
				throw new Exception(String.format("Tried to create new symbol with name %s, but it already exists", newLiteralRepresentation));
			}
		}

		return new Symbol(newLiteralRepresentation);
	}
}
