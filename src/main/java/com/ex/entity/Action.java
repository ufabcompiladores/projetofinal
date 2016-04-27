package com.ex.entity;

import java.util.List;
import java.util.Set;

/**
 * Represents an Action in the context of SLR parsing.
 * The type of action is determined by the instantiated subclass - Goto, Shift, Reduce or Accept.
 * Actions store also the line and columns in which they are placed on the SLR table.
 * @author andre0991
 *
 */
public abstract class Action {

	private List<State> nextItemSets;
	private int lineToStoreActionInTable;
	private Set<Symbol> columnToStoreActionInTable;
	
	public Action(int currentStateNumber, RuleWithDot ruleWithDot, List<State> allStates){
		this.lineToStoreActionInTable = currentStateNumber;
		this.nextItemSets = allStates;
	}

	public List<State> getNextItemSets() {
		return nextItemSets;
	}

	public int getLineToStoreActionInTable() {
		return lineToStoreActionInTable;
	}


	public Set<Symbol> getColumnToStoreActionInTable() {
		return columnToStoreActionInTable;
	}


	public void setNextItemSets(List<State> nextItemSets) {
		this.nextItemSets = nextItemSets;
	}


	public void setLineToStoreActionInTable(int lineToStoreActionInTable) {
		this.lineToStoreActionInTable = lineToStoreActionInTable;
	}

	public void setColumnToStoreActionInTable(Set<Symbol> columnToStoreActionInTable) {
		this.columnToStoreActionInTable = columnToStoreActionInTable;
	}
}
