package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.editors.JuzgadoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.dao.IJuzgadoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IHistorialUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;
import com.usco.esteban.agenda_procesos.app.models.service.IUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.JpaUsuarioDetailsService;

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

	private Juzgado juzgado;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.registerCustomEditor(Juzgado.class, "juzgado", juzgadoEditor);
	}

	@RequestMapping(value = "/listarUsuarios")
	public String listarUsuarios(Map<String, Object> model) {
		model.put("titulo", "Listado de Usuarios");
		model.put("usuarios", usuarioService.findAll());

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
				return "redirect:usuario/listarUsuarios";
			}
		} else {
			flash.addFlashAttribute("error", "El id del usuario no puede ser cero");
			return "redirect:usuario/listarUsuarios";
		}

		this.juzgado = usuario.getJuzgado();
		
		System.out.println("el juzgado desde editar es: ".concat(this.juzgado.getNombre()));
		
		

		model.addAttribute("usuario", usuario);
		model.addAttribute("titulo", "Editar Usuario");
		model.addAttribute("juzgados", juzgadoService.findAll());

		return "usuario/formUsuario";
	}

	@RequestMapping(value = "/guardarUsuario")
	public String guardarUsuario(Model model, Usuario usuario, SessionStatus status) {

		String ps = usuario.getPassword();
		String bycryptPassword = passwordEncoder.encode(ps);
		usuario.setPassword(bycryptPassword);
		System.out.println("La contraseña encriptada es: ".concat(bycryptPassword));

		// se guardar el usuario
		usuarioService.save(usuario);

		/* ======================================================================= */
		/* Bloque de codigo para crear HistorialUsuario */
		HistorialUsuario historialUsuario = new HistorialUsuario();

		String descripcion = "Usuario creado el: " + usuario.getCreateAt() + ", pertenece a la especialidad "
				+ usuario.getJuzgado().getEspecialidad().getNombre() + " y al juzgado: "
				+ usuario.getJuzgado().getNombre();

		Date fechaIngreso = usuario.getCreateAt();
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();

		historialUsuario.setDescripcion(descripcion);
		historialUsuario.setFechaIngreso(fechaIngreso);
		historialUsuario.setEspecialidad(especialidad);
		historialUsuario.setUsuario(usuario);

		historialUsuarioService.save(historialUsuario);
		
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
