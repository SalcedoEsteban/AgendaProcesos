package com.usco.esteban.agenda_procesos.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

	@GetMapping("/")
	public String index(RedirectAttributes flash)
	{
		return "index";
	}
	
}
