package com.usco.esteban.agenda_procesos.app.models.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;


public interface IProcesoUsuarioService {
	
	public Page<ProcesoUsuario> findAllById(Long id, Pageable pageable);
	//public Page<ProcesoUsuario> findAllByUsuario(Usuario usuario, Pageable pageable);
	
	public void save(ProcesoUsuario procesoUsuario);
	
	public void delete(Long id);
	
}
