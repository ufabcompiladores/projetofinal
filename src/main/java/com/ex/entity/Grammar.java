package com.ex.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.ex.entity.Symbol.SymbolType;

/**
 * Represents a grammar.
 * @author andre0991
 *
 */
public final class Grammar {

	/**
	 * Conjunto de regras da gramática. O símbolo é o produtor daquele conjunto de regras.
	 */
	private Map<Symbol, Set<Rule>> rules;
	private Set<Symbol> terminals;
	private Set<Symbol> nonTerminals;
	private Symbol startSymbol;
	private Map<Symbol, Set<Symbol>> firstSets;
	private Map<Symbol, Set<Symbol>> followSets;
	private Map<Symbol, Boolean> nonTerminalsToProducesEps;
	private int numberOfRules;
	public Map<String,List<String>> outputString;


	public Grammar(String inputGrammar) throws Exception {
		initialiseOutputMap();

		this.numberOfRules = 0;
		this.rules = new HashMap<Symbol, Set<Rule>>();
		this.terminals = new HashSet<Symbol>();
		this.nonTerminals = new HashSet<Symbol>();

		isValidGrammar(inputGrammar);

		this.startSymbol = addStartSymbol(inputGrammar);
		addNonTerminals(inputGrammar);
		addTerminals(inputGrammar);
		readAllRules(inputGrammar);
		buildAllNonTerminalsThatProduceEps();
		buildAllFirstSets();
		buildAllFollowSets();
		printOutput();
	}

	public Grammar(Map<Symbol, Set<Rule>> rules, Set<Symbol> terminals, Set<Symbol> nonTerminals, Symbol startSymbol,
			int numberOfRules) {
		super();
		initialiseOutputMap();
		this.rules = rules;
		this.terminals = terminals;
		this.nonTerminals = nonTerminals;
		this.startSymbol = startSymbol;
		this.numberOfRules = numberOfRules;


		buildAllNonTerminalsThatProduceEps();

		buildAllFirstSets();
		buildAllFollowSets();
	}



	// TODO: continuar
	private final void initialiseOutputMap() {
		outputString = new HashMap<String, List<String>>();
		outputString.put("readingGrammar", new ArrayList<String>());
		outputString.put("first", new ArrayList<String>());
		outputString.put("followSetDescriptions", new ArrayList<String>());
		outputString.put("firstSetDescriptions", new ArrayList<String>());
		outputString.put("follow", new ArrayList<String>());
	}


	public final void printOutput() {
		System.out.println("READING GRAMMAR");
		System.out.println(outputString.get("readingGrammar").stream().reduce("" , (a, b) -> a + b));

		System.out.println("BUILDING FIRST SETS DESCRIPTIONS");
		System.out.println(outputString.get("firstSetDescriptions").stream().reduce("" , (a, b) -> a + b));

		System.out.println("BUILDING FIRST SETS");
		System.out.println(outputString.get("first").stream().reduce("" , (a, b) -> a + b));

		System.out.println("BUILDING FOLLOW SETS DESCRIPTIONS");
		System.out.println(outputString.get("followSetDescriptions").stream().reduce("" , (a, b) -> a + b));

		System.out.println("BUILDING FOLLOW SETS");
		System.out.println(outputString.get("follow").stream().reduce("" , (a, b) -> a + b));
	}



	public Symbol getStartSymbol() {
		return startSymbol;
	}

