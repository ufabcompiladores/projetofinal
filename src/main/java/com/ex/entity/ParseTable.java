package com.ex.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ParseTable {
	
	/**
	 * Essa estrutura de dado significa o seguinte:
	 * É o mapeamento de um produtor com a lista de terminais que existem na tabela e suas respectivas regras
	 * para aquele produtor.
	 */
	private Map<Symbol, Map<Symbol, Set<Rule>>> table;

	public ParseTable(Set<Symbol> terminals, Set<Symbol> producers) {
		terminals.remove(Symbol.DefaultSymbols.EMPTY.getSymbol());
		terminals.add(Symbol.DefaultSymbols.FINAL.getSymbol());
		
		this.table = new HashMap<Symbol, Map<Symbol, Set<Rule>>>();
		
		for (Symbol pro : producers) {
			table.put(pro, new HashMap<Symbol, Set<Rule>>(){
				private static final long serialVersionUID = 1L;
			{
				for (Symbol ter : terminals) {
					put(ter, new HashSet<Rule>());
				}
			}});
		}
	}
	
	/**
	 * Confere se existem regras duplicadas para um mesmo produtor e terminal.
	 * @return
	 */
	public boolean isAmbiguous () {
		for (Symbol s : table.keySet()) {
			if (table.get(s).values().stream().anyMatch(p -> p.size() > 1)) return true;
		}
		
		return false;
	}
	
	public void addRule(Tuple t, Rule r) {
		if (!t.getUpSymbol().equals(Symbol.DefaultSymbols.EMPTY.getSymbol()))
		table.get(t.getLeftSymbol()).get(t.getUpSymbol()).add(r);
	}

	public Map<Symbol, Map<Symbol, Set<Rule>>> getTable() {
		return table;
	}
	
	/**
	 * Printa formatada a tabela
	 */
//	@Override
//	public String toString() {
//		//Olha largura da coluna
//		Integer maxLength = 0;
//		
//		for (Set<Rule> sr : table.values()) {
//			for (Rule r : sr) {
//				for (String s : r.toString().split("->")[1].split("\\|")) {
//					if (s.length() + r.getProducer().toString().length() + 3 > maxLength) {
//						maxLength = s.length() + r.getProducer().toString().length() + 3;
//					}
//				}
//			}
//		}
//		
//		return generateSide(maxLength, terminals.size()+1, "=") + "\n"
//				+ generateTerminalsLine(maxLength, terminals) + "\n"
//				+ generateSide(maxLength, terminals.size()+1, "-") + "\n"
//				+ generateValuesLine(maxLength, table) + "\n"
//				+ generateSide(maxLength, terminals.size()+1, "=");
//	}
//	
//	private String generateValuesLine(Integer maxLength, Map<Tuple, Set<Rule>> table) {
//		String result = "";
//		
//		for (Tuple t : table.keySet()) {
//			result += "|" + normalizeSymbol(maxLength, t.getLeftSymbol()) + "|";
//			
//			for (Rule rule : table.get(t)) {
//				result += "|" + normalizeString(maxLength, rule.toString()) + "|";
//			}
//			
//			result += "\n";
//		}
//		
//		return result.substring(0, result.length()-1);
//	}
//
//	private String normalizeString(Integer maxLength, String centralSymbol) {
//		if (centralSymbol.length() <= maxLength) {
//			while (centralSymbol.length() <= maxLength) {
//				centralSymbol = " " + centralSymbol + " ";
//			}
//			if (centralSymbol.length() == maxLength + 1) {
//				centralSymbol += " ";
//			}
//		}
//		return centralSymbol;
//	}
//
//	private static String generateSide (Integer length, Integer repeat, String type) {
//		String result = "";
//		
//		for (int i = 0; i < repeat; i++) {
//			String side = "|";
//			for (int j = 0; j < length; j++) {
//				side += type;
//			}
//			side += type + type + "|";
//			
//			result += side;
//		}
//		
//		return result;
//	}
//	
//	private static String generateTerminalsLine (Integer length, Set<Symbol> symbols) {
//		String result = generateSide(length, 1, " ");
//		
//		for (Symbol s : symbols) {
//			result += "|" + normalizeSymbol(length, s) + "|";
//		}
//		
//		return result;
//	}
//
//	private static String normalizeSymbol(Integer length, Symbol s) {
//		String centralSymbol = s.toString();
//		while (centralSymbol.length() <= length) {
//			centralSymbol = " " + centralSymbol + " ";
//		}
//		if (centralSymbol.length() == length + 1) {
//			centralSymbol += " ";
//		}
//		return centralSymbol;
//	}
//	/*
//	 * Fim da formatação da tabela
//	 */
//
//	public static void main(String[] args) {
//		HashSet<Tuple> tuples = new HashSet<Tuple>() {private static final long serialVersionUID = 1L; {
//			add(new Tuple(Symbol.DefaultSymbols.EMPTY.getSymbol(), Symbol.DefaultSymbols.FINAL.getSymbol()));
//			add(new Tuple(Symbol.DefaultSymbols.FINAL.getSymbol(), Symbol.DefaultSymbols.EMPTY.getSymbol()));
//		}};
//		
//		HashSet<Symbol> terminals = new HashSet<Symbol>() {private static final long serialVersionUID = 1L; {
//			add(Symbol.DefaultSymbols.EMPTY.getSymbol());
//			add(Symbol.DefaultSymbols.FINAL.getSymbol());
//		}};
//		
//		ParseTable parseTable = new ParseTable(tuples, terminals);
//		
//		parseTable.table.put(new Tuple(Symbol.DefaultSymbols.EMPTY.getSymbol(), Symbol.DefaultSymbols.FINAL.getSymbol()),
//				new HashSet<Rule>(){private static final long serialVersionUID = 1L; {add(Rule.ERROR);}});
//		
//		System.out.println(parseTable);
//	}	
}
