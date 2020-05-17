package com.usco.esteban.agenda_procesos.app.controllers;



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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.TipoProcesoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.dao.IUsuarioDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoService;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoUsuarioService;
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
	
	
	@GetMapping(value = "/verTerminosProceso/{id}")
	public String verTerminos(@PathVariable(name ="id") Long id, Map<String, Object> model, RedirectAttributes flash)
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
	
	@RequestMapping(value = "/listarProcesos", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 3);
		
		Long id = getUserId();
		//Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		Page<ProcesoUsuario> procesosUsuario = procesoUsuarioService.findAllById(id, pageRequest);
		
		PageRender<ProcesoUsuario> pageRender = new PageRender<>("/listarProcesos", procesosUsuario); 
		
		//System.out.println(procesosUsuario.isEmpty());
		//System.out.println(procesosUsuario.getNumberOfElements());
		
		for (ProcesoUsuario procesoUsuario : procesosUsuario) {
			System.out.println(procesoUsuario.getProceso().getRadicado());
		}
		
		
		
		model.addAttribute("titulo", "Listado de procesos");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesosUsuario);
		model.addAttribute("page", pageRender);
		return "listarProcesos";
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
	
	@RequestMapping(value="/formProceso/{id}")
	public String editar(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Proceso proceso = null;
		
		/* si el id es mayor que cero se busca en la base de datos*/
		if(id > 0)
		{
			/* si lo encuentra se almacena*/
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
			/* si no se encuentra se redirige al listado */
			return "redirect:/listarProcesos";
		}
		model.put("proceso", proceso);
		model.put("titulo", "Editar Proceso");
		model.put("tipoProcesos", tipoProcesoDao.findAll());
		model.put("juzgados", juzgadoService.findAll());
		
		
		
		return "formProceso";
	}
	
	@RequestMapping(value = "/guardarProceso")
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
		
		String mensajeFlash = (proceso.getId() != null) ? "Proceso editado con exito" : "Proceso creado con exito";
		
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
		
		ProcesoUsuario procesoUsuario = new ProcesoUsuario();
		procesoUsuario.setProceso(proceso);
		procesoUsuario.setUsuario(usu);
		
		procesoUsuarioService.save(procesoUsuario);
		
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

