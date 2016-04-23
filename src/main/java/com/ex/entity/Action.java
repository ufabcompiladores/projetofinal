package com.ex.entity;

import java.util.List;
import java.util.Set;

public abstract class Action {

	private List<Set<RuleWithDot>> nextItemSets;
	private int lineToStoreActionInTable;
	private Set<Symbol> columnToStoreActionInTable;
	
	
	public Action(int currentStateNumber, RuleWithDot ruleWithDot, List<Set<RuleWithDot>> allStates){
		this.lineToStoreActionInTable = currentStateNumber;
		this.nextItemSets = allStates;
	}


	public List<Set<RuleWithDot>> getNextItemSets() {
		return nextItemSets;
	}


	public int getLineToStoreActionInTable() {
		return lineToStoreActionInTable;
	}


	public Set<Symbol> getColumnToStoreActionInTable() {
		return columnToStoreActionInTable;
	}


	public void setNextItemSets(List<Set<RuleWithDot>> nextItemSets) {
		this.nextItemSets = nextItemSets;
	}


	public void setLineToStoreActionInTable(int lineToStoreActionInTable) {
		this.lineToStoreActionInTable = lineToStoreActionInTable;
	}


	public void setColumnToStoreActionInTable(Set<Symbol> columnToStoreActionInTable) {
		this.columnToStoreActionInTable = columnToStoreActionInTable;
	}
	
	

}
