package com.ex.entity;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.ex.entity.Symbol.SymbolType;

public final class Grammar {

	private Map<Symbol, Set<Rule>> rules;
	private Set<Symbol> terminals;
	private Set<Symbol> nonTerminals;
	private Map<Symbol, Set<Symbol>> firstSets;
	private Map<Symbol, Set<Symbol>> followSets;

	public Grammar(String inputGrammar) throws Exception {
		this.rules = new HashMap<Symbol, Set<Rule>>();
		this.terminals = new HashSet<Symbol>();
		this.nonTerminals = new HashSet<Symbol>();

		isValidGrammar(inputGrammar);
		addNonTerminals(inputGrammar);
		readAllRules(inputGrammar);


		this.firstSets = buildAllFirstSets();
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append("Grammar rules: \n");
		for (Symbol nonTerminal : nonTerminals) {
			for (Rule rule : rules.get(nonTerminal)) {
				string.append(String.format("%s \n", rule));
			}
		}
		return string.toString();
	}

	private void addRuleLine(String rulesText) throws Exception {
		String[] splitLine = rulesText.split("->");
		String producerText = splitLine[0].trim();
		Symbol producerSymbol = new Symbol(producerText);
		String[] rightSideTextProductions = splitLine[1].split("\\|");

		for (int i = 0; i < rightSideTextProductions.length; i++) {
			System.out.println("producer text: " + producerText);
			System.out.println("Right side text production" + rightSideTextProductions[i]);
			if (!rules.containsKey(producerSymbol)) {
				rules.put(producerSymbol, new HashSet<Rule>());
			}
			rules.get(producerSymbol).add(new Rule(producerText, rightSideTextProductions[i].trim()));
		}
	}

	private void readAllRules(String rulesText) throws Exception {
		String[] rules = rulesText.split("\n");
		for (String rule : rules) {
			System.out.println("Add rule: " + rule);
			addRuleLine(rule);
		}
	}

	private void isValidGrammar(String inputGrammar) throws Exception {

		Set<Symbol> nonTerminalsLHS = new HashSet<Symbol>();
		Set<Symbol> nonTerminalsRHS = new HashSet<Symbol>();

		String[] lines = inputGrammar.split("\n");

		for (String line : lines) {
			// Verifica se ha "->"
			String[] splitLine = line.split("->");
			if (splitLine.length < 2) {
				throw new Exception("Regra deveria ter ->: " + line);
			}

			// Verifica se parte esquerda eh nao terminal
			String LHS = splitLine[0].trim();
			if (!LHS.matches(Symbol.NONTERMINAL_REGEX)) {
				throw new Exception("Símbolo do lado esquerdo não é um não terminal: " + LHS);
			} else {
				nonTerminalsLHS.add(new Symbol(SymbolType.NONTERMINAL, LHS));
			}

			// Verifica se parte direita representa sequencias de simbolos
			// (possivelmente dividos por |)
			String RHS = splitLine[1];
			String[] productions = RHS.split("\\|");
			for (int i = 0; i < productions.length; i++) {
				String[] sequenceOfSymbols = productions[i].split("\\s");
				for (int j = 0; j < sequenceOfSymbols.length; j++) {
					String symbolText = sequenceOfSymbols[j].trim();
					if (!symbolText.matches(Symbol.EMPTY_STRING_REGEX) && !symbolText.matches(Symbol.TERMINAL_REGEX)
							&& !symbolText.matches(Symbol.NONTERMINAL_REGEX)) {
						throw new Exception("Símbolo não segue formato válido: " + symbolText);
					} else if (symbolText.matches(Symbol.NONTERMINAL_REGEX)) {
						nonTerminalsRHS.add(new Symbol(SymbolType.NONTERMINAL, symbolText));
					}
				}
			}
		}

		// if (!nonTerminalsLHS.equals(nonTerminalsRHS)) {
		// throw new Exception ("Quantidade de não-terminais à esquerda
		// diferente da quantidade à direita.");
		// }
	}

