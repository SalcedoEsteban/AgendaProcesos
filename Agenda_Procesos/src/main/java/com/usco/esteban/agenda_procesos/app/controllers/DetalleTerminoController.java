package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.usco.esteban.agenda_procesos.app.models.dao.IDetalleTerminoDao;
import com.usco.esteban.agenda_procesos.app.models.dao.ITerminoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoService;

@Controller
@SessionAttributes("detalleTermino")
public class DetalleTerminoController {

	@Autowired
	private ITerminoDao terminoDao;
	
	@Autowired
	private IProcesoService procesoService; 
	
	@Autowired
	private IDetalleTerminoDao detalleTerminoDao;
	
	@RequestMapping(value="/crearDetalleTermino/{procesoId}")
	public String crear(@PathVariable(value="procesoId") Long procesoId, Model model)
	{
		
		Proceso proceso = procesoService.findOne(procesoId);
		
		if(proceso == null)
		{
			return "redirect:/listarProcesos";
		}
		
		DetalleTermino detalleTermino = new DetalleTermino();
		
		detalleTermino.setProceso(proceso);
		
		model.addAttribute("titulo", "Formulario Detalle Termino");
		model.addAttribute("terminos", terminoDao.findAll());
		model.addAttribute("detalleTermino", detalleTermino);
		
		
		return "detalleTermino/formDetalleTermino";
	}
	
	@RequestMapping(value ="/guardarDetalleTermino", method = RequestMethod.POST)
	public String guardar(DetalleTermino detalleTermino, Model model)
	{
		
		Long idTermino = Long.parseLong(detalleTermino.getTer());
		
		Termino termino = terminoDao.findOne(idTermino);
		
		detalleTermino.setTermino(termino);
		
		detalleTerminoDao.save(detalleTermino);
		
		List<Proceso> procesos = procesoService.findAll();
		
		model.addAttribute("procesos", procesos);
		
		return "listarProcesos";
	}
	
}
