package com.ex.entity;


import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;

@Entity
public class SLR {

	private Grammar grammar;
	private List<Set<DottedSymbolSequence>> gotoSets;

	public SLR(Grammar grammar) {
		super();
		this.grammar = grammar;
		this.gotoSets = new ArrayList<>();
	}
	
	

}
