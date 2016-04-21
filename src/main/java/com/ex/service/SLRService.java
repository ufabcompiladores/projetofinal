package com.ex.service;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ex.entity.Rule;
import com.ex.entity.SLRTable;

@Service("SLR")
public class SLRService {
	
	@Autowired
	GrammarService grammarService;

	public Set<Rule> buildSLRCanonical () {
		return null;
	}
	
	/**
	 * Criar tabela SLR a partir 
	 * @return
	 */
	public SLRTable buildSLRTable () {
		return null;
	}
}
