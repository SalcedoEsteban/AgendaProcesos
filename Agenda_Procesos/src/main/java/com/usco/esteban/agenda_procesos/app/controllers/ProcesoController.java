package com.usco.esteban.agenda_procesos.app.controllers;



import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.usco.esteban.agenda_procesos.app.models.dao.IJuzgadoDao;
import com.usco.esteban.agenda_procesos.app.models.dao.IProcesoDao;
import com.usco.esteban.agenda_procesos.app.models.dao.ITipoProcesoDao;
import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.Proceso;
import com.usco.esteban.agenda_procesos.app.models.entity.TipoProceso;
import com.usco.esteban.agenda_procesos.app.models.service.IProcesoService;

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
	private ITipoProcesoDao tipoProcesoDao;
	
	@Autowired
	private IJuzgadoDao juzgadoDao;
	
	@GetMapping(value="/verDetalleProceso/{id}")
	public String verDetalleProceso(@PathVariable(name = "id") Long id, Map<String, Object> model)
	{
		Proceso proceso = procesoService.findOne(id);
		
		String radicado = proceso.getRadicado();
		
		model.put("proceso", proceso);
		model.put("titulo", "Detalle de proceso con el Radicado: " + radicado);
		
		return "detalleProceso";
	}
	
	
	@GetMapping(value = "/verTerminos/{id}")
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
	public String buscarProceso(@RequestParam(value="radicado") String radicado, Map<String, Object> model)
	{
		List<Proceso> proceso = procesoService.findByRadicado(radicado);
		
		model.put("procesos", proceso);
		
		return "listarProcesos";
	}
	
	@RequestMapping(value = "/listarProcesos", method = RequestMethod.GET)
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model)
	{
		Pageable pageRequest = PageRequest.of(page, 4);
		
		Page<Proceso> procesos = procesoService.findAll(pageRequest);
		
		model.addAttribute("titulo", "Listado de procesos");
		//model.addAttribute("procesos", procesoService.findAll());
		model.addAttribute("procesos", procesos);
		return "listarProcesos";
	}
	
	@RequestMapping(value ="/formProceso")
	public String crear(Map<String, Object> model)
	{
		/* esta es una primera fase en la que se muestra el formulario al ususario, se hace
		 * una instancia de un proceso y se envia a la vista*/
		
		Proceso proceso = new Proceso();
		
		model.put("proceso", proceso);
		model.put("tipoProcesos", tipoProcesoDao.findAll());
		model.put("juzgados", juzgadoDao.findAll());
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
		model.put("juzgados", juzgadoDao.findAll());
		
		return "formProceso";
	}
	
	@RequestMapping(value = "/guardarProceso")
	public String guardar(@Valid Proceso proceso, BindingResult result, Model model, RedirectAttributes flash, SessionStatus status)
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
		Long tProceso = Long.parseLong(proceso.gettProceso());
		
		/* luego de parseado se busca el objeto y se asigna*/
		TipoProceso tipoProceso = tipoProcesoDao.findOne(tProceso);
		
		/* y se establece la relacion guradando el tipo de proceso*/
		proceso.setTipoProceso(tipoProceso);
		
		
		/*obtenemos el id tipo string del juzgado */
		Long juz = Long.parseLong(proceso.getJuz());
		Juzgado juzgado = juzgadoDao.findOne(juz);
		
		proceso.setJuzgado(juzgado);
		
		
		procesoService.save(proceso);
		
		
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

