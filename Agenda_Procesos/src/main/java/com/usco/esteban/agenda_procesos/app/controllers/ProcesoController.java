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
import org.springframework.data.domain.PageImpl;
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
import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Termino;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IAlarmaService;
import com.usco.esteban.agenda_procesos.app.models.service.IDetalleTerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.IRolService;
import com.usco.esteban.agenda_procesos.app.models.service.ITerminoService;
import com.usco.esteban.agenda_procesos.app.models.service.ITipoProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.util.paginator.PageRender;
import com.usco.esteban.agenda_procesos.app.validation.CalculaDiasHabiles;

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
	private IRolService rolService;
	
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
		
		List<Alarma>alarmas = proceso.getAlarma();
		boolean alarmaAdmision = false;
		
		for(Alarma alarma: alarmas)
		{
			if(alarma.getDescripcion().contentEquals("Admision"))
			{
				alarmaAdmision = true;
			}
		}
		
		List<DetalleTermino> detalleTerminos = proceso.getDetalleTerminos();
		boolean terminoAdmision = false;
		
		for(DetalleTermino detalle: detalleTerminos)
		{
			if(detalle.getTermino().getNombre().equalsIgnoreCase("admision"))
			{	
				terminoAdmision = true;
				model.put("terminoAdmision", terminoAdmision);
			}
		}
		
		String radicado = proceso.getRadicado();
		
		Long idTipoProceso = proceso.getTipoProceso().getId();
		TipoProceso tipo = tipoProcesoDao.findOne(idTipoProceso);
		String tipoProceso = tipo.getNombre();
		
		Long idJuzgado = proceso.getJuzgado().getId();
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		String nombreJuzgado = juzgado.getNombre();
		
		
		model.put("alarmaAdmision", alarmaAdmision);
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
		
		List<Proceso> procesosAVencer = new ArrayList<Proceso>();
		List<Proceso> procesosAdmitir = new ArrayList<Proceso>();
		
		Pageable pageRequest = PageRequest.of(page, 5);
		
		Long id = getUserId();
		
		//List<Proceso> proceso = procesoService.findByRadicado(radicado);
		Page<ProcesoUsuario> procesoUsuario = procesoUsuarioService.findByIdAndRadicado(id, pageRequest, radicado);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/buscarProceso", procesoUsuario);
		
		model.put("procesosAVencer", procesosAVencer);
		model.put("procesosAdmitir", procesosAdmitir);
		model.put("procesos", procesoUsuario);
		model.put("page", pageRender);
		model.put("titulo", "Proceso(s) encontrado(s)"); 
		
		return "listarProcesos";
	}
	
	@RequestMapping(value ="/formAgregarDiasHabiles")
	public String formAgregarDiasHabiles(Model model)
	{
		model.addAttribute("titulo", "Agregar Días Calendario a los Procesos");
		
		return "formAgregarDiasHabiles";
	}
	
	@RequestMapping(value="/agregarDiasHabiles", method = RequestMethod.POST)
	public String agregarDiasHabiles(@RequestParam(value="fecha") Calendar fecha, Map<String, Object> model, RedirectAttributes flash)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		Juzgado juzgado = usuario.getJuzgado();
		
		Calendar fechaInicial = Calendar.getInstance(timeZone, locale);
		fechaInicial.set(Calendar.HOUR, 0);
		fechaInicial.set(Calendar.HOUR_OF_DAY, 0);
		fechaInicial.set(Calendar.MINUTE, 0);
		fechaInicial.set(Calendar.SECOND, 0);
		//long fechaInicialMS = fechaInicial.getTimeInMillis();
		
		Calendar fechaFinal = Calendar.getInstance(timeZone, locale);
		fechaFinal.set(Calendar.YEAR, fecha.get(Calendar.YEAR));
		fechaFinal.set(Calendar.MONTH, fecha.get(Calendar.MONTH));
		fechaFinal.set(Calendar.DATE, fecha.get(Calendar.DATE));
		
		fechaFinal.set(Calendar.HOUR, 0);
		fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
		fechaFinal.set(Calendar.MINUTE, 0);
		fechaFinal.set(Calendar.SECOND, 0);
		//long fechaFinalMS = fechaFinal.getTimeInMillis();
		
		int diasHabiles = CalculaDiasHabiles.diasHabiles(fechaInicial, fechaFinal);
		
		List<ProcesoUsuario> procesos = procesoUsuarioService.findAll(juzgado);
		
		for(ProcesoUsuario proceso : procesos)
		{
			List<DetalleTermino> detalles = proceso.getProceso().getDetalleTerminos();
			
			for(DetalleTermino detalle: detalles)
			{
				if(detalle.getTermino().getNombre().equalsIgnoreCase("admision"))
				{
					Calendar fechaFinalAdmision = Calendar.getInstance(timeZone, locale);
					fechaFinalAdmision = detalle.getFechaFinal();
					fechaFinalAdmision.set(Calendar.DAY_OF_YEAR, diasHabiles);
					detalleTerminoService.save(detalle);
				}
				else if(detalle.getTermino().getNombre().equalsIgnoreCase("subsanar demanda"))
				{
					Calendar fechaFinalSubsanarDemanda = Calendar.getInstance(timeZone, locale);
					fechaFinalSubsanarDemanda = detalle.getFechaFinal();
					fechaFinalSubsanarDemanda.set(Calendar.DAY_OF_YEAR, diasHabiles);
					detalleTerminoService.save(detalle);
				}
				
			}
		}
		
		//flash.addFlashAttribute("success", "Fueron agregados " + dias +" días correctamente a los terminos de los procesos");
		return "redirect:/listarProcesos";
	}
	
	//===========================================
	@RequestMapping(value ="/formAgregarDiasCalendario")
	public String formAgregarDias(Model model)
	{
		model.addAttribute("titulo", "Agregar Días Calendario a los Procesos");
		
		return "formAgregarDiasCalendario";
	}
	
	
	@RequestMapping(value="/agregarDiasCalendario", method = RequestMethod.POST)
	public String agregarDiasCalendario(@RequestParam(value="dias") int dias, Map<String, Object> model, RedirectAttributes flash)
	{
			//Long id = getUserId();
			//int diasParaAgregar = dias;
			
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			UserDetails userDetail = (UserDetails) auth.getPrincipal();
			usuario = this.usuarioService.findByUsername(userDetail.getUsername());
			
			Juzgado juzgado = usuario.getJuzgado();
			
			List<ProcesoUsuario> procesos = procesoUsuarioService.findAll(juzgado);
			
			TimeZone timeZone = TimeZone.getDefault();
			Locale locale = Locale.getDefault();
			
			for(ProcesoUsuario proceso : procesos)
			{
				List<DetalleTermino> detalles = proceso.getProceso().getDetalleTerminos();
				
				for(DetalleTermino detalle: detalles)
				{
					
					if(detalle.getTermino().getNombre().equalsIgnoreCase("Termino 121"))
					{
						Calendar fechaFinal = Calendar.getInstance(timeZone, locale);
						fechaFinal = detalle.getFechaFinal();
						fechaFinal.add(Calendar.DAY_OF_YEAR, dias);
						detalleTerminoService.save(detalle);
					} 
					
				}
			}
			
			flash.addFlashAttribute("success", "Fueron agregados " + dias +" días correctamente a los terminos de los procesos");
			return "redirect:/listarProcesos";
		}
	
	@RequestMapping(value="/agregarProrroga/{id}")
	public String agregarProrroga(@PathVariable(value ="id")Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		//Long id = getUserId();
		//int diasParaAgregar = dias;
		Proceso proceso = procesoService.findOne(id);
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		
		//List<ProcesoUsuario> procesos = procesoUsuarioService.findAllByPrioritario(juzgado);
		
		TimeZone timeZone = TimeZone.getDefault();
		Locale locale = Locale.getDefault();
		
		List<DetalleTermino> detalles = proceso.getDetalleTerminos();
			
		for(DetalleTermino detalle: detalles)
		{
			String nombre = detalle.getTermino().getNombre();
				
			if(nombre.contentEquals("Termino 121"))
			{
				Calendar fecha = Calendar.getInstance(timeZone, locale);
				fecha = detalle.getFechaFinal();
				fecha.add(Calendar.MONTH, 5);
				detalleTerminoService.save(detalle);
			}	
				
		}
		
		flash.addFlashAttribute("success", "Fueron agregado la Prorroga correctamente a los terminos del proceso");
		return "redirect:/listarProcesos";
	}
	
	@RequestMapping(value="/dictarSentencia/{id}")
	public String dictarSentencia(@PathVariable(value ="id") Long id, RedirectAttributes flash)
	{
		
		
		Proceso proceso = null;
		
		if(id > 0)
		{
			proceso = procesoService.findOne(id);
			
			if(proceso == null)
			{
				flash.addFlashAttribute("error", "El proceso no existe");
				return "redirect:/listarProcesos";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El id del proceso no puede ser cero");
			return "redirect:/listarProcesos";
		}
		String strSentencia = "Se hizo la sentencia";
		
		proceso.setSentencia(true);
		proceso.setEstado(false);
		procesoService.save(proceso);
		
		flash.addFlashAttribute("success", "Ha sido dicatada la sentencia del proceso con radicado".concat(proceso.getRadicado()));
		
		return "redirect:/listarProcesos";
	}
	

//	private int page;
//	private Pageable pageRequest = PageRequest.of(page, 3);
//	private List<ProcesoUsuario> list = new ArrayList<ProcesoUsuario>();
//	private Page<ProcesoUsuario> procesosUsuarioListar = new PageImpl<>(list, pageRequest, list.size());
	private Usuario usuarioListar;
	private Page<ProcesoUsuario> procesosUsuarioListar;
	private Long idUsuario;
//	private Page<ProcesoUsuario> procesosUsuarioListarAsignar;
	
	@RequestMapping(value="/listarProcesosPorUsuario")
	public String listarProcesosPorUsuario(@RequestParam(value="idUsuario", required = false, defaultValue = "0")  Long idUsuario, @RequestParam(value="page", defaultValue = "0") int page, Model model, RedirectAttributes flash)
	{
		
		Pageable pageRequest = PageRequest.of(page, 3);
		Long id = getUserId();
		
		if(idUsuario > 0)
		{
			this.idUsuario = idUsuario;
		}
		
		boolean botonBandera = true;
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Juzgado juzgado = usuario.getJuzgado();
		//Usuario usuario = usuarioService.findOne(idUsuario);
		
//		if(idUsuario == null)
//		{
//			model.addAttribute("titulo", )
//		}
		
		System.out.println("EL ID DESDE EL ARGUMENTO ES: " + idUsuario);
		System.out.println("EL ID DESDE LA VARIABLE DE INSTANCIA ES: " + this.idUsuario);
		
		if(usuarioListar == null)
		{
			if(idUsuario.toString().contentEquals("0")) {
				System.out.println("EL DEFAULT VALUE ES CERO");
				usuarioListar = usuarioService.findOne(this.idUsuario);
			}
			else
			{
				System.out.println("EL DEFAULT VALUE NO ES CERO");
				usuarioListar = usuarioService.findOne(idUsuario);
			}
				
		}
		
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		List<Proceso> procesosAVencer = new ArrayList<Proceso>();
		List<Proceso> procesosAdmitir = new ArrayList<Proceso>();
		
//		if(procesosUsuarioListar.isEmpty()) {
//			procesosUsuarioListar = procesoUsuarioService.findAllById(idUsuario, pageRequest, juzgado);
//		}
		
		//System.out.println("procesosUsuarioListar está vacio?: " + procesosUsuarioListar.isEmpty());
		
//		if(procesosUsuarioListar == null)
//		{
//			System.out.println("SE HIZO LA CONSULTA DE PROCESOUSUARIOLISTAR PORQUE ES NULL");
//			procesosUsuarioListar = procesoUsuarioService.findAllById(idUsuario, pageRequest, juzgado);
//		}
		
		
		
		procesosUsuarioListar = procesoUsuarioService.findAllById(this.idUsuario, pageRequest, juzgado);
		
		
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/listarProcesosPorUsuario", procesosUsuarioListar);
		
		model.addAttribute("procesosAVencer", procesosAVencer);
		model.addAttribute("procesosAdmitir", procesosAdmitir);
		//model.addAttribute("usuario", usuario);
		model.addAttribute("usuarios", usuarios);
		model.addAttribute("botonBandera", botonBandera);
		model.addAttribute("procesos", procesosUsuarioListar);
		model.addAttribute("page", pageRender);
		model.addAttribute("titulo", "Listado de procesos del Usuario: ".concat(usuarioListar.getNombre().concat(usuarioListar.getApellido())));
		
		usuarioListar = null;
		
//		procesosUsuarioListarAsignar = procesosUsuarioListar;
		
//		procesosUsuarioListar = null;
		
		botonBandera = false;
		
		return "listarProcesos";
	}
	

	
	@RequestMapping(value="/formAsignarProcesos")
	public String formAsignarProceso(Model model)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		Juzgado juzgado = usuario.getJuzgado();
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		
		model.addAttribute("titulo", "Asignar procesos a usuario");
		model.addAttribute("usuarios", usuarios);
		
		return "formAsignarProceso";
	}
	
	@PostMapping(value="/asignarProcesos")
	public String asignarProcesos(@RequestParam(value="idUsuario") String idUsuario, RedirectAttributes flash)
	{
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		Long id = Long.parseLong(idUsuario);
		Usuario usuario = usuarioService.findOne(id);
		
		for(ProcesoUsuario proceso: procesosUsuarioListar)
		{
			proceso.setUsuario(usuario);
			procesoUsuarioService.save(proceso);
		}
		
		procesosUsuarioListar = null;
		
		flash.addFlashAttribute("success", "Los procesos fueron asignados al uuario: ".concat(usuario.getNombre().concat(usuario.getApellido())));
		
		
		return "redirect:/listarProcesos";
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
		
//		Page<ProcesoUsuario> procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest, juzgado);
		
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		model.addAttribute("usuarios", usuarios);
		
		
		Page<ProcesoUsuario> procesosUsuario = null;
		
		
		/* ========== bloque de codigo que valida los procesos que se listaran 
		 * dependiendo del tipo de usuario ====================================*/
		List<Rol> roles = rolService.findByUsuario(usuario);
		System.out.println("la lista roles es vacia: " + roles.isEmpty());
		
		String nombre = null; 
		
		for (Rol rol : roles)
		{
			
			System.out.println(rol.getRol());
			
			//nombre = rol.getRol();
			
			if(rol.getRol().contentEquals("ROLE_USER"))
			{
				System.out.println("rol desde el if por SUPER_USER: " + rol.getRol());
				nombre = rol.getRol();
				break;
			}
			else if(rol.getRol().contentEquals("ROLE_ADMIN"))
			{
				System.out.println("rol desde el if por ROLE_ADMIN: " + rol.getRol());
				nombre = rol.getRol();
				break;
			}
			else if(rol.getRol().contentEquals("ROLE_SUPER_ADMIN"))
			{
				System.out.println("rol desde el if por ROLE_SUPER_ADMIN: " + rol.getRol());
				nombre = rol.getRol();
				break;
			}
		}
		
		System.out.println("el rol es: " + nombre);
		
		if(nombre.contentEquals("ROLE_SUPER_ADMIN"))
		{
			System.out.println("se hace la consulta de todos los procesos activos de todos los juzgados");
			procesosUsuario = procesoUsuarioService.findAllBySuperAdmin(pageRequest);
		}
		else if(nombre.contentEquals("ROLE_ADMIN"))
		{
			System.out.println("se hace la consulta de los procesos por juzgado");
			/*se ejecuta la consulta de proceos por id y juzgado para que el admin tenga la opcion
			 * de filtrar los procesos por usuario */
			procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest, juzgado);
		}
		else if(nombre.contentEquals("ROLE_USER"))
		{
			System.out.println("se hace la consulta de los proceos por usuario");
			procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest, juzgado);
		}
		
		/*====== fin de bloque de codigo de validacion de los procesos que si listaran*/
		
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
		boolean subsanaDemanda = false;
		int dias = 0;
		int diasSubsanarDemanda = 0;
		boolean banderaSubsanaDemanda = false;
		int procesosAdmitidos =0;
		int procesosConSentencia = 0;
		String radicado = null;
		Long idProceso = null;
		
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
					
					//Calendar fechaInicial = detalle.getFechaInicial();
					Calendar fechaFinal = detalle.getFechaFinal();
					
					fechaActual.set(Calendar.HOUR, 0);
					fechaActual.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual.set(Calendar.MINUTE, 0);
					fechaActual.set(Calendar.SECOND, 0);
					
					fechaFinal.set(Calendar.HOUR, 0);
					fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal.set(Calendar.MINUTE, 0);
					fechaFinal.set(Calendar.SECOND, 0);
					
					
					
					
					long fechaFinalMS = fechaFinal.getTimeInMillis();
					long fechaActualMS = fechaActual.getTimeInMillis();
					
					//int dias = (int) ((Math.abs(fechaFinalMS - fechaActualMS)) / (1000 * 60 * 60* 24));
