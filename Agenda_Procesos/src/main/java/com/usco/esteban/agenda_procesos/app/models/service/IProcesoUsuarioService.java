package com.usco.esteban.agenda_procesos.app.models.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;


public interface IProcesoUsuarioService {
	
	public Page<ProcesoUsuario> findAllById(Long id, Pageable pageable, Juzgado juzgado);
	//public Page<ProcesoUsuario> findAllByUsuario(Usuario usuario, Pageable pageable);
	
	public void save(ProcesoUsuario procesoUsuario);
	
	public void delete(Long id);
	
	public Page<ProcesoUsuario> findByIdAndRadicado(Long id, Pageable pageable, String radicado);
	
	public Page<ProcesoUsuario> findByUsuarioAndEstadoAndJuzgadoAndPrioritario(Long id, Pageable pageable, Juzgado juzgado);
	
	public Page<ProcesoUsuario> findAllBy(Long id, Pageable pageable, Juzgado juzgado);
	
	public List<ProcesoUsuario> findAll(Juzgado juzgado);
	
}
