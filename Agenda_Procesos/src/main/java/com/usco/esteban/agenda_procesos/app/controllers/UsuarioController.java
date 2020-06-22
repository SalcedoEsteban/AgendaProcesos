package com.usco.esteban.agenda_procesos.app.controllers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.JuzgadoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Rol;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IHistorialUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;
import com.usco.esteban.agenda_procesos.app.models.service.IRolService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;

@Controller
@SessionAttributes("usuario")
public class UsuarioController {

	@Autowired
	private IJuzgadoService juzgadoService;

	@Autowired
	private JuzgadoPropertyEditor juzgadoEditor;

	@Autowired
	private IUsuarioService usuarioService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private IHistorialUsuarioService historialUsuarioService;
	
	@Autowired
	private IRolService rolService;

	private Juzgado juzgado;
	
	private Usuario usuario;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Juzgado.class, "juzgado", juzgadoEditor);
	}

	@RequestMapping(value = "/listarUsuarios")
	public String listarUsuarios(Map<String, Object> model) {
		
		/*Se obtiene el usuario logeado*/
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		UserDetails userDetail = (UserDetails) auth.getPrincipal();
		usuario = this.usuarioService.findByUsername(userDetail.getUsername());
		
		/* Se obtiene el juzgado del usuario logueado*/
		Juzgado juzgado = usuario.getJuzgado();
		
		/*se obtienen los roles del usuario*/
		List<Rol> roles = rolService.findByUsuario(usuario);
		
		System.out.println("la lista roles es vacia: " + roles.isEmpty());
		
		
		
		String nombre = null;
		List<Usuario> usuarios = null; 
		
		for (Rol rol : roles) {
			
			System.out.println(rol.getRol());
			
			//nombre = rol.getRol();
			
			if(rol.getRol().contentEquals("ROLE_SUPER_ADMIN"))
			{
				System.out.println("rol desde el if por SUPER_ADMIN: " + rol.getRol());
				nombre = rol.getRol();
			}else if(rol.getRol().contentEquals("ROLE_ADMIN"))
			{
				System.out.println("rol desde el if por ROLE_ADMIN: " + rol.getRol());
				nombre = rol.getRol();
			}
		}
		
		System.out.println("el rol es: " + nombre);
		
		if(nombre.contentEquals("ROLE_SUPER_ADMIN"))
		{
			System.out.println("se hace la consulta de todos los usuarios");
			usuarios = usuarioService.findAll();
		}
		else if(nombre.contentEquals("ROLE_ADMIN"))
		{
			System.out.println("se hace la consulta de usuario por juzgado");
			usuarios = usuarioService.findByJuzgado(juzgado);
		}
		 
		
		model.put("titulo", "Listado de Usuarios");
		model.put("usuarios", usuarios);

		return "usuario/listarUsuarios";
	}

	@RequestMapping(value = "/formUsuario")
	public String crearUsuario(Map<String, Object> model) {
		Usuario usuario = new Usuario();

		model.put("usuario", usuario);
		model.put("juzgados", juzgadoService.findAll());
		model.put("titulo", "Crear Usuario");

		return "usuario/formUsuario";
	}

	@RequestMapping(value = "/editarUsuario/{id}")
	public String editarUsuario(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Usuario usuario = null;

		if (id > 0) {
			usuario = usuarioService.findOne(id);

			if (usuario == null) {
				flash.addFlashAttribute("error", "El usuario no existe");
				return "redirect:/listarUsuarios";
			}
		} else {
			flash.addFlashAttribute("error", "El id del usuario no puede ser cero");
			return "redirect:/listarUsuarios";
		}

		/*this.juzgado = usuario.getJuzgado();
		
		System.out.println("el juzgado desde editar es: ".concat(this.juzgado.getNombre()));*/
		
		

		model.addAttribute("usuario", usuario);
		model.addAttribute("titulo", "Editar Usuario");
		model.addAttribute("juzgados", juzgadoService.findAll());

		return "usuario/formUsuario";
	}

	//Usuario usuario1 = null;
	boolean bandera = false;
	
	/*@RequestMapping(value = "/administrarUsuario/{id}")
	public String editarUsuarioHistorial(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Usuario usuario1 = null;

		if (id > 0) {
			usuario1 = usuarioService.findOne(id);

			if (usuario1 == null) {
				flash.addFlashAttribute("error", "El usuario no existe");
				return "redirect:/listarUsuarios";
			}
		} else {
			flash.addFlashAttribute("error", "El id del usuario no puede ser cero");
			return "redirect:/listarUsuarios";
		}

		/*this.juzgado = usuario1.getJuzgado();
		System.out.println("el juzgado desde editar es: ".concat(this.juzgado.getNombre()));
		
		Date fechaIngreso = usuario1.getCreateAt();

		model.addAttribute("usuario", usuario1);
		model.addAttribute("titulo1", "Salida Usuario");
		model.addAttribute("titulo", "Historial Usuario");
		//model.addAttribute("titulo2", "Nuevo Historial");
		model.addAttribute("fechaIngreso", fechaIngreso);
		model.addAttribute("bandera", bandera);
		//model.addAttribute("juzgados", juzgadoService.findAll());
		//model.addAttribute("juzgados", juzgadoService.findAll());

		return "usuario/adminUsuario";
	}*/
	
	
	@RequestMapping(value = "/administrarUsuario/{id}")
	public String adminUsuario(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {

		Usuario usuario = null;

		if (id > 0) {
			usuario = usuarioService.findOne(id);

			if (usuario == null) {
				flash.addFlashAttribute("error", "El usuario no existe");
				return "redirect:usuario/listarUsuarios";
			}
		} else {
			flash.addFlashAttribute("error", "El id del usuario no puede ser cero");
			return "redirect:usuario/listarUsuarios";
		}

		/*this.juzgado = usuario1.getJuzgado();
		System.out.println("el juzgado desde editar es: ".concat(this.juzgado.getNombre()));*/
		
		//Date fechaIngreso = usuario.getCreateAt();
		
		
		model.addAttribute("usuario", usuario);
		//model.addAttribute("fechaIngreso", fechaIngreso);
		//model.addAttribute("titulo", "Salida Usuario");
		
		
		return "usuario/adminUsuario";

		
	}
	
	private Date fechaIngreso = null;
	private HistorialUsuario historial = null;
	
	@RequestMapping(value="/salidaUsuario/{id}")
	public String salidaUsuario(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Usuario usuario = null;
		
		if(id > 0)
		{
			usuario = usuarioService.findOne(id);
			
			if(usuario == null)
			{
				flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
				return "redirect:/listarUsuarios";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID no puede ser Cero");
			return "redirect:/listarUsuarios";
		}
		
		Date fechaIngreso = usuario.getCreateAt();
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		
		model.put("usuario", usuario);
		model.put("fechaIngreso", fechaIngreso);
		model.put("titulo", "Salida Usuario");
		
		
		return "usuario/salidaUsuario";
	}
	
	@RequestMapping(value="/entradaUsuario/{id}")
	public String entradaUsuario(@PathVariable(value="id") Long id, Map<String, Object> model, RedirectAttributes flash)
	{
		Usuario usuario = null;
		
		if(id > 0)
		{
			usuario = usuarioService.findOne(id);
			
			if(usuario == null)
			{
				flash.addFlashAttribute("error", "El usuario no existe en la base de datos");
				return "redirect:/listarUsuarios";
			}
		}
		else
		{
			flash.addFlashAttribute("error", "El ID no puede ser Cero");
			return "redirect:/listarUsuarios";
		}
		
		Date fechaIngreso = usuario.getCreateAt();
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		
		model.put("usuario", usuario);
		//model.put("fechaIngreso", fechaIngreso);
		model.put("juzgados", juzgadoService.findAll());
		model.put("titulo", "Entrada Usuario");
		
		
		return "usuario/entradaUsuario";
	}
	
	/*@RequestMapping(value="/guardarSalidaUsuario/{id}", method = RequestMethod.POST)
	public String guardarSalidaHistorial(@PathVariable(value="id") Long id, @RequestParam(name="fechaSalida") Date fechaSalida, Model model)
	{
		Usuario usuario = usuarioService.findOne(id);
		if(this.fechaIngreso == null)
		{
			fechaIngreso = usuario.getCreateAt();
		}
		
		System.out.println("la fecha ingreso es: " + fechaIngreso);
		
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		Long idHistorial = (long) 0;
		List<HistorialUsuario> hist = usuario.getHistorialUsuarios();
		for(HistorialUsuario historial: hist)
		{
			if(historial.getId() == idHistorial)
			{
				
			}
		}
		Date fechaSal = fechaSalida;
		
		historial = historialUsuarioService.findByUsuarioAndEspecialidadAndFechaIngreso(usuario, especialidad, fechaIngreso);
		
		
		historial.setFechaSalida(fechaSal);
		historialUsuarioService.save(historial);
		
		
		System.out.println("la fecha salida es: " + fechaSalida);
		System.out.println("El id del historial es: " + historial.getId());
		//this.fechaIngreso = null;
		//idHistorial = historial.getId();
		//System.out.println("el id del historial es: " + idHistorial);
		
		bandera = true;
		
		model.addAttribute("fechaIngreso", fechaIngreso);
		model.addAttribute("titulo1", "Salida Usuario");
		model.addAttribute("titulo", "Historial Usuario");
		model.addAttribute("titulo2", "Nuevo Historial");
		//model.addAttribute("historial", historial);
		model.addAttribute("bandera", bandera);
		model.addAttribute("juzgados", juzgadoService.findAll());
		return "usuario/adminUsuario";
	}*/
	
	@RequestMapping(value="/guardarSalidaUsuario/{id}", method = RequestMethod.POST)
	public String guardarSalidaHistorial(@PathVariable(value="id") Long id, @RequestParam(name="fechaSalida") Date fechaSalida, Model model, RedirectAttributes flash)
	{
		Usuario usuario = usuarioService.findOne(id);
		
		if(this.fechaIngreso == null)
		{
			fechaIngreso = usuario.getCreateAt();
		}
		
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		//Date fechaSal = fechaSalida;
		System.out.println("la fecha ingreso es: " + fechaIngreso);
		
		
		
		HistorialUsuario historial = historialUsuarioService.findByUsuarioAndEspecialidadAndFechaIngreso(usuario, especialidad, fechaIngreso);
		
		historial.setFechaSalida(fechaSalida);
		historialUsuarioService.save(historial);
		
		
		System.out.println("la fecha salida es: " + fechaSalida);
		System.out.println("El id del historial es: " + historial.getId());
		
		
		bandera = true;
		
		flash.addFlashAttribute("succes", "El usuario ya no pertenece al juzgado, salió el: " + fechaSalida);
		
		return "redirect:/listarUsuarios";
	}
	
	/*@RequestMapping(value="/guardarEntradaUsuario/{id}", method = RequestMethod.POST )
	public String guardarEntradaHistorial(@PathVariable(value="id")Long id, @RequestParam(name="fechaIngreso") Date fechaIngreso, @RequestParam(name="idJuzgado") Long idJuzgado, Model model, RedirectAttributes flash)
	{
		Usuario usuario = usuarioService.findOne(id);
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		System.out.println("la especialidad es: " + especialidad.getNombre());
		String nombreJuzgado = juzgado.getNombre();
		
		System.out.println("el juzgado es: "+ nombreJuzgado);
		
		usuario.setJuzgado(juzgado);	
		usuarioService.save(usuario);
		
		String descripcion = "Usuario Trasladado el: " + fechaIngreso + ", pertenece a la especialidad "
				+ especialidad.getNombre() + " y al juzgado: " + nombreJuzgado;
		
		this.historial = historialUsuarioService.findByUsuarioAndEspecialidadAndFechaIngreso(usuario, especialidad, fechaIngreso);
		this.fechaIngreso = fechaIngreso;
		
		HistorialUsuario historial = new HistorialUsuario();
		historial.setUsuario(usuario);
		historial.setDescripcion(descripcion);
		historial.setEspecialidad(especialidad);
		historial.setFechaIngreso(fechaIngreso);
		
		historialUsuarioService.save(historial);
		
		
		bandera = false;
		
		model.addAttribute("fechaIngreso", this.fechaIngreso);
		//model.addAttribute("titulo1", "Salida Usuario");
		//model.addAttribute("titulo", "Historial Usuario");
		//model.addAttribute("titulo2", "Nuevo Historial");
		//model.addAttribute("historial", historial);
		//model.addAttribute("bandera", bandera);
		flash.addFlashAttribute("success", "El usuario fue trasladado de especialidad con exito");
		
		
		
		return "redirect:/listarUsuarios";
	}*/
	
	@RequestMapping(value="/guardarEntradaUsuario/{id}", method = RequestMethod.POST )
	public String guardarEntradaHistorial(@PathVariable(value="id")Long id, @RequestParam(name="fechaIngreso") Date fechaIngreso, @RequestParam(name="idJuzgado") Long idJuzgado, Model model, RedirectAttributes flash)
	{
		Usuario usuario = usuarioService.findOne(id);
		Juzgado juzgado = juzgadoService.findOne(idJuzgado);
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		System.out.println("la especialidad es: " + especialidad.getNombre());
		String nombreJuzgado = juzgado.getNombre();
		
		System.out.println("el juzgado es: "+ nombreJuzgado);
		
		usuario.setJuzgado(juzgado);	
		usuarioService.save(usuario);
		
		String descripcion = "Usuario Trasladado el: " + fechaIngreso + ", pertenece a la especialidad "
				+ especialidad.getNombre() + " y al juzgado: " + nombreJuzgado;
		
		//HistorialUsuario historial = historialUsuarioService.findByUsuarioAndEspecialidadAndFechaIngreso(usuario, especialidad, fechaIngreso);
		this.fechaIngreso = fechaIngreso;
		
		HistorialUsuario historial = new HistorialUsuario();
		historial.setUsuario(usuario);
		historial.setDescripcion(descripcion);
		historial.setEspecialidad(especialidad);
		historial.setFechaIngreso(fechaIngreso);
		historialUsuarioService.save(historial);
		
		
		bandera = false;
		
		//model.addAttribute("fechaIngreso", this.fechaIngreso);
		flash.addFlashAttribute("success", "El usuario fue trasladado de especialidad con exito, ahora pertenece a: " + especialidad.getNombre());
		
		
		
		return "redirect:/listarUsuarios";
	}
	
	@RequestMapping(value = "/guardarUsuario")
	public String guardarUsuario(Model model, Usuario usuario, SessionStatus status, RedirectAttributes flash) {

		String ps = usuario.getPassword();
		String bycryptPassword = passwordEncoder.encode(ps);
		usuario.setPassword(bycryptPassword);
		System.out.println("La contraseña encriptada es: ".concat(bycryptPassword));

		// se guardar el usuario
		
		usuarioService.save(usuario);
		flash.addFlashAttribute("success", "El usuario fue guardado con éxito");
		

		/* ======================================================================= */
		/* Bloque de codigo para crear HistorialUsuario */
		HistorialUsuario historialUsuario = new HistorialUsuario();

		String descripcion = "Usuario creado el: " + usuario.getCreateAt() + ", pertenece a la especialidad "
				+ usuario.getJuzgado().getEspecialidad().getNombre() + " y al juzgado: "
				+ usuario.getJuzgado().getNombre();

		
		/*Calendar calendar = Calendar.getInstance();
		calendar.set(2018, 11, 31);
		Date fechaIngreso = calendar.getTime();*/
		Date fechaIngreso = usuario.getCreateAt();
		System.out.println("la fecha ingreso es: "+ fechaIngreso);
		// Date fechaIngreso = StringToDate("10-Jan-2016");
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();

		historialUsuario.setDescripcion(descripcion);
		historialUsuario.setFechaIngreso(fechaIngreso);
		historialUsuario.setEspecialidad(especialidad);
		historialUsuario.setUsuario(usuario);

		historialUsuarioService.save(historialUsuario);
		//this.fechaIngreso = historialUsuario.getFechaIngreso();
		//this.fechaIngreso = null;
		//this.juzgado = usuario.getJuzgado();

		/* ======================================================================= */

		/*if (usuario.getJuzgado() != this.juzgado)
		{
			Date fechaIngreso1 = historialUsuario.getFechaIngreso();
			Date fechaIngreso1 = new Date(9-07-2019) ;*/

			/*Long id = usuario.getId();
			HistorialUsuario historialUsuario1 = historialUsuarioService.findByUsuarioAndFechaIngreso(usuario,
					fechaIngreso1);
			System.out.println("el id del historial usuario es: " + historialUsuario1.getId());
			historialUsuario1.setFechaSalida(new Date());
			historialUsuarioService.save(historialUsuario1);

			HistorialUsuario historialUsuario2 = new HistorialUsuario();

			String descripcion1 = "Usuario creado el: " + usuario.getCreateAt() + ", pertenece a la especialidad "
					+ usuario.getJuzgado().getEspecialidad().getNombre() + " y al juzgado: "
					+ usuario.getJuzgado().getNombre();
			Date fechaIngreso1 = usuario.getCreateAt();
			Especialidad especialidad = usuario.getJuzgado().getEspecialidad();

			historialUsuario2.setFechaSalida(new Date());
		}*/

		// usuarioService.save(usuario);
		status.setComplete();

		return "redirect:/listarUsuarios";
	}
	
	
	public Date StringToDate(String s){

	    Date result = null;
	    try{
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	        result  = dateFormat.parse(s);
	    }

	    catch(ParseException e){
	        e.printStackTrace();

	    }
	    return result ;
	}
	
	@RequestMapping(value="/desactiActiUsuario/{id}")
	public String desactivarUsuario(@PathVariable(value ="id") Long id, RedirectAttributes flash)
	{
		Usuario usuario = null;
		
		if(id > 0)
		{
			usuario = usuarioService.findOne(id);
			
			if(usuario == null)
			{
				flash.addFlashAttribute("warning", "El usuario no existe");
				return "redirect:/listarUsuarios";
			}
			
			if(usuario.isEnabled())
			{
				usuario.setEnabled(false);
				usuarioService.save(usuario);
				flash.addFlashAttribute("success", "El usuario ha sido desactivado con exito");
				//return "redirect:/listarUsuarios";
			}
			else if(!usuario.isEnabled())
			{
				usuario.setEnabled(true);
				usuarioService.save(usuario);
				flash.addFlashAttribute("success", "El usuario ha sido Activado con exito");
				//return "redirect:/listarUsuarios";
			}	
		}
		else
		{
			flash.addFlashAttribute("error", "El ID del usuario no puede ser cero");
			return "redirect:/listarUsuarios";
		}
		
		return "redirect:/listarUsuarios";
	}
}
