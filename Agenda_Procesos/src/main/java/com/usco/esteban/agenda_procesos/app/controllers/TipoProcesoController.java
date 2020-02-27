package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.service.ITipoProcesoService;

@Controller
@SessionAttributes("tipoProceso")
public class TipoProcesoController {
	
	@Autowired
	/* la anotacion @qualifier se usa para cuando hay varios repositorios que implementan
	 * la misma interfaz pero con diferente forma de trabajar, uno puede trabajar con JPA
	 * pero otro puede ser con JDBC, esto se usa para elegir cual es el que se desea usar*/
	private ITipoProcesoService tipoProcesoService;
	
	@RequestMapping(value ="/listarTiposProceso", method = RequestMethod.GET)
	public String listar(Model model)
	{
		model.addAttribute("titulo", "Listado de tipos de procesos");
		model.addAttribute("tiposProceso", tipoProcesoService.findAll());
		
		return "listarTipoProcesos";
	}
	
	/* como primera fase, se le muestra el formulario al usuario, y en este se le pasa
	 * el objeto que se quiere guardar, a la vista*/
	@GetMapping(value ="/formTipoProceso")
	public String crear(Map<String, Object> model)
	{
		
		TipoProceso tipoProceso = new TipoProceso();
		
		model.put("titulo", "Formulario de Tipo de Proceso");
		model.put("tipoProceso", tipoProceso);
		
		
		return "formTipoProceso";
	}
	
	
	@RequestMapping(value ="/formTipoProceso/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model)
	{
		
		TipoProceso tipoProceso = null;
		
		if(id > 0)
		{
			tipoProceso = tipoProcesoService.findOne(id);
		}
		else
		{
			return "redirect:/listarTiposProceso";
		}
		
		model.put("tipoProceso", tipoProceso);
		model.put("titulo", "Editar Tipo Proceso");
		return "formTipoProceso";
	}
	
	@RequestMapping(value ="/guardarTipoProceso",method = RequestMethod.POST)
	/* aqui se le pasa como parametro el objeto con los datos guardados desde el 
	 * formulario */
	public String guardar(TipoProceso tipoProceso, SessionStatus status)
	{
		tipoProcesoService.save(tipoProceso);
		status.setComplete();
		
		/* se hace el redirect a la url*/
		return "redirect:listarTiposProceso";
	}
	
	@RequestMapping(value ="/eliminarTipoProceso/{id}")
	public String eliminar(@PathVariable(value="id") Long id)
	{
		if(id > 0)
		{
			tipoProcesoService.delete(id);
		}
		return "redirect:/listarTiposProceso";
	}

}