	private void addNonTerminals(String inputGrammar) throws Exception {
		String[] lines = inputGrammar.split("\n");
		for (String line : lines) {
			String[] splitLine = line.split("->");
			String LHS = splitLine[0].trim();
			this.nonTerminals.add(new Symbol(LHS));
		}
	}

	/**
	 * Deve retornar o first de uma única regra.
	 * Função vai ser usada para conferir se a gramática é LL.1
	 * @param rule
	 * @return Set<Symbol>
	 */
	public Set<Symbol> first(Rule rule){
		Set<Symbol> firstFromRule = new HashSet<Symbol>();
		if(rule.producesEmptyString()){
			firstFromRule.add(new Symbol(SymbolType.EMPTYSTRING, ""));
			return firstFromRule;
		}

		int indexSymbol = 0;
		Symbol sym = rule.getProduction().get(indexSymbol);

		if (sym.isTerminal()){
			firstFromRule.add(sym);
			return firstFromRule;
		}

		while(sym.isNonTerminal() && 
				this.first(sym).contains(new Symbol(SymbolType.EMPTYSTRING, "")) &&
				indexSymbol < rule.getProduction().size()){
			// todo: excluir eps
			firstFromRule.addAll(this.first(sym));
			indexSymbol++;
			sym = rule.getProduction().get(indexSymbol);
		}

		// se chegou ate o final, todos produzem ε, assim first produz ε
		if (indexSymbol == rule.getProduction().size() - 1){
			firstFromRule.add(new Symbol(SymbolType.EMPTYSTRING, ""));
			return firstFromRule;
		}

		if (sym.isTerminal()){
			firstFromRule.add(sym);
			return firstFromRule;
		}

		/*		se nao eh terminal
		 *      nem ε 
		 *      nem nao terminal que produz ε,
		so pode ser nao terminal que nao produz ε.
		 */
		firstFromRule.addAll(first(sym));
		return firstFromRule;
	}

	public Set<Symbol> getNonTerminals() {
		return nonTerminals;
	}

	public Set<Symbol> getTerminals() {
		return terminals;
	}

	public Map<Symbol, Set<Rule>> getRules() {
		return rules;
	}

	public Set<Symbol> first(Symbol sym){
		return this.firstSets.get(sym);
	}