//					dias = (int) ((fechaFinalMS - fechaActualMS) / (1000 * 60 * 60* 24));
					
					
					dias = CalculaDiasHabiles.diasHabiles(fechaActual, fechaFinal);
					System.out.println("============= fecha inicial: " + fechaActual.getTime());
					System.out.println("============= fecha final: " + fechaFinal.getTime());
					System.out.println("============= numero días de admision: " + dias);
					
					if(proceso.getUltimaActuacion().equalsIgnoreCase("admitido")
							&& dias > 0)
					{ 
						System.out.println("ultima actuacion es: Admitido");
						estado = true;	
					}
					else if(proceso.getUltimaActuacion().equalsIgnoreCase("admitido") && dias == 0)
					{
						estado = false;
					}	
					//System.out.println("los terminos del proceso de nuevo son: "+ detalle.getTermino().getNombre());	
				}
				
				if(termino.getNombre().equalsIgnoreCase("subsanar demanda"))
				{
					Calendar fechaActual = Calendar.getInstance(timeZone, locale);
					Calendar fechaFinal = detalle.getFechaFinal();
					
					fechaActual.set(Calendar.HOUR, 0);
					fechaActual.set(Calendar.HOUR_OF_DAY, 0);
					fechaActual.set(Calendar.MINUTE, 0);
					fechaActual.set(Calendar.SECOND, 0);
					//long fechaActualMS = fechaActual.getTimeInMillis();
					
					fechaFinal.set(Calendar.HOUR, 0);
					fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal.set(Calendar.MINUTE, 0);
					fechaFinal.set(Calendar.SECOND, 0);
					//long fechaFinalMS = fechaFinal.getTimeInMillis();
					
//					diasSubsanarDemanda = (int) ((fechaFinalMS - fechaActualMS) / (1000 * 60 * 60* 24));
					diasSubsanarDemanda = CalculaDiasHabiles.diasHabiles(fechaActual, fechaFinal);
					System.out.println("los numero de días para subsana demanda son: " + diasSubsanarDemanda);
					
					if(diasSubsanarDemanda > 0)
					{
						banderaSubsanaDemanda = true;
					}
					
				}
					
				if(termino.getNombre().equalsIgnoreCase("notificacion demandado") && estado == true ) // && dias1 != 0 && dias1 == 30
				{
					System.out.println("si hay termino de notificacion demandado");
					System.out.println("Para el termino 121, Se está tomando desde la fecha de notificación al demandado");
					
					
					/*la fecha final cuando esté en producción la aplicación será esta, más 365
					 * días que es el año del 121  */
//					Calendar notificacionDemandado = Calendar.getInstance();
//					notificacionDemandado = detalle.getFechaFinal();
//					int mes = notificacionDemandado.get(Calendar.MONTH);
//					int dia = notificacionDemandado.get(Calendar.DATE);
//					int anio = notificacionDemandado.get(Calendar.YEAR);
					
					/*la fecha actual si está bien (es decir, zona horaria de colombia y zona local de bogotá, lo cual si es asi porqe por defecto trae esas zona horaria y zona local), 
					 * los calculos se hacen con la fecha final que 'es la de notificacion
					 * demandado + los 365 días' menos la fecha actual */
					fechaActual1 = Calendar.getInstance(timeZone, locale);
					
					/*para efectos de las pruebas, se establecio la fecha final de esta forma
					 * para que los días den 30 para que asi se pueda ejecutar la alarma y notificacion
					 * de 30 días antes del vencimiento de términos */
					fechaFinal1 = Calendar.getInstance(timeZone, locale);
					
					int dia = 28;
					int mes = 7;
					int anio = 2020;
					
					fechaFinal1.set(anio, mes, dia);
					System.out.println("la fecha final establecida para que de 30 es: " + fechaFinal1.getTime());
					
					


//					fechaFinal1.add(Calendar.DAY_OF_YEAR, 365);
//					System.out.println("fecha final + 365: " + fechaFinal1.getTime());
					
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
				
				else if(termino.getNombre().equalsIgnoreCase("notificacion demandado") && estado == false)
				{
					System.out.println("Para el termino 121, Se está tomando desde la fecha de REPARTO");
					
					Calendar fechaReparto = Calendar.getInstance(timeZone, locale);
					fechaReparto = proceso.getFechaReparto();
					int dia = fechaReparto.get(Calendar.DATE);
					int mes = fechaReparto.get(Calendar.MONTH);
					int anio = fechaReparto.get(Calendar.YEAR);
					
					fechaActual1 = Calendar.getInstance(timeZone, locale);
					fechaFinal1 = Calendar.getInstance(timeZone, locale);
					
//					int dia = 29;
//					int mes = 6;
//					int año = 2020;
					
					fechaFinal1.set(anio, mes, dia);
					fechaFinal1.add(Calendar.DAY_OF_YEAR, 365);
					
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
			
			System.out.println("LA BANDERA1 ES: " + bandera1);
			
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
				proceso.setEstadoActual("Por admitir");
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
				proceso.setSentenciaBandera(true);
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
//				model.addAttribute("vence", "Listado de procesos Cercanos a Vencer en " +
//				dias1 + " días.");
				model.addAttribute("procesosAVencer", procesosAVencer);
				model.addAttribute("bandera", bandera);
				
				
			}
			if(dias1 <= 30 && dias1 > 1)
			{
				proceso.setProrroga(true);
				procesoService.save(proceso);
				//model.addAttribute("bandera", bandera);
			}
			
			//
			dias1 = 0;
			
			/*============== fin de codigo de alarma de 30 dias para el vencimiento de terminos ======*/
			
			/*========= CODIGO PARA LOS DIAS DE SUBSANAR DEMANDA ============= */
			
//			if(diasSubsanarDemanda =< 0)
//			{
//				proceso.setUltimaActuacion("inadmitido");
//				proceso.setEstado(false);
//				procesoService.save(proceso);
//				flash.addFlashAttribute("error", "El proceso con radicado: ".concat(proceso.getRadicado()) + " se ha inadmitido " +
//				"porque no se subsanó la demanda");
//				return "redirect:/listarProcesos";
//			}
//			diasSubsanarDemanda = 0;
			
			if(diasSubsanarDemanda <= 15 && diasSubsanarDemanda > 0)
			{
				radicado = proceso.getRadicado();
				idProceso = proceso.getId();
				subsanaDemanda = true;
				if(!proceso.getUltimaActuacion().equalsIgnoreCase("admitido"))
				{
					proceso.setEstadoActual("Por subsanar demanda");
				}
				
				procesoService.save(proceso);
				
			}
			
//			if(diasSubsanarDemanda == 0 && banderaSubsanaDemanda == true)
//			{
//				proceso.setUltimaActuacion("inadmitido");
//				proceso.setEstado(false);
//				procesoService.save(proceso);
//				flash.addFlashAttribute("error", "El proceso con radicado: ".concat(proceso.getRadicado()) + " se ha inadmitido " +
//				"porque no se subsanó la demanda");
//				return "redirect:/listarProcesos";
//			}
			diasSubsanarDemanda = 0;
			/*========= FIN DE CODIGO PARA SUBSANAR DEMANDA =====================*/
			
		}
		
		if(subsanaDemanda)
		{
			flash.addFlashAttribute("warning", "El demandante del proceso numero: " + idProceso + " con radicado: " + radicado + " debe subsanar la demanda");
//			return "redirect:/listarProcesos";
		}
		subsanaDemanda = false;
		//estado = false;
		//List<DetalleTermino> detalleTerminos = proceso.getDetalleTerminos();
		
		/*========== Bloque de codigo para vaciar la lista ProcesosAdmitir y asi no mostrar
		 * en la vista de listar procesos =================*/
		int procesoAAdmitir = procesosAdmitir.size();
		System.out.println("numero procesos a admitir: " + procesoAAdmitir);
		
		for(Proceso proceso: procesosAdmitir)
		{	System.out.println("admitir proceso: " + proceso.getUltimaActuacion());
			
			if(proceso.getUltimaActuacion().contentEquals("admitido"))
			{
				procesosAdmitidos++;
			}
		}
		System.out.println("Numero de procesos admitidos: " + procesosAdmitidos);
		
		if(procesoAAdmitir == procesosAdmitidos)
		{
			System.out.println("son iguales");
			procesosAdmitir.clear();
			model.addAttribute("procesosAdmitir", procesosAdmitir);
		}
		
		/* ======================= fin de bloque de codigo ====================================== */
		
		/*========== Bloque de codigo para vaciar la lista ProcesosAVencer y asi no mostrar
		 * en la vista de listar procesos =================*/
		
		int procesoASentencia = procesosAVencer.size();
		System.out.println("numero procesos a vencer: " + procesoASentencia);
		
		
		 /*
		  * se puede dejar solo un boton para dictar sentencia, y que este guarde en 
		  * el campo sentencia la palabra 'sentencia', luego se puede validar si el proceso
		  * contiene en el campo sentencia, la palabra sentencia para sumarle al contador
		  */
		
		for(Proceso proceso: procesosAVencer)
		{
			System.out.println("sentencia proceso: " + proceso.isSentencia());
			if(proceso.isSentencia())
			{
				procesosConSentencia++;
			}
		}
		System.out.println("Numero de procesos con sentencia: " + procesosConSentencia);
		
		if(procesoASentencia == procesosConSentencia)
		{
			System.out.println("son iguales");
			procesosAVencer.clear();	
			model.addAttribute("procesosAVencer", procesosAVencer);
		}
		
		/* ======================= fin de bloque de codigo ====================================== */
		
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
	
	@RequestMapping(value ="/subsanarDemanda/{id}")
	public String subsanarDemanda(@PathVariable(value="id") Long id, RedirectAttributes flash, Model model)
	{
		Proceso proceso = procesoService.findOne(id);
		proceso.setEstadoActual("demanda subsanada");
		proceso.setUltimaActuacion("admitido");
		procesoService.save(proceso);
		
		flash.addFlashAttribute("success", "El proceso con radicado: ".concat(proceso.getRadicado()+  " y ID #" + proceso.getId() +" subsanó la demanda y fue admitido con éxito"));
		
		return "redirect:/listarProcesos";
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
			proceso.setEstadoActual("");
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
	
	@RequestMapping(value="/inadmitirProceso/{id}")
	public String inadmitirProceso(@PathVariable(value="id") Long id, RedirectAttributes flash)
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
			
			proceso.setUltimaActuacion("inadmitido");
			proceso.setEstado(false);
			procesoService.save(proceso);
			flash.addFlashAttribute("success", "El proceso con el radiaco: "+ radicado+ " fue INADMITIDO y DESACTIVADO");
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
	public String guardar(@Valid Proceso proceso, BindingResult result, @RequestParam(value = "usuario") String usuario,  Model model, RedirectAttributes flash, SessionStatus status)
	{
		Usuario usuarioLogeado = usuarioService.findOne(getUserId());
		Long idJuzgado = usuarioLogeado.getJuzgado().getId();
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		
		List<Usuario> usuarios = usuarioService.findByJuzgado(juzgado);
		
		/* si al validar los campos, estos contienen errores se envia al formulario de
		 * crear proceso de nuevo para que se corrijan los errores*/
		/* por eso se agrega otro parametro al metodo guardar llamado BindingResult, este
		 * parametro siempre va junto al objeto que esta mapeado al formulario, en este
		 * caso, el objeto proceso */
		if (result.hasErrors())
		{
			/* se debe pasar nuevamente el titulo a la vista*/
			model.addAttribute("titulo", "Crear Proceso");
			model.addAttribute("usuarios", usuarios);
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