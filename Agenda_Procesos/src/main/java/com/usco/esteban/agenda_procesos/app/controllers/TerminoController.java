package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.models.dao.IEspecialidadDao;
import com.usco.esteban.agenda_procesos.app.models.dao.ITerminoDao;
import com.usco.esteban.agenda_procesos.app.models.dao.ITipoProcesoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;

@Controller
@SessionAttributes("termino")
public class TerminoController
{
	@Autowired
	private ITerminoDao terminoDao;
	
	@Autowired
	private ITipoProcesoDao tipoProcesoDao;
	
	@Autowired
	private IEspecialidadDao especialidadDao;
	
	@GetMapping("/formTermino/{tipoProcesoId}")
	public String crear(@PathVariable(value="tipoProcesoId") Long tipoProcesoId,
			Map<String, Object> model, RedirectAttributes flash)
	{
		
		/* FALTA PONER LOS MENSAJES DE ERROR*/
		
		/*se obtiene el tipoProceso por el id*/
		TipoProceso tipoProceso = tipoProcesoDao.findOne(tipoProcesoId);
		
		if(tipoProceso == null)
		{
			return "redirect:/listarTiposProceso";
		}
		
		Termino termino = new Termino();
		/* esta es la relacion, se asigna un tipoProceso a un término */
		termino.setTipoProceso(tipoProceso);
		
		model.put("termino", termino);
		model.put("especialidades", especialidadDao.findAll());
		model.put("titulo", "Formulario de terminos");
		
		return "formTermino";
	}
	
	@RequestMapping(value ="/listarTerminos", method = RequestMethod.GET)
	public String listar(Model model)
	{
		model.addAttribute("titulo", "Listado de Terminos");
		model.addAttribute("terminos", terminoDao.findAll());
		
		return "listarTerminos";
	}
	
	/*@RequestMapping(value ="/formTermino")
	public String crearTermino(Map<String, Object> model)
	{
		Termino termino = new Termino();
		
		model.put("termino", termino);
		model.put("tiposProceso", tipoProcesoDao.findAll());
		model.put("titulo", "Formulario de Terminos");
		
		return "formTermino";
	}*/
	
	@PostMapping(value ="/guardarTermino")
	public String guardar(Termino termino)
	{
		Long esp = Long.parseLong(termino.getEsp());
		
		Especialidad especialidad = especialidadDao.findOne(esp);
		
		termino.setEspecialidad(especialidad);
		
		
		terminoDao.save(termino);
		
		return "redirect:/listarTiposProceso";
	}
	
	@GetMapping(value="/eliminarTermino/{id}")
	public String eliminar(@RequestParam(name = "id") Long id)
	{
		if(id > 0)
		{
			terminoDao.delete(id);
		}
		
		return "redirect:/listarTerminos";
	}
	
	
}
