package com.usco.esteban.agenda_procesos.app.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.service.IHistorialUsuarioService;

@Controller
@RequestMapping("/historialUsuario")
public class HistorialUsuarioController {
	
	@Autowired
	private IHistorialUsuarioService historialUsuarioService;
	
	@RequestMapping(value ="/guardar")
	public String guardar()
	{
		HistorialUsuario historialUsuario = new HistorialUsuario();
		
		return "";
	}

}
