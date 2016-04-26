package com.ex.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.ex.entity.Grammar;
import com.ex.service.LLService;

@Controller
@RequestMapping(value="/grammar")
public class GrammarController {
	
	@RequestMapping(value="/create/text", method=RequestMethod.POST)
	public ModelAndView createTextGrammar (ModelAndView mv,
			@RequestParam("rules") String inputGrammar) {
		try {
			Grammar grammar = new Grammar(inputGrammar);
			
			mv.addObject(grammar);
			mv.setViewName("/grammar/options");
		} catch (Exception e) {
			mv.addObject("error", e.getMessage());
			
			mv.setViewName("/error");	
		}
		
		return mv;
	}
	
	/*
	 * Consertar esta entrada.
	 * Precisamos fazer de forma que consigamos uma entrada boa.
	 */
//	@RequestMapping(value="/create/text", method=RequestMethod.POST)
//	public ModelAndView createGrammar (ModelAndView mv) {
//		
//	}
	
	@RequestMapping(value="/process")
	public ModelAndView processGrammar (ModelAndView mv,
			@RequestParam("grammar-options") String option) {
		
		//TODO Ver como fazer isso
		Grammar grammar = (Grammar) mv.getModel().get("grammar");
		LLService service = new LLService(grammar);

		try {
			switch (option) {
			case ("first"):
				mv.addObject("firsts", grammar.getFirstSets());
				break;
			case ("follow"):
				mv.addObject("follows", grammar.getFollowSets());
				break;
			case ("ll"):
				mv.addObject("LLTable", service.buildParseTable());
				break;
			case ("slr"):
				//TODO
				break;
			default:
				mv.setViewName("/error");
				mv.addObject("error", "Esta opção não está disponível.");
				break;
			}
			
			return mv;
		} catch (Exception e) {
			mv.setViewName("/error");
			mv.addObject("error", e.getMessage());
			return mv;
		}
	}

}
