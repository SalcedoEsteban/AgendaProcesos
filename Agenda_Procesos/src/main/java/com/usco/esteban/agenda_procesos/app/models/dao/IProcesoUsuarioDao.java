package com.usco.esteban.agenda_procesos.app.models.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.usco.esteban.agenda_procesos.app.models.entity.Juzgado;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;


public interface IProcesoUsuarioDao extends PagingAndSortingRepository<ProcesoUsuario, Long>
{

	
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = true and pu.proceso.juzgado=?2")
	Page<ProcesoUsuario> findAllById(Long id, Pageable pageable, Juzgado juzgado);
	
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = true and pu.proceso.radicado=?2")
	public Page<ProcesoUsuario> findByIdAndRadicado(Long id, Pageable pageable, String radicado);
	//@Query("select pu from ProcesoUsuario pu where pu.usuario = ?1")
	//Page<ProcesoUsuario> findAllByUsuario(Usuario usuario, Pageable pageable);
}
