package com.ex.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class ExController {

	@RequestMapping("home")
	public ModelAndView ex() {
		return new ModelAndView("home");
	}
}
