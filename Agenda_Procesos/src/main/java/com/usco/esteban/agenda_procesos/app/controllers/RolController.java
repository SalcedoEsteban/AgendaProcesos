package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IRolService;
import com.usco.esteban.agenda_procesos.app.models.service.JpaUsuarioDetailsService;



@Controller
@SessionAttributes("rol")
public class RolController
{
	
	@Autowired
	private JpaUsuarioDetailsService usuarioService;
	
	@Autowired
	private IRolService rolService;
	
	
	@RequestMapping(value ="/guardarRol", method = RequestMethod.POST)
	public String guardarRoles(Rol rol, SessionStatus status, RedirectAttributes flash)
	{
		
		String mensajeFlash = (rol.getId() != null) ? "Rol editado con exito" : "Rol creado con exito";
	
		
		rolService.save(rol);
		status.setComplete();
		flash.addFlashAttribute("succes", mensajeFlash);
		
		return "redirect:/listarUsuarios";
	}
	
	@RequestMapping(value="/crearRol/{idUsuario}")
	public String crearRol(@PathVariable(value ="idUsuario") Long id, Map<String, Object> model)
	{
		Usuario usuario = usuarioService.findOne(id);
		
		if(usuario == null)
		{
			return "redirect:/listarUsuarios";
		}
		
		Rol rol = new Rol();
		
		rol.setUsuario(usuario);
		
		model.put("titulo", "Asignar Rol al usuario: " + usuario.getNombre()+ " " + usuario.getApellido());;
		model.put("rol", rol);
		
		return "rol/formRol";
	}
}
