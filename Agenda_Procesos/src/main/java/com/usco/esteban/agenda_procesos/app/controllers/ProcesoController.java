package com.usco.esteban.agenda_procesos.app.controllers;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.TipoProcesoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.dao.IUsuarioDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Alarma;
import com.usco.esteban.agenda_procesos.app.models.entity.DetalleTermino;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IAlarmaService;
import com.usco.esteban.agenda_procesos.app.models.service.IDetalleTerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.ITerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.ITipoProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.util.paginator.PageRender;

@Controller
/* con este atributo se pasa el objeto (y sus datos) mapeado al formulario a la sesion
 * esto se hace para que los datos queden persistentes hasta que se llame el metodo
 * guardarProceso, donde se cierra la sesion y los datos se borran */
@SessionAttributes("proceso")
public class ProcesoController {

	/* atributo del cliente dao para poder realizar la consulta */
	@Autowired
	private IProcesoService procesoService;
	
	/* con esta anotacion se busca un componente registrado en un contenedor
	 * (el cual es TipoProcesoDaoImpl que esta anotado con @Repository)
	 * que implemente la interfaz ITipoProcesoDao */
	@Autowired
	private ITipoProcesoService tipoProcesoDao;
	
	@Autowired
	private IJuzgadoService juzgadoService;
	
	@Autowired
	private IProcesoUsuarioService procesoUsuarioService;
	
	@Autowired
	private IUsuarioDao usuarioDao;
	
	@Autowired
	private IUsuarioService usuarioService;
	
	@Autowired
	private IAlarmaService alarmaService;
	
	@Autowired
	private IDetalleTerminoService detalleTerminoService;
	
	@Autowired
	private ITerminoService terminoService;
	
	@Autowired
	private TipoProcesoPropertyEditor tipoProcesoEditor;
	
	
	
	//private JpaUsuarioDetailsService usuarioService;
	
