package com.usco.esteban.agenda_procesos.app.controllers;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class IndexController {

	protected final Log logger = LogFactory.getLog(this.getClass());
	
	@GetMapping("/")
	public String index(RedirectAttributes flash, Authentication authentication)
	{
		if(authentication != null)
		{
			/* implementamos el nombre del ususario en el controlador para que muestre un
			 * mensaje en log cuando se inicia sesion correctamente */
			logger.info("Hola usuario autenticado, tu username es:".concat(authentication.getName()));
		}
		
		/* usando forma estática para obtener el nombre del usuario autenticado e imprimirlo 
		 * en el log */
		Authentication auth	 = SecurityContextHolder.getContext().getAuthentication();
		
		if(auth != null)
		{
			logger.info("Utilizando forma estática SecurityContextHolder.getContext().getAuthentication(): Usuario autenticado, tu username es:".concat(authentication.getName()));
		}
		
		return "index";
	}
	
}