	/**
	 * Returns a grammar that is identical to this, except that is has a new start symbol
	 * that produces the old start symbol.
	 * For example, if the old grammar is "S -> A,  A -> B", then the new grammar is
	 * "newS -> S, S -> A, A -> B"
	 * @return
	 * @throws Exception
	 */
	public Grammar grammarWithExtraStartSymbol() throws Exception {
		// create new start symbol
		Symbol oldStartSymbol = this.startSymbol;
		Symbol newStartSymbol = Symbol.newVersionOfGivenSymbol(oldStartSymbol, nonTerminals);

		// new number of rules
		int newNumberOfRules = this.numberOfRules + 1;

		// new nonterminals - same as before, but has new start symbol
		Set<Symbol> newNonTerminals = new HashSet<Symbol>();
		newNonTerminals.addAll(nonTerminals);
		newNonTerminals.add(newStartSymbol);

		// new rules
		List<Symbol> newStartRuleProduction = new ArrayList<Symbol>();
		newStartRuleProduction.add(oldStartSymbol);
		Rule newStartRule = new Rule(newStartSymbol, newStartRuleProduction, 0);
		Map<Symbol, Set<Rule>> newRules = new HashMap<Symbol, Set<Rule>>();
		Set<Rule> setOfRulesForFirstRule = new HashSet<Rule>();
		setOfRulesForFirstRule.add(newStartRule);
		newRules.put(newStartSymbol, setOfRulesForFirstRule);
		for (Symbol nonTerminal : nonTerminals) {
			newRules.put(nonTerminal, new HashSet<Rule>());
		}
		for (Symbol nonTerminal : nonTerminals) {
			for (Rule oldRule : rules.get(nonTerminal)) {
				/*each new rule is the same as before, 
				  except that its number increases by one */				
				Rule newRule = new Rule(oldRule.getProducer(),
						oldRule.getProduction(),
						oldRule.getNumber() + 1);
				newRules.get(nonTerminal).add(newRule);
			}
		}


		// new terminals - same elements as before
		Set<Symbol> newTerminals = new HashSet<Symbol>();
		newTerminals.addAll(terminals);

		Grammar newGrammarWithExtraStartSymbol = new Grammar(newRules, newTerminals, newNonTerminals, newStartSymbol, newNumberOfRules);
		System.out.println(newGrammarWithExtraStartSymbol);

		return newGrammarWithExtraStartSymbol;
	}

	private Symbol addStartSymbol(String inputGrammar) throws Exception {
		String[] lines = inputGrammar.split("\n");
		String line = lines[0];
		String[] splitLine = line.split("->");
		String LHS = splitLine[0].trim();
		return new Symbol(LHS);
	}

	private void addTerminals(String inputGrammar) throws Exception {
		String[] lines = inputGrammar.split("\n");
		for (String line : lines) {
			String[] splitLine = line.split("->");
			String RHS = splitLine[1];
			String[] rules = RHS.split("\\|");

			for (String rule : rules) {
				String[] symbols = rule.split("\\s");

				for (String symbol : symbols) {
					if (symbol.trim().matches(Symbol.TERMINAL_REGEX)) {
						this.terminals.add(new Symbol(symbol.trim()));
					}
				}
			}


		}
	}

	@Override
	public String toString() {
		StringBuilder string = new StringBuilder();
		string.append(String.format("\nStart Symbol: %s \n", startSymbol));
		string.append("Grammar rules: \n");
		for (Symbol nonTerminal : nonTerminals) {
			for (Rule rule : rules.get(nonTerminal)) {
				string.append(String.format("%s \n", rule));
			}
		}
		return string.toString();
	}

	private void addRuleLine(String rulesText) throws Exception {
		List<String> outReadRules = this.outputString.get("readingGrammar");
		String[] splitLine = rulesText.split("->");
		String producerText = splitLine[0].trim();
		Symbol producerSymbol = new Symbol(producerText);
		String[] rightSideTextProductions = splitLine[1].split("\\|");

		for (int i = 0; i < rightSideTextProductions.length; i++) {
			StringBuilder out = new StringBuilder();
			out.append(String.format("Producer text: %s\n", producerText));
			out.append(String.format("Right side text production: %s\n\n", rightSideTextProductions[i]));
			if (!rules.containsKey(producerSymbol)) {
				rules.put(producerSymbol, new HashSet<Rule>());
			}
			rules.get(producerSymbol).add(new Rule(producerText, rightSideTextProductions[i].trim(), this.numberOfRules));
			numberOfRules++;
			outReadRules.add(out.toString());
		}
	}

