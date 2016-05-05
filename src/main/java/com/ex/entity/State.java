package com.ex.entity;

import java.util.Set;

/**
 * Represents a state in the context of SLR parsing.
 * A state contains rules with dots.
 * @author andre0991
 *
 */
public final class State {
	Set<RuleWithDot> rules;


	public State(Set<RuleWithDot> rules) {
		super();
		this.rules = rules;
	}

	public Set<RuleWithDot> getRules() {
		return rules;
	}

	@Override
	public String toString() {
		return String.format("{%s}", rules.toString().replace("^[", "").replace("]$", ""));
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((rules == null) ? 0 : rules.hashCode());
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
		State other = (State) obj;
		if (rules == null) {
			if (other.rules != null)
				return false;
		} else if (!rules.equals(other.rules))
			return false;
		return true;
	}
	
	
}
