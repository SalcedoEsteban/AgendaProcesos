package com.usco.esteban.agenda_procesos.app.controllers;

import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.usco.esteban.agenda_procesos.app.editors.JuzgadoPropertyEditor;
import com.usco.esteban.agenda_procesos.app.models.dao.IJuzgadoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Especialidad;
import com.usco.esteban.agenda_procesos.app.models.entity.HistorialUsuario;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Usuario;
import com.usco.esteban.agenda_procesos.app.models.service.IHistorialUsuarioService;
import com.usco.esteban.agenda_procesos.app.models.service.IJuzgadoService;
import com.usco.esteban.agenda_procesos.app.models.service.JpaUsuarioDetailsService;

@Controller
@SessionAttributes("usuario")
public class UsuarioController
{
	
	@Autowired
	private IJuzgadoService juzgadoService;
	
	@Autowired
	private JuzgadoPropertyEditor juzgadoEditor;
	
	@Autowired
	private JpaUsuarioDetailsService usuarioService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private IHistorialUsuarioService historialUsuarioService;
	
	@InitBinder
	public void initBinder(WebDataBinder binder)
	{
		binder.registerCustomEditor(Juzgado.class, "juzgado", juzgadoEditor);
	}
	
	@RequestMapping(value ="/listarUsuarios")
	public String listarUsuarios(Map<String, Object> model)
	{
		model.put("titulo", "Listado de Usuarios");
		model.put("usuarios", usuarioService.findAll());
		
		return "usuario/listarUsuarios";
	}
	
	@RequestMapping(value ="/formUsuario")
	public String crearUsuario(Map<String, Object> model)
	{
		Usuario usuario = new Usuario();
		
		model.put("usuario", usuario);
		model.put("juzgados", juzgadoService.findAll());
		model.put("titulo", "Crear Usuario");
		
		return "usuario/formUsuario";
	}
	
	@RequestMapping(value="/guardarUsuario")
	public String guardarUsuario(Model model, Usuario usuario, SessionStatus status)
	{
		
		String ps = usuario.getPassword();
		String bycryptPassword = passwordEncoder.encode(ps);
		usuario.setPassword(bycryptPassword);
		System.out.println("La contraseña encriptada es: ".concat(bycryptPassword));
		
		
		usuarioService.save(usuario);
		
		/*=======================================================================*/
		/* Bloque de codigo para crear HistorialUsuario */
		HistorialUsuario historialUsuario = new HistorialUsuario();
		String descripcion = 
				"Usuario creado el: " + usuario.getCreateAt() + 
				", pertenece a la especialidad " + usuario.getJuzgado().getEspecialidad().getNombre() +
				" y al juzgado: " + usuario.getJuzgado().getNombre();
		
		Date fechaIngreso = usuario.getCreateAt();
		Especialidad especialidad = usuario.getJuzgado().getEspecialidad();
		
		historialUsuario.setDescripcion(descripcion);
		historialUsuario.setFechaIngreso(fechaIngreso);
		historialUsuario.setEspecialidad(especialidad);
		
		
		
		historialUsuario.setUsuario(usuario);
		
		historialUsuarioService.save(historialUsuario);
		
		/*=======================================================================*/
		
		usuarioService.save(usuario);
		status.setComplete();
		
		
		return "redirect:/listarUsuarios";
	}
}
