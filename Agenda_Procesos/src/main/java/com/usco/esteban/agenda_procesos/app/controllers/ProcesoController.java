package com.usco.esteban.agenda_procesos.app.controllers;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
		
		/*String nombre = null;
		List<DetalleTermino> detalleTerminos = proceso.getDetalleTerminos();
		
		for(DetalleTermino detalle: detalleTerminos)
		{
			if(detalle.getTermino().getNombre().contentEquals("admision"))
			{
				Calendar calendar = Calendar.getInstance();
				
				Calendar fechaInicial = detalle.getFechaInicial();
				Calendar fechaFinal = detalle.getFechaFinal();
				
				calendar.set(Calendar.HOUR, 0);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				
				fechaFinal.set(Calendar.HOUR, 0);
				fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
				fechaFinal.set(Calendar.MINUTE, 0);
				fechaFinal.set(Calendar.SECOND, 0);
				
				
				
				long fechaIncialMS = fechaInicial.getTimeInMillis();
				long fechaFinalMS = fechaFinal.getTimeInMillis();
				long fechaActualMS = calendar.getTimeInMillis();
				
				int dias = (int) ((Math.abs(fechaFinalMS - fechaActualMS)) / (1000 * 60 * 60* 24));
				
				if(dias == 15)
				{
					System.out.println("dias igual a 15");
					flash.addFlashAttribute("error", "el termino " + detalle.getTermino().getNombre() + "está a punto de vencer");
					return "redirect:/listarProcesos";
				}
				
				
			}
		}*/
		
		
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
		
		
		
		/*lista de procesos que estarn cercanos a vencer*/
		List<Proceso> procesosAVencer = new ArrayList<Proceso>();
		for (ProcesoUsuario procesoUsuario : procesosUsuario) {
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
					System.out.println("contiene el termino 121");
				}else
				{
					this.terminoAnio = false;
				}
				
				if(termino.getNombre().contentEquals("admision"))
				{
					System.out.println("si contiene admision en el detalle termino");
					
					Calendar calendar = Calendar.getInstance();
					
					Calendar fechaInicial = detalle.getFechaInicial();
					Calendar fechaFinal = detalle.getFechaFinal();
					
					calendar.set(Calendar.HOUR, 0);
					calendar.set(Calendar.HOUR_OF_DAY, 0);
					calendar.set(Calendar.MINUTE, 0);
					calendar.set(Calendar.SECOND, 0);
					
					fechaFinal.set(Calendar.HOUR, 0);
					fechaFinal.set(Calendar.HOUR_OF_DAY, 0);
					fechaFinal.set(Calendar.MINUTE, 0);
					fechaFinal.set(Calendar.SECOND, 0);
					
					
					
					//long fechaIncialMS = fechaInicial.getTimeInMillis();
					long fechaFinalMS = fechaFinal.getTimeInMillis();
					long fechaActualMS = calendar.getTimeInMillis();
					
					//int dias = (int) ((Math.abs(fechaFinalMS - fechaActualMS)) / (1000 * 60 * 60* 24));
					int dias = (int) ((fechaFinalMS - fechaActualMS) / (1000 * 60 * 60* 24));
					System.out.println("numero días de admision: " + dias);
					
					
					if(proceso.getUltimaActuacion().equalsIgnoreCase("admitido")
							&& dias > 0)
					{ 
						System.out.println("ultima actuacion es: Admitido");
						estado = true;
						
						
					}
					/*else
					{
						estado = false;
					}*/
					
					
					
					//System.out.println("los terminos del proceso de nuevo son: "+ detalle.getTermino().getNombre());
					
					
					
					/*===== aqui va el codigo de dias = 15* ========/
					
					
					/*=================*/
					
					
				}
					
				if(termino.getNombre().equalsIgnoreCase("notificacion demandado") && estado == true)
				{
					System.out.println("si hay termino de notificacion demandado");
					
					//proceso = procesoUsuario.getProceso();
					
					Calendar notificacionDemandado = detalle.getFechaFinal();
					fechaActual1 = Calendar.getInstance();
					
					fechaFinal1 = Calendar.getInstance();
					
					int dia = 5;
					int mes = 7;
					int año = 2020;
					
					fechaFinal1.set(año, mes-1, dia);
					System.out.println("la fecha final establecida para que de 30 es: " + fechaFinal1.getTime());
					
					
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
					
					
					
					
					//dias1 = 0;
					
					//terminoNotificacionDemandado = false;
				}
				
				//boolean bandera1 = false;
				
				
				
				
				
				
				
				
			}
			System.out.println("dias para el vencimiento 121 son: " + dias1);
			if(dias1 == 30)
			{
				
				//Proceso proceso1 = procesoUsuario.getProceso();
				procesosAVencer.add(proceso);
				bandera = true;
				System.out.println("el radicado es "+ proceso.getRadicado());
				
				/*bloque de codido para crear la alarma del proceso*/
				/*String descripcion = "Vencimiento año";
				alarma.setProceso(proceso);
				alarma.setDescripcion(descripcion);
				alarmaService.save(alarma);	*/
						
					
				proceso.setPrioritario(true);
				procesoService.save(proceso);
					
					//this.bandera = false;
				
				
				//System.out.println("dias igual a 30");
				model.addAttribute("vence", "Listado de procesos Cercanos a Vencer");
				model.addAttribute("procesosAVencer", procesosAVencer);
				
			}
			
			if(bandera == true)
			{
				/*bloque de codido para crear la alarma del proceso*/
				//Proceso proceso = procesoUsuario.getProceso();
				//String descripcion = "Alarma de: ".concat("Vencimiento año");
				String descripcion = "Vencimiento año";
				System.out.println(alarmas.isEmpty() + " alarmas está vacio");
			
				
				if(alarmas.isEmpty())
				{
					System.out.println("el proceso NO contiene el vencimiento año");
					alarma.setProceso(proceso);
					alarma.setDescripcion(descripcion);
					alarmaService.save(alarma);
						
				}
				else
				{
					System.out.println("el proceso contiene el vencimiento de año");
				}
				guardarDetalleTermino();
			}
			bandera = false;
			dias1 = 0;
			
		}
		//estado = false;
		//List<DetalleTermino> detalleTerminos = proceso.getDetalleTerminos();
		
		
		
		/* ============================================================= */
		
		
		model.addAttribute("titulo", "Listado de procesos");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		return "listarProcesos";
	}
	
	public void guardarDetalleTermino()
	{
		if(this.terminoAnio == false)
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

