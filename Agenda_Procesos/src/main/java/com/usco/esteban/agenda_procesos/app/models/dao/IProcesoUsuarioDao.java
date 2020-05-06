package com.usco.esteban.agenda_procesos.app.models.dao;


import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import com.usco.esteban.agenda_procesos.app.models.entity.ProcesoUsuario;


public interface IProcesoUsuarioDao extends PagingAndSortingRepository<ProcesoUsuario, Long>
{

	
	@Query("select pu from ProcesoUsuario pu join pu.usuario u where u.id=?1 and pu.proceso.estado = true")
	Page<ProcesoUsuario> findAllById(Long id, Pageable pageable);
	
	
	//@Query("select pu from ProcesoUsuario pu where pu.usuario = ?1")
	//Page<ProcesoUsuario> findAllByUsuario(Usuario usuario, Pageable pageable);
}