	/**
	 * Computa o conjunto First de cada não terminal.
	 * Isto é feito da mesma maneira usual na literatura,
	 * em que se usa uma tabela que é atualizada até
	 * encontrar um ponto fixo.
	 * @return Um Map de Symbol para o first set desse Symbol.
	 */
	public Map<Symbol, Set<Symbol>> buildAllFirstSets(){
		// Initialize set.
		Map<Symbol, Set<Symbol>> firstSetsBeforeIteration = new HashMap<Symbol, Set<Symbol>>();
		for (Symbol nonTerminal: nonTerminals){
			firstSetsBeforeIteration.put(nonTerminal, new HashSet<Symbol>());
		}

		// Get union of sets that represent each first set.
		Map <Symbol, Set<Symbol>> setsWhoseUnionIsFirstSet = this.buildUnionOfSetsThatRepresetFirstSets();

		// Iterate until fixed point is found
		boolean someFirstSetHasChanged = true;
		while (someFirstSetHasChanged){
			System.out.println("\n New Iteration \n ------------");
			someFirstSetHasChanged = false;
			Map<Symbol, Set<Symbol>> newFirstSets = new HashMap<Symbol, Set<Symbol>>();

			for (Symbol nonTerminal: nonTerminals){
				Set<Symbol> newSet = new HashSet<Symbol>();
				newSet.addAll(firstSetsBeforeIteration.get(nonTerminal));
				newFirstSets.put(nonTerminal, newSet);
			}

			System.out.println("newFirstSets: " + newFirstSets);
			System.out.println("FirstSets: " + firstSetsBeforeIteration);

			for (Symbol nonTerminal: nonTerminals){
				System.out.println("---- \nUpdating set " + nonTerminal);
				System.out.format("Sets whose union is first(%s): %s\n", nonTerminal, setsWhoseUnionIsFirstSet.get(nonTerminal));
				int numElementsBefore = firstSetsBeforeIteration.get(nonTerminal).size();
				for (Symbol element : setsWhoseUnionIsFirstSet.get(nonTerminal)) {
					System.out.println("Element: " + element);
					if (element.isTerminal()){
						System.out.println("element is terminal: adding to set");
						newFirstSets.get(nonTerminal).add(element);
					} else if (element.isEmptyString()){
						System.out.println("element is empty string");
						newFirstSets.get(nonTerminal).add(new Symbol(SymbolType.EMPTYSTRING, ""));
					} else {
						System.out.println("element is non terminal");
						addAllElementsFromSetExceptEmptyString(firstSetsBeforeIteration.get(element), newFirstSets.get(nonTerminal));
						System.out.println("Adding elements: " + firstSetsBeforeIteration.get(element) + " (except eps)");
					}
				}
				int numElementsAfter = newFirstSets.get(nonTerminal).size();
				if (numElementsBefore != numElementsAfter){
					someFirstSetHasChanged = true;
				}
			}
			System.out.println("Old first sets: "+ firstSetsBeforeIteration);
			System.out.println("New first sets: "+ newFirstSets);
			firstSetsBeforeIteration = newFirstSets;
		}
		System.out.println("------");
		System.out.println("Final result: ");
		System.out.println(firstSetsBeforeIteration);
		return firstSetsBeforeIteration;
	}

	/**
	 *  Descrevemos cada conjunto First como a uniao de outros conjuntos First.
	 *  Por exemplo, se a regra eh A -> BCD | G | a,
	 *  e se B e C tem cadeia vazia em suas producoes, mas G nao, entao
	 *  First(A) eh {B, C, D, G, a}, em que cada elemento desse conjunto
	 *  eh o first desse symbol. Ou seja,
	 *  First(A) = First(B) ∪ First(C) ∪ First(D) ∪ First(G) ∪ First(a).
	 *  @return Um map de Symbol para o conjunto de Symbols cuja união é o First desse Symbol.
	 */
	private Map<Symbol, Set<Symbol>> buildUnionOfSetsThatRepresetFirstSets(){
		// Initialize set
		Map<Symbol, Set<Symbol>> setsWhoseUnionIsFirstSet = new HashMap<Symbol, Set<Symbol>>();
		for (Symbol nonTerminal: nonTerminals){
			setsWhoseUnionIsFirstSet.put(nonTerminal, new HashSet<Symbol>());
		}

		// Build set
		for (Symbol nonTerminal: nonTerminals){
			for (Rule rule : rules.get(nonTerminal)) {
				int i = 0;
				System.out.println(rule);
				System.out.println("i: " + i);
				System.out.println("rule:::: " + rule.getProduction().get(i));
				while(rule.getProduction().get(i).isNonTerminal() && 
						producesEps(rule.getProduction().get(i)) &&
						i < rule.getProduction().size() - 1){
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(rule.getProduction().get(i));
					i++;
				}
				// se while percorreu todos os simbolos da producao,
				// entao todos os nao terminais iterados produzem cadeia vazia
				if(i >= rule.getProduction().size()){
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(new Symbol(SymbolType.EMPTYSTRING, ""));
				}
				else{
					setsWhoseUnionIsFirstSet.get(nonTerminal).add(rule.getProduction().get(i));
				}
			}
		}
		System.out.println("Description of first rules: " + setsWhoseUnionIsFirstSet);
		return setsWhoseUnionIsFirstSet;
	}

