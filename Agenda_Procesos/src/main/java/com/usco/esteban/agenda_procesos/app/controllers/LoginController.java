package com.usco.esteban.agenda_procesos.app.controllers;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class LoginController {
	
	@GetMapping("/login")
	public String login(@RequestParam(value="error", required = false) String error, Model model, Principal principal, RedirectAttributes flash)
	{
		/* el objeto Principal contiene al usuario logeado */
		
		/*si la condicion es verdadera es porque ya se habia iniciado sesion
		 *  y no vuelva a mostrar el formulario de login 
		 */
		if(principal != null)
		{
			flash.addFlashAttribute("info", "Ya ha iniciado sesion anteriormente");
			return "redirect:/"; /* se redirige a la pagina de incio para evitar que haga doble inicio de sesion 
			ACA SE DEBE CAMBIAR LA PAGINA DE INICIO, NO DEBE SER "/listarProcesos" */
		}
		
		//error es el parametro de que envia spring cuando ocurre un error en el login.
		if(error != null)
		{
			model.addAttribute("error", "Nombre de usuario o contraseña incorrecta, vuelva a intentarlo");
		}
		
		return "login";
	}
}
