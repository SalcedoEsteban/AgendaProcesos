package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.usco.esteban.agenda_procesos.app.models.dao.IDetalleTerminoDao;
import com.usco.esteban.agenda_procesos.app.models.dao.ITerminoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.ITerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.UsuarioServiceImpl;

@Controller
@SessionAttributes("detalleTermino")
public class DetalleTerminoController {

	@Autowired
	private ITerminoService terminoService;
	
	@Autowired
	private IProcesoService procesoService; 
	
	@Autowired
	private IDetalleTerminoDao detalleTerminoDao;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	private Usuario usuario;
	
	@RequestMapping(value="/crearDetalleTermino/{procesoId}")
	public String crear(@PathVariable(value="procesoId") Long procesoId, Model model)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		Proceso proceso = procesoService.findOne(procesoId);
		boolean basico = true;
		
		if(proceso == null)
		{
			return "redirect:/listarProcesos";
		}
		
		TipoProceso tipoProceso = proceso.getTipoProceso();
		
		List<Termino> terminos = terminoService.findByEspecialidadAndTipoProcesoAndBasico(especialidad, tipoProceso, basico);
		
		DetalleTermino detalleTermino = new DetalleTermino();
		
		detalleTermino.setProceso(proceso);
		
		model.addAttribute("titulo", "Formulario Detalle Termino");
		model.addAttribute("terminos", terminos);
		model.addAttribute("detalleTermino", detalleTermino);
		
		
		return "detalleTermino/formDetalleTermino";
	}
	
	@RequestMapping(value ="/guardarDetalleTermino", method = RequestMethod.POST)
	public String guardar(DetalleTermino detalleTermino, Model model)
	{
		
		Long idTermino = Long.parseLong(detalleTermino.getTer());
		
		Termino termino = terminoService.findOne(idTermino);
		
		detalleTermino.setTermino(termino);
		
		detalleTerminoDao.save(detalleTermino);
		
		List<Proceso> procesos = procesoService.findAll();
		
		model.addAttribute("procesos", procesos);
		
		return "listarProcesos";
	}
	
}