	public void buildAllFollowSetDescriptions(){
		// Initialize set.
		Map<Symbol, Follow> followSetsDescriptions = new HashMap<Symbol, Follow>();
		for (Symbol nonTerminal: nonTerminals){
			Follow follow = buildFollowDescription(nonTerminal);
			followSetsDescriptions.put(nonTerminal, follow);
			//			System.out.println(followSetsDescriptions.get(nonTerminal));
			System.out.println(follow);
		}
	}

	public Follow buildFollowDescription(Symbol sym){
		Follow followSet = new Follow();

		System.out.format("Follow set for %s \n", sym);

		// TODO: add {$} to first symbol
		for (Symbol nonTerminal : nonTerminals){
			for (Rule rule : rules.get(nonTerminal)){
				int i = 0;
				while (i < rule.getProduction().size()) {
					// achou sym no RHS da producao
					if (rule.getProduction().get(i).equals(sym)) {
						int indexRightSideOfSym = i + 1;
						// se nao eh ultimo simbolo da producao
						if (indexRightSideOfSym < rule.getProduction().size()) {
							Symbol symbolOnRightSide = rule.getProduction().get(indexRightSideOfSym);
							// caso: simbolo seguinte eh nao terminal que gera ε
							while(indexRightSideOfSym < rule.getProduction().size() &&
									symbolOnRightSide.isNonTerminal() && 
									first(symbolOnRightSide).contains(new Symbol(SymbolType.EMPTYSTRING, ""))){
								followSet.getFirstSetsWithoutEps().add(symbolOnRightSide);
								indexRightSideOfSym++;
								symbolOnRightSide = rule.getProduction().get(indexRightSideOfSym);
							}
							// caso: eh o ultimo simbolo da producao
							if (indexRightSideOfSym == rule.getProduction().size()) {
								followSet.getFollowSets().add(rule.getProducer());
							}
							// caso: simbolo seguinte eh terminal
							if (symbolOnRightSide.isTerminal()) {
								followSet.getTerminals().add(symbolOnRightSide);
							}
							// caso: simbolo seguinte eh nao terminal nao que gera ε
							if (symbolOnRightSide.isNonTerminal()) {
								followSet.getFirstSets().add(symbolOnRightSide);
							}
						}
						// se eh ultimo simbolo da producao
						else {
							followSet.getFollowSets().add(rule.getProducer());
						}
					}
					i++;	
				}
			}
		}
		return followSet;
	}

	private boolean producesEps(Symbol symbol) {
		for (Rule rule : this.getRules().get(symbol)){
			if (rule.producesEmptyString()) {
				return true;	
			}
		}
		return false;
	}

	private void addAllElementsFromSetExceptEmptyString(Set<Symbol> setFrom, Set<Symbol> setTo){
		for (Symbol symbol : setFrom) {
			if (!symbol.isEmptyString()){
				setTo.add(symbol);
			}
		}
	}

	public Set<Rule> getRulesBySymbol(Symbol symbol) {
		return this.rules.get(symbol);
	}

	public Set<Symbol> follow(Rule r) {
		// TODO Auto-generated method stub
		return null;
	}

	public Map<Symbol, Set<Symbol>> getFollowSets() {
		return followSets;
	}

	public static void main(String[] args) throws Exception {
		Grammar g = new Grammar("A -> B e C B B B d \nB -> b | \n C -> C a | f");
		//		Grammar g = new Grammar("S -> c A a\nA -> c B | B\n B -> b c B | \n A -> A f");
		System.out.println(g.getNonTerminals());
		System.out.println(g);

		//		System.out.println("---------");
		//		System.out.println("First de cada regra: \n");
		//		for (Symbol nonTerminal : g.getNonTerminals()){
		//			for (Rule rule : g.getRules().get(nonTerminal)){
		//				System.out.println("Regra: " + rule);
		//				System.out.println("First" + g.first(rule) + "\n"); 
		//			}
		//		}
		//		System.out.println("---------");

		g.buildAllFollowSetDescriptions();

	}
	
}