	private void readAllRules(String rulesText) throws Exception {
		List<String> outReadRules = this.outputString.get("readingGrammar");
		String[] rules = rulesText.split("\n");
		for (String rule : rules) {
			outReadRules.add(String.format("Add rule: %s\n", rule));
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

		// FIXME
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
	 * Retorna conjunto first da produção da regra dada.
	 * @param rule Regra.
	 * @return Conjunto First(X), em que X é a produção da regra dada.
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
	 * Computa o conjunto First de cada símbolo.
	 * Isto é feito da mesma maneira usual na literatura,
	 * em que se usa uma tabela que é atualizada até
	 * encontrar um ponto fixo.
	 * @return Um Map de Symbol para o first set desse Symbol.
	 */
	public final void buildAllFirstSets() {
		List<String> out = this.outputString.get("first");

		// Initialize set
		Map<Symbol, Set<Symbol>> firstSetsBeforeIteration = new HashMap<Symbol, Set<Symbol>>();
		for (Symbol nonTerminal: nonTerminals){
			firstSetsBeforeIteration.put(nonTerminal, new HashSet<Symbol>());
		}
		for (Symbol terminal: terminals){
			firstSetsBeforeIteration.put(terminal, new HashSet<Symbol>());
		}

		// Get description of each first set
		Map<Symbol, First> firstSetDescriptions = buildAllFirstSetDescriptions();

		// Iterate until fixed point is found
		boolean someFirstSetHasChanged = true;
		while (someFirstSetHasChanged) {
			StringBuilder iterationSb = new StringBuilder();
			iterationSb.append("New iteration (building first sets)\n");
			someFirstSetHasChanged = false;

			// Copy elements from old first sets to new first sets
			Map<Symbol, Set<Symbol>> firstSetsAfterIteration = new HashMap<Symbol, Set<Symbol>>();
			for (Symbol nonTerminal: nonTerminals){
				firstSetsAfterIteration.put(nonTerminal, new HashSet<Symbol>());
				firstSetsAfterIteration.get(nonTerminal).addAll(firstSetsBeforeIteration.get(nonTerminal));
			}
			for (Symbol terminal: terminals){
				firstSetsAfterIteration.put(terminal, new HashSet<Symbol>());
				firstSetsAfterIteration.get(terminal).addAll(firstSetsBeforeIteration.get(terminal));
			}

			// Updates, possibly getting new elements
			for (Symbol nonTerminal: nonTerminals){
				iterationSb.append(String.format("Updating First(%s)\n", nonTerminal));
				First firstDescription = firstSetDescriptions.get(nonTerminal);
				iterationSb.append(String.format("First(%s) = %s\n", nonTerminal, firstDescription));
				int numElementsBefore = firstSetsBeforeIteration.get(nonTerminal).size();
				firstSetsAfterIteration.get(nonTerminal).addAll(firstDescription.getAllElements(firstSetsBeforeIteration));
				iterationSb.append(String.format("Adding elements: %s\n", firstDescription.getAllElements(firstSetsBeforeIteration)));
				int numElementsAfter = firstSetsAfterIteration.get(nonTerminal).size();
				if (numElementsBefore != numElementsAfter){
					someFirstSetHasChanged = true;
				}
			}

			iterationSb.append(String.format("All elements form first sets before iteration: %s\n", firstSetsBeforeIteration));
			iterationSb.append(String.format("All elements form first sets after iteration: %s\n\n", firstSetsAfterIteration));
			out.add(iterationSb.toString());

			firstSetsBeforeIteration = firstSetsAfterIteration;
		}
		out.add(String.format("Final result: %s\n\n", firstSetsBeforeIteration));
		this.firstSets = firstSetsBeforeIteration;
	}


	public Map<Symbol, First> buildAllFirstSetDescriptions(){
		// Initialize set.
		Map<Symbol, First> firstSetsDescriptions = new HashMap<Symbol, First>();
		for (Symbol nonTerminal: nonTerminals){
			First first = buildFirstDescription(nonTerminal);
			firstSetsDescriptions.put(nonTerminal, first);
		}
		for (Symbol terminal: terminals){
			First first = buildFirstDescription(terminal);
			firstSetsDescriptions.put(terminal, first);
		}
		return firstSetsDescriptions;
	}

	private First buildFirstDescription(Symbol sym) {
		Set<Symbol> firstSets = new HashSet<Symbol>();
		Set<Symbol> firstSetsWithoutEps = new HashSet<Symbol>();
		boolean hasEps = false;

		List<String> out = this.outputString.get("firstSetDescriptions");
		out.add(String.format("Building set description for %s:\n", sym));

		if (sym.isTerminal()) {
			firstSets.add(sym);
			out.add(String.format("Symbol %s is terminal - adding {%s} to First(%s)\n", sym, sym, sym));
			return new First(firstSets, firstSetsWithoutEps, hasEps);
		}


		for (Rule rule : rules.get(sym)){
			out.add(String.format("Analysing rule %s\n", rule));
			List<Symbol> production = rule.getProduction();
			int i = 0;
			Symbol currentSymbol = production.get(i);

			// if it produces eps directly
			if (currentSymbol.isEmptyString()) {
				out.add(String.format("Rule produces eps directly. Adding {ε} to First(%s)\n", sym));
				hasEps = true;
				continue;
			}

			// add all consecutive nonTerminals that produce eps
			while (i < production.size() &&	
					producesEps(currentSymbol)) {
				out.add(String.format("Symbol %s produces ε. Adding First(%s) - ε to First(%s).\n", currentSymbol, currentSymbol, sym));
				firstSetsWithoutEps.add(currentSymbol);
				i++;
				if (i < rule.getProduction().size()) {
					currentSymbol = production.get(i);
				}
			}

			// if did not reach end of production
			if (i < rule.getProduction().size()) {
				out.add(String.format("Adding First(%s) to Firsts(%s)\n", currentSymbol, sym));
				firstSets.add(currentSymbol);
			}
			else {
				out.add(String.format("Reached end of production - adding ε to First(%s)\n", sym));
				hasEps = true;
			}

		}
		First firstSet = new First(firstSets, firstSetsWithoutEps, hasEps);
		out.add(String.format("Final description: First(%s) = %s\n\n", sym, firstSet));
		return firstSet;
	}

	private Map<Symbol, Follow> buildAllFollowSetDescriptions(){
		// Initialize set.
		Map<Symbol, Follow> followSetsDescriptions = new HashMap<Symbol, Follow>();
		for (Symbol nonTerminal: nonTerminals){
			Follow follow = buildFollowDescription(nonTerminal);
			followSetsDescriptions.put(nonTerminal, follow);
		}
		return followSetsDescriptions;
	}

	private Follow buildFollowDescription(Symbol sym){
		Follow followSet = new Follow(sym.equals(startSymbol));
		this.outputString.get("followSetDescriptions").add(String.format("Building Follow set description for %s:\n", sym));

		for (Symbol nonTerminal : nonTerminals){
			for (Rule rule : rules.get(nonTerminal)){
				int i = 0;
				while (i < rule.getProduction().size()) {
					// achou sym no RHS da producao
					if (rule.getProduction().get(i).equals(sym)) {
						StringBuilder followDescSb = new StringBuilder();
						followDescSb.append(String.format("\nFound symbol %s in rule %s at position %s:\n", sym, rule, i));
						int indexRightSideOfSym = i + 1;
						// se simbolo encontrado nao eh ultimo simbolo da producao
						if (indexRightSideOfSym < rule.getProduction().size()) {
							Symbol symbolOnRightSide = rule.getProduction().get(indexRightSideOfSym);
							followDescSb.append(String.format("Symbol on right side: %s\n", symbolOnRightSide));
							// caso: simbolo seguinte eh nao terminal que gera ε
							while(indexRightSideOfSym < rule.getProduction().size() &&
									symbolOnRightSide.isNonTerminal() && 
									first(symbolOnRightSide).contains(new Symbol(SymbolType.EMPTYSTRING, ""))) {
								followDescSb.append(String.format("Symbol %s generates ε - "
										+ "we add First(%s) - {ε} to the description of Follow(%s) \n", 
										symbolOnRightSide, symbolOnRightSide, sym));
								followSet.getFirstSetsWithoutEps().add(symbolOnRightSide);
								// FIXME: issue when A -> BC, B and C produce eps
								indexRightSideOfSym++;
								if (indexRightSideOfSym < rule.getProduction().size()) {
									symbolOnRightSide = rule.getProduction().get(indexRightSideOfSym);
								}
							}
							// caso: ja passou por toda producao
							if (indexRightSideOfSym == rule.getProduction().size()) {
								followDescSb.append(String.format("Reached end of production - "
										+ "adding Follow(%s) to Follow(%s)\n",
										rule.getProducer(), sym));
								followSet.getFollowSets().add(rule.getProducer());
							}
							else {
								// caso: simbolo seguinte eh terminal
								if (symbolOnRightSide.isTerminal()) {
									followDescSb.append(String.format("Next symbol %s is terminal - "
											+ "adding {%s} to Follow(%s):\n", 
											symbolOnRightSide, symbolOnRightSide, sym));
									followSet.getTerminals().add(symbolOnRightSide);
								}
								// caso: simbolo seguinte eh nao terminal nao que gera ε
								if (symbolOnRightSide.isNonTerminal()) {
									followDescSb.append(String.format("Next symbol %s doesn't generate ε:"
											+ " Adding First(%s) to Follow(%s) description\n",
											symbolOnRightSide, symbolOnRightSide, sym));
									followSet.getFirstSets().add(symbolOnRightSide);
								}
							}
						}
						// se eh ultimo simbolo da producao
						else {
							followDescSb.append(String.format("Symbol is the last from this production - adding Follow(%s) to Follow(%s)\n", rule.getProducer(), sym));
							followSet.getFollowSets().add(rule.getProducer());
						}
						followDescSb.append(String.format("Result:%s \n", followSet));
						this.outputString.get("followSetDescriptions").add(followDescSb.toString());
					}
					i++;	
				}
			}
		}
		return followSet;
	}

	/**
	 * Computa o conjunto Follow de cada não terminal.
	 * Isto é feito da mesma maneira usual na literatura,
	 * em que se usa uma tabela que é atualizada até
	 * encontrar um ponto fixo.
	 */
	private final void buildAllFollowSets(){
		List<String> out = this.outputString.get("follow");
		// TODO: make this function more similar to buildAllFirstSets (minimize side effects).

		// Initialize field
		Map<Symbol, Set<Symbol>> followSetsField = new HashMap<Symbol, Set<Symbol>>();
		for (Symbol nonTerminal: nonTerminals){
			followSetsField.put(nonTerminal, new HashSet<Symbol>());
		}
		this.followSets = followSetsField;

		// Get description of each follow set
		Map<Symbol, Follow> followSetDescriptions = buildAllFollowSetDescriptions();

		// Iterate until fixed point is found
		boolean someFollowSetHasChanged = true;
		while (someFollowSetHasChanged){
			out.add("New iteration (building all follow sets)\n");
			someFollowSetHasChanged = false;
			Map<Symbol, Set<Symbol>> newFollowSets = new HashMap<Symbol, Set<Symbol>>();

			// copy elements old follow sets to new follow sets
			for (Symbol nonTerminal: nonTerminals){
				Set<Symbol> newSet = new HashSet<Symbol>();
				newSet.addAll(this.follow(nonTerminal));
				newFollowSets.put(nonTerminal, newSet);
			}

			for (Symbol nonTerminal: nonTerminals){
				out.add(String.format("Updating Follow(%s)\n", nonTerminal));
				Follow followDescription = followSetDescriptions.get(nonTerminal);
				out.add(String.format("Follow(%s) = %s\n", nonTerminal, followDescription));
				int numElementsBefore = this.follow(nonTerminal).size();
				newFollowSets.get(nonTerminal).addAll(followDescription.getAllElements(this));
				int numElementsAfter = newFollowSets.get(nonTerminal).size();
				if (numElementsBefore != numElementsAfter){
					someFollowSetHasChanged = true;
				}
			}

			out.add(String.format("All elements form follow sets before iteration: %s\n", this.followSets));
			out.add(String.format("All elements form follow sets after iteration: %s\n\n", newFollowSets));
			this.followSets = newFollowSets;
		}
		out.add(String.format("Final result: %s\n\n", this.followSets));
	}

	private boolean producesEps(Symbol symbol) {
		return this.nonTerminalsToProducesEps.get(symbol);
	}

	private final void buildAllNonTerminalsThatProduceEps() {
		Set<Symbol> nonTerminalsThatGenerateEps = new HashSet<Symbol>();

		// rules that directly generate eps
		for (Symbol nonTerminal : nonTerminals) {
			for (Rule rule : rules.get(nonTerminal)) {
				if (rule.producesEmptyString()) {
					nonTerminalsThatGenerateEps.add(nonTerminal);
				}
			}
		}

		// iterates until fp is found
		boolean newNonTerminalThatGeneratesEpsHasBeenFound = true;
		while (newNonTerminalThatGeneratesEpsHasBeenFound) {
			newNonTerminalThatGeneratesEpsHasBeenFound = false;
			int setSizeBeforeIteration = nonTerminalsThatGenerateEps.size();

			for (Symbol nonTerminal : nonTerminals) {
				for (Rule rule : rules.get(nonTerminal)) {
					// verifies if all symbols from rule produce eps
					List<Symbol> production = rule.getProduction();
					boolean allSymbolsFromProductionProduceEps;
					allSymbolsFromProductionProduceEps = production
							.stream()
							.allMatch(symbol -> nonTerminalsThatGenerateEps.contains(symbol));

					// if so, add it to set
					if (allSymbolsFromProductionProduceEps) {
						nonTerminalsThatGenerateEps.add(nonTerminal);
					}
				}
			}

			// verifies whether some non terminal has been added to set
			int setSizeAfterIteration = nonTerminalsThatGenerateEps.size();
			if (setSizeBeforeIteration != setSizeAfterIteration) {
				newNonTerminalThatGeneratesEpsHasBeenFound = true;
			}
		}

		// initialise Map
		Map<Symbol, Boolean> producesEps = new HashMap<Symbol, Boolean>();
		for (Symbol nonTerminal : nonTerminals) {
			producesEps.put(nonTerminal, nonTerminalsThatGenerateEps.contains(nonTerminal));
		}
		for (Symbol terminal : terminals) {
			producesEps.put(terminal, false);
		}

		this.nonTerminalsToProducesEps = producesEps;
	}

	public static void addAllElementsFromSetExceptEmptyString(Set<Symbol> setFrom, Set<Symbol> setTo){
		for (Symbol symbol : setFrom) {
			if (!symbol.isEmptyString()){
				setTo.add(symbol);
			}
		}
	}

	public Set<Rule> getRulesBySymbol(Symbol symbol) {
		return this.rules.get(symbol);
	}

	public Set<Symbol> follow(Symbol sym) {
		return this.followSets.get(sym);
	}

	public static void main(String[] args) throws Exception {
		//		Grammar g = new Grammar("A -> B e C B B B d B \nB -> b | A | \n C -> C a | f");
		// last
		//		Grammar g = new Grammar("S -> c A B D a\nA -> c B | B\n B -> b c B | \n A -> A f\n D -> d | ");
		// falta First(D) e {a} Grammar g = new Grammar("S -> c A B  D a\nA -> c B | B\n B -> b c B | \n A -> A f\n D -> d | ");
		//		Grammar g = new Grammar("S -> a S b S \n S -> a");
		Grammar g = new Grammar("A -> B C \n B -> \n C -> ");

		SLR slr = new SLR(g);
	}

	public Map<Symbol, Set<Symbol>> getFirstSets() {
		return firstSets;
	}

	public Map<Symbol, Set<Symbol>> getFollowSets() {
		return followSets;
	}

}
