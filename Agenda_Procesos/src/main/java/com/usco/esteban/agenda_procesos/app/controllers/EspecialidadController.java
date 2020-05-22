package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.service.IEspecialidadService;

@Controller
@SessionAttributes("especialidad")
@RequestMapping("/especialidad")
public class EspecialidadController {
	
	@Autowired
	private IEspecialidadService especialidadService;
	
	@RequestMapping(value ="/crear")
	public String crear(Map<String, Object> model) {
		
		Especialidad especialidad = new Especialidad();
		
		model.put("especialidad", especialidad);
		model.put("titulo", "Crear Especialidad");
		
		return "especialidad/formEspecialidad";
	}
	
	@RequestMapping(value="/guardar")
	public String guardar(Model model, Especialidad especialidad, RedirectAttributes flash, SessionStatus status)
	{
		String mensajeFlash = (especialidad.getId() != null) ? "La Especialidad fue editada con éxito" : "La Especialidad fue guardada con éxito";
		
		especialidadService.save(especialidad);
		
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		
		return "redirect:/especialidad/listar";
	}
	
	@RequestMapping(value ="/listar")
	public String listar(Model model)
	{
		List<Especialidad> especialidades = especialidadService.findAll();
		
		model.addAttribute("especialidades", especialidades);
		model.addAttribute("titulo", "Listado de Especialidades");
		return "especialidad/listar";
	}
	
	@RequestMapping(value ="/editar/{id}")
	public String editar(@PathVariable(value ="id") Long id, Model model, RedirectAttributes flash)
	{
		Especialidad especialidad = null;
		
		if(id > 0)
		{
			especialidad = especialidadService.findOne(id);
			if(especialidad == null)
			{
				flash.addFlashAttribute("error", "La Especialidad no existe");
				return "redirect:/especialidad/listar";
			}
				
		}
		else
		{
			flash.addFlashAttribute("error", "El ID de la especialidad no puede ser cero");
			return "redirect:/especialidad/listar";
		}
		
		model.addAttribute("especialidad", especialidad);
		model.addAttribute("titulo", "Editar Especialidad");
		
		return "especialidad/formEspecialidad";
	}
	
	@RequestMapping(value ="/eliminar/{id}")
	public String eliminar(@PathVariable(value ="id") Long id, RedirectAttributes flash)
	{
		if(id > 0)
		{
			especialidadService.delete(id);
			flash.addFlashAttribute("success", "La Especialidad se eliminó con exito");
		}
		
		
		return "redirect:/especialidad/listar";
	}

}