	private Usuario usuario;
	
	
	
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		binder.registerCustomEditor(TipoProceso.class, "tipoProceso", tipoProcesoEditor);
	}
	
	
	public Long getUserId()
	{
		Long id;
		
				
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		id = usuario.getId();
		return id;
	}
	
	@GetMapping(value="/verDetalleProceso/{id}")
	public String verDetalleProceso(@PathVariable(name = "id") Long id, Map<String, Object> model)
	{
		Proceso proceso = procesoService.findOne(id);
		
		String radicado = proceso.getRadicado();
		
		Long idTipoProceso = proceso.getTipoProceso().getId();
		TipoProceso tipo = tipoProcesoDao.findOne(idTipoProceso);
		String tipoProceso = tipo.getNombre();
		
		Long idJuzgado = proceso.getJuzgado().getId();
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		String nombreJuzgado = juzgado.getNombre();
		
		
		model.put("proceso", proceso);
		model.put("tipoProceso", tipoProceso);
		model.put("juzgado", nombreJuzgado);
		model.put("titulo", "Detalle de proceso con el Radicado: " + radicado);
		
		return "detalleProceso";
	}
	
	
	@GetMapping(value = "/verDetalleTerminosProceso/{id}")
	public String verDetalleTerminos(@PathVariable(name ="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Proceso proceso = procesoService.findOne(id);
		if(proceso == null)
		{
			flash.addFlashAttribute("error", "El proceso no existe en la base de datos");
			return "redirect:/listarProcesos";
		}
		
		model.put("proceso", proceso);
		model.put("titulo", "Terminos del proceso con el radicado: " + proceso.getRadicado());
		
		return "verTerminos";
	}
	
	@RequestMapping(value="/buscarProceso")
	public String buscarProceso(@RequestParam(value="radicado") String radicado,
			Map<String, Object> model, @RequestParam(name = "page", defaultValue = "0") int page)
	{
		Pageable pageRequest = PageRequest.of(page, 3);
		
		Long id = getUserId();
		
		//List<Proceso> proceso = procesoService.findByRadicado(radicado);
		Page<ProcesoUsuario> procesoUsuario = procesoUsuarioService.findByIdAndRadicado(id, pageRequest, radicado);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/buscarProceso", procesoUsuario);
		
		model.put("procesos", procesoUsuario);
		model.put("page", pageRender);
		
		return "listarProcesos";
	}
	
	
	//DetalleTermino detalleTermino = new DetalleTermino();
	private boolean terminoAnio;
	private Proceso proceso1 = null;
	private Calendar fechaActual1;
	private Calendar fechaFinal1;
	private Locale locale = Locale.getDefault();
	private TimeZone timeZone = TimeZone.getDefault();
	private Calendar notificacionDemandado;
	
	private boolean bandera2 = false;
	
	
	@RequestMapping(value = "/listarProcesos", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model, RedirectAttributes flash)
	{
		Pageable pageRequest = PageRequest.of(page, 3);
		
		Long id = getUserId();
		//Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		
		Page<ProcesoUsuario> procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest, juzgado);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/listarProcesos", procesosUsuario); 
		
		//System.out.println(procesosUsuario.isEmpty());
		//System.out.println(procesosUsuario.getNumberOfElements());
		
		for (ProcesoUsuario procesoUsuario : procesosUsuario) {
			System.out.println(procesoUsuario.getProceso().getRadicado());
		}
		
		
		/* ====== codigo para probar las notficaciones de vencimiento de terminos =====*/
		
		boolean terminoNotificacionDemandado = false;
		
		boolean estado = false;
		int dias1 = 0;
		//Termino termino1 = new Termino();
		
		Alarma alarma = new Alarma();
		//int dias1 = 0;
		//Proceso proceso = null;
		boolean bandera = false;
		Termino termino = new Termino();
		boolean bandera1 = false;
		int dias = 0;
		
		
		
		
		/*lista de procesos que estarn cercanos a vencer*/
		List<Proceso> procesosAVencer = new ArrayList<Proceso>();
		List<Proceso> procesosAdmitir = new ArrayList<Proceso>();
		for (ProcesoUsuario procesoUsuario : procesosUsuario)
		{
			List<DetalleTermino> detallesTermino = procesoUsuario.getProceso().getDetalleTerminos();
			boolean detalleBoolean = false;
			
			Proceso proceso = procesoUsuario.getProceso();
			proceso1 = procesoUsuario.getProceso();
			List<Alarma> alarmas = proceso.getAlarma();
			for(DetalleTermino detalle: detallesTermino)
			{
				termino = detalle.getTermino();
				
				System.out.println("los terminos del proceso son: "+ detalle.getTermino().getNombre());
				
				/*if(detalle.getTermino().getNombre().contentEquals("notificacion demandado"))
				{
					terminoNotificacionDemandado = true;
					System.out.println("notificacion igual a true");
				}*/
				
				if(termino.getNombre().contentEquals("Termino 121"))
				{
					this.terminoAnio = true;
					System.out.println("contiene el termino 121" + terminoAnio);
				}else
				{
					
					this.terminoAnio = false;
					System.out.println("no contiene el termino 121" + terminoAnio);
				}
				
				if(termino.getNombre().contentEquals("admision"))
				{
					System.out.println("si contiene admision en el detalle termino");
					
					Calendar fechaActual = Calendar.getInstance();
					
					Calendar fechaInicial = detalle.getFechaInicial();
					Calendar fechaFinal = detalle.getFechaFinal();
					
					fechaActual.set(Calendar.HOUR, 0);
					fechaActual.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual.set(Calendar.MINUTE, 0);
					fechaActual.set(Calendar.SECOND, 0);
					
					fechaFinal.set(Calendar.HOUR, 0);
					fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal.set(Calendar.MINUTE, 0);
					fechaFinal.set(Calendar.SECOND, 0);
					
					
					
					//long fechaIncialMS = fechaInicial.getTimeInMillis();
					long fechaFinalMS = fechaFinal.getTimeInMillis();
					long fechaActualMS = fechaActual.getTimeInMillis();
					
					//int dias = (int) ((Math.abs(fechaFinalMS - fechaActualMS)) / (1000 * 60 * 60* 24));
					dias = (int) ((fechaFinalMS - fechaActualMS) / (1000 * 60 * 60* 24));
					System.out.println("numero días de admision: " + dias);
					
					
					if(proceso.getUltimaActuacion().equalsIgnoreCase("admitido")
							&& dias > 0)
					{ 
						System.out.println("ultima actuacion es: Admitido");
						estado = true;	
					}
					else if(proceso.getUltimaActuacion().equalsIgnoreCase("admitido") && dias < 0)
					{
						estado = false;
					}	
					//System.out.println("los terminos del proceso de nuevo son: "+ detalle.getTermino().getNombre());	
				}
					
				if(termino.getNombre().equalsIgnoreCase("notificacion demandado") && estado == true)
				{
					flash.addFlashAttribute("warning", "Para el termino 121, Se está tomando desde la fecha de"
							+ "notificación al demandado");
					System.out.println("si hay termino de notificacion demandado");
					
					//proceso = procesoUsuario.getProceso();
					
					/*la fecha final cuando esté en producción la aplicación será esta, más 365
					 * días que es el año del 121  <<REVISAR COMO FUNCIONA LA ZONA HORARIA, SI SE 
					 * PUEDE CAMBIAR A A COLOMBIA PARA TENER EN CUENTA LOS FESTIVOS >>*/
					notificacionDemandado = detalle.getFechaFinal();
					
					/*la fecha actual si está bien, los calculos se hacen con la fecha final que 'es la de notificacion
					 * demandado + los 365 días' menos la fecha actual */
					fechaActual1 = Calendar.getInstance();
					
					/*para efectos de las pruebas, se establecio la fecha final de esta forma
					 * para que los días den 30 para que asi se pueda ejecutar la alarma y notificacion
					 * de 30 días antes del vencimiento de términos */
					fechaFinal1 = Calendar.getInstance();
					
					int dia = 19;
					int mes = 6;
					int año = 2020;
					
					fechaFinal1.set(año, mes, dia);
					System.out.println("la fecha final establecida para que de 30 es: " + fechaFinal1.getTime());
					
					System.out.println("Para el termino 121, Se está tomando desde la fecha de notificación al demandado");
					/*Calendar fechaFinal1= notificacionDemandado;
					System.out.println("fecha final: " + fechaFinal1);
					fechaFinal1.add(Calendar.DAY_OF_YEAR, 365);
					System.out.println("fecha final + 365: " + fechaFinal1.getTime());*/
					
					//System.out.println("termino año es igual a: " + terminoAnio);
					
					//System.out.println("if de termino año igual a true");
					
					fechaActual1.set(Calendar.HOUR, 0);
					fechaActual1.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual1.set(Calendar.MINUTE, 0);
					fechaActual1.set(Calendar.SECOND, 0);
					long fechaActual1MS = fechaActual1.getTimeInMillis();
					
					fechaFinal1.set(Calendar.HOUR, 0);
					fechaFinal1.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal1.set(Calendar.MINUTE, 0);
					fechaFinal1.set(Calendar.SECOND, 0);
					long fechaFinal1MS = fechaFinal1.getTimeInMillis();
					
					dias1 = (int) ((Math.abs(fechaFinal1MS - fechaActual1MS)) / (1000 * 60 * 60* 24));
					System.out.println("numero de días para el vencimiento del 121 son: " + dias1);
					bandera1 = true;
					
				}
				
				if(termino.getNombre().equalsIgnoreCase("notificacion demandado") && estado == false)
				{
					flash.addFlashAttribute("warning", "Para el termino 121, Se está tomando desde la fecha de"
							+ "reparto");
					System.out.println("Para el termino 121, Se está tomando desde la fecha de REPARTO");
					
					Calendar fechaReparto = proceso.getFechaReparto();
					
					fechaActual1 = Calendar.getInstance(timeZone, locale);
					fechaFinal1 = Calendar.getInstance(timeZone, locale);
					/*fechaFinal1 = fechaReparto;
					fechaReparto.add(Calendar.DAY_OF_YEAR, 365);*/
					
					int dia = 19;
					int mes = 6;
					int año = 2020;
					
					fechaFinal1.set(año, mes, dia);
					
					fechaActual1.set(Calendar.HOUR, 0);
					fechaActual1.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual1.set(Calendar.MINUTE, 0);
					fechaActual1.set(Calendar.SECOND, 0);
					long fechaActual1MS = fechaActual1.getTimeInMillis();
					
					fechaFinal1.set(Calendar.HOUR, 0);
					fechaFinal1.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal1.set(Calendar.MINUTE, 0);
					fechaFinal1.set(Calendar.SECOND, 0);
					long fechaFinal1MS = fechaFinal1.getTimeInMillis();
					
					dias1 = (int) ((Math.abs(fechaFinal1MS - fechaActual1MS)) / (1000 * 60 * 60* 24));
					System.out.println("numero de días para el vencimiento del 121 son: " + dias1);
					bandera1 = true;
				}
			}
			
			
			
			
			
			if(terminoAnio == false && bandera1 == true)
			{
				guardarDetalleTermino();
			}
			bandera1 = false;
			
			/*============= aqui va el codigo de dias = 15 para la admision ========*/
			if(dias == 15)
			{
				this.bandera2 = true;
				procesosAdmitir.add(proceso);
				/*bloque de codido para crear la alarma del proceso*/
				
				String descripcion = "Admision";
				Alarma alarma1 = alarmaService.findByDescripcionAndProceso(descripcion, proceso);
				
				if(alarma1 == null)
				{
					guardarAlarmaAdmision(proceso);
				}
				
				
				System.out.println("dias igual a 15");
				/*flash.addFlashAttribute("error", "el termino " + detalle.getTermino().getNombre() + " del proceso con radicado: "+ 
				procesoUsuario.getProceso().getRadicado()+ " está a punto de vencer");*/
				model.addAttribute("procesosAdmitir", procesosAdmitir);
				
				
			}
			dias = 0;
			/*=========================================================*/
			
			
			/*============== codigo de alarma de 30 dias para el vencimiento de terminos ======*/
			System.out.println("dias para el vencimiento 121 son: " + dias1);
			if(dias1 == 30)
			{
				procesosAVencer.add(proceso);
				//bandera = true;
				System.out.println("el radicado es "+ proceso.getRadicado());
				
				
				String descripcion = "Vencimiento anio";
				Alarma alarma1 = alarmaService.findByDescripcionAndProceso(descripcion, proceso);
				
				if(alarma1 == null)
				{
					guardarAlarma121(proceso);
				}
				
					
				proceso.setPrioritario(true);
				procesoService.save(proceso);
					
					//this.bandera = false;
				
				
				//System.out.println("dias igual a 30");
				model.addAttribute("vence", "Listado de procesos Cercanos a Vencer en " +
				dias1 + " días.");
				model.addAttribute("procesosAVencer", procesosAVencer);
				
			}
			
			//bandera = false;
			dias1 = 0;
			
			/*============== codigo de alarma de 30 dias para el vencimiento de terminos ======*/
			
		}
		//estado = false;
		//List<DetalleTermino> detalleTerminos = proceso.getDetalleTerminos();
		
		
		
		/* ============================================================= */
		if(this.terminoAnio == false)
		{
			System.out.println("TERMINO AÑO FALSE");
		}else if(this.terminoAnio == true)
		{
			System.out.println("TERMINO AÑO TRUE");
		}
		
		model.addAttribute("titulo", "Listado de procesos Activos");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		return "listarProcesos";
		
		
	}
	
	public void guardarDetalleTermino()
	{
		
			Long idUser = getUserId();
			Usuario usuario = usuarioService.findOne(idUser);
			String nombre = "Termino 121";
			Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
			TipoProceso tipoProceso = proceso1.getTipoProceso();
			
			/*termino1.setBasico(basico); 
			termino1.setNombre(nombre);
			termino1.setNumeroDias(numeroDias);
			termino1.setEspecialidad(especialidad);
			termino1.setTipoProceso(tipoProceso);
			terminoService.save(termino1);*/
			
			Termino termino1 = terminoService.findByNombreAndEspecialidadAndTipoProceso(nombre, especialidad, tipoProceso);
			DetalleTermino detalleTermino = new DetalleTermino();
			
			detalleTermino.setProceso(proceso1);
			detalleTermino.setTermino(termino1);
			detalleTermino.setDiasHabiles(true);
			detalleTermino.setFechaInicial(fechaActual1);
			detalleTermino.setFechaFinal(fechaFinal1);
			detalleTerminoService.save(detalleTermino);
			System.out.println("SE GUARDO EL DETALLE TERMINO");
		
	}
	
	public void guardarAlarmaAdmision(Proceso proceso)
	{
		Alarma alarma = new Alarma();
		String descripcion = "Admision";
		alarma.setProceso(proceso);
		alarma.setDescripcion(descripcion);
		alarmaService.save(alarma);	
		System.out.println("SE GUARDO LA ALARMA DE ADMISION");
		
	}
	
	public void guardarAlarma121(Proceso proceso)
	{
		Alarma alarma = new Alarma();
		String descripcion = "Vencimiento anio";
		alarma.setProceso(proceso);
		alarma.setDescripcion(descripcion);
		alarmaService.save(alarma);	
		System.out.println("SE GUARDO LA ALARMA DE TERMINO 121");
		
	}
	
	/* ==========================================================================*/
	
	@RequestMapping(value="/adminEstadoProceso/{id}")
	public String administrarEstadoProceso(@PathVariable(value="id") Long id, RedirectAttributes flash)
	{
		Proceso proceso = null;
		
		if(id > 0)
		{
			proceso = procesoService.findOne(id);
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "el proceso no existe");
				return "redirect:/verProcesos";
			}
		}
		
		boolean estado = proceso.getEstado();
		if(estado)
		{
			proceso.setEstado(false);
			String radicado = proceso.getRadicado();
			procesoService.save(proceso);
			flash.addFlashAttribute("success", "El proceso con radicado: " + radicado + " fue DESACTIVADO exitosamente");
			return "redirect:/verProcesos";
		}
		else
		{
			proceso.setEstado(true);
			String radicado = proceso.getRadicado();
			procesoService.save(proceso);
			flash.addFlashAttribute("success", "El proceso con radicado: " + radicado + " fue ACTIVADO exitosamente");
			return "redirect:/verProcesos";
		}
		
	}
	
	@RequestMapping(value="/verProcesosPrioritarios", method = RequestMethod.GET)
	public String verProcesosPrioritarios(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 3);
		
		Long id = getUserId();
		//Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		
		Page<ProcesoUsuario> procesosUsuario = procesoUsuarioService.findByUsuarioAndEstadoAndJuzgadoAndPrioritario(id, pageRequest, juzgado);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/verProcesosPrioritarios", procesosUsuario);
		
		model.addAttribute("titulo", "Listado de procesos Prioritarios");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		return "verProcesosPrioritarios";
	}
	
	@RequestMapping(value="/verProcesos")
	public String verTodosProcesos(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Long id = getUserId();
		//Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		
		Page<ProcesoUsuario> procesosUsuario = procesoUsuarioService.findAllBy(id, pageRequest, juzgado);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/verProcesos", procesosUsuario);
		
		model.addAttribute("titulo", "Listado de Todos los procesos");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		
		return "verProcesos";
	}
	
	@RequestMapping(value="/admitirProceso/{id}")
	public String admitirProceso(@PathVariable(value="id") Long id, RedirectAttributes flash)
	{
		Proceso proceso = null;
		if(id>0)
		{
			proceso = procesoService.findOne(id);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}
		}
		String radicado = proceso.getRadicado();
		String admitido = proceso.getUltimaActuacion();
		if(admitido.isEmpty())
		{
			
			proceso.setUltimaActuacion("admitido");
			procesoService.save(proceso);
			flash.addFlashAttribute("success", "El proceso con el radiaco: "+ radicado+ " fue ADMITIDO");
			return "redirect:/listarProcesos";
		}
		else if(admitido.contentEquals("admitido"))
		{
			flash.addFlashAttribute("warning", "El proceso con el radiaco: "+ radicado+ " YA FUE ADMITIDO");
		}
		
		return "redirect:/listarProcesos";
	}
	
	/*===============================================================================*/
	@RequestMapping(value="/verAlarmasProceso/{id}")
	public String verAlarmasProceso(@PathVariable(value="id") Long id, Model model)
	{
		Proceso proceso = procesoService.findOne(id);
		List<Alarma> alarmas = alarmaService.findByProceso(proceso);
		String radicado = proceso.getRadicado();
		
		model.addAttribute("titulo", "Alarmas del Proceso con radicado: " + radicado);
		model.addAttribute("alarmas", alarmas);
		return "alarma/verAlarmas";
	}
	
	@RequestMapping(value ="/formProceso")
	public String crear(Map<String, Object> model)
	{
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Long id = usuario.getJuzgado().getId();
		System.out.println("id del jusgado: " + id);
		
		String nombre = usuario.getNombre();
		System.out.println("nombre usuario: " + nombre);
		
		Juzgado juzgado = usuario.getJuzgado();
		System.out.println("el juzgado es: " + juzgado);
		
		List<Usuario> usuarios = usuarioDao.findByJuzgado(juzgado);
		/* esta es una primera fase en la que se muestra el formulario al ususario, se hace
		 * una instancia de un proceso y se envia a la vista*/
		Proceso proceso = new Proceso();
		
		model.put("proceso", proceso);
		model.put("usuarios", usuarios);
		model.put("tipoProcesos", tipoProcesoDao.findAll());
		model.put("juzgados", juzgadoService.findAll());
		model.put("titulo", "Crear Proceso");
		return "formProceso";
	}
	
	@RequestMapping(value="/editarProceso/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Proceso proceso = null;
		
		/* si el id es mayor que cero se busca en la base de datos*/
		if(id > 0)
		{
			/* si lo encuentra se almacena*/
			proceso = procesoService.findOne(id);
			System.out.println("se encontró el proceso");
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}		
		}
		else
		{
			flash.addFlashAttribute("error", "El id del proceso no puede ser cero");
			/* si no se encuentra se redirige al listado */
			return "redirect:/listarProcesos";
		}
		
		Juzgado juzgado = proceso.getJuzgado();
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		
		model.put("proceso", proceso);
		model.put("usuarios", usuarios);
		model.put("titulo", "Editar Proceso");
		model.put("tipoProcesos", tipoProcesoDao.findAll());
		model.put("juzgados", juzgadoService.findAll());
		
		
		
		return "formProceso";
	}
	
	@PostMapping(value = "/guardarProceso")
	public String guardar(@RequestParam(value = "usuario") String usuario, @Valid Proceso proceso, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status)
	{
		/* si al validar los campos, estos contienen errores se envia al formulario de
		 * crear proceso de nuevo para que se corrijan los errores*/
		/* por eso se agrega otro parametro al metodo guardar llamado BindingResult, este
		 * parametro siempre va junto al objeto que esta mapeado al formulario, en este
		 * caso, el objeto proceso */
		if (result.hasErrors())
		{
			/* se debe pasar nuevamente el titulo a la vista*/
			model.addAttribute("titulo", "Crear Proceso");
			
			model.addAttribute("tipoProcesos", tipoProcesoDao.findAll());
			
			/* se retorna el formulario con los errores*/
			return "formProceso";
		}
		
		System.out.println("el id del proceso a guardar es: " + proceso.getId());
		String mensajeFlash = (proceso.getId() != null) ? "Proceso editado con exito" : "Proceso creado con exito";
		boolean bandera = false;
		if(proceso.getId() == null)
		{
			bandera = true;
		}
		
		/* obtenemos el id de tipo string y lo se parsea a Long */
		//Long tProceso = Long.parseLong(proceso.gettProceso());
		/* luego de parseado se busca el objeto y se asigna*/
		//TipoProceso tipoProceso = tipoProcesoDao.findOne(tProceso);
		/* y se establece la relacion guradando el tipo de proceso*/
		//proceso.setTipoProceso(tipoProceso);
		
		
		
		Usuario usuarioLogeado = usuarioService.findOne(getUserId());
		Long idJuzgado = usuarioLogeado.getJuzgado().getId();
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		proceso.setJuzgado(juzgado);
		
		Long id = Long.parseLong(usuario);
		Usuario usu = new Usuario(); 
		usu = usuarioService.findOne(id);
		
		System.out.println("el id del usuario es " + id);
		System.out.println("el nombre del usuario es" + usu.getNombre());
		System.out.println("el radicado del proceso es: " + proceso.getRadicado());
		
		procesoService.save(proceso);
		
		/*if(proceso.getId() == null)
		{
			ProcesoUsuario procesoUsuario = new ProcesoUsuario();
			procesoUsuario.setProceso(proceso);
			procesoUsuario.setUsuario(usu);
			
			procesoUsuarioService.save(procesoUsuario);
		}*/
		
		
		if(bandera == true)
		{
			
			ProcesoUsuario procesoUsuario = new ProcesoUsuario();
			
			List<ProcesoUsuario> listProcesosUsuarios = new ArrayList<ProcesoUsuario>();
			listProcesosUsuarios.add(procesoUsuario);
			proceso.setProcesosUsuarios(listProcesosUsuarios);
			procesoUsuario.setProceso(proceso);
			procesoUsuario.setUsuario(usu);
			
			procesoUsuarioService.save(procesoUsuario);
		}
		
		
		
		
		
		
		/* despues de guardar el objeto, se termina la sesion */
		status.setComplete();
		flash.addFlashAttribute("success", mensajeFlash);
		return "redirect:listarProcesos";
	}
	
	@RequestMapping(value ="/eliminarProceso/{id}")
	public String eliminar(@PathVariable(value = "id") Long id, RedirectAttributes flash)
	{
		if(id > 0)
		{
			procesoService.delete(id);
			flash.addFlashAttribute("success", "El proceso se ha eliminado con èxito");
		}
		
		return "redirect:/listarProcesos";
	}
}