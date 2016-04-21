package com.ex.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ex.entity.Grammar;
import com.ex.entity.Rule;
import com.ex.entity.Symbol;
import com.ex.entity.Symbol.SymbolType;

@Service("grammar")
public class GrammarService {

	private Grammar grammar;

	public GrammarService(Grammar grammar) throws Exception {
		this.grammar = grammar;
	}

	
	
}
